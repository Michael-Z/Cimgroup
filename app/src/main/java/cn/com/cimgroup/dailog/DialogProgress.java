package cn.com.cimgroup.dailog;


import cn.com.cimgroup.R;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
/**
 * 
 * @Description:
 * @author:zhangjf 
 * @see:   
 * @since:      
 * @copyright www.wenchuang.com
 * @Date:2015-2-26
 */
public class DialogProgress extends DialogFragment {

	public static DialogProgress newIntance(String msg, boolean isShowMsg) {
		DialogProgress dialog = new DialogProgress();
		Bundle bundle = new Bundle();
		bundle.putString("msg", msg);
		bundle.putBoolean("isShowMsg", isShowMsg);
		dialog.setArguments(bundle);
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		String msg = bundle.getString("msg");
		boolean isShow = bundle.getBoolean("isShowMsg");
		Dialog dialog = new Dialog(getActivity(), R.style.prompt_dialog);
		View view = View.inflate(getActivity(), R.layout.dialog_progress, null);
		TextView mTVProgressMsg = (TextView) view.findViewById(R.id.progress_msg);
		if (!TextUtils.isEmpty(msg)) {
			mTVProgressMsg.setText(msg);
		}
		if (isShow) {
			mTVProgressMsg.setVisibility(View.VISIBLE);
		} else {
			mTVProgressMsg.setVisibility(View.GONE);
		}
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		return dialog;
	}
	
}
