package lk.dialog.pe.common.dto;
import java.time.LocalDate;
import java.util.Date;

import lk.dialog.pe.common.util.CommandReadEnum;
import lombok.Data;

@Data
public class CancelPaymentRequestDto {

	private Long reqId;
	private String productCategory;
	private String sbu;
	private Integer querySystem;
	private String accountNo;
	private String contractNo;
	private Long physicalSeq;
	private Long accountSeq;
	private String chqReturn;
	private String chqSuspend;
	private String reversalType;
	private Long transferredType;
	private String transferredMode; 
	private String transferredNo;
	private String transferredRef;
	private String mistakeBy;
	private String remarks;
	private String cancelledReason;
	private String cancelledUser;
	private String approvedUser;
	private Long productType;
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private String receiptUser;
	private Date receiptDate;
	private String chqBank;
	private String chqNo;
	private String chqBranch;
	private String terminalID;
	private String sourceSystem;
	private Date physicalPaymentDate;
	private LocalDate createdDate;
	private String createdUser;
	private CommandReadEnum commandread;
	private Double paymentAmount;

}
