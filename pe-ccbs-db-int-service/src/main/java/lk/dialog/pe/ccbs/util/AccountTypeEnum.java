package lk.dialog.pe.ccbs.util;

import java.util.Arrays;
import java.util.Optional;

public enum AccountTypeEnum {

	POSTPAID(2),
	PREPAID(1);
	
	private final Integer value;

	private AccountTypeEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static Optional<AccountTypeEnum> getAccountTypeByValue(String accountType) {
		if (accountType == null)
			return Optional.empty();
		else {
			Integer accountValue = Integer.parseInt(accountType);
			return Arrays.asList(values())
				.stream()
				.filter(m -> m.getValue().equals(accountValue))
				.findFirst();
		}
	}
	
	public static AccountTypeEnum getAccountType(String value){
		AccountTypeEnum rt = null;
		for(AccountTypeEnum e : AccountTypeEnum.values()){
			if(e.name().equalsIgnoreCase(value)){
				rt = e;
	            break;
			}
		}
		return rt;
	}
}
