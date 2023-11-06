package lk.dialog.pe.ccbs.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RBMPayment {

	@Id
	@Column(name = "REQ_ID")
	private Long reqId;

	@Column(name = "SUBSCRIBER_NODE_ID")
	private String subscriberNodeId;

	@Column(name = "RECEIPT_NUMBER")
	private String receiptNo;

	@Column(name = "RECEIPT_BRANCH")
	private String receiptBranch;

	@Column(name = "BRANCH_COUNTER")
	private String branchCounter;

	@Column(name = "CONTRACT_ID")
	private String contractId;

	@Column(name = "CONN_REF")
	private String connRef;

	@Column(name = "PRODUCT_CATEGORY")
	private String productCategory;

	@Column(name = "SUBSIDIARY_CODE")
	private String sbu;

	@Column(name = "CONNECTION_TYPE")
	private String accountType;

	@Column(name = "PHYSICAL_PAYMENT_DATE")
	private Date physicalPaymentDate;

	@Column(name = "PAYMENT_AMOUNT")
	private Long paymentAmount;

	@Column(name = "PAYMENT_CURRENCY")
	private String paymentCurrency;

	@Column(name = "RECEIPT_USER")
	private String receiptUser;

	@Column(name = "RECEIPT_DATE")
	private Date receiptDate;

	@Column(name = "PAYMENT_METHOD_ID")
	private Long paymentMethodId;

	@Column(name = "PHYSICAL_PAYMENT_TEXT")
	private String physicalPaymentText;

	@Column(name = "PAYMENT_REF")
	private String paymentRef;

	@Column(name = "PAYMENT_MODE")
	private String paymentMode;

	@Column(name = "PAYMENT_TEXT")
	private String paymentText;

	@Column(name = "TERMINAL_ID")
	private String terminalId;

	@Column(name = "CHQ_NO")
	private String chqNo;

	@Column(name = "CHQ_BANK")
	private String chqBank;

	@Column(name = "CHQ_BRANCH")
	private String chqBranch;

	@Column(name = "CHQ_DATE")
	private Date chqDate;

	@Column(name = "CARD_APRV_CODE")
	private String cardAprvCode;

	@Column(name = "MODULE_NAME")
	private String moduleName;

	@Column(name = "TRANSFERRED")
	private Integer transferredType;

	@Column(name = "TRANSFER_MODE")
	private String transferredMode;

	@Column(name = "TRANSFER_NO")
	private String transferredNo;

	@Column(name = "CHQ_SUSPEND")
	private String chqSuspend;

	@Column(name = "CARD_TYPE")
	private String cardType;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "REASON_CODE")
	private String transferReasonCode;

	@Column(name = "PRODUCT_TYPE")
	private Integer productType;

	@Column(name = "CONTACT_NO")
	private String contactNo;

	@Column(name = "CARD_AGENT")
	private String cardAgent;

	@Column(name = "CARD_NO")
	private String cardNo;

	@Column(name = "CONNECTION_STATUS")
	private String connectionStatus;

}
