package lk.dialog.pe.customer.info.service.impl;

import lk.dialog.pe.common.domain.CRMProfileRequest;
import lk.dialog.pe.common.domain.ConnectionRef;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.ProductTypeEnum;
import lk.dialog.pe.common.util.*;
import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;
import lk.dialog.pe.customer.info.domain.*;
import lk.dialog.pe.customer.info.service.MifeIntegrationService;
import lk.dialog.pe.customer.info.service.PersistanceIntegrationService;
import lk.dialog.pe.customer.info.service.QueryProfileService;
import lk.dialog.pe.customer.info.util.ErrorCodeEnum;
import lk.dialog.pe.customer.info.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QueryProfileServiceImpl implements QueryProfileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryProfileServiceImpl.class);

	@Autowired
	private SBUCriteria sbuCriteria;

	@Autowired
	private RequestTypeCriteria rtCriteria;

	@Autowired
	private PersistanceIntegrationService persistanceIntegrationService;

	@Autowired
	private MifeIntegrationService mifeIntegrationService;

	private final String VOLTE_CONN_REF = "VOLTE_CONN_REF";
	private final String VOLTE_CONTRACT = "VOLTE_CONTRACT";
	private final String TELBIZ_CONN_REF = "TELBIZ_CONN_REF";
	private final String TELBIZ_CONTRACT = "TELBIZ_CONTRACT";

	@Override
	public CRMProfileResponse queryProfile(CRMProfileRequest crmProfileRequest, String traceId) throws DCPEException {
		Instant start = Instant.now();
		String requestString = DCPEUtil.convertToString(crmProfileRequest);
		LOGGER.info("queryCRMProfileRequest : traceId={}|CRMProfileRequest={}", traceId, requestString);
		sbuCriteria.setFilter(Integer.toString(crmProfileRequest.getSbu()));
		rtCriteria.setFilter(Integer.toString(crmProfileRequest.getReqType()));
		FilterCriteria sbuReqTypeCriteria = new AndCriteria(sbuCriteria, rtCriteria);
		String logConstDbRqt = "Call DB API to get CX profile data ::";
		CRMProfileResponse jsonResponse = new CRMProfileResponse();
		
		if(crmProfileRequest.getProductCategory().equals(ProductCategoryEnum.NFC.valueOf())) {
			
			jsonResponse = getNFCProfiles(crmProfileRequest, sbuReqTypeCriteria, traceId);
				
		}else{

			if (persistanceIntegrationService != null) {
				if ((crmProfileRequest.getCustRef() != null && !"".equals(crmProfileRequest.getCustRef())) || (crmProfileRequest.getOldCustRef() != null && !"".equals(crmProfileRequest.getOldCustRef()))) {
					LOGGER.info("Received request to query by ID : traceId={}|trxID{}|logConstDbRqt{}", traceId, crmProfileRequest.getTrxID(), logConstDbRqt);
					
					jsonResponse = setupProfileListByCustRefferenceDetails(crmProfileRequest.getCustRefType(), crmProfileRequest.getProductCategory(), crmProfileRequest, sbuReqTypeCriteria, traceId);

				} else if (crmProfileRequest.getBillInvoiceNo() != null && !"".equals(crmProfileRequest.getBillInvoiceNo())) {
					LOGGER.info("Received request to query by  bill invoice no: traceId={}|logConstDbRqt{}| trxId{}", traceId, logConstDbRqt, crmProfileRequest.getTrxID());
					List<String> contractList = new ArrayList<>();
					List<String> mobileList = new ArrayList<>();
					
					jsonResponse = setupProfileListByBillInvoiceNo(mobileList, contractList, crmProfileRequest, traceId);
				} else {
					jsonResponse = setupProfileListByAccountDetails(crmProfileRequest, sbuReqTypeCriteria, traceId);
				}
			} else {
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_INVALID_PC.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_INVALID_PC.getErrorCode());
			}
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(jsonResponse);
		LOGGER.info("getCRMProfileResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);

		return jsonResponse;
	}

	private CRMProfileResponse getNFCProfiles(CRMProfileRequest crmProfileRequest, FilterCriteria sbuReqTypeCriteria, String traceId) throws DCPEException {
		Instant start = Instant.now();
		String requestString = DCPEUtil.convertToString(crmProfileRequest);
		LOGGER.info("getNFCProfilesRequest : traceId={}|CRMProfileRequest={}", traceId, requestString);

		CRMProfileResponse jsonResponse = new CRMProfileResponse();
		if ((crmProfileRequest.getCustRef() != null && !crmProfileRequest.getCustRef().isEmpty()) || (crmProfileRequest.getOldCustRef() != null && !crmProfileRequest.getOldCustRef().isEmpty())) {
			try {
				jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_INPUT_DATA.getErrorCode());
				jsonResponse.setErrorDesc(Constants.ERR_DESC_500_INVALID_SCENARIO);
			} catch (Exception e) {
				LOGGER.error(crmProfileRequest.getTrxID() + ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), e);
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_RBM.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_RBM_DESC.getErrorCode() + e.getMessage());
			}
		} else {
			LOGGER.info("Received NFC request to query by  mobile/invoice number/contract id :traceId={}|TrxID={}", crmProfileRequest.getTrxID());
			jsonResponse = setupNFCProfileList(crmProfileRequest, sbuReqTypeCriteria, traceId);
		}
		String responseString = DCPEUtil.convertToString(jsonResponse);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getNFCProfilesResponse: traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);
		return jsonResponse;
	}
	private CRMProfileResponse setupNFCProfileList(CRMProfileRequest crmProfileRequest,
			FilterCriteria sbuReqTypeCriteria, String traceId) throws DCPEException {
		Instant start = Instant.now();
		Long timeTaken;
		String requestString = DCPEUtil.convertToString(crmProfileRequest);
		LOGGER.info("setupNFCProfileListRequest: traceId={}|CRMProfileRequest={}", traceId, requestString);

		List<String> contractList = null;
		List<Profile> filterProfiles;
		CRMProfileResponse jsonResponse = new CRMProfileResponse();
		contractList = setupContractList(crmProfileRequest.getBillInvoiceNo(), crmProfileRequest.getTrxID(),
				crmProfileRequest.getAccounts(), traceId);

		try {
			List<Profile> listProfile = getRBMcustomerProfile(crmProfileRequest, contractList, ProductTypeEnum.NFC.getType(), traceId);

			if (listProfile != null && !listProfile.isEmpty()) {
				String listProfileConvertToString = DCPEUtil.convertToString(listProfile);

				LOGGER.info("Received RBM API response for CX data :traceId={}|TrxId={}|profileList={}", traceId, crmProfileRequest.getTrxID(), listProfileConvertToString);
				filterProfiles = sbuReqTypeCriteria.execute(listProfile);
				jsonResponse.setProfiles(filterProfiles);
				jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
				jsonResponse.setInvalidAccounts(getInvalidAccounts(filterProfiles, contractList, null));
			} else {
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_PROFILE_NOT_FOUND.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_PROFILE_NOT_FOUND.getErrorCode());
			}

		} catch (Exception ex) {
			LOGGER.info("CRMProfileResponse: traceId={}|{}", traceId, crmProfileRequest.getTrxID() + ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
			jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_RBM.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_RBM_DESC.getErrorCode() + ex.getMessage());
		    timeTaken = DCPEUtil.getTimeTaken(start);
		    String responseString = DCPEUtil.convertToString(jsonResponse);
		    LOGGER.info("setupNFCProfileListResponse: traceId={}|timeTaken={}|response={}", traceId,timeTaken, responseString);
			return jsonResponse;
		}
		String responseString = DCPEUtil.convertToString(jsonResponse);
		timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("setupNFCProfileListResponse: traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);

		return jsonResponse;
	}

	private CRMProfileResponse setupProfileListByAccountDetails(CRMProfileRequest crmProfileRequest,
			FilterCriteria sbuReqTypeCriteria, String traceId) {
		Instant start = Instant.now();
		String requestString = DCPEUtil.convertToString(crmProfileRequest);
		LOGGER.info("setupProfileListByAccountDetailsRquest: traceId={}|CRMProfileRequest={}", traceId, requestString);

		CRMProfileResponse jsonResponse = new CRMProfileResponse();
		List<Profile> listProfile = null;
		String logConstDbRsp = "Received DB API response for CX profileData ::";
		String logConstDbRqt = "Call DB API to get CX profile data ::";

		LOGGER.info("Received request to query by  mobile/contract id :: traceId={}|{}", traceId, crmProfileRequest.getTrxID());
		List<String> mobileList = new ArrayList<>();
		List<String> contractList = new ArrayList<>();

		List<ConnectionRef> accounts = crmProfileRequest.getAccounts() == null ? new ArrayList<>()
				: crmProfileRequest.getAccounts();
		accounts.forEach(account -> {
			if (account.getContractNo() != null && !account.getContractNo().isEmpty())
				contractList.add(account.getContractNo());
			else if (account.getConnRef() != null && !account.getConnRef().isEmpty())
				mobileList.add(account.getConnRef());

		});

		LOGGER.info("setupProfileListByAccountDetailsResponse: traceId={}|logConstDbRqt={}|TrxID={}", traceId, logConstDbRqt, crmProfileRequest.getTrxID());
		try {

			if (crmProfileRequest.getProductCategory().equals(ProductCategoryEnum.CCBS.valueOf())
					|| crmProfileRequest.getProductCategory().equals(ProductCategoryEnum.NFC.valueOf())) {
				listProfile = getProfileListByAccountNo(persistanceIntegrationService, mobileList, contractList,
						crmProfileRequest.getRequestUserId(), crmProfileRequest.getRemoteIP(),
						crmProfileRequest.getSourceSystem(), traceId);

				listProfile = accountsFilter(listProfile);

				LOGGER.info("setupProfileListByAccountDetailsResponse: traceId={}|logConstDbRqt={}|TrxID={}", traceId, logConstDbRsp, crmProfileRequest.getTrxID());
				jsonResponse = setupProfilesWithInvalidAccounts(listProfile, contractList, mobileList, sbuReqTypeCriteria, accounts, traceId);

			} else if (crmProfileRequest.getProductCategory().equals(ProductCategoryEnum.TELBIZ.valueOf())) {
				//todo - {done | testing_external calls only [(ProfileQueryServiceImpl.java:541, QueryProfileServiceImpl.java:638, external calls only), (ProfileQueryServiceImpl.java:515, QueryProfileServiceImpl.java:614, external calls only)]} - wrong telbiz implementation | [pe-bll/src/main/java/lk/dialog/crm/pe/bll/service/impl/ProfileQueryServiceImpl.java:310]
				LOGGER.info("ProductCategory TelBiz: traceId=[{}]", traceId);
				List<Profile> listProfile1 = null;

				List<String> connRefNewList = new ArrayList<>();
				List<String> contractNewList = new ArrayList<>();

				List<String> connRefListVolte = new ArrayList<>();
				List<String> contractListVolte = new ArrayList<>();
				List<String> connRefListTelbiz = new ArrayList<>();
				List<String> contracListTelbiz = new ArrayList<>();
				List<String> invalidConnRefList = new ArrayList<>();
				List<String> invalidContracList = new ArrayList<>();

				Map<String, List<String>> mapContractAndConnection = filterContractAndConnection(contractList, mobileList, crmProfileRequest.getTrxID(), traceId);
				String mapContractAndConnectionString = DCPEUtil.convertToString(mapContractAndConnection);
				LOGGER.info("traceId=[{}] | mapContractAndConnection=[{}]", traceId, mapContractAndConnectionString);

				for (Map.Entry<String, List<String>> connRefContractEntry : mapContractAndConnection.entrySet()) {
					final String INVALID_CONN_REF = "INVALID_CONN_REF";
					final String INVALID_CONTRACT = "INVALID_CONTRACT";
					if (connRefContractEntry.getKey().equalsIgnoreCase(VOLTE_CONN_REF)) {
						connRefListVolte = connRefContractEntry.getValue();
					} else if (connRefContractEntry.getKey().equalsIgnoreCase(VOLTE_CONTRACT)) {
						contractListVolte = connRefContractEntry.getValue();
					} else if (connRefContractEntry.getKey().equalsIgnoreCase(TELBIZ_CONN_REF)) {
						connRefListTelbiz = connRefContractEntry.getValue();
					} else if (connRefContractEntry.getKey().equalsIgnoreCase(TELBIZ_CONTRACT)) {
						contracListTelbiz = connRefContractEntry.getValue();
					} else if (connRefContractEntry.getKey().equalsIgnoreCase(INVALID_CONTRACT)) {
						invalidContracList = connRefContractEntry.getValue();
					} else if (connRefContractEntry.getKey().equalsIgnoreCase(INVALID_CONN_REF)) {
						invalidConnRefList = connRefContractEntry.getValue();
					}
				}

				if ((connRefListVolte != null && !connRefListVolte.isEmpty()) || (contractListVolte != null && !contractListVolte.isEmpty())) {
					LOGGER.info("Process volte lists: traceId=[{}]", traceId);
					listProfile1 = getProfileListByAccountNo(persistanceIntegrationService, connRefListVolte, contractListVolte, crmProfileRequest.getRequestUserId(), crmProfileRequest.getRemoteIP(),
							crmProfileRequest.getSourceSystem(), traceId);
					connRefNewList.addAll(mobileList);
					contractNewList.addAll(contractListVolte);
					connRefNewList.addAll(invalidConnRefList);
					contractNewList.addAll(invalidContracList);
				}

				if ((connRefListTelbiz != null && !connRefListTelbiz.isEmpty()) || (contracListTelbiz != null && !contracListTelbiz.isEmpty())) {
					LOGGER.info("Process telbiz lists: traceId=[{}]", traceId);
					connRefNewList.addAll(connRefListTelbiz);
					contractNewList.addAll(contracListTelbiz);
					connRefNewList.addAll(invalidConnRefList);
					contractNewList.addAll(invalidContracList);
				}

                if (listProfile1 != null && !listProfile1.isEmpty()) {
					String listProfile1String = DCPEUtil.convertToString(listProfile1);
					LOGGER.info("traceId=[{}] | listProfile1=[{}]", traceId, listProfile1String);
					listProfile = listProfile1;
                }

				LOGGER.info("Received request to query by ID :traceId=[{}]|trxId=[{}]|logConstDbRsp=[{}]", traceId, crmProfileRequest.getTrxID(), logConstDbRsp);
				if (listProfile == null || listProfile.isEmpty()) {
					jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
					jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_PROFILE_NOT_FOUND.getErrorCode());
					jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_PROFILE_NOT_FOUND.getErrorCode());
					jsonResponse.setInvalidAccounts(accounts);
				} else {
					LOGGER.info("listProfile contain values: traceId=[{}]", traceId);
					listProfile = sbuReqTypeCriteria.execute(listProfile);
					jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
					jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
					jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
					jsonResponse.setProfiles(listProfile);
					jsonResponse.setInvalidAccounts(getInvalidAccounts(listProfile, contractNewList, connRefNewList));
				}
			}

		} catch (Exception ex) {
			//todo - done - why not log.error | [pe-bll/src/main/java/lk/dialog/crm/pe/bll/service/impl/ProfileQueryServiceImpl.java:407]
			LOGGER.error("setupProfileListByAccountDetailsResponse - ERROR: traceId={}|TrxID={}|Error={}",traceId, crmProfileRequest.getTrxID() + ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
			jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_DATA_QUERY.getErrorCode());
			jsonResponse.setErrorDesc("Data query error ::" + ex.getMessage());
			String responseString = DCPEUtil.convertToString(jsonResponse);
			Long  timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("setupProfileListByAccountDetailsResponse: traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);
			return jsonResponse;
		}
		String responseString = DCPEUtil.convertToString(jsonResponse);
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("setupProfileListByAccountDetailsResponse: traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);
		return jsonResponse;

	}

	private CRMProfileResponse setupProfileListByBillInvoiceNo(List<String> mobileList, List<String> contractList,
			CRMProfileRequest crmProfileRequest, String traceId) throws DCPEException {
		Instant start = Instant.now();
		String crmProfileRequestConvertString = DCPEUtil.convertToString(crmProfileRequest);
		String mobileListConvertString = DCPEUtil.convertToString(mobileList);
		String contractListConvertString = DCPEUtil.convertToString(contractList);
		LOGGER.info("setupProfileListByBillInvoiceNoRequest: traceId={}|CRMProfileRequest={}|contractList={}|mobileList={}", traceId, crmProfileRequestConvertString, contractListConvertString, mobileListConvertString);

		String logConstDbRsp = "Received DB API response for CX profile data ::";
		CRMProfileResponse jsonResponse = new CRMProfileResponse();
		List<Profile> listProfile = null;
		if (crmProfileRequest.getProductCategory().equals(ProductCategoryEnum.CCBS.valueOf())
				&& crmProfileRequest.getBillInvoiceNo().startsWith("WBR")) {
			try {

				List<Account> accountNoList = persistanceIntegrationService
						.findCcbsWifiAccountNoByInvoiceNo(crmProfileRequest.getBillInvoiceNo(), traceId);
				LOGGER.info("Received DB API response for CX account data ::traceId={}|{}", traceId, crmProfileRequest.getTrxID());
				accountNoList.forEach(account -> {
					if (!account.getContractNo().isEmpty()) {
						contractList.add(account.getContractNo());

					} else if (account.getConnRef() != null && !account.getConnRef().isEmpty()) {
						mobileList.add(account.getConnRef());
					}
				});

				CCBSProfileRequest ccbsProfileRequest = new CCBSProfileRequest();
				ccbsProfileRequest.setContractList(contractList);
				ccbsProfileRequest.setMobileList(mobileList);
				ccbsProfileRequest.setRemoteIP(crmProfileRequest.getRemoteIP());
				ccbsProfileRequest.setRequestUserId(crmProfileRequest.getRequestUserId());
				ccbsProfileRequest.setSourceSystem(ccbsProfileRequest.getSourceSystem());
				listProfile = persistanceIntegrationService.findProfilesByAccount(ccbsProfileRequest, traceId);

			} catch (Exception ex) {
				LOGGER.info("setupProfileListByBillInvoiceNoResponse: traceId={}|TrxID={}|Error={}", traceId, crmProfileRequest.getTrxID() + ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_RBM.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_RBM_DESC.getErrorCode() + ex.getMessage());
				String responseConvertString = DCPEUtil.convertToString(jsonResponse);
				Long  timeTaken = DCPEUtil.getTimeTaken(start);
				LOGGER.info("setupProfileListByBillInvoiceNoResponse: traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseConvertString);
				return jsonResponse;
			}
			//todo - done - add else if telbiz | testing_external calls only [(ProfileQueryServiceImpl.java:237, QueryProfileServiceImpl.java:363), (ProfileQueryServiceImpl.java:249, QueryProfileServiceImpl.java:379)] | issues - (time-track is wrong, logConstDbRqtCall DB API to get CX profile data ::| trxIdnull) | [pe-bll/src/main/java/lk/dialog/crm/pe/bll/service/impl/ProfileQueryServiceImpl.java:233]
		} else if (ProductCategoryEnum.TELBIZ.valueOf().equals(crmProfileRequest.getProductCategory())) {
			String accountNumber;
			try {
				List<Account> listAccount = persistanceIntegrationService.findDBNAccountNoByInvoiceNo(crmProfileRequest.getBillInvoiceNo(), traceId);
				accountNumber = listAccount != null && !listAccount.isEmpty() ? listAccount.get(0).getContractNo() : null;
			} catch (Exception e) {
				//todo - to verify - is it ok to use logger.error here
				LOGGER.error("setupProfileListByBillInvoiceNoResponse: traceId=[{}]|TrxID=[{}]|Error=[{}]", traceId, crmProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), e);
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_RBM.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_RBM_DESC.getErrorCode() + e.getMessage());
				String responseConvertString = DCPEUtil.convertToString(jsonResponse);
				Long  timeTaken = DCPEUtil.getTimeTaken(start);
				LOGGER.info("setupProfileListByBillInvoiceNoResponse: traceId=[{}]|timeTaken=[{}]|response=[{}]", traceId, timeTaken, responseConvertString);
				return jsonResponse;
			}
			if (accountNumber != null && !accountNumber.isEmpty()) {
				try {
					FixedNumberDTO fixedNumberDetailsDTO = mifeIntegrationService.validateSystemAvailability(accountNumber, "AccountNo", traceId);
					String crmSystem = fixedNumberDetailsDTO.getCrmSystem();
					contractList.add(accountNumber);
					if (Constants.CRM_SYS_CCBS.equalsIgnoreCase(crmSystem)) {
						listProfile = getProfileListByAccountNo(persistanceIntegrationService, mobileList, contractList, crmProfileRequest.getRequestUserId(), crmProfileRequest.getRemoteIP(), crmProfileRequest.getSourceSystem(), traceId);
					}
                } catch (Exception e) {
					//todo - to verify - is it ok to use logger.error here
					LOGGER.error("setupProfileListByBillInvoiceNoResponse: traceId=[{}]|TrxID=[{}]|Error=[{}]", traceId, crmProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), e);
					jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
					jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_RBM.getErrorCode());
					jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_RBM_DESC.getErrorCode() + e.getMessage());
					String responseConvertString = DCPEUtil.convertToString(jsonResponse);
					Long  timeTaken = DCPEUtil.getTimeTaken(start);
					LOGGER.info("setupProfileListByBillInvoiceNoResponse: traceId=[{}]|timeTaken=[{}]|response=[{}]", traceId, timeTaken, responseConvertString);
					return jsonResponse;
				}
			}
		} else {
			listProfile = persistanceIntegrationService.findProfilesByInvoiceNo(crmProfileRequest.getBillInvoiceNo(),
					traceId);

		}
		String listProfileConvertString = DCPEUtil.convertToString(listProfile);
		LOGGER.info("setupProfileListByBillInvoiceNoResponse: traceId={}|logConstDbRsp={}|TrxID={}|ProfileList={}", traceId, logConstDbRsp, crmProfileRequest.getTrxID(), listProfileConvertString);
		jsonResponse = setupProfiles(listProfile);
		String responseConvertString = DCPEUtil.convertToString(jsonResponse);
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("setupProfileListByBillInvoiceNoResponse: traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseConvertString);
		return jsonResponse;
	}

	private CRMProfileResponse setupProfileListByCustRefferenceDetails(String custRefType, Integer productCategory,
			CRMProfileRequest crmProfileRequest, FilterCriteria sbuReqTypeCriteria, String traceId)
			throws DCPEException {
		Instant start = Instant.now();
		CRMProfileResponse jsonResponse = new CRMProfileResponse();
		String logConstDbRsp = "Received DB API response for CX profile data ::";
		String crmProfileRequestConvertToString = DCPEUtil.convertToString(crmProfileRequest);
		LOGGER.info("setupProfileListByCustRefferenceDetailsRequest :custRefType={}|productCategory={}|crmProfileRequest={}traceId={}",
				custRefType, productCategory, crmProfileRequestConvertToString, traceId);

		if (custRefType != null && !"".equalsIgnoreCase(custRefType)) {
			List<Profile> listProfile = null;
			//todo - done - add if current, else if telbiz | testing_external calls only [(ProfileQueryServiceImpl.java:166, QueryProfileServiceImpl.java:414)] | [pe-bll/src/main/java/lk/dialog/crm/pe/bll/service/impl/ProfileQueryServiceImpl.java:164]
			if (productCategory.equals(ProductCategoryEnum.CCBS.valueOf())
					|| productCategory.equals(ProductCategoryEnum.NFC.valueOf())) {

				listProfile = persistanceIntegrationService.findProfilesById(crmProfileRequest, traceId);
				LOGGER.info("listProfile : traceId={}|crmProfileRequest{}", traceId, crmProfileRequest);
			} else if (ProductCategoryEnum.TELBIZ.valueOf().equals(productCategory)) {
				listProfile = persistanceIntegrationService.findVolteProfilesById(crmProfileRequest, traceId);
				LOGGER.info("listProfile | volte : traceId=[{}]|crmProfileRequest=[{}]", traceId, crmProfileRequest);
			}
			LOGGER.info("Received request to query by ID :traceId={}|trxId={}|logConstDbRsp={}", traceId, crmProfileRequest.getTrxID(), logConstDbRsp);
			if (listProfile == null || listProfile.isEmpty()) {
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_PROFILE_NOT_FOUND.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_PROFILE_NOT_FOUND.getErrorCode());
			} else {
				listProfile = sbuReqTypeCriteria.execute(listProfile);
				jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
				jsonResponse.setProfiles(listProfile);
			}
		} else {
			jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_CUSTOMER_REF_TYPE.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_CUSTOMER_REF_TYPE.getErrorCode());
			String responseString = DCPEUtil.convertToString(jsonResponse);
			Long  timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("setupProfileListByCustRefferenceDetailsResponse :requestString={}|traceId={}|timeTaken={}", responseString, traceId, timeTaken);
			return jsonResponse;
		}
		String responseString = DCPEUtil.convertToString(jsonResponse);
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("setupProfileListByCustRefferenceDetailsResponse :requestString={}|traceId={}|timeTaken={}", responseString, traceId, timeTaken);
		return jsonResponse;
	}

	private List<String> setupContractList(String billInvoiceNo, Long trxId, List<ConnectionRef> accounts,
			String traceId) throws DCPEException {
		
		Instant start = Instant.now();
		List<String> contractList = new ArrayList<>();
		if (billInvoiceNo != null && !billInvoiceNo.isEmpty() && billInvoiceNo.startsWith("NBR")) {

			List<Account> accountNoList = persistanceIntegrationService.findAccountNoByInvoiceNo(billInvoiceNo,
					traceId);
			String requestString = DCPEUtil.convertToString(accountNoList);
			LOGGER.info(
					"setupContractListRequest - Received DB API response for CX account data 1111:BillInvoiceNo={}|accountNoList={}|trxId={}|traceId={}",
					billInvoiceNo, requestString, trxId, traceId);

			accountNoList.forEach(account -> {
				if (!account.getContractNo().isEmpty())
					LOGGER.info("account.getContractNo():traceId={}|accountNoList={} ", traceId, accountNoList);

				contractList.add(account.getContractNo());
			});

		} else {
			String accountsConverToString = DCPEUtil.convertToString(accounts);
			LOGGER.info("accounts :accountNoList={}|traceId={}", accountsConverToString, traceId);

			List<ConnectionRef> accountList = accounts == null ? new ArrayList<>() : accounts;
			accountList.forEach(account -> {
				if (account.getContractNo() != null && !account.getContractNo().isEmpty() && account.getContractNo()
						.matches(ValidationPatternEnum.NFC_CONTRACT_NO_VALIDATOR.getPattern())) {
					contractList.add(account.getContractNo());
				}
			});

		}
		
		String contractListConverToString = DCPEUtil.convertToString(contractList);
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("setupContractListResponse :contractList={}|traceId={}|timeTaken={}", contractListConverToString, traceId, timeTaken);
		return contractList;
	}

	//todo - done - exception is not thrown | [pe-bll/src/main/java/lk/dialog/crm/pe/bll/service/impl/ProfileQueryServiceImpl.java:591]
	public List<Profile> getRBMcustomerProfile(CRMProfileRequest jsonReq, List<String> contractList, int productType,
			String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getRBMcustomerProfileRequest  : contractList={}|traceId={}", contractList.toArray(), traceId);

		List<Profile> listProfile = new ArrayList<>();
		LOGGER.info("Call RBM API to get CX profile data ::traceId={}|{}", traceId, jsonReq.getTrxID());
		contractList.forEach(contract -> {
			try {
				fillRBMProfiles(listProfile, persistanceIntegrationService, contract, productType, traceId);
			} catch (Exception e) {
				Long timeTaken = DCPEUtil.getTimeTaken(start);
				LOGGER.error("getRBMcustomerProfileResponse - ERROR : traceId ={}|timeTaken={}|error={} ", traceId, timeTaken, e.getMessage(), e);
				throw new RuntimeException(e);
			}
		});
		Long timeTaken1 = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getRBMcustomerProfileResponse - SIZE : listProfile ={}|traceId={}|timeTaken={} ", listProfile.size(), traceId, timeTaken1);

		return listProfile;
	}

	public void fillRBMProfiles(List<Profile> listProfile, PersistanceIntegrationService persistanceIntegrationService,
			String contract, int productType, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("fillRBMProfiles : listProfile ={}|traceId={} ", listProfile.toArray(), traceId);

		RBMCustomer customer = null;

		customer = mifeIntegrationService.findCustomerByAccountRef(contract, traceId);
		if (customer != null) {
			String email;
			Profile profile = new Profile();
			String title = customer.getTitle() == null || customer.getTitle().isEmpty() ? "" : customer.getTitle();
			String name = customer.getCustName() == null || customer.getCustName().isEmpty() ? ""
					: customer.getCustName();
			profile.setCustName(title + " " + name);
			profile.setAddrLine1(customer.getAddrLine1());
			profile.setAddrLine2(customer.getAddrLine2());
			profile.setAddrLine3(customer.getAddrLine3());
			profile.setPostalCode(customer.getPostalCode());
			email = persistanceIntegrationService.nfcEmailbyContract(contract, traceId);
			profile.setEmail(email);

			List<Account> accountsList = new ArrayList<>();
			Account account = new Account();
			RBMAccount accountDTO = mifeIntegrationService.readAllAccountAttributes(contract, traceId);
			account.setAccountNo(customer.getCustRef());
			account.setContractNo(accountDTO.getAccountNum());
			account.setAccountType(accountDTO.getAccountType());

			RBMCustomer customerDTO = mifeIntegrationService.readAllCustomerAttributes(contract, traceId);
			account.setPrCode(customerDTO.getPrCustRef());
			account.setBillCycle(customerDTO.getBillRunCode());
			profile.setCustRef(customerDTO.getCustRef());


			SwitchStatus sw = SwitchStatus.getSwitchStatus(accountDTO.getConStatus());
			account.setConStatus(sw != null ? sw.valueOf() : null);
			account.setProductType(productType);
			account.setHybridFlag(HybridStatusEnum.NFC_HYBRID.getStatus());
			account.setPrEmail(email);
			account.setContractEmail(email);
			SBU sbu = SBU.getSBU(accountDTO.getSbu());
			if (sbu != null)
				account.setSbu(sbu.valueOf());
			accountsList.add(account);
			profile.setAccounts(accountsList);
			listProfile.add(profile);
			Long  timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("fillRBMProfilesresponse : listProfileSize={}|traceId={}|timeTaken={} ", listProfile.size(), traceId, timeTaken);

		}
	}

	private List<Profile> getProfileListByAccountNo(PersistanceIntegrationService persistanceIntegrationService,
			List<String> mobileBulkList, List<String> contractBulkList, String requestUserId, String remoteIP,
			String sourceSystem, String traceId) throws DCPEException {
		Instant start = Instant.now();
		CCBSProfileRequest ccbsProfileRequest = new CCBSProfileRequest();
		ccbsProfileRequest.setContractList(contractBulkList);
		ccbsProfileRequest.setMobileList(mobileBulkList);
		ccbsProfileRequest.setRequestUserId(requestUserId);
		ccbsProfileRequest.setRemoteIP(remoteIP);
		ccbsProfileRequest.setSourceSystem(sourceSystem);
		String ccbsProfileRequestConvertToString = DCPEUtil.convertToString(ccbsProfileRequest);
		LOGGER.info("getProfileListByAccountNoRequest: traceId={}|ccbsProfileRequest={}", traceId, ccbsProfileRequestConvertToString);

		if (mobileBulkList.size() > lk.dialog.pe.customer.info.util.Constants.BULK_REQUEST_PER_BATCH
				|| contractBulkList.size() > lk.dialog.pe.customer.info.util.Constants.BULK_REQUEST_PER_BATCH) {
			Long  timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("getProfileListByAccountNoResponse: traceId={}|timeTaken={}", traceId, timeTaken);
			return persistanceIntegrationService.findProfilesByBulkAccount(ccbsProfileRequest, traceId);
		} else {
			Long  timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("getProfileListByAccountNoResponse: traceId={}|timeTaken={}", traceId, timeTaken);
			return persistanceIntegrationService.findProfilesByAccount(ccbsProfileRequest, traceId);
		}
	}

	private Map<String, List<String>> filterContractAndConnection(List<String> contractList, List<String> mobileList, Long reqId, String traceId) throws DCPEException {

		Map<String, List<String>> mapContractAndConnection = new HashMap<>();

		List<String> connRefListVolte = new ArrayList<>();
		List<String> contractListVolte = new ArrayList<>();
		List<String> connRefListTelbiz = new ArrayList<>();
		List<String> contractListTelbiz = new ArrayList<>();

		LOGGER.info("filterContractAndConnectionRequest: traceId=[{}]|contractList=[{}]|mobileList=[{}]|reqId=[{}]", traceId, contractList, mobileList, reqId);
		if (contractList != null && !contractList.isEmpty()) {
			for (String contractNo : contractList) {
				if (contractNo.matches("[0-9]+")) {
					FixedNumberDTO fixedNumberDetailsDTO = persistanceIntegrationService.validateCcbsAvailablityForContract(contractNo, traceId);
					if (fixedNumberDetailsDTO != null) {
						String crmSystem = fixedNumberDetailsDTO.getCrmSystem();
						if ("CCBS".equalsIgnoreCase(crmSystem)) {
							contractListVolte.add(contractNo);
						} else {
							contractListTelbiz.add(contractNo);
							contractListVolte.add(contractNo);
						}
					} else {
						contractListTelbiz.add(contractNo);
						contractListVolte.add(contractNo);
					}
				} else {
					/* Alphanumeric contract obliviously DBN */
					contractListTelbiz.add(contractNo);
					contractListVolte.add(contractNo);
				}
			}
			mapContractAndConnection.put(VOLTE_CONTRACT, contractListVolte);
			mapContractAndConnection.put(TELBIZ_CONTRACT, contractListTelbiz);
		}

		if (mobileList != null && !mobileList.isEmpty()) {
			for (String mobileNo : mobileList) {
				FixedNumberDTO fixedNumberDetailsDTO = persistanceIntegrationService.validateCcbsAvailablityForConnRef(mobileNo, traceId);
				if (fixedNumberDetailsDTO != null) {
					String crmSystem = fixedNumberDetailsDTO.getCrmSystem();
					if (("CCBS".equalsIgnoreCase(crmSystem) || "DCS".equalsIgnoreCase(crmSystem))) {
						connRefListVolte.add(mobileNo.startsWith("0") ? mobileNo.substring(1) : mobileNo);
					} else {
						connRefListTelbiz.add(mobileNo);
					}
				} else {
					connRefListTelbiz.add(mobileNo);
				}
			}

			mapContractAndConnection.put(VOLTE_CONN_REF, connRefListVolte);
			mapContractAndConnection.put(TELBIZ_CONN_REF, connRefListTelbiz);
		}

		LOGGER.info("filterContractAndConnectionResponse: traceId=[{}]|mapContractAndConnection=[{}]", traceId, mapContractAndConnection);
		return mapContractAndConnection;

	}
//todo - add logs
	private List<ConnectionRef> getInvalidAccounts(List<Profile> validProfiles, List<String> queryContracts,
			List<String> queryConnRefs) {

		List<Account> validAccountList = validProfiles.stream().flatMap(prof -> prof.getAccounts().stream())
				.collect(Collectors.toList());
		List<String> validConnRefArr = validAccountList.stream().map(Account::getConnRef).collect(Collectors.toList());
		List<String> validContractList = validAccountList.stream().map(Account::getContractNo)
				.collect(Collectors.toList());
		List<ConnectionRef> conRefList = new ArrayList<>();
		if (queryContracts != null) {
			List<String> filterContractList = queryContracts.stream().filter(n -> !validContractList.contains(n))
					.collect(Collectors.toList());
			List<String> uniqueContractList = filterContractList.stream().distinct().collect(Collectors.toList());
			uniqueContractList.stream().forEach(fc -> conRefList.add(new ConnectionRef(null, fc)));
		}
		if (queryConnRefs != null) {
			List<String> filterConnRefList = queryConnRefs.stream().filter(n -> !validConnRefArr.contains(n))
					.collect(Collectors.toList());
			List<String> uniqueConnRefList = filterConnRefList.stream().distinct().collect(Collectors.toList());
			uniqueConnRefList.stream().forEach(fc -> conRefList.add(new ConnectionRef(fc, null)));
		}
		
		return conRefList;
	}
	private CRMProfileResponse setupProfilesWithInvalidAccounts(List<Profile> listProfile, List<String> contractList, List<String> mobileList, FilterCriteria sbuReqTypeCriteria, List<ConnectionRef> accounts, String traceId ) {
		CRMProfileResponse jsonResponse = new CRMProfileResponse();
		Instant start = Instant.now();
		if (listProfile == null || listProfile.isEmpty()) {
			jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_PROFILE_NOT_FOUND.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_PROFILE_NOT_FOUND.getErrorCode());
			jsonResponse.setInvalidAccounts(accounts);
		} else {
			////todo - this is commented out in old pe code | [pe-bll/src/main/java/lk/dialog/crm/pe/bll/service/impl/ProfileQueryServiceImpl.java:386]
			listProfile = sbuReqTypeCriteria.execute(listProfile);
			//todo - some code are missing | [pe-bll/src/main/java/lk/dialog/crm/pe/bll/service/impl/ProfileQueryServiceImpl.java:388]
			jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
			jsonResponse.setProfiles(listProfile);
			jsonResponse.setInvalidAccounts(getInvalidAccounts(listProfile, contractList, mobileList));

			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("setupProfilesWithInvalidAccountsResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, jsonResponse);
			//todo - done - logs are missing | [pe-bll/src/main/java/lk/dialog/crm/pe/bll/service/impl/ProfileQueryServiceImpl.java:400]
		}
		return jsonResponse;
	}
	private CRMProfileResponse setupProfiles(List<Profile> listProfile) {
		CRMProfileResponse jsonResponse = new CRMProfileResponse();
			if (listProfile == null || listProfile.isEmpty()) {
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_PROFILE_NOT_FOUND.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_PROFILE_NOT_FOUND.getErrorCode());
			} else {
				jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
				jsonResponse.setProfiles(listProfile);
			}
		return jsonResponse;
	}
	private List<Profile> accountsFilter(List<Profile> list) {
		list.forEach(pro -> {
			List<Account> listr = new ArrayList<>();
			listr = pro.getAccounts().stream().filter(account -> (SBU.DBN.valueOf() != account.getSbu()))
					.collect(Collectors.toList());
			pro.setAccounts(listr);

		});

		return list;
	}
}
