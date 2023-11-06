package lk.dialog.pe.retrieval.cancelation.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.qos.logback.classic.Logger;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.NEConstants;
import lk.dialog.pe.retrieval.cancelation.domain.PaymentDTO;
import lk.dialog.pe.retrieval.cancelation.service.MifeIntegrationService;
import lk.dialog.pe.retrieval.cancelation.util.QueryPaymentsSummery;
import lk.dialog.pe.credit.cam.dto.AccountPKRS;
import lk.dialog.pe.credit.cam.dto.AccountPaymentData;
import lk.dialog.pe.credit.cam.dto.DbtAccountPaymentDataResult;
import lk.dialog.pe.credit.cam.dto.DbtPaymentExtensionData;
import lk.dialog.pe.credit.cam.dto.DbtQueryAllPaymentDetailsRequest;
import lk.dialog.pe.credit.cam.dto.DbtQueryAllPaymentDetailsResponse;
import lk.dialog.pe.credit.cam.service.common.MifeApiService;
import lk.dialog.pe.credit.cam.util.MifeUrlEnum;

@Service
public class MifeIntegrationServiceImpl implements MifeIntegrationService {

	private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MifeIntegrationServiceImpl.class);

	@Autowired
	private MifeApiService mifeApiService;

	@Override
	public List<PaymentDTO> queryPaymentsSummery(QueryPaymentsSummery queryPaymentsSummery, String receiptNo,
			String traceId) throws DCPEException, ParseException {

		final List<PaymentDTO> paymentList = new ArrayList<>();

		final DbtQueryAllPaymentDetailsRequest dbtQueryPaymentInput = new DbtQueryAllPaymentDetailsRequest();

		if (queryPaymentsSummery.getContractNo() != null && !"".equals(queryPaymentsSummery.getContractNo())) {
			AccountPKRS accountPKRS = new AccountPKRS();
			accountPKRS.setConnectionRef(queryPaymentsSummery.getContractNo());
			dbtQueryPaymentInput.setAccountPKRs(accountPKRS);

		}

		dbtQueryPaymentInput.setReceiptBranch(null); // null happnes ->2
		dbtQueryPaymentInput.setChequeNum(queryPaymentsSummery.getChequeNo());
		dbtQueryPaymentInput.setBranchCounter(null); // null happnes ->1

		if (queryPaymentsSummery.getReceiptFromDate() != null) {
			
			String fromDate = new DCPEUtil().convertStringtoDateFormat(queryPaymentsSummery.getReceiptFromDate());
			dbtQueryPaymentInput.setReceiptFromDate(fromDate);
		}
		if (queryPaymentsSummery.getReceiptToDate() != null) {
			String toDate = new DCPEUtil().convertStringtoDateFormat(queryPaymentsSummery.getReceiptToDate());
			dbtQueryPaymentInput.setReceiptToDate(toDate);
		}

		dbtQueryPaymentInput.setReceiptNumber(receiptNo);

		DbtQueryAllPaymentDetailsResponse dbtQueryAllPaymentDetailsResponse = null;
		try {

			dbtQueryAllPaymentDetailsResponse = mifeApiService.post(MifeUrlEnum.RBM_DBT_QUERY_ALL_PAYMENT_DETAILS,
					dbtQueryPaymentInput, traceId);

		} catch (Exception ex) {
			throw new DCPEException(ex.getMessage(), DCPEErrorCode.INTERNAL_SERVER_ERROR.getStatus());
		}

		final DbtAccountPaymentDataResult[] queryPaymentDataResult = dbtQueryAllPaymentDetailsResponse
				.getDbtAccountPaymentDataResultRs();

		if (queryPaymentDataResult != null) {
			/* payments data available */
			for (final DbtAccountPaymentDataResult result : queryPaymentDataResult) {
				this.addPayment(result, paymentList, traceId);
			}
		}

		String respaymentList = DCPEUtil.convertToString(paymentList);

		LOGGER.info("QueryPaymentsSummery : traceId={}|PaymentList={}", traceId, respaymentList);

		return paymentList;
	}

	private void addPayment(DbtAccountPaymentDataResult result, List<PaymentDTO> paymentList, String traceId) {
		final PaymentDTO payment = new PaymentDTO();

		final AccountPaymentData accountPaymentData = result.getAccountPaymentDataRs();

		if (accountPaymentData != null) {
			/* main payments data available */
			payment.setContractNo(accountPaymentData.getAccountNo());

			payment.setAccountPaymentDate(accountPaymentData.getAccountPaymentDat());
			payment.setAccountPaymentMny(
					accountPaymentData.getAccountPaymentMny() / NEConstants.RBM_AMOUNT_DIVISER);
			payment.setAccountPaymentRef(accountPaymentData.getAccountPaymentRef());
			payment.setAccountPaymentSeq(accountPaymentData.getAccountPaymentSeq());
			payment.setAccountPaymentStatus(accountPaymentData.getAccountPaymentStatus());
			payment.setAccountPaymentText(accountPaymentData.getAccountPaymentText());
			payment.setAccountType(NEConstants.POSTPAID);
			payment.setCancelledDtm(accountPaymentData.getCancelledDtm());
			payment.setAccountNo(accountPaymentData.getCustomerPK().getCustomerRef());
			payment.setPhysicalPaymentSeq(accountPaymentData.getPhysicalPaymentPkRs().getPhysicalPaymentSeq());
			payment.setCreatedDtm(accountPaymentData.getCreatedDtm());
			payment.setSbu(-1);
			payment.setProductType(-1);

		}

		final DbtPaymentExtensionData dbtExtensionData = result.getDbtPaymentExtensionDataRs();
		if (dbtExtensionData != null) {
			/* payments extension data available */
			payment.setBranchCounter(dbtExtensionData.getBranchCounter());
			payment.setCancelledByUser(dbtExtensionData.getPaymentCancelledUser());
			payment.setPaymentCancelledReason(dbtExtensionData.getPaymentCancelledReason());
			payment.setReceiptBranch(dbtExtensionData.getReceiptBranchName());
			payment.setReceiptNo(dbtExtensionData.getReceiptNumber());
			payment.setReceiptUser(dbtExtensionData.getReceiptUser());
			payment.setReceiptDate(dbtExtensionData.getReceiptDate());
			payment.setPaymentMode(dbtExtensionData.getPaymentMode());
			payment.setPaymentRefNo(dbtExtensionData.getPaymentRefNo());
			payment.setRemarks(dbtExtensionData.getRemarks());
			payment.setStatusReason(dbtExtensionData.getStatusReason());
			payment.setPaymentReversalType(dbtExtensionData.getPaymentReversalType());

			if (dbtExtensionData.getPaymentMode().equals(NEConstants.PAY_MODE_CARD)) {

				payment.setCcApprovalCode(dbtExtensionData.getPaymentExtAttribute()[3]);
				payment.setCcNo(dbtExtensionData.getPaymentExtAttribute()[2]);
				payment.setCcType(dbtExtensionData.getPaymentExtAttribute()[0]);
				payment.setCcBankCode(dbtExtensionData.getPaymentExtAttribute()[1]);
				if (dbtExtensionData.getPaymentExtAttribute()[11] != null)
					payment.setTransferredAmount(
							Double.parseDouble(dbtExtensionData.getPaymentExtAttribute()[11]));
				payment.setTransferredMode(dbtExtensionData.getPaymentExtAttribute()[10]);
				payment.setTransferredTo(dbtExtensionData.getPaymentExtAttribute()[7]);

			} else if (dbtExtensionData.getPaymentMode().equals(NEConstants.PAY_MODE_CHEQ)) {

				payment.setChequebankBranchCode(dbtExtensionData.getPaymentExtAttribute()[1]);
				payment.setChequebankCode(dbtExtensionData.getPaymentExtAttribute()[0]);
				payment.setChequeNo(dbtExtensionData.getPaymentExtAttribute()[2]);
				payment.setTransferredTo(dbtExtensionData.getPaymentExtAttribute()[8]);
				payment.setTransferredTo(dbtExtensionData.getPaymentExtAttribute()[7]);

			} else if (dbtExtensionData.getPaymentMode().equals(NEConstants.PAY_MODE_CASH)) {
				if (dbtExtensionData.getPaymentExtAttribute()[11] != null)
					payment.setTransferredAmount(
							Double.parseDouble(dbtExtensionData.getPaymentExtAttribute()[11]));
				payment.setTransferredMode(dbtExtensionData.getPaymentExtAttribute()[10]);
				payment.setTransferredTo(dbtExtensionData.getPaymentExtAttribute()[7]);
			}
		}
		String convertedPaymentList = DCPEUtil.convertToString(payment);

		LOGGER.info("QueryPaymentDataResult: traceId={}|Payment={}", traceId, convertedPaymentList);
		paymentList.add(payment);
	}
}
