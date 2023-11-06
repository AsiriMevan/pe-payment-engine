package lk.dialog.pe.scheduler.core.retry;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.scheduler.core.handler.ChequeForcefulRealizeHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.exception.NEException;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@Qualifier("chequeForcefulRealizeFailHandler")
public class ChequeForcefulRealizeFailHandler extends ChequeForcefulRealizeHandler {

    public ChequeForcefulRealizeFailHandler(){
        isRetry=true;
    }

    @Override
    public void execute() {
        /*cheque cannot be realize at once in RBM. First need to suspend cheque in RBM for each contract payment.*/
        loggerInitTraceId();
        List<Payment> payments = loadPaymentData();
        payments.forEach(payment -> {
            loggerAppendTrxId(payment.getTrxID());
            String paymentString = SchUtil.convertToStringPretty(payment);
            log.info("ChequeRealizeHandler-RETRY starting execution of Check ForceFul realization contractId={}|payment={}",payment.getContractNo(),paymentString);
            int phySeq;
            try {
                if ((phySeq = validatePaymentData(payment)) == -2) {
                    /* payment not exist. hence re-push the record */
                    postPaymentData(payment, Constants.SUSPEND_MODE);
                    log.info("ChequeRealizeHandler-RETRY Payment post to billing system success . trxId={}", payment.getTrxID());
                    peIntegrationService.updatePaymentPostSuccess(HANDLER.CHEQUE_FORCEFUL_PAY, COMMAND_READ.P,payment.getTrxID());
                }else if(phySeq == -1){
                    /* exception occurred during payment query hence retry fail payments again in future */
                    log.info("ChequeRealizeHandler-RETRY  exception occurred during payment query hence updating row to retry fail payments again in future . trxId={}", payment.getTrxID());
                    peIntegrationService.updatePaymentPostRetryFailure(HANDLER.CHEQUE_FORCEFUL_PAY_RETRY,payment.getTrxID());

                } else {
                    /* payment already exist */
                    log.info("ChequeRealizeHandler-RETRY  payment already exist so updating row as PAYMENT-ALREADY-EXIST. trxId={}", payment.getTrxID());
                    peIntegrationService.updatePaymentPostSuccessWithPaySequenceAndErrDesc(HANDLER.CHEQUE_FORCEFUL_PAY_RETRY, COMMAND_READ.P,payment.getTrxID(),null,"PAYMENT-ALREADY-EXIST");

                }
            } catch (Exception e) {
                String errorDescription = getFailMessageWithCause(e);
                peIntegrationService.updatePaymentPostFailure(HANDLER.CHEQUE_FORCEFUL_PAY,COMMAND_READ.F,payment.getTrxID(),errorDescription);
                log.error("ChequeRealizeHandler-RETRY Payment post failed in billing system. trxId={}|contractNo={}|connectionReference={}|chequeNo={}|error={}",payment.getTrxID(),payment.getContractNo(),payment.getConnRef(),payment.getChequeNo(),e.getMessage(),e);
            }
        });
        /* cheque suspend is done. Now need to realize cheque */
        log.info("ChequeRealizeHandler-RETRY cheque suspend is done, realize cheques started.");
        postForcefulChqPaymentData();
        log.info("ChequeRealizeHandler-RETRY cheque suspend is done, realize cheques completed.");
    }

    @Override
    public synchronized List<Payment> loadPaymentData() {
        return peIntegrationService.loadPaymentsForProcessing(HANDLER.CHEQUE_FORCEFUL_PAY_RETRY);
    }

    @Override
    public int validatePaymentData(Payment paymentDAO) {
        int paymentSeq = -1;
        try {
            PaymentDTO paymentDTO = new PaymentDTO();
            BeanUtils.copyProperties(paymentDAO, paymentDTO);
            soapIntegrationService.queryUnrealizedCheques(paymentDTO);
            paymentSeq = 1;
        }catch(NEException neException){
            /* No suspend cheque found */
            String paymentString = SchUtil.convertToString(paymentDAO);
            log.error("ChequeRealizePaymentHandler-RETRY Fail queryUnrealizedCheques trxId={}|payment={}|error={}" ,paymentDAO.getTrxID(),paymentString,neException.getErrorMessage(),neException);
            paymentSeq = -2;
        } catch (DCPEException exception) {
            String paymentString = SchUtil.convertToString(paymentDAO);
            log.error("ChequeRealizePaymentHandler-RETRY Fail payment queryUnrealizedCheques with exception trxId={}|payment={}|error={}" ,paymentDAO.getTrxID(),paymentString,exception.getMessage(),exception);
        }
        return paymentSeq;
    }

}
