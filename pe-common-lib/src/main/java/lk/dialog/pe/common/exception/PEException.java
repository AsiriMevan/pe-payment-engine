package lk.dialog.pe.common.exception;

public class PEException extends Exception {

	private static final long serialVersionUID = 1L;
	private String errorMessage;
	 
	public String getErrorMessage() {
		return errorMessage;
	}
	public PEException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	public PEException() {
		super();
	}
	
}