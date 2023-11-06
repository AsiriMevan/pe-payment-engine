package lk.dialog.pe.scheduler.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import lk.dialog.pe.common.util.AccountTypeEnum;
import lk.dialog.pe.common.util.Util;
import lk.dialog.pe.scheduler.domain.Payment;


/**
 * read DBN pending table and map column data to object
 * @author Mohan_02392
 */
public class DBNPaymentMapper implements RowMapper<Payment> {
	private static final Logger log = LoggerFactory.getLogger(DBNPaymentMapper.class);

	@Override
	public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
		Payment pendingPayment = new Payment();
		pendingPayment.setTrxID(rs.getLong("REQ_ID"));
		pendingPayment.setAccountNo(rs.getString("ACCOUNT_NO"));
		pendingPayment.setContractNo(rs.getString("CONTRACT_ID"));
		pendingPayment.setConnRef(rs.getString("CONN_REF"));
		if (rs.getString("CONNECTION_TYPE") != null)
			pendingPayment.setAccountType(AccountTypeEnum.getAccountType(rs.getString("CONNECTION_TYPE")).valueOf());
		pendingPayment.setAccountPaymentMny(rs.getDouble("PAYMENT_AMOUNT"));
		pendingPayment.setPaymentMethodID(rs.getInt("PAYMENT_METHOD_ID"));
		pendingPayment.setPaymentMode(rs.getString("PAYMENT_MODE"));
		pendingPayment.setAccountPaymentRef(rs.getString("PAYMENT_REF"));
		pendingPayment.setChequebankCode(rs.getString("CHQ_BANK"));
		pendingPayment.setChequeNo(rs.getString("CHQ_NO"));
		pendingPayment.setChequebankBranchCode(rs.getString("CHQ_BRANCH"));
		try {
			if (rs.getTimestamp("CHQ_DATE") != null)
				pendingPayment.setChequeDate(Util.getCalender(rs.getTimestamp("CHQ_DATE")));
			if (rs.getTimestamp("RECEIPT_DATE") != null)
				pendingPayment.setReceiptDate(Util.getCalender(rs.getTimestamp("RECEIPT_DATE")));
		} catch (SQLException e) {
			log.error("Payment date column loading failed . Trx id:" + rs.getLong("REQ_ID"), e);
		}

		pendingPayment.setChequeSuspend(rs.getString("CHQ_SUSPEND"));
		pendingPayment.setCcBankCode(rs.getString("CARD_AGENT"));
		pendingPayment.setCcApprovalCode(rs.getString("CARD_APRV_CODE"));
		pendingPayment.setCcNo(rs.getString("CARD_NO"));
		pendingPayment.setCcType(rs.getString("CARD_TYPE"));

		pendingPayment.setReceiptNo(rs.getString("RECEIPT_NUMBER"));
		pendingPayment.setReceiptBranch(rs.getString("RECEIPT_BRANCH"));
		pendingPayment.setBranchCounter(rs.getString("BRANCH_COUNTER"));
		pendingPayment.setReceiptUser(rs.getString("RECEIPT_USER"));
		pendingPayment.setTransferredType(rs.getInt("TRANSFERRED"));
		pendingPayment.setTransferredMode(rs.getString("TRANSFER_MODE"));
		pendingPayment.setTransferredNo(rs.getString("TRANSFER_NO"));
		pendingPayment.setRemarks(rs.getString("REMARKS"));
		pendingPayment.setContactNo(rs.getString("CONTACT_NO"));
		pendingPayment.setTransferReasonCode(rs.getString("REASON_CODE"));
		pendingPayment.setInvoiceList(rs.getString("INVOICE_LIST"));
		pendingPayment.setConnectionStatus(rs.getString("CONNECTION_STATUS"));
		pendingPayment.setProductType(rs.getInt("PRODUCT_TYPE"));
		pendingPayment.setSbu(4);
		/*
		 * pendingPayment.setPaymentCurrency(rs.getString("PAYMENT_CURRENCY"));
		 * pendingPayment.setPaymentText(rs.getString("PAYMENT_TEXT"));
		 * pendingPayment.setPhysicalPaymentDate(rs.getTimestamp(
		 * "PHYSICAL_PAYMENT_DATE"));
		 * pendingPayment.setPhysicalPaymentText(rs.getString(
		 * "PHYSICAL_PAYMENT_TEXT")); if (rs.getString("PRODUCT_CATEGORY") !=
		 * null)
		 * pendingPayment.setProductCategory(ProductCategory.getProductCategory(
		 * rs.getString("PRODUCT_CATEGORY")).valueOf());
		 * pendingPayment.setProductType(rs.getInt("PRODUCT_TYPE"));
		 * pendingPayment.setTerminalID(rs.getString("TERMINAL_ID")); if
		 * (rs.getString("SUBSIDIARY_CODE") != null)
		 * pendingPayment.setSbu(SBU.getSBU(rs.getString("SUBSIDIARY_CODE")).
		 * valueOf());
		 */
		return pendingPayment;
	}

}
