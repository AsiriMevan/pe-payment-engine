package lk.dialog.pe.ccbs.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ForcefulRealizeChequesDto {

	private Long reqId;	
	private String subscriberNodeId;	
	private String contractId;	
	private String productCategory;	
	private String subsidiaryCode;	//sbu
	private String connectionType;	//AccountType
	private Double paymentAmount;	
	private String chqBank;	
	private String chqBranch;	
	private String chqNo;	
	private Date chqDate;	
	private String terminalId;	
	private String receiptNumber;	
	private String receiptBranch;
	private String branchCounter;	
	private Date receiptDate;	
	private String receiptUser;	
	private Date physicalPaymentDate;	
	private Long productType;	
	private String realTime;	
	private String connRef;
	private String paymentRef;	
	private String remarks;	
	private String connectionStatus;	
	private String paymentMode;	
	private String cardNo;
}

