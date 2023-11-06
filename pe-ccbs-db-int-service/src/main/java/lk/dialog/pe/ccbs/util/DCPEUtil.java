package lk.dialog.pe.ccbs.util;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;

public class DCPEUtil {

	private DCPEUtil() {
	}
	public static String convertToString(Object object) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(object);
		} catch (Exception e) {
			return null;
		}
	}

	public static Long getTimeTaken(Instant start) {
		return Duration.between(start, Instant.now()).toMillis();
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

	public static String getTraceId() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmmss");
		String dateTime = LocalDateTime.now().format(formatter);
		StringBuilder stringBuilder = new StringBuilder(dateTime);
		stringBuilder.append(ThreadLocalRandom.current().nextInt(50000, 800000));

		return String.format("DCPE%s", stringBuilder.substring(6, 18));
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