package lk.dialog.pe.other.payment.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class BaseRequest {
			
	private Integer productCategory = null; 
	private Long trxID;

}