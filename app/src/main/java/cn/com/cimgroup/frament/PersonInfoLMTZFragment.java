package cn.com.cimgroup.frament;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.CommitPayActivity;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.activity.TzDetailActivity;
import cn.com.cimgroup.activity.TzListActivity.MCenterRecordTz;
import cn.com.cimgroup.animation.StretchAnimation;
import cn.com.cimgroup.bean.AllBetting;
import cn.com.cimgroup.bean.AllBettingList;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.TzObj;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.view.FlowLayout;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 个人中心乐米我的投注
 * 
 * @author 秋风
 * 
 */
@SuppressLint("InflateParams")
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class PersonInfoLMTZFragment extends BaseFrament implements
		OnClickListener {
	private View mView;
	/**
	 * 投注记录标签 全部：id_tz_all 代购：id_tz_purchasing 追号：id_tz_during 中奖：id_tz_winning
	 */
	private TextView id_tz_all, id_tz_purchasing, id_tz_during, id_tz_winning;
	/** 位移线 */
	private View id_line;
	/** 投注记录ListView */
	private ListView id_tz_listview;

	/** 投注记录下方横线 */
	private View id_bottom_line;
	/** 数据源集合 */
	private List<AllBetting> mList = null;
	/***/
	private TzAdapter mTZAdapter = null;

	private float moveLength;

	/** 大乐透选号区域 */
	private FlowLayout id_flowlayout;
	/** 大乐透投注 */
	private TextView id_dlt_tz;
	/** 投注倍数显示 */
	private TextView id_tz_bs_show;
	/** 共计支付乐米数 */
	private TextView id_pay_lemi_num;
	/** 投注 */
	private TextView id_dlt_bei_10, id_dlt_bei_20, id_dlt_bei_50;
	/** 显示/隐藏倍数选区 */
	private StretchAnimation mBSAnimation;
	/** 大乐透倍数选区 */
	private LinearLayout id_bs_choose_layout;

	/** 大乐透快投选区 */
	private ViewStub id_viewstub_lmtz;
	/** 投注期次 */
	private String mPeriod;

	/** 是否可以加载更多 */
	private boolean isCanLoadDatas = true;
	/** 遮罩层 */
	private View id_cover_view;

	// 投注记录类型
	private MCenterRecordTz mCenterRecordTz;
	
	private ImageView emptyImage;
	private TextView oneText;
	private TextView twoText;
	private TextView button;
	

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_person_info_lemi_tz,
					null);
			mCenterRecordTz = MCenterRecordTz.ALL;
			requestNo = GlobalConstants.NUM_ALL;
			colorRed = getActivity().getResources().getColor(R.color.color_red);
			colorGray = getActivity().getResources().getColor(R.color.color_gray_indicator);
			// 初始化视图组件
			initView();
			// 绑定事件
			initEvent();
			Bundle bundle = getArguments();
			if (bundle != null) {
				endIndex = Integer.parseInt(bundle.getString("tag", "0"));
				startIndex = 0;
//				lineAnimation();
			}

			mList = new ArrayList<AllBetting>();
			mTZAdapter = new TzAdapter();
			id_tz_listview.setAdapter(mTZAdapter);
			id_tz_listview.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_MOVE:
						return true;
					default:
						break;
					}
					return true;
				}
			});

		}
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (parent != null) {
			parent.removeView(mView);
		}
		

		return mView;
	}

	/** 重置ViewStub */
	private void initViewStub(boolean isShow, String errCode, int type) {
		if (null == id_viewstub_lmtz) {
			id_viewstub_lmtz = (ViewStub) mView
					.findViewById(R.id.id_viewstub_lmtz);
			id_viewstub_lmtz.inflate();
//			id_flowlayout = (FlowLayout) mView.findViewById(R.id.id_flowlayout);
//			id_dlt_tz = (TextView) mView.findViewById(R.id.id_dlt_tz);
//			id_tz_bs_show = (TextView) mView.findViewById(R.id.id_tz_bs_show);
//			id_pay_lemi_num = (TextView) mView
//					.findViewById(R.id.id_pay_lemi_num);
//
//			id_dlt_bei_10 = (TextView) mView.findViewById(R.id.id_dlt_bei_10);
//			id_dlt_bei_20 = (TextView) mView.findViewById(R.id.id_dlt_bei_20);
//			id_dlt_bei_50 = (TextView) mView.findViewById(R.id.id_dlt_bei_50);
//			id_bs_choose_layout = (LinearLayout) mView
//					.findViewById(R.id.id_bs_choose_layout);
//			id_bs_choose_layout.setVisibility(View.GONE);
//
//			mBSAnimation = new StretchAnimation(id_bs_choose_layout,
//					StretchAnimation.TYPE.vertical, 300);
//			mBSAnimation.setInterpolator(new AccelerateInterpolator());
//
//			id_dlt_tz.setOnClickListener(this);
//
//			id_dlt_bei_10.setOnClickListener(this);
//			id_dlt_bei_20.setOnClickListener(this);
//			id_dlt_bei_50.setOnClickListener(this);
//			resetBS(R.id.id_dlt_bei_10);
//			mView.findViewById(R.id.id_choose_bs_layout).setOnClickListener(
//					this);
//			mView.findViewById(R.id.id_exchange_num).setOnClickListener(this);
			
			emptyImage = (ImageView) mView.findViewById(R.id.imageView_load_empty);
			
			oneText = (TextView) mView.findViewById(R.id.textView_load_one);
			
			twoText = (TextView) mView.findViewById(R.id.textView_load_two);
			
			button = (TextView) mView.findViewById(R.id.button_load);
			
			button.setOnClickListener(this);
		}
		if (isShow) {
			// 获取大乐透投注信息
			id_viewstub_lmtz.setVisibility(View.VISIBLE);
			NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, errCode, type);
		} else {
			id_viewstub_lmtz.setVisibility(View.GONE);
		}
	}


	/**
	 * 绑定事件
	 */
	private void initEvent() {
		id_tz_all.setOnClickListener(clickListener);
		id_tz_purchasing.setOnClickListener(clickListener);
		id_tz_during.setOnClickListener(clickListener);
		id_tz_winning.setOnClickListener(clickListener);

	}

	/**
	 * 公开父类调用方法
	 * 
	 * @param id
	 *            ：按钮标识
	 */
	public void tagOnClick(int id) {
		switch (id) {
		case 0:
			clickListener.onClick(id_tz_all);
			break;
		case 1:
			clickListener.onClick(id_tz_purchasing);
			break;
		case 2:
			clickListener.onClick(id_tz_during);
			break;
		case 3:
			clickListener.onClick(id_tz_winning);
			break;

		default:
			break;
		}
	}

	private OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mList.clear();
			requestPage = 1;
			isRefresh = true;
			switch (v.getId()) {
			case R.id.id_tz_all:
				// 点击全部
				endIndex = 0;
				requestNo = GlobalConstants.NUM_ALL;
				onlyWin = "";
				mCenterRecordTz = MCenterRecordTz.ALL;
				break;
			case R.id.id_tz_purchasing:
				// 点击代购
				endIndex = 1;
				onlyWin = "";
				requestNo = GlobalConstants.NUM_TZDG;
				mCenterRecordTz = MCenterRecordTz.BUY;
				break;
			case R.id.id_tz_during:
				// 点击追号
				endIndex = 2;
				onlyWin = "";
				requestNo = GlobalConstants.NUM_ZHUI;
				mCenterRecordTz = MCenterRecordTz.ZHUI;
				break;
			case R.id.id_tz_winning:
				// 点击中奖
				endIndex = 3;
				requestNo = GlobalConstants.NUM_ALL;
				onlyWin = "1";
				mCenterRecordTz = MCenterRecordTz.WINNING;
				break;

			default:
				break;
			}
			lineAnimation();
		}
	};
	/** 起始点 */
	private int startIndex = 0;
	/** 点击项 */
	private int endIndex;
	/**红色*/
	private int colorRed = 0;
	/**灰色*/
	private int colorGray =0;
	

	/**
	 * 红线的运动
	 */
	private void lineAnimation() {
		isRefresh = true;
		id_tz_all.setTextColor(colorGray);
		id_tz_purchasing.setTextColor(colorGray);
		id_tz_during.setTextColor(colorGray);
		id_tz_winning.setTextColor(colorGray);
		switch (endIndex) {
		case 0:
			id_tz_all.setTextColor(colorRed);
			break;
		case 1:
			id_tz_purchasing.setTextColor(colorRed);
			break;
		case 2:
			id_tz_during.setTextColor(colorRed);
			break;
		case 3:
			id_tz_winning.setTextColor(colorRed);
			break;

		default:
			break;
		}
		if (mTagChangeListener != null) {
			mTagChangeListener.setIndex(endIndex);
		}
		if (startIndex != endIndex) {
			float start = startIndex * moveLength;
			float end = endIndex * moveLength;
			TranslateAnimation animation = new TranslateAnimation(start, end,
					0f, 0f);
			animation.setInterpolator(new LinearInterpolator());
			animation.setDuration(100 * (Math.abs(endIndex - startIndex)));
			animation.setFillAfter(true);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					initDatas();
				}
			});
			id_line.startAnimation(animation);
			startIndex = endIndex;
		} else {
			initDatas();
		}
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_cover_view = mView.findViewById(R.id.id_cover_view);
		id_cover_view.setVisibility(View.GONE);

		id_tz_all = (TextView) mView.findViewById(R.id.id_tz_all);
		id_tz_purchasing = (TextView) mView.findViewById(R.id.id_tz_purchasing);
		id_tz_during = (TextView) mView.findViewById(R.id.id_tz_during);
		id_tz_winning = (TextView) mView.findViewById(R.id.id_tz_winning);

		id_line = mView.findViewById(R.id.id_line);

		id_tz_listview = (ListView) mView.findViewById(R.id.id_tz_listview);
		id_bottom_line = mView.findViewById(R.id.id_bottom_line);

		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		moveLength = dm.widthPixels / 4;
	}

	/** 设置遮罩层的显示/隐藏 */
	public void setCoverViewState(boolean isShow) {
		id_cover_view.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}

	/** 调用接口：全部3015，代购3002，追号3003，中奖3015 */
	private String requestNo;
	/** 是否中奖查询：1，中奖；‘’全部 */
	private String onlyWin = "";
	/** 请求页码 */
	private int requestPage;

	private long startTime = 0;
	private long endTime = 0;

	/**
	 * 获取我的投注记录
	 */
	private void initDatas() {
		endTime = System.currentTimeMillis();
		if (endTime - startTime < 500) {
			isCanLoadDatas = false;
			notifyParentHideLoading();
			return;
		}
		startTime = endTime;
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId()) && GlobalConstants.personTagIndex == 0) {
			
			if (!NetUtil.isConnected(getActivity())) {
				initViewStub(true,"10000", 0);
				isCanLoadDatas = false;
				notifyParentHideLoading();
				return;
			}
			if (mTagChangeListener!= null) {
				mTagChangeListener.showLodingDialog();
			}
			Controller.getInstance().getLoBoTzList(GlobalConstants.personGameNo, requestNo,
					App.userInfo.getUserId(), onlyWin, 10 + "", requestPage + "",
					mCallBack);
		}
	}
	/**
	 * 通知父布局收回刷新
	 */
	private void notifyParentHideLoading(){
		if (mTagChangeListener != null) {
			mTagChangeListener.hideLodingDialog();
			if (isRefresh) {
				mTagChangeListener.onRefreshFinish(isCanLoadDatas);
			} else {
				mTagChangeListener.onLoadFinish(isCanLoadDatas);
			}
		}
	}

	/** 修改彩种 */
	public void changeGameNo(String no) {
		GlobalConstants.personGameNo = no;
		isRefresh = true;
		requestPage = 1;
		initDatas();
	}

	private CallBack mCallBack = new CallBack() {

		@Override
		public void getLoBoTzListSuccess(final AllBettingList all) {

			getActivity().runOnUiThread(new Runnable() {

				public void run() {
					List<AllBetting> bettingList = null;
					if (all.getResCode().equals("0")) {
						bettingList = all.getList();
						if (isRefresh) {
							mList.clear();
						}
						mList.addAll(bettingList);
						mTZAdapter.notifyDataSetChanged();
						ViewUtils
								.setListViewHeightBasedOnChildren(id_tz_listview);

					} else {
						ToastUtil.shortToast(getActivity(), all.getResMsg());
					}
					if (bettingList != null && bettingList.size() == 10) {
						requestPage++;
						// 通知父容器可以加载数据
						isCanLoadDatas = true;
					} else {
						// 通知父容器不可以加载数据
						isCanLoadDatas = false;
					}
					FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) id_cover_view
							.getLayoutParams();
					if (null == mList || mList.size() == 0) {
						id_tz_listview.setVisibility(View.GONE);
						id_bottom_line.setVisibility(View.GONE);
						initViewStub(true, "0", 1);
						lp.height = DensityUtils.dip2px(400);
					} else {
						id_tz_listview.setVisibility(View.VISIBLE);
						id_bottom_line.setVisibility(View.VISIBLE);
						initViewStub(false, "0", 1);
						ViewUtils.measureView(id_tz_listview);
						int height = id_tz_listview.getMeasuredHeight();
						if (height < DensityUtils.dip2px(400)) {
							height = DensityUtils.dip2px(400);
						}
						lp.height = height + 200;
					}
					id_cover_view.setLayoutParams(lp);
					id_cover_view.invalidate();
					notifyParentHideLoading();
					GlobalConstants.isRefreshFragment = false;
				}
			});

		}

		@Override
		public void getLoBoTzListFailure(final String error) {
			super.onFail(error);
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// 还原页码
					
					FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) id_cover_view
							.getLayoutParams();
					if (null == mList || mList.size() == 0) {
						isCanLoadDatas = false;
						id_tz_listview.setVisibility(View.GONE);
						id_bottom_line.setVisibility(View.GONE);
						lp.height = DensityUtils.dip2px(400);
						initViewStub(true, error, 0);
					} else {
						ViewUtils.measureView(id_tz_listview);
						int height = id_tz_listview.getMeasuredHeight();
						if (height < DensityUtils.dip2px(400)) {
							height = DensityUtils.dip2px(400);
						}
						lp.height = height + 200;
						id_tz_listview.setVisibility(View.VISIBLE);
						id_bottom_line.setVisibility(View.VISIBLE);
						initViewStub(false, error, 0);
					}
					notifyParentHideLoading();
					id_cover_view.setLayoutParams(lp);
					id_cover_view.invalidate();
					if (mTagChangeListener!= null) {
						mTagChangeListener.showLodingDialog();
					}
				}
				
			});

		}

		public void getLoBoPeriodSuccess(final List<LoBoPeriodInfo> infos) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					LoBoPeriodInfo periodInfo = infos.get(0);
					mPeriod = periodInfo.getIssue();
					if (isTZ) {
//						dltTZ();
						isTZ = false;
					}
				}
			});

		};

		public void getLoBoPeriodFailure(String error) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(getActivity(), "期次获取失败，请重新投注");
				}
			});

		};
	};
	private boolean isFirst = true;
	@Override
	public void onResume() {
		super.onResume();
		if (isFirst) {
			isFirst=false;
			getDatas();
		}else {
			if (GlobalConstants.isRefreshFragment) {
				if (GlobalConstants.personEndIndex!=-1) {
					endIndex = GlobalConstants.personEndIndex;
					GlobalConstants.personEndIndex = -1;
				}
				getDatas();
			}else {
				if (endIndex !=0) {
					startIndex = 0;
					changeLine();
				}
				if (mList==null||mList.size()==0) {
					refreshDatas();
				}
			}
			
		}
	}
	/**修改红线位置*/
	private void changeLine() {
		if (mTagChangeListener != null) {
			mTagChangeListener.setIndex(endIndex);
		}
		if (startIndex != endIndex) {
			float start = startIndex * moveLength;
			float end = endIndex * moveLength;
			TranslateAnimation animation = new TranslateAnimation(start, end,
					0f, 0f);
			animation.setInterpolator(new LinearInterpolator());
			animation.setDuration(100 * (Math.abs(endIndex - startIndex)));
			animation.setFillAfter(true);
			id_line.startAnimation(animation);
			startIndex = endIndex;
		} 
	}

	private void getDatas() {
		switch (endIndex) {
		case 0:
			clickListener.onClick(id_tz_all);
			break;
		case 1:
			clickListener.onClick(id_tz_purchasing);
			break;
		case 2:
			clickListener.onClick(id_tz_during);
			break;
		case 3:
			clickListener.onClick(id_tz_winning);
			break;
		default:
			break;
		}
	};

	/**
	 * 获取碎片实例
	 * 
	 * @param tag
	 * @return
	 */
	public static PersonInfoLMTZFragment newInstance(String tag) {
		PersonInfoLMTZFragment fragment = new PersonInfoLMTZFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		fragment.setArguments(bundle);
		return fragment;

	}

	private class TzAdapter extends BaseAdapter {

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
			ViewHolder holder = null;
			if (null == view) {
				holder = new ViewHolder();
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_fragment_tzjl, null);
				holder.textView_tz_item_time = (TextView) view
						.findViewById(R.id.textView_tz_item_time);
				holder.textview_tz_gamename = (TextView) view
						.findViewById(R.id.textview_tz_gamename);
				holder.textView_tz_item_state = (TextView) view
						.findViewById(R.id.textView_tz_item_state);
				holder.textView_tz_item_money = (TextView) view
						.findViewById(R.id.textView_tz_item_money);
				holder.textView_tz_item_margin = view
						.findViewById(R.id.textView_tz_item_margin);
				holder.imageview_tz_state = (ImageView) view
						.findViewById(R.id.imageview_tz_state);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();
			holder.imageview_tz_state.setVisibility(View.INVISIBLE);
			holder.textView_tz_item_margin.setVisibility(View.GONE);
			final AllBetting allBetting = mList.get(position);
			String time = DateUtil.getTimeInMillisToStr(allBetting.getCreateTime());
			String state = "";
			String betMoney ="";
			boolean isWin = false;
			switch (endIndex) {
			case 0:
				// 全部
				betMoney = allBetting.getBetMoney();
				if (allBetting.getIsAward().equals("2")) {
					state = allBetting.getAwardDes();
					if (allBetting.getStatusDes().trim().equals("已派奖")) {
						holder.imageview_tz_state.setVisibility(View.VISIBLE);
					}
					isWin = true;
				} else {
					state = allBetting.getStatusDes();
					isWin = false;
				}
				break;
			case 1:
				// 代购
				betMoney = getActivity()
				.getResources().getString(R.string.betting)
				+ allBetting.getMoney()
				+ getActivity().getResources().getString(
						R.string.tz_item_mi);
				if (!StringUtil.isEmpty(allBetting.getWinMoney())&&Double.parseDouble(allBetting.getWinMoney()) > 0) {
					state = String.format("中奖：%s米", allBetting.getWinMoney());
					if (allBetting.getStatus().equals("6")) {
						holder.imageview_tz_state.setVisibility(View.VISIBLE);
					}
					isWin = true;
				}else {
					isWin = false;
					state = allBetting.getStatusDes();
				}
				break;
			case 2:
				// 追号
				isWin = false;
				betMoney = getActivity()
						.getResources().getString(R.string.betting)
						+ allBetting.getChaseAllMoney()
						+ getActivity().getResources().getString(
								R.string.tz_item_mi);

				if (!StringUtil.isEmpty(allBetting.getWinMoney())
						&& Double.parseDouble(allBetting.getWinMoney()) > 0) {
					isWin = true;
					holder.imageview_tz_state.setVisibility(View.VISIBLE);
					state = String.format("中奖%s米", allBetting.getWinMoney());
				}else {
					state = allBetting.getChaseStatus();
				if (!TextUtils.isEmpty(state)
						&& allBetting.getChaseStatus().equals("1"))
					state = "追号中";
				else
					state = "追停结束";
				}
				
				break;
			case 3:
				// 中奖
				if (allBetting.getIsAward().equals("2")) {
					state = allBetting.getAwardDes();
//					if (allBetting.getStatusDes().trim().equals("已派奖")) {
//						holder.imageview_tz_state.setVisibility(View.VISIBLE);
//					}
					holder.imageview_tz_state.setVisibility(View.VISIBLE);
					isWin = true;
					
				} else {
					isWin = false;
					state = allBetting.getStatusDes();
				}
				betMoney = allBetting.getBetMoney();
				state = allBetting.getAwardDes();
				break;

			default:
				break;
			}
			if (isWin) {
				holder.textView_tz_item_state.setTextColor(getActivity().getResources().getColor(R.color.color_red));
			}else
				holder.textView_tz_item_state.setTextColor(getActivity().getResources().getColor(R.color.color_gray_secondary));
			String gameName = getGameName(allBetting.getGameName());
			if (gameName.length() == 6) {
				holder.textview_tz_gamename.setPadding(0, PADDING_TOP , 0, 0);
			}else
				holder.textview_tz_gamename.setPadding(0,  PADDING_TOP_DEFAULT , 0, 0);
			holder.textview_tz_gamename.setText(gameName);
			holder.textView_tz_item_money.setText(betMoney);
			holder.textView_tz_item_state.setText(state);
			holder.textView_tz_item_time.setText(time);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mFragmentActivity,
							TzDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("data", allBetting);
					intent.putExtras(bundle);
					intent.putExtra("mcenterrecordtz", mCenterRecordTz);
					startActivity(intent);
				}
			});

			return view;
		}

		class ViewHolder {
			/** 彩种类型 */
			TextView textview_tz_gamename;
			/** 投注时间 */
			TextView textView_tz_item_time;
			/** 订单状态（图） */
			ImageView imageview_tz_state;
			/** 订单状态 （文） */
			TextView textView_tz_item_state;
			/** 投注金额 */
			TextView textView_tz_item_money;
			/** 底部间隔线 */
			View textView_tz_item_margin;
		}
	}
	private static final int PADDING_TOP = DensityUtils.dip2px(13);
	private static final int PADDING_TOP_DEFAULT = DensityUtils.dip2px(6);
	/**
	 * 将玩法类型由行显示转换成列显示
	 * @param gameName
	 * @return
	 */
	private String getGameName(String gameName){
		char[] arr = gameName.toCharArray();
		StringBuilder builder = new StringBuilder("");
		int size = arr.length;
		
		for (int i = 0; i < size; i++) {
			builder.append(arr[i]).append("\n");
		}
		builder.substring(0, builder.length()-1);
		return builder.toString();
	}

	@Override
	public void onPause() {
		super.onPause();
		Controller.getInstance().removeCallback(mCallBack);
	}

	@Override
	protected void lazyLoad() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_choose_bs_layout:
			// 倍数点击事件
			mBSAnimation.startAnimation(id_bs_choose_layout);
			break;
		case R.id.id_exchange_num:
			// 换一注
			ecChangeDLTNum();
			break;
		case R.id.id_dlt_bei_10:
			// 投注倍数10
			changeBS(R.id.id_dlt_bei_10);
			break;
		case R.id.id_dlt_bei_20:
			// 投注倍数20
			changeBS(R.id.id_dlt_bei_20);
			break;
		case R.id.id_dlt_bei_50:
			// 投注倍数50
			changeBS(R.id.id_dlt_bei_50);
			break;
		case R.id.id_dlt_tz:
			// 大乐透投注
			dltTZ();
			break;
		case R.id.button_load:
			
			switch (Integer.parseInt(NoDataUtils.getmErrCode())) {
			
			case CException.NET_ERROR:
			case CException.IOERROR:
				initDatas();
				break;
			case 0:
				//2,兑换记录、我的红包-前往商城
				switch (NoDataUtils.getmType()) {
				case 1:
					startActivity(LotteryFootballActivity.class);
					break;
				default:
					break;
				}
			default:
				break;
			}
			break;

		default:
			break;
		}

	}

	/**
	 * 修改选中倍数
	 * 
	 * @param idDltBei10
	 */
	private void changeBS(int id) {
		resetBS(id);
		mBSAnimation.startAnimation(id_bs_choose_layout);
	}

	/**
	 * 重置倍数
	 * 
	 * @param id
	 */
	private void resetBS(int id) {
		id_dlt_bei_10.setBackgroundColor(getActivity().getResources().getColor(
				R.color.layout_bg_color));
		id_dlt_bei_20.setBackgroundColor(getActivity().getResources().getColor(
				R.color.layout_bg_color));
		id_dlt_bei_50.setBackgroundColor(getActivity().getResources().getColor(
				R.color.layout_bg_color));
		id_dlt_bei_10.setTextColor(getActivity().getResources().getColor(
				R.color.color_black));
		id_dlt_bei_20.setTextColor(getActivity().getResources().getColor(
				R.color.color_black));
		id_dlt_bei_50.setTextColor(getActivity().getResources().getColor(
				R.color.color_black));
		switch (id) {
		case R.id.id_dlt_bei_10:
			// 投注倍数10
			id_dlt_bei_10.setBackgroundColor(getActivity().getResources()
					.getColor(R.color.color_red));
			id_dlt_bei_10.setTextColor(getActivity().getResources().getColor(
					R.color.color_white));
			id_pay_lemi_num.setText("共计：20乐米");
			id_pay_lemi_num.setTag("10");
			id_tz_bs_show.setText("10倍");
			break;
		case R.id.id_dlt_bei_20:
			// 投注倍数20
			id_dlt_bei_20.setBackgroundColor(getActivity().getResources()
					.getColor(R.color.color_red));
			id_dlt_bei_20.setTextColor(getActivity().getResources().getColor(
					R.color.color_white));
			id_pay_lemi_num.setText("共计：40乐米");
			id_pay_lemi_num.setTag("20");
			id_tz_bs_show.setText("20倍");
			break;
		case R.id.id_dlt_bei_50:
			// 投注倍数50
			id_dlt_bei_50.setBackgroundColor(getActivity().getResources()
					.getColor(R.color.color_red));
			id_dlt_bei_50.setTextColor(getActivity().getResources().getColor(
					R.color.color_white));
			id_pay_lemi_num.setText("共计：100乐米");
			id_pay_lemi_num.setTag("50");
			id_tz_bs_show.setText("50倍");
			break;
		default:
			break;
		}
	}

	/** 号码TextVeiw集合 */
	private List<TextView> mNumList = null;
	/** 红蓝号码 */
	private ArrayList<Integer> redList, blueList;

	private DecimalFormat format;

	/**
	 * 随机选大乐透号码
	 */
	private void ecChangeDLTNum() {
		if (mNumList == null) {
			mNumList = new ArrayList<TextView>();
		} else
			mNumList.clear();
		id_flowlayout.removeAllViews();
		if (redList == null) {
			redList = new ArrayList<Integer>();
			blueList = new ArrayList<Integer>();
		} else {
			blueList.clear();
			redList.clear();
		}
		LotteryShowUtil.randomBall(redList, 5, 35);
		LotteryShowUtil.randomBall(blueList, 2, 12);
		format = new DecimalFormat("00");
		if (format == null) {
			format = new DecimalFormat("00");
		}
		int redSize = redList.size();
		for (int i = 0; i < redSize; i++) {
			TextView tv = createNum(redList.get(i), true);
			mNumList.add(tv);
			id_flowlayout.addView(tv);
		}
		int blueSize = blueList.size();
		for (int i = 0; i < blueSize; i++) {
			TextView tv = createNum(blueList.get(i), false);
			mNumList.add(tv);
			id_flowlayout.addView(tv);
		}
		startAnimation(0);
	}

	/**
	 * 创建号码
	 * 
	 * @param i号码
	 * @param isRed是否红球
	 * @return
	 */
	private TextView createNum(int i, boolean isRed) {
		LayoutParams params = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(0, 0, 0, 3);
		TextView tv = new TextView(getActivity());
		int color = 0;
		if (isRed) {
			//2.7期修改 去掉50*50的小红蓝球  由于该页面暂时不用 所以先注释了
//			color = R.drawable.icon_hall_redball;
		} else
//			color = R.drawable.icon_hall_blueball;
		tv.setBackgroundResource(color);
		tv.setLayoutParams(params);
		tv.setTextSize(14);
		tv.setGravity(Gravity.CENTER);
		tv.setText(format.format(i + 1));
		tv.setTextColor(mFragmentActivity.getResources()
				.getColor(R.color.color_white));

		return tv;
	}

	/** 选号的动画 */
	private void startAnimation(int index) {
		if (index >= mNumList.size()) {
			return;
		}
		for (int i = 0; i < mNumList.size(); i++) {

			Animation animation = AnimationUtils.loadAnimation(
					mFragmentActivity, R.anim.rotate_scale_360_time);
			TextView textView = mNumList.get(i);
			// 开启动画
			textView.startAnimation(animation);
		}
	}

	private boolean isTZ = false;

	/** 大乐透投注 */
	private void dltTZ() {
		if (mPeriod == null&& GlobalConstants.personTagIndex == 0) {
			Controller.getInstance().getLoBoPeriod(
					GlobalConstants.NUM_LOTTERY_PRE, GlobalConstants.TC_DLT,
					mCallBack);
			isTZ = true;
			return;
		}
		final PromptDialog_Black_Fillet dialog1 = new PromptDialog_Black_Fillet(
				mFragmentActivity);
		final String multipe = id_pay_lemi_num.getTag().toString();
		final String price = Integer.parseInt(multipe) * 2 + "";
		dialog1.setMessage(Html.fromHtml(getResources().getString(
				R.string.lottery_betting_text)
				+ "<font color='"
				+ getResources().getColor(R.color.color_red)
				+ "'>" + price + "</font>" + "米"));
		dialog1.setNegativeButton(getString(R.string.btn_canl),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog1.hideDialog();
					}
				});
		dialog1.setPositiveButton(getString(R.string.btn_ok),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog1.hideDialog();

						if (App.userInfo == null) {
							// 未登录、进入登陆页面
							startActivity(new Intent(mFragmentActivity,
									LoginActivity.class));
						} else {
							if (NetUtil.getNetworkType(getActivity()) == 0) {
								ToastUtil.shortToast(getActivity(),
										"网络错误，请检查网络并重试");
								return;
							}
							// if (mPeriod==null) {
							// Controller.getInstance().getLoBoPeriod(
							// GlobalConstants.NUM_LOTTERY_PRE,
							// GlobalConstants.TC_DLT,mCallBack );
							// }

							StringBuilder sb = new StringBuilder();
							sb.append(LotteryBettingUtil.tzDlt(redList,
									blueList));

							Map<Integer, Map<String, String>> lists = new LinkedHashMap<Integer, Map<String, String>>();
							Map<String, String> bettingReq = new LinkedHashMap<String, String>();

							bettingReq.put("codePlay", "101011001");
							// 选中的注码
							bettingReq.put("codeContent", sb.toString());
							// 单个倍数
							bettingReq.put("codeMultiple", "1");
							// 单个钱数
							bettingReq.put(
									"codeMoney",
									(Integer.parseInt(price) / Integer
											.parseInt(multipe)) + "");
							// 注数
							bettingReq.put("codeNumbers", "1");

							lists.put(0, bettingReq);

							TzObj obj = new TzObj();
							obj.bettingType = "1";
							obj.gameNo = GlobalConstants.TC_DLT;
							obj.issue = mPeriod;
							obj.multiple = multipe;
							obj.totalSum = price; // 订单金额
							obj.chase = "1";
							obj.isChase = "0";
							obj.choiceType = "1";
							obj.planEndTime = "";
							obj.stopCondition = "0";
							obj.gameType = GlobalConstants.TC_DLT;
							obj.lotteryName = 100;
							obj.bettingReq = lists;

							String moneyString = getResources().getString(
									R.string.all);
							String num = String.valueOf(1);
							String numString = getResources().getString(
									R.string.betting1);
							String mult = multipe;
							String multString = getResources().getString(
									R.string.cart_add_bei);
							String term = "1";
							String termString = getResources().getString(
									R.string.cart_add_qi);
							String totalNum = moneyString + num + numString
									+ mult + multString + term + termString;

							Intent intent = new Intent(mFragmentActivity,
									CommitPayActivity.class);
							intent.putExtra(CommitPayActivity.DATA, obj);
							intent.putExtra("desc", totalNum);
							startActivity(intent);
						}
					}
				});
		dialog1.showDialog();
	}

	/** 是否是刷新数据 */
	private boolean isRefresh = true;

	/** 刷新数据 */
	public void refreshDatas() {
		isRefresh = true;
		requestPage = 1;
		initDatas();
	}

	/** 加载数据 */
	public void loadDatas() {
		isRefresh = false;
		initDatas();
	}

	/** 监听标签改变 */
	private PersonInflLMTZLinstener mTagChangeListener;

	public void setTagChangeLinstener(PersonInflLMTZLinstener listener) {
		mTagChangeListener = listener;
	}

	/**
	 * 修改当前选中标签
	 * 
	 * @author 秋风
	 * 
	 */
	interface PersonInflLMTZLinstener {
		/**
		 * 修改当前选中tag
		 * 
		 * @param tag
		 */
		void setIndex(int tag);

		/***
		 * 信息加载完成
		 * 
		 * @param isCanLoadDatas
		 */
		void onLoadFinish(boolean isCanLoadDatas);

		void onRefreshFinish(boolean isCanLoadDatas);

		int getTabIndex();
		void showLodingDialog();
		void hideLodingDialog();

	}
}
