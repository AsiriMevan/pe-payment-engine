package lk.dialog.pe.scheduler.util;

import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.QuerySystem;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.domain.Payment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CancelTestDataGenerator {

    public  String getCommandReadByTrxIdFromDb(Long trxId, JdbcTemplate peJdbcTemplate){
        return peJdbcTemplate.queryForObject("select command_read from cancel_payment where req_id='"+trxId+"'",String.class);
    }

    public Payment getCancelPaymentFromDb(Long trxId , JdbcTemplate peJdbcTemplate){
        try {
            Payment payment = peJdbcTemplate.queryForObject("SELECT cp.*  ,(SELECT rmap.description FROM cancel_adj_reason_map rmap WHERE rmap.cpos_id=cp.cancelled_reason) as cancelled_reason_desc FROM cancel_payment  cp WHERE  cp.req_id='" + trxId + "'", new RowMapper<Payment>() {
                @Override
                public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Payment pendingPayment = new Payment();
                    pendingPayment.setErrDesc(rs.getString("ERROR_DESC"));
                    pendingPayment.setErrCode(rs.getString("ERROR_CODE"));
                    if(rs.getString("RBM_PHYSICAL_SEQ")!=null)
                        pendingPayment.setPhysicalPaymentSeq(Integer.parseInt(rs.getString("RBM_PHYSICAL_SEQ")));

                    return pendingPayment;
                }
            });
            return payment;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public  void saveCancelPaymentToDb(Payment payment, COMMAND_READ commandRead, JdbcTemplate peJdbcTemplate){
        deleteCancelPaymentFromDb(payment.getTrxID(),peJdbcTemplate);
        lodgePaymentCancel(payment,commandRead,peJdbcTemplate);
    }

    public  void deleteCancelPaymentFromDb(long trxId , JdbcTemplate peJdbcTemplate){

        try{
            int result = peJdbcTemplate.update("delete from Cancel_payment where req_id='"+trxId+"'");
            System.out.println("deleted Cancel payment with trxId="+trxId+"result="+result);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("no Cancel payment records to delete");
        }
    }

    public void deleteChequeReturnsFromDb(Payment payment,JdbcTemplate jdbcTemplate){
        jdbcTemplate.update("delete FROM cheque_return_log crl WHERE crl.chq_no=? AND crl.chq_bank = ?  and crl.chq_branch= ?", new Object[]{payment.getChequeNo(), payment.getChequebankCode(), payment.getChequebankBranchCode()});
    }

    public List<Payment> getChequeReturnFromDb(Payment payment, JdbcTemplate jdbcTemplate){
        try {
            return jdbcTemplate.query("select * FROM cheque_return_log crl WHERE crl.receipt_number=? AND crl.contract_id=? AND crl.chq_no=? AND crl.chq_bank = ?  and crl.chq_branch= ?", new Object[]{payment.getReceiptNo(), payment.getContractNo(), payment.getChequeNo(), payment.getChequebankCode(), payment.getChequebankBranchCode()}, new RowMapper<Payment>() {
                @Override
                public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Payment payment = new Payment();
                    payment.setChequeNo(rs.getString("chq_no"));
                    payment.setChequebankCode(rs.getString("chq_bank"));
                    payment.setChequebankBranchCode(rs.getString("chq_branch"));
                    payment.setReceiptNo(rs.getString("receipt_number"));
                    payment.setContractNo(rs.getString("contract_id"));
                    return payment;
                }
            });
        }catch (EmptyResultDataAccessException ex){
            System.out.println("no chequeReturns found");
            return new ArrayList<>();
        }
    }

    public void lodgePaymentCancel(Payment payRequest ,COMMAND_READ commandRead,JdbcTemplate peJdbcTemplate) {
        /* insert payment cancellation request */

        String sqlPayPostStr = QueryConfig.getQueryByKey("SQL_PAYMENT_CANCEL");
        peJdbcTemplate.update(sqlPayPostStr, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, payRequest.getTrxID());
                ps.setString(2, ProductCategoryEnum.getProductCategoryByValue(payRequest.getProductCategory()).name());
                ps.setString(3, SBUEnum.getSBU(payRequest.getSbu()).name());
                ps.setString(4, QuerySystem.getQuerySystem(payRequest.getQuerySystem()).name());
                ps.setString(5, payRequest.getAccountNo());
                ps.setString(6, payRequest.getContractNo());
                ps.setLong(7, payRequest.getPhysicalPaymentSeq());
                ps.setLong(8, payRequest.getAccountPaymentSeq());

                ps.setString(9, payRequest.getChequeReturn());
                ps.setString(10, payRequest.getChequeSuspend());
                ps.setString(11, payRequest.getPaymentReversalType());
                ps.setInt(12, payRequest.getTransferredType());
                ps.setString(13, payRequest.getTransferredMode());
                if (payRequest.getTransferredNo() != null)
                    ps.setString(14, payRequest.getTransferredNo());
                else
                    ps.setNull(14, java.sql.Types.INTEGER);
                ps.setString(15, payRequest.getTransferredRef());
                String mistakeBy = payRequest.getMistakeBy();
                if (mistakeBy != null && !mistakeBy.isEmpty()) {
                    ps.setString(16, mistakeBy);
                } else {
                    ps.setString(16, "CX");
                }
                ps.setString(17, payRequest.getRemarks());
                ps.setString(18, payRequest.getStatusReason());
                ps.setString(19, payRequest.getCancelledByUser());
                ps.setString(20, payRequest.getCancelledApproveBy());
                ps.setInt(21, payRequest.getProductType());

                ps.setString(22, payRequest.getReceiptNo());
                ps.setString(23, payRequest.getReceiptBranch());
                ps.setString(24, payRequest.getBranchCounter());
                ps.setString(25, payRequest.getReceiptUser());
                if (payRequest.getReceiptDate() != null)ps.setTimestamp(26,  new Timestamp(payRequest.getReceiptDate().getTime().getTime()));
                else ps.setNull(26, java.sql.Types.DATE);
                ps.setString(27, payRequest.getChequebankCode());
                ps.setString(28, payRequest.getChequeNo());
                ps.setString(29, payRequest.getChequebankBranchCode());
                ps.setString(30, payRequest.getTerminalID());
                ps.setString(31, "CPOS");
                if (payRequest.getAccountPaymentDate() != null) ps.setTimestamp(32, new Timestamp(payRequest.getAccountPaymentDate().getTime().getTime()));
                else ps.setNull(32, Types.TIMESTAMP);
                ps.setDouble(33, payRequest.getTransferredAmount());

                if(commandRead==null){
                    ps.setString(34, "X");
                    if(payRequest.getErrCode()==null)ps.setNull(35, Types.VARCHAR);
                    else ps.setString(35, payRequest.getErrCode());
                }
                else {
                    ps.setString(34, commandRead.name());
                    ps.setString(35, commandRead.getErrorCode());

                }
                if(payRequest.getErrDesc()==null)ps.setNull(36, Types.VARCHAR);
                else ps.setString(36, payRequest.getErrDesc());
            }
        });
    }


    public Payment getSuccessPayment() {
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        java.util.Date date=new Date(ts.getTime());
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(date);

        Payment payment = new Payment();
        payment.setProductCategory(1);
        payment.setTrxID(112646);
        payment.setAccountPaymentStatus(0);
        payment.setAccountPaymentSeq(-1);
        payment.setCancelledApproveBy("007");
        payment.setAccountNo("97010267");
        payment.setContractNo("97010415");
        payment.setAccountPaymentMny(0.0);
        payment.setAccountPaymentDate(calendarTime);
        payment.setReceiptNo("FBDPARP00029593");
        payment.setReceiptBranch("710");
        payment.setBranchCounter("PMT");
        payment.setReceiptUser("002");
        payment.setReceiptDate(calendarTime);
        payment.setRemarks("cancel");
        payment.setStatusReason("RC48");
        payment.setPaymentReversalType("R");
        payment.setCancelledByUser("007");
        payment.setAccountType(0);
        payment.setSbu(3);
        payment.setTransferredMode("S");
        payment.setTransferredAmount(10.0);
        payment.setTransferredType(2);
        payment.setChequeReturn("N");
        payment.setChequeSuspend("SUSPEND");
        payment.setProductType(0);
        payment.setPaymentMethodID(0);
        payment.setTerminalID("10.0.21.234");
        payment.setPhysicalPaymentSeq(44);
        payment.setRealtime(false);
        payment.setErrCode("1");
        payment.setErrDesc("SUCCESS");
        payment.setMistakeBy("002");
        payment.setQuerySystem(3);
        payment.setTranType(0);
        return payment;
    }
}


