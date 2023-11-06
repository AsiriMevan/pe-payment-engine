package lk.dialog.pe.scheduler.core.handler;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.BaseResponse;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.QuerySystem;
import lk.dialog.pe.scheduler.core.PaymentHandler;
import lk.dialog.pe.scheduler.domain.Payment;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.util.COMMAND_READ;
import lk.dialog.pe.scheduler.util.HANDLER;
import lk.dialog.pe.scheduler.util.QUERY;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

/**
 * This job will cancel payments which were posted to Telbiz & RBM Billing
 * systems.
 */
@Slf4j
@Service
@Qualifier("paymentCancelHandler")
public class PaymentCancelHandler extends PaymentHandler {
    private static final  String REMARK_TEXT = "{0}|Mistake Done By:{1}" ;
    private static final  String PAYMENT_NOT_FOUND ="Payment-Not-Found";

    @Override
    public void execute() {
        loggerInitTraceId();
        List<Payment> payments = loadPaymentData();
        payments.forEach(payment -> {
            BaseResponse baseResponse;
            String paymentString = SchUtil.convertToStringPretty(payment);
            log.info("CancelPaymentHandler starting execution of Cancel payment submission contractId={}|payment={}",payment.getContractNo(),paymentString);
            loggerAppendTrxId(payment.getTrxID());
            try {
                baseResponse = processCancelPayment(payment);
                log.info("CancelPaymentHandler Payment cancellation post to billing system successfully. contractNo={} | receiptNo={}",payment.getContractNo(),payment.getReceiptNo());
                if (baseResponse.getStatus() == Constants.SUCCESS) {
                    peIntegrationService.updatePaymentPostSuccessWithPaySequence(HANDLER.CANCEL_PAY, COMMAND_READ.S,payment.getTrxID(),Integer.parseInt(String.valueOf(payment.getPhysicalPaymentSeq())));
                    if (payment.getChequeReturn().equalsIgnoreCase("Y") && !peIntegrationService.isChequeReturnExist(payment.getChequeNo(), payment.getChequebankCode(), payment.getChequebankBranchCode())) {
                        /* apply surcharge for first payment of the cheque */
                        log.info("CancelPaymentHandler applying surcharge for first payment of the cheque contractNo={} | receiptNo={}",payment.getContractNo(),payment.getReceiptNo());
                        String status = smsService.insertOtcPayment(payment);
                        log.info("CancelPaymentHandler One time charge status ={}|contractNo={}",status,payment.getContractNo());
                        peIntegrationService.logChequeReturn(payment.getContractNo(), payment.getChequebankCode(), payment.getChequebankBranchCode(), payment.getChequeNo(), payment.getReceiptNo());
                        log.info("CancelPaymentHandler applying surcharge for first payment of the cheque success contractNo={} | receiptNo={}",payment.getContractNo(),payment.getReceiptNo());
                    }

                } else {
                    String errorDescription= "Error: Invalid response status from modifyPaymentExtension SOAP API";
                    peIntegrationService.updatePaymentPostCustom(HANDLER.CANCEL_PAY, QUERY.SQL_UPDATE_CANCEL_PAYMENT_RES,COMMAND_READ.F,payment.getTrxID(),Integer.parseInt(String.valueOf(payment.getPhysicalPaymentSeq())),errorDescription);
                    log.error("CancelPaymentHandler Payment cancellation post failed in billing system. trxId={}|contractNo={}|errorDescription={}",payment.getTrxID(),payment.getContractNo(),errorDescription);
                }
            } catch (Exception exception) {

                if (PAYMENT_NOT_FOUND.equals(exception.getMessage())) {
                    String errorDescription= getFailMessage(exception);
                    peIntegrationService.updatePaymentPostFailureWithPaySequence(HANDLER.CANCEL_PAY, COMMAND_READ.N,payment.getTrxID(),Integer.parseInt(String.valueOf(payment.getPhysicalPaymentSeq())),errorDescription);
                    log.error("CancelPaymentHandler Payment not posted to relevant billing system yet to cancel. trxId={}|contractNo={}|error={}" ,payment.getTrxID(),payment.getContractNo(),exception.getMessage(),exception);
                } else {
                    String errorDescription= getFailMessageWithCause(exception);
                    peIntegrationService.updatePaymentPostCustom(HANDLER.CANCEL_PAY, QUERY.SQL_UPDATE_CANCEL_PAYMENT_RES,COMMAND_READ.F,payment.getTrxID(),Integer.parseInt(String.valueOf(payment.getPhysicalPaymentSeq())),errorDescription);
                    log.error("CancelPaymentHandler Payment cancellation post failed in billing system. trxId={}|contractNo={}|error={}" ,payment.getTrxID(),payment.getContractNo(),exception.getMessage(),exception);
                }
            }

        });
        loggerClearTraceId();
    }

    //-----------------logic blocks start----------------

    private BaseResponse processCancelPayment(Payment payment) throws DCPEException {
        log.info("processCancelPaymentRequest errorCode={}|errorDescription={}",payment.getErrCode(),payment.getErrDesc());
        if ((payment.getErrCode() != null && Integer.parseInt(payment.getErrCode()) == Constants.TRANSFER_FAIL)
                && (payment.getErrDesc() != null && payment.getErrDesc().contains("MODIF_PAY_EXT_FAIL"))) {
        log.info("processCancelPayment processing cancel complete scenario");
            return cancelPaymentData(payment, true);
        } else {
            log.info("processCancelPayment processing cancel incomplete scenario");
            return  cancelPaymentData(payment, false);
        }
    }

    @Override
    public BaseResponse cancelPaymentData(Payment paymentDAO, boolean isCancelComplete) throws DCPEException {
        log.info("cancelPaymentDataRequest querySystem={}|productCategory={}|isCancelComplete={}",QuerySystem.getQuerySystem(paymentDAO.getQuerySystem()),ProductCategoryEnum.getProductCategoryByValue(paymentDAO.getProductCategory()),isCancelComplete);
        boolean querySystemIsPeOrCcbs= QuerySystem.getQuerySystem(paymentDAO.getQuerySystem()).orEqual(QuerySystem.PE,QuerySystem.CPOS);

        switch (ProductCategoryEnum.getProductCategoryByValue(paymentDAO.getProductCategory())) {
            case TELBIZ: case CCBS: case NFC:
                /* dcs or volte(as telebiz) or RBM(as CCBS or NFC) payment cancellations */
                if (querySystemIsPeOrCcbs) {
                    /* get PE pending payments account & physical sequence from RBM*/
                    getRbmPaymentsSummary(paymentDAO);
                }
                BaseResponse response = cancelCCBSPayment(paymentDAO, isCancelComplete);
                log.info("cancelPaymentDataResponse success ");
                return response;
            default:
                String errorMessage = "product category ="+paymentDAO.getProductCategory()+" not support for payment cancellation.";
                log.error("cancelPaymentDataResponse failed: {}",errorMessage);
                throw new DCPEException(errorMessage);
        }

    }

    private BaseResponse cancelCCBSPayment(Payment payment, boolean isCancelComplete) throws DCPEException {
        log.info("cancelCCBSPaymentRequest isCancelComplete={}|contractNo={}|connectionReference={}",isCancelComplete,payment.getContractNo(),payment.getConnRef());
        BaseResponse response = new BaseResponse();
        PaymentDTO paymentDTO = new PaymentDTO();
        BeanUtils.copyProperties(payment, paymentDTO);
        if (payment.getMistakeBy() != null) {
            paymentDTO.setRemarks(MessageFormat.format(REMARK_TEXT,paymentDTO.getRemarks(),payment.getMistakeBy()));
        } else {
            paymentDTO.setRemarks(MessageFormat.format(REMARK_TEXT,paymentDTO.getRemarks(),"CX"));
        }
        log.info("cancelCCBSPaymentRequest setting remark text complete remarkText={}",paymentDTO.getRemarks());
        if (!isCancelComplete) {
            try {
                log.info("cancelCCBSPayment requesting cancel payment from RBM" );
                soapIntegrationService.cancelPayment(paymentDTO);
                log.info("cancelCCBSPayment RBM cancelPayment() API success contractNo={}",payment.getContractNo());
            } catch (Exception ex) {
                log.error("cancelCCBSPayment RBM cancelPayment() API failed contractNo={}|connectionReference={}|trxId={}|error={}",payment.getContractNo(),payment.getConnRef(),payment.getTrxID(),ex.getMessage(),ex);
                throw ex;
            }
        }
        try {
            response.setStatus(soapIntegrationService.modifyPaymentExtension(paymentDTO));
        } catch (Exception ex) {
            log.error("cancelCCBSPayment RBM modifyPaymentExtension() API failed contractNo={}|connectionReference={}|error={}",payment.getContractNo(),payment.getConnRef(),payment.getTrxID(),ex.getMessage(),ex);
            throw new DCPEException("MODIF_PAY_EXT_FAIL",ex);
        }
        String responseString= SchUtil.convertToString(response);
        log.info("cancelCCBSPaymentResponse success isCancelComplete={}|contractNo={}|connectionReference={}|baseResponse={}",isCancelComplete,payment.getContractNo(),payment.getConnRef(),responseString);
        return response;
    }

    private void getRbmPaymentsSummary(Payment paymentDAO) throws DCPEException {
        log.info("getRbmPaymentsSummaryRequest receiptNo={}|receiptBranch={}",paymentDAO.getReceiptNo(),paymentDAO.getReceiptBranch());
        PaymentDTO paymentDTO = soapIntegrationService.queryPaymentsSummery(paymentDAO.getReceiptNo(), paymentDAO.getReceiptBranch());
        if (paymentDTO != null) {
            paymentDAO.setPhysicalPaymentSeq(paymentDTO.getPhysicalPaymentSeq());
            paymentDAO.setAccountPaymentSeq(paymentDTO.getAccountPaymentSeq());
            log.info("getRbmPaymentsSummaryResponse success receiptNo={}|receiptBranch={}|physicalPaymentSequence={}|accountPaymentSequence={}",paymentDAO.getReceiptNo(),paymentDAO.getReceiptBranch(),paymentDTO.getPhysicalPaymentSeq(),paymentDTO.getAccountPaymentSeq());
        } else {
            log.error("getRbmPaymentsSummaryResponse failure receiptNo={}|receiptBranch={}|error={}",paymentDAO.getReceiptNo(),paymentDAO.getReceiptBranch(),PAYMENT_NOT_FOUND);
            throw new DCPEException(PAYMENT_NOT_FOUND);
        }
    }


    //-----------------logic blocks end------------------

    @Override
    public synchronized List<Payment> loadPaymentData() {
        return peIntegrationService.loadPaymentsForProcessing(HANDLER.CANCEL_PAY);
    }

    @Override
    public int validatePaymentData(Payment payment) throws DCPEException {
        throw new DCPEException("Please implement payment validations");
    }

    @Override
    public BaseResponse postPaymentData(Payment payment, Object... args) throws DCPEException {
        throw new DCPEException("Only handle payment cancelations");
    }

}
