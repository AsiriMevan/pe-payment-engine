package lk.dialog.pe.ccbs.api;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import lk.dialog.pe.ccbs.service.GetMobileOfContractIdService;
import lk.dialog.pe.common.util.DCPEUtil;

@RestController
public class GetMobileOfContractIdController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetMobileOfContractIdController.class);
	
	@Autowired
	private GetMobileOfContractIdService getMobileOfContractIdService;
	
	/**
	 * Get mobile by contract number
	 * 
	 * @param contractNo
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@GetMapping("/mobile-of-contractId/{contractNo}")
	@ApiOperation(value = "Get mobile by contractNo", notes = "Retrieve mobile by by contractNo. The contractNo and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Contract No", paramType = "path", name = "contractNo", required = true, dataType = "string"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getMobileOfContractId(@PathVariable String contractNo, @RequestParam String traceId) throws DCPEException{
		
		String requestString = String.valueOf(contractNo);		
		Instant start = Instant.now();		
		LOGGER.info("getMobileOfContractIdRequest : traceId={}|contractNo={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus());
			}
		
		String mobileNo = getMobileOfContractIdService.getMobileOfContractId(contractNo, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getMobileOfContractIdResponse : traceId={}|timeTaken={}|mobileNo={}", traceId, timeTaken, mobileNo);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, mobileNo));
	}
	
}
