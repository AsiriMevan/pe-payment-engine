package lk.dialog.pe.cheque.payment.domain;

public enum NfcProductCategoryEnum {

	PC_NFC(3), PC_CCBS(1), PC_TELBIZ(2), PC_PE(4);
	
	public final int status;

	private NfcProductCategoryEnum(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}
	
	
}
