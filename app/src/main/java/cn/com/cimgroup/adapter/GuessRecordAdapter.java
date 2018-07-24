package cn.com.cimgroup.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.GuessRecordBean;

public class GuessRecordAdapter extends SimpleBaseAdapter {

	private Context context;

	public GuessRecordAdapter(Context context, List data) {
		super(context, data);
		this.context = context;
	}

	@Override
	public int getItemResource() {
		return R.layout.adapter_guess_record;
	}

	@Override
	public View getItemView(int position, View convertView,
			ViewHolder holder) {
		TextView tv_time = (TextView) holder.getView(R.id.tv_time);
		TextView tv_match_team_host = (TextView) holder
				.getView(R.id.tv_match_team_host);
		TextView tv_match_team_guest = (TextView) holder
				.getView(R.id.tv_match_team_guest);
		TextView tv_bet_no = (TextView) holder.getView(R.id.tv_bet_no);
		TextView tv_bet_result = (TextView) holder.getView(R.id.tv_bet_result);
		TextView tv_bet_amount = (TextView) holder.getView(R.id.tv_bet_amount);
		TextView tv_bet_win_amount = (TextView) holder
				.getView(R.id.tv_bet_win_amount);

		GuessRecordBean bean = (GuessRecordBean) getItem(position);
		// 设置主队名称
		tv_match_team_host.setText(bean.getHostName());
		// 设置客队名称
		tv_match_team_guest.setText(bean.getGuestName());
		// 设置期次
		tv_bet_no.setText(bean.getDateNo());
		String status = "";
		// 设置投注状态
		tv_bet_result.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
		if (bean.getStatus().equals("0")) {
			status = "等待开奖";
			tv_bet_result.setText(status);
		} else if (bean.getStatus().equals("1")) {
			status = "已派奖";
			tv_bet_result.setText(Html.fromHtml("<font color='"
					+ context.getResources().getColor(R.color.color_red) + "'>"
					+ status + "</font>"));
		} else if (bean.getStatus().equals("2")) {
			status = "未中奖";
			tv_bet_result.setText(status);
		} else if (bean.getStatus().equals("3")) {
			status = "竞猜取消";
			tv_bet_result.setText(status);
		}
		// 设置投注日期
		tv_time.setText(bean.getAddTime());
		// 设置投注金额
		tv_bet_amount.setText(Html.fromHtml("投注<font color='"
				+ context.getResources().getColor(R.color.color_red) + "'>"
				+ bean.getBetAmount() + "</font>米"));
		// 设置中奖金额
		if (bean.getStatus() != null && !TextUtils.isEmpty(bean.getStatus())) {
			if (bean.getStatus().equals("1")) {
				tv_bet_win_amount.setVisibility(View.VISIBLE);
				tv_bet_win_amount.setText(Html.fromHtml("<font color='"
						+ context.getResources().getColor(R.color.color_red)
						+ "'>中奖" + bean.getReturnAmount() + "米</font>"));
			} else if (bean.getStatus().equals("3")) {
				tv_bet_win_amount.setVisibility(View.VISIBLE);
				tv_bet_win_amount.setText(Html.fromHtml("<font color='"
						+ context.getResources().getColor(R.color.color_red)
						+ "'>返款" + bean.getReturnAmount() + "米</font>"));
			}
			else
				tv_bet_win_amount.setVisibility(View.GONE);
		}
		return convertView;
	}

}
