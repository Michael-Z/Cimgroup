package cn.com.cimgroup.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * @author zhangjf 2014-09-27
 */
public class DragLayout extends FrameLayout {

	public static final int PART_1 = 1;
	public static final int PART_2 = 2;
	public static final int STATE_NONE = 0;
	public static final int STATE_OPEN_1 = 1;
	public static final int STATE_OPEN_2 = 2;
	private View mBackView;
	private View mFrontView;
	private ViewDragHelper mDragHelper;
	private int mFrontTop;
	private int mReleasedTop;
	private int mState;
	private boolean mClampPosition;
	private float mDownY;
	private int mScaledTouchSlop;
	private boolean isDrag;
	private boolean isCanDrag = true;
	private int mActionId;

	public DragLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
		mScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		setBackgroundColor(Color.WHITE);
	}

	public DragLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DragLayout(Context context) {
		this(context, null);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		mBackView = getChildAt(0);
		mFrontView = getChildAt(1);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	class DragHelperCallback extends ViewDragHelper.Callback {

		public int clampViewPositionVertical(View child, int top, int dy) {
			// if (mFrontTop > mBackView.getHeight()) {
			// return mBackView.getHeight();
			// }
			mClampPosition = true;
//			return top > 0 ? top : 0;
			int h = mBackView.getHeight();
			return top > 0 ? (top > h ? h : top) : 0;
		};

		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return child == mFrontView;
		}

		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			mClampPosition = false;
			mReleasedTop = mFrontTop;
			if (mFrontTop > 30) {
				if (mState == STATE_NONE) {
					if (mFrontTop > mBackView.getHeight() / 4) {
						open(PART_2);
					} else {
						open(PART_1);
					}
				} else if (mState == STATE_OPEN_1) {
					if (mFrontTop > mBackView.getHeight() / 2) {
						open(PART_2);
					} else if (mFrontTop < mBackView.getHeight() / 2 - mBackView.getHeight() / 8) {
						close(PART_1);
					} else {
						open(PART_1);
					}
				} else {
					if (mFrontTop < mBackView.getHeight() / 8) {
						close(PART_1);
					} else if (mFrontTop < mBackView.getHeight() - (mBackView.getHeight() / 8)) {
						close(PART_2);
					} else {
						open(PART_2);
					}
				}
			} else {
				mState = STATE_NONE;
			}
		}

		@Override
		public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
			mFrontTop = top;
		}

		@Override
		public int getViewVerticalDragRange(View child) {
			return super.getViewVerticalDragRange(child);
		}
	};

	public void open(int part) {
//		if (isDrag) {
			switch (part) {
			case PART_1:
				mDragHelper.smoothSlideViewTo(mFrontView, 0, mBackView.getHeight() / 2);
				mState = STATE_OPEN_1;
				break;
			case PART_2:
				mDragHelper.smoothSlideViewTo(mFrontView, 0, mBackView.getHeight());
				mState = STATE_OPEN_2;
				break;
			}
			ViewCompat.postInvalidateOnAnimation(this);
//		}
	}

	public void close(int part) {
//		if (isDrag) {
			switch (part) {
			case PART_1:
				mDragHelper.smoothSlideViewTo(mFrontView, 0, 0);
				mState = STATE_NONE;
				break;
			case PART_2:
				mDragHelper.smoothSlideViewTo(mFrontView, 0, mBackView.getHeight() / 2);
				mState = STATE_OPEN_1;
				break;
			}
			ViewCompat.postInvalidateOnAnimation(this);
//		}
	}

	@Override
	public void computeScroll() {
		if (mDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = MotionEventCompat.getActionMasked(ev);
		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			mDragHelper.cancel();

		}
		return true;
		// return mDragHelper.shouldInterceptTouchEvent(ev);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mFrontView.layout(mFrontView.getLeft(), mFrontTop, mFrontView.getRight(), mFrontTop + mFrontView.getMeasuredHeight());
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mState == STATE_NONE) {
			// if (mClampPosition) {
			// ev.setAction(MotionEvent.ACTION_UP);
			// }
		}
		final int action = MotionEventCompat.getActionMasked(ev);
//		if (ev.getY() < mFrontTop) {
			// TODO 
			ev.offsetLocation(0, mReleasedTop);
//		}
		switch (action) {
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_DOWN:
			mDownY = ev.getY();
			mDragHelper.processTouchEvent(ev);
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isCanDrag || !isCanDrag()) {
				isCanDrag = false;
				break;
			}
			float offsetY = ev.getY() - mDownY;
			if (mState != STATE_NONE) {
				offsetY = Math.abs(offsetY);
			}
			if (!isDrag && offsetY > mScaledTouchSlop) {
				isDrag = true;
			} else {
				isDrag = false;
			}
			mDragHelper.processTouchEvent(ev);
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_UP:
			isDrag = false;
			isCanDrag = true;
			mDragHelper.processTouchEvent(ev);
			break;
		}
		if (ev.getPointerId(ev.getActionIndex()) != mActionId)
			return false;
		if (isCanDrag() && isDrag) {
//			int pointerCount = ev.getPointerCount();
//			if (pointerCount == 1){
//				mDragHelper.processTouchEvent(ev);
				ev.setAction(MotionEvent.ACTION_CANCEL);
				mFrontView.dispatchTouchEvent(ev);
//			}
		} else if (ev.getPointerCount() == 1){
			ev.offsetLocation(0, -mFrontTop -mReleasedTop);
			mFrontView.dispatchTouchEvent(ev);
		}
		return true;
	}

	private boolean isCanDrag() {
		return mFrontView.getScrollY() == 0;
	}
	
	public boolean isOpen () {
		return mState != STATE_NONE;
	}
}
