package cn.com.cimgroup.view;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;

public class ViewPagerAdapter extends PagerAdapter {

    private List<View> mListViews;
    
    private OnItemClickListener onListener;

    public ViewPagerAdapter(List<View> listViews) {
        mListViews = listViews;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
    	mListViews.get(position).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onListener.onItemClick(position);
			}
		});
        container.addView(mListViews.get(position), 0);// 添加页卡
        return mListViews.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView(mListViews.get(position));
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListViews.size();
    }
	
	public void setOnListener(OnItemClickListener onListener) {
		this.onListener = onListener;
	}

	@Override
    public boolean isViewFromObject(View view, Object object) {
        // TODO Auto-generated method stub
        return view == object;
    }

    public interface OnItemClickListener {

        void onItemClick(int position);
    }

}
