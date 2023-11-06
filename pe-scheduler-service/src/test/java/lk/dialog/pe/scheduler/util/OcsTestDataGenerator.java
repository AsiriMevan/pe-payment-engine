package lk.dialog.pe.scheduler.util;

import lk.dialog.pe.common.util.AccountTypeEnum;
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

public class OcsTestDataGenerator {


    public Payment getSuccessOcsPayment(){
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=new Date(ts.getTime());
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(date);
        Payment payment = new Payment();

        payment.setProductCategory(1);
        payment.setTrxID(2006253);
        payment.setAccountPaymentStatus(0);
        payment.setAccountPaymentRef("CGTEST_TEST_23454454");
        payment.setAccountPaymentSeq(0);
        payment.setAccountNo("97010172");
        payment.setContractNo("97003910");
        payment.setAccountPaymentMny(10.0);
        payment.setReceiptNo("CGT2108308916289");
        payment.setReceiptBranch("TEST");
        payment.setBranchCounter("ONLINE");
        payment.setReceiptUser("CG_USER");
        payment.setReceiptDate(calendarTime);
        payment.setPaymentMode("CA");
        payment.setRemarks("ONLINE PAYMENT");
        payment.setAccountType(2);
        payment.setSbu(3);
        payment.setTransferredAmount(0.0);
        payment.setTransferredType(0);
        payment.setProductType(0);
        payment.setPaymentMethodID(0);
        payment.setPhysicalPaymentSeq(0);
        payment.setConnRef("762437267");
        payment.setRealtime(false);
        payment.setOcsReasonCode("PAY");
        payment.setOcsReasonId("2003");
        payment.setQuerySystem(0);
        payment.setTranType(1);
        return payment;
    }

    public  String getCommandReadByTrxIdFromDb(Long trxId, JdbcTemplate peJdbcTemplate){
        return peJdbcTemplate.queryForObject("select command_read from ocs_payment where req_id='"+trxId+"'",String.class);
    }

    public Payment getOcsPaymentFromDb(Long trxId ,JdbcTemplate peJdbcTemplate){
        try {
            Payment payment = peJdbcTemplate.queryForObject("SELECT op.*,orc.reason_code as ocs_reason_code,orc.reason_id FROM ocs_payment op,ocs_reason_codes orc  WHERE op.tran_type_id = orc.tran_type_id and op.req_id='" + trxId + "'", new RowMapper<Payment>() {
                @Override
                public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Payment pendingPayment = new Payment();
                    pendingPayment.setErrDesc(rs.getString("ERROR_DESC"));
                    pendingPayment.setErrCode(rs.getString("ERROR_CODE"));
                    return pendingPayment;
                }
            });
            return payment;
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
        }

    public  void saveOCSPaymentToDb(Payment payment, COMMAND_READ commandRead, JdbcTemplate peJdbcTemplate){
        deleteOCSPaymentFromDb(payment.getTrxID(),peJdbcTemplate);
        lodgeOCSPayment(payment,commandRead,peJdbcTemplate);
    }

    public  void deleteOCSPaymentFromDb(long trxId , JdbcTemplate peJdbcTemplate){

        try{
            int result = peJdbcTemplate.update("delete from ocs_payment where req_id='"+trxId+"'");
            System.out.println("deleted ocs payment with trxId="+trxId+"result="+result);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("no ocs payment records to delete");
        }
    }

    public void lodgeOCSPayment(Payment payRequest,COMMAND_READ commandRead, JdbcTemplate peJdbcTemplate) {

        /* insert payment to OCS payment table */
        String sqlPayPostStr = QueryConfig.getQueryByKey("SQL_OCS_PAYMENT_POST");

        peJdbcTemplate.update(sqlPayPostStr, new PreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, payRequest.getTrxID());
                ps.setString(2, payRequest.getAccountNo());
                ps.setString(3, payRequest.getContractNo());
                ps.setString(4, payRequest.getConnRef());
                ps.setString(5, ProductCategoryEnum.getProductCategoryByValue(payRequest.getProductCategory()).name());
                ps.setString(6, SBUEnum.getSBU(payRequest.getSbu()).name());

                if (AccountTypeEnum.getAccountType(payRequest.getAccountType()) != null)
                    ps.setString(7, AccountTypeEnum.getAccountType(payRequest.getAccountType()).name());
                else
                    ps.setNull(7, java.sql.Types.VARCHAR);

                if (payRequest.getAccountPaymentDate() != null)
                    ps.setTimestamp(8, new Timestamp(payRequest.getAccountPaymentDate().getTime().getTime()));
                else
                    ps.setNull(8, java.sql.Types.DATE);

                ps.setDouble(9, payRequest.getAccountPaymentMny());

                if(payRequest.getAccountPaymentRef()!=null)
                    ps.setString(10, payRequest.getAccountPaymentRef());
                else
                    ps.setNull(10, Types.VARCHAR);

                if (payRequest.getReceiptNo() != null)
                    ps.setString(11, payRequest.getReceiptNo());
                else
                    ps.setNull(11, java.sql.Types.INTEGER);

                ps.setString(12, payRequest.getReceiptBranch());
                ps.setString(13, payRequest.getBranchCounter());
                ps.setString(14, payRequest.getReceiptUser());

                if (payRequest.getReceiptDate() != null)
                    ps.setTimestamp(15,  new Timestamp(payRequest.getReceiptDate().getTime().getTime()));
                else
                    ps.setNull(15, java.sql.Types.DATE);


                ps.setString(16, payRequest.getPaymentMode());
                ps.setString(17, payRequest.getChequeNo());
                ps.setString(18, payRequest.getChequebankCode());
                ps.setString(19, payRequest.getChequebankBranchCode());
                ps.setString(20, payRequest.getRemarks());
                ps.setString(21, payRequest.getTransferReasonCode());
                ps.setInt(22, payRequest.getProductType());
                ps.setString(23, payRequest.getContactNo());
                ps.setInt(24, payRequest.getTranType());

                if(commandRead==null){
                    ps.setString(25, "X");
                    ps.setString(26, "0");
                    ps.setNull(27, Types.VARCHAR);
                }
                else {
                    ps.setString(25, commandRead.name());
                    ps.setString(26, commandRead.getErrorCode());
                    if(payRequest.getErrDesc()==null)ps.setNull(27, Types.VARCHAR);
                    else ps.setString(27, payRequest.getErrDesc());
                }

            }
        });
    }

}
