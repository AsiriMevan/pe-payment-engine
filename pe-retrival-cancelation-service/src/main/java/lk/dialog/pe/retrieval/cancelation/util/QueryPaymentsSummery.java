package lk.dialog.pe.retrieval.cancelation.util;

import java.util.Date;

import lombok.Data;
@Data
public class QueryPaymentsSummery {

	private Date receiptFromDate;
	private Date receiptToDate;
	private String receiptBranch;
	private String branchCounter;
	private String contractNo;
	private String chequeBankCode;
	private String chequeBankBranchCode;
	private String chequeNo;
	private String user;
	private String receiptNo;

}
