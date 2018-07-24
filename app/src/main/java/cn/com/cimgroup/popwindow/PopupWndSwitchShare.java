package cn.com.cimgroup.popwindow;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;

public class PopupWndSwitchShare  extends PopWnd {

	PlayMenuItemClick mPlayMenuItemClick;

	public interface PlayMenuItemClick {

		public void PopShare(int type);

	}

	@Override
	protected boolean isWrapContent() {
		return true;
	}

	public PopupWndSwitchShare(Context ctx, PlayMenuItemClick itemClick) {
		super(ctx, 0, null, null, null);
		if (itemClick != null) {
			mPlayMenuItemClick = itemClick;
		}
		initView(view);
	}

	public PopupWndSwitchShare(Context ctx, int tag, Handler mHandlerEx, LinearLayout popBg, TextView switchText) {
		super(ctx, tag, mHandlerEx, popBg, switchText);
	}

	private void initView(View view) {
		TextView wx = (TextView) view.findViewById(R.id.pop_share_wx);
		TextView wxFriend = (TextView) view.findViewById(R.id.pop_share_wx_friend);
		TextView qq = (TextView) view.findViewById(R.id.pop_share_qq);
		TextView qqFriend = (TextView) view.findViewById(R.id.pop_share_qq_friend);

		wx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayMenuItemClick.PopShare(0);
				dissmiss();
			}
		});
		wxFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayMenuItemClick.PopShare(1);
				dissmiss();
			}
		});
		qq.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dissmiss();
				mPlayMenuItemClick.PopShare(2);
			}
		});
		qqFriend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dissmiss();
				mPlayMenuItemClick.PopShare(3);
			}
		});
	}

	@Override
	public int getLayoutResource() {
		return R.layout.layout_pop_switch_share;
	}

	@Override
	public int getMessageWhat() {
		return GlobalConstants.MSG_NOTIFY_SWITCH_SHARE;
	}

}