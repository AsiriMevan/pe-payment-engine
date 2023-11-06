package lk.dialog.pe.scheduler.core.retry;

import lk.dialog.pe.scheduler.core.handler.DBNPaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentPostResponse;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.QUERY;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

//the unused implementations from old code is removed
@Slf4j
@Service
@Qualifier("dbnPaymentFailHandler")
public class DBNPaymentFailHandler extends DBNPaymentHandler {

    @Override
    public synchronized List<Payment> loadPaymentData() {
        return peIntegrationService.loadPaymentsForProcessing(HANDLER.DBN_PAY_RETRY);
    }

    @Override
    public int validatePaymentData(Payment paymentDTO) {
        return -2;
    }

    @Override
    public void execute() {
        List<Payment> payments = loadPaymentData();
        payments.forEach(payment -> {
            String paymentString = SchUtil.convertToStringPretty(payment);
            log.info("DBNPaymentHandler-RETRY starting execution of DBN payment submission contractId={}|payment={}",payment.getContractNo(),paymentString);
            PaymentPostResponse response;
            try {
                if ((validatePaymentData(payment)) == -2) {
                    /* payment not exist */
                    response = postPaymentData(payment);
                    if (response.getStatus() != 1) {
                        log.info("DBNPaymentHandler-RETRY updating DBN payments as failed (default flow) contractId={}",payment.getContractNo());
                        peIntegrationService.updatePaymentPostCustom(HANDLER.DBN_PAY, QUERY.SQL_UPDATE_DBN_PAYMENT_RES, COMMAND_READ.F,payment.getTrxID(),response.getResponseSeq(),response.getErrorDesc());
                    }
                }

            } catch (Exception exception) {
                String errorMessage= getFailMessageWithCause(exception);
                log.error("DBNPaymentHandler-RETRY Payment post failed in billing system with exception trxId={}|contractId={}|error={}", payment.getTrxID(), payment.getContractNo(),exception.getMessage(),exception);
                peIntegrationService.updatePaymentPostFailureWithPaySequence(HANDLER.DBN_PAY_RETRY, COMMAND_READ.F,payment.getTrxID(),-1,errorMessage);
            }

        });

    }
}
