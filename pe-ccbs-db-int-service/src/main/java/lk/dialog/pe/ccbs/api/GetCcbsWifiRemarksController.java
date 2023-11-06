package lk.dialog.pe.ccbs.api;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.ccbs.dto.DCPEResponse;
import lk.dialog.pe.ccbs.dto.HotlineRemarkRequest;
import lk.dialog.pe.ccbs.dto.RemarkInfo;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.ccbs.service.GetCcbsWifiRemarksService;
import lk.dialog.pe.ccbs.util.DCPEUtil;

@RestController
public class GetCcbsWifiRemarksController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetCcbsWifiRemarksController.class);
	
	@Autowired
	private GetCcbsWifiRemarksService getCcbsWifiRemarksService;
	
	/**
	 * Get ccbs wifi remarks
	 * 
	 * @param hotlineRemarkRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-ccbs-wifi-remarks")
	@ApiOperation(value = "Get ccbs wifi remarks", notes = "Retrieve ccbs wifi remarks.  The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the Hotline Remark", paramType = "body", name = "hotlineRemarkRequest", required = true, dataType = "HotlineRemarkRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getCcbsWifiRemarks(@RequestBody HotlineRemarkRequest hotlineRemarkRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = String.valueOf(hotlineRemarkRequest);		
		Instant start = Instant.now();		
		LOGGER.info("getCcbsWifiRemarksRequest : traceId={}|wifiContractList={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		
		List<RemarkInfo> wifiContractListInfo = getCcbsWifiRemarksService.getCcbsWifiRemarks(hotlineRemarkRequest, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getCcbsWifiRemarksResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, wifiContractListInfo);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, wifiContractListInfo));
	}
}
