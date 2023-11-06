package lk.dialog.pe.billing.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lk.dialog.pe.billing.domain.*;
import lk.dialog.pe.common.util.*;
import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.dialog.pe.billing.service.MifeIntegrationService;
import lk.dialog.pe.billing.service.PersistanceIntegrationService;
import lk.dialog.pe.billing.service.QueryRBMProfileService;
import lk.dialog.pe.billing.util.ValidationPatternEnum;
import lk.dialog.pe.common.exception.DCPEException;

@Service
public class QueryRBMProfileServiceImpl implements QueryRBMProfileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryRBMProfileServiceImpl.class);

	@Autowired
	private PersistanceIntegrationService persistanceIntegrationService;

	@Autowired
	private MifeIntegrationService mifeIntegrationService;

	@Override
	public BillingProfileResponse queryRBMProfile(BillingProfileRequest billingProfileRequest, String traceId) throws DCPEException {

		Instant start = Instant.now();
		LOGGER.info("queryRBMProfileRequest : traceId={}|BillingProfileRequest={}", traceId, billingProfileRequest);

		List<AccountRef> accounts = billingProfileRequest.getAccounts();
		BillingProfileResponse jsonResponse = new BillingProfileResponse();
		ProductCategoryEnum productCategory = ProductCategoryEnum.getProductCategoryByValue(billingProfileRequest.getProductCategory());

		switch (productCategory) {
		case NFC: {
			try {
				return this.getNfcBillingProfileResponse(accounts, billingProfileRequest, jsonResponse, traceId);
			} catch (Exception ex) {
				LOGGER.error("queryRBMProfileResponse: traceId={}|TrxID={}|ErrorCode={}|Error={}", traceId, billingProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_FAIL.getErrorCode());
				jsonResponse.setErrorDesc(ex.getMessage());
				return jsonResponse;
			}

		}
		case CCBS: {
			try {

				return this.getCcbsBillingProfileResponse(accounts, billingProfileRequest, jsonResponse, traceId);

			} catch (DCPEException ex) {
				LOGGER.error("queryRBMProfileResponse: traceId={}|TrxID={}|ErrorCode={}|Error={}", traceId, billingProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_INVALID_PT.getErrorCode());
				jsonResponse.setErrorDesc(ex.getErrorMessage());
				return jsonResponse;
			} catch (NoSuchBeanDefinitionException ex) {
				LOGGER.error("queryRBMProfileResponse: traceId={}|TrxID={}|ErrorCode={}|Error={}", traceId, billingProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_INVALID_PC.getErrorCode());
				jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_INVALID_PC.getErrorCode());
				return jsonResponse;
			} catch (Exception ex) {
				LOGGER.error("queryRBMProfileResponse: traceId={}|TrxID={}|ErrorCode={}|Error={}", traceId, billingProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
				jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_FAIL.getErrorCode());
				jsonResponse.setErrorDesc(ex.getMessage());
				
				Long timeTaken = DCPEUtil.getTimeTaken(start);
				String responseString = DCPEUtil.convertToString(jsonResponse);
				LOGGER.info("queryRBMProfileResponse : traceId={}|timeTaken={}|responseString={}", traceId, timeTaken, responseString);

				return jsonResponse;
			}
		}
		case TELBIZ:
            return getTelbizBillingProfileResponse(traceId, accounts, jsonResponse);

		default: 
			jsonResponse = setupFailResponse(traceId);
			return jsonResponse;
		

		}

	}

	private BillingProfileResponse getTelbizBillingProfileResponse(String traceId, List<AccountRef> accounts, BillingProfileResponse jsonResponse) {
		LOGGER.info("getTelbizBillingProfileResponse Request: accounts=[{}] | jsonResponse=[{}] | traceId=[{}]", accounts, jsonResponse, traceId);
		try {
			List<BillingAccount> accountsList = new ArrayList<>();
			List<String> connRefListVolte = new ArrayList<>();
			List<String> contractListVolte = new ArrayList<>();
			List<BillingAccount> billBalanceListCcbs = null;
			String connRef="";
			String contractNo;

			for (AccountRef accountRef : accounts) {
				LOGGER.info("getTelbizBillingProfileResponse looping accounts : accountRef=[{}] | traceId=[{}]", accountRef, traceId);
				if (accountRef.getConnRef() != null && !accountRef.getConnRef().isEmpty()) {
					if (accountRef.getConnRef().matches(Constants.TELBIZ_CONN_REF_VALIDATOR)) {
						connRef = 0 + accountRef.getConnRef();
					} else if (accountRef.getConnRef().matches(Constants.TELBIZ_VALID_CONN_REF_VALIDATOR)) {
						connRef = accountRef.getConnRef();
					}
					LOGGER.info("getTelbizBillingProfileResponse format Telbiz connRef : connRef=[{}] | traceId=[{}]", connRef, traceId);

					String connRefWithoutStaringZero = connRef.startsWith("0") ? connRef.substring(1) : connRef;
					if (accountRef.getProductType()!=null && accountRef.getProductType()==Constants.PT_VOLTE) {
						FixedNumberDTO fixedNumberDetailsDTO = persistanceIntegrationService.validateCcbsAvailablityForConnRef(connRef, traceId);
						if (fixedNumberDetailsDTO != null) {
							String crmSystem = fixedNumberDetailsDTO.getCrmSystem();
							LOGGER.info("getTelbizBillingProfileResponse VOLTE PT : crmSystem=[{}] | traceId=[{}]", crmSystem, traceId);
							if (("CCBS".equalsIgnoreCase(crmSystem) || "DCS".equalsIgnoreCase(crmSystem))) {
								connRefListVolte.add(connRefWithoutStaringZero);
							}
						}
					} else if (accountRef.getProductType()!=null && accountRef.getProductType()==Constants.PT_DCS){
						LOGGER.info("getTelbizBillingProfileResponse DCS PT : traceId=[{}]", traceId);
						connRefListVolte.add(connRefWithoutStaringZero);
					}

				} else if (accountRef.getContractNo() != null && !accountRef.getContractNo().isEmpty()) {
					contractNo=accountRef.getContractNo();
					LOGGER.info("getTelbizBillingProfileResponse using contractNo path : contractNo=[{}], traceId=[{}]", contractNo, traceId);
					if (accountRef.getProductType() != null && accountRef.getProductType() == Constants.PT_VOLTE) {
						LOGGER.info("getTelbizBillingProfileResponse VOLTE PT : traceId=[{}]", traceId);
						if (contractNo.matches("[0-9]+")) {
							LOGGER.info("getTelbizBillingProfileResponse contractNo only contains digits: traceId=[{}]", traceId);
							FixedNumberDTO fixedNumberDetailsDTO = persistanceIntegrationService.validateCcbsAvailablityForContract(contractNo, traceId);
							if (fixedNumberDetailsDTO != null) {
								String crmSystem = fixedNumberDetailsDTO.getCrmSystem();
								LOGGER.info("getTelbizBillingProfileResponse using contractNo path : crmSystem=[{}] | traceId=[{}]", crmSystem, traceId);
								if ("CCBS".equalsIgnoreCase(crmSystem)) {
									contractListVolte.add(contractNo);
								}
							}
						}
					} else if (accountRef.getProductType() != null && accountRef.getProductType() == Constants.PT_DCS){
						LOGGER.info("getTelbizBillingProfileResponse using contractNo path DCS PT : traceId=[{}]", traceId);
						contractListVolte.add(accountRef.getContractNo());
					}
				}
			}

			if (!connRefListVolte.isEmpty() || !contractListVolte.isEmpty()) {
				LOGGER.info("getTelbizBillingProfileResponse VOLTE connRef or contract list is not empty : traceId=[{}]", traceId);
				billBalanceListCcbs = processBillAccountInfo(connRefListVolte, contractListVolte, traceId);
			}

			if (billBalanceListCcbs != null && !billBalanceListCcbs.isEmpty()) {
				accountsList = billBalanceListCcbs;
				LOGGER.info("getTelbizBillingProfileResponse billBalanceListCcbs is neither null nor empty : accountsList=[{}] | traceId=[{}]", accountsList, traceId);
			}

			jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
			jsonResponse.setAccounts(accountsList);

		} catch (Exception ex) {
			LOGGER.error(traceId + Constants.ERR_LOG_CODE, ex);
			jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_FAIL.getErrorCode());
			jsonResponse.setErrorDesc(ex.getMessage());
		}
		LOGGER.info("getTelbizBillingProfileResponse Response: jsonResponse=[{}] | traceId=[{}]", jsonResponse, traceId);
		return jsonResponse;
	}

	private List<BillingAccount> processBillAccountInfo(List<String> mobileList, List<String> contractList, String traceId) {
		LOGGER.info("processBillAccountInfo Request: mobileList=[{}] | contractList=[{}] | traceId=[{}]", mobileList, contractList, traceId);
		List<BillingAccount> balanceList = new ArrayList<>();
		String mobile;
		String contractID;
		try {
			if (contractList != null && !contractList.isEmpty()) {
				for (String contractNo : contractList) {
					LOGGER.info("processBillAccountInfo looping contractList : contractNo=[{}] | traceId=[{}]", contractNo, traceId);
					contractID = contractNo;
					if (contractID.matches(Constants.WIFI_NO_PATTERN) && contractID.length() == 8) {
						LOGGER.info("processBillAccountInfo WIFI number : traceId=[{}]", traceId);
						List<Profile> profileList = persistanceIntegrationService.getWifiContractIdOrMobile(null, contractNo, traceId);
						mobile = profileList.get(0).getAccounts().get(0).getConnRef();
						balanceList.add(getBillingAccountList(mobile, contractID, traceId));
					} else {
						BillingAccount billingAccount = null;
						if (contractNo.matches(Constants.INTEGER_VALIDATOR)) {
							//ccbs-volte
							LOGGER.info("processBillAccountInfo ccbs-volte : traceId=[{}]", traceId);
							FixedNumberDTO fixedNumberDetailsDTO = persistanceIntegrationService.validateCcbsAvailablityForContract(contractNo, traceId);
							if (fixedNumberDetailsDTO != null) {
								String crmSystem = fixedNumberDetailsDTO.getCrmSystem();
								LOGGER.info("processBillAccountInfo ccbs-volte : crmSystem=[{}] | traceId=[{}]", crmSystem, traceId);
								if ("CCBS".equalsIgnoreCase(crmSystem)) {
									billingAccount=getBillingAccountList(fixedNumberDetailsDTO.getMobileNo(), contractID, traceId);
								}
							}
						}
						// dcs
						List<DcsProfile> dcsMobileOfContractId = persistanceIntegrationService.getDcsMobileOfContractId(contractID, traceId);
						if (dcsMobileOfContractId != null) {
							LOGGER.info("processBillAccountInfo DcsProfile not null : dcsMobileOfContractId=[{}] | traceId=[{}]", dcsMobileOfContractId, traceId);
							for (DcsProfile dcsProfile : dcsMobileOfContractId) {
								LOGGER.info("processBillAccountInfo looping dcs profiles : dcsProfile=[{}] | traceId=[{}]", dcsProfile, traceId);
								if (dcsProfile.getMainNumber() != null && "Y".equals(dcsProfile.getMainNumber())) {
									LOGGER.info("processBillAccountInfo dcs profiles main number : traceId=[{}]", traceId);
									billingAccount=getBillingAccountList(dcsProfile.getMobileNumber(), dcsProfile.getContractId(), traceId);
								} else {
									LOGGER.info("processBillAccountInfo not dcs profiles main number : traceId=[{}]", traceId);
									billingAccount = new BillingAccount();
									billingAccount.setConnRef(dcsProfile.getMobileNumber());
									billingAccount.setContractNo(dcsProfile.getContractId());
									billingAccount.setLastBill(0.D);
									billingAccount.setTotalOust(0.D);
									billingAccount.setAccountType(null);
									billingAccount.setHybridFlag(null);
									billingAccount.setConStatus(null);
									billingAccount.setProductType(null);
									billingAccount.setSbu(null);
								}
							}
						}
						/*
						Returning the mobile no with leading 0 for DCS and VOLTE numbers
						 */
						mobile = billingAccount.getConnRef();
						billingAccount.setConnRef(!mobile.startsWith("0")? "0"+mobile : mobile);
						balanceList.add(billingAccount);
					}
				}
			} else if (mobileList != null && !mobileList.isEmpty()) {
				for (String mobileNo : mobileList) {
					LOGGER.info("processBillAccountInfo mobile no path looping : mobileNo=[{}] | traceId=[{}]", mobileNo, traceId);
					BillingAccount billingAccount;
					if (mobileNo.matches(Constants.CCBS_WIFI_CONN_REF_VALIDATOR)) {
						LOGGER.info("processBillAccountInfo wifi conn ref : traceId=[{}]", traceId);
						mobile = mobileNo;
						List<Profile> profileList = persistanceIntegrationService.getWifiContractIdOrMobile(mobile, null, traceId);
						Account account = profileList.get(0).getAccounts().get(0);
						contractID = account.getContractNo();
						billingAccount = getBillingAccountList(mobile, contractID, traceId);
					} else {
						/*
						Returning the mobile no with leading 0 for DCS and VOLTE numbers
						 */
						LOGGER.info("processBillAccountInfo DCS and VOLTE mobile numbers : traceId=[{}]", traceId);
						mobile = mobileNo;
						contractID = persistanceIntegrationService.getContractIdOfMobile(mobile, traceId);
						billingAccount = getBillingAccountList(mobile, contractID, traceId);
						billingAccount.setConnRef(!mobile.startsWith("0") ? "0" + mobile : mobile);
					}
					balanceList.add(billingAccount);
				}
			}

		} catch (Exception ex) {
			LOGGER.error(traceId + Constants.ERR_LOG_CODE, ex);
		}
		LOGGER.info("processBillAccountInfo Response: balanceList=[{}] | traceId=[{}]", balanceList, traceId);
		return balanceList;
	}

	private BillingAccount getBillingAccountList(String connRef, String contractID, String traceId) {
		LOGGER.info("getBillingAccountList Request: connRef=[{}] | contractID=[{}] | traceId=[{}]", connRef, contractID, traceId);
		//should pass contractId with relevant mobileNo
		BillingAccount account = new BillingAccount();
		if (contractID != null && !contractID.isEmpty()) {
			try {
				RBMAccountDTO accountDTO = mifeIntegrationService.queryBalanceForAccount(contractID, traceId);
				account.setConnRef(connRef);
				account.setContractNo(contractID);
				account.setLastBill(accountDTO.getLastBill());
				account.setTotalOust(accountDTO.getTotalOust());
				account.setAccountType(null);
				account.setHybridFlag(null);
				account.setConStatus(null);
				account.setProductType(null);
				account.setSbu(null);
			} catch (Exception ex) {
				LOGGER.error(traceId + Constants.ERR_LOG_CODE, ex);
			}
		}
		LOGGER.info("getBillingAccountList Response: account=[{}] | traceId=[{}]", account, traceId);
		return account;
	}

	private BillingProfileResponse getCcbsBillingProfileResponse(List<AccountRef> accounts, BillingProfileRequest billingProfileRequest, BillingProfileResponse jsonResponse, String traceId) throws DCPEException {
		List<BillingAccount> accountsList = new ArrayList<>();
		for (AccountRef accountRef : accounts) {
			if (!accountRef.getProductType().equals(ProductTypeEnum.VOLTE.getType())) {
				this.addAccountsOfVolte(accountRef, accountsList, billingProfileRequest, traceId);
			} else {
				throw new DCPEException("Invalid Product Type.");
			}
		}
		jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
		jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
		jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
		jsonResponse.setAccounts(accountsList);
		return jsonResponse;
	}

	void addAccountsOfVolte(AccountRef accountRef, List<BillingAccount> accountsList, BillingProfileRequest billingProfileRequest, String traceId) {
		if (accountRef.getContractNo() != null && !accountRef.getContractNo().isEmpty()) {
			try {
				this.addCcbsAccountsByContactNo(accountRef, accountsList, traceId);
			} catch (Exception ex) {
				LOGGER.error("addAccountsOfVolteResponse: traceId={}|TrxID={}|ErrorCode={}|Error={}", traceId, billingProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
			}
		} else if (accountRef.getConnRef() != null && !accountRef.getConnRef().isEmpty()) {
			try {
				this.addCcbsAccountsByConnRef(accountRef, accountsList, traceId);
			} catch (Exception e) {
				LOGGER.error("addAccountsOfVolteResponse: traceId={}|TrxID={}|ErrorCode={}|Error={}", traceId, billingProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), e);
			}
		}
	}

	private void addCcbsAccountsByContactNo(AccountRef accountRef, List<BillingAccount> accountsList, String traceId) throws DCPEException {
		String mobile;
		if (accountRef.getContractNo()
				.matches(ValidationPatternEnum.WIFI_NO_PATTERN.getPattern())) {
			List<Profile> profileList = persistanceIntegrationService
					.getWifiContractIdOrMobile(null, accountRef.getContractNo(), traceId);
			mobile = profileList.get(0).getAccounts().get(0).getConnRef();
		} else {
			mobile = persistanceIntegrationService
					.getMobileOfContractId(accountRef.getContractNo(), traceId);
		}
		
		SBUEnum sbu = SBUEnum.getSBUByValue(accountRef.getSbu());
		BillingAccount account = new BillingAccount();
		RBMAccountDTO acountDTO = mifeIntegrationService
				.rbmQueryBalanceForAccount(accountRef.getContractNo(),mobile,sbu,traceId);
		account.setConnRef(mobile);
		account.setContractNo(accountRef.getContractNo());
		account.setLastBill(acountDTO.getLastBill());
		account.setTotalOust(acountDTO.getTotalOust());
		account.setAccountType(null);
		account.setHybridFlag(null);
		account.setConStatus(null);
		account.setProductType(null);
		account.setSbu(null);
		accountsList.add(account);
	}

	private void addCcbsAccountsByConnRef(AccountRef accountRef, List<BillingAccount> accountsList, String traceId) throws DCPEException {
		String contractID;
		if (accountRef.getConnRef()
				.matches(ValidationPatternEnum.CCBS_WIFI_CONN_REF_VALIDATOR.getPattern())) {
			List<Profile> profileList = persistanceIntegrationService
					.getWifiContractIdOrMobile(accountRef.getConnRef(), null, traceId);
			Account account = profileList.get(0).getAccounts().get(0);
			contractID = account.getContractNo();
		} else {
			contractID = persistanceIntegrationService
					.getContractIdOfMobile(accountRef.getConnRef(), traceId);
		}

		BillingAccount account = new BillingAccount();
		
		SBUEnum sbu = SBUEnum.getSBUByValue(accountRef.getSbu());
		
		RBMAccountDTO acountDTO = mifeIntegrationService
				.rbmQueryBalanceForAccount(contractID,accountRef.getConnRef(),sbu,traceId);

		account.setConnRef(accountRef.getConnRef());
		account.setContractNo(contractID);
		account.setLastBill(acountDTO.getLastBill());
		account.setTotalOust(acountDTO.getTotalOust());
		account.setAccountType(null);
		account.setHybridFlag(null);
		account.setConStatus(null);
		account.setProductType(null);
		account.setSbu(null);
		accountsList.add(account);
	}

	private BillingProfileResponse getNfcBillingProfileResponse(List<AccountRef> accounts, BillingProfileRequest billingProfileRequest, BillingProfileResponse jsonResponse, String traceId) throws DCPEException {
		List<BillingAccount> accountsList = new ArrayList<>();
		for (AccountRef accountRef : accounts) {
			if (accountRef.getContractNo() != null && !accountRef.getContractNo().isEmpty()) {
				try {
					BillingAccount account = new BillingAccount();
					
					SBUEnum sbu = SBUEnum.getSBUByValue(accountRef.getSbu());
					
					RBMAccountDTO acountDTO = mifeIntegrationService
							.rbmQueryBalanceForAccount(accountRef.getContractNo(),accountRef.getConnRef(),sbu,traceId);

					account.setContractNo(accountRef.getContractNo());
					account.setLastBill(acountDTO.getLastBill());
					account.setTotalOust(acountDTO.getTotalOust());
					accountsList.add(account);

				} catch (Exception e) {
					LOGGER.error("getNfcBillingProfileResponse: traceId={}|TrxID={}|ErrorCode={}|Error={}", traceId, billingProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), e);
					throw new DCPEException(ErrorCodeEnum.ERR_LOG_CODE.getErrorCode());
				}
			}
		}
		jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
		jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
		jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
		jsonResponse.setAccounts(accountsList);
		return jsonResponse;
	}
	
	private BillingProfileResponse setupFailResponse(String traceId) {
		BillingProfileResponse jsonResponse= new BillingProfileResponse();
		jsonResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
		jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_INVALID_PC.getErrorCode());
		jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_INVALID_PC.getErrorCode());
		Instant start = Instant.now();
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(jsonResponse);
		LOGGER.info("setupFailResponse : traceId={}|timeTaken={}|responseString={}", traceId, timeTaken, responseString);
		return jsonResponse;
	}

}
