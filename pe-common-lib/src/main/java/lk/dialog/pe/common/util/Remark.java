package lk.dialog.pe.common.util;

import java.util.Date;


import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class Remark {
	
	@JsonProperty("remark")
	private String remarks;	
	private String createdUser;	
	private Date createdDate = null;	
}
