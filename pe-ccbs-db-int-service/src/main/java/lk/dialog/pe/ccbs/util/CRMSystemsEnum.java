package lk.dialog.pe.ccbs.util;

public enum CRMSystemsEnum {

	CRM_SYS_CCBS("CCBS"),
	CRM_SYS_NFC("NFC"),
	CRM_SYS_TELBIZ("TELBIZ"),
	UNION_APPENDER("UNION"),
	STR_PROFILE_ID("SUBSCRIBER_PROFILE_ID"),
	STR_NODE_ID("SUBSCRIBER_NODE_ID"),
	STR_CONTRACT_ID("CONTRACT_ID"),
	STR_SUBSIDIARY_TYPE ("CONTRACT_SUBSIDIARY_TYPE"),
	STR_MOBILE_NO ("MOBILE_NO"),
	STR_PRE_POST ("PRE_POST"),
	STR_EMAIL_ADDR("EMAIL_ADDRESS"),
	STR_PR_CODE("PR_CODE"),
	STR_PR_EMAIL("PR_EMAIL"),
	STR_OCS_HYBRID("OCS_HYBRID"),
	STR_SWITCH_STATUS("SWITCH_STATUS"),
	STR_STATUS_REASON_ID("STATUS_REASON_ID"),
	STR_DESCRIPTION("DESCRIPTION"),
	STR_BILL_RUN_CODE("BILL_RUN_CODE"),
	STR_VOLTE("VOLTE"),
	STR_ID_NUMBER("IDENTIFICATION_NUMBER"),
	STR_OLD_NIC_NO("OLD_NIC_NO"),
	STR_ID_TYPE("IDENTIFICATION_TYPE"),
	STR_TITLE ("TITLE"),
	STR_NAME1("NAME1"),
	STR_NAME2 ("NAME2"),
	STR_DEFAULT_ADDR ("CX_DEFAULT_ADDRESS"),
	STR_CUST_ID ("CUST_ID"),
	STR_CREATED_DATE ("CREATED_DATE"),
	STR_CREATED_USER("CREATED_USER");

	
	private String crm;
 
	private  CRMSystemsEnum(String crm) {
		this.crm = crm;
	}

	public String getCrm() {
		return crm;
	}


}
