package lk.dialog.pe.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
@Data
@JsonInclude(Include.NON_NULL)
public class BaseResponse {

	private int status;
	private String errorCode;
	private String errorDesc;

}
