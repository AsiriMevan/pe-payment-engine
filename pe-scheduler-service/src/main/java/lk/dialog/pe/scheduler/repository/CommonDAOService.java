package lk.dialog.pe.scheduler.repository;

import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.SmsRequest;

public interface CommonDAOService {

    public String sendSMS(SmsRequest smsRequest);

    public void sendSmsProxy(Payment payment, String paymentSystem);

    String getVolteContactNoForContractNo(String contractNo);

    String getDtvContactNoForContractNo(String contractNo);

    public String insertOTCpayment(Payment payment);

    String getOcsCommandRead(Long reqId);

    String getOcsHistoryCommandRead(Long reqId);
}
