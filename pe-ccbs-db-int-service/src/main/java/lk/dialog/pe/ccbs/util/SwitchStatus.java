package lk.dialog.pe.ccbs.util;

public enum SwitchStatus {

	C(1), D(2), B(3), T(3), NC(4), OK(1), TX(2), SU(3), PE(4), A(1), I(4), P(4);

	private int value;

	private SwitchStatus(int val) {
		this.value = val;
	}

	public int valueOf() {
		return this.value;
	}

	public static SwitchStatus getSwitchStatus(int value) {
		SwitchStatus rt = null;
		for (SwitchStatus e : SwitchStatus.values()) {
			if (e.valueOf() == value) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static SwitchStatus getSwitchStatus(String value) {
		SwitchStatus rt = null;
		for (SwitchStatus e : SwitchStatus.values()) {
			if (e.name().equalsIgnoreCase(value)) {
				rt = e;
				break;
			}
		}
		return rt;
	}

}
