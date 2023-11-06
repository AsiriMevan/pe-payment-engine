package lk.dialog.pe.customer.info.service.impl;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.dialog.pe.common.domain.CRMProfileRequest;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.RestExecutor;
import lk.dialog.pe.customer.info.domain.Account;
import lk.dialog.pe.customer.info.domain.CCBSProfileRequest;
import lk.dialog.pe.customer.info.domain.Profile;
import lk.dialog.pe.customer.info.service.PersistanceIntegrationService;

@Service
public class PersistanceIntegrationServiceImpl implements PersistanceIntegrationService{

	private static final Logger LOGGER = LoggerFactory.getLogger(PersistanceIntegrationServiceImpl.class);

	private static final String INVOICE_NO = "{invoiceNo}";
	@Autowired
	private RestExecutor restExecutor;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Value("${pe.ccbs.persistance.url.accountNo-by-invoiceNo}")
	String getAccountNoByInvoiceNoEndpoint;
	
	@Value("${pe.ccbs.persistance.url.dbn-accountNo-by-invoiceNo}")
	String getDBNAccountNoByInvoiceNoEndpoint;
	
	@Value("${pe.ccbs.persistance.url.nfc-email-by-contract}")
	String getNFCEmailbyContractEndpoint;
	
	@Value("${pe.ccbs.persistance.url.ccbs-wifi-accountNo-by-invoiceNo}")
	String getCcbsWifiAccountNoByInvoiceNoEndpoint;
	
	@Value("${pe.ccbs.persistance.url.volte-profile-by-id}")
	String getVolteProfilesByIdEndpoint;
	
	@Value("${pe.ccbs.persistance.url.profiles-by-id}")
	String  getProfilesByIdEndpoint;
	
	@Value("${pe.ccbs.persistance.url.profiles-by-invoiceNo}")
	String getProfilesByInvoiceNoEndpoint;
	
	@Value("${pe.ccbs.persistance.url.profiles-by-acountNo}")
	String getProfilesByAccountEndpoint;
	
	@Value("${pe.ccbs.persistance.url.profiles-by-bulk-acountNo}")
	String getProfilesByBulkAccountEndpoint;

	@Value("${pe.ccbs.persistance.url.ccbs-availablity-by-contractNo}")
	String getIsSystemCcbsByContract;


	@Value("${pe.ccbs.persistance.url.ccbs-availablity-by-mobileNo}")
	String getIsSystemCcbsByMobileNo;
	
	@Override
	public List<Account> findAccountNoByInvoiceNo(String invoiceNumber, String traceId) throws DCPEException {
		LOGGER.info("getAccountNoByInvoiceNoRequest : traceId={}|invoiceNumber={}", traceId, invoiceNumber);
		Instant start = Instant.now();
		String uri = getAccountNoByInvoiceNoEndpoint.replace("{invoiceNumber}",invoiceNumber);
		
		JsonNode json  = restExecutor.post(uri, traceId, invoiceNumber, JsonNode.class); 
		
		 List<Account> response= new ArrayList<>();
			
			for (JsonNode jsonNode : json) {
				Account account= mapper.convertValue(jsonNode, Account.class);
				response.add(account);			
			}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getAccountNoByInvoiceNoResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
		return response;
	}

	@Override
	public List<Account> findDBNAccountNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException {
		LOGGER.info("getDBNAccountNoByInvoiceNoRequest : traceId={}|invoiceNo={}", traceId, invoiceNo);
		Instant start = Instant.now();
		String uri = getDBNAccountNoByInvoiceNoEndpoint.replace(INVOICE_NO,invoiceNo);
	
		JsonNode json = restExecutor.post(uri, traceId, invoiceNo, JsonNode.class);
						
		 List<Account> response= new ArrayList<>();
			
			for (JsonNode jsonNode : json) {
				Account account= mapper.convertValue(jsonNode, Account.class);
				response.add(account);			
			}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
			//todo - wrong log message | object is not log correctly
		LOGGER.info("getDBNAccountNoByInvoiceNoRequest : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
		return response;
	}

	@Override
	public List<Account> findCcbsWifiAccountNoByInvoiceNo(String invoiceNo, String traceId) throws DCPEException {
		LOGGER.info("getCcbsWifiAccountNoByInvoiceNoRequest : traceId={}|invoiceNo={}", traceId, invoiceNo);
		Instant start = Instant.now();
		//todo - there could be a trace id error because traceId is not replaced
		String uri = getCcbsWifiAccountNoByInvoiceNoEndpoint.replace(INVOICE_NO,invoiceNo);

	    JsonNode json = restExecutor.post(uri, traceId, invoiceNo, JsonNode.class);  
				
		 List<Account> response= new ArrayList<>();
			
			for (JsonNode jsonNode : json) {
				Account account= mapper.convertValue(jsonNode, Account.class);
				response.add(account);			
			}
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getCcbsWifiAccountNoByInvoiceNoResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
		return response;
	}

	@Override
	public List<Profile> findVolteProfilesById(CRMProfileRequest jsonReq, String traceId) throws DCPEException {
		LOGGER.info("getVolteProfilesByIdRequest : traceId={}|contractList={}", traceId, jsonReq);
		Instant start = Instant.now();
		JsonNode json = restExecutor.post(getVolteProfilesByIdEndpoint, traceId, jsonReq, JsonNode.class);
				
		 List<Profile> response= new ArrayList<>();
			
			for (JsonNode jsonNode : json) {
				Profile profile= mapper.convertValue(jsonNode, Profile.class);
				response.add(profile);			
			}
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getVolteProfilesByIdResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
		
		return response; 
	}

	@Override
	public List<Profile> findProfilesById(lk.dialog.pe.common.domain.CRMProfileRequest jsonReq, String traceId) throws DCPEException {
		LOGGER.info("getProfilesByIdRequest : traceId={}|jsonReq={}", traceId, jsonReq);
		Instant start = Instant.now(); 		
		JsonNode json = restExecutor.post(getProfilesByIdEndpoint, traceId, jsonReq, JsonNode.class);
		
		 List<Profile> response= new ArrayList<>();
			
			for (JsonNode jsonNode : json) {
				Profile profile= mapper.convertValue(jsonNode, Profile.class);
				response.add(profile);			
			}
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByIdResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
	
		return response;
	}

	@Override
	public List<Profile> findProfilesByInvoiceNo(String invoiceNo,String traceId) throws DCPEException {
		LOGGER.info("getProfilesByInvoiceNoRequest : traceId={}|invoiceNo={}", traceId, invoiceNo);
		Instant start = Instant.now();
		String urlEncodedInvoiceNo = "";
		try {
			urlEncodedInvoiceNo = URLEncoder.encode(invoiceNo, StandardCharsets.UTF_8.toString());
		} catch (Exception exception) {
			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("findProfilesByInvoiceNo-ERROR: traceId={}|timeTaken={}|error={}", traceId, timeTaken, exception.getMessage(), exception);
		}
		String uri = getProfilesByInvoiceNoEndpoint.replace(INVOICE_NO,urlEncodedInvoiceNo);
	
		JsonNode json = restExecutor.post(uri, traceId, invoiceNo, JsonNode.class); 
		
		 List<Profile> response= new ArrayList<>();
			
			for (JsonNode jsonNode : json) {
				Profile profile= mapper.convertValue(jsonNode, Profile.class);
				response.add(profile);			
			}
			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByInvoiceNoResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
		return response ;
	}

	@Override
	public String nfcEmailbyContract(String accountNo, String traceId) throws DCPEException {
		
		LOGGER.info("getnfcEmailbyContractRequest : traceId={}|accountNo={}", traceId, accountNo);
		Instant start = Instant.now();
		String uri = getNFCEmailbyContractEndpoint.replace("{contractId}",accountNo);
	
		String response = restExecutor.get(uri, traceId, String.class);  
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getnfcEmailbyContractResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
		return response ;				

	}

	@Override
	public List<Profile> findProfilesByAccount(CCBSProfileRequest ccbsProfileRequest, String traceId) throws DCPEException {
		LOGGER.info("getProfilesByAccountRequest : traceId={}|ccbsProfileRequest={}", traceId, ccbsProfileRequest);
		Instant start = Instant.now();
		JsonNode json = restExecutor.post(getProfilesByAccountEndpoint, traceId, ccbsProfileRequest, JsonNode.class);  
		
		 List<Profile> response= new ArrayList<>();
			
			for (JsonNode jsonNode : json) {
				Profile profile= mapper.convertValue(jsonNode, Profile.class);
				response.add(profile);			
			}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
			//todo - no toString for account so account data is not printed
		LOGGER.info("getProfilesByAccountReponce : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
		return response ;
	}

	public List<Profile> findProfilesByBulkAccount(CCBSProfileRequest ccbsProfileRequest, String traceId) throws DCPEException {
		LOGGER.info("getProfilesByBulkAccountRequest : traceId={}|ccbsProfileRequest={}", traceId, ccbsProfileRequest);
		Instant start = Instant.now();

		List<Profile> response = restExecutor.post(getProfilesByBulkAccountEndpoint, traceId, ccbsProfileRequest, ArrayList.class);  

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByBulkAccountResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
		return response ;
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


}
