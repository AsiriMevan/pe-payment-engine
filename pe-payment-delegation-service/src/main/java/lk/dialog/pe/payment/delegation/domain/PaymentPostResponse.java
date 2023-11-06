package lk.dialog.pe.payment.delegation.domain;

import java.util.Objects;

public class PaymentPostResponse extends BaseResponse {
	
		private Long paymentSeq;

		public Long getPaymentSeq() {
			return paymentSeq;
		}

		public void setPaymentSeq(Long paymentSeq) {
			this.paymentSeq = paymentSeq;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = super.hashCode();
			result = prime * result + Objects.hash(paymentSeq);
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
			PaymentPostResponse other = (PaymentPostResponse) obj;
			return Objects.equals(paymentSeq, other.paymentSeq);
		}
		
}
