package lk.dialog.pe.customer.info.util;

import java.util.List;

import lk.dialog.pe.common.domain.ConnectionRef;
import lk.dialog.pe.customer.info.domain.Profile;

public class CRMProfileResponse extends BaseResponse{

	private List<Profile> profiles;
	private List<lk.dialog.pe.common.domain.ConnectionRef> invalidAccounts;
	
	public List<Profile> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}
	public List<lk.dialog.pe.common.domain.ConnectionRef> getInvalidAccounts() {
		return invalidAccounts;
	}
	public void setInvalidAccounts(List<ConnectionRef> invalidAccounts) {
		this.invalidAccounts = invalidAccounts;
	}
	
	
}
