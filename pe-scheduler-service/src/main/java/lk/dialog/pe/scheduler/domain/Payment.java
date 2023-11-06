package lk.dialog.pe.scheduler.domain;

import java.util.Calendar;

/**
 * payment Data Access Object
 * @author Mohan_02392
 */
public class Payment {
	private int productCategory;
	private long trxID;
	private int accountPaymentStatus;
	private Calendar createdDtm;
	private String accountPaymentText; // PHYSICAL_PAYMENT_TEXT
	private String accountPaymentRef; // PAYMENT_REF
	private long accountPaymentSeq;
	private Calendar cancelledDtm;
	private String cancelledApproveBy;
	private String accountNo;
	private String contractNo;
	private double accountPaymentMny;
	private Calendar accountPaymentDate;
	private String receiptNo;
	private String receiptBranch;
	private String branchCounter;
	private String receiptUser;
	private Calendar receiptDate;
	private String paymentMode;
	private String paymentRefNo; // PAYMENT_TEXT
	private String remarks;
	private String statusReason;
	private String paymentReversalType;
	private String paymentCancelledReason;
	private String cancelledByUser;
	private int accountType;
	private int sbu;
	private String transferredNo;
	private String transferredMode;
	private double transferredAmount;
	private String transferReasonCode;
	private int transferredType;
	private String transferredRef;
	private String chequebankCode;
	private String chequebankBranchCode;
	private String chequeNo;
	private Calendar chequeDate;
	private String chequeReturn;
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
	private String contactNo;
	private boolean realtime;
	private String readStatus;
	private String errCode;
	private String errDesc;
	private String mistakeBy;
	private String ocsReasonCode;
	private String ocsReasonId;
	private int querySystem;
	private int tranType;
	private String invoiceList;
	private String connectionStatus;

	/**
	 * @return the trxID
	 */
	public long getTrxID() {
		return trxID;
	}

	/**
	 * @param trxID the trxID to set
	 */
	public void setTrxID(long trxID) {
		this.trxID = trxID;
	}

	/**
	 * @return the accountPaymentStatus
	 */
	public int getAccountPaymentStatus() {
		return accountPaymentStatus;
	}

	/**
	 * @param accountPaymentStatus the accountPaymentStatus to set
	 */
	public void setAccountPaymentStatus(int accountPaymentStatus) {
		this.accountPaymentStatus = accountPaymentStatus;
	}

	/**
	 * @return the createdDtm
	 */
	public Calendar getCreatedDtm() {
		return createdDtm;
	}

	/**
	 * @param createdDtm the createdDtm to set
	 */
	public void setCreatedDtm(Calendar createdDtm) {
		this.createdDtm = createdDtm;
	}

	/**
	 * @return the accountPaymentText
	 */
	public String getAccountPaymentText() {
		return accountPaymentText;
	}

	/**
	 * @param accountPaymentText the accountPaymentText to set
	 */
	public void setAccountPaymentText(String accountPaymentText) {
		this.accountPaymentText = accountPaymentText;
	}

	/**
	 * @return the accountPaymentRef
	 */
	public String getAccountPaymentRef() {
		return accountPaymentRef;
	}

	/**
	 * @param accountPaymentRef the accountPaymentRef to set
	 */
	public void setAccountPaymentRef(String accountPaymentRef) {
		this.accountPaymentRef = accountPaymentRef;
	}

	/**
	 * @return the accountPaymentSeq
	 */
	public long getAccountPaymentSeq() {
		return accountPaymentSeq;
	}

	/**
	 * @param accountPaymentSeq the accountPaymentSeq to set
	 */
	public void setAccountPaymentSeq(long accountPaymentSeq) {
		this.accountPaymentSeq = accountPaymentSeq;
	}

	/**
	 * @return the cancelledDtm
	 */
	public Calendar getCancelledDtm() {
		return cancelledDtm;
	}

	/**
	 * @param cancelledDtm the cancelledDtm to set
	 */
	public void setCancelledDtm(Calendar cancelledDtm) {
		this.cancelledDtm = cancelledDtm;
	}

	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}

	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	/**
	 * @return the contractNo
	 */
	public String getContractNo() {
		return contractNo;
	}

	/**
	 * @param contractNo the contractNo to set
	 */
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	/**
	 * @return the accountPaymentMny
	 */
	public double getAccountPaymentMny() {
		return accountPaymentMny;
	}

	/**
	 * @param accountPaymentMny the accountPaymentMny to set
	 */
	public void setAccountPaymentMny(double accountPaymentMny) {
		this.accountPaymentMny = accountPaymentMny;
	}

	/**
	 * @return the accountPaymentDate
	 */
	public Calendar getAccountPaymentDate() {
		return accountPaymentDate;
	}

	/**
	 * @param accountPaymentDate the accountPaymentDate to set
	 */
	public void setAccountPaymentDate(Calendar accountPaymentDate) {
		this.accountPaymentDate = accountPaymentDate;
	}

	/**
	 * @return the receiptNo
	 */
	public String getReceiptNo() {
		return receiptNo;
	}

	/**
	 * @param receiptNo the receiptNo to set
	 */
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	/**
	 * @return the receiptBranch
	 */
	public String getReceiptBranch() {
		return receiptBranch;
	}

	/**
	 * @param receiptBranch the receiptBranch to set
	 */
	public void setReceiptBranch(String receiptBranch) {
		this.receiptBranch = receiptBranch;
	}

	/**
	 * @return the branchCounter
	 */
	public String getBranchCounter() {
		return branchCounter;
	}

	/**
	 * @param branchCounter the branchCounter to set
	 */
	public void setBranchCounter(String branchCounter) {
		this.branchCounter = branchCounter;
	}

	/**
	 * @return the receiptUser
	 */
	public String getReceiptUser() {
		return receiptUser;
	}

	/**
	 * @param receiptUser the receiptUser to set
	 */
	public void setReceiptUser(String receiptUser) {
		this.receiptUser = receiptUser;
	}

	/**
	 * @return the receiptDate
	 */
	public Calendar getReceiptDate() {
		return receiptDate;
	}

	/**
	 * @param receiptDate the receiptDate to set
	 */
	public void setReceiptDate(Calendar receiptDate) {
		this.receiptDate = receiptDate;
	}

	/**
	 * @return the paymentMode
	 */
	public String getPaymentMode() {
		return paymentMode;
	}

	/**
	 * @param paymentMode the paymentMode to set
	 */
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}

	/**
	 * @return the paymentRefNo
	 */
	public String getPaymentRefNo() {
		return paymentRefNo;
	}

	/**
	 * @param paymentRefNo the paymentRefNo to set
	 */
	public void setPaymentRefNo(String paymentRefNo) {
		this.paymentRefNo = paymentRefNo;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * @return the statusReason
	 */
	public String getStatusReason() {
		return statusReason;
	}

	/**
	 * @param statusReason the statusReason to set
	 */
	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	/**
	 * @return the paymentReversalType
	 */
	public String getPaymentReversalType() {
		return paymentReversalType;
	}

	/**
	 * @param paymentReversalType the paymentReversalType to set
	 */
	public void setPaymentReversalType(String paymentReversalType) {
		this.paymentReversalType = paymentReversalType;
	}

	/**
	 * @return the paymentCancelledReason
	 */
	public String getPaymentCancelledReason() {
		return paymentCancelledReason;
	}

	/**
	 * @param paymentCancelledReason the paymentCancelledReason to set
	 */
	public void setPaymentCancelledReason(String paymentCancelledReason) {
		this.paymentCancelledReason = paymentCancelledReason;
	}

	/**
	 * @return the accountType
	 */
	public int getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the sbu
	 */
	public int getSbu() {
		return sbu;
	}

	/**
	 * @param sbu the sbu to set
	 */
	public void setSbu(int sbu) {
		this.sbu = sbu;
	}

	/**
	 * @return the transferredMode
	 */
	public String getTransferredMode() {
		return transferredMode;
	}

	/**
	 * @param transferredMode the transferredMode to set
	 */
	public void setTransferredMode(String transferredMode) {
		this.transferredMode = transferredMode;
	}

	/**
	 * @return the chequebankCode
	 */
	public String getChequebankCode() {
		return chequebankCode;
	}

	/**
	 * @param chequebankCode the chequebankCode to set
	 */
	public void setChequebankCode(String chequebankCode) {
		this.chequebankCode = chequebankCode;
	}

	/**
	 * @return the chequebankBranchCode
	 */
	public String getChequebankBranchCode() {
		return chequebankBranchCode;
	}

	/**
	 * @param chequebankBranchCode the chequebankBranchCode to set
	 */
	public void setChequebankBranchCode(String chequebankBranchCode) {
		this.chequebankBranchCode = chequebankBranchCode;
	}

	/**
	 * @return the chequeNo
	 */
	public String getChequeNo() {
		return chequeNo;
	}

	/**
	 * @param chequeNo the chequeNo to set
	 */
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	/**
	 * @return the chequeDate
	 */
	public Calendar getChequeDate() {
		return chequeDate;
	}

	/**
	 * @param chequeDate the chequeDate to set
	 */
	public void setChequeDate(Calendar chequeDate) {
		this.chequeDate = chequeDate;
	}

	/**
	 * @return the chequeSuspend
	 */
	public String getChequeSuspend() {
		return chequeSuspend;
	}

	/**
	 * @param chequeSuspend the chequeSuspend to set
	 */
	public void setChequeSuspend(String chequeSuspend) {
		this.chequeSuspend = chequeSuspend;
	}

	/**
	 * @return the ccType
	 */
	public String getCcType() {
		return ccType;
	}

	/**
	 * @param ccType the ccType to set
	 */
	public void setCcType(String ccType) {
		this.ccType = ccType;
	}

	/**
	 * @return the ccBankCode
	 */
	public String getCcBankCode() {
		return ccBankCode;
	}

	/**
	 * @param ccBankCode the ccBankCode to set
	 */
	public void setCcBankCode(String ccBankCode) {
		this.ccBankCode = ccBankCode;
	}

	/**
	 * @return the ccNo
	 */
	public String getCcNo() {
		return ccNo;
	}

	/**
	 * @param ccNo the ccNo to set
	 */
	public void setCcNo(String ccNo) {
		this.ccNo = ccNo;
	}

	/**
	 * @return the ccApprovalCode
	 */
	public String getCcApprovalCode() {
		return ccApprovalCode;
	}

	/**
	 * @param ccApprovalCode the ccApprovalCode to set
	 */
	public void setCcApprovalCode(String ccApprovalCode) {
		this.ccApprovalCode = ccApprovalCode;
	}

	/**
	 * @return the productType
	 */
	public int getProductType() {
		return productType;
	}

	/**
	 * @param productType the productType to set
	 */
	public void setProductType(int productType) {
		this.productType = productType;
	}

	/**
	 * @return the paymentCurrency
	 */
	public String getPaymentCurrency() {
		return paymentCurrency;
	}

	/**
	 * @param paymentCurrency the paymentCurrency to set
	 */
	public void setPaymentCurrency(String paymentCurrency) {
		this.paymentCurrency = paymentCurrency;
	}

	/**
	 * @return the paymentMethodID
	 */
	public int getPaymentMethodID() {
		return paymentMethodID;
	}

	/**
	 * @param paymentMethodID the paymentMethodID to set
	 */
	public void setPaymentMethodID(int paymentMethodID) {
		this.paymentMethodID = paymentMethodID;
	}

	/**
	 * @return the terminalID
	 */
	public String getTerminalID() {
		return terminalID;
	}

	/**
	 * @param terminalID the terminalID to set
	 */
	public void setTerminalID(String terminalID) {
		this.terminalID = terminalID;
	}

	/**
	 * @return the physicalPaymentSeq
	 */
	public long getPhysicalPaymentSeq() {
		return physicalPaymentSeq;
	}

	/**
	 * @param physicalPaymentSeq the physicalPaymentSeq to set
	 */
	public void setPhysicalPaymentSeq(long physicalPaymentSeq) {
		this.physicalPaymentSeq = physicalPaymentSeq;
	}

	/**
	 * @return the connRef
	 */
	public String getConnRef() {
		return connRef;
	}

	/**
	 * @param connRef the connRef to set
	 */
	public void setConnRef(String connRef) {
		this.connRef = connRef;
	}

	/**
	 * @return the contactNo
	 */
	public String getContactNo() {
		return contactNo;
	}

	/**
	 * @param contactNo the contactNo to set
	 */
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	/**
	 * @return the transferReasonCode
	 */
	public String getTransferReasonCode() {
		return transferReasonCode;
	}

	/**
	 * @param transferReasonCode the transferReasonCode to set
	 */
	public void setTransferReasonCode(String transferReasonCode) {
		this.transferReasonCode = transferReasonCode;
	}

	/**
	 * @return the transferredType
	 */
	public int getTransferredType() {
		return transferredType;
	}

	/**
	 * @param transferredType the transferredType to set
	 */
	public void setTransferredType(int transferredType) {
		this.transferredType = transferredType;
	}

	/**
	 * @return the productCategory
	 */
	public int getProductCategory() {
		return productCategory;
	}

	/**
	 * @param productCategory the productCategory to set
	 */
	public void setProductCategory(int productCategory) {
		this.productCategory = productCategory;
	}

	/**
	 * @return the realtime
	 */
	public boolean isRealtime() {
		return realtime;
	}

	/**
	 * @param realtime the realtime to set
	 */
	public void setRealtime(boolean realtime) {
		this.realtime = realtime;
	}

	/**
	 * @return the readStatus
	 */
	public String getReadStatus() {
		return readStatus;
	}

	/**
	 * @param readStatus the readStatus to set
	 */
	public void setReadStatus(String readStatus) {
		this.readStatus = readStatus;
	}

	/**
	 * @return the errCode
	 */
	public String getErrCode() {
		return errCode;
	}

	/**
	 * @param errCode the errCode to set
	 */
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	/**
	 * @return the errDesc
	 */
	public String getErrDesc() {
		return errDesc;
	}

	/**
	 * @param errDesc the errDesc to set
	 */
	public void setErrDesc(String errDesc) {
		this.errDesc = errDesc;
	}

	/**
	 * @return the chequeReturn
	 */
	public String getChequeReturn() {
		return chequeReturn;
	}

	/**
	 * @param chequeReturn the chequeReturn to set
	 */
	public void setChequeReturn(String chequeReturn) {
		this.chequeReturn = chequeReturn;
	}

	/**
	 * @return the transferredRef
	 */
	public String getTransferredRef() {
		return transferredRef;
	}

	/**
	 * @param transferredRef the transferredRef to set
	 */
	public void setTransferredRef(String transferredRef) {
		this.transferredRef = transferredRef;
	}

	/**
	 * @return the mistakeBy
	 */
	public String getMistakeBy() {
		return mistakeBy;
	}

	/**
	 * @param mistakeBy the mistakeBy to set
	 */
	public void setMistakeBy(String mistakeBy) {
		this.mistakeBy = mistakeBy;
	}

	/**
	 * @return the transferredNo
	 */
	public String getTransferredNo() {
		return transferredNo;
	}

	/**
	 * @param transferredNo the transferredNo to set
	 */
	public void setTransferredNo(String transferredNo) {
		this.transferredNo = transferredNo;
	}

	/**
	 * @return the cancelledApproveBy
	 */
	public String getCancelledApproveBy() {
		return cancelledApproveBy;
	}

	/**
	 * @param cancelledApproveBy the cancelledApproveBy to set
	 */
	public void setCancelledApproveBy(String cancelledApproveBy) {
		this.cancelledApproveBy = cancelledApproveBy;
	}

	/**
	 * @return the ocsReasonCode
	 */
	public String getOcsReasonCode() {
		return ocsReasonCode;
	}

	/**
	 * @param ocsReasonCode the ocsReasonCode to set
	 */
	public void setOcsReasonCode(String ocsReasonCode) {
		this.ocsReasonCode = ocsReasonCode;
	}

	/**
	 * @return the ocsReasonId
	 */
	public String getOcsReasonId() {
		return ocsReasonId;
	}

	/**
	 * @param ocsReasonId the ocsReasonId to set
	 */
	public void setOcsReasonId(String ocsReasonId) {
		this.ocsReasonId = ocsReasonId;
	}

	/**
	 * @return the transferredAmount
	 */
	public double getTransferredAmount() {
		return transferredAmount;
	}

	/**
	 * @param transferredAmount the transferredAmount to set
	 */
	public void setTransferredAmount(double transferredAmount) {
		this.transferredAmount = transferredAmount;
	}

	/**
	 * @return the querySystem
	 */
	public int getQuerySystem() {
		return querySystem;
	}

	/**
	 * @param querySystem the querySystem to set
	 */
	public void setQuerySystem(int querySystem) {
		this.querySystem = querySystem;
	}

	/**
	 * @return the cancelledByUser
	 */
	public String getCancelledByUser() {
		return cancelledByUser;
	}

	/**
	 * @param cancelledByUser the cancelledByUser to set
	 */
	public void setCancelledByUser(String cancelledByUser) {
		this.cancelledByUser = cancelledByUser;
	}

	/**
	 * @return the tranType
	 */
	public int getTranType() {
		return tranType;
	}

	/**
	 * @param tranType the tranType to set
	 */
	public void setTranType(int tranType) {
		this.tranType = tranType;
	}

	public String getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(String invoiceList) {
		this.invoiceList = invoiceList;
	}

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

}
