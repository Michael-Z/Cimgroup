package cn.com.cimgroup.popwindow;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import cn.com.cimgroup.R;

/**
 * 比分直播类型选择
 * 
 * @author 秋风
 * 
 */
@SuppressLint("ClickableViewAccessibility")
public class PopupWndScoreType {
	private PopupWindow mPopupWindow;

	// 上下文
	private Context mContext;

	// 布局view
	private View mView;

	// 竞彩方式选项
	private GridView mGridView;

	// 选择方式
	private List<String> mMatchSelect = new ArrayList<String>();

	// 竞彩方式适配器
	private PopWindowZqjcAdapter mAdapter = new PopWindowZqjcAdapter();

	// 页面回调
	private OnTypeItemClickListener mListener;

	// 默认选中
	private int mSelectIndex = 0;

	private LinearLayout mLinearLayout;
	
	/**标题提示文字**/
	private TextView textview_title;

	public PopupWndScoreType(Context context, LinearLayout linearLayout) {

		mLinearLayout = linearLayout;
		this.mContext = context;
		mView = View
				.inflate(mContext, R.layout.cv_popwindow_football_way, null);
		textview_title = (TextView) mView.findViewById(R.id.textview_title);
		mGridView = (GridView) mView.findViewById(R.id.gridView_jczq_select);
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
	public void setData(List<String> list) {
		mMatchSelect = list == null ? new ArrayList<String>() : list;
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 初始化PopupWindow数据(带默认)
	 * 
	 * @param list
	 * @param selectIndex
	 */
	public void setData(List<String> list, int selectIndex) {
		this.mSelectIndex = selectIndex;
		mMatchSelect = list == null ? new ArrayList<String>() : list;
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
		public void onSelectItemClick(int position);
	}

	/**
	 * popupWindow适配器
	 * 
	 * @author 秋风
	 * 
	 */
	private class PopWindowZqjcAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMatchSelect.size();
		}

		@Override
		public Object getItem(int position) {
			return mMatchSelect.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_cv_popwindow_text, null);
				holder = new ViewHolder();
				holder.id_choose_text = (TextView) convertView
						.findViewById(R.id.btnView_football_itemselect);
				holder.id_choose_image = (ImageView) convertView
						.findViewById(R.id.id_choose_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (mSelectIndex == position) {
				holder.id_choose_text.setSelected(true);
				holder.id_choose_image.setVisibility(View.VISIBLE);
			} else {
				holder.id_choose_text.setSelected(false);
				holder.id_choose_image.setVisibility(View.GONE);
			}
			holder.id_choose_text.setTag(position);
			holder.id_choose_text.setText(mMatchSelect.get(position));
			holder.id_choose_text
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							if (mListener != null) {
								int index = (Integer) v.getTag();
								mSelectIndex = index;
								mListener.onSelectItemClick(index);
							}
							if (mPopupWindow != null) {
								mPopupWindow.dismiss();
							}

						}
					});
			return convertView;
		}

		class ViewHolder {

			TextView id_choose_text;
			ImageView id_choose_image;
		}

	}
	
	/**
	 * 设置标题内容文字
	 */
	public void setTitleText(String text){
		textview_title.setText(text);
	}
}
