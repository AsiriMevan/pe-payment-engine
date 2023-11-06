package lk.dialog.pe.scheduler.core.retry;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.PAY_MODE;
import lk.dialog.pe.common.util.PRODUCT_TYPE;
import lk.dialog.pe.scheduler.core.handler.RBMPaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.dto.PaymentPostResponse;
import lk.dialog.pe.scheduler.exception.NEException;
import lk.dialog.pe.scheduler.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@Qualifier("rbmPaymentFailHandler")
public class RBMPaymentFailHandler extends RBMPaymentHandler {

    public RBMPaymentFailHandler(){
        isRetryProcess=true;
    }

    @Override
    public void execute(){
        loggerInitTraceId();
        List<Payment> payments = loadPaymentData();
        payments.forEach(payment -> {
            loggerAppendTrxId(payment.getTrxID());
            String paymentString = SchUtil.convertToStringPretty(payment);
            log.info("RBMPaymentHandler-RETRY starting execution of RBM payment submission contractId={}|payment={}",payment.getContractNo(),paymentString);
            int phySeq;
            try {
                phySeq = validatePaymentData(payment) ;

                if (phySeq== -2) {
                    /* payment not exist. hence re-push the record */
                    PaymentPostResponse response = postPaymentData(payment);
                    peIntegrationService.updatePaymentPostSuccessWithPaySequence(HANDLER.RBM_PAY_RETRY, COMMAND_READ.S,payment.getTrxID(),response.getResponseSeq());
                    log.info("RBMPaymentHandler-RETRY in reconnection and SMS phase switchStatus={}|productType={}|responseSequence={}", smsConfig.getSwitchStatus(),payment.getProductType(),response.getResponseSeq());
                    if(Boolean.TRUE.equals(smsConfig.isSwitchActive())){
                        executeRbmLogic(response.isPaymentSequenceValid(),payment);
                    }
                } else if (phySeq == -1) {
                    /*exception occurred during payment query hence retry fail payments again in future*/
                    log.info("RBMPaymentHandler-RETRY exception occurred during payment query hence update as failed to retry in future contractId={}|payment={}",payment.getContractNo(),paymentString);
                    peIntegrationService.updatePaymentPostRetryFailure(HANDLER.RBM_PAY_RETRY,payment.getTrxID());
                } else {
                    /* payment already exist. hence only reconnect the line */
                    log.info("RBMPaymentHandler-RETRY payment already exist. hence only reconnecting the line contractId={}",payment.getContractNo());
                    peIntegrationService.updatePaymentPostCustom(HANDLER.RBM_PAY_RETRY, QUERY.SQL_UPDATE_RBM_PAYMENT_RES, COMMAND_READ.S,payment.getTrxID(),phySeq,"PAYMENT-ALREADY-EXIST");
                    reExecuteFailedRbmReconnectLine(payment);
                }
            } catch (NEException exception) {
                String errorDescription= getFailMessage(exception);
                peIntegrationService.updatePaymentPostFailureWithPaySequence(HANDLER.RBM_PAY_RETRY, COMMAND_READ.N,payment.getTrxID(),-1,errorDescription);
                log.error("RBMPaymentHandler-RETRY Payment post failed in billing system. trxId={}|contractId={}|error={}", payment.getTrxID(), payment.getContractNo(),exception.getMessage(),exception);
            } catch (Exception exception) {
                String errorMessage= getFailMessageWithCause(exception);
                log.error("RBMPaymentHandler-RETRY Payment post failed in billing system with exception trxId={}|contractId={}|error={}", payment.getTrxID(), payment.getContractNo(),exception.getMessage(),exception);
                peIntegrationService.updatePaymentPostFailureWithPaySequence(HANDLER.RBM_PAY_RETRY, COMMAND_READ.F,payment.getTrxID(),-1,errorMessage);
            }
        });
    }

    @Override
    public synchronized List<Payment> loadPaymentData() {
        return peIntegrationService.loadPaymentsForProcessing(HANDLER.RBM_PAY_RETRY);
    }

    @Override
    public int validatePaymentData(Payment paymentDTO) throws DCPEException {
        log.info("RBMPaymentHandler-RETRY validatePaymentDataRequest contractId={}",paymentDTO.getContractNo());
        int paymentSeq = -1;

        PaymentDTO payment = soapIntegrationService.queryPaymentsSummery(paymentDTO.getReceiptNo(), null);
        if (payment != null)
            paymentSeq = (int) payment.getPhysicalPaymentSeq();
        else
            paymentSeq = -2;
        log.info("RBMPaymentHandler-RETRY validatePaymentDataResponse paymentSequence={}|contractId={}",paymentSeq,paymentDTO.getContractNo());

        return paymentSeq;
    }
    private void reExecuteFailedRbmReconnectLine(Payment payment){
        if (!PAY_MODE.CHQ.equals(payment.getPaymentMode()) && (PRODUCT_TYPE.OTHER.equals(payment.getProductType()) || PRODUCT_TYPE.VOLTE.equals(payment.getProductType()))) {
            /* not cheque suspend scenario */
            log.info("RBMPaymentHandler-RETRY  executing reconnection and SMS phase. trxId={}|contractId={}", payment.getTrxID(), payment.getContractNo());
            if(Boolean.TRUE.equals(smsConfig.isSwitchActive())){
                reConnectionHandler.reconnect(payment.getContractNo(), payment.getAccountPaymentMny());
                smsService.sendSmsProxy(payment,PAYMENT_SYSTEM.GSM);
            }
        }
    }
}
