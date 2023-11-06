package lk.dialog.pe.scheduler.repository;

import lk.dialog.pe.scheduler.domain.QuartzJobDetail;

public interface QuartzJobDetailRepository {
    QuartzJobDetail getQuartzDetailsByJobTriggerName(String triggerName);
}
