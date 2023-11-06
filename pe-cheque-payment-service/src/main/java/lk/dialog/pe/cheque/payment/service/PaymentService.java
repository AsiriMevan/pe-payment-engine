package lk.dialog.pe.cheque.payment.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.ChequeRealizeRequest;
import lk.dialog.pe.common.util.ChequeRealizeResponse;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.PaymentPostResponse;

public interface PaymentService {
	
	public PaymentPostResponse postPayment(PaymentPostRequest jsonReq,String traceId) throws PEException, DCPEException;
	
	public ChequeRealizeResponse forcefulChequeRealize(ChequeRealizeRequest jsonReq, String traceId) throws PEException, DCPEException;

}
