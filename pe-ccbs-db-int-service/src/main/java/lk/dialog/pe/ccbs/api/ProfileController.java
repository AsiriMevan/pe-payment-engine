package lk.dialog.pe.ccbs.api;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
import lk.dialog.pe.ccbs.dto.CCBSProfileRequest;
import lk.dialog.pe.ccbs.dto.CRMProfileRequest;
import lk.dialog.pe.ccbs.dto.DCPEResponse;
import lk.dialog.pe.ccbs.dto.Profile;
import lk.dialog.pe.common.util.DCPEErrorCode;
import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.ccbs.service.ProfileService;
import lk.dialog.pe.ccbs.util.DCPEUtil;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);
	@Autowired
	private ProfileService profileService;
	
	/**
	 * Get profiles by invoice-number
	 * 
	 * @param invoiceNo
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-profiles-by-invoiceNo/{invoiceNo}")
	@ApiOperation(value = "Get profiles by invoice-number", notes = "Retrieve profiles by invoice-number. The invoice number and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Invoice Number", paramType = "path", name = "invoiceNo", required = false, dataType = "string"),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getProfilesByInvoiceNo(@PathVariable String invoiceNo, @RequestParam String traceId) throws DCPEException{
		
		Instant start = Instant.now();		
		LOGGER.info("getProfilesByInvoiceNoRequest : traceId={}|invoiceNo={}", traceId, invoiceNo);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getProfilesByInvoiceNoResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); 
			}
		List<Profile> response = null;
		try {
		
		response  = profileService.getProfilesByInvoiceNo(invoiceNo, traceId);

		}catch(Exception e) {
			
		LOGGER.info("getProfilesByInvoiceNo : Exception ", e);

		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByInvoiceNoResponse : traceId={}|timeTaken={}|response={}", traceId, timeTaken, response);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, response));
	}
	
	/**
	 * Get volte-profiles by customer id
	 * 
	 * @param crmProfileRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-volet-profiles-by-id")
	@ApiOperation(value = "Get volte-profiles by customer id", notes = "Retrieve volte-profiles by customer id.The request body code and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the customer id", name = "crmProfileRequest", paramType = "body", dataType = "CRMProfileRequest", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getVolteProfilesById(@RequestBody CRMProfileRequest crmProfileRequest, @RequestParam String traceId) throws DCPEException{
		String requestString = DCPEUtil.convertToString(crmProfileRequest);	
		Instant start = Instant.now();		
		LOGGER.info("getVolteProfilesByIdRequest : traceId={}|getVolteProfilesByIdRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getVolteProfilesByIdResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		List<Profile> volteProfileList = null;
		try {
		volteProfileList = profileService.getVolteProfilesById(crmProfileRequest, traceId);
		}catch(Exception e) {
			LOGGER.error("getVolteProfilesById : Exception ", e);
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getVolteProfilesByIdResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, volteProfileList);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, volteProfileList));				
	}
	
	/**
	 * Get profile by customer id
	 * 
	 * @param crmProfileRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-profile-by-id")
	@ApiOperation(value = "Get profile by customer id", notes = "Retrieve profiles by customer id.The request body code and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the customer id", name = "crmProfileRequest", paramType = "body", dataType = "CRMProfileRequest", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getProfilesById(@RequestBody CRMProfileRequest crmProfileRequest, @RequestParam String traceId) throws DCPEException{
		String requestString = DCPEUtil.convertToString(crmProfileRequest);	
		Instant start = Instant.now();		
		LOGGER.info("getProfilesByIdRequest : traceId={}|requestString={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("getProfilesByIdResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		
		List<Profile> getProfilesListById = null;
		try {
			getProfilesListById = profileService.getProfilesById(crmProfileRequest, traceId);
		}catch (Exception e) {
			LOGGER.error("getProfilesById : Exception ", e);
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByIdResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, getProfilesListById);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, getProfilesListById));				
	}
	
	/**
	 * Get profile by accounts
	 * 
	 * @param ccbsProfileRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-profiles-by-account")
	@ApiOperation(value = "Get profile by accounts", notes = "Retrieve profiles by account.The request body code and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the account", name = "ccbsProfileRequest", paramType = "body", dataType = "CCBSProfileRequest", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getProfilesByAccount(@RequestBody CCBSProfileRequest ccbsProfileRequest, @RequestParam String traceId) throws DCPEException{
		
		String requestString = DCPEUtil.convertToString(ccbsProfileRequest);	
		Instant start = Instant.now();		
		LOGGER.info("getProfilesByAccountRequest : traceId={}|getProfilesByAccountRequest={}", traceId,requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		List<Profile> profileList = null;
		try {
		profileList = profileService.getProfilesByAccount(ccbsProfileRequest.getMobileList() == null ? new ArrayList<>() : ccbsProfileRequest.getMobileList(),ccbsProfileRequest.getContractList(), traceId);
		}catch (Exception e) {
			LOGGER.error("getProfilesByAccount : Exception", e);
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByAccountResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, profileList);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, profileList));				
	}
	
	/**
	 * Get profile by bulk account
	 * 
	 * @param ccbsProfileRequest
	 * @param traceId
	 * @return ResponseEntity<DCPEResponse>
	 * @throws DCPEException
	 */
	@PostMapping("/get-profiles-by-bulk-account")
	@ApiOperation(value = "Get profile by bulk account", notes = "Retrieve profiles by bulk account.The request body code and traceId are mandatory.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@ApiImplicitParams({ @ApiImplicitParam(value = "Details of the bulk account", name = "ccbsProfileRequest", paramType = "body", dataType = "CCBSProfileRequest", required = true),
	@ApiImplicitParam(value = "Trace id of this API requester", paramType = "query", name = "traceId", required = true, dataType = "string") })
	public ResponseEntity<DCPEResponse> getProfilesByBulkAccount(@RequestBody CCBSProfileRequest ccbsProfileRequest, @RequestParam String traceId) throws DCPEException{

		String requestString = DCPEUtil.convertToString(ccbsProfileRequest);	
		Instant start = Instant.now();		
		LOGGER.info("getProfilesByBulkAccountRequest : traceId={}|getVolteProfilesByIdRequest={}", traceId, requestString);
		
		if (!DCPEUtil.traceIdValidation(traceId)) {
			LOGGER.error("traceIdValidationResponse : Invalid Trace Id traceId={}", traceId);
			throw new DCPEException(DCPEErrorCode.INVALID_TRACE_ID.getMessage(),
					DCPEErrorCode.INVALID_TRACE_ID.getStatus()); }
		List<Profile> profileList = null;
		try {
		 profileList = profileService.getProfilesByBulkAccount(ccbsProfileRequest.getMobileList(),ccbsProfileRequest.getContractList(), traceId);
		}catch (Exception e) {
			LOGGER.error("getProfilesByBulkAccountResponse : Exception", e);
		}
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		LOGGER.info("getProfilesByBulkAccountResponse : traceId={}|timeTaken={}|body={}", traceId, timeTaken, profileList);
		return ResponseEntity.ok().body(new DCPEResponse(HttpStatus.OK, profileList));				
	}
}
