package lk.dialog.pe.scheduler.core.retry;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.scheduler.core.PaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.exception.NEException;
import lk.dialog.pe.scheduler.service.ReconnectionService;
import lk.dialog.pe.scheduler.service.SmsService;
import lk.dialog.pe.scheduler.service.SoapIntegrationService;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.ChequeTestDataGenerator;
import lk.dialog.pe.scheduler.util.PAYMENT_SYSTEM;
import org.apache.axis2.engine.AxisError;
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
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ChequeForcefulRealizeFailHandlerTest {

    @InjectMocks
    @Autowired
    @Qualifier("chequeForcefulRealizeFailHandler")
    PaymentHandler chequeRealizeFailHandler = new ChequeForcefulRealizeFailHandler();

    @Mock
    @Autowired
    private SoapIntegrationService soapIntegrationService;

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate peJdbcTemplate;

    @Mock
    @Autowired
    ReconnectionService reconnectionService;

    @Mock
    @Autowired
    SmsService smsService;

    Payment paymentInput;

    ChequeTestDataGenerator testDataGenerator = new ChequeTestDataGenerator();

    @Test
    public void SuccessWithPaySequenceNegativeTwo() throws NEException, DCPEException {
        setInitData();
        Mockito.doThrow(new NEException("NO_SUSPEND_CHEQUE_FOUND")).when(soapIntegrationService).queryUnrealizedCheques(Mockito.any(PaymentDTO.class));

        chequeRealizeFailHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"P","1","SUCCESS",true);

    }

    @Test
    public void retryInFutureWithPaySequenceNegativeOne() throws NEException, DCPEException {
        setInitData();
        Mockito.doThrow(new AxisError("AXIS_ERROR")).when(soapIntegrationService).queryUnrealizedCheques(Mockito.any(PaymentDTO.class));
        testDataGenerator.savePaymentToDb(paymentInput, COMMAND_READ.F,true,peJdbcTemplate);

        chequeRealizeFailHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"F","2","Error:AXIS_ERROR",true);

    }

    //payment-already-exist
    @Test
    public void succesWithPaySequenceOtherThanNegativeOneOrTwo() throws NEException, DCPEException {
        setInitData();
        Mockito.doReturn("SUSPEND").when(soapIntegrationService).queryUnrealizedCheques(Mockito.any(PaymentDTO.class));

        chequeRealizeFailHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"P","1","PAYMENT-ALREADY-EXIST",true);
    }

    @Test
    public void testChequeSusPendExceptionFailure() throws NEException, DCPEException {
        setInitData();
        Mockito.doThrow(new NEException("NO_SUSPEND_CHEQUE_FOUND")).when(soapIntegrationService).queryUnrealizedCheques(Mockito.any(PaymentDTO.class));
        Mockito.doThrow(new HttpClientErrorException(HttpStatus.BAD_GATEWAY)).when(soapIntegrationService).postChqInSuspendState(Mockito.any(PaymentDTO.class),Mockito.anyLong());

        chequeRealizeFailHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"F","2","Error:502 BAD_GATEWAY",true);
    }

    @Test
    public void verifySMSSendIsNotTriggered() throws NEException, DCPEException {
        MockitoAnnotations.initMocks( this );
        paymentInput = testDataGenerator.getSuccessPayment();
        Payment nAndNPayment = testDataGenerator.getSuccessPayment(); //payment with command read=N and realTime= N
        nAndNPayment.setTrxID(10000);
        nAndNPayment.setReceiptNo("10000");
        nAndNPayment.setChequeNo(paymentInput.getChequeNo());

        testDataGenerator.savePaymentToDb(nAndNPayment,COMMAND_READ.N,false,peJdbcTemplate);//since queryUnrealizedCheques is only done for the first one in the table has to move this up
        testDataGenerator.takeUpdateTimeTenMinutesBackByTrxId(nAndNPayment.getTrxID(),peJdbcTemplate);
        testDataGenerator.savePaymentToDb(paymentInput,COMMAND_READ.P,true,peJdbcTemplate);
        testDataGenerator.takeUpdateTimeTenMinutesBackByTrxId(paymentInput.getTrxID(),peJdbcTemplate);

        Mockito.doReturn(Constants.CHQ_MODE_SUSPEND).when(soapIntegrationService).queryUnrealizedCheques(Mockito.any(PaymentDTO.class));
        Mockito.doNothing().when(reconnectionService).reconnect(paymentInput.getContractNo(),paymentInput.getAccountPaymentMny());
        Mockito.doNothing().when(smsService).sendSmsProxy(Mockito.isA(Payment.class),Mockito.isA(PAYMENT_SYSTEM.class));

        chequeRealizeFailHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"S","1","SUCCESS",true);
        assertUpdates(nAndNPayment.getTrxID(),"S","1","SUCCESS",true);
        Mockito.verify(smsService,Mockito.times(0)).sendSmsProxy(Mockito.any(Payment.class), Mockito.any(PAYMENT_SYSTEM.class));
    }

    private Payment assertUpdates(Long trxId,String commandRead,String errorCode,String errorDesc,boolean deleteRecord){
        Payment payment = testDataGenerator.getPaymentFromDb(trxId,peJdbcTemplate);
        Assert.assertEquals(commandRead,payment.getReadStatus());
        Assert.assertEquals(errorCode,payment.getErrCode());
        Assert.assertTrue(payment.getErrDesc().startsWith(errorDesc));
        if(deleteRecord)testDataGenerator.deletePaymentFromDb(trxId,peJdbcTemplate);
        return payment;

    }

    private void setInitData(){
        MockitoAnnotations.initMocks( this );
        paymentInput = testDataGenerator.getSuccessPayment();
        paymentInput.setErrDesc("Account does not exist"); //error comes from retry_error_desc table
        testDataGenerator.savePaymentToDb(paymentInput, COMMAND_READ.F,true,peJdbcTemplate);
    }
}
