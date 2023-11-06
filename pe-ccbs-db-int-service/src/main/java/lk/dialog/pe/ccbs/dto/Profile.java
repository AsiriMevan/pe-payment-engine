package lk.dialog.pe.ccbs.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
@Data
@JsonIgnoreProperties({"profileId"})
public class Profile{
	
	private String custRef;
	private String oldCustRef;
	private String custRefType;
	private String custName;
	private String addrLine1;
	private String addrLine2;
	private String addrLine3;
	private String email;
	private String postalCode;
	private List<Account> accounts;	
	private String profileId;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((profileId == null) ? 0 : profileId.hashCode());
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
		Profile other = (Profile) obj;
		if (profileId == null) {
			if (other.profileId != null)
				return false;
		} else if (!profileId.equals(other.profileId))
			return false;
		return true;
	}

}
