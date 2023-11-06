/**
 * 
 */
package lk.dialog.pe.scheduler.dto;

import lk.dialog.pe.common.util.BaseResponse;
import lombok.Getter;
import lombok.Setter;

/**
 * Store OCS payment response data
 */
@Getter @Setter
public class PaymentPostResponse extends BaseResponse {

	private Integer responseSeq;


	public PaymentPostResponse(){

	}
	public PaymentPostResponse(Integer responseSeq){
		this.responseSeq = responseSeq;
	}

	public boolean isPaymentSequenceValid(){
		return this.responseSeq !=-1;
	}
}
