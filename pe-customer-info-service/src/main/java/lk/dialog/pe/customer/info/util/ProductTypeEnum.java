package lk.dialog.pe.customer.info.util;

public enum ProductTypeEnum {

	PT_OTHER(0),
	PT_WIFI(1),
	PT_NFC(2),
	PT_CDMA(3),
	PT_LTE(4),
	PT_VOLTE(5),
	PT_DCS(6);
	
	private final Integer type;

	private ProductTypeEnum(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return type;
	}

}
