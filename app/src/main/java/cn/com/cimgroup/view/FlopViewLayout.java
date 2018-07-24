package cn.com.cimgroup.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.com.cimgroup.App;
import cn.com.cimgroup.R;
import cn.com.cimgroup.xutils.SoundUtils;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 棋牌游戏版面
 * 
 * @author 秋风
 * 
 */
public class FlopViewLayout extends RelativeLayout implements OnClickListener {
	/** 是否已经投注结束 */
	private boolean isBetting = false;

	/** 游戏区域宽度 */
	private int mWidth;
	/** 游戏区域的高度 */
	private int mHeight;

	/** 控制第一次测量标识 */
	private boolean isOnce = true;
	/** 游戏面板Margin值 */
	private int mMargin;

	/** 纸牌宽度 */
	private int mFlopWidth;
	/** 纸牌高度 */
	private int mFlopHeight;
	/** 纸牌间距 */
	private int mDistance;
	/** 纸牌行间距 */
	private int mDistanceLine;

	/** 发牌机 */
	private ImageView mSendView;
	/** 默认纸牌数量 */
	private int mFlopCount = 8;
	/** 纸牌行数 */
	private int mLines = 2;

	/** 界面纸牌 */
	private List<ImageView> mListFlops;
	/** 动画纸牌 */
	private List<ImageView> mAnimationFlops;

	/** 纸牌Bitmap */
	private Bitmap mFlopBitmap;

	/** 游戏纸牌背面Bitmap */
	private Bitmap mFlopBackBitmap;

	private AnimationSet animationSet;
	/** 是否播放音效 */
	private boolean mIsOpenSound = true;

	public FlopViewLayout(Context context) {
		this(context, null);
	}

	public FlopViewLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FlopViewLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				3, getResources().getDisplayMetrics());

		mDistanceLine = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 5, getResources()
						.getDisplayMetrics());
		// 初始化翻转动画
		initFlopScaleAnimation();
	}

	/**
	 * 初始化翻转动画
	 */
	private void initFlopScaleAnimation() {
		animationSet = new AnimationSet(true);
		ScaleAnimation mBackAnimation = new ScaleAnimation(1, 0f, 1, 1f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animationSet.addAnimation(mBackAnimation);
		animationSet.setDuration(350);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (isOnce) {
			// 初始化游戏区域宽高
			initFlopAttr();
			// 初始化View布局并展示纸牌区域
			initChildViews();
			isOnce = false;
		}
		setMeasuredDimension(mWidth, mHeight);
	}

	/**
	 * 初始化纸牌属性
	 */
	private void initFlopAttr() {
		mHeight = getMeasuredHeight();
		mWidth = getMeasuredWidth();
		mFlopBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_game_flop_outline);
		mFlopBackBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_game_flop_revers_2);

		mFlopWidth = mFlopBitmap.getWidth();
		mFlopHeight = mFlopBitmap.getHeight();
		mDistance = (int) (mWidth / (mFlopCount / mLines) - mFlopWidth) / 2 - 1;
	}

	/**
	 * 初始化View布局并展示纸牌区域
	 */
	private void initChildViews() {
		// 添加纸牌
		addFlopViews();
		// 添加动画层
		addAnimationLayout();
		// 添加头部发牌机
		addHeaderSendView();
	}

	/**
	 * 添加头部发牌机
	 */
	private void addHeaderSendView() {
		mSendView = new ImageView(getContext());
		LayoutParams lp = new LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_TOP);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		mSendView.setLayoutParams(lp);
		mSendView.setImageResource(R.drawable.e_ui_game_flop_send);
		addView(mSendView);
	}

	/**
	 * 添加扑克牌
	 */
	@SuppressWarnings("ResourceType")
	private void addFlopViews() {
		LayoutParams flp = null;
		mListFlops = new ArrayList<ImageView>();
		for (int i = 0; i < mFlopCount; i++) {
			ImageView iv = new ImageView(getContext());
			iv.setId(i + 10);
			iv.setTag("" + i);
			flp = new LayoutParams(mFlopWidth, mFlopHeight);
			if (i % (mFlopCount / mLines) != 0) {
				// 不是第一列，设置RightOf属性
				flp.addRule(RelativeLayout.RIGHT_OF,
						(mListFlops.get(i - 1).getId()));
				flp.leftMargin = mDistance * 2;
			} else if (i % (mFlopCount / mLines) == 0) {
				// 如果是第一列，设置现对于父布局做停靠
				flp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				flp.leftMargin = mDistance;
			}
			if (i < mFlopCount / mLines)
				// 如果是最后一排
				flp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			else
				// 如果不是最后一排
				flp.addRule(RelativeLayout.ABOVE, i + 10 - mFlopCount / mLines);

			flp.bottomMargin = mDistanceLine;
			iv.setImageBitmap(mFlopBitmap);
			mListFlops.add(iv);
			addView(iv, flp);
		}
	}

	/**
	 * 发牌动画
	 */
	public void runInAnimation() {
		// 添加动画纸牌
		addAnimationViews();
	}

	/** 纸牌动画层 */
	private RelativeLayout mAnimationLayout;

	/**
	 * 添加动画层
	 */
	private void addAnimationLayout() {
		mAnimationLayout = new RelativeLayout(getContext());
		LayoutParams lp = new LayoutParams(mWidth, mHeight);
		mAnimationLayout.setLayoutParams(lp);
		addView(mAnimationLayout);
		mAnimationLayout.setVisibility(View.GONE);
	}

	/**
	 * 添加动画纸牌
	 */
	@SuppressWarnings("ResourceType")
	private void addAnimationViews() {
		mAnimationLayout.setVisibility(View.VISIBLE);
		if (mAnimationFlops == null)
			mAnimationFlops = new ArrayList<ImageView>();
		else
			mAnimationFlops.clear();
		LayoutParams flp = null;
		for (int i = 0; i < mFlopCount; i++) {
			final ImageView iv = new ImageView(getContext());
			iv.setId(i + 100);
			final int index = i;
			iv.setTag("" + i);
			iv.setOnClickListener(this);
			flp = new LayoutParams(mFlopWidth, mFlopHeight);
			flp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			flp.topMargin = mMargin * i - (mFlopBackBitmap.getHeight() / 2);
			iv.setImageBitmap(mFlopBackBitmap);
			mAnimationFlops.add(iv);
			mAnimationLayout.addView(iv, flp);
			ObjectAnimator translationX = ObjectAnimator.ofFloat(iv,
					"translationX", mListFlops.get(i).getLeft()
							- (mWidth - mFlopWidth) / 2);
			ObjectAnimator translationY = ObjectAnimator.ofFloat(iv,
					"translationY", mListFlops.get(i).getTop() - flp.topMargin);
			AnimatorSet set = new AnimatorSet();
			set.setDuration(300);
			set.play(translationX).with(translationY);
			set.setStartDelay(i * 150);
			set.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
					// if (index == 0) {
					// SoundUtils.startSoundFromResource(getContext(),
					// R.raw.card_deal);
					// }
				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// 如果是最后一次发牌动画结束，则将原有的图片都设置为纸牌背景,并且隐藏动画层
					// 移除所有动画图片
					// mAnimationLayout.setVisibility(View.GONE);
					if (index == mFlopCount - 1) {
						removeMoveViews();
					}

				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}
			});
			set.start();
		}
		if (mIsOpenSound) {
			new Thread() {
				public void run() {
					for (int j = 0; j < mFlopCount; j++) {
						mHandler.sendEmptyMessageDelayed(PLAY_SOUND, j * 150);
					}
				};
			}.start();
		}

	}

	private int mFlopNum = 0;

	private void startSendFlopAnimation() {
		int rowNum = mFlopCount / mLines;
		int lineNum = 0;
		for (int i = 0; i < mFlopCount; i++) {
			final int index = i;
			lineNum = i / rowNum;
			mFlopNum = mFlopCount - lineNum * rowNum - rowNum + i % rowNum;
//			Log.e("qiufeng", "lineNum:" + lineNum
//					+ "____mFlopCount-lineNum*rowNum-rowNum+lineNum:第"
//					+ mFlopNum + "张牌");
			ImageView iv = mAnimationFlops.get(mFlopNum);

			LayoutParams flp = (LayoutParams) iv.getLayoutParams();
			ObjectAnimator translationX = ObjectAnimator.ofFloat(iv,
					"translationX", mListFlops.get(mFlopNum).getLeft()
							- (mWidth - mFlopWidth) / 2);
			ObjectAnimator translationY = ObjectAnimator.ofFloat(iv,
					"translationY", mListFlops.get(mFlopNum).getTop()
							- flp.topMargin);
			AnimatorSet set = new AnimatorSet();
			set.setDuration(300);
			set.play(translationX).with(translationY);
			set.setStartDelay(i * 150);
			set.addListener(new AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
					// if (index == 0) {
					// SoundUtils.startSoundFromResource(getContext(),
					// R.raw.card_deal);
					// }
				}

				@Override
				public void onAnimationRepeat(Animator animation) {

				}

				@Override
				public void onAnimationEnd(Animator animation) {
					// 如果是最后一次发牌动画结束，则将原有的图片都设置为纸牌背景,并且隐藏动画层
					// 移除所有动画图片
					// mAnimationLayout.setVisibility(View.GONE);
					if (index == mFlopCount - 1) {
						removeMoveViews();
					}

				}

				@Override
				public void onAnimationCancel(Animator animation) {

				}
			});
			set.start();
		}
		new Thread() {
			public void run() {
				for (int j = 0; j < mFlopCount; j++) {
					mHandler.sendEmptyMessageDelayed(PLAY_SOUND, j * 150);
				}
			};
		}.start();

	}

	private final static int PLAY_SOUND = 0;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PLAY_SOUND:
				// SoundUtils.playSound(getContext(), R.raw.card_send);
				// SoundUtils.playSound(2);
				SoundUtils
						.startSoundFromResource(getContext(), R.raw.card_send);
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 移除所有动画图片
	 */
	private void removeMoveViews() {
		for (int i = 0; i < mFlopCount; i++) {
			removeView(mAnimationFlops.get(i));
		}

	}

	/**
	 * 纸牌移除动画并删除
	 */
	public void removeAnimationViews() {

		for (int i = 0; i < mFlopCount; i++) {
			ImageView iIv = mAnimationFlops.get(i);
			if (mChooseFlop.equals(iIv)) {
				continue;
			}
			TranslateAnimation animation = new TranslateAnimation(0, mWidth, 0,
					mHeight);
			animation.setDuration(300);
			animation.setFillAfter(true);
			animation.setStartOffset(i * 100);
			// iIv.setImageBitmap(mFlopBitmap);
			if (i == mFlopCount - 1) {
				animation.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {

					}

					@Override
					public void onAnimationRepeat(Animation animation) {

					}

					@Override
					public void onAnimationEnd(Animation animation) {
						removeMoveFlops();
						if (mFlopGameListener!=null) {
							mFlopGameListener.resetFlopData();
						}
					}
				});
			}
			iIv.startAnimation(animation);
		}
	}

	/**
	 * 移除位移控件
	 */
	private void removeMoveFlops() {
		this.removeAllViews();
		initChildViews();
	}

	/**
	 * 获取多个参数的最小值
	 */
	@SuppressWarnings("unused")
	private int min(int... params) {
		int min = params[0];

		for (int param : params) {
			if (param < min)
				min = param;
		}
		return min;
	}

	private ImageView mChooseFlop = null;

	@Override
	public void onClick(final View v) {
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
			if (mFlopGameListener != null) {
				if (!isBetting) {
					ToastUtil.shortToast(getContext(), "请先选择投注方式");
				} else if (!mFlopGameListener.isBettingLemi()) {
					ToastUtil.shortToast(getContext(), "请输入投注金额");
				} else if (Integer
						.parseInt(mFlopGameListener.getBettingCount()) < 10) {
					ToastUtil.shortToast(getContext(), "最小投注金额为10乐米");
				} else if (BigDecimal
						.valueOf(
								Double.parseDouble(mFlopGameListener
										.getBettingCount())).compareTo(
								BigDecimal.valueOf(Double
										.parseDouble(App.userInfo
												.getIntegralAcnt()))) == 1) {
					ToastUtil.shortToast(getContext(), "乐米不足，点击右上角快速购买");
				} else {
					// 触发投注请求
					mFlopGameListener.sendBettingRequest();
					// 触发监听，发送翻牌请求
					Object tag = v.getTag();
					for (int i = 0; i < mFlopCount; i++) {
						if (mListFlops.get(i).getTag().equals(tag)) {
							mFlopGameListener.startFlopAnimation(mListFlops
									.get(i).getLeft(), mListFlops.get(i)
									.getTop());
							break;
						}
					}
					v.setVisibility(View.GONE);
					mChooseFlop = (ImageView) v;
				}
			}
		} else
			// 用户未登陆 提示用户去登陆
			mFlopGameListener.toastUserLoad();

	}

	/** 游戏监听器 */
	private FlopGameListener mFlopGameListener;

	public void setOnFlopGameListener(FlopGameListener mFlopGameListener) {
		this.mFlopGameListener = mFlopGameListener;
	}

	/**
	 * 游戏监听
	 * 
	 * @author 秋风
	 * 
	 */
	public interface FlopGameListener {
		/**
		 * 开始通知布局加载纸牌翻转动画
		 * 
		 * @param left控件距屏幕左侧距离
		 * @param top控件距屏幕顶端位置
		 */
		void startFlopAnimation(int left, int top);

		/**
		 * 投注开始前确认是否已经投注了乐米
		 * 
		 * @return
		 */
		boolean isBettingLemi();

		/**
		 * 发送投注
		 */
		void sendBettingRequest();

		void toastUserLoad();

		String getBettingCount();
		
		void resetFlopData();
	}

	/**
	 * 更改投注类型
	 * 
	 * @param type
	 */
	public void setBettingType(boolean type) {
		isBetting = type;
	}

	/**
	 * 设置播放音效开关
	 * 
	 * @param isOpenSound
	 */
	public void setSoundOpen(boolean isOpenSound) {
		this.mIsOpenSound = isOpenSound;

	}
}
