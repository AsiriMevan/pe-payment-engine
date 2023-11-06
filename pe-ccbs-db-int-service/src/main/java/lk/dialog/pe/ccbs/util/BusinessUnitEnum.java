package lk.dialog.pe.ccbs.util;

public enum BusinessUnitEnum {

	SBU_ALL(0),
	SBU_DTV_PRE(1),
	SBU_DTV_POST(2),
	SBU_GSM(3),
	SBU_DBN(4);
	
	private final int type;
	
	BusinessUnitEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}
	
}
