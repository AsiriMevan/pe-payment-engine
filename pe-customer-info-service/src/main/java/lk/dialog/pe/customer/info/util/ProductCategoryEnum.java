package lk.dialog.pe.customer.info.util;

public enum ProductCategoryEnum {

	PC_CCBS(1),
	PC_TELBIZ(2),
	PC_NFC(3),
	PC_PE(4);
	
	public final int category;

	private ProductCategoryEnum(int category) {
		this.category = category;
	}

	public int getCategory() {
		return category;
	}
	
}
