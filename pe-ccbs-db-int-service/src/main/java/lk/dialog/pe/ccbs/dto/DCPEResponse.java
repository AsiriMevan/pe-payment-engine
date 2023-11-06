package lk.dialog.pe.ccbs.dto;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
@Data
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

}