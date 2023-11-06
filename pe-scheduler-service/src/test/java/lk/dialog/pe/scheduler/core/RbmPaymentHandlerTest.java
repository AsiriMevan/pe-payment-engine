package lk.dialog.pe.scheduler.core;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.scheduler.core.handler.RBMPaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.exception.NEException;
import lk.dialog.pe.scheduler.service.SoapIntegrationService;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.RbmTestDataGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RbmPaymentHandlerTest {
    @InjectMocks
    @Autowired
    @Qualifier("rbmPaymentHandler")
    PaymentHandler rbmPaymentHandler = new RBMPaymentHandler();

    @Mock
    @Autowired
    private SoapIntegrationService soapIntegrationService;

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate peJdbcTemplate;

    Payment paymentInput;
    Payment expectedPayment;
    Payment actualPayment;

    RbmTestDataGenerator rbmTestDataGenerator = new RbmTestDataGenerator();


//    @Test
//    public void getRbmPaymentFromDb() {
//        Payment payment = peJdbcTemplate.queryForObject("select * from rbm_payment where req_id='" + 1234 + "'", new RBMPaymentMapper());
//    }

    @Test
    public  void updateAsFDueToPostPaymentApplicationException(){
        paymentInput = rbmTestDataGenerator.getRbmObjectForApplicationException();
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,peJdbcTemplate);
        rbmPaymentHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.F.name(),actualCommandRead);
        String errorCode = rbmTestDataGenerator.getErrorCodeByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.F.getErrorCode(),errorCode);
    }

    @Test
    public  void updateAsNDueToNEException(){
        paymentInput = rbmTestDataGenerator.getWifiSuccessRbmCashPayment();
        paymentInput.setAccountNo("0000000");
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,peJdbcTemplate);
        rbmPaymentHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.N.name(),actualCommandRead);
        String errorCode = rbmTestDataGenerator.getErrorCodeByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.N.getErrorCode(),errorCode);
    }

    @Test
    public void successWithWifiProductTypeAndCashPayMode(){
        paymentInput = rbmTestDataGenerator.getWifiSuccessRbmCashPayment();
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,peJdbcTemplate);
        rbmPaymentHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),actualCommandRead);
    }

    @Test
    /**
     * need to set unique trxId and receiptNo eachTime to have postChequeRealize success
     * returns -1 from postChqInSuspendState soap call
     */
    public void successWithVolteProductTypeAndChequePayMode(){
        paymentInput = rbmTestDataGenerator.getVolteSuccessRbmChequePayment();
        int trxId = (int) (Math.random() * (60000000 - 50000000)) + 50000000;
        paymentInput.setTrxID(trxId);
        paymentInput.setReceiptNo(Integer.toString(trxId));
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,peJdbcTemplate);
        rbmPaymentHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),actualCommandRead);

    }


    @Test
    public void successWithOtherProductTypeAndDtvSbuAndCashPayModeWithStatusC(){
        paymentInput = rbmTestDataGenerator.getOtherProductTypeAndDtvSbuSuccessCashPayment();
        int trxId = (int) (Math.random() * (60000000 - 50000000)) + 50000000;
        paymentInput.setTrxID(trxId); //   need unique id for DTV_FREEBEE_ON_PAYMENT table(ccbs)
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,peJdbcTemplate);
        rbmPaymentHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),actualCommandRead);
    }

    @Test
    public void successDtvCashPayWithContatusDAndIsReconChargeExistYAndInvalidDisconReason(){
        paymentInput = rbmTestDataGenerator.getDtvCashPaymentWithSContatusDAndIsReconChargeExistYAndInvalidDisconReason();
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,peJdbcTemplate);
        rbmPaymentHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),actualCommandRead);
    }

    @Test
    //disconReason - BDTV
    public void successDtvCashPayWithContatusDAndIsReconChargeExistYAndValidDisconReason(){
        paymentInput = rbmTestDataGenerator.getDtvCashPaymentWithSContatusDAndIsReconChargeExistYAndInvalidDisconReason();
        int trxId = (int) (Math.random() * (60000000 - 50000000)) + 50000000;
        paymentInput.setTrxID(trxId); //   need unique id for DTV_FREEBEE_ON_PAYMENT table(ccbs)
        paymentInput.setContractNo("65000238"); //manually made the isreconChargeNeeded="N" for the contract No, prefer the script for the query
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,peJdbcTemplate);
        rbmPaymentHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),actualCommandRead);
    }

    @Test
    public void successWithOtherProductTypeAndDtvSbuAndCashPayModeWithStatusD(){
        paymentInput = rbmTestDataGenerator.getOtherProductTypeAndDtvSbuSuccessCashPayment();
        paymentInput.setTrxID((int) (Math.random() * (60000000 - 50000000)) + 50000000); //   need unique id for DTV_FREEBEE_ON_PAYMENT table(ccbs)
        paymentInput.setConnectionStatus("D");
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,peJdbcTemplate);
        rbmPaymentHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),actualCommandRead);
    }

    @Test
    //smsProxy call with RBM_CHQ as paymentSystem input
    public void rbmChqSms() throws NEException, DCPEException {
        MockitoAnnotations.initMocks(this);
        paymentInput = rbmTestDataGenerator.getVolteSuccessRbmCashPayment();
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,peJdbcTemplate);
        Mockito.when(soapIntegrationService.postPayment(Mockito.any(),Mockito.anyLong())).thenReturn(-1);
        rbmPaymentHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),actualCommandRead);

    }


}
