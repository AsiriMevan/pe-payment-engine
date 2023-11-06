package lk.dialog.pe.common.domain;

import java.util.List;

import lk.dialog.pe.common.util.BaseResponse;
import lombok.Data;

@Data
public class BillingProfileResponse extends BaseResponse {

	private List<BillingAccount> accounts;
}
