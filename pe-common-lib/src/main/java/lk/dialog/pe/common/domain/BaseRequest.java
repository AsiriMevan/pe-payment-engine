package lk.dialog.pe.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class BaseRequest {
	
	private Integer productCategory = null;
	private Long trxID;

}
