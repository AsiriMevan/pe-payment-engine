package lk.dialog.pe.billing.service;

import lk.dialog.pe.billing.domain.DcsProfile;
import lk.dialog.pe.billing.domain.Profile;
import lk.dialog.pe.billing.util.HotlineRemarkRequest;
import lk.dialog.pe.billing.util.RemarkInfo;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;

import java.util.List;

public interface PersistanceIntegrationService {

	public String getMobileOfContractId(String contractId, String traceId) throws DCPEException;

	public String getContractIdOfMobile(String mobileNo, String traceId) throws DCPEException;
	
	public List<RemarkInfo> getHotlineRemarks(HotlineRemarkRequest jsonReq,String traceId)throws DCPEException;
	
	public List<RemarkInfo> getCcbsWifiRemarks(HotlineRemarkRequest jsonReq,String traceId)throws DCPEException;

	public List<Profile> getWifiContractIdOrMobile(String mobile, String contractID, String traceId) throws DCPEException;

	FixedNumberDTO validateCcbsAvailablityForContract(String ContractNo, String traceId);

	FixedNumberDTO validateCcbsAvailablityForConnRef(String mobileNo, String traceId);

	List<DcsProfile> getDcsMobileOfContractId(String contractID, String traceId) throws DCPEException;

	List<RemarkInfo> getDcsRemarks(List<String> volteContractList, String traceId) throws DCPEException;
}
