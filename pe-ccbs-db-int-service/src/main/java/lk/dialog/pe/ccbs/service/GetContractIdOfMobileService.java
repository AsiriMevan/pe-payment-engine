package lk.dialog.pe.ccbs.service;

import lk.dialog.pe.common.exception.DCPEException;

public interface GetContractIdOfMobileService {

	public String getContractIdOfMobile (String mobileNo, String traceId) throws DCPEException;
	
}
