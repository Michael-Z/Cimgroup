package cn.com.cimgroup.popwindow;

import java.util.ArrayList;
import java.util.List;

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
 * 竞彩足球投注方式选择popWindow
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-1-16
 */
public class PopWindowFootballLotteryWay {

	private PopupWindow mPopupWindow;

	// 上下文
	private Context mContext;

	// 布局view
	private View mView;

	// 竞彩方式选项
	private GridView mGridView;

	// 选择方式
	private List<String[]> mMatchSelect = new ArrayList<String[]>();

	// 竞彩方式适配器
	private PopWindowZqjcAdapter mAdapter = new PopWindowZqjcAdapter();

	// 页面回调
	private onItemClick mCallBack;
	
	//默认选中
	private int mSelectIndex=0;
	
	private LinearLayout mPopBg;
	
	/**头部标题内容**/
	private TextView textview_title;

	public PopWindowFootballLotteryWay(Context context, LinearLayout popBg) {

		mPopBg = popBg;
		this.mContext = context;
		mView = View.inflate(mContext, R.layout.cv_popwindow_football_way, null);
		textview_title = (TextView) mView.findViewById(R.id.textview_title);
		mGridView = (GridView) mView.findViewById(R.id.gridView_jczq_select);
		mGridView.setAdapter(mAdapter);
		mPopupWindow = new PopupWindow(mView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, false);
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
					 hideWindow();
					 return true;
				 }
				return false;
			}
		});
		mPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				if (mPopBg != null) {
					mPopBg.setVisibility(View.GONE);
				}
			}
		});
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
		}else{
			hideWindow();
		}
	}

	/**
	 * 是否正在显示中
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2015-1-17
	 */
	public Boolean isShowing(){
		return mPopupWindow.isShowing();
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
	 * 赋值竞彩方式
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-16
	 */
	public void setData(List<String[]> matchSelect) {
		if (matchSelect == null) {
			this.mMatchSelect.clear();
		}
		this.mMatchSelect.addAll(matchSelect);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 赋值竞彩方式
	 * @Description:
	 * @param matchSelect
	 * @param selectIndex 默认选中的索引
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2015-1-17
	 */
	public void setData(List<String[]> matchSelect,int selectIndex) {
		this.mSelectIndex=selectIndex;
		if (matchSelect == null) {
			this.mMatchSelect.clear();
		}
		this.mMatchSelect.addAll(matchSelect);
		mAdapter.notifyDataSetChanged();
	}
	/**
	 * 注册回调
	 * 
	 * @Description:
	 * @param onItemClick
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-16
	 */
	public void addCallBack(onItemClick onItemClick) {
		mCallBack = onItemClick;
	}

	/**
	 * 获取所有竞彩方式
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-16
	 */
	public List<String[]> getData() {
		return this.mMatchSelect;
	}

	/**
	 * 回调
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @see:
	 * @since:
	 * @copyright www.wenchuang.com
	 * @Date:2015-1-16
	 */
	public interface onItemClick {

		/**
		 * 
		 * @Description:
		 * @param selectData
		 *            选择的内容
		 * @param position
		 *            选择的索引
		 * @see:
		 * @since:
		 * @author:www.wenchuang.com
		 * @date:2015-1-16
		 */
		public void onSelectItemClick(String[] selectData, int position);
	}

	private class PopWindowZqjcAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return getData().size();
		}

		@Override
		public Object getItem(int position) {
			return mMatchSelect.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.item_cv_popwindow_football, null);
				holder = new ViewHolder();
				holder.mItemSelectView = (TextView) convertView.findViewById(R.id.btnView_football_itemselect);
				holder.mItemSelectImageView = (ImageView) convertView.findViewById(R.id.id_choose_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(mSelectIndex==position){
				holder.mItemSelectView.setSelected(true);
				holder.mItemSelectImageView.setVisibility(View.VISIBLE);
			}else{
				holder.mItemSelectView.setSelected(false);
				holder.mItemSelectImageView.setVisibility(View.GONE);
			}
			holder.mItemSelectView.setTag(position);
			holder.mItemSelectView.setText(getData().get(position)[1]);
			holder.mItemSelectView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mCallBack != null) {
						int index = (Integer) v.getTag();
						mSelectIndex=index;
						mCallBack.onSelectItemClick(getData().get(index), index);
					}
					hideWindow();
				}
			});
			return convertView;
		}

		class ViewHolder {

			public TextView mItemSelectView;
			
			public ImageView mItemSelectImageView;
		}

	}
	
	/**
	 * 设置头部标题提示内容
	 * @param title
	 */
	public void setTitleText(String title){
		textview_title.setText(title);
	}
}
