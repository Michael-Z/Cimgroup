package cn.com.cimgroup;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.IBinder;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 常用工具类
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年10月21日
 */
public class GlobalTools {

	/**
	 * 使用32位MD5加密算法进行加密
	 * 
	 * @Description:
	 * @param text
	 *            要加密的字符串
	 * @return 加密后字符串
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2012-2-24
	 */
	public static String md5Encrypt(String text) {
		// 空串就不用加密了
		if (text == null) {
			return text;
		}
		try {
			MessageDigest md5 = MessageDigest.getInstance("md5");
			char[] charArr = text.toCharArray();
			byte[] byteArr = new byte[charArr.length];
			for (int i = 0; i < charArr.length; i++) {
				byteArr[i] = (byte) charArr[i];
			}
			return StringUtil.bytes2HexString(md5.digest(byteArr));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * dip转换成Px
	 * 
	 * @Description:
	 * @param context
	 * @param dipValue
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-30
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @Description:
	 * @param context
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-30
	 */
	public static boolean hideSoftInput(Activity context) {
		try {
			InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (inputMethodManager != null && context.getCurrentFocus() != null && context.getCurrentFocus().getWindowToken() != null) {
				return inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception e) {
		}
		return false;
	}

	/**
	 * 切换输入法显示隐藏状态
	 * 
	 * @Description:
	 * @param context
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-30
	 */
	public static void toggleSoftInput(Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	/**
	 * 显示软键盘
	 * 
	 * @Description:
	 * @param context
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-30
	 */
	public static void openSoftInput(View view,Context context) {
		InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}

	/**
	 * 隐藏输入法
	 * 
	 * @Description:
	 * @param context
	 * @param binder
	 *            输入法所在控件的token
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-30
	 */
	public static void hideSoftInput(Context context, IBinder binder) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(binder, 0);
	}

	/**
	 * px转换成dp
	 * 
	 * @Description:
	 * @param context
	 * @param pxValue
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-30
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 存储屏幕高宽的数组
	 */
	private static int[] screenSize = null;

	/**
	 * 获取屏幕高宽
	 * 
	 * @Description:
	 * @param activity
	 * @return 屏幕宽高的数组 [0]宽， [1]高
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-30
	 */
	@SuppressWarnings("deprecation")
	public static int[] getScreenSize(Activity activity) {
		if (screenSize == null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			screenSize = new int[2];
			screenSize[0] = display.getWidth();
			screenSize[1] = display.getHeight();
		}
		return screenSize;
	}

	/**
	 * 清除List内容，并置为null
	 * 
	 * @Description:
	 * @param list
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-8-2
	 */
	public static void clearList(Collection<?> list) {
		if (list != null) {
			list.clear();
			list = null;
		}
	}

	/**
	 * 关闭cursor
	 * 
	 * @Description:
	 * @param cursor
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-8-2
	 */
	public static void closeCursor(Cursor cursor) {
		if (cursor != null) {
			cursor.close();
		}
	}

	/**
	 * 取两个集合的并集
	 * 
	 * @Description:
	 * @param c1
	 * @param c2
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-8-9
	 */
	public static Collection<String> mixedList(Collection<String> c1, Collection<String> c2) {
		// 定义两个空的集合，分别存放最大和最小的集合，用来取交集
		Collection<String> tmpBig = new ArrayList<String>();
		Collection<String> tmpSmall = new ArrayList<String>();
		// 为最大和最小集合赋值
		if (c1.size() > c2.size()) {
			tmpBig.addAll(c1);
			tmpSmall.addAll(c2);
		} else {
			tmpBig.addAll(c2);
			tmpSmall.addAll(c1);
		}
		tmpBig.retainAll(tmpSmall);
		tmpSmall = null;
		return tmpBig;
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static float sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return spValue * fontScale + 0.5f;
	}

	/**
	 * 讲时间戳转换年月日;
	 * @param seconds
	 * @param format
	 * @return
	 */
	public static String timeStamp2Date(String seconds,String format){
		if(seconds == null || seconds.isEmpty() || seconds.equals("null")){
			return "";
		}
		if(format == null || format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date(Long.valueOf(seconds)));
	}

	/**
	 *
	 * @param s
	 * @return
	 */
	public static SpannableString string2Span(String s){
		s += "元";
		SpannableString sp = new SpannableString(s);
		RelativeSizeSpan span = new RelativeSizeSpan(1.5f);
//		SizeS span = new AbsoluteSizeSpan(200);
		sp.setSpan(span,0,s.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return sp;
	}


}
