package lk.dialog.pe.billing.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lk.dialog.pe.billing.domain.DcsProfile;
import lk.dialog.pe.billing.util.DcsRemarkRequest;
import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.dialog.pe.billing.domain.Profile;
import lk.dialog.pe.billing.service.PersistanceIntegrationService;
import lk.dialog.pe.billing.util.HotlineRemarkRequest;
import lk.dialog.pe.billing.util.RemarkInfo;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.RestExecutor;

@Service
public class PersistanceIntegrationServiceImpl implements PersistanceIntegrationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersistanceIntegrationServiceImpl.class);

	@Autowired
	private RestExecutor restExecutor;
	
	@Value("${pe.ccbs.persistance.url.get-mobile-of-contractId}")
	String getMobileOfContractIdEndpoint;
	
	@Value("${pe.ccbs.persistance.url.get-contractId-of-mobile}")
	String getContractIdOfMobileEndpoint;
	
	@Value("${pe.ccbs.persistance.url.get-wifi-contract-by-mobile}")
	String getWifiContractIdOrMobileEndpoint;
	
	@Value("${pe.ccbs.persistance.url.get-hotline-remarks}")
	String getHotlineRemarksEndpoint;
	
	@Value("${pe.ccbs.persistance.url.get-wifi-remarks}")
	String getCcbsWifiRemarksEndpoint;

	@Value("${pe.ccbs.persistance.url.ccbs-availablity-by-contractNo}")
	String getIsSystemCcbsByContract;

	@Value("${pe.ccbs.persistance.url.ccbs-availablity-by-mobileNo}")
	String getIsSystemCcbsByMobileNo;

	@Value("${pe.ccbs.persistance.url.get-dcs-mobile-by-contractNo}")
	String getDcsMobileByContract;

	@Value("${pe.ccbs.persistance.url.get-dcs-remarks}")
	String getDcsRemarksEndpoint;
	
	@Autowired
	ObjectMapper mapper;

	@Override
	public String getMobileOfContractId(String contractNo, String traceId) throws DCPEException {

		LOGGER.info("getMobileOfContractIdRequest : traceId={}|contractNo={}", traceId, contractNo);
		Instant start = Instant.now();
		
		String uri = getMobileOfContractIdEndpoint.replace("{contractNo}",contractNo);
		
		LOGGER.info("getMobileOfContractIdRequest : traceId={}|contractNo={}|uri{}", traceId, contractNo, uri);
		 String response = restExecutor.get(uri, traceId, String.class);  

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getMobileOfContractIdResponse : traceId={}|timeTaken={}|mobileNo={}", traceId, timeTaken, response);
		return response ;		
	}
	
	@Override
	public String getContractIdOfMobile(String mobileNo, String traceId) throws DCPEException {

		LOGGER.info("getContractIdOfMobileRequest : traceId={}|mobileNo={}", traceId, mobileNo);
		Instant start = Instant.now();
		String uri = getContractIdOfMobileEndpoint.replace("{mobileNumber}",mobileNo);
		String response = restExecutor.get(uri, traceId, String.class);  
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getContractIdOfMobileResponse : traceId={}|timeTaken={}|contractNo={}", traceId, timeTaken, response);
		return response ;		
	}
	
	@Override
	public List<Profile> getWifiContractIdOrMobile(String mobile, String contractID, String traceId) throws DCPEException {
		LOGGER.info("getWifiContractIdOrMobileRequest : traceId={}|mobileNo={}|contractID={}", traceId, mobile, contractID);
		Instant start = Instant.now();
		
		try {
			List<Map<String, Object>> rows = null;
			List<Profile> profileList = new ArrayList<>();
			String uri = String.format(getWifiContractIdOrMobileEndpoint, mobile, contractID);
			rows = restExecutor.get(uri, traceId, ArrayList.class);
			for (Map<String, Object> map: rows){
				Profile profile = (Profile) map.values();
				profileList.add(profile);
			}
			
			String responseString = DCPEUtil.convertToString(profileList);
			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("getWifiContractIdOrMobileResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);
			return profileList;		
		} catch (Exception ex) {
			LOGGER.error("getWifiContractIdOrMobileResponse: error={}", ex);
			return Collections.emptyList();
		}
	}

	@Override
	public FixedNumberDTO validateCcbsAvailablityForContract(String contractNo, String traceId) {
		LOGGER.info("validateCcbsAvailabilityForContractRequest : traceId=[{}]|ContractNo=[{}]", traceId, contractNo);
		Instant start = Instant.now();
		try {
			String uri = getIsSystemCcbsByContract.replace("{contractId}",contractNo).replace("{traceId}", traceId);
			FixedNumberDTO fixedNumberDTO = restExecutor.get(uri, traceId, FixedNumberDTO.class);
			Long timeTaken = DCPEUtil.getTimeTaken(start);
			String fixedNumberDTOString = DCPEUtil.convertToString(fixedNumberDTO);
			LOGGER.info("validateCcbsAvailabilityForContractResponse: traceId=[{}]|ContractNo=[{}]|fixedNumberDetailsDTO=[{}]|timeTaken=[{}]", traceId, contractNo, fixedNumberDTOString, timeTaken);
			return fixedNumberDTO;
		} catch (DCPEException dcpeException) {
			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("validateCcbsAvailabilityForContract error occurred: error=[{}]|traceId=[{}]|ContractNo=[{}]|timeTaken=[{}]", dcpeException.getMessage(), traceId, contractNo, timeTaken);
			return null;
		}
	}

	@Override
	public FixedNumberDTO validateCcbsAvailablityForConnRef(String mobileNo, String traceId) {
		LOGGER.info("validateCcbsAvailabilityForConnRefRequest : traceId=[{}]|MobileNo=[{}]", traceId, mobileNo);
		Instant start = Instant.now();
		try {
			String uri = getIsSystemCcbsByMobileNo.replace("{mobileNo}",mobileNo).replace("{traceId}", traceId);
			FixedNumberDTO fixedNumberDTO = restExecutor.get(uri, traceId, FixedNumberDTO.class);
			Long timeTaken = DCPEUtil.getTimeTaken(start);
			String fixedNumberDTOString = DCPEUtil.convertToString(fixedNumberDTO);
			LOGGER.info("validateCcbsAvailabilityForConnRefResponse: traceId=[{}]|MobileNo=[{}]|fixedNumberDetailsDTO=[{}]|timeTaken=[{}]", traceId, mobileNo, fixedNumberDTOString, timeTaken);
			return fixedNumberDTO;
		} catch (DCPEException dcpeException) {
			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("validateCcbsAvailabilityForConnRef error occurred: error=[{}]|traceId=[{}]|MobileNo=[{}]|timeTaken=[{}]", dcpeException.getMessage(), traceId, mobileNo, timeTaken);
			return null;
		}
	}

	@Override
	public List<DcsProfile> getDcsMobileOfContractId(String contractID, String traceId) throws DCPEException {
		LOGGER.info("getDcsMobileOfContractId Request : traceId=[{}] | contractID=[{}]", traceId, contractID);
		Instant start = Instant.now();
		String uri = getDcsMobileByContract.replace("{contractId}", contractID).replace("{traceId}", traceId);

		JsonNode json = restExecutor.post(uri, traceId, contractID, JsonNode.class);

		List<DcsProfile> response= new ArrayList<>();

		for (JsonNode jsonNode : json) {
			DcsProfile dcsProfile= mapper.convertValue(jsonNode, DcsProfile.class);
			response.add(dcsProfile);
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getDcsMobileOfContractId Response : traceId=[{}] | timeTaken=[{}] | response=[{}]", traceId, timeTaken, response);
		return response;
	}

	@Override
	public List<RemarkInfo> getDcsRemarks(List<String> volteContractList, String traceId) throws DCPEException {
		LOGGER.info("getDcsRemarks Request : traceId=[{}] | volteContractList=[{}]", traceId, volteContractList);
		Instant start = Instant.now();
		DcsRemarkRequest jsonReq = new DcsRemarkRequest(volteContractList);
		JsonNode json = restExecutor.post(getDcsRemarksEndpoint, traceId, jsonReq, JsonNode.class);

		List<RemarkInfo> dcsRemarkInfoList = mapper.convertValue(json,
				new TypeReference<List<RemarkInfo>>() {});

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getDcsRemarks Response : traceId=[{}] | timeTaken=[{}] | dcsRemarkInfoList=[{}]", traceId, timeTaken, dcsRemarkInfoList);

		return dcsRemarkInfoList;
	}

	@Override
	public List<RemarkInfo> getHotlineRemarks(HotlineRemarkRequest jsonReq,String traceId) throws DCPEException {
		
		LOGGER.info("getHotlineRemarksRequest : traceId={}|contractList={}", traceId, jsonReq);
		Instant start = Instant.now();
		JsonNode json = restExecutor.post(getHotlineRemarksEndpoint, traceId, jsonReq, JsonNode.class);

		List<RemarkInfo> remarkInfoList = mapper.convertValue(json,
				new TypeReference<List<RemarkInfo>>() {});

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		//todo - why is this commented out?
//		LOGGER.info("getHotlineRemarksResponse : traceId={}|timeTaken={}|hotLineRemarkList={}", traceId, timeTaken, remarkInfoList);

		return remarkInfoList;
	}

	@Override
	public List<RemarkInfo> getCcbsWifiRemarks(HotlineRemarkRequest jsonReq, String traceId)throws DCPEException {
		
		LOGGER.info("getCcbsWifiRemarksRequest : traceId={}|contractNo={}", traceId, jsonReq);
		Instant start = Instant.now();
		JsonNode json = restExecutor.post(getCcbsWifiRemarksEndpoint, traceId, jsonReq, JsonNode.class);
		List<RemarkInfo> remarkWifiInfoList= new ArrayList<>();
		
		for (JsonNode jsonNode : json) {
			RemarkInfo infoList= mapper.convertValue(jsonNode, RemarkInfo.class);
			remarkWifiInfoList.add(infoList);			
		}
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getCcbsWifiRemarksResponse : traceId={}|timeTaken={}|wifiRemarkList={}", traceId, timeTaken, remarkWifiInfoList);
		
		return remarkWifiInfoList;
	}

}
