package lk.dialog.pe.cheque.payment.domain;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Receipt {

	private String receiptNo;
	private String connRef;
	private String receiptBranch;
	private String branchCounter;
	private Long receiptDate;
	private String receiptUser;
	private String accountNo;
	private String contractNo;
	private Double paymentAmount = null;
	private SBUEnum sbu;
	private Integer productType;
	
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getConnRef() {
		return connRef;
	}
	public void setConnRef(String connRef) {
		this.connRef = connRef;
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
	public Long getReceiptDate() {
		return receiptDate;
	}
	public void setReceiptDate(Long receiptDate) {
		this.receiptDate = receiptDate;
	}
	public String getReceiptUser() {
		return receiptUser;
	}
	public void setReceiptUser(String receiptUser) {
		this.receiptUser = receiptUser;
	}
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public Double getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	public SBUEnum getSbu() {
		return sbu;
	}
	public void setSbu(SBUEnum sbu) {
		this.sbu = sbu;
	}
			
}
