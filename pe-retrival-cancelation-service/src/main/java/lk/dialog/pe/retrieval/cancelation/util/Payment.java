package lk.dialog.pe.retrieval.cancelation.util;

import lombok.Data;

@Data
public class Payment {
	
	Integer accountPaymentStatus;
	String createdDtm;
	String accountPaymentText;
	String accountPaymentRef;
	Long accountPaymentSeq;
	String cancelledDtm;
	String cancelledByUser;
	String contractNo;
	Double accountPaymentMny;
	String accountPaymentDate;
	String accountNo;
	Long physicalPaymentSeq;
	String receiptNo;
	String receiptBranch;
	String branchCounter;
	String receiptUser;
	String receiptDate;
	String paymentMode;
	String paymentRefNo;
	String remarks;
	String statusReason;
	String paymentReversalType;
	String paymentCancelledReason;
	Integer accountType;
	Integer sbu;
	Integer productType;
	String transferredTo;
	String transferredMode;
	Double transferredAmount;
	String chequebankCode;
	String chequebankBranchCode;
	String chequeNo;
	String chequeSuspend;
	String ccType;
	String ccBankCode;
	String ccNo;
	String ccApprovalCode;

}
