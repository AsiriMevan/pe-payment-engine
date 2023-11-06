package lk.dialog.pe.billing.domain;

import lombok.Data;

@Data
public class RBMAccountDTO {

	private String customerRef;	
	private int accountType;		
	private String accountNum;	
	private String conStatus;	
	private String disconReasonCode;	
	private String disconReason;	
	private String productType;	
	private String billCycle;	
	private String sbu;
	private double lastBill;	
	private double totalOust;

}
