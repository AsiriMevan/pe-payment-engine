package lk.dialog.pe.common.util;

import java.util.Arrays;

public enum ConnectionStatus {
	C(1), D(2), B(3), T(3), NC(4);

	private int value;

	private ConnectionStatus(int val) {
		this.value = val;
	}

	public int valueOf() {
		return this.value;
	}

	public static ConnectionStatus getConnectionStatus(int value) {
		ConnectionStatus rt = null;
		for (ConnectionStatus e : ConnectionStatus.values()) {
			if (e.valueOf() == value) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public static ConnectionStatus getConnectionStatus(String value) {
		ConnectionStatus rt = null;
		for (ConnectionStatus e : ConnectionStatus.values()) {
			if (e.name().equalsIgnoreCase(value)) {
				rt = e;
				break;
			}
		}
		return rt;
	}

	public boolean orEqual(ConnectionStatus... compareWith) {

		return Arrays.stream(compareWith).anyMatch(val -> val == this);
	}
}
