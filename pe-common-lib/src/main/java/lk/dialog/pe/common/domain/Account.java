package lk.dialog.pe.common.domain;

import lombok.Data;

@Data
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

}
