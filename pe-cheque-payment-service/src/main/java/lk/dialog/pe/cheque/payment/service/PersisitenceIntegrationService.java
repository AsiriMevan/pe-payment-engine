package lk.dialog.pe.cheque.payment.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.PaymentPostRequest;

public interface PersisitenceIntegrationService {

	public Long nextPaymentSequence(String traceId) throws PEException, DCPEException;

	public void lodgeChqForecefulRealize(PaymentPostRequest paymentPostRequest, long trxId, String traceId,boolean isRealTime) throws DCPEException;
		
	public void lodgeOCSPayment(PaymentPostRequest payRequest, long trxId, String traceId) throws DCPEException;
	
	public void lodgeRBMPayment(PaymentPostRequest paymentPostRequest, long trxId, String traceId) throws DCPEException;

	void lodgeDBNPayment(PaymentPostRequest payRequest, long payTrxID, String traceId) throws DCPEException;
}
