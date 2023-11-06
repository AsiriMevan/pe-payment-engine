package lk.dialog.pe.billing.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.Map;
import lk.dialog.pe.billing.domain.DcsProfile;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.credit.cam.dto.FixedNumberDTO;
import lk.dialog.pe.credit.cam.dto.MinimumPayResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.dialog.pe.billing.domain.Account;
import lk.dialog.pe.billing.domain.AccountRef;
import lk.dialog.pe.billing.domain.Balance;
import lk.dialog.pe.billing.domain.BillBalance;
import lk.dialog.pe.billing.domain.PEProfileRequest;
import lk.dialog.pe.billing.domain.PEProfileResponse;
import lk.dialog.pe.billing.domain.Profile;
import lk.dialog.pe.billing.domain.RBMAccountDTO;
import lk.dialog.pe.billing.exception.InvalidContractNoException;
import lk.dialog.pe.billing.service.MifeIntegrationService;
import lk.dialog.pe.billing.service.PersistanceIntegrationService;
import lk.dialog.pe.billing.service.QueryPEProfileService;
import lk.dialog.pe.common.util.DCPEErrorEnum;
import lk.dialog.pe.common.util.ErrorCodeEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.billing.util.ValidationPatternEnum;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.credit.cam.dto.AccountBalanceDTO;

@Service
public class QueryPEProfileServiceImpl implements QueryPEProfileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryPEProfileServiceImpl.class);

	@Autowired
	private PersistanceIntegrationService persistanceIntegrationService;

	@Autowired
	private MifeIntegrationService mifeIntegrationService;

	private String VOLTE_CONN_REF = "VOLTE_CONN_REF";
	private String VOLTE_CONTRACT = "VOLTE_CONTRACT";
	private String TELBIZ_CONN_REF = "TELBIZ_CONN_REF";
	private String TELBIZ_CONTRACT = "TELBIZ_CONTRACT";

//todo - log coverage in this class is very poor
	@Override
	public PEProfileResponse queryPEProfile(PEProfileRequest peProfileRequest, String traceId)
			throws Exception {

		PEProfileResponse queryResponse;
		List<Balance> balanceList = new ArrayList<>();
		List<AccountRef> accounts = peProfileRequest.getAccounts();

		ProductCategoryEnum productCategory = ProductCategoryEnum
				.getProductCategoryByValue(peProfileRequest.getProductCategory());

		switch (productCategory) {

			case CCBS:
				queryResponse = setupCCBS(peProfileRequest, accounts, balanceList, traceId);
				break;

			case NFC:
				queryResponse = setupNFC(peProfileRequest, accounts, balanceList, traceId);
				break;

			case TELBIZ:
				//todo - to test - [pe-bll/src/main/java/lk/dialog/crm/pe/bll/service/impl/ProfileQueryServiceImpl.java:1345]
				queryResponse = setupTELBIZ(accounts, balanceList, traceId);
				break;
			default:
				queryResponse = setupFailStatus();

		}
		return queryResponse;
	}

	private PEProfileResponse setupCCBS(PEProfileRequest peProfileRequest, List<AccountRef> accounts,
			List<Balance> balanceList, String traceId) throws DCPEException, IOException {
		PEProfileResponse queryResponse = new PEProfileResponse();
		List<Balance> balanceInfoList = new ArrayList<>();
		for (AccountRef accountRef : accounts) {
			String mobile = null;
			String contractNo = null;

			try {
				List<String> mobileAndContractNo = this.getUpMobileAndContractNo(accountRef, traceId);
				mobile = mobileAndContractNo.get(0);
				contractNo = mobileAndContractNo.get(1);

//					if (mobile == null)
//						continue;
			} catch (Exception ex) {
				LOGGER.error("setupCCBSResponse: traceId={}|TrxID={}|ErrorCode={}|Error={}", traceId,
						peProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
			}

			try {
				balanceList = setBalanceList(accountRef.getSbu(), mobile, contractNo,
						accountRef.getConnRef(), balanceInfoList, traceId);
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}

		}
		queryResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
		queryResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
		queryResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
		queryResponse.setAccounts(balanceList);
		return queryResponse;
	}

	List<String> getUpMobileAndContractNo(AccountRef accountRef, String traceId)
			throws DCPEException {
		String mobile = null;
		String contractNo = null;

		//accountRef.setContractNo(null);

		if (accountRef.getContractNo() != null && !accountRef.getContractNo().isEmpty()) {
			contractNo = accountRef.getContractNo();
			if (accountRef.getContractNo().matches(ValidationPatternEnum.WIFI_NO_PATTERN.getPattern())) {
				List<Profile> profileList = persistanceIntegrationService.getWifiContractIdOrMobile(null,
						contractNo, traceId);
				if (!profileList.isEmpty()) {
					mobile = profileList.get(0).getAccounts().get(0).getConnRef();
				}
			} else {
				mobile = persistanceIntegrationService.getMobileOfContractId(contractNo, traceId);
			}

		} else if (accountRef.getConnRef() != null && !accountRef.getConnRef().isEmpty()) {
			if (accountRef.getConnRef()
					.matches(ValidationPatternEnum.CCBS_WIFI_CONN_REF_VALIDATOR.getPattern())) {
				mobile = accountRef.getConnRef();
				List<Profile> profileList = persistanceIntegrationService.getWifiContractIdOrMobile(mobile,
						null, traceId);
				Account account = profileList.get(0).getAccounts().get(0);
				contractNo = account.getContractNo();

			} else {
				mobile = accountRef.getConnRef();
				contractNo = persistanceIntegrationService.getContractIdOfMobile(mobile, traceId);
			}

		}
		return Arrays.asList(mobile, contractNo);
	}

	private PEProfileResponse setupFailStatus() {
		PEProfileResponse queryResponse = new PEProfileResponse();
		queryResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
		queryResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_INVALID_PC.getErrorCode());
		queryResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_INVALID_PC.getErrorCode());
		return queryResponse;
	}

	private PEProfileResponse setupNFC(PEProfileRequest peProfileRequest, List<AccountRef> accounts,
			List<Balance> balanceList, String traceId) {
		PEProfileResponse queryResponse = new PEProfileResponse();
		for (AccountRef accountRef : accounts) {
			try {
				RBMAccountDTO acountDTO = mifeIntegrationService.queryBalanceForAccount(
						accountRef.getContractNo(),
						traceId);

				BillBalance balanceInfo = new BillBalance();
				balanceInfo.setContractNo(accountRef.getContractNo());
				balanceInfo.setConnRef(accountRef.getContractNo());
				balanceInfo.setMinAmtToConnect(0.00);
				balanceInfo.setReconFee(0.00);
				balanceInfo.setTotalOS(acountDTO.getTotalOust());
				balanceInfo.setInterimUsage(0.00);
				balanceList.add(balanceInfo);

			} catch (Exception e) {
				LOGGER.error("setupNFCResponse: traceId={}|TrxID={}|ErrorCode={}|Error={}", traceId,
						peProfileRequest.getTrxID(), ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), e);
				queryResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
				queryResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_FAIL.getErrorCode());
				queryResponse.setErrorDesc(e.getMessage() + " " + e.getCause());
				return queryResponse;
			}
		}
		queryResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
		queryResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
		queryResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
		queryResponse.setAccounts(balanceList);
		return queryResponse;

	}

	private List<Balance> setBalanceList(Integer sbuValue, String mobile, String contractNo,
			String connRef,
			List<Balance> balanceInfoList, String traceId) throws DCPEException {
		List<Balance> balanceList = new ArrayList<>();

		try {
			LOGGER.info("setBalanceListResponse: list={}|traceID={}", balanceList, traceId);

			SBUEnum sbu = null;
			if (sbuValue != null) {
				sbu = SBUEnum.getSBUByValue(sbuValue);
			} else {
				throw new DCPEException("SBU is missing or unknown value received.");
			}

			balanceList = setupBalanceInfo(sbu, mobile, contractNo, connRef, balanceInfoList, traceId);

		} catch (DCPEException ex) {
			throw new DCPEException(ex.getMessage(), ex);
		} catch (Exception e) {
			String errorMessage = "{0} traceId={1}".replace("{0}", e.getMessage())
					.replace("{1}", traceId);
			throw new DCPEException(e.getMessage().concat(errorMessage), e);

		}
		return balanceList;
	}

	private List<Balance> setupBalanceInfo(SBUEnum sbu, String mobile, String contractNo,
			String connref,
			List<Balance> balanceInfoList, String traceId)
			throws DCPEException, InvalidContractNoException, IOException {

		if (mobile != null ? mobile.matches(
				ValidationPatternEnum.CCBS_WIFI_CONN_REF_VALIDATOR.getPattern())
				: false || contractNo != null ? contractNo.matches(
						ValidationPatternEnum.WIFI_NO_PATTERN.getPattern()) : false) {

			try {
				RBMAccountDTO acountDTO = mifeIntegrationService.queryBalanceForAccount(contractNo,
						traceId);

				BillBalance balanceInfo = new BillBalance();
				balanceInfo.setContractNo(contractNo);
				balanceInfo.setConnRef(connref);
				balanceInfo.setMinAmtToConnect(0.00);
				balanceInfo.setReconFee(0.00);
				balanceInfo.setTotalOS(acountDTO.getTotalOust());
				balanceInfo.setInterimUsage(0.00);
				balanceInfoList.add(balanceInfo);
			} catch (Exception e) {
				throw new IOException(e.getMessage());
			}


		} else {

			AccountBalanceDTO balanceDTO = mifeIntegrationService.queryOustandingBalance(mobile,
					sbu.name().startsWith("DTV") ? "DTV" : sbu.name(), "FALSE", traceId);

			if (balanceDTO.getExecustionStatus() != null && "00".equals(
					balanceDTO.getExecustionStatus())) {

				BillBalance balanceInfo = new BillBalance();
				balanceInfo.setContractNo(contractNo);
				balanceInfo.setConnRef(mobile);
				balanceInfo.setMinAmtToConnect(balanceDTO.getMinPayment());
				balanceInfo.setReconFee(balanceDTO.getDtvReconFee());
				balanceInfo.setTotalOS(balanceDTO.getOutstanding());
				balanceInfo.setInterimUsage(balanceDTO.getRunningBill());
				balanceInfoList.add(balanceInfo);
			}
		}
		return balanceInfoList;
	}

	private PEProfileResponse setupTELBIZ(List<AccountRef> accounts, List<Balance> balanceList, String traceId) throws Exception {
		LOGGER.info("setupTELBIZ Request: traceId=[{}]", traceId);
		List<String> mobileList = new ArrayList<>();
		List<String> contractList = new ArrayList<>();
		PEProfileResponse queryResponse = new PEProfileResponse();
		for (AccountRef accountRef : accounts) {
			if (accountRef.getConnRef() != null && !accountRef.getConnRef().isEmpty()) {
				if (accountRef.getConnRef().matches(Constants.TELBIZ_CONN_REF_VALIDATOR)) {
					mobileList.add(0 + accountRef.getConnRef());
				} else if (accountRef.getConnRef().matches(Constants.TELBIZ_VALID_CONN_REF_VALIDATOR)) {
					mobileList.add(accountRef.getConnRef());
				}

			} else if (accountRef.getContractNo() != null && !accountRef.getContractNo().isEmpty()) {
				contractList.add(accountRef.getContractNo());
			}
		}

		List<String> connRefListVolte = new ArrayList<>();
		List<String> contractListVolte = new ArrayList<>();
		List<BillBalance> billBalanceListCcbs = null;
		List<BillBalance> billBalanceList = null;

		LOGGER.info("setupTELBIZ: Before filter contract no & mobile no | traceId=[{}] | mobileList=[{}] | contractList=[{}]", traceId, mobileList, contractList);
		Map<String, List<String>> mapContractAndConnection = filterContractAndConnection(contractList, mobileList, traceId);

		for (Map.Entry<String, List<String>> connRefContractEntry : mapContractAndConnection.entrySet()) {
			if (connRefContractEntry.getKey().equalsIgnoreCase(VOLTE_CONN_REF)) {
				connRefListVolte = connRefContractEntry.getValue();
			} else if (connRefContractEntry.getKey().equalsIgnoreCase(VOLTE_CONTRACT)) {
				contractListVolte = connRefContractEntry.getValue();
			}
        }

		if ((connRefListVolte != null && !connRefListVolte.isEmpty()) || (contractListVolte != null
				&& !contractListVolte.isEmpty())) {
			billBalanceListCcbs = processBillBalanceInfo(connRefListVolte, contractListVolte, traceId);
		}

        if (billBalanceListCcbs != null && !billBalanceListCcbs.isEmpty()) {
            billBalanceList = billBalanceListCcbs;
        }

        balanceList.addAll(billBalanceList);
		queryResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
		queryResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
		queryResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
		queryResponse.setAccounts(balanceList);

		LOGGER.info("setupTELBIZ Response: traceId=[{}] | queryResponse=[{}]", traceId, queryResponse);
		return queryResponse;
	}


	private Map<String, List<String>> filterContractAndConnection(List<String> contractList,
			List<String> mobileList, String traceId) throws Exception {
		LOGGER.info("filterContractAndConnection Request: traceId=[{}]", traceId);
		Map<String, List<String>> mapContractAndConnection = new HashMap<>();

		List<String> connRefListVolte = new ArrayList<>();
		List<String> contractListVolte = new ArrayList<>();
		List<String> connRefListTelbiz = new ArrayList<>();
		List<String> contractListTelbiz = new ArrayList<>();

		if (contractList != null && !contractList.isEmpty()) {
			for (String contractNo : contractList) {
				if (contractNo.matches("[0-9]+")) {
					FixedNumberDTO fixedNumberDetailsDTO = persistanceIntegrationService.validateCcbsAvailablityForContract(
							contractNo, traceId);
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
				FixedNumberDTO fixedNumberDetailsDTO = persistanceIntegrationService.validateCcbsAvailablityForConnRef(
						mobileNo, traceId);
				if (fixedNumberDetailsDTO != null) {
					String crmSystem = fixedNumberDetailsDTO.getCrmSystem();
					if ("CCBS".equalsIgnoreCase(crmSystem) || "DCS".equalsIgnoreCase(crmSystem)) {
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

		LOGGER.info("filterContractAndConnection Response: traceId=[{}] | mapContractAndConnection=[{}]", traceId, mapContractAndConnection);
		return mapContractAndConnection;
	}

	private List<BillBalance> processBillBalanceInfo(List<String> mobileList,
			List<String> contractList, String traceId) {
		LOGGER.info("processBillBalanceInfo Request: traceId=[{}] | mobileList=[{}] | contractList=[{}]", traceId, mobileList, contractList);
		List<BillBalance> balanceList = new ArrayList<>();
		String mobile;
		String contractID;
		try {
			if (contractList != null && !contractList.isEmpty()) {
				for (String contractNo : contractList) {
					mobile = null;
					contractID = contractNo;
					if (contractID.matches(Constants.WIFI_NO_PATTERN)) {
						LOGGER.info("processBillBalanceInfo contractID matches WIFI_NO_PATTERN: traceId=[{}] | contractID=[{}]", traceId, contractID);
						List<Profile> profileList = persistanceIntegrationService.getWifiContractIdOrMobile(
								null, contractID, traceId);
						mobile = profileList.get(0).getAccounts().get(0).getConnRef();
						balanceList.add(getBalanceList(mobile, contractID, traceId));
					} else {
						if (contractNo.matches(Constants.INTEGER_VALIDATOR)) {
							LOGGER.info("processBillBalanceInfo contractID only has digits: traceId=[{}] | contractNo=[{}]", traceId, contractNo);
							/*
							Returning mobile no with leading 0 for VOLTE numbers
							 */
							mobile = persistanceIntegrationService.getMobileOfContractId(contractID, traceId);
							BillBalance volteBillBalance = getBalanceList(mobile, contractID, traceId);
							volteBillBalance.setConnRef(!mobile.startsWith("0") ? "0" + mobile : mobile);
							balanceList.add(volteBillBalance);
						}
					/* For DCS Contracts -> List all the sub numbers of the contract and display balance information only for the Main Number,
						Return mobile no with leading 0
					 */
						if (mobile == null || mobile.isEmpty()) {
							LOGGER.info("processBillBalanceInfo: mobile == null || mobile.isEmpty(): traceId=[{}]", traceId);
							List<DcsProfile> mobileNos = persistanceIntegrationService.getDcsMobileOfContractId(contractID, traceId);
							if (mobileNos != null) {
								for (DcsProfile dcsProfile : mobileNos) {
									LOGGER.info("processBillBalanceInfo: traceId=[{}] | dcsProfile=[{}]", traceId, dcsProfile);
									mobile = dcsProfile.getMobileNumber();
									BillBalance balanceInfo;
									if (dcsProfile.getMainNumber() != null && "Y".equals(
											dcsProfile.getMainNumber())) {
										LOGGER.info("processBillBalanceInfo: main dcs number: traceId=[{}] | mobile=[{}]", traceId, mobile);
										balanceInfo = getBalanceList(mobile, contractID, traceId);
									} else {
										LOGGER.info("processBillBalanceInfo: not the main dcs number: traceId=[{}] | mobile=[{}]", traceId, mobile);
										balanceInfo = new BillBalance();
										balanceInfo.setContractNo(contractID);
										balanceInfo.setConnRef(mobile);
										balanceInfo.setMinAmtToConnect(0.00);
										balanceInfo.setReconFee(0.00);
										balanceInfo.setTotalOS(0.00);
										balanceInfo.setInterimUsage(0.00);

									}
									balanceInfo.setConnRef(!mobile.startsWith("0") ? "0" + mobile : mobile);
									balanceList.add(balanceInfo);
								}
							}
						}
					}
				}
			} else if (mobileList != null && !mobileList.isEmpty()) {
				for (String mobileNo : mobileList) {
					BillBalance billBalance;
					if (mobileNo.matches(Constants.CCBS_WIFI_CONN_REF_VALIDATOR)) {
						LOGGER.info("processBillBalanceInfo mobileNo matches CCBS_WIFI_CONN_REF_VALIDATOR: traceId=[{}] | mobileNo=[{}]", traceId, mobileNo);
						mobile = mobileNo;
						List<Profile> profileList = persistanceIntegrationService.getWifiContractIdOrMobile(mobile, null, traceId);
						Account account = profileList.get(0).getAccounts().get(0);
						contractID = account.getContractNo();
						billBalance = getBalanceList(mobile, contractID, traceId);
					} else {
						LOGGER.info("processBillBalanceInfo mobileNo not matched w/CCBS_WIFI_CONN_REF_VALIDATOR: traceId=[{}] | mobileNo=[{}]", traceId, mobileNo);
						/*
						Returning mobile no with leading 0 for DCS and VOLTE Numbers
						 */
						mobile = mobileNo;
						contractID = persistanceIntegrationService.getContractIdOfMobile(mobile, traceId);
						billBalance = getBalanceList(mobile, contractID, traceId);
						billBalance.setConnRef(!mobile.startsWith("0") ? "0"+mobile : mobile);
					}
					balanceList.add(billBalance);
				}
			}

		} catch (Exception ex) {
			LOGGER.error(traceId + Constants.ERR_LOG_CODE, ex);
		}
		LOGGER.info("processBillBalanceInfo Response: traceId=[{}] | balanceList=[{}]", traceId, balanceList);
		return balanceList;
	}



	private BillBalance getBalanceList(String connRef, String contractID, String traceId) throws Exception {
		LOGGER.info("getBalanceList Request: traceId=[{}] | connRef=[{}] | contractID=[{}]", traceId, connRef, contractID);
		try {

			if (connRef != null ? connRef.matches(Constants.CCBS_WIFI_CONN_REF_VALIDATOR) : (contractID != null) && contractID.matches(Constants.WIFI_NO_PATTERN)) {
				LOGGER.info("Query for WIFI account: traceId=[{}]", traceId);
				RBMAccountDTO acountDTO = mifeIntegrationService.queryBalanceForAccount(contractID, traceId);

				BillBalance balanceInfo = new BillBalance();
				balanceInfo.setContractNo(contractID);
				balanceInfo.setConnRef(connRef);
				balanceInfo.setMinAmtToConnect(0.00);
				balanceInfo.setReconFee(0.00);
				balanceInfo.setTotalOS(acountDTO.getTotalOust());
				balanceInfo.setInterimUsage(0.00);
				LOGGER.info("getBalanceList Response: traceId=[{}] | balanceInfo=[{}]", traceId, balanceInfo);
				return balanceInfo;
			} else {
				LOGGER.info("Query for Non-WIFI account: traceId=[{}]", traceId);
				AccountBalanceDTO balanceDTO = mifeIntegrationService.queryOustandingBalance(connRef, "DBN", "FALSE", traceId);
				LOGGER.info("BillInfo API status executionStatus=[{}] | traceId=[{}]", balanceDTO.getExecustionStatus(), traceId);
				BillBalance balanceInfo = new BillBalance();
				balanceInfo.setContractNo(contractID);
				balanceInfo.setConnRef(connRef);
				if (balanceDTO.getExecustionStatus() != null && "00".equals(balanceDTO.getExecustionStatus())) {
					balanceInfo.setReconFee(balanceDTO.getDtvReconFee());
					balanceInfo.setTotalOS(balanceDTO.getOutstanding());
					balanceInfo.setInterimUsage(balanceDTO.getRunningBill());
				}
				// Call a different Mife API to get MinPaymentToConnect since it is not returning from BillingInfo API
				MinimumPayResponse minPayment = mifeIntegrationService.queryMinimumPayment("MSISDN", connRef, traceId);
				LOGGER.info("GetMinimumPayment API status result=[{}] | traceId=[{}]", minPayment.getResult(), traceId);

				if(minPayment.getResult().equalsIgnoreCase("SUCCESS")) {
					balanceInfo.setMinAmtToConnect(minPayment.getMinimumPayment());
				}else{
					balanceInfo.setMinAmtToConnect(0.00);
				}

				LOGGER.info("getBalanceList Response: traceId=[{}] | balanceInfo=[{}]", traceId, balanceInfo);
				return balanceInfo;
			}
		} catch (DCPEException ex) {
			LOGGER.error("getBalanceList Response - DCPEException: traceId=[{}] | ::Error ->", traceId, ex);
			throw ex;
		} catch (Exception e) {
			LOGGER.error("getBalanceList Response - Exception: traceId=[{}] | ::Error ->", traceId, e);
			throw e;
		}

	}
}
