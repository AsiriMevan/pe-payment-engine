package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.util.PAYMENT_SYSTEM;

public interface SmsService {

    void sendSmsProxy(Payment payment, PAYMENT_SYSTEM paymentSystem);

    String insertOtcPayment(Payment payment);
}
