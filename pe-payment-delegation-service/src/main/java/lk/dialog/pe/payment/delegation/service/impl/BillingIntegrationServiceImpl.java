package lk.dialog.pe.payment.delegation.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.PaymentPostResponse;
import lk.dialog.pe.common.util.RestExecutor;
import lk.dialog.pe.common.util.ChequeRealizeResponse;
import lk.dialog.pe.common.util.ConnectionStatusEnum;
import lk.dialog.pe.payment.delegation.dto.ChequeRealizeRequest;
import lk.dialog.pe.payment.delegation.exception.ConnectionStatusException;
import lk.dialog.pe.payment.delegation.exception.PaymentPostException;
import lk.dialog.pe.payment.delegation.service.BillingIntegrationService;

@Service
public class BillingIntegrationServiceImpl implements BillingIntegrationService {

	@Autowired
	private RestExecutor restExecutor;
	
	@Value("${api.pe-cheque-payment-host.forcefulChqRealize}")
	private String forcefulRealizeChequesEndpoint;
	
	@Value("${api.pe-cheque-payment-host.postPayment}")
	private String postChequePaymentEndpoint;
	
	@Value("${api.pe-other-payment-host.postPayment}")
	private String postOtherPaymentEndpoint;
		
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingIntegrationServiceImpl.class);

	@Override
	public ChequeRealizeResponse forcefulChqRealize(ChequeRealizeRequest chequeRealizeRequest, String traceId)
			throws DCPEException,ConnectionStatusException{
		Instant start = Instant.now();
		String requestString = DCPEUtil.convertToString(chequeRealizeRequest);
		LOGGER.info("forcefulChqRealizeRequest : traceId={}|requestBody={}", traceId, requestString);
		
		Integer connectionStatus = chequeRealizeRequest.getConnectionStatus();
		
		if(connectionStatus != null) {
			SortedSet<Integer> enumValues = getConnectionStatusEnumValues();
			if (enumValues.first() > connectionStatus || enumValues.last() < connectionStatus) {
				throw new ConnectionStatusException();
			}
		}else {
			throw new ConnectionStatusException();
		}
		
		ChequeRealizeResponse response = restExecutor.post(forcefulRealizeChequesEndpoint, traceId, chequeRealizeRequest, ChequeRealizeResponse.class);  
		
		String responseString = DCPEUtil.convertToString(response);
		Long end = Duration.between(start, Instant.now()).toMillis();
		LOGGER.info("forcefulChqRealizeResponse : timeTaken={}|traceId={}|response={}", end, traceId, responseString);
		return response;
	}

	@Override
	public PaymentPostResponse postChequePayment(PaymentPostRequest jsonReq, String traceId)throws DCPEException {
		
		Instant start = Instant.now();
		LOGGER.info("getPostChequePaymentRequest : traceId={}|payamentRequest={}", traceId,jsonReq);
		
		PaymentPostResponse response = restExecutor.post(postChequePaymentEndpoint,traceId,jsonReq,PaymentPostResponse.class);
		
		String responseString = DCPEUtil.convertToString(response);
		Long end = Duration.between(start, Instant.now()).toMillis();
		
		LOGGER.info("getPostChequePaymentResponse : timeTaken={}|traceId={}| body={}", end, traceId, responseString);
		return response;
	}

	@Override
	public PaymentPostResponse postOtherPayment(PaymentPostRequest jsonReq, String traceId)throws DCPEException,PaymentPostException{
		
		Instant start = Instant.now();
		LOGGER.info("getPostOtherPaymentRequest : traceId={}|payamentRequest={}", traceId,jsonReq);
		
		PaymentPostResponse response = null;
		try {
			response = restExecutor.post(postOtherPaymentEndpoint,traceId,jsonReq,PaymentPostResponse.class);
		} catch (Exception e) {
			if (e.getMessage() == null) {
				throw new PaymentPostException();
			}else {
				throw new DCPEException(e.getMessage());
			}
		}
		
		
		String responseString = DCPEUtil.convertToString(response);
		Long end = Duration.between(start, Instant.now()).toMillis();
		
		LOGGER.info("getPostOtherPaymentResponse : timeTaken={}|traceId={}| body={}", end, traceId, responseString);
		return response;
	}
	
	public static SortedSet<Integer> getConnectionStatusEnumValues() {
		List<Integer> enumResults = new ArrayList<>();
		List<ConnectionStatusEnum> result = new ArrayList<>(EnumSet.allOf(ConnectionStatusEnum.class));
		for (ConnectionStatusEnum connectionStatusEnum : result) {
			enumResults.add(connectionStatusEnum.getValue());
		}
		return new TreeSet<>(enumResults.subList(0, enumResults.size()));
	}
	
}
