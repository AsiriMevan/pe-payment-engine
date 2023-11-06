package lk.dialog.pe.customer.info.util;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

	private int status;
	
	private String errorCode;
	
	private String errorDesc;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errCodeSuccess) {
		this.errorCode = errCodeSuccess;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errDescInvalidPc) {
		this.errorDesc = errDescInvalidPc;
	}
}
