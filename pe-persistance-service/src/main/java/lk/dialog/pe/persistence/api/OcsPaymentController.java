package lk.dialog.pe.persistence.api;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.dto.OcsPaymentDto;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PayModeEnum;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.common.util.AccountTypeEnum;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.persistence.domain.OcsPayment;
import lk.dialog.pe.persistence.service.OcsPaymentService;
import lk.dialog.pe.persistence.util.OcsTranTypeEnum;

@RestController
@RequestMapping("/ocs-payment")
public class OcsPaymentController {

	@Autowired
	private OcsPaymentService ocsPaymentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(OcsPaymentController.class);

	/**
	 * Create new ocs -payment for the request
	 * 
	 * @param ocsPaymentDto
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/save-ocs-payment")
	@ApiOperation(value = "Create new ocs -payment for the request.", notes = "The request parameter traceId  and body code are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the ocs-payment to be saved", name = "ocsPaymentDto", paramType = "body", dataType = "OcsPaymentDto", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> saveOcsPayment(@RequestBody OcsPaymentDto ocsPaymentDto, @RequestParam String traceId) throws DCPEException {

		String requestString = DCPEUtil.convertToString(ocsPaymentDto);
		Instant start = Instant.now();
		LOGGER.info("saveOcsPaymentRequest : traceId={}|ocsPaymentDto={}", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("sendOcsPaymentResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(), DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}

		Object status = ocsPaymentService.saveOcsPayment(dtoToDamainsaveOcsPayment(ocsPaymentDto, traceId), traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("sendOcsPaymentResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, status);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, status));
	}

	/**
	 * Create cancel-ocs-payment for the request
	 * 
	 * @param ocsPaymentDto
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/cancel-ocs-payment")
	@ApiOperation(value = "Create cancel-ocs-payment for the request.", notes = "The request body code  and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the cancel-ocs-payment to be saved", name = "ocsPaymentDto", paramType = "body", dataType = "OcsPaymentDto", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> cancelOcsPayment(@RequestBody OcsPaymentDto ocsPaymentDto, @RequestParam String traceId) throws DCPEException {

		String requestString = DCPEUtil.convertToString(ocsPaymentDto);
		Instant start = Instant.now();
		LOGGER.info("cancelOcsPaymentRequest : traceId={}|ocsPayment={}", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("cancelOcsPaymentResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(), DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}

		Object status = ocsPaymentService.saveOcsPayment(dtoToDamainCancelOcsPayment(ocsPaymentDto, traceId), traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("cancelOcsPaymentResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, status);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, status));
	}

	/**
	 * 
	 * @param ocsPaymentDto
	 * @param traceId
	 * @return OcsPayment
	 */
	private OcsPayment dtoToDamainsaveOcsPayment(OcsPaymentDto ocsPaymentDto, String traceId) {
		String ocsPaymentDtoConvertToString = DCPEUtil.convertToString(ocsPaymentDto);
		LOGGER.info("dtoToDamainsaveOcsPayment : body={}|traceId={}", ocsPaymentDtoConvertToString, traceId);

		OcsPayment ocsPayment = new OcsPayment();

		ocsPayment.setReqId(ocsPaymentDto.getReqId());
		ocsPayment.setAccountNo(ocsPaymentDto.getAccountNo());
		ocsPayment.setContractNo(ocsPaymentDto.getContractNo());
		ocsPayment.setConnRef(ocsPaymentDto.getConnRef());

		Optional<ProductCategoryEnum> productCategory = ProductCategoryEnum.getProductCategory(Integer.parseInt(ocsPaymentDto.getProductCategory()));
		if (productCategory.isPresent()) {
			ocsPayment.setProductCategory(productCategory.get().name());
		}

		Optional<SBUEnum> sbu = SBUEnum.getSBUByValue(ocsPaymentDto.getSbu());
		if (sbu.isPresent()) {
			ocsPayment.setSbu(sbu.get().name());
		}

		Optional<AccountTypeEnum> accountTypeEnum = Optional.ofNullable(AccountTypeEnum.getAccountType(ocsPaymentDto.getAccountType()));
		if (accountTypeEnum.isPresent()) {
			ocsPayment.setAccountType(accountTypeEnum.get().name());
		}

		if (ocsPaymentDto.getPhysicalPaymentDate() != null) {
			ocsPayment.setPhysicalPaymentDate(ocsPaymentDto.getPhysicalPaymentDate());
		}

		if (ocsPaymentDto.getPaymentAmount() != null) {
			ocsPayment.setPaymentAmount(ocsPaymentDto.getPaymentAmount());
		}

		String paymentRef;
		if (PayModeEnum.PAY_MODE_CARD.getPayMode().equalsIgnoreCase(ocsPaymentDto.getPaymentMode())) {
			paymentRef = ocsPaymentDto.getCardNo();
		} else if (PayModeEnum.PAY_MODE_CHQ.getPayMode().equalsIgnoreCase(ocsPaymentDto.getPaymentMode())) {
			paymentRef = ocsPaymentDto.getChqBank() + ocsPaymentDto.getChqBranch() + ocsPaymentDto.getChqNo();
		} else {
			paymentRef = ocsPaymentDto.getPaymentRef();
		}
		ocsPayment.setPaymentRef(paymentRef);

		if (ocsPaymentDto.getReceiptNo() != null) {
			ocsPayment.setReceiptNo(ocsPaymentDto.getReceiptNo());
		}

		ocsPayment.setReceiptBranch(ocsPaymentDto.getReceiptBranch());
		ocsPayment.setBranchCounter(ocsPaymentDto.getBranchCounter());
		ocsPayment.setReceiptUser(ocsPaymentDto.getReceiptUser());

		if (ocsPaymentDto.getReceiptDate() != null) {
			ocsPayment.setReceiptDate(ocsPaymentDto.getReceiptDate());
		}

		ocsPayment.setPaymentMode(ocsPaymentDto.getPaymentMode());
		ocsPayment.setChqNo(ocsPaymentDto.getChqNo());
		ocsPayment.setChqBank(ocsPaymentDto.getChqBank());
		ocsPayment.setChqBranch(ocsPaymentDto.getChqBranch());
		ocsPayment.setRemarks(ocsPaymentDto.getRemarks());
		ocsPayment.setReasonCode(ocsPaymentDto.getTransferReasonCode());
		if (ocsPaymentDto.getProductType() != null) {
			ocsPayment.setProductType(ocsPaymentDto.getProductType());
		}

		ocsPayment.setContactNo(ocsPaymentDto.getContactNo());
		ocsPayment.setTranTypeId(OcsTranTypeEnum.OCS_TRAN_PAY.getValue());
		ocsPayment.setErrorDesc("Pending");
		String ocsPaymentConverToString = DCPEUtil.convertToString(ocsPayment);
		LOGGER.info("dtoToDamainsaveOcsPayment : body={}|traceId={}", ocsPaymentConverToString, traceId);
		return ocsPayment;
	}

	/**
	 * 
	 * @param ocsPaymentDto
	 * @param traceId
	 * @return OcsPayment
	 */
	private OcsPayment dtoToDamainCancelOcsPayment(OcsPaymentDto ocsPaymentDto, String traceId) {
		String ocsPaymentDtoConverToString = DCPEUtil.convertToString(ocsPaymentDto);
		LOGGER.info("dtoToDamainCancelOcsPayment : traceId={}|ocsPaymentDto={}", traceId, ocsPaymentDtoConverToString);
		OcsPayment ocsPayment = new OcsPayment();

		ocsPayment.setReqId(ocsPaymentDto.getReqId());
		ocsPayment.setAccountNo(ocsPaymentDto.getAccountNo());
		ocsPayment.setContractNo(ocsPaymentDto.getContractNo());
		ocsPayment.setConnRef(ocsPaymentDto.getConnRef());

		Optional<ProductCategoryEnum> productCategory = ProductCategoryEnum.getProductCategory(Integer.parseInt(ocsPaymentDto.getProductCategory()));
		if (productCategory.isPresent()) {
			ocsPayment.setProductCategory(productCategory.get().name());
		}

		Optional<SBUEnum> sbu = SBUEnum.getSBUByValue(ocsPaymentDto.getSbu());
		if (sbu.isPresent()) {
			ocsPayment.setSbu(sbu.get().name());
		}

		ocsPayment.setAccountType(AccountTypeEnum.POSTPAID.name());

		if (ocsPaymentDto.getPhysicalPaymentDate() != null) {
			ocsPayment.setPhysicalPaymentDate(ocsPaymentDto.getPhysicalPaymentDate());
		}

		if (ocsPaymentDto.getPaymentAmount() != null) {
			ocsPayment.setPaymentAmount(ocsPaymentDto.getPaymentAmount());
		}

		ocsPayment.setPaymentRef(null);

		if (ocsPaymentDto.getReceiptNo() != null) {
			ocsPayment.setReceiptNo(ocsPaymentDto.getReceiptNo());
		}

		ocsPayment.setReceiptBranch(ocsPaymentDto.getReceiptBranch());
		ocsPayment.setBranchCounter(ocsPaymentDto.getBranchCounter());
		ocsPayment.setReceiptUser(ocsPaymentDto.getReceiptUser());

		if (ocsPaymentDto.getReceiptDate() != null) {
			ocsPayment.setReceiptDate(ocsPaymentDto.getReceiptDate());
		}

		ocsPayment.setPaymentMode(null);
		ocsPayment.setChqNo(ocsPaymentDto.getChqNo());
		ocsPayment.setChqBank(ocsPaymentDto.getChqBank());
		ocsPayment.setChqBranch(ocsPaymentDto.getChqBranch());
		ocsPayment.setRemarks(ocsPaymentDto.getRemarks());
		ocsPayment.setReasonCode(ocsPaymentDto.getCancelledReason());

		if (ocsPaymentDto.getProductType() != null) {
			ocsPayment.setProductType(ocsPaymentDto.getProductType());
		}

		ocsPayment.setContactNo(null);
		ocsPayment.setTranTypeId(OcsTranTypeEnum.OCS_TRAN_CANCEL.getValue());
		ocsPayment.setErrorDesc("Cancellation Pending");
		String ocsPaymentConverToString = DCPEUtil.convertToString(ocsPayment);
		LOGGER.info("dtoToDamainCancelOcsPayment : traceId={}|ocsPayment={}", traceId, ocsPaymentConverToString);
		return ocsPayment;
	}
}
