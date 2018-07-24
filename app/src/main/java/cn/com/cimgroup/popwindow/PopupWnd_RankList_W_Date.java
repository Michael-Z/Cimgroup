package cn.com.cimgroup.popwindow;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.util.DensityUtils;
/**
 * 期次列表PopupWindow
 * @author 秋风
 *
 */
public class PopupWnd_RankList_W_Date {

	/** 上下文**/
	private Context mContext;
	private View mView;
	/** 期次信息的ListView**/
	private ListView lv_date_list;
	/** popupwindow的背景**/
	private LinearLayout mPopBg;
	/** 期次信息的数据**/
	private List<String> mList;
	/** 数据适配器**/
	private mAdapter mAdapter;
	/** popupwindow的基类**/
	private PopupWindow mPopupWindow;

	/****
	 * 构造方法
	 * @param context
	 * @param popBg
	 */
	public PopupWnd_RankList_W_Date(Context context, LinearLayout popBg) {

		this.mContext = context;
		this.mPopBg = popBg;
		mList = new ArrayList<String>();

		mView = View.inflate(mContext, R.layout.fragment_ranklist_w_date, null);

		lv_date_list = (ListView) mView.findViewById(R.id.lv_date_list);
		mAdapter = new mAdapter();
		lv_date_list.setAdapter(mAdapter);

		lv_date_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
//				String date = "";
//				if (mList != null || mList.size() > 0
//						&& mList.get(arg2) != null) {
//					date = mList.get(arg2);
//				}
				mListener.dateSelected(mList.get(arg2));
			}
		});
		
		//

		mPopupWindow = new PopupWindow(mView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, false);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		// bitmapDrawable不会影响你的背景
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置外部可以点击
		mPopupWindow.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击 
		// mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
		mPopupWindow.setFocusable(true);
		mPopupWindow.update();
		mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					hideWindow();
					return true;
				}
				return false;
			}
		});

	}

	/**
	 * 隐藏window
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-16
	 */
	public void hideWindow() {
		if (mPopupWindow.isShowing()) {
			mPopupWindow.dismiss();
		}
	}

	/**
	 * 显示window
	 * 
	 * @Description:
	 * @param down
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-16
	 */
	public void show2hideWindow(View down) {
		if (!mPopupWindow.isShowing()) {
			mPopupWindow.showAsDropDown(down);
		} else {
			hideWindow();
		}
	}

	/**
	 * 是否正在显示�??
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-17
	 */
	public Boolean isShowing() {
		return mPopupWindow.isShowing();
	}

	/**
	 * 初始化日期的list
	 * 
	 * @param lists
	 */
	public void setDateList(List<String> lists) {
		if (lists != null) {
			this.mList.clear();
		}
		this.mList.addAll(lists);
		mAdapter.notifyDataSetChanged();
		
		//如果mList条目大于5 则固定高度
		if(mList != null && mList.size() > 5){
			//期次信息大于5   那么只显示前五条 后边数据通过下拉查看
			ViewGroup.LayoutParams lp = lv_date_list.getLayoutParams();
			int height = DensityUtils.dip2px(105);
			lp.height = height;
			lv_date_list.setLayoutParams(lp);
			lv_date_list.requestLayout();
		}
	}

	/**
	 * 设置listView的adapter
	 * 
	 * @author leo
	 * 
	 */
	private class mAdapter extends BaseAdapter {

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
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			ViewHolder holder = null;

			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_rank_list_date, null);
				holder = new ViewHolder();
				holder.tv_spanner_text = (TextView) convertView
						.findViewById(R.id.tv_spanner_text);
				// holder.tv_spinner_arrow = (TextView)
				// convertView.findViewById(R.id.tv_spinner_arrow);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// 因为是使用之前的布局 �??以隐藏掉不需要的
			// holder.tv_spinner_arrow.setVisibility(View.GONE);

			holder.tv_spanner_text.setText("第" + mList.get(position) + "期");

			return convertView;
		}

	}

	class ViewHolder {

		/** 期次信息显示条**/
		private TextView tv_spanner_text;
		// private TextView tv_spinner_arrow;

	}

	public interface onDateSelecteListener {
		public void dateSelected(String date);
	}

	private onDateSelecteListener mListener;

	public void setOnDateSelectedListener(onDateSelecteListener listener) {
		this.mListener = listener;
	}

}
