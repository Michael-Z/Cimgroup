package cn.com.cimgroup.popwindow;

import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.xutils.XLog;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 * 各Popwindow基类
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public abstract class PopWnd {

	protected PopupWindow popupWindow;
	protected Handler mHandler;
	protected Context context;
	protected int mTag;
	protected String mTitle;
	protected View view;
	/** 是否从头部出现 */
	private boolean mIsUpShow = true;

	// PopupWindow 布局
	public abstract int getLayoutResource();

	// PopupWindow Handler 发送Message What
	public abstract int getMessageWhat();

	// PopupWindow 是否是包裹内容
	protected boolean isWrapContent() {
		return false;
	}

	@SuppressWarnings("deprecation")
	public PopWnd(Context ctx, int tag, Handler mHandlerEx,
			final LinearLayout popBg, final TextView switchText) {
		mHandler = mHandlerEx;
		context = ctx;
		mTag = tag;
		view = LayoutInflater.from(ctx).inflate(getLayoutResource(), null);
		if (isWrapContent()) {
			popupWindow = new PopupWindow(view,
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		} else {
			popupWindow = new PopupWindow(view,
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.update();
		popupWindow.setTouchInterceptor(new View.OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (popBg != null) {
					popBg.setVisibility(View.GONE);
				}
				if (switchText != null) {
					LotteryShowUtil.setTextViewRightDrawable(
							switchText,
							context.getResources().getDrawable(
									R.drawable.icon_tz_pull));
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	public PopWnd(Context ctx, int tag, Handler mHandlerEx,
			final LinearLayout popBg, final TextView switchText,
			boolean isUpShow) {
		mIsUpShow = isUpShow;
		mHandler = mHandlerEx;
		context = ctx;
		mTag = tag;
		view = LayoutInflater.from(ctx).inflate(getLayoutResource(), null);
		if (isWrapContent()) {
			popupWindow = new PopupWindow(view,
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		} else {
			popupWindow = new PopupWindow(view,
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
		}
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		if (mIsUpShow) {
			popupWindow.setAnimationStyle(R.style.popwin_anim_style);
		} else {
			popupWindow.setAnimationStyle(R.style.popwin_down_anim_style);
		}
		if (switchText != null) {
			switchText.setVisibility(View.GONE);
		}
		popupWindow.setOutsideTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.update();
		popupWindow.setTouchInterceptor(new View.OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					popupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (popBg != null) {
					popBg.setVisibility(View.GONE);
				}
				if (switchText != null) {
					LotteryShowUtil.setTextViewRightDrawable(
							switchText,
							context.getResources().getDrawable(
									R.drawable.icon_tz_pull));
				}
			}
		});
	}

	public void dissmiss() {
		popupWindow.dismiss();
	}

	public void showPopWindow(View v) {
		popupWindow.showAsDropDown(v);
	}

	public class PwItemOnClickListener implements OnClickListener {

		@Override
		public void onClick(View arg0) {

			Integer tag = (Integer) arg0.getTag();
			showSelBg(tag);
			Message msg = new Message();
			msg.arg1 = tag;
			msg.obj = showTitle(tag);
			msg.what = getMessageWhat();
			mHandler.sendMessage(msg);
		}
	}

	protected Object showTitle(Integer tag) {
		return null;
	}

	protected void showSelBg(Integer tag) {

	}
}
