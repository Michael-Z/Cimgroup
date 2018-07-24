package cn.com.cimgroup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.TcjczqObj;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * Created by small on 16/2/1.
 */
public class TzPkgAdapter extends SimpleBaseAdapter<TcjczqObj> {
	
    public TzPkgAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_tz_red_pkg;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        ImageView picView = (ImageView) holder.getView(R.id.pkg_type);
        TextView countView = (TextView) holder.getView(R.id.pkg_count);
        TextView desView = (TextView) holder.getView(R.id.pkg_des);
        View choose = holder.getView(R.id.check);

        TcjczqObj item = (TcjczqObj) getItem(position);

        if(item.choose){
            choose.setVisibility(View.VISIBLE);
        }else{
            choose.setVisibility(View.GONE);
        }

        if(item != null){
            if(item.type == TcjczqObj.GIVE){
            	if (item.giveRedMoney != null) {
            		picView.setImageResource(R.drawable.red_pkg_free);
            		String time = GlobalTools.timeStamp2Date(item.giveFailTime,"");
	                
	                if (!StringUtil.isEmpty(time)) {
	                	time = time.substring(5, (time.length() - 3));
					}
                    desView.setText(time + "失效" + "\n" + item.giveDes);
                    countView.setText(GlobalTools.string2Span(item.giveRedMoney));
				}
            }else if(item.type == TcjczqObj.CONVERT){
            	if (item.converRedMoney != null) {
	                picView.setImageResource(R.drawable.red_pkg_convert);
	                String time = GlobalTools.timeStamp2Date(item.converFailTime,"");
	                
	                if (!StringUtil.isEmpty(time)) {
	                	time = time.substring(5, (time.length() - 3));
					}
	                
	                desView.setText(time + "失效" + "\n" + item.converDes);
	                countView.setText(GlobalTools.string2Span(item.converRedMoney));
            	}
            }else if(item.type == TcjczqObj.BUY){
            	if (item.buyRedMoney != null) {
	                picView.setImageResource(R.drawable.red_pkg_buy);
	                desView.setText("无失效期" + "\n" + "全场购彩通用");
	                countView.setText(GlobalTools.string2Span(item.buyRedMoney));
            	}
            }
        }

        return convertView;
    }
}
