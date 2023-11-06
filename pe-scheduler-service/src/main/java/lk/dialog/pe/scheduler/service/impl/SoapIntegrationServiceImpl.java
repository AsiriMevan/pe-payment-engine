package lk.dialog.pe.scheduler.service.impl;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.dto.QueryPaymentsSummaryRequest;
import lk.dialog.pe.scheduler.exception.NEException;
import lk.dialog.pe.scheduler.service.SoapIntegrationService;
import lk.dialog.pe.scheduler.soap.SoapExecutor;
import lk.dialog.pe.scheduler.soap.custom.ApplicationExceptionError;
import lk.dialog.pe.scheduler.soap.custom.ParameterExceptionError;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class SoapIntegrationServiceImpl implements SoapIntegrationService {

    @Autowired
    SoapExecutor billingInterface;

    @Override
    public int postPayment(PaymentDTO paymentDTO, Long trxId) throws NEException, DCPEException {
        Instant startTime =Instant.now();
        String requestString = SchUtil.convertToString(paymentDTO);
        log.info("postPaymentRequest[SOAP] paymentDto={}|trxId={}",requestString,trxId);
        try {
            int paymentSequence = billingInterface.postPayment(paymentDTO);
            log.info("postPaymentResponse[SOAP] Success paymentSequence={}|timeTaken={}|trxId={}",paymentSequence,SchUtil.getTimeTaken(startTime),trxId);
            return paymentSequence ;
        } catch (RemoteException|ApplicationExceptionError|ParameterExceptionError ex) {
            log.error("postPaymentResponse[SOAP] Failed trxId={}|timeTaken={}|error={}",trxId,SchUtil.getTimeTaken(startTime),ex.getMessage(),ex);
            throw new DCPEException(ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public int postChqInSuspendState(PaymentDTO paymentDTO, Long trxId) throws NEException,DCPEException {
        Instant startTime =Instant.now();
        String requestString = SchUtil.convertToString(paymentDTO);
        log.info("postChqInSuspendStateRequest[SOAP] paymentDto={}|trxId={}",requestString,trxId);
        try {
            int paymentSequence = billingInterface.postChqInSuspendState(paymentDTO);
            log.info("postChqInSuspendStateResponse[SOAP] Success paymentSequence={}|timeTaken={}|trxId={}",paymentSequence,SchUtil.getTimeTaken(startTime),trxId);
            return paymentSequence ;
        } catch (RemoteException|ApplicationExceptionError|ParameterExceptionError ex) {
            log.error("postChqInSuspendStateResponse[SOAP] Failed trxId={}|timeTaken={}|error={}",trxId,SchUtil.getTimeTaken(startTime),ex.getMessage(),ex);
            throw new DCPEException(ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public int doRealizeChqForcefully(PaymentDTO paymentDTO)throws DCPEException {
        Instant startTime =Instant.now();
        String requestString = SchUtil.convertToString(paymentDTO);
        log.info("doRealizeChqForcefullyRequest[SOAP] paymentDto={}|trxId={}",requestString,paymentDTO.getTrxID());
        try {
            int response = billingInterface.doRealizeChqForcefully(paymentDTO);
            log.info("doRealizeChqForcefullyResponse[SOAP] Success response={}|timeTaken={}|trxId={}", SchUtil.getTimeTaken(startTime),paymentDTO.getTrxID());
            return response;
        } catch (RemoteException|ApplicationExceptionError|ParameterExceptionError ex) {
            log.error("doRealizeChqForcefullyResponse[SOAP] Failed trxId={}|timeTaken={}|error={}",paymentDTO.getTrxID(),SchUtil.getTimeTaken(startTime),ex.getMessage(),ex);
            throw new DCPEException(ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public String queryUnrealizedCheques(PaymentDTO paymentDto) throws NEException,DCPEException {
        Instant startTime =Instant.now();
        String requestString = SchUtil.convertToString(paymentDto);
        log.info("queryUnrealizedChequesRequest[SOAP] paymentDto={}|trxId={}",requestString,paymentDto.getTrxID());
        try {
            String response = billingInterface.queryUnrealizedCheques(paymentDto);
            log.info("queryUnrealizedChequesResponse[SOAP] Success response={}|timeTaken={}|trxId={}", SchUtil.getTimeTaken(startTime),paymentDto.getTrxID());
            return response;
        } catch (RemoteException | ApplicationExceptionError | ParameterExceptionError ex) {
            log.error("queryUnrealizedChequesResponse[SOAP] Failed trxId={}|timeTaken={}|error={}",paymentDto.getTrxID(),SchUtil.getTimeTaken(startTime),ex.getMessage(),ex);
            throw new DCPEException(ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public void cancelPayment(PaymentDTO paymentDto) throws DCPEException {
        Instant startTime =Instant.now();
        String requestString = SchUtil.convertToString(paymentDto);
        log.info("cancelPaymentRequest[SOAP] paymentDto={}|trxId={}",requestString,paymentDto.getTrxID());
        try {
            int response = billingInterface.cancelPayment(paymentDto);
            log.info("cancelPaymentResponse[SOAP] Success response={}|timeTaken={}|trxId={}", SchUtil.getTimeTaken(startTime),paymentDto.getTrxID());
        } catch (Exception ex) {
            log.error("cancelPaymentRequestResponse[SOAP] Failed trxId={}|timeTaken={}|error={}",paymentDto.getTrxID(),SchUtil.getTimeTaken(startTime),ex.getMessage(),ex);
            throw new DCPEException(ex.getMessage(),ex.getCause());
        }
    }



    @Override
    public int modifyPaymentExtension(PaymentDTO paymentDto) throws DCPEException {
        Instant startTime =Instant.now();
        String requestString = SchUtil.convertToString(paymentDto);
        log.info("modifyPaymentExtensionRequest[SOAP] paymentDto={}|trxId={}",requestString,paymentDto.getTrxID());
        try {
            int response = billingInterface.modifyPaymentExtension(paymentDto);
            log.info("modifyPaymentExtensionResponse[SOAP] Success response={}|timeTaken={}|trxId={}", SchUtil.getTimeTaken(startTime),paymentDto.getTrxID());
            return  response;
        } catch (Exception ex) {
            log.error("modifyPaymentExtensionResponse[SOAP] Failed trxId={}|timeTaken={}|error={}",paymentDto.getTrxID(),SchUtil.getTimeTaken(startTime),ex.getMessage(),ex);
            throw new DCPEException(ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public PaymentDTO queryPaymentsSummery(String receiptNo, String receiptBranch) throws DCPEException {
        Instant startTime =Instant.now();
        log.info("queryPaymentsSummeryRequest[SOAP] receiptNo={}|receiptBranch={}",receiptNo,receiptBranch);
        try {
            PaymentDTO response = billingInterface.queryPaymentsSummery(receiptNo,receiptBranch);
            String responseString = SchUtil.convertToString(response);
            log.info("queryPaymentsSummeryResponse[SOAP] Success timeTaken={}|receiptNo={}|receiptBranch={}|response={}", SchUtil.getTimeTaken(startTime),receiptNo,receiptBranch,responseString);
            return  response;
        } catch (Exception ex) {
            log.error("queryPaymentsSummeryResponse[SOAP] Failed |receiptNo={}|receiptBranch={}|timeTaken={}|error={}",receiptNo,receiptBranch,SchUtil.getTimeTaken(startTime),ex.getMessage(),ex);
            throw new DCPEException(ex.getMessage(),ex.getCause());
        }
    }

    @Override
    public List<PaymentDTO> queryPaymentsSummery(QueryPaymentsSummaryRequest queryPaymentsSummaryRequest) throws DCPEException {
        Instant startTime =Instant.now();
        String requestString = SchUtil.convertToString(queryPaymentsSummaryRequest);
        log.info("queryPaymentsSummeryRequest[SOAP] request={}",requestString);
        try {
            List<PaymentDTO> response = billingInterface.queryPaymentsSummery(queryPaymentsSummaryRequest);
            String responseString = DCPEUtil.convertToString(response);
            log.info("queryPaymentsSummeryResponse[SOAP] Success response={}|timeTaken={}", responseString, SchUtil.getTimeTaken(startTime));
            return  response;
        } catch (Exception ex) {
            log.error("modifyPaymentExtensionResponse[SOAP] Failed timeTaken={}|error={}",SchUtil.getTimeTaken(startTime),ex.getMessage(),ex);
            throw new DCPEException(ex.getMessage(),ex.getCause());
        }
    }

}
