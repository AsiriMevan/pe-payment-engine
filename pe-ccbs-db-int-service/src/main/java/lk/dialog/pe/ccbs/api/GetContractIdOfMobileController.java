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
import lk.dialog.pe.ccbs.service.GetContractIdOfMobileService;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;

@RestController
public class GetContractIdOfMobileController {

	@Autowired
	private GetContractIdOfMobileService getContractIdOfMobileService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GetContractIdOfMobileController.class);
	
	/**
	 * Get contract id by mobile
	 * 
	 * @param mobileNumber
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@GetMapping("/contractId-of-mobile/{mobileNumber}")
	@ApiOperation(value = "Get contractId by mobile", notes = "Retrieve contractId by by mobile. The mobileNumber and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Mobile Number", paramType = "path", name = "mobileNumber", required = true, dataType = "string"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getContractIdOfMobile (@PathVariable String mobileNumber, @RequestParam String traceId) throws DCPEException{
		
		String requestString = String.valueOf(mobileNumber);		
		Instant start = Instant.now();		
		LOGGER.info("getContractIdOfMobileRequest : traceId={}|mobileNo={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		
		String contractNo = getContractIdOfMobileService.getContractIdOfMobile(mobileNumber, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getContractIdOfMobileResponse : traceId={}|timeTaken={}|contractId={}", traceId, timeTaken, contractNo);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, contractNo));
	}
}
