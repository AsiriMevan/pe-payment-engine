package lk.dialog.pe.payment.delegation.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {

	private int status;
	private String errorCode;
	private String errorDesc;


}
