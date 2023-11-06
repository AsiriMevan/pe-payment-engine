package lk.dialog.pe.scheduler.core.impl;

import lk.dialog.pe.scheduler.core.PaymentHandler;
import lk.dialog.pe.scheduler.core.PaymentHandlerExecutorService;
import lk.dialog.pe.scheduler.dto.JobDetailDto;
import lk.dialog.pe.scheduler.service.JobConfigService;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.JobKeys;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@ConfigurationProperties(prefix = "pe.thread-pool")
@Slf4j @Setter
public class PaymentHandlerExecutorServiceImpl implements PaymentHandlerExecutorService {
    @Autowired
    @Qualifier("rbmPaymentHandler")
    private PaymentHandler rbmPaymentHandler;

    @Autowired
    private ThreadPoolTaskExecutor rbmThreadExecutor;

    @Autowired
    @Qualifier("rbmPaymentFailHandler")
    private PaymentHandler rbmPaymentFailHandler;

    @Autowired
    private ThreadPoolTaskExecutor rbmRetryThreadExecutor;

    @Autowired
    @Qualifier("ocsPaymentHandler")
    private PaymentHandler ocsPaymentHandler;

    @Autowired
    private ThreadPoolTaskExecutor ocsThreadExecutor;

    @Autowired
    @Qualifier("paymentCancelHandler")
    private PaymentHandler paymentCancelHandler;

    @Autowired
    private ThreadPoolTaskExecutor cancelThreadExecutor;

    @Autowired
    @Qualifier("chequeForcefulRealizeHandler")
    private PaymentHandler chequeForcefulRealizeHandler;

    @Autowired
    private ThreadPoolTaskExecutor chequeThreadExecutor;

    @Autowired
    @Qualifier("chequeForcefulRealizeFailHandler")
    private PaymentHandler chequeForcefulRealizeFailHandler;

    @Autowired
    private ThreadPoolTaskExecutor chequeRetryThreadExecutor;

    @Autowired
    private JobConfigService jobConfigService ;


    private int rbmCoreSize;
    private int rbmRetryCoreSize;
    private int ocsCoreSize;
    private int cancelCoreSize;
    private int chequeRealizeCoreSize;
    private int chequeRealizeRetryCoreSize;


    @Override
    public void submitRbmHandler(String traceId){
        submitRunnable(HANDLER.RBM_PAY,rbmPaymentHandler,rbmThreadExecutor,rbmCoreSize,traceId);
    }

    @Override
    public void submitRbmRetryHandler(String traceId){
        submitRunnable(HANDLER.RBM_PAY_RETRY,rbmPaymentFailHandler,rbmRetryThreadExecutor,rbmRetryCoreSize,traceId);
    }
    @Override
    public void submitOcsHandler(String traceId){
        submitRunnable(HANDLER.OCS_PAY,ocsPaymentHandler,ocsThreadExecutor,ocsCoreSize,traceId);
    }
    @Override
    public void submitCancelHandler(String traceId){
        submitRunnable(HANDLER.CANCEL_PAY,paymentCancelHandler,cancelThreadExecutor,cancelCoreSize,traceId);
    }
    @Override
    public void submitForceFulCheckRealizeHandler(String traceId){
        submitRunnable(HANDLER.CHEQUE_FORCEFUL_PAY,chequeForcefulRealizeHandler,chequeThreadExecutor,chequeRealizeCoreSize,traceId);
    }
    @Override
    public void submitForceFulCheckRealizeFailHandler(String traceId){
        submitRunnable(HANDLER.CHEQUE_FORCEFUL_PAY_RETRY,chequeForcefulRealizeFailHandler,chequeRetryThreadExecutor,chequeRealizeRetryCoreSize,traceId);
    }

    private void submitRunnable(HANDLER handler,PaymentHandler paymentHandler,ThreadPoolTaskExecutor threadExecutor,Integer configuredThreadCount,String traceId) {
        int neededThreadCount =  configuredThreadCount - threadExecutor.getActiveCount();

        if(jobConfigService.getStopStatus(handler)) {
            log.info("submitRunnable stopStatus=1 for {} so no jobs will be created |traceId={}",handler,traceId);
            return ;
        }

        if(neededThreadCount > 0) {
            log.info("submitRunnable submitting runnable for {} initiated: neededThreads={}|traceId={}",handler,neededThreadCount,traceId);
            for (int i = 0; i < neededThreadCount; i++) {

                threadExecutor.submit(() -> {
                    log.info("submitRunnable for {} started|traceId={}",handler,traceId);
                    Instant start = Instant.now();
                    try
                    {
                        paymentHandler.execute();
                        Long end = SchUtil.getTimeTaken(start);
                        log.error("executeAsync for {} Completed ########### timeTaken={}|traceId={}",handler,end,traceId);
                    }
                    catch (Exception e)
                    {
                        Long end = SchUtil.getTimeTaken(start);
                        log.error("submitRunnable for {} Failed with an exception timeTaken={}|traceId={}",handler,end,traceId,e);
                    }

                });
            }
            log.info("submitRunnable submitting runnable for {} completed: neededThreads={}|traceId={}",handler,neededThreadCount,traceId);
        }
        else{
            log.info("submitRunnable No new Threads created for {} since configured thread count is already active: neededThreads={}|traceId={}",handler,neededThreadCount,traceId);
        }
    }

    @Override
    public List<JobDetailDto> setThreadDetails(List<JobDetailDto> jobDetailDtoList,String traceId) {
        String jobDetailListString = SchUtil.convertToString(jobDetailDtoList);
        log.info("setThreadDetailsRequest request={}|traceId={}",jobDetailListString,traceId);
        jobDetailDtoList.forEach(detail->{

                if(detail.getJobKey().equals(JobKeys.RBM.getJobName())){
                    detail.setActiveThreads(rbmThreadExecutor.getActiveCount());
                    detail.setConfiguredThreads(rbmCoreSize);
                }
                else if(detail.getJobKey().equals(JobKeys.RBM_RETRY.getJobName())){
                    detail.setActiveThreads(rbmRetryThreadExecutor.getActiveCount());
                    detail.setConfiguredThreads(rbmRetryCoreSize);
                }
                else if(detail.getJobKey().equals(JobKeys.OCS.getJobName())){
                    detail.setActiveThreads(ocsThreadExecutor.getActiveCount());
                    detail.setConfiguredThreads(ocsCoreSize);
                }
                else if(detail.getJobKey().equals(JobKeys.CANCEL_PAYMENT.getJobName())){
                    detail.setActiveThreads(cancelThreadExecutor.getActiveCount());
                    detail.setConfiguredThreads(cancelCoreSize);
                }
                else if(detail.getJobKey().equals(JobKeys.CHEQUE_REALIZE.getJobName())){
                    detail.setActiveThreads(chequeThreadExecutor.getActiveCount());
                    detail.setConfiguredThreads(chequeRealizeCoreSize);
                }
                else if(detail.getJobKey().equals(JobKeys.CHEQUE_REALIZE_RETRY.getJobName())){
                    detail.setActiveThreads(chequeRetryThreadExecutor.getActiveCount());
                    detail.setConfiguredThreads(chequeRealizeRetryCoreSize);
                }
        });
        jobDetailListString = SchUtil.convertToString(jobDetailDtoList);
        log.info("setThreadDetailsResponse response={}|traceId={}",jobDetailListString,traceId);
        return jobDetailDtoList;
    }
}
