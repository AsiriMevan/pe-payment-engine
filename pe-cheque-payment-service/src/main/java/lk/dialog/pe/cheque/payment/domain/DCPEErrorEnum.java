package lk.dialog.pe.cheque.payment.domain;

public enum DCPEErrorEnum {

	SUCCESS(1), FAIL(0), TRANSFER_FAIL(2);

	public final int status;

	private DCPEErrorEnum(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}
