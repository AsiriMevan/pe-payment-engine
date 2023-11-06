package lk.dialog.pe.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

	private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

	public static Timestamp getSQlTimestamp(java.util.Date utilDate) {

		return new java.sql.Timestamp(utilDate.getTime());
	}

	public static java.util.Date timeStampToUtilDate(java.sql.Timestamp timeStamp) {
		return new java.util.Date(timeStamp.getTime());
	}

	public static java.util.Calendar timeStampToCalender(java.sql.Timestamp timeStamp) {
		java.util.Date utilDate = null;
		Calendar cal = null;
		if (timeStamp != null) {
			utilDate = new java.util.Date(timeStamp.getTime());
			cal = Calendar.getInstance();
			cal.setTime(utilDate);
		}
		return cal;
	}

	public static java.util.Calendar timeStampToCalenderTimeModiify(java.sql.Timestamp timeStamp) {
		java.util.Date utilDate = null;
		Calendar cal = null;
		if (timeStamp != null) {
			utilDate = new java.util.Date(timeStamp.getTime());
			cal = Calendar.getInstance();
			cal.setTime(utilDate);
			cal.add(Calendar.HOUR_OF_DAY, 5); // adds five hour
			cal.add(Calendar.MINUTE, 30); // adds thirty Minute

			if (cal.get(Calendar.AM_PM) == Calendar.PM) {
				cal.set(Calendar.AM_PM, Calendar.PM);
			} else if (cal.get(Calendar.AM_PM) == Calendar.AM) {
				cal.set(Calendar.AM_PM, Calendar.AM);
			}
		}
		return cal;
	}

	public static java.util.Calendar getCalender(java.sql.Date sqlDate) {
		java.util.Date calenderDate = new java.util.Date(sqlDate.getTime());
		Calendar cal = Calendar.getInstance();
		cal.setTime(calenderDate);
		return cal;
	}

	public static java.sql.Date getSQlDate(java.util.Date utilDate) {
		java.sql.Date sqlDate = null;
		if (utilDate != null)
			sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}

	public static java.util.Calendar getCalender(java.util.Date utilDate) {
		Calendar cal = null;

		if (utilDate != null) {
			cal = Calendar.getInstance();
			cal.setTime(utilDate);

		}

		return cal;
	}

	public static void main(String[] args) {
		Date date = new Date();
		DateFormat gmtFormat = new SimpleDateFormat();
		TimeZone gmtTime = TimeZone.getTimeZone("GMT");
		displayTimeZone(gmtTime);
		gmtFormat.setTimeZone(gmtTime);
		String formattedGmtTime = gmtFormat.format(date);

		LOGGER.info("Current Time from Util class : date={}", date);
		LOGGER.info("GMT Time from Util class : GMTdate={}", formattedGmtTime);

	}

	private static String displayTimeZone(TimeZone tz) {

		long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset()) + 5;
		long minutes = (TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours)) + 30;
		// avoid -4:-30 issue
		minutes = Math.abs(minutes);

		String result = "";
		if (hours > 0) {
			result = String.format("(GMT+%d:%02d) %s", hours, minutes, tz.getID());
		} else {
			result = String.format("(GMT%d:%02d) %s", hours, minutes, tz.getID());
		}

		return result;

	}
}
