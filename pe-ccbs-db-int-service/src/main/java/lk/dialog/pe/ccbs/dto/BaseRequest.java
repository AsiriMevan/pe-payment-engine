package lk.dialog.pe.ccbs.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseRequest {
			
		private Integer productCategory = null; 		
		private Long trxID;
		
}
