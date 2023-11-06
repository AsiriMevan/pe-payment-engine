package lk.dialog.pe.ccbs.util;

public enum ProductType {

	OTHER(0), WIFI(1), NFC(2), CDMA(3), VOLTE(5), LTE(4), DCS(6);

	private int value;

	private ProductType(int val) {
		this.value = val;
	}

	public int valueOf() {
		return this.value;
	}

	public static ProductType getProductType(int value) {
		ProductType rt = null;
		for (ProductType e : ProductType.values()) {
			if (e.valueOf() == value) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static ProductType getProductType(String value) {
		ProductType rt = null;
		for (ProductType e : ProductType.values()) {
			if (e.name().equalsIgnoreCase(value)) {
				rt = e;
				break;
			}
		}
		return rt;
	}

}
