package lk.dialog.pe.customer.info.exception;

public class DCPEException extends Exception{

	private static final long serialVersionUID = 1L;
	private final String errorMessage;
 
	public String getErrorMessage() {
		return errorMessage;
	}
	public DCPEException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	public DCPEException() {
		super();
		this.errorMessage = null;
	}
	
}
