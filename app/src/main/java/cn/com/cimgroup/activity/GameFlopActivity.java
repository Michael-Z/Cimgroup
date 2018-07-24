package cn.com.cimgroup.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.bean.ShareAddLeMiObj;
import cn.com.cimgroup.bean.ShareObj;
import cn.com.cimgroup.bean.UserInfo;
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
import cn.com.cimgroup.view.FlopViewLayout;
import cn.com.cimgroup.view.FlopViewLayout.FlopGameListener;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.SoundUtils;
import cn.com.cimgroup.xutils.ToastUtil;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 翻牌游戏
 * 
 * @author 秋风
 * 
 */
@SuppressLint({ "ClickableViewAccessibility", "HandlerLeak", "UseSparseArrays" })
public class GameFlopActivity extends BaseActivity implements OnClickListener,
		FlopGameListener, PlayMenuItemClick {

	/** 是否是第一次初始化布局 */
	private boolean isFirstInitLayout = true;
	/** 翻牌翻转放大展示结果布局 */
	private RelativeLayout id_layout;

	/** 动画视图 */
	private FlopViewLayout id_flopview;

	/** 开始游戏 */
	private ImageView id_start_game;

	/** 通知开始旋转放大 */
	private static final int NOTFY_START_ROTATE_ANIMATION = 0x10000;

	/** 声音控制开关图片 */
	private ImageView id_sound;
	/** 投注选项按钮背景 */
	private ImageView id_big_bg;
	/** 投注选项小背景 */
	private ImageView id_small_bg;
	/** 投注选项奇数背景 */
	private ImageView id_odd_bg;;
	/** 投注选项偶数背景 */
	private ImageView id_even_bg;
	/** 投注乐米数编辑框 */
	private EditText id_lemi_edit;
	/** 投注乐米数显示框 */
	private TextView id_lemi_show;

	/** 乐米投注50 */
	private ImageView id_fifty;
	/** 乐米投注200 */
	private ImageView id_two_hundred;
	/** 乐米投注500 */
	private ImageView id_five_hundred;
	/** 自由输入 */
	private TextView id_auto;

	/** 音乐开关标识（默认打开） */
	private boolean isOpenSound = true;

	/** 是否已选投注类别（默认false） */
	private boolean isChoosePlayType = false;
	/** 投注类型 :1【大】，2【小】，3【奇】,4【偶】 */
	private String mBettingType = "";

	/** 翻牌结果展示布局 */
	private RelativeLayout id_flop_result_layout;
	/** 翻牌结果展示--纸牌左上角文字 */
	private TextView id_toast_left_text;
	/** 翻牌结果展示--纸牌右下角文字 */
	private TextView id_toast_rignt_text;
	/** 翻牌结果展示--纸牌左上角花色 */
	private ImageView id_toast_left_color;
	/** 翻牌结果展示--纸牌右下角花色 */
	private ImageView id_toast_rignt_color;
	/** 翻牌结果展示--纸牌中间花色 */
	private ImageView id_toast_middle_color;
	/** 翻牌结果展示--是否中奖icon */
	private ImageView id_draw_icon;
	/** 翻牌结果展示--中奖结果title */
	private TextView id_draw_toast_title;
	/** 翻牌结果展示--中奖结果详情 */
	private TextView id_draw_toast_desc;
	/** 翻牌结果展示--中奖结果分享 */
	private ImageView id_toast_share;
	/** 翻牌结果展示--开始下一局 */
	private ImageView id_toast_ok;

	/** 用户乐米数 */
	private TextView id_lemi_count;

	/** 分享用的工具类 **/
	private ShareUtils shareUtils;
	/** 头部分享popupwindow **/
	private PopupWndSwitchShare mPopMenu;
	/** 分享类型 **/
	private int mType;

	private InputMethodManager imm;

	/** 是否需要进行翻牌游戏的指引 */
	private boolean isGuide = true;
	/** 引导层 */
	private RelativeLayout id_guide_layout;
	/** 规则 */
	private TextView id_explain;
	/**颜色 红*/
	private int colorRed = 0;
	/** 颜色黑 */
	private int colorBlack = 0;

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		colorRed = getResources().getColor(R.color.color_red);
		colorBlack = getResources().getColor(R.color.color_gray_secondary);
		
		
		id_lemi_count = (TextView) findViewById(R.id.id_lemi_count);

		id_fifty = (ImageView) findViewById(R.id.id_fifty);
		id_two_hundred = (ImageView) findViewById(R.id.id_two_hundred);
		id_five_hundred = (ImageView) findViewById(R.id.id_five_hundred);
		id_auto = (TextView) findViewById(R.id.id_auto);

		id_flopview = (FlopViewLayout) findViewById(R.id.id_flopview);
		id_start_game = (ImageView) findViewById(R.id.id_start_game);
		id_layout = (RelativeLayout) findViewById(R.id.id_layout);

		id_sound = (ImageView) findViewById(R.id.id_sound);

		id_big_bg = (ImageView) findViewById(R.id.id_big_bg);
		id_small_bg = (ImageView) findViewById(R.id.id_small_bg);
		id_odd_bg = (ImageView) findViewById(R.id.id_odd_bg);
		id_even_bg = (ImageView) findViewById(R.id.id_even_bg);

		id_lemi_edit = (EditText) findViewById(R.id.id_lemi_edit);
		id_lemi_edit.setFocusable(false);
		id_lemi_show = (TextView) findViewById(R.id.id_lemi_show);

		id_toast_share = (ImageView) findViewById(R.id.id_toast_share);
		id_toast_ok = (ImageView) findViewById(R.id.id_toast_ok);
		id_explain = (TextView) findViewById(R.id.id_explain);
		if (isGuide) {
			// 如果需要进行引导
			id_guide_layout = (RelativeLayout) findViewById(R.id.id_guide_layout);
			id_guide_layout.setVisibility(View.VISIBLE);
		}

		id_toast_left_text = (TextView) findViewById(R.id.id_toast_left_text);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus && id_guide_layout != null) {
			addGuideView(id_explain.getMeasuredHeight());

		}
	}

	/** 记录点击次数 */
	private int mClickCount = 0;

	private void addGuideView(int margin) {

		final long addStart = System.currentTimeMillis();
		Bitmap bmFirst = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_guide_flop_first);
		LayoutParams lpFirst = new LayoutParams(bmFirst.getWidth(),
				bmFirst.getHeight());
		lpFirst.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lpFirst.bottomMargin = margin + DensityUtils.dip2px(5);
		lpFirst.addRule(RelativeLayout.CENTER_HORIZONTAL);
		final ImageView ivFirst = new ImageView(this);
		ivFirst.setImageBitmap(bmFirst);
		id_guide_layout.addView(ivFirst, lpFirst);
		ObjectAnimator animatorTop = ObjectAnimator.ofFloat(ivFirst, "alpha",
				0f, 1f);
		animatorTop.setDuration(2000);
		animatorTop.start();

		Bitmap bmSecond = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_guide_flop_second);
		LayoutParams lpSecond = new LayoutParams(bmSecond.getWidth(),
				bmSecond.getHeight());
		lpSecond.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lpSecond.bottomMargin = margin + DensityUtils.dip2px(65);
		lpSecond.addRule(RelativeLayout.CENTER_HORIZONTAL);
		final ImageView ivSecond = new ImageView(this);
		ivSecond.setImageBitmap(bmSecond);
		ivSecond.setAlpha(0f);
		id_guide_layout.addView(ivSecond, lpSecond);

		Bitmap bmThird = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_guide_flop_third);
		LayoutParams lpThird = new LayoutParams(bmThird.getWidth(),
				bmThird.getHeight());
		lpThird.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lpThird.bottomMargin = margin + DensityUtils.dip2px(55);
		lpThird.addRule(RelativeLayout.CENTER_HORIZONTAL);
		final ImageView ivThird = new ImageView(this);
		ivThird.setImageBitmap(bmThird);
		ivThird.setAlpha(0f);
		id_guide_layout.addView(ivThird, lpThird);

		id_guide_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (System.currentTimeMillis() - addStart > 1500) {
					if (mClickCount == 0) {
						id_guide_layout.removeView(ivFirst);

						ObjectAnimator animatorMiddle = ObjectAnimator.ofFloat(
								ivSecond, "alpha", 0f, 1f);
						animatorMiddle.setDuration(1000);
						animatorMiddle.start();
					} else if (mClickCount == 1) {
						id_guide_layout.removeView(ivSecond);
						ObjectAnimator animatorBottom = ObjectAnimator.ofFloat(
								ivThird, "alpha", 0f, 1f);
						animatorBottom.setDuration(1000);
						animatorBottom.start();

					} else if (mClickCount == 2) {
						id_guide_layout.removeAllViews();
						id_guide_layout.setVisibility(View.GONE);
						id_guide_layout = null;
					}

					mClickCount++;
				}
			}
		});

	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 返回
		findViewById(R.id.id_back).setOnClickListener(this);
		// 购买乐米
		findViewById(R.id.id_jump_recharge).setOnClickListener(this);

		findViewById(R.id.id_sound_layout).setOnClickListener(this);
		findViewById(R.id.id_big_layout).setOnClickListener(this);
		findViewById(R.id.id_small_layout).setOnClickListener(this);
		findViewById(R.id.id_odd_layout).setOnClickListener(this);
		findViewById(R.id.id_even_layout).setOnClickListener(this);
		id_auto.setOnClickListener(this);
		id_fifty.setOnClickListener(this);
		id_two_hundred.setOnClickListener(this);
		id_five_hundred.setOnClickListener(this);
		// 头部分享按钮
		findViewById(R.id.id_share).setOnClickListener(this);

		findViewById(R.id.id_explain).setOnClickListener(this);

		id_start_game.setOnClickListener(this);
		id_flopview.setOnFlopGameListener(this);

		// 翻牌结果--点击分享按钮
		id_toast_share.setOnClickListener(this);
		// 翻牌结果--点击确定按钮
		id_toast_ok.setOnClickListener(this);

		id_layout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		id_lemi_edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = "";
				if (!TextUtils.isEmpty(s)) {
					str = s.toString();
					int lemi = Integer.valueOf(str);
					if (lemi > 9999) {
						id_lemi_edit.setText("9999");
						str = "9999";
					}
					id_lemi_show.setText(str);
				}
			}
		});
		id_lemi_edit.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				id_lemi_show
						.setText(((EditText) v).getText().toString().trim());
			}
		});

		findViewById(R.id.id_parent_layout).setOnClickListener(this);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case NOTFY_START_ROTATE_ANIMATION:
				initRotateAnimation();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_flop);
		SharedPreferences shared = getSharedPreferences(
				GlobalConstants.PATH_SHARED_MAC, 0x0000);
		isGuide = shared.getBoolean("isGameGuideFlop", true);
		if (isGuide) {
			Editor ed = shared.edit();
			ed.putBoolean("isGameGuideFlop", false);
			ed.commit();
		}
		initView();
		initEvent();
		initAttrs();
		initSound();
	}

	private Map<Integer, Integer> mSoundIds;

	/**
	 * 初始化音效
	 */
	private void initSound() {
		if (mSoundIds == null) {
			mSoundIds = new HashMap<Integer, Integer>();
		} else
			mSoundIds.clear();
		mSoundIds.put(1,
				SoundUtils.getInstance().load(this, R.raw.button_pressed, 1));
		mSoundIds.put(2, SoundUtils.getInstance()
				.load(this, R.raw.card_send, 1));
		mSoundIds.put(3, SoundUtils.getInstance().load(this, R.raw.win, 1));
		mSoundIds.put(4, SoundUtils.getInstance().load(this, R.raw.lose, 1));
		SoundUtils.setIds(mSoundIds);

	}

	/**
	 * 初始化属性
	 */
	private void initAttrs() {
		mMargin = DensityUtils.dip2px(3);
		mTextSize = DensityUtils.px2sp(id_toast_left_text.getTextSize());
		mWidth = DensityUtils.getScreenWidth(mActivity);
		shareUtils = new ShareUtils();
	}

	/**
	 * 执行动画
	 */
	private void startAnimation(int left, int top) {
		// 将图片位移 动画XY
		ObjectAnimator moveX = ObjectAnimator.ofFloat(mMoveFlop,
				"translationX", (mWidth - mFlopWidth) / 2 - left);
		ObjectAnimator moveY = ObjectAnimator.ofFloat(mMoveFlop,
				"translationY", id_flopview.getMeasuredHeight() / 2
						- mFlopHeight / 2 - top - mMargin * 3);
		// 将图片放大
		ObjectAnimator scaleX = ObjectAnimator.ofFloat(mMoveFlop, "scaleX", 1f,
				mMultipe);
		ObjectAnimator scaleY = ObjectAnimator.ofFloat(mMoveFlop, "scaleY", 1f,
				mMultipe);
		AnimatorSet animSet = new AnimatorSet();
		animSet.play(moveX).with(moveY).with(scaleX).with(scaleY);
		animSet.setDuration(500);
		animSet.start();
		animSet.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				isCanGoAnimation = true;
				mHandler.sendEmptyMessage(NOTFY_START_ROTATE_ANIMATION);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});

	}

	/** 动画布局的宽度 */
	private int mWidth;

	private Bitmap mFlop;
	private ImageView mMoveFlop;

	private void addMoveFlop(int left, int top) {

		if (isFirstInitLayout) {
			initRotateLayout();
			isFirstInitLayout = false;
		}
		LayoutParams lp = new LayoutParams(mFlop.getWidth(), mFlop.getHeight());
		lp.leftMargin = left;
		lp.topMargin = top;
		mMoveFlop = new ImageView(this);
		mMoveFlop.setImageBitmap(BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_game_flop_revers_1));
		id_layout.addView(mMoveFlop, lp);
	}

	// /** 背景白板Bitmap */
	// private Bitmap mFlopBg;
	/** 纸牌花色 */
	private Bitmap[] mCardColor;
	/** 纸牌宽 */
	private int mFlopWidth;
	/** 纸牌高 */
	private int mFlopHeight;
	/** 花色宽 */
	private int mCardWidth;
	/** 花色高 */
	private int mCardHeight;

	private void initRotateLayout() {
		// mFlopBg = BitmapFactory.decodeResource(getResources(),
		// R.drawable.e_ui_game_flop_show);

		mFlop = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_game_flop_outline);
		mCardColor = new Bitmap[] {
				BitmapFactory.decodeResource(getResources(),
						R.drawable.e_ui_game_flop_heart),
				BitmapFactory.decodeResource(getResources(),
						R.drawable.e_ui_game_flop_spade),
				BitmapFactory.decodeResource(getResources(),
						R.drawable.e_ui_game_flop_square),
				BitmapFactory.decodeResource(getResources(),
						R.drawable.e_ui_game_flop_plum) };
		// 获取布局宽高

		// 获取纸牌宽高
		mFlopWidth = mFlop.getWidth();
		mFlopHeight = mFlop.getHeight();
		// 花色宽高
		mCardWidth = mCardColor[0].getWidth();
		mCardHeight = mCardColor[0].getHeight();
	}

	/** 随机生成器 */
	private Random mRand = new Random();
	/** 左上角图片 */
	private ImageView mTopImage;
	/** 中间图片 */
	private ImageView mMiddleImage;
	/** 底部图片 */
	private ImageView mBottomImage;
	/** 纸牌左上角数字 */
	private TextView mTopN0;
	/** 纸牌右下角图片 */
	private TextView mBottomNO;

	/** 纸牌布局 */
	private RelativeLayout mFlowLayout;
	/** 纸牌布局layoutparams */
	private LayoutParams mFlowLayoutParams;

	/** 纸牌放大系数 */
	private float mMultipe = 1.56f;

	/** 四周间距 */
	private int mMargin;

	private float mTextSize;

	private int mColorIndex = -1;

	/**
	 * 添加纸牌布局
	 */
	@SuppressWarnings("ResourceType")
	private void addFlopLayout() {
		mColorIndex = mRand.nextInt(4);
		mFlowLayout = new RelativeLayout(this);
		mFlowLayoutParams = new LayoutParams(mFlopWidth, mFlopHeight);
		mFlowLayoutParams.topMargin = pxToDip(120);
		mFlowLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mFlowLayout.setBackgroundResource(R.drawable.e_ui_game_flop_show);
		// 添加左上角文字 因为需要翻转所以左右方向相反
		LayoutParams lpTNo = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lpTNo.rightMargin = mMargin;
		lpTNo.topMargin = -mMargin;
		lpTNo.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mTopN0 = new TextView(this);
		mTopN0.setId(2001);
		// mTopN0.setText("A");
		mTopN0.setRotationY(180f);
		mTopN0.setTextSize(mTextSize);
		mTopN0.setScaleX(1 / mMultipe);
		mTopN0.setScaleY(1 / mMultipe);
		mTopN0.setTextColor(mColorIndex % 2 == 0 ? colorRed : colorBlack);
		mFlowLayout.addView(mTopN0, lpTNo);
		// 添加左下角文字 因为需要翻转所以左右方向相反
		LayoutParams lpBNo = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lpBNo.leftMargin = mMargin;
		lpBNo.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mBottomNO = new TextView(this);
		// mBottomNO.setText("A");
		mBottomNO.setRotationX(180f);
		mBottomNO.setId(2002);
		mBottomNO.setTextSize(mTextSize);
		mBottomNO.setTextColor(mColorIndex % 2 == 0 ? colorRed : colorBlack);
		mBottomNO.setScaleX(1 / mMultipe);
		mBottomNO.setScaleY(1 / mMultipe);
		mFlowLayout.addView(mBottomNO, lpBNo);
		// 添加左上角花色 因为需要翻转所以左右方向相反
		LayoutParams lptImage = new LayoutParams(
				(int) (mCardWidth * 1 / mMultipe),
				(int) (mCardHeight * 1 / mMultipe));
		lptImage.topMargin = (int) (-mMargin * 2.6);
		lptImage.rightMargin = -mMargin;
		lptImage.addRule(RelativeLayout.BELOW, mTopN0.getId());
		lptImage.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		mTopImage = new ImageView(this);
		mTopImage.setScaleX(0.5f);
		mTopImage.setScaleY(0.6f);
		mTopImage.setImageBitmap(mCardColor[mColorIndex]);
		mTopImage.setScaleX(0.4f);
		mTopImage.setScaleY(0.6f);
		mFlowLayout.addView(mTopImage, lptImage);
		// 添加右下角花色 因为需要翻转所以左右方向相反
		LayoutParams lbtImage = new LayoutParams(
				(int) (mCardWidth * 1 / mMultipe),
				(int) (mCardHeight * 1 / mMultipe));
		lbtImage.addRule(RelativeLayout.ABOVE, mBottomNO.getId());
		lbtImage.bottomMargin = -mMargin * 2;
		lbtImage.rightMargin = -mMargin;
		mBottomImage = new ImageView(this);
		mBottomImage.setImageBitmap(mCardColor[mColorIndex]);
		mBottomImage.setRotation(180);
		mBottomImage.setScaleX(0.4f);
		mBottomImage.setScaleY(0.6f);
		mFlowLayout.addView(mBottomImage, lbtImage);
		// 添加中间花色
		LayoutParams lbmImage = new LayoutParams(mCardWidth, mCardHeight);
		lbmImage.rightMargin = mMargin;
		lbmImage.bottomMargin = mMargin;
		lbmImage.addRule(RelativeLayout.CENTER_IN_PARENT, mBottomNO.getId());
		mMiddleImage = new ImageView(this);
		mMiddleImage.setImageBitmap(mCardColor[mColorIndex]);
		mMiddleImage.setScaleX(1 / mMultipe);
		mMiddleImage.setScaleY(1 / mMultipe);
		mFlowLayout.addView(mMiddleImage, lbmImage);
		// 将布局添加到自定义布局中
		id_layout.addView(mFlowLayout, mFlowLayoutParams);
		mFlowLayout.setVisibility(View.INVISIBLE);
	}

	/**
	 * 清空游戏纸牌布局
	 */
	private void clearFlowLayout() {
		if (mFlowLayout != null) {
			mFlowLayout.removeAllViews();
		}
		id_layout.removeAllViews();
		// id_layout.removeView(mMoveFlop);
		mMoveFlop = null;
		mTopN0 = null;
		mBottomNO = null;
		mTopImage = null;
		mBottomImage = null;
		mFlowLayoutParams = null;
		mFlowLayout = null;
	}

	/**
	 * 初始化结果展示布局
	 */
	private void initToastLayout() {
		if (id_toast_left_text == null) {
			id_toast_left_text = (TextView) findViewById(R.id.id_toast_left_text);
		}

		id_toast_rignt_text = (TextView) findViewById(R.id.id_toast_rignt_text);
		id_toast_left_color = (ImageView) findViewById(R.id.id_toast_left_color);
		id_toast_rignt_color = (ImageView) findViewById(R.id.id_toast_rignt_color);
		id_toast_middle_color = (ImageView) findViewById(R.id.id_toast_middle_color);
		id_draw_icon = (ImageView) findViewById(R.id.id_draw_icon);
		id_toast_share = (ImageView) findViewById(R.id.id_toast_share);
		id_toast_ok = (ImageView) findViewById(R.id.id_toast_ok);
		id_draw_toast_title = (TextView) findViewById(R.id.id_draw_toast_title);
		id_draw_toast_desc = (TextView) findViewById(R.id.id_draw_toast_desc);
	}

	/**
	 * 初始化结果展示页面数据
	 */
	private void initToastLayoutDatas() {
		id_toast_left_text.setText(mLotterResult);
		id_toast_rignt_text.setText(mLotterResult);
		id_toast_left_text.setTextColor(mColorIndex % 2 == 0 ? colorRed
				: colorBlack);
		id_toast_rignt_text.setTextColor(mColorIndex % 2 == 0 ? colorRed
				: colorBlack);
		id_toast_left_color.setImageBitmap(mCardColor[mColorIndex]);
		id_toast_rignt_color.setImageBitmap(mCardColor[mColorIndex]);
		id_toast_middle_color.setImageBitmap(mCardColor[mColorIndex]);
		if (isWin.equals("0")) {
			// SoundUtils.playSound(mActivity, R.raw.lose);
			// SoundUtils.startSoundFromResource(mActivity,
			// R.raw.lose);
			// SoundUtils.playSound(4,mActivity);
			if (isOpenSound) {
				SoundUtils.playSound(4);
			}

			mResultTitle = "很遗憾！大奖飞走了~";
			id_draw_icon.setImageResource(R.drawable.e_ui_game_draw_icon_false);
		} else {
			// SoundUtils.playSound(mActivity, R.raw.win);
			// SoundUtils.playSound(3,mActivity);
			// SoundUtils.startSoundFromResource(mActivity,
			// R.raw.win);
			if (isOpenSound) {
				SoundUtils.playSound(3);
			}

			id_draw_icon.setImageResource(R.drawable.e_ui_game_draw_icon_true);
			mResultTitle = "恭喜您!赢啦！  ";
			// if (mContinueWinNum == 1) {
			// mResultTitle = "恭喜！！中奖了~";
			// } else {
			// mResultTitle = "恭喜！！连赢" + mContinueWinNum + "次了~";
			// }
		}
		id_draw_toast_title.setText(mResultTitle);
		id_draw_toast_desc.setText(mResultDesc);
	}

	/**
	 * px转换dp
	 * 
	 * @param px
	 * @return
	 */
	private int pxToDip(int px) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px,
				getResources().getDisplayMetrics());
	}

	/** 是否已经返回请求结果 */
	private boolean isRequestCallback = false;

	/**
	 * 开始旋转动画
	 */
	private void initRotateAnimation() {
		if (isRequestCallback && isCanGoAnimation) {

			mFlowLayout.setVisibility(View.VISIBLE);
			mFlowLayout.setRotationY(90f);
			mFlowLayout.setScaleX(mMultipe);
			mFlowLayout.setScaleY(mMultipe);

			ObjectAnimator rotation = ObjectAnimator.ofFloat(mMoveFlop,
					"rotationY", 0, 90);
			ObjectAnimator rotationY = ObjectAnimator.ofFloat(mFlowLayout,
					"rotationY", 90, 180);
			AnimatorSet animSet = new AnimatorSet();
			animSet.setDuration(300);
			animSet.play(rotationY).after(rotation);
			animSet.start();
			animSet.addListener(new AnimatorListener() {

				@Override
				public void onAnimationStart(Animator animation) {
					mMoveFlop.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// 重置位移动画布局
					clearFlowLayout();
					if (id_flop_result_layout == null) {
						id_flop_result_layout = (RelativeLayout) findViewById(R.id.id_flop_result_layout);
						initToastLayout();
					}
					// 初始化结果展示布局数据结果
					initToastLayoutDatas();
					id_flop_result_layout.setVisibility(View.VISIBLE);

				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}
			});
		}
	}

	private boolean isSendFlop = false;

	@Override
	public void onResume() {
		super.onResume();
		setLemi();
	}

	/**
	 * 显示乐米
	 */
	private void setLemi() {
		String moneyStr = "0";
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())) {

			BigDecimal money = new BigDecimal(TextUtils.isEmpty(App.userInfo
					.getIntegralAcnt()) ? "0" : App.userInfo.getIntegralAcnt());

			if (money.doubleValue() > 100000d) {
				moneyStr = money.divide(new BigDecimal("10000"), 2,
						RoundingMode.DOWN).toPlainString()
						+ "万";
			} else
				moneyStr = money.toPlainString();
		}
		id_lemi_count.setText(moneyStr);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.id_jump_recharge:
			// 跳转去购买乐米
			if (App.userInfo != null) {
				Intent buyLeMi = new Intent(GameFlopActivity.this,
						BuyRedPacketActivity.class);
				buyLeMi.putExtra(GridFragment.TYPE, GridFragment.BUYLEMI);
				startActivity(buyLeMi);
			} else {
				startActivity(LoginActivity.class);
			}

			break;
		case R.id.id_back:
			finish();
			break;
		case R.id.id_start_game:
			// 开始运行动画
			if (!isSendFlop) {
				if (isOpenSound) {
					// SoundUtils.playSound(1,mActivity);
					// SoundUtils.startSoundFromResource(mActivity,
					// R.raw.button_pressed);
					SoundUtils.playSound(1);
					// SoundUtils.playSound(mActivity, R.raw.button_pressed);
				}
				isSendFlop = true;
				id_flopview.runInAnimation();
				id_start_game
						.setImageResource(R.drawable.e_ui_game_flop_start_d);
			}
			if (id_auto.getVisibility() == View.GONE) {
				showOrhideSoftInputFroWindow(true);
			}
			break;
		case R.id.id_sound_layout:
			// 设置音乐开关
			setSound();
			// 声音开关
			break;
		case R.id.id_big_layout:
			// 投注选项“大”
			typeOnclick(id_big_bg, "1");
			break;
		case R.id.id_small_layout:
			// 投注选项“小”
			typeOnclick(id_small_bg, "2");
			break;
		case R.id.id_odd_layout:
			// 投注选项“奇数”
			typeOnclick(id_odd_bg, "3");
			break;
		case R.id.id_even_layout:
			// 投注选项偶数
			typeOnclick(id_even_bg, "4");
			break;
		case R.id.id_fifty:
			// 固定投注50乐米

			setBettingLemi("50");
			id_fifty.setSelected(true);
			break;
		case R.id.id_two_hundred:
			// 固定投注200乐米
			setBettingLemi("200");
			id_two_hundred.setSelected(true);
			break;
		case R.id.id_five_hundred:
			// 固定投注500乐米
			setBettingLemi("500");
			id_five_hundred.setSelected(true);
			break;
		case R.id.id_auto:
			// 手动输入投注数额
			resetBettings();
			// switchInputType();
			showOrhideSoftInputFroWindow(false);
			break;
		case R.id.id_toast_ok:
			// 展示结果时点击确定按钮
			// 重置游戏画面
			if (isOpenSound) {
				// SoundUtils.playSound(mActivity, R.raw.button_pressed);
				// SoundUtils.playSound(1,mActivity);
				// SoundUtils.startSoundFromResource(mActivity,
				// R.raw.button_pressed);
				SoundUtils.playSound(1);
			}
			reSetFlop();
			break;
		case R.id.id_toast_share:
			// 分享-游戏结果
			mType = 0;
			shareUtils.initApi(GameFlopActivity.this);
			Controller.getInstance().share(GlobalConstants.NUM_SHARE, "0", "1",
					mBack);
			break;
		case R.id.id_share:
			// 分享-标题
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchShare(mActivity, this);
			}
			mPopMenu.showPopWindow(view);
			break;
		case R.id.id_explain: // 玩法介绍(跳转)
			Intent intent = new Intent();
			intent.putExtra(TextActiity.TYPE, TextActiity.GAMEHELP);
			intent.setClass(GameFlopActivity.this, TextActiity.class);
			startActivity(intent);
			break;
		case R.id.id_parent_layout:
			if (id_auto.getVisibility() == View.GONE) {
				showOrhideSoftInputFroWindow(true);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 重置投注金额按钮状态
	 */
	private void resetBettings() {
		id_fifty.setSelected(false);
		id_two_hundred.setSelected(false);
		id_five_hundred.setSelected(false);
	}

	/**
	 * 重置游戏画面
	 */
	private void reSetFlop() {
		isRequestCallback = false;// 初始化请求返回标识
		isCanGoAnimation = false;// 初始化动画可否开始标识
		// 隐藏翻转动画布局
		id_layout.setVisibility(View.GONE);
		// 隐藏翻牌结果布局
		id_flop_result_layout.setVisibility(View.GONE);
		// 执行纸牌移除动画，并移除所有纸牌
		id_flopview.removeAnimationViews();

		// 初始化所有玩法类型的背景
		initAllChooseType();
		mBettingType = "0";
		isChoosePlayType = false;
		// id_lemi_show.setText("");
		id_flopview.setBettingType(isChoosePlayType);
		resetBettings();
	}

	/**
	 * 点击自定义
	 */
	private void switchInputType() {
		id_auto.setVisibility(View.GONE);
		id_lemi_edit.setVisibility(View.VISIBLE);
		id_lemi_edit.setFocusable(true);
		id_lemi_edit.setFocusableInTouchMode(true);
		id_lemi_edit.requestFocus();
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		String text = id_lemi_show.getText().toString().trim();
		id_lemi_edit.setText(text);
		id_lemi_edit.setSelection(TextUtils.isEmpty(text) ? 0 : text.length());
	}

	/**
	 * 投注乐米数
	 * 
	 * @param number数量
	 */
	private void setBettingLemi(String number) {
		// 充值投注金额状态
		resetBettings();
		id_lemi_show.setText(number);
		id_lemi_edit.setText(number);
		showOrhideSoftInputFroWindow(true);
		if (isOpenSound) {
			// SoundUtils.playSound(mActivity, R.raw.button_pressed);
			// SoundUtils.startSoundFromResource(mActivity,
			// R.raw.button_pressed);
			// SoundUtils.playSound(1,mActivity);
			SoundUtils.playSound(1);
		}
	}

	/**
	 * 显示或隐藏软键盘
	 * 
	 * @param isShowState
	 */
	private void showOrhideSoftInputFroWindow(boolean isShowState) {
		if (isShowState) {
			id_auto.setVisibility(View.VISIBLE);
			id_lemi_edit.setVisibility(View.GONE);
			id_lemi_edit.setFocusable(false);
			imm.hideSoftInputFromWindow(id_lemi_edit.getWindowToken(), 0);
		} else {
			id_auto.setVisibility(View.GONE);
			id_lemi_edit.setVisibility(View.VISIBLE);
			id_lemi_edit.setFocusable(true);
			id_lemi_edit.setFocusableInTouchMode(true);
			id_lemi_edit.requestFocus();
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			String text = id_lemi_show.getText().toString().trim();
			id_lemi_edit.setText(text);
			id_lemi_edit.setSelection(TextUtils.isEmpty(text) ? 0 : text
					.length());
		}
	}

	/**
	 * 投注选项按下
	 * 
	 * @param view点击的按钮
	 * @param type选中时的类型
	 */
	private void typeOnclick(ImageView view, String type) {
		if (mBettingType.equals(type)) {
			// 说明点击的是同一个投注类型
			return;
		}
		if (id_auto.getVisibility() == View.GONE) {
			showOrhideSoftInputFroWindow(true);
		}
		if (isChoosePlayType) {// 已经有选定，选择了其他的类型
			// 初始化所有玩法类型的背景
			initAllChooseType();
		}
		view.setImageResource(R.drawable.e_ui_game_flop_c);
		mBettingType = type;
		isChoosePlayType = true;
		id_flopview.setBettingType(isChoosePlayType);
		if (isOpenSound) {
			// SoundUtils.playSound(mActivity, R.raw.button_pressed);
			// SoundUtils.startSoundFromResource(mActivity,
			// R.raw.button_pressed);
			// SoundUtils.playSound(1,mActivity);
			SoundUtils.playSound(1);
		}
	}

	/**
	 * 初始化所有玩法类型的背景
	 */
	private void initAllChooseType() {
		id_big_bg.setImageResource(R.drawable.e_ui_game_flop_n);
		id_small_bg.setImageResource(R.drawable.e_ui_game_flop_n);
		id_odd_bg.setImageResource(R.drawable.e_ui_game_flop_n);
		id_even_bg.setImageResource(R.drawable.e_ui_game_flop_n);
	}

	/**
	 * 设置音乐开关
	 */
	private void setSound() {
		if (isOpenSound) {

			id_sound.setImageResource(R.drawable.e_ui_game_sound_c);
			ToastUtil.shortToast(mActivity, "音效已关闭");
		} else {
			// SoundUtils.playSound(mActivity, R.raw.button_pressed);
			// SoundUtils.startSoundFromResource(mActivity,
			// R.raw.button_pressed);
			// SoundUtils.playSound(1,mActivity);
			SoundUtils.playSound(1);
			id_sound.setImageResource(R.drawable.e_ui_game_sound_o);
			ToastUtil.shortToast(mActivity, "音效已打开");
		}
		isOpenSound = !isOpenSound;
		id_flopview.setSoundOpen(isOpenSound);
	}

	@Override
	public void startFlopAnimation(int left, int top) {
		id_layout.removeAllViews();
		id_layout.setVisibility(View.VISIBLE);
		addMoveFlop(left, top);
		addFlopLayout();
		startAnimation(left, top);
	}

	@Override
	public boolean isBettingLemi() {
		id_lemi_edit.setFocusable(false);
		return TextUtils.isEmpty(id_lemi_show.getText().toString().trim())
		// || TextUtils.isEmpty(id_lemi_edit.getText().toString().trim())
		? false
				: true;
	}

	@Override
	public void sendBettingRequest() {
		if (id_auto.getVisibility() == View.GONE) {
			showOrhideSoftInputFroWindow(true);
		}
		isRequestCallback = false;
		/**
		 * num_接口码 userId_用户Id gameId_ leagueCode questionId content mBack num
		 * userId gameId betAmount
		 */
		sendRequest();
	}

	/**
	 * 发送请求
	 */
	private void sendRequest() {
		Controller.getInstance().gameBetting(GlobalConstants.URL_GAME_BETTING,
				App.userInfo.getUserId(), "2", "", "", mBettingType,
				id_lemi_edit.getText().toString().trim(), new CallBack() {
					@Override
					public void resultSuccessStr(final String json) {
						runOnUiThread(new Runnable() {
							public void run() {
								getResultJson(json);
								isRequestCallback = true;
								mHandler.sendEmptyMessage(NOTFY_START_ROTATE_ANIMATION);
							}
						});
					}

					@Override
					public void resultFailure(final String error) {
						runOnUiThread(new Runnable() {
							public void run() {
								ToastUtil.shortToast(mActivity, error);
							}
						});
					}
				});
	}

	@Override
	public void toastUserLoad() {
		// 提示用户去登陆
		startActivity(LoginActivity.class);
	}

	@Override
	public String getBettingCount() {
		// 由于之前判断了非空 所以此处不再判断
		return id_lemi_edit.getText().toString().trim();
	}

	@Override
	public void resetFlopData() {
		id_start_game.setImageResource(R.drawable.selector_game_flop_start);
		isSendFlop = false;// 设置可以再次发牌
	}

	/**
	 * 解析json获取中奖结果
	 * 
	 * @param json
	 */
	private void getResultJson(String json) {
		try {
			JSONObject object = new JSONObject(json);
			String resCode = object.optString("resCode", "");
			// win==0的时候 为空object.getString("winAmount");
			if (resCode.equals("0")) {
				App.userInfo.setBetAcnt(object.getString("betAcnt"));
				App.userInfo.setIntegralAcnt(object.getString("integralAcnt"));
				UserLogic.getInstance().saveUserInfo(App.userInfo);
				setLemi();
				// 是否中奖
				isWin = object.getString("isWin");
				if (isWin.equals("1")) {
					mContinueWinNum++;
					String money = object.optString("winAmount", "0");
					mResultDesc = "获得：" + (Integer.parseInt(money)*2) + "乐米";
				} else {
					mContinueWinNum = 0;
					// mResultDesc = "乐米数减少"
					// + id_lemi_edit.getText().toString().trim();
					mResultDesc = "再来一次吧？";
				}
				// 翻牌结果
				mLotterResult = object.getString("lotteryResult");
				mTopN0.setText(mLotterResult);
				mBottomNO.setText(mLotterResult);
			} else if (resCode.equals("3002")) {

				load();
			} else {
				ToastUtil.shortToast(mActivity, object.getString("resMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 登陆超时,重新登陆
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
									sendRequest();
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
									ToastUtil.shortToast(GameFlopActivity.this,
											json);
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
								sendRequest();
							}
						}

						@Override
						public void loginFailure(final String error) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.shortToast(GameFlopActivity.this,
											error);
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

	/** 翻牌展示结果标题 */
	private String mResultTitle;
	/** 翻牌展示结果描述 */
	private String mResultDesc;
	/** 翻牌开奖结果 */
	private String mLotterResult;
	/** 是否中奖 */
	private String isWin = "0";
	/** 连续中奖次数 */
	@SuppressWarnings("unused")
	private int mContinueWinNum = 0;
	/** 是否可以开始动画 */
	private boolean isCanGoAnimation = false;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (id_guide_layout != null)
				return false;
			if (id_flop_result_layout != null
					&& id_flop_result_layout.getVisibility() == View.VISIBLE) {
				// 翻牌结果已经显示,等同于点击确定按钮
				onClick(id_toast_ok);
			} else {
				finish();
				return false;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 接口回调 用于微信朋友圈的分享
	 */
	private CallBack mBack = new CallBack() {

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
						setLemi();
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
	 * 分享按钮点击的回调
	 */
	@Override
	public void PopShare(int type) {
		mType = type;

		shareUtils.initApi(GameFlopActivity.this);
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
