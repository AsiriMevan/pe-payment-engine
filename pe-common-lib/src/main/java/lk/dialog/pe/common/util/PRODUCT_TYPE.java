package lk.dialog.pe.common.util;

import java.util.Arrays;

public enum PRODUCT_TYPE {

	OTHER(0), WIFI(1), NFC(2), CDMA(3), LTE(4), VOLTE(5), DCS(6);

	private final Integer value;

	private PRODUCT_TYPE(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static PRODUCT_TYPE getProductTypeByValue(Integer value) {
		return Arrays.asList(values()).stream().filter(val -> val.getValue().equals(value)).findFirst().orElse(null);
	}

	public boolean equalProductType(int value) {
		return this.value == value;
	}

	public boolean orEqual(PRODUCT_TYPE... compareWith) {
		return Arrays.stream(compareWith).anyMatch(val -> val == this);
	}
}