package lk.dialog.pe.other.payment.exception;

public class DCPEException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String errorMessage;
	private final int errorCode;

	public DCPEException(String errorMessage, int errorCode) {
		super();
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

}