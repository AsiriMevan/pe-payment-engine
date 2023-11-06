package lk.dialog.pe.retrieval.cancelation.util;

import java.util.List;

import lk.dialog.pe.common.util.BaseResponse;
import lombok.Data;
@Data
public class QueryPaymentResponse extends BaseResponse {	
	List<Payment> payments;
}
