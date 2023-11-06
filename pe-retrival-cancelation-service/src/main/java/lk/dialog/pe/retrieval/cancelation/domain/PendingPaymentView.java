package lk.dialog.pe.retrieval.cancelation.domain;

import lombok.Data;
@Data
public class PendingPaymentView {

	private String connRef;
	private String contractNo;
	private String cancelledDtm;
	private String paymentCancelledReason;
	private String paymentCancelledUser;
	private String accountNo;
	private Double accountPaymentMny;
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private String receiptUser;
	private String receiptDate;
	private String paymentMode;
	private String paymentRefNo;
	private Integer accountType;
	private Integer sbu;
	private Integer productType;
	private String transferErrDesc;
	private String lastTransferRecordedTime;

}
