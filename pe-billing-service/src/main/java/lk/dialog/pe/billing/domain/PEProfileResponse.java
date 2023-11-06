package lk.dialog.pe.billing.domain;

import java.util.ArrayList;
import java.util.List;

import lk.dialog.pe.billing.util.BaseResponse;
import lombok.Data;
@Data
public class PEProfileResponse extends BaseResponse{

	List<Balance> accounts = new ArrayList<>();
	
}
