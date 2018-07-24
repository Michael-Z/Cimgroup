package cn.com.cimgroup.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.TzListActivity.MCenterRecordTz;
import cn.com.cimgroup.bean.AllBetting;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 投注记录Adapter
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年10月31日
 */
public class CenterListTzAdapter extends SimpleBaseAdapter<AllBetting> {
	private MCenterRecordTz mCenterRecordTz;

	public CenterListTzAdapter(Context context, MCenterRecordTz centerRecordTz) {
		super(context, null);
		mCenterRecordTz = centerRecordTz;
	}

	@Override
	public int getItemResource() {
		return R.layout.item_fragment_tzjl_old;
	}

	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
		ImageView hemaiImage = holder.getView(R.id.imageView_tz_item_hemai);
		TextView timeTextView = holder.getView(R.id.textView_tz_item_time);
		TextView titleTextView = holder.getView(R.id.textView_tz_item_title);
		TextView qiTextView = holder.getView(R.id.textView_tz_item_qi);
		TextView stateTextView = holder.getView(R.id.textView_tz_item_state);
		TextView moneyTextView = holder.getView(R.id.textView_tz_item_money);
		TextView isWinTextView = holder.getView(R.id.textView_tz_item_iswin);
		View marg = holder.getView(R.id.textView_tz_item_margin);

		// top = (TextView) holder.getView(R.id.textView_tzjl_line);
		// time = (TextView) holder.getView(R.id.textView_line_item_time);

		final AllBetting allBetting = mList.get(position);
		// titleTextView.setText(Html.fromHtml("<font color='" +
		// context.getResources().getColor(R.color.hall_kafei_word) + "'><b>" +
		// allBetting.getGameName() + "</b></font>"));
		titleTextView.setText(allBetting.getGameName());

		timeTextView.setText(DateUtil.getTimeInMillisToStr(allBetting
				.getCreateTime()));

		switch (mCenterRecordTz) {
		case ALL:
			qiTextView.setText(allBetting.getIssueDes());
			stateTextView.setText(allBetting.getStatusDes());
			// moneyTextView.setText(Html.fromHtml("<font color='" +
			// context.getResources().getColor(R.color.hall_kafei_word) + "'>" +
			// allBetting.getBetMoney() + "</font>"));
			moneyTextView.setText(allBetting.getBetMoney());
			switch (allBetting.getIsAward()) {
			case "2":
				isWinTextView.setVisibility(View.VISIBLE);
				isWinTextView.setText(allBetting.getAwardDes());
				break;

			default:
				isWinTextView.setVisibility(View.GONE);
				break;
			}
			break;
		case BUY:
			qiTextView.setText(context.getResources().getString(
					R.string.record_tz_detail_qi, allBetting.getIssue()));
			// 使用status字段判断彩种状态
			// String status = allBetting.getStatus();
			// if (status.equals("1000")) {
			// stateTextView.setText(allBetting.getStatusDes());
			// }
			// String[] statusDes = new
			// String[]{"已受理","受理失败","出票成功","已退票","未中奖","已派奖","已中奖未派奖","出票成功，扣费失败","出票失败，解冻失败","自算奖","需审核"};
			// stateTextView.setText(statusDes[Integer.parseInt(status)-1]);
			stateTextView.setText(allBetting.getStatusDes());
			// moneyTextView.setText(Html.fromHtml("<font color='" +
			// context.getResources().getColor(R.color.hall_kafei_word) + "'>" +
			// context.getResources().getString(R.string.betting) +
			// allBetting.getMoney() +
			// context.getResources().getString(R.string.tz_item_mi) +
			// "</font>"));
			moneyTextView.setText(context.getResources().getString(
					R.string.betting)
					+ allBetting.getMoney()
					+ context.getResources().getString(R.string.tz_item_mi));
			if (!StringUtil.isEmpty(allBetting.getWinMoney())) {
				if (Double.parseDouble(allBetting.getWinMoney()) > 0) {
					isWinTextView.setVisibility(View.VISIBLE);
					isWinTextView.setText(Html.fromHtml("<font color='"
							+ context.getResources().getColor(R.color.color_red)
							+ "'>"
							+ context.getResources().getString(
									R.string.tz_title_zj)
							+ allBetting.getWinMoney()
							+ context.getResources().getString(
									R.string.tz_item_mi) + "</font>"));
				} else {
					isWinTextView.setVisibility(View.GONE);
				}
			} else {
				isWinTextView.setVisibility(View.GONE);
			}
//			if (allBetting.getStatusDes().equals("待付款")) {
//				stateTextView.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						Intent intent=new Intent(context,CommitPayActivity.class);
//						Bundle bundle=new Bundle();
//						bundle.putSerializable("data", allBetting);
//						intent.putExtras(bundle);
//						context.startActivity(intent);
//					}
//				});
//			}
			break;
		case ZHUI:
			qiTextView.setText("已追" + allBetting.getFinishTimes() + "期，共"
					+ allBetting.getChaseAllTimes() + "期");
			switch (allBetting.getChaseStatus()) {
			case "1":
				stateTextView.setText("追号中");
				break;
			case "2":
				stateTextView.setText("追停结束");
				break;
			default:
				break;
			}
			// moneyTextView.setText(Html.fromHtml("<font color='" +
			// context.getResources().getColor(R.color.hall_kafei_word) + "'>" +
			// context.getResources().getString(R.string.betting) +
			// allBetting.getChaseAllMoney() +
			// context.getResources().getString(R.string.tz_item_mi) +
			// "</font>"));
			moneyTextView.setText(context.getResources().getString(
					R.string.betting)
					+ allBetting.getChaseAllMoney()
					+ context.getResources().getString(R.string.tz_item_mi));
			if (!StringUtil.isEmpty(allBetting.getWinMoney())) {
				if (Double.parseDouble(allBetting.getWinMoney()) > 0) {
					isWinTextView.setVisibility(View.VISIBLE);
					// isWinTextView.setText(Html.fromHtml("<font color='" +
					// context.getResources().getColor(R.color.hall_kafei_word)
					// + "'>" +
					// context.getResources().getString(R.string.tz_title_zj) +
					// "</font>" + "<font color='"+
					// context.getResources().getColor(R.color.hall_red) +"'>" +
					// allBetting.getWinMoney() + "</font>"+"<font color='" +
					// context.getResources().getColor(R.color.hall_kafei_word)
					// + "'>" +
					// context.getResources().getString(R.string.tz_item_mi) +
					// "</font>"));
					isWinTextView.setText(Html.fromHtml("<font color='"
							+ context.getResources().getColor(R.color.color_red)
							+ "'>"
							+ context.getResources().getString(
									R.string.tz_title_zj)
							+ allBetting.getWinMoney()
							+ context.getResources().getString(
									R.string.tz_item_mi) + "</font>"));
				} else {
					isWinTextView.setVisibility(View.GONE);
				}
			} else {
				isWinTextView.setVisibility(View.GONE);
			}
			break;
		case WINNING:
			qiTextView.setText(allBetting.getIssueDes());
			stateTextView.setText(allBetting.getStatusDes());
			// moneyTextView.setText(Html.fromHtml("<font color='" +
			// context.getResources().getColor(R.color.hall_kafei_word) + "'>" +
			// allBetting.getBetMoney() + "</font>"));
			moneyTextView.setText(allBetting.getBetMoney());
			switch (allBetting.getIsAward()) {
			case "2":
				isWinTextView.setVisibility(View.VISIBLE);
				isWinTextView.setText(allBetting.getAwardDes());
				break;

			default:
				isWinTextView.setVisibility(View.GONE);
				break;
			}
			break;
		default:
			break;
		}

		if (position == (mList.size() - 1)) {
			marg.setVisibility(View.GONE);
		} else {
			marg.setVisibility(View.VISIBLE);
		}

		// addLine(getData());
		// View view = View.inflate(context, R.layout.item_fragment_tzline,
		// null);
		// AllBetting cur = lists.get(i);
		// String curTime =
		// DateUtil.getTimeInMillisToStr(allBetting.getCreateTime()).substring(0,
		// 10);
		// String mm = curTime.substring(5, 7);
		// String dd = curTime.substring(9, 10);
		// time.setText(Html.fromHtml(context.getResources().getString(R.string.tz_line_time,
		// mm,dd)));
		// int height = GlobalTools.dip2px(context, 70);
		// top.setHeight(height);
		return convertView;
	}

	private int itemlen = 0;

	public TextView time;
	public TextView top;

	private void addLine(List<AllBetting> lists) {
		for (int i = 0; i < lists.size(); i++) {
			View view = View.inflate(context, R.layout.item_fragment_tzline,
					null);

			if ((i + 1) < lists.size()) {

				AllBetting next = lists.get(i + 1);
				AllBetting cur = lists.get(i);
				String curTime = DateUtil.getTimeInMillisToStr(
						cur.getCreateTime()).substring(0, 10);
				String nextTime = DateUtil.getTimeInMillisToStr(
						next.getCreateTime()).substring(0, 10);
				if (curTime.equals(nextTime)) {
					itemlen = itemlen + 1;
				} else {

					String mm = curTime.substring(5, 7);
					String dd = curTime.substring(9, 10);
					time.setText(Html.fromHtml(context.getResources()
							.getString(R.string.tz_line_time, mm, dd)));
					// int height = ((itemlen - 1) *
					// (GlobalTools.dip2px(context, 6)) + itemlen *
					// GlobalTools.dip2px(context, 70) -
					// GlobalTools.dip2px(context, 40)) / 2;
					// top.setHeight(height);
					// down.setHeight(height);
					itemlen = 1;
				}
			} else {
				AllBetting cur = lists.get(i);
				String curTime = DateUtil.getTimeInMillisToStr(
						cur.getCreateTime()).substring(0, 10);
				String mm = curTime.substring(5, 7);
				String dd = curTime.substring(9, 10);
				time.setText(Html.fromHtml(context.getResources().getString(
						R.string.tz_line_time, mm, dd)));
				// int height = ((itemlen - 1) * (GlobalTools.dip2px(context,
				// 6)) + itemlen * GlobalTools.dip2px(context, 70) -
				// GlobalTools.dip2px(context, 40)) / 2;
				// top.setHeight(height);
			}

		}
	}
}
