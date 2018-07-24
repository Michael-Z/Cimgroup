package cn.com.cimgroup.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.TzListActivity.MCenterRecordTz;
import cn.com.cimgroup.bean.AllBetting;
import cn.com.cimgroup.bean.ScoreDetailGame;
import cn.com.cimgroup.bean.ScoreDetailOdds;
import cn.com.cimgroup.logic.CallBack;

public class ScoreAsiaAdapter extends SimpleBaseAdapter<ScoreDetailOdds> {
	
	public ScoreAsiaAdapter(Context context, MCenterRecordTz centerRecordTz) {
		super(context, null);
	}

	@Override
	public int getItemResource() {
		return R.layout.item_score_asis;
	}

	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
		
		TextView companyText = holder.getView(R.id.textView_score_asia_item_company);
		TextView chuS = holder.getView(R.id.textView_score_asia_item_chu_sheng);
		TextView chuP = holder.getView(R.id.textView_score_asia_item_chu_ping);
		TextView chuF = holder.getView(R.id.textView_score_asia_item_chu_fu);
		TextView jiS = holder.getView(R.id.textView_score_asia_item_ji_sheng);
		TextView jiP = holder.getView(R.id.textView_score_asia_item_ji_ping);
		TextView jiF = holder.getView(R.id.textView_score_asia_item_ji_fu);
		
		ScoreDetailOdds odds = mList.get(position);
		String company = odds.getCompany();
		String initOdds = odds.getInitOdds();
		String changeOdds = odds.getChangeOdds();
		
		companyText.setText(company);
		
		String[] initArr = initOdds.split("\\_");
		String[] changeArr = changeOdds.split("\\_");
		
		chuS.setText(initArr[0]);
		chuP.setText(initArr[1]);
		chuF.setText(initArr[2]);
		
//		jiP.setText(changeArr[1]);
		
		if(changeArr[0].indexOf("+") != -1){
			jiS.setText(changeArr[0].split("\\+")[0]);
			switch (changeArr[0].split("\\+")[1]) {
			case "-1":
				jiS.setTextColor(context.getResources().getColor(R.color.color_green_warm));
				jiS.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_down), null);
				break;
			case "0":
				jiS.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
				jiS.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				break;
			case "1":
				jiS.setTextColor(context.getResources().getColor(R.color.color_red));
				jiS.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_up), null);
				break;
			default:
				break;
			}
		} else {
			jiS.setText(changeArr[0]);
			jiS.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
			jiS.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}
		
		if(changeArr[1].indexOf("+") != -1){
			jiP.setText(changeArr[1].split("\\+")[0]);
			switch (changeArr[1].split("\\+")[1]) {
			case "-1":
				jiP.setTextColor(context.getResources().getColor(R.color.color_green_warm));
				jiP.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_down), null);
				break;
			case "0":
				jiP.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
				jiP.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				break;
			case "1":
				jiP.setTextColor(context.getResources().getColor(R.color.color_red));
				jiP.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_up), null);
				break;
			default:
				break;
			}
		} else {
			jiP.setText(changeArr[1]);
			jiP.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
			jiP.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}
		
		if(changeArr[2].indexOf("+") != -1){
			jiF.setText(changeArr[2].split("\\+")[0]);
			switch (changeArr[2].split("\\+")[1]) {
			case "-1":
				jiF.setTextColor(context.getResources().getColor(R.color.color_green_warm));
				jiF.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_down), null);
				break;
			case "0":
				jiF.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
				jiF.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				break;
			case "1":
				jiF.setTextColor(context.getResources().getColor(R.color.color_red));
				jiF.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_up), null);
				break;
			default:
				break;
			}
		} else {
			jiF.setText(changeArr[0]);
			jiF.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
			jiF.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
		}
		return convertView;
	}
}