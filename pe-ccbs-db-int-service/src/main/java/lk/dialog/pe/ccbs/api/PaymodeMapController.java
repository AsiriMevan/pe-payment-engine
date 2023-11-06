package lk.dialog.pe.ccbs.api;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.ccbs.dto.DCPEResponse;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.ccbs.service.PaymodeMapService;
import lk.dialog.pe.ccbs.util.DCPEUtil;

@RestController
@RequestMapping("/PaymodeMap")
public class PaymodeMapController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PaymodeMapController.class);
	
	@Autowired
	private PaymodeMapService paymodeMapService;

	/**
	 * Get pay mode for the request
	 * 
	 * @param cposId
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@GetMapping
	@ApiOperation(value = "Get pay mode for the request.", notes = "The request parameter traceId  and cposId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "CPOS Id", name = "cposId", paramType = "path", dataType = "int", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> paymodeMap(@RequestParam String cposId,@RequestParam String traceId) throws DCPEException {
		
		LOGGER.info("paymodeMapRequest : traceId={}|cposId={}", traceId, cposId);
		Instant start = Instant.now();
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
		LOGGER.error("paymodeMapResponse : Invalid Trace Id traceId={}", traceId);
		throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
				DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		
		Object paymodeMap= paymodeMapService.getPayModeMap(traceId,cposId);
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("paymodeMapResponse : traceId={}| cposId={}| timeTaken={}| params={}", traceId,cposId, timeTaken, paymodeMap);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, paymodeMap));		
	}
}
