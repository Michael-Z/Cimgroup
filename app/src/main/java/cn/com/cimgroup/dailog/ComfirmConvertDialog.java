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
public class ComfirmConvertDialog extends Dialog implements View.OnClickListener {

    TextView contentView,cancleView,confirmView,sureView,titleView;
    private Product product;
//    private int mType;
    /** 标志位  1-充值卡余额不足   其他-剩余的乐米兑换**/
    public int flag;

    public ComfirmConvertDialog(Context context) {
        super(context);
    }

    public ComfirmConvertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ComfirmConvertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_convert);
        findViewById();
    }

    private void findViewById(){
        contentView = (TextView) findViewById(R.id.dia_des);
        cancleView = (TextView) findViewById(R.id.cancel);
        confirmView = (TextView) findViewById(R.id.comfirm);
        sureView = (TextView) findViewById(R.id.sure_confirm);
        titleView = (TextView) findViewById(R.id.id_title);

        cancleView.setOnClickListener(this);
        confirmView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                if(cancleView.getText().equals("取消")){
                    dismiss();
                }else if(cancleView.getText().equals("查看记录")){
                    if(listener != null)
                        dismiss();
                        listener.onCheck();
                }

                break;
            case R.id.comfirm:
                if(confirmView.getText().equals("确定")){
                    if(listener != null)
                        listener.onClick(product);
                }else if(confirmView.getText().equals("继续兑换")){
                    dismiss();
                    if(listener != null)
                        listener.onGo();
                }

        }
    }

    public void showSucc(int type){
        show();
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
        sureView.setText(text);
        contentView.setVisibility(View.INVISIBLE);
        cancleView.setText("查看记录");
        confirmView.setText("继续兑换");
        titleView.setText("兑换成功");
        titleView.setTextSize(18);
        titleView.setVisibility(View.VISIBLE);
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

        titleView.setVisibility(View.GONE);
        this.product = product;
        cancleView.setText("取消");
        confirmView.setText("确定");
//        contentView.setText("wo hahaah");
        contentView.setVisibility(View.VISIBLE);
        contentView.setText(product.productSaleMoney + "乐米兑换" + product.productMoney + text);
        sureView.setText("确认兑换?");
    }
    
    /**
     * 余额不足 去充值？ 的提示dialog
     */
    public void setData(String text){
//    	sureView.setText("余额不足，去充值？");
    	sureView.setText(text);
    	cancleView.setText("取消");
    	confirmView.setText("确定");
    	contentView.setVisibility(View.GONE);
    }


    public interface ConfirmListener{
        void onClick(Product product);
        void onGo();
        void onCheck();
    }

    private ConfirmListener listener;
    public void setListener(ConfirmListener listener){
        this.listener = listener;
    }
    
    /**
     * 根据需求 设置dialog的内容为指定内容  
     * 非通用
     */
    public void changeContent(String text){
    	
    	sureView.setText(text);
        contentView.setVisibility(View.INVISIBLE);
        cancleView.setText("查看记录");
        confirmView.setText("继续兑换");
        titleView.setText("兑换成功");
        titleView.setVisibility(View.VISIBLE);
    	
    }
}
