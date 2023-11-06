package lk.dialog.pe.customer.info.util;

public enum ValidationPatternEnum {

	WIFI_NO_PATTERN("^90(\\\\d+)"),
	CCBS_WIFI_CONN_REF_VALIDATOR("^CI(\\\\d+)"),
	TELBIZ_CONN_REF_VALIDATOR("[^0](\\\\d+){8}"),
	TELBIZ_VALID_CONN_REF_VALIDATOR("^0(\\\\d+){9}"),
	INTEGER_VALIDATOR("(\\d+)"),
    NFC_CONTRACT_NO_VALIDATOR( "^95(\\d+)");

	private String pattern;

	private ValidationPatternEnum(String pattern) {
		this.pattern = pattern;
	}

	public String getPattern() {
		return pattern;
	}
	
}
