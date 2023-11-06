package lk.dialog.pe.common.util;

import java.util.Arrays;

public enum QuerySystem {
	
	BS(1), PE(2), CPOS(3);

	private int value;

	private QuerySystem(int val) {
		this.value = val;
	}

	public int valueOf() {
		return this.value;
	}

	public static QuerySystem getQuerySystem(int value) {
		QuerySystem rt = null;
		for (QuerySystem e : QuerySystem.values()) {
			if (e.valueOf() == value) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static QuerySystem getQuerySystem(String value) {
		QuerySystem rt = null;
		for (QuerySystem e : QuerySystem.values()) {
			if (e.name().equalsIgnoreCase(value)) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public boolean orEqual(QuerySystem... compareWith) {
		return Arrays.stream(compareWith).anyMatch(val -> val == this);
	}

}
