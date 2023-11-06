package lk.dialog.pe.billing.domain;

import java.util.List;
import lombok.Data;

@Data
public class OCSProfileRequest extends BaseRequest {

	private List<AccountRef> accounts;

}
