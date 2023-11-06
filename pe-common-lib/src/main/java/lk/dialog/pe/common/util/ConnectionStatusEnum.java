package lk.dialog.pe.common.util;

import java.util.Arrays;
import java.util.Optional;

public enum ConnectionStatusEnum {

	C(1), D(2), B(3), T(3), NC(4);

	private final Integer value;

	private ConnectionStatusEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static Optional<ConnectionStatusEnum> getConnectionStatusByValue(String value) {
		if (value == null)
			return Optional.empty();
		else {
			Integer connectionStatus = Integer.parseInt(value);
			return Arrays.asList(values()).stream().filter(m -> m.getValue().equals(connectionStatus)).findFirst();
		}
	}
}
