package lk.dialog.pe.payment.delegation.dto;

import java.util.Calendar;

import lombok.Data;

@Data
public class PendingPaymentNew {

	private Long id;	
	private String connRef;		
    private String contractNo;  
	private Calendar cancelledDtm;	
    private String paymentCancelledReason;
    private String paymentCancelledUser;	
    private String accountNo;
    private Double accountPaymentMny;	
    private String receiptNo;	
    private String receiptBranch;	
    private String branchCounter;	
    private String receiptUser;   
    private Calendar receiptDate;
    private String paymentMode;   
    private String accountType;	
    private String sbu;
    private int productType;   
    
	private String rbmErrDecs;
	private String ocsErrCode; 
	private String ocsErrDecs;
	private String dbnErrCode;  
	private String dbnErrDecs; 
	private String cancelErrCode;
	private String cancelErrDecs;
	private Integer recCount;
	private Integer reqId;
	private String productCategory;
	private String paymentText;
	
}
