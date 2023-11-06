package lk.dialog.pe.customer.info.util;

import java.util.ArrayList;
import java.util.List;

import lk.dialog.pe.customer.info.domain.Account;
import lk.dialog.pe.customer.info.domain.Profile;

public class SBUCommonFilter {

	private SBUCommonFilter() {}
	public static <T> List<T> volteSbuFilter(List<T> list) {
		
		for (T sbuProfile : list) {
			Profile profile = (Profile) sbuProfile;
			List<Account> filterAccounts = new ArrayList<>();
			List<Account> accounts = profile.getAccounts();
			accounts.forEach(account -> {
				if (SBU.DBN.valueOf() != account.getSbu()) {
					filterAccounts.add(account);
				}
			});
			profile.setAccounts(filterAccounts);
		}

		return list;
	}
	
}
