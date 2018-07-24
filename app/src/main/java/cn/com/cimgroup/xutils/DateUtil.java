package cn.com.cimgroup.xutils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.regex.Pattern;

import cn.com.cimgroup.App;
import cn.com.cimgroup.R;
import android.text.TextUtils;

/**
 * @title: 时间工具类
 * @description: 用户操作时间
 * @company: 三五互联
 * @author 杨福海
 * @version 1.0
 * @created on 2011-3-3
 */
public final class DateUtil {

	/**
	 * 格式：年－月－日 小时：分钟：秒
	 */
	public static final String FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 格式：年－月－日 小时：分钟
	 */
	public static final String FORMAT_TWO = "yyyy-MM-dd HH:mm";

	/**
	 * 格式：年月日 小时分钟秒
	 */
	public static final String FORMAT_THREE = "yyyyMMdd-HHmmss";

	/**
	 * 格式：年－月－日
	 */
	public static final String LONG_DATE_FORMAT = "yyyy-MM-dd";
	/**
	 * 格式：年月日
	 */
	public static final String LONG_DATE_FORMAT2 = "yyyyMMdd";

	/**
	 * 格式：月－日
	 */
	public static final String SHORT_DATE_FORMAT = "MM-dd";

	/**
	 * 格式: 年月日时分秒
	 */
	public static final String SHORT_LINE_FORMAT = "yyyyMMddHHmmss";

	/**
	 * 格式: 年月日时分秒
	 */
	public static final String SHORT_LINE_FORMAT_TWO = "yyyyMMddHHmm";
	/**
	 * 格式：小时：分钟：秒
	 */
	public static final String LONG_TIME_FORMAT = "HH:mm:ss";
	/**
	 * 格式：小时：分钟
	 */
	public static final String LONG_TIME_FORMAT2 = "HH:mm";
	/**
	 * 格式：年-月
	 */
	public static final String MONTG_DATE_FORMAT = "yyyy-MM";

	/**
	 * 年的加减
	 */
	public static final int SUB_YEAR = Calendar.YEAR;

	/**
	 * 月加减
	 */
	public static final int SUB_MONTH = Calendar.MONTH;

	/**
	 * 天的加减
	 */
	public static final int SUB_DAY = Calendar.DATE;

	/**
	 * 小时的加减
	 */
	public static final int SUB_HOUR = Calendar.HOUR;

	/**
	 * 分钟的加减
	 */
	public static final int SUB_MINUTE = Calendar.MINUTE;

	/**
	 * 秒的加减
	 */
	public static final int SUB_SECOND = Calendar.SECOND;

	static final String dayNames[] = App.getInstance().getResources()
			.getStringArray(R.array.one_week);
	/**
	 * 默认formater yyyy-MM-dd HH:mm:ss
	 */
	public static final SimpleDateFormat TIMEFORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.getDefault());

	public DateUtil() {
	}

	/**
	 * 实例化SimpleDateFormat
	 * 
	 * @Description:
	 * @param format
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static SimpleDateFormat getFormater(String format) {
		return new SimpleDateFormat(format, Locale.getDefault());
	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 * 
	 * @param dateStr
	 * @return null or date
	 */
	public static Date stringtoDate(String dateStr, String format) {
		Date d = null;
		try {
			SimpleDateFormat formater = getFormater(format);
			formater.setLenient(false);
			d = formater.parse(dateStr);
		} catch (Exception e) {
			// log.error(e);
			d = null;
		}
		return d;
	}

	/**
	 * 把符合日期格式的字符串转换为日期类型
	 */
	public static Date stringtoDate(String dateStr, String format,
			ParsePosition pos) {
		Date d = null;
		try {
			SimpleDateFormat formater = getFormater(format);
			formater.setLenient(false);
			d = formater.parse(dateStr, pos);
		} catch (Exception e) {
			d = null;
		}
		return d;
	}

	/**
	 * 把日期转换为字符串
	 * 
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date, String format) {
		String result = "";
		try {
			result = getFormater(format).format(date);
		} catch (Exception e) {
			// log.error(e);
		}
		return result;
	}

	/**
	 * 日期格式字符串转换
	 * 
	 * @Description:
	 * @param dateFormat
	 * @param format2
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-19
	 */
	public static String dateStrFormat2Format(String dateFormat, String format2) {
		String result = "";
		try {
			result = dateToString(stringtoDate(dateFormat, LONG_DATE_FORMAT2),
					format2);
		} catch (Exception e) {
			// log.error(e);
		}
		return result;
	}

	/**
	 * 把时间戳字符串转化为日期字符串
	 * 
	 * @Description:
	 * @param time
	 * @param format
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2015-1-7
	 */
	public static String stringTimeTostringFormat(String time, String format) {
		String result = "";
		try {
			if (!TextUtils.isEmpty(time)) {
				SimpleDateFormat sdf1 = getFormater(SHORT_LINE_FORMAT);
				SimpleDateFormat sdf2 = getFormater(format);
				Date date = sdf1.parse(time);
				result = sdf2.format(date);
			}
		} catch (ParseException e) {

		}

		return result;
	}

	/**
	 * 获取当前时间的指定格式
	 * 
	 * @param format
	 * @return
	 */
	public static String getCurrDate(String format) {
		return dateToString(new Date(), format);
	}

	/**
	 * 
	 * @param dateStr
	 * @param amount
	 * @return
	 */
	public static String dateSub(int dateKind, String dateStr, int amount) {
		Date date = stringtoDate(dateStr, FORMAT_ONE);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(dateKind, amount);
		return dateToString(calendar.getTime(), FORMAT_ONE);
	}

	/**
	 * 两个日期相减
	 * 
	 * @param firstTime
	 * @param secTime
	 * @return 相减得到的秒数
	 */
	public static long timeSub(String firstTime, String secTime) {
		long first = stringtoDate(firstTime, FORMAT_ONE).getTime();
		long second = stringtoDate(secTime, FORMAT_ONE).getTime();
		return (second - first) / 1000;
	}

	/**
	 * 获得某月的天数
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int
	 * @return int
	 */
	public static int getDaysOfMonth(String year, String month) {
		int days = 0;
		if (month.equals("1") || month.equals("3") || month.equals("5")
				|| month.equals("7") || month.equals("8") || month.equals("10")
				|| month.equals("12")) {
			days = 31;
		} else if (month.equals("4") || month.equals("6") || month.equals("9")
				|| month.equals("11")) {
			days = 30;
		} else {
			if ((Integer.parseInt(year) % 4 == 0 && Integer.parseInt(year) % 100 != 0)
					|| Integer.parseInt(year) % 400 == 0) {
				days = 29;
			} else {
				days = 28;
			}
		}

		return days;
	}

	/**
	 * 获取某年某月的天数
	 * 
	 * @param year
	 *            int
	 * @param month
	 *            int 月份[1-12]
	 * @return int
	 */
	public static int getDaysOfMonth(int year, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, 1);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得当前日期(几号)
	 * 
	 * @return int
	 */
	public static int getToday() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 获得当前月份
	 * 
	 * @return int
	 */
	public static int getToMonth() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 获得当前年份
	 * 
	 * @return int
	 */
	public static int getToYear() {
		Calendar calendar = Calendar.getInstance();
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回日期的天
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DATE);
	}

	/**
	 * 返回日期的年
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * 返回日期的月份，1-12
	 * 
	 * @param date
	 *            Date
	 * @return int
	 */
	public static int getMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.MONTH) + 1;
	}

	/**
	 * 计算两个日期相差的天数，如果date2 > date1 返回正数，否则返回负数
	 * 
	 * @param date1
	 *            Date
	 * @param date2
	 *            Date
	 * @return long
	 */
	public static long dayDiff(Date date1, Date date2) {
		return (date2.getTime() - date1.getTime()) / 86400000;
	}

	/**
	 * 比较两个日期的年差
	 * 
	 * @param befor
	 * @param after
	 * @return
	 */
	public static int yearDiff(String before, String after) {
		Date beforeDay = stringtoDate(before, LONG_DATE_FORMAT);
		Date afterDay = stringtoDate(after, LONG_DATE_FORMAT);
		return getYear(afterDay) - getYear(beforeDay);
	}

	/**
	 * 比较指定日期与当前日期的差
	 * 
	 * @param befor
	 * @param after
	 * @return
	 */
	public static int yearDiffCurr(String after) {
		Date beforeDay = new Date();
		Date afterDay = stringtoDate(after, LONG_DATE_FORMAT);
		return getYear(beforeDay) - getYear(afterDay);
	}

	/**
	 * 比较指定日期与当前日期的差
	 * 
	 * @param before
	 * @return
	 * @author chenyz
	 */
	public static long dayDiffCurr(String before) {
		Date currDate = DateUtil.stringtoDate(currDay(), LONG_DATE_FORMAT);
		Date beforeDate = stringtoDate(before, LONG_DATE_FORMAT);
		return (currDate.getTime() - beforeDate.getTime()) / 86400000;

	}

	/**
	 * 获取每月的第一周
	 * 
	 * @param year
	 * @param month
	 * @return
	 * @author chenyz
	 */
	public static int getFirstWeekdayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
		c.set(year, month - 1, 1);
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取每月的最后一周
	 * 
	 * @param year
	 * @param month
	 * @return
	 * @author chenyz
	 */
	public static int getLastWeekdayOfMonth(int year, int month) {
		Calendar c = Calendar.getInstance();
		c.setFirstDayOfWeek(Calendar.SATURDAY); // 星期天为第一天
		c.set(year, month - 1, getDaysOfMonth(year, month));
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * date是周几
	 * 
	 * @Description:
	 * @param date
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-19
	 */
	public static String getWeekDay(String date) {
		Calendar calendar = Calendar.getInstance();// 获得一个日历
		calendar.setTime(stringtoDate(date, LONG_DATE_FORMAT2));
		int number = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 星期表示1-7，是从星期日开始，
		return dayNames[number];
	}

	public static String getWeekDay(long date) {
		Calendar calendar = Calendar.getInstance();// 获得一个日历
		calendar.setTime(new Date(date));
		int number = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 星期表示1-7，是从星期日开始，
		return dayNames[number];
	}

	public static int getWeekDayNum(String date) {
		Calendar calendar = Calendar.getInstance();// 获得一个日历
		calendar.setTime(stringtoDate(date, LONG_DATE_FORMAT));
		int number = calendar.get(Calendar.DAY_OF_WEEK) - 1;// 星期表示1-7，是从星期日开始，
		return number;
	}

	/**
	 * 获得当前日期字符串，格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static String getNow() {
		Calendar today = Calendar.getInstance();
		return dateToString(today.getTime(), FORMAT_ONE);
	}

	/**
	 * 根据生日获取星座
	 * 
	 * @param birth
	 *            YYYY-mm-dd
	 * @return
	 */
	public static String getAstro(String birth) {
		if (!isDate(birth)) {
			birth = "2000" + birth;
		}
		if (!isDate(birth)) {
			return "";
		}
		int month = Integer.parseInt(birth.substring(birth.indexOf("-") + 1,
				birth.lastIndexOf("-")));
		int day = Integer.parseInt(birth.substring(birth.lastIndexOf("-") + 1));
		String s = "魔羯水瓶双鱼牡羊金牛双子巨蟹狮子处女天秤天蝎射手魔羯";
		int[] arr = { 20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22 };
		int start = month * 2 - (day < arr[month - 1] ? 2 : 0);
		return s.substring(start, start + 2) + "座";
	}

	/**
	 * 判断日期是否有效,包括闰年的情况
	 * 
	 * @param date
	 *            YYYY-mm-dd
	 * @return
	 */
	public static boolean isDate(String date) {
		StringBuffer reg = new StringBuffer(
				"^((\\d{2}(([02468][048])|([13579][26]))-?((((0?");
		reg.append("[13578])|(1[02]))-?((0?[1-9])|([1-2][0-9])|(3[01])))");
		reg.append("|(((0?[469])|(11))-?((0?[1-9])|([1-2][0-9])|(30)))|");
		reg.append("(0?2-?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12");
		reg.append("35679])|([13579][01345789]))-?((((0?[13578])|(1[02]))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))");
		reg.append("-?((0?[1-9])|([1-2][0-9])|(30)))|(0?2-?((0?[");
		reg.append("1-9])|(1[0-9])|(2[0-8]))))))");
		Pattern p = Pattern.compile(reg.toString());
		return p.matcher(date).matches();
	}

	/**
	 * 取得指定日期过 months 月后的日期 (当 months 为负数表示指定月之前);
	 * 
	 * @param date
	 *            日期 为null时表示当天
	 * @param month
	 *            相加(相减)的月数
	 */
	public static Date nextMonth(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	/**
	 * 取得指定日期过 day 天后的日期 (当 day 为负数表示指日期之前);
	 * 
	 * @param date
	 *            日期 为null时表示当天
	 * @param month
	 *            相加(相减)的月数
	 */
	public static Date nextDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.DAY_OF_YEAR, day);
		return cal.getTime();
	}

	/**
	 * 取得距离今天 day 日的日期
	 * 
	 * @param day
	 * @param format
	 * @return
	 * @author chenyz
	 */
	public static String nextDay(int day, String format) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, day);
		return dateToString(cal.getTime(), format);
	}

	/**
	 * 取得指定日期过 day 周后的日期 (当 day 为负数表示指定月之前)
	 * 
	 * @param date
	 *            日期 为null时表示当天
	 */
	public static Date nextWeek(Date date, int week) {
		Calendar cal = Calendar.getInstance();
		if (date != null) {
			cal.setTime(date);
		}
		cal.add(Calendar.WEEK_OF_MONTH, week);
		return cal.getTime();
	}

	/**
	 * 获取当前的日期(yyyy-MM-dd)
	 */
	public static String currDay() {
		return DateUtil.dateToString(new Date(), DateUtil.LONG_DATE_FORMAT);
	}

	/**
	 * 获取昨天的日期
	 * 
	 * @return
	 */
	public static String befoDay() {
		return befoDay(DateUtil.LONG_DATE_FORMAT);
	}

	/**
	 * 根据时间类型获取昨天的日期
	 * 
	 * @param format
	 * @return
	 * @author chenyz
	 */
	public static String befoDay(String format) {
		return DateUtil.dateToString(DateUtil.nextDay(new Date(), -1), format);
	}

	/**
	 * 获取明天的日期
	 */
	public static String afterDay() {
		return DateUtil.dateToString(DateUtil.nextDay(new Date(), 1),
				DateUtil.LONG_DATE_FORMAT);
	}

	/**
	 * 取得当前时间距离1900/1/1的天数
	 * 
	 * @return
	 */
	public static int getDayNum() {
		int daynum = 0;
		GregorianCalendar gd = new GregorianCalendar();
		Date dt = gd.getTime();
		GregorianCalendar gd1 = new GregorianCalendar(1900, 1, 1);
		Date dt1 = gd1.getTime();
		daynum = (int) ((dt.getTime() - dt1.getTime()) / (24 * 60 * 60 * 1000));
		return daynum;
	}

	/**
	 * getDayNum的逆方法(用于处理Excel取出的日期格式数据等)
	 * 
	 * @param day
	 * @return
	 */
	public static Date getDateByNum(int day) {
		GregorianCalendar gd = new GregorianCalendar(1900, 1, 1);
		Date date = gd.getTime();
		date = nextDay(date, day);
		return date;
	}

	/** 针对yyyy-MM-dd HH:mm:ss格式,显示yyyymmdd */
	public static String getYmdDateCN(String datestr) {
		if (datestr == null)
			return "";
		if (datestr.length() < 10)
			return "";
		StringBuffer buf = new StringBuffer();
		buf.append(datestr.substring(0, 4)).append(datestr.substring(5, 7))
				.append(datestr.substring(8, 10));
		return buf.toString();
	}

	/**
	 * 获取本月第一天
	 * 
	 * @param format
	 * @return
	 */
	public static String getFirstDayOfMonth(String format) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		return dateToString(cal.getTime(), format);
	}

	/**
	 * 获取本月最后一天
	 * 
	 * @param format
	 * @return
	 */
	public static String getLastDayOfMonth(String format) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		return dateToString(cal.getTime(), format);
	}

	/**
	 * 获取yyyy年mm月dd日
	 * 
	 * @param date
	 * @return
	 */
	public static String getChineDate(Date date) {
		String strDate = DateUtil.dateToString(date, DateUtil.LONG_DATE_FORMAT);
		return strDate.split("-")[0] + "年" + strDate.split("-")[1] + "月"
				+ strDate.split("-")[2] + "日";
	}

	/**
	 * 获得yyyy年MM月dd日 hh:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getChineLongDate(Date date) {
		String strDate = DateUtil.dateToString(date, DateUtil.LONG_DATE_FORMAT);
		return strDate.split("-")[0] + "年" + strDate.split("-")[1] + "月"
				+ strDate.split("-")[2] + "日" + " "
				+ dateToString(date, LONG_TIME_FORMAT);
	}

	/**
	 * 获取mm月dd日 hh:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String getChineShortDate(Date date) {
		String strDate = DateUtil.dateToString(date, DateUtil.LONG_DATE_FORMAT);
		return strDate.split("-")[1] + "月" + strDate.split("-")[2] + "日" + " "
				+ dateToString(date, LONG_TIME_FORMAT);
	}

	/**
	 * 判断日期是否是今天
	 * 
	 * @Description:
	 * @param date
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isDateToday(Date date) {
		Date today = new Date();
		if (date.getYear() == today.getYear()
				&& date.getMonth() == today.getMonth()
				&& date.getDate() == today.getDate()) {
			return true;
		}
		return false;
	}

	/**
	 * 毫秒转化成天时分秒
	 * 
	 * @param ms
	 * @param common
	 *            是否通用，不通用，传色值
	 * @param color
	 *            色值 毫秒
	 * @return
	 */
	public static String formatTime(long ms, boolean common, int color) {

		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second
				* ss;
		String strDay = day < 10 ? "0" + day : "" + day; // 天
		String strHour = hour < 10 ? "0" + hour : "" + hour;// 小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;// 分钟
		String strSecond = second < 10 ? "0" + second : "" + second;// 秒
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
				+ milliSecond;// 毫秒
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
				+ strMilliSecond;
		if (strDay.equals("00") && strHour.equals("00")
				&& strMinute.equals("00")) {
			if (common) {
				return strSecond
						+ App.getInstance().getResources()
								.getString(R.string.lottery_second);
			} else {
				return "<font color='"
						+ color
						+ "'>"
						+ strSecond
						+ "</font>"
						+ App.getInstance().getResources()
								.getString(R.string.lottery_second);
			}
		}
		if (strDay.equals("00") && strHour.equals("00")) {
			if (common) {
				return strMinute
						+ App.getInstance().getResources()
								.getString(R.string.lottery_minute)
						+ strSecond
						+ App.getInstance().getResources()
								.getString(R.string.lottery_second);
			} else {
				return "<font color='"
						+ color
						+ "'>"
						+ strMinute
						+ "</font>"
						+ App.getInstance().getResources()
								.getString(R.string.lottery_minute)
						+ "<font color='"
						+ color
						+ "'>"
						+ strSecond
						+ "</font>"
						+ App.getInstance().getResources()
								.getString(R.string.lottery_second);
			}
		}
		if (strDay.equals("00")) {
			if (common) {
				return strHour
						+ App.getInstance().getResources()
								.getString(R.string.lottery_hour)
						+ strMinute
						+ App.getInstance().getResources()
								.getString(R.string.lottery_minute)
						+ strSecond
						+ App.getInstance().getResources()
								.getString(R.string.lottery_second);
			} else {
				return "<font color='"
						+ color
						+ "'>"
						+ strHour
						+ "</font>"
						+ App.getInstance().getResources()
								.getString(R.string.lottery_hour)
						+ "<font color='"
						+ color
						+ "'>"
						+ strMinute
						+ "</font>"
						+ App.getInstance().getResources()
								.getString(R.string.lottery_minute)
						+ "<font color='"
						+ color
						+ "'>"
						+ strSecond
						+ "</font>"
						+ App.getInstance().getResources()
								.getString(R.string.lottery_second);
			}

		}
		if (common) {
			return strDay
					+ App.getInstance().getResources()
							.getString(R.string.lottery_day)
					+ strHour
					+ App.getInstance().getResources()
							.getString(R.string.lottery_hour)
					+ strMinute
					+ App.getInstance().getResources()
							.getString(R.string.lottery_minute) + strSecond
					+ "秒";
		} else {
			return "<font color='"
					+ color
					+ "'>"
					+ strDay
					+ "</font>"
					+ App.getInstance().getResources()
							.getString(R.string.lottery_day)
					+ "<font color='"
					+ color
					+ "'>"
					+ strHour
					+ "</font>"
					+ App.getInstance().getResources()
							.getString(R.string.lottery_hour)
					+ "<font color='"
					+ color
					+ "'>"
					+ strMinute
					+ "</font>"
					+ App.getInstance().getResources()
							.getString(R.string.lottery_minute)
					+ "<font color='"
					+ color
					+ "'>"
					+ strSecond
					+ "</font>"
					+ App.getInstance().getResources()
							.getString(R.string.lottery_second);
		}
	}

	/**
	 * 将毫秒值转化成day : hour : minute : second
	 * 
	 * @Description:
	 * @param ms
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年10月29日
	 */
	public static String formatTime(long ms) {

		int ss = 1000;
		int mi = ss * 60;
		int hh = mi * 60;
		int dd = hh * 24;

		long day = ms / dd;
		long hour = (ms - day * dd) / hh;
		long minute = (ms - day * dd - hour * hh) / mi;
		long second = (ms - day * dd - hour * hh - minute * mi) / ss;
		long milliSecond = ms - day * dd - hour * hh - minute * mi - second
				* ss;

		String strDay = day < 10 ? "0" + day : "" + day; // 天
		String strHour = hour < 10 ? "0" + hour : "" + hour;// 小时
		String strMinute = minute < 10 ? "0" + minute : "" + minute;// 分钟
		String strSecond = second < 10 ? "0" + second : "" + second;// 秒
		String strMilliSecond = milliSecond < 10 ? "0" + milliSecond : ""
				+ milliSecond;// 毫秒
		strMilliSecond = milliSecond < 100 ? "0" + strMilliSecond : ""
				+ strMilliSecond;
		// if (strDay.equals("00") && strHour.equals("00") &&
		// strMinute.equals("00")) {
		// return strSecond;
		// }
		// if (strDay.equals("00") && strHour.equals("00")) {
		// return strMinute + " : " + strSecond;
		// }
		if (strDay.equals("00")) {
			return strHour + " : " + strMinute + " : " + strSecond;
		}
		return strDay + " : " + strHour + " : " + strMinute + " : " + strSecond;
	}

	/**
	 * 将字符串格式的时间转换成以毫秒为单位的时间
	 * 
	 * @param time
	 *            时间，格式为yyyyMMddHHmmss
	 */
	public static long getTimeInMillisFromStr(String time) {
		long result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());

		try {
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(sdf.parse(time));
			result = calendar.getTimeInMillis();

			return result;
		} catch (Exception e) {
		}

		return 0;
	}

	/**
	 * 将日期时间转换成long
	 * 
	 * @param format
	 * @param date
	 * @return
	 */
	public static long getTimeFormatLong(String format, String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());

		Date dt2 = null;
		try {
			dt2 = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt2.getTime();
	}

	/**
	 * 讲毫秒值转换成时间格式
	 * 
	 * @Description:
	 * @param time
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月2日
	 */
	public static String getTimeInMillisToStr(String time) {
		if (!StringUtil.isEmpty(time)) {
			Date date = new Date(Long.parseLong(time));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return sdf.format(date);
		} else {
			return "";
		}
	}

	public static String getYearMonthAndDay(String time) {
		String result = time;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd",
				Locale.getDefault());

		try {
			Date date = sdf1.parse(time);
			result = sdf2.format(date);
		} catch (ParseException e1) {
		} catch (Exception e2) {
		}
		return result;
	}

	public static String getMonthAndDay(String time) {
		String result = time;
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		SimpleDateFormat sdf2 = new SimpleDateFormat("MM月dd日",
				Locale.getDefault());
		try {
			Date date = sdf1.parse(time);
			result = sdf2.format(date);
		} catch (ParseException e1) {
		} catch (Exception e2) {
		}
		return result;
	}

	/**
	 * 是否小于当前时间
	 * 
	 * @Description:
	 * @param time
	 * @return
	 * @author:zhangjf
	 * @date:2015-2-26
	 */
	public static boolean isTimeGreater(String time) {
		long nowTime = System.currentTimeMillis();
		long endTime = Long.parseLong(time);
		if (endTime > nowTime) {
			return true;
		}
		return false;
	}

	/**
	 * 时间转字符串，格式：00:00:00
	 */
	public static String formatSecond(long second) {
		long d = second / 60 / 60 / 24;
		long h = second / 60 / 60 % 24;
		long m = second / 60 % 60;
		long s = second % 60;
		return (d > 0 ? d + "天  " : "") + (h < 10 ? "0" + h : h) + ":"
				+ (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
	}
	
	//把字符串转为日期  
    public static Date ConverToDate(String strDate) 
    {  
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
        Date date = null;
        try {
			date =  df.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
			date = null;
		}  
        return date;
    } 
}