package lk.dialog.pe.scheduler.repository.Impl;

import lk.dialog.pe.scheduler.domain.QuartzJobDetail;
import lk.dialog.pe.scheduler.repository.QuartzJobDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Repository
public class QuartzJobDetailRepositoryImpl implements QuartzJobDetailRepository {

    @Autowired
    @Qualifier("peJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Override
    public QuartzJobDetail getQuartzDetailsByJobTriggerName(String triggerName) {
        QuartzJobDetail quartzJobDetail = jdbcTemplate.queryForObject("select * from qrtz_triggers where trigger_name=?", new Object[]{triggerName}, (rs, rowNum) -> {
            QuartzJobDetail jobDetail=new QuartzJobDetail();
            jobDetail.setTriggerState(rs.getString("trigger_state"));
            jobDetail.setPreviousFireTime(Instant.ofEpochMilli(rs.getLong("prev_fire_time")).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            jobDetail.setStartTime(Instant.ofEpochMilli(rs.getLong("start_time")).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            jobDetail.setNextFireTime(Instant.ofEpochMilli(rs.getLong("next_fire_time")).atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            return  jobDetail;
        });
        return quartzJobDetail;
    }


}
