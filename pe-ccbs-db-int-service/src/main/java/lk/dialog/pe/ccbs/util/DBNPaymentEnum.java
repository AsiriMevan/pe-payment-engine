package lk.dialog.pe.ccbs.util;

public enum DBNPaymentEnum {
	USER("USER"),
	COMMAND_READ("N");
	
	private final String name;

	private DBNPaymentEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
