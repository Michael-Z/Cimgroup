package cn.com.cimgroup.frament;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;

import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.GameGuessMatchActivity;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.activity.MessageCenterActivity;
import cn.com.cimgroup.adapter.BaseLotteryBidOnlyAdapter;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.NoDataUtils;

public abstract class BaseLotteryBidFrament extends BaseFrament {

	/** 布局View **/
	private View mView;
	
	public View mEmptyView;

//	protected PullToRefreshListView mListView;
	protected ExpandableListView mExpandableListView;
	
	protected PullToRefreshExpandableListView pullToRefreshExpandableListView;

	protected boolean mShouldInitialize = true;

	// 业务逻辑类
	protected Controller mController = Controller.getInstance();
	
	/**筛选方式**/
	public MatchSearchType mSearchType;
	
	private static final String SEARCH_MATCH = "SEARCH_MATCH";
	
	private int page = 1;
	
	private int oldPage = 0;
	
	private BaseExpandableListAdapter mAdapter;
	
	// 标志位，标志已经初始化完成。
	private boolean isPrepared;
	
	public BaseLotteryBidOnlyAdapter mBaseAdapter;
	
	public ImageView emptyImage;
	public TextView oneText;
	public TextView twoText;
	public TextView button;
	
	/**
	 * 足彩筛选
	 * @Description:
	 * @author:zhangjf 
	 * @copyright www.wenchuang.com
	 * @Date:2015年11月16日
	 */
	public enum MatchSearchType{
		//过关
		TWIN,
		//单关
		SINGLE,
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//获取足彩筛选方式
		mSearchType=getArguments()==null?MatchSearchType.TWIN:MatchSearchType.values()[getArguments().getInt(SEARCH_MATCH)];
		String temp = "1";
		if (savedInstanceState != null) {  
			temp = savedInstanceState.getString("searchType");
			switch (temp) {
			case "1":
				mSearchType = MatchSearchType.TWIN;
				break;
			case "2":
				mSearchType = MatchSearchType.SINGLE;
				break;
			default:
				break;
			}
		}else {
//			mSearchType = MatchSearchType.TWIN;
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		switch (mSearchType) {
		case TWIN:
			outState.putString("searchType", "1");
			break;
		case SINGLE:
			outState.putString("searchType", "2");
			break;
		default:
			break;
		}
		
	}
	
	/**
	 * 创建、初始化Veiw
	 * @Description:
	 * @param inflater
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	protected void initView(LayoutInflater inflater) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.frament_lotterybid, null);
			mEmptyView = inflater.inflate(R.layout.layout_loading_empty, null);
			mEmptyView.setVisibility(View.GONE);
		} else {
//			ViewGroup parent = (ViewGroup) mView.getParent();
//			parent.removeView(mView);
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {
			pullToRefreshExpandableListView = (PullToRefreshExpandableListView) mView.findViewById(R.id.elistView_lotterybid);
			mExpandableListView = pullToRefreshExpandableListView.getRefreshableView();
			emptyImage = (ImageView) mEmptyView.findViewById(R.id.imageView_load_empty);
			oneText = (TextView) mEmptyView.findViewById(R.id.textView_load_one);
			twoText = (TextView) mEmptyView.findViewById(R.id.textView_load_two);
			button = (TextView) mEmptyView.findViewById(R.id.button_load);
			initData();
			isPrepared = true;
			lazyLoad();
		}
		
		if (button != null) {
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					emptyBtnOnClick();
				}
			});
		}
	}

	/**
	 * 获取布局view
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	protected View getContentView() {
		return mView;
	}

	/**
	 * 展开mExpandableListView每一项
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	protected void expandGroup() {
		if (mExpandableListView != null)
			for (int i = 0; i < mExpandableListView.getExpandableListAdapter().getGroupCount(); i++) {
				mExpandableListView.expandGroup(i);
			}
	}

	
	/**
	 * 初始化View时，首次加载或者初始化一些资源
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	public abstract void initData();
	
	
	public void initLoad() {
//		pullToRefreshExpandableListView = listView;
//		mAdapter = adapter;
		initView();
//		loadFirstData();
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
		if(pullToRefreshExpandableListView != null) {
			pullToRefreshExpandableListView.setOnRefreshListener(new OnRefreshListener2<ExpandableListView>() {

				@Override
				public void onPullDownToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
					loadFirstData();
				}

				@Override
				public void onPullUpToRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
					loadNextData();
				}

			});
			pullToRefreshExpandableListView.setRefreshing(false);
			pullToRefreshExpandableListView.setEmptyView(mEmptyView);
			pullToRefreshExpandableListView.setMode(Mode.PULL_FROM_START);
//		pullToRefreshExpandableListView.setAdapter(mAdapter);
		}
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
				Intent convertIntent = new Intent(getActivity(),MessageCenterActivity.class);
				convertIntent.putExtra(MessageCenterActivity.TYPE,MessageCenterActivity.LEMICONVERT);
				getActivity().startActivity(convertIntent);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
//		pullToRefreshExpandableListView = null;
//		mExpandableListView = null;
		if (mView != null) {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
		}
//		mView = null;
	}

	/**
	 * 加载数据
	 */
	protected abstract void loadData(int page);
	
	/**
	 * 数据加载完毕
	 */
	protected void loadFinish() {
		hideLoadingDialog();
		pullToRefreshExpandableListView.onRefreshComplete();
	}


	/**
	 * 宿主Activity控件的点击事件，传递给frament，去做相应的事件处理
	 * @Description:
	 * @param id
	 * @param data
	 * @author:www.wenchuang.com
	 * @date:2015年11月16日
	 */
	public abstract void onSuperActivityOnClick(int id,Object ...data);

	/**
	 * 模拟activity onActivityResult，由宿主activity进行分发
	 */
	public abstract void onActivityResult(int requestCode, int resultCode, Intent data);
	
	
	//以下代码实现限制fragment预加载
	protected boolean isVisible;
	
	/**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    
    protected void onVisible(){
        lazyLoad();
    }
    
    protected void onInvisible(){}
    
	protected void lazyLoad() {
		if(!isPrepared || !isVisible) {
			return;
		} else {
			isPrepared = false;
			initLoad();
		}
	}
}
