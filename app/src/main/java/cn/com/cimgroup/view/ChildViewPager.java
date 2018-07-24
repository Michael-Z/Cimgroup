package cn.com.cimgroup.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class ChildViewPager extends ViewPager {

	public ChildViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ChildViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	/**
	 * 控制父控件与子控件滑动影响
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int curPosition;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(false);
			break;
		case MotionEvent.ACTION_MOVE:
			curPosition = this.getCurrentItem();
			int count = this.getAdapter().getCount();
			if (curPosition == count -1 || curPosition == 0) {
				getParent().requestDisallowInterceptTouchEvent(true);
			}
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

}
