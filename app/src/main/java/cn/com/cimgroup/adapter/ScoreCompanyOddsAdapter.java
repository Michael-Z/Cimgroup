package cn.com.cimgroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.TzListActivity.MCenterRecordTz;
import cn.com.cimgroup.bean.AllBetting;
import cn.com.cimgroup.bean.ScoreDetailGame;
import cn.com.cimgroup.bean.ScoreDetailOdds;
import cn.com.cimgroup.bean.TheOdds;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.xutils.XLog;

public class ScoreCompanyOddsAdapter extends SimpleBaseAdapter<TheOdds> {
	
	private List<TheOdds> mList;
	
	public ScoreCompanyOddsAdapter(Context context, List<TheOdds> list) {
		super(context, null);
		mList = list;
	}

	@Override
	public int getItemResource() {
		return R.layout.item_score_company_odds;
	}

	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
//		LinearLayout asiaAdd =  holder.getView(R.id.layout_score_asia_add);
//		TextView itemS = holder.getView(R.id.textView_score_odds_item_s);
//		TextView itemP = holder.getView(R.id.textView_score_odds_item_p);
//		TextView itemF = holder.getView(R.id.textView_score_odds_item_f);
//		TextView itemTime = holder.getView(R.id.textView_score_odds_item_time);
//		 
//		TheOdds odds = data.get(position);
		XLog.i("================================================");
		return convertView;
	}
}