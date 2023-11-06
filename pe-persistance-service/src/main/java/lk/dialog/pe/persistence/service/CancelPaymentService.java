package lk.dialog.pe.persistence.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.persistence.domain.CancelPayment;

public interface CancelPaymentService {

	public Object saveCancelPayment(CancelPayment cancelPayment, String traceId) throws DCPEException;

}
