package lk.dialog.pe.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BaseRequest {

	private Integer productCategory = null;
	private Long trxID;
}
