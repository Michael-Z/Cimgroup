package cn.com.cimgroup.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.cimgroup.R;

/**
 * 
 * @author 秋风 自定义Listview　下拉刷新,点击加载更多
 */
@SuppressLint({ "ClickableViewAccessibility", "InflateParams" })
public class AutoListView extends ListView implements OnScrollListener {

	// 区分当前操作是刷新还是加载
	public static final int REFRESH = 0;
	public static final int LOAD = 1;

	// 区分PULL和RELEASE的距离的大小
	private static final int SPACE = 20;

	// 定义header的四种状态和当前状态
	/** 默认状态 */
	private static final int NONE = 0;
	/** 向下拉伸 */
	private static final int PULL = 1;
	/** 向上收起 */
	private static final int RELEASE = 2;
	/** 刷新状态 */
	private static final int REFRESHING = 3;
	private int state;

	private LayoutInflater inflater;
	private View e_ui_header;
	private View e_ui_footer;
	private TextView id_header_tip;
	// private TextView id_header_last_update;
	private ImageView id_header_arrow;

	private ImageView id_header_refreshing;

	private TextView id_footer_desc;
	private ImageView id_footer_loading_icon;

	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;
	/** 底部加载跟多的旋转动画 */
	private RotateAnimation mRotateAnimation;

	private int startY;

	private int firstVisibleItem;
	private int scrollState;
	private int headerContentInitialHeight;
	private int headerContentHeight;

	// 只有在listview第一个item显示的时候（listview滑到了顶部）才进行下拉刷新， 否则此时的下拉只是滑动listview
	private boolean isRecorded;
	private boolean isLoading;// 判断是否正在加载
	private boolean loadEnable = true;// 开启或者关闭加载更多功能
	private boolean isLoadFull;
	private int pageSize = 10;

	private OnRefreshListener onRefreshListener;
	private OnLoadListener onLoadListener;

	public AutoListView(Context context) {
		super(context);
		initView(context);
	}

	public AutoListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public AutoListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	// 下拉刷新监听
	public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
		this.onRefreshListener = onRefreshListener;
	}

	// 加载更多监听
	public void setOnLoadListener(OnLoadListener onLoadListener) {
		this.loadEnable = true;
		this.onLoadListener = onLoadListener;
	}

	public boolean isLoadEnable() {
		return loadEnable;
	}

	// 这里的开启或者关闭加载更多，并不支持动态调整
	public void setLoadEnable(boolean loadEnable) {
		this.loadEnable = loadEnable;
		this.removeFooterView(e_ui_footer);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	// 初始化组件
	private void initView(Context context) {

		// 设置箭头特效
		animation = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(500);
		animation.setRepeatCount(-1);
		animation.setFillAfter(true);

		reverseAnimation = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setRepeatCount(-1);
		reverseAnimation.setDuration(500);
		reverseAnimation.setFillAfter(true);

		mRotateAnimation = new RotateAnimation(0, 359,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setInterpolator(new LinearInterpolator());
		mRotateAnimation.setDuration(800);
		mRotateAnimation.setRepeatCount(-1);

		inflater = LayoutInflater.from(context);

		e_ui_header = inflater.inflate(R.layout.e_ui_header, null);
		// id_header_arrow = (ImageView) e_ui_header
		// .findViewById(R.id.id_header_arrow);
		id_header_tip = (TextView) e_ui_header.findViewById(R.id.id_header_tip);
		// id_header_last_update = (TextView) e_ui_header
		// .findViewById(R.id.id_header_last_update);

		id_header_refreshing = (ImageView) e_ui_header
				.findViewById(R.id.id_header_refreshing);

		e_ui_footer = inflater.inflate(R.layout.e_ui_footer, null);
		id_footer_desc = (TextView) e_ui_footer
				.findViewById(R.id.id_footer_desc);
		id_footer_loading_icon = (ImageView) e_ui_footer
				.findViewById(R.id.id_footer_loading_icon);
		// 为listview添加头部和尾部，并进行初始化
		headerContentInitialHeight = e_ui_header.getPaddingTop();
		measureView(e_ui_header);
		headerContentHeight = e_ui_header.getMeasuredHeight();
		topPadding(-headerContentHeight);
		this.addHeaderView(e_ui_header);
		this.addFooterView(e_ui_footer);
		this.setOnScrollListener(this);

		id_footer_loading_icon.setVisibility(View.GONE);

		e_ui_footer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onLoad();
			}
		});
	}

	public void onRefresh() {
		if (onRefreshListener != null) {
			onRefreshListener.onRefresh();
		}
	}

	public void onLoad() {
		if (onLoadListener != null) {
			onLoadListener.onLoad();
			id_footer_loading_icon.startAnimation(mRotateAnimation);
			id_footer_desc.setText("加载中...");
			id_footer_loading_icon.setVisibility(View.VISIBLE);
		}
	}

	public void onRefreshComplete(String updateTime) {
		// id_header_last_update.setText(this.getContext().getString(
		// R.string.lastUpdateTime, Utils.getCurrentTime()));

	}

	// 用于下拉刷新结束后的回调
	public void onRefreshComplete() {
		state = NONE;
		refreshHeaderViewByState();
		// String currentTime = Utils.getCurrentTime();
		// onRefreshComplete(currentTime);
	}

	// 用于加载更多结束后的回调
	public void onLoadComplete() {
		isLoading = false;
		id_footer_desc.setText("点击加载更多");
		id_footer_loading_icon.clearAnimation();
		id_footer_loading_icon.setVisibility(View.GONE);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.firstVisibleItem = firstVisibleItem;
	}
	
	/**
	 * 设置ListView底部是否显示
	 * @param state
	 */
	public void setFooterState(int state){
		e_ui_footer.setVisibility(state);
		if (state==View.VISIBLE) {
			if (getFooterViewsCount()==0) {
				addFooterView(e_ui_footer);
			}
			
//			e_ui_footer.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					onLoad();
//				}
//			});
		}else {
			removeFooterView(e_ui_footer);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.scrollState = scrollState;
		// ifNeedLoad(view, scrollState);
	}

	// 根据listview滑动的状态判断是否需要加载更多
	private void ifNeedLoad(AbsListView view, int scrollState) {
		if (!loadEnable) {
			return;
		}
		try {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					&& !isLoading
					&& view.getLastVisiblePosition() == view
							.getPositionForView(e_ui_footer) && !isLoadFull) {
				onLoad();
				isLoading = true;
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 监听触摸事件，解读手势
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (firstVisibleItem == 0) {
				isRecorded = true;
				startY = (int) ev.getY();
			}
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (state == PULL) {
				state = NONE;
				refreshHeaderViewByState();
			} else if (state == RELEASE) {
				state = REFRESHING;
				refreshHeaderViewByState();
				onRefresh();
			}
			isRecorded = false;
			break;
		case MotionEvent.ACTION_MOVE:
			whenMove(ev);
			break;
		}
		return super.onTouchEvent(ev);
	}

	/***/
	private int tmpY = 0;

	private int mSpace = 0;

	private int mTopPadding = 0;

	// 解读手势，刷新header状态
	private void whenMove(MotionEvent ev) {
		if (!isRecorded) {
			return;
		}
		tmpY = (int) ev.getY();
		mSpace = (tmpY - startY) / 2;
		mTopPadding = mSpace - headerContentHeight;
		switch (state) {
		case NONE:
			if (mSpace > 0) {
				state = PULL;
				refreshHeaderViewByState();
			}
			break;
		case PULL:
			topPadding(mTopPadding);
			if (scrollState == SCROLL_STATE_TOUCH_SCROLL
					&& mSpace > headerContentHeight + SPACE) {
				state = RELEASE;
				refreshHeaderViewByState();
			}
			break;
		case RELEASE:
			topPadding(mTopPadding);
			if (mSpace > 0 && mSpace < headerContentHeight + SPACE) {
				state = PULL;
				refreshHeaderViewByState();
			} else if (mSpace <= 0) {
				state = NONE;
				refreshHeaderViewByState();
			}
			break;
		}

	}

	// 调整header的大小。其实调整的只是距离顶部的高度。
	private void topPadding(int topPadding) {
		e_ui_header.setPadding(e_ui_header.getPaddingLeft(), topPadding,
				e_ui_header.getPaddingRight(), e_ui_header.getPaddingBottom());
		e_ui_header.invalidate();
	}

	/**
	 * 这个方法是根据结果的大小来决定footer显示的。
	 * <p>
	 * 这里假定每次请求的条数为10。如果请求到了10条。则认为还有数据。如过结果不足10条，则认为数据已经全部加载，这时footer显示已经全部加载
	 * </p>
	 * 
	 * @param resultSize
	 */
	public void setResultSize(int resultSize) {
		if (resultSize == 0) {
			isLoadFull = true;
			id_footer_desc.setVisibility(View.GONE);
		} else if (resultSize > 0 && resultSize < pageSize) {
			isLoadFull = true;
			id_footer_desc.setVisibility(View.GONE);
		} else if (resultSize == pageSize) {
			isLoadFull = false;
			id_footer_desc.setVisibility(View.VISIBLE);
		}
	}

	// 根据当前状态，调整header
	private void refreshHeaderViewByState() {
		switch (state) {
		case NONE:
			topPadding(-headerContentHeight);
			id_header_tip.setText("下拉刷新...");
			// id_header_refreshing.setVisibility(View.GONE);
			id_header_refreshing.clearAnimation();
			// id_header_arrow.clearAnimation();
			// id_header_arrow.setImageResource(R.drawable.pull_to_refresh_arrow);
			break;
		case PULL:
			// id_header_arrow.setVisibility(View.VISIBLE);
			id_header_tip.setVisibility(View.VISIBLE);
			// id_header_refreshing.setVisibility(View.GONE);
			// id_header_refreshing.clearAnimation();
			id_header_refreshing.startAnimation(mRotateAnimation);
			id_header_tip.setText("下拉刷新...");
			// id_header_arrow.clearAnimation();
			// id_header_arrow.setAnimation(reverseAnimation);
			break;
		case RELEASE:
			// id_header_arrow.setVisibility(View.VISIBLE);
			id_header_tip.setVisibility(View.VISIBLE);
			// id_header_refreshing.setVisibility(View.GONE);
			// id_header_refreshing.clearAnimation();
			// id_header_refreshing.startAnimation(reverseAnimation);
			id_header_tip.setText("放开可以刷新...");
			// id_header_arrow.clearAnimation();
			// id_header_arrow.setAnimation(animation);
			break;
		case REFRESHING:
			topPadding(headerContentInitialHeight);
			id_header_refreshing.setVisibility(View.VISIBLE);
			// id_header_refreshing.clearAnimation();
			// id_header_refreshing.startAnimation(mRotateAnimation);
			// id_header_arrow.clearAnimation();
			id_header_tip.setText("正在加载中...");
			// id_header_arrow.setVisibility(View.GONE);
			break;
		}
	}

	// 用来计算header大小的。比较隐晦。因为header的初始高度就是0,貌似可以不用。
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	/*
	 * 定义下拉刷新接口
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}

	/*
	 * 定义加载更多接口
	 */
	public interface OnLoadListener {
		public void onLoad();
	}

}
