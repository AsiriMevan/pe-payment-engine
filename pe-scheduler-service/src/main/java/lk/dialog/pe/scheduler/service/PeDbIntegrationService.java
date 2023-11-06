package lk.dialog.pe.scheduler.service;

import lk.dialog.pe.scheduler.domain.PayMode;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.QUERY;
import org.springframework.cache.annotation.Cacheable;

import java.sql.SQLException;
import java.util.List;

public interface PeDbIntegrationService {

    @Cacheable("payModeCache")
    PayMode getPayMode(String cposId) throws SQLException;

    List<Payment> loadPaymentsForProcessing(HANDLER handler);

    int updatePaymentPostSuccess(HANDLER handler, COMMAND_READ commandRead, Long trxId);

    int updatePaymentPostSuccessWithPaySequence(HANDLER handler, COMMAND_READ commandRead, Long trxId, Integer paymentSequence);

    int updatePaymentPostSuccessWithPaySequenceAndErrDesc(HANDLER handler, COMMAND_READ commandRead, Long trxId, Integer paymentSequence,String errorDescription);


    int updatePaymentPostFailure(HANDLER handler, COMMAND_READ commandRead, Long trxId, String errorDescription);

    int updatePaymentPostRetryFailure(HANDLER handler, Long trxId);

    int updatePaymentPostFailureWithPaySequence(HANDLER handler, COMMAND_READ commandRead, Long trxId, Integer paymentSequence, String errorDescription);

    int updatePaymentPostCustom(HANDLER handler, QUERY query, COMMAND_READ commandRead, Long trxId, Integer paymentSequence, String errorDescription);

    List<Payment> getPartialChqRealizePayments();

    List<Payment> getFullCheckRealizePayments(String accountNo, String chequeNo);

    boolean isChequeReturnExist(String chqNo, String chqBank, String chqBranch);

    boolean logChequeReturn(String contractNo, String chqBank, String chqBranch, String chqNo, String receiptNo);

    void deleteOcsPayment(Long trxId);
}
