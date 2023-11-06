package lk.dialog.pe.billing.domain;

import lombok.Data;

@Data
public class RealTimeBalance extends Balance {

	private Integer creditStatus;	
	private Double oustanding ;	
	private String cxCatagory;	
	private Integer paidMode ;	
	private Double totalCredit;	
	private Double balance;	
	
}
