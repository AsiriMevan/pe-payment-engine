package lk.dialog.pe.persistence.service.impl;

import java.sql.SQLException;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.dto.PendingPayment;
import lk.dialog.pe.common.dto.QueryPaymentRequest;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.AccountTypeEnum;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.Receipt;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.common.util.SQLQueryEnum;
import lk.dialog.pe.common.util.Util;
import lk.dialog.pe.persistence.reader.repository.QueryExcecuterReaderRepository;
import lk.dialog.pe.persistence.service.PendingPaymentService;

import javax.annotation.Nullable;

@Service
public class PendingPaymentServiceImpl implements PendingPaymentService {

	@Autowired
	private QueryExcecuterReaderRepository queryExcecuterReaderRepository;

	@Autowired
	@Qualifier("paymentMap")
	private Map<String, String> map;

	private static final Logger LOGGER = LoggerFactory.getLogger(PendingPaymentServiceImpl.class);

	public final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssz", Locale.ENGLISH);

	private static final String SUBSIDIARY_CODE = "SUBSIDIARY_CODE";

	@Override
	public List<PendingPayment> getPendingPaymentsByBranchAndReceptNo(QueryPaymentRequest jsonInput, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(jsonInput);
		LOGGER.info("getPendingPaymentsByBranchAndReceptNoRequest : traceId={}|QueryPaymentRequest={}", traceId,
				requestString);
		Instant start = Instant.now();

		List<String> receiptList = new ArrayList<>();
		for (Receipt receipt : jsonInput.getReceipts()) {
			receiptList.add(receipt.getReceiptNo());
		}

		String receiptBranch = jsonInput.getReceiptBranch();

		Optional<ProductCategoryEnum> productCategoryEnum = ProductCategoryEnum
				.getProductCategory(jsonInput.getProductCategory());
		String productCategory = null;
		if (productCategoryEnum.isPresent()) {
			productCategory = productCategoryEnum.get().name();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info(
				"getPendingPaymentsByBranchAndReceptNoResponse : traceId={}|ProductCategory={}|ReceiptBranch={}|Receipts={}|timeTaken={}",
				traceId, productCategory, receiptBranch, receiptList, timeTaken);

		try {
			return getBranchAndReceptNo(receiptList, receiptBranch, productCategory, traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

	}

	@Override
	public List<PendingPayment> getPendingPaymentsByReceiptNoAndDate(QueryPaymentRequest jsonInput, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(jsonInput);
		LOGGER.info("getPendingPaymentsByReceiptNoAndDateRequest : traceId={}|QueryPaymentRequest={}", traceId,
				requestString);
		Instant start = Instant.now();

		String receiptBranch = jsonInput.getReceiptBranch();

		Optional<ProductCategoryEnum> productCategoryEnum = ProductCategoryEnum
				.getProductCategory(jsonInput.getProductCategory());
		String productCategory = null;
		if (productCategoryEnum.isPresent()) {
			productCategory = productCategoryEnum.get().name();
		}

		/*
		 * Long receiptFromDate = null;
		 * 
		 * if(jsonInput.getReceiptFromDate() != null) { receiptFromDate =
		 * LocalDate.parse(jsonInput.getReceiptFromDate(), dateFormatter)
		 * .atStartOfDay(ZoneOffset.UTC) .toInstant() .toEpochMilli(); }
		 * 
		 * Date fromDate = new Date(receiptFromDate);
		 * 
		 * 
		 */
		Calendar cal = Calendar.getInstance();

		Date fromDate = jsonInput.getReceiptFromDate();

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info(
				"getPendingPaymentsByReceiptNoAndDateResponse : traceId={}|ProductCategory={}|ReceiptBranch={}|fromDate={} calDate={} timeTaken={}",
				traceId, productCategory, receiptBranch, fromDate, cal.getTime(), timeTaken);

		try {
			return getBranchAndDate(productCategory, receiptBranch, sdf.format(fromDate), sdf.format(cal.getTime()),
					traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

	}

	@Override
	public List<PendingPayment> getPendingPaymentsByBranchAndCounter(QueryPaymentRequest jsonInput, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(jsonInput);
		LOGGER.info("getPendingPaymentsByBranchAndCounterRequest : traceId={}|QueryPaymentRequest={}", traceId,
				requestString);
		Instant start = Instant.now();

		String receiptBranch = jsonInput.getReceiptBranch();
		String branchCounter = jsonInput.getBranchCounter();

		Optional<ProductCategoryEnum> productCategoryEnum = ProductCategoryEnum
				.getProductCategory(jsonInput.getProductCategory());
		String productCategory = null;
		if (productCategoryEnum.isPresent()) {
			productCategory = productCategoryEnum.get().name();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info(
				"getPendingPaymentsByBranchAndCounterResponse : traceId={}|ProductCategory={}|ReceiptBranch={}|BranchCounter={}|timeTaken={}",
				traceId, productCategory, receiptBranch, branchCounter, timeTaken);
		try {
			return getBranchAndCounter(productCategory, receiptBranch, branchCounter, traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

	}

	@Override
	public List<PendingPayment> getPendingPaymentsByChqBranchAndChqNo(QueryPaymentRequest jsonInput, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(jsonInput);
		LOGGER.info("getPendingPaymentsByChqBranchAndChqNoRequest : traceId={}|QueryPaymentRequest={}", traceId,
				requestString);
		Instant start = Instant.now();

		String chqBranch = jsonInput.getChequebankBranchCode();
		String chqBank = jsonInput.getChequebankCode();
		String chqNo = jsonInput.getChequeNo();

		Optional<ProductCategoryEnum> productCategoryEnum = ProductCategoryEnum
				.getProductCategory(jsonInput.getProductCategory());
		String productCategory = null;
		if (productCategoryEnum.isPresent()) {
			productCategory = productCategoryEnum.get().name();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info(
				"getPendingPaymentsByChqBranchAndChqNoResponse : traceId={}|ProductCategory={}|ChqBranch={}|ChqBank={}|ChqNo={}|timeTaken={}",
				traceId, productCategory, chqBranch, chqBank, chqNo, timeTaken);
		try {
			return getChqBankAndChqNo(productCategory, chqBranch, chqBank, chqNo, traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

	}

	@Override
	public List<PendingPayment> getPendingPaymentsByChqNumber(QueryPaymentRequest jsonInput, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(jsonInput);
		LOGGER.info("getPendingPaymentsByChqNumberRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		Instant start = Instant.now();

		String chqNumber = jsonInput.getChequeNo();

		Optional<ProductCategoryEnum> productCategoryEnum = ProductCategoryEnum
				.getProductCategory(jsonInput.getProductCategory());
		String productCategory = null;
		if (productCategoryEnum.isPresent()) {
			productCategory = productCategoryEnum.get().name();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getPendingPaymentsByChqNumberResponse : traceId={}|ProductCategory={}|ChqNumber={}|timeTaken={}",
				traceId, productCategory, chqNumber, timeTaken);

		try {
			return getChqNumber(productCategory, chqNumber, traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

	}

	@Override
	public List<PendingPayment> getPendingPaymentsByFromDateToDate(QueryPaymentRequest jsonInput, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(jsonInput);
		LOGGER.info("getPendingPaymentsByFromDateToDateRequest : traceId={}|QueryPaymentRequest={}", traceId,
				requestString);
		Instant start = Instant.now();

		String contractId = jsonInput.getContractNo();

		Date fromDate = jsonInput.getReceiptFromDate();
		Date toDate = jsonInput.getReceiptToDate();

		Optional<ProductCategoryEnum> productCategoryEnum = ProductCategoryEnum
				.getProductCategory(jsonInput.getProductCategory());
		String productCategory = null;
		if (productCategoryEnum.isPresent()) {
			productCategory = productCategoryEnum.get().name();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info(
				"getPendingPaymentsByFromDateToDateResponse : traceId={}|ProductCategory={}|contractId={}|FromDate={}|ToDate={}|timeTaken={}",
				traceId, productCategory, contractId, fromDate, toDate, timeTaken);

		try {
			return getFromDateToDate(productCategory, contractId, sdf.format(fromDate), sdf.format(toDate), traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

	}

	@Override
	public List<PendingPayment> getPendingPaymentsByUserBranch(QueryPaymentRequest jsonInput, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(jsonInput);
		LOGGER.info("getPendingPaymentsByUserBranchRequest : traceId={}|QueryPaymentRequest={}",
				traceId,
				requestString);
		Instant start = Instant.now();

		String receiptUser = jsonInput.getReceiptUser();
		String receiptBranch = jsonInput.getReceiptBranch();

		Date fromDate = jsonInput.getReceiptFromDate();
		Date toDate = jsonInput.getReceiptToDate();

		Optional<ProductCategoryEnum> productCategoryEnum = ProductCategoryEnum
				.getProductCategory(jsonInput.getProductCategory());
		String productCategory = null;
		if (productCategoryEnum.isPresent()) {
			productCategory = productCategoryEnum.get().name();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info(
				"getPendingPaymentsByUserBranchResponse : traceId={}|ProductCategory={}|ReceiptUser={} |ReceiptBranch={}|FromDate={}|ToDate={}|timeTaken={}",
				traceId, productCategory, receiptUser, receiptBranch, fromDate, toDate, timeTaken);

		try {
			return getUserBranch(productCategory, receiptUser, receiptBranch, sdf.format(fromDate), sdf.format(toDate),
					traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}

	}

	@Override
	public List<PendingPayment> getPendingPaymentsByUserDate(QueryPaymentRequest jsonInput, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(jsonInput);
		LOGGER.info("getPendingPaymentsByUserDateRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		Instant start = Instant.now();

		String receiptUser = jsonInput.getReceiptUser();

		Date fromDate = jsonInput.getReceiptFromDate();
		Date toDate = jsonInput.getReceiptToDate();

		Optional<ProductCategoryEnum> productCategoryEnum = ProductCategoryEnum
				.getProductCategory(jsonInput.getProductCategory());
		String productCategory = null;
		if (productCategoryEnum.isPresent()) {
			productCategory = productCategoryEnum.get().name();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info(
				"getPendingPaymentsByUserDateResponse : traceId={}|ProductCategory={}|ReceiptUser={} |FromDate={}|ToDate={}|timeTaken={}",
				traceId, productCategory, receiptUser, fromDate, toDate, timeTaken);

		try {
			return getUserDate(productCategory, receiptUser, sdf.format(fromDate), sdf.format(toDate),
					traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}
	}

	@Override
	public List<PendingPayment> getPendingPaymentsByBranchfromToDate(QueryPaymentRequest jsonInput, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(jsonInput);
		LOGGER.info("getPendingPaymentsByBranchFromToDateRequest : traceId={}|QueryPaymentRequest={}", traceId,
				requestString);
		Instant start = Instant.now();

		String receiptBranch = jsonInput.getReceiptBranch();

		Date fromDate = jsonInput.getReceiptFromDate();
		Date toDate = jsonInput.getReceiptToDate();

		Optional<ProductCategoryEnum> productCategoryEnum = ProductCategoryEnum
				.getProductCategory(jsonInput.getProductCategory());
		String productCategory = null;
		if (productCategoryEnum.isPresent()) {
			productCategory = productCategoryEnum.get().name();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info(
				"getPendingPaymentsByBranchFromToDateResponse : traceId={}|ProductCategory={}|ReceiptBranch={} |FromDate={}|ToDate={}|timeTaken={}",
				traceId, productCategory, receiptBranch, fromDate, toDate, timeTaken);

		try {
			return getPendingPaymentsByBranchDate(productCategory, receiptBranch, sdf.format(fromDate),
					sdf.format(toDate), traceId);
		} catch (Exception e) {
			throw new DCPEException(e.getMessage());
		}
	}

	private List<PendingPayment> getBranchAndReceptNo(List<String> receipts, String receiptBranch,
			String productCategory, String traceId) throws SQLException {

		Instant start = Instant.now();
		LOGGER.info("getBranchAndReceptNoRequest: receiptBranch={}| TraceId={}", receiptBranch, traceId);

		List<Map<String, Object>> resultList = null;
		List<Map<String, Object>> dataMergeList = new ArrayList<>();
		StringBuilder strBuilder = new StringBuilder();
		String reciptStr;

		for (String receipt : receipts) {
			strBuilder.append("'" + receipt + "',");
		}

		String commonQuerySql = map.get(SQLQueryEnum.SQL_PEND_PAYMENTS.getValue());
		String querySql = commonQuerySql + map.get(SQLQueryEnum.SQL_PEND_PAYMENTS_BY_RCPTNO_BRANCH.getValue());
		reciptStr = strBuilder.deleteCharAt(strBuilder.lastIndexOf(",")).toString();
		querySql = querySql + reciptStr + ")" + " AND receipt_branch ='" + receiptBranch + "' AND product_category='"
				+ productCategory + "'";

		LOGGER.info("CompleteQuery: FinalQuery={}| TraceId={}", querySql, traceId);

		resultList = queryExcecuterReaderRepository.getDataByQuery(querySql, traceId);

		Map<String, Object> mergedMap = new HashMap<>();

		for (Map<String, Object> map : resultList) {

			for (Map.Entry<String, Object> entry : map.entrySet()) {
				String key = entry.getKey().toUpperCase();
				Object value = entry.getValue();

				if (mergedMap.containsKey(key)) {
					if (key.equals("OCS_ERR_CODE") || key.equals("OCS_ERR_DESC") || key.equals("DBN_ERR_DESC") || key.equals("CANCEL_ERR_CODE") || key.equals("CANCEL_ERR_DESC")){

						mergedMap.put(key, value);
					}
				} else {
					mergedMap.put(key, value);
				}
			}
		}

		dataMergeList.add(mergedMap);


		String responseString = DCPEUtil.convertToString(resultList);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getBranchAndReceptNoResponse: traceId={}| timeTaken={}| response={}", traceId, timeTaken,
				responseString);

		return convertResultList(dataMergeList, traceId);

	}

	private List<PendingPayment> getBranchAndDate(String productCategory, String receiptBranch, String fromDate,
			String getTime, String traceId) throws SQLException {

		Instant start = Instant.now();
		LOGGER.info("getBranchAndDateRequest: TraceId={}", traceId);

		LOGGER.info("Date 2 : traceId={}|fromDate={} |getTime={} ", traceId, fromDate, getTime);
		List<Map<String, Object>> resultList = null;

		String commonQuerySql = map.get(SQLQueryEnum.SQL_PEND_PAYMENTS.getValue());
		String querySql = commonQuerySql + map.get(SQLQueryEnum.SQL_PEND_PAYMENTS_BY_BRANCH_DATE.getValue());

		@Nullable
		Object[] params = { productCategory, receiptBranch, fromDate, getTime, fromDate, fromDate };

		LOGGER.info("Details : traceId={} | productCategory={} | receiptBranch={}|fromDate={}", traceId,
				productCategory, receiptBranch, fromDate);
		resultList = queryExcecuterReaderRepository.getData(querySql, params, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getBranchAndDateResponse: traceId={}|timeTaken={}|FinalQuery={}", traceId, timeTaken, querySql);
		return convertResultList(resultList, traceId);
	}

	private List<PendingPayment> getBranchAndCounter(String productCategory, String receiptBranch, String branchCounter,
			String traceId) throws SQLException {

		Instant start = Instant.now();
		LOGGER.info("getBranchAndCounterRequest : traceId={}", traceId);

		List<Map<String, Object>> resultList = null;
		String commonQuerySql = map.get(SQLQueryEnum.SQL_PEND_PAYMENTS.getValue());
		String querySql = commonQuerySql + map.get(SQLQueryEnum.SQL_PEND_PAYMENTS_BY_BRANCH_PAYMODE.getValue());

		@Nullable
		Object[] params = { productCategory, receiptBranch, branchCounter };

		LOGGER.info("Details : traceId={}|productCategory={}|receiptBranch={}|branchCounter={}| ", traceId,
				productCategory, receiptBranch, branchCounter);
		resultList = queryExcecuterReaderRepository.getData(querySql, params, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getBranchAndCounterResponse: traceId={}|timeTaken={}|FinalSQLQuery={}", traceId, timeTaken,
				querySql);
		return convertResultList(resultList, traceId);

	}

	private List<PendingPayment> getChqBankAndChqNo(String productCategory, String chqBranch, String chqBank,
			String chqNo, String traceId) throws SQLException {
		Instant start = Instant.now();
		List<Map<String, Object>> resultList = null;
		LOGGER.info("getChqBankAndChqNo: traceId={}", traceId);

		String commonQuerySql = map.get(SQLQueryEnum.SQL_PEND_PAYMENTS.getValue());
		String querySql = commonQuerySql + map.get(SQLQueryEnum.SQL_PEND_PAYMENTS_BY_BANK_BRANCH_CHQNO.getValue());

		@Nullable
		Object[] params = { productCategory, chqBranch, chqBank, chqNo };

		LOGGER.info("Details : traceId={}|productCategory={}|chqBranch={}|chqBank={}|chqNo={} :", traceId,
				productCategory, chqBranch, chqBank, chqNo);
		resultList = queryExcecuterReaderRepository.getData(querySql, params, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("CompleteQuery: traceId={}|timeTaken={}|FinalSQLQuery={}", traceId, timeTaken, querySql);
		return convertResultList(resultList, traceId);
	}

	private List<PendingPayment> getChqNumber(String productCategory, String chqNumber, String traceId)
			throws SQLException {
		Instant start = Instant.now();
		LOGGER.info("getChqNumberRequest: traceId={}", traceId);

		List<Map<String, Object>> resultList = null;

		String commonQuerySql = map.get(SQLQueryEnum.SQL_PEND_PAYMENTS.getValue());
		String querySql = commonQuerySql + map.get(SQLQueryEnum.SQL_PEND_PAYMENTS_BY_CHQNO.getValue());

		@Nullable
		Object[] params = { productCategory, chqNumber };

		LOGGER.info("Details : traceId={}|productCategory={}|chqNumber={}:", traceId, productCategory, chqNumber);
		resultList = queryExcecuterReaderRepository.getData(querySql, params, traceId);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getChqNumber: traceId={}|timeTaken={}|FinalQuery={}", traceId, timeTaken, querySql);
		return convertResultList(resultList, traceId);
	}

	private List<PendingPayment> getFromDateToDate(String productCategory, String contractId, String fromDate,
			String toDate, String traceId) throws SQLException {

		Instant start = Instant.now();
		LOGGER.info("getFromDateToDateRequest: traceId={}", traceId);

		List<Map<String, Object>> resultList = null;

		String commonQuerySql = map.get(SQLQueryEnum.SQL_PEND_PAYMENTS.getValue());
		String querySql = commonQuerySql + map.get(SQLQueryEnum.SQL_PEND_PAYMENTS_BY_CONTRACT_DATE.getValue());

		@Nullable
		Object[] params = { productCategory, contractId, fromDate, toDate, fromDate, toDate };

		LOGGER.info("Details : traceId={}|productCategory={}|contractId={}|fromDate={}|toDate{} :", traceId,
				productCategory, contractId, fromDate, toDate);
		resultList = queryExcecuterReaderRepository.getData(querySql, params, traceId);

		String responseString = DCPEUtil.convertToString(resultList);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getFromDateToDateResponse: traceId={}|timeTaken={}|response={}", querySql, timeTaken,
				responseString);
		return convertResultList(resultList, traceId);
	}

	private List<PendingPayment> getUserBranch(String productCategory, String receiptUser, String receiptBranch,
			String fromDate, String toDate, String traceId) throws SQLException {
		Instant start = Instant.now();
		LOGGER.info("getUserBranchRequest: traceId={}", traceId);

		List<Map<String, Object>> resultList = null;

		String commonQuerySql = map.get(SQLQueryEnum.SQL_PEND_PAYMENTS.getValue());
		String querySql = commonQuerySql + map.get(SQLQueryEnum.SQL_PEND_PAYMENTS_BY_BRANCH_USER_DATE.getValue());

		@Nullable
		Object[] params = { productCategory, receiptUser, receiptBranch, fromDate, toDate, fromDate, toDate };

		LOGGER.info(
				"Details : traceId={}|productCategory={}|receiptUser={}|getPendingPaymentsByUserBranchResponse={}|fromDate={}|toDate={} :",
				traceId, productCategory, receiptUser, receiptBranch, fromDate, toDate);
		resultList = queryExcecuterReaderRepository.getData(querySql, params, traceId);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getUserBranch: query={}|traceId={}|timeTaken={}", querySql, traceId, timeTaken);
		return convertResultList(resultList, traceId);
	}

	private List<PendingPayment> getUserDate(String productCategory, String receiptUser, String fromDate, String toDate,
			String traceId) throws SQLException {
		Instant start = Instant.now();
		LOGGER.info("getUserDateRequest : traceId={}|productCategory={}|receiptUser={}|fromDate={}|toDate={}", traceId,
				productCategory, receiptUser, fromDate, toDate);

		List<Map<String, Object>> resultList = null;

		String commonQuerySql = map.get(SQLQueryEnum.SQL_PEND_PAYMENTS.getValue());
		String querySql = commonQuerySql + map.get(SQLQueryEnum.SQL_PEND_PAYMENTS_BY_USER_DATE.getValue());

		@Nullable
		Object[] params = { productCategory, receiptUser, fromDate, toDate, fromDate, toDate };

		LOGGER.info("Details : traceId={}|productCategory={}|receiptUser={}|fromDate={}|toDate={} :", traceId,
				productCategory, receiptUser, fromDate, toDate);
		resultList = queryExcecuterReaderRepository.getData(querySql, params, traceId);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getUserDate: sqlquery={}| traceId={}| timeTaken={}", querySql, traceId, timeTaken);
		return convertResultList(resultList, traceId);
	}

	private List<PendingPayment> getPendingPaymentsByBranchDate(String productCategory, String receiptBranch,
			String fromDate, String toDate, String traceId) throws SQLException {
		Instant start = Instant.now();
		List<Map<String, Object>> resultList = null;
		LOGGER.info(
				"getPendingPaymentsByBranchDateRequest: traceId={}|productCategory={}|receiptBranch={}|fromDate={}|toDate={}",
				traceId, productCategory, receiptBranch, fromDate, toDate);

		String commonQuerySql = map.get(SQLQueryEnum.SQL_PEND_PAYMENTS.getValue());
		String querySql = commonQuerySql + map.get(SQLQueryEnum.SQL_PEND_PAYMENTS_BY_BRANCH_DATE.getValue());

		@Nullable
		Object[] params = { productCategory, receiptBranch, fromDate, toDate, fromDate, toDate };

		LOGGER.info("Details : traceId={}|productCategory={}|receiptBranch={}|fromDate={}|toDate={}:", traceId,
				productCategory, receiptBranch, fromDate, toDate);
		resultList = queryExcecuterReaderRepository.getData(querySql, params, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(resultList);
		LOGGER.info("getPendingPaymentsByBranchDateResponse: traceId={}|timeTaken={}| response={}", traceId, timeTaken,
				responseString);
		return convertResultList(resultList, traceId);
	}

	private List<PendingPayment> convertResultList(List<Map<String, Object>> resultList, String traceId) {

		Instant start = Instant.now();
		String requestString = DCPEUtil.convertToString(resultList);
		LOGGER.info("convertResultListRequest: traceId={}| response={}", traceId, requestString);

		StringBuilder strBuilder = null;
		List<PendingPayment> payments = new ArrayList<>();

		for (Map<String, Object> row : resultList) {
			PendingPayment pendingPayment = new PendingPayment();

			pendingPayment = setupPandingPayment(row, traceId);
			pendingPayment.setSbu(SBUEnum.getSBUTypeIntfromString((String) row.get(SUBSIDIARY_CODE)));
			String acctType = (String) row.get("CONNECTION_TYPE");
			if (acctType != null && !acctType.isEmpty())
				pendingPayment.setAccountType(AccountTypeEnum.getAccountType(acctType).valueOf());
			if (row.get("PRODUCT_TYPE") != null)
				pendingPayment.setProductType((int) ((Double) row.get("PRODUCT_TYPE")).doubleValue());
			/* set NE element statuses */
			strBuilder = setNEelementStatus(row, traceId);
			if (strBuilder != null && strBuilder.capacity() > 0 && strBuilder.indexOf("|") > 0)
				pendingPayment.setTransferErrDesc(strBuilder.deleteCharAt(strBuilder.lastIndexOf("|")).toString());
			if (row.get("UPDATE_DATE") != null)
				pendingPayment.setLastTransferRecordedTime(
						Util.timeStampToCalender((java.sql.Timestamp) row.get("UPDATE_DATE")));
			payments.add(pendingPayment);
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(payments);
		LOGGER.info("convertResultListResponse: traceId={}|timeTaken={}|response={}", traceId, timeTaken,
				responseString);

		return payments;

	}

	private StringBuilder setNEelementStatus(Map<String, Object> row, String traceId) {
		Instant start = Instant.now();
		String rowString = DCPEUtil.convertToString(row);
		LOGGER.info("setNEelementStatusRequest: traceId={}|request={}", traceId, rowString);

		StringBuilder strBuilder = new StringBuilder();
		if ((String) row.get("RBM_ERR_CODE") != null)
			strBuilder.append("RBM:" + (String) row.get("RBM_ERR_DESC") + "|");
		if ((String) row.get("OCS_ERR_CODE") != null)
			strBuilder.append("OCS:" + (String) row.get("OCS_ERR_DESC") + "|");
		if ((String) row.get("DBN_ERR_CODE") != null)
			strBuilder.append("TELBIZ:" + (String) row.get("DBN_ERR_DESC") + "|");
		if ((String) row.get("CANCEL_ERR_CODE") != null && row.get(SUBSIDIARY_CODE) != null
				&& "DBN".equals(row.get(SUBSIDIARY_CODE)))
			strBuilder.append("TELBIZ:" + (String) row.get("CANCEL_ERR_DESC") + "|");
		else if ((String) row.get("CANCEL_ERR_CODE") != null)
			strBuilder.append("RBM:" + (String) row.get("CANCEL_ERR_DESC") + "|");
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("setNEelementStatusResponse: traceId={}|timeTaken={}|strBuilder={}", traceId, timeTaken,
				strBuilder);

		return strBuilder;
	}

	private PendingPayment setupPandingPayment(Map<String, Object> row, String traceId) {
		Instant start = Instant.now();
		String rowString = DCPEUtil.convertToString(row);
		LOGGER.info("setupPandingPaymentRequest: traceId={}| row={}", traceId, rowString);

		PendingPayment pendingPayment = new PendingPayment();
		pendingPayment.setConnRef((String) row.get("CONN_REF"));
		if (row.get("CONTRACT_ID") != null)
			pendingPayment.setContractNo((String) row.get("CONTRACT_ID"));
		pendingPayment.setPaymentCancelledReason((String) row.get("CANCELLED_REASON"));
		pendingPayment.setPaymentCancelledUser((String) row.get("CANCELLED_USER"));
		pendingPayment.setPaymentCancelledReason((String) row.get("CANCELLED_REASON"));
		pendingPayment.setCancelledDtm(Util.timeStampToCalender((java.sql.Timestamp) row.get("CANCELLED_DATE")));
		if (row.get("SUBSCRIBER_NODE_ID") != null)
			pendingPayment.setAccountNo((String) row.get("SUBSCRIBER_NODE_ID"));
		if (row.get("PAYMENT_AMOUNT") != null)
			pendingPayment.setAccountPaymentMny(((java.math.BigDecimal) row.get("PAYMENT_AMOUNT")).doubleValue());
		pendingPayment.setReceiptNo((String) row.get("RECEIPT_NUMBER"));
		pendingPayment.setReceiptBranch((String) row.get("RECEIPT_BRANCH"));
		pendingPayment.setBranchCounter((String) row.get("BRANCH_COUNTER"));
		pendingPayment.setReceiptUser((String) row.get("RECEIPT_USER"));
		if (row.get("RECEIPT_DATE") != null)
			pendingPayment.setReceiptDate(Util.timeStampToCalender((java.sql.Timestamp) row.get("RECEIPT_DATE")));
		pendingPayment.setPaymentMode((String) row.get("PAYMENT_MODE"));
		pendingPayment.setPaymentRefNo((String) row.get("PAYMENT_TEXT"));
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("setupPandingPaymentResponse: traceId={}|timeTaken={}|responseString={}", traceId, timeTaken,
				responseString);
		return pendingPayment;
	}

}
