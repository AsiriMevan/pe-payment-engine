package lk.dialog.pe.scheduler.dto;

import lombok.Data;

@Data
public class OCSTransaction {
	private String serialNo;
	private String msisdn;
	private String action;
	private String amount;
	private String reasonCode;
	private String description;
}
