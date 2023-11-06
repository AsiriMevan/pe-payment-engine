package lk.dialog.pe.persistence.util;

import java.util.Arrays;
import java.util.Optional;

public enum QuerySystemEnum {

	BS(1), PE(2), CPOS(3);

	private final Integer value;

	private QuerySystemEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static Optional<QuerySystemEnum> getQuerySystemByValue(String value) {
		if (value == null)
			return Optional.empty();
		else {
			Integer type = Integer.parseInt(value);
			return Arrays.asList(values()).stream().filter(m -> m.getValue().equals(type)).findFirst();
		}
	}
}
