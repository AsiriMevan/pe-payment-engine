package lk.dialog.pe.other.payment.service.impl;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.ErrorCodeEnum;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.PaymentPostResponse;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.common.util.ProductTypeEnum;
import lk.dialog.pe.common.util.DCPEErrorEnum;
import lk.dialog.pe.other.payment.service.PaymentService;
import lk.dialog.pe.other.payment.service.PersisitenceIntegrationService;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
	private PersisitenceIntegrationService persisitenceIntegrationService;
//todo - not throw any pe exception
	@Override
	public PaymentPostResponse postPayment(PaymentPostRequest jsonReq, String traceId) throws PEException, DCPEException {

		String requestString = DCPEUtil.convertToString(jsonReq);
		LOGGER.info("postOtherPaymentRequest : traceId={}|PostPayment={}", traceId, requestString);
		Instant start = Instant.now();

		PaymentPostResponse jsonResponse = new PaymentPostResponse();

		try {

			long paySeq = insertOtherPayment(jsonReq, traceId);

			jsonResponse.setPaymentSeq(paySeq);
			jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());

			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("postOtherPaymentResponse : traceId={}|timeTaken={}", traceId, timeTaken);

			return jsonResponse;
		} catch (Exception ex) {

			LOGGER.error("Post Other Payment Error: traceId={}|errorMessage={}", traceId, ex.getMessage());
			throw new DCPEException(ex.getMessage());
		}

	}
//todo - not catch pe exception in higher level
	private long insertOtherPayment(PaymentPostRequest payRequest, String traceId) throws PEException, DCPEException {

		/* get payment next sequence value */

		long nextSeqStr = -1l;
		try {
			nextSeqStr = persisitenceIntegrationService.nextPaymentSequence(traceId);
		} catch (PEException e) {
			//todo - this is never thrown
			LOGGER.error("Get payment next sequence value PE Error: traceId={}|errorMessage={}", traceId, e.getMessage());
		} catch (DCPEException ex) {
			LOGGER.error("Get payment next sequence value DCPE Error: traceId={}|errorMessage={}", traceId, ex.getMessage());

		}
		long payTrxID = -1l;

		int chargeType = payRequest.getAccountType();
		String payMode = payRequest.getPaymentMode();
		String chqSuspend = payRequest.getChqSuspend();

		LOGGER.info("insertOtherPayment : ConnRef={}|SBU={}|ChargeTYpe={}|PayMode={}|ChqSuspend={}|traceId={}", payRequest.getConnRef(), payRequest.getSbu(), chargeType, payMode, chqSuspend, traceId);
		ProductCategoryEnum productCategory = ProductCategoryEnum.getProductCategoryByValue(payRequest.getProductCategory());
		switch (productCategory) {
			//todo - done - new telbiz case to be added - [pe-persistance/src/main/java/lk/dialog/crm/pe/persistence/dao/impl/PaymentDAOImpl.java:143]
			//todo - indentation is wrong
		case CCBS:// 1
			payTrxID = setupCCBSProductCategory(chargeType, payRequest, payMode, payTrxID, nextSeqStr, traceId);

			break;

			case TELBIZ:
				switch (payRequest.getProductType()) {
					case Constants.PT_VOLTE:
					case Constants.PT_DCS:
						payTrxID = setupPtVolteDcs(chargeType, nextSeqStr, payRequest, traceId);
						break;
					case Constants.PT_OTHER:
					case Constants.PT_LTE:
					case Constants.PT_WIFI:
					case Constants.PT_CDMA:
						payTrxID = setupPtNonVolteDcs(chargeType, nextSeqStr, payRequest, traceId);
						break;
					default:
						throw new PEException("Invalid Product Type");
				}
				break;
			case NFC:// 3
			if (chargeType == Constants.POSTPAID) {
				//todo - no use of this condition
				if (!Constants.PAY_MODE_CHQ.equalsIgnoreCase(payMode)) { // != CHE [CA,...]
					/* not cheque payments */
					payTrxID = nextSeqStr;
					persisitenceIntegrationService.lodgeRBMPayment(payRequest, payTrxID, traceId);
				}
			} else {
				LOGGER.error("insertOtherPayment Error: traceId={}|errorMessage={}", traceId, ErrorCodeEnum.ERR_DESC_ONLY_POSTPAID_PAYMENT_SUPPORT.getErrorCode());
				throw new PEException(ErrorCodeEnum.ERR_DESC_ONLY_POSTPAID_PAYMENT_SUPPORT.getErrorCode());
			}
			break;
		default:
			LOGGER.error("insertOtherPayment Error: traceId={}|errorMessage={}", traceId, ErrorCodeEnum.ERR_DESC_INVALID_PRODUCT_CATEGORY.getErrorCode());
			throw new PEException(ErrorCodeEnum.ERR_DESC_INVALID_PRODUCT_CATEGORY.getErrorCode());
		}
		return payTrxID;
	}

	private long setupPtNonVolteDcs(int chargeType, long nextSeqStr, PaymentPostRequest payRequest, String traceId) throws PEException, DCPEException {
		long payTrxID;
		if (chargeType == Constants.POSTPAID) {
			LOGGER.info("setupPtNonVolteDcs - chargeType is POSTPAID | traceId=[{}]", traceId);
			payTrxID = nextSeqStr;
			persisitenceIntegrationService.lodgeDBNPayment(payRequest, payTrxID, traceId);
		} else {
			throw new PEException("Only postpaid payments support");
		}
		return payTrxID;
	}

	private long setupPtVolteDcs(int chargeType, long nextSeqStr, PaymentPostRequest payRequest, String traceId) throws PEException, DCPEException {
		long payTrxID;
		if (chargeType == Constants.POSTPAID) {
			LOGGER.info("setupPtVolteDcs - chargeType is POSTPAID | traceId=[{}]", traceId);
			payTrxID = nextSeqStr;
			persisitenceIntegrationService.lodgeRBMPayment(payRequest, payTrxID, traceId);
			persisitenceIntegrationService.lodgeOCSPayment(payRequest, payTrxID, traceId);
		} else {
			throw new PEException("Only postpaid payments support");
		}
		return payTrxID;
	}

	private long setupCCBSProductCategory(int chargeType, PaymentPostRequest payRequest, String payMode, long payTrxID, long nextSeqStr, String traceId) throws DCPEException, PEException {
		if (chargeType == Constants.POSTPAID) {// AccountType = 2
			switch (SBUEnum.getSBUByValue(payRequest.getSbu())) {
			case GSM:// 3
				payTrxID = cheqPaymode(payRequest, payMode, payTrxID, nextSeqStr, traceId);
				break;
			case DTV_POSTPAID: // 2
				final String paymentPost = Constants.PAY_MODE_CHQ;
				//todo - no use of this if
				if (!paymentPost.equalsIgnoreCase(payMode)) {
					/* not cheque payments */
					if (payRequest.getProductType().equals(ProductTypeEnum.OTHER.getType())) {
						payTrxID = nextSeqStr;
						persisitenceIntegrationService.lodgeRBMPayment(payRequest, payTrxID, traceId);
					} else {
						LOGGER.error("setupCCBSProductcategory Error: traceId={}|errorMessage={}", traceId, ErrorCodeEnum.ERR_DESC_PRODUCT_TYPE_NOT_SUPPORT.getErrorCode());
						throw new PEException(ErrorCodeEnum.ERR_DESC_PRODUCT_TYPE_NOT_SUPPORT.getErrorCode());
					}
				}
				break;
			default: {
				LOGGER.error("setupCCBSProductcategory Error: traceId={}|errorMessage={}", traceId, ErrorCodeEnum.ERR_DESC_SBU_NOT_SUPPORT.getErrorCode());
				throw new PEException(ErrorCodeEnum.ERR_DESC_SBU_NOT_SUPPORT.getErrorCode());
			}
			}

		} else {
			LOGGER.error("setupCCBSProductCategory Error: traceId={}|errorMessage={}", traceId, ErrorCodeEnum.ERR_DESC_ONLY_POSTPAID_PAYMENT_SUPPORT.getErrorCode());
			throw new PEException(ErrorCodeEnum.ERR_DESC_ONLY_POSTPAID_PAYMENT_SUPPORT.getErrorCode());
		}
		return payTrxID;
	}

	//todo - incorrect method name
	private long cheqPaymode(PaymentPostRequest payRequest, String payMode, long payTrxID, long nextSeqStr, String traceId) throws DCPEException, PEException {
		
		final String paymentPost = Constants.PAY_MODE_CHQ;
		//todo - no use of this if
		if (!paymentPost.equalsIgnoreCase(payMode)) {// !=CHE
			/* not cheque payments */
			payTrxID = nextSeqStr;
			if (payRequest.getProductType().equals(ProductTypeEnum.WIFI.getType()) || payRequest.getProductType().equals(ProductTypeEnum.NFC.getType())) { // 1 // 2
				persisitenceIntegrationService.lodgeRBMPayment(payRequest, payTrxID, traceId);
			} else if (payRequest.getProductType().equals(ProductTypeEnum.OTHER.getType())) {
				persisitenceIntegrationService.lodgeRBMPayment(payRequest, payTrxID, traceId);
				if (payRequest.getConnRef().startsWith("7")) {
					persisitenceIntegrationService.lodgeOCSPayment(payRequest, payTrxID, traceId);
				}
			} else {
				LOGGER.error("setupCCBSProductCategory Error: traceId={}|errorMessage={}", traceId, ErrorCodeEnum.ERR_DESC_PRODUCT_TYPE_NOT_SUPPORT.getErrorCode());
				throw new PEException(ErrorCodeEnum.ERR_DESC_PRODUCT_TYPE_NOT_SUPPORT.getErrorCode());
			}
		}
		return payTrxID;
	}

}
