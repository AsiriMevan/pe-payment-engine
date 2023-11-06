package lk.dialog.pe.ccbs.dto;

import lombok.Data;

@Data
public class Account {
	private String connRef;
	private String accountNo;
	private Integer accountType;	
	private String contractNo;
	private String contractEmail;
	private String prCode;
	private String prEmail;
	private Integer hybridFlag;
	private Integer conStatus;
	private String disconReasonCode;
	private String disconReason;
	private Integer productType;
	private String billCycle;
	private Integer sbu;	
	private String contactNo;

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
		Account other = (Account) obj;
		if (contractNo == null) {
			if (other.contractNo != null)
				return false;
		} else if (!contractNo.equals(other.contractNo))
			return false;
		return true;
	}
	
}
