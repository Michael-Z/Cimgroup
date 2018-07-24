package cn.com.cimgroup.frament;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.BuyRedPacketActivity;
import cn.com.cimgroup.activity.CardAddCardActivity;
import cn.com.cimgroup.activity.CenterOutCashActivity;
import cn.com.cimgroup.activity.CenterReChargeActivity;
import cn.com.cimgroup.activity.MainActivity;
import cn.com.cimgroup.activity.MessageCenterActivity;
import cn.com.cimgroup.activity.MessagePersonActivity;
import cn.com.cimgroup.activity.SettingActivity;
import cn.com.cimgroup.activity.UserManageActivity;
import cn.com.cimgroup.bean.NoticeOrMessageCount;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.dailog.BottomToastDialog;
import cn.com.cimgroup.dailog.BottomToastDialog.OnSelectItemClickListener;
import cn.com.cimgroup.dailog.BottomToastDialog.SelectItemColor;
import cn.com.cimgroup.dailog.DialogProgress;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.frament.PersonInfoCashFragment.OnCashFragmentListener;
import cn.com.cimgroup.frament.PersonInfoLMDetailFragment.OnPersonIfnoLMDetailListener;
import cn.com.cimgroup.frament.PersonInfoLMExchangeRecordFragment.OnPersonInfoLEMIExchangeListener;
import cn.com.cimgroup.frament.PersonInfoLMTZFragment.PersonInflLMTZLinstener;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.PopupWndSwitch_TzList_New;
import cn.com.cimgroup.popwindow.PopupWndSwitch_TzList_New.OnTypeItemClickListener;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.view.CircleImageView;
import cn.com.cimgroup.view.HorizontalScrollTabStrip;
import cn.com.cimgroup.view.HorizontalScrollTabStrip.TagChangeListener;
import cn.com.cimgroup.view.MoveScrollView;
import cn.com.cimgroup.view.MoveScrollView.OnScrollListener;
import cn.com.cimgroup.xutils.NetUtil;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 个人中心
 * 
 * @author 秋风
 * 
 */
@SuppressLint({ "Recycle", "ResourceAsColor", "InflateParams",
		"ClickableViewAccessibility", "HandlerLeak" })
public class PersonInfoFragment extends BaseFrament implements OnClickListener,
		OnTypeItemClickListener, OnScrollListener, TagChangeListener {
	/**等待加载框*/
	private DialogProgress mDialog = null;

	/** 整个页面view */
	private View mView;
	/** 存在未读消息时的小点 */
	private ImageView iv_red_point;

	/** 标识当前界面是true：乐米显示，还是false：现金显示 */
	private boolean mShowType = true;

	/** ScrollView */
	private MoveScrollView id_scrollview;
	/** ScrollView 的直接子级 */
	private LinearLayout id_scrollview_child_layout;

	/** ScrollView 的外层 Layout */
	private LinearLayout id_fragment_scroll_linearlayout;

	// private LinearLayout id_person_info_replace_layout;

	private LinearLayout id_add_layout;
	/** 购买布局的高度 */
	private int mMoveLayoutHeight;
	/** MoveScrollView与其父类布局的顶部距离 */
	private int mScrollViewTop;
	/** 购买布局与其父类布局的顶部距离--单项 */
	private int mSearchLayoutTopSingle;
	/** 可隐藏头部 */
	private LinearLayout id_fragment_header_layout;
	/** 标题栏布局 */
	private RelativeLayout id_title_layout;
	/** 个人中心碎片集合 */
	private List<Fragment> mFragments = null;
	/** 当前显示的Fragment */
	private Fragment mShowFragment = null;

	/** 个人中心当前选择的tag索引 */
	private int persontagIndex = 0;
	/** 切换乐米模块按钮 */
	private TextView id_change_lemi_model;
	/** 切换现金模块按钮 */
	private TextView id_change_cash_model;

	/** 用户昵称 */
	private TextView id_person_nickname;
	/** 金额显示 */
	private TextView id_person_wealth;
	/** 红包信息 */
	private TextView id_red_package_count;
	/** 购买乐米 */
	private TextView id_buy_lemi;
	/** 用户头像 */
	private CircleImageView id_person_header;
	/** 中间navi布局 */
	private LinearLayout id_navi_layout;
	/** 筛选我的投注玩法Pop */
	private PopupWndSwitch_TzList_New popList;
	/** pop背景 */
	private LinearLayout id_pop_bg_layout;
	/** 玩法类型标题数据源 */
	private List<String> mTitles;

	private ImageLoader mImageLoader;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;

	// /** 屏幕高度 */
	// private int mScreenHeight = 0;
	
	/**红色*/
	private int colorRed = 0;
	/**灰色*/
	private int colorGray =0;
	/**字体黑色*/
	private int colorBlack = 0;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_person_info, null);
			
			
			mScreenWidth = DensityUtils.getScreenWidth(getActivity());
			// mScreenHeight = DensityUtils.getScreenHeight(getActivity());
			// rect = new Rect(0, DensityUtils.dip2px(50), mScreenWidth,
			// mScreenHeight);
			// 初始化通用视图组件
			initView();
			// id_person_info_replace_layout = (LinearLayout) mView
			// .findViewById(R.id.id_person_info_replace_layout);
			id_add_layout = (LinearLayout) mView.findViewById(R.id.id_add_layout);
			
			colorRed = getActivity().getResources().getColor(R.color.color_red);
			colorGray = getActivity().getResources().getColor(R.color.color_gray_indicator);
 			colorBlack = getActivity().getResources().getColor(R.color.color_gray_secondary);
			
			// 初始化导航栏
			initNavi();
			// 默认加载乐米模块显示
			initFragment();
			initEvent();
			resetNaviTag();
			if (popList == null) {
				initTitles();
				popList = new PopupWndSwitch_TzList_New(getActivity(),
						id_pop_bg_layout, getShowTagIndex(), mTitles);
				popList.setOnTypeItemClickListener(this);
			}
			// 初始化下拉刷新头部、上拉下载底部
			initHeaderAndFooter(inflater);
			setScrollViewTouchListener();
		} 
		ViewGroup parent = (ViewGroup) mView.getParent();
		if (null != parent) {
			parent.removeView(mView);
		}
		
		return mView;
	}

	/** 标识是否可以进行加载更多的操作 */
	private boolean mIsCanLoadDatas = true;
	/** 下拉刷新头部 */
	private LinearLayout scrollview_refresh_header_layout;
	/** 上拉加载底部 */
	private LinearLayout scrollview_load_footer_layout;

	/** 头部高度 */
	private int headerHeight;
	/** 底部高度 */
	private int footerHeight;

	/** 最后一次调用MoveHeader的Padding */
	private int lastHeaderPadding;
	/** 最后一次MoveFooter的Margin */
	private int lastFooterMargin;
	/** 头部显示文字 */
	private TextView id_header_toast_title;
	/** footer提示文字 */
	private TextView id_footer_toast_title;

	/**
	 * 初始化下拉刷新头部、上拉加载底部
	 */
	private void initHeaderAndFooter(LayoutInflater inflater) {
		scrollview_refresh_header_layout = (LinearLayout) View.inflate(
				getActivity(), R.layout.scrollview_refresh_header_layout_noarrow, null);
		scrollview_load_footer_layout = (LinearLayout) View.inflate(
				getActivity(), R.layout.scrollview_load_footer_layout_noarrow, null);

		ViewUtils.measureView(scrollview_refresh_header_layout);
		ViewUtils.measureView(scrollview_load_footer_layout);

		headerHeight = scrollview_refresh_header_layout.getMeasuredHeight();
		footerHeight = scrollview_load_footer_layout.getMeasuredHeight();

		lastHeaderPadding = (-1 * headerHeight); // 最后一次调用Move Header的Padding
		lastFooterMargin = -1 * footerHeight;
		// 初始化头部padding
		scrollview_refresh_header_layout.setPadding(0, lastHeaderPadding, 0, 0);
		// 初始化底部Margin
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(0, 0, 0, lastFooterMargin);
		scrollview_load_footer_layout.setLayoutParams(lp);
		scrollview_load_footer_layout.invalidate();
		// 初始化文字
		id_header_toast_title = (TextView) scrollview_refresh_header_layout
				.findViewById(R.id.id_header_toast_title);

		id_footer_toast_title = (TextView) scrollview_load_footer_layout
				.findViewById(R.id.id_footer_toast_title);
		// 添加
		id_add_layout.addView(scrollview_refresh_header_layout, 0);
		id_fragment_scroll_linearlayout.addView(scrollview_load_footer_layout);
	}

	private int headerState = DONE;// 头部状态，默认完成
	private int footerState = DONE;
	private static final int RELEASE_To_REFRESH = 0;
	private static final int PULL_To_REFRESH = 1;
	private static final int REFRESHING = 2;
	private static final int DONE = 3;
	private boolean isBack; // 从Release 转到 pull

	/**
	 * 设置ScrollView触摸监听
	 */
	private void setScrollViewTouchListener() {
		// TODO 触摸监听
		id_scrollview.setOnTouchListener(new OnTouchListener() {
			// 手指的位置
			int beginY = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					if (beginY == 0) {
						beginY = (int) ((int) event.getY());
					}
					int interval = (int) (event.getY() - beginY);
					// 处理上拉刷新
					if ((v.getScrollY() == 0) && headerState != REFRESHING) {
						interval = (int) (interval / 2.5);// 下滑阻力
						// 拿到滑动的Y轴距离
						// 是向下滑动而不是向上滑动
						if (interval > 0) {
							lastHeaderPadding = interval + (-1 * headerHeight);
							scrollview_refresh_header_layout.setPadding(0,
									lastHeaderPadding, 0, 0);
							float alpha=0;
							if (lastHeaderPadding < 0) {
								alpha = ((headerHeight + lastHeaderPadding)*1.0f)
										/ (headerHeight*1.0f);
							}else
								alpha = 1;
							scrollview_refresh_header_layout.setAlpha(alpha);
							if (lastHeaderPadding > 0) {
								headerState = RELEASE_To_REFRESH;
								// 是否已经更新了UI
								if (!isBack) {
									isBack = true;
									changeHeaderViewByState();
								}
							} else {
								headerState = PULL_To_REFRESH;
								changeHeaderViewByState();
							}
						}
					}
					// 处理下拉加载
					if (mIsCanLoadDatas && interval < 0) {
						if ((id_scrollview_child_layout.getMeasuredHeight() - 2 <= //
						(id_scrollview.getScrollY() + id_scrollview.getHeight())//
						|| lastFooterMargin > (-footerHeight))//
								&& headerState != REFRESHING) {
							// 拿到滑动的Y轴距离
							interval = (int) (interval / 2);// 上滑阻力
							lastFooterMargin = -1 * (interval + footerHeight);
							LayoutParams lp = (LayoutParams) scrollview_load_footer_layout
									.getLayoutParams();
							lp.setMargins(0, 0, 0, lastFooterMargin);
							scrollview_load_footer_layout.setLayoutParams(lp);
							if (lastFooterMargin > 0) {
								footerState = RELEASE_To_REFRESH;
								// 是否已经更新了UI
								if (!isBack) {
									isBack = true; // 到了Release状态，如果往回滑动到了pull
									changeFooterViewByState();
								}
							} else {
								footerState = PULL_To_REFRESH;
								changeFooterViewByState();
							}
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					beginY = 0;
					if (headerState != REFRESHING) {
						switch (headerState) {
						case DONE:
							// 什么也不干
							break;
						case PULL_To_REFRESH:
							headerState = DONE;
							lastHeaderPadding = -1 * headerHeight;
							scrollview_refresh_header_layout.setPadding(0,
									lastHeaderPadding, 0, 0);
							changeHeaderViewByState();
							break;
						case RELEASE_To_REFRESH:
							isBack = false; // 准备开始刷新，此时将不会往回滑动
							headerState = REFRESHING;
							changeHeaderViewByState();
							// 刷新数据
							onRefreshDatas();
							break;
						}
					}
					if (footerState != REFRESHING) {
						switch (footerState) {
						case DONE:
							// 什么也不干
							break;
						case PULL_To_REFRESH:
							footerState = DONE;
							lastFooterMargin = -footerHeight;
							LayoutParams lp = (LayoutParams) scrollview_load_footer_layout
									.getLayoutParams();
							lp.setMargins(0, 0, 0, 1 * (lastFooterMargin));
							scrollview_load_footer_layout.setLayoutParams(lp);
							changeFooterViewByState();
							break;
						case RELEASE_To_REFRESH:
							isBack = false; // 准备开始刷新，此时将不会往回滑动
							footerState = REFRESHING;
							changeFooterViewByState();
							// 加载数据
							onLoadDatas();
							break;
						default:
							break;
						}
					}
					break;
				}
				// 如果Header是完全被隐藏的则让ScrollView正常滑动，让事件继续否则的话就阻断事件
				if ((lastHeaderPadding > (-1 * headerHeight) && headerState != REFRESHING)
						&& (lastFooterMargin > (-1 * footerHeight) && footerState != REFRESHING)) {
					return true;
				} else {

					return false;
				}
			}
		});
	}

	/** 刷新数据 */
	protected void onRefreshDatas() {
		// TODO 刷新数据
		if (!mShowType) {
			// 现金模块
			cashFragment.refreshDatas();
		} else {
			switch (persontagIndex) {
			case 0:
				// 乐米投注
				tzFragment.refreshDatas();
				break;
			case 1:
				// 乐米明细
				detailFragment.refreshDatas();
				break;
			case 2:
				// 兑换记录
				exchangeFragment.refreshDatas();
				break;
			default:
				break;
			}
		}
	}

	/** 加载数据 */
	protected void onLoadDatas() {
		// TODO 加载数据
		if (!mShowType) {
			// 现金模块
			cashFragment.loadDatas();
		} else {
			switch (persontagIndex) {
			case 0:
				// 乐米投注
				tzFragment.loadDatas();
				break;
			case 1:
				// 乐米明细
				detailFragment.loadDatas();
				break;
			case 2:
				// 兑换记录
				exchangeFragment.loadDatas();
				break;

			default:
				break;
			}
		}
	}

	// /** 屏幕显示矩形（去头） */
	// private Rect rect = null;
	


	/**
	 * ScollView的滚动监听
	 */
	private boolean isScroll = true;
	@Override
	public void onScroll(int scrollY) {
		// 如果是现金Fragment、乐米明细或者兑换记录
		if (isScroll) {
			isScroll = false;
			return ;
		}
		if (!mShowType) {
			// TODO 现金碎片
			// 第一种判断逻辑
			// int[] location = new int[2];
			// id_navi_layout.getLocationInWindow(location);
			// if (!id_navi_layout.getLocalVisibleRect(rect)) {
			// showCashSuspend();
			// resetTagLocation_Line();
			// } else {
			// removeCashSuspend();
			// }
			// 第二种判断逻辑
			if (scrollY >= mMoveLayoutHeight + mSearchLayoutTopSingle) {
				showCashSuspend();
				resetTagLocation_Line();
			} else if (scrollY <= mScrollViewTop + mMoveLayoutHeight
					+ mSearchLayoutTopSingle) {
				removeCashSuspend();
			}
		} else {
			switch (persontagIndex) {
			case 0:
				// 我的投注碎片
				if (scrollY >= mMoveLayoutHeight) {
					showDoubleSuspend();
					resetTab_Line_Location();
					changeTabTextColor();
					
				} else if (scrollY <= mScrollViewTop + mMoveLayoutHeight) {
					removeDoubleSuspend();
				}
				break;
			default:
				// 1：乐米明细
				// 2：兑换记录
				if (scrollY >= mMoveLayoutHeight) {
					showSingleSuspend();
					resetSingleTabState();
				} else if (scrollY <= mScrollViewTop + mMoveLayoutHeight) {
					removeSingleSuspend();
				}
				break;
			}
		}
		// }
	};
	
	

	/***
	 * 头部状态改变时
	 */
	private void changeHeaderViewByState() {
		switch (headerState) {
		case PULL_To_REFRESH:
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				id_header_toast_title.setText("下拉刷新...");
			}
			id_header_toast_title.setText("下拉刷新...");
			break;
		case RELEASE_To_REFRESH:
			id_header_toast_title.setText("放开可以刷新...");
			break;
		case REFRESHING:
			lastHeaderPadding = 0;
			scrollview_refresh_header_layout.setPadding(0, lastHeaderPadding,
					0, 0);
			scrollview_refresh_header_layout.invalidate();
			// 刷新完成后，默认将焦点附加给ViewPager 强迫ScrollView滚动到顶端
			id_header_toast_title.setText("正在刷新...");
			break;
		case DONE:
			scrollview_refresh_header_layout.setAlpha(0f);
			lastHeaderPadding = -1 * headerHeight;
			scrollview_refresh_header_layout.setPadding(0, lastHeaderPadding,
					0, 0);
			scrollview_refresh_header_layout.invalidate();
			id_header_toast_title.setText("下拉刷新...");
			break;
		default:
			break;
		}
	}

	/**
	 * 更新底部状态
	 */
	private void changeFooterViewByState() {
		switch (footerState) {
		case PULL_To_REFRESH:
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
				id_footer_toast_title.setText("上拉加载...");
			}

			break;
		case RELEASE_To_REFRESH:
			id_footer_toast_title.setText("放开可以加载...");
			break;
		case REFRESHING:
			lastFooterMargin = 0;
			LayoutParams lp = (LayoutParams) scrollview_load_footer_layout
					.getLayoutParams();
			lp.setMargins(0, 0, 0, lastFooterMargin);
			scrollview_load_footer_layout.setLayoutParams(lp);
			id_footer_toast_title.setText("正在加载...");
			break;
		case DONE:
			lastFooterMargin = -footerHeight;

			LayoutParams llp = (LayoutParams) scrollview_load_footer_layout
					.getLayoutParams();
			llp.setMargins(0, 0, 0, lastFooterMargin);
			scrollview_load_footer_layout.setLayoutParams(llp);
			id_footer_toast_title.setText("上拉加载...");
			break;
		default:
			break;
		}
	}

	/**
	 * 刷新完成后
	 */
	public void onRefreshComplete() {
		headerState = DONE;
		changeHeaderViewByState();
	}

	/**
	 * 加载结束后
	 */
	public void onLoadComplete() {
		footerState = DONE;
		changeFooterViewByState();
	}

	/**
	 * 初始化通用视图组件
	 */
	private void initView() {
		iv_red_point = (ImageView) mView.findViewById(R.id.iv_red_point);

		id_scrollview = (MoveScrollView) mView.findViewById(R.id.id_scrollview);
		id_scrollview_child_layout = (LinearLayout) mView
				.findViewById(R.id.id_scrollview_child_layout);
		id_fragment_scroll_linearlayout = (LinearLayout) mView
				.findViewById(R.id.id_fragment_scroll_linearlayout);
		id_fragment_header_layout = (LinearLayout) mView
				.findViewById(R.id.id_fragment_header_layout);
		id_title_layout = (RelativeLayout) mView
				.findViewById(R.id.id_title_layout);
		id_navi_layout = (LinearLayout) mView.findViewById(R.id.id_navi_layout);

		id_my_tz_title = (TextView) mView.findViewById(R.id.id_my_tz_title);

		id_person_nickname = (TextView) mView
				.findViewById(R.id.id_person_nickname);

		id_change_lemi_model = (TextView) mView
				.findViewById(R.id.id_change_lemi_model);
		id_change_cash_model = (TextView) mView
				.findViewById(R.id.id_change_cash_model);

		id_person_wealth = (TextView) mView.findViewById(R.id.id_person_wealth);
		id_red_package_count = (TextView) mView
				.findViewById(R.id.id_red_package_count);
		id_red_package_count.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		id_red_package_count.getPaint().setAntiAlias(true);
		id_buy_lemi = (TextView) mView.findViewById(R.id.id_buy_lemi);

		id_person_header = (CircleImageView) mView
				.findViewById(R.id.id_person_header);

	}

	/** 乐米模块导航 */
	private LinearLayout id_navigation_lemi_layout;
	/** 现金模块导航 */
	private LinearLayout id_navigation_cash_layout;

	/** 我的投注类型展开状态 */
	private final static String TAG_0 = "0";
	/** 我的投注类型隐藏状态 */
	private final static String TAG_1 = "1";
	/** 当前切换为现金状态 */
	private final static String TAG_2 = "2";
	/** 我的投注面板是否显示0,显示；1不显示；2现金模板 */
	private String tag = TAG_1;
	/** 我的投注 */
	private LinearLayout id_navi_my_tz;
	/** 我的投注--标题 */
	private TextView id_my_tz_title;
	/** 乐米投注箭头图片 */
	private ImageView id_my_tz_icon;
	/** 乐米详情 */
	private TextView id_lemi_details;
	/** 兑换记录 */
	private TextView id_exchange_record;

	/** 初始化导航栏 */
	private void initNavi() {
		id_pop_bg_layout = (LinearLayout) mView
				.findViewById(R.id.id_pop_bg_layout);
		id_navigation_lemi_layout = (LinearLayout) mView
				.findViewById(R.id.id_navigation_lemi_layout);
		id_navi_my_tz = (LinearLayout) mView.findViewById(R.id.id_navi_my_tz);
		id_my_tz_icon = (ImageView) mView.findViewById(R.id.id_my_tz_icon);
		id_lemi_details = (TextView) mView.findViewById(R.id.id_lemi_details);
		id_exchange_record = (TextView) mView
				.findViewById(R.id.id_exchange_record);
		id_navigation_cash_layout = (LinearLayout) mView
				.findViewById(R.id.id_navigation_cash_layout);
	}

	/** Fragment管理者 */
	private FragmentManager mManager;
	/** 碎片--我的投注 */
	private PersonInfoLMTZFragment tzFragment;
	/** 碎片--乐米明细 */
	private PersonInfoLMDetailFragment detailFragment;
	/** 碎片--兑换记录 */
	private PersonInfoLMExchangeRecordFragment exchangeFragment;
	/** 碎片--资金明细 */
	private PersonInfoCashFragment cashFragment;
	/** 我的投注标签选择 */
	private int mTagIndex = 0;

	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		mFragments = new ArrayList<Fragment>();
		tzFragment = PersonInfoLMTZFragment.newInstance(mTagIndex + "");
		detailFragment = PersonInfoLMDetailFragment.newInstance("2");
		exchangeFragment = PersonInfoLMExchangeRecordFragment.newInstance("3");
		cashFragment = PersonInfoCashFragment.newInstance("4");
		mFragments.add(tzFragment);
		mFragments.add(detailFragment);
		mFragments.add(exchangeFragment);
		mFragments.add(cashFragment);
	}
	
	/**
	 * 切换Fragment
	 * 
	 * @param position
	 */
	private void changeFragment(int position) {
		if (null == mManager) {
			mManager = getChildFragmentManager();
		}
		FragmentTransaction transaction = mManager.beginTransaction();
		if (position == 0) {
			Bundle bundle = new Bundle();
			if (GlobalConstants.personEndIndex!=-1) {
				mTagIndex = GlobalConstants.personEndIndex;
			}
			bundle.putString("tag", mTagIndex + "");
			mFragments.get(position).setArguments(bundle);
		}
		mShowFragment = mFragments.get(position);
		transaction.replace(R.id.id_person_info_replace_layout, mShowFragment,
				"" + position);
		mManager.executePendingTransactions();
		transaction.commitAllowingStateLoss();
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		// 头部电话、设置
		mView.findViewById(R.id.id_setting).setOnClickListener(this);
		mView.findViewById(R.id.id_message).setOnClickListener(this);
		// 乐米、现金切换
		id_change_lemi_model.setOnClickListener(this);
		id_change_cash_model.setOnClickListener(this);
		//跳转我的红包
		id_red_package_count.setOnClickListener(this);
		// 头像
		id_person_header.setOnClickListener(this);
		// 投注类型
		id_navi_my_tz.setOnClickListener(this);
		id_lemi_details.setOnClickListener(this);
		id_exchange_record.setOnClickListener(this);
		//充值
		mView.findViewById(R.id.id_person_rechange).setOnClickListener(this);
		//提现
		mView.findViewById(R.id.id_person_outcash).setOnClickListener(this);
		
		id_buy_lemi.setOnClickListener(this);
		// 为乐米投注碎片绑定监听
		tzFragment.setTagChangeLinstener(new PersonInflLMTZLinstener() {

			@Override
			public void setIndex(int tag) {
				mTagIndex = tag;
				if (id_navi_mytz_all!=null) {
					changeTabTextColor();
				}
				
			}

			@Override
			public void onRefreshFinish(boolean isCanLoadDatas) {
				finishRefresh(isCanLoadDatas);
			}

			@Override
			public void onLoadFinish(boolean isCanLoadDatas) {
				finishLoading(isCanLoadDatas);
			}

			@Override
			public int getTabIndex() {
				return mTagIndex;
			}

			@Override
			public void showLodingDialog() {
				showDialog();
			}

			@Override
			public void hideLodingDialog() {
				hideDialog();
			}

		});
		// 为乐米明细添加监听
		detailFragment
				.setOnPersonIfnoLMDetailListener(new OnPersonIfnoLMDetailListener() {

					@Override
					public void onRefreshFinish(boolean isCanLoadDatas) {
						finishRefresh(isCanLoadDatas);
					}

					@Override
					public void onLoadFinish(boolean isCanLoadDatas) {
						finishLoading(isCanLoadDatas);
					}

					@Override
					public void showLodingDialog() {
						showDialog();
					}

					@Override
					public void hideLodingDialog() {
						hideDialog();
					}
				});
		exchangeFragment
				.setOnPersonInfoLEMIExchangeListener(new OnPersonInfoLEMIExchangeListener() {

					@Override
					public void onRefreshFinish(boolean isCanLoadDatas) {
						finishRefresh(isCanLoadDatas);
					}

					@Override
					public void onLoadFinish(boolean isCanLoadDatas) {
						finishLoading(isCanLoadDatas);
					}

					@Override
					public void showLodingDialog() {
						showDialog();
					}

					@Override
					public void hideLodingDialog() {
						hideDialog();
					}
				});
		// 为现金设置监听
		cashFragment.setOnCashFragmentListener(new OnCashFragmentListener() {
			@Override
			public void scrollToLocation(int y) {
				id_scrollview.scrollTo(0, y);
			}

			@Override
			public void changeCashTagIndex(int position) {
				mTabPosition = position;
			}

			@Override
			public void onRefreshFinish(boolean isCanLoadDatas) {
				finishRefresh(isCanLoadDatas);
			}

			@Override
			public void onLoadFinish(boolean isCanLoadDatas) {
				finishLoading(isCanLoadDatas);
			}
			@Override
			public void showLodingDialog() {
				showDialog();
			}

			@Override
			public void hideLodingDialog() {
				hideDialog();
			}

		});
		// 设置ScrollView 的滚动监听
		id_scrollview.setOnScrollListener(this);

	}
	/**
	 * 加载结束
	 * @param isCanLoadDatas
	 */
	private void finishLoading(boolean isCanLoadDatas) {
		mIsCanLoadDatas = isCanLoadDatas;
		onLoadComplete();
	}
	/**
	 * 刷新结束
	 * @param isCanLoadDatas
	 */
	private void finishRefresh(boolean isCanLoadDatas) {
		if (isUpdateUserInfo) {
			isUpdateUserInfo = false;
			updateUserInfo();
		}
		onRefreshComplete();
		id_scrollview.smoothScrollTo(0, 1);
		mIsCanLoadDatas = isCanLoadDatas;
	}

	/**
	 * 初始化标题数据源
	 */
	private void initTitles() {
		mTitles = new ArrayList<String>();
		mTitles.add("ALL,全部");
		mTitles.add("TC_JCLQ,竞彩篮球");
		mTitles.add("TC_JCZQ,竞彩足球");
		mTitles.add("TC_SF14,胜负彩");
		mTitles.add("TC_SF9,任九场");
		mTitles.add("TC_JQ4,进球彩");
		mTitles.add("TC_BQ6,半全场");
		mTitles.add("TC_DLT,大乐透");
		mTitles.add("TC_PL3,排列三");
		mTitles.add("TC_QXC,七星彩");
		mTitles.add("TC_PL5,排列五");
	}
	/**
	 * 确定当前选中的tag
	 * @return
	 */
	private int getShowTagIndex(){
		int size =mTitles.size();
		for (int i = 0; i < size; i++) {
			if (mTitles.get(i).split(",")[0].equals(GlobalConstants.personGameNo)) {
				return i;
			}
			
		}
		return 0;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_red_package_count:
			//跳转 我的红包
			jumpMyRedPackage();
			break;
		case R.id.id_buy_lemi:
			Intent buyLeMi = new Intent(getActivity(),BuyRedPacketActivity.class);
			buyLeMi.putExtra(GridFragment.TYPE,GridFragment.BUYLEMI);
			getActivity().startActivity(buyLeMi);
			break;
		case R.id.id_message:
			// 消息查看
			startActivity(MessagePersonActivity.class);
			break;
		case R.id.id_setting:
			// 跳转设置界面
			startActivity(new Intent(getActivity(), SettingActivity.class));
			break;
		case R.id.id_change_lemi_model:
			if (!mShowType) {
				// 隐藏悬浮tab
				hideViewStubLayout();
				// 切换乐米模块
				mShowType = true;
				GlobalConstants.isShowLeMiFragment = true;
				// 重置用户及界面信息
				resetNaviTag();
			}
			break;
		case R.id.id_change_cash_model:
			if (mShowType) {
				// 隐藏悬浮tab
				hideViewStubLayout();
				// 切换现金模块
				mShowType = false;
				GlobalConstants.isShowLeMiFragment = false;
				// 重置用户及界面信息
				resetNaviTag();
			}
			break;
		case R.id.id_person_header:
			// TODO 更换头像
			isChangeHeaderImage=true;
			showSelectPictureMenu();
			break;

		case R.id.id_navi_my_tz:
//			id_scrollview.scrollTo(0, 1);
			id_my_tz_icon.setImageResource(R.drawable.icon_hall_pull_red);
			// 点击我的投注
			if (tag.equals(TAG_0)) {
				id_my_tz_icon.setRotation(0);
				tag = TAG_1;
			} else if (tag.equals(TAG_1)) {
				tag = TAG_0;
				id_my_tz_icon.setRotation(180);
				popList.showOrhideWindow(id_navi_layout,getShowTagIndex());
				tzFragment.setCoverViewState(true);
			} else {
				tag = TAG_1;
				changeFragment(0);
				id_my_tz_title.setTextColor(colorRed);
				id_lemi_details.setTextColor(colorBlack);
				id_exchange_record.setTextColor(colorBlack);
			}
			persontagIndex = 0;
			GlobalConstants.personTagIndex = 0;
			break;
		case R.id.id_lemi_details:
//			id_scrollview.scrollTo(0, 1);
			id_my_tz_icon.setImageResource(R.drawable.icon_hall_pull_black);
			// 点击乐米明细
			if (tag.equals(TAG_0)) {
				id_my_tz_icon.setRotation(0);
				tag = TAG_1;
			} else {
				tag = TAG_2;
				id_lemi_details.setTextColor(colorRed);
				id_my_tz_title.setTextColor(colorBlack);
				id_exchange_record.setTextColor(colorBlack);
				changeFragment(1);
			}
			persontagIndex = 1;
			GlobalConstants.personTagIndex = 1;
			break;
		case R.id.id_exchange_record:
			id_my_tz_icon.setImageResource(R.drawable.icon_hall_pull_black);
			// 点击切换兑换记录
			if (tag.equals(TAG_0)) {
				id_my_tz_icon.setRotation(0);
				tag = TAG_1;
			} else {
				tag = TAG_2;
				id_exchange_record.setTextColor(colorRed);
				id_my_tz_title.setTextColor(colorBlack);
				id_lemi_details.setTextColor(colorBlack);
				// 修改为
				// 重新再添加一个Fragment--兑换记录
				changeFragment(2);
			}
			persontagIndex = 2;
			GlobalConstants.personTagIndex = 2;
			break;
		case R.id.id_person_rechange:
			// 充值
			if (!TextUtils.isEmpty(App.userInfo.getRealName())) {
				startActivity(new Intent(getActivity(),
						CenterReChargeActivity.class));
			} else {
				isValidate();
			}
			break;
		case R.id.id_person_outcash:
			// 提现
			if (TextUtils.isEmpty(App.userInfo.getRealName())) {
				// 去认证
				isValidate();
			} else if (App.userInfo.getIsBindedBank().equals("0")
					|| App.userInfo.getIsBindedBank().equals("0")) {
				// 去绑定银行卡
				isBandBank();

			} else {
				startActivity(new Intent(getActivity(),
						CenterOutCashActivity.class));
			}
			break;
		default:
			break;
		}
	}
	/**跳转我的红包*/
	private void jumpMyRedPackage() {
		Intent intent = new Intent(getActivity(), MessageCenterActivity.class);
		intent.putExtra(MessageCenterActivity.TYPE, MessageCenterActivity.REDPACKET);
		startActivity(intent);		
	}

	/** 弹出选择照片菜单 */
	public void showSelectPictureMenu() {
		new BottomToastDialog(getActivity())
				.builder()
				.setCancelable(true)
				.setTitle("请选择操作")
				.setCanceledOnTouchOutside(true)
				.addSelectItem("拍照", SelectItemColor.Green,
						new OnSelectItemClickListener() {
							@Override
							public void onClick(int which) {
								// 注册广播
								registReceiver();
								// 调用父级Activity启动相机
								((MainActivity) getActivity()).startCamera();
							}
						})
				.addSelectItem("图库", SelectItemColor.Green,
						new OnSelectItemClickListener() {
							@Override
							public void onClick(int which) {
								// 注册广播
								registReceiver();
								// 调用父级Activity启动图库
								((MainActivity) getActivity()).startAlbum();
							}
						}).show();
	}
	/** 隐藏悬浮Tab */
	private void hideViewStubLayout() {
		if (null != id_navi_viewstub_single) {
			id_navi_viewstub_single.setVisibility(View.GONE);
		}
		if (null != id_navi_viewstub_double) {
			id_navi_viewstub_double.setVisibility(View.GONE);
		}
		if (null != id_navi_viewstub_cash) {
			id_navi_viewstub_cash.setVisibility(View.GONE);
		}
	}
	/**
	 * 重置用户及界面信息
	 */
	private void resetNaviTag() {
		if (mImageLoader == null) {
			mImageLoader = ImageLoader.getInstance();
		}
		id_change_lemi_model.setSelected(mShowType);
		id_change_cash_model.setSelected(!mShowType);
		reSetScreenInfo();
	}
	/** 重置界面信息 */
	private void reSetScreenInfo() {
		if (App.userInfo ==null) {
			return ;
		}
		if (!TextUtils.isEmpty(App.userInfo.getHeadImgUrl())) {
			mImageLoader.displayImage(App.userInfo.getHeadImgUrl(),
					id_person_header);
		}
		id_person_nickname.setText(App.userInfo.getUserName());
		if (mShowType) {
			id_change_lemi_model.setTextColor(colorBlack);
			id_change_cash_model.setTextColor(getActivity().getResources()
					.getColor(R.color.color_white));
			// 切换到乐米模块
			id_person_wealth.setText("乐米：" + getUserLemi());
			id_red_package_count.setVisibility(View.GONE);
			id_buy_lemi.setVisibility(View.VISIBLE);
			changeFragment(persontagIndex);

		} else {
			// 切换到现金
			id_buy_lemi.setVisibility(View.GONE);
			id_change_lemi_model.setTextColor(getActivity().getResources()
					.getColor(R.color.color_white));
			id_change_cash_model.setTextColor(colorBlack);
			id_person_wealth.setText("余额：" + getUserCash());
//			Spanned redPackage = Html.fromHtml("<ui>红包："+App.userInfo.redPkgNum+"个</ui>");
			id_red_package_count.setVisibility(View.VISIBLE);
//			id_red_package_count.setText(redPackage);
			id_red_package_count.setText("红包："+App.userInfo.redPkgNum+"个");
			
			changeFragment(3);
		}
		// 切换navi
		resetModelType();
	}
	/**
	 * 切换视图类型:true：乐米显示，还是false：现金显示
	 */
	private void resetModelType() {
		if (mShowType) {
			id_navigation_lemi_layout.setVisibility(View.VISIBLE);
			id_navigation_cash_layout.setVisibility(View.GONE);
		} else {
			id_navigation_lemi_layout.setVisibility(View.GONE);
			id_navigation_cash_layout.setVisibility(View.VISIBLE);
		}
	}

	/** 图片的默认地址 */
	private String mDefaultImageUrl = "drawable://"
			+ R.drawable.icon_center_head;
	/**
	 * 跟新用户信息
	 */
	private void resetUserInfo() {
		if (mImageLoader == null) {
			mImageLoader = ImageLoader.getInstance();
		}
		id_person_nickname.setText(App.userInfo.getUserName());
		if (!TextUtils.isEmpty(App.userInfo.getHeadImgUrl())) {
			mImageLoader.displayImage(App.userInfo.getHeadImgUrl(),
					id_person_header);
		} else {
			mImageLoader.displayImage(mDefaultImageUrl, id_person_header);
		}
		if (mShowType) {
			id_person_wealth.setText("乐米：" + getUserLemi());
		} else {
			id_person_wealth.setText("余额：" + getUserCash());
//			Spanned redPackage = Html.fromHtml("<ui>红包："+App.userInfo.redPkgNum+"个</ui>");
//			id_red_package_count.setText(redPackage);
			id_red_package_count.setText("红包："+App.userInfo.redPkgNum+"个");
		}
	}
	/**重置界面信息*/
	public void resetPageInfo(){
		resetUserInfo();
	}

	/**
	 * 获取用户乐米数
	 * 
	 * @return
	 */
	private String getUserLemi() {
		String moneyStr = "0";
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
			BigDecimal money = new BigDecimal(TextUtils.isEmpty(App.userInfo
					.getIntegralAcnt()) ? "0" : App.userInfo.getIntegralAcnt());

			if (money.doubleValue() > 100000d) {
				moneyStr = money.divide(new BigDecimal("10000"), 2,
						RoundingMode.DOWN).toPlainString()
						+ "万米";
			} else
				moneyStr = money.toPlainString() + "米";
		}
		return moneyStr;
	}
	/**
	 * 获取用户现金余额
	 * 
	 * @return
	 */
	private String getUserCash() {
		String money = "";
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
			BigDecimal bet = new BigDecimal(TextUtils.isEmpty(App.userInfo
					.getBetAcnt()) ? "0" : App.userInfo.getBetAcnt());
			BigDecimal price = new BigDecimal(TextUtils.isEmpty(App.userInfo
					.getPrizeAcnt()) ? "0" : App.userInfo.getPrizeAcnt());
			money = bet.add(price).toPlainString();
			BigDecimal moneyStr = bet.add(price);
			if (moneyStr.doubleValue() > 100000d) {
				money = moneyStr.divide(new BigDecimal("10000"), 2,
						RoundingMode.DOWN).toPlainString()
						+ "万元";
			} else
				money = moneyStr.toPlainString() + "元";

		}
		return money;
	}
	/**
	 * 切换我的投注玩法类型
	 */
	@Override
	public void onSelectItemClick(String tag, String title) {
		// TODO 根据选择不同的彩种类型查询信息
		tzFragment.changeGameNo(tag);
		tzFragment.setCoverViewState(false);
	}
	@Override
	public void onCancel() {
		if (id_mytz_icon != null) {
			id_mytz_icon.setRotation(0);
		}
		id_my_tz_icon.setRotation(0);
		tag = TAG_1;
		tzFragment.setCoverViewState(false);
	}
	@Override
	protected void lazyLoad() {
	}
	
	/** 是否是第一次加载 */
	public boolean isFirstLoad = true;
	/** 是否需要更新用户信息 */
	private boolean isUpdateUserInfo = true;
	private long startTime = 0;
	private long endTime = 0;
	@Override
	public void onResume() {
		super.onResume();
		endTime = System.currentTimeMillis();
		if (endTime - startTime<500) {
			return;
		}
		startTime = endTime;
		isScroll = true;
		// 检查未读消息数量
		isUpdateUserInfo = true;
		if (isFirstLoad) {
			getSomeDatas();
			isFirstLoad = false;
		}
		if (GlobalConstants.isRefreshFragment) {
			// 修改 乐米还是现金显示
			if (GlobalConstants.isShowLeMiFragment) {
				if (mShowType == GlobalConstants.isShowLeMiFragment
						&& persontagIndex != GlobalConstants.personTagIndex) {
					switch (GlobalConstants.personTagIndex) {
					case 0:
						tag = "2";
						onClick(id_navi_my_tz);
						break;
					case 1:
						onClick(id_lemi_details);
						break;
					case 2:
						onClick(id_exchange_record);
						break;
					default:
						break;
					}
				} else {
					onClick(id_change_lemi_model);
				}
				mShowType = true;
			} else {
				onClick(id_navigation_cash_layout);
				mShowType = false;
			}
		}
	}
	/** 检查未读消息数量 */
	private void checkMessageInfo() {
		if (!NetUtil.isConnected(getActivity())) {
			return;
		}
		Controller.getInstance().getNewNoticeOrMessage(
				GlobalConstants.NUM_NOTICORMESSAGE, App.userInfo.getUserId(),
				"0", "1", new CallBack() {
					@Override
					public void getNewNoticeOrMessageSuccess(
							final NoticeOrMessageCount count) {
						super.getNewNoticeOrMessageSuccess(count);
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if (count != null
										&& count.getResCode().equals("0")) {
									String msgRead = count.getIsNotMsg();
									if (!TextUtils.isEmpty(msgRead)) {
										if (msgRead.equals("0")) {
											iv_red_point
													.setVisibility(View.GONE);
										} else {
											iv_red_point
													.setVisibility(View.VISIBLE);
										}
									}
								}
							}
						});
					}

					@Override
					public void getNewNoticeOrMessageFailure(String error) {
						super.getNewNoticeOrMessageFailure(error);
					}
				});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (null != mPhotoReceiver) {
			getActivity().unregisterReceiver(mPhotoReceiver);
			mPhotoReceiver = null;
		}
	}
	
	private boolean isChangeHeaderImage = false;

	/** 获取用户信息 */
	public void updateUserInfo() {
		//如果是切换头像的时候会执行两遍方法，不过这里实际不需要请求网络
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())&&!isChangeHeaderImage) {
			if (!NetUtil.isConnected(getActivity())) {
				return;
			}
			showDialog();
			Controller.getInstance().getUserInfo(
					GlobalConstants.NUM_GETUSERINFO, App.userInfo.getUserId(),
					mCallback);
		}
		isChangeHeaderImage=false;
	}

	private void getSomeDatas() {
		ViewTreeObserver obServerScrollView = id_scrollview
				.getViewTreeObserver();
		obServerScrollView
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						// 获取移动控件的高度
						mMoveLayoutHeight = id_fragment_header_layout
								.getHeight();
						mSearchLayoutTopSingle = DensityUtils.dip2px(42);
						mScrollViewTop = id_title_layout.getMeasuredHeight();
					}
				});
	}

	/** 页面请求 */
	private CallBack mCallback = new CallBack() {
		public void getUserInfoSuccess(final UserInfo info) {
			mFragmentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (info.getResCode() != null
							&& info.getResCode().equals("0")) {
						if (App.userInfo != null
								&& App.userInfo.getUserId() != null) {
							info.setUserId(App.userInfo.getUserId());
						}
						if (App.userInfo != null
								&& App.userInfo.getPassword() != null) {
							info.setPassword(App.userInfo.getPassword());
						}
						App.userInfo = info;
						// 更新本地文件
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						resetUserInfo();
					}
					checkMessageInfo();
					hideDialog();
				}
			});
		};

		public void getUserInfoFailure(String error) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					checkMessageInfo();
					hideDialog();
				}
			});
		}
	};

	/** 单行导航栏 */
	private ViewStub id_navi_viewstub_single;
	/** 双行导航栏 */
	private ViewStub id_navi_viewstub_double;
	/** 现金导航Tab栏 */
	private ViewStub id_navi_viewstub_cash;

	/** 停靠tab 横线位置，int[0]代表横线横向位置,int[1]代表当前选项卡index */
	private int[] mLineLocation = null;
	/** 停靠tab TagView位置 */
	private int[] mViewLocation = null;
	/** 停靠tab 横线当前x位置 */
	private int mLineLocation_X = 0;
	
	/** 默认tab索引 */
	private int mTabPosition = 0;

	/** 停靠tab 横向 第0个标签的位置 */
	@SuppressWarnings("unused")
	private int mTabView0_x = 0;

	/** 重置导航状态，红线状态 */
	private void resetTagLocation_Line() {
		// TODO 充值导航状态
		Map<String, int[]> map = ((PersonInfoCashFragment) mFragments.get(3))
				.getLocation();
		mLineLocation = map.get("line");
		mViewLocation = map.get("view");
		id_navi_cash_strip.changeActiviveTagText(mTabPosition);
		if (mLineLocation[0] != mLineLocation_X) {
			TranslateAnimation animation = new TranslateAnimation(
					mLineLocation_X, mLineLocation[0], 0f, 0f);
			animation.setInterpolator(new LinearInterpolator());
			animation.setDuration(0);
			animation.setFillAfter(true);
			id_navi_cash_line.startAnimation(animation);
			mLineLocation_X = mLineLocation[0];
		}
		View v = ((LinearLayout) id_navi_cash_strip.getChildAt(0))
				.getChildAt(0);
		int[] location = new int[2];
		v.getLocationInWindow(location);
		if (location[0] != mViewLocation[0]) {
			id_navi_cash_strip.tagScrollBy(location[0] - mViewLocation[0]);
		}

	}
	@Override
	public void changeLine(int location_x, int position, boolean isClick) {
		TranslateAnimation animation = new TranslateAnimation(mLineLocation_X,
				location_x, 0f, 0f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(0);
		animation.setFillAfter(true);
		id_navi_cash_line.startAnimation(animation);
		// 点击悬浮标签的时候，修改数据的同时，修改标签的显示
		if (isClick) {
			((PersonInfoCashFragment) mFragments.get(3))
					.changeStripTag(position);
			mTabPosition = position;
		} else {
			((PersonInfoCashFragment) mFragments.get(3))
					.tagScrollBy(mLineLocation_X - location_x);
		}
	
		mLineLocation_X = location_x;

	}

	/** 切换悬浮导航状态 */
	private void resetSingleTabState() {
		if (persontagIndex == 1) {
			id_single_lemi_details.setTextColor(colorRed);
			id_single_exchange_record.setTextColor(colorBlack);
		} else if (persontagIndex == 2) {
			id_single_lemi_details.setTextColor(colorBlack);
			id_single_exchange_record.setTextColor(colorRed);
		}

	}

	/** 显示现金Tab栏 */
	private void showCashSuspend() {
		if (id_navi_viewstub_cash == null) {
			id_navi_viewstub_cash = (ViewStub) mView
					.findViewById(R.id.id_navi_viewstub_cash);
			id_navi_viewstub_cash.inflate();
			initCashViewStub();
		} else {
			id_navi_viewstub_cash.setVisibility(View.VISIBLE);
		}
	}

	/** tag数据源 */
	private List<String> mTags = null;
	/** 停靠--现金滚动标签栏 */
	private HorizontalScrollTabStrip id_navi_cash_strip;
	/** 指示器下的红线的参数 */
	private LayoutParams lineLp;
	/** 停靠--现金滚动标签-横线 */
	private View id_navi_cash_line;

	/** 初始化现金Tab栏 */
	private void initCashViewStub() {
		id_navi_cash_strip = (HorizontalScrollTabStrip) mView
				.findViewById(R.id.id_navi_cash_strip);
		initLineParams();
		id_navi_cash_line = mView.findViewById(R.id.id_navi_cash_line);
		id_navi_cash_line.setLayoutParams(lineLp);
		if (mTags == null) {
			mTags = new ArrayList<String>();
			mTags.add("全部订单");
			mTags.add("购彩明细");
			mTags.add("派奖明细");
			mTags.add("充值明细");
			mTags.add("提现");
			mTags.add("红包明细");
		}
		id_navi_cash_strip.setTags(mTags,false);
		id_navi_cash_strip.setOnTagChangeListener(this);

	}

	/** 移除现金Tab栏 */
	private void removeCashSuspend() {
		if (id_navi_viewstub_cash != null) {
			id_navi_viewstub_cash.setVisibility(View.GONE);
		}
	}

	/** 显示购买的悬浮框 */
	private void showSingleSuspend() {
		removeDoubleSuspend();
		if (id_navi_viewstub_single == null) {
			id_navi_viewstub_single = (ViewStub) mView
					.findViewById(R.id.id_navi_viewstub_single);
			id_navi_viewstub_single.inflate();
			initSingleViewStub();
		}
		id_navi_viewstub_single.setVisibility(View.VISIBLE);
	}

	/** 悬浮窗--乐米--我的投注 */
	private LinearLayout id_single_navi_my_tz;
	/** 悬浮窗--乐米--乐米明细 */
	private TextView id_single_lemi_details;
	/** 悬浮窗--乐米--交易记录 */
	private TextView id_single_exchange_record;

	/**
	 * 初始化乐米 导航
	 */
	private void initSingleViewStub() {
		id_single_navi_my_tz = (LinearLayout) mView
				.findViewById(R.id.id_single_navi_my_tz);
		id_single_lemi_details = (TextView) mView
				.findViewById(R.id.id_single_lemi_details);
		id_single_exchange_record = (TextView) mView
				.findViewById(R.id.id_single_exchange_record);
		id_single_navi_my_tz.setOnClickListener(singleTabOnclick);
		id_single_lemi_details.setOnClickListener(singleTabOnclick);
		id_single_exchange_record.setOnClickListener(singleTabOnclick);
	}

	/** 悬浮窗--我的投注--乐米明细、兑换记录点击事件 */
	private OnClickListener singleTabOnclick = new OnClickListener() {
		// TODO 缺少 同一个按钮的点击判断
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.id_single_navi_my_tz:
				// 点击我的投注
//				id_scrollview.scrollTo(0, 1);
				PersonInfoFragment.this.onClick(id_navi_my_tz);
				id_navi_viewstub_single.setVisibility(View.GONE);
				break;
			case R.id.id_single_lemi_details:
				// 点击乐米明细
//				id_scrollview.scrollTo(0, 1);
				PersonInfoFragment.this.onClick(id_lemi_details);
				break;
			case R.id.id_single_exchange_record:
				// 点击兑换记录
//				id_scrollview.scrollTo(0, 1);
				PersonInfoFragment.this.onClick(id_exchange_record);
				break;

			default:
				break;
			}
		}
	};

	/** 移除购买的悬浮框 */
	public void removeSingleSuspend() {
		if (id_navi_viewstub_single != null) {
			id_navi_viewstub_single.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示我的投注Tab栏
	 */
	private void showDoubleSuspend() {
		if (id_navi_viewstub_double == null) {
			id_navi_viewstub_double = (ViewStub) mView
					.findViewById(R.id.id_navi_viewstub_double);
			id_navi_viewstub_double.inflate();
			initDoubleViewStub();
		}
		id_navi_viewstub_double.setVisibility(View.VISIBLE);
	}

	/** 停靠显示导航--我的投注-布局 */
	private LinearLayout id_navi_mytz;
	/** 停靠显示导航--我的投注-title */
	private TextView id_mytz_title;
	/** 停靠显示导航--我的投注-下箭头 */
	private ImageView id_mytz_icon;
	/** 停靠显示导航--乐米明细 */
	private TextView id_navi_details;
	/** 停靠显示导航--兑换记录 */
	private TextView id_navi_exchange_record;
	/** 停靠显示导航--我的投注-全部 */
	private TextView id_navi_mytz_all;
	/** 停靠显示导航--我的投注-代购 */
	private TextView id_navi_mytz_purchasing;
	/** 停靠显示导航--我的投注-追号 */
	private TextView id_navi_mytz_during;
	/** 停靠显示导航--我的投注-中奖 */
	private TextView id_navi_mytz_winning;
	/** 停靠显示导航--我的投注-选中红线 */
	private View id_navi_mytz_line;

	/** 初始化我的投注Tab导航 */
	private void initDoubleViewStub() {
		id_navi_mytz = (LinearLayout) mView.findViewById(R.id.id_navi_mytz);
		id_mytz_title = (TextView) mView.findViewById(R.id.id_mytz_title);
		id_mytz_icon = (ImageView) mView.findViewById(R.id.id_mytz_icon);

		id_navi_details = (TextView) mView.findViewById(R.id.id_navi_details);
		id_navi_exchange_record = (TextView) mView
				.findViewById(R.id.id_navi_exchange_record);

		id_navi_mytz_all = (TextView) mView.findViewById(R.id.id_navi_mytz_all);
		id_navi_mytz_purchasing = (TextView) mView
				.findViewById(R.id.id_navi_mytz_purchasing);
		id_navi_mytz_during = (TextView) mView
				.findViewById(R.id.id_navi_mytz_during);
		id_navi_mytz_winning = (TextView) mView
				.findViewById(R.id.id_navi_mytz_winning);
		id_navi_mytz_line = mView.findViewById(R.id.id_navi_mytz_line);

		// 设置点击事件
		id_navi_mytz.setOnClickListener(myTzClick);
		id_navi_details.setOnClickListener(myTzClick);
		id_navi_exchange_record.setOnClickListener(myTzClick);
		id_navi_mytz_all.setOnClickListener(myTzClick);
		id_navi_mytz_purchasing.setOnClickListener(myTzClick);
		id_navi_mytz_during.setOnClickListener(myTzClick);
		id_navi_mytz_winning.setOnClickListener(myTzClick);
	}

	/** 重置红线位置 */
	private void resetTab_Line_Location() {
		float end = mScreenWidth / 4 * mTagIndex;
		if (id_navi_mytz_line != null) {
			LayoutParams lp = (LayoutParams) id_navi_mytz_line
					.getLayoutParams();
			lp.weight = 0f;
			lp.width = mScreenWidth / 4;
			lp.setMargins((int) end, 0, 0, 0);
			id_navi_mytz_line.getParent().requestLayout();
		}
	}

	/** 我的投注--悬浮框导航点击事件 */
	private OnClickListener myTzClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.id_navi_mytz:
				// 我的投注点击
				// 点击我的投注
				if (tag.equals(TAG_0)) {
					id_my_tz_icon.setRotation(0);
					id_mytz_icon.setRotation(0);
					tag = TAG_1;
				} else if (tag.equals(TAG_1)) {
					tag = TAG_0;
					id_my_tz_icon.setRotation(180);
					id_mytz_icon.setRotation(180);
					popList.showOrhideWindow(id_mytz_title,getShowTagIndex());
					tzFragment.setCoverViewState(true);
				}
				persontagIndex = 0;
				GlobalConstants.personTagIndex = 0;
				break;
			case R.id.id_navi_details:
				// 乐米明细
				changeNaviTab_TZ(R.id.id_navi_details);
				break;
			case R.id.id_navi_exchange_record:
				// 兑换记录
				changeNaviTab_TZ(R.id.id_navi_exchange_record);
				break;
			case R.id.id_navi_mytz_all:
				// TODO 全部
				if (mTagIndex != 0) {
					tzFragment.tagOnClick(0);
					changeTabTextColor();
				}
				break;
			case R.id.id_navi_mytz_purchasing:
				// 代购
				if (mTagIndex != 1) {
					tzFragment.tagOnClick(1);
					changeTabTextColor();
				}
				break;
			case R.id.id_navi_mytz_during:
				// 追号
				if (mTagIndex != 2) {
					tzFragment.tagOnClick(2);
					changeTabTextColor();
				}
				break;
			case R.id.id_navi_mytz_winning:
				// 中奖
				if (mTagIndex != 3) {
					tzFragment.tagOnClick(3);
					changeTabTextColor();
				}
				break;

			default:
				break;
			}
		}
	};
	/**
	 * 修改当前点击的tag样式
	 * @param persontagIndex
	 */
	protected void changeTabTextColor() {
		id_navi_mytz_all.setTextColor(colorGray);
		id_navi_mytz_purchasing.setTextColor(colorGray);
		id_navi_mytz_during.setTextColor(colorGray);
		id_navi_mytz_winning.setTextColor(colorGray);
		switch (mTagIndex) {
		case 0:
			id_navi_mytz_all.setTextColor(colorRed);
			break;
		case 1:
			id_navi_mytz_purchasing.setTextColor(colorRed);
			break;
		case 2:
			id_navi_mytz_during.setTextColor(colorRed);
			break;
		case 3:
			id_navi_mytz_winning.setTextColor(colorRed);
			break;

		default:
			break;
		}
	}

	/** 我的投注--标签切换 */
	private void changeNaviTab_TZ(int id) {
		// 修改标签，我的投注未选中；乐米明细选中；兑换记录选中
		// 1、隐藏该悬浮tag
		id_navi_viewstub_double.setVisibility(View.GONE);
//		// 2、ScrollView 滚动到顶部
//		id_scrollview.scrollTo(0, 1);
		// 3、执行点击事件
		switch (id) {
		case R.id.id_navi_details:
			// 切换到乐米明细
			if (persontagIndex != 1) {
//				id_scrollview.scrollTo(0, 1);
				onClick(id_lemi_details);
			}

			break;
		case R.id.id_navi_exchange_record:
			// 切换到兑换记录
			if (persontagIndex != 2) {
//				id_scrollview.scrollTo(0, 1);
				onClick(id_exchange_record);
			}
			break;
		case R.id.id_navi_mytz_all:
			// 全部
			break;
		case R.id.id_navi_mytz_purchasing:
			// 代购
			break;
		case R.id.id_navi_mytz_during:
			// 追号
			break;
		case R.id.id_navi_mytz_winning:
			// 中奖
			break;
		default:
			break;
		}
	}

	

	/** 移除购买的悬浮框 */
	public void removeDoubleSuspend() {
		if (id_navi_viewstub_double != null) {
			id_navi_viewstub_double.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化指示器“红线”参数
	 */
	private void initLineParams() {
		lineLp = new LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lineLp.weight = 0f;
		lineLp.width = (int) (mScreenWidth / (4 + 0.6));
		lineLp.height = (int) (getResources().getDisplayMetrics().density * 2 + 0.5f);
	}

	@Override
	public void onPause() {
		super.onPause();
		Controller.getInstance().removeCallback(mCallback);
	}

	

	

	@Override
	public void updateDatas(int position) {
		((PersonInfoCashFragment) mFragments.get(3))
				.updateDatas(position, true);
	}

	/**
	 * 账户未完善信息，是否去完善
	 */
	private void isValidate() {
		final PromptDialog_Black_Fillet dialog = new PromptDialog_Black_Fillet(
				getActivity());
		dialog.setMessage("您未实名认证，请先完成认证？");
		dialog.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.hideDialog();
			}
		});
		dialog.setPositiveButton("去认证", new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.hideDialog();
				startActivity(new Intent(getActivity(),
						UserManageActivity.class));
			}
		});
		dialog.showDialog();
	}

	/**
	 * 去绑定银行卡
	 */
	private void isBandBank() {
		final PromptDialog_Black_Fillet dialog = new PromptDialog_Black_Fillet(
				getActivity());
		dialog.setMessage("您未绑定银行卡，请先完成绑定？");
		dialog.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.hideDialog();
			}
		});
		dialog.setPositiveButton("去绑定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.hideDialog();
				Intent intent = new Intent(getActivity(),
						CardAddCardActivity.class);
				intent.putExtra("type", "1");
				startActivity(intent);
			}
		});
		dialog.showDialog();
	}

	/** 注册头像更换的广播 */
	private void registReceiver() {
		if (null == mPhotoReceiver) {
			mPhotoReceiver = new ChangePhotoReceiver();
		}
		if (null == mPhotoIntent) {
			mPhotoIntent = new IntentFilter("com.lebocp.changephoto");
		}
		getActivity().registerReceiver(mPhotoReceiver, mPhotoIntent);
	}

	/** 头像更换Intent */
	private IntentFilter mPhotoIntent;

	/** 头像更换Receiver */
	private ChangePhotoReceiver mPhotoReceiver;

	class ChangePhotoReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 收到成功的广播，更新头像信息，并且更新本地用户信息
			if (null != intent) {
				isChangeHeaderImage = true;
				mImageLoader.displayImage(intent.getStringExtra("filePath"),
						id_person_header);
			}
		}
	}
	
	/**
	 * 显示LoadingDialog
	 */
	private void showDialog(){
		if (mDialog == null) {
			mDialog = DialogProgress.newIntance("正在加载...", true);
		}

		if (!mDialog.isAdded()

		&& !mDialog.isVisible()

		&& !mDialog.isRemoving())
		{
			mDialog.show(getFragmentManager().beginTransaction(), "dataDialog");
		}

	}
	/**
	 * 隐藏LoadingDialog
	 */
	private void hideDialog(){
		if (mDialog!=null) {
			mDialog.dismiss();
		}
		
	}
}
