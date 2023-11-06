package lk.dialog.pe.retrieval.cancelation.api;

import java.io.IOException;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.APIvalidator;
import lk.dialog.pe.common.util.CRMSystemValidator;
import lk.dialog.pe.common.util.CancelPaymentRequest;
import lk.dialog.pe.common.util.CancelPaymentResponse;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.JSONSchemaValidator;
import lk.dialog.pe.common.util.PaymentDelegationEnum;
import lk.dialog.pe.retrieval.cancelation.exception.QuerySystemException;
import lk.dialog.pe.retrieval.cancelation.service.PaymentService;

@RestController
@RequestMapping(value = "/pe")
public class CancelPaymentController {

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private JSONSchemaValidator jsonValidator;

	private static final Logger LOGGER = LoggerFactory.getLogger(CancelPaymentController.class);

	/**
	 * [IP511] Accept payment cancellation for the request
	 * 
	 * @param cancelPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<CancelPaymentResponse>
	 * @throws DCPEException
	 */
	// serve IP511 Accept payment cancellation
	@PutMapping(value = "/payment/cancel")
	@ApiOperation(value = "[IP511] Accept payment cancellation for the request.", notes = "The request parameter traceId is optional. The request body productCategory, sbu, querySystem, physicalSeq, cancelledReason, cancelledUser, sourceSystem and productType are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of cancel payment", name = "cancelPaymentRequest", paramType = "body", dataType = "CancelPaymentRequest", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })
	public ResponseEntity<CancelPaymentResponse> cancelPayment(@RequestBody CancelPaymentRequest cancelPaymentRequest, @RequestParam(required = false) String traceId) throws DCPEException,ProcessingException,IOException,QuerySystemException{
		if(traceId==null) traceId = DCPEUtil.generateTraceId();
		String requestString = DCPEUtil.convertToString(cancelPaymentRequest);
		LOGGER.info("cancelPaymentRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		String validationError = jsonValidator.validateJSON(timeTaken, cancelPaymentRequest,PaymentDelegationEnum.JSON_SCHEMA_511.getName());

		if (validationError != null && !validationError.isEmpty()) {
			LOGGER.error("cancelPayment : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(validationError);
		}
		String sysValidateStatus = CRMSystemValidator.isValidSystemAgainstPcSbuPt(cancelPaymentRequest.getProductCategory(), cancelPaymentRequest.getSbu(), cancelPaymentRequest.getProductType());
		String ip511Status = APIvalidator.validateIP511(cancelPaymentRequest);

		if (!"VALID".equalsIgnoreCase(sysValidateStatus)) {
			throw new DCPEException(sysValidateStatus);
		} else if (!"VALID".equalsIgnoreCase(ip511Status)) {
			throw new DCPEException(ip511Status);
		}

		CancelPaymentResponse response = paymentService.cancelPayment(cancelPaymentRequest, traceId);

		
		String responseString = DCPEUtil.convertToString(response);
		LOGGER.info("cancelPaymentResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);
		return new ResponseEntity<>(response, HttpStatus.OK);

	}

}
