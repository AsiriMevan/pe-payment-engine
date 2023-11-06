package lk.dialog.pe.ccbs.service;

import java.util.List;

import lk.dialog.pe.ccbs.dto.DcsRemarkRequest;
import lk.dialog.pe.ccbs.dto.HotlineRemarkRequest;
import lk.dialog.pe.ccbs.dto.RemarkInfo;
import lk.dialog.pe.common.exception.DCPEException;

public interface GetHotLineRemarksService {
	
	public List<RemarkInfo> getHotLineRemarks(HotlineRemarkRequest jsonReq,String traceId) throws DCPEException;

    List<RemarkInfo> getDcsRemarks(DcsRemarkRequest dcsRemarkRequest,
                                   String traceId) throws DCPEException;
}
