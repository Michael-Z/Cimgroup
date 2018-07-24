package cn.com.cimgroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.view.FlowLayout;
import cn.com.cimgroup.xutils.StringUtil;

/**
 * 开奖列表adapter
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年3月15日
 */
@SuppressWarnings("rawtypes")
public class LotteryDrawAdapter extends SimpleBaseAdapter<LotteryDrawInfo> {
	private String mGameName="";

	@SuppressWarnings("unchecked")
	public LotteryDrawAdapter(Context context, List data,String gameName) {
		super(context, data);
		mGameName=gameName;
	}

	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
		// 彩票名称
		TextView mLotteryClassView = holder.getView(R.id.txtView_looterydraw_title);
		TextView mLotteryTermNumView = holder.getView(R.id.txtView_looterydraw_term);
		TextView mLotteryDrawDateView = holder.getView(R.id.txtView_looterydraw_date);
		FlowLayout mLotteryDrawNumberView = (FlowLayout) convertView.findViewById(R.id.cvView_lotterydraw_number);

		LotteryDrawInfo item = (LotteryDrawInfo) getItem(position);
		mLotteryClassView.setText(mGameName==null?item.getGameName():mGameName);
		mLotteryTermNumView.setText(context.getString(R.string.record_tz_detail_qi, item.getIssueNo()));
		if (!StringUtil.isEmpty(item.getAwardDate())) {
			mLotteryDrawDateView.setText(item.getAwardDate().substring(0, 10));
		}
		
		if (item.getGameNo().equals(GlobalConstants.TC_JCZQ) || item.getGameNo().equals(GlobalConstants.TC_JCLQ)) {
			LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams1.setMargins(0,15,0,10);
			mLotteryDrawNumberView.setLayoutParams(layoutParams1);
			mLotteryDrawNumberView.removeAllViews();
			TextView numTv = new TextView(context);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			numTv.setLayoutParams(layoutParams);
			numTv.setTextSize(18);
			numTv.setGravity(Gravity.LEFT);
			numTv.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
			StringBuilder sb = new StringBuilder();
			if (!StringUtil.isEmpty(item.getWinCode())) {
				String[] text = item.getWinCode().split("\\_");
				sb.append(text[0]);
				sb.append("    ");
				sb.append(text[1]);
				sb.append("    ");
				sb.append(text[2]);
			}
			
			numTv.setText(sb.toString());
			
			mLotteryDrawNumberView.addView(numTv);
		} else {
			// 篮球
			String winbasecode = "";
			// 红球
			String winspecialcode = "";
			String[] jiCode = item.getWinCode().split("\\ ");
			if (jiCode != null && jiCode.length > 0) {
				winbasecode = jiCode[0];
			}
			if (jiCode != null && jiCode.length > 1) {
				winspecialcode = jiCode[1];
			}
			mLotteryDrawNumberView.removeAllViews();
			
			boolean showBg = false;
			switch (item.getGameNo()) {
			case GlobalConstants.TC_SF14:
			case GlobalConstants.TC_SF9:
			case GlobalConstants.TC_JQ4:
			case GlobalConstants.TC_BQ6:
				showBg = true;
				break;
	
			default:
				showBg = false;
				break;
			}
			
			LotteryShowUtil.showNumberLotteryView(context, 0, mLotteryDrawNumberView, winbasecode, showBg);
			LotteryShowUtil.showNumberLotteryView(context, 1, mLotteryDrawNumberView, winspecialcode, showBg);
		}
		return convertView;
	}

	@Override
	public int getItemResource() {
		return R.layout.item_lotterydraw_list;
	}

}
