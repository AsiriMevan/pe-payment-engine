package lk.dialog.pe.ccbs.util;

import java.util.Arrays;
import java.util.Optional;

public enum ProductCategoryEnum {

	CCBS(1),
	TELBIZ(2),
	NFC(3);
	
	private final Integer value;

	private ProductCategoryEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static Optional<ProductCategoryEnum> getProductCategoryByValue(Integer value) {
		return Arrays.asList(values())
				.stream()
				.filter(m -> m.getValue().equals(value))
				.findFirst();
	}
}