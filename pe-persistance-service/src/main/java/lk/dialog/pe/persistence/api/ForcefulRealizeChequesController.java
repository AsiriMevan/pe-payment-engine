package lk.dialog.pe.persistence.api;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Optional;

import java.util.TimeZone;
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
import io.swagger.annotations.ApiResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.dto.ForcefulRealizeChequesDto;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.AccountTypeEnum;
import lk.dialog.pe.common.util.ConnectionStatusEnum;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PayModeEnum;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.util.SBUEnum;
import lk.dialog.pe.persistence.domain.ForcefulRealizeCheques;
import lk.dialog.pe.persistence.service.ForcefulRealizeChequesService;

@RestController
@RequestMapping("/forceful-realize-cheques")
public class ForcefulRealizeChequesController {

	@Autowired
	private ForcefulRealizeChequesService forcefulRealizeChequesService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ForcefulRealizeChequesController.class);

	/**
	 * Create forceful-realize-cheques for the request
	 * 
	 * @param forcefulRealizeChequesDto
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping
	@ApiOperation(value = "Create forceful-realize-cheques for the request.", notes = "The request parameter traceId  and request body code are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Details of the forceful-realize-cheques to be saved", name = "forcefulRealizeChequesDto", paramType = "body", dataType = "ForcefulRealizeChequesDto", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> saveForcefulRealizeCheques(@RequestBody ForcefulRealizeChequesDto forcefulRealizeChequesDto, @RequestParam String traceId)
			throws DCPEException ,ParseException{

		String requestString = DCPEUtil.convertToString(forcefulRealizeChequesDto);
		Instant start = Instant.now();
		LOGGER.info("saveForcefulRealizeChequesRequest : traceId={}|forcefulRealizeChequesDto={}", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("saveForcefulRealizeChequesResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException("Invalid Trace Id", 403);
		}
		Object status = null;
		try {
			status = forcefulRealizeChequesService.saveForcefulRealizeCheques(dtoToDamain(forcefulRealizeChequesDto, traceId), traceId);
		} catch (DataAccessException | DCPEException e) {
			throw new DCPEException(e.getMessage());
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("saveForcefulRealizeChequesResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, status);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, status));
	}

	/**
	 * 
	 * @param forcefulRealizeChequesDto
	 * @param traceId
	 * @return ForcefulRealizeCheques
	 */
	private ForcefulRealizeCheques dtoToDamain(ForcefulRealizeChequesDto forcefulRealizeChequesDto, String traceId) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String requestString = DCPEUtil.convertToString(forcefulRealizeChequesDto);
		LOGGER.info("dtoToDamainRequest : traceId={}|forcefulRealizeChequesDto={}", traceId, requestString);

		final ForcefulRealizeCheques forcefulRealizeCheques;
		forcefulRealizeCheques = setupForcefulRealizeChequesConvertion(forcefulRealizeChequesDto, traceId);
		if (forcefulRealizeChequesDto.getPhysicalPaymentDate() != null) {
			forcefulRealizeCheques.setPhysicalPaymentDate(Timestamp.valueOf(df.format(forcefulRealizeCheques.getPhysicalPaymentDate().getTime())));

			if (forcefulRealizeChequesDto.getProductType() != null) {
				forcefulRealizeCheques.setProductType(forcefulRealizeChequesDto.getProductType());
			}

			setPaymentRefAndRemarkAndConStatus(forcefulRealizeChequesDto, forcefulRealizeCheques);

		}
		String forcefulRealizeChequesToString = DCPEUtil.convertToString(forcefulRealizeCheques);
		LOGGER.info("dtoToDamainResponse : traceId={}|forcefulRealizeCheques={}", traceId, forcefulRealizeChequesToString);

		return forcefulRealizeCheques;
	}

	/**
	 * 
	 * @param forcefulRealizeChequesDto
	 * @param traceId
	 * @return ForcefulRealizeCheques
	 */
	private ForcefulRealizeCheques setupForcefulRealizeChequesConvertion(ForcefulRealizeChequesDto forcefulRealizeChequesDto, String traceId) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String forcefulRealizeChequesDtoToString = DCPEUtil.convertToString(forcefulRealizeChequesDto);
		LOGGER.info("setupForcefulRealizeChequesConvertionRequest : traceId={}|forcefulRealizeChequesDto={}", traceId, forcefulRealizeChequesDtoToString);

		ForcefulRealizeCheques forcefulRealizeCheques = new ForcefulRealizeCheques();
		
		if (forcefulRealizeChequesDto.getPhysicalPaymentDate() != null) {
			forcefulRealizeCheques.setPhysicalPaymentDate((Timestamp.valueOf(df.format(forcefulRealizeCheques.getPhysicalPaymentDate().getTime()))));

			if (forcefulRealizeChequesDto.getProductType() != null) {
				forcefulRealizeCheques.setProductType(forcefulRealizeChequesDto.getProductType());
			}
			forcefulRealizeCheques = convertDtoToEntity(forcefulRealizeChequesDto);
			setPaymentRefAndRemarkAndConStatus(forcefulRealizeChequesDto, forcefulRealizeCheques);

			forcefulRealizeCheques.setReceiptDate(forcefulRealizeChequesDto.getReceiptDate());
		}

		forcefulRealizeCheques.setReceiptUser(forcefulRealizeChequesDto.getReceiptUser());

		String forcefulRealizeChequesToString = DCPEUtil.convertToString(forcefulRealizeCheques);
		LOGGER.info("setupForcefulRealizeChequesConvertionResponse : traceId={}|forcefulRealizeCheques={}", traceId, forcefulRealizeChequesToString);

		return forcefulRealizeCheques;
	}

	private void setPaymentRefAndRemarkAndConStatus(ForcefulRealizeChequesDto forcefulRealizeChequesDto, ForcefulRealizeCheques forcefulRealizeCheques) {
		forcefulRealizeCheques.setRealTime(forcefulRealizeChequesDto.getRealTime());
		forcefulRealizeCheques.setConnRef(forcefulRealizeChequesDto.getConnRef());

		String paymentRef;
		if (PayModeEnum.PAY_MODE_CARD.getPayMode().equalsIgnoreCase(forcefulRealizeChequesDto.getPaymentMode())) {
			paymentRef = forcefulRealizeChequesDto.getCardNo();
		} else if (PayModeEnum.PAY_MODE_CHQ.getPayMode().equalsIgnoreCase(forcefulRealizeChequesDto.getPaymentMode())) {
			paymentRef = forcefulRealizeChequesDto.getChqBank() + forcefulRealizeChequesDto.getChqBranch() + forcefulRealizeChequesDto.getChqNo();
		} else {
			paymentRef = forcefulRealizeChequesDto.getPaymentRef();
		}
		forcefulRealizeCheques.setPaymentRef(paymentRef != null && paymentRef.length() > 50 ? paymentRef.substring(0, 50) : paymentRef);

		forcefulRealizeCheques.setRemarks(forcefulRealizeChequesDto.getRemarks());

		Optional<ConnectionStatusEnum> connectionStatus = ConnectionStatusEnum.getConnectionStatusByValue(forcefulRealizeChequesDto.getConnectionStatus());
		if (connectionStatus.isPresent()) {
			forcefulRealizeCheques.setConnectionStatus(connectionStatus.get().name());
		}
	}

	/**
	 * 
	 * @param forcefulRealizeChequesDto
	 * @return ForcefulRealizeCheques
	 */
	private ForcefulRealizeCheques convertDtoToEntity(ForcefulRealizeChequesDto forcefulRealizeChequesDto) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ForcefulRealizeCheques forcefulRealizeCheques = new ForcefulRealizeCheques();
		forcefulRealizeCheques.setReqId(forcefulRealizeChequesDto.getReqId());
		forcefulRealizeCheques.setAccountNo(forcefulRealizeChequesDto.getAccountNo());
		forcefulRealizeCheques.setContractId(forcefulRealizeChequesDto.getContractNo());
		Optional<ProductCategoryEnum> productCategory = ProductCategoryEnum.getProductCategory(Integer.parseInt(forcefulRealizeChequesDto.getProductCategory()));
		if (productCategory.isPresent()) {
			forcefulRealizeCheques.setProductCategory(productCategory.get().name());
		}

		Optional<SBUEnum> sbu = SBUEnum.getSBUByValue(forcefulRealizeChequesDto.getSbu());
		if (sbu.isPresent()) {
			forcefulRealizeCheques.setSbu(sbu.get().name());
		}

		Optional<AccountTypeEnum> accountTypeEnum = Optional.ofNullable(AccountTypeEnum.getAccountType(forcefulRealizeChequesDto.getAccountType()));
		if (accountTypeEnum.isPresent()) {
			forcefulRealizeCheques.setAccountType(accountTypeEnum.get().name());
		}

		if (forcefulRealizeChequesDto.getPaymentAmount() != null) {
			forcefulRealizeCheques.setPaymentAmount(forcefulRealizeChequesDto.getPaymentAmount());
		}

		forcefulRealizeCheques.setChqBank(forcefulRealizeChequesDto.getChqBank());
		forcefulRealizeCheques.setChqBranch(forcefulRealizeChequesDto.getChqBranch());
		forcefulRealizeCheques.setChqNo(forcefulRealizeChequesDto.getChqNo());

		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		if (forcefulRealizeChequesDto.getChqDate() != null) {
			forcefulRealizeCheques.setChqDate(Timestamp.valueOf(df.format(forcefulRealizeCheques.getChqDate().getTime())));
		}

		forcefulRealizeCheques.setTerminalId(forcefulRealizeChequesDto.getTerminalId());
		forcefulRealizeCheques.setReceiptNo(forcefulRealizeChequesDto.getReceiptNo());
		forcefulRealizeCheques.setReceiptBranch(forcefulRealizeChequesDto.getReceiptBranch());
		forcefulRealizeCheques.setBranchCounter(forcefulRealizeChequesDto.getBranchCounter());

		df.setTimeZone(TimeZone.getTimeZone("UTC"));
		if (forcefulRealizeChequesDto.getReceiptDate() != null) {
			forcefulRealizeCheques.setReceiptDate(Timestamp.valueOf(df.format(forcefulRealizeCheques.getReceiptDate().getTime())));
		}

		forcefulRealizeCheques.setReceiptUser(forcefulRealizeChequesDto.getReceiptUser());

		return forcefulRealizeCheques;
	}
}
