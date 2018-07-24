package cn.com.cimgroup.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * 滚动
 * @author 秋风
 *
 */
public class MoveScrollView extends ScrollView {
	private OnScrollListener onScrollListener;
	/**
	 * 主要是用在用户手指离开MyScrollView，MyScrollView还在继续滑动，我们用来保存Y的距离，然后做比较
	 */
	private int lastScrollY;

	public MoveScrollView(Context context) {
		this(context, null);
	}

	public MoveScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MoveScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 设置滚动接口
	 * 
	 * @param onScrollListener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.onScrollListener = onScrollListener;
	}


	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		onScrollListener.onScroll(t);
		super.onScrollChanged(l, t, oldl, oldt);
	}

	/**
	 * 
	 * 滚动的回调接口
	 * 
	 * @author xiaanming
	 * 
	 */
	public interface OnScrollListener {
		/**
		 * 回调方法， 返回MyScrollView滑动的Y方向距离
		 * 
		 * @param scrollY
		 *            、
		 */
		public void onScroll(int scrollY);
	}

}
