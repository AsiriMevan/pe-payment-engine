package lk.dialog.pe.customer.info.util;

public enum PaymentDelegationEnum {

	JSON_SCHEMA_512("forcefulrealize"),
	JSON_SCHEMA_510("payment"),
	JSON_SCHEMA_511("paymentCancel"),
	JSON_SCHEMA_503("rbmprofile"),
	JSON_SCHEMA_506("paymentquery"),
	
	JSON_SCHEMA_500("crmprofile"),
	
	ERR_CODE_INVALID_PC("INVALID_PC"),
	ERR_CODE_INVALID_SBU("INVALID_SBU"),
	ERR_CODE_INVALID_PT("INVALID_PT"),

	PAY_MODE_CHQ("CHE"),
	PAY_MODE_CARD("CC"),
	
	PAYMENT_PENDDING_DATE_FORMAT("yyyy-MM-dd HH:mm:ss");
	
	private final String name;

	PaymentDelegationEnum(String name) {
		this.name =name;
	}

	public String getName() {
		return name;
	}

}
