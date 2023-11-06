package lk.dialog.pe.ccbs.util;

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
