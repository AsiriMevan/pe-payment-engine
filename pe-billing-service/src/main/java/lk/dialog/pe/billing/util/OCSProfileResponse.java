package lk.dialog.pe.billing.util;

import java.util.ArrayList;
import java.util.List;

import lk.dialog.pe.billing.domain.ConnectionRef;
import lk.dialog.pe.billing.domain.RealTimeBalance;
import lombok.Data;
@Data
public class OCSProfileResponse extends BaseResponse {

	private List<RealTimeBalance> accounts = new ArrayList<>();
	
	private List<ConnectionRef> invalidAccounts;

}
