package lk.dialog.pe.scheduler.util;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum JobKeys {
    RBM("rbm-job","rbm-job-trigger","rbm-thread"),
    RBM_RETRY("rbm-retry-job","rbm-retry-job-trigger","rbm-retry-thread"),
    OCS("ocs-job","ocs-job-trigger","ocs-thread"),
    CANCEL_PAYMENT("payment-cancel-job","payment-cancel-job-trigger","cancel-thread"),
    CHEQUE_REALIZE("cheque-realize-job","cheque-realize-job-trigger","cheque-thread"),
    CHEQUE_REALIZE_RETRY("cheque-realize-retry-job","cheque-realize-retry-job-trigger","cheque-retry-thread"),
    KAFKA_PUBLISH_GSM_DTV("kafka-publish-gsm-dtv-job","kafka-publish-gsm-dtv-job-trigger","null"),
    KAFKA_PUBLISH_CHEQUE("kafka-publish-cheque-job","kafka-publish-cheque-job-trigger","null"),
    KAFKA_PUBLISH_CANCEL_PAYMENT("kafka-publish-cancel-job","kafka-publish-cancel-job-trigger","null");
    private String jobName;
    private String triggerName;
    private String threadName;

    JobKeys(String name, String trigger,String threadName){
        this.jobName = name;
        this.triggerName =trigger;
        this.threadName = threadName;
    }

    public static JobKeys getJobTypeByKey(String key){
       return  Arrays.stream(JobKeys.values()).filter(job->job.getJobName().equals(key)).findFirst().orElse(null);
    }
}
