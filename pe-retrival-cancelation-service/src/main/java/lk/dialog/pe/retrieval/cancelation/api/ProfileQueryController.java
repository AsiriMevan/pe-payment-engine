package lk.dialog.pe.retrieval.cancelation.api;

import java.io.IOException;
import java.time.Instant;

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
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.JSONSchemaValidator;
import lk.dialog.pe.common.util.PaymentDelegationEnum;
import lk.dialog.pe.retrieval.cancelation.service.ProfileQueryService;
import lk.dialog.pe.retrieval.cancelation.util.ProfileQueryRequest;
import lk.dialog.pe.retrieval.cancelation.util.QueryPaymentResponse;

@RestController
@RequestMapping("/pe")
public class ProfileQueryController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileQueryController.class);

	@Autowired
	ProfileQueryService profileQueryService;
	
	@Autowired
	private JSONSchemaValidator jsonValidator;

	/**
	 * [IP505] Get query payments summary of a contract for the request
	 * 
	 * @param profileQueryRequest
	 * @param traceId
	 * @return ResponseEntity<QueryPaymentResponse>
	 * @throws DCPEException
	 */
	// serve IP505 query payments summary of a contract
	@PutMapping(value = "/payment")
	@ApiOperation(value = "[IP505] Get query payments summary of a contract for the request.", notes = "The request parameter traceId is optional.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Details of query payment", name = "queryPaymentRequest", paramType = "body", dataType = "QueryPaymentRequest", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })
	public ResponseEntity<QueryPaymentResponse> queryPayments(@RequestBody ProfileQueryRequest profileQueryRequest,
			@RequestParam(required = false) String traceId) throws DCPEException,ProcessingException,IOException {
		if (traceId == null)
			traceId = DCPEUtil.generateTraceId();
		String requestString = DCPEUtil.convertToString(profileQueryRequest);
		Instant start = Instant.now();
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("queryPaymentsRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage());
		}
		
		  String validationError = jsonValidator.validateJSON(timeTaken,profileQueryRequest, PaymentDelegationEnum.JSON_SCHEMA_506.getName());

			if (validationError != null && !validationError.isEmpty()) {
				throw new DCPEException(validationError);
			}
		 

		QueryPaymentResponse queryPaymentResponse = profileQueryService.queryPayments(profileQueryRequest, traceId);

		String responsetString = DCPEUtil.convertToString(queryPaymentResponse);
		LOGGER.info("queryPaymentsResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responsetString);

		return new ResponseEntity<>(queryPaymentResponse, HttpStatus.OK);

	}

}
