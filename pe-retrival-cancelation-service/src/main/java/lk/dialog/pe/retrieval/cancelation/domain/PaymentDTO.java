package lk.dialog.pe.retrieval.cancelation.domain;

import lombok.Data;

@Data
public class PaymentDTO {

	private long trxID;
	private int accountPaymentStatus;
	private String createdDtm;
	private String accountPaymentText;
	private String accountPaymentRef;
	private long accountPaymentSeq;
	private String cancelledDtm;
	private String cancelledByUser;
	private String accountNo;
	private String contractNo;
	private double accountPaymentMny;
	private String accountPaymentDate;
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private String receiptUser;
	private String receiptDate;
	private String paymentMode;
	private String paymentRefNo;
	private String remarks;
	private String statusReason;
	private String paymentReversalType;
	private String paymentCancelledReason;
	private int accountType;
	private int sbu;
	private String transferredNo;
	private String transferredMode;
	private double transferredAmount;
	private String chequebankCode;
	private String chequebankBranchCode;
	private String chequeNo;
	private String chequeDate;
	private String chequeSuspend;
	private String ccType;
	private String ccBankCode;
	private String ccNo;
	private String ccApprovalCode;
	private int productType;
	private String paymentCurrency;
	private int paymentMethodID;
	private String terminalID;
	private long physicalPaymentSeq;
	private String connRef;
	private String transferredTo;

}
