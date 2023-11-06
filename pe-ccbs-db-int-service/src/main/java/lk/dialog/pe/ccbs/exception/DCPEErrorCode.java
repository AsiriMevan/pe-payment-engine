package lk.dialog.pe.ccbs.exception;

public enum DCPEErrorCode {

	INTERNAL_SERVER_ERROR("001", 500, "Internal Server Error"), NOT_FOUND("002", 404, "No Data Found"), BAD_REQUEST("003", 403, "Bad Request"), INVALID_TRACE_ID("004", 403, "Invalid Trace Id"),
	DATABASE_ERROR("005", 500, "Databse error"), INVALID_REQUEST("006", 400, "Invalid Request");

	private int status;
	private String code;
	private String message;

	private DCPEErrorCode(String errorCode, int errorStatus, String errorMessage) {
		this.status = errorStatus;
		this.code = errorCode;
		this.message = errorMessage;
	}

	public int getStatus() {
		return status;
	}


	public String getCode() {
		return code;
	}


	public String getMessage() {
		return message;
	}
}
