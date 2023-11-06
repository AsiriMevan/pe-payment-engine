package lk.dialog.pe.scheduler.dto;

import lombok.Data;

@Data
public class SmsRequest {

	private String moduleId;
	private String phoneNo;
	private String connRef;
	private double amount;
	private String message;
	private String readStatus;
	private String actionDate;


	public void setMessage(){

	}
}
