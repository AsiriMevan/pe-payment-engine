package lk.dialog.pe.billing.domain;

import java.util.List;
import lk.dialog.pe.billing.util.BaseResponse;
import lombok.Data;
@Data
public class BillingProfileResponse extends BaseResponse{

	private List<BillingAccount> accounts;
	
}
