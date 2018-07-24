package cn.com.cimgroup.dailog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.com.cimgroup.R;

/**
 * @Description:
 * @author:yang
 * @copyright www.wenchuang.com
 * @Date 16/1/19
 */
public class MessageDeleteDialog extends Dialog implements View.OnClickListener {

    public MessageDeleteDialog(Context context) {
        super(context);
    }

    public MessageDeleteDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MessageDeleteDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public interface ClickListener{
      void  OnClick();
    };
    private ClickListener listener;
    public void setListener(ClickListener listener){
        this.listener = listener;
    }

    TextView deleteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_message_delete);
        deleteView = (TextView) findViewById(R.id.delete_view);
        deleteView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.OnClick();
        }
    }
}
