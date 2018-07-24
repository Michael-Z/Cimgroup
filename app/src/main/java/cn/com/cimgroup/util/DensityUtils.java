package cn.com.cimgroup.util;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import cn.com.cimgroup.App;

public class DensityUtils {

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(float pxValue) {
		final float scale = App.getInstance().getResources()
				.getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 * @param scale
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(float dipValue) {
		final float scale = App.getInstance().getResources()
				.getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(float pxValue) {
		final float fontScale = App.getInstance().getResources()
				.getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(float spValue) {
		final float fontScale = App.getInstance().getResources()
				.getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/** 获取屏幕的宽度 */
	@SuppressWarnings("deprecation")
	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	/** 获取屏幕的高度 */
	@SuppressWarnings("deprecation")
	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context.getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

}
