package lk.dialog.pe.ccbs.dto;

import java.util.List;

import lombok.Data;
@Data
public class HotlineRemarkRequest extends BaseRequest {
	
	private List<AccountRef> accounts;
	
}
