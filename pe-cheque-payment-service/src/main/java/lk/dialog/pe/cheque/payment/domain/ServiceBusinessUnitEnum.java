package lk.dialog.pe.cheque.payment.domain;

public enum ServiceBusinessUnitEnum {

	SBU_ALL(0), 
	SBU_DTV_PRE(1),
	SBU_DTV_POST(2),
	SBU_GSM(3), 
	SBU_DBN(4), 

	REQ_TYPE_ALL(1), 
	REQ_TYPE_LIMITED(0);

	private final int unit;
	
	private ServiceBusinessUnitEnum(int unit) {
		this.unit = unit;
	}
	
	public int getUnit() {
		return unit;
	}
	
}
