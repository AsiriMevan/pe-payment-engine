package lk.dialog.pe.common.util;

import java.util.Arrays;
import java.util.Optional;

public enum ProductTypeEnum {

	OTHER(0), WIFI(1), NFC(2), CDMA(3), LTE(4), VOLTE(5), DCS(6);

	private final Integer type;

	private ProductTypeEnum(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return type;
	}

	public static Optional<ProductTypeEnum> getProductTypeById(Integer type) {
		return Arrays.asList(values()).stream().findFirst();
	}
}