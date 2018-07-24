package cn.com.cimgroup;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import cn.com.cimgroup.activity.GameGuessMatchActivity;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.activity.MessageCenterActivity;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.view.PullToRefreshSwipeListView;
import cn.com.cimgroup.view.swipemenu.SwipeMenuListView;

public abstract class BaseLoadActivity extends BaseActivity implements OnItemClickListener {

	protected PullToRefreshListView mListView;
	protected SwipeMenuListView mSwipMenuListView;
	protected PullToRefreshSwipeListView mSwipeListView;
	protected PullToRefreshScrollView mScrollView;

	private int page = 1;
	private int oldPage = 0;
	private BaseAdapter mAdapter;
	
	public View mEmptyView;
	
	public ImageView emptyImage;
	public TextView oneText;
	public TextView twoText;
	public TextView button;
	
	protected boolean mShouldInitialize = true;
	
	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		initView(getLayoutInflater());
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * 初始化无数据页面
	 * @Description:
	 * @param inflater
	 * @author:www.wenchuang.com
	 * @date:2016-12-29
	 */
	private void initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		if (mEmptyView == null) {
			mEmptyView = inflater.inflate(R.layout.layout_loading_empty, null);
			
		} else {
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {
			emptyImage = (ImageView) mEmptyView.findViewById(R.id.imageView_load_empty);
			oneText = (TextView) mEmptyView.findViewById(R.id.textView_load_one);
			twoText = (TextView) mEmptyView.findViewById(R.id.textView_load_two);
			button = (TextView) mEmptyView.findViewById(R.id.button_load);
		}
		if (button != null) {
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					emptyBtnOnClick();
				}
			});
		}
	}


	public void initLoad(PullToRefreshListView listView, BaseAdapter adapter) {
		mListView = listView;
		mAdapter = adapter;
		initView();
//		loadFirstData();
	}
	
	public void initLoad_swip(PullToRefreshSwipeListView swipView, SwipeMenuListView listView, BaseAdapter adapter) {
		mSwipMenuListView = listView;
		mSwipeListView = swipView;
		mAdapter = adapter;
		initView_swip();
//		loadFirstData();
	}

	/**
	 * 滑动下拉刷新
	 * @Description:
	 * @param scrollView
	 * @author:www.wenchuang.com
	 * @date:2016-12-29
	 */
	public void initLoad(PullToRefreshScrollView scrollView) {
		mScrollView = scrollView;
		initView_scroll();
	}

	/**
	 * 判断是否是首页
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月2日
	 */
	protected Boolean isFirstPage(){
		return page==1;
	}
	/**
	 * 加载第一页
	 */
	protected void loadFirstData() {
		oldPage = page;
		page = 1;
		showLoadingDialog();
		loadData(page);
	}

	/**
	 * 加载下一页
	 */
	protected void loadNextData() {
		oldPage = page;
		page++;
		showLoadingDialog();
		loadData(page);
	}

	/**
	 * 还原页码
	 */
	protected void restorePage() {
		page = oldPage;
	}
	
	protected void reSetPage() {
		page = 1;
		oldPage = 0;
	}

	private void initView() {
		// 设置刷新监听器
		mListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadFirstData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				loadNextData();
			}

		});
		mListView.setRefreshing(false);
		mListView.setEmptyView(mEmptyView);
		mListView.setMode(Mode.PULL_FROM_START);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}
	
	private void initView_swip() {
		// 设置刷新监听器
		mSwipeListView.setOnRefreshListener(new OnRefreshListener2<SwipeMenuListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<SwipeMenuListView> refreshView) {
				loadFirstData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<SwipeMenuListView> refreshView) {
				loadNextData();
			}

		});
		mSwipeListView.setRefreshing(false);
		mSwipeListView.setEmptyView(mEmptyView);
		mSwipeListView.setMode(Mode.PULL_FROM_START);
		mSwipeListView.setAdapter(mAdapter);
		mSwipeListView.setOnItemClickListener(this);
	}
	
	/**
	 * 初始化滑动下拉刷新
	 * @Description:
	 * @param scrollView
	 * @author:www.wenchuang.com
	 * @date:2016-12-29
	 */
	private void initView_scroll() {
		// 设置刷新监听器
		mScrollView.setOnRefreshListener(new OnRefreshListener2<ScrollView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				loadFirstData();
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				loadNextData();
			}

		});
//		mScrollView.setRefreshing(false);
//		mScrollView.setMode(Mode.PULL_FROM_START);
	}
	
	/**
	 * 无数据时的统一跳转处理
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2016-12-29
	 */
	public void emptyBtnOnClick() {
		switch (Integer.parseInt(NoDataUtils.getmErrCode())) {
		
		case CException.NET_ERROR:
		case CException.IOERROR:
			loadFirstData();
			break;
		case 0:
			//type 1,投注记录、资金明细、红包明细-立即购彩
			//2,兑换记录、我的红包-前往商城
			//3,排行榜-参与竞猜
			switch (NoDataUtils.getmType()) {
			case 1:
				startActivity(LotteryFootballActivity.class);
				break;
			case 2:
				Intent convertIntent = new Intent(BaseLoadActivity.this,MessageCenterActivity.class);
				convertIntent.putExtra(MessageCenterActivity.TYPE,MessageCenterActivity.LEMICONVERT);
				startActivity(convertIntent);
				break;
			case 3:
				startActivity(GameGuessMatchActivity.class);
				break;

			default:
				break;
			}
			break;
		}
	}

	/**
	 * 加载数据
	 */
	protected abstract void loadData(int page);
	
	/**
	 * 加载数据
	 */
	public abstract void loadData(int title, int page);

	/**
	 * 数据加载完毕
	 */
	protected void loadFinish() {
		hideLoadingDialog();
		if (mSwipeListView!=null) {
			mSwipeListView.onRefreshComplete();
		}else if(mListView != null){
			mListView.onRefreshComplete();
		}else if (mScrollView != null) {
			mScrollView.onRefreshComplete();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}
}
