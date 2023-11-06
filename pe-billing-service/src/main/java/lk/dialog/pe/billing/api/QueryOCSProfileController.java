package lk.dialog.pe.billing.api;

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
import lk.dialog.pe.billing.domain.OCSProfileRequest;
import lk.dialog.pe.billing.service.QueryOCSProfileService;
import lk.dialog.pe.common.util.JSONSchemaValidator;
import lk.dialog.pe.billing.util.OCSProfileResponse;
import lk.dialog.pe.common.util.PaymentDelegationEnum;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.DCPEUtil;

@RestController
@RequestMapping("/pe")
public class QueryOCSProfileController {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryOCSProfileController.class);
	
	@Autowired
	private QueryOCSProfileService queryOCSProfileService;
	
	@Autowired
	private JSONSchemaValidator jsonValidator;
	
	/**
	 * [IP503]Get OCS profile details for the request
	 * 
	 * @param ocsProfileRequest
	 * @param traceId
	 * @return ResponseEntity<OCSProfileResponse>
	 * @throws DCPEException
	 */
	   //IP503 get OCS profile details
		@PutMapping(value = "/ocsprofile")
		@ApiOperation(value = "[IP503]Get OCS profile details for the request.", notes = "The request parameter traceId is optional. The request body sbu, productType, accountType and productCategory are mandatory.")
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
		@ApiImplicitParams({ @ApiImplicitParam(value = "Details of OCS profile", name = "ocsProfileRequest", paramType = "body", dataType = "OCSProfileRequest", required = true),
		@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })	
		public ResponseEntity<OCSProfileResponse> getOCSProfile(@RequestBody OCSProfileRequest ocsProfileRequest, @RequestParam(required = false) String traceId) throws DCPEException,ProcessingException,IOException {
			
			if(traceId==null) traceId = DCPEUtil.generateTraceId();
			String requestString = DCPEUtil.convertToString(ocsProfileRequest);
			LOGGER.info("getOCSProfileRequest : traceId={}|requestString={}", traceId, requestString);
			Instant start = Instant.now();
			
			Long timeTaken = DCPEUtil.getTimeTaken(start);

			if (!DCPEUtil.traceIdValidation(traceId)) {
				LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
				throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(), DCPEErrorCode.INVALID_TRACE_ID.getStatus());
			}
	
			String validationError = jsonValidator.validateJSON(timeTaken, ocsProfileRequest,PaymentDelegationEnum.JSON_SCHEMA_503.getName());
			
			if (validationError != null && !validationError.isEmpty()) {
				throw new DCPEException(validationError);
			}
			
			OCSProfileResponse response = queryOCSProfileService.queryOCSProfile(ocsProfileRequest, traceId);

			String responseString = DCPEUtil.convertToString(response);
			//todo - fix bellow time taken issue
			LOGGER.info("getOCSProfileResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		
	
}
