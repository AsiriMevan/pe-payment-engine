package lk.dialog.pe.common.domain;

import java.util.List;

import lombok.Data;

@Data
public class PEProfileRequest extends BaseRequest{

	private List<AccountRef> accounts;
	
}
