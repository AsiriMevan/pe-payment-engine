package lk.dialog.pe.ccbs.util;

public enum SearchingCriteriaEnum {

	/* IP500 searching criteria */
//	CUST_PROFILE_CUST_REF("CUST_REF"),
//	CUST_PROFILE_INVOICE_NO("INVOICE_NO"),
//	CUST_PROFILE_CONTRACT_ID("CONTRACT_ID"),
//	CUST_PROFILE_CONN_REF("CONN_REF"),
//	CUST_PROFILE_CONN_REF_AND_CONTRACT("CONTRACT_ID_AND_CONN_REF_AND"),
	BULK_REQUEST_PER_BATCH(1000f);
	
	private final float value; 
	
	 SearchingCriteriaEnum(float value) {
		this.value = value;
	}

	public float getValue() {
		return value;
	}
	 
	
}
