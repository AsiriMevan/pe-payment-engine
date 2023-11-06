package lk.dialog.pe.customer.info.util;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lk.dialog.pe.customer.info.domain.Profile;
import lk.dialog.pe.customer.info.domain.Account;
@Service
public class SBUCriteria implements FilterCriteria{

	private String filter;

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	@Override
	public <T> List<T> execute(List<T> list) {

		switch (Integer.valueOf(filter)) {
			case Constants.SBU_DTV_PRE:
				this.filterSbuDtvPre(list);
				break;
			case Constants.SBU_DTV_POST:
				this.filterSbuDtvPost(list);
				break;
			case Constants.SBU_GSM:
				this.filterSbuGsm(list);
				break;
			case Constants.SBU_DBN:
				this.filterSbuDbn(list);
				break;
			default:
				break;

		}
		return list;
	}

	private <T> void filterSbuDtvPre(List<T> list) {
		for (T sbuProfile : list) {
			Profile profile = (Profile) sbuProfile;
			List<Account> filterAccounts = new ArrayList<>();
			List<Account> accounts = profile.getAccounts();
			accounts.forEach(account -> {
				if (account.getSbu() != null  && SBU.DTV_PREPAID.valueOf() == account.getSbu()) {
					filterAccounts.add(account);
				}
			});
			profile.setAccounts(filterAccounts);
		}
	}

	private <T> void filterSbuDtvPost(List<T> list) {
		for (T sbuProfile : list) {
			Profile profile = (Profile) sbuProfile;
			List<Account> filterAccounts = new ArrayList<>();
			List<Account> accounts = profile.getAccounts();
			accounts.forEach(account -> {
				if (account.getSbu() != null  && SBU.DTV_POSTPAID.valueOf() == account.getSbu()) {
					filterAccounts.add(account);
				}
			});
			profile.setAccounts(filterAccounts);
		}
	}

	private <T> void filterSbuGsm(List<T> list) {
		for (T sbuProfile : list) {
			Profile profile = (Profile) sbuProfile;
			List<Account> filterAccounts = new ArrayList<>();
			List<Account> accounts = profile.getAccounts();
			accounts.forEach(account -> {

				if (account.getSbu() != null  && SBU.GSM.valueOf() == account.getSbu()) {
					filterAccounts.add(account);
				}
			});
			profile.setAccounts(filterAccounts);
		}
	}

	private <T> void filterSbuDbn(List<T> list) {
		for (T sbuProfile : list) {
			Profile profile = (Profile) sbuProfile;
			List<Account> filterAccounts = new ArrayList<>();
			List<Account> accounts = profile.getAccounts();
			if (accounts != null) {
				accounts.forEach(account -> {
					if (account.getSbu() != null  && SBU.DBN.valueOf() == account.getSbu()) {
						filterAccounts.add(account);
					}
				});
			}
			profile.setAccounts(filterAccounts);
		}
	}
}
