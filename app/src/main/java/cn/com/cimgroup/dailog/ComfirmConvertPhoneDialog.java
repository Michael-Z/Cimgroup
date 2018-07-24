package cn.com.cimgroup.dailog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Product;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * @Description:
 * @author:yang
 * @copyright www.wenchuang.com
 * @Date 16/1/21
 */
public class ComfirmConvertPhoneDialog extends Dialog implements View.OnClickListener {

    private TextView cancleView,confirmView;
    private EditText phone;
    private Product product;
    private Context mContext;
    private TextView money;
    private TextView dui;

    public ComfirmConvertPhoneDialog(Context context) {
        super(context);
    }

    public ComfirmConvertPhoneDialog(Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }

    protected ComfirmConvertPhoneDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_phone_convert);
        findViewById();
    }

    private void findViewById(){
    	phone = (EditText) findViewById(R.id.editView_convert_phone);
        cancleView = (TextView) findViewById(R.id.cancel);
        confirmView = (TextView) findViewById(R.id.comfirm);
        money = (TextView) findViewById(R.id.textView_convert_money);
		dui = (TextView) findViewById(R.id.textView_convert_duihuan);

        cancleView.setOnClickListener(this);
        confirmView.setOnClickListener(this);
    }
    
    public void setData(Product product){
    	this.product = product;
    	money.setText(product.productSaleMoney + "乐米");
    	dui.setText(product.productMoney + "元话费");
    	if (App.userInfo != null && !TextUtils.isEmpty(App.userInfo.getMobile())) {
			phone.setText(App.userInfo.getMobile());
		}
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                dismiss();
                break;
            case R.id.comfirm:
                if(confirmView.getText().equals("确定")){
                    if(listener != null){
                    	String telRegex = "^0?1[3|4|5|6|7|8|9][0-9]\\d{8}$";
                    	String mPhoneText = phone.getText().toString();
						boolean mPhoneRes = mPhoneText.matches(telRegex);
						if (mPhoneRes) {
							listener.onClick(product, mPhoneText);
						}else {
							ToastUtil.shortToast(mContext, mContext.getResources().getString(R.string.register_error0));
						}
                    }
                }

        }
    }

    public interface ConfirmPhoneListener{
        void onClick(Product product, String phone);
    }

    private ConfirmPhoneListener listener;
    public void setListener(ConfirmPhoneListener listener){
        this.listener = listener;
    }
}
