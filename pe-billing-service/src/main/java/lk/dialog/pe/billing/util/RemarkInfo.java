package lk.dialog.pe.billing.util;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
@Data
public class RemarkInfo {
	
	private String contractNo;
	private List<Remark> remarks = new ArrayList<>();
	
}
