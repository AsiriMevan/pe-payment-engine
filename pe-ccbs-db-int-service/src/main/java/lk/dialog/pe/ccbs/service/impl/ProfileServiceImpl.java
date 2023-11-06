package lk.dialog.pe.ccbs.service.impl;

import java.time.Instant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;

import lk.dialog.pe.ccbs.dto.Account;
import lk.dialog.pe.ccbs.dto.CRMProfileRequest;
import lk.dialog.pe.ccbs.dto.Profile;
import lk.dialog.pe.ccbs.repository.QueryExecuterRepository;
import lk.dialog.pe.ccbs.service.ProfileService;
import lk.dialog.pe.ccbs.util.AccountType;
import lk.dialog.pe.ccbs.util.BusinessUnitEnum;
import lk.dialog.pe.ccbs.util.CRMSystemsEnum;
import lk.dialog.pe.ccbs.util.ChargeTypeEnum;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.ccbs.util.HybridStatus;
import lk.dialog.pe.ccbs.util.HybridStatusEnum;
import lk.dialog.pe.ccbs.util.ProductType;
import lk.dialog.pe.ccbs.util.SBU;
import lk.dialog.pe.ccbs.util.SQLQueryEnum;
import lk.dialog.pe.ccbs.util.SearchingCriteriaEnum;
import lk.dialog.pe.ccbs.util.ServiceBusineessUnitEnum;
import lk.dialog.pe.ccbs.util.SwitchStatus;
import lk.dialog.pe.ccbs.util.ValidationPatternEnum;
import lk.dialog.pe.common.exception.DCPEException;

@Service
public class ProfileServiceImpl implements ProfileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceImpl.class);

	@Autowired
	private QueryExecuterRepository queryExecuterRepository;

	@Autowired
	@Qualifier("queryMap")
	private Map<String, String> map;

	private StringBuilder mobileQueryStrBuilder;
	private StringBuilder mobileDcsQueryStrBuilder;
	private StringBuilder contractQueryStrBuilder;
	private StringBuilder contractDcsQueryStrBuilder;
	private StringBuilder wifiContractQueryStrBuilder;
	private StringBuilder wifiMobileQueryStrBuilder;
	private boolean isMobileVisit;
	private boolean isContractVisit;
	private boolean isContractDcsVisit;
	private boolean isWiFiContractVisit;
	private boolean isWiFiMobileVisit;
	private String finalSQL;
	private List<Profile> profileList;

	public List<Profile> getProfilesByInvoiceNo(String invoiceNo, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getProfilesByInvoiceNoRequest : traceId={}|invoiceNo={}", traceId, invoiceNo);

		List<Profile> accountNoList = getProfileByInvoiceNo(invoiceNo, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByInvoiceNoResponse : traceId={}|timeTaken={}|accountNoList={}", traceId, timeTaken,
				accountNoList);
		return accountNoList;
	}

	@Override
	public List<Profile> getVolteProfilesById(CRMProfileRequest crmProfileRequest, String traceId)
			throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getVolteProfilesByIdRequest : traceId={}|crmProfileRequest={}", traceId, crmProfileRequest);

		List<Profile> volteList = getVolteProfileById(crmProfileRequest, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getVolteProfilesByIdResponse : traceId={}|timeTaken={}|volteList={}", traceId, timeTaken,
				volteList);
		return volteList;
	}

	@Override
	public List<Profile> getProfilesById(CRMProfileRequest crmProfileRequest, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getProfilesByIdRequest : traceId={}|crmProfileRequest={}", traceId, crmProfileRequest);

		List<Profile> getProfileList = getProfileById(crmProfileRequest, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(getProfileList);
		LOGGER.info("getProfilesByIdResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);
		return getProfileList;
	}

	@Override
	public List<Profile> getProfilesByAccount(List<String> mobileList, List<String> contractList, String traceId)
			throws DCPEException {
		Instant start = Instant.now();
		String mobileListConvertToString = DCPEUtil.convertToString(mobileList);
		String contractListConvertToString = DCPEUtil.convertToString(contractList);
		LOGGER.info("getProfilesByAccountRequest : traceId={}|mobileList={}|contractList={}", traceId,
				mobileListConvertToString, contractListConvertToString);
		List<Profile> getprofileList = getProfileByAccount(mobileList, contractList, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(getprofileList);
		LOGGER.info("getProfilesByAccountResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken,
				responseString);
		return getprofileList;
	}

	@Override
	public List<Profile> getProfilesByBulkAccount(List<String> mobileList, List<String> contractList, String traceId)
			throws DCPEException {
		Instant start = Instant.now();
		String mobileListConvertToString = DCPEUtil.convertToString(mobileList);
		String contractListConvertToString = DCPEUtil.convertToString(contractList);

		LOGGER.info("getProfilesByBulkAccountRequest : traceId={}|mobileList={}|contractList={}", traceId,
				mobileListConvertToString, contractListConvertToString);
		List<Profile> profileListByBulkAccount = getProfileByBulkAccount(mobileList, contractList, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileListByBulkAccount);
		LOGGER.info("getProfilesByBulkAccountResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken,
				responseString);
		return profileListByBulkAccount;
	}

	public List<Profile> getProfileByInvoiceNo(String invoiceNo, String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getProfilesByInvoiceNoRequest: invoiceNo={}|traceId={}", invoiceNo, traceId);

		List<Map<String, Object>> rows = null;
		try {
			rows = queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_CCBS_PROFILE_BY_INVOICE_NO.getValue()),
					new Object[] { invoiceNo }, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("getProfilesByInvoiceNoResponse error occured : errorMesseage={}|error={}|traceId={}",
					e.getMessage(), e, traceId);
			return Collections.emptyList();
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByInvoiceNoResponse : timeTaken={}|traceId={}", timeTaken, traceId);

		return processRows(rows, traceId);
	}

	public List<Profile> getVolteProfileById(CRMProfileRequest jsonReq, String traceId) throws DCPEException {
		String requestString = DCPEUtil.convertToString(jsonReq);
		Instant start = Instant.now();
		LOGGER.info("getVolteProfilesByIdRequest : traceId={}|requestString={}", traceId, requestString);

		Object[] params = { jsonReq.getCustRef(), jsonReq.getOldCustRef(), jsonReq.getCustRefType(),
				jsonReq.getCustRef(), jsonReq.getOldCustRef(), jsonReq.getCustRefType() };
		List<Map<String, Object>> rows = null;
		try {
			rows = queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_CCBS_VOLTE_PROFILE_BY_ID.getValue()),
					params, traceId);
		} catch (DataAccessException e) {
			LOGGER.error("getVolteProfilesByIdResponse error occured: errorMessage={}|error={}|traceId={}",
					e.getMessage(), e, traceId);
			return Collections.emptyList();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getVolteProfilesByIdResponse : timeTaken={}|traceId={}", timeTaken, traceId);

		return processRows(rows, traceId);
	}

	public List<Profile> getProfileById(CRMProfileRequest jsonReq, String traceId) throws DCPEException {
		String requestString = DCPEUtil.convertToString(jsonReq);
		Instant start = Instant.now();
		LOGGER.info("getProfileByIdRequest : traceId={}|requestString={}", traceId, requestString);

		List<Map<String, Object>> rows = null;
		List<Profile> profileListLocal = null;
		Object[] params = { jsonReq.getCustRef(), jsonReq.getOldCustRef(), jsonReq.getCustRefType() };

		/* query ccbs GSM & DTV profiles */
		rows = queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_CCBS_PROFILE_BY_ID.getValue()), params,
				traceId);
		profileListLocal = processRows(rows, traceId);
		/* query ccbs hotspot wifi profiles */
		rows = queryExecuterRepository.getData(map.get(SQLQueryEnum.SQL_WIFI_PROFILE_BY_ID.getValue()), params,
				traceId);

		if (profileListLocal != null)
			profileListLocal.addAll(processWiFiRows(rows, traceId));
		else
			profileListLocal = processWiFiRows(rows, traceId);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileListLocal);
		LOGGER.info("getProfileByIdResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);
		return profileListLocal;
	}

	public List<Profile> getProfileByAccount(List<String> mobileList, List<String> contractList, String traceId)
			throws DCPEException {
		Instant start = Instant.now();
		String mobileListConvertToString = DCPEUtil.convertToString(mobileList);
		String contractListConvertToString = DCPEUtil.convertToString(contractList);
		LOGGER.info("getProfileByAccountRequest : traceId={}|mobileList={}|contractList= {}", traceId,
				mobileListConvertToString, contractListConvertToString);

		finalSQL = "";
		profileList = new ArrayList<>();
		mobileQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_CCBS_PROFILE_BY_MSISDN.getValue()));
		contractQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_CCBS_PROFILE_BY_CONTRACT.getValue()));
		mobileDcsQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_DCS_PROFILE_BY_MSISDN.getValue()));
		contractDcsQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_DCS_PROFILE_BY_CONTRACT.getValue()));
		wifiContractQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_WIFI_PROFILE_BY_CONTRACT.getValue()));
		wifiMobileQueryStrBuilder = new StringBuilder(
				map.get(SQLQueryEnum.SQL_WIFI_PROFILE_BY_CONNECTION_REF.getValue()));

		isMobileVisit = false;
		isContractVisit = false;
		isContractDcsVisit = false;
		isWiFiContractVisit = false;
		isWiFiMobileVisit = false;

		String mobileQueryStr = "";
		String mobileDcsQueryStr = "";
		String contractQueryStr = "";
		String contractDcsQueryStr = "";
		String wifiContractQueryStr = "";
		String wifimobileQueryStr = "";

		/* traverse through mobile list */
		mobileList.stream().forEach(mobileNo -> {
			Matcher m = (Pattern.compile(ValidationPatternEnum.CCBS_WIFI_CONN_REF_VALIDATOR.getPattern()))
					.matcher(mobileNo);

			if (m.matches()) {
				/* WiFi connection */
				wifiMobileQueryStrBuilder.append("'" + mobileNo + "',");
				isWiFiMobileVisit = true;

			} else {
				mobileQueryStrBuilder.append("'" + mobileNo + "',");
				mobileDcsQueryStrBuilder.append("'" + mobileNo + "',");
				isMobileVisit = true;
			}

		});
		/* traverse through contract list */
		contractList.stream().forEach(contractNo -> {
			Matcher m = (Pattern.compile(ValidationPatternEnum.WIFI_NO_PATTERN.getPattern())).matcher(contractNo);

			if (contractNo.matches(ValidationPatternEnum.INTEGER_VALIDATOR.getPattern())) {
				if (m.matches() && contractNo.length() == 8) {
					/* WiFi connection */
					wifiContractQueryStrBuilder.append("'" + contractNo + "',");
					isWiFiContractVisit = true;
				} else {
					contractQueryStrBuilder.append("'" + contractNo + "',");
					contractDcsQueryStrBuilder.append("'" + contractNo + "',");
					isContractVisit = true;
					isContractDcsVisit = true;
				}
			} else {
				contractDcsQueryStrBuilder.append("'" + contractNo + "',");
				isContractDcsVisit = true;
			}
		});
		profileList = getProfilesByAccountQueryBuillder(mobileQueryStr, mobileDcsQueryStr, contractQueryStr,
				contractDcsQueryStr, wifiContractQueryStr, wifimobileQueryStr, traceId);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileList);

		LOGGER.info("getProfileByAccountResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken,
				responseString);

		return profileList;
	}

	private List<Profile> getProfilesByAccountQueryBuillder(String mobileQueryStr, String mobileDcsQueryStr,
			String contractQueryStr, String contractDcsQueryStr, String wifiContractQueryStr, String wifimobileQueryStr,
			String traceId) throws DCPEException {
		Instant start = Instant.now();
		LOGGER.info("getProfilesByAccountQueryBuillderRequest : traceId={}", traceId);

		List<Map<String, Object>> rows;

		if (isMobileVisit) {
			mobileQueryStr = mobileQueryStrBuilder.deleteCharAt(mobileQueryStrBuilder.length() - 1).append(")")
					.toString();
			mobileDcsQueryStr = mobileDcsQueryStrBuilder.deleteCharAt(mobileDcsQueryStrBuilder.length() - 1).append(")")
					.toString();
		}
		if (isContractVisit) {
			contractQueryStr = contractQueryStrBuilder.deleteCharAt(contractQueryStrBuilder.length() - 1).append(")")
					.toString();
		}
		if (isContractDcsVisit) {
			contractDcsQueryStr = contractDcsQueryStrBuilder.deleteCharAt(contractDcsQueryStrBuilder.length() - 1)
					.append(")").toString();
		}
		if (isWiFiContractVisit) {
			wifiContractQueryStr = wifiContractQueryStrBuilder.deleteCharAt(wifiContractQueryStrBuilder.length() - 1)
					.append(")").toString();
		}
		if (isWiFiMobileVisit) {
			wifimobileQueryStr = wifiMobileQueryStrBuilder.deleteCharAt(wifiMobileQueryStrBuilder.length() - 1)
					.append(")").toString();
		}
		finalSQL = getSqlQueryApender(mobileQueryStr, mobileDcsQueryStr, contractQueryStr, contractDcsQueryStr,
				traceId);

		if (!finalSQL.equals("")) {
			rows = queryExecuterRepository.getData(finalSQL, new Object[] {}, traceId);
			profileList.addAll(processRows(rows, traceId));
		}

		if (isWiFiContractVisit) {
			finalSQL = wifiContractQueryStr;
			rows = queryExecuterRepository.getData(finalSQL, new Object[] {}, traceId);
			profileList.addAll(processWiFiRows(rows, traceId));
		}
		if (isWiFiMobileVisit) {
			finalSQL = wifimobileQueryStr;
			rows = queryExecuterRepository.getData(finalSQL, new Object[] {}, traceId);
			profileList.addAll(processWiFiRows(rows, traceId));
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileList);

		LOGGER.info("getProfilesByAccountQueryBuillderResponse: traceId={}|timeTaken={}|response={}", traceId,
				timeTaken, responseString);

		return profileList;
	}

	private String getSqlQueryApender(String mobileQueryStr, String mobileDcsQueryStr, String contractQueryStr,
			String contractDcsQueryStr, String traceId) {
		Instant start = Instant.now();
		LOGGER.info("getSqlQueryApenderRequest: traceId={}|mobileQueryStr={}|contractQueryStr={}", traceId,
				mobileQueryStr, contractQueryStr);

		if (isMobileVisit && isContractVisit && isContractDcsVisit) {
			finalSQL = mobileQueryStr + CRMSystemsEnum.UNION_APPENDER.getCrm() + " " + mobileDcsQueryStr
					+ CRMSystemsEnum.UNION_APPENDER.getCrm() + " " + contractQueryStr
					+ CRMSystemsEnum.UNION_APPENDER.getCrm() + " " + contractDcsQueryStr;
		} else if (isMobileVisit && isContractVisit) {
			finalSQL = mobileQueryStr + CRMSystemsEnum.UNION_APPENDER.getCrm() + " " + mobileDcsQueryStr
					+ CRMSystemsEnum.UNION_APPENDER.getCrm() + " " + contractQueryStr;
		} else if (isMobileVisit && isContractDcsVisit) {
			finalSQL = mobileQueryStr + CRMSystemsEnum.UNION_APPENDER.getCrm() + " " + mobileDcsQueryStr
					+ CRMSystemsEnum.UNION_APPENDER.getCrm() + " " + contractDcsQueryStr;
		} else if (isContractVisit && isContractDcsVisit) {
			finalSQL = contractQueryStr + CRMSystemsEnum.UNION_APPENDER.getCrm() + " " + contractDcsQueryStr;
		} else if (isMobileVisit) {
			finalSQL = mobileQueryStr + CRMSystemsEnum.UNION_APPENDER.getCrm() + " " + mobileDcsQueryStr;
		} else if (isContractVisit) {
			finalSQL = contractQueryStr;
		} else if (isContractDcsVisit) {
			finalSQL = contractDcsQueryStr;
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getSqlQueryApenderResponse: traceId={}|timeTaken={}|finalSQL={}", traceId, timeTaken, finalSQL);

		return finalSQL;
	}

	public List<Profile> getProfileByBulkAccount(List<String> mobileBulkList, List<String> contractBulkList,
			String traceId) throws DCPEException {
		Instant start = Instant.now();
		String mobileBulkListString = DCPEUtil.convertToString(mobileBulkList);
		String contractBulkListString = DCPEUtil.convertToString(contractBulkList);
		LOGGER.info("getProfileByBulkAccountRequest: traceId={}|mobileBulkList={}|contractBulkList={}", traceId,
				mobileBulkListString, contractBulkListString);

		finalSQL = "";
		List<Map<String, Object>> rows = new ArrayList<>();

		isMobileVisit = false;
		isContractVisit = false;
		isContractDcsVisit = false;
		isWiFiContractVisit = false;
		isWiFiMobileVisit = false;

		int bulkSize;
		int mobileBulkSize = 0;
		int contractBulkSize = 0;

		if (mobileBulkList != null && !mobileBulkList.isEmpty()) {
			mobileBulkSize = (int) Math
					.ceil((mobileBulkList.size() / SearchingCriteriaEnum.BULK_REQUEST_PER_BATCH.getValue()));
		}
		if (contractBulkList != null && !contractBulkList.isEmpty()) {
			contractBulkSize = (int) Math
					.ceil((contractBulkList.size() / SearchingCriteriaEnum.BULK_REQUEST_PER_BATCH.getValue()));
		}

		if (mobileBulkSize > contractBulkSize) {
			bulkSize = mobileBulkSize;
		} else {
			bulkSize = contractBulkSize;
		}

		for (int x = 0; x < bulkSize; x++) {
			List<String> mobileList = new ArrayList<>();
			List<String> contractList = new ArrayList<>();

			if (mobileBulkList != null && !mobileBulkList.isEmpty()) {
				mobileList = mobileBulkList.stream()
						.limit((long) SearchingCriteriaEnum.BULK_REQUEST_PER_BATCH.getValue())
						.collect(Collectors.toList());
				mobileBulkList.subList(0, mobileList.size()).clear();
			}
			if (contractBulkList != null && !contractBulkList.isEmpty()) {
				contractList = contractBulkList.stream()
						.limit((long) SearchingCriteriaEnum.BULK_REQUEST_PER_BATCH.getValue())
						.collect(Collectors.toList());
				contractBulkList.subList(0, contractList.size()).clear();
			}
			// When query by DCS Contract number, it should return only the main Number
			// details
			mobileQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_CCBS_PROFILE_BY_MSISDN.getValue()));
			contractQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_CCBS_PROFILE_BY_CONTRACT.getValue()));
			mobileDcsQueryStrBuilder = new StringBuilder(map.get(SQLQueryEnum.SQL_DCS_PROFILE_BY_MSISDN.getValue()));
			contractDcsQueryStrBuilder = new StringBuilder(
					map.get(SQLQueryEnum.SQL_DCS_PROFILE_BY_CONTRACT.getValue()));
			wifiContractQueryStrBuilder = new StringBuilder(
					map.get(SQLQueryEnum.SQL_WIFI_PROFILE_BY_CONTRACT.getValue()));
			wifiMobileQueryStrBuilder = new StringBuilder(
					map.get(SQLQueryEnum.SQL_WIFI_PROFILE_BY_CONNECTION_REF.getValue()));

			String mobileQueryStr = "";
			String mobileDcsQueryStr = "";
			String contractQueryStr = "";
			String contractDcsQueryStr = "";
			String wifiContractQueryStr = "";
			String wifimobileQueryStr = "";

			/* traverse through mobile list */
			for (int i = 0; i < mobileList.size(); i++) {
				String mobileNo = mobileList.get(i);
				Matcher m = (Pattern.compile(ValidationPatternEnum.CCBS_WIFI_CONN_REF_VALIDATOR.getPattern()))
						.matcher(mobileNo);

				if (m.matches()) {
					/* WiFi connection */
					wifiMobileQueryStrBuilder.append("'" + mobileNo + "',");
					isWiFiMobileVisit = true;

				} else {
					mobileQueryStrBuilder.append("'" + mobileNo + "',");
					mobileDcsQueryStrBuilder.append("'" + mobileNo + "',");
					isMobileVisit = true;
				}

			}
			/* traverse through contract list */
			for (int i = 0; i < contractList.size(); i++) {
				String contractNo = contractList.get(i);
				Matcher m = (Pattern.compile(ValidationPatternEnum.WIFI_NO_PATTERN.getPattern())).matcher(contractNo);

				if (contractNo.matches(ValidationPatternEnum.INTEGER_VALIDATOR.getPattern())) {
					if (m.matches() && contractNo.length() == 8) {
						/* WiFi connection */
						wifiContractQueryStrBuilder.append("'" + contractNo + "',");
						isWiFiContractVisit = true;

					} else {
						contractQueryStrBuilder.append("'" + contractNo + "',");
						contractDcsQueryStrBuilder.append("'" + contractNo + "',");
						isContractVisit = true;
						isContractDcsVisit = true;
					}
				} else {
					contractDcsQueryStrBuilder.append("'" + contractNo + "',");
					isContractDcsVisit = true;
				}
			}

			if (isMobileVisit) {
				mobileQueryStr = mobileQueryStrBuilder.deleteCharAt(mobileQueryStrBuilder.length() - 1).append(")")
						.toString();
				mobileDcsQueryStr = mobileDcsQueryStrBuilder.deleteCharAt(mobileDcsQueryStrBuilder.length() - 1)
						.append(")").toString();
			}
			if (isContractVisit) {
				contractQueryStr = contractQueryStrBuilder.deleteCharAt(contractQueryStrBuilder.length() - 1)
						.append(")").toString();
			}
			if (isContractDcsVisit) {
				contractDcsQueryStr = contractDcsQueryStrBuilder.deleteCharAt(contractDcsQueryStrBuilder.length() - 1)
						.append(")").toString();
			}
			if (isWiFiContractVisit) {
				wifiContractQueryStr = wifiContractQueryStrBuilder
						.deleteCharAt(wifiContractQueryStrBuilder.length() - 1).append(")").toString();
			}
			if (isWiFiMobileVisit) {
				wifimobileQueryStr = wifiMobileQueryStrBuilder.deleteCharAt(wifiMobileQueryStrBuilder.length() - 1)
						.append(")").toString();
			}

			finalSQL = getSqlQueryApender(mobileQueryStr, mobileDcsQueryStr, contractQueryStr, contractDcsQueryStr,
					traceId);
			if (!finalSQL.equals("")) {
				List<Map<String, Object>> row = queryExecuterRepository.getData(finalSQL, new Object[] {}, traceId);
				if (row != null && !row.isEmpty())
					rows.addAll(row);
			}

			if (isWiFiContractVisit || isWiFiMobileVisit) {
				List<Map<String, Object>> rowContract = null;
				List<Map<String, Object>> rowMobile = null;

				if (isWiFiContractVisit) {
					finalSQL = wifiContractQueryStr;
					LOGGER.info("getProfilesByBulkAccount query :finalSQL={}|traceId={}", finalSQL, traceId);

					rowContract = queryExecuterRepository.getData(finalSQL, new Object[] {}, traceId);
				}
				if (isWiFiMobileVisit) {
					finalSQL = wifimobileQueryStr;
					LOGGER.info("getProfilesByBulkAccount query: finalSQL={}|traceId={}", finalSQL, traceId);
					rowMobile = queryExecuterRepository.getData(finalSQL, new Object[] {}, traceId);
				}

				if (rowContract != null && !rowContract.isEmpty())
					rows.addAll(rowContract);
				if (rowMobile != null && !rowMobile.isEmpty())
					rows.addAll(rowMobile);
			}
		}
		// end of bulk process

		profileList = processRows(rows, traceId);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileList);
		LOGGER.info("getProfileByBulkAccountRequest: traceId={}|timeTaken={}|response={}", traceId, timeTaken,
				responseString);

		return profileList;
	}

	public List<Profile> processWiFiRows(List<Map<String, Object>> rows, String traceId) {
		Instant start = Instant.now();
		String resquestString = DCPEUtil.convertToString(rows);
		LOGGER.info("processWiFiRowsRequest: traceId={}|row={}", traceId, resquestString);

		profileList = new ArrayList<>();
		Profile profile = new Profile();
		for (Map<String, Object> row : rows) {
			profile.setProfileId(((java.math.BigDecimal) row.get(CRMSystemsEnum.STR_CUST_ID.getCrm())).toString());
			if (profileList.contains(profile)) {
				setupWifiCustomerRows(profileList, profile, row, traceId);
			} else {
				profileList = setupWifiAllCustomerRows(profileList, profile, row, traceId);

			}
			
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileList);
		LOGGER.info("processWiFiRowsResponse: traceId={}|timeTaken={}|profileList={}", traceId, timeTaken,
				responseString);

		return profileList;

	}

	private List<Profile> processRows(List<Map<String, Object>> rows, String traceId) {
		Instant start = Instant.now();
		String requestString = DCPEUtil.convertToString(rows);
		LOGGER.info("processRowsRequest: traceId={}|response={}", traceId, requestString);

		profileList = new ArrayList<>();
		for (Map<String, Object> row : rows) {
			Profile profile = new Profile();
			profile.setProfileId(row.get(CRMSystemsEnum.STR_PROFILE_ID.getCrm()).toString());
			if (profileList.contains(profile)) {
				this.processRowIfProfileExist(profile, row, profileList);
			} else {
				this.setupProfileList(profile, row, profileList, traceId);
			}
		}
		String responseString = DCPEUtil.convertToString(profileList);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("processRowsResponse: traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);

		return profileList;
	}

	/**
	 * @param profile
	 * @param row
	 * @return 0 -> continue, 1 -> break, 2 -> return
	 */
	private List<Boolean> processRowIfProfileExist(Profile profile, Map<String, Object> row, List<Profile> profiles) {
		int position = profiles.indexOf(profile);
		profile = profiles.get(position);
		Account account = new Account();
		account.setAccountNo(row.get(CRMSystemsEnum.STR_NODE_ID.getCrm()).toString());
		account.setContractNo(row.get(CRMSystemsEnum.STR_CONTRACT_ID.getCrm()).toString());
		if (profile.getAccounts().contains(account)
				&& !row.get(CRMSystemsEnum.STR_SUBSIDIARY_TYPE.getCrm()).equals("DCS")) {
			return Arrays.asList(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
		}
		account.setConnRef((String) row.get(CRMSystemsEnum.STR_MOBILE_NO.getCrm()));
		String chargeType = (String) row.get(CRMSystemsEnum.STR_PRE_POST.getCrm());
		AccountType mapVal = AccountType.getAccountType(chargeType);
		if (mapVal != null)
			account.setAccountType(mapVal.valueOf());

		account.setContractEmail((String) row.get(CRMSystemsEnum.STR_EMAIL_ADDR.getCrm()));
		account.setPrCode(row.get(CRMSystemsEnum.STR_PR_CODE.getCrm()).toString());
		account.setPrEmail((String) row.get(CRMSystemsEnum.STR_PR_EMAIL.getCrm()));
		account.setHybridFlag(HybridStatus
				.getHybridStatus((String) row.get(CRMSystemsEnum.STR_OCS_HYBRID.getCrm())).valueOf());
		account.setConStatus(SwitchStatus
				.getSwitchStatus((String) row.get(CRMSystemsEnum.STR_SWITCH_STATUS.getCrm())).valueOf());
		account.setDisconReasonCode((String) row.get(CRMSystemsEnum.STR_STATUS_REASON_ID.getCrm()));
		account.setDisconReason((String) row.get(CRMSystemsEnum.STR_DESCRIPTION.getCrm()));

		SBU sbu = this.getSbuToSetupProfileList(row, chargeType, account);

		this.setSbuAndProductTypeToSetupProfileList(sbu, account);

		account.setBillCycle((String) row.get(CRMSystemsEnum.STR_BILL_RUN_CODE.getCrm()));

		if (sbu != null && (sbu.toString().equalsIgnoreCase(CRMSystemsEnum.STR_VOLTE.getCrm())
				|| sbu.toString().equalsIgnoreCase("DCS")))
			account.setConnRef("0" + row.get(CRMSystemsEnum.STR_MOBILE_NO.getCrm()));

		profile.getAccounts().add(account);
		return Arrays.asList(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
	}

	private void setupProfileList(Profile profile, Map<String, Object> row, List<Profile> profileList, String traceId) {
		Instant start = Instant.now();
		String profileString = DCPEUtil.convertToString(profile);
		String rowString = DCPEUtil.convertToString(row);
		LOGGER.info("setupProfileListRequest: traceId={}|row={}|profile={}", traceId, rowString, profileString);
		profile.setCustRef((String) row.get(CRMSystemsEnum.STR_ID_NUMBER.getCrm()));
		profile.setOldCustRef((String) row.get(CRMSystemsEnum.STR_OLD_NIC_NO.getCrm()));
		profile.setCustRefType((String) row.get(CRMSystemsEnum.STR_ID_TYPE.getCrm()));
		profile.setCustName(((String) row.get(CRMSystemsEnum.STR_TITLE.getCrm()) == null ? ""
				: (String) row.get(CRMSystemsEnum.STR_TITLE.getCrm()) + " ")
				+ ((String) row.get(CRMSystemsEnum.STR_NAME1.getCrm()) == null ? ""
						: (String) row.get(CRMSystemsEnum.STR_NAME1.getCrm()) + " ")
				+ ((String) row.get(CRMSystemsEnum.STR_NAME2.getCrm()) == null ? ""
						: (String) row.get(CRMSystemsEnum.STR_NAME2.getCrm())));
		String defaultAddrStr = (String) row.get(CRMSystemsEnum.STR_DEFAULT_ADDR.getCrm());
		if (defaultAddrStr != null) {
			String[] addrInfo = defaultAddrStr.split("%");
			// profile = getProfile(profile,addrInfo); //OLD Commented-Keshan 2022-11-02

			// NEW Code -keshan
			profile.setAddrLine1(addrInfo[0]);
			profile.setAddrLine2(addrInfo[1]);
			profile.setAddrLine3(addrInfo[2]);
			profile.setPostalCode(addrInfo[3]);
			profile.setEmail(addrInfo[4]);
			// NEW Code -keshan

		}
		List<Account> accountList = new ArrayList<>();

		Account account = new Account();
		account.setConnRef((String) row.get(CRMSystemsEnum.STR_MOBILE_NO.getCrm()));
		account.setAccountNo(row.get(CRMSystemsEnum.STR_NODE_ID.getCrm()).toString());
		String chargeType = (String) row.get(CRMSystemsEnum.STR_PRE_POST.getCrm());

		// account =setupAccount(account,row, chargeType, traceId); //OLD Commented
		// Keshan 2022-11-02
		// NEW Code -keshan
		AccountType mapVal = AccountType.getAccountType(chargeType);
		if (mapVal != null)
			account.setAccountType(mapVal.valueOf());
		account.setContractNo(row.get(CRMSystemsEnum.STR_CONTRACT_ID.getCrm()).toString());
		account.setContractEmail((String) row.get(CRMSystemsEnum.STR_EMAIL_ADDR.getCrm()));
		account.setPrCode(row.get(CRMSystemsEnum.STR_PR_CODE.getCrm()).toString());
		account.setPrEmail((String) row.get(CRMSystemsEnum.STR_PR_EMAIL.getCrm()));
		account.setHybridFlag(
				HybridStatus.getHybridStatus((String) row.get(CRMSystemsEnum.STR_OCS_HYBRID.getCrm())).valueOf());
		account.setConStatus(
				SwitchStatus.getSwitchStatus((String) row.get(CRMSystemsEnum.STR_SWITCH_STATUS.getCrm())).valueOf());
		account.setDisconReasonCode((String) row.get(CRMSystemsEnum.STR_STATUS_REASON_ID.getCrm()));
		account.setDisconReason((String) row.get(CRMSystemsEnum.STR_DESCRIPTION.getCrm()));
		// NEW Code -keshan

		SBU sbu = this.getSbuToSetupProfileList(row, chargeType, account);

		// account = setAccounts(account,row, sbu, traceId);//OLD Commented keshan
		// NEW -keshan
		this.setSbuAndProductTypeToSetupProfileList(sbu, account);

		account.setBillCycle((String) row.get(CRMSystemsEnum.STR_BILL_RUN_CODE.getCrm()));

		if (sbu != null && (sbu.toString().equalsIgnoreCase(CRMSystemsEnum.STR_VOLTE.getCrm())
				|| sbu.toString().equalsIgnoreCase("DCS")))
			account.setConnRef("0" + row.get(CRMSystemsEnum.STR_MOBILE_NO.getCrm()));
		// NEW -keshan

		accountList.add(account);

		profile.setAccounts(accountList);
		profileList.add(profile);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileList);
		LOGGER.info("setupProfileListResponse: traceId={}|timeTaken={}|response={}", traceId, timeTaken,
				responseString);
	}

	private SBU getSbuToSetupProfileList (Map<String, Object> row, String chargeType, Account account) {
		String subsidiary = (String) row.get(CRMSystemsEnum.STR_SUBSIDIARY_TYPE.getCrm());
		SBU sbu = null;
		if (subsidiary != null) {
			if (subsidiary.equals("DTV")) {
				if (chargeType != null) {
					if (account.getAccountType() == 1) {
						sbu = SBU.DTV_PREPAID;
					} else {
						sbu = SBU.DTV_POSTPAID;
					}
				}
			} else {
				sbu = SBU.getSBU((String) row.get(CRMSystemsEnum.STR_SUBSIDIARY_TYPE.getCrm()));
			}
		}
		return sbu;
	}

	private void setSbuAndProductTypeToSetupProfileList(SBU sbu, Account account) {
		if (sbu != null) {
			account.setSbu(sbu.valueOf());
			if (sbu.toString().equalsIgnoreCase(CRMSystemsEnum.STR_VOLTE.getCrm()))
				account.setProductType(ProductType.VOLTE.valueOf());
			else if (sbu.toString().equalsIgnoreCase("DCS"))
				account.setProductType(ProductType.DCS.valueOf());
			else
				account.setProductType(ProductType.OTHER.valueOf());
		} else {
			account.setProductType(ProductType.OTHER.valueOf());
		}
	}

	private List<Profile> setupWifiAllCustomerRows(List<Profile> profileList, Profile profile, Map<String, Object> row,
			String traceId) {
		Instant start = Instant.now();
		String profileConvertToString = DCPEUtil.convertToString(profile);
		String profilrListConvertToString = DCPEUtil.convertToString(profileList);
		LOGGER.info("setupWifiAllCustomerRowsRequest : traceId={}|profile={}|profileList={}", traceId,
				profileConvertToString, profilrListConvertToString);
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
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(profileList);
		LOGGER.info("setupWifiAllCustomerRowsResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken,
				responseString);

		return profileList;

	}

	private void setupWifiCustomerRows(List<Profile> profileList, Profile profile, Map<String, Object> row,
			String traceId) {
		Instant start = Instant.now();
		String profileConvertToString = DCPEUtil.convertToString(profileList);
		String rowConvertToString = DCPEUtil.convertToString(row);
		LOGGER.info("setupWifiCustomerRowsRequest : traceId={}|profile={}|row={}", traceId, profileConvertToString,
				rowConvertToString);

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
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(accountList);
		LOGGER.info("setupWifiCustomerRowsResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken,
				responseString);

	}

}
