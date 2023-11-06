package lk.dialog.pe.persistence.api;

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
import io.swagger.annotations.ApiResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiOperation;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.persistence.service.NextPaymentService;

@RestController
@RequestMapping("/next-payment-sequence")
public class NextPaymentSequenceController {

	@Autowired
	private NextPaymentService nextPaymentService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NextPaymentSequenceController.class);
	
	/**
	 * Get next-payment-sequence for the request
	 * 
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@GetMapping
	@ApiOperation(value = "Get next-payment-sequence for the request.", notes = "The request parameter traceId is mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> nextPaymentSequence(@RequestParam String traceId) throws DCPEException {
		
		LOGGER.info("nextPaymentSequenceRequest : traceId={}", traceId);
		Instant start = Instant.now();
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
		LOGGER.error("nextPaymentSequenceResponse : Invalid Trace Id traceId={}", traceId);
		throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),DCPEErrorCode.INVALID_TRACE_ID.getStatus()); 
		}
		
		Long nextSeqId = nextPaymentService.nextPaymentSequence(traceId);
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("nextPaymentSequenceResponse : traceId={}|timeTaken={}|params={}", traceId, timeTaken, nextSeqId);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, nextSeqId));		
	}
}
