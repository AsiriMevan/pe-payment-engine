package lk.dialog.pe.ccbs.api;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import lk.dialog.pe.ccbs.service.MapService;
import lk.dialog.pe.ccbs.util.DCPEUtil;

@RestController
@RequestMapping("/map")
public class MapController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MapController.class);
	
	@Autowired
	private MapService mapService;
	
	/**
	 * Get CposId for the request
	 * 
	 * @param id
	 * @param isRbm
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@GetMapping("/cpos-paymode/{id}")
	@ApiOperation(value = "Get CposId for the request.", notes = "The traceId, id and isRbm  are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Id", name = "id", paramType = "path", dataType = "string", required = true),
	@ApiImplicitParam(value = "IsRbm", paramType = "query", name = "isRbm", required = true, dataType = "Boolean"),	
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getCposId(@PathVariable String id, @RequestParam(required = true) Boolean isRbm , @RequestParam String traceId) throws DCPEException{
		
		String requestString = String.valueOf(id);		
		Instant start = Instant.now();		
		LOGGER.info("getCposIdRequest : traceId={}|id={}|isRbm={}", traceId, requestString,isRbm);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		
		String cposID = mapService.findCposIdFromPaymodeMap(id,isRbm, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getCposIdResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, cposID);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, cposID));
	}
	
}