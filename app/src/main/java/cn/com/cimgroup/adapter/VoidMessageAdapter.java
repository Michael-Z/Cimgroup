package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.bean.MessageObj;

public class VoidMessageAdapter extends BaseAdapter {

	protected Context context;
	protected List<MessageObj> mList;

	public VoidMessageAdapter(Context context, List<MessageObj> data) {
		this.context = context;
		this.mList = data == null ? new ArrayList<MessageObj>() : new ArrayList<MessageObj>(data);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		if (position >= mList.size())
			return null;
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		// 消息为空时的加载流程
		if (mList.size() == 1&& mList.get(0).titile.equals("void_message_wushiqiu")) {
			if (convertView == null) {
				view = View.inflate(context, R.layout.fragment_message_void_sign, null);
				holder = new ViewHolder();
				holder.tv_no_data_message = (TextView) view.findViewById(R.id.tv_no_data_message);
				holder.tv_no_data_lemi = (TextView) view.findViewById(R.id.tv_no_data_lemi);
				view.setTag(holder);
			}else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_no_data_lemi.setVisibility(View.GONE);
			holder.tv_no_data_message.setText("暂时没有消息");
		}else if(mList.size() == 1&& mList.get(0).titile.equals("void_act_wushiqiu")){
			if (convertView == null) {
				view = View.inflate(context, R.layout.fragment_message_void_sign, null);
				holder = new ViewHolder();
				holder.tv_no_data_message = (TextView) view.findViewById(R.id.tv_no_data_message);
				holder.tv_no_data_lemi = (TextView) view.findViewById(R.id.tv_no_data_lemi);
				view.setTag(holder);
			}else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_no_data_lemi.setVisibility(View.GONE);
			holder.tv_no_data_message.setText("暂时没有活动");
		}else{
			// 有消息的加载流程
			if (convertView == null) {
				view = View.inflate(context, R.layout.item_message, null);
				holder = new ViewHolder();
				holder.markView = (ImageView) view.findViewById(R.id.message_state);
				holder.contentView = (TextView) view.findViewById(R.id.message_content);
				holder.tiemView = (TextView) view.findViewById(R.id.message_time);
				holder.titleView = (TextView) view.findViewById(R.id.message_des);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			// 消息详细内容的点击事件，不做处理，就可拦截点击事件
			holder.contentView.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {

				}
			});

			MessageObj item = mList.get(position);

			holder.titleView.setText(item.titile);
			holder.contentView.setText(item.content);
			holder.tiemView.setText(item.timeQuJian);

			if (item.isRead.equals("0")) {
				holder.markView.setVisibility(View.VISIBLE);
			} else {
				holder.markView.setVisibility(View.INVISIBLE);
			}

			if (item.isShowDetail) {
				holder.contentView.setVisibility(View.VISIBLE);
			} else {
				holder.contentView.setVisibility(View.GONE);
			}
		}
		return view;
	}

	public class ViewHolder {
		ImageView markView;
		TextView contentView;
		TextView tiemView;
		TextView titleView;
		TextView tv_no_data_lemi;
		TextView tv_no_data_message;
	}

	public void addAll(List<MessageObj> elem) {
		mList.addAll(elem);
		notifyDataSetChanged();
	}

	public void remove(MessageObj elem) {
		mList.remove(elem);
		notifyDataSetChanged();
	}

	public void remove(int index) {
		mList.remove(index);
		notifyDataSetChanged();
	}

	public void replaceAll(List<MessageObj> elem) {
		mList.clear();
		mList.addAll(elem);
		notifyDataSetChanged();
	}

	public List<MessageObj> getData() {
		return mList;
	}

}
