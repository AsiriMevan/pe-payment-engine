 package lk.dialog.pe.persistence.api;

import java.time.Instant;
import java.util.List;
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
import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.dto.PendingPayment;
import lk.dialog.pe.common.dto.QueryPaymentRequest;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.util.DCPEUtil;
import lk.dialog.pe.persistence.service.PendingPaymentService;

@RestController
@RequestMapping("/pending-payments")
public class PendingPaymentsController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(PendingPaymentsController.class);
	
	@Autowired
	PendingPaymentService pendingPaymentService;
	

	/**
	 * Get pending payments by branch and receiptNo
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/branchId-receiptNo")
	@ApiOperation(value = "Get pending payments by branch and receiptNo", notes = "Retrieve pending payments by branch and receiptNo. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Query Payment Request ", paramType = "body", name = "queryPaymentRequest", required = true, dataType = "QueryPaymentRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getPendingPaymentsByBranchAndReceiptNo(@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		Instant start = Instant.now();		
		LOGGER.info("getPendingPaymentsByBranchAndReceiptNoRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getPendingPaymentsByBranchAndReceiptNoResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage()); }

		
		List<PendingPayment> pendingPayment = pendingPaymentService.getPendingPaymentsByBranchAndReceptNo(queryPaymentRequest,traceId);
				
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responceString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("getPendingPaymentsByBranchAndReceiptNoResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responceString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, pendingPayment));		
		
	}	
	
	/**
	 * Get pending payments by receiptNo and date
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/receiptNo-Date")
	@ApiOperation(value = "Get pending payments by receiptNo and date", notes = "Retrieve pending payments by receiptNo and date. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Query Payment Request ", paramType = "body", name = "queryPaymentRequest", required = true, dataType = "QueryPaymentRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getPendingPaymentsByReceiptNoAndDate(@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		Instant start = Instant.now();		
		LOGGER.info("getPendingPaymentsByReceiptNoAndDateRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getPendingPaymentsByReceiptNoAndDateResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage()); 
			}

		
		List<PendingPayment> pendingPayment = pendingPaymentService.getPendingPaymentsByReceiptNoAndDate(queryPaymentRequest,traceId);
		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responceString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("getPendingPaymentsByReceiptNoAndDateResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responceString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, pendingPayment));		
	}
		
	/**
	 * Get pending payments by branch and counter
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/branchId-counter")
	@ApiOperation(value = "Get pending payments by branch and counter", notes = "Retrieve pending payments by branch and counter. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Query Payment Request ", paramType = "body", name = "queryPaymentRequest", required = true, dataType = "QueryPaymentRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getPendingPaymentsByBranchAndCounter(@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		Instant start = Instant.now();		
		LOGGER.info("getPendingPayemntsByBranchAndCounterRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getPendingPaymentsByBranchAndCounterResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage()); 
			}
		
		List<PendingPayment> pendingPayment = pendingPaymentService.getPendingPaymentsByBranchAndCounter(queryPaymentRequest, traceId);		

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responceString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("getPendingPaymentsByReceiptNoAndDateResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responceString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, pendingPayment));		
		
	}	
	
	/**
	 * Get pending payments by cheque branch and cheque No
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/chqBranch-chqNo")
	@ApiOperation(value = "Get pending payments by chq branch and chqNo", notes = "Retrieve pending payments by chq branch and chqNo. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Query Payment Request ", paramType = "body", name = "queryPaymentRequest", required = true, dataType = "QueryPaymentRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getPendingPaymentsByChqBranchAndChqNo(@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		Instant start = Instant.now();		
		LOGGER.info("getPendingPayemntsByChqBranchAndChqNoRequest : traceId={}|QueryPaymentRequest={}", queryPaymentRequest, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getPendingPaymentsByChqBranchAndChqNoResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage()); 
			}
		
		List<PendingPayment> pendingPayment = pendingPaymentService.getPendingPaymentsByChqBranchAndChqNo(queryPaymentRequest, traceId);		

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responceString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("getPendingPayemntsByChqBranchAndChqNoResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responceString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, pendingPayment));
		
	}
	
	/**
	 * Get pending payments by cheque No
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/chqNo")
	@ApiOperation(value = "Get pending payments by chqNo", notes = "Retrieve pending payments by chqNo. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Query Payment Request ", paramType = "body", name = "queryPaymentRequest", required = true, dataType = "QueryPaymentRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getPendingPaymentsByChqNumber(@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		Instant start = Instant.now();		
		LOGGER.info("getPendingPayemntsByChqNumberRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getPendingPayemntsByChqNumberResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage()); 
		}
		
		List<PendingPayment> pendingPayment = pendingPaymentService.getPendingPaymentsByChqNumber(queryPaymentRequest, traceId);			
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responceString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("getPendingPayemntsByChqNumberResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responceString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, pendingPayment));
		
	}
	
	/**
	 * Get pending payments by fromDate and toDate
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/fromDate-toDate")
	@ApiOperation(value = "Get pending payments by fromDate and toDate", notes = "Retrieve pending payments by fromDate and toDate. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Query Payment Request ", paramType = "body", name = "queryPaymentRequest", required = true, dataType = "QueryPaymentRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getPendingPaymentsByFromDateToDate(@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		Instant start = Instant.now();		
		LOGGER.info("getPendingPayemntsByFromDateToDateRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getPendingPayemntsByFromDateToDateResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage()); 
		}
		
		List<PendingPayment> pendingPayment = pendingPaymentService.getPendingPaymentsByFromDateToDate(queryPaymentRequest, traceId);		

		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		String responceString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("getPendingPayemntsByFromDateToDateResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responceString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, pendingPayment));
		
	}
	
	/**
	 * Get pending payments by user branch
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/user-branch")
	@ApiOperation(value = "Get pending payments by user branch", notes = "Retrieve pending payments by user branch. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Query Payment Request ", paramType = "body", name = "queryPaymentRequest", required = true, dataType = "QueryPaymentRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getPendingPaymentsByUserBranch(@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		Instant start = Instant.now();		
		LOGGER.info("getPendingPayemntsByUserBranchRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getPendingPayemntsByUserBranchResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage()); 
		}
		
		List<PendingPayment> pendingPayment = pendingPaymentService.getPendingPaymentsByUserBranch(queryPaymentRequest, traceId);		

		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		String responceString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("getPendingPayemntsByUserBranchResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responceString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, pendingPayment));
		
	}
	
	/**
	 * Get pending payments by user date
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/user-date")
	@ApiOperation(value = "Get pending payments by user date", notes = "Retrieve pending payments by user date. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Query Payment Request ", paramType = "body", name = "queryPaymentRequest", required = true, dataType = "QueryPaymentRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getPendingPaymentsByUserDate(@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		Instant start = Instant.now();		
		LOGGER.info("getPendingPaymentsByUserDateRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getPendingPaymentsByUserDateResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage()); 
		}
		
		List<PendingPayment> pendingPayment = pendingPaymentService.getPendingPaymentsByUserDate(queryPaymentRequest, traceId);		

		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		
		String responceString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("getPendingPaymentsByUserDateResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responceString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, pendingPayment));
		
	}
	
	/**
	 * Get pending payments by branch from to date
	 * 
	 * @param queryPaymentRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/branch-fromTodate")
	@ApiOperation(value = "Get pending payments by branch from to date", notes = "Retrieve pending payments by branch by to date. The request body and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Query Payment Request ", paramType = "body", name = "queryPaymentRequest", required = true, dataType = "QueryPaymentRequest"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getPendingPaymentsByBranchFromToDate(@RequestBody QueryPaymentRequest queryPaymentRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(queryPaymentRequest);
		Instant start = Instant.now();		
		LOGGER.info("getPendingPaymentsByBranchFromToDateRequest : traceId={}|QueryPaymentRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getPendingPaymentsByBranchFromToDateResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage()); 
		}
		
		List<PendingPayment> pendingPayment = pendingPaymentService.getPendingPaymentsByBranchfromToDate(queryPaymentRequest, traceId);		

		
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String responceString = DCPEUtil.convertToString(pendingPayment);
		LOGGER.info("getPendingPaymentsByBranchFromToDateResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, responceString);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, pendingPayment));
		
	}
			
}
