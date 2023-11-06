package lk.dialog.pe.scheduler.config.thread;

import lk.dialog.pe.scheduler.util.JobKeys;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ConfigurationProperties(prefix = "pe.thread-pool")
@EnableAsync @Setter
public class ThreadPoolConfig {

    private Integer rbmCoreSize;
    private Integer rbmRetryCoreSize;
    private Integer ocsCoreSize;
    private Integer cancelCoreSize;
    private Integer chequeRealizeCoreSize;
    private Integer chequeRealizeRetryCoreSize;

    private static final int QUEUE_SIZE =0;

    @Bean
    public ThreadPoolTaskExecutor rbmThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(rbmCoreSize);
        executor.setMaxPoolSize(rbmCoreSize);
        executor.setQueueCapacity(QUEUE_SIZE);
        executor.setThreadNamePrefix(JobKeys.RBM.getThreadName());
        executor.setRejectedExecutionHandler(new QueueRejectedExecutionHandler());
        executor.initialize();
        return executor;
    }
    @Bean
    public ThreadPoolTaskExecutor rbmRetryThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(rbmRetryCoreSize);
        executor.setMaxPoolSize(rbmRetryCoreSize);
        executor.setQueueCapacity(QUEUE_SIZE);
        executor.setThreadNamePrefix(JobKeys.RBM_RETRY.getThreadName());
        executor.setRejectedExecutionHandler(new QueueRejectedExecutionHandler());
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor ocsThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(ocsCoreSize);
        executor.setMaxPoolSize(ocsCoreSize);
        executor.setQueueCapacity(QUEUE_SIZE);
        executor.setThreadNamePrefix(JobKeys.OCS.getThreadName());
        executor.setRejectedExecutionHandler(new QueueRejectedExecutionHandler());
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor cancelThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cancelCoreSize);
        executor.setMaxPoolSize(cancelCoreSize);
        executor.setQueueCapacity(QUEUE_SIZE);
        executor.setThreadNamePrefix(JobKeys.CANCEL_PAYMENT.getThreadName());
        executor.setRejectedExecutionHandler(new QueueRejectedExecutionHandler());
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor chequeThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(chequeRealizeCoreSize);
        executor.setMaxPoolSize(chequeRealizeCoreSize);
        executor.setQueueCapacity(QUEUE_SIZE);
        executor.setThreadNamePrefix(JobKeys.CHEQUE_REALIZE.getThreadName());
        executor.setRejectedExecutionHandler(new QueueRejectedExecutionHandler());
        executor.initialize();
        return executor;
    }

    @Bean
    public ThreadPoolTaskExecutor chequeRetryThreadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(chequeRealizeRetryCoreSize);
        executor.setMaxPoolSize(chequeRealizeRetryCoreSize);
        executor.setQueueCapacity(QUEUE_SIZE);
        executor.setThreadNamePrefix(JobKeys.CHEQUE_REALIZE_RETRY.getThreadName());
        executor.setRejectedExecutionHandler(new QueueRejectedExecutionHandler());
        executor.initialize();
        return executor;
    }

}
