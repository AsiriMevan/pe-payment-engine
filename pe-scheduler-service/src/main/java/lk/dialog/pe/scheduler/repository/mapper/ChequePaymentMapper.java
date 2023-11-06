package lk.dialog.pe.scheduler.repository.mapper;

import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.common.util.Util;
import lk.dialog.pe.scheduler.domain.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Mohan_02392
 */
public class ChequePaymentMapper implements RowMapper<Payment> {
	private static final Logger log = LoggerFactory.getLogger(ChequePaymentMapper.class);
	boolean isLimited;

	public ChequePaymentMapper(boolean isLimited) {
		this.isLimited = isLimited;
	}

	@Override
	public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
		Payment pendingPayment = new Payment();
		if (!isLimited) {
			pendingPayment.setTrxID(rs.getLong("REQ_ID"));
			pendingPayment.setAccountNo(rs.getString("SUBSCRIBER_NODE_ID"));
			pendingPayment.setContractNo(rs.getString("CONTRACT_ID"));
			pendingPayment.setAccountPaymentMny(rs.getDouble("PAYMENT_AMOUNT"));
			pendingPayment.setChequebankCode(rs.getString("CHQ_BANK"));
			pendingPayment.setChequebankBranchCode(rs.getString("CHQ_BRANCH"));
			try {
				if (rs.getTimestamp("CHQ_DATE") != null)
					pendingPayment.setChequeDate(Util.getCalender(rs.getTimestamp("CHQ_DATE")));
				if (rs.getTimestamp("PHYSICAL_PAYMENT_DATE") != null)
					pendingPayment.setAccountPaymentDate(Util.getCalender(rs.getTimestamp("PHYSICAL_PAYMENT_DATE")));
				if (rs.getTimestamp("RECEIPT_DATE") != null)
					pendingPayment.setReceiptDate(Util.getCalender(rs.getTimestamp("RECEIPT_DATE")));
			} catch (SQLException e) {
				log.error("Payment date column loading failed . Trx id:" + rs.getLong("REQ_ID"), e);	
				
			}
			pendingPayment.setChequeNo(rs.getString("CHQ_NO"));			
			pendingPayment.setBranchCounter(rs.getString("BRANCH_COUNTER"));
			pendingPayment.setReceiptBranch(rs.getString("RECEIPT_BRANCH"));			
			pendingPayment.setReceiptNo(rs.getString("RECEIPT_NUMBER"));
			pendingPayment.setReceiptUser(rs.getString("RECEIPT_USER"));
			pendingPayment.setTerminalID(rs.getString("TERMINAL_ID"));
			pendingPayment.setRealtime("Y".equals(rs.getString("REALTIME")) ? true : false);
			pendingPayment.setReadStatus(rs.getString("COMMAND_READ"));
			pendingPayment.setErrCode(rs.getString("ERROR_CODE"));
			pendingPayment.setErrDesc(rs.getString("ERROR_DESC"));
			pendingPayment.setConnRef(rs.getString("CONN_REF"));
			pendingPayment.setProductType(rs.getInt("PRODUCT_TYPE"));
			pendingPayment.setSbu(SBUEnum.getSBU(rs.getString("SUBSIDIARY_CODE")).valueOf());
			pendingPayment.setConnectionStatus(rs.getString("CONNECTION_STATUS"));

		} else {
			pendingPayment.setAccountNo(rs.getString("SUBSCRIBER_NODE_ID"));
			pendingPayment.setChequeNo(rs.getString("CHQ_NO"));
		}
		return pendingPayment;
	}

}
