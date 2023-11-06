package lk.dialog.pe.customer.info.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RBMAccount {

	private String customerRef;
	private int accountType;	
	private String accountNum;
	@JsonProperty(value = "switchStatus")
	private String conStatus;
	private String disconReasonCode;
	private String disconReason;
	private String productType;
	private String billCycle;
	@JsonProperty(value = "contractSubsidiaryType")
	private String sbu;

	public String getCustomerRef() {
		return customerRef;
	}
	public void setCustomerRef(String customerRef) {
		this.customerRef = customerRef;
	}
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}
	public String getAccountNum() {
		return accountNum;
	}
	public void setAccountNum(String accountNum) {
		this.accountNum = accountNum;
	}
	public String getConStatus() {
		return conStatus;
	}
	public void setConStatus(String conStatus) {
		this.conStatus = conStatus;
	}
	public String getDisconReasonCode() {
		return disconReasonCode;
	}
	public void setDisconReasonCode(String disconReasonCode) {
		this.disconReasonCode = disconReasonCode;
	}
	public String getDisconReason() {
		return disconReason;
	}
	public void setDisconReason(String disconReason) {
		this.disconReason = disconReason;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getBillCycle() {
		return billCycle;
	}
	public void setBillCycle(String billCycle) {
		this.billCycle = billCycle;
	}
	public String getSbu() {
		return sbu;
	}
	public void setSbu(String sbu) {
		this.sbu = sbu;
	}
	
}
