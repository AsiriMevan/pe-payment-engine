package lk.dialog.pe.retrieval.cancelation.api;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.dto.QueryPaymentRequest;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.DateTimeValidator;
import lk.dialog.pe.common.util.JSONSchemaValidator;
import lk.dialog.pe.common.util.PaymentDelegationEnum;
import lk.dialog.pe.retrieval.cancelation.domain.QueryPendingPaymentResponse;
import lk.dialog.pe.retrieval.cancelation.service.PaymentService;

@RestController
@RequestMapping("/pe")
public class PendingPaymentsController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private JSONSchemaValidator jsonValidator;

	private static final Logger LOGGER = LoggerFactory.getLogger(PendingPaymentsController.class);

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssz", Locale.ENGLISH);

	/**
	 * [IP506] Get pending payments for the request
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<QueryPendingPaymentResponse>
	 * @throws DCPEException
	 */
	// serve IP506 query pending payments from payment engine
	@PutMapping(value = "/payment/pending")
	@ApiOperation(value = "[IP506] Get pending payments for the request.", notes = "The request parameter traceId is optional. The request body productCategory, receiptFromDate, receiptToDate and receiptUser are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Details of payment", name = "queryPaymentRequest", paramType = "body", dataType = "QueryPaymentRequest", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })
	public ResponseEntity<QueryPendingPaymentResponse> queryPendingPayment(
			@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam(required = false) String traceId)
			throws DCPEException, ProcessingException, IOException,PEException {
		if (traceId == null)
			traceId = DCPEUtil.generateTraceId();
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		LOGGER.info("queryPendingPaymentRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();

		Long timeTaken = DCPEUtil.getTimeTaken(start);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage());
		}


		String validationError = jsonValidator.validateJSON(timeTaken, queryPaymentRequest,
				PaymentDelegationEnum.JSON_SCHEMA_506.getName());

		if (validationError != null && !validationError.isEmpty()) {
			throw new DCPEException(validationError);
		}

		
		List<Date> datelist= new ArrayList<>();
		datelist.add(queryPaymentRequest.getReceiptFromDate());
		datelist.add(queryPaymentRequest.getReceiptToDate());

		if (queryPaymentRequest.getReceiptFromDate() != null) {
			if (queryPaymentRequest.getReceiptToDate() == null) {
				throw new PEException("Invalid query parameter combination.");
			}
		} else if (queryPaymentRequest.getReceiptToDate() != null) {
			if (queryPaymentRequest.getReceiptFromDate() == null) {
				throw new PEException("Invalid query parameter combination.");
			}
		}

		String dateValidationStatus = DateTimeValidator.validateDateFormat(datelist, Constants.PAYMENT_PENDDING_DATE_FORMAT);
		if (dateValidationStatus != null && !dateValidationStatus.isEmpty()) {
			throw new PEException(dateValidationStatus);
		}


		QueryPendingPaymentResponse response = paymentService.queryPendingPayment(queryPaymentRequest, traceId);

		String responseString = DCPEUtil.convertToString(response);
		LOGGER.info("queryPendingPaymentResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken,
				responseString);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
