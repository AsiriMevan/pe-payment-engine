package lk.dialog.pe.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class DateTimeValidator {
	private DateTimeValidator() {
		
	}

//	Uses to validate the date against the passed date format.
	public static String validateDateFormat(List<Date> dateList, String dateFormat) {

		String validateStatus = null;
		DateFormat dateFormatter = new SimpleDateFormat(dateFormat);
		dateFormatter.setLenient(false);

		for (Date date : dateList) {
			if (date != null) {
				try {
					dateFormatter.parse(dateFormatter.format(date));
				} catch (Exception e) {
					validateStatus = e.getMessage();
					break;
				}
			}

		}

		return validateStatus;
	}
	
}

