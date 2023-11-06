package lk.dialog.pe.persistence.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lk.dialog.pe.common.dto.DCPEResponse;
import lk.dialog.pe.common.util.DCPEErrorEnum;
import lk.dialog.pe.common.exception.DCPEException;
import org.springframework.web.context.request.WebRequest;

//Generic Exception handler

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private static final String STR_TRACE_ID = "traceId";

	@ExceptionHandler(Exception.class)
	public ResponseEntity<DCPEResponse> exceptionHandler(Exception ex, WebRequest request) {
		String traceId = request.getParameter(STR_TRACE_ID);
		LOGGER.error("Global Error : traceId={}|message={}|error={}", traceId, ex.getMessage(), ex.getStackTrace());
		DCPEResponse dcpeResponse = new DCPEResponse();
		dcpeResponse.setStatusCode(DCPEErrorEnum.FAIL.getStatus());
		dcpeResponse.setMessage(ex.getMessage());
		return new ResponseEntity<>(dcpeResponse, HttpStatus.OK);
	}

	@ExceptionHandler(DCPEException.class)
	public ResponseEntity<DCPEResponse> dcpeExceptionHandler(DCPEException dcpeEx, WebRequest request) {
		String traceId = request.getParameter(STR_TRACE_ID);
		LOGGER.error("Global DCPE Error : traceId={}|message={}|error={}", traceId, dcpeEx.getMessage(), dcpeEx.getStackTrace());
		DCPEResponse dcpeResponse = new DCPEResponse();
		dcpeResponse.setStatusCode(dcpeEx.getErrorCode());
		dcpeResponse.setMessage(dcpeEx.getErrorMessage());
		return new ResponseEntity<>(dcpeResponse, HttpStatus.OK);
	}
}
