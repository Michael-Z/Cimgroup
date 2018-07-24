package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Sign;
import cn.com.cimgroup.xutils.DateUtil;

public class SignAdapter extends SimpleBaseAdapter<Sign> {
	
	private List<Sign> items = new ArrayList<Sign>();

	public SignAdapter(Context context, List<Sign> data) {
		super(context, data);
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getItemResource() {
		// TODO Auto-generated method stub
		return R.layout.item_sign;
	}

	@SuppressLint("NewApi")
	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
		// TODO Auto-generated method stub
		ImageView image = (ImageView) holder.getView(R.id.imageView_sign_image);
		TextView text = (TextView) holder.getView(R.id.imageView_sign_text);
		
		Sign sign = (Sign) getItem(position);
		text.setText(sign.getDay());
		if (sign.isSelect()) {
			image.setVisibility(View.VISIBLE);
//			convertView.setBackgroundColor(context.getResources().getColor(R.color.color_grey_bg));
		} else {
			image.setVisibility(View.INVISIBLE);
//			convertView.setBackground(context.getResources().getDrawable(R.drawable.transparent));
		}
		if (items.size() > 0) {
			int week = DateUtil.getWeekDayNum(DateUtil.getToYear() + "-" + DateUtil.getToMonth() + "-01");
			int num = 0;
			switch (week) {
			case 0:
				num = 6;
				break;
			case 1:
				num = 0;
				break;
			case 2:
				num = 1;
				break;
			case 3:
				num = 2;
				break;
			case 4:
				num = 3;
				break;
			case 5:
				num = 4;
				break;
			case 6:
				num = 5;
				break;
			default:
				break;
			}
			for (Sign sign2 : items) {
				if (position >= num) {
					if ((position - num + 1) == Integer.parseInt(sign2.getDay())) {
						sign.setSelect(true);
						image.setVisibility(View.VISIBLE);
//						convertView.setBackgroundColor(context.getResources().getColor(R.color.color_grey_bg));
					}
				}
				
			}
			
		}
		
		return convertView;
	}
	
	public void setData(List<Sign> lists) {
		items = lists;
		notifyDataSetChanged();
	}
	
	
	public List<Sign> getData() {
		return items;
	}
}
