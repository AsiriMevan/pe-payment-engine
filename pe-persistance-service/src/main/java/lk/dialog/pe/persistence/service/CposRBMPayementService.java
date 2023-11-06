package lk.dialog.pe.persistence.service;


import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.persistence.domain.RBMPayment;

public interface CposRBMPayementService {
	
	public Object saveRBMpayment(RBMPayment rbmPayment, String traceIdString) throws DCPEException ;	
}
