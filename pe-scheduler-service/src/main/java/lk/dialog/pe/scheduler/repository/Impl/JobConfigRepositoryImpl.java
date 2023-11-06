package lk.dialog.pe.scheduler.repository.Impl;

import lk.dialog.pe.scheduler.domain.JobConfig;
import lk.dialog.pe.scheduler.repository.JobConfigRepository;
import lk.dialog.pe.scheduler.util.JobKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Slf4j
public class JobConfigRepositoryImpl implements JobConfigRepository {

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Override
    public boolean updateJobStopStatus(JobKeys jobKey, boolean stopJob,String user) {
        return jdbcTemplate.update("update job_status_config set stop_job=?,stop_job_updated_by=?,stop_job_update_time=current_timestamp where job_key=?", new Object[]{stopJob ? 1 : 0,user,jobKey.getJobName()}) > 0;
    }

    @Override
    public boolean getJobStopStatus(JobKeys jobKey) {
        try {
            return jdbcTemplate.queryForObject("select stop_job from job_status_config where job_key=?", new Object[]{jobKey.getJobName()}, Integer.class) > 0;

        }catch (EmptyResultDataAccessException exception){
            log.error("getJobStopStatus no results found for jobName={}|error={}",jobKey.getJobName(),exception);
            return false;
        }

    }
    @Override
    public List<JobConfig> getAll() {
        return jdbcTemplate.query("select * from job_status_config", new BeanPropertyRowMapper<>(JobConfig.class));
    }
    @Override
    public JobConfig getByJobKey(JobKeys jobKey) {

        try {
            return jdbcTemplate.queryForObject("select * from job_status_config where job_key=?", new Object[]{jobKey.getJobName()}, new BeanPropertyRowMapper<>(JobConfig.class));

        }catch (EmptyResultDataAccessException exception){
            log.error("getByJobKey no results found for jobName={}|error={}",jobKey.getJobName(),exception);
            return null;
        }
    }
    @Override
    public boolean insertJob(JobKeys jobKey, boolean stopJob) {
        return jdbcTemplate.update("insert into job_status_config (job_key, stop_job) values(?,?)", new Object[]{jobKey.getJobName(),stopJob ? 1 : 0 }) > 0;
    }
}