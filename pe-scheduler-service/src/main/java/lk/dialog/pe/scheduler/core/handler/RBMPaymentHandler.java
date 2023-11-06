package lk.dialog.pe.scheduler.core.handler;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.BaseResponse;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.PAY_MODE;
import lk.dialog.pe.common.util.PRODUCT_TYPE;
import lk.dialog.pe.scheduler.core.PaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.dto.PaymentPostResponse;
import lk.dialog.pe.scheduler.exception.NEException;
import lk.dialog.pe.scheduler.service.ReconnectionService;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.PAYMENT_SYSTEM;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Slf4j
@Service
@Qualifier("rbmPaymentHandler")
public class RBMPaymentHandler extends PaymentHandler {
	@Autowired
	protected ReconnectionService reConnectionHandler;

	protected boolean isRetryProcess = false;

	@Override
	public void execute() {
		loggerInitTraceId();
		List<Payment> payments = loadPaymentData();
		payments.stream().forEach(payment -> {
			String paymentString = SchUtil.convertToStringPretty(payment);
			log.info("RBMPaymentHandler starting execution of RBM payment submission contractId={}|payment={}",payment.getContractNo(),paymentString);
			loggerAppendTrxId(payment.getTrxID());
			try {
				PaymentPostResponse response = postPaymentData(payment);
				peIntegrationService.updatePaymentPostSuccessWithPaySequence(HANDLER.RBM_PAY, COMMAND_READ.S,payment.getTrxID(),response.getResponseSeq());
				log.info("RBMPaymentHandler in reconnection and SMS phase switchStatus={}|productType={}|responseSequence={}| receiptNo={}", smsConfig.getSwitchStatus(),payment.getProductType(),response.getResponseSeq(),payment.getReceiptNo());
				if(Boolean.TRUE.equals(smsConfig.isSwitchActive())){
					executeRbmLogic(response.isPaymentSequenceValid(),payment);
				}
			} catch (NEException neException) {
				String errorMessage= getFailMessage(neException);
				log.error("RBMPaymentHandler Payment post failed in billing system. error={}",neException.getMessage(), neException);
				peIntegrationService.updatePaymentPostFailureWithPaySequence(HANDLER.RBM_PAY, COMMAND_READ.N,payment.getTrxID(),-1,errorMessage);
			} catch (Exception exception) {
				String errorMessage= getFailMessageWithCause(exception);
				log.error("RBMPaymentHandler Payment post failed in billing system error={}" ,exception.getMessage(),exception);
				peIntegrationService.updatePaymentPostFailureWithPaySequence(HANDLER.RBM_PAY, COMMAND_READ.F,payment.getTrxID(),-1,errorMessage);
			}

		});
		loggerClearTraceId();
	}

	@Override
	public synchronized List<Payment> loadPaymentData() {
		return peIntegrationService.loadPaymentsForProcessing(HANDLER.RBM_PAY);
	}

	@Override
	public int validatePaymentData(Payment paymentDTO) throws DCPEException {
		throw new DCPEException("Please implement payment validations");
	}

	@Override
	public PaymentPostResponse postPaymentData(Payment paymentDAO, Object... args) throws SQLException, NEException,DCPEException {
		PaymentDTO paymentDTO = new PaymentDTO(paymentDAO);
		paymentDTO.setPaymentMode(peIntegrationService.getPayMode(paymentDAO.getPaymentMode()).getRbmId());
		log.info("receivedMappedPayModeResponse inputPayMode={}|mappedPayMode={}",paymentDAO.getPaymentMode(),paymentDTO.getPaymentMode());
		int paymentSeq=-1;
		if (Constants.PAY_MODE_CHQ.equalsIgnoreCase(paymentDAO.getPaymentMode())) {
			paymentSeq = soapIntegrationService.postChqInSuspendState(paymentDTO,paymentDTO.getTrxID());
		} else {
			paymentSeq = soapIntegrationService.postPayment(paymentDTO,paymentDTO.getTrxID());
		}
		log.info("RBMPaymentHandler Payment post to billing system success paymentSequence={}", paymentSeq);
		return new PaymentPostResponse(paymentSeq);
	}

	protected void executeRbmLogic( boolean isPaymentSequenceValid ,Payment payment){
		PRODUCT_TYPE productType = PRODUCT_TYPE.getProductTypeByValue(payment.getProductType());
		log.info("RBMPaymentHandler:executeRbmLogic productType={}|isRetryProcess={}|payMode={}",productType,isRetryProcess,payment.getPaymentMode());

		if (isPaymentSequenceValid && productType.orEqual(PRODUCT_TYPE.OTHER,PRODUCT_TYPE.VOLTE,PRODUCT_TYPE.DCS)) {
			log.info("RBMPaymentHandler reconnecting line");
			reConnectionHandler.reconnect(payment.getContractNo(), payment.getAccountPaymentMny());

			if (productType != PRODUCT_TYPE.DCS && !PAY_MODE.CHQ.equals(payment.getPaymentMode())) {
				smsService.sendSmsProxy(payment,PAYMENT_SYSTEM.RBM);
			}
		} else if ( !isRetryProcess && !isPaymentSequenceValid && (PRODUCT_TYPE.OTHER.equals(payment.getProductType()) || PRODUCT_TYPE.VOLTE.equals(payment.getProductType())) && !PAY_MODE.CHQ.equals(payment.getPaymentMode())) {
			smsService.sendSmsProxy(payment,  PAYMENT_SYSTEM.RBM_CHQ);
		}
	}

	@Override
	public BaseResponse cancelPaymentData(Payment paymentDTO, boolean isCancelComplete) throws DCPEException {
		throw new DCPEException("payment cancellations handle by PaymentCancelHandler");
	}
}
