package lk.dialog.pe.ccbs.api;

import java.time.Instant;

import lk.dialog.pe.ccbs.dto.FixedNumberDTO;
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
import lk.dialog.pe.ccbs.service.CamContractService;
import lk.dialog.pe.ccbs.util.DCPEUtil;

@RestController
@RequestMapping("/cam-contract")
public class CamContractController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CamContractController.class);
	
	@Autowired
	private CamContractService camContractService;
	
	/**
	 * Get contract subsidiary type for the request
	 * 
	 * @param contractId
	 * @param dcsOnly
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@GetMapping("/get-contract-subsidiary-type/{contractId}")
	@ApiOperation(value = "Get contract subsidiary type for the request.", notes = "The traceId, contractId and dcsOnly  are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Contract Id", name = "contractId", paramType = "path", dataType = "string", required = true),
	@ApiImplicitParam(value = "dcsOnly", paramType = "query", name = "dcsOnly", required = true, dataType = "Boolean"),	
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getContractSubsidiaryType(@PathVariable String contractId, @RequestParam(required = true) Boolean dcsOnly , @RequestParam String traceId) throws DCPEException{
		Instant start = Instant.now();	
		String requestString = String.valueOf(contractId);					
		LOGGER.info("getContractSubsidiaryTypeRequest : traceId={}|contractId={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		
		String contractSubsidiaryType= camContractService.findContractSubsidiaryTypeById(contractId,dcsOnly, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getContractSubsidiaryTypeResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, contractSubsidiaryType);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, contractSubsidiaryType));
	}

	@GetMapping("/get-is-system-ccbs-by-contractNo/{contractId}")
	@ApiOperation(value = "Get if the system ccbs for the request by contract no.", notes = "The traceId and contractId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Contract Id", name = "contractId", paramType = "path", dataType = "string", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getSystemCcbsByContractNo(@PathVariable String contractId, @RequestParam String traceId) throws DCPEException{
		Instant start = Instant.now();
		String requestString = String.valueOf(contractId);
		LOGGER.info("getSystemCcbsByContractNoRequest : traceId=[{}]|contractId=[{}]", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId=[{}]", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}

		FixedNumberDTO fixedNumberDTO= camContractService.findSystemCcbsByContractId(contractId,traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getSystemCcbsByContractNoResponse : traceId=[{}]|timeTaken=[{}]|body=[{}]", traceId, timeTaken, fixedNumberDTO);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, fixedNumberDTO));
	}

	@GetMapping("/get-is-system-ccbs-by-mobileNo/{mobileNo}")
	@ApiOperation(value = "Get if the system ccbs for the request by mobile no.", notes = "The traceId and contractId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Mobile No", name = "mobileNo", paramType = "path", dataType = "string", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getSystemCcbsByMobileNo(@PathVariable String mobileNo, @RequestParam String traceId) throws DCPEException{
		Instant start = Instant.now();
		String requestString = String.valueOf(mobileNo);
		LOGGER.info("getSystemCcbsByMobileNoRequest : traceId=[{}]|mobileNo=[{}]", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId=[{}]", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}

		FixedNumberDTO fixedNumberDTO= camContractService.findSystemCcbsByMobileNo(mobileNo,traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getSystemCcbsByMobileNoResponse : traceId=[{}]|timeTaken=[{}]|body=[{}]", traceId, timeTaken, fixedNumberDTO);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, fixedNumberDTO));
	}
	
}
