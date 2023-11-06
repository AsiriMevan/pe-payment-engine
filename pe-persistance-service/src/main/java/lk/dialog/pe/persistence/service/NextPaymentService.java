package lk.dialog.pe.persistence.service;

import lk.dialog.pe.common.exception.DCPEException;

public interface NextPaymentService {

	public Long nextPaymentSequence(String traceId) throws DCPEException;

}
