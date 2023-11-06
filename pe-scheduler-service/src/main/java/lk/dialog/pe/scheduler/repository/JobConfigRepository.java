package lk.dialog.pe.scheduler.repository;

import lk.dialog.pe.scheduler.domain.JobConfig;
import lk.dialog.pe.scheduler.util.JobKeys;

import java.util.List;

public interface JobConfigRepository {
    boolean updateJobStopStatus(JobKeys jobKey, boolean stopJob,String user);

    boolean getJobStopStatus(JobKeys jobKey);

    List<JobConfig> getAll();

    JobConfig getByJobKey(JobKeys jobKey);

    boolean insertJob(JobKeys jobKey, boolean stopJob);
}
