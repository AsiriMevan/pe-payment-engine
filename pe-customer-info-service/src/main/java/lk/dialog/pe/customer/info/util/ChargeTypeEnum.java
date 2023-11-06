package lk.dialog.pe.customer.info.util;

public enum ChargeTypeEnum {

	POSTPAID(2),
	PREPAID(1);
	
	private final int type;
	
	ChargeTypeEnum(int type) {
		this.type=type;
	}

	public int getType() {
		return type;
	}
	
}
