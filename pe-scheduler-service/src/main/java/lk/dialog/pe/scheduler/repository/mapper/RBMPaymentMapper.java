package lk.dialog.pe.scheduler.repository.mapper;

import lk.dialog.pe.common.util.*;
import lk.dialog.pe.scheduler.domain.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * read RBM pending table and map data to object
 * @author Mohan_02392
 */
public class RBMPaymentMapper implements RowMapper<Payment> {
	private static final Logger log = LoggerFactory.getLogger(RBMPaymentMapper.class);

	@Override
	public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
		log.info("mapRowRequest :  rowNum={}", rowNum);
		Payment pendingPayment = new Payment();
		pendingPayment.setTrxID(rs.getLong("REQ_ID"));
		pendingPayment.setAccountNo(rs.getString("SUBSCRIBER_NODE_ID"));
		if (rs.getString("CONNECTION_TYPE") != null)
			pendingPayment.setAccountType(AccountTypeEnum.getAccountType(rs.getString("CONNECTION_TYPE")).valueOf());
		pendingPayment.setBranchCounter(rs.getString("BRANCH_COUNTER"));
		pendingPayment.setCcBankCode(rs.getString("CARD_AGENT"));
		pendingPayment.setCcApprovalCode(rs.getString("CARD_APRV_CODE"));
		pendingPayment.setCcNo(rs.getString("CARD_NO"));
		pendingPayment.setCcType(rs.getString("CARD_TYPE"));
		pendingPayment.setChequebankCode(rs.getString("CHQ_BANK"));
		pendingPayment.setChequebankBranchCode(rs.getString("CHQ_BRANCH"));
		try {
			if (rs.getTimestamp("CHQ_DATE") != null)
				pendingPayment.setChequeDate(Util.getCalender(rs.getTimestamp("CHQ_DATE")));
			if (rs.getTimestamp("PHYSICAL_PAYMENT_DATE") != null)
				pendingPayment.setAccountPaymentDate(Util.getCalender(rs.getTimestamp("PHYSICAL_PAYMENT_DATE")));
			if (rs.getTimestamp("RECEIPT_DATE") != null)
				pendingPayment.setReceiptDate(Util.getCalender(rs.getTimestamp("RECEIPT_DATE")));
			if (rs.getTimestamp("CREATED_DATE") != null)
				pendingPayment.setCreatedDtm(Util.getCalender(rs.getTimestamp("CREATED_DATE")));
		} catch (SQLException e) {
			log.error("Payment date column loading failed . Trx id:" + rs.getLong("REQ_ID"), e);
		}
		pendingPayment.setChequeNo(rs.getString("CHQ_NO"));
		pendingPayment.setChequeSuspend(rs.getString("CHQ_SUSPEND"));
		pendingPayment.setConnRef(rs.getString("CONN_REF"));
		pendingPayment.setContactNo(rs.getString("CONTACT_NO"));
		pendingPayment.setContractNo(rs.getString("CONTRACT_ID"));
		pendingPayment.setAccountPaymentMny(rs.getDouble("PAYMENT_AMOUNT"));
		pendingPayment.setPaymentCurrency(rs.getString("PAYMENT_CURRENCY"));
		pendingPayment.setPaymentMethodID(rs.getInt("PAYMENT_METHOD_ID"));
		pendingPayment.setPaymentMode(rs.getString("PAYMENT_MODE"));
		pendingPayment.setAccountPaymentRef(rs.getString("PAYMENT_REF"));
		pendingPayment.setPaymentRefNo(rs.getString("PAYMENT_TEXT"));
		pendingPayment.setAccountPaymentText(rs.getString("PHYSICAL_PAYMENT_TEXT"));

		if (rs.getString("PRODUCT_CATEGORY") != null)
			pendingPayment.setProductCategory(ProductCategoryEnum.valueOf(rs.getString("PRODUCT_CATEGORY")).getCategory());
		pendingPayment.setProductType(rs.getInt("PRODUCT_TYPE"));
		pendingPayment.setReceiptBranch(rs.getString("RECEIPT_BRANCH"));

		pendingPayment.setReceiptNo(rs.getString("RECEIPT_NUMBER"));
		pendingPayment.setReceiptUser(rs.getString("RECEIPT_USER"));
		pendingPayment.setRemarks(rs.getString("REMARKS"));
		pendingPayment.setTerminalID(rs.getString("TERMINAL_ID"));
		log.info("mapRow Response : sbu={}", rs.getString("SUBSIDIARY_CODE"));
		if (rs.getString("SUBSIDIARY_CODE") != null){	
			log.info("mapRow Response : SBU={} " + SBUEnum.getSBU(rs.getString("SUBSIDIARY_CODE")).valueOf());	
			pendingPayment.setSbu(SBUEnum.getSBU(rs.getString("SUBSIDIARY_CODE")).valueOf());
		}
		pendingPayment.setTransferReasonCode(rs.getString("REASON_CODE"));
		if (rs.getInt("TRANSFERRED") == 1) {
			pendingPayment.setTransferredMode(rs.getString("TRANSFER_MODE"));
			pendingPayment.setTransferredNo(rs.getString("TRANSFER_NO"));
			pendingPayment.setTransferredType(rs.getInt("TRANSFERRED"));
			pendingPayment.setTransferredAmount(rs.getDouble("PAYMENT_AMOUNT"));
		}
		pendingPayment.setConnectionStatus(rs.getString("CONNECTION_STATUS"));
		String responseString = DCPEUtil.convertToString(pendingPayment);
		log.info("mapRow Response : payments={}", responseString);
		return pendingPayment;

	}
}
