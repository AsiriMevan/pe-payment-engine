package lk.dialog.pe.ccbs.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import lk.dialog.pe.ccbs.dto.FixedNumberDTO;
import lk.dialog.pe.common.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.CamContractService;
import lk.dialog.pe.ccbs.util.CRMSystemsEnum;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.ccbs.util.SQLQueryEnum;
import lk.dialog.pe.common.exception.DCPEException;

@Service
public class CamContractServiceImpl implements CamContractService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CamContractServiceImpl.class);
	
	@Autowired
	private QueryExecuterRepository queryExecuterRepository;
    
	@Autowired
	@Qualifier("queryMap")
	private  Map<String, String>  map;
	
	@Override
	public String findContractSubsidiaryTypeById(String contractId, boolean dcsOnly, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("findContractSubsidiaryTypeByIdRequest : traceId={}",traceId);
		
		String contractSubsidiaryType = null; 
		
		if(dcsOnly) {
			contractSubsidiaryType = getContractsSubsidiaryTypeAsDCS(contractId, traceId);
		}else {
			contractSubsidiaryType = findUnifiedContractsSubsidiaryTypeById(contractId, traceId);
		}
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("findContractSubsidiaryTypeByIdResponse : traceId={}|timeTaken={}",traceId,timeTaken);
		return contractSubsidiaryType;
	}

	@Override
	public FixedNumberDTO findSystemCcbsByContractId(String contractId, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("findSystemCcbsByContractIdRequest :contractId=[{}]|traceId=[{}]" , contractId, traceId);
		FixedNumberDTO response;
		List<Map<String, Object>> rows;
		try {
			rows =	queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_CCBS_SYSTEM_QRY_CONTRACT.getValue()), new Object[] { contractId }, traceId);
			response = processSysRows(rows);
		} catch (DataAccessException e) {
			LOGGER.error("findSystemCcbsByContractIdRequest error occurred : traceId=[{}]|errorMessage=[{}]", traceId, e.getMessage(), e);
			return null;
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);

		String requestString = DCPEUtil.convertToString(rows);
		LOGGER.info("findSystemCcbsByContractIdResponse :timeTaken=[{}]|traceId=[{}]|getResult=[{}]" , timeTaken, traceId, requestString);
		return response;
	}

	@Override
	public FixedNumberDTO findSystemCcbsByMobileNo(String mobileNo, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("findSystemCcbsByMobileNoRequest :mobileNo=[{}]|traceId=[{}]" , mobileNo, traceId);
		FixedNumberDTO response;
		List<Map<String, Object>> rows;
		try {
			rows =	queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_CCBS_SYSTEM_QRY_CONREF.getValue()), new Object[] { mobileNo, mobileNo }, traceId);
			response = processSysRows(rows);
		} catch (DataAccessException e) {
			LOGGER.error("findSystemCcbsByMobileNoRequest error occurred : traceId=[{}]|errorMessage=[{}]", traceId, e.getMessage(), e);
			return null;
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);

		String requestString = DCPEUtil.convertToString(rows);
		LOGGER.info("findSystemCcbsByMobileNoResponse :timeTaken=[{}]|traceId=[{}]|getResult=[{}]" , timeTaken, traceId, requestString);
		return response;
	}

	private String getContractsSubsidiaryTypeAsDCS(String contractId, String traceId) throws DCPEException{
		Instant start = Instant.now();
		LOGGER.info("getContractSubsidiaryTypeAsDCSRequest :contractId={}|traceId={}" , contractId, traceId);
		String response = null;	
		try {
			response =	(String) queryExecuterRepository.getSingleObject(map.get(SQLQueryEnum.SQL_DCS_SBU_BY_CONTRACT_ID.getValue()), new Object[] { contractId }, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("getContractSubsidiaryTypeAsDCSResponse error occured : errorMessage={}|error={}|traceId={}", e.getMessage(), e, traceId);
			return "DBN";
		}		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		String requestString = DCPEUtil.convertToString(response);
		LOGGER.info("getContractSubsidiaryTypeAsDCSResponse :timeTaken={}|traceId={}|getResult={}" , timeTaken, traceId, requestString);
		return response;
	}
	
	private String findUnifiedContractsSubsidiaryTypeById(String contractId, String traceId)throws DCPEException{
		Instant start = Instant.now();
		LOGGER.info("findUnifiedContractSubsidiaryTypeByIdRequest :contractId={}|traceId={}" , contractId, traceId);
		String finalSQL = map.get(SQLQueryEnum.SQL_CCBS_SBU_BY_CONTRACT_ID.getValue())+ " "+CRMSystemsEnum.UNION_APPENDER.getCrm()+" "+ map.get(SQLQueryEnum.SQL_DCS_SBU_BY_CONTRACT_ID.getValue());
        String result = null;
        try {
         result =	(String) queryExecuterRepository.getSingleObject(finalSQL,new Object[] {contractId,contractId}, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("getContractSubsidiaryTypeAsDCSResponse error occured: errorMessage={}|error={}|traceId={}", e.getMessage(), e,traceId);
			return "DBN";
		}
    	
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		String getResult = DCPEUtil.convertToString(result);
		LOGGER.info("findUnifiedContractSubsidiaryTypeByIdResponse : timeTaken={}|traceId={}|getResult={}" , timeTaken, traceId, getResult);

		return result;
	}

	private FixedNumberDTO processSysRows(List<Map<String, Object>> rows) {
		FixedNumberDTO fixedNumberDetailsDTO = null;
		for (Map<String, Object> row : rows) {

			fixedNumberDetailsDTO = new FixedNumberDTO();
			String crmSys = (String) row.get("SYSID");
			fixedNumberDetailsDTO.setCrmSystem(crmSys);
			String switchStatus = (String) row.get(Constants.STR_SWITCH_STATUS);
			fixedNumberDetailsDTO.setStatusCode(switchStatus);
			String prePost = (String) row.get(Constants.STR_PRE_POST);
			fixedNumberDetailsDTO.setPaidMode(prePost);
			String contractId = (String.valueOf(row.get(Constants.STR_CONTRACT_ID)));
			fixedNumberDetailsDTO.setCrmContractId(contractId);
			String mobileNo = (String) row.get(Constants.STR_MOBILE_NO);
			fixedNumberDetailsDTO.setMobileNo(mobileNo);

		}
		return fixedNumberDetailsDTO;
	}
}
