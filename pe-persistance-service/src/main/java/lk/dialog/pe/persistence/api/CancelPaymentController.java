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

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.common.dto.CancelPaymentRequestDto;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.CommandReadEnum;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.persistence.domain.CancelPayment;
import lk.dialog.pe.persistence.service.CancelPaymentService;
import lk.dialog.pe.persistence.util.QuerySystemEnum;

@RestController
@RequestMapping("/cancel-payment")
public class CancelPaymentController {

	@Autowired
	private CancelPaymentService cancelPaymentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(CancelPaymentController.class);

	/**
	 * Create cancel payment for the request
	 * 
	 * @param cancelPaymentDto
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping
	@ApiOperation(value = "Create cancel payment for the request.", notes = "The request parameter traceId  and request body code are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the cancel payment to be saved", name = "cancelPaymentDto", paramType = "body", dataType = "CancelPaymentRequestDto", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> saveCancelPayment(@RequestBody CancelPaymentRequestDto cancelPaymentDto, @RequestParam String traceId) throws DCPEException {

		String requestString = DCPEUtil.convertToString(cancelPaymentDto);
		Instant start = Instant.now();
		LOGGER.info("saveCancelPaymentRequest : traceId={}|CancelPayment={}", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("saveCancelPaymentResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(), DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}

		Object status = cancelPaymentService.saveCancelPayment(dtoToDamain(cancelPaymentDto), traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("saveCancelPaymentResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, status);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, status));
	}

	/**
	 * 
	 * @param cancelPaymentDto
	 * @return CancelPayment
	 */
	private CancelPayment dtoToDamain(CancelPaymentRequestDto cancelPaymentDto) {
		CancelPayment cancelPayment = new CancelPayment();

		cancelPayment.setReqId(cancelPaymentDto.getReqId());

		Optional<ProductCategoryEnum> productCategory = ProductCategoryEnum.getProductCategory(Integer.parseInt(cancelPaymentDto.getProductCategory()));
		if (productCategory.isPresent()) {
			cancelPayment.setProductCategory(productCategory.get().name());
		}

		Optional<SBUEnum> sbu = SBUEnum.getSBUByValue(cancelPaymentDto.getSbu()+"");
		if (sbu.isPresent()) {
			cancelPayment.setSbu(sbu.get().name());
		}

		Optional<QuerySystemEnum> querySystem = QuerySystemEnum.getQuerySystemByValue(cancelPaymentDto.getQuerySystem()+"");
		if (querySystem.isPresent()) {
			cancelPayment.setQuerySystem(querySystem.get().name());
		}

		cancelPayment.setAccountNo(cancelPaymentDto.getAccountNo());
		cancelPayment.setContractNo(cancelPaymentDto.getContractNo());

		if (cancelPaymentDto.getPhysicalSeq() != null) {
			cancelPayment.setPhysicalSeq(cancelPaymentDto.getPhysicalSeq());
		}

		if (cancelPaymentDto.getAccountSeq() != null) {
			cancelPayment.setAccountSeq(cancelPaymentDto.getAccountSeq());
		}

		cancelPayment.setChqReturn(cancelPaymentDto.getChqReturn());
		cancelPayment.setChqSuspend(cancelPaymentDto.getChqSuspend());
		cancelPayment.setReversalType(cancelPaymentDto.getReversalType());

		if (cancelPaymentDto.getTransferredType() != null) {
			cancelPayment.setTransferredType(cancelPaymentDto.getTransferredType());
		}

		cancelPayment.setTransferredMode(cancelPaymentDto.getTransferredMode());

		if (cancelPaymentDto.getTransferredNo() != null) {
			cancelPayment.setTransferredNo(cancelPaymentDto.getTransferredNo());
		}

		cancelPayment.setTransferredRef(cancelPaymentDto.getTransferredRef());

		if (cancelPaymentDto.getMistakeBy() != null && !cancelPaymentDto.getMistakeBy().isEmpty()) {
			cancelPayment.setMistakeBy(cancelPaymentDto.getMistakeBy());
		} else {
			cancelPayment.setMistakeBy("CX");
		}

		cancelPayment.setRemarks(cancelPaymentDto.getRemarks());
		cancelPayment.setCancelledReason(cancelPaymentDto.getCancelledReason());
		cancelPayment.setCancelledUser(cancelPaymentDto.getCancelledUser());
		cancelPayment.setApprovedUser(cancelPaymentDto.getApprovedUser());

		if (cancelPaymentDto.getProductType() != null) {
			cancelPayment.setProductType(cancelPaymentDto.getProductType());
		}

		cancelPayment.setReceiptNo(cancelPaymentDto.getReceiptNo());
		cancelPayment.setReceiptBranch(cancelPaymentDto.getReceiptBranch());
		cancelPayment.setBranchCounter(cancelPaymentDto.getBranchCounter());
		cancelPayment.setReceiptUser(cancelPaymentDto.getReceiptUser());
		cancelPayment.setReceiptDate(cancelPaymentDto.getReceiptDate());
		cancelPayment.setChqBank(cancelPaymentDto.getChqBank());
		cancelPayment.setChqNo(cancelPaymentDto.getChqNo());
		cancelPayment.setChqBranch(cancelPaymentDto.getChqBranch());
		cancelPayment.setTerminalId(cancelPaymentDto.getTerminalID());
		cancelPayment.setSourceSystem(cancelPaymentDto.getSourceSystem());

		if (cancelPaymentDto.getPhysicalPaymentDate() != null) {
			cancelPayment.setPhysicalPaymentDate(cancelPaymentDto.getPhysicalPaymentDate());
		}

		cancelPayment.setCreatedDate(cancelPaymentDto.getCreatedDate());
		cancelPayment.setCreatedUser(cancelPaymentDto.getCreatedUser());
		cancelPayment.setCommandread(CommandReadEnum.N);

		if (cancelPaymentDto.getPaymentAmount() != null) {
			cancelPayment.setPaymentAmount(cancelPaymentDto.getPaymentAmount());
		}

		return cancelPayment;
	}

}
