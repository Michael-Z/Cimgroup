package cn.com.cimgroup.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.LotteryListObj;

/**
 * 购彩界面多期开奖列表
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class LotteryDrawInfoAdapter extends SimpleBaseAdapter<LotteryListObj> {
    
	public LotteryDrawInfoAdapter(Context context, List<LotteryListObj> data) {
		super(context, data);
	}

	@Override
	public int getItemResource() {
		return R.layout.item_multerm_award_list;
	}

	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
		TextView mItemTermNum = holder.getView(R.id.item_award_term_num);
		TextView textView = holder.getView(R.id.item_award_num);
		
		LotteryListObj lotteryDrawInfo = (LotteryListObj) getItem(position);
		mItemTermNum.setText(lotteryDrawInfo.getIssueNo() + context.getResources().getString(R.string.cart_add_qi));
		String code = lotteryDrawInfo.getWinCode();
		textView.setTextSize(16);
		textView.setGravity(Gravity.CENTER);
		int red = context.getResources().getColor(R.color.color_red);
		int blue = context.getResources().getColor(R.color.hall_blue);
		String[] arr = code.split(" ");
		if (arr.length > 1) {
			textView.setText(Html.fromHtml("<font color='" + red + "'>" + arr[0].replaceAll(",", " ") + "</font> <font color='" + blue +"'>" + arr[1].replaceAll(",", " ") + "</font>"));
		}else {
			textView.setText(Html.fromHtml("<font color='" + red + "'>" + code.replaceAll(",", " ") + "</font>"));
		}
		
		return convertView;
	}

}
