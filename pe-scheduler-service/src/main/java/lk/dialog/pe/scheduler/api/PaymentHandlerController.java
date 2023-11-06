package lk.dialog.pe.scheduler.api;

import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.scheduler.core.handler.ChequeForcefulRealizeHandler;
import lk.dialog.pe.scheduler.core.handler.OCSPaymentHandler;
import lk.dialog.pe.scheduler.core.handler.PaymentCancelHandler;
import lk.dialog.pe.scheduler.core.handler.RBMPaymentHandler;
import lk.dialog.pe.scheduler.core.retry.ChequeForcefulRealizeFailHandler;
import lk.dialog.pe.scheduler.core.retry.RBMPaymentFailHandler;
import lk.dialog.pe.scheduler.exception.DCPEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
@RestController
@RequestMapping("/payment-handler")
@Slf4j

public class PaymentHandlerController {

    @Autowired
    @Qualifier("rbmPaymentHandler")
    RBMPaymentHandler rBMPaymentHandler;

    @Autowired
    @Qualifier("rbmPaymentFailHandler")
    RBMPaymentFailHandler rBMPaymentFailHandler;

    @Autowired
    @Qualifier("paymentCancelHandler")
    PaymentCancelHandler paymentCancelHandler;

    @Autowired
    @Qualifier("chequeForcefulRealizeHandler")
    ChequeForcefulRealizeHandler chequeForcefulRealizeHandler;

    @Autowired
    @Qualifier("chequeForcefulRealizeFailHandler")
    ChequeForcefulRealizeFailHandler chequeForcefulRealizeFailHandler;

    @Autowired
    @Qualifier("ocsPaymentHandler")
    OCSPaymentHandler oCSPaymentHandler;

    @GetMapping(value = "/rbm")
    public ResponseEntity<?> postChqInSuspendState() throws DCPEException {
        Instant start = Instant.now();
        String traceId = null;
        if(traceId==null) traceId = DCPEUtil.generateTraceId();
        log.info("rbmRequest : traceId={}", traceId);
        Object response=null;
        try {

            rBMPaymentHandler.execute();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Long timeTaken = DCPEUtil.getTimeTaken(start);
        log.info("rbmResponse : traceId={}|timeTaken={}", traceId, timeTaken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/rbm-retry")
    public ResponseEntity<?> rbmRetry() throws DCPEException {
        Instant start = Instant.now();
        String traceId = null;
        if(traceId==null) traceId = DCPEUtil.generateTraceId();
        log.info("rbmRetryRequest : traceId={}", traceId);
        Object response=null;
        try {
            rBMPaymentFailHandler.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long timeTaken = DCPEUtil.getTimeTaken(start);
        log.info("rbmRetryResponse : traceId={}|timeTaken={}", traceId, timeTaken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/payment-cancel")
    public ResponseEntity<?> paymentCancel() throws DCPEException {
        Instant start = Instant.now();
        String traceId = null;
        if(traceId==null) traceId = DCPEUtil.generateTraceId();
        log.info("paymentCancelRequest : traceId={}", traceId);
        Object response=null;
        try {
            paymentCancelHandler.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long timeTaken = DCPEUtil.getTimeTaken(start);
        log.info("paymentCancelResponse : traceId={}|timeTaken={}", traceId, timeTaken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/cheque-forceful-realize")
    public ResponseEntity<?> chequeForcefulRealize() throws DCPEException {
        Instant start = Instant.now();
        String traceId = null;
        if(traceId==null) traceId = DCPEUtil.generateTraceId();
        log.info("rchequeForcefulRealizeRequest : traceId={}", traceId);
        Object response=null;
        try {
            chequeForcefulRealizeHandler.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long timeTaken = DCPEUtil.getTimeTaken(start);
        log.info("chequeForcefulRealizeResponse : traceId={}|timeTaken={}", traceId, timeTaken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping(value = "/cheque-forceful-realize-retry")
    public ResponseEntity<?> chequeForcefulRealizeRetry() throws DCPEException {
        Instant start = Instant.now();
        String traceId = null;
        if(traceId==null) traceId = DCPEUtil.generateTraceId();
        log.info("chequeForcefulRealizeRetryRequest : traceId={}", traceId);
        Object response=null;
        try {
            chequeForcefulRealizeFailHandler.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long timeTaken = DCPEUtil.getTimeTaken(start);
        log.info("chequeForcefulRealizeRetryResponse : traceId={}|timeTaken={}", traceId, timeTaken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/ocs")
    public ResponseEntity<?> ocs() throws DCPEException {
        Instant start = Instant.now();
        String traceId = null;
        if(traceId==null) traceId = DCPEUtil.generateTraceId();
        log.info("ocsRequest : traceId={}", traceId);
        Object response=null;
        try {
            oCSPaymentHandler.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long timeTaken = DCPEUtil.getTimeTaken(start);
        log.info("ocsResponse : traceId={}|timeTaken={}", traceId, timeTaken);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

