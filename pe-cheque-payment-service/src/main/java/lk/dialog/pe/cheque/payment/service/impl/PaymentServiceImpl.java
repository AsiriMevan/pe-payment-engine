package lk.dialog.pe.cheque.payment.service.impl;
import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.util.ChequeRealizeRequest;
import lk.dialog.pe.cheque.payment.service.PaymentService;
import lk.dialog.pe.cheque.payment.service.PersisitenceIntegrationService;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.ChequeRealizeResponse;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.DCPEErrorEnum;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.ErrorCodeEnum;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.PaymentPostResponse;
import lk.dialog.pe.common.util.ProductTypeEnum;
import lk.dialog.pe.common.util.Receipt;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.common.util.ProductCategoryEnum;

@Service
public class PaymentServiceImpl implements PaymentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentServiceImpl.class);

	@Autowired
	private PersisitenceIntegrationService persisitenceIntegrationService;
//todo - PEException not thrown
	@Override
	public PaymentPostResponse postPayment(PaymentPostRequest jsonReq, String traceId) throws PEException, DCPEException {

		String requestString = DCPEUtil.convertToString(jsonReq);
		LOGGER.info("postPaymentRequest : traceId={}|PostPayment={}", traceId, requestString);
		Instant start = Instant.now();

		PaymentPostResponse jsonResponse = new PaymentPostResponse();

		try {

			long paySeq = insertPayment(jsonReq, traceId);
			jsonResponse.setPaymentSeq(paySeq);
			jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());

			Long timeTaken = DCPEUtil.getTimeTaken(start);
			LOGGER.info("postPaymenResponse : traceId={}|timeTaken={}", traceId, timeTaken);

			return jsonResponse;
		} catch (Exception ex) {
			//todo - where is exception logs
			throw new DCPEException(ex.getMessage());
		}
	}

	public long insertPayment(PaymentPostRequest payRequest, String traceId) throws PEException, DCPEException {

		/* get payemnt next sequence value */

		long nextSeqStr = -1l;
		try {
			nextSeqStr = persisitenceIntegrationService.nextPaymentSequence(traceId);
		} catch (PEException e) {
			//todo - this haven't being used
			LOGGER.error("Get payment next sequence value PE Error: traceId={}|errorMessage={}", traceId, e.getMessage());
		} catch (DCPEException ex) {
			LOGGER.error("Get payment next sequence value DCPE Error: traceId={}|errorMessage={}", traceId, ex.getMessage());
		}
		long payTrxID = -1l;

		int chargeType = payRequest.getAccountType();
		String payMode = payRequest.getPaymentMode();
		String chqSuspend = payRequest.getChqSuspend();

		LOGGER.info("Insert Sequence :nextSeqStr={}|payTrxID={}|ConnRef={}|SBU={}|ChargeType={}|PayMode={}|ChqSuspend={}|traceId={}", nextSeqStr, payTrxID, payRequest.getConnRef(),
				payRequest.getSbu(), chargeType, payMode, chqSuspend, traceId);
		ProductCategoryEnum productCategory = ProductCategoryEnum.getProductCategoryByValue(payRequest.getProductCategory());
		
		switch (productCategory) {
			//todo - done - new telbiz case to be added - [pe-persistance/src/main/java/lk/dialog/crm/pe/persistence/dao/impl/PaymentDAOImpl.java:143]
		case CCBS:// 1
			if (chargeType == Constants.POSTPAID) {// 2
				switch (SBUEnum.getSBUByValue(payRequest.getSbu())) {
				case GSM:// 3
					payTrxID = setupSbuGSM(chqSuspend, nextSeqStr, payRequest, traceId);
					break;
				case DTV_POSTPAID:// 2
					payTrxID = setupSbuDTVPost(chqSuspend, nextSeqStr, payRequest, traceId);
					break;
				default:
					//todo - no logs
					throw new PEException("SBU not support for payment post function");
				}
			} else {
				throw new DCPEException("Only postpaid payments support");
			}
			break;
		case TELBIZ:
			switch (payRequest.getProductType()) {
				case Constants.PT_VOLTE:
				case Constants.PT_DCS:
					payTrxID = setupPtVolteDcs(chargeType, chqSuspend, nextSeqStr, payRequest, traceId);
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
			payTrxID = setupSbuPCNFCProductCategory(chargeType, chqSuspend, nextSeqStr, payRequest, traceId);
			break;
		default:
			throw new PEException("Invalid product category");
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

	private long setupPtVolteDcs(int chargeType, String chqSuspend, long nextSeqStr, PaymentPostRequest payRequest, String traceId) throws PEException, DCPEException {
		long payTrxID;
		if (chargeType == Constants.POSTPAID) {
			LOGGER.info("setupPtVolteDcs - chargeType is POSTPAID | traceId=[{}]", traceId);
			/* If the product Type is DCS(6), Check payments should be realized forcefully whether the value of "chqSuspend" is CREDIT or SUSPEND
			03/07/2019, Updated by Chintha
			 */
			if (Constants.CHQ_MODE_CREDIT.equalsIgnoreCase(chqSuspend) || payRequest.getProductType().equals(Constants.PT_DCS)) {
				/* Chq forceful realization */
				LOGGER.info("setupPtVolteDcs - Chq forceful realization | traceId=[{}]", traceId);
				payTrxID = nextSeqStr;
				persisitenceIntegrationService.lodgeChqForecefulRealize(payRequest, payTrxID, traceId,true);
				persisitenceIntegrationService.lodgeOCSPayment(payRequest, payTrxID, traceId);
			} else {
				/* suspend chq payments */
				LOGGER.info("setupPtVolteDcs - Suspend chq payments | traceId=[{}]", traceId);
				payTrxID = nextSeqStr;
				persisitenceIntegrationService.lodgeRBMPayment(payRequest, payTrxID, traceId);
			}
		} else {
			throw new PEException("Only postpaid payments support");
		}
		return payTrxID;
	}

	private long setupSbuGSM(String chqSuspend, long nextSeqStr, PaymentPostRequest payRequest, String traceId) throws DCPEException, PEException {
		long payTrxID;
		final String cheqSuspend = Constants.CHQ_MODE_CREDIT;
		if (cheqSuspend.equalsIgnoreCase(chqSuspend)) {
			payTrxID = nextSeqStr;
			if (payRequest.getProductType().equals(ProductTypeEnum.WIFI.getType()) || payRequest.getProductType().equals(ProductTypeEnum.NFC.getType())) {
				persisitenceIntegrationService.lodgeChqForecefulRealize(payRequest, payTrxID, traceId,true);
			} else if (payRequest.getProductType().equals(ProductTypeEnum.OTHER.getType())) {
				if (payRequest.getConnRef().startsWith("7")) {
					persisitenceIntegrationService.lodgeOCSPayment(payRequest, payTrxID, traceId);
				}
				persisitenceIntegrationService.lodgeChqForecefulRealize(payRequest, payTrxID, traceId,true);
			} else {
				throw new PEException("insertPayment : Product type not support for payment post function");
			}
		} else {
			/* suspend chq payments */
			if (payRequest.getProductType().equals(ProductTypeEnum.WIFI.getType()) || payRequest.getProductType().equals(ProductTypeEnum.NFC.getType()) || payRequest.getProductType().equals(ProductTypeEnum.OTHER.getType())) {
				payTrxID = nextSeqStr;
				persisitenceIntegrationService.lodgeRBMPayment(payRequest, payTrxID, traceId);
			} else {
				throw new PEException("Product type not support for payment post function");
			}	
		}
		return payTrxID;
	}

	private long setupSbuPCNFCProductCategory(int chargeType, String chqSuspend, long nextSeqStr, PaymentPostRequest payRequest, String traceId) throws DCPEException, PEException {
		long payTrxID;
		if (chargeType == Constants.POSTPAID) {
			//todo - can remove this variable
			final String cheqSuspend = Constants.CHQ_MODE_CREDIT;
			if (cheqSuspend.equalsIgnoreCase(chqSuspend)) {
				//todo - no logs
				/* Chq forceful realization */
				payTrxID = nextSeqStr;
				persisitenceIntegrationService.lodgeChqForecefulRealize(payRequest, payTrxID, traceId,true); // working well PC and SBU only be 3 PT only be 2
			} else {
				//todo - no logs
				/* suspend chq payments */
				payTrxID = nextSeqStr;
				persisitenceIntegrationService.lodgeRBMPayment(payRequest, payTrxID, traceId);// working well PC and SBU only be 3 PT only be 2
			}
		} else {
			throw new PEException("Only postpaid payments support");
		}
		return payTrxID;
	}

	private long setupSbuDTVPost(String chqSuspend, long nextSeqStr, PaymentPostRequest payRequest, String traceId) throws DCPEException, PEException {
		long payTrxID;
		//todo - enum should be at start
		if (payRequest.getProductType().equals(ProductTypeEnum.OTHER.getType())) {

			final String cheqSuspend = Constants.CHQ_MODE_SUSPEND;

			if (cheqSuspend.equalsIgnoreCase(chqSuspend)) {
				//todo - no logs
				/* Chq forceful realization */
				payTrxID = nextSeqStr;
				persisitenceIntegrationService.lodgeChqForecefulRealize(payRequest, nextSeqStr, traceId,true);// working well save data correct table
			} else {
				//todo - no logs
				/* suspend chq payments */
				payTrxID = nextSeqStr;
				persisitenceIntegrationService.lodgeRBMPayment(payRequest, payTrxID, traceId); //
			}
		} else {
			//todo - no logs
			throw new PEException("Product type not support for payment post function");
		}
		LOGGER.info("setupSbuDTVPost : traceId={}|payTrxID={}|nextSeqStr={}", traceId, payTrxID, nextSeqStr);
		return payTrxID;
	}

	@Override
	public ChequeRealizeResponse forcefulChequeRealize(ChequeRealizeRequest jsonReq, String traceId) throws PEException, DCPEException {
		String requestString = DCPEUtil.convertToString(jsonReq);
		LOGGER.info("forcefulChequeRealizeRequest : traceId={}|ForcefulRealizeCheques={}", traceId, requestString);
		Instant start = Instant.now();

		ChequeRealizeResponse jsonResponse = new ChequeRealizeResponse();
		try {
			jsonResponse = realizeCheque(jsonReq, traceId);
			jsonResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
			jsonResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
			jsonResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());

			Long timeTaken = DCPEUtil.getTimeTaken(start);
			String responseString = DCPEUtil.convertToString(jsonResponse);
			LOGGER.info("forcefulChequeRealizeResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, responseString);
			return jsonResponse;
		} catch (Exception ex) {
			throw new PEException(ex.getMessage());
		}
	}

	private ChequeRealizeResponse realizeCheque(ChequeRealizeRequest chqRequest, String traceId) throws PEException, DCPEException {

		List<Receipt> receiptsINo = chqRequest.getReceiptsInfo();

		ChequeRealizeResponse jsonResponse = new ChequeRealizeResponse();
		if (receiptsINo != null) {
			for (Receipt receipt : receiptsINo) {
				jsonResponse = this.realizeChequeReceipt(receipt,chqRequest, jsonResponse, traceId);
			}
		} else {
			throw new PEException("No rececipts details found");
		}
		return jsonResponse;
	}


	private ChequeRealizeResponse realizeChequeReceipt (Receipt receipt, ChequeRealizeRequest chqRequest, ChequeRealizeResponse jsonResponse, String traceId) throws PEException, DCPEException {
		long nextSeqStr = -1l;
		try {
			nextSeqStr = persisitenceIntegrationService.nextPaymentSequence(traceId);
		} catch (PEException e) {

			throw new PEException(e.getMessage());
		} catch (DCPEException e) {

			throw new DCPEException(e.getMessage());
		}

		LOGGER.info("RealizeChq Received : ChqNo={}|ChqBank={}|ChqBranch={}|ReceiptNo={}|traceId={} :", chqRequest.getChqNo(), chqRequest.getChqBank(), chqRequest.getChqBranch(),
				receipt.getReceiptNo(), traceId);

		PaymentPostRequest payRequest = new PaymentPostRequest();
		setupPaymentPost(traceId, chqRequest, payRequest, receipt);
		switch (SBUEnum.getSBUByValue(payRequest.getSbu())) {
			case GSM:
			case DBN: {
				jsonResponse = sbuDBNsupportForPaymentPost(payRequest, nextSeqStr, traceId);
				break;
			}
			case DTV_POSTPAID: {
				final long payTrxID = nextSeqStr;
				if (payRequest.getProductType().equals(ProductTypeEnum.OTHER.getType())) {
					persisitenceIntegrationService.lodgeChqForecefulRealize(payRequest, payTrxID, traceId,false);
				} else {

					throw new PEException("realizeCheque : Product type not support for payment post function");

				}
				break;
			}
			default:

				LOGGER.info("SBUEnum not support for realizeCheque function: traceId={}", traceId);
				throw new PEException("SBUEnum not support for realizeCheque function");

		}
		return jsonResponse;
	}

	private ChequeRealizeResponse sbuDBNsupportForPaymentPost(PaymentPostRequest payRequest, long nextSeqStr, String traceId) throws PEException, DCPEException {
		String requestString = DCPEUtil.convertToString(payRequest);
		LOGGER.info("sbuDBNsupportForPaymentPost : traceId={}|payRequest={}", traceId, requestString);
		ChequeRealizeResponse jsonResponse = new ChequeRealizeResponse();

		final long payTrxID = nextSeqStr;
		if (payRequest.getProductType().equals(ProductTypeEnum.WIFI.getType()) || payRequest.getProductType().equals(ProductTypeEnum.NFC.getType())) {
			persisitenceIntegrationService.lodgeChqForecefulRealize(payRequest, payTrxID, traceId,false);
		} else if (payRequest.getProductType().equals(ProductTypeEnum.OTHER.getType()) || payRequest.getProductType().equals(ProductTypeEnum.VOLTE.getType())
				|| payRequest.getProductType().equals(ProductTypeEnum.DCS.getType())) {

			persisitenceIntegrationService.lodgeOCSPayment(payRequest, payTrxID, traceId);
			persisitenceIntegrationService.lodgeChqForecefulRealize(payRequest, payTrxID, traceId,false);

		} else {
			throw new PEException("realizeCheque else : Product type not support for payment post function");
		}

		return jsonResponse;
	}

	private PaymentPostRequest setupPaymentPost(String traceId, ChequeRealizeRequest chqRequest, PaymentPostRequest payRequest, Receipt receipt) {

		String receiptRequestString = DCPEUtil.convertToString(receipt);
		LOGGER.info("setupPaymentPost : traceId={}|chqRequest={}", traceId, receiptRequestString);

		payRequest.setProductCategory(chqRequest.getProductCategory());
		payRequest.setChqNo(chqRequest.getChqNo());
		payRequest.setChqBank(chqRequest.getChqBank());
		payRequest.setChqBranch(chqRequest.getChqBranch());
		payRequest.setSbu(SBUEnum.getSBUType(receipt.getSbu()));
		payRequest.setAccountNo(receipt.getAccountNo());
		payRequest.setContractNo(receipt.getContractNo());
		payRequest.setPaymentAmount(receipt.getPaymentAmount());
		payRequest.setReceiptNo(receipt.getReceiptNo());
		payRequest.setReceiptBranch(receipt.getReceiptBranch());
		payRequest.setBranchCounter(receipt.getBranchCounter());
		payRequest.setReceiptDate(receipt.getReceiptDate());
		payRequest.setReceiptUser(receipt.getReceiptUser());
		payRequest.setProductType(receipt.getProductType());
		payRequest.setPhysicalPaymentDate(chqRequest.getPhysicalPaymentDate());
		payRequest.setConnRef(receipt.getConnRef());
		payRequest.setConnectionStatus(chqRequest.getConnectionStatus());
		String payRequestString = DCPEUtil.convertToString(payRequest);

		LOGGER.info("setupPaymentPost : traceId={}|payRequest={}", traceId, payRequestString);
		return payRequest;
	}

}
