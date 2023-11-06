package lk.dialog.pe.common.dto;

import java.util.Date;

import lombok.Data;
@Data
public class OcsPaymentDto {

	private Long reqId;
	private String accountNo;
	private String contractNo;
	private String connRef;
	private String productCategory;
	private String sbu;
	private String accountType;
	private Date physicalPaymentDate;
	private Double paymentAmount;
	private String paymentRef;
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private String receiptUser;
	private Date receiptDate;
	private String paymentMode;
	private String chqNo;
	private String chqBank;
	private String chqBranch;
	private String remarks;
	private Date createdDate;
	private String createdUser;
	private String commandRead;
	private Long productType;
	private String contactNo;
	private String transferReasonCode;
	private Integer tranTypeId;
	private String errorDesc;
	private String cardNo;
	private String cancelledReason;
}
