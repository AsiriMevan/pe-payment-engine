package lk.dialog.pe.scheduler.core.retry;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.PAY_MODE;
import lk.dialog.pe.scheduler.core.PaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.exception.NEException;
import lk.dialog.pe.scheduler.service.ReconnectionService;
import lk.dialog.pe.scheduler.service.SmsService;
import lk.dialog.pe.scheduler.service.SoapIntegrationService;
import lk.dialog.pe.scheduler.soap.custom.ApplicationExceptionError;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.PAYMENT_SYSTEM;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RBMPaymentFailHandlerTest {

    @Autowired
    @Qualifier("rbmPaymentFailHandler")
    @InjectMocks
    PaymentHandler rbmPaymentFailHandler = new RBMPaymentFailHandler();

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate peJdbcTemplate;

    Payment paymentInput;
    Payment expectedPayment;
    Payment actualPayment;

    private static final String failErrorDesc = "Read timed out";
    @Autowired
    @Mock
    SoapIntegrationService soapIntegrationService;

    @Autowired
    @Mock
    ReconnectionService reconnectionService;

    @Autowired
    @Mock
    SmsService smsService;

    RbmTestDataGenerator rbmTestDataGenerator = new RbmTestDataGenerator();


    @Test
    public void validateRetryParameter(){

    }

    @Test
    public void loadRetryPaymentsIntegrationTest(){
        List<Payment> expectedPayments = new ArrayList<>();
        for(int i=1;i<=8;i++) {
            Payment payment= rbmTestDataGenerator.getRbmResubmitFailSelectPayment();
            payment.setTrxID(i);
            payment.setReceiptNo(String.valueOf(i));
            expectedPayments.add(payment);
        }
        expectedPayments.get(0).setErrDesc("java.net.SocketTimeoutException");
        expectedPayments.get(1).setErrDesc("WstxUnexpectedCharException");
        expectedPayments.get(2).setErrDesc("Account does not exist");
        expectedPayments.get(3).setErrDesc("java.net.SocketException");
        expectedPayments.get(4).setErrDesc("Read timed out");
        expectedPayments.get(5).setErrDesc("Connection refused");
        expectedPayments.get(6).setErrDesc("Could not instantiate bean");
        expectedPayments.get(7).setErrDesc("Error:Transport error: 401 Error: ");

        expectedPayments.stream().forEach(payment->{
            rbmTestDataGenerator.saveRBMPaymentToDb(payment,peJdbcTemplate);
        });
        List<Payment> payments = rbmPaymentFailHandler.loadPaymentData();
        Assert.assertEquals(expectedPayments.size(),payments.size());

        expectedPayments.stream().forEach(payment->{
            rbmTestDataGenerator.deleteRBMPaymentFromDb(payment.getTrxID(),peJdbcTemplate);
        });
        }

        @Test
        public void validatePaymentDataIntegrationTest() throws DCPEException {
            expectedPayment= rbmTestDataGenerator.getRbmResubmitFailSelectPayment();
            Integer paymentSequence = rbmPaymentFailHandler.validatePaymentData(expectedPayment);
            System.out.println(paymentSequence);
        }

        @Test
        public void successWithChequePaymodeAndPaymentSeqNegativeTwo() throws NEException, DCPEException {
            MockitoAnnotations.initMocks( this );

            paymentInput = rbmTestDataGenerator.getRbmResubmitFailSelectPayment();
            paymentInput.setPaymentMode(PAY_MODE.CHQ.getValue());
            paymentInput.setErrCode("2");
            paymentInput.setErrDesc(failErrorDesc);
            rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,COMMAND_READ.F,peJdbcTemplate);

            PaymentDTO paymentDTO = new PaymentDTO(paymentInput);

            paymentDTO.setPhysicalPaymentSeq(-2);
            Mockito.doReturn(paymentDTO).when(soapIntegrationService).queryPaymentsSummery(paymentDTO.getReceiptNo(),null);

            Mockito.when(soapIntegrationService.postPayment(paymentDTO,paymentInput.getTrxID())).thenReturn(-1);
            Mockito.doNothing().when(smsService).sendSmsProxy( Mockito.any(Payment.class), Mockito.any(PAYMENT_SYSTEM.class));
            Mockito.doNothing().when(reconnectionService).reconnect(paymentInput.getContractNo(), paymentInput.getAccountPaymentMny());

            rbmPaymentFailHandler.execute();

            String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
            rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
            Assert.assertEquals(COMMAND_READ.S.name(),actualCommandRead);
        }

    @Test
    public void failureWithChequePaymodeAndPaymentSeqNegativeOne() throws NEException, DCPEException {
        MockitoAnnotations.initMocks( this );

        paymentInput = rbmTestDataGenerator.getRbmResubmitFailSelectPayment();
        paymentInput.setPaymentMode(PAY_MODE.CHQ.getValue());
        paymentInput.setErrCode("2");
        paymentInput.setErrDesc(failErrorDesc);
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,COMMAND_READ.F,peJdbcTemplate);

        PaymentDTO paymentDTO = new PaymentDTO(paymentInput);

        paymentDTO.setPhysicalPaymentSeq(-1);
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentDTO.getReceiptNo(),null)).thenReturn(paymentDTO);
        Mockito.when(soapIntegrationService.postPayment(Mockito.any(PaymentDTO.class),Mockito.anyLong())).thenReturn(-1);
        Mockito.doNothing().when(smsService).sendSmsProxy( Mockito.any(Payment.class), Mockito.any(PAYMENT_SYSTEM.class));
        Mockito.doNothing().when(reconnectionService).reconnect(paymentInput.getContractNo(), paymentInput.getAccountPaymentMny());

        rbmPaymentFailHandler.execute();

        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.F.name(),actualCommandRead);
    }

    @Test
    public void sucessWithChequePaymodeAndPaymentSeqPositive() throws NEException, DCPEException {
        MockitoAnnotations.initMocks( this );

        paymentInput = rbmTestDataGenerator.getRbmResubmitFailSelectPayment();
        paymentInput.setPaymentMode(PAY_MODE.CHQ.getValue());
        paymentInput.setErrCode("2");
        paymentInput.setErrDesc(failErrorDesc);
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,COMMAND_READ.F,peJdbcTemplate);

        PaymentDTO paymentDTO = new PaymentDTO(paymentInput);

        paymentDTO.setPhysicalPaymentSeq(10);
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentDTO.getReceiptNo(),null)).thenReturn(paymentDTO);
        Mockito.when(soapIntegrationService.postPayment(Mockito.any(PaymentDTO.class),Mockito.anyLong())).thenReturn(-1);
        Mockito.doNothing().when(smsService).sendSmsProxy( Mockito.any(Payment.class), Mockito.any(PAYMENT_SYSTEM.class));
        Mockito.doNothing().when(reconnectionService).reconnect(paymentInput.getContractNo(), paymentInput.getAccountPaymentMny());

        rbmPaymentFailHandler.execute();

        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),actualCommandRead);
    }
    @Test
    public void failureDueToNeException() throws DCPEException, NEException {
        MockitoAnnotations.initMocks( this );

        paymentInput = rbmTestDataGenerator.getRbmResubmitFailSelectPayment();
        paymentInput.setPaymentMode(PAY_MODE.CHQ.getValue());
        paymentInput.setErrCode("2");
        paymentInput.setErrDesc(failErrorDesc);
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,COMMAND_READ.F,peJdbcTemplate);

        PaymentDTO paymentDTO = new PaymentDTO(paymentInput);

        paymentDTO.setPhysicalPaymentSeq(-2);
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentDTO.getReceiptNo(),null)).thenReturn(paymentDTO);
        Mockito.when(soapIntegrationService.postChqInSuspendState(Mockito.any(PaymentDTO.class),Mockito.anyLong())).thenThrow(new NEException("No Cx Found"));

        rbmPaymentFailHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.N.name(),actualCommandRead);


    }

    @Test
    public void failureDueToException()throws DCPEException, NEException {
        MockitoAnnotations.initMocks( this );

        paymentInput = rbmTestDataGenerator.getRbmResubmitFailSelectPayment();
        paymentInput.setPaymentMode(PAY_MODE.CHQ.getValue());
        paymentInput.setErrCode("2");
        paymentInput.setErrDesc(failErrorDesc);
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput,COMMAND_READ.F,peJdbcTemplate);

        PaymentDTO paymentDTO = new PaymentDTO(paymentInput);

        paymentDTO.setPhysicalPaymentSeq(-2);
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentDTO.getReceiptNo(),null)).thenReturn(paymentDTO);
        Mockito.when(soapIntegrationService.postChqInSuspendState(Mockito.any(PaymentDTO.class),Mockito.anyLong())).thenThrow(new DCPEException("Application Exception",new ApplicationExceptionError("application exception")));

        rbmPaymentFailHandler.execute();
        String actualCommandRead = rbmTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.F.name(),actualCommandRead);

    }

}
