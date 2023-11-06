package lk.dialog.pe.ccbs.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
@Data
public class WifiAccountDto {
	
	 private String contractNo;
	 private List<Remark> remarks = new ArrayList<>();

}
