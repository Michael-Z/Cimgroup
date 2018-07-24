package cn.com.cimgroup.xutils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.cimgroup.R;

/**
 * @author Tienfook
 * @version 2011-8-19 下午04:14:48 Toast提示工具类
 */
public class ToastUtil {
	private static long mStartTime = 0;
	private static long mEndTime = 0;

	private static boolean isShow() {
		mEndTime = System.currentTimeMillis();
		if (mEndTime - mStartTime > 2000) {
			mStartTime = mEndTime;
			return true;
		} else
			return false;
	}

	/**
	 * 长时间
	 * 
	 * @param context
	 * @param message
	 */
	public static void longToast(Context context, String message) {
		if (isShow()) {
			Toast.makeText(context, message, Toast.LENGTH_LONG).show();
		}

	}

	/**
	 * 长时间 控制位置
	 * 
	 * @param context
	 * @param message
	 */
	public static void longToast(Context context, String message, int xOffset,
			int yOffset) {
		if (isShow()) {
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, xOffset, yOffset);
			toast.show();
		}

	}

	/**
	 * 长时间 中间位置显示
	 * 
	 * @param context
	 * @param message
	 */
	public static void longToastCenter(Context context, String message) {
		if (isShow()) {
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER,
					new Toast(context).getXOffset() / 2,
					new Toast(context).getYOffset() / 2);
			toast.show();
		}

	}

	/**
	 * 短时间
	 * 
	 * @param context
	 * @param message
	 */
	public static void shortToast(Context context, String message) {
		if (isShow()) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}

	}

	public static void shortToast(Context context, int message) {
		if (isShow()) {
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 短时间 控制位置
	 * 
	 * @param context
	 * @param message
	 */
	public static void shortToast(Context context, String message, int xOffset,
			int yOffset) {
		if (isShow()) {
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, xOffset, yOffset);
			toast.show();
		}
	}

	/**
	 * 短时间 中间位置显示
	 * 
	 * @param context
	 * @param message
	 */
	public static void shortToastCenter(Context context, String message) {
		if (isShow()) {
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER,
					new Toast(context).getXOffset() / 2,
					new Toast(context).getYOffset() / 2);
			toast.show();
		}
	}

	/**
	 * 带有图标的吐丝
	 * 
	 * @Description 带有图标的吐丝
	 * @param context
	 *            上下文
	 * @param message
	 *            提示文字
	 * @see
	 * @since
	 * @author ChenLong
	 * @date 2013-10-28
	 */
	public static void toastWithIcon(Context context, String message) {
		if (isShow()) {
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.toast_layout_with_icon, null);
			TextView textView = (TextView) view.findViewById(R.id.text);
			textView.setText(message);
			Toast toast = new Toast(context);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(view);
			toast.setGravity(Gravity.TOP, 0, 110);
			toast.show();
		}
	}
}
