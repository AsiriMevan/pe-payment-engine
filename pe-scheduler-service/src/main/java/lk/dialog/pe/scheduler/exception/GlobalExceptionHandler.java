package lk.dialog.pe.scheduler.exception;

import lk.dialog.pe.common.dto.DCPEResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(value = DCPEException.class)
    public ResponseEntity<DCPEResponse> handleGlobalException(DCPEException ddtException) {

        String message;
        if (ddtException.getMessage() == null) {
            message = ddtException.getErrorMessage();
        } else {
            message = ddtException.getMessage();
        }

        return ResponseEntity.status(HttpStatus.OK).body(new DCPEResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),message));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<DCPEResponse> handleGlobalException(RuntimeException e) {
        log.error("RuntimeException : {}", e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.OK).body(new DCPEResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DCPEResponse> handleGlobalException(Exception e) {
        log.error("Exception : {}", e.getMessage(),e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new DCPEResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
    }

}
