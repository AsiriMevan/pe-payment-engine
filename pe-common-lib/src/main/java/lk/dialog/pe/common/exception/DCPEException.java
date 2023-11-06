package lk.dialog.pe.common.exception;

public class DCPEException extends Exception {

	private static final long serialVersionUID = 1L;
	private final String errorMessage;
	private final int errorCode;
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
	public DCPEException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.errorCode = 0;
		
	}
	public DCPEException(String errorMessage,Throwable cause) {
		super(errorMessage,cause);
		this.errorMessage = errorMessage;
		this.errorCode = 0;

	}	
	public DCPEException(String errorMessage, int errorCode) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.errorCode = errorCode;
	}
	public DCPEException(String errorMessage, String errorCode) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.errorCode = Integer.parseInt(errorCode);
	}	
	public DCPEException() {
		super("a",new NullPointerException());
		this.errorMessage = "";
		this.errorCode = 0;
	}

	public DCPEException(String contextInfo ,Exception exception) {
		super(contextInfo,exception);
		this.errorMessage = contextInfo;
		this.errorCode = 0;
	}

	public int getErrorCode() {
		return errorCode;
	}
	
	
	

}