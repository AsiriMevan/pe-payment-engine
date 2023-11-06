package lk.dialog.pe.common.util;

import java.util.List;

import lombok.Data;
@Data
public class HotlineRemarkResponse extends BaseResponse {

	private List<RemarkInfo> accounts;
}
