package lk.dialog.pe.retrieval.cancelation.service;

import lk.dialog.pe.common.dto.QueryPaymentRequest;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.retrieval.cancelation.domain.QueryPendingPaymentResponse;

public interface QueryPendingPaymentService {
	
	public QueryPendingPaymentResponse queryPendingPayment(QueryPaymentRequest payRequest,String traceId) throws  DCPEException;
}

