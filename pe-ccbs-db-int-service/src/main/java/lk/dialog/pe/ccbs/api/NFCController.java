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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lk.dialog.pe.ccbs.dto.Account;
import lk.dialog.pe.ccbs.dto.DCPEResponse;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.ccbs.service.NFCService;
import lk.dialog.pe.ccbs.util.DCPEUtil;

@RequestMapping("/nfc")
@RestController
public class NFCController {

	private static final Logger LOGGER = LoggerFactory.getLogger(NFCController.class);

	@Autowired
	private NFCService nfcService;
	
	/**
	 * Get email by contract id
	 * 
	 * @param contractId
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@GetMapping("/get-email-by-contractId/{contractId}")
	@ApiOperation(value = "Get email by contractId", notes = "Retrieve email by contractId.The contractId and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Contract Id", name = "contractId", paramType = "path", dataType = "string", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getNfcEmailByContract(@PathVariable String contractId, @RequestParam String traceId) throws DCPEException{
		
		Instant start = Instant.now();		
		LOGGER.info("getnfcEmailByContractRequest : traceId={}|contractId={}", traceId, contractId);
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus());
		}
		
		String email = nfcService.getNfcEmailByContract(contractId, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getnfcEmailByContractResponse : traceId={}|timeTaken={}|email={}", traceId, timeTaken, email);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, email));
	}
	
	/**
	 * Get nfc wifi accountNo by invoiceNo
	 * 
	 * @param invoiceNo
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-nfc-wifi-accountNo-by-invoiceNo/{invoiceNo}")
	@ApiOperation(value = "Get nfc wifi accountNo by invoiceNo", notes = "Retrieve nfc wifi accountNo by invoiceNo.The invoiceNo and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Invoice No", name = "invoiceNo", paramType = "path", dataType = "string", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })	
	public ResponseEntity<DCPEResponse> getNfcWifiAccountNoByInvoiceNo(@PathVariable String invoiceNo, @RequestParam String traceId) throws DCPEException{
		
		Instant start = Instant.now();		
		LOGGER.info("getNfcWifiAccountNoByInvoiceNoIdRequest : traceId={}|invoiceNo={}", traceId, invoiceNo);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}|invoiceNo={}", traceId,invoiceNo);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		
		List<Account> accountNoList = nfcService.getNfcWifiAccountNoByInvoiceNo(invoiceNo, traceId);

		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getNfcWifiAccountNoByInvoiceNoResponse : traceId={}|timeTaken={}|accountNoList={}", traceId, timeTaken, accountNoList);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, accountNoList));
	}


	
}
