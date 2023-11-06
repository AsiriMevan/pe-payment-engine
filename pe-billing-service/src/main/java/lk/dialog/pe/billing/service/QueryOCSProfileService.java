package lk.dialog.pe.billing.service;

import lk.dialog.pe.billing.domain.OCSProfileRequest;
import lk.dialog.pe.billing.util.OCSProfileResponse;
import lk.dialog.pe.common.exception.DCPEException;

public interface QueryOCSProfileService {

	public OCSProfileResponse queryOCSProfile(OCSProfileRequest jsonReq, String traceId) throws DCPEException;
}
