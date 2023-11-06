package lk.dialog.pe.billing.api;

import io.swagger.annotations.*;
import lk.dialog.pe.billing.domain.PEProfileRequest;
import lk.dialog.pe.billing.domain.PEProfileResponse;
import lk.dialog.pe.billing.service.QueryPEProfileService;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.JSONSchemaValidator;
import lk.dialog.pe.common.util.PaymentDelegationEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;


@RestController
@RequestMapping("/pe")
public class QueryPEProfileController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryPEProfileController.class);
	
	@Autowired
	private QueryPEProfileService queryPEProfileService;
	
	@Autowired
	private JSONSchemaValidator jsonValidator;
	
	/**
	 * [IP501]Get PE Profile details for the request
	 * 
	 * @param peProfileRequest
	 * @param traceId
	 * @return ResponseEntity<PEProfileResponse>
	 * @throws DCPEException
	 */
	//serve IP501 query OCS and RBM outstanding
	@PutMapping(value = "/peprofile")
	@ApiOperation(value = "[IP501]Get PE Profile details for the request.", notes = "The request parameter traceId is optional. The request body accounts and productCategory are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of PE profile", name = "peProfileRequest", paramType = "body", dataType = "PEProfileRequest", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })	
	public ResponseEntity<PEProfileResponse> getPEProfile(@RequestBody PEProfileRequest peProfileRequest, @RequestParam(required = false) String traceId) throws Exception {

		if(traceId==null) traceId = DCPEUtil.generateTraceId();
		String requestString = DCPEUtil.convertToString(peProfileRequest);
		LOGGER.info("getPEProfileRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(), DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}
		
		String validationError = jsonValidator.validateJSON(timeTaken, peProfileRequest,PaymentDelegationEnum.JSON_SCHEMA_501.getName());
		
		if (validationError != null && !validationError.isEmpty()) {
			LOGGER.error("queryOCSProfileResponse : Invalid Trace Id traceId={}",traceId);
			throw new DCPEException(validationError);
		}
		
		PEProfileResponse respons = queryPEProfileService.queryPEProfile(peProfileRequest, traceId);
		
		String responseString = DCPEUtil.convertToString(respons);
		LOGGER.info("getPEProfileResponse : traceId={}|timeTaken={}|responseString={}", traceId, timeTaken, responseString);
		return new ResponseEntity<>(respons, HttpStatus.OK);
	}
	
}
