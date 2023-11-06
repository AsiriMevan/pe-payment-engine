package lk.dialog.pe.scheduler.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.QuerySystem;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.common.util.Util;
import lk.dialog.pe.scheduler.domain.Payment;


/**
 * @author Mohan_02392
 */
public class CancelPaymentMapper implements RowMapper<Payment> {
	private static final Logger log = LoggerFactory.getLogger(CancelPaymentMapper.class);

	@Override
	public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
		Payment pendingPayment = new Payment();
		pendingPayment.setTrxID(rs.getLong("REQ_ID"));
		if (rs.getString("PRODUCT_CATEGORY") != null)
			pendingPayment.setProductCategory(ProductCategoryEnum.valueOf(rs.getString("PRODUCT_CATEGORY")).getCategory());
		if (rs.getString("SUBSIDIARY_CODE") != null)
			pendingPayment.setSbu(SBUEnum.valueOf((rs.getString("SUBSIDIARY_CODE"))).valueOf());
		if (rs.getString("QUERY_SYSTEM") != null)
			pendingPayment.setQuerySystem(QuerySystem.getQuerySystem(rs.getString("QUERY_SYSTEM")).valueOf());
		pendingPayment.setAccountNo(rs.getString("SUBSCRIBER_NODE_ID"));
		pendingPayment.setContractNo(rs.getString("CONTRACT_ID"));
		pendingPayment.setPhysicalPaymentSeq(rs.getLong("RBM_PHYSICAL_SEQ"));
		pendingPayment.setAccountPaymentSeq(rs.getLong("RBM_ACCOUNT_SEQ"));
		pendingPayment.setChequeReturn(rs.getString("CHQ_RETURN"));
		pendingPayment.setChequeSuspend(rs.getString("CHQ_SUSPEND"));
		pendingPayment.setPaymentReversalType(rs.getString("REVERSAL_TYPE"));
		pendingPayment.setTransferredType(rs.getInt("TRANSFERRED"));
		pendingPayment.setTransferredMode(rs.getString("TRANSFER_MODE"));
		pendingPayment.setTransferredNo(rs.getString("TRANSFER_NO"));
		pendingPayment.setTransferredRef(rs.getString("TRANSFERED_REF"));
		pendingPayment.setMistakeBy(rs.getString("MISTAKE_BY"));
		pendingPayment.setRemarks(rs.getString("REMARKS"));
		pendingPayment.setPaymentCancelledReason(rs.getString("CANCELLED_REASON_DESC"));
		pendingPayment.setStatusReason(rs.getString("CANCELLED_REASON"));
		pendingPayment.setCancelledByUser(rs.getString("CANCELLED_USER"));
		pendingPayment.setCancelledApproveBy(rs.getString("APPROVED_USER"));
		pendingPayment.setProductType(rs.getInt("PRODUCT_TYPE"));
		pendingPayment.setReceiptNo(rs.getString("RECEIPT_NUMBER"));
		pendingPayment.setReceiptBranch(rs.getString("RECEIPT_BRANCH"));
		pendingPayment.setReceiptUser(rs.getString("RECEIPT_USER"));
		try {
			if (rs.getTimestamp("RECEIPT_DATE") != null)
				pendingPayment.setReceiptDate(Util.getCalender(rs.getTimestamp("RECEIPT_DATE")));
			if (rs.getTimestamp("PHYSICAL_PAYMENT_DATE") != null)
				pendingPayment.setAccountPaymentDate(Util.getCalender(rs.getTimestamp("PHYSICAL_PAYMENT_DATE")));
		} catch (SQLException e1) {
			log.error("Payment date column loading failed . Trx id:" + rs.getLong("REQ_ID"), e1);
		}
		pendingPayment.setChequebankCode(rs.getString("CHQ_BANK"));
		pendingPayment.setChequebankBranchCode(rs.getString("CHQ_BRANCH"));
		pendingPayment.setChequeNo(rs.getString("CHQ_NO"));
		pendingPayment.setErrCode(rs.getString("ERROR_CODE"));
		pendingPayment.setErrDesc(rs.getString("ERROR_DESC"));
		pendingPayment.setBranchCounter(rs.getString("BRANCH_COUNTER"));
		pendingPayment.setTerminalID(rs.getString("TERMINAL_ID"));
		pendingPayment.setTransferredAmount(rs.getDouble("PAYMENT_AMOUNT"));
		return pendingPayment;

	}

}
