package lk.dialog.pe.common.util;

public class NEConstants {

	private NEConstants() {
	}
	public static final int QUERY_BAL_EXEMPT_DAYS = 0;
	public static final double RBM_AMOUNT_DIVISER = 1000.00;

	public static final int PREPAID = 1;
	public static final int POSTPAID = 2;
	public static final int OCS_AMOUNT_DIVISER = 10000;
	public static final String PAY_MODE_CASH = "CASH";
	public static final String PAY_MODE_CHEQ = "CHEQ";
	public static final String PAY_MODE_CARD = "CARD";
	public static final String REGEX_DTV = "60";
	public static final String REGEX_NFC = "95";
	public static final String REGEX_WIFI = "900";
	public static final String REGEX_DBN1 = "70";
	public static final String REGEX_DBN2 = "72";

	/* Product Type */
	public static final int PT_OTHER = 0;
	public static final int PT_WIFI = 1;
	public static final int PT_NFC = 2;

	public static final int SBU_DTV_PRE = 1;
	public static final int SBU_DTV_POST = 2;
	public static final int SBU_GSM = 3;
	public static final int SBU_DBN = 4;

	/* Payment Posting */
	public static final String RECEIPT_TYPE = "BI";
}
