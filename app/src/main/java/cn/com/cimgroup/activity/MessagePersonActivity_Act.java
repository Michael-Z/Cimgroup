package cn.com.cimgroup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.cimgroup.BaseLoadActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.MessageActAdapter;
import cn.com.cimgroup.bean.Message;
import cn.com.cimgroup.bean.MessageObj;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.xutils.StringUtil;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class MessagePersonActivity_Act extends BaseLoadActivity implements OnItemClickListener, OnClickListener {

	protected PullToRefreshListView mPullToRefreshListView;
	private MessageActAdapter adapter;

	private MessageCenterActivity mActivity;
	/** 界面中第一个可见条目的position **/
	private int firstVisiable;
	/** 界面中可见条目的条数 **/
	private int visibleCount;
	/** 界面中总显示条数 **/
	private int totalItems;

	/** 返回按钮 **/
	private TextView left_button;
	/** 标题按钮 **/
	private TextView title_view;

	@Override
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_act_list);

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list_view_act);

		adapter = new MessageActAdapter(MessagePersonActivity_Act.this, null);
		((ListView) mPullToRefreshListView.getRefreshableView()).setDivider(null);

		initViews();
		initEvent();
		initLoad();
	}

	protected void loadData(int page) {
		// get data;获取列表数据;
		Controller.getInstance().getInsideMessageList(GlobalConstants.NUM_MESSAGE, "", "2", 10 + "", page + "", callBack);
	}

	private void initViews() {

		left_button = (TextView) findViewById(R.id.left_button);
		left_button.setOnClickListener(this);
		title_view = (TextView) findViewById(R.id.title_view);
		title_view.setText("活动");
		title_view.setTextSize(18);
		title_view.setTextColor(getResources().getColor(R.color.color_white));

	}

	@Override
	public void onResume() {
		super.onResume();
		initViews();
		initEvent();
	}

	private CallBack callBack = new CallBack() {

		@Override
		public void onSuccess(final Object t, final String... params) {
			if (t != null) {
				if (t instanceof Message) {
					// 获取列表;
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							loadFinish();
							Message msg = (Message) t;
							if (Integer.parseInt(msg.resCode) == 0) {
								if (isFirstPage()) {
									adapter.replaceAll(msg.list);
								} else {
									adapter.addAll(msg.list);
								}

								if (msg.list == null || msg.list.size() == 0) {
									// 还原页码
									// mPullToRefreshListView.setEmptyView(tv_sign_void);
									restorePage();
								}
								if (adapter.getCount() < Integer.parseInt(msg.total)) {
									mPullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
								}

								if (adapter.getCount() <= 0) {
									NoDataUtils.setNoDataView(MessagePersonActivity_Act.this, emptyImage, oneText, twoText, button, "0", 6);
								}

							} else {
								NoDataUtils.setNoDataView(MessagePersonActivity_Act.this, emptyImage, oneText, twoText, button, "0", 6);
							}
						}
					});
				} 
			}
		};

		public void onFail(final String error, final String[] params) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					loadFinish();
					if (params == null) {
						NoDataUtils.setNoDataView(MessagePersonActivity_Act.this, emptyImage, oneText, twoText, button, error, 0);
					}
				}
			});
		};
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		// 活动列表;
		MessageObj item = (MessageObj) adapter.getItem(position - 1);

		// if (item.isJumpURL.equals("0")) {
		// } else {
		if (!StringUtil.isEmpty(item.jumpURL)) {
			if (item.jumpURL.indexOf("http") != -1) {
				Intent it = new Intent(MessagePersonActivity_Act.this, HtmlCommonActivity.class);
				it.putExtra("isWeb", true);
				it.putExtra("webUrl", item.jumpURL);
				startActivity(it);
			}
		}
		// }

	}

	/**
	 * 初始化listView下拉刷新
	 * 
	 * @Description:
	 * @param listView
	 * @param adapter
	 * @author:www.wenchuang.com
	 * @date:2016-12-29
	 */
	private void initLoad() {
//		// 设置刷新监听器
//		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {
//
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//				loadFirstData();
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
//				loadNextData();
//			}
//
//		});
//		mPullToRefreshListView.setRefreshing(false);
//		mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
//		mPullToRefreshListView.setAdapter(adapter);
//		mPullToRefreshListView.setEmptyView(mEmptyView);
//		mPullToRefreshListView.setOnItemClickListener(this);
		//设置刷新监听器 调用父类的方法
		super.initLoad(mPullToRefreshListView, adapter);
	}
	
	private void initEvent() {
		// 设置下拉刷新的滑动监听，动态的返回滑动的位置
		mPullToRefreshListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {

			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				firstVisiable = arg1;
				visibleCount = arg2;
				totalItems = arg3;
			}
		});

	}

	// 2016-9-19 修改记录 将removeCallBack放到onStop()中进行
	@Override
	public void onStop() {
		super.onStop();
		Controller.getInstance().removeCallback(callBack);
	}

	@Override
	public void loadData(int title, int page) {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_button:
			//点击返回
			finish();
			break;
		default:
			break;
		}
	}

}
