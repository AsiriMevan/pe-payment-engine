package lk.dialog.pe.retrieval.cancelation.service;

import lk.dialog.pe.common.dto.QueryPaymentRequest;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.CancelPaymentRequest;
import lk.dialog.pe.common.util.CancelPaymentResponse;
import lk.dialog.pe.retrieval.cancelation.domain.QueryPendingPaymentResponse;
import lk.dialog.pe.retrieval.cancelation.exception.QuerySystemException;

public interface PaymentService {

	public CancelPaymentResponse cancelPayment(CancelPaymentRequest cancelPaymentRequest, String traceId) throws DCPEException,QuerySystemException;

	public QueryPendingPaymentResponse queryPendingPayment(QueryPaymentRequest jsonReq, String traceId) throws DCPEException;

}
