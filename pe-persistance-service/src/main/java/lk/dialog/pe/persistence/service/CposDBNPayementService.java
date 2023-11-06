package lk.dialog.pe.persistence.service;


import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.persistence.domain.DBNPayment;

public interface CposDBNPayementService {
	
	Object saveDbnPayment(DBNPayment rbmPayment, String traceIdString) throws DCPEException ;
}
