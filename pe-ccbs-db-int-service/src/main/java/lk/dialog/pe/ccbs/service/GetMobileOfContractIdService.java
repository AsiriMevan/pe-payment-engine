package lk.dialog.pe.ccbs.service;

import lk.dialog.pe.common.exception.DCPEException;

public interface GetMobileOfContractIdService {

	public String getMobileOfContractId(String contractNo, String traceId)throws DCPEException;
}
