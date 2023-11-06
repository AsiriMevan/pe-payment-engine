package lk.dialog.pe.payment.delegation.exception;

public class ConnectionStatusException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private final String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	public ConnectionStatusException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	public ConnectionStatusException() {
		super();
		this.errorMessage = null;
	}

}
