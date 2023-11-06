package lk.dialog.pe.cheque.payment.service.impl;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lk.dialog.pe.cheque.payment.service.PersisitenceIntegrationService;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.RestExecutor;

@Service
public class PersisitenceIntegrationServiceImpl implements PersisitenceIntegrationService {

	@Autowired
	private RestExecutor restExecutor;

	@Autowired
	protected ObjectMapper objectMapper;

	@Value("${pe.persistance.url.forceful-realize-cheques}")
	private String forcefulRealizeChequesEndpoint;

	@Value("${pe.persistance.url.ocs-payment}")
	private String ocsPaymentEndpoint;

	@Value("${pe.persistance.url.next-payment-sequence}")
	private String nextPaymentSequence;

	@Value("${pe.persistance.url.rbm-payment}")
	private String rbmPaymentEndpoint;

	@Value("${pe.persistance.url.dbn-payment}")
	private String dbnPaymentEndpoint;

	private static final Logger LOGGER = LoggerFactory.getLogger(PersisitenceIntegrationServiceImpl.class);

	private static final String SERVICE_ERROR = "Error occurred while processing your request. please try again.";

	public void lodgeChqForecefulRealize(PaymentPostRequest paymentPostRequest, long trxId, String traceId,boolean isRealTime)
			throws DCPEException {
		String requestString = DCPEUtil.convertToString(paymentPostRequest);

		Instant start = Instant.now();
		LOGGER.info("lodgeChqForecefulRealizeRequest : traceId={}|requestBody={}", traceId, requestString);

		paymentPostRequest.setReqId(trxId);
		String response = null;
    paymentPostRequest.setRealTime(isRealTime ? "Y" : "N");
		response = restExecutor.post(forcefulRealizeChequesEndpoint, traceId, paymentPostRequest, String.class);
		LOGGER.info("lodgeChqForecefulRealizeResponse : traceId={}|response={}", traceId, response);

		Long timeTaken = DCPEUtil.getTimeTaken(start);

		LOGGER.info("lodgeChqForecefulRealizeResponse : end={}|traceId={}", timeTaken, traceId);
	}

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

	public void lodgeRBMPayment(PaymentPostRequest paymentPostRequest, long trxId, String traceId)
			throws DCPEException {
		String requestString = DCPEUtil.convertToString(paymentPostRequest);
		Instant start = Instant.now();
		//todo - logs typo
		LOGGER.info("lodgeRBMPaymentRequest : traceId={}requestBody={}", traceId, requestString);

		paymentPostRequest.setReqId(trxId);

		String response = restExecutor.post(rbmPaymentEndpoint, traceId, paymentPostRequest, String.class);
		//todo - incorrect log and typo
		LOGGER.info("lodgeRBMPaymentRequest : traceId={}|response{}", traceId, response);

		Long end = Duration.between(start, Instant.now()).toMillis();
		//todo - incorrect log
		LOGGER.info("lodgeRBMPaymentResponse : end={}|traceId={}", end, traceId);

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

	//todo - PEException never thrown
	@Override
	public Long nextPaymentSequence(String traceId) throws PEException, DCPEException {
		Instant start = Instant.now();
		LOGGER.info("nextPaymentSequenceRequest : traceId={}", traceId);
		long nextSeqStr = -1l;
		try {
			nextSeqStr = restExecutor.get(nextPaymentSequence, traceId, Long.class);
		} catch (Exception e) {
			//todo - some error by intellij
			LOGGER.error("nextPaymentSequenceResponse : traceId={}|message={}|error={}", traceId, e.getMessage(), e);
			throw new DCPEException(SERVICE_ERROR, 99);
		}

		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("nextPaymentSequenceResponse : end={}|nextSeqStr{}|traceId={}", end, nextSeqStr, traceId);
		return nextSeqStr;
	}

}
