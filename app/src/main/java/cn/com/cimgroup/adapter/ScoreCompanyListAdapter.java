package cn.com.cimgroup.adapter;

import android.content.Context;
import android.view.View;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.TzListActivity.MCenterRecordTz;
import cn.com.cimgroup.bean.Company;

public class ScoreCompanyListAdapter extends SimpleBaseAdapter<Company> {
	
	public ScoreCompanyListAdapter(Context context, MCenterRecordTz centerRecordTz) {
		super(context, null);
	}

	@Override
	public int getItemResource() {
		return R.layout.frament_score_asia;
	}

	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
//		LinearLayout asiaAdd =  holder.getView(R.id.layout_score_asia_add);
		
		
		Company odds = mList.get(position);
		
		return convertView;
	}
}
