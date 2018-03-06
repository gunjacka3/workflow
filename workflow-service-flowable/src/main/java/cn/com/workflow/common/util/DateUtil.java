package cn.com.workflow.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 日期工具类
 * 
 * @author Spring.Cao
 * @version v1.0 2013-03-22
 */
public class DateUtil {
    private static String defaultDatePattern = "yyyy-MM-dd";

    /**
     * 获得一个date pattern
     */
    public static String getDatePattern() {
        return defaultDatePattern;
    }

    /**
     * 格式化当前日期
     */
    public static String getToday() {
        Date today = new Date();
        return format(today);
    }

    /**
     * 格式化指定日期
     */
    public static String format(Date date) {
        return date == null ? " " : format(date, getDatePattern());
    }

    /**
     * 按格式格式化指定日期字符串
     */
    public static String format(Date date, String pattern) {
        return date == null ? " " : new SimpleDateFormat(pattern).format(date);
    }

    /**
     * 获取Date
     */
    public static Date parse(String strDate) throws ParseException {
        return StringUtils.isBlank(strDate) ? null : parse(strDate, getDatePattern());
    }

    /**
     * 获取格式化Date
     */
    public static Date parse(String strDate, String pattern) throws ParseException {
        return StringUtils.isBlank(strDate) ? null : new SimpleDateFormat(pattern).parse(strDate);
    }

    /**
     * 增加N个月
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 获取某个月的最后一天
     * 
     * @param year
     * @param month
     * @return
     * @author wangzhiyin 2017年9月27日 上午9:45:16
     */
    public static String getLastDayOfMonth(String year, String month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(year));
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, Integer.parseInt(month));
        cal.add(Calendar.DATE, -1);
        return String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 根据年月日计算Date
     * 
     * @param year
     * @param month
     * @param day
     * @return
     * @throws ParseException
     * @author wangzhiyin 2017年9月27日 上午9:45:54
     */
    public static Date getDate(String year, String month, String day) throws ParseException {
        String result = year + "-" + (month.length() == 1 ? ("0 " + month) : month) + "- "
                + (day.length() == 1 ? ("0 " + day) : day);
        return parse(result);
    }
}
