package lk.dialog.pe.retrieval.cancelation.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import lk.dialog.pe.common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.dto.QueryPaymentRequest;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.retrieval.cancelation.domain.QueryPendingPaymentResponse;
import lk.dialog.pe.retrieval.cancelation.exception.QuerySystemException;
import lk.dialog.pe.retrieval.cancelation.service.CancelPaymentService;
import lk.dialog.pe.retrieval.cancelation.service.PaymentService;
import lk.dialog.pe.retrieval.cancelation.service.QueryPendingPaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private CancelPaymentService cancelPaymentService;

	@Autowired
	private QueryPendingPaymentService queryPaymentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Override
	public CancelPaymentResponse cancelPayment(CancelPaymentRequest cancelPaymentRequest, String traceId) throws DCPEException,QuerySystemException {

		String requestString = DCPEUtil.convertToString(cancelPaymentRequest);
		LOGGER.info("cancelPaymentRequest : traceId={}|CancelPaymentRequest={}", traceId, requestString);
		Instant start = Instant.now();

		CancelPaymentResponse jsonResponse = new CancelPaymentResponse();
		Long trxId = 0L;
		Integer querySystem = cancelPaymentRequest.getQuerySystem();
		
		if(querySystem != null) {
			SortedSet<Integer> enumValues = getQuerySystemEnumValues();
			if (enumValues.first() > querySystem || enumValues.last() < querySystem) {
				throw new QuerySystemException();
			}
		}else {
			throw new QuerySystemException();
		}
		
		
		try {
			cancelPayment(cancelPaymentRequest, trxId, traceId);
			jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());

			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("cancelPaymentResponse : traceId={}|timeTaken={}", traceId, timeTaken);
			return jsonResponse;
		} catch (Exception ex) {
			String message = ex.getMessage();
			LOGGER.error("Cancel Payment Error: traceId={}|error={}", traceId, message, ex);
			throw new DCPEException("Cancel Payment Error:".concat(message));
		}
	}
	@Override
	public QueryPendingPaymentResponse queryPendingPayment(QueryPaymentRequest queryPaymentRequest, String traceId)
			throws DCPEException {

		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		LOGGER.info("queryPendingPaymentRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		Instant start = Instant.now();

		QueryPendingPaymentResponse jsonResponse = null;

		try {
			jsonResponse = queryPaymentService.queryPendingPayment(queryPaymentRequest, traceId);
			jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());

			Long timeTaken = DCPEUtil.getTimeTaken(start);
			
			String responceString = DCPEUtil.convertToString(jsonResponse);
			
			LOGGER.info("queryPendingPaymentResponse : traceId={}|jsonResponse={}|timeTaken={} ", traceId,responceString,timeTaken);
			return jsonResponse;
		} catch (Exception ex) {
			String message = ex.getMessage();
			LOGGER.error("Pending Payment Error: traceId={}|error={}", traceId, message, ex);
			throw new DCPEException(message);
		}

	}


	private void cancelPayment(CancelPaymentRequest payRequest, Long trxId, String traceId) throws DCPEException {

		Long nextSeqStr = cancelPaymentService.getNextSeqStr(trxId, traceId);
		final long payTrxID = nextSeqStr;

		LOGGER.info("cancelPayment : traceId={}|payTrxID={}|SBU={}|PhysicalPaySeq={}|PC ={}|contractID={}", traceId, payTrxID, payRequest.getSbu(), payRequest.getPhysicalSeq(), payRequest.getProductCategory(), payRequest.getContractNo());
		SBUEnum sbu = SBUEnum.getSBUByValue(payRequest.getSbu());

		switch (sbu) {
		case GSM: 
			setupGSM(payRequest, payTrxID, traceId);
			break;
		
		case DTV_POSTPAID: 
			setupPOSTPAID(payRequest, payTrxID, traceId);
			break;
		
		case DBN: 
			setupDBN(payRequest, payTrxID, traceId);
			break;
		
		default:
			LOGGER.info("SBU not support for payment cancel function: traceId={}", traceId);
		}
	}

	private void setupDBN(CancelPaymentRequest payRequest, long payTrxID, String traceId) throws DCPEException {
		Optional<ProductTypeEnum> productType = ProductTypeEnum.getProductTypeById(payRequest.getProductType());
		if(productType.isPresent()) {
		switch (productType.get()) {
		case VOLTE:
		case DCS:
			cancelPaymentService.lodgePaymentCancel(payRequest, payTrxID, traceId);
			cancelPaymentService.lodgeOCSPayment(payRequest, payTrxID, traceId);
			break;
		default:
			cancelPaymentService.lodgePaymentCancel(payRequest, payTrxID, traceId);
			break;
		}}
	}
	
	private void setupPOSTPAID(CancelPaymentRequest payRequest, long payTrxID, String traceId) throws DCPEException  {
		if (payRequest.getProductType().equals(ProductTypeEnum.OTHER.getType()))
			cancelPaymentService.lodgePaymentCancel(payRequest, payTrxID, traceId);
		else
			throw new DCPEException("Product type not support for payment cancel function");

	}
	
	private void setupGSM(CancelPaymentRequest payRequest, long payTrxID, String traceId) throws DCPEException {
		try {
			if(ProductCategoryEnum.TELBIZ.valueOf().equals(payRequest.getProductCategory())) {
				payRequest.setConnRef(null);
			} else {
				String mobile = cancelPaymentService.getMobileOfContractId(payRequest.getContractNo(), traceId);
				payRequest.setConnRef(mobile);
			}
		} catch (Exception e) {
			LOGGER.info("cancelPayment : traceId={}|cause={}| msg={}", traceId, e.getCause(), e.getMessage());
		}
		if (payRequest.getProductType().equals(ProductTypeEnum.WIFI.getType()) || payRequest.getProductType().equals(ProductTypeEnum.NFC.getType())) {// PT_WIFI = 1 , 2
			cancelPaymentService.lodgePaymentCancel(payRequest, payTrxID, traceId);
		} else if (payRequest.getProductType().equals(ProductTypeEnum.OTHER.getType())) { //0
			cancelPaymentService.lodgePaymentCancel(payRequest, payTrxID, traceId);
			cancelPaymentService.lodgeOCSPayment(payRequest, payTrxID, traceId);
		} else {
			throw new DCPEException("Product type not support for payment cancel function");
		}
	}
	
	public static SortedSet<Integer> getQuerySystemEnumValues() {
		List<Integer> enumResults = new ArrayList<>();
		List<QuerySystem> result = new ArrayList<>(EnumSet.allOf(QuerySystem.class));
		for (QuerySystem querySystemEnum : result) {
			enumResults.add(querySystemEnum.valueOf());
		}
		return new TreeSet<>(enumResults.subList(0, enumResults.size()));
	}
}
