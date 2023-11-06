package lk.dialog.pe.scheduler.config.quarz;

import lk.dialog.pe.scheduler.job.*;
import lk.dialog.pe.scheduler.util.JobKeys;
import lombok.Setter;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
@ConfigurationProperties(prefix = "pe.job")
@Setter
public class QuartzSubmitJobs {

    private Long handlerThreadsCheckInInterval;
    private Long handlerRetryThreadsCheckInInterval;
    private String kafkaGsmDtvCronInterval;
    private String kafkaChequeCronInterval;
    private String kafkaCancelCronInterval;

    @Bean(name = "rbmJobDetail")
    public JobDetailFactoryBean getRbmJobDetail() {
        return QuartzConfig.createJobDetail(RbmJob.class, JobKeys.RBM.getJobName());
    }
    @Bean
    public SimpleTriggerFactoryBean getRbmTrigger(@Qualifier("rbmJobDetail") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, handlerThreadsCheckInInterval, JobKeys.RBM.getTriggerName());
    }

    @Bean(name = "rbmRetryJobDetail")
    public JobDetailFactoryBean getRbmRetryJobDetail() {
        return QuartzConfig.createJobDetail(RbmRetryJob.class, JobKeys.RBM_RETRY.getJobName());
    }
    @Bean
    public SimpleTriggerFactoryBean getRbmRetryTrigger(@Qualifier("rbmRetryJobDetail") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, handlerRetryThreadsCheckInInterval, JobKeys.RBM_RETRY.getTriggerName());
    }

    @Bean(name = "ocsJobDetail")
    public JobDetailFactoryBean getOcsJobDetail() {
        return QuartzConfig.createJobDetail(OcsJob.class, JobKeys.OCS.getJobName());
    }
    @Bean
    public SimpleTriggerFactoryBean getOcsTrigger(@Qualifier("ocsJobDetail") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, handlerThreadsCheckInInterval, JobKeys.OCS.getTriggerName());
    }

    @Bean(name = "cancelJobDetail")
    public JobDetailFactoryBean getCancelJobDetail() {
        return QuartzConfig.createJobDetail(CancelPaymentJob.class, JobKeys.CANCEL_PAYMENT.getJobName());
    }
    @Bean
    public SimpleTriggerFactoryBean getCancelTrigger(@Qualifier("cancelJobDetail") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, handlerThreadsCheckInInterval, JobKeys.CANCEL_PAYMENT.getTriggerName());
    }

    @Bean(name = "chequeJobDetail")
    public JobDetailFactoryBean getChequeJobDetail() {
        return QuartzConfig.createJobDetail(ChequeRealizeJob.class, JobKeys.CHEQUE_REALIZE.getJobName());
    }
    @Bean
    public SimpleTriggerFactoryBean getChequeTrigger(@Qualifier("chequeJobDetail") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, handlerThreadsCheckInInterval, JobKeys.CHEQUE_REALIZE.getTriggerName());
    }

    @Bean(name = "chequeRealizeRetryDetail")
    public JobDetailFactoryBean getChequeRetryJobDetail() {
        return QuartzConfig.createJobDetail(ChequeRealizeResubmitJob.class, JobKeys.CHEQUE_REALIZE_RETRY.getJobName());
    }
    @Bean
    public SimpleTriggerFactoryBean getChequeRetryTrigger(@Qualifier("chequeRealizeRetryDetail") JobDetail jobDetail) {
        return QuartzConfig.createTrigger(jobDetail, handlerRetryThreadsCheckInInterval, JobKeys.CHEQUE_REALIZE_RETRY.getTriggerName());
    }

    @Bean(name = "kafkaPublishGsmDtvDetail")
    public JobDetailFactoryBean kafkaPublishGsmDtvJobDetail() {
        return QuartzConfig.createJobDetail(ChequeRealizeResubmitJob.class, JobKeys.KAFKA_PUBLISH_GSM_DTV.getJobName());
    }
    @Bean
    public CronTriggerFactoryBean kafkaPublishGsmDtvTrigger(@Qualifier("kafkaPublishGsmDtvDetail") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, kafkaGsmDtvCronInterval, JobKeys.KAFKA_PUBLISH_GSM_DTV.getTriggerName());
    }

    @Bean(name = "kafkaPublishChequeDetail")
    public JobDetailFactoryBean kafkaPublishChequeJobDetail() {
        return QuartzConfig.createJobDetail(ChequeRealizeResubmitJob.class, JobKeys.KAFKA_PUBLISH_CHEQUE.getJobName());
    }
    @Bean
    public CronTriggerFactoryBean kafkaPublishChequeTrigger(@Qualifier("kafkaPublishChequeDetail") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, kafkaChequeCronInterval, JobKeys.KAFKA_PUBLISH_CHEQUE.getTriggerName());
    }

    @Bean(name = "kafkaPublishCancelDetail")
    public JobDetailFactoryBean kafkaPublishCancelJobDetail() {
        return QuartzConfig.createJobDetail(ChequeRealizeResubmitJob.class, JobKeys.KAFKA_PUBLISH_CANCEL_PAYMENT.getJobName());
    }
    @Bean
    public CronTriggerFactoryBean kafkaPublishCancelTrigger(@Qualifier("kafkaPublishCancelDetail") JobDetail jobDetail) {
        return QuartzConfig.createCronTrigger(jobDetail, kafkaCancelCronInterval, JobKeys.KAFKA_PUBLISH_CANCEL_PAYMENT.getTriggerName());
    }




}
