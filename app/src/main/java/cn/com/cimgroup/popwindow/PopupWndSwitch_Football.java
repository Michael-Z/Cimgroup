package cn.com.cimgroup.popwindow;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.popwindow.PopWnd;

public class PopupWndSwitch_Football extends PopWnd {

	public PopupWndSwitch_Football(Context ctx, int tag, Handler mHandlerEx,
			LinearLayout popBg, TextView switchText) {
		super(ctx, tag, mHandlerEx, popBg, switchText);
		// TODO Auto-generated constructor stub
		initView(view);
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLayoutResource() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMessageWhat() {
		// TODO Auto-generated method stub
		return 0;
	}

}
