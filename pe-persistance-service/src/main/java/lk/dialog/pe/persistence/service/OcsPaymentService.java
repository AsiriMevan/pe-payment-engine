package lk.dialog.pe.persistence.service;


import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.persistence.domain.OcsPayment;

public interface OcsPaymentService {

	public Object saveOcsPayment(OcsPayment ocspayment, String traceId) throws DCPEException;
	
}
