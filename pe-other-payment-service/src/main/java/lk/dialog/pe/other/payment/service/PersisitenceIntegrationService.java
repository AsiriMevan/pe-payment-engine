package lk.dialog.pe.other.payment.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.PaymentPostRequest;

public interface PersisitenceIntegrationService {

	public Long nextPaymentSequence(String traceId) throws PEException, DCPEException;

	public void lodgeRBMPayment(PaymentPostRequest paymentPostRequest, long trxId, String traceId) throws PEException, DCPEException;

	public void lodgeOCSPayment(PaymentPostRequest paymentPostRequest, long trxId, String traceId) throws PEException, DCPEException;

	void lodgeDBNPayment(PaymentPostRequest payRequest, long payTrxID, String traceId) throws DCPEException;
}
