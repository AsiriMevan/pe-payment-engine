package lk.dialog.pe.ccbs.dto;

public class FixedNumberDTO {

	private String crmSystem;

	private String crmContractId;

	private String mobileNo;

	private String paidMode;

	private String connectionStatus;

	private String statusCode;

	private String statusDescription;

	public String getCrmSystem() {
		return crmSystem;
	}

	public void setCrmSystem(String crmSystem) {
		this.crmSystem = crmSystem;
	}

	public String getCrmContractId() {
		return crmContractId;
	}

	public void setCrmContractId(String crmContractId) {
		this.crmContractId = crmContractId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getPaidMode() {
		return paidMode;
	}

	public void setPaidMode(String paidMode) {
		this.paidMode = paidMode;
	}

	public String getConnectionStatus() {
		return connectionStatus;
	}

	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	@Override
	public String toString() {
		return "FixedNumberDTO [crmSystem=" + crmSystem + ", crmContractId=" + crmContractId + ", mobileNo="
				+ mobileNo + ", paidMode=" + paidMode + ", connectionStatus=" + connectionStatus + ", statusCode="
				+ statusCode + ", statusDescription=" + statusDescription + "]";
	}
	
}
