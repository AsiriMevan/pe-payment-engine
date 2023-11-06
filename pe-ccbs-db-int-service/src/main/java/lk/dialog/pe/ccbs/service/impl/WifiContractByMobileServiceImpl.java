package lk.dialog.pe.ccbs.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

import lk.dialog.pe.ccbs.dto.Account;
import lk.dialog.pe.ccbs.dto.Profile;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.WifiContractByMobileService;
import lk.dialog.pe.ccbs.util.BusinessUnitEnum;
import lk.dialog.pe.ccbs.util.CRMSystemsEnum;
import lk.dialog.pe.ccbs.util.ChargeTypeEnum;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.ccbs.util.HybridStatusEnum;
import lk.dialog.pe.ccbs.util.ProductType;
import lk.dialog.pe.ccbs.util.SQLQueryEnum;
import lk.dialog.pe.ccbs.util.ServiceBusineessUnitEnum;
import lk.dialog.pe.ccbs.util.SwitchStatus;

@Service
public class WifiContractByMobileServiceImpl implements WifiContractByMobileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WifiContractByMobileServiceImpl.class);
	
	@Autowired
	QueryExecuterRepository queryExecuterRepository;

	@Autowired
	@Qualifier("queryMap")
	private Map<String, String> map;
	
	public List<Profile> getWifiContractIdOrMobile(String mobile, Integer contractId, String traceId) throws DCPEException {
		Instant start = Instant.now();	
		LOGGER.info("getWifiContractIdOrMobileRequest : mobile={}|contractId={}",mobile,contractId);
		
		List<Profile> wifiList = getWifiDetailsContractIdOrMobile(mobile, contractId, traceId);
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(wifiList);
		LOGGER.info("getWifiContractIdOrMobileResponse : traceId={}|timeTaken={}|wifiList={}",traceId,timeTaken,responseString);
		return wifiList;
	}
	
	public List<Profile> getWifiDetailsContractIdOrMobile(String mobile, Integer contractId, String traceId)
			throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getWifiContractIdOrMobileRequest : mobile={}|contractId={}|traceId={}", mobile, contractId,
				traceId);
		List<Profile> profileList = new ArrayList<>();
		List<Map<String, Object>> rows = null;
		try {
			Object[] params = { mobile, contractId };
			rows = queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_WIFI_CONTRACT_BY_MOBILE.getValue()), params, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("getWifiContractIdOrMobileResponse error occured: errorMessage={}|error={}|traceId={}",
					e.getMessage(), e, traceId);
			return Collections.emptyList();
		}

		profileList.addAll(processWiFiRows(rows, traceId));		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getWifiContractIdOrMobileResponse : traceId={}|timeTaken={}|profileList={}", traceId, timeTaken,
				profileList);
		return profileList;
	}

	private List<Profile> processWiFiRows(List<Map<String, Object>> rows, String traceId) {
		Instant start = Instant.now();
		String requestString = DCPEUtil.convertToString(rows);
		LOGGER.info("processWiFiRowsRequest : traceId={}|request={}", traceId, requestString);

		List<Profile> profileList = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			Profile profile = new Profile();
			profile.setProfileId(((java.math.BigDecimal) row.get(CRMSystemsEnum.STR_CUST_ID.getCrm())).toString());
			if (profileList.contains(profile)) {
				setupWifiCustomerRows(profileList, profile, row, traceId);
			} else {
				return setupWifiAllCustomerRows(profileList, profile, row, traceId);
			}
		}
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileList);
		LOGGER.info("processWiFiRowsResponse : traceId={}|timeTaken={}|request={}", traceId, timeTaken, responseString);
		return profileList;

	}

	private List<Profile> setupWifiAllCustomerRows(List<Profile> profileList, Profile profile,
			Map<String, Object> row, String traceId) {
		Instant start = Instant.now();
		String profileListString = DCPEUtil.convertToString(profileList);
		String profileString = DCPEUtil.convertToString(profile);
		LOGGER.info("setupWifiAllCustomerRowsRequest : traceId={}|profile={}|profileList={}", traceId, profileString, profileListString);

		profile.setCustRef((String) row.get("ID_NUMBER"));
		profile.setOldCustRef((String) row.get(CRMSystemsEnum.STR_OLD_NIC_NO.getCrm()));
		profile.setCustRefType((String) row.get("ID_TYPE"));
		profile.setCustName(((String) row.get(CRMSystemsEnum.STR_TITLE.getCrm()) == null ? ""
				: (String) row.get(CRMSystemsEnum.STR_TITLE.getCrm()) + " ")
				+ ((String) row.get("FIRST_NAME") == null ? "" : (String) row.get("FIRST_NAME") + " ")
				+ ((String) row.get("LAST_NAME") == null ? "" : (String) row.get("LAST_NAME")));
		profile.setAddrLine1((String) row.get("ADDRESS_LINE1"));
		profile.setAddrLine2((String) row.get("ADDRESS_LINE2"));
		profile.setAddrLine3((String) row.get("ADDRESS_LINE3"));
		profile.setPostalCode((String) row.get("POSTAL_CODE"));
		String email = (String) row.get("EMAIL");
		profile.setEmail(email);
		List<Account> accountList = new ArrayList<>();
		Account account = new Account();
		if (row.get(CRMSystemsEnum.STR_CUST_ID.getCrm()) != null) {
			account.setAccountNo(((java.math.BigDecimal) row.get(CRMSystemsEnum.STR_CUST_ID.getCrm())).toString());
			account.setConnRef(row.get("CUST_IDENTIFIER").toString());
			account.setPrEmail(email);
		}
		account.setAccountType(ChargeTypeEnum.POSTPAID.getType());
		account.setContractNo(((java.math.BigDecimal) row.get("ACCOUNT_ID")).toString());
		account.setContractEmail((String) row.get("BILLING_EMAIL"));
		String swStatus = (String) row.get("STATUS");
		account.setConStatus(swStatus != null ? SwitchStatus.getSwitchStatus(swStatus).valueOf() : null);
		account.setSbu(BusinessUnitEnum.SBU_GSM.getType());
		account.setBillCycle((String) row.get(CRMSystemsEnum.STR_BILL_RUN_CODE.getCrm()));
		account.setProductType(ProductType.WIFI.valueOf());
		account.setHybridFlag(HybridStatusEnum.NFC_HYBRID.getStatus());
		accountList.add(account);

		profile.setAccounts(accountList);
		profileList.add(profile);
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileList);
		LOGGER.info("setupWifiAllCustomerRowsResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);

		return profileList;

	}

	private void setupWifiCustomerRows(List<Profile> profileList, Profile profile, Map<String, Object> row, String traceId) {
		Instant start = Instant.now();
		String profileListString = DCPEUtil.convertToString(profileList);
		String rowString = DCPEUtil.convertToString(row);
		LOGGER.info("setupWifiCustomerRowsRequest : traceId={}|profileList={}|row={}", traceId, profileListString, rowString);

		int position = profileList.indexOf(profile);
		profile = profileList.get(position);
		List<Account> accountList = profile.getAccounts();
		Account account = new Account();
		String email = (String) row.get("BILLING_EMAIL");

		if (row.get(CRMSystemsEnum.STR_CUST_ID.getCrm()) != null) {
			account.setAccountNo(((java.math.BigDecimal) row.get(CRMSystemsEnum.STR_CUST_ID.getCrm())).toString());
			account.setConnRef(row.get("CUST_IDENTIFIER").toString());
			account.setPrEmail(email);
		}
		account.setAccountType(ChargeTypeEnum.POSTPAID.getType());
		account.setContractNo(((java.math.BigDecimal) row.get("ACCOUNT_ID")).toString());
		account.setContractEmail(email);
		String swStatus = (String) row.get("STATUS");
		account.setConStatus(swStatus != null ? SwitchStatus.getSwitchStatus(swStatus).valueOf() : null);
		account.setSbu(ServiceBusineessUnitEnum.SBU_GSM.getValue());
		account.setBillCycle((String) row.get(CRMSystemsEnum.STR_BILL_RUN_CODE.getCrm()));
		account.setProductType(ProductType.WIFI.valueOf());
		account.setHybridFlag(HybridStatusEnum.NFC_HYBRID.getStatus());
		accountList.add(account);
		Long  timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(accountList);
		LOGGER.info("setupWifiCustomerRowsResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);

	}

}
