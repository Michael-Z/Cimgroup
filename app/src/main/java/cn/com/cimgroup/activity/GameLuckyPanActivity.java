package cn.com.cimgroup.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import u.aly.br;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.bean.ShareAddLeMiObj;
import cn.com.cimgroup.bean.ShareObj;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.dailog.LuckyPanResultDialog;
import cn.com.cimgroup.dailog.LuckyPanResultDialog.onTryAgainPressedListener;
import cn.com.cimgroup.frament.GridFragment;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare.PlayMenuItemClick;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.thirdSDK.ShareUtils;
import cn.com.cimgroup.view.LuckyPanRelativeLayout;
import cn.com.cimgroup.view.LuckyPanRelativeLayout.AnimationFinished;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.SoundUtils;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class GameLuckyPanActivity extends BaseActivity implements
		OnClickListener, PlayMenuItemClick, AnimationFinished {

	/** handler的标志位 转盘停止 **/
	protected static final int FINISHED = 0;
	/** handler的标志位 更新乐米显示 **/
	protected static final int UPDATE_LEMI = 1;
	/** 开始按钮 **/
	private ImageView iv_start;
	/** 自定义的转盘控件 **/
	private LuckyPanRelativeLayout mRelativeLayout;

	/** 竞彩按钮-大 **/
	private ImageView iv_button_big;
	/** 竞彩按钮-小 **/
	private ImageView iv_button_small;

	/** 投注金额-显示 **/
	private TextView tv_bet_money;
	/** 投注金额-编辑 **/
	// private EditText ed_bet_money;

	/** 投注金额-50米 **/
	private ImageView iv_fifty;
	/** 投注金额-200米 **/
	private ImageView iv_two_hundred;
	/** 投注金额-500米 **/
	private ImageView iv_five_hundred;
	/** 按钮-音效开关 **/
	private ImageView iv_sound;

	/** 按钮-标题栏-返回 **/
	private RelativeLayout id_back;
	/** 按钮-标题栏-分享 **/
	private RelativeLayout id_share;
	/** 标志位-大小按钮是否已选择 **/
	private boolean flagForBigOrSmall = false;
	/** 投注金额 **/
	private String betCount;
	/** 用户的投注选项 1【大】，2【小】 **/
	private String content;
	/** 转盘停止位置，有对应规则，在getResponseInfo（）方法里 **/
	private int stopAt;
	/** 控件-右上角的用户金额显示 **/
	private TextView tv_money_show;
	/** 参数-右上角用户金额 **/
	private String lemiTextContent = "0";
	/** 提示框-游戏结果提示框 **/
	private LuckyPanResultDialog mLuckyPanResultDialog;
	/** 用户输赢的标志 1-赢 0-输 **/
	private int winOrLose = 9999;
	/** 控件-自定义的投注金额 **/
	private EditText et_bet_count;
	/** 弹出软键盘的管理器 **/
	private InputMethodManager imm;
	/** 参数-用户该次盈利的乐米 **/
	private int winAmount;
	/** 音乐播放器api **/
	private MediaPlayer mPlayer;
	/** 标志位-声音开关状态 true-打开 false-关闭 **/
	private boolean soundOn = true;
	/** 浮动控件-标题栏-分享下拉 **/
	private PopupWndSwitchShare mPopMenu;
	/** 分享类型 微信分享到_0：好友;_1：朋友圈 _2:QQ空间 3_:QQ好友 **/
	private int mType;
	/** 标志位-转盘正在转动 **/
	private boolean isRolling = false;
	/** 分享工具类 **/
	private ShareUtils shareUtils = new ShareUtils();
	/** 点击购买乐米 **/
	private RelativeLayout tv_buy_lemi;
	/** 控件-右下角的 规则说明 **/
	private TextView tv_rule;
	/** 存储用户是否需要读取引导图 **/
	private SharedPreferences sp;
	/** 是否需要展示引导图 **/
	private boolean isGuide = true;
	/** 投注金币+自定义的整体布局 **/
	private LinearLayout ll_betAmount;
	/** 引导图的Bitmap集和 用来计算图片的宽高 **/
	private Bitmap[] mBitmaps;
	/** 引导图的Image集和 用来setImageResorce 0-大小 1-投注金额 2-开始按钮 **/
	private int[] mImages;
	/** 引导图的背景 **/
	private RelativeLayout rl_guide;
	/** 顶部整体布局 （转盘+音效键+乐米展示） 用来计算高度 以便设置引导图 **/
	private RelativeLayout rl_lemi_sound_pan;
	/** 整体布局 用来完成点击软键盘外隐藏软键盘 **/
	private LinearLayout ll_content_above;
	/** 消息传递用的handler **/
	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FINISHED:
				// Toast.makeText(LuckyPanActivity.this, "恭喜你中奖", 0).show();
				mLuckyPanResultDialog.showDialog(winOrLose, winAmount);
				mLuckyPanResultDialog.setCanceledOnTouchOutside(false);
				// 屏蔽物理返回键
				mLuckyPanResultDialog.setCancelable(false);
				// 参数：1-赢 0-输
				if (winOrLose == 1 && soundOn) {
					// startMP3("win.mp3");
					SoundUtils.startSoundFromResource(mActivity, R.raw.win);
				} else if (winOrLose == 0 && soundOn) {
					// startMP3("lose.mp3");
					SoundUtils.startSoundFromResource(mActivity, R.raw.lose);
				}
				initLemi();
				// 初始化按钮的状态和点击等
				initStatus();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_luckypan_main);

		initSP();
		initView();
		initEvent();
		initData();
		addGuideView();

		// 设置自定义控件 也就是surfaceView的背景透明
		// mRelativeLayout.setZOrderOnTop(true);
		// mRelativeLayout.getHolder().setFormat(PixelFormat.TRANSLUCENT);
		// iv_start.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {}
		// });

	}

	// @Override
	// public void onWindowFocusChanged(boolean hasFocus) {
	// super.onWindowFocusChanged(hasFocus);
	// if (hasFocus && iv_fill_bottom != null) {
	// // addGuideView(iv_fill_bottom.getMeasuredHeight());
	// iv_height = iv_fill_bottom.getMeasuredHeight();
	// addGuideView();
	//
	// }
	// }

	/** 添加引导界面 **/
	private void addGuideView() {

		getScreenMetric();

		ViewTreeObserver observer = rl_lemi_sound_pan.getViewTreeObserver();
		observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				rl_lemi_sound_pan.getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
				int height = rl_lemi_sound_pan.getMeasuredHeight();
				// android.view.ViewGroup.LayoutParams lpp =
				// rl_lemi_sound_pan.getLayoutParams();
				// rl_lemi_sound_pan.getBottom();
				int width = rl_lemi_sound_pan.getMeasuredWidth();
				// 添加引导图 如果引导背景不为空 才加载引导图
				// 55dp = "规则"文字的Height　+ marginBottom
				if (rl_guide != null) {
					//添加引导图片 引导图是绘制上去的
					addGuideView(height);
				}
			}
		});
	}

	/** 获取屏幕的宽高 **/
	private void getScreenMetric() {

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

	}

	/** 引导图的点击次数 0-picturre1 1-picture2 2-picture3 **/
	private int pointAmount = 0;

	/** 给引导图relativeLayout添加图片 **/
	private void addGuideView(int height) {

		final long startTime = System.currentTimeMillis();

		// 大小按钮的引导页
		final ImageView viewBigOrSmall = new ImageView(
				GameLuckyPanActivity.this);
		LayoutParams lp1 = new LayoutParams(mBitmaps[0].getWidth(),
				mBitmaps[0].getHeight());
		lp1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp1.addRule(RelativeLayout.CENTER_HORIZONTAL);
		// marginBottom的值为height（下边两个布局的高度 + margintop）
		// lp1.topMargin = height + DensityUtils.dip2px(20 + 60 + 30);
		// 使用marginbottom计算 屏幕高度-自定义控件高度-dp转px（标题+大小按钮整体高度）
		// lp1.bottomMargin = screenHeight - height -
		// DensityUtils.dip2px(50+50);
		lp1.topMargin = height - DensityUtils.dip2px(18);
		viewBigOrSmall.setImageResource(mImages[0]);
		rl_guide.addView(viewBigOrSmall, lp1);
		// 给图片添加动画效果
		ObjectAnimator animator = ObjectAnimator.ofFloat(viewBigOrSmall,
				"alpha", 0f, 1f);
		animator.setDuration(2000);
		animator.start();

		// 投注金额的引导页
		final ImageView viewBetAmount = new ImageView(GameLuckyPanActivity.this);
		LayoutParams lp2 = new LayoutParams(mBitmaps[1].getWidth(),
				mBitmaps[1].getHeight());
		lp2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp2.addRule(RelativeLayout.CENTER_HORIZONTAL);
		viewBetAmount.setImageResource(mImages[1]);
		lp2.topMargin = height - DensityUtils.dip2px(18)
				+ mBitmaps[0].getHeight() - DensityUtils.dip2px(50);
		viewBetAmount.setAlpha(0f);// 设置全透明 为了展示不可见
		rl_guide.addView(viewBetAmount, lp2);

		// 游戏开始的引导页
		final ImageView viewStart = new ImageView(GameLuckyPanActivity.this);
		LayoutParams lp3 = new LayoutParams(mBitmaps[2].getWidth(),
				mBitmaps[2].getHeight());
		lp3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lp3.topMargin = height - DensityUtils.dip2px(18)
				+ DensityUtils.dip2px(100);
		viewStart.setImageResource(mImages[2]);
		viewStart.setAlpha(0f); // 设置全透明 为了展示不可见
		rl_guide.addView(viewStart, lp3);

		rl_guide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 如果点击事件间隔超1.5s 才算生效的点击
				// float temporaryTime = startTime;
				if (System.currentTimeMillis() - startTime > 1500) {
					if (pointAmount == 0) {
						// 第一次点击 展示大小的说明
						// 点击后 大小说明消除 添加投注金额说明
						rl_guide.removeView(viewBigOrSmall);

						ObjectAnimator animatior1 = ObjectAnimator.ofFloat(
								viewBetAmount, "alpha", 0f, 1f);
						animatior1.setDuration(1000);
						animatior1.start();
					} else if (pointAmount == 1) {
						// 第二次点击 展示开始的说明
						rl_guide.removeView(viewBetAmount);
						System.out.println("进入到了第二次的点击事件");

						ObjectAnimator animator2 = ObjectAnimator.ofFloat(
								viewStart, "alpha", 0f, 1f);
						animator2.setDuration(1000);
						animator2.start();
					} else if (pointAmount == 2) {
						// 点击三次

						System.out.println("进入到了第三次的点击事件");
						rl_guide.removeAllViews();
						rl_guide.setVisibility(View.GONE);
						rl_guide = null;
					}
					pointAmount++;
				}
			}
		});

	}

	/** 初始化sharedpreference **/
	private void initSP() {
		sp = getSharedPreferences(GlobalConstants.PATH_SHARED_MAC, MODE_PRIVATE);
		isGuide = sp.getBoolean("isGameGuideLuckyPan", true);
		if (isGuide) {
			Editor editor = sp.edit();
			editor.putBoolean("isGameGuideLuckyPan", false);
			editor.commit();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// 获取用户账户中的乐米数 这个数值放在App.userInfo中
		initLemi();
		// 重置按钮状态
		initStatus();
	}

	/**
	 * 重置乐米数
	 */
	private void initLemi() {
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())
				&& !StringUtil.isEmpty(App.userInfo.getIntegralAcnt())) {
			BigDecimal money = new BigDecimal(App.userInfo.getIntegralAcnt());
			if (money.doubleValue() > 100000d) {
				lemiTextContent = money.divide(new BigDecimal("10000"), 2,
						RoundingMode.DOWN).toPlainString()
						+ "万";
			} else
				lemiTextContent = money.toPlainString();
		} else
			lemiTextContent = "0";
		tv_money_show.setText(lemiTextContent);
	}

	// 初始化按钮的点击状态等
	private void initStatus() {

		flagForBigOrSmall = false;
		iv_button_big.setImageResource(R.drawable.button_big_default);
		iv_button_big.setSelected(false);
		iv_button_small.setImageResource(R.drawable.button_small_default);
		iv_button_small.setSelected(false);
		iv_start.setImageResource(R.drawable.button_start_default);
		iv_start.setSelected(false);
		iv_fifty.setSelected(false);
		iv_two_hundred.setSelected(false);
		iv_five_hundred.setSelected(false);
		// et_bet_count.setCursorVisible(false);
		// et_bet_count.clearFocus();
		et_bet_count.getText().clear();
		et_bet_count.setHint("自定义");
		isRolling = false;

	}

	/**
	 * 初始化 ：投注金额 + Bitmap[]
	 */
	private void initData() {
		// 用户的投注金额
		betCount = tv_bet_money.getText().toString().trim();
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 将图片转换成Bitmap资源 为之后的测量做准备
		mImages = new int[] { R.drawable.luckypan_guide_big_small,
				R.drawable.luckypan_guide_bet_amount,
				R.drawable.luckypan_guide_start };
		mBitmaps = new Bitmap[mImages.length];
		int size = mImages.length;
		for (int i = 0; i < size; i++) {
			mBitmaps[i] = BitmapFactory.decodeResource(getResources(),
					mImages[i]);
		}
	}

	/**
	 * 初始化点击事件
	 */
	private void initEvent() {
		iv_button_big.setOnClickListener(this);
		iv_button_small.setOnClickListener(this);
		iv_start.setOnClickListener(this);

		iv_fifty.setOnClickListener(this);
		iv_two_hundred.setOnClickListener(this);
		iv_five_hundred.setOnClickListener(this);

		et_bet_count.setOnClickListener(this);

		iv_sound.setOnClickListener(this);

		id_back.setOnClickListener(this);

		id_share.setOnClickListener(this);

		tv_rule.setOnClickListener(this);

		tv_buy_lemi.setOnClickListener(this);

		ll_content_above.setOnClickListener(this);

		et_bet_count.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				String hint;
				if (hasFocus) {
					hint = et_bet_count.getHint().toString();
					et_bet_count.setTag(hint);
					et_bet_count.setHint("");
				} else {
					hint = et_bet_count.getTag().toString();
					et_bet_count.setHint(hint);
				}
			}
		});

		et_bet_count.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 设置投注金额 的现实 时时变化
				// tv_bet_money.setText(count+"");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (!TextUtils.isEmpty(s)) {
					int betMoney = Integer.parseInt(s.toString().trim());
					if (betMoney > 9999) {
						tv_bet_money.setText("9999");
						et_bet_count.setText("9999");
					} else {
						tv_bet_money.setText(s.toString());
					}
				}

			}
		});

		// 转盘停止的监听
		mRelativeLayout.setAnimationFinished(new AnimationFinished() {

			@Override
			public void finish() {
				// 发送消息
				Message msg = Message.obtain();
				msg.what = FINISHED;
				handler.sendMessage(msg);
			}
		});
		// 结果提示框 “是” or “否” 点击事件的监听
		mLuckyPanResultDialog
				.setOnTryAgainPressedListener(new onTryAgainPressedListener() {

					@Override
					public void updateIntegralAcnt() {
						initLemi();
						// 重置盘块的初始位置
						mRelativeLayout.resetRollTable();
					}
				});

	}

	/**
	 * 初始化视图
	 */
	private void initView() {
		mRelativeLayout = (LuckyPanRelativeLayout) findViewById(R.id.myLuckyPan);
		iv_start = (ImageView) findViewById(R.id.iv_start);
		iv_button_big = (ImageView) findViewById(R.id.iv_button_big);
		iv_button_small = (ImageView) findViewById(R.id.iv_button_small);
		tv_bet_money = (TextView) findViewById(R.id.tv_bet_money);
		iv_fifty = (ImageView) findViewById(R.id.iv_fifty);
		iv_two_hundred = (ImageView) findViewById(R.id.iv_two_hundred);
		iv_five_hundred = (ImageView) findViewById(R.id.iv_five_hundred);
		tv_money_show = (TextView) findViewById(R.id.tv_money_show);
		et_bet_count = (EditText) findViewById(R.id.et_bet_count);
		// ed_bet_money = (EditText) findViewById(R.id.ed_bet_money);
		iv_sound = (ImageView) findViewById(R.id.iv_sound);
		id_back = (RelativeLayout) findViewById(R.id.id_back);
		id_share = (RelativeLayout) findViewById(R.id.id_share);
		tv_rule = (TextView) findViewById(R.id.tv_rule);
		ll_betAmount = (LinearLayout) findViewById(R.id.ll_betAmount);
		tv_buy_lemi = (RelativeLayout) findViewById(R.id.tv_buy_lemi);
		rl_lemi_sound_pan = (RelativeLayout) findViewById(R.id.rl_lemi_sound_pan);
		ll_content_above = (LinearLayout) findViewById(R.id.ll_content_above);
		// 根据标识判断是否加载引导页
		if (isGuide) {
			rl_guide = (RelativeLayout) findViewById(R.id.rl_guide);
			rl_guide.setVisibility(View.VISIBLE);
		}

		mLuckyPanResultDialog = new LuckyPanResultDialog(this);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_button_big:
			// 猜“大”
			// 点击声音控制
			if (soundOn) {
				// startMP3("button_pressed.mp3");
				SoundUtils.startSoundFromResource(mActivity,
						R.raw.button_pressed);
			}
			// 选择“大” 之后的状态控制
			if (!iv_button_big.isSelected()) {
				iv_button_big.setSelected(true);
				content = "1";
				iv_button_big.setImageResource(R.drawable.button_big_pressed);
				if (iv_button_small.isSelected()) {
					iv_button_small.setSelected(false);
					iv_button_small
							.setImageResource(R.drawable.button_small_default);
				}
				flagForBigOrSmall = true;
			} else {
				iv_button_big.setSelected(false);
				iv_button_big.setImageResource(R.drawable.button_big_default);
				flagForBigOrSmall = false;
			}
			changeBettingBtnState(null);
			break;
		case R.id.iv_button_small:
			// 猜“小”
			if (soundOn) {
				// startMP3("button_pressed.mp3");
				SoundUtils.startSoundFromResource(mActivity,
						R.raw.button_pressed);
			}
			// 点击“小” 之后的状态控制
			if (!iv_button_small.isSelected()) {
				iv_button_small.setSelected(true);
				content = "2";
				iv_button_small
						.setImageResource(R.drawable.button_small_pressed);
				if (iv_button_big.isSelected()) {
					iv_button_big.setSelected(false);
					iv_button_big
							.setImageResource(R.drawable.button_big_default);
				}
				flagForBigOrSmall = true;
			} else {
				iv_button_small.setSelected(false);
				iv_button_small
						.setImageResource(R.drawable.button_small_default);
				flagForBigOrSmall = false;
			}
			changeBettingBtnState(null);
			break;

		case R.id.iv_start:
			// 点击开始

			// 获取到用户的自定义投注金额
			// betCount = ed_bet_money.getText().toString().trim();
			betCount = tv_bet_money.getText().toString().trim();
			// 开始时强制关闭软键盘
			changeBettingBtnState(null);

			if (!isRolling) {
				if (App.userInfo == null || App.userInfo.getUserId() == null
						|| TextUtils.isEmpty(App.userInfo.getUserId())) {
					// 用户未登陆 则跳转登陆
					Intent intent = new Intent(GameLuckyPanActivity.this,
							LoginActivity.class);
					startActivity(intent);
				} else if (betCount != null && !TextUtils.isEmpty(betCount)
						&& lemiTextContent != null
						&& !TextUtils.isEmpty(lemiTextContent)) {
					// 如果 投入钱数 > 剩余钱数 则跳转充值

					if (BigDecimal.valueOf(Double.parseDouble(betCount))
							.compareTo(
									BigDecimal.valueOf(Double
											.parseDouble(App.userInfo
													.getIntegralAcnt()))) == 1) {
						ToastUtil.shortToast(GameLuckyPanActivity.this,
								"乐米不足，点击右上角快速购买");
					} else {

						if (!flagForBigOrSmall) {
							Toast.makeText(GameLuckyPanActivity.this,
									"请选择大小号码", 0).show();
						} else if (!TextUtils.isEmpty(betCount)
								&& !betCount.equals("")) {
							if (Integer.parseInt(betCount) < 10) {
								Toast.makeText(GameLuckyPanActivity.this,
										"投注金额最小为10乐米", 0).show();
							} else {

								// 用户点击时转盘是停止的
								// 则该点击事件是开始命令

								// 设置flag为true 不允许用户再次点击开始
								isRolling = true;
								Controller.getInstance().gameBetting(
										GlobalConstants.URL_GAME_BETTING,
										App.userInfo.getUserId(), "3", "", "",
										content, betCount, mBack);
							}
						}
					}
				}
			} else {
				// 重复的点击处理
			}

			break;

		case R.id.iv_fifty:
			if (soundOn) {
				// startMP3("button_pressed.mp3");
				SoundUtils.startSoundFromResource(mActivity,
						R.raw.button_pressed);
			}
			changeBettingBtnState(iv_fifty);

			tv_bet_money.setText("50");
			tv_bet_money.setTextColor(Color.WHITE);
			betCount = "50";
			// ed_bet_money.setText("50");
			break;
		case R.id.iv_two_hundred:
			if (soundOn) {
				// startMP3("button_pressed.mp3");
				SoundUtils.startSoundFromResource(mActivity,
						R.raw.button_pressed);
			}
			// 修改投注按钮状态
			changeBettingBtnState(iv_two_hundred);
			tv_bet_money.setText("200");
			tv_bet_money.setTextColor(Color.WHITE);
			betCount = "200";
			// ed_bet_money.setText("200");
			break;
		case R.id.iv_five_hundred:
			if (soundOn) {
				// startMP3("button_pressed.mp3");
				SoundUtils.startSoundFromResource(mActivity,
						R.raw.button_pressed);
			}
			changeBettingBtnState(iv_five_hundred);

			tv_bet_money.setText("500");
			tv_bet_money.setTextColor(Color.WHITE);
			betCount = "500";
			break;
		case R.id.et_bet_count:

			if (iv_fifty.isSelected()) {
				iv_fifty.setSelected(false);
			} else if (iv_two_hundred.isSelected()) {
				iv_two_hundred.setSelected(false);
			} else if (iv_five_hundred.isSelected()) {
				iv_five_hundred.setSelected(false);
			}

			// 自定义投注金额的焦点控制
			// tv_bet_money.setVisibility(View.GONE);
			// ed_bet_money.setVisibility(View.VISIBLE);
			// ed_bet_money.setFocusable(true);
			// ed_bet_money.setFocusableInTouchMode(true);
			// ed_bet_money.requestFocus();
			// imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			// String text = tv_bet_money.getText().toString().trim();
			// ed_bet_money.setText(text);
			// ed_bet_money.setSelection(TextUtils.isEmpty(text) ? 0 : text
			// .length());
			// 2016-9-1 新的逻辑 点击自定义 输入内容 投注金额出的内容随着输入而改变
			// 设置光标可见
			et_bet_count.setCursorVisible(true);
			// et_bet_count.setFocusable(true);
			// et_bet_count.setFocusableInTouchMode(true);
			et_bet_count.requestFocus();
			et_bet_count.setHint("");

			// 点击 自定义 如果此时没有
			// et_bet_count
			// .setOnEditorActionListener(new OnEditorActionListener() {
			//
			// @Override
			// public boolean onEditorAction(TextView v, int actionId,
			// KeyEvent event) {
			// if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
			// // 点击软键盘中的回车按钮
			// // et_bet_count.setCursorVisible(false);
			// // et_bet_count.clearFocus();
			// // imm.hideSoftInputFromWindow(
			// // et_bet_count.getWindowToken(), 0);
			// }
			// return true;
			// }
			// });
			// 设置监听

			break;

		case R.id.iv_sound:
			// 音量控制按钮
			if (soundOn) {
				iv_sound.setImageResource(R.drawable.luckypan_sound_off);
				soundOn = false;
				if (soundOn) {
					SoundUtils.startSoundFromResource(mActivity,
							R.raw.button_pressed);
				}
			} else {
				iv_sound.setImageResource(R.drawable.luckypan_sound);
				soundOn = true;
			}

			break;

		case R.id.id_back:
			// 点击返回
			finish();
			break;
		case R.id.id_share:
			// 点击分享
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchShare(mActivity, this);
			}
			mPopMenu.showPopWindow(v);
			break;
		case R.id.tv_buy_lemi:
			// 点击，跳转乐米兑换
			Intent suitableIntent = new Intent();
			if (App.userInfo == null) {
				suitableIntent.setClass(GameLuckyPanActivity.this,
						LoginActivity.class);
			} else {
				suitableIntent.setClass(GameLuckyPanActivity.this,
						BuyRedPacketActivity.class);
				suitableIntent
						.putExtra(GridFragment.TYPE, GridFragment.BUYLEMI);
			}
			// Intent buyLeMi = new Intent(GameLuckyPanActivity.this,
			// BuyRedPacketActivity.class);
			// buyLeMi.putExtra(GridFragment.TYPE, GridFragment.BUYLEMI);
			startActivity(suitableIntent);
			break;
		case R.id.tv_rule:
			// 点击查看规则
			Intent intent = new Intent();
			intent.putExtra(TextActiity.TYPE, TextActiity.GAMEHELP);
			intent.setClass(GameLuckyPanActivity.this, TextActiity.class);
			startActivity(intent);
			break;
		case R.id.ll_content_above:
			if (et_bet_count.isFocusable()) {
				// 开始时强制关闭软键盘
				et_bet_count.setCursorVisible(false);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				et_bet_count.getText().clear();
				et_bet_count.setHint("自定义");
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 更改按钮选中状态
	 * 
	 * @param bettingBtn
	 */
	private void changeBettingBtnState(ImageView bettingBtn) {

		if (bettingBtn != null) {

			iv_fifty.setSelected(false);
			iv_two_hundred.setSelected(false);
			iv_five_hundred.setSelected(false);

			bettingBtn.setSelected(true);
		}
		et_bet_count.getText().clear();
		et_bet_count.setHint("自定义");
		if (et_bet_count.isFocusable()) {
			// 开始时强制关闭软键盘
			et_bet_count.setCursorVisible(false);
			imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}

	}

	/**
	 * 转盘开始转动 i为转盘停止的位置
	 * 
	 * @param i
	 */
	private void startToPlay(int i, boolean soundOn) {
		// mRelativeLayout.luckyStart(i);
		mRelativeLayout.startAnimation(i, soundOn);
		iv_start.setSelected(true);
		iv_start.setImageResource(R.drawable.button_start_pressed);
		// 以下是点击后自动停止的逻辑
		// 要延长或缩短停止前转动的事件，则只需要更改luckyStart()里面的targetForm（）和targetEnd（）中的倍数即可
		// mRelativeLayout.luckyEnd();
	}

	/**
	 * 请求的回调 ：转盘接口+分享接口（无登陆分享+分享得乐米）
	 */
	public CallBack mBack = new CallBack() {
		@Override
		public void resultSuccessStr(final String json) {
			super.resultSuccessStr(json);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					getResponseInfo(json);
				}
			});
		}

		@Override
		public void resultFailure(final String error) {
			super.resultFailure(error);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(GameLuckyPanActivity.this, error);
				}
			});
		}

		public void shareSuccess(final ShareObj obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (obj.getResCode().equals("0")) {
						switch (mType) {
						case 0:
						case 1:
							// 微信分享到_0：好友;_1：朋友圈
							Bitmap bm = BitmapFactory.decodeResource(
									getResources(), R.drawable.e_ui_game_share);
							shareUtils.shareToWX(obj.getTitle(),
									obj.getContent(), obj.getUrl(), bm, mType);
							break;
						case 2:
							// 分享到QQ空间
							shareUtils.shareToQQzone(obj.getTitle(),
									obj.getUrl(), obj.getImgUrl(),
									qqShareListener);
							break;
						case 3:
							// 分享到QQ
							shareUtils.shareToQQ(obj.getTitle(),
									obj.getContent(), obj.getUrl(),
									obj.getImgUrl(), qqShareListener);
							break;
						}
					}
				}
			});
		};

		public void shareFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(mActivity, error);
				}
			});
		};

		public void shareAddLeMiSuccess(final ShareAddLeMiObj obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (obj.getResCode().equals("0")) {
						double money = Double.parseDouble(TextUtils
								.isEmpty(App.userInfo.getIntegralAcnt()) ? "0"
								: App.userInfo.getIntegralAcnt())
								+ Double.parseDouble(TextUtils.isEmpty(obj
										.getAddIntegral()) ? "0" : obj
										.getAddIntegral());
						App.userInfo.setIntegralAcnt(money + "");
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						initLemi();
					}
				}
			});
		};

		public void shareAddLeMiFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(mActivity, error);
				}
			});
		};

	};

	/**
	 * 解析投注接口的返回json
	 * 
	 * @param json
	 */
	private void getResponseInfo(String json) {

		try {
			JSONObject object = new JSONObject(json);
			String resCode = object.getString("resCode");
			if (resCode != null && resCode.equals("0")) {
				if (!object.getString("lotteryResult").isEmpty()) {
					if (object.getString("lotteryResult").equals("1")) {
						stopAt = 2;
					} else if (object.getString("lotteryResult").equals("2")) {
						stopAt = 9;
					} else if (object.getString("lotteryResult").equals("3")) {
						stopAt = 7;
					} else if (object.getString("lotteryResult").equals("4")) {
						stopAt = 4;
					} else if (object.getString("lotteryResult").equals("5")) {
						stopAt = 8;
					} else if (object.getString("lotteryResult").equals("6")) {
						stopAt = 3;
					} else if (object.getString("lotteryResult").equals("7")) {
						stopAt = 6;
					} else if (object.getString("lotteryResult").equals("8")) {
						stopAt = 1;
					} else if (object.getString("lotteryResult").equals("发")) {
						Random random = new Random();
						if (random.nextInt(2) == 0) {
							stopAt = 5;
						} else {
							stopAt = 10;
						}
					}
					startToPlay(stopAt, soundOn);
				}
				if (object.getString("isWin") != null) {
					if (object.getString("isWin").equals("1")) {
						winOrLose = 1;
						if (object.getString("winAmount") != null
								&& !TextUtils.isEmpty("winAmount")) {

							winAmount = Integer.parseInt(object.optString(
									"winAmount", "0"));
						}
					} else if (object.getString("isWin").equals("0")) {
						winOrLose = 0;
					}
				}

				App.userInfo.setBetAcnt(object.getString("betAcnt"));
				App.userInfo.setIntegralAcnt(object.getString("integralAcnt"));
				UserLogic.getInstance().saveUserInfo(App.userInfo);
			} else if (resCode.equals("3002")) {
				// 登陆超时
				load();
			} else {
				ToastUtil.shortToast(GameLuckyPanActivity.this,
						object.getString("resMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/***
	 * 登陆超时的处理方法
	 */
	private void load() {

		SharedPreferences shared = getSharedPreferences(
				GlobalConstants.PATH_SHARED_MAC,
				Context.MODE_PRIVATE);
		int loginPattern = shared.getInt("loginpattern", 0);
		String openId = shared.getString("openid", "");
		boolean auto = shared.getBoolean("auto", false);
		if (loginPattern == 1 && auto) {
			// 之前的登陆状态为微信
			Controller.getInstance().loginWithOpenid(
					GlobalConstants.URL_WECHAT_LOAD, openId, new CallBack() {
						/** 使用openid登陆微信成功 **/
						public void loginWithOpenidSuccess(String json) {
							try {
								JSONObject jsonObject = new JSONObject(json);
								String resCode = jsonObject
										.getString("resCode");
								if (resCode != null && resCode.equals("0")) {
									App.userInfo = (UserInfo) Parser
											.parse(cn.com.cimgroup.protocol.Command.LOGIN,
													jsonObject);
									UserLogic.getInstance().saveUserInfo(
											App.userInfo);
									Controller.getInstance().gameBetting(
											GlobalConstants.URL_GAME_BETTING,
											App.userInfo.getUserId(), "3", "",
											"", content, betCount, mBack);

								}
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (CException e) {
								e.printStackTrace();
							}
						}

						/** 使用openid登陆微信失败 **/
						public void loginWithOpenidFailure(final String json) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.shortToast(
											GameLuckyPanActivity.this, json);
									finish();
								}
							});
						}
					});
		} else if (loginPattern == 2 && auto) {
			// 之前的登陆状态为自营
			Controller.getInstance().login(GlobalConstants.NUM_LOGIN,
					App.userInfo.getUserName(), App.userInfo.getPassword(),
					new CallBack() {
						@Override
						public void loginSuccess(UserInfo info) {
							if (info != null && info.getResCode().equals("0")) {
								String password = App.userInfo.getPassword();
								App.userInfo = info;
								App.userInfo.setPassword(password);
								// 将用户信息保存到配置文件中
								UserLogic.getInstance().saveUserInfo(
										App.userInfo);
								Controller.getInstance().gameBetting(
										GlobalConstants.URL_GAME_BETTING,
										App.userInfo.getUserId(), "3", "", "",
										content, betCount, mBack);
							}
						}

						@Override
						public void loginFailure(final String error) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.shortToast(
											GameLuckyPanActivity.this, error);
									finish();
								}
							});
						}
					});
		} else if (!auto) {
			MainActivity mainActivity = (MainActivity) ActivityManager
					.isExistsActivity(MainActivity.class);
			if (mainActivity != null) {
				// 如果存在MainActivity实例，那么展示LoboHallFrament页面
				Intent intent = new Intent(mainActivity, LoginActivity.class);
				intent.putExtra("callthepage", CallThePage.LOBOHALL);
				mainActivity.startActivity(intent);
			} else {
				LoginActivity.forwartLoginActivity(App.getInstance(),
						CallThePage.LOBOHALL);
			}
		}

	}

	// /**
	// * 音效播放
	// * @param url
	// */
	// public void startMP3(String url){
	//
	// AssetManager assetManager = this.getApplicationContext().getAssets();
	// try {
	// AssetFileDescriptor fileDescriptor = assetManager.openFd(url);
	// mPlayer = new MediaPlayer();
	// mPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
	// fileDescriptor.getStartOffset(), fileDescriptor.getLength());
	// mPlayer.prepare();
	// mPlayer.start();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }

	/**
	 * 音效停止
	 */
	public void stopMP3() {
		mPlayer.stop();
	}

	/**
	 * 销毁activity的时候关闭资源
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// mPlayer.release();
		mPlayer = null;
		handler.removeCallbacksAndMessages(null);
		handler = null;
		mRelativeLayout.setHandler(null);
		mRelativeLayout.removeAllViews();
	}

	/**
	 * 分享得乐米请求
	 */
	@Override
	public void PopShare(int type) {
		mType = type;

		shareUtils.initApi(GameLuckyPanActivity.this);
		Controller.getInstance().share(GlobalConstants.NUM_SHARE, "0", "1",
				mBack);

	}

	/**
	 * qq分享的回调
	 */
	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onComplete(Object response) {
			if (App.userInfo != null) {
				Controller.getInstance().shareAddLeMi(
						GlobalConstants.NUM_SHAREADDLEMI,
						App.userInfo.getUserId(), "0", "", mBack);
			}
			ToastUtil.shortToast(mActivity, "分享成功");
		}

		@Override
		public void onError(UiError e) {
			ToastUtil.shortToast(mActivity, e.errorMessage);
		}

		@Override
		public void onCancel() {
			ToastUtil.shortToast(mActivity, "取消分享");
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == Constants.REQUEST_QQ_SHARE
				|| resultCode == Constants.ACTIVITY_OK) {
			Tencent.onActivityResultData(requestCode, resultCode, data,
					qqShareListener);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
