package lk.dialog.pe.retrieval.cancelation.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.dialog.pe.common.dto.QueryPaymentRequest;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.AccountTypeEnum;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.RestExecutor;
import lk.dialog.pe.common.util.SBUEnum;

import lk.dialog.pe.retrieval.cancelation.domain.PendingPaymentView;
import lk.dialog.pe.retrieval.cancelation.domain.QueryPendingPaymentResponse;
import lk.dialog.pe.retrieval.cancelation.service.QueryPendingPaymentService;

@Service
public class QueryPendingPaymentServiceImpl implements QueryPendingPaymentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryPendingPaymentServiceImpl.class);

	@Autowired
	private RestExecutor restExecutor;

	@Value("${pe.persistance.url.pending-payment.branchId-receiptNo}")
	private String branchIdreceiptNoEndPoint;

	@Value("${pe.persistance.url.pending-payment.receiptNo-Date}")
	private String receiptNofromDateEndPoint;

	@Value("${pe.persistance.url.pending-payment.branchId-counter}")
	private String counterReceiptBranchEndPoint;

	@Value("${pe.persistance.url.pending-payment.chqBranch-chqNo}")
	private String chqBankChqNoEndPoint;

	@Value("${pe.persistance.url.pending-payment.chqNo}")
	private String chqNoEndPoint;

	@Value("${pe.persistance.url.pending-payment.fromDate-toDate}")
	private String fromDateToDateEndPoint;

	@Value("${pe.persistance.url.pending-payment.user-branch}")
	private String userBranchEndPoint;

	@Value("${pe.persistance.url.pending-payment.user-date}")
	private String userDateEndPoint;

	@Value("${pe.persistance.url.pending-payment.branch-fromTodate}")
	private String branchDateEndPoint;

	@Autowired
	ObjectMapper mapper;

	private List<PendingPaymentView> getPendingPaymentsByBranchAndReceptNo(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		LOGGER.info("getPendingPaymentsByBranchAndReceptNoRequest : traceId={}|QueryPaymentRequest={}", traceId, jsonReq);
		Instant start = Instant.now();
		List<PendingPaymentView> aList = new ArrayList<>();

		JsonNode json = null;
		try {
			json = restExecutor.post(branchIdreceiptNoEndPoint, traceId, jsonReq, JsonNode.class);
		} catch (lk.dialog.pe.common.exception.DCPEException e) {
			LOGGER.error("getPendingPaymentsByBranchAndReceptNoResponse : traceId={}|error={}", traceId, e.getMessage(), e);

		}

		for (JsonNode jsonNode : json) {
			PendingPaymentView pl = mapper.convertValue(jsonNode, PendingPaymentView.class);
			aList.add(pl);
		}

		Long end = Duration.between(start, Instant.now()).toMillis();
		String aListConverted = DCPEUtil.convertToString(aList);
		LOGGER.info("getPendingPaymentsByBranchAndReceptNoResponse : timeTaken={}|traceId={}|json={}| aListConverted={}", end, traceId, json, aListConverted);
		return aList;

	}

	private List<PendingPaymentView> getPendingPaymentsByBranchAndDate(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		LOGGER.info("getPendingPaymentsByBranchAndDateRequest : traceId={}|QueryPaymentRequest={}", traceId, jsonReq);
		Instant start = Instant.now();
		List<PendingPaymentView> aList = new ArrayList<>();

		JsonNode json = null;
		try {
			json = restExecutor.post(receiptNofromDateEndPoint, traceId, jsonReq, JsonNode.class);
		} catch (DCPEException e) {
			LOGGER.error("getPendingPaymentsByBranchAndDateResponse : traceId={}|error={}", traceId, e.getMessage(), e);
		}

		for (JsonNode jsonNode : json) {
			PendingPaymentView pl = mapper.convertValue(jsonNode, PendingPaymentView.class);
			aList.add(pl);
		}
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("getPendingPaymentsByBranchAndDateResponse : timeTaken={}|traceId={}|json={}", end, traceId, json);
		return aList;

	}

	private List<PendingPaymentView> getPendingPaymentsByBranchAndCounter(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		LOGGER.info("getPendingPaymentsByBranchAndCounterRequest : traceId={}|QueryPaymentRequest={}", traceId, jsonReq);
		Instant start = Instant.now();
		List<PendingPaymentView> aList = new ArrayList<>();

		JsonNode json = null;
		try {
			json = restExecutor.post(counterReceiptBranchEndPoint, traceId, jsonReq, JsonNode.class);
		} catch (DCPEException e) {
			LOGGER.error("getPendingPaymentsByBranchAndCounterResponse : traceId={}|error={}", traceId, e.getMessage(), e);
		}

		for (JsonNode jsonNode : json) {
			PendingPaymentView pl = mapper.convertValue(jsonNode, PendingPaymentView.class);
			aList.add(pl);
		}
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("getPendingPaymentsByBranchAndCounterResponse : timeTaken={}|traceId={}|json={}", end, traceId, json);
		return aList;

	}

	private List<PendingPaymentView> getPendingPaymentsByChqBankAndChqNo(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		LOGGER.info("getPendingPaymentsByChqBankAndChqNoRequest : traceId={}|QueryPaymentRequest={}", traceId, jsonReq);
		Instant start = Instant.now();
		List<PendingPaymentView> aList = new ArrayList<>();

		JsonNode json = null;
		try {
			json = restExecutor.post(chqBankChqNoEndPoint, traceId, jsonReq, JsonNode.class);
		} catch (DCPEException e) {
			LOGGER.error("getPendingPaymentsByChqBankAndChqNoResponse : traceId={}|error={}", traceId, e.getMessage(), e);
		}

		for (JsonNode jsonNode : json) {
			PendingPaymentView pl = mapper.convertValue(jsonNode, PendingPaymentView.class);
			aList.add(pl);
		}
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("getPendingPaymentsByChqBankAndChqNoResponse : timeTaken={}|traceId={}|json={}", end, traceId, json);
		return aList;

	}

	private List<PendingPaymentView> getPendingPaymentsByChqNumber(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		LOGGER.info("getPendingPaymentsByChqNumberRequest : traceId={}|QueryPaymentRequest={}", traceId, jsonReq);
		Instant start = Instant.now();
		List<PendingPaymentView> aList = new ArrayList<>();

		JsonNode json = null;
		try {
			json = restExecutor.post(chqNoEndPoint, traceId, jsonReq, JsonNode.class);
		} catch (DCPEException e) {
			LOGGER.error("getPendingPaymentsByChqNumberResponse : traceId={}|error={}", traceId, e.getMessage(), e);
		}

		for (JsonNode jsonNode : json) {
			PendingPaymentView pl = mapper.convertValue(jsonNode, PendingPaymentView.class);
			aList.add(pl);
		}
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("getPendingPaymentsByChqNumberResponse : timeTaken={}|traceId={}|json={}", end, traceId, json);
		return aList;

	}

	private List<PendingPaymentView> getPendingPaymentsByFromDateToDate(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		LOGGER.info("getPendingPaymentsByFromDateToDateRequest : traceId={}|QueryPaymentRequest={}", traceId, jsonReq);
		Instant start = Instant.now();
		List<PendingPaymentView> aList = new ArrayList<>();

		JsonNode json = null;
		try {
			json = restExecutor.post(fromDateToDateEndPoint, traceId, jsonReq, JsonNode.class);
		} catch (DCPEException e) {
			LOGGER.error("getPendingPaymentsByFromDateToDateResponse : traceId={}|error={}", traceId, e.getMessage(), e);
		}

		for (JsonNode jsonNode : json) {
			PendingPaymentView pl = mapper.convertValue(jsonNode, PendingPaymentView.class);
			aList.add(pl);
		}
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("getPendingPaymentsByFromDateToDateResponse : timeTaken={}|traceId={}|json={}", end, traceId, json);
		return aList;

	}

	private List<PendingPaymentView> getPendingPaymentsByUserBranch(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		LOGGER.info("getPendingPaymentsByUserBranchRequest : traceId={}|QueryPaymentRequest={}", traceId, jsonReq);
		Instant start = Instant.now();
		List<PendingPaymentView> aList = new ArrayList<>();

		JsonNode json = null;
		try {
			json = restExecutor.post(userBranchEndPoint, traceId, jsonReq, JsonNode.class);
		} catch (DCPEException e) {
			LOGGER.error("getPendingPaymentsByUserBranchResponse : traceId={}|error={}", traceId, e.getMessage(), e);
		}

		for (JsonNode jsonNode : json) {
			PendingPaymentView pl = mapper.convertValue(jsonNode, PendingPaymentView.class);
			aList.add(pl);
		}
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("getPendingPaymentsByUserBranchResponse : timeTaken={}|traceId={}|json={}", end, traceId, json);
		return aList;

	}

	private List<PendingPaymentView> getPendingPaymentsByUserDate(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		LOGGER.info("getPendingPaymentsByUserDateRequest : traceId={}|QueryPaymentRequest={}", traceId, jsonReq);
		Instant start = Instant.now();
		List<PendingPaymentView> aList = new ArrayList<>();

		JsonNode json = null;
		try {
			json = restExecutor.post(userDateEndPoint, traceId, jsonReq, JsonNode.class);
		} catch (DCPEException e) {
			LOGGER.error("getPendingPaymentsByUserDateResponse : traceId={}|error={}", traceId, e.getMessage(), e);
		}

		for (JsonNode jsonNode : json) {
			PendingPaymentView pl = mapper.convertValue(jsonNode, PendingPaymentView.class);
			aList.add(pl);
		}
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("getPendingPaymentsByUserDateResponse : timeTaken={}|traceId={}|json={}", end, traceId, json);
		return aList;

	}

	private List<PendingPaymentView> getPendingPaymentsByBranchDate(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		LOGGER.info("getPendingPaymentsByBranchDateRequest : traceId={}|QueryPaymentRequest={}", traceId, jsonReq);
		Instant start = Instant.now();
		List<PendingPaymentView> aList = new ArrayList<>();

		JsonNode json = restExecutor.post(branchDateEndPoint, traceId, jsonReq, JsonNode.class);

		for (JsonNode jsonNode : json) {
			PendingPaymentView pl = mapper.convertValue(jsonNode, PendingPaymentView.class);
			aList.add(pl);
		}
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("getPendingPaymentsByBranchDateResponse : timeTaken={}|traceId={}|json={}", end, traceId, json);
		return aList;

	}

	@Override
	public QueryPendingPaymentResponse queryPendingPayment(QueryPaymentRequest jsonReq, String traceId) throws DCPEException {

		List<PendingPaymentView> payments = new ArrayList<>();
		List<PendingPaymentView> resultList;

		if (jsonReq.getProductCategory() != null) {
			ProductCategoryEnum productCategoryObj = ProductCategoryEnum.getProductCategoryByValue(jsonReq.getProductCategory());

			if (productCategoryObj != null) {

				String requestString = DCPEUtil.convertToString(productCategoryObj);
				LOGGER.info("queryPendingPaymentRequest :   traceId={}|productCategoryObj={}", traceId, requestString);

				if (jsonReq.getReceiptBranch() != null && !jsonReq.getReceiptBranch().isEmpty() && jsonReq.getReceipts() != null && !jsonReq.getReceipts().isEmpty()
						&& jsonReq.getReceipts().get(0) != null && jsonReq.getReceipts().get(0).getReceiptNo() != null && !jsonReq.getReceipts().get(0).getReceiptNo().isEmpty()) {
					/* query by receipt branch + receipt no */
					resultList = getPendingPaymentsByBranchAndReceptNo(jsonReq, traceId);

				} else if (jsonReq.getReceiptBranch() != null && !jsonReq.getReceiptBranch().isEmpty() && jsonReq.getReceiptFromDate() != null && jsonReq.getReceiptToDate() == null) {
					/* query by receipt branch + date */
					resultList = getPendingPaymentsByBranchAndDate(jsonReq, traceId);

				} else if (jsonReq.getReceiptBranch() != null && !jsonReq.getReceiptBranch().isEmpty() && jsonReq.getBranchCounter() != null && !jsonReq.getBranchCounter().isEmpty()) {
					/*
					 * query by receipt branch + branch counter (payment mode)
					 */

					resultList = getPendingPaymentsByBranchAndCounter(jsonReq, traceId);

				} else if (jsonReq.getChequeNo() != null && !jsonReq.getChequeNo().isEmpty() && jsonReq.getChequebankCode() != null && !jsonReq.getChequebankCode().isEmpty()
						&& jsonReq.getChequebankBranchCode() != null && !jsonReq.getChequebankBranchCode().isEmpty()) {
					/* query by bank code + branch code + chq no */

					resultList = getPendingPaymentsByChqBankAndChqNo(jsonReq, traceId);

				} else if (jsonReq.getChequeNo() != null && !jsonReq.getChequeNo().isEmpty()) {
					/* query by cheque no */

					resultList = getPendingPaymentsByChqNumber(jsonReq, traceId);

				} else if (jsonReq.getContractNo() != null && !jsonReq.getContractNo().isEmpty() && jsonReq.getReceiptFromDate() != null && jsonReq.getReceiptToDate() != null) {
					/* query by contract no + from-date + to-date */

					resultList = getPendingPaymentsByFromDateToDate(jsonReq, traceId);

				} else if (jsonReq.getReceiptUser() != null && !jsonReq.getReceiptUser().isEmpty() && jsonReq.getReceiptBranch() != null && !jsonReq.getReceiptBranch().isEmpty()
						&& jsonReq.getReceiptFromDate() != null && jsonReq.getReceiptToDate() != null) {
					/*
					 * query by receipt-user + receipt-branch + from-date + to-date
					 */
					resultList = getPendingPaymentsByUserBranch(jsonReq, traceId);

				} else if (jsonReq.getReceiptUser() != null && !jsonReq.getReceiptUser().isEmpty() && jsonReq.getReceiptFromDate() != null && jsonReq.getReceiptToDate() != null) {
					/* query by receipt-user + from-date + to-date */

					resultList = getPendingPaymentsByUserDate(jsonReq, traceId);
				} else if (jsonReq.getReceiptBranch() != null && !jsonReq.getReceiptBranch().isEmpty() && jsonReq.getReceiptFromDate() != null && jsonReq.getReceiptToDate() != null) {
					/* query by receipt branch + date */
					resultList = getPendingPaymentsByBranchDate(jsonReq, traceId);
				} else {
					throw new DCPEException("Invalid query parameter combination");
				}

				for (PendingPaymentView row : resultList) {

					PendingPaymentView pendingPayment = new PendingPaymentView();

					pendingPayment.setSbu(row.getSbu() != null ? SBUEnum.getSBUByValue(row.getSbu()).valueOf() : -1);
					pendingPayment.setConnRef(row.getConnRef());
					if (row.getContractNo() != null)
						pendingPayment.setContractNo(row.getContractNo());
					pendingPayment.setPaymentCancelledReason(row.getPaymentCancelledReason());
					pendingPayment.setPaymentCancelledUser(row.getPaymentCancelledUser());
					pendingPayment.setCancelledDtm(row.getCancelledDtm());

					if (row.getAccountNo() != null)
						pendingPayment.setAccountNo(row.getAccountNo());
					if (row.getAccountPaymentMny() != null) {
						pendingPayment.setAccountPaymentMny((row.getAccountPaymentMny()));
						pendingPayment.setReceiptNo(row.getReceiptNo());
						pendingPayment.setReceiptBranch(row.getReceiptBranch());
						pendingPayment.setBranchCounter(row.getBranchCounter());
						pendingPayment.setReceiptUser(row.getReceiptUser());
					}
					if (row.getReceiptDate() != null)
						pendingPayment.setReceiptDate(row.getReceiptDate());
					pendingPayment.setPaymentMode(row.getPaymentMode());
					pendingPayment.setPaymentRefNo(row.getPaymentRefNo());
					Integer acctType = row.getAccountType();

					if (acctType != null) {
						pendingPayment.setAccountType(AccountTypeEnum.getAccountType(acctType).valueOf());
						pendingPayment.setProductType(row.getProductType());
					}
					pendingPayment.setTransferErrDesc(row.getTransferErrDesc());
					pendingPayment.setLastTransferRecordedTime(row.getLastTransferRecordedTime());
					payments.add(pendingPayment);

				}

			} else {
				throw new DCPEException(Constants.ERR_DESC_INVALID_PC);
			}
		} else {
			throw new DCPEException(Constants.ERR_DESC_INVALID_PC);
		}

		//DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		QueryPendingPaymentResponse lastRes = new QueryPendingPaymentResponse();
		lastRes.setPayments(payments);
		
		if(!lastRes.getPayments().isEmpty()) {
		
		if(lastRes.getPayments().get(0).getReceiptDate()!=null) {
			/*
			 * String beforeConvertReceiptDate
			 * =lastRes.getPayments().get(0).getReceiptDate(); OffsetDateTime dtReceiptDate
			 * = OffsetDateTime.parse(beforeConvertReceiptDate); String
			 * afterConvertReceiptDate = fmt.format(dtReceiptDate);
			 */
		lastRes.getPayments().get(0).setReceiptDate(lastRes.getPayments().get(0).getReceiptDate());
		}
		
		
		if(lastRes.getPayments().get(0).getLastTransferRecordedTime()!=null) {
			/*
			 * String beforeLastTransferRecordedTime
			 * =lastRes.getPayments().get(0).getLastTransferRecordedTime(); OffsetDateTime
			 * dtLastTransferRecordedTime =
			 * OffsetDateTime.parse(beforeLastTransferRecordedTime); String
			 * afterConvertLastTransferRecordedTime =
			 * fmt.format(dtLastTransferRecordedTime);
			 */
		lastRes.getPayments().get(0).setLastTransferRecordedTime(lastRes.getPayments().get(0).getLastTransferRecordedTime());
		}
		
		if(lastRes.getPayments().get(0).getCancelledDtm()!=null) {
			/*
			 * String beforeCancelledDtm =lastRes.getPayments().get(0).getCancelledDtm();
			 * OffsetDateTime dtCancelledDtm = OffsetDateTime.parse(beforeCancelledDtm);
			 * String afterCancelledDtm = fmt.format(dtCancelledDtm);
			 */
		lastRes.getPayments().get(0).setCancelledDtm(lastRes.getPayments().get(0).getCancelledDtm());
		}
		
		}

		LOGGER.info("final Response : traceId={}|FinalResponse={}", traceId, lastRes);
		return lastRes;

	}

}
