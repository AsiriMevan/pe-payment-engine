package lk.dialog.pe.other.payment.api;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.other.payment.service.PaymentService;

import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.PaymentPostResponse;

@RestController
@RequestMapping(value = "/other-payment")
public class OtherPaymentController {
	
	@Autowired
	private PaymentService  paymentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(OtherPaymentController.class);
	
	/**
	 * Post payment  for the request
	 * 
	 * @param jsonReq
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws PEException
	 * @throws DCPEException
	 */
	@PostMapping("/post-payment")
	@ApiOperation(value = "Post payment  for the request.", notes = "The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of payment post", name = "paymentPostRequest", paramType = "body", dataType = "PaymentPostRequest", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> postPayment(@RequestBody PaymentPostRequest jsonReq, @RequestParam String traceId)throws PEException, DCPEException{
		
		String requestString = DCPEUtil.convertToString(jsonReq);
		Instant start = Instant.now();
		LOGGER.info("postPaymentRequest : traceId={}|PostPaymentRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("postPaymentResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException("Invalid Trace Id.", 403); 
			}
		
		PaymentPostResponse jsonResponse = paymentService.postPayment(jsonReq, traceId);
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(jsonResponse);
		LOGGER.info("postPaymentResponse : traceId={}|timeTaken={}|PostPaymentRequest={}", traceId	, timeTaken, responseString);
		
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, jsonResponse));
	}
}