package lk.dialog.pe.cheque.payment.domain;

public class PaymentPostResponse {

	private int statusCode;
	private String statusDescription;
	private long paymentSeq;
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public long getPaymentSeq() {
		return paymentSeq;
	}
	public void setPaymentSeq(long paymentSeq) {
		this.paymentSeq = paymentSeq;
	}
}