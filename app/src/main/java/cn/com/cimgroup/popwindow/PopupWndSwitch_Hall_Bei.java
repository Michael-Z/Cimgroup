package cn.com.cimgroup.popwindow;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;

/**
 * 主页选择倍数的弹出框
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class PopupWndSwitch_Hall_Bei extends PopWnd {

	public PopupWndSwitch_Hall_Bei(Context ctx, int tag, Handler mHandlerEx,
			LinearLayout popBg, TextView switchText) {
		super(ctx, tag, mHandlerEx, popBg, switchText);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return R.layout.layout_pop_switch_hall_bei;
	}

	@Override
	public int getMessageWhat() {
		// TODO Auto-generated method stub
		return GlobalConstants.MSG_NOTIFY_SWITCH_HALL_BEI;
	}
	
	PlayMenuItemClick mPlayMenuItemClick;
	TextView mPopTen;
	TextView mPopTwenty;
	TextView mPopFifty;
	ImageView mPop;
	ImageView mPopPull;

	public interface PlayMenuItemClick {

		public void PopTen(View view);

		public void PopTwenty(View view);

		public void PopFifty(View view);
	}

	@Override
	protected boolean isWrapContent() {
		return true;
	}

	public PopupWndSwitch_Hall_Bei(Context ctx, PlayMenuItemClick itemClick) {
		super(ctx, 0, null, null, null);
		if (itemClick != null) {
			mPlayMenuItemClick = itemClick;
		}
		initView(view);
	}

	private void initView(View view) {
		mPopTen = (TextView) view.findViewById(R.id.pop_ten);
		mPopTwenty = (TextView) view.findViewById(R.id.pop_twenty);
		mPopFifty = (TextView) view.findViewById(R.id.pop_fifty);
		mPop = (ImageView) view.findViewById(R.id.pop_pop_pull);
		mPopPull = (ImageView) view.findViewById(R.id.pop_pop);

		mPopTen.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayMenuItemClick.PopTen(mPopTen);
				dissmiss();
			}
		});
		mPopTwenty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayMenuItemClick.PopTwenty(mPopTwenty);
				dissmiss();
			}
		});
		mPopFifty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dissmiss();
				mPlayMenuItemClick.PopFifty(mPopFifty);
			}
		});
	}

	public void showAsDropDown(View anchor, int i, int offset) {
		// TODO Auto-generated method stub
		popupWindow.showAsDropDown(anchor, i,offset);
	}

	public View getContentView() {
		// TODO Auto-generated method stub
		return popupWindow.getContentView();
	}
	
	public void showPop(){
		mPop.setVisibility(View.VISIBLE);
		mPopPull.setVisibility(View.GONE);
	}
	
	public void showPopPull(){
		mPop.setVisibility(View.GONE);
		mPopPull.setVisibility(View.VISIBLE);
	}

	/**
	 * 自动适配popupwindow在view的上下位置
	 * @Description:
	 * @param act
	 * @param anchor
	 * @author:www.wenchuang.com
	 * @date:2015年10月29日
	 */
	public void showListItemViewPopupWindow(Activity act, View anchor) {
		DisplayMetrics dm = new DisplayMetrics(); 
		act.getWindowManager().getDefaultDisplay().getMetrics(dm); 
				
		View popView = getContentView();
		popView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

		int popHeight = popView.getMeasuredHeight();
		int viewHeight = anchor.getHeight();
				
		int[] viewLocation = new int[2];
		anchor.getLocationOnScreen(viewLocation);
				
		int offset;
		if (viewLocation[1] + viewHeight + popHeight > dm.heightPixels) {
			if (viewLocation[1] >= (viewHeight + popHeight)) {
				offset = (viewHeight + popHeight) * (-1);
				showPop();
			} else {
				offset = 0;
				showPopPull();
			}
		} else {
			offset = 0;
			showPopPull();
		}
									
		showAsDropDown(anchor, 0, offset);
	}
}
