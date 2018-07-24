package cn.com.cimgroup.popwindow;

import java.util.List;

import cn.com.cimgroup.R;
import android.R.layout;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;

public class PopWindowBasketballLotteryWay {
	
	private PopupWindow mBasketballPop;
	private View mView;
	private GridView mGridView;
	private List<String[]> mList;

	//构造方法，初始化布局
	public PopWindowBasketballLotteryWay(Context context) {
		
		mView = View.inflate(context, R.layout.cv_popwindow_basketball_way, null);
		mGridView = (GridView) mView.findViewById(R.id.gv_jclq_select);
		mGridView.setAdapter(new MyAdapter());
		
		mBasketballPop = new PopupWindow(mView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, false);
		
//		super();
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 头部popupwindow中显示内容的adapter
	 *
	 */
	public class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
	
}
