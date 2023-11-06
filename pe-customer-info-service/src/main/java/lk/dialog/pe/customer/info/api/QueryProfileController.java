package lk.dialog.pe.customer.info.api;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import lk.dialog.pe.customer.info.service.QueryProfileService;
import lk.dialog.pe.customer.info.util.CRMProfileResponse;
import lk.dialog.pe.common.util.JSONSchemaValidator;
import lk.dialog.pe.common.util.PaymentDelegationEnum;
import lk.dialog.pe.common.util.ProductCategoryEnum;
import lk.dialog.pe.common.domain.CRMProfileRequest;
import lk.dialog.pe.common.dto.ValidatorResponse;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.APIvalidator;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.common.util.DCPEUtil;

@RestController
@RequestMapping("/pe")

public class QueryProfileController {

	@Autowired
	private QueryProfileService queryProfileService;
	
	@Autowired
	private JSONSchemaValidator jsonValidator;

	private static final Logger LOGGER = LoggerFactory.getLogger(QueryProfileController.class);

	/**
	 * Get CRM profile details for the request
	 * 
	 * @param crmProfileRequest
	 * @param traceId
	 * @return ResponseEntity<CRMProfileResponse>
	 * @throws DCPEException
	 */
	// serve IP500 API get CRM profile details
	@PutMapping(value = "/ccbsprofile")
	@ApiOperation(value = "Get CRM profile details for the request.", notes = "The request parameter traceId is mandatory. The request body productCategory, sbu, reqType, requestUserId, remoteIP, sourceSystem  are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
	@ApiImplicitParam(value = "Details of CRM profile", name = "crmProfileRequest", paramType = "body", dataType = "CRMProfileRequest", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })
	public ResponseEntity<CRMProfileResponse> getCCBSProfile(@RequestBody CRMProfileRequest crmProfileRequest,@RequestParam(required = false) String traceId) throws DCPEException,ProcessingException,IOException {
		if (traceId == null)
			traceId = DCPEUtil.generateTraceId();
		String requestString = DCPEUtil.convertToString(crmProfileRequest);
		LOGGER.info("CRMProfileRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
		//todo - done - wrong time track
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		String validationError = jsonValidator.validateJSON(timeTaken,crmProfileRequest,PaymentDelegationEnum.JSON_SCHEMA_500.getName());

		if (validationError != null && !validationError.isEmpty()) {
			throw new DCPEException(validationError);
		}
		
		ValidatorResponse validatorResponse = APIvalidator.validateIP500TelbizConRef(crmProfileRequest);

		if (Constants.ERR_CODE_VALID.equalsIgnoreCase(validatorResponse.getStatus())) {
			if (ProductCategoryEnum.TELBIZ.toString().equalsIgnoreCase(validatorResponse.getCrmSystem())) {
				crmProfileRequest = validatorResponse.getCrmProfileRequest();
			}
		} else {
			throw new DCPEException(validatorResponse.getStatus());
		}

		Long timeTakenRes = DCPEUtil.getTimeTaken(start);
		LOGGER.info("CRMProfileRequest : traceId={}|requestString={}", traceId, requestString);
		CRMProfileResponse response = queryProfileService.queryProfile(crmProfileRequest, traceId);

		String responseString = DCPEUtil.convertToString(response);
		LOGGER.info("getCRMProfileResponse : traceId={}|timeTaken={}|{}", traceId, timeTakenRes, responseString);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}
}
