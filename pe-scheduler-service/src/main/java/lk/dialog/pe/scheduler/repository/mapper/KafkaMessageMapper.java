package lk.dialog.pe.scheduler.repository.mapper;

import lk.dialog.pe.scheduler.dto.PaymentJSON;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
@Slf4j
public class KafkaMessageMapper implements RowMapper<PaymentJSON> {

        private HANDLER handler;

        public KafkaMessageMapper(HANDLER handler){
            this.handler = handler;
        }

        @Override
        public PaymentJSON mapRow(ResultSet rs, int rowNum) throws SQLException {
            PaymentJSON payment = new PaymentJSON();
            payment.setReqId(rs.getString("REQ_ID"));
            payment.setProductCategory(rs.getString("PRODUCT_CATEGORY"));
            payment.setReceiptNumber(rs.getString("RECEIPT_NUMBER"));
            payment.setAccountNumber(rs.getString("CONTRACT_ID"));
            payment.setAccountType(rs.getString("SUBSIDIARY_CODE"));
            if(rs.getString("PAYMENT_AMOUNT") != null)
                payment.setAmount(rs.getDouble("PAYMENT_AMOUNT"));
            if (rs.getTimestamp("CREATED_DATE") != null)
                payment.setTxDate(SchUtil.convertDate(rs.getTimestamp("CREATED_DATE")));
            payment.setReceiptBranch(rs.getString("RECEIPT_BRANCH"));
            payment.setBranchCounter(rs.getString("RECEIPT_BRANCH"));
            payment.setConnectionType("CONNECTION_TYPE");
            payment.setReceiptUser(rs.getString("RECEIPT_USER"));
            payment.setCommandRead(rs.getString("COMMAND_READ"));
            payment.setErrorCode(rs.getString("ERROR_CODE"));
            payment.setErrorDesc(rs.getString("ERROR_DESC"));
            payment.setProductType(rs.getLong("PRODUCT_TYPE"));
            if (rs.getTimestamp("RECEIPT_DATE") != null)
                payment.setReceiptDate(SchUtil.convertDate(rs.getTimestamp("RECEIPT_DATE")));
            if (rs.getTimestamp("PHYSICAL_PAYMENT_DATE") != null)
                payment.setPhysicalPaymentDate(SchUtil.convertDate(rs.getTimestamp("PHYSICAL_PAYMENT_DATE")));
            payment.setCreatedUser(rs.getString("CREATED_USER"));
            System.out.println(handler.name());


            if(handler != HANDLER.CHEQUE_FORCEFUL_PAY && handler != HANDLER.CANCEL_PAY){
                payment.setPaymentType(SchUtil.convertPaymentMode(rs.getString("PAYMENT_MODE")));
                payment.setPaymentText(rs.getString("PAYMENT_TEXT"));
                payment.setPhysicalPaymentText(rs.getString("PHYSICAL_PAYMENT_TEXT"));
                payment.setModuleName(rs.getString("MODULE_NAME"));
                payment.setStatusReason(rs.getString("REASON_CODE"));
            }
            if(handler != HANDLER.CANCEL_PAY){
                payment.setPaymentRef(rs.getString("PAYMENT_REF"));
                payment.setConnRef(rs.getString("CONN_REF"));
                payment.setConnectionStatus(rs.getString("CONNECTION_STATUS"));
                payment.setConnectionType(rs.getString("CONNECTION_TYPE"));
            }

            return payment;

        }
    }
