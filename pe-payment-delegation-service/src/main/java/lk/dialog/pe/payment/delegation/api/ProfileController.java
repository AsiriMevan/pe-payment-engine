package lk.dialog.pe.payment.delegation.api;

import java.io.IOException;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.common.util.PaymentPostRequest;
import lk.dialog.pe.common.util.PaymentPostResponse;
import lk.dialog.pe.common.util.ChequeRealizeResponse;
import lk.dialog.pe.common.util.Constants;
import lk.dialog.pe.payment.delegation.dto.ChequeRealizeRequest;
import lk.dialog.pe.payment.delegation.exception.ConnectionStatusException;
import lk.dialog.pe.payment.delegation.exception.PaymentPostException;
import lk.dialog.pe.payment.delegation.service.BillingIntegrationService;
import lk.dialog.pe.payment.delegation.util.APIvalidator;
import lk.dialog.pe.payment.delegation.util.CRMSystemValidator;
import lk.dialog.pe.common.util.JSONSchemaValidator;
import lk.dialog.pe.common.util.PaymentDelegationEnum;


@RestController
@RequestMapping("/pe")
public class ProfileController {

	@Autowired
	private BillingIntegrationService billingIntegrationService;
	
	@Autowired
	private JSONSchemaValidator jsonValidator;

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);

	/**
	 * [IP512] Accept forceful cheque realization for the request
	 * 
	 * @param chequeRealizeRequest
	 * @param traceId
	 * @return ResponseEntity<ChequeRealizeResponse>
	 * @throws DCPEException
	 */
	// serve IP512 Accept forceful cheque realization request
	@PutMapping(value = "/cheque/realize") 
	@ApiOperation(value = " [IP512] Accept forceful cheque realization for the request.", notes = "The request parameter traceId is optional. The request body productCategory, chqBank, chqBranch, chqNo, physicalPaymentDate and receiptsInfo details are  are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Details of forceful cheque realization", name = "chequeRealizeRequest", paramType = "body", dataType = "ChequeRealizeRequest", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })

	public ResponseEntity<ChequeRealizeResponse> forcefulChqRealize(@RequestBody ChequeRealizeRequest chequeRealizeRequest, @RequestParam(required = false) String traceId) throws DCPEException,ProcessingException,IOException,ConnectionStatusException{
		if(traceId==null) traceId = DCPEUtil.generateTraceId();
		String requestString = DCPEUtil.convertToString(chequeRealizeRequest);
		LOGGER.info("forcefulChqRealizeRequest : traceId={}|request{}", traceId, requestString);

		Instant start = Instant.now();
		LOGGER.info("forcefulChqRealizeRequest : traceId={}|receiptInfo={}", traceId, chequeRealizeRequest.getReceiptsInfo());
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		String validationError = jsonValidator.validateJSON(timeTaken,chequeRealizeRequest, PaymentDelegationEnum.JSON_SCHEMA_512.getName());

		if (validationError != null && !validationError.isEmpty()) {
			throw new DCPEException(validationError);
		}

		ChequeRealizeResponse response = billingIntegrationService.forcefulChqRealize(chequeRealizeRequest, traceId);
		
		String responseString = DCPEUtil.convertToString(response);
		LOGGER.info("forcefulChqRealizeResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// serve IP510 Accept payment posting
	@PostMapping(value = "/payment")
	@ApiOperation(value = "[IP510] Accept payment posting for the request.", notes = "The request parameter traceId is optional. The request body productCategory, transferredType, accountNo, contractNo, sbu, accountType, physicalPaymentDate, paymentAmount, paymentCurrency, paymentMethodId, receiptNo,  receiptBranch, branchCounter, receiptUser, receiptDate, paymentMode, moduleName, terminalID, productType and connectionStatus are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({
			@ApiImplicitParam(value = "Details of payment posting", name = "payamentRequest", paramType = "body", dataType = "PaymentPostRequest", required = true),
			@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = false, dataType = "string") })
	public ResponseEntity<PaymentPostResponse> postPayment(@RequestBody PaymentPostRequest payamentRequest,
			@RequestParam(required = false) String traceId) throws DCPEException,ProcessingException,IOException,PaymentPostException {
		if(traceId==null) traceId = DCPEUtil.generateTraceId();
		PaymentPostResponse response = null;

		String requestString = DCPEUtil.convertToString(payamentRequest);
		LOGGER.info("postPaymentRequest : traceId={}|requestString={}", traceId, requestString);
		Instant start = Instant.now();
		//todo - time taken wrong
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		String validationError = jsonValidator.validateJSON(timeTaken,payamentRequest, PaymentDelegationEnum.JSON_SCHEMA_510.getName());

		if (validationError != null && !validationError.isEmpty()) {
			LOGGER.info("validationError : traceId={}|message={}", traceId, validationError);
			throw new DCPEException(validationError);
		}

		//todo - where is TrxID set
		String sysValidateStatus = CRMSystemValidator.isValidSystemAgainstPcSbuPt(payamentRequest.getProductCategory(), payamentRequest.getSbu(), payamentRequest.getProductType(), traceId);
		//todo - trace Id is not passed
		String ip510Status = APIvalidator.validateIP510(payamentRequest);
//todo - no logs and trace ids
		if (!"VALID".equalsIgnoreCase(sysValidateStatus)) {
			throw new DCPEException(sysValidateStatus);
		} else if (!"VALID".equalsIgnoreCase(ip510Status)) {
			throw new DCPEException(ip510Status);
		}

		//todo - wrong variable names | can move to if statement
		final String cheqePayment = Constants.PAY_MODE_CHQ;
		//todo - values should be inverse | why use trim
		if (payamentRequest.getPaymentMode().equalsIgnoreCase(cheqePayment.trim())) {

			response = billingIntegrationService.postChequePayment(payamentRequest, traceId);
			String responseString = DCPEUtil.convertToString(response);
			//todo - time taken wrong
			LOGGER.info("postPaymentResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);

			return new ResponseEntity<>(response, HttpStatus.OK);

		} else {

			response = billingIntegrationService.postOtherPayment(payamentRequest, traceId);
			String responseString = DCPEUtil.convertToString(response);
			//todo - time taken wrong
			LOGGER.info("postPaymentResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responseString);

			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

}
