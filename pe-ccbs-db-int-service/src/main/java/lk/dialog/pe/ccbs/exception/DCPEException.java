package lk.dialog.pe.ccbs.exception;

public class DCPEException extends Exception {

	private static final long serialVersionUID = 1L;
	private final DCPEErrorCode error;
	private final String message;

	public DCPEException(DCPEErrorCode exceptionError) {
		this.error = exceptionError;
		this.message = exceptionError.getMessage();
	}
	
	public DCPEException(DCPEErrorCode error, String erroMessage) {
		this.error = error;
		this.message = erroMessage;
	}

	public DCPEErrorCode getError() {
		return error;
	}

	@Override
	public String getMessage() {
		return message;
	}

}