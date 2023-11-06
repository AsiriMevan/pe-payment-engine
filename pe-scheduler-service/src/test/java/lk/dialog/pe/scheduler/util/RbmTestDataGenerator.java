package lk.dialog.pe.scheduler.util;

import lk.dialog.pe.common.util.AccountTypeEnum;
import lk.dialog.pe.common.util.ConnectionStatus;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.repository.mapper.RBMPaymentMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class RbmTestDataGenerator {

    private static final long trxId = 2312215;





    public Payment getRbmObjectForApplicationException(){
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=new Date(ts.getTime());
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(date);
        Payment payment = new Payment();
        payment.setTrxID(2312215);
        payment.setProductCategory(1);
        payment.setAccountPaymentStatus(0);
        payment.setAccountPaymentText("PAYMENT - CASH UPG_EZCASH_20210922673216");
        payment.setAccountPaymentRef("UPG_EZCASH_20210922673216");
        payment.setAccountPaymentSeq(0);
        payment.setAccountNo("45015486");
        payment.setContractNo("39171286");
        payment.setAccountPaymentMny(1);
        payment.setAccountPaymentDate(calendarTime);
        payment.setReceiptNo("UPG1632289782695");
        payment.setReceiptBranch("UPG");
        payment.setBranchCounter("ONLINE");
        payment.setReceiptUser("CG_USER");
        payment.setReceiptDate(calendarTime);
        payment.setPaymentMode("CA");
        payment.setPaymentRefNo("PAYMENT - UPG-UPG1632289782695-ONLINE-CASH-UPG_EZCASH_20210922673216");
        payment.setRemarks("ONLINE PAYMENT");
        payment.setAccountType(2);
        payment.setSbu(3);
        payment.setTransferredAmount(0);
        payment.setPaymentCurrency("LKR");
        payment.setPaymentMethodID(1);
        payment.setTerminalID("172.29.67.10");
        payment.setPhysicalPaymentSeq(0);
        payment.setConnRef("761053514");
        payment.setRealtime(false);
        payment.setQuerySystem(0);
        payment.setConnectionStatus("C");


        return payment;
    }
/*
* product type wifi = 1
* paymode CA
*
*/
    public Payment getWifiSuccessRbmCashPayment(){
        Payment payment = getRbmObjectForApplicationException();
        payment.setAccountNo("90003277");
        payment.setAccountPaymentMny(1.0);
        payment.setAccountPaymentRef(null);
        payment.setAccountPaymentText("PAYMENT - CASH ");
        payment.setBranchCounter("PMT");
        payment.setConnRef("CI003816");
        payment.setConnectionStatus(null);
        payment.setContractNo("90003485");
        payment.setPaymentRefNo("PAYMENT - 53632-DBDPBACK00016132-PMT-CASH");
        payment.setProductType(1);
        payment.setReceiptBranch("53632");
        payment.setReceiptNo("DBDPBACK00016132");
        payment.setReceiptUser("007");
        payment.setRemarks(null);
        payment.setTerminalID("::1");
        payment.setTrxID(112514);

        return payment;
    }

    public Payment getOtherProductTypeAndDtvSbuSuccessCashPayment(){
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=new Date(ts.getTime());
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(date);
        Payment payment = new Payment();
        payment.setProductCategory(1);
        payment.setTrxID(2006260);
        payment.setAccountPaymentStatus(0);
        payment.setCreatedDtm(calendarTime);
        payment.setAccountPaymentText("PAYMENT - CASH ");
        payment.setAccountPaymentSeq(0);
        payment.setAccountNo("60717693");
        payment.setContractNo("60714926");
        payment.setAccountPaymentMny(20000.0);
        payment.setAccountPaymentDate(calendarTime);
        payment.setReceiptNo("FBPPARP00872916");
        payment.setReceiptBranch("710");
        payment.setBranchCounter("PMT");
        payment.setReceiptUser("DTQA00001");
        payment.setReceiptDate(calendarTime);
        payment.setPaymentMode("CA");
        payment.setPaymentRefNo("PAYMENT - 710-FBPPARP00872916-PMT-CASH");
        payment.setAccountType(2);
        payment.setSbu(2);
        payment.setTransferredAmount(0.0);
        payment.setTransferredType(0);
        payment.setChequeSuspend("SUSPEND");
        payment.setProductType(0);
        payment.setPaymentCurrency("LKR");
        payment.setPaymentMethodID(1);
        payment.setTerminalID("10.0.196.216");
        payment.setPhysicalPaymentSeq(0);
        payment.setConnRef("60715070");
        payment.setRealtime(false);
        payment.setQuerySystem(0);
        payment.setTranType(0);
        payment.setConnectionStatus("C");
        return payment;
    }
    //manually made the isreconChargeNeeded="N" for the contract No, prefer the script for the query
    public Payment getDtvCashPaymentWithSContatusDAndIsReconChargeExistYAndInvalidDisconReason(){
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=new Date(ts.getTime());
        Calendar calendarTime = Calendar.getInstance();
        Payment payment = new Payment();
        payment.setProductCategory(1);
        payment.setTrxID(1075321);
        payment.setAccountPaymentStatus(0);
        payment.setCreatedDtm(calendarTime);
        payment.setAccountPaymentText("PAYMENT - CASH K0AWZ44A");
        payment.setAccountPaymentRef("K0AWZ44A");
        payment.setAccountPaymentSeq(0);
        payment.setAccountNo("65000256");
        payment.setContractNo("65000234");
        payment.setAccountPaymentMny(10.0);
        payment.setAccountPaymentDate(calendarTime);
        payment.setReceiptNo("FBBPNNASN04261");
        payment.setReceiptBranch("53632");
        payment.setBranchCounter("PMT");
        payment.setReceiptUser("007");
        payment.setReceiptDate(calendarTime);
        payment.setPaymentMode("CA");
        payment.setPaymentRefNo("PAYMENT - 53632-FBBPNNASN04261-PMT-CASH-K0AWZ44A");
        payment.setAccountType(2);
        payment.setSbu(2);
        payment.setTransferredAmount(0.0);
        payment.setTransferredType(0);
        payment.setProductType(0);
        payment.setPaymentCurrency("LKR");
        payment.setPaymentMethodID(1);
        payment.setTerminalID("10.0.21.106");
        payment.setPhysicalPaymentSeq(0);
        payment.setConnRef("65000234");
        payment.setRealtime(false);
        payment.setQuerySystem(0);
        payment.setTranType(0);
        payment.setConnectionStatus("D");
        return payment;
    }

    public Payment getRbmResubmitFailSelectPayment(){
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=new Date(ts.getTime());
        Calendar calendarTime = Calendar.getInstance();
        Payment payment = new Payment();
        payment.setProductCategory(1);
        payment.setTrxID(112472);
        payment.setAccountPaymentStatus(0);
        payment.setCreatedDtm(calendarTime);
        payment.setAccountPaymentText("PAYMENT - CASH ");
        payment.setAccountPaymentSeq(0);
        payment.setAccountNo("90003277");
        payment.setContractNo("90003485");
        payment.setAccountPaymentMny(1.0);
        payment.setAccountPaymentDate(calendarTime);
        payment.setReceiptNo("DBDPBACK00016160");
        payment.setReceiptBranch("53632");
        payment.setBranchCounter("PMT");
        payment.setReceiptUser("007");
        payment.setReceiptDate(calendarTime);
        payment.setPaymentMode("CA");
        payment.setPaymentRefNo("PAYMENT - 53632-DBDPBACK00016160-PMT-CASH");
        payment.setAccountType(2);
        payment.setSbu(3);
        payment.setTransferredAmount(0.0);
        payment.setTransferredType(0);
        payment.setChequeSuspend("SUSPEND");
        payment.setProductType(1);
        payment.setPaymentCurrency("LKR");
        payment.setPaymentMethodID(1);
        payment.setTerminalID("::1");
        payment.setPhysicalPaymentSeq(0);
        payment.setConnRef("CI003816");
        payment.setRealtime(false);
        payment.setQuerySystem(0);
        payment.setTranType(0);
        return payment;
    }

    public static void printObjectValueSetters(String objectName,Object object){
        String convertedStr = SchUtil.convertToStringPretty(object);
        String[] keyValArray = convertedStr.split(",");
        Arrays.stream(keyValArray).forEach(keyValStr->{
            String[] pair = keyValStr.split(":");

            String value = pair[1].replace("}","").trim();

            if(!value.equals("null")){

                String key = pair[0].replaceAll("\"","").replace("{","").trim();
                key = key.substring(0, 1).toUpperCase() + key.substring(1);

                System.out.println(objectName+".set"+key+"("+value+");");
            }
        });
    }

    public Payment getVolteSuccessRbmChequePayment(){
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=new Date(ts.getTime());
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(date);

        Payment payment = new Payment();
        payment.setProductCategory(2);
        payment.setTrxID(44112899);
        payment.setAccountPaymentStatus(0);
        payment.setCreatedDtm(calendarTime);
        payment.setAccountPaymentText("PAYMENT - CHEQ 7010774712345  - DIRECT CREDIT");
        payment.setAccountPaymentRef("7010774712345");
        payment.setAccountPaymentSeq(0);
        payment.setAccountNo("70001101");
        payment.setContractNo("70001043");
        payment.setAccountPaymentMny(6.0);
        payment.setAccountPaymentDate(calendarTime);
        payment.setReceiptNo("null");
        payment.setReceiptBranch("53632");
        payment.setBranchCounter("PMT");
        payment.setReceiptUser("CPOS");
        payment.setReceiptDate(calendarTime);
        payment.setPaymentMode("CHE");
        payment.setPaymentRefNo("PAYMENT - 53632-DBBPBACK00016259-PMT-CHEQ-7010774712345");
        payment.setRemarks("TEST PAY - RBM TREAD");
        payment.setAccountType(2);
        payment.setSbu(4);
        payment.setTransferredAmount(0.0);
        payment.setTransferredType(0);
        payment.setChequebankCode("7010");
        payment.setChequebankBranchCode("774");
        payment.setChequeNo("712345");
        payment.setChequeDate(calendarTime);
        payment.setChequeSuspend("SUSPEND");
        payment.setProductType(5);
        payment.setPaymentCurrency("LKR");
        payment.setPaymentMethodID(2);
        payment.setTerminalID("10.0.21.145");
        payment.setPhysicalPaymentSeq(0);
        payment.setConnRef("0654302157");
        payment.setRealtime(false);
        payment.setQuerySystem(0);
        payment.setTranType(0);

        return payment;
    }

    public Payment getVolteSuccessRbmCashPayment(){
        Timestamp ts=new Timestamp(System.currentTimeMillis());
        Date date=new Date(ts.getTime());
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTime(date);

        Payment payment = new Payment();
        payment.setTrxID(988617);
        payment.setAccountPaymentStatus(0);
        payment.setCreatedDtm(calendarTime);
        payment.setAccountPaymentText("PAYMENT - CASH K0AWZ44A");
        payment.setAccountPaymentRef("K0AWZ44A");
        payment.setAccountPaymentSeq(0);
        payment.setAccountNo("93000029");
        payment.setContractNo("93000029");
        payment.setAccountPaymentMny(55.75);
        payment.setAccountPaymentDate(calendarTime);
        payment.setReceiptNo("FBBPNNAS442");
        payment.setReceiptBranch("53632");
        payment.setBranchCounter("PMT");
        payment.setReceiptUser("007");
        payment.setReceiptDate(calendarTime);
        payment.setPaymentMode("CA");
        payment.setPaymentRefNo("PAYMENT - 53632-FBBPNNAS442-PMT-CASH-K0AWZ44A");
        payment.setAccountType(2);
        payment.setSbu(4);
        payment.setTransferredAmount(0.0);
        payment.setTransferredType(0);
        payment.setProductType(5);
        payment.setPaymentCurrency("LKR");
        payment.setPaymentMethodID(1);
        payment.setTerminalID("10.0.21.106");
        payment.setPhysicalPaymentSeq(0);
        payment.setConnRef("114386002");
        payment.setRealtime(false);
        payment.setQuerySystem(0);
        payment.setTranType(0);
        payment.setConnectionStatus("D");
        payment.setProductCategory(2);
        return payment;
    }


    public Payment getRbmPaymentFromDb(Payment paymentSource ,JdbcTemplate peJdbcTemplate){
        Payment payment = peJdbcTemplate.queryForObject("select * from rbm_payment where req_id='"+paymentSource.getTrxID()+"'", new RBMPaymentMapper());
        return payment;
    }

    public  void saveRBMPaymentToDb(Payment payment,JdbcTemplate peJdbcTemplate){
        deleteRBMPaymentFromDb(payment.getTrxID(),peJdbcTemplate);
        lodgeRBMPayment(payment,null,peJdbcTemplate);
    }

    public  void saveRBMPaymentToDb(Payment payment,COMMAND_READ commandRead,JdbcTemplate peJdbcTemplate){
        deleteRBMPaymentFromDb(payment.getTrxID(),peJdbcTemplate);
        lodgeRBMPayment(payment,commandRead,peJdbcTemplate);
    }

    public  void deleteRBMPaymentFromDb(long trxId , JdbcTemplate peJdbcTemplate){

        try{
            int result = peJdbcTemplate.update("delete from rbm_payment where req_id='"+trxId+"'");
            System.out.println("deleted rbm payment with trxId="+trxId+"result="+result);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("no rbm payment records to delete");
        }
    }

    public  String getCommandReadByTrxIdFromDb(Long trxId, JdbcTemplate peJdbcTemplate){
        return peJdbcTemplate.queryForObject("select command_read from rbm_payment where req_id='"+trxId+"'",String.class);
    }

    public  Map<String,String> getDtvFreeBeeOnPaymentById(Long trxId, JdbcTemplate peJdbcTemplate){
        return peJdbcTemplate.queryForObject("select record_id from DTV_FREEBEE_ON_PAYMENT where record_id='"+trxId+"'", Map.class);
    }



//    public   setDtvFreeBeeOnPaymentDisconReason(Long trxId,String disconReason, JdbcTemplate peJdbcTemplate){
//       peJdbcTemplate.update("set record_id from DTV_FREEBEE_ON_PAYMENT where record_id='"+trxId+"'", Map.class);
//    }

    public  String getErrorCodeByTrxIdFromDb(Long trxId, JdbcTemplate peJdbcTemplate){
        return peJdbcTemplate.queryForObject("select error_code from rbm_payment where req_id='"+trxId+"'",String.class);
    }
    public void lodgeRBMPayment(Payment payRequest ,COMMAND_READ commandRead,JdbcTemplate peJdbcTemplate) {
        /* insert payment to RBM payment table */
        String sqlPayPostStr = QueryConfig.getQueryByKey("SQL_RBM_PAYMENT_POST");
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
                if (payRequest.getAccountPaymentDate() != null) ps.setTimestamp(8, new Timestamp(payRequest.getAccountPaymentDate().getTime().getTime()));
                else ps.setNull(8, java.sql.Types.DATE);
                    ps.setDouble(9, payRequest.getAccountPaymentMny());

                ps.setString(10, payRequest.getPaymentCurrency());
                    ps.setInt(11, payRequest.getPaymentMethodID());
                ps.setString(12, payRequest.getAccountPaymentText());
                if(payRequest.getAccountPaymentRef()!=null)
                    ps.setString(13, payRequest.getAccountPaymentRef());
                else
                    ps.setNull(13, Types.VARCHAR);
                ps.setString(14, payRequest.getReceiptNo());
                ps.setString(15, payRequest.getReceiptBranch());
                ps.setString(16, payRequest.getBranchCounter());
                ps.setString(17, payRequest.getReceiptUser());
                if (payRequest.getReceiptDate() != null)
                    ps.setTimestamp(18,  new Timestamp(payRequest.getReceiptDate().getTime().getTime()));
                else
                    ps.setNull(18, java.sql.Types.DATE);
                ps.setString(19, payRequest.getPaymentMode());
                ps.setString(20, payRequest.getAccountPaymentText());
                ps.setString(21, payRequest.getTerminalID());
                ps.setString(22, payRequest.getChequeNo());
                ps.setString(23, payRequest.getChequebankCode());
                ps.setString(24, payRequest.getChequebankBranchCode());
                if (payRequest.getChequeDate() != null)
                    ps.setTimestamp(25, new Timestamp((payRequest.getChequeDate().getTime().getTime())));
                else
                    ps.setNull(25, java.sql.Types.DATE);

                ps.setString(26, payRequest.getChequeSuspend());

            ps.setString(27, payRequest.getCcType());
                ps.setString(28, payRequest.getCcBankCode());
                ps.setString(29, payRequest.getCcNo());
                ps.setString(30, payRequest.getCcApprovalCode());
                ps.setString(31, "CG");
                ps.setInt(32, payRequest.getTransferredType());
                ps.setString(33, payRequest.getTransferredMode());
                ps.setString(34, payRequest.getTransferredNo());
                if(payRequest.getRemarks()==null)
                    ps.setString(35, payRequest.getRemarks());
                else
                    ps.setNull(35, Types.VARCHAR);
                ps.setString(36, payRequest.getTransferReasonCode());
                    ps.setInt(37, payRequest.getProductType());
                ps.setString(38, payRequest.getContactNo());
                if(payRequest.getConnectionStatus()!=null)
                    ps.setString(39, ConnectionStatus.getConnectionStatus(payRequest.getConnectionStatus()).name());
                else
                    ps.setNull(39, Types.VARCHAR);

                if(commandRead==null){
                    ps.setString(40, "X");
                    ps.setString(41, "0");
                    ps.setNull(42, Types.VARCHAR);
                }
                else {
                    ps.setString(40, commandRead.name());
                    ps.setString(41, commandRead.getErrorCode());
                    if(payRequest.getErrDesc()==null)ps.setNull(42, Types.VARCHAR);
                    else ps.setString(42, payRequest.getErrDesc());
                }



            }
        });
    }

}
