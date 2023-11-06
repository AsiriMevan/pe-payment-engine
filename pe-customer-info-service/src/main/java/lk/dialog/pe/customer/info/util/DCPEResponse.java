package lk.dialog.pe.customer.info.util;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "statusCode", "message", "data" })
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DCPEResponse {

	private int statusCode;
	
	private String message;
	
	private Object data;
	
	public DCPEResponse() {
		super();
	}

	public DCPEResponse(int status, String message) {
		super();
		this.statusCode = status;
		this.message = message;
	}

	public DCPEResponse(HttpStatus status, Object data) {
		super();
		this.statusCode = status.value();
		this.message = status.name();
		this.data = data;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
