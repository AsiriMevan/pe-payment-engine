package lk.dialog.pe.common.util;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class CancelPaymentRequest {

	private Long reqId;
	private Integer productCategory = null;
	private int sbu;	
	private Integer querySystem;
	private String accountNo;	
	private String contractNo;	
	private Long physicalSeq = null;	
	private Long accountSeq = null;
	private String chqReturn;
	private String chqSuspend;
	private String reversalType;	
	private Long transferredType = null;
	private String transferredMode;	
	private String transferredNo = null;	
	private String transferredRef;	
	private String mistakeBy;
	private String remarks;
	private String cancelledReason;	
	private String cancelledUser;	
	private String approvedUser;		
	private Integer productType;	
	private String receiptNo;	
	private String receiptBranch;	
	private String branchCounter;	
	private String receiptUser;	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = Shape.NUMBER_INT)
	private Date receiptDate;	
	private String chqBank;	
	private String chqNo;	
	private String chqBranch;
	private String terminalID;	
	private String sourceSystem;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",shape = Shape.NUMBER_INT)
	private Date physicalPaymentDate;	
	private Date createdDate;	
	private String createdUser;	
	private CommandReadEnum commandread;	
	private Double paymentAmount = null;	
	@JsonProperty("ConnRef")
	private String connRef;
}
