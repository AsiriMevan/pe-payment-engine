package lk.dialog.pe.common.util;

public enum ErrorCodeEnum {

	ERR_CODE_SUCCESS("SUCCESS"), ERR_CODE_PAYMENT_EXIST("PAYMENT-ALREADY-EXIST"), ERR_CODE_FAIL("RUNTIME_ERROR"),
	ERR_DESC_SUCCESS("Operation success"), ERR_CODE_CUSTOMER_NOT_FOUND("CUSTOMER_NOT_FOUND"),
	ERR_DESC_CUSTOMER_NOT_FOUND("Customer doesn't exist. Check input values again."),
	ERR_CODE_PROFILE_NOT_FOUND("PROFILE_NOT_FOUND"),
	ERR_DESC_PROFILE_NOT_FOUND("Profile doesn't exist. Check query parameters again."),
	ERR_CODE_INVALID_PC("INVALID_PC"), ERR_CODE_INVALID_SBU("INVALID_SBU"), ERR_CODE_INVALID_PT("INVALID_PT"),
	ERR_CODE_VALID("VALID"), ERR_CONN_REF("INVALID_CONN_REF_AND_CONTRACT_NO"), ERR_INPUT_DATA("INVALID"),
	ERR_DESC_INPUT_DATA("INVALID_INPUT_VALUES"),
	ERR_DESC_INVALID_PC("Couldn't find end subsystem or API not support for given product catagory"),
	ERR_CODE_RBM("RBM_QUERY_ERROR"), ERR_RBM_DESC("RBM query error ::"), ERR_LOG_CODE("::Error ->"),
	ERR_CODE_DATA_QUERY("DATA_QUERY_ERROR"), ERR_CODE_PC_N_SBU_MISMATCH("PC_SBU_MISMATCH"),
	ERR_DESC_PC_N_SBU_MISMATCH("Product category not compatible with contract SBU"),

	ERR_DESC_ONLY_POSTPAID_PAYMENT_SUPPORT("Only postpaid payments support"),
	ERR_DESC_INVALID_PRODUCT_CATEGORY("Invalid product category"),
	ERR_DESC_PRODUCT_TYPE_NOT_SUPPORT("Product type not support for payment post function"),
	ERR_DESC_SBU_NOT_SUPPORT("SBU not support for payment post function"),
	ERR_CODE_CUSTOMER_NFC("Not a Valid Scenario."),
	ERR_DESC_CUSTOMER_NFC("\"Remarks\" not applicable for product category \"NFC\".");

	private String code;
	
	private ErrorCodeEnum(String code) {
		this.code = code;
	}

	public String getErrorCode() {
		return code;
	}
}
