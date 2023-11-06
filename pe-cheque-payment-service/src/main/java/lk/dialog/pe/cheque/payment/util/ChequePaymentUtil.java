package lk.dialog.pe.cheque.payment.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.Instant;

public class ChequePaymentUtil {

	private ChequePaymentUtil() {
		throw new IllegalStateException("DTVUtil class");
	}

	public static Long getTimeTaken(Instant start) {
		return Duration.between(start, Instant.now()).toMillis();
	}
	
	public static String convertToString(Object object) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static boolean traceIdValidation(String traceId) {
		return !(traceId == null || !isLengthEqualValid(traceId, 15) || hasEmptySpace(traceId) || haveSpecialCharacters(traceId) || !isTraceIdFormat(traceId));
	}
	
	public static boolean isLengthEqualValid(String inputString, int length) {
		return (inputString.length() == length);
	}
	
	private static boolean hasEmptySpace(String string) {
		Pattern whitespace = Pattern.compile("\\s");
		Matcher matcher = whitespace.matcher(string);
		return matcher.find();
	}
	
	public static boolean haveSpecialCharacters(String input) {

		Pattern special = Pattern.compile("[^A-Za-z0-9]+");
		Matcher hasSpecial = special.matcher(input);
		return (hasSpecial.find());
	}

	public static boolean isTraceIdFormat(String inputString) {
		String head = inputString.substring(0, 3);
		String tail = inputString.substring(3, 15);
		return isCharacters(head) && isNumeric(tail);
	}
	
	public static boolean isCharacters(String input) {
		return input.matches("^[a-zA-Z]*$");
	}

	public static boolean isNumeric(String stringNumber) {
		Pattern numeric = Pattern.compile("\\D");
		Matcher digits = numeric.matcher(stringNumber);
		return !digits.find();
	}
}
