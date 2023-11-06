package lk.dialog.pe.common.util;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class RemarkInfo {

	private String contractNo;
	private List<Remark> remarks = new ArrayList<>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contractNo == null) ? 0 : contractNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RemarkInfo other = (RemarkInfo) obj;
		if (contractNo == null) {
			if (other.contractNo != null)
				return false;
		} else if (!contractNo.equals(other.contractNo))
			return false;
		return true;
	}

}
