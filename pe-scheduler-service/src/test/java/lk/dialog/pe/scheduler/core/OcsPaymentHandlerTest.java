package lk.dialog.pe.scheduler.core;

import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.scheduler.core.handler.OCSPaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.OCSTransaction;
import lk.dialog.pe.scheduler.service.MifeIntegrationService;
import lk.dialog.pe.scheduler.service.SoapIntegrationService;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.OcsTestDataGenerator;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OcsPaymentHandlerTest {

    @InjectMocks
    @Autowired
    @Qualifier("ocsPaymentHandler")
    PaymentHandler ocsPaymentHandler = new OCSPaymentHandler();

    @Mock
    @Autowired
    private SoapIntegrationService soapIntegrationService;

    @Mock
    @Autowired
    MifeIntegrationService mifeIntegrationService;

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate peJdbcTemplate;

    Payment paymentInput;
    Payment actualPayment;

    private static final String ocsResponseString = "|1|2|3|{status}|5";

    OcsTestDataGenerator ocsTestDataGenerator = new OcsTestDataGenerator();

    @Test
    public void loadPaymentsIntegrationTest(){
        paymentInput = ocsTestDataGenerator.getSuccessOcsPayment();
        paymentInput.setTrxID(1);
        paymentInput.setReceiptNo("1");
        ocsTestDataGenerator.saveOCSPaymentToDb(paymentInput,null,peJdbcTemplate);
        paymentInput.setTrxID(2);
        paymentInput.setReceiptNo("2");
        ocsTestDataGenerator.saveOCSPaymentToDb(paymentInput,null,peJdbcTemplate);
        List<Payment> actualPayments =  ocsPaymentHandler.loadPaymentData();
        Assert.assertEquals(2,actualPayments.size());
        assertThat(actualPayments.get(1)).usingRecursiveComparison().ignoringFields("receiptDate").isEqualTo(paymentInput);
        paymentInput.setTrxID(1);
        paymentInput.setReceiptNo("1");
        assertThat(actualPayments.get(0)).usingRecursiveComparison().ignoringFields("receiptDate").isEqualTo(paymentInput);
        ocsTestDataGenerator.deleteOCSPaymentFromDb(1,peJdbcTemplate);
        ocsTestDataGenerator.deleteOCSPaymentFromDb(2,peJdbcTemplate);
    }

    @Test
    public void failureWithZeroPayment(){
        paymentInput = ocsTestDataGenerator.getSuccessOcsPayment();
        paymentInput.setAccountPaymentMny(0);
        ocsTestDataGenerator.saveOCSPaymentToDb(paymentInput,null,peJdbcTemplate);
        ocsPaymentHandler.execute();
        actualPayment = ocsTestDataGenerator.getOcsPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertEquals("Payment Amount is <= 0",actualPayment.getErrDesc());
        ocsTestDataGenerator.deleteOCSPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
    }
    @Test
    public void failureWithoutGsmOrDbnSbu(){
        paymentInput = ocsTestDataGenerator.getSuccessOcsPayment();
        paymentInput.setSbu(SBUEnum.VOLTE.valueOf());
        ocsTestDataGenerator.saveOCSPaymentToDb(paymentInput,null,peJdbcTemplate);
        ocsPaymentHandler.execute();
        actualPayment = ocsTestDataGenerator.getOcsPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertEquals("SBU(VOLTE) not support",actualPayment.getErrDesc());
        ocsTestDataGenerator.deleteOCSPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
    }

    @Test
    public void failureWithOcsTrxStatusOne(){
        executeSwitchCaseCheckData(1);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertEquals("OCS -> Invalid external system ID",actualPayment.getErrDesc());

    }

    @Test
    public void failureWithOcsTrxStatusTwo(){
        executeSwitchCaseCheckData(2);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertEquals("OCS -> Invalid MSISDN",actualPayment.getErrDesc());

    }

    @Test
    public void failureWithOcsTrxStatusFive(){
        executeSwitchCaseCheckData(5);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertEquals("OCS -> Invalid reason code",actualPayment.getErrDesc());

    }
    @Test
    public void failureWithOcsTrxStatusSeven(){
        executeSwitchCaseCheckData(7);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertEquals("OCS -> Adjustment passes the minimum threshold of the account(insufficient balance)",actualPayment.getErrDesc());

    }
    @Test
    public void failureWithOcsTrxFailure(){
        executeSwitchCaseCheckData(9);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertEquals("OCS -> System error",actualPayment.getErrDesc());

    }
    @Test
    public void failureWithUnknownException(){
        MockitoAnnotations.initMocks( this );
        Mockito.when(mifeIntegrationService.submitOcsTransactionRequest(Mockito.any(OCSTransaction.class),Mockito.anyString())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        paymentInput = ocsTestDataGenerator.getSuccessOcsPayment();
        ocsTestDataGenerator.saveOCSPaymentToDb(paymentInput,null,peJdbcTemplate);
        ocsPaymentHandler.execute();
        actualPayment = ocsTestDataGenerator.getOcsPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        String commandRead = ocsTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        ocsTestDataGenerator.deleteOCSPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertEquals(COMMAND_READ.F.name(),commandRead);
        Assert.assertTrue(actualPayment.getErrDesc().contains("404 NOT_FOUND"));

    }

    @Test
    public void successWithVolteSbu(){
        MockitoAnnotations.initMocks( this );
        Mockito.when(mifeIntegrationService.submitOcsTransactionRequest(Mockito.any(OCSTransaction.class),Mockito.anyString())).thenReturn(generateOcsResponse(0));
        paymentInput = ocsTestDataGenerator.getSuccessOcsPayment();
        ocsTestDataGenerator.saveOCSPaymentToDb(paymentInput,null,peJdbcTemplate);
        ocsPaymentHandler.execute();
        actualPayment = ocsTestDataGenerator.getOcsPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertNull(actualPayment);
    }



    private void executeSwitchCaseCheckData(Integer ocsTrxStatus ){
        MockitoAnnotations.initMocks( this );
        Mockito.when(mifeIntegrationService.submitOcsTransactionRequest(Mockito.any(OCSTransaction.class),Mockito.anyString())).thenReturn(generateOcsResponse(ocsTrxStatus));
        paymentInput = ocsTestDataGenerator.getSuccessOcsPayment();
        ocsTestDataGenerator.saveOCSPaymentToDb(paymentInput,null,peJdbcTemplate);
        ocsPaymentHandler.execute();
        actualPayment = ocsTestDataGenerator.getOcsPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        ocsTestDataGenerator.deleteOCSPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
    }

    private String generateOcsResponse(Integer ocsTrxStatus){
        return ocsResponseString.replace("{status}",String.valueOf(ocsTrxStatus));
    }

}
