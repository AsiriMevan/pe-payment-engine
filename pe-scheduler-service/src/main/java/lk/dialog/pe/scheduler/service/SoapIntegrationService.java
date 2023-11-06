package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.dto.QueryPaymentsSummaryRequest;
import lk.dialog.pe.scheduler.exception.NEException;

import java.util.List;

public interface SoapIntegrationService {

    int postPayment(PaymentDTO paymentDTO,Long traceId) throws NEException, DCPEException;

    int postChqInSuspendState(PaymentDTO paymentDTO,Long traceId) throws NEException, DCPEException;

    int doRealizeChqForcefully(PaymentDTO paymentDTO) throws DCPEException;

    String queryUnrealizedCheques(PaymentDTO paymentDto) throws DCPEException, NEException;

    void cancelPayment(PaymentDTO paymentDTO) throws DCPEException;

    int modifyPaymentExtension(PaymentDTO paymentDTO) throws DCPEException;

    PaymentDTO queryPaymentsSummery(String receiptNo, String receiptBranch) throws DCPEException;

    List<PaymentDTO> queryPaymentsSummery(QueryPaymentsSummaryRequest queryPaymentsSummaryRequest) throws DCPEException;
}

