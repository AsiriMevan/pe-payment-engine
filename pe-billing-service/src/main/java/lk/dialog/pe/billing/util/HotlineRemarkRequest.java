package lk.dialog.pe.billing.util;

import java.util.List;

import lk.dialog.pe.billing.domain.AccountRef;
import lk.dialog.pe.billing.domain.BaseRequest;
import lombok.Data;
@Data
public class HotlineRemarkRequest extends BaseRequest {
	
	private List<AccountRef> accounts;
	
}
