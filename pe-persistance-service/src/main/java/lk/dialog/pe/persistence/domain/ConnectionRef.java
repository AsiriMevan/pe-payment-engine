package lk.dialog.pe.persistence.domain;

import lombok.Data;

@Data
public class ConnectionRef {

	private String connRef;

	private String contractNo;
	
	public ConnectionRef(){}
	
	public ConnectionRef(String connRef, String contractNo){
		this.connRef = connRef;
		this.contractNo = contractNo;
	}

}
