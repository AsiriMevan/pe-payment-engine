package lk.dialog.pe.persistence.domain;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;

import lk.dialog.pe.common.util.CommandReadEnum;
import lombok.Data;

@Data
@Entity
@Table(name = "CANCEL_PAYMENT", schema = "CPOS")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancelPayment {

	@Id
	@Column(name = "REQ_ID")
	private Long reqId;

	@Column(name = "PRODUCT_CATEGORY")
	private String productCategory;

	@Column(name = "SUBSIDIARY_CODE")
	private String sbu;

	@Column(name = "QUERY_SYSTEM")
	private String querySystem;

	@Column(name = "SUBSCRIBER_NODE_ID")
	private String accountNo;

	@Column(name = "CONTRACT_ID")
	private String contractNo;

	@Column(name = "RBM_PHYSICAL_SEQ")
	private Long physicalSeq;

	@Column(name = "RBM_ACCOUNT_SEQ")
	private Long accountSeq;

	@Column(name = "CHQ_RETURN")
	private String chqReturn;

	@Column(name = "CHQ_SUSPEND")
	private String chqSuspend;

	@Column(name = "REVERSAL_TYPE")
	private String reversalType;

	@Column(name = "TRANSFERRED")
	private Long transferredType;

	@Column(name = "TRANSFER_MODE")
	private String transferredMode;

	@Column(name = "TRANSFER_NO")
	private String transferredNo;

	@Column(name = "TRANSFERED_REF")
	private String transferredRef;

	@Column(name = "MISTAKE_BY")
	private String mistakeBy;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "CANCELLED_REASON")
	private String cancelledReason;

	@Column(name = "CANCELLED_USER")
	private String cancelledUser;

	@Column(name = "APPROVED_USER")
	private String approvedUser;

	@Column(name = "PRODUCT_TYPE")
	private Long productType;

	@Column(name = "RECEIPT_BRANCH")
	private String receiptBranch;

	@Column(name = "RECEIPT_NUMBER")
	private String receiptNo;

	@Column(name = "RECEIPT_USER")
	private String receiptUser;

	@Column(name = "BRANCH_COUNTER")
	private String branchCounter;
	
	@Column(name = "CHQ_BANK")
	private String chqBank;

	@Column(name = "RECEIPT_DATE")
	private Date receiptDate;

	@Column(name = "CHQ_NO")
	private String chqNo;

	@Column(name = "CHQ_BRANCH")
	private String chqBranch;

	@Column(name = "TERMINAL_ID")
	private String terminalId;

	@Column(name = "SOURCE_SYSTEM")
	private String sourceSystem;

	@Column(name = "CREATED_DATE")
	private LocalDate createdDate;
	
	@Column(name = "PHYSICAL_PAYMENT_DATE")
	private Date physicalPaymentDate;

	@Column(name = "CREATED_USER")
	private String createdUser;

	@Column(name = "COMMAND_READ")
	private CommandReadEnum commandread;

	@Column(name = "PAYMENT_AMOUNT")
	private Double paymentAmount;

}
