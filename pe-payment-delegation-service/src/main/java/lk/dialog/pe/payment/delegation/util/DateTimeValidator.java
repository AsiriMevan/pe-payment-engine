package lk.dialog.pe.payment.delegation.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lk.dialog.pe.common.util.PaymentDelegationEnum;


public class DateTimeValidator {

	private DateTimeValidator() {}
//	Uses to validate the date against the passed date format.
	public static String validateDateFormat(List<Date> dateList) {

		String validateStatus = null;
		DateFormat dateFormatter = new SimpleDateFormat(PaymentDelegationEnum.PAYMENT_PENDDING_DATE_FORMAT.getName());
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
