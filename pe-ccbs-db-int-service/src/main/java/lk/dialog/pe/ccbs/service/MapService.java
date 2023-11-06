package lk.dialog.pe.ccbs.service;

import lk.dialog.pe.common.exception.DCPEException;

public interface MapService {

	public String findCposIdFromPaymodeMap(String id,Boolean isRbm,String traceId) throws DCPEException;
	
}