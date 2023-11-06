package lk.dialog.pe.customer.info.exception;

import lk.dialog.pe.common.util.DCPEUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lk.dialog.pe.common.util.BaseResponse;
import lk.dialog.pe.common.util.DCPEErrorEnum;
import lk.dialog.pe.common.exception.DCPEException;
import org.springframework.web.context.request.WebRequest;
import java.time.Instant;

//Generic Exception handler

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	private static final String STR_TRACE_ID = "traceId";

	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseResponse> exceptionHandler(Exception ex, WebRequest request) {

		String traceId = request.getParameter(STR_TRACE_ID);
		LOGGER.error("Global Error : traceId={}|message={}|error={}", traceId, ex.getMessage(), ex.getStackTrace());
		BaseResponse error = new BaseResponse();
		error.setStatus(DCPEErrorEnum.FAIL.getStatus());
		error.setErrorCode(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()));
		error.setErrorDesc(ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.OK);
	}

	@ExceptionHandler(DCPEException.class)
	public ResponseEntity<BaseResponse> dcpeExceptionHandler(DCPEException dcpeEx, WebRequest request) {
		Instant start = Instant.now();
		Long timeTaken = DCPEUtil.getTimeTaken(start);
		String traceId = request.getParameter(STR_TRACE_ID);

		BaseResponse baseResponse = new BaseResponse();
		baseResponse.setStatus(dcpeEx.getErrorCode());
		baseResponse.setErrorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()));
		baseResponse.setErrorDesc(dcpeEx.getErrorMessage());

		String responseString = DCPEUtil.convertToString(baseResponse);
		LOGGER.error("dcpeExceptionHandlerResponse : traceId={}|timeTaken={}|baseResponse={}", traceId, timeTaken, responseString);

		return new ResponseEntity<>(baseResponse, HttpStatus.OK);
	}
}
