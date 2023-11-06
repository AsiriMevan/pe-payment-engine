package lk.dialog.pe.customer.info.domain;

public class ConnectionRef {

	private String connRef;
	private String contractNo;
	
	public ConnectionRef() {

	} 
	
	public ConnectionRef(String connRef, String contractNo) {
		super();
		this.connRef = connRef;
		this.contractNo = contractNo;
	}

	public String getConnRef() {
		return connRef;
	}

	public void setConnRef(String connRef) {
		this.connRef = connRef;
	}

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	
	
	
	
}
