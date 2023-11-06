package lk.dialog.pe.other.payment.service.impl;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.RestExecutor;
import lk.dialog.pe.other.payment.service.PersisitenceIntegrationService;

@Service
public class PersisitenceIntegrationServiceImpl implements PersisitenceIntegrationService {

	@Autowired
	private RestExecutor restExecutor;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${dcpe.rest.url.ocs-payment}")
	private String ocsPaymentEndpoint;

	@Value("${dcpe.rest.url.next-payment-sequence}")
	private String nextPaymentSequence;

	@Value("${dcpe.rest.url.rbm-payment}")
	private String rbmPaymentEndpoint;

	@Value("${dcpe.rest.url.dbn-payment}")
	private String dbnPaymentEndpoint;

	private static final Logger LOGGER = LoggerFactory.getLogger(PersisitenceIntegrationServiceImpl.class);

	private static final String SERVICE_ERROR = "Error occurred while processing your request. please try again.";
	
	public void lodgeOCSPayment(PaymentPostRequest payRequest, long trxId, String traceId) throws DCPEException {

		String requestString = DCPEUtil.convertToString(payRequest);
		Instant start = Instant.now();
		LOGGER.info("lodgeOCSPaymentRequest : traceId={}|requestBody={}|trxId={}", traceId, requestString, trxId);

		payRequest.setReqId(trxId);		

		String response = restExecutor.post(ocsPaymentEndpoint, traceId, payRequest, String.class);
		LOGGER.info("lodgeOCSPaymentResponse : traceId={}|response={}", traceId, response);

		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("lodgeOCSPaymentResponse : end={}|traceId={}", end, traceId);
	}

	public void lodgeRBMPayment(PaymentPostRequest paymentPostRequest, long trxId, String traceId) throws DCPEException {

		String requestString = DCPEUtil.convertToString(paymentPostRequest);
		Instant start = Instant.now();
		LOGGER.info("lodgeRBMPaymentRequest :requestBody={} | traceId={}", requestString, traceId);

		paymentPostRequest.setReqId(trxId);

		String response = restExecutor.post(rbmPaymentEndpoint, traceId, paymentPostRequest, String.class);
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("lodgeRBMPaymentResponse : response{} | timeDuration={} | traceId={}", response, end, traceId);

	}

	@Override
	public void lodgeDBNPayment(PaymentPostRequest paymentPostRequest, long payTrxID, String traceId) throws DCPEException {
		String requestString = DCPEUtil.convertToString(paymentPostRequest);
		Instant start = Instant.now();
		LOGGER.info("lodgeDBNPaymentRequest : traceId=[{}] | requestBody=[{}]", traceId, requestString);

		paymentPostRequest.setReqId(payTrxID);

		String response = restExecutor.post(dbnPaymentEndpoint, traceId, paymentPostRequest, String.class);
		LOGGER.info("lodgeDBNPaymentPostResponse : traceId=[{}] | response=[{}]", traceId, response);

		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("lodgeDBNPaymentResponse : timeTaken=[{}] | traceId=[{}]", end, traceId);
	}

//todo - remove unwanted PEException
	@Override
	public Long nextPaymentSequence(String traceId) throws PEException, DCPEException {
		Instant start = Instant.now();
		LOGGER.info("nextPaymentSequenceRequest : traceId={}", traceId);
		long nextSeqStr = -1l;
		try {
			nextSeqStr = restExecutor.get(nextPaymentSequence, traceId, Long.class);
		} catch (Exception e) {
			LOGGER.error("nextPaymentSequenceResponse : traceId={}|error{}", traceId, e.getMessage(), e);
			throw new DCPEException(SERVICE_ERROR, 99);
		}

		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("nextPaymentSequenceResponse : nextSeqStr={}|timeDuration={}|traceId={}", nextSeqStr, end, traceId);
		return nextSeqStr;
	}

}
