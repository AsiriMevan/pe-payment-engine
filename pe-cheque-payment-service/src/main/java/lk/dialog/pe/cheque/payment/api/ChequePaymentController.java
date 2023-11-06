package lk.dialog.pe.cheque.payment.api;

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

import lk.dialog.pe.cheque.payment.service.PaymentService;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.exception.PEException;
import lk.dialog.pe.common.util.ChequeRealizeRequest;
import lk.dialog.pe.common.util.ChequeRealizeResponse;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.PaymentPostResponse;

@RestController
@RequestMapping(value = "/cheque-payment")
public class ChequePaymentController {

	@Autowired
	private PaymentService paymentService;

	private static final Logger LOGGER = LoggerFactory.getLogger(ChequePaymentController.class);

	/**
	 * Post payment  for the request
	 * 
	 * @param paymentPostRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws PEException
	 * @throws DCPEException
	 */
	@PostMapping("/post-payment")
	@ApiOperation(value = "Post payment  for the request.", notes = "The request body mandatory and traceId is optional.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Details of payment post", name = "paymentPostRequest", paramType = "body", dataType = "PaymentPostRequest", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })
	public ResponseEntity<DCPEResponse> postPayment(@RequestBody PaymentPostRequest paymentPostRequest,
			@RequestParam String traceId) throws PEException, DCPEException {

		String requestString = DCPEUtil.convertToString(paymentPostRequest);
		Instant start = Instant.now();
		LOGGER.info("postPaymentRequest : traceId={}|ChequeRealizeRequest={}", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("postPaymentResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),DCPEErrorCode.INVALID_TRACE_ID.getCode());
		}
//todo - where is time taken if any error occured
		PaymentPostResponse jsonResponse = paymentService.postPayment(paymentPostRequest, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(jsonResponse);
		LOGGER.info("postPaymentResponse : traceId={}|timeTaken={}|PostPaymentRequest={}", traceId, timeTaken, responseString);

		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, jsonResponse));
	}

	/**
	 * Forceful cheque realize  for the request
	 * 
	 * @param chequeRealizeRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws PEException
	 * @throws DCPEException
	 */
	@PostMapping("/forceful-cheque-realize")
	@ApiOperation(value = "Forceful cheque realize  for the request.", notes = "The request body mandatory and traceId is optional.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Details of cheque realize", name = "chequeRealizeRequest", paramType = "body", dataType = "ChequeRealizeRequest", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })
	public ResponseEntity<DCPEResponse> forcefulChequeRealize(@RequestBody ChequeRealizeRequest chequeRealizeRequest,
			@RequestParam String traceId) throws PEException, DCPEException {
		String requestString = DCPEUtil.convertToString(chequeRealizeRequest);
		Instant start = Instant.now();
		LOGGER.info("forcefulChequeRealizeRequest : traceId={}|ChequeRealizeRequest={}", traceId, requestString);

		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("forcefulChequeRealizeResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException("Invalid Trace Id.", 400);
		}

		ChequeRealizeResponse jsonResponse = paymentService.forcefulChequeRealize(chequeRealizeRequest, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responseString = DCPEUtil.convertToString(jsonResponse);
		LOGGER.info("forcefulChequeRealizeResponse : traceId={}|timeTaken={}|ChequeRealizeRequest={}", traceId, timeTaken, responseString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, jsonResponse));

	}
}
