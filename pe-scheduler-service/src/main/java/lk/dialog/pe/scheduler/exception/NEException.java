package lk.dialog.pe.scheduler.exception;

/**
 * PE Custom Exception handling class
 * @author Mohan_02392
 *
 */
public class NEException extends Exception{
	private static final long serialVersionUID = 1L;
	private String errorMessage;
 
	public String getErrorMessage() {
		return errorMessage;
	}
	public NEException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	public NEException() {
		super();
	}
}
