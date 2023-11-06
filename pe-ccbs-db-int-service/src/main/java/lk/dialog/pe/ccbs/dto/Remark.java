package lk.dialog.pe.ccbs.dto;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Remark {
	
	String remarks;
	String createdUser;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
	Calendar createdDate = null;	
	
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
	public Calendar getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Calendar createdDate) {
		this.createdDate = createdDate;
	}
	
	
}
