package lk.dialog.pe.ccbs.util;

public enum ServiceBusineessUnitEnum {

	SBU_ALL(0),
	SBU_DTV_PRE(1),
	SBU_DTV_POST(2),
	SBU_GSM(3),
	SBU_DBN(4),
	REQ_TYPE_ALL(1),
	REQ_TYPE_LIMITED(0);

	private final Integer value; 

	private ServiceBusineessUnitEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}
		
}
