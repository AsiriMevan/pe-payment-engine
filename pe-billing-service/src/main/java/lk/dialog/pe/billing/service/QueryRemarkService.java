package lk.dialog.pe.billing.service;

import lk.dialog.pe.billing.util.HotlineRemarkRequest;
import lk.dialog.pe.billing.util.HotlineRemarkResponse;

public interface QueryRemarkService {
	
	public HotlineRemarkResponse getHotlineRemarks(HotlineRemarkRequest jsonReq, String traceId);

}
