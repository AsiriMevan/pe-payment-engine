package lk.dialog.pe.scheduler.core.handler;


import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.BaseResponse;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.scheduler.core.PaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.dto.PaymentPostResponse;
import lk.dialog.pe.scheduler.exception.NEException;
import lk.dialog.pe.scheduler.service.ReconnectionService;
import lk.dialog.pe.scheduler.util.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * This job will query payments pending at FORCEFUL_REALIZE_CHEQUES table and
 * post to billing system
 */
@Slf4j
@Service
@Qualifier("chequeForcefulRealizeHandler")
public class ChequeForcefulRealizeHandler extends PaymentHandler {
	@Autowired
	ReconnectionService reConnectionHandler;



	protected boolean isRetry=false;

	@Override
	public void execute() {
		/* cheque cannot be realize at once in RBM. First need to suspend cheque in RBM for each contract payment.*/
		loggerInitTraceId();
		List<Payment> payments = loadPaymentData();
		payments.forEach(payment -> {
			loggerAppendTrxId(payment.getTrxID());
			String paymentString = SchUtil.convertToStringPretty(payment);
			log.info("ChequeRealizeHandler starting execution of Check ForceFul realization contractId={}|payment={} | receiptNo={}",payment.getContractNo(),paymentString,payment.getReceiptNo());
			try {
				postPaymentData(payment, Constants.SUSPEND_MODE);
				log.info("ChequeRealizeHandler Payment post to billing system success. trxId={} | receiptNo={}",payment.getTrxID(),payment.getReceiptNo());
				peIntegrationService.updatePaymentPostSuccess(HANDLER.CHEQUE_FORCEFUL_PAY, COMMAND_READ.P,payment.getTrxID());

			} catch (Exception e) {
				String errorDescription = getFailMessageWithCause(e);
				peIntegrationService.updatePaymentPostFailure(HANDLER.CHEQUE_FORCEFUL_PAY,COMMAND_READ.F,payment.getTrxID(),errorDescription);
				log.error("ChequeRealizeHandler Payment post failed in billing system. trxId={}|contractNo={}|connectionReference={}|chequeNo={}|error={}",payment.getTrxID(),payment.getContractNo(),payment.getConnRef(),payment.getChequeNo(),e.getMessage(),e);
			}
		});
		/* cheque suspend is done. Now need to realize cheque */
		postForcefulChqPaymentData();
	}

	@Override
	public synchronized List<Payment> loadPaymentData() {
		return  peIntegrationService.loadPaymentsForProcessing(HANDLER.CHEQUE_FORCEFUL_PAY);
	}
	//	------------logic blocks start------------------------
	private Payment mapToPayment(Payment chqPayment){
		Payment paymentDTO = new Payment();
		paymentDTO.setTrxID(chqPayment.getTrxID());
		paymentDTO.setAccountNo(chqPayment.getAccountNo());
		paymentDTO.setChequebankBranchCode(chqPayment.getChequebankBranchCode());
		paymentDTO.setChequebankCode(chqPayment.getChequebankCode());
		paymentDTO.setChequeNo(chqPayment.getChequeNo());
		paymentDTO.setContractNo(chqPayment.getContractNo());
		paymentDTO.setAccountPaymentMny(chqPayment.getAccountPaymentMny());
		if(!isRetry){
			paymentDTO.setReceiptNo(chqPayment.getReceiptNo());
			paymentDTO.setConnRef(chqPayment.getConnRef());
			paymentDTO.setContactNo(chqPayment.getContactNo());
			paymentDTO.setProductType(chqPayment.getProductType());
			paymentDTO.setSbu(chqPayment.getSbu());
			paymentDTO.setConnectionStatus(chqPayment.getConnectionStatus());
		}
		paymentDTO.setRealtime(chqPayment.isRealtime());
		return paymentDTO;
	}

	private void doRealizationForCustomerOnce(Payment fullChqPayment,String partialPayment) throws DCPEException, NEException {
		/* do realization for customer only once */
		log.info("doRealizationForCustomerOnce starting execution fullChqPaymentTrxId={}|realTime={}|productType={}|partialPayment={}",fullChqPayment.getTrxID(),fullChqPayment.isRealtime(),fullChqPayment.getProductType(),partialPayment);
		PaymentDTO paymentDto = new PaymentDTO();
		paymentDto.setChequeNo(fullChqPayment.getChequeNo());
		paymentDto.setChequebankCode(fullChqPayment.getChequebankBranchCode());
		paymentDto.setChequebankCode(fullChqPayment.getChequebankCode());
		paymentDto.setChequebankBranchCode(fullChqPayment.getChequebankBranchCode());
		paymentDto.setContractNo(fullChqPayment.getContractNo());

		String chequeStatus = null;
		if (!fullChqPayment.isRealtime()) {
			chequeStatus = soapIntegrationService.queryUnrealizedCheques(paymentDto);
		}
		log.info("doRealizationForCustomerOnce queryUnrealizedCheques completed trxId={}|chequeStatus={}|isRealTime={}",fullChqPayment.getTrxID(),chequeStatus,fullChqPayment.isRealtime());

		if ((chequeStatus != null && chequeStatus.equalsIgnoreCase(Constants.CHQ_MODE_SUSPEND)) || fullChqPayment.isRealtime()) {

			postPaymentData(fullChqPayment, Constants.CREDIT_MODE);
			log.info("Came to the reconnection phase. partialPayment={}|switchStatus={}", partialPayment, smsConfig.getSwitchStatus());
			if(smsConfig.getSwitchStatus().equalsIgnoreCase("Y")) {
				reConnectionHandler.reconnect(fullChqPayment.getContractNo(),fullChqPayment.getAccountPaymentMny());

			}
			peIntegrationService.updatePaymentPostSuccess(HANDLER.CHEQUE_FORCEFUL_PAY, COMMAND_READ.S,fullChqPayment.getTrxID());

			log.info("Came to the SMS phase. partialPayment={}|switchStatus={}", partialPayment, smsConfig.getSwitchStatus());
			if(!isRetry && smsConfig.getSwitchStatus().equalsIgnoreCase("Y") && fullChqPayment.getProductType() != 6 ) {
				smsService.sendSmsProxy(fullChqPayment, PAYMENT_SYSTEM.RBM);
			}
		} else {
			String paymentBody= SchUtil.convertToString(fullChqPayment);
			log.error("doRealizationForCustomerOnce: ForcefulRealization failed for fullChqPaymentTrxId={} due to invalid chequeStatus={}fullPaymentBody={}",fullChqPayment.getTrxID(), chequeStatus,paymentBody);
			String errorDescription="ForcefulRealization failed since chequeStatus:"+chequeStatus;
			peIntegrationService.updatePaymentPostCustom(HANDLER.CHEQUE_FORCEFUL_PAY, QUERY.SQL_UPDATE_FORCEFUL_CHQ,COMMAND_READ.F,fullChqPayment.getTrxID(),null,errorDescription);
		}

	}

	private boolean mapAndValidatePartialChequePaymentRealization(Payment partialPayment,List<Payment> chqPayments ,List<Payment>  mappedFullChqPayments){
		log.info("mapAndValidatePartialChequePaymentRealizationRequest: chqPayments(unprocessed fullChequePayments) rows count ={}|partialPayment={subscriberNodeId(accountNo)={},chequeNo={}}",chqPayments.size(),partialPayment.getAccountNo(),partialPayment.getChequeNo());
		String commonErr = null;
		boolean isFailExist = false;
		for (Payment chqPayment : chqPayments) {
			mappedFullChqPayments.add(mapToPayment(chqPayment));

			if (chqPayment.isRealtime() && !(chqPayment.getReadStatus().equals("P") && chqPayment.getErrCode().equals("1"))){
				isFailExist = true;
				commonErr = chqPayment.getErrDesc();
				String paymentString=SchUtil.convertToString(chqPayment);
				log.error("mapAndValidatePartialChequePaymentRealization detected a fail record (isRealTime={} && readStatus={} && errorCode={}) during full cheque realization in payment with : trxId={}|contractNo={}|accountNo={}|chequeNo={}|error={}|paymentFullBody={}",
						chqPayment.isRealtime(),chqPayment.getReadStatus(),chqPayment.getErrCode(),chqPayment.getTrxID(),chqPayment.getContractNo(),chqPayment.getAccountNo(),chqPayment.getChequeNo(),commonErr,paymentString);

			}
		}

		if(isFailExist){
			final String errorDescription = "Fail record exist in the batch. Reason ::"+commonErr;
			log.error("mapAndValidatePartialChequePaymentRealization is exiting for partialPayment={subscriberNodeId(accountNo)={},chequeNo={}} due to a Fail record in fullChqPayment batch size={}|error={}",partialPayment.getAccountNo(),partialPayment.getChequeNo(),mappedFullChqPayments.size(),errorDescription);
			mappedFullChqPayments.stream().forEach(innerPayment -> {
				try {
					peIntegrationService.updatePaymentPostCustom(HANDLER.CHEQUE_FORCEFUL_PAY, QUERY.SQL_UPDATE_FORCEFUL_CHQ,COMMAND_READ.W,innerPayment.getTrxID(),null,errorDescription);

				} catch (Exception ex) {
					String responseString =SchUtil.convertToString(innerPayment);
					log.error("mapAndValidatePartialChequePaymentRealization failed with exception during failed row update (fullCheckRealizePayment) to database trxId={}|contractNo={}|accountNo={}|chequeNo={}|fullBody={}|error={}",innerPayment.getTrxID(),innerPayment.getContractNo(),innerPayment.getAccountNo(),innerPayment.getChequeNo(),responseString,ex.getMessage(),ex);
				}
			});

			return false;
		}
		log.error("mapAndValidatePartialChequePaymentRealization is success for partialPayment={subscriberNodeId(accountNo)={},chequeNo={}} for fullChqPayment batch size={}",partialPayment.getAccountNo(),partialPayment.getChequeNo(),mappedFullChqPayments.size());

		return  true;
	}

	private boolean executeMappedFullChequePaymentRealizations(int index,Payment fullChequePayment,Payment partialPayment) throws DCPEException {
		boolean success = true;
		String partialPaymentBody="{subscriberNodeId(accountNo)="+partialPayment.getAccountNo()+",chequeNo="+partialPayment.getChequeNo()+"}";
		try {
			if (index == 0) {
				/* do realization for customer only once */
				log.info("postForcefulChqPaymentData:executeMappedFullChequePaymentRealizations do realization for customer only once. partialPayment={}|fullChequePaymentTrxId={}",partialPaymentBody,fullChequePayment.getTrxID());
				doRealizationForCustomerOnce(fullChequePayment,partialPaymentBody);

			} else {
				/*call reconnection logic for each contract payment*/
				log.info("postForcefulChqPaymentData:executeMappedFullChequePaymentRealizations in reconnection phase. partialPayment={}|fullChequePaymentTrxId={}|switchStatus={}", partialPaymentBody,fullChequePayment.getTrxID(), smsConfig.getSwitchStatus());
				if(smsConfig.getSwitchStatus().equalsIgnoreCase("Y")) {
					reConnectionHandler.reconnect(fullChequePayment.getContractNo(),fullChequePayment.getAccountPaymentMny());
				}
				peIntegrationService.updatePaymentPostSuccess(HANDLER.CHEQUE_FORCEFUL_PAY, COMMAND_READ.S,fullChequePayment.getTrxID());
				log.info("postForcefulChqPaymentData:executeMappedFullChequePaymentRealizations completed successfully. partialPayment={}|fullChequePaymentTrxId={}|switchStatus={}", partialPaymentBody,fullChequePayment.getTrxID(), smsConfig.getSwitchStatus());

			}
		} catch (NEException ex) {
			String groupPaymentString = SchUtil.convertToString(fullChequePayment);
			log.error("postForcefulChqPaymentData:executeMappedFullChequePaymentRealizations ForcefulRealization failed in billing system (full check payment). partialPayment={}|fullChqPaymentTrxId={}|fullChqPaymentBody={}|error={}" ,partialPaymentBody,fullChequePayment.getTrxID(),groupPaymentString, ex.getMessage(),ex);
			peIntegrationService.updatePaymentPostFailure(HANDLER.CHEQUE_FORCEFUL_PAY,COMMAND_READ.N,fullChequePayment.getTrxID(),"ForcefulRealization failed due to:"+ex.getMessage());
			success = false;
		} catch (Exception ex) {
			String fullChequePaymentString = SchUtil.convertToString(fullChequePayment);
			String errorMsg = "executeMappedFullChequePaymentRealizations: Failed for fullChqPaymentTrxId={0}|error={1}:{2}";
			errorMsg = MessageFormat.format(errorMsg,String.valueOf(fullChequePayment.getTrxID()),ex.getMessage(),ex.getCause());
			throw new DCPEException(errorMsg,ex);
		}
		return success;
	}
//-------------------logic blocks end-----------------------
	/**
	 * realize cheque forcefully in billing system (only once for customer) call
	 * reconnection logic delete payment from forceful cheque realization table
	 */
	protected synchronized void postForcefulChqPaymentData() {
		log.info("postForcefulChqPaymentData started execution");
		List<Payment> partialChqRealizePayments = peIntegrationService.getPartialChqRealizePayments();
		log.info("postForcefulChqPaymentData received partialPayments count = {}",partialChqRealizePayments.size());
		for (Payment partialPayment : partialChqRealizePayments) {
			String partialPaymentBody="{subscriberNodeId(accountNo)="+partialPayment.getAccountNo()+",chequeNo="+partialPayment.getChequeNo()+"}";
			log.info("postForcefulChqPaymentData starting posting of partial cheque realization partialPayment={}",partialPaymentBody);
			List<Payment> mappedFullChqPayments = new ArrayList<>();
			List<Payment> chqPayments = peIntegrationService.getFullCheckRealizePayments(partialPayment.getAccountNo(),partialPayment.getChequeNo());
			if ( !mapAndValidatePartialChequePaymentRealization(partialPayment,chqPayments,mappedFullChqPayments) ) continue;

			try {
				for (int i = 0; i < mappedFullChqPayments.size(); i++) {
					Payment fullChequePayment = mappedFullChqPayments.get(i);
					loggerAppendTrxId(fullChequePayment.getTrxID());
					String groupChqPaymentStr = SchUtil.convertToStringPretty(fullChequePayment);
					log.info("postForcefulChqPaymentData starting realization. partialPayment={{}}|fullChequePaymentTrxId={}|fullChequePaymentBody={}", partialPaymentBody,fullChequePayment.getTrxID(),groupChqPaymentStr);
					if ( !executeMappedFullChequePaymentRealizations(i,fullChequePayment,partialPayment) )break;

				}
			} catch (Exception ex) {
				log.error("postForcefulChqPaymentData Cheque forceful realization or reconnection  partialPayment={{}}|error={}",partialPaymentBody,ex.getMessage(),ex);
				mappedFullChqPayments.stream().forEach(innerPayment -> {
					MDC.put("traceId",traceId+"-"+partialPayment.getTrxID()+"-"+innerPayment.getTrxID());
					try {
						String err = "Req_ID:"+innerPayment.getTrxID()+" Payment not posted to relevant billing system yet to realize, Forceful Cheque Update Error. Cause:"+ex.getMessage();
						peIntegrationService.updatePaymentPostCustom(HANDLER.CHEQUE_FORCEFUL_PAY, QUERY.SQL_UPDATE_FORCEFUL_CHQ,COMMAND_READ.W,innerPayment.getTrxID(),null,err);
					} catch (Exception e) {
						log.error("postForcefulChqPaymentData failed with exception during  Cheque forceful realization or reconnection phase, failed row update (fullCheckRealizePayment) to database partialPayment={{}}|trxId={}|contractNo={}|accountNo={}|chequeNo={}|error={}",partialPaymentBody,innerPayment.getTrxID(),innerPayment.getContractNo(),innerPayment.getAccountNo(),innerPayment.getChequeNo(),ex.getMessage(),ex);
					}
				});
			}
		}
		log.info("postForcefulChqPaymentData completed execution ");
	}

	@Override
	public int validatePaymentData(Payment paymentDTO) throws DCPEException {
		throw new DCPEException("Please implement payment validations");
	}

	@Override
	public PaymentPostResponse postPaymentData(Payment paymentDAO,Object... args) throws NEException,DCPEException {
		PaymentDTO paymentDTO = new PaymentDTO();
		BeanUtils.copyProperties(paymentDAO, paymentDTO);
		String paymentString = SchUtil.convertToStringPretty(paymentDTO);
		Integer soapResponse;
		String mode;
		if ((int) args[0] == Constants.SUSPEND_MODE) {
			mode="SUSPEND";
			log.info("postPaymentDataRequest postChqInSuspendState mode={}|trxId={}|contractNo={}|paymentDto={}",mode,paymentDTO.getTrxID(),paymentDTO.getContractNo(),paymentString);
			soapResponse = soapIntegrationService.postChqInSuspendState(paymentDTO, paymentDTO.getTrxID());
		}else {
			mode="CREDIT";
			log.info("postPaymentDataRequest doRealizeChqForcefully mode={}|trxId={}|contractNo={}|paymentDto={}",mode,paymentDTO.getTrxID(),paymentDTO.getContractNo(),paymentString);
			soapResponse = soapIntegrationService.doRealizeChqForcefully(paymentDTO);
		}
		log.info("postPaymentDataResponse success soapResponse={}|mode={}|trxId={}|contractNo={}",soapResponse,mode,paymentDTO.getTrxID(),paymentDTO.getContractNo());
		return null;
	}

	@Override
	public BaseResponse cancelPaymentData(Payment paymentDTO, boolean isCancelComplete) throws DCPEException {
		throw new DCPEException("payment cancelations handle by PaymentCancelHandler");
	}

}
