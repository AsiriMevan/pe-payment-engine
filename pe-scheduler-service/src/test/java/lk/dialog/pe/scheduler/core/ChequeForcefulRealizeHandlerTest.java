package lk.dialog.pe.scheduler.core;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.scheduler.core.handler.ChequeForcefulRealizeHandler;
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
import org.junit.jupiter.api.Order;
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

import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ChequeForcefulRealizeHandlerTest {

    @InjectMocks
    @Autowired
    @Qualifier("chequeForcefulRealizeHandler")
    PaymentHandler chequeRealizeHandler = new ChequeForcefulRealizeHandler();

    @Mock
    @Autowired
    private SoapIntegrationService soapIntegrationService;

    @Mock
    @Autowired
    ReconnectionService reconnectionService;

    @Mock
    @Autowired
    SmsService smsService;

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate peJdbcTemplate;

    Payment paymentInput;

    ChequeTestDataGenerator testDataGenerator = new ChequeTestDataGenerator();

    @Test
    public void loadPaymentsIntegrationTest(){  
         paymentInput = testDataGenerator.getSuccessPayment();
        paymentInput.setTrxID(1);
        paymentInput.setReceiptNo("1");
        testDataGenerator.savePaymentToDb(paymentInput,null,paymentInput.isRealtime(),peJdbcTemplate);
        paymentInput.setTrxID(2);
        paymentInput.setReceiptNo("2");
        testDataGenerator.savePaymentToDb(paymentInput,null,paymentInput.isRealtime(),peJdbcTemplate);
        List<Payment> actualPayments =  chequeRealizeHandler.loadPaymentData();
        Assert.assertEquals(2,actualPayments.size());
        assertUpdates(1L,"Y",paymentInput.getErrCode(),paymentInput.getErrDesc(),true);
        assertUpdates(2L,"Y",paymentInput.getErrCode(),paymentInput.getErrDesc(),true);

    }

    //main-update :update of command_read = P , realTime = Y with soap success (response=1)
    @Test
    @Order(1)
    public void checkSuspendIntegrationSuccessTest(){
        paymentInput = testDataGenerator.getSuccessPayment();
        int trxId = (int) (Math.random() * (60000000 - 50000000)) + 50000000;
        paymentInput.setTrxID(trxId);
        paymentInput.setReceiptNo(String.valueOf(trxId));
        testDataGenerator.savePaymentToDb(paymentInput,null,true,peJdbcTemplate);
        chequeRealizeHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"P","1","SUCCESS",true);

    }

    //main-update :fail scenario with soap failed
    @Test
    public void checkSuspendFailure() throws NEException, DCPEException {
        MockitoAnnotations.initMocks( this );
        paymentInput = testDataGenerator.getSuccessPayment();
        testDataGenerator.savePaymentToDb(paymentInput,null,true,peJdbcTemplate);
        Mockito.doThrow(new AxisError("AXIS_ERROR")).when(soapIntegrationService).postChqInSuspendState(Mockito.any(PaymentDTO.class),Mockito.anyLong());
        chequeRealizeHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"F","2","Error:",true);

    }

    /* not testing X , N scenario since it doesn't lead to any outcome */

    //full-update : commandRead = P , realtime = Y
    @Test
    public void commandReadPAndRealTimeYSuccess() throws NEException, DCPEException {
        MockitoAnnotations.initMocks( this );
        paymentInput = testDataGenerator.getSuccessPayment();
        PaymentDTO paymentDTO = new PaymentDTO(paymentInput);

        Mockito.doReturn(1).when(soapIntegrationService).postPayment(paymentDTO,paymentInput.getTrxID());
        Mockito.doNothing().when(reconnectionService).reconnect(paymentInput.getContractNo(),paymentInput.getAccountPaymentMny());
        Mockito.doNothing().when(smsService).sendSmsProxy(Mockito.isA(Payment.class),Mockito.isA(PAYMENT_SYSTEM.class));

        testDataGenerator.savePaymentToDb(paymentInput,null,true,peJdbcTemplate);
        chequeRealizeHandler.execute();//to save P , Y status record
        assertUpdates(paymentInput.getTrxID(),"P","1","SUCCESS",false);

        testDataGenerator.takeUpdateTimeTenMinutesBackByTrxId(paymentInput.getTrxID(),peJdbcTemplate);
        chequeRealizeHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"S","1","SUCCESS",true);

    }
    //full-update : commandRead = P , realtime = Y , errorCode = 2
    @Test
    public void FailureWithRealTimeYAndCommandReadPAndErrorCodeTwo() throws NEException, DCPEException {
        MockitoAnnotations.initMocks( this );
        paymentInput = testDataGenerator.getSuccessPayment();
        PaymentDTO paymentDTO = new PaymentDTO(paymentInput);

        Mockito.doNothing().when(reconnectionService).reconnect(paymentInput.getContractNo(),paymentInput.getAccountPaymentMny());
        Mockito.doNothing().when(smsService).sendSmsProxy(Mockito.isA(Payment.class),Mockito.isA(PAYMENT_SYSTEM.class));

        testDataGenerator.savePaymentToDb(paymentInput,COMMAND_READ.P,true,peJdbcTemplate);
        testDataGenerator.takeUpdateTimeTenMinutesBackByTrxId(paymentInput.getTrxID(),peJdbcTemplate);
        testDataGenerator.changeErrorCodeByTrxId("2",paymentInput.getTrxID(),peJdbcTemplate);
        chequeRealizeHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"W","2","Fail record exist in the batch. Reason ::",true);
    }

    //full-update : NoCxFoundException
    @Test
    public void FailureWithNoCxFoundException() throws NEException, DCPEException {
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

        Mockito.doThrow(new NEException("NO_CX_FOUND")).when(soapIntegrationService).queryUnrealizedCheques(Mockito.any(PaymentDTO.class));
        Mockito.doNothing().when(reconnectionService).reconnect(paymentInput.getContractNo(),paymentInput.getAccountPaymentMny());
        Mockito.doNothing().when(smsService).sendSmsProxy(Mockito.isA(Payment.class),Mockito.isA(PAYMENT_SYSTEM.class));

        chequeRealizeHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"P","1","SUCCESS",true);
        assertUpdates(nAndNPayment.getTrxID(),"N","2","ForcefulRealization failed due to:NO_CX_FOUND",true);
    }

    //full-update : failure with soap generic exception
    @Test
    public void FailureWithSoapGenericException() throws NEException, DCPEException {
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

        Mockito.doThrow(new AxisError("AXIS_ERROR")).when(soapIntegrationService).queryUnrealizedCheques(Mockito.any(PaymentDTO.class));
        Mockito.doNothing().when(reconnectionService).reconnect(paymentInput.getContractNo(),paymentInput.getAccountPaymentMny());
        Mockito.doNothing().when(smsService).sendSmsProxy(Mockito.isA(Payment.class),Mockito.isA(PAYMENT_SYSTEM.class));

        chequeRealizeHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"W","2","Req_ID:526061 Payment not posted",true);
        assertUpdates(nAndNPayment.getTrxID(),"W","2","Req_ID:10000 Payment not posted",true);
    }
    //full-update : failure with invalid checkStatus from queryUnrealizedCheques
    @Test
    public void FailureWithInvalidChequeStatus() throws NEException, DCPEException {
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

        Mockito.doReturn("INVALID_RESPONSE").when(soapIntegrationService).queryUnrealizedCheques(Mockito.any(PaymentDTO.class));
        Mockito.doNothing().when(reconnectionService).reconnect(paymentInput.getContractNo(),paymentInput.getAccountPaymentMny());
        Mockito.doNothing().when(smsService).sendSmsProxy(Mockito.isA(Payment.class),Mockito.isA(PAYMENT_SYSTEM.class));

        chequeRealizeHandler.execute();
        assertUpdates(paymentInput.getTrxID(),"S","1","SUCCESS",true);
        assertUpdates(nAndNPayment.getTrxID(),"F","2","ForcefulRealization failed since chequeStatus:INVALID_RESPONSE",true);
    }
    //full-update : success valid checkStatus = SUSPEND from queryUnrealizedCheques
    @Test
    public void SuccessWithValidChequeStatus() throws NEException, DCPEException {
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

        chequeRealizeHandler.execute();
        Mockito.verify(smsService,Mockito.times(1)).sendSmsProxy(Mockito.any(Payment.class), Mockito.any(PAYMENT_SYSTEM.class)); //checking isRetry parameter
        assertUpdates(paymentInput.getTrxID(),"S","1","SUCCESS",true);
        assertUpdates(nAndNPayment.getTrxID(),"S","1","SUCCESS",true);
    }


    private void assertUpdates(Long trxId,String commandRead,String errorCode,String errorDesc,boolean deleteRecord){
        Payment payment = testDataGenerator.getPaymentFromDb(trxId,peJdbcTemplate);
        Assert.assertEquals(commandRead,payment.getReadStatus());
        Assert.assertEquals(errorCode,payment.getErrCode());
        Assert.assertTrue(payment.getErrDesc().startsWith(errorDesc));
        if(deleteRecord)testDataGenerator.deletePaymentFromDb(trxId,peJdbcTemplate);

    }

}
