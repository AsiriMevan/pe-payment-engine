package lk.dialog.pe.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DBNPayment {

	@Id
	@Column(name = "REQ_ID")
	private Long reqId;

	@Column(name = "ACCOUNT_NO")
	private String accountNo;

	@Column(name = "CONTRACT_ID")
	private String contractNo;

	@Column(name = "CONN_REF")
	private String connRef;

	@Column(name = "CONNECTION_TYPE")
	private String accountType;

	@Column(name = "PAYMENT_AMOUNT")
	private Double paymentAmount;

	@Column(name = "PAYMENT_METHOD_ID")
	private Long paymentMethodId;

	@Column(name = "PAYMENT_MODE")
	private String paymentMode;

	@Column(name = "PAYMENT_REF")
	private String paymentRef;

	@Column(name = "CHQ_BANK")
	private String chqBank;

	@Column(name = "CHQ_NO")
	private String chqNo;

	@Column(name = "CHQ_BRANCH")
	private String chqBranch;

	@Column(name = "CHQ_DATE")
	private Date chqDate;

	@Column(name = "CHQ_SUSPEND")
	private String chqSuspend;

	@Column(name = "CARD_TYPE")
	private String cardType;

	@Column(name = "CARD_AGENT")
	private String cardAgent;

	@Column(name = "CARD_NO")
	private String cardNo;

	@Column(name = "CARD_APRV_CODE")
	private String cardAprvCode;

	@Column(name = "RECEIPT_NUMBER")
	private String receiptNo;

	@Column(name = "RECEIPT_BRANCH")
	private String receiptBranch;

	@Column(name = "BRANCH_COUNTER")
	private String branchCounter;

	@Column(name = "RECEIPT_USER")
	private String receiptUser;

	@Column(name = "RECEIPT_DATE")
	private Date receiptDate;

	@Column(name = "TRANSFERRED")
	private Integer transferredType;

	@Column(name = "TRANSFER_MODE")
	private String transferredMode;

	@Column(name = "TRANSFER_NO")
	private String transferredNo;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "CONTACT_NO")
	private String contactNo;

	@Column(name = "REASON_CODE")
	private String transferReasonCode;

	@Column(name = "PHYSICAL_PAYMENT_DATE")
	private Date physicalPaymentDate;

	@Column(name = "PRODUCT_TYPE")
	private Integer productType;

	@Column(name = "TERMINAL_ID")
	private String terminalId;

	@Column(name = "MODULE_NAME")
	private String moduleName;

	@Column(name = "INVOICE_LIST")
	private String invoiceList;

	@Column(name = "CONNECTION_STATUS")
	private String connectionStatus;

}
