package lk.dialog.pe.payment.delegation.domain;

import java.util.Date;

public class Remark {

	private String remarks;
	
	private String createdUser;
	
	Date createdDate = null;

	public String getRemark() {
		return remarks;
	}

	public void setRemark(String remarks) {
		this.remarks = remarks;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	
}
