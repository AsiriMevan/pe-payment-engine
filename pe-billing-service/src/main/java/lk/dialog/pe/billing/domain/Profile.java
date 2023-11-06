package lk.dialog.pe.billing.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
@Data
@JsonIgnoreProperties({"profileId"})
public class Profile {

	private String custRef;	
	private String oldCustRef;	
	private String custRefType;	
	private String custName;	
	private String addrLine1;	
	private String addrLine2;	
	private String addrLine3;	
	private String email;	
	private String postalCode;	
	private List<Account> accounts;		
	private String profileId;

}
