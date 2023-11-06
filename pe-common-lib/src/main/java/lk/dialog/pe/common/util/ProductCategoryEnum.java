package lk.dialog.pe.common.util;

import java.util.Arrays;
import java.util.Optional;

public enum ProductCategoryEnum {

	CCBS(1), TELBIZ(2), NFC(3);

	public final int category;

	private ProductCategoryEnum(int category) {
		this.category = category;
	}

	public static ProductCategoryEnum getProductCategoryByValue(Integer value) {
		return Arrays.asList(values()).stream().filter(val -> val.category == value).findFirst().orElse(null);
	}

	public static Optional<ProductCategoryEnum> getProductCategory(Integer value) {
		return Arrays.asList(values()).stream().filter(m -> m.valueOf().equals(value)).findFirst();
	}

	public Integer valueOf() {
		return this.category;
	}

	public int getCategory() {
		return category;
	}

}
