package cn.com.workflow.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 
 * @author Administrator
 * 
 */
public class DateUtil {
	private static Log log = LogFactory.getLog(DateUtil.class);
	public static String DATE_PATTERN = "yyyy-MM-dd";;
	public static String TIME_PATTERN = DATE_PATTERN + " HH:MM:ss";
	/**
	 * 
	 */
	public static final long MILLIS_PER_SECOND = 1000;
	/**
	 * 
	 */
	public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	/**
	 * 
	 */
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	/**
	 * 
	 */
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
	/**
	 * 
	 */
	public static final long SECOND_PER_DAY = 24 * 60 * 60;

	public final static int WeekSpan = 7;
	public static int month[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30,
			31 };

	/**
	 * 锟叫讹拷锟角凤拷锟斤拷锟斤拷
	 * 
	 * @param year
	 * @return
	 */
	private static boolean leapYear(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 锟皆革拷位锟斤拷锟斤拷锟铰凤拷之前锟斤拷锟斤拷
	 * 
	 * @param month
	 * @return
	 */
	private static String impleMonth(int month) {
		String monthStr = Integer.toString(month);
		if (monthStr.length() == 1) {
			monthStr = "0" + monthStr;
		}
		return monthStr;
	}

	/**
	 * 锟皆革拷位锟斤拷锟斤拷锟斤拷锟斤拷之前锟斤拷锟斤拷
	 * 
	 * @param day
	 * @return
	 */
	private static String impleDay(int day) {
		String dayStr =  Integer.toString(day);
		if (dayStr.length() == 1) {
			dayStr = "0" + dayStr;
		}
		return dayStr;
	}

	/**
	 * 锟矫碉拷锟斤拷前锟杰的碉拷一锟届（锟斤拷一锟斤拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @return
	 */
	public static String getWeekFirstDate() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		// 锟斤拷前锟斤拷锟斤拷锟斤拷锟斤拷锟节硷拷
		if (dayOfWeek != Calendar.SUNDAY) {
			cal.add(Calendar.DATE, Calendar.MONDAY - dayOfWeek);
		} else {
			cal.add(Calendar.DATE, Calendar.SUNDAY - Calendar.MONTH);
		}
		String weekFirstDay = Integer.toString(cal.get(Calendar.YEAR));
		weekFirstDay = weekFirstDay + "-"
				+ impleMonth(cal.get(Calendar.MONTH) + 1);
		weekFirstDay = weekFirstDay + "-" + impleDay(cal.get(Calendar.DATE));
		return weekFirstDay;
	}

	/**
	 * 锟矫碉拷锟斤拷前锟杰碉拷锟斤拷锟揭伙拷欤拷锟斤拷眨锟斤拷锟斤拷锟斤拷锟�
	 * 
	 * @return
	 */
	public static String getWeekLastDate() {
		Calendar cal = Calendar.getInstance();
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 锟斤拷前锟斤拷锟斤拷锟斤拷锟斤拷锟节硷拷
		// 锟斤拷锟斤拷锟角帮拷锟斤拷锟斤拷锟斤拷锟斤拷眨锟斤拷锟角帮拷锟轿拷锟斤拷一锟斤拷
		if (dayOfWeek != Calendar.SUNDAY) {
			cal.add(Calendar.DATE, 7 - dayOfWeek + Calendar.SUNDAY);
		}
		String weekLastDay = Integer.valueOf(cal.get(Calendar.YEAR)).toString();
		weekLastDay = weekLastDay + "-"
				+ impleMonth(cal.get(Calendar.MONTH) + 1);
		weekLastDay = weekLastDay + "-" + impleDay(cal.get(Calendar.DATE));
		return weekLastDay;
	}

	/**
	 * 锟矫碉拷锟斤拷前锟铰的碉拷一锟斤拷锟斤拷锟斤拷锟�
	 * 
	 * @return
	 */
	public static String getFirstDateOfMonth() {
		Calendar cal = Calendar.getInstance();
		String monthFirstDay = Integer.valueOf(cal.get(Calendar.YEAR)).toString()
				+ "-" + impleMonth(cal.get(Calendar.MONTH) + 1) + "-01";
		return monthFirstDay;
	}

	/**
	 * 锟矫碉拷锟斤拷前锟铰碉拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @return
	 */
	public static String getLastDateOfMonth() {
		return getLastDateOfMonth(Calendar.getInstance());
	}

	/**
	 * 锟矫碉拷一锟斤拷锟铰碉拷锟斤拷锟揭伙拷锟斤拷锟斤拷锟斤拷
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getLastDateOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		return getLastDateOfMonth(cal);
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static String getLastDateOfMonth(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		return getLastDateOfMonth(cal);
	}

	/**
	 * 
	 * @param cal
	 * @return
	 */
	public static String getLastDateOfMonth(Calendar cal) {
		int monthSpan = ((cal.get(Calendar.MONTH) + 1) == 2 && leapYear(cal
				.get(Calendar.YEAR))) ? 29 : month[cal.get(Calendar.MONTH)];
		String MonthLastDay = Integer.valueOf(cal.get(Calendar.YEAR)).toString()
				+ "-" + impleMonth(cal.get(Calendar.MONTH) + 1) + "-"
				+ impleDay(monthSpan);
		return MonthLastDay;
	}

	/**
	 * 锟矫碉拷锟斤拷前锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷锟节革拷式为 YYYY-MM-DD
	 * 
	 * @return
	 */
	public static String getCurrentDateStr() {
		Calendar cal = Calendar.getInstance();
		String currentDay = Integer.valueOf(cal.get(Calendar.YEAR)).toString();
		currentDay = currentDay + "-" + impleMonth(cal.get(Calendar.MONTH) + 1);
		currentDay = currentDay + "-" + impleDay(cal.get(Calendar.DATE));
		return currentDay;
	}

	/**
	 * 
	 * @return
	 */
	public static Date getCurrentDateSys() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 
	 * @param s
	 * @param len
	 * @return
	 */
	private static String strLen(String s, int len) {
		if (s == null || s.trim().length() <= 0) {
			s = "";
		}
		if (s.length() == 8) {
			return s;
		}
		for (int i = 0; i < len - s.length(); i++) {
			s = "0" + s;
			if (s.length() == 8) {
				break;
			}
		}
		return s;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷
	 * 
	 * @param cal
	 * @return
	 */
	public static String getYear(Calendar cal) {
		return String.valueOf(cal.get(Calendar.YEAR));
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷(锟斤拷位)
	 * 
	 * @param cal
	 * @return
	 */
	public static String getMonth(Calendar cal) {
		return strLen(String.valueOf(cal.get(Calendar.MONTH) + 1), 2);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷(锟斤拷位)
	 * 
	 * @param cal
	 * @return
	 */
	public static String getDay(Calendar cal) {
		return strLen(String.valueOf(cal.get(Calendar.DAY_OF_MONTH)), 2);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷时锟街凤拷锟斤拷(锟斤拷位)
	 * 
	 * @param cal
	 * @return
	 */
	public static String getHour(Calendar cal) {
		return strLen(String.valueOf(cal.get(Calendar.HOUR_OF_DAY)), 2);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟侥凤拷锟街凤拷锟斤拷(锟斤拷位)
	 * 
	 * @param cal
	 * @return
	 */
	public static String getMinute(Calendar cal) {
		return strLen(String.valueOf(cal.get(Calendar.MINUTE)), 2);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷(锟斤拷位)
	 * 
	 * @param cal
	 * @return
	 */
	public static String getSecond(Calendar cal) {
		return strLen(String.valueOf(cal.get(Calendar.SECOND)), 2);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷式锟斤拷"yyyy-mm-dd"锟斤拷
	 * 
	 * @param cal
	 * @return
	 */
	public static String getDateStr(Calendar cal) {
		return getYear(cal) + "-" + getMonth(cal) + "-" + getDay(cal);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷式锟斤拷"hh:ss"锟斤拷
	 * 
	 * @param cal
	 * @return
	 */
	public static String getTimeStr(Calendar cal) {
		return getHour(cal) + ":" + getMinute(cal);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷式锟斤拷"yyyy-mm-dd hh:ss"锟斤拷
	 * 
	 * @param cal
	 * @return
	 */
	public static String getDate(Calendar cal) {
		return getDateStr(cal) + " " + getTimeStr(cal);
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷("yyyy-mm-dd hh:ss:mm")锟斤拷锟斤拷
	 * 
	 * @param s
	 * @return
	 */
	public static int getYear(String s) {
		if (s == null || s.length() < 10) {
			return 1970;
		}
		return Integer.parseInt(s.substring(0, 4));
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷("yyyy-mm-dd hh:ss:mm")锟斤拷锟斤拷
	 * 
	 * @param s
	 * @return
	 */
	public static int getMonth(String s) {
		if (s == null || s.length() < 10) {
			return 1;
		}
		return Integer.parseInt(s.substring(5, 7));
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷("yyyy-mm-dd hh:ss:mm")锟斤拷锟斤拷
	 * 
	 * @param s
	 * @return
	 */
	public static int getDay(String s) {
		if (s == null || s.length() < 10) {
			return 1;
		}
		return Integer.parseInt(s.substring(8, 10));
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷("yyyy-mm-dd hh:ss:mm")锟斤拷时
	 * 
	 * @param s
	 * @return
	 */
	public static int getHour(String s) {
		if (s == null || s.length() < 16) {
			return 0;
		}
		return Integer.parseInt(s.substring(11, 13));
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷("yyyy-mm-dd hh:ss:mm")锟侥凤拷
	 * 
	 * @param s
	 * @return
	 */
	public static int getMinute(String s) {
		if (s == null || s.length() < 16) {
			return 0;
		}
		return Integer.parseInt(s.substring(14, 16));
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷("yyyy-mm-dd hh:ss:mm")锟斤拷锟斤拷
	 * 
	 * @param s
	 * @return
	 */
	public static int getSecond(String s) {
		if (s == null || s.length() < 18) {
			return 0;
		}
		return Integer.parseInt(s.substring(16, 18));
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷锟街凤拷锟斤拷锟斤拷应锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷式锟斤拷"yyyy-mm-dd hh:ss"锟斤拷
	 * 
	 * @param s
	 * @return
	 */
	public static Calendar getCal(String s) {
		Calendar cal = Calendar.getInstance();
		cal.set(getYear(s), getMonth(s), getDay(s), getHour(s), getMinute(s),
				getSecond(s));
		return cal;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟斤拷时锟斤拷锟街凤拷锟斤拷锟斤拷应锟斤拷SQL锟斤拷锟节ｏ拷锟斤拷式锟斤拷"yyyy-mm-dd hh:ss"锟斤拷
	 * 
	 * @param s
	 * @return
	 */
	public static java.sql.Date getSqlDate(String s) {
		return java.sql.Date.valueOf(s);
	}

	/**
	 * 锟斤拷锟截碉拷锟斤拷锟斤拷锟节讹拷应锟斤拷SQL锟斤拷锟节ｏ拷锟斤拷
	 * 
	 * @return
	 */
	public static java.sql.Date getSqlDate() {
		return java.sql.Date.valueOf(getNowDate());
	}

	/**
	 * 取锟斤拷前锟斤拷锟斤拷时锟斤拷锟斤拷址锟斤拷锟斤拷锟斤拷锟绞轿�yyyy-mm-dd hh:ss"
	 * 
	 * @return
	 */
	public static String getNow() {
		Calendar now = Calendar.getInstance();
		return getDateStr(now) + " " + getTimeStr(now);
	}

	/**
	 * 取锟斤拷前锟斤拷锟节碉拷锟街凤拷锟斤拷锟斤拷锟斤拷式为"yyyy-mm-dd"
	 * 
	 * @return
	 */
	public static String getNowDate() {
		Calendar now = Calendar.getInstance();
		return getDateStr(now);
	}

	/**
	 * 取锟斤拷前时锟斤拷锟斤拷址锟斤拷锟斤拷锟斤拷锟绞轿�hh:ss"
	 * 
	 * @return
	 */
	public static String getNowTime() {
		Calendar now = Calendar.getInstance();
		return getTimeStr(now);
	}

	/**
	 * 取锟斤拷前时锟斤拷锟斤拷址锟斤拷锟�
	 * 
	 * @return
	 */
	public static String getCurrentTimeMillisStr() {

		return (Long.valueOf(System.currentTimeMillis()).toString());
	}

	/**
	 * 锟斤拷锟捷碉拷前时锟斤拷暮锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷January 1, 1970 00:00:00锟斤拷锟斤拷取锟斤拷前时锟斤拷锟斤拷址锟斤拷锟�
	 * 
	 * @param time
	 * @return
	 */
	public static String changTimeMillisToStr(String time) {
		long t = 0l;
		try {
			t = Long.parseLong(time);
		} catch (Exception e) {
			return "";
		}
		Date date = new Date();
		date.setTime(t);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return getDateStr(cal) + " " + getTimeStr(cal);
	}

	/**
	 * 锟斤拷锟捷碉拷前时锟斤拷暮锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷January 1, 1970 00:00:00锟斤拷锟斤拷取锟斤拷前时锟斤拷锟斤拷址锟斤拷锟�
	 * 
	 * @param time
	 * @return
	 */
	public static String changTimeMillisToStr(long time) {
		String str = "";
		try {
			str = Long.toString(time);
		} catch (Exception e) {
		}
		return changTimeMillisToStr(str);
	}

	/**
	 * 锟斤拷式锟斤拷锟街凤拷锟斤拷为锟斤拷锟节的猴拷锟斤拷.
	 * 
	 * @param strDate
	 *            锟街凤拷锟斤拷.
	 * @param format
	 *            转锟斤拷锟斤拷式锟斤拷:"yyyy-MM-dd mm:ss"
	 * @return 锟街凤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷.
	 */
	public static Date parseDate(String strDate, String format) {
		try {
			if (strDate == null || strDate.equals(""))
				return null;
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
			return simpleDateFormat.parse(strDate);
		} catch (Exception e) {
		}
		return new Date();
	}

	/**
	 * 锟斤拷式锟斤拷锟斤拷锟斤拷为锟街凤拷锟斤拷锟斤拷锟斤拷.
	 * 
	 * @param date
	 *            锟斤拷锟斤拷.
	 * @param format
	 *            转锟斤拷锟斤拷式."yyyy-MM-dd mm:ss"
	 * @return 锟斤拷锟斤拷转锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷.
	 */
	public static String formatDate(Date date, String format) {
		if (date == null)
			return "";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		return simpleDateFormat.format(date);
	}

	/**
	 * 锟斤拷锟斤拷缺省锟斤拷时锟斤拷模式 (yyyy-MM-dd)
	 * 
	 * @return a string representing the date pattern on the UI
	 */
	public static String getDatePattern() {
		return DATE_PATTERN;
	}

	/**
	 * 锟斤拷锟斤拷锟斤拷锟节凤拷锟斤拷yyyy-MM-dd锟斤拷式锟斤拷锟斤拷锟斤拷
	 * 
	 * @param aDate
	 *            date from database as a string
	 * @return formatted string for the ui
	 */
	public static final String getDate(Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(DATE_PATTERN);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 锟斤拷锟斤拷指锟斤拷锟侥革拷式锟斤拷锟斤拷一锟斤拷锟斤拷锟斤拷锟街凤拷锟斤拷转锟斤拷为 Date锟斤拷锟斤拷
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param strDate
	 *            a string representation of a date
	 * @return a converted Date object
	 * @throws ParseException
	 * @see java.text.SimpleDateFormat
	 */
	public static final Date convertStringToDate(String aMask, String strDate) {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);

		if (log.isDebugEnabled()) {
			log.debug("converting '" + strDate + "' to date with mask '"
					+ aMask + "'");
		}
		if (strDate == null) {
			return null;
		}
		if (strDate.length() == 0) {
			return null;
		}
		try {
			date = df.parse(strDate);
		} catch (ParseException pe) {
			log.error("ParseException: " + pe);
			// throw new ParseException(pe.getMessage(), pe.getErrorOffset());
			return null;
		}

		return (date);
	}

	/**
	 * 锟斤拷yyyy-MM-dd HH:MM a锟斤拷锟节革拷式锟斤拷锟截碉拷前锟斤拷时锟斤拷
	 * 
	 * 
	 * @param theTime
	 *            the current time
	 * @return the current date/time
	 */
	public static String getTimeNow(Date theTime) {
		return getDateTime(TIME_PATTERN, theTime);
	}

	/**
	 * 锟斤拷锟截碉拷前锟斤拷锟斤拷锟节ｏ拷锟斤拷式yyyy-MM-dd
	 * 
	 * @return the current date
	 * @throws ParseException
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(DATE_PATTERN);

		// This seems like quite a hack (date -> string -> date),
		// but it works ;-)
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(convertStringToDate(todayAsString));

		return cal;
	}

	/**
	 * 锟斤拷锟斤拷指锟斤拷锟斤拷锟斤拷前锟斤拷式锟斤拷锟斤拷锟斤拷锟斤拷锟节讹拷锟斤拷锟接︼拷锟斤拷址锟斤拷锟斤拷锟�
	 * 
	 * @param aMask
	 *            the date pattern the string is in
	 * @param aDate
	 *            a date object
	 * @return a formatted string representation of the date
	 * @see java.text.SimpleDateFormat
	 */
	public static final String getDateTime(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";

		if (aDate != null) {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	/**
	 * 锟斤拷锟斤拷Date 锟斤拷锟襟返伙拷 锟斤拷锟斤拷锟节革拷式锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷锟节革拷式锟斤拷yyyy-MM-dd
	 * 
	 * @param aDate
	 *            A date to convert
	 * @return a string representation of the date
	 */
	public static final String convertDateToString(Date aDate) {
		return getDateTime(DATE_PATTERN, aDate);
	}

	/**
	 * 锟斤拷锟斤拷Date 锟斤拷锟襟返伙拷 锟斤拷锟斤拷锟节革拷式锟斤拷锟街凤拷锟斤拷锟斤拷锟斤拷锟节革拷式锟斤拷yyyy-MM-dd
	 * 
	 * @param aDate
	 *            A date to convert
	 * @return a string representation of the date
	 */
	public static final String convertDateToString(Date aDate, String format) {
		return getDateTime(format, aDate);
	}

	/**
	 * 使锟斤拷锟斤拷锟节革拷式锟斤拷yyyy-MM-dd锟斤拷转锟斤拷一锟斤拷锟街凤拷锟斤拷为Date锟斤拷锟斤拷
	 * 
	 * @param strDate
	 *            the date to convert (in format yyyy-MM-dd)
	 * @return a date object
	 * @throws ParseException
	 */
	public static Date convertStringToDate(String strDate) {
		Date aDate = null;

		try {
			if (log.isDebugEnabled()) {
				log.debug("converting date with pattern: " + DATE_PATTERN);
			}

			aDate = convertStringToDate(DATE_PATTERN, strDate);
		} catch (Exception pe) {
			log.error("Could not convert '" + strDate
					+ "' to a date, throwing exception");
			pe.printStackTrace();
			return null;

		}

		return aDate;
	}

	/**
	 * 
	 * @return
	 */
	public static String getOracleDatetimeStr() {
		Calendar cal = Calendar.getInstance();
		return "to_date('" + cal.getTime() + "','yyyy-mm-dd hh:Mi:ss')";
	}

	public static java.sql.Date getsqlDate(java.util.Date udate) {
		java.sql.Date date;
		if (udate != null)
			date = new java.sql.Date(udate.getTime());
		else
			date = null;

		return date;
	}

	public static String getTimePattern() {
		return TIME_PATTERN;
	}

	public static void setTimePattern(String timePattern) {
		TIME_PATTERN = timePattern;
	}

	public static void setDatePattern(String datePattern) {
		DATE_PATTERN = datePattern;
	}

	/**
	 * 
	 * @param time
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static Date calculate(Date time, int year, int month, int date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.YEAR, year);
		cal.add(Calendar.MONTH, month);
		cal.add(Calendar.DATE, date);
		return cal.getTime();
	}
}
