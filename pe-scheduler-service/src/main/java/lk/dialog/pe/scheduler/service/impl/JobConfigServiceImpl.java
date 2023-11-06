package lk.dialog.pe.scheduler.service.impl;

import lk.dialog.pe.scheduler.core.PaymentHandlerExecutorService;
import lk.dialog.pe.scheduler.domain.JobConfig;
import lk.dialog.pe.scheduler.dto.JobDetailDto;
import lk.dialog.pe.scheduler.repository.JobConfigRepository;
import lk.dialog.pe.scheduler.repository.QuartzJobDetailRepository;
import lk.dialog.pe.scheduler.service.JobConfigService;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.JobKeys;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JobConfigServiceImpl implements JobConfigService {

    @Autowired
    JobConfigRepository jobConfigRepository;

    @Autowired
    QuartzJobDetailRepository quartzJobDetailRepository;

    @Autowired
    PaymentHandlerExecutorService paymentHandlerExecutorService;

    @Override
    public boolean updateStopJobStatus(JobKeys jobKey, boolean stopStatus, String user){
      log.info("stopJobRequest jobName={}",jobKey.getJobName());
        boolean response = jobConfigRepository.updateJobStopStatus(jobKey,stopStatus,user);
      log.info("stopJobResponse success updateStatus={}|jobName={}",response,jobKey.getJobName());
      return response;
    }
    @Override
    public List<JobDetailDto> getAllJobDetails(String traceId){
        log.info("getAllJobDetailsRequest |traceId={}",traceId);
        List<JobConfig> response = jobConfigRepository.getAll();
        List<JobDetailDto> jobDetails =response.stream().map(config->{
            JobDetailDto detail= new JobDetailDto();
            detail.setJobKey(config.getJobKey());
            detail.setStopJob(config.getStopJob());
            detail.setStopJobLastUpdatedBy(config.getStopJobUpdatedBy());
            detail.setStopJobLastUpdatedDate(config.getStopJobUpdateTime());
            JobKeys jobKey = JobKeys.getJobTypeByKey(config.getJobKey());
            detail.setQuartzJobDetail(quartzJobDetailRepository.getQuartzDetailsByJobTriggerName(jobKey.getTriggerName().toLowerCase()));
            return detail;
        }).collect(Collectors.toList());
        paymentHandlerExecutorService.setThreadDetails(jobDetails,traceId);
        String jobDetailString = SchUtil.convertToString(jobDetails);
        log.info("getAllJobDetailsResponse success response={}|traceId={}",jobDetailString,traceId);
        return jobDetails;
    }

    @Override
    public boolean getStopStatus(HANDLER handler) {
        log.info("getStopStatusRequest handler={}", handler);
        JobKeys jobKey = null;
        switch (handler) {
            case RBM_PAY:
                jobKey = JobKeys.RBM;
                break;
            case RBM_PAY_RETRY:
                jobKey = JobKeys.RBM;
                break;
            case OCS_PAY:
                jobKey = JobKeys.RBM;
                break;
            case CANCEL_PAY:
                jobKey = JobKeys.RBM;
                break;
            case CHEQUE_FORCEFUL_PAY:
                jobKey = JobKeys.RBM;
                break;
            case CHEQUE_FORCEFUL_PAY_RETRY:
                jobKey = JobKeys.RBM;
        }
        boolean status = jobConfigRepository.getJobStopStatus(jobKey);
        log.info("getStopStatusResponse handler={}|stopStatus={}", handler,status);
        return status;
    }


    @Override
    public void initializeJobConfigData(){
        Arrays.stream(JobKeys.values()).forEach(key->{
            if(jobConfigRepository.getByJobKey(key)==null){
                jobConfigRepository.insertJob(key,false);
            }
        });
    }

}
