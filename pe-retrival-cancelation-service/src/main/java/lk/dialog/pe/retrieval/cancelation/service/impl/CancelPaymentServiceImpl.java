package lk.dialog.pe.retrieval.cancelation.service.impl;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.RestExecutor;
import lk.dialog.pe.common.util.CancelPaymentRequest;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.retrieval.cancelation.service.CancelPaymentService;

@Service
public class CancelPaymentServiceImpl implements CancelPaymentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CancelPaymentServiceImpl.class);

	@Autowired
	private RestExecutor restExecutor;

	@Value("${api.pe.persistance.url.cancel-payment}")
	private String cancelPayentEndpoint;

	@Value("${api.pe.persistance.url.next-payment-sequence}")
	private String nextPaymentSequenceEndpoint;

	@Value("${api.pe.persistance.url.cancel-ocs-payment}")
	private String ocsPaymentEndpoint;
	
	@Value("${api.pe.ccbs.persistance.url.get-mobile-of-contractId}")
	String getMobileOfContractIdEndpoint;

	public Long getNextSeqStr(long trxId, String traceId) throws DCPEException {

		LOGGER.info("getNextSeqStrRequest : traceId={}", traceId);
		Instant start = Instant.now();
		Long trxNewId = -1l;
		try {
			trxNewId = restExecutor.get(nextPaymentSequenceEndpoint, traceId, Long.class);
		} catch (lk.dialog.pe.common.exception.DCPEException e) {
			LOGGER.info("getNextSeqStrResponse : traceId={}|error{}", traceId, e.getMessage(), e);
		}

		if (trxNewId == null)
			trxNewId = trxId;

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getNextSeqStrResponse : traceId={}|timeTaken={}|{}", traceId, timeTaken, trxNewId);
		return trxNewId;
	}

	public void lodgePaymentCancel(CancelPaymentRequest payRequest, long trxId, String traceId) throws DCPEException {

		LOGGER.info("lodgePaymentCancelRequest : traceId={}", traceId);
		Instant start = Instant.now();

		payRequest.setReqId(trxId);

		String response = restExecutor.post(cancelPayentEndpoint, traceId, payRequest, String.class);


		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("lodgePaymentCancelResponse : timeTaken={}|traceId={}|response={}", end, traceId, response);
	}

	public void lodgeOCSPayment(CancelPaymentRequest payRequest, long trxId, String traceId) throws DCPEException {

		Instant start = Instant.now();
		LOGGER.info("lodgeOCSPaymentRequest : traceId={}", traceId);

		payRequest.setReqId(trxId);

		String response = restExecutor.post(ocsPaymentEndpoint, traceId, payRequest, String.class);

		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("lodgeOCSPaymentResponse : timeTaken={}|traceId={}|response={}", end, traceId, response);
	}

	@Override
	public String getMobileOfContractId(String contractNo, String traceId) throws DCPEException {

		LOGGER.info("getMobileOfContractIdRequest : traceId={}|contractNo={}", traceId, contractNo);
		Instant start = Instant.now();
		
		String uri = getMobileOfContractIdEndpoint.replace("{contractNo}",contractNo);
		String response = restExecutor.get(uri, traceId, String.class);  
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getMobileOfContractIdResponse : traceId={}|timeTaken={}|mobileNo={}", traceId, timeTaken, response);
		return response ;		
	}
}
