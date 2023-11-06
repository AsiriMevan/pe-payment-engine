package lk.dialog.pe.retrieval.cancelation.exception;

public class QuerySystemException extends Exception{
	
	private static final long serialVersionUID = 1L;
	private final String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}
	public QuerySystemException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	public QuerySystemException() {
		super();
		this.errorMessage = null;
	}

}
