package lk.dialog.pe.ccbs.service;

import lk.dialog.pe.common.exception.DCPEException;

public interface PaymodeMapService {
	
	public Object getPayModeMap(String traceId,String cposId) throws DCPEException;
}
