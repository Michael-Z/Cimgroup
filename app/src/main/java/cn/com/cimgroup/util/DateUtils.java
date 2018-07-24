package cn.com.cimgroup.util;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

	/**
	 * 获取指定格式的当前时间yyyy-MM-dd HH:mm:ss
	 */
	public static String currentDateByFormat(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
		return sdf.format(new Date());
	}

	/**
	 * 获取当前时间搓
	 */
	public static String currentTimestamp() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.CHINA);
		return format.format(new Date());
	}

	/**
	 * 判断该日期段是否合法
	 */
	public static boolean dateValidate(String startDate, String endDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		format.setLenient(false);
		try {
			Calendar start = Calendar.getInstance();
			start.setTime(format.parse(startDate));
			Calendar end = Calendar.getInstance();
			end.setTime(format.parse(endDate));
			if (end.getTime().getTime() >= start.getTime().getTime()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断该日期和时间是否合法
	 */
	public static boolean datetimeValidate(String datetime) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		format.setLenient(false);
		try {
			format.parse(datetime);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 判断该日期是今天、昨天、以前
	 */
	public static int getDayCount(String date) {
		int day = -1;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
					Locale.CHINA);
			String d1 = format.format(new Date());
			if (d1.equals(date.substring(0, 10))) {
				day = 0;
			} else {
				Calendar now = Calendar.getInstance();
				now.setTime(format.parse(date));
				now.set(Calendar.DAY_OF_MONTH,
						now.get(Calendar.DAY_OF_MONTH) + 1);
				int year = now.get(Calendar.YEAR);
				int month = now.get(Calendar.MONTH) + 1;
				int d = now.get(Calendar.DAY_OF_MONTH);
				if (d1.equals(year + "-" + (month >= 10 ? month : "0" + month)
						+ "-" + (d >= 10 ? d : "0" + d))) {
					day = 1;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return day;

	}

	/**
	 * 获取多少分钟后的时间
	 */
	public static String getMinuteAfterDate(String currentDate, int m) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
			Calendar now = Calendar.getInstance();
			now.setTime(format.parse(currentDate));
			now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + m);
			return format.format(now.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return currentDate;
	}

	/**
	 * 获取多久以前
	 */
	public static String getCreateTime(String time) {
		try {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss", Locale.CHINA);
			long second = (new Date().getTime() - format.parse(time).getTime()) / 1000;// 秒
			long d = second / (24 * 60 * 60);// 天
			long h = second / (60 * 60);// 小时
			long m = second / 60;// 分钟
			if (d > 0) {
				return d + "天前";
			} else if (h > 0) {
				return h + "小时前";
			} else if (m > 0) {
				return m + "分钟前";
			} else {
				return second + "秒前";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return time;
	}

	/**
	 * 时间转字符串，格式：00:00:00
	 */
	public static String formatSecond(long second) {
		long h = second / 60 / 60;
		long m = second / 60 % 60;
		long s = second % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}

	/**
	 * 时间转字符串，格式：00:00:00
	 */
	public static String formatToDate(long second) {
		long d = second / 60 / 60 / 24;
		long h = second / 60 / 60 % 24;
		long m = second / 60 % 60;
		long s = second % 60;
		String data = "";
		data = (d < 10 ? "0" + d : d) + ":" + (h < 10 ? "0" + h : h) + ":"
				+ (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);

		return data;
	}

	/**
	 * long转字符串
	 */
	public static String formatToString(long milliseconds) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd",
				Locale.CHINA);
		return format.format(new Date(milliseconds));
	}

	/**
	 * long转字符串
	 */
	public static String MMddhhmm(long milliseconds) {
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm",
				Locale.CHINA);
		return format.format(new Date(milliseconds));
	}

	/**
	 * long转字符串
	 */
	public static String formatTimeToString(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.CHINA);
		return format.format(new Date(time));
	}

	/**
	 * 将时间毫秒数转换成Date
	 * @param expireDate
	 * @return
	 */
	public static Date getSecondsFromDate(String expireDate) {
		if (expireDate == null || expireDate.trim().equals(""))
			return new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = sdf.parse(expireDate);
//			return (int) (date.getTime() / 1000);
			return date ;
		} catch (ParseException e) {
			e.printStackTrace();
			return new Date();
		}
	}
	/**
	 * 根据日期（Date）获取当前星期几
	 * @param date
	 * @return
	 */
	public static String getWeek_(Date date){ 
		SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
		String week = sdf.format(date);
		return week;
		}

}
