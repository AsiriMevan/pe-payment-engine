package lk.dialog.pe.other.payment.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.PaymentPostResponse;

public interface PaymentService {
	
	public PaymentPostResponse postPayment(PaymentPostRequest jsonReq,String traceId) throws PEException,DCPEException;
}
