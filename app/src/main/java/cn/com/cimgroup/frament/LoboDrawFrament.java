package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.CenterPushSetActivity;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.LotteryDrawBasketballListActivity;
import cn.com.cimgroup.activity.LotteryDrawFootballListActivity;
import cn.com.cimgroup.activity.LotteryDrawListActivity;
import cn.com.cimgroup.adapter.LotteryDrawAdapter;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.view.FlowLayout;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 开奖
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年3月15日
 */
@SuppressLint("InflateParams")
public class LoboDrawFrament extends BaseLoadFragment {

	private View mView;
	// title bar的中间的文字
	private TextView mTitleNameView;
	// 开奖彩票集合
//	private PullToRefreshListView mPullToRefreshListView;
	// 开奖adapter
	private LotteryDrawAdapter mAdapter;
	private boolean mShouldInitialize = true;
	/** 请求数据成功 */
	private int GET_DATA_OK = 0;

	/** 数据源 */
	private List<LotteryDrawInfo> mList;
	/** 数据适配器 */
	private MyAdapter mMyAdapter;
	
	private RelativeLayout drawHead;
	
	private boolean isPrepared;

	
	private TextView textView_title_service;
	
	public static final String SOURCE_FRAGMENT = "lobodrawfragment";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (mView == null) {
			mView = inflater.inflate(R.layout.frament_lotterydraw, null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {
//			mProgressDialog.showDialog();
			mMyAdapter = new MyAdapter();
			initView();
			isPrepared = true;
			// 初始化mPullToRefreshListView 上下拉刷新、添加适配器，加载数据等功能
			super.initLoad(mListView, mMyAdapter);
			
			lazyLoad();
		}
		return mView;
	}

	@SuppressLint("NewApi")
	private void initView() {
		mList = new ArrayList<LotteryDrawInfo>();
		
		drawHead = (RelativeLayout)mView.findViewById(R.id.layout_draw_head);

		//头部标题的设置
		((TextView) mView.findViewById(R.id.textView_title_back))
				.setVisibility(View.GONE);
		mTitleNameView = (TextView) mView
				.findViewById(R.id.textView_title_word);
		mTitleNameView.setText(getString(R.string.popwindow_lotterybid_more2));
		mListView = (PullToRefreshListView) mView
				.findViewById(R.id.cvLView_lottery_draw);
		textView_title_service = (TextView) mView.findViewById(R.id.textView_title_sign);
		textView_title_service.setBackground(getResources().getDrawable(R.drawable.lobo_draw_notic));
		textView_title_service.setVisibility(View.VISIBLE);
		textView_title_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (!NetUtil.isConnected(mFragmentActivity)) {
					//无网
					ToastUtil.shortToast(mFragmentActivity, "网络不给力");
				}else {
					//跳转消息推送设置界面
					if (App.userInfo != null && !TextUtils.isEmpty(App.userInfo.getUserId())) {
						startActivity(CenterPushSetActivity.class);
					}else {
						Intent intent = new Intent();
						intent.setClass(getActivity(), LoginActivity.class);
						intent.putExtra(LoboDrawFrament.SOURCE_FRAGMENT, LoboDrawFrament.SOURCE_FRAGMENT);
						startActivity(intent);
					}
				}
			}
		});
		// mAdapter = new LotteryDrawAdapter(mFragmentActivity, null, null);

		
//		mPullToRefreshListView.setAdapter(mMyAdapter);
//		mPullToRefreshListView.setRefreshing();
//		mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
	}

	private CallBack mBack = new CallBack() {
		@Override
		//用于接收数据的初始化。数据保存在mList中
		public void syncLotteryDrawSuccess(
				final List<LotteryDrawInfo> lotteryDrawInfos) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loadFinish();
//					hideLoadingDialog();
					if (lotteryDrawInfos != null) {
//						for (int i = 0; i < lotteryDrawInfos.size(); i++) {
//							LotteryDrawInfo drawInfo = lotteryDrawInfos.get(i);
//							if (drawInfo.getGameNo().equals(
//									GlobalConstants.TC_JCLQ)) {
//								lotteryDrawInfos.remove(i);
//								i--;
//							}
//						}
						mList = lotteryDrawInfos;
						if (mList != null && mList.size() != 0) {
							mHandler.sendEmptyMessage(GET_DATA_OK);
						} else {
							NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 10);
						}
					}
					mListView.setMode(Mode.PULL_FROM_START);

					// if (isFirstPage()) {
					// mAdapter.replaceAll(lotteryDrawInfos);
					// } else {
					// mAdapter.addAll(lotteryDrawInfos);
					// }
				}
			});
		}

		@Override
		public void syncLotteryDrawError(final String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loadFinish();
//					Toast.makeText(mFragmentActivity, error, 0).show();
					NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, error, 0);
				}
			});
		}

	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		LotteryDrawInfo result = (LotteryDrawInfo) mMyAdapter.getItem(position
				- mListView.getRefreshableView()
						.getHeaderViewsCount());
		if (result != null) {
			// 跳转到此彩种的信息页面
			if (result.getGameNo().equals(GlobalConstants.TC_JCZQ)){
				//竞彩足球的跳转页面
				Intent intent = new Intent(mFragmentActivity,
						LotteryDrawFootballListActivity.class);
				intent.putExtra("LotteryDrawInfo", result);
				startActivity(intent);
			}else if(result.getGameNo().equals(GlobalConstants.TC_JCLQ)){
				//竞彩篮球的跳转页面
				Intent intent = new Intent(mFragmentActivity,
						LotteryDrawBasketballListActivity.class);
				intent.putExtra("LotteryDrawInfo", result);
				startActivity(intent);
			}else {
				Intent intent = new Intent(mFragmentActivity,
						LotteryDrawListActivity.class);
				//2.4期需求  4场进球  --> 进球彩      6场半全场 --> 半全场
				if(result.getGameName()!=null && !TextUtils.isEmpty(result.getGameName())){
					if(result.getGameName().equals("4场进球")){
						result.setGameName("进球彩");
					}else if (result.getGameName().equals("6场半全场")) {
						result.setGameName("半全场");
					}
				}
				intent.putExtra("LotteryDrawInfo", result);
				startActivity(intent);
			}
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	//2016-9-19 修改记录 将removeCallBack放到onStop()中进行
	@Override
	public void onStop() {
		super.onStop();
		Controller.getInstance().removeCallback(mBack);
	}

	/** 接口开始调用时间 */
	private long startTime = 0;

	/**
	 * 获取数据
	 */
	private void getDatas() {
		long endTime = System.currentTimeMillis();
//		showLoadingDialog();
		if (endTime - startTime > 800) {
			Controller.getInstance().syncLotteryDraw(
					GlobalConstants.NUM_HALLLOTTERYDRAW, mBack);
			startTime = endTime;
		} else {
			loadFinish();
		}

	}

	@Override
	protected void loadData(int page) {
		getDatas();
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		getDatas();
//	}
	
//	@Override
//	public void setUserVisibleHint(boolean isVisibleToUser) {
//		super.setUserVisibleHint(isVisibleToUser);
//		// 可见时候调用
//		if (getUserVisibleHint()) {
//			getDatas();
//		}
//	}

	@Override
	public void loadData(int title, int page) {

	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == GET_DATA_OK) {
				mMyAdapter.notifyDataSetChanged();
			}
		};
	};

	/**
	 * 开奖信息adapter
	 * 
	 * @author 秋风
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

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
		public View getView(int position, View view, ViewGroup parent) {
			Object obj = getItem(position);
			ViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_lotterydraw_list, null);
				holder = new ViewHolder();
				holder.mLotteryClassView = (TextView) view
						.findViewById(R.id.txtView_looterydraw_title);
				holder.mLotteryTermNumView = (TextView) view
						.findViewById(R.id.txtView_looterydraw_term);
				holder.mLotteryDrawDateView = (TextView) view
						.findViewById(R.id.txtView_looterydraw_date);
				holder.mLotteryDrawNumberView = (FlowLayout) view
						.findViewById(R.id.cvView_lotterydraw_number);
				holder.id_image = (ImageView) view.findViewById(R.id.id_image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			//根据服务器返回的数据 填充布局的内容
			LotteryDrawInfo item = (LotteryDrawInfo) getItem(position);
			if(item.getGameName() != null && !TextUtils.isEmpty(item.getGameName())){
				//2.4期需求  4场进球  --> 进球彩      6场半全场 --> 半全场
				if(item.getGameName().equals("4场进球")){
					holder.mLotteryClassView.setText("进球彩");
				}else if(item.getGameName().equals("6场半全场")){
					holder.mLotteryClassView.setText("半全场");
				}else {
					holder.mLotteryClassView.setText(item.getGameName());
				}
			}
			holder.mLotteryTermNumView.setText(getActivity().getString(
					R.string.record_tz_detail_qi, item.getIssueNo()));
			if (!StringUtil.isEmpty(item.getAwardDate())) {
				holder.mLotteryDrawDateView.setText(item.getAwardDate()
						.substring(0, 10));
			}

			if (item.getGameNo().equals(GlobalConstants.TC_JCZQ)
					|| item.getGameNo().equals(GlobalConstants.TC_JCLQ)) {
				// LinearLayout.LayoutParams layoutParams1 = new
				// LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				// ViewGroup.LayoutParams.WRAP_CONTENT);
				// layoutParams1.setMargins(0,15,0,10);
				// holder.mLotteryDrawNumberView.setLayoutParams(layoutParams1);
				//竞彩足球+竞彩篮球的开奖信息显示 因为和数字彩不同 所以自定义显示
				holder.mLotteryDrawNumberView.removeAllViews();
				LinearLayout numTv = generateLinearLayout(item, item.getGameNo());

				holder.mLotteryDrawNumberView.addView(numTv);
			} else {
				// 篮球
				String winbasecode = "";
				// 红球
				String winspecialcode = "";
				String[] jiCode = new String[]{};
				if (TextUtils.isEmpty(item.getWinCode())) {
					
				}else {
					jiCode = item.getWinCode().split("\\ ");
				}
				
				if (jiCode != null && jiCode.length > 0) {
					winbasecode = jiCode[0];
				}
				if (jiCode != null && jiCode.length > 1) {
					winspecialcode = jiCode[1];
				}
				holder.mLotteryDrawNumberView.removeAllViews();

				boolean showBg = false;
				switch (item.getGameNo()) {
				case GlobalConstants.TC_SF14:
				case GlobalConstants.TC_SF9:
				case GlobalConstants.TC_JQ4:
				case GlobalConstants.TC_BQ6:
					showBg = true;
					break;

				default:
					showBg = false;
					break;
				}

				LotteryShowUtil.showNumberLotteryView(getActivity(), 0,
						holder.mLotteryDrawNumberView, winbasecode, showBg);
				LotteryShowUtil.showNumberLotteryView(getActivity(), 1,
						holder.mLotteryDrawNumberView, winspecialcode, showBg);
			}
			holder.id_image.setVisibility(View.VISIBLE);
			return view;
		}

		private LinearLayout generateLinearLayout(LotteryDrawInfo item, String type) {
			
			int image_icon = 0;
			int image_left = 0;
			int image_right = 0;
			
			int color_bg_text = 0;
			
			if (item.getGameNo().equals(GlobalConstants.TC_JCZQ)) {
				//竞彩足球图片初始化
				image_icon = R.drawable.lobo_draw_football_icon;
				image_left = R.drawable.lobo_draw_football_drawable_left;
				image_right = R.drawable.lobo_draw_football_drawable_right;
				
			}else if (item.getGameNo().equals(GlobalConstants.TC_JCLQ)) {
				//竞彩篮球图片初始化
				image_icon = R.drawable.lobo_draw_basketball_icon;
				image_left = R.drawable.lobo_draw_basketball_drawable_left;
				image_right = R.drawable.lobo_draw_basketball_drawable_right;
			}
			
			//添加最外层的LinearLayout
			LinearLayout content = new LinearLayout(getActivity());
			content.setOrientation(LinearLayout.HORIZONTAL);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, 
					ViewGroup.LayoutParams.MATCH_PARENT);
			lp.gravity = Gravity.CENTER_VERTICAL;
			content.setLayoutParams(lp);
			
			//图标
			ImageView imageView_icon = new ImageView(getActivity());
			LinearLayout.LayoutParams lp_icon = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT);
			imageView_icon.setImageResource(image_icon);
			imageView_icon.setLayoutParams(lp_icon);
			
			//左侧圆弧
			ImageView imageView_left = new ImageView(getActivity());
			LinearLayout.LayoutParams lp_left = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT);
			lp_left.gravity = Gravity.CENTER_VERTICAL;
			imageView_left.setImageResource(image_left);
			imageView_left.setLayoutParams(lp_left);
			ViewUtils.measureView(imageView_left);
			
			//右侧圆弧
			ImageView imageView_right = new ImageView(getActivity());
			LinearLayout.LayoutParams lp_right = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT);
			lp_right.gravity = Gravity.CENTER_VERTICAL;
			lp_right.rightMargin = DensityUtils.dip2px(20);
			imageView_right.setImageResource(image_right);
			imageView_right.setLayoutParams(lp_right);
			
			//比分整体的外层LinearLayout
			LinearLayout content_child = new LinearLayout(getActivity());
			LinearLayout.LayoutParams lp_child = new LinearLayout.LayoutParams(0, imageView_left.getMeasuredHeight());
			lp_child.gravity = Gravity.CENTER_VERTICAL;
			lp_child.weight = 1;
			content_child.setLayoutParams(lp_child);
			content_child.setOrientation(LinearLayout.HORIZONTAL);
			if (item.getGameNo().equals(GlobalConstants.TC_JCZQ)) {
				//竞彩足球球队+比分背景颜色
				color_bg_text = getResources().getColor(R.color.color_green_cold);
				
			}else if (item.getGameNo().equals(GlobalConstants.TC_JCLQ)) {
				//竞彩篮球球队+比分背景颜色
				color_bg_text = getResources().getColor(R.color.color_orange_red);
			}
			content_child.setBackgroundColor(color_bg_text);
			
			//根据服务器返回值截取主队 + 比分 + 客队
			String hostName = "";
			String score = "";
			String guestName = "";
			if (!StringUtil.isEmpty(item.getWinCode())) {
				String[] text = item.getWinCode().split("\\_");
				if (text[0].length() > 4) {
					hostName = text[0].substring(0,4)+"...";
				}else {
					hostName = text[0];
				}
				score = text[1];
				if (text[2].length() > 4) {
					guestName = text[2].substring(0,4)+"...";
				}else {
					guestName = text[2];
				}
			}
			
			
			//添加主队名称
			TextView numTv_host = new TextView(getActivity());
			LinearLayout.LayoutParams layoutParams_host = new LinearLayout.LayoutParams(
					0, ViewGroup.LayoutParams.MATCH_PARENT);
			layoutParams_host.gravity = Gravity.CENTER_VERTICAL;
			layoutParams_host.weight = 1;
			numTv_host.setLayoutParams(layoutParams_host);
			numTv_host.setGravity(Gravity.CENTER|Gravity.RIGHT);
			numTv_host.setTextSize(16);
			numTv_host.setText(hostName);
//			numTv_host.setMaxEms(4);
//			numTv_host.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
//			numTv_host.setSingleLine(true);
//			numTv_host.setEllipsize(TruncateAt.END);
			numTv_host.setTextColor(Color.WHITE);
			
			//添加比分
			TextView numTv_score = new TextView(getActivity());
			LinearLayout.LayoutParams layoutParams_score = new LinearLayout.LayoutParams(
					DensityUtils.dip2px(60), ViewGroup.LayoutParams.MATCH_PARENT);
			layoutParams_score.gravity = Gravity.CENTER_VERTICAL;
			layoutParams_score.leftMargin = DensityUtils.dip2px(10);
			layoutParams_score.rightMargin = DensityUtils.dip2px(10);
			numTv_score.setLayoutParams(layoutParams_score);
			numTv_score.setGravity(Gravity.CENTER);
			numTv_score.setTextSize(16);
			numTv_score.setText(score);
			numTv_score.setTextColor(Color.WHITE);
			
			//添加客队名称
			TextView numTv_guest = new TextView(getActivity());
			LinearLayout.LayoutParams layoutParams_guest = new LinearLayout.LayoutParams(
					0, ViewGroup.LayoutParams.MATCH_PARENT);
			layoutParams_guest.gravity = Gravity.CENTER_VERTICAL;
			layoutParams_guest.weight = 1;
			numTv_guest.setLayoutParams(layoutParams_guest);
			numTv_guest.setGravity(Gravity.CENTER|Gravity.LEFT);
			numTv_guest.setTextSize(16);
			numTv_guest.setText(guestName);
//			numTv_guest.setMaxEms(0);
//			numTv_guest.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
//			numTv_guest.setSingleLine(true);
//			numTv_guest.setEllipsize(TruncateAt.END);
			numTv_guest.setTextColor(Color.WHITE);
			
			content_child.addView(numTv_host);
			content_child.addView(numTv_score);
			content_child.addView(numTv_guest);
			
			content.addView(imageView_icon);
			content.addView(imageView_left);
			content.addView(content_child);
			content.addView(imageView_right);
			return content;
		}

	}

	/**
	 * viewHolder
	 * 
	 * @author 秋风
	 * 
	 */
	class ViewHolder {
		TextView mLotteryClassView;
		TextView mLotteryTermNumView;
		TextView mLotteryDrawDateView;
		FlowLayout mLotteryDrawNumberView;
		ImageView id_image;
	}

	@Override
	protected void lazyLoad() {
		if (!isVisible || !isPrepared) {
			return;
		} else {
			getDatas();
		}
	}

}
