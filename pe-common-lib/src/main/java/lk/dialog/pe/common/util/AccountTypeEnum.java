package lk.dialog.pe.common.util;

public enum AccountTypeEnum {

	POSTPAID(2), PREPAID(1);

	private Integer value;

	private AccountTypeEnum(int val) {
		this.value = val;
	}

	public Integer valueOf() {
		return this.value;
	}

	public static AccountTypeEnum getAccountType(int value) {
		AccountTypeEnum rt = null;
		for (AccountTypeEnum e : AccountTypeEnum.values()) {
			if (e.valueOf() == value) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static AccountTypeEnum getAccountType(String value) {
		AccountTypeEnum rt = null;
		for (AccountTypeEnum e : AccountTypeEnum.values()) {
			if (e.name().equalsIgnoreCase(value)) {
				rt = e;
				break;
			}
		}
		return rt;
	}

}
