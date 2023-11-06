package lk.dialog.pe.ccbs.api;

import java.time.Instant;
import java.util.List;

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
import lk.dialog.pe.ccbs.dto.Profile;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.ccbs.service.WifiContractByMobileService;
import lk.dialog.pe.ccbs.util.DCPEUtil;

@RestController
@RequestMapping
public class WifiContractByMobileController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WifiContractByMobileController.class);
	
	@Autowired
	private WifiContractByMobileService wifiContractByMobileService;
	
	/**
	 * Get wifi contract or mobile
	 * 
	 * @param mobile
	 * @param contractID
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@GetMapping("/wifi-contract-by-mobile/{mobile}/{contractID}")
	@ApiOperation(value = "Get wifi-contract-or-mobile", notes = "Retrieve wifi details by contract or mobile. The mobile,contractID and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Mobile", paramType = "path", name = "mobile", required = true, dataType = "string"),
	@ApiImplicitParam(value = "Contract ID", paramType = "path", name = "contractID", required = true, dataType = "string"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> findWifiContractByMobile(@PathVariable(required = true) String mobile, @PathVariable(required = true) Integer contractID , @RequestParam String traceId) throws DCPEException{
				
		Instant start = Instant.now();		
		LOGGER.info("findWifiContractByMobileRequest : traceId={}|mobile={}|contractID={}", traceId, mobile, contractID);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		
		List<Profile> row = wifiContractByMobileService.getWifiContractIdOrMobile(mobile, contractID, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("findWifiContractByMobileResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, row);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, row));
	}
}
