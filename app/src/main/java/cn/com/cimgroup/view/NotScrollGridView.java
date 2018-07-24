package cn.com.cimgroup.view;

import cn.com.cimgroup.R;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.view.AutoScrollTextView.OnItemClickListener;
import android.R.integer;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotScrollGridView extends GridView {
	
	private View mEmptyView;
	
	public ImageView emptyImage;
	public TextView oneText;
	public TextView twoText;
	public TextView button;
	public LinearLayout emptyLinearLayout;
	
	protected boolean mShouldInitialize = true;
	
	private OnClickListener clickListener;
	
    public NotScrollGridView(Context context) {
        super(context);
    }

    public NotScrollGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotScrollGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);

    }
    
    public void initView(LayoutInflater inflater) {
		if (mEmptyView == null) {
			mEmptyView = inflater.inflate(R.layout.layout_loading_empty, null);
			LinearLayout.LayoutParams p = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT); 
			((ViewGroup)getParent()).addView(mEmptyView, p);
		} else {
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {
			emptyImage = (ImageView) mEmptyView.findViewById(R.id.imageView_load_empty);
			oneText = (TextView) mEmptyView.findViewById(R.id.textView_load_one);
			twoText = (TextView) mEmptyView.findViewById(R.id.textView_load_two);
			button = (TextView) mEmptyView.findViewById(R.id.button_load);
			emptyLinearLayout = (LinearLayout) mEmptyView.findViewById(R.id.linear_loading_empty);
		}
		
		if (button != null) {
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					clickListener.onClick(Integer.parseInt(NoDataUtils.getmErrCode()));
				}
			});
		}
		
		setEmptyView(mEmptyView);
	}
    
    /**
     * 重置gridView的高度
     * @param height
     */
    public void resetHeight(int height){
    	
    	LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
    	lp.height = height;
    	mEmptyView.setLayoutParams(lp);
    	mEmptyView.requestLayout();
    }

	public void setOnClickListener(OnClickListener clickListener) {
		this.clickListener = clickListener;
	}

	public interface OnClickListener {

		/**
		 * 点击回调
		 * 
		 * @param position
		 *            当前点击ID
		 */
		public void onClick(int position);

	}
}
