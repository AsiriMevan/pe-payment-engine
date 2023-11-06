package lk.dialog.pe.scheduler.core.handler;


import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.BaseResponse;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.scheduler.core.PaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.OCSTransaction;
import lk.dialog.pe.scheduler.dto.PaymentPostResponse;
import lk.dialog.pe.scheduler.service.MifeIntegrationService;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * This job will query payments pending at OCS_PAYMENT table and post to OCS
 */
@Slf4j
@Service
@Qualifier("ocsPaymentHandler")
public class OCSPaymentHandler extends PaymentHandler {

	@Autowired
	MifeIntegrationService mifeIntegrationService;

	@Override
	public void execute() {
		loggerInitTraceId();
		List<Payment> payments = loadPaymentData();
		payments.forEach(payment -> {
			String paymentString = SchUtil.convertToStringPretty(payment);
			log.info("OCSPaymentHandler starting execution of OCS payment submission contractId={}|payment={}",payment.getContractNo(),paymentString);
			try {
				PaymentPostResponse response = postPaymentData(payment);
				log.info("OCSPaymentHandler Payment post to OCS system completed successfully ocsPaymentSequence={}|postPaymentStatus={}|receiptNo={}",response.getResponseSeq(),response.getStatus(),payment.getReceiptNo());
				if (response.getStatus() == 1){
					peIntegrationService.updatePaymentPostSuccessWithPaySequenceAndErrDesc(HANDLER.OCS_PAY, COMMAND_READ.S,payment.getTrxID(),response.getResponseSeq(),response.getErrorDesc());
					peIntegrationService.deleteOcsPayment(payment.getTrxID());
				}else{
					peIntegrationService.updatePaymentPostFailureWithPaySequence(HANDLER.OCS_PAY,COMMAND_READ.F,payment.getTrxID(), -1, response.getErrorDesc());
					log.error("OCSPaymentHandler Payment post failed in OCS system. contractNo={}|error={}",payment.getContractNo(),response.getErrorDesc());
				}
			} catch (Exception exception) {
				String errorMessage= getFailMessageWithCause(exception);
				peIntegrationService.updatePaymentPostFailureWithPaySequence(HANDLER.OCS_PAY,COMMAND_READ.F,payment.getTrxID(), -1, errorMessage);
				log.error("OCSPaymentHandler Payment post failed in OCS system contractNo={}|error={}",payment.getContractNo(),exception.getMessage(),exception);
			}

		});
	}

	@Override
	public BaseResponse cancelPaymentData(Payment paymentDTO, boolean isCancelComplete) throws DCPEException {
		throw new DCPEException("payment cancelations handle by PaymentCancelHandler");
	}

	@Override
	public synchronized List<Payment> loadPaymentData() {
		return peIntegrationService.loadPaymentsForProcessing(HANDLER.OCS_PAY);
	}

	@Override
	public int validatePaymentData(Payment paymentDTO) throws DCPEException {
		throw new DCPEException("Please implement payment validations");
	}

	@Override
	public PaymentPostResponse postPaymentData(Payment paymentDTO, Object... args) throws Exception {
		int ocsTrxStatus = -1;
		PaymentPostResponse response = new PaymentPostResponse();
		if (paymentDTO.getAccountPaymentMny() <= 0) {
			response.setStatus(2);
			response.setErrorDesc("Payment Amount is <= 0");
			log.error("OCSPaymentHandler:postPayment  Failed due to Payment Amount is <= 0 contractId={}|connectionReference={}",paymentDTO.getContractNo(),paymentDTO.getConnRef());
			return response;
		}
		if (!(paymentDTO.getSbu() == SBUEnum.GSM.valueOf() || paymentDTO.getSbu() == SBUEnum.DBN.valueOf())) {
			response.setStatus(2);
			String errDesc= MessageFormat.format("SBU({0}) not support",SBUEnum.getSBU(paymentDTO.getSbu()).name());
			response.setErrorDesc(errDesc);
			log.error("OCSPaymentHandler:postPayment  Failed due to {} |contractId={}|connectionReference={}",errDesc,paymentDTO.getContractNo(),paymentDTO.getConnRef());
			return response;
		}
		OCSTransaction ocsTrx = new OCSTransaction();
		ocsTrx.setSerialNo("CCBS"+String.format("%015d", paymentDTO.getTrxID()));
		String connRef=paymentDTO.getConnRef();
		ocsTrx.setMsisdn(connRef.startsWith("0")?connRef.substring(1) : connRef);
		ocsTrx.setAction(paymentDTO.getTranType() == 2 ? "1" : "0");
		ocsTrx.setAmount(String.valueOf((long) (paymentDTO.getAccountPaymentMny() * Constants.OCS_AMOUNT_MULTIPLIER)));
		ocsTrx.setReasonCode(paymentDTO.getOcsReasonId());
		ocsTrx.setDescription(paymentDTO.getOcsReasonCode()+"-"+paymentDTO.getReceiptNo());
		try {
			String ocsResponse = mifeIntegrationService.submitOcsTransactionRequest(ocsTrx, String.valueOf(paymentDTO.getTrxID()));
			log.info("OCSPaymentHandler:postPayment OCS Response received contractNo={}|connectionReference={}|ocsResponse={}",paymentDTO.getContractNo(),paymentDTO.getConnRef(),ocsResponse);
			if (ocsResponse.contains("|")) {

				int statusPOS = ordinalIndexOf(ocsResponse, "|", 4, false);
				int endPOS = ordinalIndexOf(ocsResponse, "|", 5, false);
				int seqPOS = ordinalIndexOf(ocsResponse, "|", 1, false);
				int seqPOS2 = ordinalIndexOf(ocsResponse, "|", 2, false);
				ocsTrxStatus = Integer.parseInt(ocsResponse.substring(statusPOS + 1, endPOS));
				response.setResponseSeq(Integer.parseInt(ocsResponse.substring(seqPOS + 1, seqPOS2)));
				switch (ocsTrxStatus) {
					case 0:
						response.setStatus(1);
						response.setErrorDesc("OCS -> Success");
						break;
					case 1:
						response.setStatus(2);
						response.setErrorDesc("OCS -> Invalid external system ID");
						break;
					case 2:
						response.setStatus(2);
						response.setErrorDesc("OCS -> Invalid MSISDN");
						break;
					case 5:
						response.setStatus(2);
						response.setErrorDesc("OCS -> Invalid reason code");
						break;
					case 7:
						response.setStatus(2);
						response.setErrorDesc("OCS -> Adjustment passes the minimum threshold of the account(insufficient balance)");
						break;
					default:
						response.setStatus(2);
						response.setErrorDesc("OCS -> System error");
				}

			} else {
				response.setStatus(2);
				response.setErrorDesc(ocsResponse);
			}
			log.info("OCSPaymentHandler:postPayment  completed ocsTrxStatus={}|errorDesc={}|contractNo={}|connectionReference={}",ocsTrxStatus,response.getErrorDesc(),paymentDTO.getContractNo(),connRef);

		} catch (Exception ex) {
			throw new DCPEException("OCSPaymentHandler:postPayment Failed contractNo="+paymentDTO.getContractNo(),ex);
		}
		return response;
	}
	private static int ordinalIndexOf(CharSequence str, CharSequence searchStr, int ordinal, boolean lastIndex) {
		if (str == null || searchStr == null || ordinal <= 0) {
			return -1;
		}
		if (searchStr.length() == 0) {
			return lastIndex ? str.length() : 0;
		}
		int found = 0;
		int index = lastIndex ? str.length() : -1;
		do {
			if (lastIndex) {
				index = str.toString().lastIndexOf(searchStr.toString(),  index - 1);

			} else {
				index = str.toString().indexOf(searchStr.toString(), index + 1);

			}
			if (index < 0) {
				return index;
			}
			found++;
		} while (found < ordinal);
		return index;
	}
}
