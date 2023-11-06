package lk.dialog.pe.ccbs.util;

import java.util.Arrays;
import java.util.Optional;

public enum SBUEnum {

	SBU_ALL(0), 
	SBU_DTV_PRE(1),
	DTV_POSTPAID(2),
	SBU_DTV_POST(2),
	SBU_GSM(3), 
	SBU_DBN(4);
	
	private final Integer value; 

	private SBUEnum(Integer value) {
		this.value = value;
	}

	public Integer getValue() {
		return value;
	}

	public static Optional<SBUEnum> getSBUByValue(String value) {
		if (value == null) 
			return Optional.empty();
		else {
			
			Integer sbuValue = Integer.parseInt(value);
			return Arrays.asList(values())
				.stream()
				.filter(m -> m.getValue().equals(sbuValue))
				.findFirst();
		}
	}
}