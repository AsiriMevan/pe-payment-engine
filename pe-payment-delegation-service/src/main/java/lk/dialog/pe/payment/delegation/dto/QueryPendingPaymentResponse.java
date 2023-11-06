package lk.dialog.pe.payment.delegation.dto;

import java.util.List;
import java.util.Objects;

import lk.dialog.pe.payment.delegation.domain.BaseResponse;
import lk.dialog.pe.payment.delegation.domain.PendingPaymentReturn;


public class QueryPendingPaymentResponse extends BaseResponse {
	
	private List<PendingPaymentReturn> payments;

	public List<PendingPaymentReturn> getPayments() {
		return payments;
	}

	public void setPayments(List<PendingPaymentReturn> payments) {
		this.payments = payments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(payments);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryPendingPaymentResponse other = (QueryPendingPaymentResponse) obj;
		return Objects.equals(payments, other.payments);
	}
		
}
