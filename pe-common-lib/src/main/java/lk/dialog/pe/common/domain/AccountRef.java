package lk.dialog.pe.common.domain;

import lombok.Data;

@Data
public class AccountRef extends ConnectionRef {

	private Integer sbu = -1;
	private Integer accountType = -1;
	private Integer productType = -1;

}
