package cn.com.cimgroup.popwindow;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 数字彩、老足彩右上角更多按钮点击显示Pop
 * @author 秋风
 *
 */
public class PopupWndSwitchPLayMenu extends PopWnd {
	/**是否属于数字彩或者老足彩*/
	private boolean isNumPlay=false;

	PlayMenuItemClick mPlayMenuItemClick;
	TextView tzList;

	public interface PlayMenuItemClick {

		public void PopDraw();

		public void PopPLay();
		
		public void PopTzList();
		/**跳转走势图*/
		void PopZST();
	}

	@Override
	protected boolean isWrapContent() {
		return true;
	}

	public PopupWndSwitchPLayMenu(Context ctx, PlayMenuItemClick itemClick) {
		super(ctx, 0, null, null, null);
		if (itemClick != null) {
			mPlayMenuItemClick = itemClick;
		}
		initView(view);
	}
	public PopupWndSwitchPLayMenu(Context ctx, PlayMenuItemClick itemClick,boolean isNumPop) {
		super(ctx, 0, null, null, null);
		isNumPlay=isNumPop;
		if (itemClick != null) {
			mPlayMenuItemClick = itemClick;
		}
		initView(view);
	}

	public PopupWndSwitchPLayMenu(Context ctx, int tag, Handler mHandlerEx, LinearLayout popBg, TextView switchText) {
		super(ctx, tag, mHandlerEx, popBg, switchText);
	}

	private void initView(View view) {
		
		
		TextView mPopDraw = (TextView) view.findViewById(R.id.pop_draw);
		TextView pop_zst=(TextView) view.findViewById(R.id.pop_zst);
		TextView mPopPLay = (TextView) view.findViewById(R.id.pop_play);
		tzList = (TextView) view.findViewById(R.id.pop_tzlist);
		if (!isNumPlay) {
			view.findViewById(R.id.id_line).setVisibility(View.GONE);
			pop_zst.setVisibility(View.GONE);
			//2.5期替换足彩 右上角  更多  的下拉内容， 将   开奖信息  替换为   比分直播
			mPopDraw.setText("比分直播");
		}

		mPopDraw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayMenuItemClick.PopDraw();
				dissmiss();
			}
		});
		pop_zst.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPlayMenuItemClick.PopZST();
				dissmiss();
			}
		});
		
		mPopPLay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dissmiss();
				mPlayMenuItemClick.PopPLay();
			}
		});
		tzList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dissmiss();
				mPlayMenuItemClick.PopTzList();
			}
		});
	}

	@Override
	public int getLayoutResource() {
		return R.layout.layout_pop_switch_play_menu_new;
	}

	@Override
	public int getMessageWhat() {
		return GlobalConstants.MSG_NOTIFY_SWITCH_PLAY_MENU;
	}

}
