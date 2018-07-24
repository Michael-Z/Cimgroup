package cn.com.cimgroup.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.R;

/**
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright © www.wenchuang.com
 * @Date:2015-1-7
 */
public class Msg_ViewPagerIndicator extends LinearLayout {

	/**
	 * 绘制三角形的画笔
	 */
	private Paint mPaint;
	/**
	 * path构成一个三角形
	 */
	private Path mPath;
	/**
	 * 三角形的宽度
	 */
	private int mTriangleWidth;
	/**
	 * 三角形的高度
	 */
	private int mTriangleHeight = 10;

	/**
	 * 三角形的宽度为单个Tab的
	 */
	private final float RADIO_TRIANGEL = 1.0f;

	/**
	 * 初始时，三角形指示器的偏移量
	 */
	private int mInitTranslationX;
	/**
	 * 手指滑动时的偏移量
	 */
	private float mTranslationX;

	/**
	 * 默认的Tab数量
	 */
	private final int COUNT_DEFAULT_TAB = 2;
	/**
	 * tab数量
	 */
	private int mTabVisibleCount = COUNT_DEFAULT_TAB;

	/**
	 * tab上的内容
	 */
//	private List<String> mTabTitles;
	/**
	 * 与之绑定的ViewPager
	 */
	public ViewPager mViewPager;

	/**
	 * 标题正常时的颜色
	 */
	private int COLOR_TEXT_NORMAL = 0x77FFFFFF;
	/**
	 * 标题选中时的颜色
	 */
	private int COLOR_TEXT_HIGHLIGHTCOLOR = 0xFFFFFFFF;

	/**
	 * 标题正常时背景的颜色
	 */
	private int COLOR_TEXT_BG_NORMAL = 0xFFFFFF;

	/**
	 * 标题选中时背景的颜色
	 */
	private int COLOR_TEXT_BG_HIGHLIGHTCOLOR = 0xFFFFFF;

	private float COLOR_TEXT_SIZE = 16;

	private int COLOR_BOTTOM_LINE = R.color.bg_lotterydraw_tab_line;
	
	private ImageView imageView_msg;
	private ImageView imageView_activity;

	/**
	 * 底部line背景色
	 */
	private int MColor_bottom_line = COLOR_BOTTOM_LINE;

	/** 标题所有View的集合（类型为TextView） **/
	private TextView[] titleTextView;
	/** 标题所有View的集合（类型为RelativeLayout） **/
	private RelativeLayout[] titleRelativeLayout;

	public Msg_ViewPagerIndicator(Context context) {
		this(context, null);
	}

	public Msg_ViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		MColor_bottom_line = getResources().getColor(MColor_bottom_line);
		COLOR_TEXT_NORMAL = getResources().getColor(R.color.color_black);
		COLOR_TEXT_HIGHLIGHTCOLOR = getResources().getColor(
				R.color.bg_lotterydraw_tab_line);
		// 获得自定义属性，tab的数量
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ViewPagerIndicator);
		mTabVisibleCount = a.getInt(R.styleable.ViewPagerIndicator_item_count,
				COUNT_DEFAULT_TAB);
		MColor_bottom_line = a.getColor(
				R.styleable.ViewPagerIndicator_line_color, MColor_bottom_line);
		if (mTabVisibleCount < 0)
			mTabVisibleCount = COUNT_DEFAULT_TAB;
		a.recycle();

		// 初始化画笔
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(MColor_bottom_line);
		mPaint.setStyle(Style.FILL);
		mPaint.setPathEffect(new CornerPathEffect(3));

	}

	/**
	 * 绘制指示器
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		canvas.save();
		// 画笔平移到正确的位置
		canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
		canvas.drawPath(mPath, mPaint);
		canvas.restore();

		super.dispatchDraw(canvas);
	}

	/**
	 * 初始化三角形的宽度
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGEL);// 1/6 of
																		// width

		// 初始化三角形
		initTriangle();

		// 初始时的偏移量
		mInitTranslationX = getWidth() / mTabVisibleCount / 2 - mTriangleWidth
				/ 2;
	}

	/**
	 * 设置可见的tab的数量
	 * 
	 * @param count
	 */
	public void setVisibleTabCount(int count) {
		this.mTabVisibleCount = count;
	}

	/**
	 * 设置title内容
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-2-2
	 */
	public void setTitle(int index, String title) {
		if (titleTextView != null && titleTextView.length > index) {
			titleTextView[index].setText(title);
		}
		if (titleRelativeLayout != null && titleRelativeLayout.length > index) {
			((TextView) (titleRelativeLayout[index].getChildAt(0)))
					.setText(title);
		}
	}

	/**
	 * 设置tab的标题内容 可选，可以自己在布局文件中写死
	 * 
	 * @param datas
	 */
	public void setTabItemTitles(List<String> datas, int type) {
		titleTextView = new TextView[datas.size()];
		titleRelativeLayout = new RelativeLayout[datas.size()];
		// 如果传入的list有值，则移除布局文件中设置的view
		if (datas != null && datas.size() > 0) {
			this.removeAllViews();
//			this.mTabTitles = datas;
			if (type == -1) {
				// type = -1 表示 消息/活动
				for (int i = 0; i < datas.size(); i++) {
					// 添加view
					titleRelativeLayout[i] = generateRelativeLayout(datas
							.get(i));
					// addView(titleTextView[i]=generateTextView(datas.get(i)));
					addView(titleRelativeLayout[i]);
				}
			} else {
				for (int i = 0; i < datas.size(); i++) {
					// 添加view
					// titleRelativeLayout[i] = generateTextView(datas.get(i));
					addView(titleTextView[i] = generateTextView(datas.get(i)));
//					addView(titleRelativeLayout[i]);
				}
			}

			// 设置item的click事件
			setItemClickEvent();
		}
//		setMessageState(0, 0);
	}

	/**
	 * 设置字体颜色、选中、为选中 注意：颜色设置需再setTabItemTitles 方法之前
	 * 
	 * @Description:
	 * @param colors
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-18
	 */
	public void setTabItemTextColor(int colorNor, int colorPress) {
		COLOR_TEXT_NORMAL = getResources().getColor(colorNor);
		COLOR_TEXT_HIGHLIGHTCOLOR = getResources().getColor(colorPress);
	}

	/**
	 * 设置选项卡背景颜色、选中、为选中 注意：颜色设置需再setTabItemTitles 方法之前
	 * 
	 * @Description:
	 * @param colors
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-18
	 */
	public void setTabItemBgColor(int colorNor, int colorPress) {
		COLOR_TEXT_BG_NORMAL = getResources().getColor(colorNor);
		COLOR_TEXT_BG_HIGHLIGHTCOLOR = getResources().getColor(colorPress);
	}

	/**
	 * 设置分割线高度
	 * 
	 * @Description:
	 * @param height
	 * @author:www.wenchuang.com
	 * @date:2015年11月17日
	 */
	public void setIndicatorHeight(int height) {
		mTriangleHeight = height;
	}

	/**
	 * 设置文字大小
	 * 
	 * @Description:
	 * @param size
	 * @author:www.wenchuang.com
	 * @date:2015年11月17日
	 */
	public void setTextSize(float size) {
		COLOR_TEXT_SIZE = size;
	}

	/**
	 * 对外的ViewPager的回调接口
	 * 
	 * @author zhy
	 * 
	 */
	public interface PageChangeListener {

		public void onPageScrolled(int position, float positionOffset,
								   int positionOffsetPixels);

		public void onPageSelected(int position);

		public void onPageScrollStateChanged(int state);
	}

	// 对外的ViewPager的回调接口
	private PageChangeListener onPageChangeListener;

	// 对外的ViewPager的回调接口的设置
	public void setOnPageChangeListener(PageChangeListener pageChangeListener) {
		this.onPageChangeListener = pageChangeListener;
	}

	// 设置关联的ViewPager
	public void setViewPager(ViewPager mViewPager, int pos, final int type) {
		this.mViewPager = mViewPager;

		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 重置文本颜色
				if (type == -1) {
					resetRelativeLayoutColor(titleRelativeLayout);
					highLightRelativeLayout(position, titleRelativeLayout);
				}else {
					resetTextViewColor();
					highLightTextView(position);
				}
				// 回调
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// 滚动
				scroll(position, positionOffset);

				// 回调
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageScrolled(position,
							positionOffset, positionOffsetPixels);
				}

			}

			@Override
			public void onPageScrollStateChanged(int state) {
				// 回调
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageScrollStateChanged(state);
				}

			}
		});
		// 设置当前页
		mViewPager.setCurrentItem(pos);
		// 高亮
		if (type == -1) {
			highLightRelativeLayout(pos, titleRelativeLayout);
		}else {
			highLightTextView(pos);
		}
	}

	/**
	 * 高亮文本
	 * 
	 * @param position
	 */
	protected void highLightTextView(int position) {
		View view = getChildAt(position);
		if (view instanceof TextView) {
			((TextView) view).setTextSize(COLOR_TEXT_SIZE);
			((TextView) view).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
			((TextView) view).setBackgroundColor(COLOR_TEXT_BG_HIGHLIGHTCOLOR);

		}

	}
	
	/**
	 * tab被选中 文本设置为高亮（2.5期滑动删除后添加）
	 * @param position
	 * @param mLayouts
	 */
	protected void highLightRelativeLayout(int position, RelativeLayout[] mLayouts) {
		
		RelativeLayout rl = mLayouts[position];
		((TextView)rl.getChildAt(0)).setTextSize(COLOR_TEXT_SIZE);
		((TextView)rl.getChildAt(0)).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
		((TextView)rl.getChildAt(0)).setBackgroundColor(COLOR_TEXT_BG_HIGHLIGHTCOLOR);
		
	}
	
	/**
	 * 重置文本/文本背景颜色
	 */
	private void resetTextViewColor() {
		for (int i = 0; i < getChildCount(); i++) {
			View view = getChildAt(i);
			if (view instanceof TextView) {
				((TextView) view).setTextSize(COLOR_TEXT_SIZE);
				((TextView) view).setTextColor(COLOR_TEXT_NORMAL);
				((TextView) view).setBackgroundColor(COLOR_TEXT_BG_NORMAL);
			}
		}
	}
	
	/**
	 * 充值文本/文字颜色（2.5期滑动删除后添加）
	 * @param mLayouts
	 */
	private void resetRelativeLayoutColor(RelativeLayout[] mLayouts){
		
		for (int i = 0; i < mLayouts.length; i++) {
			((TextView)mLayouts[i].getChildAt(0)).setTextSize(COLOR_TEXT_SIZE);
			((TextView)mLayouts[i].getChildAt(0)).setTextColor(COLOR_TEXT_NORMAL);
			((TextView)mLayouts[i].getChildAt(0)).setBackgroundColor(COLOR_TEXT_BG_NORMAL);
		}
		
	}
	

	/**
	 * 设置点击事件
	 */
	public void setItemClickEvent() {
		int cCount = getChildCount();
		for (int i = 0; i < cCount; i++) {
			final int j = i;
			View view = getChildAt(i);
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mViewPager.setCurrentItem(j);
				}
			});
		}
	}

	/**
	 * 根据标题生成我们的RelativeLayout
	 * 
	 * @param text
	 * @return
	 */
	private RelativeLayout generateRelativeLayout(String text) {

		RelativeLayout rl = new RelativeLayout(getContext());
		RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		rllp.width = getScreenWidth() / mTabVisibleCount;
		rllp.bottomMargin = mTriangleHeight;
		// 这个颜色会盖掉三角形的颜色 可能是因为绘制先后顺序的问题
		// rl.setBackgroundColor(0xffff9900);//这里的颜色必须是带透明度的 即8位数 这里是橙色
		rl.setLayoutParams(rllp);

		TextView tv = new TextView(getContext());
		// LinearLayout.LayoutParams lp = new
		// LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		lp.bottomMargin = mTriangleHeight;
		lp.width = getScreenWidth() / mTabVisibleCount;
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setBackgroundColor(COLOR_TEXT_BG_NORMAL);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setLayoutParams(lp);

		
		
		ImageView imageView = new ImageView(getContext());
		RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(12, 12);
		imageView.setImageResource(R.drawable.shape_circle_no_border);
		p.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// 具体数值好像和布局文件中的数值不一样
		p.topMargin = dip2px(getContext(), 10);
		p.rightMargin = getScreenWidth() / mTabVisibleCount / 8 * 3;
//		imageView.setVisibility(View.GONE);
		imageView.setLayoutParams(p);
		
		if (text.equals("活动")) {
			imageView_activity = imageView;
		}else if (text.equals("消息")) {
			imageView_msg = imageView;
		}

		rl.addView(tv);
		rl.addView(imageView);
		
		//初始化 将红点去掉
		//以免在网络请求中调用时，取消红点时，会出现红点一闪而过的情况
		setMessageState(0, 1);

		return rl;
		// return tv;

	}

	/**
	 * 根据标题生成我们的TextView
	 * 
	 * @param text
	 * @return
	 */
	private TextView generateTextView(String text) {

		TextView tv = new TextView(getContext());
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.bottomMargin = mTriangleHeight;
		lp.width = getScreenWidth() / mTabVisibleCount;
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setBackgroundColor(COLOR_TEXT_BG_NORMAL);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setLayoutParams(lp);

		return tv;

	}

	/**
	 * 初始化三角形指示器
	 */
	private void initTriangle() {
		mPath = new Path();

		mPath.moveTo(0, 0);
		mPath.lineTo(mTriangleWidth, 0);
		mPath.lineTo(mTriangleWidth, -mTriangleHeight);
		mPath.lineTo(0, -mTriangleHeight);
		mPath.close();
	}

	/**
	 * 指示器跟随手指滚动，以及容器滚动
	 * 
	 * @param position
	 * @param offset
	 */
	public void scroll(int position, float offset) {
		/**
		 * <pre>
		 *  0-1:position=0 ;1-0:postion=0;
		 * </pre>
		 */
		// 不断改变偏移量，invalidate
		mTranslationX = getWidth() / mTabVisibleCount * (position + offset);

		int tabWidth = getScreenWidth() / mTabVisibleCount;

		// 容器滚动，当移动到倒数最后一个的时候，开始滚动
		if (offset > 0 && position >= (mTabVisibleCount - 2)
				&& getChildCount() > mTabVisibleCount) {
			if (mTabVisibleCount != 1) {
				this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth
						+ (int) (tabWidth * offset), 0);
			} else
			// 为count为1时 的特殊处理
			{
				this.scrollTo(position * tabWidth + (int) (tabWidth * offset),
						0);
			}
		}

		invalidate();
	}

	/**
	 * 设置布局中view的一些必要属性；如果设置了setTabTitles，布局中view则无效
	 */
	@Override
	protected void onFinishInflate() {
		Log.e("TAG", "onFinishInflate");
		super.onFinishInflate();

		int cCount = getChildCount();

		if (cCount == 0)
			return;

		for (int i = 0; i < cCount; i++) {
			View view = getChildAt(i);
			LayoutParams lp = (LayoutParams) view
					.getLayoutParams();
			lp.weight = 0;
			lp.width = getScreenWidth() / mTabVisibleCount;
			view.setLayoutParams(lp);
		}
		if (imageView_activity != null) {
		}else {
		}
		if (imageView_msg != null) {
		}else {
		}
		// 设置点击事件
		setItemClickEvent();

	}

	/**
	 * 获得屏幕的宽度
	 * 
	 * @return
	 */
	public int getScreenWidth() {
		WindowManager wm = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	/**
	 * dip转px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
	
	/**
	 * 设置消息状态
	 * @param type 消息类型  0-全部 1-消息 2-活动
	 * @param finish 消息状态 0-设置未读完  1-设置已读完
	 */
	public void setMessageState(int type,int finish){
		
		if (imageView_activity != null && imageView_msg != null) {
			
			if (type == 0 && finish == 0) {
				imageView_msg.setVisibility(View.VISIBLE);
				imageView_activity.setVisibility(View.VISIBLE); 
			}else if (type == 0 && finish == 1) {
				imageView_msg.setVisibility(View.GONE);
				imageView_activity.setVisibility(View.GONE); 
			}else if (type == 1 && finish == 0) {
				imageView_msg.setVisibility(View.VISIBLE);
			}else if (type == 1 && finish == 1){
				imageView_msg.setVisibility(View.GONE);
			}else if (type == 2 && finish == 0){
				imageView_activity.setVisibility(View.VISIBLE);
			}else if (type == 2 && finish == 1){
				imageView_activity.setVisibility(View.GONE);
			}
			
		}
	}

}
