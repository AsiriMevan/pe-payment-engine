package lk.dialog.pe.common.util;

public enum PAY_MODE {

	CHQ("CHE"), CASH("CA"), CARD("CC");

	private String type;

	PAY_MODE(String type) {
		this.type = type;
	}

	public boolean equalPaymodeType(String type) {
		return this.type.equalsIgnoreCase(type);
	}

	public String getValue() {
		return type;
	}

}
