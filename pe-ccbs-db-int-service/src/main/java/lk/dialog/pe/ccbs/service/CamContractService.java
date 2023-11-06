package lk.dialog.pe.ccbs.service;

import lk.dialog.pe.ccbs.dto.FixedNumberDTO;
import lk.dialog.pe.common.exception.DCPEException;

public interface CamContractService {
	public String findContractSubsidiaryTypeById(String contractId, boolean dcsOnly, String traceId) throws DCPEException;
	public FixedNumberDTO findSystemCcbsByContractId(String contractId, String traceId) throws DCPEException;
	public FixedNumberDTO findSystemCcbsByMobileNo(String mobileNo, String traceId) throws DCPEException;

}
