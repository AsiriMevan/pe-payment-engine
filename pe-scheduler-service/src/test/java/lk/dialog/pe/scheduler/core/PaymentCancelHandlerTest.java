package lk.dialog.pe.scheduler.core;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.scheduler.core.handler.PaymentCancelHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.service.SmsService;
import lk.dialog.pe.scheduler.service.SoapIntegrationService;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.CancelTestDataGenerator;
import org.junit.After;
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
public class PaymentCancelHandlerTest {
    @InjectMocks
    @Autowired
    @Qualifier("paymentCancelHandler")
    PaymentHandler paymentCancelHandler = new PaymentCancelHandler();

    @Mock
    @Autowired
    private SoapIntegrationService soapIntegrationService;

    @Mock
    @Autowired
    private SmsService smsService;

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate peJdbcTemplate;

    Payment paymentInput;
    Payment actualPayment;

    CancelTestDataGenerator cancelTestDataGenerator = new CancelTestDataGenerator();

    @After
    public void clearRecord(){
        if(paymentInput!=null)cancelTestDataGenerator.deleteCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
    }

    @Test
    public void loadPaymentsIntegrationTest(){
        paymentInput = cancelTestDataGenerator.getSuccessPayment();
        paymentInput.setTrxID(1);
        paymentInput.setReceiptNo("1");
        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        paymentInput.setTrxID(2);
        paymentInput.setReceiptNo("2");
        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        List<Payment> actualPayments =  paymentCancelHandler.loadPaymentData();
        Assert.assertEquals(2,actualPayments.size());
        assertThat(actualPayments.get(1)).usingRecursiveComparison().ignoringFields("receiptDate","accountPaymentDate").isEqualTo(paymentInput);
        paymentInput.setTrxID(1);
        paymentInput.setReceiptNo("1");
        assertThat(actualPayments.get(0)).usingRecursiveComparison().ignoringFields("receiptDate","accountPaymentDate").isEqualTo(paymentInput);
        cancelTestDataGenerator.deleteCancelPaymentFromDb(1,peJdbcTemplate);
        cancelTestDataGenerator.deleteCancelPaymentFromDb(2,peJdbcTemplate);
    }

    @Test
    public void failureWithPaymentNotFound() throws DCPEException {
        MockitoAnnotations.initMocks( this );
        paymentInput = cancelTestDataGenerator.getSuccessPayment();
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentInput.getReceiptNo(),paymentInput.getReceiptBranch())).thenReturn(null);
        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        paymentCancelHandler.execute();
        String commandRead = cancelTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        actualPayment = cancelTestDataGenerator.getCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.N.name(),commandRead);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertTrue(actualPayment.getErrDesc().contains("Payment-Not-Found"));
    }

    @Test
    public void failureWithCancelIncompleteAndModifyPayExtensionFail() throws DCPEException {
        paymentInput = cancelTestDataGenerator.getSuccessPayment();

        MockitoAnnotations.initMocks( this );
        MockitoAnnotations.initMocks( this );
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setPhysicalPaymentSeq(1);
        paymentDto.setAccountPaymentSeq(2);
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentInput.getReceiptNo(),paymentInput.getReceiptBranch())).thenReturn(paymentDto);
        Mockito.doNothing().when(soapIntegrationService).cancelPayment(Mockito.any(PaymentDTO.class));
        Mockito.when(soapIntegrationService.modifyPaymentExtension(Mockito.any(PaymentDTO.class))).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        paymentCancelHandler.execute();
        String commandRead = cancelTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        actualPayment = cancelTestDataGenerator.getCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.F.name(),commandRead);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertTrue(actualPayment.getErrDesc().contains("MODIF_PAY_EXT_FAIL"));
        Assert.assertTrue(actualPayment.getErrDesc().contains("404 NOT_FOUND"));

    }

    @Test
    public void failureWithUnknownException() throws DCPEException {
        MockitoAnnotations.initMocks( this );
        paymentInput = cancelTestDataGenerator.getSuccessPayment();
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentInput.getReceiptNo(),paymentInput.getReceiptBranch())).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));
        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        paymentCancelHandler.execute();
        String commandRead = cancelTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        actualPayment = cancelTestDataGenerator.getCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.F.name(),commandRead);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertTrue(actualPayment.getErrDesc().contains("404 NOT_FOUND"));
    }

    @Test
    public void failureWithInvalidPaymentSystem() throws DCPEException {
        paymentInput = cancelTestDataGenerator.getSuccessPayment();
        paymentInput.setProductCategory(ProductCategoryEnum.PE.getCategory());

        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        paymentCancelHandler.execute();
        String commandRead = cancelTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        actualPayment = cancelTestDataGenerator.getCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.F.name(),commandRead);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertTrue(actualPayment.getErrDesc().contains("product category =4 not support for payment cancellation."));
        Assert.assertEquals(paymentInput.getPhysicalPaymentSeq(),actualPayment.getPhysicalPaymentSeq());
    }

    @Test
    public void failureWithFailedModifyPaymentExtensionResponse() throws DCPEException {
        paymentInput = cancelTestDataGenerator.getSuccessPayment();

        MockitoAnnotations.initMocks( this );
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setPhysicalPaymentSeq(1);
        paymentDto.setAccountPaymentSeq(2);
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentInput.getReceiptNo(),paymentInput.getReceiptBranch())).thenReturn(paymentDto);
        Mockito.doNothing().when(soapIntegrationService).cancelPayment(Mockito.any(PaymentDTO.class));
        Mockito.when(soapIntegrationService.modifyPaymentExtension(Mockito.any(PaymentDTO.class))).thenReturn(2);

        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        paymentCancelHandler.execute();
        String commandRead = cancelTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        actualPayment = cancelTestDataGenerator.getCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.F.name(),commandRead);
        Assert.assertEquals("2",actualPayment.getErrCode());
        Assert.assertTrue(actualPayment.getErrDesc().contains("Invalid response status from modifyPaymentExtension SOAP API"));
        Assert.assertEquals( paymentDto.getPhysicalPaymentSeq(),actualPayment.getPhysicalPaymentSeq());
    }

    @Test
    public void successWithCancelCompleteFalse() throws DCPEException {
        paymentInput = cancelTestDataGenerator.getSuccessPayment();

        MockitoAnnotations.initMocks( this );
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setPhysicalPaymentSeq(1);
        paymentDto.setAccountPaymentSeq(2);
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentInput.getReceiptNo(),paymentInput.getReceiptBranch())).thenReturn(paymentDto);
        Mockito.doNothing().when(soapIntegrationService).cancelPayment(Mockito.any(PaymentDTO.class));
        Mockito.when(soapIntegrationService.modifyPaymentExtension(Mockito.any(PaymentDTO.class))).thenReturn(1);
//        Mockito.doNothing().when(smsService).insertOtcPayment(paymentInput);

        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        paymentCancelHandler.execute();
        String commandRead = cancelTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        actualPayment = cancelTestDataGenerator.getCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),commandRead);
        Assert.assertEquals("1",actualPayment.getErrCode());
        Assert.assertTrue(actualPayment.getErrDesc().contains("SUCCESS"));
        Assert.assertEquals(paymentDto.getPhysicalPaymentSeq(),actualPayment.getPhysicalPaymentSeq());
    }


    @Test
    public void successWithCancelCompleteTrue() throws DCPEException {
        paymentInput = cancelTestDataGenerator.getSuccessPayment();
        paymentInput.setErrCode(String.valueOf(Constants.TRANSFER_FAIL));
        paymentInput.setErrDesc("MODIF_PAY_EXT_FAIL");

        MockitoAnnotations.initMocks( this );
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setPhysicalPaymentSeq(1);
        paymentDto.setAccountPaymentSeq(2);
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentInput.getReceiptNo(),paymentInput.getReceiptBranch())).thenReturn(paymentDto);
        Mockito.when(soapIntegrationService.modifyPaymentExtension(Mockito.any(PaymentDTO.class))).thenReturn(1);

        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        paymentCancelHandler.execute();
        String commandRead = cancelTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        actualPayment = cancelTestDataGenerator.getCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),commandRead);
        Assert.assertEquals("1",actualPayment.getErrCode());
        Assert.assertTrue(actualPayment.getErrDesc().contains("SUCCESS"));
        Assert.assertEquals(paymentDto.getPhysicalPaymentSeq(),actualPayment.getPhysicalPaymentSeq());
    }

    @Test
    public void successWithCheckReturnY() throws DCPEException {
        paymentInput = cancelTestDataGenerator.getSuccessPayment();

        Payment chequeReturnExpectedLog = new Payment();
        chequeReturnExpectedLog.setContractNo(paymentInput.getContractNo());
        chequeReturnExpectedLog.setChequeNo("1");
        chequeReturnExpectedLog.setChequebankCode("1");
        chequeReturnExpectedLog.setChequebankBranchCode("1");
        chequeReturnExpectedLog.setReceiptNo("1");

        paymentInput.setChequeNo(chequeReturnExpectedLog.getChequeNo());
        paymentInput.setChequebankCode(chequeReturnExpectedLog.getChequebankCode());
        paymentInput.setChequebankBranchCode(chequeReturnExpectedLog.getChequebankBranchCode());
        paymentInput.setReceiptNo(chequeReturnExpectedLog.getReceiptNo());
        paymentInput.setChequeReturn("Y");
        MockitoAnnotations.initMocks( this );
        PaymentDTO paymentDto = new PaymentDTO();
        paymentDto.setPhysicalPaymentSeq(1);
        paymentDto.setAccountPaymentSeq(2);
        Mockito.when(soapIntegrationService.queryPaymentsSummery(paymentInput.getReceiptNo(),paymentInput.getReceiptBranch())).thenReturn(paymentDto);
        Mockito.doNothing().when(soapIntegrationService).cancelPayment(Mockito.any(PaymentDTO.class));
        Mockito.when(soapIntegrationService.modifyPaymentExtension(Mockito.any(PaymentDTO.class))).thenReturn(1);

        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput,null,peJdbcTemplate);
        cancelTestDataGenerator.deleteChequeReturnsFromDb(paymentInput,peJdbcTemplate);

        paymentCancelHandler.execute();

        String commandRead = cancelTestDataGenerator.getCommandReadByTrxIdFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        actualPayment = cancelTestDataGenerator.getCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
        Assert.assertEquals(COMMAND_READ.S.name(),commandRead);
        Assert.assertEquals("1",actualPayment.getErrCode());
        Assert.assertTrue(actualPayment.getErrDesc().contains("SUCCESS"));
        Assert.assertEquals(paymentDto.getPhysicalPaymentSeq(),actualPayment.getPhysicalPaymentSeq());

        Payment chequeReturnActualLogOne = cancelTestDataGenerator.getChequeReturnFromDb(paymentInput,peJdbcTemplate).get(0);
        assertThat(chequeReturnActualLogOne).usingRecursiveComparison().isEqualTo(chequeReturnExpectedLog);

        //retesting for isChequeReturnAlreadyExist scenario
        paymentCancelHandler.execute();

        int currentLogSize = cancelTestDataGenerator.getChequeReturnFromDb(paymentInput,peJdbcTemplate).size();
        assertThat(currentLogSize).isEqualTo(1);

        Assert.assertEquals(COMMAND_READ.S.name(),commandRead);
        Assert.assertEquals("1",actualPayment.getErrCode());
        Assert.assertTrue(actualPayment.getErrDesc().contains("SUCCESS"));
        Assert.assertEquals(paymentDto.getPhysicalPaymentSeq(),actualPayment.getPhysicalPaymentSeq());

        cancelTestDataGenerator.deleteChequeReturnsFromDb(paymentInput,peJdbcTemplate);

    }

}
