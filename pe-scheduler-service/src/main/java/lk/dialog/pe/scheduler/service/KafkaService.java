package lk.dialog.pe.scheduler.service;

public interface KafkaService {
    void publishGsmDtvRecords(String traceId);

    void publishDbnRecords(String traceId);

    void publishChequePaymentRecords(String traceId);

    void publishCancellations(String traceId);
}
