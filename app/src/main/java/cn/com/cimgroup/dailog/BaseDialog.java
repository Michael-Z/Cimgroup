package cn.com.cimgroup.dailog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import cn.com.cimgroup.R;

public class BaseDialog extends Dialog {

	public BaseDialog(Context context) {
		super(context, R.style.dialog_style);
	}

	public void initStyle(int width, int height) {
		try {
			WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
			layoutParams.height = width;
			layoutParams.width = height;
			layoutParams.gravity = Gravity.CENTER;
		} catch (Exception e) {
		}
	}

}
