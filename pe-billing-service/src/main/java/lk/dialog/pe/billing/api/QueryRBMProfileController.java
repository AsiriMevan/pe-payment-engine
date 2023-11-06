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

import lk.dialog.pe.billing.domain.BillingProfileRequest;
import lk.dialog.pe.billing.domain.BillingProfileResponse;
import lk.dialog.pe.billing.service.QueryRBMProfileService;
import lk.dialog.pe.common.util.JSONSchemaValidator;
import lk.dialog.pe.common.util.PaymentDelegationEnum;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.DCPEUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/pe")
public class QueryRBMProfileController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryRBMProfileController.class);
	
	@Autowired
	private QueryRBMProfileService queryRBMProfileService;
	
	@Autowired
	private JSONSchemaValidator jsonValidator;

	/**
	 * [IP502]Get RBM billing profile details for the request
	 * 
	 * @param billingProfileRequest
	 * @param traceId
	 * @return ResponseEntity<BillingProfileResponse>
	 * @throws DCPEException
	 */
	//serve IP502 get RBM billing profile details
	@PutMapping(value = "/rbmprofile")
	@ApiOperation(value = "[IP502]Get RBM billing profile details for the request.", notes = "The request parameter traceId is optinal. The request body sbu, productType, accountType and productCategory are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of RBM billing profile", name = "billingProfileRequest", paramType = "body", dataType = "BillingProfileRequest", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })	
	public ResponseEntity<BillingProfileResponse> queryRBMProfile(@RequestBody BillingProfileRequest billingProfileRequest, @RequestParam(required = false) String traceId) throws DCPEException,ProcessingException,IOException {if(traceId==null) traceId = DCPEUtil.generateTraceId();
		String requestString = DCPEUtil.convertToString(billingProfileRequest);
		LOGGER.info("queryRBMProfileRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(), DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String validationError = jsonValidator.validateJSON(timeTaken, billingProfileRequest,PaymentDelegationEnum.JSON_SCHEMA_502.getName());
		
		if (validationError != null && !validationError.isEmpty()) {
			LOGGER.error("queryRBMProfileResponse : Invalid Trace Id traceId={}",traceId);
			throw new DCPEException(validationError);
		}

		BillingProfileResponse response = queryRBMProfileService.queryRBMProfile(billingProfileRequest, traceId);
		
		String responseString = DCPEUtil.convertToString(response);
		//todo - fix bellow time taken issue
		LOGGER.info("queryRBMProfileResponse : traceId={}|timeTaken={}|RBMProfileResponse={}", traceId, timeTaken, responseString);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
