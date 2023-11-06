package lk.dialog.pe.other.payment.domain;

import lombok.Data;

@Data
public class PaymentPostResponse {

	private int statusCode;
	private String statusDescription;
	private long paymentSeq;
	
}