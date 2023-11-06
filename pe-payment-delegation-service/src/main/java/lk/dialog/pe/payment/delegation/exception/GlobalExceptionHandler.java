package lk.dialog.pe.payment.delegation.exception;

import lk.dialog.pe.payment.delegation.util.DCPEUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lk.dialog.pe.common.exception.DCPEException;
import lk.dialog.pe.common.util.BaseResponse;
import lk.dialog.pe.common.util.DCPEErrorEnum;
import org.springframework.web.context.request.WebRequest;

//Generic Exception handler

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private static final String STR_TRACE_ID = "traceId";

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse> exceptionHandler(Exception ex, WebRequest request) {
		String traceId = request.getParameter(STR_TRACE_ID);
		LOGGER.error("Global Error : traceId={}|message={}|error={}",traceId, ex.getMessage(), ex.getStackTrace());

		BaseResponse error = new BaseResponse();
		error.setStatus(DCPEErrorEnum.FAIL.getStatus());
		error.setErrorCode(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		error.setErrorDesc(ex.getMessage());

		String requestString = DCPEUtil.convertToString(error);
		LOGGER.error("Global Error : traceId={}|baseResponse={}",traceId, requestString);
	
		return new ResponseEntity<>(error,HttpStatus.OK);

	}

	@ExceptionHandler(DCPEException.class)
	public ResponseEntity<BaseResponse> dcpeExceptionHandler(DCPEException dcpeEx, WebRequest request) {
		String traceId = request.getParameter(STR_TRACE_ID);
		LOGGER.error("Global DCPE Error : traceId={}|message={}|error={}",traceId, dcpeEx.getMessage(), dcpeEx.getStackTrace());

		BaseResponse baseResponse = new BaseResponse();

		baseResponse.setStatus(dcpeEx.getErrorCode());
		baseResponse.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		baseResponse.setErrorDesc(dcpeEx.getErrorMessage());

		String requestString = DCPEUtil.convertToString(baseResponse);
		LOGGER.error("Global DCPE Error : traceId={}|baseResponse={}",traceId, requestString);

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}

	@ExceptionHandler(ConnectionStatusException.class)
	public ResponseEntity<BaseResponse> connectionStatusExceptionHandler(ConnectionStatusException csEx, WebRequest request) {
		String traceId = request.getParameter(STR_TRACE_ID);
		LOGGER.error("Global ConnectionStatus Error : traceId={}|message={}|error={}",traceId, csEx.getMessage(), csEx.getStackTrace());

		BaseResponse baseResponse = new BaseResponse();

		baseResponse.setStatus(DCPEErrorEnum.FAIL.getStatus());
		baseResponse.setErrorCode(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		baseResponse.setErrorDesc(csEx.getMessage());

		String requestString = DCPEUtil.convertToString(baseResponse);
		LOGGER.error("Global ConnectionStatus Error : traceId={}|baseResponse={}",traceId, requestString);
		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}
}
