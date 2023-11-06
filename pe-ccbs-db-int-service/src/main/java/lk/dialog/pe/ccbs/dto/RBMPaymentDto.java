package lk.dialog.pe.ccbs.dto;

import java.util.Date;

import lombok.Data;
@Data
public class RBMPaymentDto {	
	
	private Long reqId;	
	private String subscriberNodeId;	
	private String contractId;	
	private String connRef;	
	private String productCategory;	
	private String sbu;
	private String accountType;	
	private Date physicalPaymentDate;	
	private Long paymentAmount;	
	private String paymentCurrency;	
	private Long paymentMethodId;	
	private String physicalPaymentText;	
	private String paymentRef;	
	private String receiptNo;	
	private String receiptBranch;	
	private String branchCounter;
	private String receiptUser;	
	private Date receiptDate;	
	private String paymentMode;	
	private String paymentText;	
	private String terminalId;		
	private String chqNo;	
	private String chqBank;	
	private String chqBranch;	
	private Date chqDate;	
	private String chqSuspend;	
	private String cardType;	
	private String cardAgent;	
	private String cardNo;	
	private String cardAprvCode;	
	private String moduleName;	
	private Integer transferredType;	
	private String transferredMode ;	
	private String transferredNo;	
	private String remarks;	
	private String transferReasonCode;
	private Integer productType;	
	private String contactNo;
	private String connectionStatus;
	
}
