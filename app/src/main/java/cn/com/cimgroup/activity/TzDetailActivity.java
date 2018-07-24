package cn.com.cimgroup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.TzListActivity.MCenterRecordTz;
import cn.com.cimgroup.animation.StretchAnimation;
import cn.com.cimgroup.bean.AllBetting;
import cn.com.cimgroup.bean.FootballDetail;
import cn.com.cimgroup.bean.FootballDetailList;
import cn.com.cimgroup.bean.PlanContent;
import cn.com.cimgroup.bean.TzDetail;
import cn.com.cimgroup.bean.ZhuiContent;
import cn.com.cimgroup.bean.ZhuiDetail;
import cn.com.cimgroup.bean.ZhuiDetailList;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.PlayInfo;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 投注记录详情
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
@SuppressLint({ "ClickableViewAccessibility", "InflateParams", "HandlerLeak" })
public class TzDetailActivity extends BaseActivity implements OnClickListener {
	/** 跳转到各类彩票的投注 */
	private TextView id_detail_betting_jump;

	public TextView mDetailStatus;

	public List<AllBetting> mSaveAllBettings;

	public List<String> mSaveCodes;
	/** 投注号码 */
	private TextView textView_tzdetail_num;

	private LinearLayout textView_tzdetail_items;
	/** 追号信息数据显示 */
	private LinearLayout textView_zhuidetail_items;

	private LinearLayout zhuiLayout;
	/** 开奖号码布局 */
	private LinearLayout id_detail_lotterynum_layout;
	/** 竞彩足球布局 */
	private LinearLayout id_detail_football_layout;
	private static final String MCENTERRECORDTZ = "mcenterrecordtz";

	// 投注记录类型
	private MCenterRecordTz mCenterRecordTz;

	private Intent intent;
	/** 订单类型 */
	private TextView id_detail_orderid;
	/** 投注彩种类型名称 */
	private TextView id_detail_type;
	/** 投注时间 */
	private TextView id_detail_tz_time;
	/**玩法名称*/
	private TextView id_game_name;
	/**头部第一行 彩种+期次（追期）*/
	private TextView id_tz_title;
	
//	/** 订单期次 */
//	private TextView id_detail_qici;
	/** 追号状体 */
	private TextView id_detail_state;
	/** 开奖号码期次 */
	private TextView id_detail_issue_num;

	private TextView winTextView;
	/** 投注金额 */
	private TextView id_detail_tz_money;

	private LinearLayout id_detail_winmoney_layout;

	private TextView id_detail_winmoney;

	/** 投注号码 */
	private TextView footballPlayText;

	/** 包含scrollview 的LinearLayout */
	private LinearLayout id_layout_with_scrollview;
	/** 滚动视图 */
	private ScrollView id_tzdetail_scrollview;

	/** 下拉刷新 头部局 */
	private LinearLayout scrollview_refresh_header_layout;
	/** 提示文字 */
	private TextView id_header_toast_title;
	/** 头高度 */
	private int headerHeight;
	/** 最后一次调用Move Header的Padding */
	private int lastHeaderPadding;

	private boolean isBack; // 从Release 转到 pull
	private int headerState = DONE;// 头部状态，默认完成
	private static final int RELEASE_To_REFRESH = 0;
	private static final int PULL_To_REFRESH = 1;
	private static final int REFRESHING = 2;
	private static final int DONE = 3;
	/** 彩种名称 */
	private String mGameName="";

	/** 投注信息 */
	private AllBetting allBetting;

	/** 订单金额 **/
	private String orderMoney;

	private TextView detailDan;
	
	/**灰色more竖线*/
	private  String mGrayVLine;
	
	private boolean isJPush;
	/**投注状态--文字*/
	private TextView id_tz_state;
	/**投注状态--图片*/
	private ImageView id_image_state;
	/**投注类型玩法（非足球篮球竞猜）*/
	private LinearLayout id_order_detail_type_layout;
	/**方案内容布局*/
	private LinearLayout id_details_layout;
	/**展开收起文字*/
	private TextView id_detail_content_state;
	/**展开收起图标*/
	private ImageView id_detail_content_state_img;
	
	/**红色*/
	private int mColorRed = 0;
	/**灰色线*/
	private int mColorGrayLine = 0;
	/**浅灰色*/
	private int mColorGrayI = 0;
	
	/**追期中，当前期*/
	private Integer mFinishTimes = 0;
	/**未中奖提示文字*/
	private final static String  UNWIN_DEFAULT_TEXT = "木有中奖，继续努力哦~";
	
	private final static String UNLOTTERY_DEFAULT_TEXT = "还未开奖哦~";
	/**未中奖状态图片*/
	private int mUnWinImage = 0;
	/**未开奖状态图片*/
	private int mUnLotteryImage = 0;
	/**中奖状态图片*/
	private int mWinImage = 0;
	/**拨打客服电话*/
	private TextView textView_title_service;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mcenter_record_tz_detail);
		//初始化颜色
		mColorRed = getResources().getColor(R.color.color_red);
		mColorGrayLine = getResources().getColor(R.color.color_grey_bg);
		mColorGrayI = getResources().getColor(R.color.color_gray_indicator);
		//初始化图片
		mUnWinImage = R.drawable.e_ui_detail_fail;
		mUnLotteryImage = R.drawable.e_ui_detail_wei;
		mWinImage = R.drawable.e_ui_detail_win;
		intent = getIntent();
		mCenterRecordTz = (MCenterRecordTz) intent
				.getSerializableExtra(MCENTERRECORDTZ);
		allBetting = (AllBetting) intent.getSerializableExtra("data");
		if (allBetting != null) {
			initViews();
			initHeaderLayout(LayoutInflater.from(mActivity));
			setScrollViewHeaderEvent();
			
			initDatas();
			initEvent();
			mGrayVLine="<font color='"+mColorGrayLine+"'> | </font>";
		} else {
			isJPush = true;
			showLoadingDialog();
			mCenterRecordTz = MCenterRecordTz.ALL;
			String orderId = intent.getStringExtra("orderId");
			String userId = intent.getStringExtra("userId");
			String gameNo = intent.getStringExtra("gameNo");
			if (!StringUtil.isEmpty(orderId) && !StringUtil.isEmpty(userId)) {
				if (intent.getBooleanExtra("isZhui", false)) {
					Controller.getInstance().getLoBoTzZhuiDetail(
							orderId,
							GlobalConstants.NUM_ZHUIDETAIL,
							userId, "1", mBack);
				} else {
					if (gameNo.equals(GlobalConstants.TC_JCZQ)
							|| gameNo.equals(GlobalConstants.TC_JCLQ)) {
						Controller.getInstance()
								.getFootballDetail(orderId,
										GlobalConstants.NUM_FOOTBALLDETAIL,
										userId,
										gameNo, mBack);
					} else {
						Controller.getInstance().getLoBoTzDetail(orderId, GlobalConstants.NUM_TZDGDETAIL, userId, mBack);
					}
				}
			}
		}
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		// textView_tzdetail_state.setOnClickListener(this);
		findViewById(R.id.id_show_or_hide_content).setOnClickListener(this);
		textView_title_service.setOnClickListener(this);

	}

	
	private void initDatas() {
		String tag = intent.getStringExtra("tag");
		if (!StringUtil.isEmpty(tag)) {
			id_detail_orderid.setText(Html.fromHtml("方案编号：<font color = '"+mColorGrayI+"'>"+tag+"</font>"));
			Controller.getInstance().getLoBoTzDetail(tag,
					GlobalConstants.NUM_TZDGDETAIL, App.userInfo.getUserId(),
					mBack);
		} else if (allBetting != null) {
			id_game_name.setText(mGameName);
			String titleLeft = "投注金额";
			if (allBetting.getOrderType().equals("3")) {
				titleLeft = "总投注金额";
			}
			switch (mCenterRecordTz) {
			case ALL:
				//当前期
				String finishTimes = "";
				//总期数
				String allTimes = "";
				//由全部跳转进入 判断当前是否是追号
				id_tz_title.setText(allBetting.getIssueDes());
				if (allBetting.getStatusDes().equals("追号中")) {
					//说明是追号投注 追号中
					String issude = allBetting.getIssueDes();
					finishTimes = issude.substring(issude.indexOf("已追")+2, issude.indexOf("/"));
					allTimes = issude.substring( issude.indexOf("/")+1,issude.length()-2);
					allBetting.setFinishTimes(finishTimes);
					allBetting.setChaseAllTimes(allTimes);
					mFinishTimes = Integer.parseInt(finishTimes);
					
				}
				if (allBetting.getStatusDes().equals("追停结束")) {
					//说明是追号投注 追停结束 
					//并没有返回追号（当/总）期信息
				}
				
				id_detail_state.setText(Html.fromHtml("订单状态：<font color = '"+mColorGrayI+"'>"+allBetting.getStatusDes()+"</font>"));
				String betMoney = allBetting.getBetMoney().indexOf("投注")==-1?allBetting.getBetMoney():allBetting.getBetMoney().substring(2, allBetting.getBetMoney().length());
				
				id_detail_tz_money.setText(String.format("%s：%s", titleLeft,betMoney));
				if (!allBetting.getOrderType().equals("3")) {
					switch (allBetting.getIsAward()) {
					// 是否中奖标志位 0-未开奖 1-未中奖 2-中奖
					case "2":
						id_tz_state.setText(String.format("恭喜 您中了：%s",
								allBetting.getAwardDes().replace("中奖", "")));
						setStateImage(2);
						break;
					case "0":
						id_tz_state.setText(UNLOTTERY_DEFAULT_TEXT);
						setStateImage(0);
						break;
					default:
						id_tz_state.setText(UNWIN_DEFAULT_TEXT);
						setStateImage(1);
						break;
					}
				}
				
				break;
			case BUY:
				id_tz_title.setText(String.format("第%s期", allBetting.getIssue()));
				id_detail_state.setText(Html.fromHtml("订单状态：<font color = '"+mColorGrayI+"'>"+allBetting.getStatusDes()+"</font>"));
				String lemi = allBetting.getMoney().indexOf("米")==-1?allBetting.getMoney():allBetting.getMoney().substring(0,allBetting.getMoney().length()-1);
				id_detail_tz_money.setText(String.format("%s：%s米", titleLeft,lemi));
				if (Double.parseDouble(allBetting.getWinMoney()) > 0) {
					id_tz_state.setText(String.format("恭喜 您中了：%s米", allBetting.getWinMoney()));
					setStateImage(2);
				} else if(allBetting.getStatusDes().equals("未中奖")){
					setStateImage(1);
					id_tz_state.setText(UNWIN_DEFAULT_TEXT);
				}else if (allBetting.getStatusDes().equals("等待开奖")) {
					setStateImage(0);
					id_tz_state.setText(UNLOTTERY_DEFAULT_TEXT);
				}
				break;
			case ZHUI:
				zhuiLayout.setVisibility(View.VISIBLE);
				id_tz_title.setText(String.format("追号( 已追%s/%s期 )", allBetting.getFinishTimes(),allBetting.getChaseAllTimes()));
				mFinishTimes = Integer.parseInt(allBetting.getFinishTimes());
				String state = "";
				switch (allBetting.getChaseStatus()) {
				case "1":
					state = "追号中";
					break;
				case "2":
					state = "追停结束";
					break;
				default:
					break;
				}
				id_detail_state.setText(Html.fromHtml("订单状态：<font color = '"+mColorGrayI+"'>"+state+"</font>"));
				id_detail_tz_money.setText(String.format("%s：%s", titleLeft,allBetting.getChaseAllMoney()));
				if (!StringUtil.isEmpty(allBetting.getWinMoney())) {
					if (Double.parseDouble(allBetting.getWinMoney()) > 0) {
						id_tz_state.setText(String.format("恭喜您中了：%s米", allBetting.getWinMoney()));
						setStateImage(2);
					} 
					//如果状态时没中 加载时无法判断出  所以会先显示等待开奖状态，请求返回时才能判断 是未开奖还是未中奖  如果是未中奖，图片会先显示等待状态，所以 优先不显示状态，等待请求返回
//					else{
//						id_tz_state.setText(UNWIN_DEFAULT_TEXT);
//						setStateImage(0);
//					}
				}
				break;
			case WINNING:
				id_tz_title.setText(String.format(allBetting.getIssueDes()));
				id_detail_state.setText(Html.fromHtml("订单状态：<font color = '"+mColorGrayI+"'>"+allBetting.getStatusDes()+"</font>"));
				id_detail_tz_money.setText(String.format("%s：%s", titleLeft,allBetting.getBetMoney()));
				switch (allBetting.getIsAward()) {
				case "2":
					id_tz_state.setText(String.format("恭喜 您中了：%s", allBetting.getAwardDes().replace("中奖", "")));
					setStateImage(2);
					break;
				case "0":
					id_tz_state.setText(UNLOTTERY_DEFAULT_TEXT);
					setStateImage(0);
				default:
					id_tz_state.setText(UNWIN_DEFAULT_TEXT);
					setStateImage(1);
					break;
				}
				break;
			default:
				break;
			}
			if (!isJPush) {
				showLoadingDialog();
				if (allBetting.getGameNo().equals(GlobalConstants.TC_JCZQ)
						|| allBetting.getGameNo().equals(GlobalConstants.TC_JCLQ)) {
					id_detail_football_layout.setVisibility(View.VISIBLE);
					Controller.getInstance()
							.getFootballDetail(allBetting.getOrderId(),
									GlobalConstants.NUM_FOOTBALLDETAIL,
									App.userInfo.getUserId(),
									allBetting.getGameNo(), mBack);
				} else {
					id_detail_football_layout.setVisibility(View.GONE);
					switch (mCenterRecordTz) {
					case ZHUI:
						zhuiLayout.setVisibility(View.VISIBLE);
						Controller.getInstance().getLoBoTzZhuiDetail(
								allBetting.getChaseId(),
								GlobalConstants.NUM_ZHUIDETAIL,
								App.userInfo.getUserId(), "1", mBack);
						break;
					default:
						switch (allBetting.getOrderType()) {
						case "3":
							zhuiLayout.setVisibility(View.VISIBLE);
							Controller.getInstance().getLoBoTzZhuiDetail(
									allBetting.getOrderId(),
									GlobalConstants.NUM_ZHUIDETAIL,
									App.userInfo.getUserId(), "1", mBack);
							break;
						default:
							Controller.getInstance().getLoBoTzDetail(
									allBetting.getOrderId(),
									GlobalConstants.NUM_TZDGDETAIL,
									App.userInfo.getUserId(), mBack);
							break;
						}
						break;
					}

				}
			}
			isJPush = false;
		}

	}
	/**
	 * 设置状态图片
	 * @param i,0未开奖，1未中奖，2中奖
	 */
	private void setStateImage(int i) {
		switch (i) {
		case 0:
			id_image_state.setImageResource(mUnLotteryImage);
			break;
		case 1:
			id_image_state.setImageResource(mUnWinImage);
			break;
		case 2:
			id_image_state.setImageResource(mWinImage);
			break;

		default:
			break;
		}
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mBack);
	}

	/**
	 * 初始化视图组件
	 */
	private void initViews() {
		// 实例化 滚动视图以及 滚动视图内的直接Layout布局
		id_game_name = (TextView) findViewById(R.id.id_game_name);
		//方案内容布局
		id_details_layout = (LinearLayout) findViewById(R.id.id_details_layout);
		id_detail_content_state = (TextView) findViewById(R.id.id_detail_content_state);
		id_detail_content_state_img = (ImageView) findViewById(R.id.id_detail_content_state_img);
		
		
		id_order_detail_type_layout = (LinearLayout) findViewById(R.id.id_order_detail_type_layout);
		id_tz_state = (TextView) findViewById(R.id.id_tz_state);
		id_image_state = (ImageView) findViewById(R.id.id_image_state);
		
		id_tzdetail_scrollview = (ScrollView) findViewById(R.id.id_tzdetail_scrollview);

		id_detail_betting_jump = (TextView) findViewById(R.id.id_detail_betting_jump);
		id_detail_betting_jump.setOnClickListener(this);

		TextView back = (TextView) findViewById(R.id.textView_title_back);
		back.setOnClickListener(this);

		TextView word = (TextView) findViewById(R.id.textView_title_word);
		word.setText(getResources().getString(R.string.tz_detail));
		
		id_tz_title = (TextView) findViewById(R.id.id_tz_title);
		

		id_detail_orderid = (TextView) findViewById(R.id.id_detail_orderid);
		

		id_detail_type = (TextView) findViewById(R.id.id_detail_type);
		

		id_detail_state = (TextView) findViewById(R.id.id_detail_state);

		id_detail_issue_num = (TextView) findViewById(R.id.id_detail_issue_num);

		id_detail_tz_money = (TextView) findViewById(R.id.id_detail_tz_money);

		id_detail_tz_time = (TextView) findViewById(R.id.id_detail_tz_time);
		
		if (allBetting != null) {
			if (allBetting.getCreateTime() != null) {
				id_detail_tz_time.setText(Html.fromHtml("认购时间：<font color = '"+mColorGrayI+"'>"+DateUtil.getTimeInMillisToStr(allBetting
						.getCreateTime())+"</font>"));
			}
			// TODO
			mGameName = allBetting.getGameName();
//			if (mGameName.equals("竞彩足球")||mGameName.equals("竞彩篮球")) {
//				id_order_detail_type_layout.setVisibility(View.GONE);
//			}
//			id_detail_type.setText(allBetting.getGameName());
			// id_detail_betting_jump.setVisibility(allBetting.getGameName().equals(
			// "竞彩篮球") ? View.GONE : View.VISIBLE);
			id_detail_betting_jump.setText(allBetting.getStatusDes() != null
					&& allBetting.getStatusDes().equals("待支付") ? "去支付" : allBetting
					.getGameName() + "投注");
			
			
			id_detail_orderid.setText(Html.fromHtml("方案编号：<font color = '"+mColorGrayI+"'>"+allBetting.getOrderId()+"</font>"));
		}
		
		id_detail_winmoney_layout = (LinearLayout) findViewById(R.id.id_detail_winmoney_layout);

		id_detail_winmoney = (TextView) findViewById(R.id.id_detail_winmoney);

		winTextView = (TextView) findViewById(R.id.textView_tzdetail_win);

		zhuiLayout = (LinearLayout) findViewById(R.id.layout_zhuidetail);
		zhuiLayout.setVisibility(View.GONE);

		id_detail_lotterynum_layout = (LinearLayout) findViewById(R.id.id_detail_lotterynum_layout);

		id_detail_football_layout = (LinearLayout) findViewById(R.id.id_detail_football_layout);
		id_detail_football_layout.setVisibility(View.GONE);

		detailDan = (TextView) findViewById(R.id.textView_tzdetail_dan);

		footballPlayText = (TextView) findViewById(R.id.textView_football_play);
		textView_tzdetail_num = (TextView) findViewById(R.id.textView_tzdetail_num);

		textView_tzdetail_items = (LinearLayout) findViewById(R.id.textView_tzdetail_items);

		textView_zhuidetail_items = (LinearLayout) findViewById(R.id.textView_zhuidetail_items);
		textView_title_service = (TextView) findViewById(R.id.textView_title_service);
		textView_title_service.setVisibility(View.VISIBLE);
		
	}

	/**
	 * 初始化下拉刷新头布局
	 * 
	 * @param inflater
	 */
	private void initHeaderLayout(LayoutInflater inflater) {
		id_layout_with_scrollview = (LinearLayout) findViewById(R.id.id_layout_with_scrollview);

		scrollview_refresh_header_layout = (LinearLayout) inflater.inflate(
				R.layout.scrollview_refresh_header_layout_noarrow, null);

		ViewUtils.measureView(scrollview_refresh_header_layout);

		headerHeight = scrollview_refresh_header_layout.getMeasuredHeight();

		lastHeaderPadding = (-1 * headerHeight); // 最后一次调用Move Header的Padding
		scrollview_refresh_header_layout.setPadding(0, lastHeaderPadding, 0, 0);
		scrollview_refresh_header_layout.invalidate();
		id_header_toast_title = (TextView) scrollview_refresh_header_layout
				.findViewById(R.id.id_header_toast_title);

		id_layout_with_scrollview.addView(scrollview_refresh_header_layout, 0);


	}

	/**
	 * 为滚动视图添加下拉刷新事件
	 */
	private void setScrollViewHeaderEvent() {
		id_tzdetail_scrollview.setOnTouchListener(new OnTouchListener() {
			int beginY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 加上下滑阻力与实际滑动距离的差（大概值）
					// beginY = (int) ((int) event.getY() +
					// id_main_fragment_move_scrollview.getScrollY() * 1.5);
					break;
				case MotionEvent.ACTION_MOVE:
					// 由于不知道 什么原因 MotionEvent.Action_DOWN不执行，用此方法来为beginY赋值
					if (beginY == 0) {
						// beginY = (int) ((int) event.getY() +
						// id_main_fragment_move_scrollview.getScrollY() * 1.5);
						beginY = (int) event.getY();
					}
					// sc.getScrollY == 0 scrollview 滑动到头了
					// lastHeaderPadding > (-1*headerHeight) 表示header还没完全隐藏起来时
					// headerState != REFRESHING 当正在刷新时
					int interval = (int) (event.getY() - beginY);
					if ((v.getScrollY() == 0) && headerState != REFRESHING) {
						// 拿到滑动的Y轴距离
						// 是向下滑动而不是向上滑动
						if (interval > 0) {
							interval = interval / 2;// 下滑阻力
							lastHeaderPadding = interval + (-1 * headerHeight);
							scrollview_refresh_header_layout.setPadding(0,
									lastHeaderPadding, 0, 0);
							if (lastHeaderPadding > 0) {
								// txView.setText("我要刷新咯");
								headerState = RELEASE_To_REFRESH;
								// 是否已经更新了UI
								if (!isBack) {
									isBack = true; // 到了Release状态，如果往回滑动到了pull则启动动画
									changeHeaderViewByState();
								}
							} else {
								headerState = PULL_To_REFRESH;
								changeHeaderViewByState();
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
							mHandler.sendEmptyMessage(0);
							break;
						}
					}
					break;
				}
				// 如果Header是完全被隐藏的则让ScrollView正常滑动，让事件继续否则的话就阻断事件
				if ((lastHeaderPadding > (-1 * headerHeight) && headerState != REFRESHING)) {
					return true;
				} else {

					return false;
				}
			}
		});

	}

	/***
	 * 头部状态改变时
	 */
	private void changeHeaderViewByState() {
		switch (headerState) {
		case PULL_To_REFRESH:
			// 是由RELEASE_To_REFRESH状态转变来的
			if (isBack) {
				isBack = false;
//				id_header_arrow.startAnimation(arrowReverseAnimation);
				id_header_toast_title.setText("下拉刷新...");
			}
			id_header_toast_title.setText("下拉刷新...");
			break;
		case RELEASE_To_REFRESH:
			id_header_toast_title.setText("松开刷新...");
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
			lastHeaderPadding = -1 * headerHeight;
			scrollview_refresh_header_layout.setPadding(0, lastHeaderPadding,
					0, 0);
			scrollview_refresh_header_layout.invalidate();
//			id_header_arrow.clearAnimation();
//			id_header_arrow.setVisibility(View.VISIBLE);
			id_header_toast_title.setText("下拉可以刷新...");
			break;
		default:
			break;
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				initDatas();
				break;
//			case 1:
//				id_details_layout.setVisibility(View.VISIBLE);
//				break;
			default:
//				showOrHideContent(0);
				break;
			}
			
		};
	};

	/**
	 * 刷新完成后
	 */
	public void onRefreshComplete() {
		headerState = DONE;
		changeHeaderViewByState();
	}

	/**
	 * 每一条代购的数据及处理
	 */
	public class ItemDetail {
		/** 我的投注号码 */
		public TextView mItemDetailNum;
		public TextView mItemDetailBeiZhu;
		public TextView mItemDetailBei;

		public ItemDetail(View view) {
			mItemDetailNum = (TextView) view
					.findViewById(R.id.textView_tzdetail_item_num);
			mItemDetailBeiZhu = (TextView) view
					.findViewById(R.id.textView_tzdetail_item_beizhu);
			mItemDetailBei = (TextView) view
					.findViewById(R.id.textView_tzdetail_item_tzbei);
		}

	}

	/**
	 * 每一条追号的数据及处理
	 */
	public class ItemZhuiDetail {

		public TextView mItemDetailNum;
		public TextView mItemDetailStatus;
		public TextView mItemDetailMoney;
		public ImageView mItemDetailForward;
		public LinearLayout mItemDetailLayout;

		public ItemZhuiDetail(View view) {
			mItemDetailNum = (TextView) view
					.findViewById(R.id.textView_zhuidetail_item_num);
			mItemDetailStatus = (TextView) view
					.findViewById(R.id.textView_zhuidetail_item_status);
			mItemDetailMoney = (TextView) view
					.findViewById(R.id.textView_zhuidetail_item_money);
			mItemDetailForward = (ImageView) view
					.findViewById(R.id.ImageView_zhuidetail_item_detail);
			mItemDetailLayout = (LinearLayout) view
					.findViewById(R.id.layout_zhuidetail_item);
		}
	}

	/**
	 * 每一条竞彩足球的数据及处理
	 */
	public class ItemFootballDetail {

		public TextView mItemDetailTime;
		public TextView mItemDetailName;
		public TextView mItemDetailHome;
		public TextView mItemDetailScore;
		public TextView mItemDetailGuest;
		public TextView mItemDetailTz;
		public TextView mItemDetailDan;
		public TextView mItemDetailResult;

		public ItemFootballDetail(View view) {
			mItemDetailTime = (TextView) view
					.findViewById(R.id.textView_football_item_time);
			mItemDetailName = (TextView) view
					.findViewById(R.id.textView_football_item_name);
			mItemDetailHome = (TextView) view
					.findViewById(R.id.textView_football_item_home);
			mItemDetailScore = (TextView) view
					.findViewById(R.id.textView_football_item_score);
			mItemDetailGuest = (TextView) view
					.findViewById(R.id.textView_football_item_guest);
			mItemDetailTz = (TextView) view
					.findViewById(R.id.textView_football_item_tz);
			mItemDetailDan = (TextView) view
					.findViewById(R.id.textView_football_item_dan);
			mItemDetailResult = (TextView) view
					.findViewById(R.id.textView_football_item_result);
		}
	}

	// TODO 返回
	private CallBack mBack = new CallBack() {
		// 代购
		public void getLoBoTzDetailSuccess(final TzDetail detail) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					if (detail.getResCode() != null
							&& detail.getResCode().equals("0")) {
						if (allBetting == null) {
							allBetting = new AllBetting();
							allBetting.setMoney(detail.getMoney());
							allBetting.setGameNo(detail.getGameNo());
							allBetting.setGameName(detail.getGameName());
							allBetting.setCreateTime(detail.getCreateTime() + "");
							allBetting.setStatusDes(detail.getStatusDes());
							allBetting.setAwardDes(detail.getWinMoney());
							allBetting.setIssue(detail.getIssue());
							allBetting.setOrderId(detail.getOrderId());
							allBetting.setOrderType("0");
							if (Double.parseDouble(detail.getWinMoney()) > 0) {
								allBetting.setIsAward("2");
							} else {
								allBetting.setIsAward("0");
							}
							
							initViews();
							setScrollViewHeaderEvent();
							initHeaderLayout(LayoutInflater.from(mActivity));
							initDatas();
							initEvent();
							mGrayVLine="<font color='"+mColorGrayLine+"'> | </font>";
						}
						mGameName =detail.getGameName();
						id_tz_title.setText(String.format("第%s期", detail.getIssue()));
						id_detail_issue_num.setText(getResources()
								.getString(R.string.record_tz_detail_qi,
										detail.getIssue()));
						footballPlayText.setText(Html.fromHtml("投注玩法：<font color = '"+mColorGrayI+"'>"+mGameName+",  "+getResources()
								.getString(R.string.tz_detail_tz_bei, detail.getCountNum(), detail.getMultiple())+"</font>"));
						id_detail_state.setText(Html.fromHtml("订单状态：<font color = '"+mColorGrayI+"'>"+allBetting.getStatusDes()+"</font>"));
						
						id_detail_tz_time.setText(Html.fromHtml("认购时间：<font color = '"+mColorGrayI+"'>"+DateUtil
								.getTimeInMillisToStr(detail.getCreateTime()
										+ "")+"</font>"));
						if (!StringUtil.isEmpty(detail.getAwardCode())) {
							id_detail_lotterynum_layout
									.setVisibility(View.VISIBLE);
							StringBuilder sb = new StringBuilder();
							switch (allBetting.getGameNo()) {
							case GlobalConstants.TC_DLT:
								String[] awardArr = detail.getAwardCode()
										.split("\\ ");
								String[] redArr = awardArr[0].split("\\,");
								String[] blueArr = awardArr[1].split("\\,");
								for (String j : redArr) {
									sb.append(
											getResources().getString(
													R.string.tzlist_zhong_text,
													j)).append(" ");
								}
								sb.append(mGrayVLine);
								for (String j : blueArr) {
									sb.append(
											getResources()
													.getString(
															R.string.tzlist_zhong_text1,
															j)).append(" ");
								}
								sb.deleteCharAt(sb.length() - 1);
								textView_tzdetail_num.setText(Html.fromHtml(sb
										.toString()));
								break;
							default:
								String[] zdAwardArr = detail.getAwardCode()
										.split("\\,");
								for (String j : zdAwardArr) {
									sb.append(
											getResources().getString(
													R.string.tzlist_zhong_text,
													j)).append(" ");
								}
								sb.deleteCharAt(sb.length() - 1);
								textView_tzdetail_num.setText(Html.fromHtml(sb.toString()));
								break;
							}
						} else {
							id_detail_lotterynum_layout
									.setVisibility(View.GONE);
							textView_tzdetail_num.setText("");
						}

						List<PlanContent> lists = detail.getList();
						if (lists != null) {
							textView_tzdetail_items.removeAllViews();
							LayoutParams lp = null;
							for (int i = 0; i < lists.size(); i++) {
								View view = View.inflate(mActivity,
										R.layout.item_tz_detail, null);
								PlanContent plan = lists.get(i);
								ItemDetail item = new ItemDetail(view);
								StringBuilder sb = new StringBuilder();

								if (!StringUtil.isEmpty(detail.getAwardCode())) {
									switch (allBetting.getGameNo()) {
									case GlobalConstants.TC_DLT:
										String[] awardArr = detail
												.getAwardCode().split("\\ ");
										String[] contentArr = plan
												.getCodeContent().split("\\|");
										String[] redArr = awardArr[0]
												.split("\\,");
										String[] blueArr = awardArr[1]
												.split("\\,");
										switch (plan.getCodePlay() + "") {
										case PlayInfo.DLT_DTZJ:
										case PlayInfo.DLT_DT:
											String[] contentRedDanArr = contentArr[0]
													.split("\\,");
											String[] contentRed1Arr = contentArr[1]
													.split("\\,");
											String[] contentBlueDanArr = contentArr[2]
													.split("\\,");
											String[] contentBlue1Arr = contentArr[3]
													.split("\\,");

											sb.append("[");
											sb.append(getResources().getString(
													R.string.tzlist_zhong_text,
													"胆"));
											for (String j : contentRedDanArr) {
												boolean aa = true;
												for (String k : redArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											sb.deleteCharAt(sb.length() - 1);
											sb.append("]");

											for (String j : contentRed1Arr) {
												boolean aa = true;
												for (String k : redArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											sb.append(mGrayVLine);
											sb.append("[");
											sb.append(getResources()
													.getString(
															R.string.tzlist_zhong_text1,
															"胆"));

											for (String j : contentBlueDanArr) {
												boolean aa = true;
												for (String k : blueArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											sb.deleteCharAt(sb.length() - 1);
											sb.append("]");

											for (String j : contentBlue1Arr) {
												boolean aa = true;
												for (String k : blueArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											break;
										default:
											String[] contentRedArr = contentArr[0]
													.split("\\,");
											String[] contentBlueArr = contentArr[1]
													.split("\\,");
											for (String j : contentRedArr) {
												boolean aa = true;
												for (String k : redArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											sb.append(mGrayVLine);
											for (String j : contentBlueArr) {
												boolean aa = true;
												for (String k : blueArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											break;
										}
										Spanned text=Html.fromHtml(sb
												.toString());
										item.mItemDetailNum.setText(text);
										break;
									case GlobalConstants.TC_PL3:
										switch (plan.getCodePlay() + "") {
										case PlayInfo.PL3_ZXDS:
										case PlayInfo.PL3_ZXFS:
											//中奖号码
											String[] zdAwardArr = detail
													.getAwardCode()
													.split("\\,");
											//我的投注号码
											String[] zdContentArr = plan
													.getCodeContent().split(
															"\\|");
											for (int j = 0; j < zdContentArr.length; j++) {
												String[] strs = zdContentArr[j]
														.split("\\,");
												for (String k : strs) {
													String[] temps = k
															.split("\\ ");
													for (String l : temps) {
														if (l.equals(zdAwardArr[j])) {
															sb.append(
																	getResources()
																			.getString(
																					R.string.tzlist_zhong_text,
																					l))
																	.append(",");
														} else {
															sb.append(l)
																	.append(",");
														}
													}
													sb.deleteCharAt(sb.length() - 1);
												}
												sb.append(mGrayVLine);
											}
											sb.deleteCharAt(sb.length() - 2);
											break;
										case PlayInfo.PL3_ZUX3DS:
										case PlayInfo.PL3_ZUX3FS:
											String[] zxAwardArr = detail
													.getAwardCode()
													.split("\\,");
											String[] zxContentArr = plan
													.getCodeContent().split(
															"\\,");

											Arrays.sort(zxAwardArr);
											for (int k = 0; k < zxContentArr.length; k++) {
												if (zxContentArr[k]
														.equals(zxAwardArr[k])) {
													sb.append(
															getResources()
																	.getString(
																			R.string.tzlist_zhong_text,
																			zxContentArr[k]))
															.append(" ");
												} else {
													sb.append(zxContentArr[k])
															.append(" ");
												}
											}
											break;
										case PlayInfo.PL3_ZUX6DS:
										case PlayInfo.PL3_ZUX6FS:
											String[] plAwardArr = detail
													.getAwardCode()
													.split("\\,");
											String[] plContentArr = plan
													.getCodeContent().split(
															"\\|");

											Arrays.sort(plAwardArr);
											for (String j : plContentArr) {
												boolean aa = true;
												for (String k : plAwardArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											break;
										case PlayInfo.PL3_ZXHZ:
										case PlayInfo.PL3_ZUXHZ:
											String[] zhAwardArr = detail
													.getAwardCode()
													.split("\\,");
											String zhContentArr = plan
													.getCodeContent();
											int awardAll = 0;
											for (String j : zhAwardArr) {
												awardAll += Integer.parseInt(j);
											}

											if (Integer.parseInt(zhContentArr) == awardAll) {
												sb.append(getResources()
														.getString(
																R.string.tzlist_zhong_text,
																plan.getCodeContent()));
											} else {
												sb.append(plan.getCodeContent());
											}
											break;
										default:
											break;
										}

										item.mItemDetailNum.setText(Html.fromHtml(sb.toString()));
										break;
									case GlobalConstants.TC_QXC:
									case GlobalConstants.TC_PL5:
										String[] qxcAwardArr = detail
												.getAwardCode().split("\\,");
										String[] qxcContentArr = plan
												.getCodeContent().split("\\|");

										for (int j = 0; j < qxcContentArr.length; j++) {
											String[] arr = qxcContentArr[j]
													.split("\\,");
											for (String k : arr) {
												sb.append(" ");
												if (qxcAwardArr[j].equals(k)) {
													sb.append(getResources().getString(R.string.tzlist_zhong_text,k))
															.append(" ");
												} else {
													sb.append(k).append(" ");
												}
											}
											sb.append(mGrayVLine);
										}
										sb.deleteCharAt(sb.length() - 1);
										item.mItemDetailNum.setText(Html.fromHtml(sb
												.toString()));
										break;
									default:
										// 老足彩
										String[] lzcContentArr = plan
												.getCodeContent().split("\\|");
										if (lzcContentArr.length > 1) {
											String[] qianArr = lzcContentArr[0]
													.split("\\,");
											String[] houArr = lzcContentArr[1]
													.split("\\,");
											List<String> list = new ArrayList<String>(
													Arrays.asList(qianArr));

											for (String index : houArr) {
												for (int j = 0; j < list.size(); j++) {
													if ((j + 1) == Integer
															.parseInt(index)) {
														list.remove(j);
														list.add(j, "[胆"
																+ qianArr[j]
																+ "]");
													}
												}
											}
											String temp = list
													.toString()
													.substring(
															1,
															(list.toString()
																	.length() - 1));
											item.mItemDetailNum.setText(temp
													.replace(",", " "));
										} else {
											item.mItemDetailNum.setText(Html.fromHtml(plan
													.getCodeContent().replace(
															",", " ").replace("|", mGrayVLine)));
										}
										break;
									}
								} else {
									switch (allBetting.getGameNo()) {
									case GlobalConstants.TC_DLT:
										String[] contentArr = plan
												.getCodeContent().split("\\|");
										switch (plan.getCodePlay() + "") {
										case PlayInfo.DLT_DTZJ:
										case PlayInfo.DLT_DT:
											String[] contentRedDanArr = contentArr[0]
													.split("\\,");
											String[] contentRed1Arr = contentArr[1]
													.split("\\,");
											String[] contentBlueDanArr = contentArr[2]
													.split("\\,");
											String[] contentBlue1Arr = contentArr[3]
													.split("\\,");

											sb.append("[");
											sb.append(getResources().getString(
													R.string.tzlist_zhong_text,
													"胆"));
											for (String j : contentRedDanArr) {
												sb.append(j).append(" ");
											}
											sb.deleteCharAt(sb.length() - 1);
											sb.append("]");

											for (String j : contentRed1Arr) {
												sb.append(j).append(" ");
											}

											sb.append(mGrayVLine);

											sb.append("[");
											sb.append(getResources()
													.getString(
															R.string.tzlist_zhong_text1,
															"胆"));

											for (String j : contentBlueDanArr) {
												sb.append(j).append(" ");
											}
											sb.deleteCharAt(sb.length() - 1);
											sb.append("]");

											for (String j : contentBlue1Arr) {
												sb.append(j).append(" ");
											}
											break;
										default:
											String[] contentRedArr = contentArr[0]
													.split("\\,");
											String[] contentBlueArr = contentArr[1]
													.split("\\,");

											for (String j : contentRedArr) {
												sb.append(j).append(" ");
											}

											sb.append(mGrayVLine);

											for (String j : contentBlueArr) {
												sb.append(j).append(" ");
											}
											break;
										}

										item.mItemDetailNum.setText(Html.fromHtml(sb
												.toString()));
										break;
									case GlobalConstants.TC_PL3:
										switch (plan.getCodeContent()) {
										case "27":
											item.mItemDetailNum
													.setText("9|9|9");
											break;
										case "26":
											item.mItemDetailNum
													.setText("8|9|9");
											break;
										case "01":
											item.mItemDetailNum
													.setText("0|0|1");
											break;
										case "00":
											item.mItemDetailNum
													.setText("0|0|0");
											break;
										default:
											item.mItemDetailNum.setText(Html.fromHtml(plan
													.getCodeContent().replace(",", "  ").replace("|", sb.append(mGrayVLine))));
											break;
										}
										break;
									case GlobalConstants.TC_SF14:
									case GlobalConstants.TC_SF9:
									case GlobalConstants.TC_JQ4:
									case GlobalConstants.TC_BQ6:
										// 老足彩
										String[] lzcContentArr = plan
												.getCodeContent().split("\\|");
										if (lzcContentArr.length > 1) {
											String[] qianArr = lzcContentArr[0]
													.split("\\,");
											String[] houArr = lzcContentArr[1]
													.split("\\,");
											List<String> list = new ArrayList<String>(
													Arrays.asList(qianArr));

											for (String index : houArr) {
												for (int j = 0; j < list.size(); j++) {
													if ((j + 1) == Integer
															.parseInt(index)) {
														list.remove(j);
														list.add(j, "[胆"
																+ qianArr[j]
																+ "]");
													}
												}
											}
											String temp = list
													.toString()
													.substring(
															1,
															(list.toString()
																	.length() - 1));
											item.mItemDetailNum.setText(temp
													.replace(",", " "));
										} else {
											item.mItemDetailNum.setText(Html.fromHtml(plan
													.getCodeContent().replace(",",mGrayVLine)));
										}
										break;
									default:
										String defStr = plan.getCodeContent()
												.replace(",", " ")
												.replace("|", mGrayVLine);
										item.mItemDetailNum.setText(Html
												.fromHtml(defStr.substring(0, defStr.lastIndexOf("|"))));
										break;
									}
								}

								item.mItemDetailBeiZhu.setText(plan
										.getCodePlayDes()
										+ plan.getCodeNumbers()
										+ getResources().getString(
												R.string.betting1));
								item.mItemDetailBei.setText(plan
										.getCodeMultiple()
										+ getResources().getString(
												R.string.cart_add_bei));
								textView_tzdetail_items.addView(view);
							}
							TextView v = new TextView(TzDetailActivity.this);
							lp = new LayoutParams(LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT);
							v.setGravity(Gravity.CENTER);
							v.setText("位置");
							v.setTextSize(40);
							v.setTextColor(getResources().getColor(R.color.color_gray_secondary));
							v.setVisibility(View.INVISIBLE);
							v.setLayoutParams(lp);
							v.setEnabled(false);
							v.setFocusable(false);
							textView_tzdetail_items.addView(v);
						}
					} else {
						ToastUtil.shortToast(TzDetailActivity.this,
								detail.getResMsg());
					}
					onRefreshComplete();
					
				}
			});

		};

		public void getLoBoTzDetailFailure(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					showToast(getResources().getString(
							R.string.record_tz_detail_failure));
				}
			});
		};

		// TODO 追号
		public void getLoBoTzZhuiDetailSuccess(final ZhuiDetailList detail) {
			runOnUiThread(new Runnable() {
				// 投注成功回调
				public void run() {
					hideLoadingDialog();
					ZhuiDetailList details = detail;
					if (details != null && detail.getResCode().equals("0")) {
						String state = detail.getChaseStatusDes()
								+ "( 追停：" + detail.getStopCondition() + " )";
						id_detail_state.setText(Html.fromHtml("订单状态：<font color = '"+mColorGrayI+"'>"+state+"</font>"));
						mGameName = detail.getGameName();
						allBetting.setStatus(detail.getChaseStatusDes());
						
						id_detail_tz_time.setText(Html.fromHtml("认购时间：<font color = '"+mColorGrayI+"'>"+detail.getBetTime()+"</font>"));
						id_detail_lotterynum_layout.setVisibility(View.GONE);
						allBetting.setMoney(detail.getChaseAllMoney());
						// allBetting.setMoney(detail.get)
						textView_tzdetail_num.setText("");
						List<ZhuiDetail> lists = detail.getIssueList();
						int allWinMoney = Integer.parseInt(TextUtils.isEmpty(allBetting.getWinMoney())?"0":allBetting.getWinMoney());
						
						if (lists != null) {
							textView_zhuidetail_items.removeAllViews();
							LayoutParams lp = null;
							int size = lists.size();
							final String allTimes = size+"";
							if (!TextUtils.isEmpty(allBetting.getStatusDes())&&allBetting.getStatusDes().equals("追停结束")) {
								mFinishTimes = size;
							}
							
							for (int i = 0; i < size; i++) {
								final int  times = i;
								//每一个追号的项的状态
								String status = "";
								View view = View.inflate(mActivity,
										R.layout.item_tz_zhui_detail, null);
								final ZhuiDetail zhuiDetail = lists.get(i);
								ItemZhuiDetail item = new ItemZhuiDetail(view);
								if (i == 0) {
									//倍数
									footballPlayText.setText(Html.fromHtml("投注玩法：<font color = '"+mColorGrayI+"'>"+mGameName+"追号, 共"+detail.getChaseAllTimes()+"期  "+getResources()
											.getString(R.string.tz_detail_tz_bei, details.getCountNum(), zhuiDetail.getMultiBetCnt())+"</font>"));
									id_detail_orderid.setText(Html.fromHtml("方案编号：<font color = '"+mColorGrayI+"'>"+zhuiDetail.getOrderId()+"</font>"));
								}
								
								
								item.mItemDetailNum.setText(getResources()
										.getString(
												R.string.record_tz_detail_qi,
												zhuiDetail.getIssueNo()));
								item.mItemDetailMoney.setText(getResources()
										.getString(
												R.string.tzdetail_zhui_money,
												zhuiDetail.getMoney()));
								int winMoney = Integer.parseInt(zhuiDetail.getTaxedWinMoney());
								status = zhuiDetail
										.getOrderStatusDes();
								if (mFinishTimes == (i + 1)) {
									id_tz_title.setText(String.format(
											"追号第%s期 ( 已追%s/%s期 )", 
											zhuiDetail.getIssueNo(),
											mFinishTimes,
											detail.getChaseAllTimes()));
									if (allWinMoney == 0) {
										if (!TextUtils.isEmpty(status)) {
											status.trim();
										}else {
											status = "";
										}
										String winStatus = zhuiDetail
												.getWinStatusDes();
										if (!TextUtils.isEmpty(winStatus)) {
											winStatus.trim();
										}else {
											winStatus = "";
										}
										if (("出票成功").equals(status)&& ("未开奖").equals(winStatus)) {
											status = "未开奖";
											id_tz_state.setText(UNLOTTERY_DEFAULT_TEXT);
											setStateImage(0);
										} 
										if (winStatus.equals("未中奖")) {
											setStateImage(1);
											id_tz_state.setText(UNWIN_DEFAULT_TEXT);
										}
									}else {
										setStateImage(2);
										id_tz_state.setText(String.format("恭喜 您中了：%s米", allBetting.getWinMoney()));
									}
								}
								// 追号信息栏中中奖状态的TextView
								if (winMoney != 0) {
									// 中奖了 直接填写中奖金额
									Spanned stateStr = Html.fromHtml("<font color = '"+mColorRed+"'>中奖"+zhuiDetail
											.getTaxedWinMoney()+"米</font>");
									item.mItemDetailStatus.setText(stateStr);
								} else {
									// 没有中奖 填写开奖状态
									// 2016-5-12 honglin 1期bug310 修改投注状态显示
									if (!TextUtils.isEmpty(status)) {
										status.trim();
									}else {
										status = "";
									}
									String winStatus = zhuiDetail
											.getWinStatusDes();
									if (!TextUtils.isEmpty(winStatus)) {
										winStatus.trim();
									}else {
										winStatus = "";
									}
									if (("出票成功").equals(status)&& ("未开奖").equals(winStatus)) {
										status = "未开奖";
									} 
									if (winStatus.equals("未中奖")) {
										status = "未中奖";
									}
									if (status.equals("待出票")||status.indexOf("追号失败")!=-1) {
										item.mItemDetailForward
										.setVisibility(View.INVISIBLE);
										item.mItemDetailLayout
										.setEnabled(false);
									}else {
										item.mItemDetailForward
										.setVisibility(View.VISIBLE);
										item.mItemDetailLayout
										.setEnabled(true);
									}
									item.mItemDetailStatus.setText(status);
								}
								if (item.mItemDetailLayout.isEnabled()) {
									view.setTag(zhuiDetail.getOrderId());
									view.setOnClickListener(new OnClickListener() {
										@Override
										public void onClick(View v) {
											// TODO 整理跳转第二层界面数据
											Intent intent = new Intent(
													TzDetailActivity.this,
													Tz2DetailActivity.class);
											if ((String) v.getTag() != null) {
												intent.putExtra("tag",
														(String) v.getTag());
												intent.putExtra("gameNo",
														detail.getGameNo());
												intent.putExtra("gameName",
														allBetting.getGameName());
												//下级页面标题
												String title = String.format("第%s期",zhuiDetail.getIssueNo());
												//投注金额
												String tzMoney = String.format("投注金额：%s米", zhuiDetail.getMoney());
												//当前期次
												String nowTimes = (times+1)+"";
												intent.putExtra("title", title);
												intent.putExtra("tzMoney", tzMoney);
												intent.putExtra("nowTimes", nowTimes);
												intent.putExtra("allTimes", allTimes);
												startActivity(intent);
											}
										}
									});
								}
								
								textView_zhuidetail_items.addView(view);
							}
							TextView v = new TextView(TzDetailActivity.this);
							lp = new LayoutParams(LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT);
							v.setGravity(Gravity.CENTER);
							v.setText("位置");
							v.setTextSize(40);
							v.setVisibility(View.INVISIBLE);
							v.setTextColor(getResources().getColor(R.color.color_gray_secondary));
							v.setLayoutParams(lp);
							v.setEnabled(false);
							v.setFocusable(false);
							textView_zhuidetail_items.addView(v);
						}

						List<ZhuiContent> list = detail.getContentList();
						textView_tzdetail_items.removeAllViews();
						LayoutParams lp = null;
						if (list != null && list.size() != 0) {
							for (int i = 0; i < list.size(); i++) {
								View view = View.inflate(mActivity,
										R.layout.item_tz_detail, null);
								ZhuiContent plan = list.get(i);
								ItemDetail item = new ItemDetail(view);
								StringBuilder sb = new StringBuilder();
								detail.setAwardCode("");
//								if (!StringUtil.isEmpty(detail.getAwardCode())) {
									//开奖显示
									switch (allBetting.getGameNo()) {
									case GlobalConstants.TC_DLT:
										String[] awardArr = detail
												.getAwardCode().split("\\ ");
										String[] contentArr = plan.getContent()
												.split("\\|");
										String[] redArr = awardArr[0]
												.split("\\,");
										String[] blueArr=null;
										if (awardArr.length==1) {
											blueArr=new String[]{""};
										}else
											blueArr=awardArr[1].split("\\,");
										switch (plan.getPlay() + "") {
										case PlayInfo.DLT_DTZJ:
										case PlayInfo.DLT_DT:
											String[] contentRedDanArr = contentArr[0]
													.split("\\,");
											String[] contentRed1Arr = contentArr[1]
													.split("\\,");
											String[] contentBlueDanArr = contentArr[2]
													.split("\\,");
											String[] contentBlue1Arr = contentArr[3]
													.split("\\,");

											sb.append("[");
											sb.append(getResources().getString(
													R.string.tzlist_zhong_text,
													"胆"));
											for (String j : contentRedDanArr) {
												sb.append(j).append(" ");
											}
											sb.deleteCharAt(sb.length() - 1);
											sb.append("]");

											for (String j : contentRed1Arr) {
												sb.append(j).append(" ");
											}

											sb.append(mGrayVLine);

											sb.append("[");
											sb.append(getResources()
													.getString(
															R.string.tzlist_zhong_text1,
															"胆"));

											for (String j : contentBlueDanArr) {
												sb.append(j).append(" ");
											}
											sb.deleteCharAt(sb.length() - 1);
											sb.append("]");

											for (String j : contentBlue1Arr) {
												sb.append(j).append(" ");
											}
											break;
										default:
											String[] contentRedArr = contentArr[0]
													.split("\\,");
											String[] contentBlueArr = contentArr[1]
													.split("\\,");

											for (String j : contentRedArr) {
												boolean aa = true;
												for (String k : redArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}

											sb.append(mGrayVLine);
											for (String j : contentBlueArr) {
												boolean aa = true;
												for (String k : blueArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											break;
										}

										item.mItemDetailNum.setText(Html.fromHtml(sb
												.toString()));
										break;
									case GlobalConstants.TC_PL3:
										switch (plan.getPlay() + "") {
										case PlayInfo.PL3_ZXDS:
										case PlayInfo.PL3_ZXFS:
											String str = plan
													.getContent().replace(",", " ").replace("|", "<font color='"+mColorGrayLine+"'> | </font>");
											Log.e("qiufeng", "str:"+str);
											item.mItemDetailNum.setText(Html.fromHtml(str));
											break;
										case PlayInfo.PL3_ZUX3DS:
										case PlayInfo.PL3_ZUX3FS:
										case PlayInfo.PL3_ZUX6DS:
										case PlayInfo.PL3_ZUX6FS:
											String[] zxContentArr = plan
													.getContent().split("\\,");

//											Arrays.sort(zxAwardArr);
											for (int k = 0; k < zxContentArr.length; k++) {
												sb.append(zxContentArr[k])
														.append(" ");
											}
											item.mItemDetailNum.setText(Html.fromHtml(sb
													.toString()));
											break;
										case PlayInfo.PL3_ZXHZ:
										case PlayInfo.PL3_ZUXHZ:
											sb.append(plan.getContent());
											item.mItemDetailNum.setText(Html.fromHtml(sb
													.toString()));
											break;
										default:
											break;
										}

										
										break;
									case GlobalConstants.TC_QXC:
									case GlobalConstants.TC_PL5:
										String[] qxcContentArr = plan
												.getContent().split("\\|");

										for (int j = 0; j < qxcContentArr.length; j++) {
											String[] arr = qxcContentArr[j]
													.split("\\,");
											for (String k : arr) {
												sb.append(" ");
													sb.append(k).append(" ");
											}
											sb.append("<font color='"+mColorGrayLine+"'> | </font>");
										}
										String dStr = sb.toString();
										item.mItemDetailNum.setText(Html
												.fromHtml(dStr.substring(0, dStr.lastIndexOf("|"))));
										break;
									default:
										String defStr = plan
												.getContent()
												.replace(",", " ")
												.replace("|", "<font color='"+mColorGrayLine+"'> | </font>");
										item.mItemDetailNum.setText(Html.fromHtml(defStr));
										break;
									}
//								} else {
//									//未开奖显示
//									switch (allBetting.getGameNo()) {
//									case GlobalConstants.TC_DLT:
//										item.mItemDetailNum.setText(plan
//												.getContent().replace(" ", " | ")
//												.replace(",", " "));
//										break;
//									default:
//										item.mItemDetailNum
//												.setText(plan.getContent()
//														.replace(",", " "));
//										break;
//									}
//								}
								item.mItemDetailBeiZhu.setText(plan
										.getPlayDes()
										+ plan.getSaleCnt()
										+ getResources().getString(
												R.string.betting1));
								item.mItemDetailBei.setText(plan
										.getMultiBetCnt()
										+ getResources().getString(
												R.string.cart_add_bei));
								textView_tzdetail_items.addView(view);
							}
							TextView v = new TextView(TzDetailActivity.this);
							lp = new LayoutParams(LayoutParams.MATCH_PARENT,
									LayoutParams.WRAP_CONTENT);
							v.setGravity(Gravity.CENTER);
							v.setText("位置");
							v.setTextSize(40);
							v.setVisibility(View.INVISIBLE);
							v.setTextColor(getResources().getColor(R.color.color_gray_secondary));
							v.setLayoutParams(lp);
							v.setEnabled(false);
							v.setFocusable(false);
							textView_zhuidetail_items.addView(v);

						}

					} else {
						ToastUtil.shortToast(TzDetailActivity.this,
								detail.getResMsg());
					}
					onRefreshComplete();
				}
			});
		};

		public void getLoBoTzZhuiDetailFailure(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					showToast(getResources().getString(
							R.string.record_tz_detail_failure));
				}
			});
		};

		// TODO 足彩
		public void getFootballDetailSuccess(final FootballDetailList detail) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					if (Integer.parseInt(detail.getResCode()) == 0) {
						if (allBetting == null) {
							allBetting = new AllBetting();
							allBetting.setMoney(detail.getMoney());
							allBetting.setGameNo(detail.getGameNo());
							allBetting.setGameName(detail.getGameName());
							allBetting.setCreateTime(detail.getCreateTime() + "");
							allBetting.setStatusDes(detail.getStatusDes());
							allBetting.setAwardDes("中奖" + detail.getWinMoney() + "米");
							allBetting.setIssue(detail.getIssue());
							allBetting.setIssueDes("第" + detail.getIssue() + "期");
							allBetting.setOrderId(detail.getOrderId());
							allBetting.setBetMoney("投注" + detail.getMoney() + "米");
							allBetting.setOrderType("0");
							if (Double.parseDouble(detail.getWinMoney()) > 0) {
								allBetting.setIsAward("2");
							} else {
								allBetting.setIsAward("0");
							}
							initViews();
							setScrollViewHeaderEvent();
							initHeaderLayout(LayoutInflater.from(mActivity));
							initDatas();
							initEvent();
							mGrayVLine="<font color='"+mColorGrayLine+"'> | </font>";
						}
						id_detail_football_layout.setVisibility(View.VISIBLE);
						id_detail_lotterynum_layout.setVisibility(View.GONE);
						
						allBetting.setMoney(detail.getMoney());
						List<FootballDetail> lists = detail.getList();
						if (lists != null && lists.size() > 0) {
							textView_tzdetail_items.removeAllViews();
							// TODO
							footballPlayText.setText(Html.fromHtml("投注玩法：<font color = '"+mColorGrayI+"'>"+lists.get(0)
									.getPlayMethod()+detail.getPassType()+",  "+getResources()
									.getString(R.string.tz_detail_tz_bei, detail.getCountNum(), detail.getMultiple())+"</font>"));
							
							if (lists.get(0).getPlayMethod().equals("一场制胜玩法")) {
								detailDan.setText("匹配");
							}
						}
						for (int i = 0; i < lists.size(); i++) {
							View view = View.inflate(mActivity,
									R.layout.item_football_detail, null);
							FootballDetail detail = lists.get(i);
							ItemFootballDetail item = new ItemFootballDetail(
									view);
							String time = detail.getMatchCode().substring(0,2)+"\n"+detail.getMatchCode().substring(2, detail.getMatchCode().length());
							item.mItemDetailTime.setText(time);
							item.mItemDetailName.setText(detail.getLevelName());
							item.mItemDetailHome.setText(detail.getHomeTeam());
							String[] results = null;
							if (detail.getFullScore() != null) {
								item.mItemDetailScore.setText(detail
										.getFullScore());
							} else {
								if (detail.getHaifScore() != null) {
									item.mItemDetailScore.setText(detail
											.getHaifScore());
								} else {
									item.mItemDetailScore.setText("VS");
								}
							}

							item.mItemDetailGuest
									.setText(detail.getGuestTeam());
							String betItem = detail.getBetItem();
							String str = "";
							String s = betItem.replaceAll("\\|", "\\,");// 首先将分隔符修改为一致
							if (!TextUtils.isEmpty(detail.getResult())) {// 如果已经开奖
								results = detail.getResult().split("\\,");
								StringBuilder sb1 = new StringBuilder();
								String[] resultStr = s.split("\\,");
								int size = resultStr.length;
								StringBuilder sb = new StringBuilder("");
								for (int k = 0; k < results.length; k++) {
									for (int j = 0; j < size; j++) {
										if (results[k].equals(resultStr[j]
												.split("\\(")[0])) {
											resultStr[j] = getResources()
													.getString(
															R.string.tzlist_zhong_text,
															resultStr[j]);
											break;
										}
									}
									// 组装结果
									sb1.append(results[k]);
									if (k < (results.length - 1)) {
										sb1.append(getResources().getString(
												R.string.tzdetail_football_br));
									}
								}
								for (int j = 0; j < resultStr.length; j++) {
									sb.append(j == (resultStr.length - 1) ? resultStr[j]
											: resultStr[j] + "<br>");
								}
								item.mItemDetailResult.setText(Html
										.fromHtml(sb1.toString()));
								str = sb.toString();
							} else {
								item.mItemDetailResult
										.setText(getResources()
												.getString(
														R.string.tzdetail_football_wait));
								str = betItem
										.replaceAll(
												"\\|",
												getResources()
														.getString(
																R.string.tzdetail_football_br))//
										.replaceAll(
												"\\,",
												getResources()
														.getString(
																R.string.tzdetail_football_br));
							}
							item.mItemDetailTz.setText(Html.fromHtml(str));
							if (lists.get(0).getPlayMethod().equals("一场制胜玩法")) {
								if (detail.getIsDT().equals("0")) {
									item.mItemDetailDan.setText("自选");
								} else {
									item.mItemDetailDan.setText("匹配");
								}
							} else {
								if (detail.getIsDT().equals("0")) {
									item.mItemDetailDan.setText("X");
								} else {
									item.mItemDetailDan.setText("√");
								}
							}

							textView_tzdetail_items.addView(view);
						}
					} else {
						ToastUtil.shortToast(TzDetailActivity.this,
								detail.getResMsg());
					}
					onRefreshComplete();
				}
			});
		};

		public void getFootballDetailFailure(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					showToast(getResources().getString(
							R.string.record_tz_detail_failure));
				}
			});
		};
	};
	/**显示true还是隐藏false方案内容*/
	private boolean isShowContent = true;
	/**显示或隐藏动画*/
	private StretchAnimation mBSAnimation;
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_service:
			//拨打客服电话
			callService();
			break;
		case R.id.id_show_or_hide_content:
			showOrHideContent(300);
			//显示或收起方案内容
			break;
		case R.id.textView_title_back:
			finish();
			break;
		case R.id.id_detail_betting_jump:
			// 跳转到各自的投注页面
			if (id_detail_betting_jump.getText().toString().trim()
					.equals("去支付")) {
				Intent intent = new Intent(TzDetailActivity.this,
						CommitPayActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("data", allBetting);
				intent.putExtras(bundle);
				startActivity(intent);
			} else {
				if (allBetting.getGameName().equals("竞彩足球")) {
					startActivity(LotteryFootballActivity.class);
				} else if (allBetting.getGameName().equals("大乐透")) {
					startActivity(LotteryDLTActivity.class);
				} else if (allBetting.getGameName().equals("排列三")) {
					startActivity(LotteryPL3Activity.class);
				} else if (allBetting.getGameName().equals("七星彩")) {
					startActivity(LotteryQxcActivity.class);
				} else if (allBetting.getGameName().equals("胜负彩")
						|| allBetting.getGameName().equals("任选9场")) {
					Intent intent = new Intent(TzDetailActivity.this,
							LotteryOldFootballActivity.class);
					intent.putExtra("lotoId", 0);
					startActivity(intent);
				} else if (allBetting.getGameName().equals("排列五")) {
					startActivity(LotteryPL5Activity.class);
				} else if (allBetting.getGameName().equals("6场半全场")) {
					Intent intent = new Intent(TzDetailActivity.this,
							LotteryOldFootballActivity.class);
					intent.putExtra("lotoId", 2);
					startActivity(intent);
				} else if (allBetting.getGameName().equals("4场进球")) {
					Intent intent = new Intent(TzDetailActivity.this,
							LotteryOldFootballActivity.class);
					intent.putExtra("lotoId", 1);
					startActivity(intent);
				} else if (allBetting.getGameName().equals("竞彩篮球")) {
					startActivity(LotteryBasketballActivity.class);
				}
			}
		default:
			break;
		}
	}
	/**拨打客服电话*/
	private void callService() {
		 Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:010-65617701"));  
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		    startActivity(intent);  
	}

	/**显示或隐藏方案内容布局*/
	private void showOrHideContent(int duration) {
		if (mBSAnimation == null) {
			mBSAnimation = new StretchAnimation(id_details_layout,
					StretchAnimation.TYPE.vertical, duration);
			mBSAnimation.setInterpolator(new AccelerateInterpolator());
		}
		if (isShowContent) {
			id_detail_content_state.setText("展开");
			id_detail_content_state_img.setRotation(0);
		}else {
			id_detail_content_state.setText("收起");
			id_detail_content_state_img.setRotation(180);
		}
		isShowContent = !isShowContent;
		mBSAnimation.setDuration(duration);
		mBSAnimation.startAnimation(id_details_layout);
	}

}
