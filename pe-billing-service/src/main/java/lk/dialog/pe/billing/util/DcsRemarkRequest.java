package lk.dialog.pe.billing.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DcsRemarkRequest {
	
	private List<String> volteContractList;

}
