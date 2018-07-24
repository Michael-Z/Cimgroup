package cn.com.cimgroup.popwindow;

import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * @Description:购彩排列3Popupwindow
 * @author:zhangjf 
 * @see:   
 * @since:      
 * @copyright www.wenchuang.com
 * @Date:2015-1-8
 */

public class PopupWndSwitch_PL3 extends PopWnd {
	
	private TextView mZxTextView;
	private TextView mZxhzTextView;
	private TextView mZ3TextView;
	private TextView mZhixhzTextView;
	private TextView mZ6TextView;
	
	private ImageView mZxImageView;
	private ImageView mZxhzImageView;
	private ImageView mZ3ImageView;
	private ImageView mZhixhzImageView;
	private ImageView mZ6ImageView;
	
	@Override
	public int getLayoutResource() {
		return R.layout.layout_pop_switch_way_pl3;
	}
	
	@Override
	public int getMessageWhat() {
		return GlobalConstants.MSG_NOTIFY_SWITCH_WAY_PL3;
	}
	
	public PopupWndSwitch_PL3(Context ctx, int tag, Handler mHandlerEx, LinearLayout popBg, TextView switchText) {
		super(ctx, tag, mHandlerEx, popBg, switchText);
		
		initView(view);
	}

	private void initView(View view) {
		mZxTextView = (TextView) view.findViewById(R.id.textView_pl3_zx);
		mZxhzTextView = (TextView) view.findViewById(R.id.textView_pl3_zxhz);
		mZ3TextView = (TextView) view.findViewById(R.id.textView_pl3_z3);
		mZhixhzTextView = (TextView) view.findViewById(R.id.textView_pl3_zhixhz);
		mZ6TextView = (TextView) view.findViewById(R.id.textView_pl3_z6);
		
		mZxImageView = (ImageView) view.findViewById(R.id.imageView_pl3_zx);
		mZxhzImageView = (ImageView) view.findViewById(R.id.imageView_pl3_zxhz);
		mZ3ImageView = (ImageView) view.findViewById(R.id.imageView_pl3_z3);
		mZhixhzImageView = (ImageView) view.findViewById(R.id.imageView_pl3_zhixhz);
		mZ6ImageView = (ImageView) view.findViewById(R.id.imageView_pl3_z6);
		
		mZxTextView.setTag(GlobalConstants.LOTTERY_PL3_ZX);
		mZxhzTextView.setTag(GlobalConstants.LOTTERY_PL3_ZXHZ);
		mZ3TextView.setTag(GlobalConstants.LOTTERY_PL3_Z3);
		mZhixhzTextView.setTag(GlobalConstants.LOTTERY_PL3_ZHIXHZ);
		mZ6TextView.setTag(GlobalConstants.LOTTERY_PL3_Z6);
		
		mZxTextView.setOnClickListener(new PwItemOnClickListener());
		mZxhzTextView.setOnClickListener(new PwItemOnClickListener());
		mZ3TextView.setOnClickListener(new PwItemOnClickListener());
		mZhixhzTextView.setOnClickListener(new PwItemOnClickListener());
		mZ6TextView.setOnClickListener(new PwItemOnClickListener());
		if (mTag > 0) {
			showSelBg(mTag);
		}
	}
	
	@Override
	protected void showSelBg(Integer tag) {
		super.showSelBg(tag);
		mZxTextView.setSelected(false);
		mZxhzTextView.setSelected(false);
		mZ3TextView.setSelected(false);
		mZhixhzTextView.setSelected(false);
		mZ6TextView.setSelected(false);
		
		mZxImageView.setVisibility(View.GONE);
		mZxhzImageView.setVisibility(View.GONE);
		mZ3ImageView.setVisibility(View.GONE);
		mZhixhzImageView.setVisibility(View.GONE);
		mZ6ImageView.setVisibility(View.GONE);
		switch (tag) {
		case GlobalConstants.LOTTERY_PL3_ZX:
			mZxTextView.setSelected(true);
			mZxImageView.setVisibility(View.VISIBLE);
			break;
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			mZxhzTextView.setSelected(true);
			mZxhzImageView.setVisibility(View.VISIBLE);
			break;
		case GlobalConstants.LOTTERY_PL3_Z3:
			mZ3TextView.setSelected(true);
			mZ3ImageView.setVisibility(View.VISIBLE);
			break;
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			mZhixhzTextView.setSelected(true);
			mZhixhzImageView.setVisibility(View.VISIBLE);
			break;
		case GlobalConstants.LOTTERY_PL3_Z6:
			mZ6TextView.setSelected(true);
			mZ6ImageView.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected Object showTitle(Integer tag) {
		String title = "";
		switch (tag) {
		case GlobalConstants.LOTTERY_PL3_ZX:
			title = App.getInstance().getResources().getString(R.string.lottery_p3) + "-" + App.getInstance().getResources().getString(R.string.lottery_p3_pop_zx);
			break;
		case GlobalConstants.LOTTERY_PL3_ZXHZ:
			title = App.getInstance().getResources().getString(R.string.lottery_p3) + "-" + App.getInstance().getResources().getString(R.string.lottery_p3_pop_zxhz);
			break;
		case GlobalConstants.LOTTERY_PL3_Z3:
			title = App.getInstance().getResources().getString(R.string.lottery_p3) + "-" + App.getInstance().getResources().getString(R.string.lottery_p3_pop_z3);
			break;
		case GlobalConstants.LOTTERY_PL3_ZHIXHZ:
			title = App.getInstance().getResources().getString(R.string.lottery_p3) + "-" + App.getInstance().getResources().getString(R.string.lottery_p3_zxhz);
			break;
		case GlobalConstants.LOTTERY_PL3_Z6:
			title = App.getInstance().getResources().getString(R.string.lottery_p3) + "-" + App.getInstance().getResources().getString(R.string.lottery_p3_pop_z6);
			break;
		default:
			break;
		}
		return title;
	}

}
