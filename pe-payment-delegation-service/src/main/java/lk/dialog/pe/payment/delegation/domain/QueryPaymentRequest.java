package lk.dialog.pe.payment.delegation.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lk.dialog.pe.payment.delegation.dto.BaseRequest;
import lk.dialog.pe.payment.delegation.dto.Receipt;

@JsonInclude(Include.NON_NULL)
public class QueryPaymentRequest extends BaseRequest {

	private Date receiptFromDate;
	
	private Date receiptToDate;
	
	private String receiptBranch;
	
	private String branchCounter;
	
	private String contractNo;
	
	private String chequebankCode;
	
	private String chequebankBranchCode;
	
	private String chequeNo;
	
	private String receiptUser;
	
	private List<Receipt> receipts;

	public Date getReceiptFromDate() {
		return receiptFromDate;
	}

	public void setReceiptFromDate(Date receiptFromDate) {
		this.receiptFromDate = receiptFromDate;
	}

	public Date getReceiptToDate() {
		return receiptToDate;
	}

	public void setReceiptToDate(Date receiptToDate) {
		this.receiptToDate = receiptToDate;
	}

	public String getReceiptBranch() {
		return receiptBranch;
	}

	public void setReceiptBranch(String receiptBranch) {
		this.receiptBranch = receiptBranch;
	}

	public String getBranchCounter() {
		return branchCounter;
	}

	public void setBranchCounter(String branchCounter) {
		this.branchCounter = branchCounter;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getChequebankCode() {
		return chequebankCode;
	}

	public void setChequebankCode(String chequebankCode) {
		this.chequebankCode = chequebankCode;
	}

	public String getChequebankBranchCode() {
		return chequebankBranchCode;
	}

	public void setChequebankBranchCode(String chequebankBranchCode) {
		this.chequebankBranchCode = chequebankBranchCode;
	}

	public String getChequeNo() {
		return chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	public String getReceiptUser() {
		return receiptUser;
	}

	public void setReceiptUser(String receiptUser) {
		this.receiptUser = receiptUser;
	}

	public List<Receipt> getReceipts() {
		return receipts;
	}

	public void setReceipts(List<Receipt> receipts) {
		this.receipts = receipts;
	}
	
}
