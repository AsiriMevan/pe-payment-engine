package lk.dialog.pe.customer.info.domain;

import lombok.Data;

@Data
public class RBMCustomer {

	private String id;
	private String idType;
	private String custRef;
	private String custName;
	private String addrLine1;
	private String addrLine2;
	private String addrLine3;
	private String email;
	private String postalCode;
	private String title;
	private String prCustRef;
	private String billRunCode;
	
}
