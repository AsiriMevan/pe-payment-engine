package lk.dialog.pe.billing.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.dialog.pe.billing.domain.AccountRef;
import lk.dialog.pe.billing.domain.ConnectionRef;
import lk.dialog.pe.billing.domain.OCSProfileRequest;
import lk.dialog.pe.billing.domain.RealTimeBalance;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.NEConstants;
import lk.dialog.pe.billing.service.MifeIntegrationService;
import lk.dialog.pe.billing.service.PersistanceIntegrationService;
import lk.dialog.pe.billing.service.QueryOCSProfileService;
import lk.dialog.pe.billing.util.CreditStatusEnum;
import lk.dialog.pe.common.util.DCPEErrorEnum;
import lk.dialog.pe.common.util.ErrorCodeEnum;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.billing.util.OCSProfileResponse;
import lk.dialog.pe.common.util.ProductTypeEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.credit.cam.dto.OCSStatusResponse;

@Service
public class QueryOCSProfileServiceImpl implements QueryOCSProfileService {


	@Autowired
	private PersistanceIntegrationService persistanceIntegrationService;

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryOCSProfileServiceImpl.class);

	@Autowired
	private MifeIntegrationService mifeIntegrationService;

	@Override
	public OCSProfileResponse queryOCSProfile(OCSProfileRequest ocsProfileRequest, String traceId) throws DCPEException {

		Instant start = Instant.now();
		String ocsresponseString = DCPEUtil.convertToString(ocsProfileRequest);
		LOGGER.info("queryOCSProfileRequest : traceId={}|OCSProfileRequest={}", traceId, ocsresponseString);

		OCSProfileResponse queryResponse = null;

		List<AccountRef> accounts = ocsProfileRequest.getAccounts();
		if(ocsProfileRequest.getProductCategory().equals(ProductCategoryEnum.CCBS.valueOf())) {
			List<Object> invalidAccountsAndBalanceList = this.addInvalidAccountsAndBalanceList(ocsProfileRequest, accounts, traceId);
			queryResponse = setupSuccessResponse((List<ConnectionRef>) invalidAccountsAndBalanceList.get(0), (List<RealTimeBalance>) invalidAccountsAndBalanceList.get(1));
		} else if (ocsProfileRequest.getProductCategory().equals(ProductCategoryEnum.TELBIZ.valueOf())) {
			LOGGER.info("queryOCSProfile PC - TelBiz: traceId=[{}]", traceId);
			List<Object> invalidAccountsAndBalanceListForTelbizProducts = this.invalidAccountsAndBalanceListForTelbizProducts(ocsProfileRequest, accounts, traceId);
			queryResponse = setupSuccessResponse((List<ConnectionRef>) invalidAccountsAndBalanceListForTelbizProducts.get(0), (List<RealTimeBalance>) invalidAccountsAndBalanceListForTelbizProducts.get(1));
		}else{
			queryResponse = setupFailResponse();
		}

		String responseString = DCPEUtil.convertToString(queryResponse);
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("queryOCSProfileResponse : timeTaken={}|traceId={}|body={}", end, traceId, responseString);
		return queryResponse;
	}

	List<Object> addInvalidAccountsAndBalanceList(OCSProfileRequest ocsProfileRequest, List<AccountRef> accounts, String traceId){
		List<RealTimeBalance> balanceList = new ArrayList<>();
		List<ConnectionRef> invalidAccounts = new ArrayList<>();
		for (AccountRef accountRef : accounts) {
			if ((!accountRef.getSbu().equals(SBUEnum.DTV_POSTPAID.valueOf()) && !accountRef.getProductType().equals(ProductTypeEnum.WIFI.getType()))) {
				try {
					List<String> mobileAndContractId = this.getMobileAndContactId(accountRef, traceId);
					if (mobileAndContractId.get(0) == null)
						continue;

					OCSStatusResponse realTimeBal = mifeIntegrationService.queryRealTimeBalance(mobileAndContractId.get(0), traceId);

					setupBalanceList(balanceList, realTimeBal, mobileAndContractId.get(1), mobileAndContractId.get(0));

				} catch (Exception ex) {
					LOGGER.error("addInvalidAccountsAndBalanceListResponse: " + ocsProfileRequest.getTrxID() + ErrorCodeEnum.ERR_LOG_CODE.getErrorCode() + "", ex);
				}
			} else {
				ConnectionRef connectionRef = new ConnectionRef();
				connectionRef.setConnRef(accountRef.getConnRef());
				connectionRef.setContractNo(accountRef.getContractNo());
				invalidAccounts.add(connectionRef);
			}
		}
		return Arrays.asList(invalidAccounts, balanceList);
	}

	private List<Object> invalidAccountsAndBalanceListForTelbizProducts(OCSProfileRequest ocsProfileRequest, List<AccountRef> accounts, String traceId) {
		LOGGER.info("invalidAccountsAndBalanceListForTelbizProducts Request : ocsProfileRequest=[{}] | accounts=[{}] | traceId=[{}]", ocsProfileRequest, accounts, traceId);
		List<RealTimeBalance> balanceList = new ArrayList<>();
		List<ConnectionRef> invalidAccounts = new ArrayList<>();
		for (AccountRef accountRef : accounts) {
			LOGGER.info("invalidAccountsAndBalanceListForTelbizProducts : accountRef=[{}] | traceId=[{}]", accountRef, traceId);
			if (accountRef.getSbu().equals(SBUEnum.DBN.valueOf()) && (accountRef.getProductType().equals(ProductTypeEnum.VOLTE.getType()) || accountRef.getProductType().equals(ProductTypeEnum.DCS.getType()))) {
				try {
					List<String> mobileAndContractId = this.getMobileAndContactIdForTelbizProducts(accountRef, traceId);
					/* For DCS numbers, mobile no is required to get the OCS Status, If not provided skip the process
					 */
					if (mobileAndContractId.get(0) == null)
						continue;
					OCSStatusResponse realTimeBal = mifeIntegrationService.queryRealTimeBalance(mobileAndContractId.get(0), traceId);
					LOGGER.info("OCSStatusInfo API status : ResCode=[{}] | Desc=[{}] | traceId=[{}]", realTimeBal.getResCode(), realTimeBal.getResDesc(), traceId);
					setupBalanceListTelBiz(balanceList, realTimeBal, mobileAndContractId.get(1), mobileAndContractId.get(0), traceId);
				} catch (Exception ex){
					LOGGER.error(ocsProfileRequest.getTrxID() + ErrorCodeEnum.ERR_LOG_CODE.getErrorCode(), ex);
				}
			} else {
				LOGGER.info("invalidAccountsAndBalanceListForTelbizProducts set invalid accounts: traceId=[{}]", traceId);
				ConnectionRef connectionRef = new ConnectionRef();
				connectionRef.setConnRef(accountRef.getConnRef());
				connectionRef.setContractNo(accountRef.getContractNo());
				invalidAccounts.add(connectionRef);
			}
		}
		LOGGER.info("invalidAccountsAndBalanceListForTelbizProducts Response : invalidAccounts=[{}] | balanceList=[{}] | traceId=[{}]", invalidAccounts, balanceList, traceId);
		return Arrays.asList(invalidAccounts, balanceList);
	}

	List<String> getMobileAndContactId(AccountRef accountRef, String traceId) throws DCPEException {
		String mobile = null;
		String contractID = null;
		if (accountRef.getContractNo() != null && !accountRef.getContractNo().isEmpty()) {
			mobile = persistanceIntegrationService.getMobileOfContractId(accountRef.getContractNo(), traceId);
			contractID = accountRef.getContractNo();
		} else if (accountRef.getConnRef() != null && !accountRef.getConnRef().isEmpty()) {
			mobile = accountRef.getConnRef();
			contractID = persistanceIntegrationService.getContractIdOfMobile(mobile, traceId);
		}

		return Arrays.asList(mobile, contractID);
	}

	List<String> getMobileAndContactIdForTelbizProducts(AccountRef accountRef, String traceId) throws DCPEException {
		LOGGER.info("getMobileAndContactIdForTelbizProducts Request : AccountRef=[{}] | traceId=[{}]", accountRef, traceId);
		String mobile = null;
		String contractID = null;
		if (accountRef.getContractNo() != null && !accountRef.getContractNo().isEmpty() && !accountRef.getProductType().equals(ProductTypeEnum.DCS.getType())) {
			mobile = persistanceIntegrationService.getMobileOfContractId(accountRef.getContractNo(), traceId);
			contractID = accountRef.getContractNo();
		} else if (accountRef.getConnRef() != null && !accountRef.getConnRef().isEmpty()) {
			mobile =  accountRef.getConnRef().startsWith("0")? accountRef.getConnRef().substring(1) : accountRef.getConnRef();
			contractID = persistanceIntegrationService.getContractIdOfMobile(mobile, traceId);
		}

		LOGGER.info("getMobileAndContactIdForTelbizProducts Response : mobile=[{}] | contractID=[{}] | traceId=[{}]", mobile, contractID, traceId);
		return Arrays.asList(mobile, contractID);
	}

	private OCSProfileResponse setupSuccessResponse(List<ConnectionRef> invalidAccounts, List<RealTimeBalance> balanceList ) {
		OCSProfileResponse ocsProfileResponse = new OCSProfileResponse();
		ocsProfileResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
		ocsProfileResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
		ocsProfileResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
		ocsProfileResponse.setInvalidAccounts(invalidAccounts);
		ocsProfileResponse.setAccounts(balanceList);
		return ocsProfileResponse;
	}
	private OCSProfileResponse setupFailResponse() {
		OCSProfileResponse ocsProfileResponse = new OCSProfileResponse();
		ocsProfileResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
		ocsProfileResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_INVALID_PC.getErrorCode());
		ocsProfileResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_INVALID_PC.getErrorCode());
		return ocsProfileResponse;
	}
	private List<RealTimeBalance> setupBalanceList(List<RealTimeBalance> balanceList, OCSStatusResponse realTimeBal, String contractID, String mobile) {

		if (realTimeBal.getResCode() != null && "200".equals(realTimeBal.getResCode())) {

			RealTimeBalance balanceInfo = new RealTimeBalance();
			
			balanceInfo.setContractNo(contractID);
			balanceInfo.setConnRef(mobile);
			balanceInfo.setCreditStatus(CreditStatusEnum.getCreditStatus(realTimeBal.getContent().getOcsStatus()) !=  null ?  CreditStatusEnum.getCreditStatus(realTimeBal.getContent().getOcsStatus()).valueOf() : null);
			balanceInfo.setCxCatagory(realTimeBal.getContent().getCxcatagory());
			balanceInfo.setOustanding(realTimeBal.getContent().getCurrruntOutstanding() != 0 ? realTimeBal.getContent().getCurrruntOutstanding()/NEConstants.OCS_AMOUNT_DIVISER : 0.0);
			balanceInfo.setPaidMode(realTimeBal.getContent().getPaidMode());
			balanceInfo.setBalance(realTimeBal.getContent().getBalance());
			balanceInfo.setTotalCredit(realTimeBal.getContent().getTotcredit() != 0 ? realTimeBal.getContent().getTotcredit()/NEConstants.OCS_AMOUNT_DIVISER : 0.0);
			balanceList.add(balanceInfo);
		}
		return balanceList;
	}

	private void setupBalanceListTelBiz(List<RealTimeBalance> balanceList, OCSStatusResponse realTimeBal, String contractID, String mobile, String traceId) {
		/* Mobile No should return with leading 0 for Both VOLTE and DCS numbers
		*/
		if (realTimeBal.getResCode() != null && "200".equals(realTimeBal.getResCode())) {

			RealTimeBalance balanceInfo = new RealTimeBalance();

			balanceInfo.setContractNo(contractID);
			balanceInfo.setConnRef(!mobile.startsWith("0")? "0"+mobile : mobile);
			balanceInfo.setCreditStatus(CreditStatusEnum.getCreditStatus(realTimeBal.getContent().getOcsStatus()).valueOf());
			balanceInfo.setCxCatagory(realTimeBal.getContent().getCxcatagory());
			balanceInfo.setOustanding(realTimeBal.getContent().getCurrruntOutstanding());
			balanceInfo.setPaidMode(realTimeBal.getContent().getPaidMode());
			balanceInfo.setBalance(realTimeBal.getContent().getBalance());
			balanceInfo.setTotalCredit(realTimeBal.getContent().getTotcredit());
			balanceList.add(balanceInfo);
			LOGGER.info("setupBalanceListTelBiz : balanceInfo=[{}] | traceId=[{}]", balanceInfo, traceId);
		}
	}
}
