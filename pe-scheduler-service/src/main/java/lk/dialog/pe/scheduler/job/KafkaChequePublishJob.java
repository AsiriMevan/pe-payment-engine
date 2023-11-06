package lk.dialog.pe.scheduler.job;

import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.scheduler.service.KafkaService;
import lk.dialog.pe.scheduler.util.JobKeys;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@DisallowConcurrentExecution
@Slf4j
public class KafkaChequePublishJob implements Job {

    @Autowired
    KafkaService kafkaService;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        Instant start = Instant.now();
        JobKeys handler = JobKeys.KAFKA_PUBLISH_CHEQUE;
        String jobDetail=context.getJobDetail().getKey().getName();
        String traceId = DCPEUtil.generateTraceId();
        log.info("[JOB][KAFKA]Execute start for {}: jobDetail={}|traceId={}|fireTime={}",handler,jobDetail,traceId, context.getFireTime());
        kafkaService.publishChequePaymentRecords(traceId);
        Long end = SchUtil.getTimeTaken(start);
        log.info("[JOB][KAFKA]Execute end for {}: jobDetail={}|traceId={}|nextFireTime={}|timeTaken={}",handler,context.getJobDetail().getKey().getName(), traceId,context.getNextFireTime(), end);
    }

}
