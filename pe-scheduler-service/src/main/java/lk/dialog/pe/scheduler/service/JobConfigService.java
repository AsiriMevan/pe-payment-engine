package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.scheduler.dto.JobDetailDto;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.JobKeys;

import java.util.List;

public interface JobConfigService {
    boolean updateStopJobStatus(JobKeys jobKey, boolean stopStatus, String user);

    List<JobDetailDto> getAllJobDetails(String traceId);

    boolean getStopStatus(HANDLER handler);

    void initializeJobConfigData();
}
