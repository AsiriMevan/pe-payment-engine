package lk.dialog.pe.scheduler.job;

import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.scheduler.core.PaymentHandlerExecutorService;
import lk.dialog.pe.scheduler.util.HANDLER;
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
public class RbmRetryJob implements Job {

    @Autowired
    PaymentHandlerExecutorService jobExecutorService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Instant start = Instant.now();
        HANDLER handler = HANDLER.RBM_PAY_RETRY;
        String traceId = DCPEUtil.generateTraceId();
        log.info("[JOB]Execute start for {} : jobDetail={}|traceId={}|fireTime={}",handler,context.getJobDetail().getKey().getName(), traceId,context.getFireTime());
        jobExecutorService.submitRbmRetryHandler(traceId);
        Long end = SchUtil.getTimeTaken(start);
        log.info("[JOB]Execute end for {}: jobDetail={}|traceId={}|nextFireTime={}|timeTaken={}",context.getJobDetail().getKey().getName(),traceId, context.getNextFireTime(), end);
    }

}
