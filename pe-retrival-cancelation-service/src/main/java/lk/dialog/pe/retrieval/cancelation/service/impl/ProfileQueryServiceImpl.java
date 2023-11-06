package lk.dialog.pe.retrieval.cancelation.service.impl;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import lk.dialog.pe.common.util.Receipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.NEConstants;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.ProductTypeEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.common.util.DCPEErrorEnum;
import lk.dialog.pe.common.util.ErrorCodeEnum;
import lk.dialog.pe.retrieval.cancelation.domain.PaymentDTO;
import lk.dialog.pe.retrieval.cancelation.service.MifeIntegrationService;
import lk.dialog.pe.retrieval.cancelation.service.PersistanceIntegrationService;
import lk.dialog.pe.retrieval.cancelation.service.ProfileQueryService;
import lk.dialog.pe.retrieval.cancelation.util.Payment;
import lk.dialog.pe.retrieval.cancelation.util.ProfileQueryRequest;
import lk.dialog.pe.retrieval.cancelation.util.QueryPaymentResponse;
import lk.dialog.pe.retrieval.cancelation.util.QueryPaymentsSummery;

@Service
public class ProfileQueryServiceImpl implements ProfileQueryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileQueryServiceImpl.class);

	@Autowired
	PersistanceIntegrationService persistanceIntegrationService;

	@Autowired
	MifeIntegrationService mifeIntegrationService;
	
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssz", Locale.ENGLISH);
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public QueryPaymentResponse queryPayments(ProfileQueryRequest profileQueryRequest, String traceID) throws DCPEException {

		String request = DCPEUtil.convertToString(profileQueryRequest);
		LOGGER.info("queryPaymentsRequest : traceId={}|request={}", traceID, request);
		Instant start = Instant.now();

		QueryPaymentResponse queryResponse = new QueryPaymentResponse();

		List<Payment> paymentsList = new ArrayList<>();
		if (profileQueryRequest.getContractNo() != null && !isMatchPCAndSBU(profileQueryRequest.getProductCategory(), profileQueryRequest.getContractNo(), traceID)) {
			queryResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
			queryResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_PC_N_SBU_MISMATCH.getErrorCode());
			queryResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_PC_N_SBU_MISMATCH.getErrorCode());
			return queryResponse;
		}
		ProductCategoryEnum productCategory = ProductCategoryEnum.getProductCategoryByValue(profileQueryRequest.getProductCategory());

		if (productCategory != null) {
			switch (productCategory) {
				case NFC:
				case CCBS:
				case TELBIZ: {
					try {
						if (profileQueryRequest.getReceipts() != null) {
							for (Receipt receipt : profileQueryRequest.getReceipts()) {

								QueryPaymentsSummery queryPaymentsSummery = profileQueryRequestInput(
										profileQueryRequest, receipt.getReceiptNo());

								List<PaymentDTO> payments = persistanceIntegrationService.getPaymentsSummery(
										queryPaymentsSummery, traceID);
								String payment = DCPEUtil.convertToString(payments);
								LOGGER.info("queryPaymentsRequest : traceId={}|paymentSummeryResponse={}", traceID,
										payment);

								for (PaymentDTO source : payments) {
									Payment target = new Payment();

									BeanUtils.copyProperties(source, target);
									/*
									 * Need to filter payments only relevant to product category
									 */
									Payment paymentNewObj = null;
									String contractId = target.getContractNo();
									paymentNewObj = paymentObjByPC(profileQueryRequest, traceID, target, contractId);

									if ((profileQueryRequest.getProductCategory()
											.equals(ProductCategoryEnum.TELBIZ.valueOf()) && paymentNewObj.getSbu()
											.equals(SBUEnum.DBN.getSbu())
											&& (paymentNewObj.getProductType().equals(ProductTypeEnum.OTHER.getType())
											|| paymentNewObj.getProductType().equals(ProductTypeEnum.VOLTE.getType())
											|| paymentNewObj.getProductType().equals(ProductTypeEnum.DCS.getType())))
											|| (!profileQueryRequest.getProductCategory()
											.equals(ProductCategoryEnum.TELBIZ.valueOf()) && !paymentNewObj.getSbu()
											.equals(SBUEnum.DBN.getSbu())
											&& paymentNewObj.getProductType().equals(ProductTypeEnum.OTHER.getType()))) {
										paymentsList.add(paymentNewObj);
									} else {
										LOGGER.error("Incompatible Payments Found .PC::{}|{}|{}|{}|{} ::|traceId={}",
												profileQueryRequest.getProductCategory(), ", SBU:", paymentNewObj.getSbu(),
												", Rcpt:",
												paymentNewObj.getReceiptNo(), traceID);
									}
								}

							}

						} else {
							QueryPaymentsSummery queryPaymentsSummeryWithOutReceipts = profileQueryRequestInput(
									profileQueryRequest, null);

							List<PaymentDTO> payments = persistanceIntegrationService.getPaymentsSummery(
									queryPaymentsSummeryWithOutReceipts, traceID);

							for (PaymentDTO source : payments) {
								Payment target = new Payment();
								BeanUtils.copyProperties(source, target);
								/*
								 * Need to filter payments only relevant to product category
								 */

								Payment paymentNewObj = null;
								String contractId = target.getContractNo();
								paymentNewObj = paymentObjByPC(profileQueryRequest, traceID, target, contractId);

								if ((profileQueryRequest.getProductCategory()
										.equals(ProductCategoryEnum.TELBIZ.valueOf()) && paymentNewObj.getSbu()
										.equals(SBUEnum.DBN.getSbu())
										&& (paymentNewObj.getProductType().equals(ProductTypeEnum.OTHER.getType())
										|| paymentNewObj.getProductType().equals(ProductTypeEnum.VOLTE.getType())
										|| paymentNewObj.getProductType().equals(ProductTypeEnum.DCS.getType())))
										|| (!profileQueryRequest.getProductCategory()
										.equals(ProductCategoryEnum.TELBIZ.valueOf()) && !paymentNewObj.getSbu()
										.equals(SBUEnum.DBN.getSbu())
										&& paymentNewObj.getProductType().equals(ProductTypeEnum.OTHER.getType()))) {
									paymentsList.add(paymentNewObj);
								} else {
									LOGGER.error("Incompatible Payments Found .PC::{}|{}|{}|{}|{}|traceId={}",
											profileQueryRequest.getProductCategory(), ", SBU:", paymentNewObj.getSbu(),
											", Rcpt:",
											paymentNewObj.getReceiptNo(), traceID);
								}
							}

						}

						List<Payment> paymentsModifiedList = new ArrayList<>();
						if (profileQueryRequest.getProductCategory()
								.equals(ProductCategoryEnum.NFC.valueOf())) {
							Iterator<Payment> iteratorPayment = paymentsList.iterator();
							while (iteratorPayment.hasNext()) {
								Payment paymentDto = iteratorPayment.next();

								if (!paymentDto.getContractNo()
										.matches(Constants.NFC_CONTRACT_NO_VALIDATOR_ZEROS)) {
									iteratorPayment.remove();

								} else {
									iteratorPayment.remove();
									paymentDto.setPaymentMode(
											getPayCposModeToRBMdata(paymentDto.getPaymentMode(), traceID));

									if (paymentDto.getCreatedDtm() != null) {
										Long createdDtmLong = Long.valueOf(paymentDto.getCreatedDtm());
										String createdDtmString = simpleDateFormat.format(new Date(createdDtmLong));
										paymentDto.setCreatedDtm(createdDtmString);
									}

									if (paymentDto.getCancelledDtm() != null) {
										Long cancelledDtmLong = Long.valueOf(paymentDto.getCancelledDtm());
										String cancelledDtmString = simpleDateFormat.format(new Date(cancelledDtmLong));
										paymentDto.setCancelledDtm(cancelledDtmString);
									}

									if (paymentDto.getAccountPaymentDate() != null) {
										Long accountPaymentDateLong = Long.valueOf(paymentDto.getAccountPaymentDate());
										String accountPaymentDateString = simpleDateFormat.format(
												new Date(accountPaymentDateLong));
										paymentDto.setAccountPaymentDate(accountPaymentDateString);
									}

									if (paymentDto.getReceiptDate() != null) {
										Long receiptDateLong = Long.valueOf(paymentDto.getReceiptDate());
										String receiptDateString = simpleDateFormat.format(new Date(receiptDateLong));
										paymentDto.setReceiptDate(receiptDateString);
									}

									paymentsModifiedList.add(paymentDto);
								}

							}
						} else {
							Iterator<Payment> iteratorPayment = paymentsList.iterator();
							while (iteratorPayment.hasNext()) {
								Payment paymentDto = iteratorPayment.next();

								if (paymentDto.getContractNo().matches(Constants.NFC_CONTRACT_NO_VALIDATOR_ZEROS)) {
									iteratorPayment.remove();
								} else {
									iteratorPayment.remove();
									paymentDto.setPaymentMode(
											getPayCposModeToRBMdata(paymentDto.getPaymentMode(), traceID));

									if (paymentDto.getCreatedDtm() != null) {
										Long createdDtmLong = Long.valueOf(paymentDto.getCreatedDtm());
										String createdDtmString = simpleDateFormat.format(new Date(createdDtmLong));
										paymentDto.setCreatedDtm(createdDtmString);
									}

									if (paymentDto.getCancelledDtm() != null) {
										Long cancelledDtmLong = Long.valueOf(paymentDto.getCancelledDtm());
										String cancelledDtmString = simpleDateFormat.format(new Date(cancelledDtmLong));
										paymentDto.setCancelledDtm(cancelledDtmString);
									}

									if (paymentDto.getAccountPaymentDate() != null) {
										Long accountPaymentDateLong = Long.valueOf(paymentDto.getAccountPaymentDate());
										String accountPaymentDateString = simpleDateFormat.format(
												new Date(accountPaymentDateLong));
										paymentDto.setAccountPaymentDate(accountPaymentDateString);
									}

									if (paymentDto.getReceiptDate() != null) {
										Long receiptDateLong = Long.valueOf(paymentDto.getReceiptDate());
										String receiptDateString = simpleDateFormat.format(new Date(receiptDateLong));
										paymentDto.setReceiptDate(receiptDateString);
									}

									paymentsModifiedList.add(paymentDto);
								}
							}

						}
						String paymentsModifiedListConvertToString = DCPEUtil.convertToString(
								paymentsModifiedList);
						LOGGER.info("queryPaymentsResponse : traceId={}|paymentsModifiedList={}", traceID,
								paymentsModifiedListConvertToString);

						queryResponse.setPayments(paymentsModifiedList);
						queryResponse.setStatus(DCPEErrorEnum.SUCCESS.getStatus());
						queryResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_SUCCESS.getErrorCode());
						queryResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_SUCCESS.getErrorCode());
					} catch (Exception e) {
						queryResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
						queryResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_FAIL.getErrorCode());
						queryResponse.setErrorDesc(e.getMessage());
						LOGGER.error(traceID + Constants.ERR_LOG_CODE, e);
					}
					break;
				}
				default:
					queryResponse = getFailStatue();

			}
		}else {
			queryResponse = getFailStatue();
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String queryResponseConverted = DCPEUtil.convertToString(queryResponse);
		LOGGER.info("queryPaymentsResponse : traceId={}|timeTaken={}|QueryResponse={}", traceID, timeTaken, queryResponseConverted);

		return queryResponse;
	}

	private QueryPaymentResponse getFailStatue() {
		QueryPaymentResponse queryResponse = new QueryPaymentResponse();
		queryResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
		queryResponse.setErrorCode(ErrorCodeEnum.ERR_CODE_INVALID_PC.getErrorCode());
		queryResponse.setErrorDesc(ErrorCodeEnum.ERR_DESC_INVALID_PC.getErrorCode());
		return queryResponse;
	}
	
	private Payment paymentObjByPC(ProfileQueryRequest profileQueryRequest, String traceID, Payment target, String contractId)
			throws DCPEException {
		return  initializeSbuAndPc(contractId, target, traceID, profileQueryRequest.getProductCategory());
	}

	private QueryPaymentsSummery profileQueryRequestInput(ProfileQueryRequest profileQueryRequest, String receiptNo) {
		QueryPaymentsSummery queryPaymentsSummery = new QueryPaymentsSummery();

		if (profileQueryRequest.getReceiptFromDate() != null && !profileQueryRequest.getReceiptFromDate().toString().isEmpty()) {
		 queryPaymentsSummery.setReceiptFromDate(profileQueryRequest.getReceiptFromDate());
		}

		if (profileQueryRequest.getReceiptToDate() != null && !profileQueryRequest.getReceiptToDate().toString().isEmpty()) {
		 queryPaymentsSummery.setReceiptToDate(profileQueryRequest.getReceiptToDate());
		}

		queryPaymentsSummery.setChequeBankBranchCode(profileQueryRequest.getChequebankBranchCode());
		queryPaymentsSummery.setReceiptBranch(profileQueryRequest.getReceiptBranch());
		queryPaymentsSummery.setBranchCounter(profileQueryRequest.getBranchCounter());
		queryPaymentsSummery.setContractNo(profileQueryRequest.getContractNo());
		queryPaymentsSummery.setChequeBankCode(profileQueryRequest.getChequebankCode());
		queryPaymentsSummery.setChequeNo(profileQueryRequest.getChequeNo());
		queryPaymentsSummery.setUser(profileQueryRequest.getReceiptUser());
		queryPaymentsSummery.setReceiptNo(receiptNo);

		return queryPaymentsSummery;
	}

	private boolean isMatchPCAndSBU(int pc, String contractId, String traceId) throws DCPEException {

		if (!contractId.startsWith(NEConstants.REGEX_NFC) && !contractId.startsWith(
				NEConstants.REGEX_WIFI)) {

			String sbu = getSbuByContractId(contractId, pc, traceId);

			if (sbu.equalsIgnoreCase("DTV"))
				sbu = "DTV_POSTPAID";

			if (((pc == ProductCategoryEnum.CCBS.valueOf() || pc == ProductCategoryEnum.NFC.valueOf())
					&& SBUEnum.getSBUTypeIntfromString(sbu) == NEConstants.SBU_DBN)
					|| (pc == ProductCategoryEnum.TELBIZ.valueOf()
					&& SBUEnum.getSBUTypeIntfromString(sbu) != NEConstants.SBU_DBN)) {
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

	private boolean productCategorySBUMatcher(int pc, String sbu) {
		boolean isMatcher = false;
		if (((pc == ProductCategoryEnum.CCBS.valueOf() || pc == ProductCategoryEnum.NFC.valueOf()) && SBUEnum.getSBUTypeIntfromString(sbu) == NEConstants.SBU_DBN)
				|| (pc == ProductCategoryEnum.TELBIZ.valueOf() && SBUEnum.getSBUTypeIntfromString(sbu) != NEConstants.SBU_DBN)) {
			isMatcher = false;
		} else {
			isMatcher = true;
		}
		return isMatcher;
	}

	private Payment initializeSbuAndPc(String contractId, Payment paymentObj, String traceId, int pc)
			throws DCPEException {

		String sbuGSM = "GSM";
		String sbuWifi = "WIFI";
		String sbuDtv = "DTV";
		String sbuVolte = "VOLTE";
		String sbuDcs = "DCS";
		String sbu = null;
		Integer subNew = -1;
		Integer productCategoryNew = -1;

		if (!contractId.startsWith(NEConstants.REGEX_NFC) && !contractId.startsWith(NEConstants.REGEX_WIFI)) {
			sbu = getSbuByContractId(contractId, pc, traceId);
		} else if (contractId.startsWith(NEConstants.REGEX_NFC)) {
			sbu = sbuGSM;
		} else if (contractId.startsWith(NEConstants.REGEX_WIFI)) {
			sbu = sbuWifi;
		}

		if (sbu != null && (sbu.equalsIgnoreCase(sbuGSM) || sbu.equalsIgnoreCase(sbuWifi))) {
			subNew = 3;
			productCategoryNew = 0;
		} else if (sbu != null && sbu.equalsIgnoreCase(sbuDtv)) {
			subNew = 2;
			productCategoryNew = 0;
		} else if (sbu != null && sbu.equalsIgnoreCase(sbuVolte)) {
			subNew = 4;
			productCategoryNew = 5;
		} else if (sbu != null && sbu.equalsIgnoreCase(sbuDcs)) {
			subNew = 4;
			productCategoryNew = 6;
		} else {
			subNew = 4;
			productCategoryNew = 0;
		}

		paymentObj.setSbu(subNew);
		paymentObj.setProductType(productCategoryNew);

		return paymentObj;

	}

	// cached request in the prev pe ,data only available in cpos db
	private String getPayCposModeToRBMdata(String id, String traceId) throws DCPEException {

		return persistanceIntegrationService.getPayCposModeToRBMdata(id, traceId);
	}

	// only available with ccbs db
	private String getSbuByContractId(String contractId, int pc, String traceId)
			throws DCPEException {

			if (pc != ProductCategoryEnum.NFC.valueOf()) {
				return contractIdValidator(contractId, traceId);
			} else {
				return "GSM";
			}	

	}

	private String contractIdValidator(String contractId, String traceId) throws DCPEException {
		String contractIdValidator = null;
		if (contractId.matches(Constants.INTEGER_VALIDATOR)) {
			contractIdValidator = persistanceIntegrationService.findContractSubsidiaryTypeById(contractId, false, traceId);
		} else {
			contractIdValidator =  persistanceIntegrationService.findContractSubsidiaryTypeById(contractId, true, traceId);
		}
		return contractIdValidator;
	}
}
