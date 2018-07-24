package cn.com.cimgroup.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

import cn.com.cimgroup.R;
import cn.com.cimgroup.xutils.SoundUtils;

public class LuckyPanRelativeLayout extends RelativeLayout implements
		AnimationListener {

	private static final int LIGHT_SHINE = 0;

	private static final int LIGHT_SHINING_SINGLE = 1;
	private static final int LIGHT_SHINING_DOUBLE = 2;

	/** 全局上下文 **/
	private Context mContext;

	/** 整个自定义控件的中心位置 **/
	private int mCenter;
	/** 中心X方向长度 **/
	private float mCenterW;
	/** 中心Y方向长度 **/
	private float mCenterH;
	/** 自定义控件的padding值 **/
	private int mPadding;
	/** 自定义控件整体宽度 **/
	private int mWidth;
	/** 自定义控件整体高度 **/
	private int mHeight;
	/** 转盘外围层背景（边缘+灯） **/
	private Bitmap mLuckyPanBgO;
	/** 转盘中间层背景（转动的盘块） **/
	private Bitmap mLuckyPanBgM;
	/** 转盘内层背景（指针+灯） **/
	private Bitmap mLuckyPanBgI;
	/** 转盘外层灯泡-亮 **/
	private Bitmap mLampBitmapO_on;
	private Bitmap mLampBitmapO_off;
	/** 转盘内层灯泡 **/
	private Bitmap mLampBitmapI_on;
	private Bitmap mLampBitmapI_off;
	/** 图片的集和 **/
	private int mImages[] = new int[] { R.drawable.luckypan_iv_bg_o,
			R.drawable.luckypan_iv_bg_m, R.drawable.luckypan_iv_bg_i,
			R.drawable.light_outside_bright, R.drawable.light_inside_bright,
			R.drawable.light_outside_dark, R.drawable.light_inside_dark };
	/** 图片的bitmap集 **/
	private Bitmap mBitmaps[] = new Bitmap[7];
	/** 外围背景的控件 **/
	private ImageView mImageViewO;
	/** 中间层背景的控件 **/
	private ImageView mImageViewM;
	/** 内层背景的控件 **/
	private ImageView mImageViewI;
	/** 设置动画层 **/
	private RelativeLayout mAnimationLayout;
	/** 盘块的转动动画 **/
	private RotateAnimation mAnimation;
	/** 盘块停止前总共旋转的角度 **/
	private float rotatedAngle;
	/** 盘块的总数 **/
	private int mItemCount = 10;
	/** 旋转动画结束的监听 **/
	private AnimationFinished mListener;
	/** 转动完成后停留的角度 **/
	private float stopAngle;
	/** 中心便宜角度 **/
	private float centerOffsetAngle = 360f;
	/** 外层灯泡的个数 **/
	private int lampNumberO = 20;
	/** 内层灯泡的个数 **/
	private int lampNumberI = 10;
	/** 外层背景的半径 **/
	private float mRadiusO;
	/** 中间层背景的半径 **/
	private float mRadiusM;
	/** 顶部的距离 **/
	private float topMargin;

	/** 指针宽度 **/
	private float indicatorWidth;
	/** 指针高度 **/
	private float indicatorHeight;

	/** 灯的宽高 **/
	private int lightHeight;
	/** 状态为-灯有没有闪烁 **/
	private boolean isShining = true;
	/** 参数-外层灯泡的控件集和 **/
	private ImageView mImageView[] = new ImageView[lampNumberO];

	/** 转动的时间 **/
	private int rollingMills = Integer.MAX_VALUE;
	/** 灯泡暗下去的动画效果 **/
	private Animation mDyingAnimation;
	/** 灯泡亮起来的动画效果 **/
	private Animation mShiningAnimation;
	/** 单双数灯泡的计数器**/
	private int singleOrDouble = 1;
	private int postTimeDelay = 200;
	/** 整体自定义控件的背景**/
	private RelativeLayout rLayout;
	/** 整体添加scrollView 保证在华为手机上不会压缩转盘图片**/
//	private ScrollView mScrollView;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {

			// 这个是灯泡同时闪烁的效果 点击开始后闪烁
			case LIGHT_SHINE:
				// 灯泡闪烁效果
				if (rollingMills > 0) {
					rollingMills -= 500;
					// 如果正在闪烁
					if (isShining) {
						// 开始灯暗的动画
						for (int j = 0; j < lampNumberO; j++) {
							ImageView imageView = mImageView[j];
							imageView.startAnimation(mDyingAnimation);

						}
						isShining = false;
					} else {
						// 开始灯灭的动画
						for (int j = 0; j < lampNumberO; j++) {
							ImageView imageView = mImageView[j];
							imageView.startAnimation(mShiningAnimation);
						}
						isShining = true;
					}
					// 延时消息传递 时间和动画时间设置相同
					if (handler != null) {
						handler.sendEmptyMessageDelayed(LIGHT_SHINE, 500);
					}

				} else {
					// 转盘完成 初始化灯泡状态和时间
					rollingMills = 4000;
					for (int i = 0; i < lampNumberO; i++) {
						ImageView imageView = mImageView[i];
						// imageView.setImageResource(0);
						imageView.setImageResource(mImages[3]);
					}
					isShining = true;
				}

				break;

			case LIGHT_SHINING_SINGLE:

				// 如果正在闪烁
				if (isShining) {
					// 开始灯暗的动画
					for (int k = 1; k < lampNumberO; k += 2) {
						ImageView imageView = mImageView[k];
						imageView.startAnimation(mDyingAnimation);

					}

					isShining = false;
				} else {
					// 开始灯灭的动画
					for (int k = 1; k < lampNumberO; k += 2) {
						ImageView imageView = mImageView[k];
						imageView.startAnimation(mShiningAnimation);
					}
					isShining = true;
				}
				// 延时消息传递 时间和动画时间设置相同
				if(singleOrDouble % 2 ==0){
					singleOrDouble++;
					if (handler != null) {
						handler.sendEmptyMessageDelayed(LIGHT_SHINING_DOUBLE, postTimeDelay);
					}
				}else {
					singleOrDouble++;
					if (handler != null) {
						handler.sendEmptyMessageDelayed(LIGHT_SHINING_SINGLE, postTimeDelay);
					}
				}

				break;

			case LIGHT_SHINING_DOUBLE:

				// 如果正在闪烁
				if (isShining) {
					// 开始灯暗的动画
					 for (int j = 0; j < lampNumberO; j+=2) {
					 ImageView imageView = mImageView[j];
					 imageView.startAnimation(mDyingAnimation);
					
					 }

					isShining = false;
				} else {
					// 开始灯灭的动画
					 for (int j = 0; j < lampNumberO; j+=2) {
					 ImageView imageView = mImageView[j];
					 imageView.startAnimation(mShiningAnimation);
					 }
					isShining = true;
				}
				// 延时消息传递 时间和动画时间设置相同
				if(singleOrDouble % 2 ==0){
					singleOrDouble++;
					if (handler != null) {
						handler.sendEmptyMessageDelayed(LIGHT_SHINING_SINGLE, postTimeDelay);
					}
				}else {
					singleOrDouble++;
					if (handler != null) {
						handler.sendEmptyMessageDelayed(LIGHT_SHINING_DOUBLE, postTimeDelay);
					}
				}

				break;
			default:
				break;
			}

		};

	};

	public LuckyPanRelativeLayout(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
	}

	public LuckyPanRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LuckyPanRelativeLayout(Context context) {
		this(context, null);
	}

	private boolean isFirstRun = true;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if (isFirstRun) {
			// 初始化视图的属性
			initViewAttr();
			// 开始添加布局
			addView();
			isFirstRun = false;
		}

		setMeasuredDimension(mWidth, mHeight);
	}

	/**
	 * 初始化参数信息
	 */
	private void initViewAttr() {

		// mWidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		mPadding = max(getPaddingLeft(), getPaddingRight(), getPaddingBottom(),
				getPaddingTop());
		mCenter = mWidth / 2;

		// 将drawable集和转化成对应的bitmap集和
		int size = mImages.length;
		for (int i = 0; i < size; i++) {
			mBitmaps[i] = BitmapFactory.decodeResource(getResources(),
					mImages[i]);
		}

		// 初始化每层背景的bitmap
		mLuckyPanBgO = mBitmaps[0];
		mLuckyPanBgM = mBitmaps[1];
		mLuckyPanBgI = mBitmaps[2];
		mLampBitmapO_on = mBitmaps[3];
		mLampBitmapI_on = mBitmaps[4];
		mLampBitmapO_off = mBitmaps[5];
		mLampBitmapI_off = mBitmaps[6];

		mRadiusO = Math.max(mLuckyPanBgO.getWidth(), mLuckyPanBgO.getHeight()) / 2;
		mRadiusM = Math.max(mLuckyPanBgM.getWidth(), mLuckyPanBgM.getHeight()) / 2;

		topMargin = pxToDip(25);

	}

	// 拿到最小的padding值 以该padding值为准进行计算
	private int max(int... params) {

		int size = params.length;
		int padding = params[0];
		for (int i = 1; i < size; i++) {
			if (padding < params[i]) {
				padding = params[i];
			}
		}

		return padding;
	}

	// 绘制转盘外层背景
	@SuppressWarnings("ResourceType")
	public void drawLuckyPanBgO() {

		mImageViewO = new ImageView(mContext);
		mImageViewO.setId(0);// setid可以在layoutBelow的时候使用

		int height = mLuckyPanBgO.getHeight();
		mCenterH = topMargin + height / 2;
		mCenterW = mWidth / 2;

		LayoutParams rlp = new LayoutParams(
				mLuckyPanBgO.getWidth(), mLuckyPanBgO.getHeight());
		rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlp.topMargin = (int) topMargin;
		mImageViewO.setLayoutParams(rlp);
		mImageViewO.setImageResource(mImages[0]);
		rLayout.addView(mImageViewO);
		// padding的值为空。。。
		// mPaddingLeft = mImageViewO.getPaddingLeft();
		// System.out.println("mpaddingLeft的值为:"+mPaddingLeft);
	}

	// 绘制转盘中间层内容
	@SuppressWarnings("ResourceType")
	public void drawLuckyPanBgM() {
		mImageViewM = new ImageView(mContext);
		mImageViewM.setId(1);

		LayoutParams rlp = new LayoutParams(
				mLuckyPanBgM.getWidth(), mLuckyPanBgM.getHeight());
		rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlp.topMargin = (int) (topMargin + (mRadiusO - mRadiusM));
		mImageViewM.setLayoutParams(rlp);
		mImageViewM.setImageResource(mImages[1]);
		rLayout.addView(mImageViewM);
	}

	// 绘制转盘内层内容
	@SuppressWarnings("ResourceType")
	public void drawLuckyPanBgI() {

		mImageViewI = new ImageView(mContext);
		mImageViewI.setId(2);

		indicatorHeight = mLuckyPanBgI.getHeight();
		indicatorWidth = mLuckyPanBgI.getWidth();

		LayoutParams rlp = new LayoutParams(
				mLuckyPanBgI.getWidth(), mLuckyPanBgI.getHeight());
		rlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rlp.topMargin = (int) (topMargin + mRadiusO
				- (indicatorHeight - indicatorWidth + indicatorWidth / 2) + 5);
		mImageViewI.setLayoutParams(rlp);
		mImageViewI.setImageResource(mImages[2]);
		rLayout.addView(mImageViewI);
	}

	// 绘制外层灯泡
	@SuppressWarnings("ResourceType")
	private void drawLuckyPanLampO() {

		int width = mLampBitmapO_on.getWidth();
		int height = mLampBitmapO_on.getHeight();
		
		float averageAngle = 90f / 4;
		float r = (mRadiusO - mRadiusM) / 2 + mRadiusM;

		// 绘制所有灯泡-外层
		for (int i = 0; i < lampNumberO; i++) {

			ImageView imageView = new ImageView(mContext);
			imageView.setId(100 + i);
			LayoutParams lp = new LayoutParams(width, height);
			lp.topMargin = (int) (mCenterH - r
					* Math.cos(averageAngle * (i + 1) * Math.PI / 180))
					- height / 2;
			lp.leftMargin = (int) (mCenterW + r
					* Math.sin(averageAngle * (i + 1) * Math.PI / 180))
					- width / 2;
			imageView.setLayoutParams(lp);
			imageView.setImageResource(mImages[5]);
			rLayout.addView(imageView);
			mImageView[i] = imageView;
		}

		// 绘制所有灯泡-外层
		for (int i = 0; i < lampNumberO; i++) {

			ImageView imageView = new ImageView(mContext);
			imageView.setId(100 + i);
			LayoutParams lp = new LayoutParams(width, height);
			lp.topMargin = (int) (mCenterH - r
					* Math.cos(averageAngle * (i + 1) * Math.PI / 180))
					- height / 2;
			lp.leftMargin = (int) (mCenterW + r
					* Math.sin(averageAngle * (i + 1) * Math.PI / 180))
					- width / 2;
			imageView.setLayoutParams(lp);
			imageView.setImageResource(mImages[3]);
			rLayout.addView(imageView);
			mImageView[i] = imageView;
		}

	}

	// 绘制内层灯泡
	@SuppressWarnings("ResourceType")
	private void drawLuckyPanLampI() {

		int height = mLampBitmapI_off.getHeight();
		int width = mLampBitmapI_off.getWidth();

		int r = (int) (indicatorWidth / 2 - width / 2);

		float averageAngle = 360f / lampNumberI;

		// 绘制所有灯泡-内层
		for (int i = 0; i < lampNumberI; i++) {

			ImageView imageView = new ImageView(mContext);
			imageView.setId(100 + i);
			LayoutParams lp = new LayoutParams(width, height);
			lp.topMargin = (int) (mCenterH - r
					* Math.cos(averageAngle * (i + 1) * Math.PI / 180))
					- height / 2;
			lp.leftMargin = (int) (mCenterW + r
					* Math.sin(averageAngle * (i + 1) * Math.PI / 180))
					- width / 2;
			imageView.setLayoutParams(lp);
			imageView.setImageResource(mImages[6]);
			rLayout.addView(imageView);
		}

	}

	/**
	 * 添加背景+盘块+指针+内/外层灯泡
	 */
	public void addView() {
		
//		mScrollView = new ScrollView(mContext);
//		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		mScrollView.setLayoutParams(lp);
//		addView(mScrollView);
		
		rLayout = new RelativeLayout(mContext);
		LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		rLayout.setLayoutParams(lParams);
//		mScrollView.addView(rLayout);
		addView(rLayout);

		// 初始化背景的ImageView 并且添加图片
		drawLuckyPanBgO();
		// 初始化盘块的ImageView 并且添加图片
		drawLuckyPanBgM();
		// 初始化指针的ImageView 并且添加图片
		drawLuckyPanBgI();
		// 初始化外层灯泡的ImageView 并且添加图片
		drawLuckyPanLampO();
		// 初始化内层灯泡的ImageView 并且添加图片
		drawLuckyPanLampI();

		initAnimation();
		Message msg = Message.obtain();
		msg.what = LIGHT_SHINING_SINGLE;
		if (handler != null) {
			handler.sendEmptyMessageDelayed(LIGHT_SHINING_SINGLE, postTimeDelay);
		}
	}

	// 初始化转动的动画
	public void initAnimation() {
		/** 设置旋转动画 */
//		mAnimation = new RotateAnimation(0, rotatedAngle,
//				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//				0.5f);
//		mAnimation.setDuration(4000);// 设置动画持续时间
//		// 设置旋转动画的监听事件 在旋转完成后使用自定义的接口回调
//		mAnimation.setAnimationListener(this);

		// 添加灯的闪烁动画
		mDyingAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.anim_light_dying);
		mShiningAnimation = AnimationUtils.loadAnimation(mContext,
				R.anim.anim_light_shining);

	}

	public void setRotatedAngle(int number) {
		// 每个盘块的角度
		float averageAngle = 360f / mItemCount;
		// 随机产生一个角度 是盘块最终停止的位置
		Random random = new Random();
		float angle = random.nextFloat() * averageAngle;
		// 因为转盘有些盘块是歪的 所以强行扣除一定角度 防止停止位置出现问题
		if (angle - 8f < 0) {
			angle = angle + 8f;
		} else if (angle + 8f > averageAngle) {
			angle = angle - 8f;
		}
		// Toast.makeText(mContext, "randomAngle="+angle, 0).show();
		// 每个盘块最左边的边线所指定的角度 以指针所指位置为0°
		// float startAngle = averageAngle * (number - 1);
		float startAngle = averageAngle * (mItemCount - number);
		// 最终的旋转角度 360° * 圈数　+ 转动到指定盘块需要旋转的角度 + 随机的旋转角度（一个范围）
		// rotatedAngle = 360f * 5 + startAngle + angle + (360 -
		// centerOffsetAngle);
		rotatedAngle = 360f * 5 + startAngle + angle;
		// Toast.makeText(mContext, "totalAngle="+rotatedAngle, 1).show();
		stopAngle = startAngle + angle;

	}

	// 旋转动画的监听事件
	@Override
	public void onAnimationStart(Animation animation) {
		// 动画开始
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// 动画结束
		mListener.finish();
		mAnimation.setFillAfter(true);
//		singleOrDouble = 1;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// 动画重复
	}

	/** 旋转动画完成的回调接口 **/
	public interface AnimationFinished {
		public void finish();
	}

	public void setAnimationFinished(AnimationFinished listener) {
		this.mListener = listener;
	}

	/** 初始化转盘位置 **/
	public void resetRollTable() {
		// 开始的时候重置转盘的角度
		// centerOffsetAngle = 360f - stopAngle;
		// mImageViewM.setRotation(centerOffsetAngle);
		mImageViewM.clearAnimation();

	}

	/** 转动开始 **/
	public void startAnimation(int number,boolean soundOn) {
		// 设置转动角度 将转盘停止在参数上
		setRotatedAngle(number);
		// 初始化转动的动画效果
		// 8-26 更改位置 不再是点击后初始化 而是之前就初始化好
//		initAnimation();
		mAnimation = new RotateAnimation(0, rotatedAngle,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mAnimation.setDuration(4000);// 设置动画持续时间
		// 设置旋转动画的监听事件 在旋转完成后使用自定义的接口回调
		mAnimation.setAnimationListener(this);
		mImageViewM.startAnimation(mAnimation);
		if (soundOn) {
			SoundUtils.startSoundFromResource(
					mContext, R.raw.lucky_pan_rolling);
		}
		

		// 转动开始 发送消息 灯泡闪烁
//		Message msg = Message.obtain();
//		msg.what = LIGHT_SHINE;
//		// handler.sendMessageDelayed(msg, 200);
//		handler.sendEmptyMessageDelayed(LIGHT_SHINE, 200);

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

	@Override
	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}
}
