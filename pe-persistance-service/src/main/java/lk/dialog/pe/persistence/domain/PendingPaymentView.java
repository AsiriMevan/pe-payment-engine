package lk.dialog.pe.persistence.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "PENDING_PAYMENT_VIEW")
public class PendingPaymentView {

	@javax.persistence.Id
	@Column
	private Long id;

	@Column(name = "CONN_REF")
	private String connRef;

	@Column(name = "CONTRACT_ID")
	private String contractNo;

	@Column(name = "CANCELLED_DATE")
	private Date cancelledDtm;

	@Column(name = "CANCELLED_REASON")
	private String paymentCancelledReason;

	@Column(name = "CANCELLED_USER")
	private String paymentCancelledUser;

	@Column(name = "SUBSCRIBER_NODE_ID")
	private String accountNo;

	@Column(name = "PAYMENT_AMOUNT")
	private Double accountPaymentMny;

	@Column(name = "RECEIPT_DATE")
	private Date receiptDate;

	@Column(name = "RECEIPT_NUMBER")
	private String receiptNo;

	@Column(name = "RECEIPT_BRANCH")
	private String receiptBranch;

	@Column(name = "PAYMENT_MODE")
	private String paymentMode;

	@Column(name = "CONNECTION_TYPE")
	private String accountType;

	@Column(name = "SUBSIDIARY_CODE")
	private String sbu;

	@Column(name = "RBM_ERR_DESC")
	private String rbmErrDecs;

	@Column(name = "BRANCH_COUNTER")
	private String branchCounter;

	@Column(name = "OCS_ERR_CODE")
	private String ocsErrCode;

	@Column(name = "OCS_ERR_DESC")
	private String ocsErrDecs;

	@Column(name = "DBN_ERR_CODE")
	private String dbnErrCode;

	@Column(name = "DBN_ERR_DESC")
	private String dbnErrDecs;

	@Column(name = "RECEIPT_USER")
	private String receiptUser;

	@Column(name = "CANCEL_ERR_CODE")
	private String cancelErrCode;

	@Column(name = "CANCEL_ERR_DESC")
	private String cancelErrDecs;

	@Column(name = "CHQ_NO")
	private String chequeNo;

	@Column(name = "CHQ_BRANCH")
	private String chequebankBranchCode;

	@Column(name = "CHQ_BANK")
	private String chequebankCode;

	@Column(name = "RBM_ERR_CODE")
	private String rbmErrCode;

	@Column(name = "PRODUCT_TYPE")
	private Integer productType;

}
