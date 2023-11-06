package lk.dialog.pe.billing.service;

import lk.dialog.pe.billing.domain.PEProfileRequest;
import lk.dialog.pe.billing.domain.PEProfileResponse;

public interface QueryPEProfileService {

	public PEProfileResponse queryPEProfile(PEProfileRequest jsonReq, String traceId) throws Exception;

}
