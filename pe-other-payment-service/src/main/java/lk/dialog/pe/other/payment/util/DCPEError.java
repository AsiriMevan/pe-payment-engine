package lk.dialog.pe.other.payment.util;

public enum DCPEError {

	SUCCESS(1), FAIL(0), TRANSFER_FAIL(2);

	public final int status;

	private DCPEError(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

}