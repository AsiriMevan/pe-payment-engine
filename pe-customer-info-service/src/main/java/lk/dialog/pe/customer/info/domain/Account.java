package lk.dialog.pe.customer.info.domain;

public class Account {

	private String connRef;
	private String accountNo;
	private Integer accountType;	
	private String contractNo;
	private String contractEmail;
	private String prCode;
	private String prEmail;
	private Integer hybridFlag;
	private Integer conStatus;
	private String disconReasonCode;
	private String disconReason;
	private Integer productType;
	private String billCycle;
	private Integer sbu;	
	private String contactNo;

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

	public String getContractEmail() {
		return contractEmail;
	}

	public void setContractEmail(String contractEmail) {
		this.contractEmail = contractEmail;
	}

	public String getPrCode() {
		return prCode;
	}

	public void setPrCode(String prCode) {
		this.prCode = prCode;
	}

	public String getPrEmail() {
		return prEmail;
	}

	public void setPrEmail(String prEmail) {
		this.prEmail = prEmail;
	}

	public String getDisconReason() {
		return disconReason;
	}

	public void setDisconReason(String disconReason) {
		this.disconReason = disconReason;
	}
	public String getDisconReasonCode() {
		return disconReasonCode;
	}
	public void setDisconReasonCode(String disconReasonCode) {
		this.disconReasonCode = disconReasonCode;
	}
		
	public String getBillCycle() {
		return billCycle;
	}

	public void setBillCycle(String billCycle) {
		this.billCycle = billCycle;
	}

	public String getConnRef() {
		return connRef;
	}

	public void setConnRef(String connRef) {
		this.connRef = connRef;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public Integer getHybridFlag() {
		return hybridFlag;
	}

	public void setHybridFlag(Integer hybridFlag) {
		this.hybridFlag = hybridFlag;
	}

	public Integer getConStatus() {
		return conStatus;
	}

	public void setConStatus(Integer conStatus) {
		this.conStatus = conStatus;
	}

	public Integer getProductType() {
		return productType;
	}

	public void setProductType(Integer productType) {
		this.productType = productType;
	}

	public Integer getSbu() {
		return sbu;
	}

	public void setSbu(Integer sbu) {
		this.sbu = sbu;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

}
