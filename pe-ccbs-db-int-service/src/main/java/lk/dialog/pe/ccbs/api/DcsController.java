package lk.dialog.pe.ccbs.api;

import io.swagger.annotations.*;
import lk.dialog.pe.ccbs.dto.DCPEResponse;
import lk.dialog.pe.ccbs.dto.DcsProfile;
import lk.dialog.pe.ccbs.service.DcsService;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RequestMapping("/dcs")
@RestController
public class DcsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DcsController.class);

	@Autowired
	private DcsService dcsService;
	
	/**
	 * Get dsc mobile no by contract id
	 * 
	 * @param contractId
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-mobileNo-by-contractNo/{contractId}")
	@ApiOperation(value = "Get dsc mobile no by contract id", notes = "Get dsc mobile no by contract id.The contractId and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Contract Id", name = "contractId", paramType = "path", dataType = "string", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getDcsMobileByContract(@PathVariable String contractId, @RequestParam String traceId) throws DCPEException{
		
		Instant start = Instant.now();		
		LOGGER.info("getDcsMobileByContract Request : traceId=[{}] | contractId=[{}]", traceId, contractId);
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId=[{}]", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}

		List<DcsProfile> dcsProfileList = dcsService.getDcsMobileByContract(contractId, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getDcsMobileByContract Response : traceId=[{}] | timeTaken=[{}] | dcsProfileList=[{}]", traceId, timeTaken, dcsProfileList);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, dcsProfileList));
	}
	
}
