package cn.com.cimgroup.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.MessageObj;
import cn.com.cimgroup.xutils.DateUtil;

/**
 * Created by small on 15/12/29. 站内信;
 */
public class MessageInsideAdapter extends SimpleBaseAdapter<MessageObj> {

	/** 信息详情的条目高度 **/
	private int itemHeight;
	/** 信息详情的条目宽度 **/
	private int itemWidth;

	public onContentViewVisiableListener mListener;

	public MessageInsideAdapter(Context context, List data) {
		super(context, data);
	}

	@Override
	public int getItemResource() {
		return R.layout.item_message;
	}

	@Override
	public View getItemView(final int position, View convertView,
			ViewHolder holder) {

		ImageView markView = (ImageView) holder.getView(R.id.message_state);
		// final TextView contentView = (TextView)
		// holder.getView(R.id.message_content);
		TextView contentView = (TextView) holder.getView(R.id.message_content);
		TextView tiemView = (TextView) holder.getView(R.id.message_time);
		TextView titleView = holder.getView(R.id.message_des);
		TextView message_explain = holder.getView(R.id.message_explain);
		ImageView message_arrow = holder.getView(R.id.message_arrow);

		// 消息详细内容的点击事件，不做处理，就可拦截点击事件
		contentView.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

			}
		});

		// ViewTreeObserver vto = contentView.getViewTreeObserver();
		// vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
		//
		// @Override
		// public boolean onPreDraw() {
		//
		// itemHeight = contentView.getMeasuredHeight();
		// itemWidth = contentView.getMeasuredWidth();
		//
		// mListener.viewVisiable(itemHeight, itemWidth, position);
		//
		// return true;
		// }
		// });

		MessageObj item = mList.get(position);

		// if(data){
		//
		// }

		titleView.setText(item.titile);
		contentView.setText(item.content);
		// 2.5期新增判断 需要将时间显示区分当天和非当天
//		 tiemView.setText(item.timeQuJian);
		if (!TextUtils.isEmpty(item.timeQuJian)) {
			String message_time = item.timeQuJian;
			boolean flag = DateUtil.isDateToday(DateUtil.ConverToDate(message_time));
			String content = null;
			if (flag) {
				content = "今天" + " " + message_time.substring(10,message_time.length()-3);
			}else {
				content = message_time.substring(2,message_time.length()-3);
			}
			tiemView.setText(content);
		}

		// 2.5期新增下拉内容 预览显示
		message_explain.setText(item.content);

		if (item.isRead.equals("0")) {
			// markView.setVisibility(View.VISIBLE);
			markView.setImageResource(R.drawable.horn_red);
		} else {
			// markView.setVisibility(View.INVISIBLE);
			markView.setImageResource(R.drawable.horn_gray);
		}

		if (item.isShowDetail) {
			contentView.setVisibility(View.VISIBLE);
			// 将箭头替换为向下箭头
			message_arrow.setImageResource(R.drawable.icon_center_right_pull);
		} else {
			contentView.setVisibility(View.GONE);
			// 将箭头替换为向右箭头
			message_arrow.setImageResource(R.drawable.icon_center_right);
		}
		return convertView;
	}

	/** 站内信详情可见后 获取其宽高 **/
	public interface onContentViewVisiableListener {
		public void viewVisiable(int height, int width, int position);
	}

	// 初始化站内信详情接口
	public void setOnContentViewVisiableListener(
			onContentViewVisiableListener listener) {
		this.mListener = listener;
	}
}
