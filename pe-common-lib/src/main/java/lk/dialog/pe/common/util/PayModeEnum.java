package lk.dialog.pe.common.util;

public enum PayModeEnum {

	PAY_MODE_CARD("CC"), PAY_MODE_CHQ("CHE"), PAYMENT_PREFIX("PAYMENT - ");

	private String mode;

	private PayModeEnum(String mode) {
		this.mode = mode;
	}

	public String getPayMode() {
		return mode;
	}

}