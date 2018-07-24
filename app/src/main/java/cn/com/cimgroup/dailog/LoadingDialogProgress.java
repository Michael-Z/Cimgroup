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
public class LoadingDialogProgress extends DialogFragment {

	public static LoadingDialogProgress newIntance(boolean isShowMsg) {
		LoadingDialogProgress dialog = new LoadingDialogProgress();
		return dialog;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.prompt_dialog);
		View view = View.inflate(getActivity(), R.layout.loading_dialog_progress, null);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		return dialog;
	}
	
}
