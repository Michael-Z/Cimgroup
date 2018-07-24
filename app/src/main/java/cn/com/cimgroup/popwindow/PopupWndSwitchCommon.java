package cn.com.cimgroup.popwindow;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PopupWndSwitchCommon extends PopWnd {

	PlayMenuItemClick mPlayMenuItemClick;
	ListView tzList;
	List<String> mList;

	private MyAdapter myAdapter;

	/**
	 * 构造方法<List<String>>
	 * 
	 * @param ctx
	 * @param list
	 * @param itemClick
	 */
	public PopupWndSwitchCommon(Context ctx, List<String> list,
			PlayMenuItemClick itemClick) {
		super(ctx, 0, null, null, null);
		if (itemClick != null) {
			mPlayMenuItemClick = itemClick;
		}
		mList = list;
		initView(view);
	}
	/**
	 * 构造方法<List<String>>
	 * 
	 * @param ctx
	 * @param list
	 * @param itemClick
	 */
	public PopupWndSwitchCommon(Context ctx, List<String> list,
			boolean isShowUp,PlayMenuItemClick itemClick) {
		super(ctx, 0, null, null, null);
		if (itemClick != null) {
			mPlayMenuItemClick = itemClick;
		}
		mList = list;
		initView(view);
	}

	/**
	 * 构造方法<Array>
	 * 
	 * @param ctx
	 * @param strArr
	 * @param itemClick
	 */
	public PopupWndSwitchCommon(Context ctx, String[] strArr,
			PlayMenuItemClick itemClick) {
		super(ctx, 0, null, null, null);
		if (itemClick != null) {
			mPlayMenuItemClick = itemClick;
		}
		mList=new ArrayList<String>();
		getList(strArr);
		initView(view);
	}

	private void getList(String[] strArr) {
		if (strArr == null) {
			return;
		}
		for (int i = 0; i < strArr.length; i++) {
			mList.add(strArr[i]);
		}
	}

	public PopupWndSwitchCommon(Context ctx, int tag, Handler mHandlerEx,
			LinearLayout popBg, TextView switchText) {
		super(ctx, tag, mHandlerEx, popBg, switchText);
	}

	@Override
	protected boolean isWrapContent() {
		return true;
	}

	public interface PlayMenuItemClick {

		public void PopTzList(View v);
	}

	private void initView(View view) {
		tzList = (ListView) view.findViewById(R.id.pop_tzlist);
		myAdapter = new MyAdapter(context);
		tzList.setAdapter(myAdapter);

	}

	/**
	 * 修改显示数据<Array>
	 * 
	 * @param strArr
	 */
	public void setDatas(String[] strArr) {
		mList.clear();
		getList(strArr);
		if (myAdapter != null) {
			myAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 修改显示数据<List>
	 * 
	 * @param list
	 */
	public void setDatas(List<String> list) {
		mList = list;
		if (myAdapter != null) {
			myAdapter.notifyDataSetChanged();
		}
	}

	private class MyAdapter extends BaseAdapter {
		private LayoutInflater mInflater;// 得到一个LayoutInfalter对象用来导入布局 /*构造函数*/

		public MyAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			// 观察convertView随ListView滚动情况

			if (convertView == null) {
				convertView = mInflater.inflate(
						R.layout.item_pop_switch_common, null);
				holder = new ViewHolder();
				/* 得到各个控件的对象 */

				holder.title = (TextView) convertView
						.findViewById(R.id.pop_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();// 取出ViewHolder对象
			}
			holder.title.setText(mList.get(position));
			holder.title.setTag(mList.get(position));
			holder.title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mPlayMenuItemClick.PopTzList(v);
					dissmiss();
				}
			});

			return convertView;
		}
	}

	public class ViewHolder {
		public TextView title;
	}

	@Override
	public int getLayoutResource() {
		return R.layout.layout_pop_switch_common;
	}

	@Override
	public int getMessageWhat() {
		return GlobalConstants.MSG_NOTIFY_SWITCH_COMMON;
	}

}
