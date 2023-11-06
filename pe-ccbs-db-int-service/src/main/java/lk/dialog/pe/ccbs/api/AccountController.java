package lk.dialog.pe.ccbs.api;

import java.time.Instant;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.ccbs.dto.Account;
import lk.dialog.pe.ccbs.dto.DCPEResponse;
import lk.dialog.pe.ccbs.service.AccountService;
import lk.dialog.pe.ccbs.util.DCPEUtil;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.exception.DCPEException;


@RestController
@RequestMapping("/account")
public class AccountController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private AccountService accountService;
	
	/**
	 * Get account numbers by invoice number
	 * 
	 * @param invoiceNumber
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-account-of-invoiceNo/{invoiceNumber}")
	@ApiOperation(value = "Get account numbers by invoice number", notes = "Retrieve account-numbers by invoice-number. The invoice number and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Invoice Number", paramType = "path", name = "invoiceNo", required = false, dataType = "string"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getAccontNoByInvoiceNo(@PathVariable String invoiceNumber, @RequestParam String traceId) throws DCPEException{
		
		Instant start = Instant.now();		
		LOGGER.info("getAccontNoByInvoiceNoRequest : traceId={}|invoiceNo={}", traceId, invoiceNumber);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); 
		}
		List<Account> accountNoList =null;
		try{
			
		accountNoList = accountService.getAccontNoByInvoiceNo(invoiceNumber, traceId);
		
		}catch (Exception e) {
			LOGGER.error("getAccontNoByInvoiceNo : Exception ", e);
		}

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getAccontNoByInvoiceNoResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, accountNoList);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, accountNoList));
	}

	/**
	 * Get dbn-account-numbers by invoice-number
	 * 
	 * @param invoiceNo
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-dbn-account-of-invoiceNo/{invoiceNo}")
	@ApiOperation(value = "Get dbn-account-numbers by invoice-number", notes = "Retrieve dbn-account-numbers by invoice-number. The invoice number and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Invoice Number", paramType = "path", name = "invoiceNo", required = false, dataType = "string"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getDBNAccountNoByInvoiceNo(@PathVariable String invoiceNo, @RequestParam String traceId) throws DCPEException{
		
		Instant start = Instant.now();		
		LOGGER.info("getDBNAccountNoByInvoiceNoRequest : traceId={}|invoiceNo={}", traceId, invoiceNo);
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}
		
		List<Account>  accountNoList = null;
		try {
		accountNoList = accountService.getDBNAccountNoByInvoiceNo(invoiceNo, traceId);
		}catch (Exception e) {
			LOGGER.info("getDBNAccountNoByInvoiceNo : Exception ", e);
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String getaccountNoList = DCPEUtil.convertToString(accountNoList);
		LOGGER.info("getDBNAccountNoByInvoiceNoResponse : traceId={}|timeTaken={}|getaccountNoList={}", traceId, timeTaken, getaccountNoList);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, accountNoList));
	}

}
