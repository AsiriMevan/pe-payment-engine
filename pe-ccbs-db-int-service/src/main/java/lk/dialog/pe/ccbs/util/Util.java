/**
 * 
 */
package lk.dialog.pe.ccbs.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class Util {

	private Util() {
	}

	public static Timestamp getSQlTimestamp(Date utilDate) {
		return new Timestamp(utilDate.getTime());
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
			cal.add(Calendar.HOUR_OF_DAY, 5);
			cal.add(Calendar.MINUTE, 30);

			if (cal.get(Calendar.AM_PM) == Calendar.PM) {
				cal.set(Calendar.AM_PM, Calendar.PM);
			} else if (cal.get(Calendar.AM_PM) == Calendar.AM) {
				cal.set(Calendar.AM_PM, Calendar.AM);
			}
		}
		return cal;
	}

	public static java.util.Date getUtilDate(java.sql.Date sqlDate) {
		return new java.util.Date(sqlDate.getTime());

	}

	public static java.sql.Date getSQlDate(java.util.Date utilDate) {
		java.sql.Date sqlDate = null;
		if (utilDate != null)
			sqlDate = new java.sql.Date(utilDate.getTime());
		return sqlDate;
	}

	public static java.util.Calendar getCalender(java.util.Date utilDate){
		Calendar cal = null;
		if (utilDate != null) {
			cal = Calendar.getInstance();
			cal.setTime(utilDate);

		}
		return cal;
	}

	public static void main(String[] args) {
		DateFormat gmtFormat = new SimpleDateFormat();
		TimeZone gmtTime = TimeZone.getTimeZone("GMT");
		displayTimeZone(gmtTime);
		gmtFormat.setTimeZone(gmtTime);

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
