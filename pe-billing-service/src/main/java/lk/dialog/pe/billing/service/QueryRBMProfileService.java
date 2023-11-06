package lk.dialog.pe.billing.service;

import lk.dialog.pe.billing.domain.BillingProfileRequest;
import lk.dialog.pe.billing.domain.BillingProfileResponse;
import lk.dialog.pe.common.exception.DCPEException;

public interface QueryRBMProfileService {

	public BillingProfileResponse queryRBMProfile(BillingProfileRequest billingProfileRequest, String traceId) throws DCPEException;

}
