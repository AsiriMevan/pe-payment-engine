package lk.dialog.pe.ccbs.api;

import java.time.Instant;
import java.util.List;

import lk.dialog.pe.ccbs.dto.DcsRemarkRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import lk.dialog.pe.ccbs.dto.HotlineRemarkRequest;
import lk.dialog.pe.ccbs.dto.RemarkInfo;
import lk.dialog.pe.ccbs.service.GetHotLineRemarksService;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;

@RestController
@RequestMapping("/hotlineRemarks")
public class GetHotLineRemarksController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetHotLineRemarksController.class);	
	
	@Autowired
	private GetHotLineRemarksService getHotLineRemarksService;
	
	/**
	 * Get hotline remarks
	 * 
	 * @param hotlineRemarkRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-hotline-remarks")
	@ApiOperation(value = "Get hotline remarks", notes = "Retrieve hotline remarks. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the hotline remark", paramType = "body", name = "hotlineRemarkRequest", required = true, dataType = "HotlineRemarkRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getHotLineRemarks(@RequestBody HotlineRemarkRequest hotlineRemarkRequest, @RequestParam String traceId) throws DCPEException{

		String requestString = String.valueOf(hotlineRemarkRequest);		
		Instant start = Instant.now();		
		LOGGER.info("getHotLineRemarksRequest : traceId={}|HotlineRemarkRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}
		
		List<RemarkInfo> remarkInfoList = getHotLineRemarksService.getHotLineRemarks(hotlineRemarkRequest, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		//todo - why is this commented out?
//		LOGGER.info("getHotLineRemarksResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, remarkInfoList);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, remarkInfoList));				
	}

	/**
	 * Get dcs remarks
	 *
	 * @param dcsRemarkRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-dcs-remarks")
	@ApiOperation(value = "Get dcs remarks", notes = "Retrieve dcs remarks. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the dcs remark", paramType = "body", name = "dcsRemarkRequest", required = true, dataType = "DcsRemarkRequest"),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getDcsRemarks(@RequestBody DcsRemarkRequest dcsRemarkRequest, @RequestParam String traceId) throws DCPEException{

		String requestString = String.valueOf(dcsRemarkRequest);
		Instant start = Instant.now();
		LOGGER.info("getDcsRemarks Request : traceId=[{}] | dcsRemarkRequest=[{}]", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId=[{}]", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}

		List<RemarkInfo> remarkInfoList = getHotLineRemarksService.getDcsRemarks(dcsRemarkRequest, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getDcsRemarks Response : traceId=[{}] | timeTaken=[{}] | body=[{}]", traceId, timeTaken, remarkInfoList);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, remarkInfoList));
	}
}
