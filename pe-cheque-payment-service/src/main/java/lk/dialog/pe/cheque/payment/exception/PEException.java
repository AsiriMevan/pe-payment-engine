package lk.dialog.pe.cheque.payment.exception;

public class PEException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String errorMessage;
 
	public String getErrorMessage() {
		return errorMessage;
	}
	public PEException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	public PEException() {
		super();
		this.errorMessage = null;
	}
	
}
