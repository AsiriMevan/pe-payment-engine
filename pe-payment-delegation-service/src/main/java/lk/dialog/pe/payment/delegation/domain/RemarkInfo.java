package lk.dialog.pe.payment.delegation.domain;

import java.util.ArrayList;
import java.util.List;

public class RemarkInfo {

	private String contractNo;
	
	private List<Remark> remarks = new ArrayList<>();

	public String getContractNo() {
		return contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public List<Remark> getRemarks() {
		return remarks;
	}

	public void setRemarks(List<Remark> remarks) {
		this.remarks = remarks;
	}
	
	
	public void addRemark(Remark remark){
		remarks.add(remark);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contractNo == null) ? 0 : contractNo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		RemarkInfo other = (RemarkInfo) obj;
		if (contractNo == null) {
			if (other.contractNo != null) {
				return false;
			}
		} else if (!contractNo.equals(other.contractNo)) {
			return false;
		}
		return true;
	}
}
