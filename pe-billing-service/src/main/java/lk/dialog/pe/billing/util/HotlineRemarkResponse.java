package lk.dialog.pe.billing.util;

import java.util.List;

import lombok.Data;
@Data
public class HotlineRemarkResponse extends BaseResponse {
	
	private List<RemarkInfo> accounts;
	
}
