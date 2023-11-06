package lk.dialog.pe.scheduler.repository.mapper;

import lk.dialog.pe.common.util.AccountTypeEnum;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.common.util.Util;
import lk.dialog.pe.scheduler.domain.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * read OCS pending table and map data to object
 * @author Mohan_02392
 */
public class OCSPaymentMapper implements RowMapper<Payment> {
	private static final Logger log = LoggerFactory.getLogger(OCSPaymentMapper.class);

	@Override
	public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
		Payment pendingPayment = new Payment();
		pendingPayment.setAccountNo(rs.getString("SUBSCRIBER_NODE_ID"));
		if (rs.getString("CONNECTION_TYPE") != null)
			pendingPayment.setAccountType(AccountTypeEnum.getAccountType(rs.getString("CONNECTION_TYPE")).valueOf());
		pendingPayment.setBranchCounter(rs.getString("BRANCH_COUNTER"));		
		pendingPayment.setConnRef(rs.getString("CONN_REF"));
		pendingPayment.setContactNo(rs.getString("CONTACT_NO"));
		pendingPayment.setContractNo(rs.getString("CONTRACT_ID"));	
		pendingPayment.setAccountPaymentMny(rs.getDouble("PAYMENT_AMOUNT"));		
		pendingPayment.setPaymentMode(rs.getString("PAYMENT_MODE"));
		pendingPayment.setAccountPaymentRef(rs.getString("PAYMENT_REF"));
		//pendingPayment.setPaymentRefNo(rs.getString("PAYMENT_TEXT"));
		//pendingPayment.setAccountPaymentText(rs.getString("PHYSICAL_PAYMENT_TEXT"));			
		if (rs.getString("PRODUCT_CATEGORY") != null)
			pendingPayment.setProductCategory(ProductCategoryEnum.valueOf(rs.getString("PRODUCT_CATEGORY")).category);
		pendingPayment.setProductType(rs.getInt("PRODUCT_TYPE"));
		pendingPayment.setReceiptBranch(rs.getString("RECEIPT_BRANCH"));
		if(rs.getTimestamp("RECEIPT_DATE") != null)
			try {
				pendingPayment.setReceiptDate(Util.getCalender(rs.getTimestamp("RECEIPT_DATE")));
			} catch (SQLException e) {				
				log.error("Payment Date loading failed . Trx id:" + rs.getLong("REQ_ID"), e);
			}	
		pendingPayment.setReceiptNo(rs.getString("RECEIPT_NUMBER"));
		pendingPayment.setReceiptUser(rs.getString("RECEIPT_USER"));
		pendingPayment.setRemarks(rs.getString("REMARKS"));		
		if (rs.getString("SUBSIDIARY_CODE") != null)
			pendingPayment.setSbu(SBUEnum.getSBU(rs.getString("SUBSIDIARY_CODE")).valueOf());
		pendingPayment.setTransferReasonCode(rs.getString("REASON_CODE"));		
		pendingPayment.setTrxID(rs.getLong("REQ_ID"));
		pendingPayment.setOcsReasonCode(rs.getString("OCS_REASON_CODE"));
		pendingPayment.setOcsReasonId(rs.getString("REASON_ID"));
		pendingPayment.setTranType(rs.getInt("TRAN_TYPE_ID"));
		return pendingPayment;
	}

}
