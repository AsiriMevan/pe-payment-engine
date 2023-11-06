package lk.dialog.pe.payment.delegation.util;

/**
 * use to place all the constants used in the project
 */
public class Constants {

	public static final int SUCCESS = 1;
	public static final int FAIL = 0;
	public static final int TRANSFER_FAIL = 2;
	public static final String ERR_CODE_SUCCESS = "SUCCESS";
	public static final String ERR_CODE_PAYMENT_EXIST = "PAYMENT-ALREADY-EXIST";
	public static final String ERR_CODE_FAIL = "RUNTIME_ERROR";
	public static final String ERR_DESC_SUCCESS = "Operation success";
	public static final String ERR_CODE_CUSTOMER_NOT_FOUND = "CUSTOMER_NOT_FOUND";
	public static final String ERR_DESC_CUSTOMER_NOT_FOUND = "Customer doesn't exist. Check input values again.";
	public static final String ERR_CODE_PROFILE_NOT_FOUND = "PROFILE_NOT_FOUND";
	public static final String ERR_DESC_PROFILE_NOT_FOUND = "Profile doesn't exist. Check query parameters again.";
	public static final String ERR_CODE_INVALID_PC = "INVALID_PC";
	public static final String ERR_CODE_INVALID_SBU = "INVALID_SBU";
	public static final String ERR_CODE_INVALID_PT = "INVALID_PT";
	public static final String ERR_CODE_VALID = "VALID";
	public static final String ERR_CONN_REF = "INVALID_CONN_REF_AND_CONTRACT_NO";
	public static final String ERR_INPUT_DATA = "INVALID";
	public static final String ERR_DESC_INPUT_DATA = "INVALID_INPUT_VALUES";
	public static final String ERR_DESC_INVALID_PC = "Couldn't find end subsystem or API not support for given product catagory";
	public static final String ERR_CODE_RBM = "RBM_QUERY_ERROR";
	public static final String ERR_RBM_DESC = "RBM query error ::";
	public static final String ERR_LOG_CODE = "::Error ->";
	public static final String ERR_CODE_DATA_QUERY = "DATA_QUERY_ERROR";
	public static final String ERR_CODE_PC_N_SBU_MISMATCH = "PC_SBU_MISMATCH";
	public static final String ERR_DESC_PC_N_SBU_MISMATCH = "Product category not compatible with contract SBU";

	public static final String TIME_ZONE = "Asia/Colombo";
	public static final String ERR_CODE_CUSTOMER_REF_TYPE = "Invalid customer ref type.";
	public static final String ERR_DESC_CUSTOMER_REF_TYPE = "Customer ref type can not be null, when customer ref not null.";
	public static final String ERR_CODE_CUSTOMER_NFC = "Not a Valid Scenario.";
	public static final String ERR_DESC_CUSTOMER_NFC = "\"Remarks\" not applicable for product category \"NFC\".";
	public static final String ERR_CODE_NO_DATA_FOUND = "DATA_NOT_FOUND";
	public static final String ERR_DESC_NO_DATA_FOUND = "No data Found for provided inputs.";
	public static final String ERR_DESC_500_INVALID_SCENARIO = "NFC Cx unable to query from \"Customer ID\".";

	/* Service Business Unit */

	public static final int SBU_ALL = 0;
	public static final int SBU_DTV_PRE = 1;
	public static final int SBU_DTV_POST = 2;
	public static final int SBU_GSM = 3;
	public static final int SBU_DBN = 4;

	public static final int REQ_TYPE_ALL = 1;
	public static final int REQ_TYPE_LIMITED = 0;

	/* NFC product category */
	public static final int PC_NFC = 3;
	public static final int PC_CCBS = 1;
	public static final int PC_TELBIZ = 2;
	public static final int PC_PE = 4;

	/* Charge Type */
	public static final int POSTPAID = 2;
	public static final int PREPAID = 1;

	/* Payment Post */
	public static final String PAY_MODE_CHQ = "CHE";
	public static final String PAY_MODE_CASH = "CA";
	public static final String PAY_MODE_CARD = "CC";
	public static final String PAYMENT_PREFIX = "PAYMENT - ";

	/* WiFi Number pattern */
	public static final String WIFI_NO_PATTERN = "^90(\\d+)";

	/* JSON schemas */
	public static final String JSON_SCHEMA_500 = "crmprofile";
	public static final String JSON_SCHEMA_501 = "peprofile";
	public static final String JSON_SCHEMA_502 = "rbmprofile";
	public static final String JSON_SCHEMA_503 = "rbmprofile";
	public static final String JSON_SCHEMA_504 = "remark";
	public static final String JSON_SCHEMA_505 = "paymentquery";
	public static final String JSON_SCHEMA_505_TEST = "paymentsummary";
	public static final String JSON_SCHEMA_506 = "paymentquery";
	public static final String JSON_SCHEMA_510 = "payment";
	public static final String JSON_SCHEMA_511 = "paymentCancel";
	public static final String JSON_SCHEMA_512 = "forcefulrealize";

	/* Global messages */
	public static final String MSG_INVALID_SCHEMA = "Valid JSON Schema Not Found";

	/* hybrid status */
	public static final Integer NFC_HYBRID = 2;

	/* Contract number validation */
	public static final String NFC_CONTRACT_NO_VALIDATOR = "^95(\\d+)";
	public static final String NFC_CONTRACT_NO_VALIDATOR_ZEROS = "^9500(\\d+)";

	/* Date fomat */
	public static final String PAYMENT_PENDDING_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/* product type */
	public static final int PT_OTHER = 0;
	public static final int PT_WIFI = 1;
	public static final int PT_NFC = 2;
	public static final int PT_CDMA = 3;
	public static final int PT_LTE = 4;
	public static final int PT_VOLTE = 5;
	public static final int PT_DCS=6;

	/* Filter Type */

	public static final String FT_CONNREF="ConnRef";
	public static final String FT_CONTRACT="Contract";

	/* OCS tran type */
	public static final int OCS_TRAN_PAY = 1;
	public static final int OCS_TRAN_CANCEL = 2;
	public static final int OCS_TRAN_CHQRTN = 3;

	/* Payment Job */
	public static final int OCS_AMOUNT_MULTIPLIER = 10000;
	public static final int SUSPEND_MODE = 0;
	public static final int CREDIT_MODE = 1;

	/* Integer validation */
	public static final String INTEGER_VALIDATOR = "(\\d+)";

	/* CCBS wifi connection ref validation */
	public static final String CCBS_WIFI_CONN_REF_VALIDATOR = "^CI(\\d+)";

	/* Telbiz connection number validation */
	public static final String TELBIZ_CONN_REF_VALIDATOR = "[^0](\\d+){8}";
	public static final String TELBIZ_VALID_CONN_REF_VALIDATOR = "^0(\\d+){9}";

	/* IP500 searching criteria */
	public static final String CUST_PROFILE_CUST_REF = "CUST_REF";
	public static final String CUST_PROFILE_INVOICE_NO = "INVOICE_NO";
	public static final String CUST_PROFILE_CONTRACT_ID = "CONTRACT_ID";
	public static final String CUST_PROFILE_CONN_REF = "CONN_REF";
	public static final String CUST_PROFILE_CONN_REF_AND_CONTRACT = "CONTRACT_ID_AND_CONN_REF_AND";
	public static final float BULK_REQUEST_PER_BATCH = 1000f;

	/* Cheque modes */
	public static final String CHQ_MODE_CREDIT = "CREDIT";
	public static final String CHQ_MODE_SUSPEND = "SUSPEND";

	private Constants() {
	}

	public enum CACHE_MAPPER {
		RECEIPT_BRANCH, BRANCH_COUNTER, RECEIPT_USER, PAYMENT_MODE, RECEIPT_BRANCH_TELBIZ, BRANCH_COUNTER_TELBIZ, RECEIPT_USER_TELBIZ, PAYMENT_MODE_TELBIZ, BANK_CODE_TELBIZ
	}

	/* CRM systems constant */
	public static final String CRM_SYS_CCBS = "CCBS";
	public static final String CRM_SYS_NFC = "NFC";
	public static final String CRM_SYS_TELBIZ = "TELBIZ";

	public static final String UNION_APPENDER = " UNION ";
	public static final String STR_PROFILE_ID = "SUBSCRIBER_PROFILE_ID";
	public static final String STR_NODE_ID = "SUBSCRIBER_NODE_ID";
	public static final String STR_CONTRACT_ID = "CONTRACT_ID";
	public static final String STR_SUBSIDIARY_TYPE ="CONTRACT_SUBSIDIARY_TYPE";
	public static final String STR_MOBILE_NO ="MOBILE_NO";
	public static final String STR_PRE_POST ="PRE_POST";
	public static final String STR_EMAIL_ADDR="EMAIL_ADDRESS";
	public static final String STR_PR_CODE="PR_CODE";
	public static final String STR_PR_EMAIL="PR_EMAIL";
	public static final String STR_OCS_HYBRID="OCS_HYBRID";
	public static final String STR_SWITCH_STATUS="SWITCH_STATUS";
	public static final String STR_STATUS_REASON_ID="STATUS_REASON_ID";
	public static final String STR_DESCRIPTION="DESCRIPTION";
	public static final String STR_BILL_RUN_CODE="BILL_RUN_CODE";
	public static final String STR_VOLTE="VOLTE";
	public static final String STR_ID_NUMBER="IDENTIFICATION_NUMBER";
	public static final String STR_OLD_NIC_NO="OLD_NIC_NO";
	public static final String STR_ID_TYPE="IDENTIFICATION_TYPE";
	public static final String STR_TITLE ="TITLE";
	public static final String STR_NAME1 ="NAME1";
	public static final String STR_NAME2 ="NAME2";
	public static final String STR_DEFAULT_ADDR ="CX_DEFAULT_ADDRESS";
	public static final String STR_CUST_ID ="CUST_ID";
	public static final String STR_CREATED_DATE ="CREATED_DATE";
	public static final String STR_CREATED_USER="CREATED_USER";

}
