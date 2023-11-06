package lk.dialog.pe.persistence.api;

import io.swagger.annotations.*;
import lk.dialog.pe.common.dto.DBNPaymentDto;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.AccountTypeEnum;
import lk.dialog.pe.common.util.ConnectionStatusEnum;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.persistence.domain.DBNPayment;
import lk.dialog.pe.persistence.service.CposDBNPayementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Optional;

@RestController

@RequestMapping("/dbn-payment")
public class DBNPaymentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBNPaymentController.class);

	@Autowired
	private CposDBNPayementService cposDbnService;

	/**
	 * Create DBN payment for the request
	 * 
	 * @param dbnPaymentDto
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping
	@ApiOperation(value = "Create DBN payment for the request.", notes = "The request parameter traceId  and request body code are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Details of the DBN payment to be saved", name = "dbnPaymentDto", paramType = "body", dataType = "DBNPaymentDto", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> insertDBNPayment(@RequestBody DBNPaymentDto dbnPaymentDto,
			@RequestParam String traceId) throws DCPEException,SQLException {

		String requestString = DCPEUtil.convertToString(dbnPaymentDto);
		Instant start = Instant.now();
		LOGGER.info("insertDBNPaymentRequest : traceId={}|DBNPaymentDTO={}", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException("Invalid Trace Id", 403);
		}

		Object status;
		try {
			status = cposDbnService.saveDbnPayment(dtoToDomain(dbnPaymentDto, traceId), traceId);
		} catch (DataAccessException | DCPEException e) {
			throw new DCPEException(e.getMessage());
		}
		String statusString = DCPEUtil.convertToString(status);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("insertDBNPaymentResponse : traceId=[{}] | timeTaken=[{}] | body=[{}]", traceId, timeTaken, statusString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, status));
	}

	/**
	 * 
	 * @param dbnPaymentDto
	 * @param traceId
	 * @return DBNPayment
	 */
	private DBNPayment dtoToDomain(DBNPaymentDto dbnPaymentDto, String traceId) {
		String requestString = DCPEUtil.convertToString(dbnPaymentDto);
		Instant start = Instant.now();
		LOGGER.info("dtoToDomainRequest : traceId=[{}] | DBNPaymentDTO=[{}]", traceId, requestString);

		DBNPayment dbnPayment = new DBNPayment();

		dbnPayment.setReqId(dbnPaymentDto.getReqId());
		dbnPayment.setAccountNo(dbnPaymentDto.getAccountNo());
		dbnPayment.setContractNo(dbnPaymentDto.getContractNo());
		dbnPayment.setConnRef(dbnPaymentDto.getConnRef());

		AccountTypeEnum accountType = AccountTypeEnum.getAccountType(Integer.parseInt(dbnPaymentDto.getAccountType()));
		if (accountType != null) {
			dbnPayment.setAccountType(accountType.name());
		}

		if (dbnPaymentDto.getPaymentAmount() != null) {
			dbnPayment.setPaymentAmount(dbnPaymentDto.getPaymentAmount());
		}

		if (dbnPaymentDto.getPaymentMethodId() != null) {
			dbnPayment.setPaymentMethodId(dbnPaymentDto.getPaymentMethodId());
		}

		dbnPayment.setPaymentMode(dbnPaymentDto.getPaymentMode());
		dbnPayment.setPaymentRef(dbnPayment.getPaymentRef());

		dbnPayment.setChqBank(dbnPaymentDto.getChqBank());
		dbnPayment.setChqNo(dbnPaymentDto.getChqNo());
		dbnPayment.setChqBranch(dbnPaymentDto.getChqBranch());

		if (dbnPaymentDto.getChqDate() != null) {
			dbnPayment.setChqDate(dbnPaymentDto.getChqDate());
		}

		dbnPayment.setChqSuspend(dbnPaymentDto.getChqSuspend());

		dbnPayment.setCardType(dbnPaymentDto.getCardType());
		dbnPayment.setCardAgent(dbnPaymentDto.getCardAgent());
		dbnPayment.setCardNo(dbnPaymentDto.getCardNo());
		dbnPayment.setCardAprvCode(dbnPaymentDto.getCardAprvCode());

		dbnPayment.setReceiptNo(dbnPaymentDto.getReceiptNo());
		dbnPayment.setReceiptBranch(dbnPaymentDto.getReceiptBranch());
		dbnPayment.setBranchCounter(dbnPaymentDto.getBranchCounter());
		dbnPayment.setReceiptUser(dbnPaymentDto.getReceiptUser());

		if (dbnPaymentDto.getReceiptDate() != null) {
			dbnPayment.setReceiptDate(dbnPaymentDto.getReceiptDate());
		}

		dbnPayment.setTransferredType(dbnPaymentDto.getTransferredType());
		dbnPayment.setTransferredMode(dbnPaymentDto.getTransferredMode());
		dbnPayment.setTransferredNo(dbnPaymentDto.getTransferredNo());

		dbnPayment.setRemarks(dbnPaymentDto.getRemarks());
		dbnPayment.setContactNo(dbnPaymentDto.getContactNo());
		dbnPayment.setTransferReasonCode(dbnPaymentDto.getTransferReasonCode());

		if (dbnPaymentDto.getPhysicalPaymentDate() != null) {
			dbnPayment.setPhysicalPaymentDate(dbnPaymentDto.getPhysicalPaymentDate());
		}

		if (dbnPaymentDto.getProductType() != null) {
			dbnPayment.setProductType(dbnPaymentDto.getProductType());
		}

		dbnPayment.setTerminalId(dbnPaymentDto.getTerminalId());
		dbnPayment.setModuleName(dbnPaymentDto.getModuleName());

		if (dbnPaymentDto.getInvoiceList() != null) {
			dbnPayment.setInvoiceList(dbnPaymentDto.getInvoiceList());
		}

		Optional<ConnectionStatusEnum> connectionStatus = ConnectionStatusEnum
				.getConnectionStatusByValue(dbnPaymentDto.getConnectionStatus());
        connectionStatus.ifPresent(connectionStatusEnum -> dbnPayment.setConnectionStatus(connectionStatusEnum.name()));

		String rbmPaymentString = DCPEUtil.convertToString(dbnPayment);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("dtoToDomainResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, rbmPaymentString);

		return dbnPayment;
	}

}
