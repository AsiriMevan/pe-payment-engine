package lk.dialog.pe.persistence.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OcsPayment {

	@Id
	@Column(name = "REQ_ID")
	private Long reqId;

	@Column(name = "CHQ_NO")
	private String chqNo;

	@Column(name = "SUBSCRIBER_NODE_ID")
	private String accountNo;

	@Column(name = "CONTRACT_ID")
	private String contractNo;

	@Column(name = "CONN_REF")
	private String connRef;

	@Column(name = "PRODUCT_CATEGORY")
	private String productCategory;

	@Column(name = "SUBSIDIARY_CODE")
	private String sbu;

	@Column(name = "RECEIPT_NUMBER")
	private String receiptNo;

	@Column(name = "CONNECTION_TYPE")
	private String accountType;

	@Column(name = "PAYMENT_REF")
	private String paymentRef;

	@Column(name = "BRANCH_COUNTER")
	private String branchCounter;

	@Column(name = "RECEIPT_USER")
	private String receiptUser;

	@Column(name = "RECEIPT_DATE")
	private Date receiptDate;

	@Column(name = "PHYSICAL_PAYMENT_DATE")
	private Date physicalPaymentDate;

	@Column(name = "PAYMENT_AMOUNT")
	private Double paymentAmount;

	@Column(name = "PAYMENT_MODE")
	private String paymentMode;

	@Column(name = "CHQ_BANK")
	private String chqBank;

	@Column(name = "CHQ_BRANCH")
	private String chqBranch;

	@Column(name = "REMARKS")
	private String remarks;

	@Column(name = "REASON_CODE")
	private String reasonCode;

	@Column(name = "PRODUCT_TYPE")
	private Long productType;

	@Column(name = "CONTACT_NO")
	private String contactNo;

	@Column(name = "TRAN_TYPE_ID")
	private Integer tranTypeId;

	@Column(name = "ERROR_DESC")
	private String errorDesc;

	@Column(name = "RECEIPT_BRANCH")
	private String receiptBranch;

}
