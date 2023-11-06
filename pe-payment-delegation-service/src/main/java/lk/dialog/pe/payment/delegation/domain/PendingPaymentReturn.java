package lk.dialog.pe.payment.delegation.domain;

import java.util.Date;

import lombok.Data;
@Data
public class PendingPaymentReturn {

	private Long id;	
	private String connRef;		
    private String contractNo;  
	private Date cancelledDtm;		
    private String paymentCancelledReason;
    private String paymentCancelledUser;	
    private String accountNo;
    private Double accountPaymentMny;	
    private String receiptNo;	
    private String receiptBranch;	
    private String branchCounter;	
    private String receiptUser;   
    private Date receiptDate;
    private String paymentMode;   
    private int accountType;
    private int sbu;
    private Integer productType;	
	private String transferErrDesc;
	private Date lastTransferRecordedTime;
	private Date updateDate;
	
	}
