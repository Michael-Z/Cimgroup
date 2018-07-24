package cn.com.cimgroup.xutils;
import cn.com.cimgroup.App;
import android.util.Log;

/**
 * 日志输出
 * 
 * @Description:
 * @author:zhangjf Logcat日志级别: verbose, debug, info,warn,error
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2014-5-14
 */
public class XLog {

	static String xLogName = XLog.class.getName();

	static final String XLOG_STRING = "XLOG=";

	public static void i(String message) {
		i(null, message);
	}

	public static void i(String tag, String message) {
		if (App.DEBUG) {
			tag = getClassName2MethodsName(tag);
			Log.i(tag, message);
		}
	}

	public static void d(String message) {
		d(null, message);
	}

	public static void d(String tag, String message) {
		if (App.DEBUG) {
			tag = getClassName2MethodsName(tag);
			Log.d(tag, message);
		}
	}

	public static void v(String message) {
		v(null, message);
	}

	public static void v(String tag, String message) {
		if (App.DEBUG) {
			tag = getClassName2MethodsName(tag);
			Log.v(tag, message);
		}
	}

	public static void w(String message) {
		i(null, message);
	}

	public static void w(String tag, String message) {
		if (App.DEBUG) {
			tag = getClassName2MethodsName(tag);
			Log.w(tag, message);
		}
	}

	public static void e(String message) {
		e(null, message);
	}

	public static void e(String tag, String message) {
		if (App.DEBUG) {
			tag = getClassName2MethodsName(tag);
			Log.e(tag, message);
		}
	}

	/**
	 * Xlog tag由类名、方法名、调用的行数以及自己加入的tag组成(tag可选)
	 * 
	 * @Description:
	 * @param c
	 * @param tag
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2015-1-6
	 */
	private static String getClassName2MethodsName(String tag) {
		String tempTag = XLOG_STRING;
		StackTraceElement[] stack = (new Throwable()).getStackTrace();
		for (StackTraceElement element : stack) {
			if (!xLogName.equals(element.getClassName())) {
				tempTag = XLOG_STRING + element.getFileName().replace(".java", "") + "#" + element.getMethodName() + "#" + element.getLineNumber();
				break;
			}
		}
		if (tag != null && !tag.equals("")) {
			tempTag = tempTag + "#" + tag;
		}
		return tempTag;
	}

}
