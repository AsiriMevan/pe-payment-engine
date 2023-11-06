package lk.dialog.pe.scheduler.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentJSON;
import lk.dialog.pe.scheduler.service.impl.KafkaServiceImpl;
import lk.dialog.pe.scheduler.util.*;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//to execute mock methods last
public class KafkaServiceTest {


    @Autowired
    @InjectMocks
    KafkaService kafkaService = new KafkaServiceImpl();

    @Autowired
    @Mock
    protected KafkaTemplate<Integer, String> kafkaTemplate;

    @Captor
    ArgumentCaptor<ProducerRecord<Integer, String>> producerRecordCaptor;

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    @Qualifier("peJdbcTemplate")
    protected JdbcTemplate peJdbcTemplate;

    @Value("${pe.kafka.push.topic-payment}")
    protected String paymentTopic;

    @Value("${pe.kafka.push.topic-cancel}")
    protected String cancelTopic;

    RbmTestDataGenerator rbmTestDataGenerator = new RbmTestDataGenerator();
    ChequeTestDataGenerator chequeTestDataGenerator = new ChequeTestDataGenerator();
    CancelTestDataGenerator cancelTestDataGenerator = new CancelTestDataGenerator();

    protected Payment paymentInput;

    protected static String RBM_TABLE="RBM_PAYMENT";
    protected static String CANCEL_TABLE="CANCEL_PAYMENT";
    protected static String CHEQUE_TABLE="FORCEFUL_REALIZE_CHEQUES";

    @Test
    public void gsmDtvPublishIntegrationSuccess(){
        paymentInput = rbmTestDataGenerator.getRbmObjectForApplicationException();
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput, COMMAND_READ.S,peJdbcTemplate);
        setUpdateTimeToToday(paymentInput.getTrxID(),RBM_TABLE);
        kafkaService.publishGsmDtvRecords();
        String updateStatus = getReadStatus(paymentInput.getTrxID(),RBM_TABLE);
        Assert.assertEquals(KafkaReadStatus.YES.getValue(),updateStatus );
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
    }
    @Test
    public void chequeRealizePublishIntegrationSuccess(){
        paymentInput = chequeTestDataGenerator.getSuccessPayment();
        chequeTestDataGenerator.savePaymentToDb(paymentInput, COMMAND_READ.S,true,peJdbcTemplate);
        setUpdateTimeToToday(paymentInput.getTrxID(),CHEQUE_TABLE);
        kafkaService.publishChequePaymentRecords();
        String updateStatus = getReadStatus(paymentInput.getTrxID(),CHEQUE_TABLE);
        Assert.assertEquals(KafkaReadStatus.YES.getValue(),updateStatus );
        chequeTestDataGenerator.deletePaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);

    }
    @Test
    public void cancelPaymentPublishIntegrationSuccess(){
        paymentInput = cancelTestDataGenerator.getSuccessPayment();
        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput, COMMAND_READ.S,peJdbcTemplate);
        setUpdateTimeToToday(paymentInput.getTrxID(),CANCEL_TABLE);
        kafkaService.publishCancellations();
        String updateStatus = getReadStatus(paymentInput.getTrxID(),CANCEL_TABLE);
        Assert.assertEquals(KafkaReadStatus.YES.getValue(),updateStatus );
        cancelTestDataGenerator.deleteCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);

    }

    @Test
    public void validateChequePublishKafkaMessage() throws JsonProcessingException {
        MockitoAnnotations.initMocks( this );
        paymentInput = chequeTestDataGenerator.getSuccessPayment();
        chequeTestDataGenerator.savePaymentToDb(paymentInput, COMMAND_READ.S,true,peJdbcTemplate);
        setUpdateTimeToToday(paymentInput.getTrxID(),CHEQUE_TABLE);
        kafkaService.publishChequePaymentRecords();
        executeAndCompareRecords(chequePublishExpectedMessage);
        chequeTestDataGenerator.deletePaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);

    }
    @Test
    public void validateCancelPayPublishKafkaMessage() throws JsonProcessingException {
        MockitoAnnotations.initMocks( this );
        paymentInput = cancelTestDataGenerator.getSuccessPayment();
        cancelTestDataGenerator.saveCancelPaymentToDb(paymentInput, COMMAND_READ.S,peJdbcTemplate);
        setUpdateTimeToToday(paymentInput.getTrxID(),CANCEL_TABLE);
        kafkaService.publishCancellations();
        executeAndCompareRecords(cancelPaymentPublishExpectedMessage);
        cancelTestDataGenerator.deleteCancelPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);
    }

    @Test
    public void validateGsmDtvPublishKafkaMessage() throws JsonProcessingException {
        MockitoAnnotations.initMocks( this );
        paymentInput = rbmTestDataGenerator.getRbmObjectForApplicationException();
        rbmTestDataGenerator.saveRBMPaymentToDb(paymentInput, COMMAND_READ.S,peJdbcTemplate);
        setUpdateTimeToToday(paymentInput.getTrxID(),RBM_TABLE);
        kafkaService.publishGsmDtvRecords();
        executeAndCompareRecords(gsmDtvPublishExpectedRecord);
        rbmTestDataGenerator.deleteRBMPaymentFromDb(paymentInput.getTrxID(),peJdbcTemplate);

    }

    private void executeAndCompareRecords(String expectedRecord) throws JsonProcessingException {
        Mockito.verify(kafkaTemplate).send(producerRecordCaptor.capture());
        PaymentJSON actualMessage = objectMapper.readValue(producerRecordCaptor.getValue().value(),PaymentJSON.class);
        PaymentJSON expectedResponse = objectMapper.readValue(expectedRecord,PaymentJSON.class);
        assertThat(actualMessage).usingRecursiveComparison().ignoringFields("physicalPaymentDate","receiptDate","receiptDate","txDate").isEqualTo(expectedResponse);
    }

    private static String gsmDtvPublishExpectedRecord = "{\n" +
            "   \"receiptNumber\":\"UPG1632289782695\",\n" +
            "   \"accountNumber\":\"39171286\",\n" +
            "   \"accountType\":\"GSM\",\n" +
            "   \"amount\":1.0,\n" +
            "   \"nomineeNumber\":\"761053514\",\n" +
            "   \"paymentType\":\"Cash Only\",\n" +
            "   \"txType\":\"Payment\",\n" +
            "   \"txDate\":\"2021-10-10 11:33:52\",\n" +
            "   \"receiptBranch\":\"UPG\",\n" +
            "   \"branchCounter\":\"UPG\",\n" +
            "   \"paymentRef\":\"UPG_EZCASH_20210922673216\",\n" +
            "   \"connRef\":\"761053514\",\n" +
            "   \"physicalPaymentText\":\"PAYMENT - CASH UPG_EZCASH_20210922673216\",\n" +
            "   \"receiptUser\":\"CG_USER\",\n" +
            "   \"paymentText\":\"PAYMENT - CASH UPG_EZCASH_20210922673216\",\n" +
            "   \"moduleName\":\"CG\",\n" +
            "   \"commandRead\":\"S\",\n" +
            "   \"errorCode\":\"1\",\n" +
            "   \"errorDesc\":null,\n" +
            "   \"productType\":0,\n" +
            "   \"connectionStatus\":\"C\",\n" +
            "   \"connectionType\":\"POSTPAID\",\n" +
            "   \"receiptDate\":\"2021-10-10 11:33:52\",\n" +
            "   \"physicalPaymentDate\":\"2021-10-10 11:33:52\",\n" +
            "   \"statusReason\":null,\n" +
            "   \"createdUser\":\"postgres\"\n" +
            "}";

    private static String chequePublishExpectedMessage = "{\n" +
            "   \"receiptNumber\":\"FBBPARP00032137\",\n" +
            "   \"accountNumber\":\"72188093\",\n" +
            "   \"accountType\":\"DBN\",\n" +
            "   \"amount\":10.0,\n" +
            "   \"nomineeNumber\":null,\n" +
            "   \"paymentType\":\"Cheque Only\",\n" +
            "   \"txType\":\"Payment\",\n" +
            "   \"txDate\":\"2021-10-10 14:35:22\",\n" +
            "   \"receiptBranch\":\"710\",\n" +
            "   \"branchCounter\":\"710\",\n" +
            "   \"paymentRef\":null,\n" +
            "   \"connRef\":\"0814505394\",\n" +
            "   \"physicalPaymentText\":null,\n" +
            "   \"receiptUser\":\"DTQA00001\",\n" +
            "   \"paymentText\":null,\n" +
            "   \"moduleName\":null,\n" +
            "   \"commandRead\":\"S\",\n" +
            "   \"errorCode\":\"1\",\n" +
            "   \"errorDesc\":\"SUCCESS\",\n" +
            "   \"productType\":5,\n" +
            "   \"connectionStatus\":null,\n" +
            "   \"connectionType\":\"0\",\n" +
            "   \"receiptDate\":\"2021-10-10 14:35:22\",\n" +
            "   \"physicalPaymentDate\":\"2021-10-10 14:35:22\",\n" +
            "   \"statusReason\":null,\n" +
            "   \"createdUser\":\"postgres\"\n" +
            "}";
    private static String cancelPaymentPublishExpectedMessage="{\n" +
            "   \"receiptNumber\":\"FBDPARP00029593\",\n" +
            "   \"accountNumber\":\"97010415\",\n" +
            "   \"accountType\":\"GSM\",\n" +
            "   \"amount\":10.0,\n" +
            "   \"nomineeNumber\":null,\n" +
            "   \"paymentType\":null,\n" +
            "   \"txType\":\"Reversal\",\n" +
            "   \"txDate\":\"2021-10-10 14:44:41\",\n" +
            "   \"receiptBranch\":\"710\",\n" +
            "   \"branchCounter\":\"710\",\n" +
            "   \"paymentRef\":null,\n" +
            "   \"connRef\":null,\n" +
            "   \"physicalPaymentText\":null,\n" +
            "   \"receiptUser\":\"002\",\n" +
            "   \"paymentText\":null,\n" +
            "   \"moduleName\":null,\n" +
            "   \"commandRead\":\"S\",\n" +
            "   \"errorCode\":\"1\",\n" +
            "   \"errorDesc\":\"SUCCESS\",\n" +
            "   \"productType\":0,\n" +
            "   \"connectionStatus\":null,\n" +
            "   \"connectionType\":\"CONNECTION_TYPE\",\n" +
            "   \"receiptDate\":\"2021-10-10 14:44:41\",\n" +
            "   \"physicalPaymentDate\":\"2021-10-10 14:44:41\",\n" +
            "   \"statusReason\":null,\n" +
            "   \"createdUser\":\"postgres\"\n" +
            "}";

    protected void setUpdateTimeToToday(Long trxId,String table){
            peJdbcTemplate.update("update "+table+" set update_date=current_timestamp  where req_id="+trxId);
    }

    protected String getReadStatus(Long trxId, String table){
       return peJdbcTemplate.queryForObject("select paypub_read from "+table+" where req_id="+trxId,String.class);
    }
}
