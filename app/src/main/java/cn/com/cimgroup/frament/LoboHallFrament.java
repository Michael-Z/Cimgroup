package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.CommitPayActivity;
import cn.com.cimgroup.activity.GameFlopActivity;
import cn.com.cimgroup.activity.GameGuessMatchActivity;
import cn.com.cimgroup.activity.GameLuckyPanActivity;
import cn.com.cimgroup.activity.HtmlCommonActivity;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.LotteryBasketballActivity;
import cn.com.cimgroup.activity.LotteryDLTActivity;
import cn.com.cimgroup.activity.LotteryDrawInfoActivity;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.activity.LotteryOldFootballActivity;
import cn.com.cimgroup.activity.LotteryPL3Activity;
import cn.com.cimgroup.activity.LotteryQxcActivity;
import cn.com.cimgroup.activity.MainActivity;
import cn.com.cimgroup.activity.MainGameActivity;
import cn.com.cimgroup.activity.MessagePersonActivity_Act;
import cn.com.cimgroup.activity.OtherLotteryActivity;
import cn.com.cimgroup.activity.ScoreListActivity;
import cn.com.cimgroup.activity.SignActivity;
import cn.com.cimgroup.activity.TextDetailActivity;
import cn.com.cimgroup.activity.ZSTListActivity;
import cn.com.cimgroup.animation.StretchAnimation;
import cn.com.cimgroup.banner.AutoScrollViewPager;
import cn.com.cimgroup.banner.CirclePageIndicator;
import cn.com.cimgroup.banner.ImagePagerAdapter;
import cn.com.cimgroup.banner.ImagePagerAdapter.VpItemClickListener;
import cn.com.cimgroup.banner.PageIndicator;
import cn.com.cimgroup.bean.ControllLottery;
import cn.com.cimgroup.bean.FocusMatch;
import cn.com.cimgroup.bean.GameObj;
import cn.com.cimgroup.bean.HallAd;
import cn.com.cimgroup.bean.HallNotice;
import cn.com.cimgroup.bean.HallText;
import cn.com.cimgroup.bean.HallTextList;
import cn.com.cimgroup.bean.ImageObj;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.bean.MatchAgainstSpInfo;
import cn.com.cimgroup.bean.NoticeContent;
import cn.com.cimgroup.bean.NoticeOrMessageCount;
import cn.com.cimgroup.bean.TzObj;
import cn.com.cimgroup.bean.Upgrade;
import cn.com.cimgroup.config.ControllLotteryConfiguration;
import cn.com.cimgroup.config.ImageConfiguration;
import cn.com.cimgroup.config.NoticeConfiguration;
import cn.com.cimgroup.dailog.PromptDialog0;
import cn.com.cimgroup.dailog.PromptDialog0.onKeydownListener;
import cn.com.cimgroup.dailog.PromptDialog1;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.DateUtils;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.LotteryBettingUtil;
import cn.com.cimgroup.util.LotteryShowUtil;
import cn.com.cimgroup.util.PlayInfo;
import cn.com.cimgroup.view.AutoScrollTextView;
import cn.com.cimgroup.view.ChildViewPager;
import cn.com.cimgroup.view.CircleImageView;
import cn.com.cimgroup.view.FlowLayout;
import cn.com.cimgroup.view.NotifyingScrollView;
import cn.com.cimgroup.view.NotifyingScrollView.OnScrollChangedListener;
import cn.com.cimgroup.view.ViewPagerAdapter;
import cn.com.cimgroup.view.ViewPagerAdapter.OnItemClickListener;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoboHallFrament extends BaseFrament implements OnClickListener, OnItemClickListener {

	// 整个页面view
	private View mView;
	
	private App app;
	
	private boolean mShouldInitialize = true;
	
	private String width;

	private String height;
	
	private NotifyingScrollView scrollHead;
	
	private RelativeLayout layoutHead;
	
	/** 轮播图片布局 */
	private RelativeLayout slideshowView;
	
	private LinearLayout winLayout;
	
	private TextView word;
	
	private FrameLayout photo;
	
	private DisplayImageOptions options;
	
	private AutoScrollViewPager viewPager;

	private ImagePagerAdapter pagerAdapter;
	
	private List<ImageObj> images = new ArrayList<ImageObj>();

	private PageIndicator mIndicator;
	
	private ImageView redPoint;
	
	private CircleImageView imagePhoto;
	
	private HallAd mHallAd;
	
	private HallNotice mNotice;
	
	private ControllLottery controll;
	
	private String timeId = "0";
	
	private String noticeTimeId = "0";
	
	private String controllTimeId = "0";
	
	private String isNewUser = "y";
	
	/** okhttp相关参数 **/
	private OkHttpClient okHttpClient;
	private String result;
	private OkHttpClient client;
	private double totalProgress;
	private String version;
	
	private PromptDialog0 dialogProgress;
	private PromptDialog0 dialog;
	/** 用户是否点击了返回按钮 **/
	private boolean flag_back_pressed = false;
	/** 强制更新（1）/非强制更新（2） **/
	private int flag = 9999;
	
	/** 如果在关闭界面的时候 添加变量防止请求数据 */
	protected static final int UPDATE_PROGRESS = 1;
	
	private int progress;
	
	private RadioGroup rg;
	private RadioButton firstBtn;
	private RadioButton secondBtn;
	private RadioButton thirdBtn;
	private RadioButton fourBtn;
	private RadioButton fiveBtn;
	private ChildViewPager mWinViewPager;
	
	private List<LotteryDrawInfo> mList;
	
	private LotteryDrawInfo currInfo;
	
	private LoBoPeriodInfo periodInfo;
	
	List<View> listViews = new ArrayList<View>();
	
	/** 图片加载框架 ImageLoader */
	protected ImageLoader imageLoader;
	
	private AutoScrollTextView autoScrollTextView;
	
	private List<HallText> textList = new ArrayList<HallText>();
	
	private LinearLayout autoScrollLayout;
	
	private int mCurrent = 0;
	
	private boolean isPrepared;
	
	private ImageView jczqStatus;
	private TextView jczqText;
	private TextView jczqTitle;
	
	private ImageView czsStatus;
	private TextView czsText;
	private TextView czsTitle;
	
	private ImageView x1Status;
	private TextView x1Text;
	private TextView x1Title;
	
	private ImageView sfcStatus;
	private TextView sfcText;
	private TextView sfcTitle;
	
	private ImageView jclqStatus;
	private TextView jclqText;
	private TextView jclqTitle;
	
	private ImageView p3Status;
	private TextView p3Text;
	private TextView p3Title;
	
	private ImageView qxcStatus;
	private TextView qxcText;
	private TextView qxcTitle;
	
	private ImageView dltStatus;
	private TextView dltText;
	private TextView dltTitle;
	
	
	private LinearLayout jczqLayout;
	private LinearLayout czsLayout;
	private LinearLayout x1Layout;
	private LinearLayout sfcLayout;
	private LinearLayout jclqLayout;
	private LinearLayout p3Layout;
	private LinearLayout dltLayout;
	private LinearLayout qxcLayout;
	
	/**快速投注最外层的整体布局**/
	private LinearLayout layout_hall_jiaodian;
	/**快速投注-整体**/
	private RadioGroup tab_quick_bet;
	/**快速投注-一场致胜**/
	private RadioButton tab_quick_only_win;
	/**快速投注-大乐透**/
	private RadioButton tab_quick_dlt;
	/**快速投注-大乐透 布局**/
	private LinearLayout quick_bet_content_dlt;
	/**快速投注-一场致胜 布局**/
	private RelativeLayout id_lobo_hall_dlt_layout_tZ;
	/**快速投注-一场制胜-头部-周次**/
	private TextView layout_hall_jiaodian_terminate_time;
	/**快速投注-一场制胜-头部-比赛类型**/
//	private TextView layout_hall_jiaodian_matchsort;
	/**快速投注-一场制胜-头部-联赛类型**/
	private TextView layout_hall_jiaodian_leagueshort;
	/**快速投注-一场制胜-主队整体布局**/
	private RelativeLayout layoutView_hall_jiaodian_win;
	/**快速投注-一场制胜-VS整体布局**/
	private RelativeLayout layoutView_hall_jiaodian_vs;
	/**快速投注-一场制胜-客队整体布局**/
	private RelativeLayout layoutView_hall_jiaodian_lost;
	/**快投-一场制胜-主队名称**/
	private TextView layout_hall_jiaodian_hometeamname;
	/**快投-一场制胜-胜SP**/
	private TextView layout_hall_jiaodian_s;
	/**快投-一场制胜-胜选中图片**/
	private ImageView imageView_jiaodian_win;
	/**快投-一场制胜-平SP**/
	private TextView layout_hall_jiaodian_p;
	/**快投-一场制胜-平选中图片**/
	private ImageView imageView_jiaodian_vs;
	/**快投-一场制胜-客队名称**/
	private TextView layout_hall_jiaodian_guestteamname;
	/**快投-一场制胜-负SP**/
	private TextView layout_hall_jiaodian_f;
	/**快投-一场制胜-负选中图片**/
	private ImageView imageView_jiaodian_lost;
	/**快投-一场制胜-理论奖金**/
	private TextView textView_hall_jiaodian_money;
	/**快投-一场制胜-倍数**/
	private EditText editView_bet_muitiple_1czs;
	/**快投-一场制胜-倍数-+**/
	private LinearLayout imgView_1czs_plus;
	/**快投-一场制胜-倍数--**/
	private LinearLayout imgView_1czs_minus;
	/**快投-一场制胜-投注**/
	private TextView textView_jiaodian_tz;
	/**快投-一场制胜-换一场**/
	private TextView textView_1czs_match_change;
	/**快投-大乐透-头部内容**/
	private LinearLayout id_lobo_hall_dlt_layout;
	/**快投-大乐透-期次**/
	private TextView textView_hall_dlt_period;
	/**快投-大乐透-号码*/
	private FlowLayout hall_jx_fl;
	/**快投 接口参数-是否改变场次**/
	private String isChange = "0"; 
	/**快投-赛事信息**/
	private FocusMatch mFMatch;
	/**快投-大乐透-倒计时**/
	private long mJiaoDianDifferTime;
	/**快投-大乐透-倒计时-天**/
	private TextView hall_dlt_time_day;
	/**快投-大乐透-倒计时-冒号**/
	private TextView hall_dlt_time_3;
	/**快投-大乐透-倒计时-小时**/
	private TextView hall_dlt_time_hour;
	/**快投-大乐透-倒计时-分钟**/
	private TextView hall_dlt_time_minute;
	/**快投-大乐透-倒计时-秒钟**/
	private TextView hall_dlt_time_second;
	/**快投-大乐透-换一个号**/
	private TextView textView_hall_dlt_refresh;
	/**快投-大乐透-投注**/
	private TextView textView_dlt_tz;
	/**快投-大乐透-投注米数**/
	private EditText editView_bet_muitiple_dlt;
	/**快投-大乐透-减两米**/
	private LinearLayout imgView_dlt_minus;
	/**快投-大乐透-加两米**/
	private LinearLayout imgView_dlt_plus;
	/** 通知修改焦点比赛视图隐藏 */
	private final static int UPDATE_HIAODIAN_VIEW_GONE = 9;
	/** 通知修改大乐透选票隐藏 */
	private final static int UPDATE_PERIOD_VIEW_GONE = 8;
	/** 通知-没有网络**/
	private final static int QUICK_BET_WITHOUT_NET = 100;
	/** 通知-一场制胜-没有比赛**/
	private final static int QUICK_BET_NO_MATCH_1CZS = 1001;
	/** 通知-一场制胜-请求无返回**/
	private final static int QUICK_BET_NO_REQUEST_1CZS = 1002;
	/** 通知-大乐透-请求无返回**/
	private final static int QUICK_BET_NO_REQUEST_DLT = 1003;
	private final static int QUICK_BET_RELOAD_DATA = 200;
	/** 大乐透投注倍数选择 动画 */
	private StretchAnimation dltBeiAnimation;
	/** 焦点投注倍数选择 动画 */
	private StretchAnimation jdBeiAnimation;
	/** 比分直播布局**/
	private RelativeLayout layout_hall_bifen;
	
	/** 快投-大乐透-倒计时时间**/
	private long mDltDifferTime;
	
	/**快投-大乐透-红球集合**/
	private ArrayList<Integer> mRedList = new ArrayList<Integer>();
	/**快投-大乐透-蓝球集合**/
	private ArrayList<Integer> mBuleList = new ArrayList<Integer>();
	/**快投-大乐透-红/蓝球展示区集合**/
	private List<TextView> mViewList = new ArrayList<TextView>();
	/**是否走了lazyload（）**/
	private boolean flag_dlt = false;
	/**快投-没有比赛 true-没有 false-有**/
	private boolean flag_0_match = false;
	
	/**暂无网络的整体布局**/
	private LinearLayout linearlayout_accident_occured;
	/**暂无网路-说明**/
	private TextView textview_accident_explain;
	/**暂无数据-按钮-点击请求**/
	private TextView textview_accident_reload;
	/**快投-一场制胜-换一场 标志位 true-换一场 false-非换一场**/
	private boolean mFlagChangeMatch = false;
	/**是否请求大乐透数据  true-是  false-不请求*/
	private boolean mIsRequest = true;
	/**更新完成**/
	private boolean updateFinish = false;
	/**
	 * 首页Banner图默认图片
	 */
	private ImageView ivHallBannerInitial;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		progress = 0;
		app = (App) mFragmentActivity.getApplication();

		initImageOptions();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.frament_lotteryhall, null);
			for (int i = 0; i < 5; i++) {
				listViews.add(inflater.inflate(R.layout.item_lotterydraw_list, null));
			}
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		
		mHallAd = ImageConfiguration.getConfiguration().getLocalImageInfo();
//		System.out.println("-----AAAAA------"+mHallAd.getTimeId());
		mNotice = NoticeConfiguration.getConfiguration().getLocalNoticeInfo();
		controll = ControllLotteryConfiguration.getConfiguration().getLocalControllInfo();
		
		if (mShouldInitialize) {
			initView();
			initData();
			initAnimation();
			imageLoader = ImageLoader.getInstance();
			isPrepared = true;
			//第一次加载 选框默认在最左边
			if (!layoutView_hall_jiaodian_win.isSelected()) {
				onClick(layoutView_hall_jiaodian_win);
			}
//			lazyLoad();
			
		}

		return mView;
	}
	
	private void initData() {
		
		dialogProgress = new PromptDialog0(getActivity());
		dialog = new PromptDialog0(mFragmentActivity);
		
		setBeiJiaoDianMoney();

	}

	/**
	 * 初始化view
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-11-24
	 */
	private void initView() {
		ivHallBannerInitial = (ImageView) mView.findViewById(R.id.iv_hall_banner_initial);

		slideshowView = (RelativeLayout) mView.findViewById(R.id.slideshowView);
		
		winLayout = (LinearLayout) mView.findViewById(R.id.layout_hall_win);
		
		word = (TextView) mView.findViewById(R.id.textView_hall_word);
		
		photo = (FrameLayout) mView.findViewById(R.id.layout_hall_photo);
		
		redPoint = (ImageView) mView.findViewById(R.id.image_hall_red_point);
		
		imagePhoto = (CircleImageView) mView.findViewById(R.id.imageView_hall_photo);
		
		autoScrollLayout = (LinearLayout) mView.findViewById(R.id.layout_hall_autoscroll);
		
		autoScrollTextView = (AutoScrollTextView)mView.findViewById(R.id.scrollView_hall_text);
		
		dialogProgress = new PromptDialog0(getActivity());
		
		autoScrollTextView.setOnItemClickListener(new AutoScrollTextView.OnItemClickListener() {
			
			@Override
			public void onItemClick(int position) {
//				XLog.e("gamename = " + textList.get(position).getGameId());
				switch (textList.get(position).getGameId()) {
				case "1":
					// 竞猜游戏(跳转)
					startActivity(GameGuessMatchActivity.class);
					break;
				case "2":
					// 翻牌游戏(跳转)
					startActivity(GameFlopActivity.class);
					break;
				case "3":
					// 大转盘游戏(跳转)
					startActivity(GameLuckyPanActivity.class);
					break;
				default:
					break;
				}
			}
		});
		
		// 设置轮播的宽高
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		LinearLayout.LayoutParams imagebtn_params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imagebtn_params.width = dm.widthPixels;
		imagebtn_params.height = dm.heightPixels / 5;
		slideshowView.setLayoutParams(imagebtn_params);
		
		width = dm.widthPixels * dm.density + "";
		height = dm.heightPixels * dm.density + "";
		
		viewPager = (AutoScrollViewPager) mView.findViewById(R.id.viewPager);
		viewPager.startAutoScroll();
		viewPager.setInterval(4000);
		viewPager.setScrollDurationFactor(3);
//		viewPager.setSlideBorderMode(AutoScrollViewPager.SLIDE_BORDER_MODE_CYCLE);
		pagerAdapter = new ImagePagerAdapter(mFragmentActivity, images, options);
		
		pagerAdapter.setVpItemClickListener(new VpItemClickListener() {

			@Override
			public void vpItemClick(ImageObj imageInfo) {
				if (imageInfo.getIsWeb().equals("y")) {
					if (imageInfo.getIsLogin().equals("y")
							&& App.userInfo == null) {
						startActivity(LoginActivity.class);
					} else {
						Intent it = new Intent(mFragmentActivity,
								HtmlCommonActivity.class);
						it.putExtra("isWeb", true);
						it.putExtra("webUrl", imageInfo.getWebUrl());
						startActivity(it);
					}

				} else {
					// 跳转本地activity
					Intent intent = new Intent(mFragmentActivity,
							TextDetailActivity.class);
					intent.putExtra("title", imageInfo.getTitle());
					intent.putExtra("time", imageInfo.getTime());
					intent.putExtra("content", imageInfo.getContent());
					startActivity(intent);
				}
			}

		});
		viewPager.setAdapter(pagerAdapter);

		mIndicator = (CirclePageIndicator) mView.findViewById(R.id.indicator);
		mIndicator.setViewPager(viewPager);
		
		scrollHead = (NotifyingScrollView) mView.findViewById(R.id.scrollView_hall_head);
		layoutHead = (RelativeLayout) mView.findViewById(R.id.layout_hall_head);
		
		layoutHead.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (progress == 0) {
					return false;
				} else {
					return true;
				}
			}
		});
		
//		layoutHead.getBackground().setAlpha(progress);
		
		
		scrollHead.setOnScrollChangedListener(new OnScrollChangedListener() {

			@SuppressLint("NewApi")
			@Override
			public void onScrollChanged(ScrollView who, int l, int t, int oldl,
					int oldt) {
				//define it for scroll height
				int lHeight = 200;
				if(t <= lHeight && t > 0){
					progress = (int)(new Float(t)/new Float(lHeight) * 255);
//					
					if (VERSION.SDK_INT <= 17) {
						
						if (t == lHeight) {
							layoutHead.setBackground(new ColorDrawable(getResources().getColor(R.color.color_black)));
						} else {
							layoutHead.setBackground(new ColorDrawable(progress));
						}
					} else {
						layoutHead.getBackground().mutate().setAlpha(progress);
					}
					
					photo.setVisibility(View.VISIBLE);
					word.setVisibility(View.VISIBLE);
				} else if (t < 10) {
					progress = 0;
					if (VERSION.SDK_INT <= 17) {
						layoutHead.setBackground(new ColorDrawable(progress));
					} else {
						layoutHead.getBackground().mutate().setAlpha(progress);
					}
					photo.setVisibility(View.GONE);
					word.setVisibility(View.GONE);
				} else {
					if (VERSION.SDK_INT <= 17) {
						layoutHead.setBackground(new ColorDrawable(getResources().getColor(R.color.color_black)));
					} else {
						layoutHead.getBackground().mutate().setAlpha(255);
					}
					photo.setVisibility(View.VISIBLE);
					word.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		rg = (RadioGroup) mView.findViewById(R.id.tab_hall_menu);
		firstBtn = (RadioButton) mView.findViewById(R.id.tab_hall_1);
		secondBtn = (RadioButton) mView.findViewById(R.id.tab_hall_2);
		thirdBtn = (RadioButton) mView.findViewById(R.id.tab_hall_3);
		fourBtn = (RadioButton) mView.findViewById(R.id.tab_hall_4);
		fiveBtn = (RadioButton) mView.findViewById(R.id.tab_hall_5);
		
		firstBtn.setOnClickListener(this);
		secondBtn.setOnClickListener(this);
		thirdBtn.setOnClickListener(this);
		fourBtn.setOnClickListener(this);
		fiveBtn.setOnClickListener(this);
		
		mWinViewPager = (ChildViewPager) mView.findViewById(R.id.v4View_hall_pager);
		ViewPagerAdapter adapter = new ViewPagerAdapter(listViews);
		mWinViewPager.setAdapter(adapter);
		adapter.setOnListener(this);
		mWinViewPager.setOnPageChangeListener(new ViewPagerListener());
		
		firstBtn.setChecked(true);
		
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab_hall_1:
					firstBtn.setChecked(true);
					mWinViewPager.setCurrentItem(0, false);
					break;
				case R.id.tab_hall_2:
					secondBtn.setChecked(true);
					mWinViewPager.setCurrentItem(1, false);
					break;
				case R.id.tab_hall_3:
					thirdBtn.setChecked(true);
					mWinViewPager.setCurrentItem(2, false);
					break;
				case R.id.tab_hall_4:
					fourBtn.setChecked(true);
					mWinViewPager.setCurrentItem(3, false);
					break;
				case R.id.tab_hall_5:
					fiveBtn.setChecked(true);
					mWinViewPager.setCurrentItem(4, false);
					break;
				default:
					break;
				}
			}
		});
		
		jczqStatus = (ImageView) mView.findViewById(R.id.image_hall_jczq_status);
		jczqText = (TextView) mView.findViewById(R.id.textView_hall_jczq_text);
		jczqTitle = (TextView) mView.findViewById(R.id.textView_hall_jczq);
		
		czsStatus = (ImageView) mView.findViewById(R.id.image_hall_1czs_status);
		czsText = (TextView) mView.findViewById(R.id.textView_hall_1czs_text);
		czsTitle = (TextView) mView.findViewById(R.id.textView_hall_1czs);
		
		x1Status = (ImageView) mView.findViewById(R.id.image_hall_2x1_status);
		x1Text = (TextView) mView.findViewById(R.id.textView_hall_2x1_text);
		x1Title = (TextView) mView.findViewById(R.id.textView_hall_2x1);
		
		sfcStatus = (ImageView) mView.findViewById(R.id.image_hall_sfc_status);
		sfcText = (TextView) mView.findViewById(R.id.textView_hall_sfc_text);
		sfcTitle = (TextView) mView.findViewById(R.id.textView_hall_sfc);
		
		jclqStatus = (ImageView) mView.findViewById(R.id.image_hall_jclq_status);
		jclqText = (TextView) mView.findViewById(R.id.textView_hall_jclq_text);
		jclqTitle = (TextView) mView.findViewById(R.id.textView_hall_jclq);
		
		p3Status = (ImageView) mView.findViewById(R.id.image_hall_p3_status);
		p3Text = (TextView) mView.findViewById(R.id.textView_hall_p3_text);
		p3Title = (TextView) mView.findViewById(R.id.textView_hall_p3);
		
		dltStatus = (ImageView) mView.findViewById(R.id.image_hall_dlt_status);
		dltText = (TextView) mView.findViewById(R.id.textView_hall_dlt_text);
		dltTitle = (TextView) mView.findViewById(R.id.textView_hall_dlt);
		
		qxcStatus = (ImageView) mView.findViewById(R.id.image_hall_qxc_status);
		qxcText = (TextView) mView.findViewById(R.id.textView_hall_qxc_text);
		qxcTitle = (TextView) mView.findViewById(R.id.textView_hall_qxc);
		
		tab_quick_bet = (RadioGroup) mView.findViewById(R.id.tab_quick_bet);
		tab_quick_only_win = (RadioButton) mView.findViewById(R.id.tab_quick_only_win);
		tab_quick_dlt = (RadioButton) mView.findViewById(R.id.tab_quick_dlt);
		
		//快速投注相关控件的初始化
		layout_hall_jiaodian = (LinearLayout) mView.findViewById(R.id.layout_hall_jiaodian);
		tab_quick_bet = (RadioGroup) mView.findViewById(R.id.tab_quick_bet);
		quick_bet_content_dlt = (LinearLayout) mView.findViewById(R.id.quick_bet_content_dlt);
		id_lobo_hall_dlt_layout_tZ = (RelativeLayout) mView.findViewById(R.id.id_lobo_hall_dlt_layout_tZ);
		layout_hall_jiaodian_hometeamname = (TextView) mView.findViewById(R.id.layout_hall_jiaodian_hometeamname);
		layout_hall_jiaodian_s = (TextView) mView.findViewById(R.id.layout_hall_jiaodian_s);
		imageView_jiaodian_win = (ImageView) mView.findViewById(R.id.imageView_jiaodian_win);
		layout_hall_jiaodian_p = (TextView) mView.findViewById(R.id.layout_hall_jiaodian_p);
		imageView_jiaodian_vs = (ImageView) mView.findViewById(R.id.imageView_jiaodian_vs);
		layout_hall_jiaodian_guestteamname = (TextView) mView.findViewById(R.id.layout_hall_jiaodian_guestteamname);
		layout_hall_jiaodian_f = (TextView) mView.findViewById(R.id.layout_hall_jiaodian_f);
		imageView_jiaodian_lost = (ImageView) mView.findViewById(R.id.imageView_jiaodian_lost);
		textView_hall_jiaodian_money = (TextView) mView.findViewById(R.id.textView_hall_jiaodian_money);
		editView_bet_muitiple_1czs = (EditText) mView.findViewById(R.id.editView_bet_muitiple_1czs);
		textView_jiaodian_tz = (TextView) mView.findViewById(R.id.textView_jiaodian_tz);
		hall_jx_fl = (FlowLayout) mView.findViewById(R.id.hall_jx_fl);
		//大乐透机选的红篮球初始化
		dltjx();
		textView_hall_dlt_refresh = (TextView) mView.findViewById(R.id.textView_hall_dlt_refresh);
		layoutView_hall_jiaodian_win = (RelativeLayout) mView.findViewById(R.id.layoutView_hall_jiaodian_win);
		layoutView_hall_jiaodian_vs = (RelativeLayout) mView.findViewById(R.id.layoutView_hall_jiaodian_vs);
		layoutView_hall_jiaodian_lost = (RelativeLayout) mView.findViewById(R.id.layoutView_hall_jiaodian_lost);
		layout_hall_jiaodian_terminate_time = (TextView) mView.findViewById(R.id.layout_hall_jiaodian_terminate_time);
		id_lobo_hall_dlt_layout = (LinearLayout) mView.findViewById(R.id.id_lobo_hall_dlt_layout);
		textView_hall_dlt_period = (TextView) mView.findViewById(R.id.textView_hall_dlt_period);
		hall_dlt_time_day = (TextView) mView.findViewById(R.id.hall_dlt_time_day);
		layout_hall_bifen = (RelativeLayout) mView.findViewById(R.id.layout_hall_bifen);
		layout_hall_jiaodian_leagueshort = (TextView) mView.findViewById(R.id.layout_hall_jiaodian_leagueshort);
		hall_dlt_time_day = (TextView) mView.findViewById(R.id.hall_dlt_time_day);
		hall_dlt_time_3 = (TextView) mView.findViewById(R.id.hall_dlt_time_3);
		hall_dlt_time_hour = (TextView) mView.findViewById(R.id.hall_dlt_time_hour);
		hall_dlt_time_minute = (TextView) mView.findViewById(R.id.hall_dlt_time_minute);
		hall_dlt_time_second = (TextView) mView.findViewById(R.id.hall_dlt_time_second);
		imgView_1czs_plus = (LinearLayout) mView.findViewById(R.id.imgView_1czs_plus);
		imgView_1czs_minus = (LinearLayout) mView.findViewById(R.id.imgView_1czs_minus);
		textView_dlt_tz = (TextView) mView.findViewById(R.id.textView_dlt_tz);
		editView_bet_muitiple_dlt = (EditText) mView.findViewById(R.id.editView_bet_muitiple_dlt);
		imgView_dlt_plus = (LinearLayout) mView.findViewById(R.id.imgView_dlt_plus);
		imgView_dlt_minus = (LinearLayout) mView.findViewById(R.id.imgView_dlt_minus);
		textView_1czs_match_change = (TextView) mView.findViewById(R.id.textView_1czs_match_change);
		
		linearlayout_accident_occured = (LinearLayout) mView.findViewById(R.id.linearlayout_accident_occured);
		textview_accident_explain = (TextView) mView.findViewById(R.id.textview_accident_explain);
		textview_accident_reload = (TextView) mView.findViewById(R.id.textview_accident_reload);
		
		tab_quick_only_win.setOnClickListener(this);
		tab_quick_dlt.setOnClickListener(this);
		layoutView_hall_jiaodian_win.setOnClickListener(this);
		layoutView_hall_jiaodian_vs.setOnClickListener(this);
		layoutView_hall_jiaodian_lost.setOnClickListener(this);
		textView_jiaodian_tz.setOnClickListener(this);
		textview_accident_reload.setOnClickListener(this);
		
		initEvent();
		
		((LinearLayout) mView.findViewById(R.id.layout_hall_sign)).setOnClickListener(this);
		((LinearLayout) mView.findViewById(R.id.layout_hall_act)).setOnClickListener(this);
		((LinearLayout) mView.findViewById(R.id.layout_hall_game)).setOnClickListener(this);
		((LinearLayout) mView.findViewById(R.id.layout_hall_info)).setOnClickListener(this);
		((LinearLayout) mView.findViewById(R.id.layout_hall_zst)).setOnClickListener(this);
		((RelativeLayout) mView.findViewById(R.id.layout_hall_bifen)).setOnClickListener(this);
		
		jczqLayout = ((LinearLayout) mView.findViewById(R.id.layout_hall_jczq));
		czsLayout = ((LinearLayout) mView.findViewById(R.id.layout_hall_1czs));
		x1Layout = ((LinearLayout) mView.findViewById(R.id.layout_hall_2x1));
		sfcLayout = ((LinearLayout) mView.findViewById(R.id.layout_hall_sfc));
		jclqLayout = ((LinearLayout) mView.findViewById(R.id.layout_hall_jclq));
		p3Layout = ((LinearLayout) mView.findViewById(R.id.layout_hall_p3));
		dltLayout = ((LinearLayout) mView.findViewById(R.id.layout_hall_dlt));
		qxcLayout = ((LinearLayout) mView.findViewById(R.id.layout_hall_qxc));
		((LinearLayout) mView.findViewById(R.id.layout_hall_other)).setOnClickListener(this);
		
		jczqLayout.setOnClickListener(this);
		czsLayout.setOnClickListener(this);
		x1Layout.setOnClickListener(this);
		sfcLayout.setOnClickListener(this);
		jclqLayout.setOnClickListener(this);
		p3Layout.setOnClickListener(this);
		dltLayout.setOnClickListener(this);
		qxcLayout.setOnClickListener(this);
		imagePhoto.setOnClickListener(this);
		imgView_1czs_plus.setOnClickListener(this);
		imgView_1czs_minus.setOnClickListener(this);
		textView_hall_dlt_refresh.setOnClickListener(this);
		textView_dlt_tz.setOnClickListener(this);
		imgView_dlt_plus.setOnClickListener(this);
		imgView_dlt_minus.setOnClickListener(this);
		textView_1czs_match_change.setOnClickListener(this);
	}

	private void initEvent() {
		//切换快速投注中的一场制胜和大乐透
		tab_quick_bet.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.tab_quick_only_win:
					
					tab_quick_only_win.setChecked(true);
					tab_quick_dlt.setChecked(false);
					if (!NetUtil.isConnected(getActivity())) {
						//没有网络
						mHandler.sendEmptyMessage(QUICK_BET_WITHOUT_NET);
					}else if (mFMatch == null) {
						mHandler.sendEmptyMessage(QUICK_BET_NO_REQUEST_1CZS);
					}else {
						// 设置一场致胜-快投 可见
						if (flag_0_match) {
							//没有比赛
							layout_hall_jiaodian.setVisibility(View.GONE);
							linearlayout_accident_occured.setVisibility(View.VISIBLE);
							textview_accident_reload.setVisibility(View.GONE);
						}else {
							//有比赛
							layout_hall_jiaodian.setVisibility(View.VISIBLE);
							linearlayout_accident_occured.setVisibility(View.GONE);
							textview_accident_reload.setVisibility(View.VISIBLE);
							quick_bet_content_dlt.setVisibility(View.GONE);
							id_lobo_hall_dlt_layout_tZ.setVisibility(View.VISIBLE);
						}
					}
					
					break;
				case R.id.tab_quick_dlt:
					
					tab_quick_dlt.setChecked(true);
					tab_quick_only_win.setChecked(false);
					// 设置大乐透-快投 可见 同时 将隐藏起来的视图显示
					if (!NetUtil.isConnected(getActivity())) {
						//没有网络
						mHandler.sendEmptyMessage(QUICK_BET_WITHOUT_NET);
					}else if (periodInfo==null) {
						mHandler.sendEmptyMessage(QUICK_BET_NO_REQUEST_DLT);
					}else {
						layout_hall_jiaodian.setVisibility(View.VISIBLE);
						linearlayout_accident_occured.setVisibility(View.GONE);
						quick_bet_content_dlt.setVisibility(View.VISIBLE);
						id_lobo_hall_dlt_layout_tZ.setVisibility(View.GONE);
					}
					
					break;
				default:
					break;
				}
			}
		});
		
		editView_bet_muitiple_1czs.addTextChangedListener(new MyWatcher());
		editView_bet_muitiple_dlt.addTextChangedListener(new DltWatcher());
	}
	
	private class MyWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			//快速投注的倍数  方法中的参数1就是edittext中的内容
			String multiple = s.toString();
			int length = multiple.length();
			if (length > 3) {
				editView_bet_muitiple_1czs.setText("999");
				ToastUtil.shortToast(mFragmentActivity, "最高选择999倍");
			}
//			else if (length==0) {
//				editView_bet_muitiple_1czs.setText("5");
//				ToastUtil.shortToast(mFragmentActivity, "最小投注倍数为5倍");
//				return;
//			}else {
//				if (Integer.parseInt(multiple)<5) {
//					editView_bet_muitiple_1czs.setText("5");
//					ToastUtil.shortToast(mFragmentActivity, "最小投注倍数为5倍");
//				}
//			}
			setBeiJiaoDianMoney();
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			
		}
		
	}
	
	private class DltWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			
			//快速投注的倍数  方法中的参数1就是edittext中的内容
			String multiple = s.toString();
			int length = multiple.length();
			if (length >= 4) {
				if (Integer.parseInt(multiple)>1998) {
					editView_bet_muitiple_dlt.setText("1998");
					ToastUtil.shortToast(mFragmentActivity, "最高选择1998米");
				}
			}
//			else if (length==0) {
//				editView_bet_muitiple_dlt.setText("2");
//				ToastUtil.shortToast(mFragmentActivity, "最小投注数额为2米");
//			}else {
//				if (Integer.parseInt(multiple)%2 != 0) {
//					//奇数
//					editView_bet_muitiple_dlt.setText(String.valueOf(Integer.parseInt(multiple)+1));
//					ToastUtil.shortToast(mFragmentActivity, "投注数额必须为偶数");
//				}
//			}
			setBeiJiaoDianMoney();
			
		}

		@Override
		public void afterTextChanged(Editable s) {
			
		}
		
	}
	
	/**
	 * 初始化动画
	 */
	private void initAnimation() {
		dltBeiAnimation = new StretchAnimation(layout_hall_bifen,
				StretchAnimation.TYPE.vertical, 300, (ScrollView)scrollHead);
		// 你可以换不能给的插值器
		dltBeiAnimation.setInterpolator(new AccelerateInterpolator());
		jdBeiAnimation = new StretchAnimation(layout_hall_bifen,
				StretchAnimation.TYPE.vertical, 300);
		// 你可以换不能给的插值器
		jdBeiAnimation.setInterpolator(new AccelerateInterpolator());
	}
	
	/**
	 * 获取数据
	 */
	private void getHallFragment() {
		if (!NetUtil.isConnected(getActivity())) {
			if (mHallAd == null) {
				if (slideshowView != null) {
					slideshowView.setVisibility(View.GONE);
					ivHallBannerInitial.setVisibility(View.VISIBLE);
				}
			} else {
				if (slideshowView != null) {
					slideshowView.setVisibility(View.VISIBLE);
					ivHallBannerInitial.setVisibility(View.GONE);
				}
			}
			//暂无网络 通知快投 进行处理
			mHandler.sendEmptyMessage(QUICK_BET_WITHOUT_NET);
//			mHandler.sendEmptyMessage(QUICK_BET_NO_MATCH_1CZS);
		} else {
			long endTime = System.currentTimeMillis();
			//两次请求的时间间隔 防止频繁切换 导致请求会多次执行
			if (endTime - startTime > 800) {
				getHallAd();
				startTime = endTime;
			}
		}

	}
	
	
	/**
	 * 获取轮播图
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-11-23
	 */
	public void getHallAd() {
		Controller.getInstance().getHallAd(GlobalConstants.NUM_HALLAD, timeId, isNewUser, mBack);
	}
	
	/**
	 * 获取最近开奖列表
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-11-24
	 */
	private void getLotteryDraw() {
		Controller.getInstance().syncLotteryDraw(GlobalConstants.NUM_HALLLOTTERYDRAW, new CallBack() {
			@Override
			//用于接收数据的初始化。数据保存在mList中
			public void syncLotteryDrawSuccess(
					final List<LotteryDrawInfo> lotteryDrawInfos) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
//						HallNotice();
						if (lotteryDrawInfos != null) {
							mList = lotteryDrawInfos;
							if (mList != null && mList.size() != 0) {
								winLayout.setVisibility(View.VISIBLE);
								updateLotteryDraw(mCurrent);
							} else {
								winLayout.setVisibility(View.GONE);
							}
						}

					}
				});
			}

			@Override
			public void syncLotteryDrawError(final String error) {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
//						loadFinish();
						//Toast.makeText(mFragmentActivity, error, 0).show();
//						HallNotice();
					}
				});
			}
		});
	}
	
	/**
	 * 获取公告提示
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-11-24
	 */
	private void HallNotice() {
		Controller.getInstance().getHallNotice(GlobalConstants.NUM_HALLNOTICE, noticeTimeId, isNewUser, width, height, mBack);
	}
	
	/**
	 * 获取升级提示
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-11-24
	 */
	private void getHallUpgrade() {
		Controller.getInstance().getHallUpgrade(GlobalConstants.NUM_UPGRADE, width, height, mBack);
	}
	
	/**
	 * 获取未读消息个数
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-11-25
	 */
	private void getMessageNum() {
		Controller.getInstance().getNewNoticeOrMessage(GlobalConstants.NUM_NOTICORMESSAGE, App.userInfo.getUserId(), "0", "1", mBack);
		
	}
	
	/**
	 * 获取快投-焦点比赛信息
	 */
	private void InitQuickBet() {
		Controller.getInstance().getFocusMatch(
				GlobalConstants.NUM_FOCUSMATCH,
				GlobalConstants.TC_JCZQ, isChange, mBack);
	}
	
	private long startTime = 0;
	
	/**
	 * 获取快投-大乐透信息
	 */
	private void InitQuickBet_Dlt(){
		//快投-大乐透请求
		if (mIsRequest) {
			mIsRequest = false;
			Controller.getInstance().getLoBoPeriod(GlobalConstants.NUM_LOTTERY_PRE,GlobalConstants.TC_DLT, mBack);
		}
	}
	
	private CallBack mBack = new CallBack() {

		//获取头部广告成功-成功 请求1
		public void getHallAdSuccess(final HallAd ad) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
//					getLotteryDraw();
					HallNotice();
//					if (ad.getResCode().equals("0")) {
//						if (!TextUtils.isEmpty(ad.getTimeId())
//								&& !ad.getTimeId().equals(timeId)) {
//							List<ImageObj> lists = ad.getList();
//							if (lists != null
//									&& lists.size() > 0
//									&& (mHallAd == null || !mHallAd.getTimeId().equals(ad.getTimeId()))) {
//								mHallAd = ad;
//								timeId = mHallAd.getTimeId();
//								ImageConfiguration.getConfiguration()
//										.saveImageConfig(mHallAd);
//								pagerAdapter.setImageIdList(mHallAd.getList());
//								pagerAdapter.notifyDataSetChanged();
//
//							}
//						}
//					}
//					if (mHallAd == null) {
//						slideshowView.setVisibility(View.GONE);
//						ivHallBannerInitial.setVisibility(View.VISIBLE);
//					} else {
//						slideshowView.setVisibility(View.VISIBLE);
//						ivHallBannerInitial.setVisibility(View.GONE);
//					}
				}
			});
		};

		//获取头部广告失败-失败 请求1
		public void getHallAdError(String error) {
			mFragmentActivity.runOnUiThread(new Runnable() {
				public void run() {
//					getLotteryDraw();
					HallNotice();
					if (mHallAd == null) {
						slideshowView.setVisibility(View.GONE);
						ivHallBannerInitial.setVisibility(View.VISIBLE);
					} else {
						slideshowView.setVisibility(View.VISIBLE);
						ivHallBannerInitial.setVisibility(View.GONE);
					}
				}
			});
		};
		
		//获取头部弹出公告-成功 请求2
		public void getHallNoticeSuccess(final HallNotice notice) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
					// 获取弹出公告成功 进行显示
					getHallUpgrade();
					if (notice.getResCode().equals("0")) {
						if (mNotice == null) {
							NoticeConfiguration.getConfiguration()
									.saveNoticeConfig(notice);
							setNotice();
						} else {
							if (notice != null && mNotice.getTimeid()!=null
									&& !mNotice.getTimeid().equals(
											notice.getTimeid())) {
								mNotice = notice;
								NoticeConfiguration.getConfiguration()
										.saveNoticeConfig(notice);
								setNotice();
							}
						}
					}
				}
			});
		};

		//获取头部弹出公告-失败 请求2
		public void getHallNoticeError(String error) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
					getHallUpgrade();
					setNotice();
				}
			});
		};
		

		//获取版本升级信息-成功 请求3
		public void getHallUpgradeSuccess(final Upgrade upgrade) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
					getHallText();
					if (upgrade.getResCode().equals("0")) {
						setUpgrade(upgrade);
					}
				}
			});
		};

		//获取版本升级信息-失败 请求3
		public void getHallUpgradeError(String error) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
					if (App.userInfo != null) {
						getHallText();
					}
					
				}
			});
		};
	
		//获取首页轮播文字-成功  请求4
		@Override
		public void getHallTextSuccess(final HallTextList hallTextList) {
			mFragmentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getControllLottery();
					if (!TextUtils.isEmpty(hallTextList.getResCode()) && hallTextList.getResCode().equals("0")) {
						List<HallText> list = hallTextList.getList();
						if (list != null) {
							if (list.size() > 0) {
								autoScrollLayout.setVisibility(View.VISIBLE);
								textList = list;
								List<String> titles = new ArrayList<String>();
								for (int i = 0; i < list.size(); i++) {
									titles.add(list.get(i).getContent());
								}
								autoScrollTextView.setTextList(titles);
								if (!autoScrollTextView.getIsScroll()) {
									autoScrollTextView.startAutoScroll();
								}
							} else {
								autoScrollLayout.setVisibility(View.GONE);
								autoScrollTextView.stopAutoScroll();
							}
						} else {
							autoScrollLayout.setVisibility(View.GONE);
							autoScrollTextView.stopAutoScroll();
						}
					} else {
						autoScrollLayout.setVisibility(View.GONE);
						autoScrollTextView.stopAutoScroll();
						ToastUtil.shortToast(mFragmentActivity, hallTextList.getResMsg());
					}
				}
			});
		}
		
		//获取首页轮播文字-失败 请求4
		@Override
		public void getHallTextFailure(final String message) {
			mFragmentActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getControllLottery();
					autoScrollLayout.setVisibility(View.GONE);
					autoScrollTextView.stopAutoScroll();
//					ToastUtil.shortToast(mFragmentActivity, message);
				}
			});
		}

		//获取彩种列表控制-成功 请求5
		public void getHallControllLotterySuccess(final ControllLottery cLottery) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
					if (App.userInfo != null) {
						getMessageNum();
					}else {
						InitQuickBet();
					}
					if (cLottery != null && cLottery.getResCode() != null
							&& cLottery.getResCode().equals("0")) {
						List<GameObj> list = cLottery.getGameList();
						if (list != null) {
							for (int i = 0; i < list.size(); i++) {
								GameObj obj = list.get(i);
								if (obj != null) {
									setLotteryStatus(obj);
								}
								
							}
						}
					}
					
				}
			});
		};
		
		//获取列表控制-失败  请求5
		public void getHallControllLotteryError(String error) {
//			System.out.println("获取失败");
			if (App.userInfo != null) {
				getMessageNum();
			}else {
				InitQuickBet();
			}
		};
		

		// 获取未读消息个数成功 请求6
		public void getNewNoticeOrMessageSuccess(
				final NoticeOrMessageCount count) {
			mFragmentActivity.runOnUiThread(new Runnable() {
				public void run() {
					//获取快速投注信息
					InitQuickBet();
					NoticeOrMessageCount result = count;
					if (result != null && result.getResCode().equals("0")) {
						String msgRead = result.getIsNotMsg();
						if (!TextUtils.isEmpty(msgRead)) {
							if (msgRead.equals("0")) {
								redPoint.setVisibility(View.GONE);
							} else {
								redPoint.setVisibility(View.VISIBLE);
							}
						} else {
							redPoint.setVisibility(View.GONE);
						}
					} else {
						redPoint.setVisibility(View.GONE);
					}
				}

			});
		};

		// 获取未读消息个数失败 请求6
		public void getNewNoticeOrMessageFailure(String error) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
//					getHallText();
					//获取快速投注信息
					InitQuickBet();
				}
			});
		};
	
		//快投-一场制胜请求-成功  请求7
		public void getFocusMatchSuccess(final FocusMatch fMatch) {
			mFragmentActivity.runOnUiThread(new Runnable() {
				public void run() {
					if (!mFlagChangeMatch) {
						InitQuickBet_Dlt();
					}
					if (fMatch != null && fMatch.getResCode() != null
							&& fMatch.getResCode().equals("0")) {
						mFMatch = fMatch;
						Match match = fMatch.getMatch();
						if (match == null) {
							//没有比赛
							flag_0_match = true;
							mHandler.sendEmptyMessage(QUICK_BET_NO_MATCH_1CZS);
						}else {
							flag_0_match = false;
							if (tab_quick_only_win.isChecked()) {
								layout_hall_jiaodian.setVisibility(View.VISIBLE);
								linearlayout_accident_occured.setVisibility(View.GONE);
								quick_bet_content_dlt.setVisibility(View.GONE);
								id_lobo_hall_dlt_layout_tZ.setVisibility(View.VISIBLE);
							}
							
							String terminate_time = DateUtils.formatTimeToString(match.getMatchSellOutTime());
							layout_hall_jiaodian_terminate_time.setText(
									terminate_time==null?"0":terminate_time.substring(0,terminate_time.length()-3)
									);//截止时间
//							time.setText(DateUtil.getTimeInMillisToStr(match
//									.getMatchSellOutTime() + ""));
//							layout_hall_jiaodian_matchsort.setText(match.getMatchSort());//场次（001，002...）
							layout_hall_jiaodian_leagueshort.setText(match.getLeagueShort());//联赛类型（国王杯...）
							layout_hall_jiaodian_hometeamname.setText(match.getHomeTeamName());
							layout_hall_jiaodian_guestteamname.setText(match.getGuestTeamName());

							MatchAgainstSpInfo spInfo = fMatch.getSpInfo();
							String[] realTimeSps = spInfo.getRealTimeSp()
									.split("@");
							if (realTimeSps[0].split("_").length < 2) {
								// sspfStop.setVisibility(View.VISIBLE);
							} else {
								// 注意这里文字之后只添加一个空格，在拆分的事后对应
								String sp_sString = realTimeSps[0].split("_")[1];
								String sp_pString = realTimeSps[1].split("_")[1];
								String sp_fString = realTimeSps[2].split("_")[1];
								layout_hall_jiaodian_s.setText(sp_sString);
								layout_hall_jiaodian_p.setText(sp_pString);
								layout_hall_jiaodian_f.setText(sp_fString);
								//找到sp的最小值，将选框默认停留在其上
								if (!TextUtils.isEmpty(sp_sString)&&!TextUtils.isEmpty(sp_sString)&&!TextUtils.isEmpty(sp_sString)) {
									float sp_s = Float.parseFloat(sp_sString);
									float sp_p = Float.parseFloat(sp_pString);
									float sp_f = Float.parseFloat(sp_fString);
									if (sp_s <= sp_p && sp_s <= sp_f) {
										if (!layoutView_hall_jiaodian_win.isSelected()) {
											onClick(layoutView_hall_jiaodian_win);
										}
									}else if (sp_p <= sp_s && sp_p <= sp_f) {
										if (!layoutView_hall_jiaodian_vs.isSelected()) {
											onClick(layoutView_hall_jiaodian_vs);
										}
									}else {
										if (!layoutView_hall_jiaodian_lost.isSelected()) {
											onClick(layoutView_hall_jiaodian_lost);
										}
									}
									
								}
							}
							setBeiJiaoDianMoney();
							// 通过竞彩截至时间来判断是否需要重新加载焦点比赛，
							// 因为焦点比赛需要实时更新
							countDownJiaoDian(match.getMatchSellOutTime());
							
						}
						
					} else {
						mHandler.sendEmptyMessage(QUICK_BET_NO_REQUEST_1CZS);
					}
				}
			});
		};

		//快投-一场制胜-失败  请求7
		public void getFocusMatchFailure(String error) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
					if (!mFlagChangeMatch) {
						InitQuickBet_Dlt();
					}
					mHandler.sendEmptyMessage(QUICK_BET_NO_REQUEST_1CZS);
				}
			});
		};
		
		//快投-大乐透 -成功  请求8
		public void getLoBoPeriodSuccess(final List<LoBoPeriodInfo> infos) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					if (infos != null && infos.size() > 0) {
						// 如果获取到大乐透投注信息，就隐藏该模板视图
						if (tab_quick_dlt.isChecked()) {
							layout_hall_jiaodian.setVisibility(View.VISIBLE);
							linearlayout_accident_occured.setVisibility(View.GONE);
							quick_bet_content_dlt.setVisibility(View.VISIBLE);
							id_lobo_hall_dlt_layout_tZ.setVisibility(View.GONE);
						}
						periodInfo = infos.get(0);
						textView_hall_dlt_period.setText(getResources().getString(
								R.string.record_tz_detail_qi,
								periodInfo.getIssue()));
						countDownDlt(periodInfo);
					} else{
						mHandler.sendEmptyMessage(QUICK_BET_NO_REQUEST_DLT);// 如果没有获取到大乐透投注信息，就隐藏该模板视图
					}
					mIsRequest = true;
				}
			});
		};

		//快投-大乐透 请求8
		public void getLoBoPeriodFailure(final String error) {
			mFragmentActivity.runOnUiThread(new Runnable() {

				public void run() {
					hideLoadingDialog();
					// 如果没有获取到大乐透投注信息，就隐藏该模板视图
					mHandler.sendEmptyMessage(QUICK_BET_WITHOUT_NET);
					mIsRequest = true;
				}
			});
		};
	
	};
	
	/**
	 * 首页轮播文字
	 */
	private void getHallText() {
		Controller.getInstance().getHallText(GlobalConstants.NUM_HALLTEXT, mBack);
	}
	
	private void getControllLottery() {
		Controller.getInstance().getHallControllLottery(GlobalConstants.NUM_CONTROLLLOTTERY, controllTimeId, "11", mBack);
	}
	
	/**
	 * 设置通知
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年3月25日
	 */
	private void setNotice() {
		if (mNotice != null) {
			String showType = mNotice.getShowType();
			if (!StringUtil.isEmpty(showType)) {
				List<NoticeContent> eventList = mNotice.getEventContent();
				final NoticeContent eventContent = eventList.get(0);
				// 根据传值的不同类别加载不同的dialog 1文本 2图片
				if (showType.equals("1")) {
					// 这是消息活动页 修改提示框时暂时不更改样式
					final PromptDialog0 dialog = new PromptDialog0(
							mFragmentActivity);
					dialog.setMessage(eventContent.getContent());
					dialog.setNegativeButton(getString(R.string.btn_canl),
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.hideDialog();

								}
							});
					dialog.setPositiveButton(getString(R.string.btn_ok),
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									dialog.hideDialog();
									if (eventContent.getIsWeb().equals("y")) {
										if (eventContent.getIsLogin().equals(
												"y")) {
											if (App.userInfo != null) {
												Uri uri = Uri
														.parse(eventContent
																.getWebUrl());
												Intent it = new Intent(
														Intent.ACTION_VIEW, uri);
												startActivity(it);
											} else {
												startActivity(LoginActivity.class);
											}
										} else {
											Uri uri = Uri.parse(eventContent
													.getWebUrl());
											Intent it = new Intent(
													Intent.ACTION_VIEW, uri);
											startActivity(it);
										}

									} else {
										// 跳转本地activity
										Intent intent = new Intent(
												mFragmentActivity,
												TextDetailActivity.class);
										intent.putExtra("title",
												eventContent.getTitle());
										intent.putExtra("time",
												eventContent.getPubTime());
										intent.putExtra("content",
												eventContent.getContent());
										startActivity(intent);
									}
								}
							});
					dialog.showDialog();
				} else {

					// 将图片加载到自定义的dialog中
					final PromptDialog1 dialog1 = new PromptDialog1(
							mFragmentActivity);
					if (!StringUtil.isEmpty(mNotice.getImgUrl())) {
						dialog1.setImageView(mNotice.getImgUrl());
						// isLogin表示显示要不要求登陆 y-需要 n-不需要
						if (eventContent.getIsLogin().equals("y")) {
							// 展示弹出广告
							if ((App.userInfo != null) && app.isFirstShow()) {
								dialog1.showDialog();
								app.setFirstShow(false);
							}
						} else if (eventContent.getIsLogin().equals("n")) {
							if (app.isFirstShow()) {
								dialog1.showDialog();
								app.setFirstShow(false);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 设置升级提示
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年3月25日
	 */
	private void setUpgrade(Upgrade mUpgrade) {
		// if (mUpgrade != null) {
		String isMust = mUpgrade.getIsMust();
		String isUpdate = mUpgrade.getIsUpdate();
		final String version = mUpgrade.getNewVersion();
		final String downUrl = mUpgrade.getDownUrl();
		//强制更新逻辑
		flag_back_pressed = false;
		flag = 9999;
		// 升级提示的dialog还是为红色 2.5期不进行修改
		dialog.setMessage(mUpgrade.getVersionMsg());
		// 非强制更新
		if (!TextUtils.isEmpty(isUpdate)) {
			if ("y".equals(isUpdate)) {
				// 需要更新
				if (!TextUtils.isEmpty(isMust)) {
					if ("y".equals(isMust)) {
						// 需要强制更新
						dialog.setTitle("软件更新");
						dialog.setPositiveButton("立即更新", new OnClickListener() {
							@Override
							public void onClick(View v) {
								// 强制更新 开始
								// 判断用户网络状态 wifi-自动下载
								// 其他-提醒用户更换网络
								int type = NetUtil
										.getNetworkType(getActivity());
								// type = 1 表示已经连接wifi网络
								// 直接帮用户进行下载
								if (type == 1) {
									downloadAPK_By_Okhttp(version, downUrl);
									dialog.setClickable(2, 2);
									dialog.hideDialog();
								} else if (type == 0) {
									ToastUtil.shortToast(mFragmentActivity,
											"您未连接网络，请连接后重试！");
								} else if (type == 2 || type == 3) {
									final PromptDialog0 dialogWlan = new PromptDialog0(
											mFragmentActivity);
									dialogWlan
											.setMessage("您现在处于2G/3G/4G联网模式下，确定使用此网络下载吗？");
									dialogWlan.setNegativeButton("连接WIFI",
											new OnClickListener() {
												@Override
												public void onClick(View v) {
													// 非wifi网络，打开wifi连接界面
													dialogWlan.hideDialog();
													startActivity(new Intent(
															Settings.ACTION_WIFI_SETTINGS));
													NetUtil.closeUsertNet(mFragmentActivity);
												}
											});
									dialogWlan.setPositiveButton("确定",
											new OnClickListener() {
												@Override
												public void onClick(View v) {
													// 点击安装
													downloadAPK_By_Okhttp(
															version, downUrl);
													dialogWlan.setClickable(2,
															2);
													dialogWlan.hideDialog();
												}
											});
									dialogWlan.showDialog();
								}
							}

						});
						dialog.setOnKeydownListener(new onKeydownListener() {
							@Override
							public int onBackClicked() {
								System.out.println("拦截到了返回键的点击事件");
								return 1;
							}
						});
						if (!dialog.isShowing()) {
							dialog.showDialog();
						}
					} else if (MainActivity.isFirstShow) {
						// 非强制更新，判断是否为最新版本，是否更新，是否在评审
						dialog = new PromptDialog0(
								mFragmentActivity);
						// 请求成功
						dialog.setTitle("软件更新");
						dialog.setMessage("软件有新版本了，现在更新？");
						dialog.setNegativeButton("稍后再说",
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										dialog.hideDialog();
									}
								});
						dialog.setPositiveButton("立即更新",
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										flag = 2;
										downloadAPK_By_Okhttp(version, downUrl);
										dialog.setClickable(2, 2);
										dialog.hideDialog();

									}

								});
						if (!dialog.isShowing()) {
							dialog.showDialog();
						}
						// 标志位置成false 不需要给用户重复展示
						MainActivity.isFirstShow = false;
					}
				}
			} else {
				// 不需要更新
			}
		}
	}
	
	/**
	 * 更新最近开奖页面
	 * @Description:
	 * @param location
	 * @author:www.wenchuang.com
	 * @date:2016-11-24
	 */
	private void updateLotteryDraw(int location) {
		View view = listViews.get(location);
		TextView title = (TextView)view.findViewById(R.id.txtView_looterydraw_title);
		TextView term = (TextView)view.findViewById(R.id.txtView_looterydraw_term);
		TextView date = (TextView)view.findViewById(R.id.txtView_looterydraw_date);
		FlowLayout num = (FlowLayout)view.findViewById(R.id.cvView_lotterydraw_number);
		((ImageView) view.findViewById(R.id.id_image)).setVisibility(View.GONE);
		date.setVisibility(View.GONE);
		mCurrent = location;
		switch (location) {
		case 0:
			setItemView(title, term, date, num, GlobalConstants.TC_DLT, false);
			break;
		case 1:
			setItemView(title, term, date, num, GlobalConstants.TC_SF14, true);
			break;
		case 2:
			setItemView(title, term, date, num, GlobalConstants.TC_QXC, false);
			break;
		case 3:
			setItemView(title, term, date, num, GlobalConstants.TC_PL3, false);
			break;
		case 4:
			setItemView(title, term, date, num, GlobalConstants.TC_PL5, false);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 设置每个开奖item
	 * @Description:
	 * @param title
	 * @param term
	 * @param date
	 * @param num
	 * @param gameNo
	 * @param showBg
	 * @author:www.wenchuang.com
	 * @date:2016-11-24
	 */
	private void setItemView(TextView title, TextView term, TextView date, FlowLayout num, String gameNo, boolean showBg) {
		for (LotteryDrawInfo info : mList) {
			if (info != null) {
				if (info.getGameNo().equals(gameNo)) {
					currInfo = info;
					title.setText(info.getGameName());
					term.setText(getActivity().getString(R.string.record_tz_detail_qi, info.getIssueNo()));
					if (!StringUtil.isEmpty(info.getAwardDate())) {
						date.setText(info.getAwardDate().substring(0, 10));
					}
					
					// 篮球
					String winbasecode = "";
					// 红球
					String winspecialcode = "";
					String[] jiCode = info.getWinCode().split("\\ ");
					if (jiCode != null && jiCode.length > 0) {
						winbasecode = jiCode[0];
					}
					if (jiCode != null && jiCode.length > 1) {
						winspecialcode = jiCode[1];
					}
					num.removeAllViews();
					LotteryShowUtil.showNumberLotteryView(getActivity(), 0, num, winbasecode, showBg);
					LotteryShowUtil.showNumberLotteryView(getActivity(), 1, num, winspecialcode, showBg);
					
				}
			}
		}
	}
	
	/**
	 * 下载apk
	 * @Description:
	 * @param version
	 * @param url
	 * @author:www.wenchuang.com
	 * @date:2016-11-24
	 */
	private void downloadAPK_By_Okhttp(final String version, String url) {
		Request request = new Request.Builder().url(url).build();
		if (client == null) {
			/** 下载相关的okhttp必备--初始化 **/
			client = new OkHttpClient();
		}
		Call call = client.newCall(request);
		this.version = version;
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				double currentProgress;
				// 参数准备
				InputStream is = null;
				byte[] buf = new byte[2048];
				int len = 0;
				FileOutputStream fos = null;
				// 请求数据解析
				try {
					// String str = "/mnt/sdcard/" + version + ".apk";
					File file = new File(Environment
							.getExternalStorageDirectory(), "temp" + version
							+ ".apk");
					double total = response.body().contentLength();
					totalProgress = total / 1024.0 / 1024.0;
					// Log.e("wushiqiu", "total------>" + total);
					long current = 0;
					is = response.body().byteStream();
					fos = new FileOutputStream(file);
					// 更新下载的提示框
					dialogProgress.setTitle("正在下载......");
					if (!dialogProgress.isShowing()) {
						dialogProgress.showProgress();
					}
					while ((len = is.read(buf)) != -1 && !flag_back_pressed) {
						current += len;
						currentProgress = current / 1024.0 / 1024.0;
						fos.write(buf, 0, len);
						// Log.e("wushiqiu", "current------>" + current);

						Message msg = Message.obtain();
						msg.what = UPDATE_PROGRESS;
						msg.obj = currentProgress;
						mHandler.sendMessage(msg);
					}

					fos.flush();

				} catch (Exception e) {
				} finally {
					try {
						if (is != null) {
							is.close();
						}
						if (fos != null) {
							fos.close();
						}
					} catch (IOException e) {
						Log.e("wushiqiu", e.toString());
					}
				}
			}
		});
	}
	
	/**
	 * 下载进度的处理
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			dialogProgress.setOnKeydownListener(new onKeydownListener() {

				@Override
				public int onBackClicked() {
					// 返回
					if (flag == 2) {
						flag_back_pressed = true;
						return 9999;
					} else {
						return 1;
					}
				}
			});
			switch (msg.what) {
			
			case UPDATE_PROGRESS:
				// 版本更新--更新进度框进度
				DecimalFormat df = new DecimalFormat("#0.00");
				dialogProgress.setMessage("安装进度为：" + df.format(msg.obj) + "M/"
						+ df.format(totalProgress) + "M");
				if (!dialogProgress.isShowing() && !flag_back_pressed) {
					dialogProgress.showDialog();
				}
				dialogProgress.updateProgress((int)((double)msg.obj/totalProgress*100));
				if (totalProgress == (double) msg.obj) {
					dialog.hideDialog();
					dialogProgress.hideDialog();
					updateFinish = true;
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					Uri data = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "temp" + version
							+ ".apk"));
					intent.setDataAndType(data,
							"application/vnd.android.package-archive");
					startActivity(intent);
				}
				dialogProgress.setCancelable(false);

				break;
			case UPDATE_HIAODIAN_VIEW_GONE:// 通知隐藏焦点比赛内容

				id_lobo_hall_dlt_layout_tZ.setVisibility(View.GONE);

				break;
			case UPDATE_PERIOD_VIEW_GONE:// 通知隐藏大乐透投注内容
				id_lobo_hall_dlt_layout.setVisibility(View.GONE);
				id_lobo_hall_dlt_layout_tZ.setVisibility(View.GONE);
				break;
				
			case QUICK_BET_WITHOUT_NET:
				//快投 无网络
				layout_hall_jiaodian.setVisibility(View.GONE);
				linearlayout_accident_occured.setVisibility(View.VISIBLE);
				textview_accident_explain.setText("网络不给力~");
				textview_accident_reload.setVisibility(View.VISIBLE);
				
				break;
			case QUICK_BET_RELOAD_DATA:
				//重新获取数据
				getHallFragment();
				layout_hall_jiaodian.setVisibility(View.VISIBLE);
				linearlayout_accident_occured.setVisibility(View.GONE);
				scrollHead.fullScroll(ScrollView.FOCUS_UP);
				
				break;
			case QUICK_BET_NO_MATCH_1CZS:
				//没有一场制胜的比赛
				if (tab_quick_only_win.isChecked()) {
					layout_hall_jiaodian.setVisibility(View.GONE);
					linearlayout_accident_occured.setVisibility(View.VISIBLE);
					textview_accident_reload.setVisibility(View.GONE);
				}
				textview_accident_explain.setText("未发布赛事，过会再来吧~");
				break;
			case QUICK_BET_NO_REQUEST_1CZS:
				
				//1czs服务器返回有问题
				if (tab_quick_only_win.isChecked()) {
					layout_hall_jiaodian.setVisibility(View.GONE);
					linearlayout_accident_occured.setVisibility(View.VISIBLE);
				}
				textview_accident_reload.setVisibility(View.GONE);
				textview_accident_explain.setText("迷路啦~过会再来吧~");
				
				break;
				
			case QUICK_BET_NO_REQUEST_DLT:
				
				//dlt服务器返回有问题
				if (tab_quick_dlt.isChecked()) {
					layout_hall_jiaodian.setVisibility(View.GONE);
					linearlayout_accident_occured.setVisibility(View.VISIBLE);
				}
				textview_accident_reload.setVisibility(View.GONE);
				textview_accident_explain.setText("迷路啦~过会再来吧~");
				
				break;
			default:
				break;
			}
		};
	};
	
	private void setLotteryStatus(GameObj obj) {
		int status = -1;
		//status 0-停售 1-销售 2-加奖 3-活动 4-今日开奖
		if (!TextUtils.isEmpty(obj.getIsSale()) && obj.getIsSale().equals("0")) {
			//是否销售：1、销售，0、停售
			status = 1;
		} else {
			if (!obj.getIsAward().equals("0")) {
				//是否嘉奖：0无图标，1嘉奖,2 活动
				if (obj.getIsAward().equals("1")) {
					status = 2;
				} else {
					status = 3;
				}
			} else {
				if (obj.getIsTodayAward().equals("1")) {
					//是否今日开奖：0否，1是
					status = 4;
				}
			}
		}
		switch (obj.getGameNo()) {
		case GlobalConstants.TC_JCZQ:
//			jczqStatus
			setTitleColor(jczqTitle, status);
			setControllLottery(jczqStatus, jczqText, jczqLayout, status);
			
			setTitleColor(czsTitle, status);
			setControllLottery(czsStatus, czsText, czsLayout, status);
			
			setTitleColor(x1Title, status);
			setControllLottery(x1Status, x1Text, x1Layout, status);
			break;
		case GlobalConstants.TC_SF9:
		case GlobalConstants.TC_SF14:
//			sfcStatus
			setTitleColor(sfcTitle, status);
			setControllLottery(sfcStatus, sfcText, sfcLayout, status);
			break;
		case GlobalConstants.TC_JCLQ:
			setTitleColor(jclqTitle, status);
			setControllLottery(jclqStatus, jclqText, jclqLayout, status);
			break;
		case GlobalConstants.TC_DLT:
			setTitleColor(dltTitle, status);
			setControllLottery(dltStatus, dltText, dltLayout, status);
			break;
		case GlobalConstants.TC_PL3:
			setTitleColor(p3Title, status);
			setControllLottery(p3Status, p3Text, p3Layout, status);
			break;
		case GlobalConstants.TC_QXC:
			setTitleColor(qxcTitle, status);
			setControllLottery(qxcStatus, qxcText, qxcLayout, status);
			break;
		default:
			break;
		}
	}
	
	private void setTitleColor(TextView text, int status) {
		//status 0-停售 1-销售 2-加奖 3-活动 4-今日开奖
		switch (status) {
		case 2:
		case 3:
		case 4:
			text.setTextColor(getResources().getColor(R.color.color_red));
			break;
		default:
			break;
		}
	}
	
	public void setControllLottery(ImageView view, TextView text, LinearLayout layout, int status){
		//status 0-停售 1-销售 2-加奖 3-活动 4-今日开奖
		switch (status) {
		case 0:
			layout.setEnabled(false);
			view.setImageDrawable(getResources().getDrawable(R.drawable.icon_lottery_stop));
			text.setText(getResources().getString(R.string.hall_lottery_stop));
			view.setVisibility(View.VISIBLE);
			text.setTextColor(getResources().getColor(R.color.color_deep_gray_line));
			break;
		case 2:
			layout.setEnabled(true);
			view.setImageDrawable(getResources().getDrawable(R.drawable.icon_lottery_other));
			text.setText(getResources().getString(R.string.hall_lottery_jiajiang));
			view.setVisibility(View.VISIBLE);
			text.setTextColor(getResources().getColor(R.color.color_yellow_light));
			break;
		case 3:
			layout.setEnabled(true);
			view.setImageDrawable(getResources().getDrawable(R.drawable.icon_lottery_other));
			text.setText(getResources().getString(R.string.hall_lottery_huodong));
			view.setVisibility(View.VISIBLE);
			text.setTextColor(getResources().getColor(R.color.color_yellow_light));
			break;
		case 4:
			layout.setEnabled(true);
			view.setImageDrawable(getResources().getDrawable(R.drawable.icon_lottery_other));
			text.setText(getResources().getString(R.string.hall_lottery_kaijiang));
			view.setVisibility(View.VISIBLE);
			text.setTextColor(getResources().getColor(R.color.color_yellow_light));
			break;
		default:
			layout.setEnabled(true);
			view.setVisibility(View.INVISIBLE);
			text.setText("");
			break;
		}
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_hall_sign:
			if (App.userInfo == null) {
				startActivity(LoginActivity.class);
			} else {
				startActivity(SignActivity.class);
			}
			break;
		case R.id.layout_hall_act:
			//最近活动
			Intent convertIntent = new Intent(mFragmentActivity, MessagePersonActivity_Act.class);
			startActivity(convertIntent);
			break;
		case R.id.layout_hall_game:
			startActivity(MainGameActivity.class);
			break;
		case R.id.layout_hall_info:
			Intent it = new Intent(mFragmentActivity, HtmlCommonActivity.class);
			it.putExtra("isWeb", true);
			it.putExtra("webUrl", GlobalConstants.getZSTUrl(4));
			it.putExtra("title", getResources().getString(R.string.lotteryhall_info));
			startActivity(it);
			break;
		case R.id.layout_hall_zst:
			startActivity(ZSTListActivity.class);
			break;
		case R.id.layout_hall_jczq:
			startActivity(LotteryFootballActivity.class);
			break;
		case R.id.layout_hall_1czs:
			Intent iItent = new Intent(mFragmentActivity, LotteryFootballActivity.class);
			iItent.putExtra("selectIndex", 5);
			startActivity(iItent);
			break;
		case R.id.layout_hall_2x1:
			Intent iiItent = new Intent(mFragmentActivity, LotteryFootballActivity.class);
			iiItent.putExtra("selectIndex", 6);
			startActivity(iiItent);
			break;
		case R.id.layout_hall_sfc:
			Intent intent = new Intent(getActivity(), LotteryOldFootballActivity.class);
			intent.putExtra("lotoId", 0);
			startActivity(intent);
			break;
		case R.id.layout_hall_jclq:
			startActivity(LotteryBasketballActivity.class);
			break;
		case R.id.layout_hall_p3:
			startActivity(LotteryPL3Activity.class);
			break;
		case R.id.layout_hall_dlt:
			startActivity(LotteryDLTActivity.class);
			break;
		case R.id.layout_hall_qxc:
			startActivity(LotteryQxcActivity.class);
			break;
		case R.id.layout_hall_other:
			startActivity(OtherLotteryActivity.class);
			break;
		case R.id.layout_hall_bifen:
			startActivity(ScoreListActivity.class);
			break;
		case R.id.imageView_hall_photo:
			MainActivity.mViewPager.setCurrentItem(3, false);
			break;
		case R.id.layoutView_hall_jiaodian_win:
			//快投-一场制胜-主队
			if (layoutView_hall_jiaodian_win.isSelected()) {
				layoutView_hall_jiaodian_win.setSelected(false);
				imageView_jiaodian_win.setVisibility(View.GONE);
			} else {
				layoutView_hall_jiaodian_win.setSelected(true);
				imageView_jiaodian_win.setVisibility(View.VISIBLE);
				layoutView_hall_jiaodian_vs.setSelected(false);
				imageView_jiaodian_vs.setVisibility(View.GONE);
				layoutView_hall_jiaodian_lost.setSelected(false);
				imageView_jiaodian_lost.setVisibility(View.GONE);
			}
			setBeiJiaoDianMoney();
			break;
		case R.id.layoutView_hall_jiaodian_vs:
			//快投-一场制胜-VS
			if (layoutView_hall_jiaodian_vs.isSelected()) {
				layoutView_hall_jiaodian_vs.setSelected(false);
				imageView_jiaodian_vs.setVisibility(View.GONE);
			} else {
				layoutView_hall_jiaodian_vs.setSelected(true);
				imageView_jiaodian_vs.setVisibility(View.VISIBLE);
				layoutView_hall_jiaodian_lost.setSelected(false);
				imageView_jiaodian_lost.setVisibility(View.GONE);
				layoutView_hall_jiaodian_win.setSelected(false);
				imageView_jiaodian_win.setVisibility(View.GONE);
			}
			setBeiJiaoDianMoney();
			break;
		case R.id.layoutView_hall_jiaodian_lost:
			//快投-一场制胜-客队
			if (layoutView_hall_jiaodian_lost.isSelected()) {
				layoutView_hall_jiaodian_lost.setSelected(false);
				imageView_jiaodian_lost.setVisibility(View.GONE);
			} else {
				layoutView_hall_jiaodian_lost.setSelected(true);
				imageView_jiaodian_lost.setVisibility(View.VISIBLE);
				layoutView_hall_jiaodian_vs.setSelected(false);
				imageView_jiaodian_vs.setVisibility(View.GONE);
				layoutView_hall_jiaodian_win.setSelected(false);
				imageView_jiaodian_win.setVisibility(View.GONE);
			}
			setBeiJiaoDianMoney();
			break;
		case R.id.imgView_1czs_plus:
			//快投-一场制胜 加倍
			add1czsMultiple();
			break;
		case R.id.imgView_1czs_minus:
			//快投-一场制胜 减倍
			minus1czsMultiple();
			break;
		case R.id.imgView_dlt_plus:
			//快投-大乐透-加两米
			addDltMultiple();
			break;
		case R.id.imgView_dlt_minus:
			//快投-大乐透-减两米
			minusDltMultiple();
			break;
		case R.id.textView_hall_dlt_refresh:
			//快投-大乐透 换一注
			dltjx();
			break;
		case R.id.textView_jiaodian_tz:
			//快投-一场制胜 投注
			quick_bet_1czs();
			break;
		case R.id.textView_dlt_tz:
			//快投-大乐透 投注
			quick_bet_dlt();
			break;
		case R.id.textView_1czs_match_change:
			//换一场
			changeMatch_1czs();
			break;
		case R.id.textview_accident_reload:
			showLoadingDialog();
			mHandler.sendEmptyMessage(QUICK_BET_RELOAD_DATA);
			break;
		default:
			break;
		}
	};
	
	
	/**
	 * 快投-一场制胜 换一换
	 */
	private void changeMatch_1czs() {
		
		mFlagChangeMatch = true;
		Controller.getInstance().getFocusMatch(
				GlobalConstants.NUM_FOCUSMATCH,
				GlobalConstants.TC_JCZQ, "1", mBack);
	}

	/**
	 * 快投-一场制胜-加一倍
	 */
	private void add1czsMultiple() {
		String multiple = editView_bet_muitiple_1czs.getText().toString().trim();
		if (multiple != null && multiple.length() > 0) {
			int result = Integer.parseInt(multiple);
//			if (result<5) {
//				editView_bet_muitiple_1czs.setText("5");
//				ToastUtil.shortToast(mFragmentActivity, "最小投注倍数为5倍");
//			}else 
			if (result > 999) {
				editView_bet_muitiple_1czs.setText("999");
				ToastUtil.shortToast(mFragmentActivity, "最高选择999倍");
			}else {
				// 加一倍
				result += 1;
				editView_bet_muitiple_1czs.setText(result+"");
			}
			
		}
	}
	
	/**
	 * 快投-一场制胜-减一倍
	 */
	private void minus1czsMultiple() {
		String multiple = editView_bet_muitiple_1czs.getText().toString().trim();
		if (multiple != null && multiple.length() > 0) {
			int result = Integer.parseInt(multiple);
//			if (result<5) {
//				editView_bet_muitiple_1czs.setText("5");
//				ToastUtil.shortToast(mFragmentActivity, "最小投注倍数为5倍");
//			}else 
			if (result > 999) {
				editView_bet_muitiple_1czs.setText("999");
				ToastUtil.shortToast(mFragmentActivity, "最高选择999倍");
			}else {
				// 减一倍
				if (result <= 5) {
					return;
				}else {
					result -= 1;
				}
				editView_bet_muitiple_1czs.setText(result+"");
			}
			
		}
	}
	
	/**
	 * 快投-大乐透-加两米
	 */
	private void addDltMultiple() {
		String multiple = editView_bet_muitiple_dlt.getText().toString().trim();
		if (multiple != null && multiple.length() > 0) {
			int result = Integer.parseInt(multiple);
//			if (result<2) {
//				editView_bet_muitiple_dlt.setText("5");
//				ToastUtil.shortToast(mFragmentActivity, "最小投注数额为2米");
//			}else 
			if (result >= 1998) {
				editView_bet_muitiple_dlt.setText("1998");
				ToastUtil.shortToast(mFragmentActivity, "最高选择1998米");
			}else {
				// 加一倍
				if (result%2 != 0) {
					result += 3;
				}else {
					result += 2;
				}
				editView_bet_muitiple_dlt.setText(result+"");
			}
			
		}
	}
	
	/**
	 * 快投-大乐透-减两米
	 */
	private void minusDltMultiple() {
		String multiple = editView_bet_muitiple_dlt.getText().toString().trim();
		if (multiple != null && multiple.length() > 0) {
			int result = Integer.parseInt(multiple);
//			if (result<2) {
//				editView_bet_muitiple_dlt.setText("2");
//				ToastUtil.shortToast(mFragmentActivity, "最小投注数额为2米");
//			}else 
			if (result > 1998) {
				editView_bet_muitiple_dlt.setText("1998");
				ToastUtil.shortToast(mFragmentActivity, "最高选择1998米");
			}else if (result <= 2) {
				return;
			}else if (result == 3) {
				editView_bet_muitiple_dlt.setText("2");
			}else{
				// 减一倍
				if (result %2 != 0) {
					result -= 1;
				}else {
					result -= 2;
				}
				editView_bet_muitiple_dlt.setText(result+"");
			}
			
		}
	}

	/**
	 * 快投-一场制胜-投注按钮点击事件
	 */
	private void quick_bet_1czs() {
		int num = 0;

		if (layoutView_hall_jiaodian_win.isSelected()) {
			num++;
		}
		if (layoutView_hall_jiaodian_vs.isSelected()) {
			num++;
		}
		if (layoutView_hall_jiaodian_lost.isSelected()) {
			num++;
		}
		if (num == 0) {
			ToastUtil.shortToast(mFragmentActivity, "至少选择一个选项");
			return;
		}

		final Match match = mFMatch.getMatch();

		//投注倍数获取
		String multipe = editView_bet_muitiple_1czs.getText().toString();
		if (TextUtils.isEmpty(multipe)) {
			multipe = "5";
			editView_bet_muitiple_1czs.setText(multipe);
			ToastUtil.shortToast(getActivity(), "最小选择5倍");
		}else {
			if (Integer.parseInt(multipe)<5) {
				multipe = "5";
				editView_bet_muitiple_1czs.setText(multipe);
				ToastUtil.shortToast(getActivity(), "最小选择5倍");
			}
		}
		final PromptDialog_Black_Fillet dialog = new PromptDialog_Black_Fillet(
				mFragmentActivity);
		
		dialog.setMessage(Html.fromHtml(getResources().getString(R.string.lottery_betting_jczq_text)
				+ "<font color='" + getResources().getColor(R.color.color_red) + "'>" 
				+ (num * 2 * 2 * Integer.parseInt(multipe)) + "</font>" 
				+ getResources().getString(R.string.lemi_unit)));
		
		dialog.setNegativeButton(getString(R.string.btn_canl),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.hideDialog();

					}
				});
		dialog.setPositiveButton(getString(R.string.btn_ok),
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.hideDialog();
						if (App.userInfo == null) {
							// 未登录、进入登陆页面
							startActivity(new Intent(mFragmentActivity,
									LoginActivity.class));
						} else {
							String multipe = editView_bet_muitiple_1czs.getText().toString();
							if (TextUtils.isEmpty(multipe)) {
								multipe = "5";
							}else {
								if (Integer.parseInt(multipe)<5) {
									multipe = "5";
								}
							}
							// String price =
							// jiaodianMoney.getText().toString().substring(0,
							// (jiaodianMoney.getText().toString().length()
							// -1));
							StringBuilder builder = new StringBuilder();

							Map<Integer, Map<String, String>> lists = new LinkedHashMap<Integer, Map<String, String>>();

							Map<String, String> bettingReq = new LinkedHashMap<String, String>();

							builder.append(match.getIssueNo()
									+ match.getWeek()
									+ match.getMatchSort() + "@");

							builder.append(PlayInfo.JC_ZQ_SPF + "@");

							int codeNumbers = 0;
							if (layoutView_hall_jiaodian_win.isSelected()) {
								builder.append("3_"
										+ layout_hall_jiaodian_s.getText().toString().trim());
								codeNumbers++;
							}
							if (layoutView_hall_jiaodian_vs.isSelected()) {
								builder.append("1_"
										+ layout_hall_jiaodian_p.getText().toString().trim());
								codeNumbers++;
							}
							if (layoutView_hall_jiaodian_lost.isSelected()) {
								builder.append("0_"
										+ layout_hall_jiaodian_f.getText().toString().trim());
								codeNumbers++;
							}

							String date = DateUtil
									.getTimeInMillisToStr(match
											.getMatchSellOutTime() + "");
							builder.append("@" + date);
							builder.append("$"+mFMatch.getSpInfo().getLetScoreMinSp());
							builder.append(";");

							builder.append(mFMatch.getIssueNoMinSp()
									+ mFMatch.getWeekMinSp()
									+ mFMatch.getMatchSortMinSp() + "@");

							String[] sysArr = mFMatch.getRealTimeSpMinSp()
									.split("\\@");
							for (int i = 0; i < sysArr.length; i++) {
								if (Integer.parseInt(mFMatch.getSpInfo().getLetScoreMinSp()) < 0) {
									if (Integer.parseInt(sysArr[i].split("\\_")[0]) == 3) {
										builder.append(PlayInfo.JC_ZQ_SPF + "@");
									} else {
										builder.append(PlayInfo.JC_ZQ_RQSPF + "@");
									}
									builder.append(sysArr[i]);
								} else {
									if (Integer.parseInt(sysArr[i].split("\\_")[0]) == 0) {
										builder.append(PlayInfo.JC_ZQ_RQSPF + "@");
										builder.append("3_" + sysArr[1].split("\\_")[1]);
									} else {
										builder.append(PlayInfo.JC_ZQ_SPF + "@");
										builder.append("0_" + sysArr[0].split("\\_")[1]);
									}
								}
								
								if (i != (sysArr.length - 1)) {
									builder.append("|");
								} else {
									
								}
							}
							
//							builder.append(PlayInfo.JC_ZQ_SPF + "@");
//							builder.append("3_" + sysArr[0].split("\\_")[1]);
//							
//							builder.append("|");
//
//							builder.append(PlayInfo.JC_ZQ_RQSPF + "@");
//							builder.append("0_" + sysArr[1].split("\\_")[1]);

							String sysDate = DateUtil
									.getTimeInMillisToStr(mFMatch
											.getMatchSellOutTimeMinSp()
											+ "");
							builder.append("@" + sysDate);
							builder.append("$"+mFMatch.getSpInfo().getLetScoreMinSp());

							builder.append("^").append("2*1");
							builder.append("^").append(
									mFMatch.getIssueNoMinSp()
											+ mFMatch.getWeekMinSp()
											+ mFMatch.getMatchSortMinSp());

							bettingReq.put("codePlay",
									PlayInfo.JC_ZQ_1CZS_DT);
							// 选中的注码
							bettingReq.put("codeContent",
									builder.toString());
							// 单个倍数
							bettingReq.put("codeMultiple", "1");
							// 单个钱数
							bettingReq.put("codeMoney", 2 * 2 + "");
							// 注数
							bettingReq.put("codeNumbers", 2 + "");

							lists.put(0, bettingReq);

							TzObj obj = new TzObj();
							obj.bettingType = "1";
							obj.gameNo = GlobalConstants.TC_JCZQ;
							obj.issue = "0";
							obj.multiple = multipe;
							obj.totalSum = (codeNumbers * 2 * 2 * Integer
									.parseInt(multipe)) + "";// 订单金额
							obj.chase = "1";
							obj.isChase = "0";
							obj.choiceType = "1";
							obj.planEndTime = "";
							obj.stopCondition = "0";
							obj.gameType = GlobalConstants.TC_JCZQ;
							obj.bettingReq = lists;
							// CommitPayActivity
							Intent intent = new Intent(mFragmentActivity,
									CommitPayActivity.class);
							intent.putExtra(CommitPayActivity.DATA, obj);
							// Intent intent = new Intent(mFragmentActivity,
							// ConfirmPayActivity.class);
							// intent.putExtra(ConfirmPayActivity.DATA,
							// obj);
							intent.putExtra("lotoId",
									FootballLotteryConstants.L502);
							intent.putExtra("selectChuan", "[2*1]");
							intent.putExtra("multiple", multipe);
							startActivity(intent);
						}

					}
				});
		dialog.showDialog();
		
	}
	
	/**
	 * 快投-大乐透-投注
	 */
	private void quick_bet_dlt() {
		final PromptDialog_Black_Fillet dialog1 = new PromptDialog_Black_Fillet(mFragmentActivity);
		String moneyString = editView_bet_muitiple_dlt.getText().toString().trim();
		if (TextUtils.isEmpty(moneyString)) {
			moneyString = "2";
			editView_bet_muitiple_dlt.setText(moneyString);
			ToastUtil.shortToast(getActivity(), "最少选择2米");
		}else {
			if (Integer.parseInt(moneyString)<2) {
				moneyString = "2";
				editView_bet_muitiple_dlt.setText(moneyString);
				ToastUtil.shortToast(getActivity(), "最少选择2米");
			}else {
				if (Integer.parseInt(moneyString)%2 != 0) {
					moneyString = String.valueOf(Integer.parseInt(moneyString)+1);
					editView_bet_muitiple_dlt.setText(moneyString);
					ToastUtil.shortToast(getActivity(), "投注的数额必须为偶数");
				}
			}
		}
		dialog1.setMessage(Html.fromHtml(getResources().getString(R.string.lottery_betting_text)
				+ "<font color='"+getResources().getColor(R.color.color_red)+"'>"
				+ moneyString +"</font>"
				+ "米"));
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
							startActivity(new Intent(mFragmentActivity,LoginActivity.class));
						} else {
							
							//投注总米数
							String price = editView_bet_muitiple_dlt.getText().toString().trim();
							if (TextUtils.isEmpty(price)) {
								price = "2";
								editView_bet_muitiple_dlt.setText(price);
								ToastUtil.shortToast(getActivity(), "最少选择2米");
							}else {
								if (Integer.parseInt(price)<2) {
									price = "2";
									editView_bet_muitiple_dlt.setText(price);
									ToastUtil.shortToast(getActivity(), "最少选择2米");
								}else {
									if (Integer.parseInt(price)%2 != 0) {
										price = String.valueOf(Integer.parseInt(price)+1);
										editView_bet_muitiple_dlt.setText(price);
										ToastUtil.shortToast(getActivity(), "投注的数额必须为偶数");
									}
								}
							}
							String multipe = String.valueOf(Integer.parseInt(price)/2);
							
							StringBuilder sb = new StringBuilder();
							
							sb.append(LotteryBettingUtil.tzDlt(mRedList,mBuleList));

							Map<Integer, Map<String, String>> lists = new LinkedHashMap<Integer, Map<String, String>>();
							Map<String, String> bettingReq = new LinkedHashMap<String, String>();

							bettingReq.put("codePlay", "101011001");
							// 选中的注码
							bettingReq.put("codeContent", sb.toString());
							// 单个倍数
							bettingReq.put("codeMultiple", "1");
							// 单个钱数
							bettingReq.put("codeMoney",(Integer.parseInt(price) / Integer.parseInt(multipe)) + "");
							// 注数
							bettingReq.put("codeNumbers", "1");
							lists.put(0, bettingReq);
							String issue = textView_hall_dlt_period.getText().toString()
									.substring(0,(textView_hall_dlt_period.getText().toString().length() - 1));

							TzObj obj = new TzObj();
							obj.bettingType = "1";
							obj.gameNo = GlobalConstants.TC_DLT;
							obj.issue = issue;
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

							String moneyString = getResources().getString(R.string.all);
							String num = String.valueOf(1);
							String numString = getResources().getString(R.string.betting1);
							String mult = multipe;
							String multString = getResources().getString(R.string.cart_add_bei);
							String term = "1";
							String termString = getResources().getString(R.string.cart_add_qi);
							String totalNum = moneyString + num + numString + mult + multString + term + termString;

							Intent intent = new Intent(mFragmentActivity,CommitPayActivity.class);
							intent.putExtra(CommitPayActivity.DATA, obj);
							intent.putExtra("isQuick", true);
							intent.putExtra("desc", totalNum);
							startActivity(intent);
						}
					}
				});
		dialog1.showDialog();
	}

	/**
	 * 初始化imageloader参数
	 * ImageLoader 这个组件的初始化在APP类中，这个类是Application类的实现类，用于存储全局的上下文和全局变量
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-11-24
	 */
	private void initImageOptions() {
		options = new DisplayImageOptions.Builder()
				.bitmapConfig(Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
	}
	
	/**
	 * 最近开奖滑动处理viewpager
	 * @Description:
	 * @author:zhangjf 
	 * @copyright www.wenchuang.com
	 * @Date:2016-11-24
	 */
	class ViewPagerListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int index) {
			if (index == 0) {
				firstBtn.setChecked(true);
			} else if (index == 1) {
				secondBtn.setChecked(true);
			} else if (index == 2) {
				thirdBtn.setChecked(true);
			} else if (index == 3) {
				fourBtn.setChecked(true);
			} else if (index == 4) {
				fiveBtn.setChecked(true);
			}
			mCurrent = index;
			updateLotteryDraw(index);
		}
	}

		@SuppressLint("NewApi")
	@Override
	protected void lazyLoad() {
		if (!isVisible || !isPrepared) {
			return;
		} else {
			if (photo != null && word != null) {
				if (progress != 0) {
					photo.setVisibility(View.VISIBLE);
					word.setVisibility(View.VISIBLE);
				} else {
					photo.setVisibility(View.GONE);
					word.setVisibility(View.GONE);
				}

				if (VERSION.SDK_INT <= 17) {
					layoutHead.setBackground(new ColorDrawable(progress));
				} else {
					layoutHead.getBackground().mutate().setAlpha(progress);
				}
			}
			
			if (mHallAd != null) {
				timeId = mHallAd.getTimeId();
				images = mHallAd.getList();
				if (pagerAdapter != null) {
					pagerAdapter.setImageIdList(images);
					pagerAdapter.notifyDataSetChanged();
				}
				
				if (slideshowView != null) {
					slideshowView.setVisibility(View.VISIBLE);
					ivHallBannerInitial.setVisibility(View.GONE);
				}
				
			}
			
			if (mNotice != null) {
				noticeTimeId = mNotice.getTimeid();
			}
			
			if (controll != null) {
				controllTimeId = controll.getTimeId();
			}
			
			if (App.userInfo != null) {
				if (!TextUtils.isEmpty(App.userInfo.getHeadImgUrl())) {
					if (imagePhoto != null) {
						imageLoader.displayImage(App.userInfo.getHeadImgUrl(), imagePhoto);
					}
				}
			} else {
				imagePhoto.setImageDrawable(getResources().getDrawable(R.drawable.icon_center_head));
				redPoint.setVisibility(View.GONE);
			}
			
			getHallFragment();
			//走了lazyload（）
			flag_dlt = true;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (updateFinish) {
			updateFinish = false;
			dialogProgress = new PromptDialog0(getActivity());
			dialog = new PromptDialog0(mFragmentActivity);
		}
		setBeiJiaoDianMoney();
		
		mFlagChangeMatch = false;
		if (mShouldInitialize) {
			lazyLoad();
		}else {
			if (!flag_dlt) {
				getHallFragment();
			}else {
				if (isVisible && isPrepared) {
					flag_dlt = false;
				}
			}
		}

		//切换页面的时候 将快投选中框初始化在最左边
//		if (!layoutView_hall_jiaodian_win.isSelected()) {
//			onClick(layoutView_hall_jiaodian_win);
//		}
	}

	@Override
	public void onItemClick(int position) {
		Intent intent1 = new Intent(mFragmentActivity, LotteryDrawInfoActivity.class);
		intent1.putExtra("lotterydrawinfo", currInfo);
		intent1.putExtra("gameName", currInfo.getGameName());
		intent1.putExtra("gameNo", currInfo.getGameNo());
		startActivity(intent1);
	}
	
	/**
	 * 计算焦点比赛预计奖金
	 */
	private void setBeiJiaoDianMoney() {
		float sMoney = 0;
		float pMoney = 0;
		float fMoney = 0;
		String bei = editView_bet_muitiple_1czs.getText().toString();
//		String bei = beiString.substring(0, beiString.indexOf("倍"));
		if (layoutView_hall_jiaodian_win.isSelected()) {
			 String win = layout_hall_jiaodian_s.getText().toString();
			 if (TextUtils.isEmpty(win)) {
				return;
			}else {
				
				sMoney = setJiaoDianMoney(Double.parseDouble(layout_hall_jiaodian_s.getText().toString()),
					Integer.parseInt(TextUtils.isEmpty(bei)?"5":bei));
			}
			
		}else if (layoutView_hall_jiaodian_vs.isSelected()) {
			String draw = layout_hall_jiaodian_p.getText().toString();
			if (TextUtils.isEmpty(draw)) {
				return;
			}else {
				pMoney = setJiaoDianMoney(Double.parseDouble(layout_hall_jiaodian_p.getText().toString()),
						Integer.parseInt(TextUtils.isEmpty(bei)?"5":bei));
			}
			
		}else if (layoutView_hall_jiaodian_lost.isSelected()) {
			String lost = layout_hall_jiaodian_f.getText().toString();
			if (TextUtils.isEmpty(lost)) {
				return;
			}else {
				fMoney = setJiaoDianMoney(Double.parseDouble(layout_hall_jiaodian_f.getText().toString()),
						Integer.parseInt(TextUtils.isEmpty(bei)?"5":bei));
			}
			
		}
		String win_money = new DecimalFormat("########0.00").format(sMoney + pMoney + fMoney);
		textView_hall_jiaodian_money.setText(
				Html.fromHtml("<font color='"+getResources().getColor(R.color.color_red)+"'>"+win_money+"</font>"
			    + "<font color='"+getResources().getColor(R.color.color_gray_secondary)+"'>"
				+ getResources().getString(R.string.lemi_unit)+"</font>"		
				));
	}
	
	/**
	 * 计算预计奖金
	 * 
	 * @Description:
	 * @param sp
	 * @param bei
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年12月1日
	 */
	private float setJiaoDianMoney(double sp, int bei) {
		String sysSp = mFMatch.getRealTimeSpMinSp();
		String[] sysArr = sysSp.split("\\@");
		double tempSp = 0;
		for (int i = 0; i < sysArr.length; i++) {
			if(tempSp < Double.parseDouble(sysArr[i].split("\\_")[1])){
				tempSp = Double.parseDouble(sysArr[i].split("\\_")[1]);
			};
		}
		tempSp = tempSp * sp;
		BigDecimal b1 = new BigDecimal(Double.valueOf(tempSp));
		BigDecimal b2 = new BigDecimal(Double.valueOf(bei * 2));
		return (b1.multiply(b2).floatValue());
	}
	
	private void countDownJiaoDian(Long sellOutTime) {
		Date dt = new Date();
		Long time = dt.getTime();
		mJiaoDianDifferTime = sellOutTime - time;
		// mJiaoDianDifferTime = 1999; 5-5 bug223测试用
		// 测试服务器添加后的字段TimeStep
		if (mJiaoDianDifferTime > 1000) {
			mHandler.postDelayed(mJiaoDianRunnable, 1000);
		}
	}
	
	Runnable mJiaoDianRunnable = new Runnable() {

		@Override
		public void run() {
			mJiaoDianDifferTime -= 1000;
			if (mJiaoDianDifferTime > 0) {
				mHandler.postDelayed(this, 1000);
			} else {
				Controller.getInstance().getFocusMatch(
						GlobalConstants.NUM_FOCUSMATCH,
						GlobalConstants.TC_JCZQ, "1", mBack);
			}
		}
	};
	
	Runnable mDltRunnable = new Runnable() {

		@Override
		public void run() {
			mDltDifferTime -= 1000;
			String formatTime = DateUtil.formatTime(mDltDifferTime);
			String[] timeSplit = formatTime.split(":");
			int size = timeSplit.length;
			if (size == 4) {
				hall_dlt_time_day.setVisibility(View.VISIBLE);
				hall_dlt_time_3.setVisibility(View.VISIBLE);
				hall_dlt_time_day.setText(timeSplit[0]);
				hall_dlt_time_hour.setText(timeSplit[1]);
				hall_dlt_time_minute.setText(timeSplit[2]);
				hall_dlt_time_second.setText(timeSplit[3]);
			} else {
				hall_dlt_time_day.setVisibility(View.GONE);
				hall_dlt_time_3.setVisibility(View.GONE);
				hall_dlt_time_hour.setText(timeSplit[0]);
				hall_dlt_time_minute.setText(timeSplit[1]);
				hall_dlt_time_second.setText(timeSplit[2]);
			}
			if (mDltDifferTime > 0) {
				mHandler.postDelayed(this, 1000);
			} else {
				textView_hall_dlt_period.setText("销售截期中");
				// Controller.getInstance().getLoBoPeriod(GlobalConstants.TC_DLT,GlobalConstants.NUM_DLT,
				// mBack);
			}
		}
	};
	
	/**
	 * 大乐透机选
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016年3月9日
	 */
	public void dltjx() {
		mViewList.clear();
		hall_jx_fl.removeAllViews();
		LotteryShowUtil.randomBall(mRedList, 5, 35);
		LotteryShowUtil.randomBall(mBuleList, 2, 12);
		DecimalFormat format = new DecimalFormat("00");
		for (Integer red : mRedList) {
			LinearLayout redLayout = new LinearLayout(mFragmentActivity);
			redLayout.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			redLayout.setBackgroundResource(R.drawable.lottery_redball_sel);
			redLayout.setGravity(Gravity.CENTER);
			TextView redTV = new TextView(mFragmentActivity);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			// redTV.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT));
			params.setMargins(0, 0, 0, 0);
			redTV.setLayoutParams(params);
			redTV.setTextSize(14);
			redTV.setGravity(Gravity.CENTER);
			redTV.setText(format.format(red + 1));
			redTV.setTextColor(mFragmentActivity.getResources().getColor(
					R.color.color_white));
			mViewList.add(redTV);
			redLayout.addView(redTV);
			hall_jx_fl.addView(redLayout);
		}
		for (Integer bule : mBuleList) {
			LinearLayout buleLayout = new LinearLayout(mFragmentActivity);
			buleLayout.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			buleLayout.setGravity(Gravity.CENTER);
			buleLayout.setBackgroundResource(R.drawable.lottery_blueball_sel);
			TextView buleTV = new TextView(mFragmentActivity);
			// buleTV.setLayoutParams(new
			// LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			params.setMargins(0, 0, 0, 0);
			buleTV.setLayoutParams(params);
			buleTV.setTextSize(14);
			buleTV.setGravity(Gravity.CENTER);
			buleTV.setText(format.format(bule + 1));
			buleTV.setTextColor(mFragmentActivity.getResources().getColor(
					R.color.color_white));
			mViewList.add(buleTV);
			buleLayout.addView(buleTV);
			hall_jx_fl.addView(buleLayout);
		}
		// 开启第一个TextView的动画
		startAnimation(0);
	}
	
	private void startAnimation(int index) {
		if (index >= mViewList.size()) {
			return;
		}
		for (int i = 0; i < mViewList.size(); i++) {

			Animation animation = AnimationUtils.loadAnimation(
					mFragmentActivity, R.anim.rotate_scale_360_time);
			TextView textView = mViewList.get(i);
			// 开启动画
			textView.startAnimation(animation);
		}
	}
	
	private boolean isFirstSendRunnable = true;
	/**
	 * 大乐透倒计时
	 * 
	 * @Description:
	 * @param periodInfo
	 * @author:www.wenchuang.com
	 * @date:2016年3月9日
	 */
	private void countDownDlt(LoBoPeriodInfo periodInfo) {
		long startTime = periodInfo.getStartTime();
		long nowTime = System.currentTimeMillis();
		long endTime = periodInfo.getEndTime();
		
		if (isFirstSendRunnable) {
			isFirstSendRunnable = false;
		}else {
			mHandler.removeCallbacks(mDltRunnable);
		}
		
		if (nowTime > startTime) {
			mDltDifferTime = endTime - nowTime;
		} else {
			mDltDifferTime = endTime - startTime;
		}
		// mDltDifferTime = endTime - startTime;
		// 测试服务器添加后的字段TimeStep
		// mDltDifferTime = Long.parseLong(periodInfo.getTimeStep());
		if (mDltDifferTime > 1000) {
			mHandler.postDelayed(mDltRunnable, 1000);
		} else {
			textView_hall_dlt_period.setText("销售截期中");
		}
	}
	
	// 2016-9-19 修改记录 将removeCallBack放到onStop()中进行
		@Override
		public void onStop() {
			super.onStop();
			Controller.getInstance().removeCallback(mBack);
//			mFMatch = null;
//			periodInfo = null;
			mHandler.removeCallbacks(mDltRunnable);
			mHandler.removeCallbacks(mJiaoDianRunnable);
		}
		
		@Override
		public void onHiddenChanged(boolean hidden) {
			super.onHiddenChanged(hidden);
			if (hidden) {
				Controller.getInstance().removeCallback(mBack);
				mHandler.removeCallbacks(mDltRunnable);
				mHandler.removeCallbacks(mJiaoDianRunnable);
			}
		}
		
}
