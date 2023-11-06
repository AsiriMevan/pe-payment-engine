package lk.dialog.pe.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

import lombok.Data;

@Data
public class ForcefulRealizeCheques {
	
	@Id
	@Column(name = "REQ_ID")
	private Long reqId;

	@Column(name = "SUBSCRIBER_NODE_ID")
	private String accountNo;
	
	@Column(name = "CONTRACT_ID")
	private String contractId;
	
	@Column(name = "PRODUCT_CATEGORY")
	private String productCategory;
	
	@Column(name = "SUBSIDIARY_CODE")
	private String sbu;
	
	@Column(name = "CONNECTION_TYPE")
	private String accountType;
	
	@Column(name = "PAYMENT_AMOUNT")
	private Double paymentAmount;
	
	@Column(name = "CHQ_BANK")
	private String chqBank;
	
	@Column(name = "CHQ_BRANCH")
	private String chqBranch;
	
	@Column(name = "CHQ_NO")
	private String chqNo;
	
	@Column(name = "CHQ_DATE")
	private Date chqDate;
	
	@Column(name = "TERMINAL_ID")
	private String terminalId;
	
	@Column(name = "RECEIPT_NUMBER")
	private String receiptNo;
	
	@Column(name = "RECEIPT_BRANCH")
	private String receiptBranch;
	
	@Column(name = "BRANCH_COUNTER")
	private String branchCounter;
	
	@Column(name = "RECEIPT_DATE")
	private Date receiptDate;
	
	@Column(name = "RECEIPT_USER")
	private String receiptUser;
	
	@Column(name = "PHYSICAL_PAYMENT_DATE")
	private Date physicalPaymentDate;
	
	@Column(name = "PRODUCT_TYPE")
	private Long productType;
	
	@Column(name = "REALTIME")
	private String realTime;
	
	@Column(name = "CONN_REF")
	private String connRef;
	
	@Column(name = "PAYMENT_REF")
	private String paymentRef;
	
	@Column(name = "REMARKS")
	private String remarks;
	
	@Column(name = "CONNECTION_STATUS")
	private String connectionStatus;

}
