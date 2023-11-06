package lk.dialog.pe.common.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@JsonInclude(Include.NON_NULL)
@Data
public class RealTimeBalance extends Balance {

	private Integer creditStatus;	
	private Double oustanding ;	
	private String cxCatagory;	
	private Integer paidMode ;	
	private Double totalCredit;	
	private Double balance;	
	private String status;
}
