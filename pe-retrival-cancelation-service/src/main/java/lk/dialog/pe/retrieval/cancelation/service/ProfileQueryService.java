package lk.dialog.pe.retrieval.cancelation.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.retrieval.cancelation.util.ProfileQueryRequest;
import lk.dialog.pe.retrieval.cancelation.util.QueryPaymentResponse;

public interface ProfileQueryService {
	
	public QueryPaymentResponse queryPayments(ProfileQueryRequest profileQueryRequest,String traceID) throws DCPEException;

}
