package lk.dialog.pe.ccbs.service;

import java.util.List;

import lk.dialog.pe.ccbs.dto.HotlineRemarkRequest;
import lk.dialog.pe.ccbs.dto.RemarkInfo;
import lk.dialog.pe.common.exception.DCPEException;

public interface GetCcbsWifiRemarksService {

	public List<RemarkInfo> getCcbsWifiRemarks(HotlineRemarkRequest jsonReq,String traceId)throws DCPEException;
}
