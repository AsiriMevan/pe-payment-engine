package lk.dialog.pe.scheduler.core.handler;


import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.BaseResponse;
import lk.dialog.pe.scheduler.core.PaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentPostResponse;
import lk.dialog.pe.scheduler.service.SmsService;
import lk.dialog.pe.scheduler.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This job will query payments pending at DBN_PAYMENT table and post to Telbiz
 * billing system
 */
@Slf4j
@Service
@Qualifier("dbnPaymentHandler")
public class DBNPaymentHandler extends PaymentHandler {

	@Autowired
	SmsService smsService;

	@Override
	public void execute() {
		List<Payment> payments = loadPaymentData();
		payments.forEach(payment -> {
			String paymentString = SchUtil.convertToStringPretty(payment);
			log.info("DBNPaymentHandler starting execution of DBN payment submission contractId={}|payment={} | receiptNo={}",payment.getContractNo(),paymentString,payment.getReceiptNo());
			try {
				PaymentPostResponse response = postPaymentData(payment);
				if (response.getStatus() == 1) { //this is never triggered since postPayment is not executed
					peIntegrationService.updatePaymentPostSuccessWithPaySequenceAndErrDesc(HANDLER.DBN_PAY, COMMAND_READ.S,payment.getTrxID(),response.getResponseSeq(),response.getErrorDesc());
					log.info("DBNPaymentHandler Came to the SMS phase. switch status={}", payment.getTrxID(), smsConfig.getSwitchStatus());
					if(smsConfig.getSwitchStatus().equalsIgnoreCase("Y")) {
						smsService.sendSmsProxy(payment, PAYMENT_SYSTEM.TELBIZ);
					}
				}
				else{
					peIntegrationService.updatePaymentPostCustom(HANDLER.DBN_PAY,QUERY.SQL_UPDATE_DBN_PAYMENT_RES,COMMAND_READ.F,payment.getTrxID(),response.getResponseSeq(),response.getErrorDesc());
				}
			} catch (Exception exception) {
				String errorMessage= getFailMessageWithCause(exception);
				peIntegrationService.updatePaymentPostFailureWithPaySequence(HANDLER.DBN_PAY, COMMAND_READ.F,payment.getTrxID(),-1,errorMessage);
				log.error("DBNPaymentHandler:Execute Payment post failed in billing system contractId={}|error={}" + payment.getContractNo(),errorMessage,exception);
			}

		});
	}

	@Override
	public synchronized List<Payment> loadPaymentData() {
		return peIntegrationService.loadPaymentsForProcessing(HANDLER.DBN_PAY);
	}

	@Override
	public int validatePaymentData(Payment paymentDTO) throws DCPEException {
		throw new DCPEException("Please implement payment validations");
	}

	@Override
	public PaymentPostResponse postPaymentData(Payment paymentDTO, Object... args) {
		return new PaymentPostResponse();
	}

	@Override
	public BaseResponse cancelPaymentData(Payment paymentDTO, boolean isCancelComplete) throws DCPEException {
		throw new DCPEException("payment cancelations handle by PaymentCancelHandler");
	}

}
