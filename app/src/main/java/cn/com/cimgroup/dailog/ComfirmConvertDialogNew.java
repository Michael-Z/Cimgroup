package cn.com.cimgroup.dailog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Product;
import cn.com.cimgroup.frament.GridFragment;

/**
 * @Description:
 * @author:yang
 * @copyright www.wenchuang.com
 * @Date 16/1/21
 */
public class ComfirmConvertDialogNew extends Dialog implements View.OnClickListener, ComfirmConvertDialog.ConfirmListener {

    TextView cancleView,confirmView,textView_convert_money,textView_convert_duihuan;
    private Product product;
//    private int mType;
    /** 标志位  1-充值卡余额不足   其他-剩余的乐米兑换**/
    public int flag;

    public ComfirmConvertDialogNew(Context context) {
        super(context);
    }

    public ComfirmConvertDialogNew(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ComfirmConvertDialogNew(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_convert_new);
        findViewById();
    }

    private void findViewById(){
    	//底部的解释内容
//        contentView = (TextView) findViewById(R.id.dia_des);
    	//取消按钮
        cancleView = (TextView) findViewById(R.id.cancel);
        //确认按钮
        confirmView = (TextView) findViewById(R.id.comfirm);
        //“确认兑换？”
//        sureView = (TextView) findViewById(R.id.sure_confirm);
        //标题
//        titleView = (TextView) findViewById(R.id.id_title);
        //兑换内容——左侧（乐米）
        textView_convert_money = (TextView) findViewById(R.id.textView_convert_money);
        //兑换内容-右侧（兑换券）
        textView_convert_duihuan = (TextView) findViewById(R.id.textView_convert_duihuan);

        cancleView.setOnClickListener(this);
        confirmView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
            	dismiss();

                break;
            case R.id.comfirm:
                if(listener != null)
                   listener.onClick(product,true);

        }
    }

    public void showSucc(Context context, int type){
    	
    	ComfirmConvertDialog dialog = new ComfirmConvertDialog(context,
				R.style.MessageDelDialog);
//        show();
        String text = "您兑换的电子卷已发送到您的个人账户";
        switch (type) {
		case GridFragment.LEMIRED:
			text = "红包金额已入账户";
			break;
		case GridFragment.LEMITELE:
			text = "请注意话费到账信息！";
			break;
		default:
			break;
		}
        dialog.showSucc(type);
        dialog.setListener(this);
    }

    /**
     * @param product
     */
    public void setData(Product product,int type){
        String text ="元购物券" ;
//        mType = type;
        switch (type){
            case GridFragment.LEMIRED:
                text = "元通用红包";
                break;
            case GridFragment.LEMITELE:
                text = "元充值卡";
                break;
            default:
                break;
        }

//        titleView.setVisibility(View.GONE);
        this.product = product;
        cancleView.setText("取消");
        confirmView.setText("确定");
//        contentView.setVisibility(View.VISIBLE);
//        contentView.setText(product.productSaleMoney + "乐米兑换" + product.productMoney + text);
        textView_convert_money.setText(product.productSaleMoney+"乐米");
        textView_convert_duihuan.setText(product.productMoney+text);
        
//        sureView.setText("确认兑换?");
    }
    

    public interface ConfirmListener{
        void onClick(Product product, boolean flag);
        void onGo();
        void onCheck();
    }

    private ConfirmListener listener;
    public void setListener(ConfirmListener listener){
        this.listener = listener;
    }

	@Override
	public void onClick(Product product) {
		listener.onClick(product, false);
	}

	@Override
	public void onGo() {
		listener.onGo();
	}

	@Override
	public void onCheck() {
		listener.onCheck();
	}
}
