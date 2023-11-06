package lk.dialog.pe.ccbs.service;

import java.util.List;

import lk.dialog.pe.ccbs.dto.Profile;
import lk.dialog.pe.common.exception.DCPEException;

public interface WifiContractByMobileService {
 	
	public List<Profile> getWifiContractIdOrMobile(String mobile, Integer contractId, String traceId) throws DCPEException;

}
