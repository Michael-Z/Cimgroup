package cn.com.cimgroup.animation;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ScrollView;

/**
 * Created by msg on 2016/5/18.
 */
@SuppressLint("HandlerLeak")
public class StretchAnimation {
	private Interpolator mInterpolator; // 插值器
	private View mView; // 你要伸缩的view
	private int mCurrSize; // 当前大小
	private int mRawSize;
	private int mMinSize; // 最小大小 固定值
	private int mMaxSize; // 最大大小 固定值
	private boolean isFinished = true;// 动画结束标识
	private TYPE mType = TYPE.vertical;
	private final static int FRAMTIME = 20;// 一帧的时间 毫秒

	/** 项目需要 滚动到视图底部 */
	private ScrollView mScrollView;

	public static enum TYPE {
		horizontal, // 改变view水平方向的大小
		vertical // 改变view竖直方向的大小
	}

	private int mDuration; // 动画运行的时间
	private long mStartTime;// 动画开始时间
	private float mDurationReciprocal;
	private int mDSize; // 需要改变view大小的增量

	private boolean mIsScrollDown = false;

	public StretchAnimation(View view, TYPE type, int duration) {
		measureView(view);
		mType = type;
		mDuration = duration;
		mIsScrollDown = false;
	}

	/**
	 * 设置是否在动画的时候滚动ScrollView
	 * 
	 * @param isScrool
	 */
	public void setIsScrollDown(boolean isScrool) {
		mIsScrollDown = isScrool;
	}

	/**
	 * 添加滚动监听构造方法
	 * 
	 * @param view
	 * @param type
	 * @param duration
	 * @param scrollView
	 */
	public StretchAnimation(View view, TYPE type, int duration,
			ScrollView scrollView) {
		measureView(view);
		mType = type;
		mDuration = duration;
		mScrollView = scrollView;
		mIsScrollDown = true;
	}

	public void measureView(View childView) {
		LayoutParams p = childView.getLayoutParams();
		if (p == null) {
			p = new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int height = p.height;
		int childHeightSpec;
		if (height > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(height,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		childView.measure(childWidthSpec, childHeightSpec);
		mMinSize = 0;
		mMaxSize = childView.getMeasuredHeight();
	}

	public void setInterpolator(Interpolator interpolator) {
		mInterpolator = interpolator;
	}

	public TYPE getmType() {
		return mType;
	}

	public boolean isFinished() {
		return isFinished;
	}

	public void setDuration(int duration) {
		mDuration = duration;
	}

	public void changeViewSize() {
		if (mView != null && mView.getVisibility() == View.VISIBLE) {
			LayoutParams params = mView.getLayoutParams();
			if (mType == TYPE.vertical) {
				params.height = mCurrSize;
			} else if (mType == TYPE.horizontal) {
				params.width = mCurrSize;
			}
			mView.setLayoutParams(params);

		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			if (msg.what == 1) {
				if (!computeViewSize()) {
					mHandler.sendEmptyMessageDelayed(1, FRAMTIME);
				} else {
					if (animationlistener != null) {
						animationlistener.animationEnd(mView);
						animationlistener.endAnimation();
					}
				}
			}
			super.handleMessage(msg);
		}

	};

	/**
	 * @return 返回true 表示动画完成
	 */
	private boolean computeViewSize() {

		if (isFinished) {
			return isFinished;
		}
		int timePassed = (int) (AnimationUtils.currentAnimationTimeMillis() - mStartTime);

		if (timePassed <= mDuration) {
			float x = timePassed * mDurationReciprocal;
			if (mInterpolator != null) {
				x = mInterpolator.getInterpolation(x);
			}
			mCurrSize = mRawSize + Math.round(x * mDSize);
		} else {
			isFinished = true;
			mCurrSize = mRawSize + mDSize;

		}
		if (mScrollView != null && mIsScrollDown) {
			mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
		}

		mView.setAlpha((float) mCurrSize / (float) mMaxSize);
		changeViewSize();

		return isFinished;
	}

	public void startAnimation(View view) {

		if (view != null) {
			mView = view;
		} else {
			return;
		}
		/** 如果是第一次添加 首先设置视图View.Visible为显示 然后认为设置为隐藏 */
		if (mView.getVisibility() == View.GONE) {
			mView.setVisibility(View.VISIBLE);
			mCurrSize = 0;
			LayoutParams params = mView.getLayoutParams();
			if (mType == TYPE.vertical) {
				params.height = mCurrSize;
			} else if (mType == TYPE.horizontal) {
				params.width = mCurrSize;
			}
			mView.setLayoutParams(params);
		} if (isFinished) {
			mDurationReciprocal = 1.0f / (float) mDuration;
			if (mType == TYPE.vertical) {
				mRawSize = mCurrSize = mView.getMeasuredHeight();
			} else if (mType == TYPE.horizontal) {
				mRawSize = mCurrSize = mView.getWidth();
			}
			if (mCurrSize > mMaxSize || mCurrSize < mMinSize) {
				return;
			}
			isFinished = false;
			mStartTime = AnimationUtils.currentAnimationTimeMillis(); // 动画开始时间
			if (mCurrSize < mMaxSize) {
				mDSize = mMaxSize - mCurrSize;
			} else {
				mDSize = mMinSize - mMaxSize;
			}
			mHandler.sendEmptyMessage(1);

		}
	}

	private AnimationListener animationlistener;

	public interface AnimationListener {
		void animationEnd(View v);

		void endAnimation();
	}

	public void setOnAnimationListener(AnimationListener listener) {
		animationlistener = listener;
	}
}
