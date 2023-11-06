package lk.dialog.pe.ccbs.util;

public enum SBU {
	DTV_PREPAID(1), DTV_POSTPAID(2), GSM(3), DBN(4), VOLTE(4), DCS(4);

	private int value;

	private SBU(int val) {
		this.value = val;
	}

	public int valueOf() {
		return this.value;
	}

	public static SBU getSBU(int value) {
		SBU rt = null;
		for (SBU e : SBU.values()) {
			if (e.valueOf() == value) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static SBU getSBU(String value) {
		SBU rt = null;
		for (SBU e : SBU.values()) {
			if (e.name().equalsIgnoreCase(value)) {
				rt = e;
				break;
			}
		}
		return rt;
	}
}

