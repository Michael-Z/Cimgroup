package cn.com.cimgroup.popwindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;

/**
 * 投注记录选择标题的弹出框
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
@SuppressLint("ClickableViewAccessibility")
public class PopupWndSwitch_TzList extends PopupWindow {
	/** 数据源 */
	private List<Map<String, String>> mList = null;
	/** 默认选择tag */
	private int mDefaultTag;

	public void setDatas(List<Map<String, String>> list) {
		mList = list;
		mAdapter.notifyDataSetChanged();
	}

	/** 选择的彩种编号 */
	private int mChooseIndex;

	/** 筛选适配器 */
	private MyGridViewAdapter mAdapter = null;

	private PopupWindow mPopupWindow;

	// 上下文
	private Context mContext;

	// 布局view
	private View mView;

	// 竞彩方式选项
	private GridView mGridView;
	// 页面回调
	private OnTypeItemClickListener mListener;

	// 默认选中
	private int mSelectIndex = 0;

	private LinearLayout mLinearLayout;

	public PopupWndSwitch_TzList(Context context, LinearLayout linearLayout,
			int tag, List<Map<String, String>> list) {

		mLinearLayout = linearLayout;
		this.mContext = context;
		mDefaultTag = tag;
		initDefaultTag(mDefaultTag);
		mList = list;
		mView = View
				.inflate(mContext, R.layout.cv_popwindow_football_way, null);
		mGridView = (GridView) mView.findViewById(R.id.gridView_jczq_select);
		mAdapter = new MyGridViewAdapter();
		mGridView.setAdapter(mAdapter);
		mPopupWindow = new PopupWindow(mView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, false);
		mPopupWindow.setBackgroundDrawable(new PaintDrawable());
		// 设置点击窗口外边窗口消失 
		mPopupWindow.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击 
		mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
		mPopupWindow.setFocusable(true);
		mPopupWindow.update();
		mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
					mPopupWindow.dismiss();
					return true;
				}
				return false;
			}
		});
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (mLinearLayout != null) {
					mLinearLayout.setVisibility(View.GONE);
				}
			}
		});
	}

	/**
	 * 初始化
	 * 
	 * @param tag
	 */
	private void initDefaultTag(int tag) {
		if (mList != null && mList.size() != 0) {
			int size = mList.size();
			for (int i = 0; i < size; i++) {
				if (tag == Integer.parseInt(mList.get(i).get("tag"))) {
					mChooseIndex = i;
				}
			}
		}
	}

	/**
	 * 显示/隐藏PopupWindow
	 * 
	 * @Description:
	 * @param down
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-16
	 */
	public void showOrhideWindow(View down) {
		if (mPopupWindow != null) {
			if (mPopupWindow.isShowing()) {
				mPopupWindow.dismiss();
			} else {
				mPopupWindow.showAsDropDown(down);
			}
		}

	}

	/**
	 * 初始化PopupWindow数据
	 * 
	 * @param list
	 */
	public void setData(List<Map<String, String>> list) {
		mSelectIndex = 0;
		mList = list == null ? new ArrayList<Map<String, String>>() : list;
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 初始化PopupWindow数据(带默认)
	 * 
	 * @param list
	 * @param selectIndex
	 */
	public void setData(List<Map<String, String>> list, int selectIndex) {
		this.mSelectIndex = selectIndex;
		mList = list == null ? new ArrayList<Map<String, String>>() : list;
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 注册监听
	 * 
	 * @param listener
	 */
	public void setOnTypeItemClickListener(OnTypeItemClickListener listener) {
		mListener = listener;
	}

	/**
	 * PopupWindow的点击事件
	 * 
	 * @author 秋风
	 * 
	 */
	public interface OnTypeItemClickListener {
		/** 监听回调方法 */
		public void onSelectItemClick(int tag, int position);
	}

	/**
	 * 筛选器Adapter
	 * 
	 * @author 秋风
	 * 
	 */
	private class MyGridViewAdapter extends BaseAdapter {

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
		public View getView(final int position, View view, ViewGroup parent) {
			ViewHolder holder = null;
			if (view == null) {
				view = View.inflate(mContext, R.layout.item_cv_popwindow_text,
						null);
				holder = new ViewHolder();
				holder.mItemSelectView = (TextView) view
						.findViewById(R.id.btnView_football_itemselect);
				holder.id_choose_image = (ImageView) view
						.findViewById(R.id.id_choose_image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			if (mChooseIndex == position) {
				holder.mItemSelectView.setSelected(true);
				holder.id_choose_image.setVisibility(View.VISIBLE);
			} else {
				holder.mItemSelectView.setSelected(false);
				holder.id_choose_image.setVisibility(View.GONE);
			}
			holder.mItemSelectView.setTag(mList.get(position));
			holder.mItemSelectView.setText(mList.get(position).get("name"));
			holder.mItemSelectView
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mChooseIndex = position;
							notifyDataSetChanged();
							mPopupWindow.dismiss();
							mListener.onSelectItemClick(Integer.parseInt(mList
									.get(mChooseIndex).get("tag")),
									mChooseIndex);
						}
					});
			return view;
		}

		class ViewHolder {
			public TextView mItemSelectView;
			private ImageView id_choose_image;
		}

	}

}
