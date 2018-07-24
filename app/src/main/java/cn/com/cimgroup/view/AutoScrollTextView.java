package cn.com.cimgroup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.xutils.XLog;

/**
 * 文字自动垂直轮播
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016-11-30
 */
public class AutoScrollTextView extends TextSwitcher implements ViewSwitcher.ViewFactory {
	private static final int FLAG_START_AUTO_SCROLL = 1001;
    private static final int FLAG_STOP_AUTO_SCROLL = 1002;

    /**
     * 轮播时间间隔
     */
    private int scrollDuration = 3000;
    /**
     * 动画时间
     */
    private int animDuration = 300;

    /**
     * 文字大小
     */
    private int mTextSize = 24;
    /**
     * 文字Padding
     */
    private int mPadding = 20;
    /**
     * 文字颜色
     */
    private int textColor = Color.BLACK;

    private OnItemClickListener itemClickListener;
    private Context mContext;
    /**
     * 当前显示Item的ID
     */
    private int currentId = -1;
    private ArrayList<String> textList;
    private Handler handler;
    
    private boolean isScroll;

    public AutoScrollTextView(Context context) {
        this(context, null);
        mContext = context;
        textColor =mContext.getResources().getColor(R.color.color_gray_secondary);
    }

    public AutoScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        textColor =mContext.getResources().getColor(R.color.color_gray_secondary);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoScrollTextView);
        mTextSize = a.getDimensionPixelSize(R.styleable.AutoScrollTextView_textSize, 24);
        mPadding = (int) a.getDimension(R.styleable.AutoScrollTextView_padding, 10);
        scrollDuration = a.getInteger(R.styleable.AutoScrollTextView_scrollDuration, 3000);
        animDuration = a.getInteger(R.styleable.AutoScrollTextView_animDuration, 300);
        textColor = a.getColor(R.styleable.AutoScrollTextView_textColor, textColor);
        a.recycle();
        init();
    }

    private void init() {
        textList = new ArrayList<String>();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FLAG_START_AUTO_SCROLL:
                        if (textList.size() > 0) {
                            currentId++;
//                            XLog.e("currentId = " + textList.get(currentId % textList.size()));
                            setText(textList.get(currentId % textList.size()));
                        }
                        handler.sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, scrollDuration);
                        break;
                    case FLAG_STOP_AUTO_SCROLL:
                        handler.removeMessages(FLAG_START_AUTO_SCROLL);
                        break;
                }
            }
        };

        setFactory(this);
        Animation in = new TranslateAnimation(0, 0, 300, 0);
        in.setDuration(animDuration);
        in.setInterpolator(new AccelerateInterpolator());
        Animation out = new TranslateAnimation(0, 0, 0, -300);
        out.setDuration(animDuration);
        out.setInterpolator(new AccelerateInterpolator());
        setInAnimation(in);
        setOutAnimation(out);
    }

    /**
     * 设置数据源
     *
     * @param titles
     */
    public void setTextList(List<String> titles) {
        textList.clear();
        textList.addAll(titles);
        currentId = -1;
    }

    /**
     * 开始轮播
     */
    public void startAutoScroll() {
    	isScroll = true;
        handler.sendEmptyMessage(FLAG_START_AUTO_SCROLL);
    }

    /**
     * 停止轮播
     */
    public void stopAutoScroll() {
    	isScroll = false;
        handler.sendEmptyMessage(FLAG_STOP_AUTO_SCROLL);
    }

    public boolean getIsScroll() {
		return isScroll;
	}
    
    @Override
    public View makeView() {
        TextView t = new TextView(mContext);
        t.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        t.setMaxLines(1);
        t.setPadding(mPadding, mPadding, mPadding, mPadding);
        t.setTextColor(textColor);
//        t.setTextSize(mTextSize);
        t.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);

        t.setClickable(true);
        t.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && textList.size() > 0 && currentId != -1) {
                    itemClickListener.onItemClick(currentId % textList.size());
                }
            }
        });

        return t;
    }

    /**
     * 设置点击事件监听
     *
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * 轮播文本点击监听器
     */
    public interface OnItemClickListener {

        /**
         * 点击回调
         *
         * @param position 当前点击ID
         */
        public void onItemClick(int position);

}
}