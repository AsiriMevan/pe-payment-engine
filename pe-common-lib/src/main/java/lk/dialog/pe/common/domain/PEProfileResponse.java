package lk.dialog.pe.common.domain;

import java.util.ArrayList;
import java.util.List;

import lk.dialog.pe.common.util.BaseResponse;
import lombok.Data;
@Data
public class PEProfileResponse extends BaseResponse{

	List<Balance> accounts = new ArrayList<>();
	
}
