package lk.dialog.pe.ccbs.util;

public enum RBMPaymentEnum {
	USER("USER"),
	COMMAND_READ("N");
	
	private final String name;

	private RBMPaymentEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
