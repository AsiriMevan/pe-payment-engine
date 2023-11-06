package lk.dialog.pe.scheduler.service.impl;

import lk.dialog.pe.scheduler.config.prop.QueryConfig;
import lk.dialog.pe.scheduler.dto.PaymentJSON;
import lk.dialog.pe.scheduler.repository.mapper.KafkaMessageMapper;
import lk.dialog.pe.scheduler.service.CcbsDbIntegrationService;
import lk.dialog.pe.scheduler.service.KafkaService;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.KafkaReadStatus;
import lk.dialog.pe.scheduler.util.QUERY;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KafkaServiceImpl implements KafkaService {

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate peJdbcTemplate;

    @Autowired
    CcbsDbIntegrationService ccbsDbIntegrationService;

    @Autowired
    private KafkaTemplate<Integer, String> kafkaTemplate;

    @Value("${pe.kafka.push.topic-payment}")
    String paymentTopic;

    @Value("${pe.kafka.push.topic-cancel}")
    String cancelTopic;

    @Value("${pe.kafka.db-process-batch-size}")
    String dbProcessBatchSize;

    private KafkaMessageMapper kafkaMessageMapperGsmDtv = new KafkaMessageMapper(HANDLER.RBM_PAY);
    private KafkaMessageMapper kafkaMessageMapperCheque = new KafkaMessageMapper(HANDLER.CHEQUE_FORCEFUL_PAY);
    private KafkaMessageMapper kafkaMessageMapperCancel = new KafkaMessageMapper(HANDLER.CANCEL_PAY);
    @Override
    public void publishGsmDtvRecords(String traceId){
        publishPayments(HANDLER.RBM_PAY,traceId);
    }
    @Override
    public void publishDbnRecords(String traceId){
        publishPayments(HANDLER.DBN_PAY,traceId);
    }
    @Override
    public void publishChequePaymentRecords(String traceId){
        publishPayments(HANDLER.CHEQUE_FORCEFUL_PAY,traceId);
    }
    @Override
    public void publishCancellations(String traceId){
        publishPayments(HANDLER.CANCEL_PAY,traceId);
    }

    private void publishPayments(HANDLER handler,String traceId){
       // String traceId = SchUtil.generateTraceId();
        MDC.put("traceId",traceId);
        Instant start = Instant.now();
        log.info("[KAFKA][{}]publishPayments started execution|traceId={}",handler,traceId);
        List<PaymentJSON> payments = loadPayments(handler,traceId);

        payments.stream().forEach(payment->{
            MDC.put("traceId",traceId+"-"+payment.getReqId());
            String requstString = SchUtil.convertToString(payment);
            log.info("[KAFKA][{}]publishPayments started publishing record req_id={}|record={}|traceId={}",handler,payment.getReqId(),requstString,traceId);
            updateReadStatus(handler, KafkaReadStatus.PROCESSING,payment.getReqId(),traceId);
            payment.setNomineeNumber(getNomineeNumber(handler,payment.getProductCategory(),payment.getAccountNumber(),traceId));
            payment.setTxType(handler == HANDLER.CANCEL_PAY?"Reversal":"Payment");
            if(handler == HANDLER.CHEQUE_FORCEFUL_PAY)
                payment.setPaymentType(SchUtil.convertPaymentMode("CHEQ"));
            String reqId = payment.getReqId();
            String topic = handler==HANDLER.CANCEL_PAY?cancelTopic:paymentTopic;
            String paymentString = SchUtil.convertToString(payment);
            try{
                pushMessage(handler,topic,paymentString,reqId,traceId);
                updateReadStatus(handler, KafkaReadStatus.YES,reqId,traceId);
                log.info("[KAFKA][{}]publishPayments submit to async kafka client success reqId={}|traceId={}",handler,reqId,traceId);
            }catch (Exception ex){
                log.info("[KAFKA][{}]publishPayments Failed reqId={}|record={}|errorMsg={}|error={}|traceId={}",handler,reqId, paymentString,ex.getMessage(),ex,traceId);
                updateReadStatus(handler, KafkaReadStatus.FAIL,reqId,traceId);

            }
        });
        log.info("[KAFKA][{}]publishPayments finished execution for recordCount={}|timeTaken={}|traceId={}",handler,payments.size(),SchUtil.getTimeTaken(start),traceId);
        MDC.clear();
    }

    private List<PaymentJSON> loadPayments(HANDLER handler,String traceId){

        String loadQuery = QueryConfig.getQuery(QUERY.SQL_SEL_KAFKA_PAYMENT).replace("{table}",getTable(handler)).replace("{batchSize}",dbProcessBatchSize);
        log.info("[KAFKA][{}]publishPayments:loadPaymentsRequest query={}|traceId={}",handler,loadQuery,traceId);
        List<PaymentJSON> payments = new ArrayList<>();
        if(handler == HANDLER.CHEQUE_FORCEFUL_PAY){
            payments =peJdbcTemplate.query(loadQuery, kafkaMessageMapperCheque);
        }
        else if(handler == HANDLER.CANCEL_PAY){
            payments =peJdbcTemplate.query(loadQuery,kafkaMessageMapperCancel);
        }
        else{
            payments =peJdbcTemplate.query(loadQuery,kafkaMessageMapperGsmDtv);
        }
        log.info("[KAFKA][{}]publishPayments:loadPaymentsResponse unpublishedPaymentsLoaded={}|traceId={}",handler,payments.size(),traceId);
        return payments;
    }

    private void updateReadStatus(HANDLER handler, KafkaReadStatus kafkaReadStatus, String reqId,String traceId){
        String updateQuery= QueryConfig.getQuery(QUERY.SQL_UPDATE_KAFKA_PAYMENT).replace("{table}",getTable(handler));
        log.info("[KAFKA][{}]publishPayments:updateReadStatusRequest readStatus={}|reqId={}|query={}|traceId={}",handler, kafkaReadStatus,reqId,updateQuery,traceId);
        int updateStatus = peJdbcTemplate.update(updateQuery,kafkaReadStatus.getValue(),Long.valueOf(reqId));
        log.info("[KAFKA][{}]publishPayments:updateReadStatusResponse updateStatus={}|traceId={}",handler,updateStatus,traceId);
    }

    private String getTable(HANDLER handler){
        switch(handler){
            case RBM_PAY:return "RBM_PAYMENT";
            case CHEQUE_FORCEFUL_PAY:return "FORCEFUL_REALIZE_CHEQUES";
            case CANCEL_PAY:return "CANCEL_PAYMENT";
            case DBN_PAY:return "DBN_PAYMENT";
            default:return null;
        }
    }

    private String getNomineeNumber(HANDLER handler,String productCategory ,String contractId,String traceId){
        log.info("[KAFKA][{}]publishPayments:getNomineeNumberRequest productCategory={}|contractId={}|traceId={}",handler,productCategory,contractId,traceId);
        if(contractId==null|| contractId.isEmpty())
            return null;

        String nomineeNumber = null;
        if(productCategory.equalsIgnoreCase("CCBS")){
            nomineeNumber = ccbsDbIntegrationService.queryForStarNomineeNumberCcbs(Long.valueOf(contractId));
        }
        else if(productCategory.equalsIgnoreCase("TELBIZ")){
            nomineeNumber = ccbsDbIntegrationService.queryForStarNomineeNumberTelbiz(contractId);
        }
        log.info("[KAFKA][{}]publishPayments:getNomineeNumberResponse success productCategory={}|contractId={}|nomineeNumber={}|traceId={}",handler,productCategory,contractId,nomineeNumber,traceId);
        return nomineeNumber;
    }

    private void pushMessage(HANDLER handler,final String topicName, String paymentJsonString,String reqId ,String traceId) {
        log.info("[KAFKA][{}]publishPayments:pushMessageRequest topicName={}|reqId={}|record={}|traceId={}",handler,topicName,reqId,paymentJsonString,traceId);
        final ProducerRecord<Integer, String> record = new ProducerRecord<>(topicName, paymentJsonString);
        ListenableFuture<SendResult<Integer, String>> future = kafkaTemplate.send(record);
        future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                log.info("[KAFKA][{}]publishPayments:pushMessageResponse Success from callback reqId={}|topic={}|partition={}|record={}|traceId={}",handler,reqId, result.getProducerRecord().topic(), result.getProducerRecord().partition(), result.getProducerRecord().value(),traceId);
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("[KAFKA][{}]publishPayments:pushMessageResponse send failure from callback reqId={}|topic={}|record={}|errorMsg={}|error={}|traceId={}",handler,reqId,topicName,paymentJsonString,ex.getMessage(),ex,traceId);
                updateReadStatus(handler, KafkaReadStatus.FAIL,reqId,traceId);
            }
        });
        log.info("[KAFKA][{}]publishPayments:pushMessage submit to async kafka client success topicName={}|reqId={}|traceId={}",handler,topicName,reqId,traceId);
    }
}
