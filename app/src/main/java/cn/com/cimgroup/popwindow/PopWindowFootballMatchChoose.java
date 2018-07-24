package cn.com.cimgroup.popwindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.xutils.ToastUtil;

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
public class PopWindowFootballMatchChoose implements OnClickListener {

	private Dialog mDialog;

	// 上下文
	private Context mContext;

	// 布局view
	private View mView;

	// 赛事选择选项
	private GridView mGridView;

	// 全选
	private LinearLayout allSelectView;

	// 反选
	private LinearLayout allOtherSelectView;

	private Integer[] selectButton = new Integer[] {
			R.id.btnView_football_match_choose_all,
			R.id.btnView_football_match_choose_noall };

	// 确定
	private TextView okView;

	private TextView cancelView;

	// 赛事选择方式
	private List<Match> mMatchSelect = new ArrayList<Match>();

	// 赛事选择适配器
	private PopWindowFootballMatchChooseAdapter mAdapter = new PopWindowFootballMatchChooseAdapter();

	/** 选择的赛事 */
	private List<String> mSelectMatchChoose = new ArrayList<String>();
	/** 时时选择的赛事 */
	private List<String> mSureSelectMatchChoose = new ArrayList<String>();
	/** 每次选择后的结果 */
	private List<String> mInitMatchChoose = new ArrayList<String>();

	// 从哪个按钮点击的 TODO 全选、反选 五大联赛
	private int mSelectButton = 0;

	private onMatchChooseItemClick mOnItemClick;
	
	/**反选按钮中间的圆形部分**/
	private View circle_right_inside;
	/**全选按钮中间的圆形部分**/
	private View circle_left_inside;

	public PopWindowFootballMatchChoose(Context context) {

		this.mContext = context;
		initView();
		initEven();

	}

	/**
	 * 初始化VIEW
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-22
	 */
	private void initView() {
		mView = View.inflate(mContext,
				R.layout.cv_popwindow_football_match_choose, null);
		mGridView = (GridView) mView
				.findViewById(R.id.gridView_football_match_choose);
		allSelectView = (LinearLayout) mView
				.findViewById(R.id.btnView_football_match_choose_all);
		allOtherSelectView = (LinearLayout) mView
				.findViewById(R.id.btnView_football_match_choose_noall);
		okView = (TextView) mView.findViewById(R.id.btnView_football_match_ok);
		cancelView = (TextView) mView
				.findViewById(R.id.btnView_football_match_cancel);
		mGridView.setAdapter(mAdapter);
		circle_right_inside = mView.findViewById(R.id.circle_right_inside);
		circle_left_inside = mView.findViewById(R.id.circle_left_inside);
		// int screenSize[] = GlobalTools.getScreenSize((Activity) mContext);
		// int width = (int)(screenSize[0] * 0.9);
		// int height = (int)(screenSize[1] * 0.6);
		// mPopupWindow = new PopupWindow(mView, width, height, true);
		// mPopupWindow.setBackgroundDrawable(new PaintDrawable());
		// // 设置点击窗口外边窗口消失 
		// mPopupWindow.setOutsideTouchable(true);
		// // 设置此参数获得焦点，否则无法点击 
		// mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
		// mPopupWindow.update();
		// mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
		// return true;
		// }
		// return false;
		// }
		// });

		mDialog = new Dialog(mContext);
		mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		int screenSize[] = GlobalTools.getScreenSize((Activity) mContext);
		int width = (int) (screenSize[0] * 0.8);
		int height = (int) (screenSize[1] * 0.6);
		mDialog.setContentView(mView, new LayoutParams(width, height));
	}

	/**
	 * 初始化监听
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-22
	 */
	private void initEven() {
		allSelectView.setOnClickListener(this);
		allOtherSelectView.setOnClickListener(this);
		okView.setOnClickListener(this);
		cancelView.setOnClickListener(this);
		// 篮球没有五大联赛
		// if(mContext instanceof LotteryBasketballActivity){
		// fiveMatchView.setVisibility(View.GONE);
		// }
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
	// public void show2hideWindow(View down) {
	// if (!mPopupWindow.isShowing()) {
	// mPopupWindow.showAsDropDown(down);
	// } else {
	// hideWindow();
	// }
	// }
	//
	// public void showAtLocation(int gravity) {
	// if (!mPopupWindow.isShowing()) {
	// for (Match match : mMatchSelect) {
	// mSelectMatchChoose.add(match.getLeagueShort());
	// }
	// mAdapter.notifyDataSetChanged();
	// mPopupWindow.showAtLocation(mView, gravity, 0, 0);
	// } else {
	// hideWindow();
	// }
	// }

	public void showFootballDialog() {
		mDialog.show();
	}

	public void hide() {
		mDialog.hide();
	}

	/**
	 * 是否正在显示中
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-17
	 */
	// public Boolean isShowing() {
	// return mPopupWindow.isShowing();
	// }

	/**
	 * 隐藏window
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-16
	 */
	// public void hideWindow() {
	// if (mPopupWindow.isShowing()) {
	// mPopupWindow.dismiss();
	// }
	// }

	/**
	 * 获取所有的赛事。根据赛事名称去重
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-16
	 */
	public void setData(List<List<Match>> matchSelect) {

		if (this.mMatchSelect != null) {
			// 清空结果集
			this.mMatchSelect.clear();
			// 清空选择的赛事集合
			mSelectMatchChoose.clear();
			mInitMatchChoose.clear();
			mSureSelectMatchChoose.clear();
		}
		// 进行赛事去重处理
		Map<String, Integer> tempMatchKey = new HashMap<String, Integer>();
		if (matchSelect != null) {
			for (int i = 0; i < matchSelect.size(); i++) {
				List<Match> matchs = matchSelect.get(i);
				for (int j = 0; j < matchs.size(); j++) {
					Match match = matchs.get(j);
					if (!tempMatchKey.containsKey(match.getLeagueShort())) {
						tempMatchKey.put(match.getLeagueShort(), 1);
						// 默认显示所有的赛事
						mSelectMatchChoose.add(match.getLeagueShort());
						mSureSelectMatchChoose.add(match.getLeagueShort());
						mInitMatchChoose.add(match.getLeagueShort());
						mMatchSelect.add(match);
					}
				}
			}
		}
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
	public void addCallBack(onMatchChooseItemClick onItemClick) {
		mOnItemClick = onItemClick;
	}

	/**
	 * 获取所有赛事
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-16
	 */
	public List<Match> getData() {
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
	public interface onMatchChooseItemClick {

		/**
		 * 
		 * @Description:
		 * @param selectData
		 *            选择的内容
		 * @see:
		 * @since:
		 * @author:www.wenchuang.com
		 * @date:2015-1-16
		 */
		public void onSelectItemClick(View view, List<String> selectData);
	}

	/**
	 * 赛事显示adapter
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @see:
	 * @since:
	 * @copyright www.wenchuang.com
	 * @Date:2015-1-22
	 */
	private class PopWindowFootballMatchChooseAdapter extends BaseAdapter {

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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_cv_popwindow_football, null);
				holder = new ViewHolder();
				holder.mItemSelectView = (TextView) convertView
						.findViewById(R.id.btnView_football_itemselect);
				holder.mItemSelectImageView = (ImageView) convertView
						.findViewById(R.id.id_choose_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (mSureSelectMatchChoose.contains(getData().get(position)
					.getLeagueShort())) {
				holder.mItemSelectView.setSelected(true);
				holder.mItemSelectImageView.setVisibility(View.VISIBLE);
			} else {
				holder.mItemSelectView.setSelected(false);
				holder.mItemSelectImageView.setVisibility(View.GONE);
			}

			holder.mItemSelectView.setTag(getData().get(position)
					.getLeagueShort());
			holder.mItemSelectView.setText(getData().get(position)
					.getLeagueShort());
			holder.mItemSelectView
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String matchName = (String) v.getTag();
							if (mSureSelectMatchChoose.contains(matchName)) {
								v.setSelected(false);
								// holder.mItemSelectImageView.setVisibility(View.GONE);
								mSureSelectMatchChoose.remove(matchName);
							} else {
								v.setSelected(true);
								mSureSelectMatchChoose.add(matchName);
							}
							mSelectButton = 0;
							chageButtonState();
							notifyDataSetChanged();
						}
					});
			return convertView;
		}

		class ViewHolder {

			public TextView mItemSelectView;

			public ImageView mItemSelectImageView;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 全选
		case R.id.btnView_football_match_choose_all:
			
			circle_left_inside.setBackgroundResource(R.drawable.shape_circle_red);
			circle_right_inside.setBackgroundResource(R.drawable.shape_circle_gray);
			
			mSelectButton = R.id.btnView_football_match_choose_all;
			List<Match> matchs = getData();
			mSureSelectMatchChoose.clear();
			if (matchs != null) {
				for (int i = 0; i < matchs.size(); i++) {
					mSureSelectMatchChoose.add(matchs.get(i).getLeagueShort());
				}
			}
			chageButtonState();
			mAdapter.notifyDataSetChanged();
			break;
		// 反选
		case R.id.btnView_football_match_choose_noall:
			
			circle_left_inside.setBackgroundResource(R.drawable.shape_circle_gray);
			circle_right_inside.setBackgroundResource(R.drawable.shape_circle_red);
			
			mSelectButton = R.id.btnView_football_match_choose_noall;
			if (mSureSelectMatchChoose.size() == getData().size()) {
				mSureSelectMatchChoose.clear();
			} else {
				List<String> mTempSelectMatchChoose = new ArrayList<String>();
				List<Match> matchs2 = getData();
				if (matchs2 != null) {
					for (int i = 0; i < matchs2.size(); i++) {
						if (!mSureSelectMatchChoose.contains(matchs2.get(i)
								.getLeagueShort())) {
							mTempSelectMatchChoose.add(matchs2.get(i)
									.getLeagueShort());
						}
					}
					mSureSelectMatchChoose = mTempSelectMatchChoose;
				}
			}
			chageButtonState();
			mAdapter.notifyDataSetChanged();
			break;
		// 确认
		case R.id.btnView_football_match_ok:
			// mSureSelectMatchChoose = new
			// ArrayList<String>(mSelectMatchChoose);
			if (mOnItemClick != null) {
				if (mSureSelectMatchChoose.size() == 0) {
					ToastUtil.shortToast(mContext, mContext.getResources()
							.getString(R.string.lottery_match_choose));
				} else {
					mInitMatchChoose = new ArrayList<String>(mSureSelectMatchChoose);
					mOnItemClick.onSelectItemClick(v, mSureSelectMatchChoose);
					hide();
				}
			}

			break;
		case R.id.btnView_football_match_cancel:
//			List<Match> matchs1 = getData();
			mSureSelectMatchChoose.clear();
			mSureSelectMatchChoose= new ArrayList<String>(mInitMatchChoose);
//			if (mSelectMatchChoose.size() > 0) {
//				if (matchs1 != null) {
//					for (String i : mSelectMatchChoose) {
//						for (Match j : matchs1) {
//							if (j.getLeagueShort().equals(i)) {
//								mSureSelectMatchChoose.add(j.getLeagueShort());
//							}
//						}
//
//					}
//				}
//			} else {
//				if (matchs1 != null) {
//					for (Match i : matchs1) {
//						mSureSelectMatchChoose.add(i.getLeagueShort());
//					}
//				}
//			}
			chageButtonState();
			mAdapter.notifyDataSetChanged();
			hide();
			break;
		default:
			break;
		}
	}

	// 改变按钮选择状态 TODO全选 /反选 /五大联赛
	private void chageButtonState() {
//		for (int i = 0; i < selectButton.length; i++) {
//			if (selectButton[i] == mSelectButton) {
//				mView.findViewById(selectButton[i]).setSelected(true);
//			} else {
//				mView.findViewById(selectButton[i]).setSelected(false);
//			}
//		}
	}
}
