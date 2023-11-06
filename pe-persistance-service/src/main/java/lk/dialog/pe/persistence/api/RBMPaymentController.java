package lk.dialog.pe.persistence.api;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.dto.RBMPaymentDto;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.AccountTypeEnum;
import lk.dialog.pe.common.util.ConnectionStatusEnum;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PayModeEnum;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.persistence.domain.RBMPayment;
import lk.dialog.pe.persistence.service.CposRBMPayementService;
import lk.dialog.pe.persistence.service.impl.MapServiceImpl;

@RestController

@RequestMapping("/rbm-payment")
public class RBMPaymentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBMPaymentController.class);

	@Autowired
	private CposRBMPayementService cposRBMService;
	
	@Autowired
	private MapServiceImpl mapServiceImpl;

	/**
	 * Create RBM payment for the request
	 * 
	 * @param rbmPaymentDto
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping
	@ApiOperation(value = "Create RBM payment for the request.", notes = "The request parameter traceId  and request body code are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Details of the RBM payment to be saved", name = "rbmPaymentDto", paramType = "body", dataType = "RBMPaymentDto", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> insertRBMPayment(@RequestBody RBMPaymentDto rbmPaymentDto,
			@RequestParam String traceId) throws DCPEException,SQLException {

		String requestString = DCPEUtil.convertToString(rbmPaymentDto);
		Instant start = Instant.now();
		LOGGER.info("insertRBMPaymentRequest : traceId={}|RBMPaymentDTO={}", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException("Invalid Trace Id", 403);
		}

		Object status = null;
		try {
			status = cposRBMService.saveRBMpayment(dtoToDomain(rbmPaymentDto, traceId), traceId);
		} catch (DataAccessException | DCPEException e) {
			throw new DCPEException(e.getMessage());
		}
		String statusString = DCPEUtil.convertToString(status);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("insertRBMPaymentResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, statusString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, status));
	}

	/**
	 * 
	 * @param rbmPaymentDto
	 * @param traceId
	 * @return RBMPayment
	 */
	private RBMPayment dtoToDomain(RBMPaymentDto rbmPaymentDto, String traceId) throws SQLException {
		String requestString = DCPEUtil.convertToString(rbmPaymentDto);
		Instant start = Instant.now();
		LOGGER.info("dtoToDomainRequest : traceId={}|RBMPaymentDTO={}", traceId, requestString);

		String phyPaymentText;
		String paymentText;
		String paymentRef;
		String payModeRef;
		String payRefCheck;
		/* set payment text */
		String payMode = mapServiceImpl.getPayModeMap(rbmPaymentDto.getPaymentMode(), traceId).getRbmId();
		/* set payment reference */
		payRefCheck = rbmPaymentDto.getPaymentRef();

		if (Constants.PAY_MODE_CARD.equalsIgnoreCase(rbmPaymentDto.getPaymentMode())) {
			paymentRef = rbmPaymentDto.getCardNo();
			payModeRef = ((paymentRef != null && paymentRef.length() < 50)
					? paymentRef + " " + (payRefCheck != null ? payRefCheck : "-" + payRefCheck)
					: paymentRef.substring(0, 50));
		} else if (Constants.PAY_MODE_CHQ.equalsIgnoreCase(rbmPaymentDto.getPaymentMode())) {
			paymentRef = rbmPaymentDto.getChqBank() + rbmPaymentDto.getChqBranch() + rbmPaymentDto.getChqNo();
			payModeRef = ((paymentRef != null && paymentRef.length() < 50)
					? paymentRef + " " + (payRefCheck != null ? payRefCheck : "-" + payRefCheck)
					: paymentRef.substring(0, 50));
		} else {
			paymentRef = payRefCheck;
			payModeRef = ((payRefCheck != null && payRefCheck.length() > 50) ? payRefCheck.substring(0, 50)
					: payRefCheck);
		}

		/* set physical payment text */
		if (Constants.PAY_MODE_CHQ.equalsIgnoreCase(rbmPaymentDto.getPaymentMode())) {
			phyPaymentText = Constants.PAYMENT_PREFIX + payMode + " " + payModeRef + " - DIRECT CREDIT";
		} else {
			phyPaymentText = Constants.PAYMENT_PREFIX + payMode + " " + payModeRef;
		}
		/* set payment text */
		if (Constants.PAY_MODE_CARD.equalsIgnoreCase(rbmPaymentDto.getPaymentMode())) {
			paymentText = Constants.PAYMENT_PREFIX + rbmPaymentDto.getReceiptBranch() + "-"
					+ rbmPaymentDto.getReceiptNo() + "-" + rbmPaymentDto.getBranchCounter() + "-" + payMode + "-"
					+ rbmPaymentDto.getCardNo();
		} else if (Constants.PAY_MODE_CHQ.equalsIgnoreCase(rbmPaymentDto.getPaymentMode())) {
			paymentText = Constants.PAYMENT_PREFIX + rbmPaymentDto.getReceiptBranch() + "-"
					+ rbmPaymentDto.getReceiptNo() + "-" + rbmPaymentDto.getBranchCounter() + "-" + payMode + "-"
					+ rbmPaymentDto.getChqBank() + rbmPaymentDto.getChqBranch() + rbmPaymentDto.getChqNo();
		} else {
			paymentText = Constants.PAYMENT_PREFIX + rbmPaymentDto.getReceiptBranch() + "-"
					+ rbmPaymentDto.getReceiptNo() + "-" + rbmPaymentDto.getBranchCounter() + "-" + payMode
					+ (rbmPaymentDto.getPaymentRef() == null || "".equalsIgnoreCase(rbmPaymentDto.getPaymentRef()) ? ""
							: "-" + rbmPaymentDto.getPaymentRef());
		}

		RBMPayment rbmPayment = new RBMPayment();

		rbmPayment.setReqId(rbmPaymentDto.getReqId());
		rbmPayment.setAccountNo(rbmPaymentDto.getAccountNo());
		rbmPayment.setContractNo(rbmPaymentDto.getContractNo());
		rbmPayment.setConnRef(rbmPaymentDto.getConnRef());

		Optional<ProductCategoryEnum> productCategory = ProductCategoryEnum
				.getProductCategory(Integer.parseInt(rbmPaymentDto.getProductCategory()));
		if (productCategory.isPresent()) {
			rbmPayment.setProductCategory(productCategory.get().name());
		}

		SBUEnum sbu = SBUEnum.getSBUByValue(Integer.parseInt(rbmPaymentDto.getSbu()));
		if (sbu != null) {
			rbmPayment.setSbu(sbu.name());
		}
		AccountTypeEnum accountType = AccountTypeEnum.getAccountType(Integer.parseInt(rbmPaymentDto.getAccountType()));
		if (accountType != null) {
			rbmPayment.setAccountType(accountType.name());
		}

		if (rbmPaymentDto.getPhysicalPaymentDate() != null) {
			rbmPayment.setPhysicalPaymentDate(rbmPaymentDto.getPhysicalPaymentDate());
		}

		if (rbmPaymentDto.getPaymentAmount() != null) {
			rbmPayment.setPaymentAmount(rbmPaymentDto.getPaymentAmount());
		}

		rbmPayment.setPaymentCurrency(rbmPaymentDto.getPaymentCurrency());

		if (rbmPaymentDto.getPaymentMethodId() != null) {
			rbmPayment.setPaymentMethodId(rbmPaymentDto.getPaymentMethodId());
		}

		rbmPayment.setPhysicalPaymentText(
				phyPaymentText != null && phyPaymentText.length() > 75 ? phyPaymentText.substring(0, 75)
						: phyPaymentText);
		rbmPayment.setPaymentRef(
				paymentRef != null && paymentRef.length() > 50 ? paymentRef.substring(0, 50) : paymentRef);

		rbmPayment.setReceiptNo(rbmPaymentDto.getReceiptNo());
		rbmPayment.setReceiptBranch(rbmPaymentDto.getReceiptBranch());
		rbmPayment.setBranchCounter(rbmPaymentDto.getBranchCounter());
		rbmPayment.setReceiptUser(rbmPaymentDto.getReceiptUser());

		if (rbmPaymentDto.getReceiptDate() != null) {
			rbmPayment.setReceiptDate(rbmPaymentDto.getReceiptDate());
		}

		rbmPayment.setPaymentMode(rbmPaymentDto.getPaymentMode());

		rbmPayment.setPaymentText(
				paymentText != null && paymentText.length() > 75 ? paymentText.substring(0, 75) : paymentText);

		rbmPayment.setTerminalId(rbmPaymentDto.getTerminalId());
		rbmPayment.setChqNo(rbmPaymentDto.getChqNo());
		rbmPayment.setChqBank(rbmPaymentDto.getChqBank());
		rbmPayment.setChqBranch(rbmPaymentDto.getChqBranch());

		if (rbmPaymentDto.getChqDate() != null) {
			rbmPayment.setChqDate(rbmPaymentDto.getChqDate());
		}

		rbmPayment.setChqSuspend(rbmPaymentDto.getChqSuspend());
		rbmPayment.setCardType(rbmPaymentDto.getCardType());
		rbmPayment.setCardAgent(rbmPaymentDto.getCardAgent());
		rbmPayment.setCardNo(rbmPaymentDto.getCardNo());
		rbmPayment.setCardAprvCode(rbmPaymentDto.getCardAprvCode());
		rbmPayment.setModuleName(rbmPaymentDto.getModuleName());

		if (rbmPaymentDto.getTransferredNo() != null) {
			rbmPayment.setTransferredNo(rbmPaymentDto.getTransferredNo());
		}
		rbmPayment.setTransferredType(rbmPaymentDto.getTransferredType());
		rbmPayment.setTransferredMode(rbmPaymentDto.getTransferredMode());
		rbmPayment.setTransferredNo(rbmPaymentDto.getTransferredNo());
		rbmPayment.setRemarks(rbmPaymentDto.getRemarks());
		rbmPayment.setTransferReasonCode(rbmPaymentDto.getTransferReasonCode());

		if (rbmPaymentDto.getProductType() != null) {
			rbmPayment.setProductType(rbmPaymentDto.getProductType());
		}

		rbmPayment.setContactNo(rbmPaymentDto.getContactNo());

		Optional<ConnectionStatusEnum> connectionStatus = ConnectionStatusEnum
				.getConnectionStatusByValue(rbmPaymentDto.getConnectionStatus());
		if (connectionStatus.isPresent()) {
			rbmPayment.setConnectionStatus(connectionStatus.get().name());
		}

		String rbmPaymentString = DCPEUtil.convertToString(rbmPayment);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("dtoToDomainResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, rbmPaymentString);

		return rbmPayment;
	}

}
