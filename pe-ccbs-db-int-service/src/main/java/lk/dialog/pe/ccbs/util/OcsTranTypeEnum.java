package lk.dialog.pe.ccbs.util;

import java.util.Arrays;
import java.util.Optional;

public enum OcsTranTypeEnum {

	OCS_TRAN_PAY(1), OCS_TRAN_CANCEL(2), OCS_TRAN_CHQRTN(3);
	
	private final Integer value;

	private OcsTranTypeEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static Optional<OcsTranTypeEnum> getOcsTranTypeByValue(Integer value) {
		return Arrays.asList(values())
				.stream()
				.filter(m -> m.getValue().equals(value))
				.findFirst();
	}
}