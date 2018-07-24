package cn.com.cimgroup.view;

import java.util.List;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.R;

/**
 * 
 * @Description: is similar to ViewpagerIndicator.java,
 * 				 incase of override or some conflict,
 *               we found a new one just for the scoreList change in v2.6
 * @author:honglin
 * @see:
 * @since:
 * @copyright © www.wenchuang.com
 * @Date:2016-11-23
 */
public class ScoreListIndicator extends LinearLayout {

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
	private int mTriangleHeight = 2;

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
	private List<String> mTabTitles;
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
	private int COLOR_TEXT_BG_NORMAL = 0xffffffff;

	/**
	 * 标题选中时背景的颜色
	 */
	private int COLOR_TEXT_BG_HIGHLIGHTCOLOR = 0xffffffff;
	
	
	private float COLOR_TEXT_SIZE = 16;

	private int COLOR_BOTTOM_LINE = R.color.bg_lotterydraw_tab_line;
	
	private int currentPosition;
	
	/**
	 * 底部line背景色
	 */
	private int MColor_bottom_line = COLOR_BOTTOM_LINE;
	
	private TextView [] titleTextView;
	private LinearLayout[] titleLinearLayoutView;
	/**黑色*/
	private int mColorBlack = 0;
	/**灰色*/
	private int mColorGray = 0;

	public ScoreListIndicator(Context context) {
		this(context, null);
	}

	public ScoreListIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		mColorBlack = context.getResources().getColor(R.color.color_gray_secondary);
		mColorGray = context.getResources().getColor(R.color.color_gray_indicator);
		MColor_bottom_line = getResources().getColor(MColor_bottom_line);
		COLOR_TEXT_NORMAL = getResources().getColor(R.color.color_black);
		COLOR_TEXT_HIGHLIGHTCOLOR = getResources().getColor(R.color.bg_lotterydraw_tab_line);
		// 获得自定义属性，tab的数量
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScoreListIndicator);
		mTabVisibleCount = a.getInt(R.styleable.ViewPagerIndicator_item_count, COUNT_DEFAULT_TAB);
		MColor_bottom_line = a.getColor(R.styleable.ViewPagerIndicator_line_color, MColor_bottom_line);
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
//	@Override
//	protected void dispatchDraw(Canvas canvas) {
//		canvas.save();
//		// 画笔平移到正确的位置
//		canvas.translate(mInitTranslationX + mTranslationX, getHeight() + 1);
//		canvas.drawPath(mPath, mPaint);
//		canvas.restore();
//
//		super.dispatchDraw(canvas);
//	}

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
		mInitTranslationX = getWidth() / mTabVisibleCount / 2 - mTriangleWidth / 2;
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
	 * @Description:
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2015-2-2
	 */
	public void setTitle(int index,String title){
		if(titleTextView!=null && titleTextView.length>index){
			titleTextView[index].setText(title);
		}
		if (titleLinearLayoutView != null && titleLinearLayoutView.length > index) {
			((TextView)titleLinearLayoutView[index].getChildAt(1)).setText(title);
		}
	}
	/**
	 * 设置tab的标题内容 可选，可以自己在布局文件中写死
	 * 
	 * @param datas
	 */
	public void setTabItemTitles(List<String> datas) {
		titleTextView=new TextView[datas.size()];
		titleLinearLayoutView = new LinearLayout[datas.size()];
		// 如果传入的list有值，则移除布局文件中设置的view
		if (datas != null && datas.size() > 0) {
			this.removeAllViews();
			this.mTabTitles = datas;

			for (int i=0; i<datas.size(); i++) {
				// 添加view
				addView(titleLinearLayoutView[i]=generateLinearLayoutView(datas.get(i)));
			}
			// 设置item的click事件
			setItemClickEvent();
		}

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
	 * @Description:
	 * @param height
	 * @author:www.wenchuang.com
	 * @date:2015年11月17日
	 */
	public void setIndicatorHeight(int height){
		mTriangleHeight = height;
	}
	
	/**
	 * 设置文字大小
	 * @Description:
	 * @param size
	 * @author:www.wenchuang.com
	 * @date:2015年11月17日
	 */
	public void setTextSize(float size){
		COLOR_TEXT_SIZE = size;
	}

	/**
	 * 对外的ViewPager的回调接口
	 * 
	 * @author zhy
	 * 
	 */
	public interface PageChangeListener {

		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

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
	public void setViewPager(ViewPager mViewPager, int pos) {
		this.mViewPager = mViewPager;
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// 设置字体颜色高亮
				resetTextViewColor();
//				highLightTextView(position);
//				highLightLinearLayout(position);
				change_title(true, "", false, position);
				currentPosition = position;

				// 回调
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageSelected(position);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				// 滚动
				scroll(position, positionOffset); 

				// 回调
				if (onPageChangeListener != null) {
					onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
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
		highLightTextView(pos);
	}
	
	/**
	 * 拼装title
	 * @param b
	 * @param titleText
	 * @param c
	 */
	private void change_title(boolean b, String titleText, boolean c, int position) {
		

		View view = getChildAt(0);
		
		// 因为tab为relativeLayout中嵌套TextView 那么取出其中的TextView 改变颜色
		if (view instanceof LinearLayout) {
			((TextView) ((LinearLayout) view).getChildAt(1)).setText(mTabTitles.get(position));
			
			if (position == mTabTitles.size()-1) {
				((TextView) ((LinearLayout) view).getChildAt(2)).setTextColor(mColorGray);
				((TextView) ((LinearLayout) view).getChildAt(0)).setTextColor(mColorBlack);
			}else if (position == 0) {
				((TextView) ((LinearLayout) view).getChildAt(2)).setTextColor(mColorBlack);
				((TextView) ((LinearLayout) view).getChildAt(0)).setTextColor(mColorGray);
			}else {
				((TextView) ((LinearLayout) view).getChildAt(2)).setTextColor(mColorBlack);
				((TextView) ((LinearLayout) view).getChildAt(0)).setTextColor(mColorBlack);
			}
		}
		
	};

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
	
	protected void highLightLinearLayout(int position) {
		View view = getChildAt(position);
		if (view instanceof LinearLayout) {
			((TextView)((LinearLayout) view).getChildAt(1)).setTextSize(COLOR_TEXT_SIZE);
			((TextView)((LinearLayout) view).getChildAt(1)).setTextColor(COLOR_TEXT_HIGHLIGHTCOLOR);
			((TextView)((LinearLayout) view).getChildAt(1)).setBackgroundColor(COLOR_TEXT_BG_HIGHLIGHTCOLOR);
		}

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
//					mViewPager.setCurrentItem(j);
				}
			});
		}
	}

	/**
	 * 根据标题生成我们的TextView
	 * 
	 * @param text
	 * @return
	 */
	private LinearLayout generateLinearLayoutView(String text) {
		
		int BasicWidth = getScreenWidth()/mTabVisibleCount;
		
		LinearLayout ll = new LinearLayout(getContext());
		LayoutParams lp_content = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp_content.bottomMargin = mTriangleHeight;
		lp_content.width = BasicWidth;
		ll.setGravity(Gravity.CENTER);
		ll.setOrientation(HORIZONTAL);
		ll.setBackgroundColor(COLOR_TEXT_BG_NORMAL);
		ll.setLayoutParams(lp_content);
		
		TextView tv_left = new TextView(getContext());
		LayoutParams lp_left = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp_left.width = BasicWidth/5 ;
		tv_left.setGravity(Gravity.CENTER);
		tv_left.setTextColor(mColorGray);
		tv_left.setBackgroundColor(COLOR_TEXT_BG_NORMAL);
		tv_left.setText("上一期");
		tv_left.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv_left.setLayoutParams(lp_left);
		
		TextView tv = new TextView(getContext());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.width = BasicWidth/5 * 3;
		tv.setGravity(Gravity.CENTER);
		tv.setTextColor(COLOR_TEXT_NORMAL);
		tv.setBackgroundColor(COLOR_TEXT_BG_NORMAL);
		tv.setText(text);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv.setLayoutParams(lp);
		
		TextView tv_right = new TextView(getContext());
		LayoutParams lp_right = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp_right.width = BasicWidth/5 ;
		tv_right.setGravity(Gravity.CENTER);
		tv_right.setTextColor(COLOR_TEXT_NORMAL);
		tv_right.setBackgroundColor(COLOR_TEXT_BG_NORMAL);
		tv_right.setText("下一期");
		tv_right.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		tv_right.setLayoutParams(lp_right);
		
		ll.addView(tv_left);
		ll.addView(tv);
		ll.addView(tv_right);
		
		tv_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(currentPosition-1);	
			}
		});
		
		tv_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(currentPosition+1);
			}
		});
		
		
		return ll;
	}
	
	/**
	 * 根据标题生成我们的TextView
	 * 
	 * @param text
	 * @return
	 */
	private TextView generateTextViewView(String text) {
		
		int BasicWidth = getScreenWidth()/mTabVisibleCount;
		
		TextView tv = new TextView(getContext());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.bottomMargin = mTriangleHeight;
		lp.width = BasicWidth/5 * 3;
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
		 *  0-1:position=0 ;1-0: =0;
		 * </pre>
		 */
		// 不断改变偏移量，invalidate
		int tabWidth = getWidth() / mTabVisibleCount;
		mTranslationX = (int) (tabWidth * (offset + position));

//		// 区分tabCount为1的情况
//		if (mTabVisibleCount != 1) {
//			// 如果当前位置为
//			if (position >= mTabVisibleCount - 2 && offset > 0
//					&& getChildCount() > mTabVisibleCount
//					&& position != getChildCount() - 2) {
//				//
//				this.scrollTo((position - (mTabVisibleCount - 2)) * tabWidth
//						+ (int) (offset * tabWidth), 0);
//
//			}
//		} else {
//			// this.scrollTo(tabWidth*position + (int)(positionOffset*tabWidth),
//			// 0);
//			this.scrollTo((int)mTranslationX, 0);
//		}

		// 当位置发生变化 重新绘制三角形区域  即 重新调用dispatchDraw()方法
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
			LayoutParams lp = (LayoutParams) view.getLayoutParams();
			lp.weight = 0;
			lp.width = getScreenWidth() / mTabVisibleCount;
			view.setLayoutParams(lp);
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
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

}
