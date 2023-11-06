package lk.dialog.pe.scheduler.api;

import io.swagger.annotations.*;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.scheduler.dto.PaymentDTO;
import lk.dialog.pe.scheduler.dto.QueryPaymentsSummaryRequest;
import lk.dialog.pe.scheduler.service.SoapIntegrationService;
import lk.dialog.pe.scheduler.util.SchUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/soap")
@Slf4j
public class soapController {

	@Autowired
	SoapIntegrationService soapIntegrationService;

	@ApiOperation(value = "Get Query payments summary response from RBM SOAP endpoint", notes = "The request parameter traceId is mandatory")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({@ApiImplicitParam(value = "Trace Id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	@PostMapping(value = "/query-payments-summery")
	public ResponseEntity<DCPEResponse> queryPaymentsSummery(@RequestBody QueryPaymentsSummaryRequest queryPaymentsSummaryRequest,@QueryParam("traceId") String traceId) throws DCPEException {
		Instant start = Instant.now();
		log.info("queryPaymentsSummeryRequest  traceId={}|request={}",traceId,queryPaymentsSummaryRequest);
		if(!DCPEUtil.traceIdValidation(traceId)){
			log.error("queryPaymentsSummeryResponse Invalid traceId={}",traceId);
			throw new DCPEException("Invalid Trace Id");
		}
		MDC.put("traceId",traceId);
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		List<PaymentDTO> response = soapIntegrationService.queryPaymentsSummery(queryPaymentsSummaryRequest);
		String responseString = SchUtil.convertToString(queryPaymentsSummaryRequest);
		log.info("queryPaymentsSummeryResponse : traceId={}|timeTaken={}|response={}",traceId,timeTaken,responseString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, response));
	}


}
