package lk.dialog.pe.payment.delegation.domain;

public enum ErrorCodeEnum {

	ERR_CODE_SUCCESS("SUCCESS"),
	ERR_CODE_PAYMENT_EXIST("PAYMENT-ALREADY-EXIST"),
	ERR_CODE_FAIL("RUNTIME_ERROR"),
	ERR_DESC_SUCCESS("Operation success"),
	ERR_CODE_CUSTOMER_NOT_FOUND("CUSTOMER_NOT_FOUND"),
	ERR_DESC_CUSTOMER_NOT_FOUND("Customer doesn't exist. Check input values again."),
	ERR_CODE_PROFILE_NOT_FOUND("PROFILE_NOT_FOUND"),
	ERR_DESC_PROFILE_NOT_FOUND("Profile doesn't exist. Check query parameters again."),
	ERR_CODE_INVALID_PC("INVALID_PC"),
	ERR_CODE_INVALID_SBU("INVALID_SBU"),
	ERR_CODE_INVALID_PT("INVALID_PT"),
	ERR_CODE_VALID("VALID"),
	ERR_CONN_REF("INVALID_CONN_REF_AND_CONTRACT_NO"),
	ERR_INPUT_DATA("INVALID"),
	ERR_DESC_INPUT_DATA("INVALID_INPUT_VALUES"),
	ERR_DESC_INVALID_PC("Couldn't find end subsystem or API not support for given product catagory"),
	ERR_CODE_RBM("RBM_QUERY_ERROR"),
	ERR_RBM_DESC("RBM query error ::"),
	ERR_LOG_CODE("::Error ->"),
	ERR_CODE_DATA_QUERY("DATA_QUERY_ERROR"),
	ERR_CODE_PC_N_SBU_MISMATCH("PC_SBU_MISMATCH"),
	ERR_DESC_PC_N_SBU_MISMATCH("Product category not compatible with contract SBUEnum");
	
	private String code;

	private ErrorCodeEnum(String code) {
		this.code = code;
	}

	public String getErrorCode() {
		return code;
	}
}
