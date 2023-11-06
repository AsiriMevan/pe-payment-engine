package lk.dialog.pe.common.util;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class ErrorMsgValidation {

	private ErrorMsgValidation() {
	}

	public static final String SBU_TEXT = "sbu";
	public static final String PRODUCT_TYPE_TEXT = "productType";
	public static final String ACCOUNT_TYPE_TEXT = "accountType";
	public static final String CONTRACT_NO_TEXT = "contractNo";
	public static final String PRODUCT_CATEGORY_TEXT = "productCategory";
	public static final String ACCOUNTS = "accounts";
	public static final String RECEIPTS_INFO = "receiptsInfo";
	public static final String REQ_TYPE_TEXT = "reqType";
	public static final String CANCELLED_USER_TEXT = "cancelledUser";
	public static final String SOURCE_SYSTEM_TEXT = "sourceSystem";
	public static final String CANCELLED_REASON_TEXT = "cancelledReason";
	public static final String ACCOUNT_NO_TEXT = "accountNo";
	public static final String CONN_REF_TEXT = "connRef";
	public static final String RECEIPT_BRANCH_TEXT = "receiptBranch";
	public static final String RECEIPT_USER_TEXT = "receiptUser";
	public static final String BRANCH_COUNTER_TEXT = "branchCounter";
	public static final String CHQ_BANK_TEXT = "chqBank";
	public static final String CHQ_BRANCH_TEXT = "chqBranch";
	public static final String CHQ_NO_TEXT = "chqNo";
	public static final String RECEIPT_NO_TEXT = "receiptNo";
	public static final String PAYMENT_CURRENCY_TEXT = "paymentCurrency";
	public static final String PAYMENT_MODE_TEXT = "paymentMode";
	public static final String MODULE_NAME_TEXT = "moduleName";
	public static final String CONNECTION_STATUS_TEXT = "connectionStatus";

	private static String msg1 = "instance value (%s) not found in enum (possible values: %s),\"/%s/0/%s\";";
	private static String msg2 = "numeric instance is lower than the required minimum (minimum: %s, found: %s),\"/%s/0/%s\"";
	private static String msg3 = "instance type (%s) does not match any allowed primitive type (allowed: [\"integer\"]),\"/%s/0/%s\"";
	private static String msg4 = "instance type (%s) does not match any allowed primitive type (allowed: [\"string\"]),\"/%s/0/%s\"";
	private static String msg5 = "string \"\" is too short (length: 0, required minimum: %s),\"/%s/0/%s\"";
	private static String msg6 = "instance value (%s) not found in enum (possible values: %s),\"/%s\";";
	private static String msg7 = "numeric instance is lower than the required minimum (minimum: %s, found: %s),\"/%s\"";
	private static String msg8 = "string \"\" is too short (length: 0, required minimum: %s),\"/%s\"";

	public static SortedSet<Integer> getSBUEnumValues() {
		List<Integer> enumResults = new ArrayList<>();
		List<SBUEnum> result = new ArrayList<>(EnumSet.allOf(SBUEnum.class));
		for (SBUEnum sbuEnum : result) {
			enumResults.add(sbuEnum.value);
		}
		return new TreeSet<>(enumResults.subList(1, enumResults.size()));
	}

	public static SortedSet<Integer> getSBUEnumValuesWithZero() {
		List<Integer> enumResults = new ArrayList<>();
		List<SBUEnum> result = new ArrayList<>(EnumSet.allOf(SBUEnum.class));
		for (SBUEnum sbuEnum : result) {
			enumResults.add(sbuEnum.value);
		}
		return new TreeSet<>(enumResults.subList(0, enumResults.size()));
	}

	public static SortedSet<Integer> getProductTypeEnumValues() {
		List<Integer> enumResults = new ArrayList<>();
		List<ProductTypeEnum> result = new ArrayList<>(EnumSet.allOf(ProductTypeEnum.class));
		for (ProductTypeEnum productTypeEnum : result) {
			enumResults.add(productTypeEnum.getType());
		}
		return new TreeSet<>(enumResults.subList(0, enumResults.size()));
	}

	public static SortedSet<Integer> getAccountTypeEnumValues() {
		List<Integer> enumResults = new ArrayList<>();
		List<AccountTypeEnum> result = new ArrayList<>(EnumSet.allOf(AccountTypeEnum.class));
		for (AccountTypeEnum accountTypeEnum : result) {
			enumResults.add(accountTypeEnum.valueOf());
		}
		return new TreeSet<>(enumResults.subList(0, enumResults.size()));
	}

	public static SortedSet<Integer> getProductCategoryEnum() {
		List<Integer> productCategoryEnumResults = new ArrayList<>();
		List<ProductCategoryEnum> result = new ArrayList<>(EnumSet.allOf(ProductCategoryEnum.class));
		for (ProductCategoryEnum productCategoryEnum : result) {
			productCategoryEnumResults.add(productCategoryEnum.valueOf());
		}
		return new TreeSet<>(productCategoryEnumResults.subList(0, 3));
	}

	public static SortedSet<Integer> getSBUEnumValuesWithinThreeLength() {
		List<Integer> enumResults = new ArrayList<>();
		List<SBUEnum> result = new ArrayList<>(EnumSet.allOf(SBUEnum.class));
		for (SBUEnum sbuEnum : result) {
			enumResults.add(sbuEnum.value);
		}
		return new TreeSet<>(enumResults.subList(2, enumResults.size()));
	}

	public static SortedSet<Integer> getReqTypeEnumValues() {
		List<Integer> enumResults = new ArrayList<>();
		enumResults.add(Constants.REQ_TYPE_LIMITED);
		enumResults.add(Constants.REQ_TYPE_ALL);
		return new TreeSet<>(enumResults.subList(0, enumResults.size()));
	}
	
	public static SortedSet<Integer> getConnectionStatusEnumValues() {
		List<Integer> enumResults = new ArrayList<>();
		List<ConnectionStatusEnum> result = new ArrayList<>(EnumSet.allOf(ConnectionStatusEnum.class));
		for (ConnectionStatusEnum connectionStatusEnum : result) {
			enumResults.add(connectionStatusEnum.getValue());
		}
		return new TreeSet<>(enumResults.subList(0, enumResults.size()));
	}
	
	public static SortedSet<Integer> getProductTypeEnumValuesForIP512() {
		List<Integer> enumResults = new ArrayList<>();
		List<ProductTypeEnum> result = new ArrayList<>(EnumSet.allOf(ProductTypeEnum.class));
		for (ProductTypeEnum productTypeEnum : result) {
			if(!productTypeEnum.getType().equals(3) && !productTypeEnum.getType().equals(4)) {
				enumResults.add(productTypeEnum.getType());
			}
		}
		return new TreeSet<>(enumResults.subList(0, enumResults.size()));
	}
	

	public static String getMsg1() {
		return msg1;
	}

	public static String getMsg2() {
		return msg2;
	}

	public static String getMsg3() {
		return msg3;
	}

	public static String getMsg4() {
		return msg4;
	}

	public static String getMsg5() {
		return msg5;
	}

	public static String getMsg6() {
		return msg6;
	}

	public static String getMsg7() {
		return msg7;
	}
	
	public static String getMsg8() {
		return msg8;
	}

}
