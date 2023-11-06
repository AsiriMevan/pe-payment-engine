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
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.billing.service.QueryRemarkService;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.billing.util.HotlineRemarkRequest;
import lk.dialog.pe.billing.util.HotlineRemarkResponse;
import lk.dialog.pe.common.util.JSONSchemaValidator;
import lk.dialog.pe.common.util.PaymentDelegationEnum;

@RestController
@RequestMapping("/pe")
public class HotLineRemarksController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HotLineRemarksController.class);

	@Autowired
	private QueryRemarkService queryRemarkService;
	
	@Autowired
	private JSONSchemaValidator jsonValidator;
	
	/**
	 * [IP504]Get hotline remarks for the request
	 * 
	 * @param hotlineRemarkRequest
	 * @param traceId
	 * @return ResponseEntity<HotlineRemarkResponse>
	 * @throws DCPEException
	 */
	// IP504 get hot-line remarks made by User..
	@PutMapping(value = "/remarks")
	@ApiOperation(value = "[IP504]Get hotline remarks for the request.", notes = "The request parameter traceId is optional. The request body sbu, productType, accountType, accounts and productCategory are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of hotline remarks", name = "hotlineRemarkRequest", paramType = "body", dataType = "HotlineRemarkRequest", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })	
	public ResponseEntity<HotlineRemarkResponse> getHotlineRemarks(@RequestBody HotlineRemarkRequest hotlineRemarkRequest, @RequestParam(required = false) String traceId) throws DCPEException,ProcessingException,IOException{
		
		if(traceId==null) traceId = DCPEUtil.generateTraceId();
		String requestString = DCPEUtil.convertToString(hotlineRemarkRequest);
		LOGGER.info("getHotlineRemarksRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
        //todo - why time taken is added here
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String validationError = jsonValidator.validateJSON(timeTaken, hotlineRemarkRequest,PaymentDelegationEnum.JSON_SCHEMA_504.getName());
		
		if (validationError != null && !validationError.isEmpty()) {
			LOGGER.error("queryHotLineRemarksResponse : Invalid Trace Id traceId={}",traceId);
			throw new DCPEException(validationError);
		}
		
		HotlineRemarkResponse response = queryRemarkService.getHotlineRemarks(hotlineRemarkRequest, traceId);

		String responseString = DCPEUtil.convertToString(response);
		//todo - why is this commented out?
//		LOGGER.info("getHotlineRemarksResponse : traceId={}|timeTaken={}|{}", traceId, timeTaken, responseString);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
