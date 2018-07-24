package cn.com.cimgroup.popwindow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.ViewUtils;

/**
 * 竞彩足球 赛事选择
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-1-16
 */
public class PopWindowScoreMatchChoose implements OnClickListener{

	private Dialog mDialog;

	// 上下文
	private Context mContext;

	// 布局view
	private View mView;

	// 赛事选择选项
	private GridView mGridView;

	// 全选
	private LinearLayout btnView_football_match_choose_all;

	// 反选
	private LinearLayout btnView_football_match_choose_noall;

	private Integer[] selectButton = new Integer[] {
			R.id.btnView_football_match_choose_all,
			R.id.btnView_football_match_choose_noall };

	// 确定
	private TextView btnView_football_match_ok;

	private TextView btnView_football_match_cancel;

	// 赛事选择方式
	private List<Match> mMatchSelect = new ArrayList<Match>();
	/** 赛事名称 */
	private List<String> mMatchNameList = new ArrayList<String>();
	/** dialog宽度 */
	private int mWidth = 0;

	/** dialog 高度 */
	private int mHeight = 0;

	// 赛事选择适配器
	private PopWindowScoreMatchNameChooseAdapter mAdapter = new PopWindowScoreMatchNameChooseAdapter();

	// 选择的赛事
	private List<String> mMatchNames = new ArrayList<String>();

	/** 用户筛选条件的集和**/
	private List<String> mInitChoose = new ArrayList<String>();

	private onMatchChooseItemClick mOnItemClick;
	
	private LinearLayout layout_football_click;
	
	/** 弹出框的标题**/
	private TextView txtView_match_choose_txt;
	
	/**筛选框的类别  0：默认初始化   1：联赛筛选   2：期次筛选**/
	private int type = 0;
	
	/**反选按钮中间的圆形部分**/
	private View circle_right_inside;
	/**全选按钮中间的圆形部分**/
	private View circle_left_inside;
	/**全选/反选整体内容**/
	private LinearLayout linearlayout_my_radiogroup;

	/**
	 * 构造函数 
	 * @param context  上下文
	 * @param type  筛选框的类别   1：联赛筛选   2：期次筛选
	 */
	public PopWindowScoreMatchChoose(Context context,int type) {

		this.mContext = context;
		initView();
		initEven();
		
		this.type = type;

		if (type == 2) {
			//期次筛选 没有  "全选" "反选" "取消" "确定" 等按钮  点击期次进行跳转
			layout_football_click.setVisibility(View.GONE);
			txtView_match_choose_txt.setText("日期选择");
		}
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		mView = View.inflate(mContext,
				R.layout.cv_popwindow_football_match_choose, null);
		mGridView = (GridView) mView
				.findViewById(R.id.gridView_football_match_choose);
		btnView_football_match_choose_all = (LinearLayout) mView
				.findViewById(R.id.btnView_football_match_choose_all);
		btnView_football_match_choose_noall = (LinearLayout) mView
				.findViewById(R.id.btnView_football_match_choose_noall);
		btnView_football_match_ok = (TextView) mView
				.findViewById(R.id.btnView_football_match_ok);
		btnView_football_match_cancel = (TextView) mView
				.findViewById(R.id.btnView_football_match_cancel);
		layout_football_click = (LinearLayout) mView.findViewById(R.id.layout_football_click);
		txtView_match_choose_txt = (TextView) mView.findViewById(R.id.txtView_match_choose_txt);
		
		circle_left_inside = mView.findViewById(R.id.circle_left_inside);
		circle_right_inside = mView.findViewById(R.id.circle_right_inside);
		
		linearlayout_my_radiogroup = (LinearLayout) mView.findViewById(R.id.linearlayout_my_radiogroup);

		mGridView.setAdapter(mAdapter);
		mDialog = new Dialog(mContext,R.style.ScoreListDialog);
//		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		int screenSize[] = GlobalTools.getScreenSize((Activity) mContext);
		mWidth = (int) (screenSize[0] * 0.8);
		mHeight = (int) (screenSize[1] * 0.6);
		mDialog.setContentView(mView, new LayoutParams(mWidth, mHeight));
	}

	/**
	 * 绑定事件
	 */
	private void initEven() {
		btnView_football_match_choose_all.setOnClickListener(this);
		btnView_football_match_choose_noall.setOnClickListener(this);
		btnView_football_match_ok.setOnClickListener(this);
		btnView_football_match_cancel.setOnClickListener(this);
	}

	/**
	 * 显示类型选择
	 */
	public void showFootballDialog() {
		if (mInitChoose != null && mInitChoose.size() != 0) {
			linearlayout_my_radiogroup.setVisibility(View.VISIBLE);
			mMatchNames.clear();
			int size = mInitChoose.size();
			for (int i = 0; i < size; i++) {
				mMatchNames.add(mInitChoose.get(i));
			}
			mAdapter.notifyDataSetChanged();
			ViewUtils.setGridViewHeightBasedOnChildren(mGridView, 3);
//			mHeight = DensityUtils.dip2px(140)
//					+ mGridView.getLayoutParams().height;
			mDialog.setContentView(mView, new LayoutParams(mWidth, mHeight));
		}
		mDialog.show();
	}
	
	/**
	 * 带参的dialog显示 用于关注日期中期次信息的展示
	 * @param position  当前的日期位置
	 * @param type 1-比赛列表  2-关注
	 */
	public void showFootballDialog(int position, int type){
		if (mInitChoose != null && mInitChoose.size() != 0) {
			linearlayout_my_radiogroup.setVisibility(View.GONE);
			mMatchNames.clear();
			//比赛列表 - 期次默认为滑动到的位置
			if (type == 1) {
				position = mInitChoose.size() - position - 1;
			}
			mMatchNames.add(mInitChoose.get(position));
			mAdapter.notifyDataSetChanged();
			ViewUtils.setGridViewHeightBasedOnChildren(mGridView, 3);
			int minus_length = 0;
			if (type == 2) {
				//期次筛选的弹框要比联赛筛选的小35dp
				minus_length = DensityUtils.dip2px(35);
			}
			mHeight = DensityUtils.dip2px(140)
					+ mGridView.getLayoutParams().height
					- minus_length;
			mDialog.setContentView(mView, new LayoutParams(mWidth, mHeight));
		}
		mDialog.show();
	}

	public void hide() {
		mDialog.hide();
	}

	/**
	 * 更新数据  比分直播-联赛筛选
	 * 
	 * @param list
	 */
	public void setDatas(List<String> list) {
		mMatchNameList = list;
		mInitChoose.clear();
		int size = mMatchNameList.size();
		for (int i = 0; i < size; i++) {
			mInitChoose.add(mMatchNameList.get(i) + "");
		}
	}

	/**
	 * 注册回调
	 * 
	 * @Description:
	 * @param onItemClick
	 * @see:
	 * @since:
	 * @date:2015-1-16
	 */
	public void setOnCallBack(onMatchChooseItemClick onItemClick) {
		mOnItemClick = onItemClick;
	}

	/**
	 * 回调监听
	 * 
	 * @author 秋风
	 * 
	 */
	public interface onMatchChooseItemClick {
		public void onSelectItemClick(List<String> selectData, boolean flag);
	}

	private class PopWindowScoreMatchNameChooseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mMatchNameList.size();
		}

		@Override
		public Object getItem(int position) {
			return mMatchNameList.get(position);
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
			if (mMatchNames.contains(mMatchNameList.get(position))) {
				holder.mItemSelectView.setSelected(true);
				holder.id_choose_image.setVisibility(View.VISIBLE);
			} else {
				holder.mItemSelectView.setSelected(false);
				holder.id_choose_image.setVisibility(View.GONE);
			}
			String content = mMatchNameList.get(position);
			if (type == 1) {
				//联赛筛选
				if (content.length() > 4) {
					content = content.substring(0,4)+"...";
				}
			}
			
			holder.mItemSelectView.setTag(mMatchNameList.get(position));
			holder.mItemSelectView.setText(content);
			holder.mItemSelectView
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String matchName = (String) v.getTag();
							if (mMatchNames.contains(matchName)) {
								if (dateListener != null && type == 2) {
									dateListener.onDateSelected(position);
								}
								v.setSelected(false);
								mMatchNames.remove(matchName);
							} else {
								v.setSelected(true);
								mMatchNames.add(matchName);
								if (dateListener!= null) {
									dateListener.onDateSelected(position);
								}
							}
							notifyDataSetChanged();
						}
					});
			return view;
		}

		class ViewHolder {
			public TextView mItemSelectView;
			private ImageView id_choose_image;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 全选
		case R.id.btnView_football_match_choose_all:
			
			circle_left_inside.setBackgroundResource(R.drawable.shape_circle_red);
			circle_right_inside.setBackgroundResource(R.drawable.shape_circle_gray);
			
			if (mMatchNameList != null && mMatchNameList.size() != 0) {
				mMatchNames.clear();
				int size = mMatchNameList.size();
				for (int i = 0; i < size; i++) {
					mMatchNames.add(mMatchNameList.get(i));
				}
				mAdapter.notifyDataSetChanged();
			}
			break;
		// 反选
		case R.id.btnView_football_match_choose_noall:
			int size = mMatchNameList.size();
			
			circle_left_inside.setBackgroundResource(R.drawable.shape_circle_gray);
			circle_right_inside.setBackgroundResource(R.drawable.shape_circle_red);
			
			for (int i = 0; i < size; i++) {
				if (mMatchNames.contains(mMatchNameList.get(i)))
					mMatchNames.remove(mMatchNameList.get(i));
				else
					mMatchNames.add(mMatchNameList.get(i));
			}
			mAdapter.notifyDataSetChanged();
			break;
		// 确认
		case R.id.btnView_football_match_ok:
			if (mMatchNames.size()==0) {
				
			}else{
				mInitChoose.clear();
			}
			if (mMatchNames != null && mMatchNames.size() != 0) {
				int sizes = mMatchNames.size();
				for (int i = 0; i < sizes; i++) {
					mInitChoose.add(mMatchNames.get(i));
				}
			}
			mOnItemClick.onSelectItemClick(mMatchNames, mMatchNameList.size()==0 ? true:false);
			// 调用回调方法
			break;
		case R.id.btnView_football_match_cancel:
			hide();
			break;
		default:
			break;
		}
	}

	/**
	 * 用户筛选日期的接口
	 * @author 83920_000
	 *
	 */
	public interface onDateSelectedListener{
		void onDateSelected(int position);
	}
	
	private onDateSelectedListener dateListener;
	
	/**
	 * 注册筛选日期的接口回调
	 * @param listener
	 */
	public void setOnDateSelectedListener(onDateSelectedListener listener){
		this.dateListener = listener;
	}
}
