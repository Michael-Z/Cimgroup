package cn.com.cimgroup.popwindow;

import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @Description:购彩大乐透Popupwindow
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-1-8
 */
public class PopupWndSwitch_DLT extends PopWnd {

	private TextView btnDlt;
	private TextView btnDltDt;
	
	private ImageView imageDlt;
	private ImageView imageDltDt;
	

	@Override
	public int getLayoutResource() {
		return R.layout.layout_pop_switch_way_dlt;
	}
	
	@Override
	public int getMessageWhat() {
		return GlobalConstants.MSG_NOTIFY_SWITCH_WAY_DLT;
	}
	@SuppressLint("InflateParams")
	public PopupWndSwitch_DLT(Context ctx, int tag, Handler mHandlerEx, LinearLayout popBg, TextView switchText) {
		super(ctx, tag, mHandlerEx, popBg, switchText);

		initView(view);
	}

	private void initView(View view) {
		btnDlt = (TextView) view.findViewById(R.id.btn_dlt_switch);
		btnDltDt = (TextView) view.findViewById(R.id.btn_dlt_switch_dt);
		
		imageDlt = (ImageView) view.findViewById(R.id.imageView_dlt_switch);
		imageDltDt = (ImageView) view.findViewById(R.id.imageView_dlt_switch_dt);
		
		btnDlt.setTag(GlobalConstants.LOTTERY_DLT_PT);
		btnDltDt.setTag(GlobalConstants.LOTTERY_DLT_DT);
		btnDlt.setOnClickListener(new PwItemOnClickListener());
		btnDltDt.setOnClickListener(new PwItemOnClickListener());
		if (mTag > 0) {
			showSelBg(mTag);
		}
	}

	@Override
	protected void showSelBg(Integer tag) {
		super.showSelBg(tag);
		btnDlt.setSelected(false);
		btnDltDt.setSelected(false);
		imageDlt.setVisibility(View.GONE);
		imageDltDt.setVisibility(View.GONE);
		switch (tag) {
		case GlobalConstants.LOTTERY_DLT_PT:
			btnDlt.setSelected(true);
			imageDlt.setVisibility(View.VISIBLE);
			break;
		case GlobalConstants.LOTTERY_DLT_DT:
			btnDltDt.setSelected(true);
			imageDltDt.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}

	@Override
	protected Object showTitle(Integer tag) {
		String title = "";
		switch (tag) {
		case GlobalConstants.LOTTERY_DLT_PT:
			title = App.getInstance().getResources().getString(R.string.lotteryhall_dlt) + "-" + App.getInstance().getResources().getString(R.string.lottery_dlt_pt);
			break;
		case GlobalConstants.LOTTERY_DLT_DT:
			title = App.getInstance().getResources().getString(R.string.lotteryhall_dlt) + "-" + App.getInstance().getResources().getString(R.string.lottery_dlt_dt);
			break;

		default:
			break;
		}
		return title;
	}

}
