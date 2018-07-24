package cn.com.cimgroup.popwindow;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;

/**
 * 投注记录选择标题的弹出框
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class PopupWndSwitch_ScoreList extends PopWnd {
	
	private TextView mBasketballTextView;
	private TextView mFootballTextView;
	private TextView mSFCTextView;
	private TextView mR9TextView;
	private TextView mJQSTextView;
	private TextView mBQCTextView;
	
	@Override
	public int getLayoutResource() {
		return R.layout.layout_pop_switch_way_scorelist;
	}
	
	@Override
	public int getMessageWhat() {
		return GlobalConstants.MSG_NOTIFY_SWITCH_WAY_SCORELIST;
	}
	
	public PopupWndSwitch_ScoreList(Context ctx, int tag, Handler mHandlerEx, LinearLayout popBg, TextView switchText) {
		super(ctx, tag, mHandlerEx, popBg, switchText);
		
		initView(view);
	}

	private void initView(View view) {
		mBasketballTextView = (TextView) view.findViewById(R.id.textView_tz_basketball);
		mFootballTextView = (TextView) view.findViewById(R.id.textView_tz_football);
		mSFCTextView = (TextView) view.findViewById(R.id.textView_tz_sfc);
		mR9TextView = (TextView) view.findViewById(R.id.textView_tz_r9);
		mJQSTextView = (TextView) view.findViewById(R.id.textView_tz_jqs);
		mBQCTextView = (TextView) view.findViewById(R.id.textView_tz_bqc);
		
		mBasketballTextView.setTag(GlobalConstants.TAG_TZLIST_BASKETBALL);
		mFootballTextView.setTag(GlobalConstants.TAG_TZLIST_FOOTBALL);
		mSFCTextView.setTag(GlobalConstants.TAG_TZLIST_SFC);
		mR9TextView.setTag(GlobalConstants.TAG_TZLIST_R9);
		mJQSTextView.setTag(GlobalConstants.TAG_TZLIST_JQS);
		mBQCTextView.setTag(GlobalConstants.TAG_TZLIST_BQC);
		
		mBasketballTextView.setOnClickListener(new PwItemOnClickListener());
		mFootballTextView.setOnClickListener(new PwItemOnClickListener());
		mSFCTextView.setOnClickListener(new PwItemOnClickListener());
		mR9TextView.setOnClickListener(new PwItemOnClickListener());
		mJQSTextView.setOnClickListener(new PwItemOnClickListener());
		mBQCTextView.setOnClickListener(new PwItemOnClickListener());
		if (mTag > 0) {
			showSelBg(mTag);
		}
	}
	
	@Override
	protected void showSelBg(Integer tag) {
		super.showSelBg(tag);
		mBasketballTextView.setSelected(false);
		mFootballTextView.setSelected(false);
		mSFCTextView.setSelected(false);
		mR9TextView.setSelected(false);
		mJQSTextView.setSelected(false);
		mBQCTextView.setSelected(false);
		switch (tag) {
		case GlobalConstants.TAG_TZLIST_BASKETBALL:
			mBasketballTextView.setSelected(true);
			break;
		case GlobalConstants.TAG_TZLIST_FOOTBALL:
			mFootballTextView.setSelected(true);
			break;
		case GlobalConstants.TAG_TZLIST_SFC:
			mSFCTextView.setSelected(true);
			break;
		case GlobalConstants.TAG_TZLIST_R9:
			mR9TextView.setSelected(true);
			break;
		case GlobalConstants.TAG_TZLIST_JQS:
			mJQSTextView.setSelected(true);
			break;
		case GlobalConstants.TAG_TZLIST_BQC:
			mBQCTextView.setSelected(true);
			break;
		default:
			break;
		}
	}
    
	@Override
	protected Object showTitle(Integer tag) {
		String title = "";
		switch (tag) {
		case GlobalConstants.TAG_TZLIST_BASKETBALL:
			title = App.getInstance().getResources().getString(R.string.tz_select_basketball);
			break;
		case GlobalConstants.TAG_TZLIST_FOOTBALL:
			title = App.getInstance().getResources().getString(R.string.tz_select_football);
			break;
		case GlobalConstants.TAG_TZLIST_SFC:
			title = App.getInstance().getResources().getString(R.string.tz_select_sfc);
			break;
		case GlobalConstants.TAG_TZLIST_R9:
			title = App.getInstance().getResources().getString(R.string.tz_select_r9);
			break;
		case GlobalConstants.TAG_TZLIST_JQS:
			title = App.getInstance().getResources().getString(R.string.tz_select_jqs);
			break;
		case GlobalConstants.TAG_TZLIST_BQC:
			title = App.getInstance().getResources().getString(R.string.tz_select_bqc);
			break;
		default:
			break;
		}
		return title;
	}

}
