package lk.dialog.pe.payment.delegation.exception;

public class PaymentPostException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private final String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	public PaymentPostException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	public PaymentPostException() {
		super();
		this.errorMessage = null;
	}

}
