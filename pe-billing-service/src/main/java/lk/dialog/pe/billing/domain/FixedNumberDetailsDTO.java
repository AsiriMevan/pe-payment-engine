package lk.dialog.pe.billing.domain;

import lombok.Data;

@Data
public class FixedNumberDetailsDTO {

	private String crmSystem;
	private String crmContractId;
	private String mobileNo;
	private String paidMode;
	private String connectionStatus;
	private String statusCode;
	private String statusDescription;

}
