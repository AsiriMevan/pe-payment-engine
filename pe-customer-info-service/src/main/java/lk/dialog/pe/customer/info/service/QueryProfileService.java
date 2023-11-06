package lk.dialog.pe.customer.info.service;

import lk.dialog.pe.common.domain.CRMProfileRequest;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.customer.info.util.CRMProfileResponse;

public interface QueryProfileService {

	public CRMProfileResponse queryProfile(CRMProfileRequest crmProfileRequest, String traceId) throws DCPEException;

}
