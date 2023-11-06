package lk.dialog.pe.billing.util;

public enum CRMSystemsEnum {

	STR_CONTRACT_ID("CONTRACT_ID"),
	STR_MOBILE_NO("MOBILE_NO"),
	STR_SWITCH_STATUS("SWITCH_STATUS"),
	STR_PRE_POST("PRE_POST");

	private String code;

	private CRMSystemsEnum(String code) {
		this.code = code;
	}

	public String getCRMSystems() {
		return code;
	}
}
