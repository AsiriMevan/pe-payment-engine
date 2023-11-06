package lk.dialog.pe.customer.info.util;

import java.util.List;

import org.springframework.stereotype.Service;

import lk.dialog.pe.customer.info.domain.Profile;
@Service
public class RequestTypeCriteria implements FilterCriteria {
	
	private String	filter;

	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	@Override
	public <E> List<E> execute(List<E> list) {
		if (Integer.valueOf(filter) == Constants.REQ_TYPE_LIMITED) {
			for (E sbuProfile : list) {
				Profile profile = (Profile) sbuProfile;
				profile.setAddrLine1(null);
				profile.setAddrLine2(null);
				profile.setAddrLine3(null);
				profile.setPostalCode(null);
				profile.setEmail(null);
			}
		}
		return list;
	
	}
}
