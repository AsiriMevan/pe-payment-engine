package lk.dialog.pe.customer.info.util;

public enum HybridStatusEnum {

	NFC_HYBRID(2);

	public final int status;
	
	HybridStatusEnum(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
	
}
