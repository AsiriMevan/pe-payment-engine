package lk.dialog.pe.scheduler.util;

import lk.dialog.pe.common.util.ConnectionStatus;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.domain.Payment;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.*;
import java.util.Calendar;
import java.util.Date;

public class ChequeTestDataGenerator {


    public void takeUpdateTimeTenMinutesBackByTrxId(Long trxId ,JdbcTemplate peJdbcTemplate){
        peJdbcTemplate.update("update forceful_realize_cheques set update_date=current_timestamp - interval '10 minutes' where req_id='"+trxId+"';");
    }
    public void changeErrorCodeByTrxId(String errorCode,Long trxId ,JdbcTemplate peJdbcTemplate){
        peJdbcTemplate.update("update forceful_realize_cheques set error_code='"+errorCode+"' where req_id='"+trxId+"';");
    }

    public  String getCommandReadByTrxIdFromDb(Long trxId, JdbcTemplate peJdbcTemplate){
        return peJdbcTemplate.queryForObject("select command_read from forceful_realize_cheques where req_id='"+trxId+"'",String.class);
    }

    public Payment getPaymentFromDb(Long trxId , JdbcTemplate peJdbcTemplate){
        try {
            Payment payment = peJdbcTemplate.queryForObject("select * from forceful_realize_cheques where req_id='" + trxId + "'", new RowMapper<Payment>() {
                @Override
                public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Payment pendingPayment = new Payment();
                    pendingPayment.setErrDesc(rs.getString("ERROR_DESC"));
                    pendingPayment.setErrCode(rs.getString("ERROR_CODE"));
                    pendingPayment.setReadStatus(rs.getString("COMMAND_READ"));
                    return pendingPayment;
                }
            });
            return payment;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public Payment getFullPaymentFromDb(Payment paymentSource , JdbcTemplate peJdbcTemplate){
        try {

            Payment payment = peJdbcTemplate.queryForObject(QueryConfig.getQueryByKey("SQL_SEL_FORCEFUL_CHQ_FULL"), new Object[] { paymentSource.getAccountNo(), paymentSource.getChequeNo() },new RowMapper<Payment>() {
                @Override
                public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Payment pendingPayment = new Payment();
                    pendingPayment.setErrDesc(rs.getString("ERROR_DESC"));
                    pendingPayment.setErrCode(rs.getString("ERROR_CODE"));
                    pendingPayment.setReadStatus(rs.getString("COMMAND_READ"));
                    return pendingPayment;
                }
            });
            return payment;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public  void saveFullPaymentToDb(Payment payment, COMMAND_READ commandRead, JdbcTemplate peJdbcTemplate){
        deletePaymentFromDb(payment.getTrxID(),peJdbcTemplate);
        lodgePayment(payment,COMMAND_READ.N,false,peJdbcTemplate);
    }

    public  void savePartialPaymentToDb(Payment payment, COMMAND_READ commandRead, JdbcTemplate peJdbcTemplate){
        deletePaymentFromDb(payment.getTrxID(),peJdbcTemplate);
        lodgePayment(payment,null,false,peJdbcTemplate);
    }

    public  void savePaymentToDb(Payment payment, COMMAND_READ commandRead,boolean isRealTime, JdbcTemplate peJdbcTemplate){
        deletePaymentFromDb(payment.getTrxID(),peJdbcTemplate);
        lodgePayment(payment,commandRead,isRealTime,peJdbcTemplate);
    }

    public  void deletePaymentFromDb(long trxId , JdbcTemplate peJdbcTemplate){
        try{
            int result = peJdbcTemplate.update("delete from forceful_realize_cheques where req_id='"+trxId+"'");
            System.out.println("deleted payment with trxId="+trxId+"result="+result);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("no payment records to delete");
        }
    }


    public void lodgePayment(Payment payRequest,COMMAND_READ  commandRead, boolean isRealTIme, JdbcTemplate peJdbcTemplate) {
        /* insert into cheque forceful realization table */
        String sqlPayPostStr = QueryConfig.getQueryByKey("SQL_CHQ_FORCEFUL_REALIZE");
        peJdbcTemplate.update(sqlPayPostStr, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, payRequest.getTrxID());
                ps.setString(2, payRequest.getAccountNo());
                ps.setString(3, payRequest.getContractNo());
                ps.setString(4, ProductCategoryEnum.getProductCategoryByValue(payRequest.getProductCategory()).name());

                ps.setString(5, SBUEnum.getSBU(payRequest.getSbu()).name());
                ps.setString(6, String.valueOf(payRequest.getAccountType()));
                ps.setDouble(7, payRequest.getAccountPaymentMny());
                ps.setString(8, payRequest.getChequebankCode());
                ps.setString(9, payRequest.getChequebankBranchCode());
                ps.setString(10, payRequest.getChequeNo());

                if (payRequest.getChequeDate() != null) ps.setTimestamp(11, new Timestamp(payRequest.getChequeDate().getTime().getTime()));
                else ps.setNull(11, Types.TIMESTAMP);

                ps.setString(12, payRequest.getTerminalID());
                ps.setString(13, payRequest.getReceiptNo());
                ps.setString(14, payRequest.getReceiptBranch());
                ps.setString(15, payRequest.getBranchCounter());
                if (payRequest.getReceiptDate() != null)ps.setTimestamp(16,  new Timestamp(payRequest.getReceiptDate().getTime().getTime()));
                else ps.setNull(16, java.sql.Types.DATE);
                ps.setString(17, payRequest.getReceiptUser());
                if (payRequest.getAccountPaymentDate() != null) ps.setTimestamp(18, new Timestamp(payRequest.getAccountPaymentDate().getTime().getTime()));
                else ps.setNull(18, Types.TIMESTAMP);
                ps.setInt(19, payRequest.getProductType());
                ps.setString(20, isRealTIme ? "Y" : "N");
                ps.setString(21, payRequest.getConnRef());
                String paymentRef= payRequest.getAccountPaymentRef();
                ps.setString(22, paymentRef != null && paymentRef.length() > 50 ? paymentRef.substring(0, 50) : paymentRef);
                ps.setString(23, payRequest.getRemarks());
                if(payRequest.getConnectionStatus()!=null) ps.setString(24, ConnectionStatus.getConnectionStatus(payRequest.getConnectionStatus()).name());
                else ps.setNull(24, Types.VARCHAR);
                if(commandRead==null){
                    ps.setString(25, "X");
                    if(payRequest.getErrCode()==null)ps.setNull(26, Types.VARCHAR);
                    else ps.setString(26, payRequest.getErrCode());
                }
                else {
                    ps.setString(25, commandRead.name());
                    ps.setString(26, commandRead.getErrorCode());

                }
                if(payRequest.getErrDesc()==null)ps.setNull(27, Types.VARCHAR);
                else ps.setString(27, payRequest.getErrDesc());
            }
        });
    }

    public Payment getSuccessPayment(){
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        java.util.Date date=new Date(ts.getTime());
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(date);
        Payment payment = new Payment();
        payment.setProductCategory(2);//this is not needed in the logic so hardcoded
        payment.setTrxID(526061);
        payment.setAccountPaymentStatus(0);
        payment.setAccountPaymentSeq(0);
        payment.setAccountNo("70500592");
        payment.setContractNo("72188093");
        payment.setAccountPaymentMny(10.0);
        payment.setAccountPaymentDate(calendarTime);
        payment.setReceiptNo("FBBPARP00032137");
        payment.setReceiptBranch("710");
        payment.setBranchCounter("PMT");
        payment.setReceiptUser("DTQA00001");
        payment.setReceiptDate(calendarTime);
        payment.setAccountType(0);
        payment.setSbu(4);
        payment.setTransferredAmount(0.0);
        payment.setTransferredType(0);
        payment.setChequebankCode("7010");
        payment.setChequebankBranchCode("774");
        payment.setChequeNo("111119");
        payment.setChequeDate(calendarTime);
        payment.setProductType(5);
        payment.setPaymentMethodID(0);
        payment.setTerminalID("10.0.21.152");
        payment.setPhysicalPaymentSeq(0);
        payment.setConnRef("0814505394");
        payment.setRealtime(true);
        payment.setReadStatus("S");
        payment.setErrCode("1");
        payment.setErrDesc("SUCCESS");
        payment.setQuerySystem(0);
        payment.setTranType(0);
        return payment;
    }
}
