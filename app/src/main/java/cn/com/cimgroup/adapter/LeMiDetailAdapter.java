package cn.com.cimgroup.adapter;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.LeMiDetail;
import cn.com.cimgroup.bean.RedPkgItemDetail;

/**
 * Created by small on 16/1/6.
 */
public class LeMiDetailAdapter extends SimpleBaseAdapter {
	public LeMiDetailAdapter(Context context, List data) {
		super(context, data);
	}

	@Override
	public int getItemResource() {
		return R.layout.item_mylemi;
	}

	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
		TextView timeView = (TextView) holder.getView(R.id.time_view);
		TextView typeView = (TextView) holder.getView(R.id.type_view);
		TextView numView = (TextView) holder.getView(R.id.num_view);
		TextView markView = (TextView) holder.getView(R.id.marks_view);
		TextView restView = (TextView) holder.getView(R.id.textView_lemi_rest);

		Object o = getItem(position);
		if (o instanceof LeMiDetail) {
			LeMiDetail item = (LeMiDetail) getItem(position);
			timeView.setText("时间:  "
					+ GlobalTools.timeStamp2Date(item.createTime, null));
			typeView.setText("类型:  " + item.typeDes);

			numView.setText("乐米:  " + item.inOut + item.money);
			// numView.setTextColor(colors)
			String numTotal = "乐米:  " + item.inOut + item.money;
			SpannableStringBuilder ssb = new SpannableStringBuilder(numTotal);
			ssb.setSpan(new ForegroundColorSpan(context.getResources()
					.getColor(R.color.hall_black_word)), 0, 3,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			if (item.inOut.equals("-")) {
				ssb.setSpan(new ForegroundColorSpan(context.getResources()
						.getColor(R.color.color_green_warm)), 4, numTotal.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else {
				ssb.setSpan(new ForegroundColorSpan(context.getResources()
						.getColor(R.color.color_red)), 4, numTotal.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			numView.setText(ssb);
			markView.setText("备注:  " + item.detailMsg);
			BigDecimal money = new BigDecimal(item.balance);
			String moneyStr = "";
			if (money.doubleValue() > 100000d) {
				moneyStr = money.divide(new BigDecimal("10000"), 2,
						RoundingMode.DOWN).toPlainString()
						+ "万";
			} else
				moneyStr = money.toPlainString();

			restView.setText("剩余乐米:" + moneyStr);
		} else if (o instanceof RedPkgItemDetail) {
			RedPkgItemDetail item = (RedPkgItemDetail) getItem(position);
			timeView.setText("时间:  " + item.tradeTime);
			typeView.setText("类型:  " + item.tradeTypeMsg);
			numView.setText("红包:  " + item.inOut + item.tradeMoney);
			if (item.inOut.equals("-")) {
				numView.setTextColor(context.getResources().getColor(
						R.color.color_green_warm));
			} else {
				numView.setTextColor(context.getResources().getColor(
						R.color.color_red));
			}
			markView.setText("备注:  " + item.detailMsg);
			BigDecimal money = new BigDecimal(item.balance);
			String moneyStr = "";
			if (money.doubleValue() > 100000d) {
				moneyStr = money.divide(new BigDecimal("10000"), 2,
						RoundingMode.DOWN).toPlainString()
						+ "万";
			} else
				moneyStr = money.toPlainString();
			restView.setText("剩余红包:" + moneyStr + "元");
		}

		return convertView;
	}
}
