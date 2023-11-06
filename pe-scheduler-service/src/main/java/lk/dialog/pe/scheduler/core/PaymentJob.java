package lk.dialog.pe.scheduler.core;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.BaseResponse;
import lk.dialog.pe.scheduler.domain.Payment;

import java.util.List;


public interface PaymentJob {

	void execute();

	List<Payment> loadPaymentData();

	int validatePaymentData(Payment paymentDTO) throws DCPEException;

	BaseResponse postPaymentData(Payment paymentDTO, Object... args) throws Exception;

	BaseResponse cancelPaymentData(Payment paymentDTO, boolean isCancelComplete) throws DCPEException;

}
