package lk.dialog.pe.common.util;

import java.util.List;

import lk.dialog.pe.common.domain.AccountRef;
import lk.dialog.pe.common.domain.BaseRequest;
import lombok.Data;
@Data
public class HotlineRemarkRequest extends BaseRequest {
	
	private List<AccountRef> accounts;

}
