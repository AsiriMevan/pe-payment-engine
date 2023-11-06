package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.scheduler.dto.SmsRequest;

public interface CcbsDbIntegrationService {
    int sendSms(SmsRequest smsRequest);

    String queryForStarNomineeNumberCcbs(Long contractId);

    String queryForStarNomineeNumberTelbiz(String contractId);
}
