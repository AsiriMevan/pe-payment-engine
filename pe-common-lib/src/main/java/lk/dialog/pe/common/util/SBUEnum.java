package lk.dialog.pe.common.util;

import java.util.Arrays;
import java.util.Optional;

public enum SBUEnum {

	ALL(0), DTV_PREPAID(1), DTV_POSTPAID(2), GSM(3), DBN(4), VOLTE(4), DCS(4);

	public final Integer value;

	public Integer getSbu() {
		return value;
	}

	private SBUEnum(int val) {
		this.value = val;
	}

	public int valueOf() {
		return this.value;
	}

	public static SBUEnum getSBUByValue(int value) {
		SBUEnum rt = null;
		for (SBUEnum e : SBUEnum.values()) {
			if (e.valueOf() == value) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static SBUEnum getSBUByStringValue(String value) {
		SBUEnum rt = null;
		for (SBUEnum e : SBUEnum.values()) {
			if (e.name().equalsIgnoreCase(value)) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static SBUEnum getSBU(int value) {
		SBUEnum rt = null;
		for (SBUEnum e : SBUEnum.values()) {
			if (e.getSbu() == value) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static SBUEnum getSBU(String value) {
		SBUEnum rt = null;
		for (SBUEnum e : SBUEnum.values()) {
			if (e.name().equalsIgnoreCase(value)) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static Integer getSBUTypeIntfromString(String value) {
		Integer rt = null;
		for (SBUEnum e : SBUEnum.values()) {
			if (e.name().equals(value)) {
				rt = e.valueOf();
				break;
			}
		}
		return rt;
	}

	public static Integer getSBUType(SBUEnum value) {
		Integer rt = null;
		for (SBUEnum e : SBUEnum.values()) {
			if (e == value) {
				rt = e.getSbu();
				break;
			}
		}
		return rt;
	}

	public static Optional<SBUEnum> getSBUByValue(String value) {
		if (value == null)
			return Optional.empty();
		else {

			Integer sbuValue = Integer.parseInt(value);
			return Arrays.asList(values()).stream().filter(m -> m.valueOf() == sbuValue).findFirst();
		}
	}

}
