package lk.dialog.pe.retrieval.cancelation.domain;

import java.util.List;

import lk.dialog.pe.common.util.BaseResponse;
import lombok.Data;

@Data
public class QueryPendingPaymentResponse extends BaseResponse {

	private List<PendingPaymentView> payments;

	public List<PendingPaymentView> getPayments() {
		return payments;
	}

	public void setPayments(List<PendingPaymentView> payments) {
		this.payments = payments;
	}

}
