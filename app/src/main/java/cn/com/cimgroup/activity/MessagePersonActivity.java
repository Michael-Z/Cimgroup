package cn.com.cimgroup.activity;

import android.R.color;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;

import cn.com.cimgroup.BaseLoadActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.MessageInsideAdapter;
import cn.com.cimgroup.bean.Message;
import cn.com.cimgroup.bean.MessageObj;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.view.PullToRefreshSwipeListView;
import cn.com.cimgroup.view.swipemenu.SwipeMenu;
import cn.com.cimgroup.view.swipemenu.SwipeMenuCreator;
import cn.com.cimgroup.view.swipemenu.SwipeMenuItem;
import cn.com.cimgroup.view.swipemenu.SwipeMenuListView;

public class MessagePersonActivity extends BaseLoadActivity implements OnItemClickListener, OnClickListener {

	protected static final int MOVE_TO_ITEM = 0;

	protected PullToRefreshSwipeListView mPullToRefreshSwipeListView;
	private SwipeMenuListView swipeMenuListView;
	private MessageInsideAdapter adapter;
	private TextView tv_sign_void;

	/** 界面中第一个可见条目的position **/
	private int firstVisiable;
	/** 界面中可见条目的条数 **/
	private int visibleCount;
	/** 界面中总显示条数 **/
	private int totalItems;
	/** 需要显示的总共条目数 **/
	private int deleteItemBackground = R.color.color_red;
	private int deleteItemHeight;
	private int deleteItemWidth;
	private int deleteItemIcon = R.drawable.del_icon_normal;

	private boolean hasDeleteItem = false;

	/** 返回按钮 **/
	private TextView left_button;
	/** 标题按钮 **/
	private TextView title_view;
	/** 当前页显示了多少条数据 默认10条 **/
	private int MessageCount;

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case MOVE_TO_ITEM:

				// 当用户点击最后一个条目，需要展开详情，需要切换焦点条目位置，将详情显露出来
				((ListView) mPullToRefreshSwipeListView.getRefreshableView()).setSelection(firstVisiable + 2);
				if (firstVisiable + visibleCount == totalItems) {
					Log.e("wushiqiu", "已经滑动到了最后一条数据");
				}

				break;

			default:

				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_message_list);

		mPullToRefreshSwipeListView = (PullToRefreshSwipeListView) findViewById(R.id.list_view);
		mPullToRefreshSwipeListView.setEmptyView(mEmptyView);
		// 初始化pulltorefreshswip的相关参数
		adapter = new MessageInsideAdapter(MessagePersonActivity.this, null);
		swipeMenuListView = mPullToRefreshSwipeListView.getRefreshableView();
		swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {

			@Override
			public void onMenuItemClick(int position, SwipeMenu menu, int index) {
				// Toast.makeText(getActivity(),
				// "删除了条目:"+adapter.getItemId(position), 1).show();
				// 点击删除按钮 删除该条数据 参数3：1=删除 0=编辑
				Controller.getInstance().updateMessageState(GlobalConstants.NUM_MESSAGE_STATE, UserLogic.getInstance().getDefaultUserInfo().getUserId(), ((MessageObj) adapter.getItem(position)).messageId, "1", callBack, (position) + "");
			}
		});
		deleteItemHeight = dip2px(MessagePersonActivity.this, 75);
		deleteItemWidth = dip2px(MessagePersonActivity.this, 90);

		SwipeMenuCreator creator = addDeleteItem(deleteItemBackground, deleteItemWidth, deleteItemHeight, deleteItemIcon);

		swipeMenuListView.setMenuCreator(creator);

		initViews();
		initEvent();
		// loadData(page);
		initView_swip();

	};

	/**
	 * 添加删除按钮
	 * 
	 * @return
	 */
	private SwipeMenuCreator addDeleteItem(int background, int width, int height, int icon) {
		// 给ListView添加显示和隐藏的布局
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem openItem = new SwipeMenuItem(MessagePersonActivity.this);
				// set item background
				// openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
				// 0xCE)));
				openItem.setBackground(color.holo_orange_light);
				// set item width
				openItem.setWidth(90);
				// set item title
				openItem.setTitle("Open");
				// set item title fontsize
				openItem.setTitleSize(18);
				// set item title font color
				openItem.setTitleColor(Color.WHITE);
				// add to menu
				// menu.addMenuItem(openItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(MessagePersonActivity.this);
				// set item background
				deleteItem.setBackground(deleteItemBackground);
				// set item width
				deleteItem.setWidth(deleteItemWidth);
				// set item height
				deleteItem.setHeight(deleteItemHeight);
				// set a icon
				deleteItem.setIcon(deleteItemIcon);
				// set text
				deleteItem.setText("删除");
				deleteItem.setTextColor(color.white);
				deleteItem.setTextSize(14);

				// add to menu
				menu.addMenuItem(deleteItem);

			}
		};
		return creator;
	}

	protected void loadData(int page) {
		// get data;获取列表数据;
		int number = 10;
		if (hasDeleteItem) {
			number = page * number;
			page = 1;
		}
		MessageCount = page * number;

		Controller.getInstance().getInsideMessageList(GlobalConstants.NUM_MESSAGE, UserLogic.getInstance().getDefaultUserInfo().getUserId(), "1", number + "", page + "", callBack);
	}

	private void initViews() {

		tv_sign_void = new TextView(MessagePersonActivity.this);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		tv_sign_void.setGravity(Gravity.CENTER);
		tv_sign_void.setText("暂时没有消息");

		tv_sign_void.setLayoutParams(lp);

		left_button = (TextView) findViewById(R.id.left_button);
		left_button.setOnClickListener(this);
		title_view = (TextView) findViewById(R.id.title_view);
		title_view.setText("消息");
		title_view.setTextSize(18);
		title_view.setTextColor(getResources().getColor(R.color.color_white));
	}

	@Override
	public void onResume() {
		super.onResume();
		initViews();
		initEvent();
	}

	private void initEvent() {
		// 设置下拉刷新的滑动监听，动态的返回滑动的位置
		mPullToRefreshSwipeListView.setOnScrollListener(new OnScrollListener() {

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

	// 初始化下拉刷新相关的参数
	private void initView_swip() {
//		// 设置刷新监听器
//		mPullToRefreshSwipeListView.setOnRefreshListener(new OnRefreshListener2<SwipeMenuListView>() {
//
//			@Override
//			public void onPullDownToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
//				loadFirstData();
//			}
//
//			@Override
//			public void onPullUpToRefresh(PullToRefreshBase<SwipeMenuListView> refreshView) {
//				loadNextData();
//			}
//
//		});
//		mPullToRefreshSwipeListView.setRefreshing(false);
//		mPullToRefreshSwipeListView.setMode(Mode.PULL_FROM_START);
//		mPullToRefreshSwipeListView.setAdapter(adapter);
//		mPullToRefreshSwipeListView.setOnItemClickListener(this);
		
		super.initLoad_swip(mPullToRefreshSwipeListView, swipeMenuListView, adapter);
		
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
									if (hasDeleteItem) {
										adapter.replaceAll(msg.list);
									} else {
										adapter.addAll(msg.list);
									}
								}
								if (msg.list == null || msg.list.size() == 0) {
									// 还原页码
									restorePage();
									NoDataUtils.setNoDataView(MessagePersonActivity.this, emptyImage, oneText, twoText, button, "0", 7);
								}
								if (adapter.getCount() < Integer.parseInt(msg.total)) {
									mPullToRefreshSwipeListView.setMode(PullToRefreshBase.Mode.BOTH);
								}

								hasDeleteItem = false;
							} else {
								NoDataUtils.setNoDataView(MessagePersonActivity.this, emptyImage, oneText, twoText, button, "0", 7);
								hasDeleteItem = false;
							}
						}
					});
				} else if (t instanceof String && params.length > 0) {

					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							String s = (String) t;
							String position = params[0];
							if (!TextUtils.isEmpty(s)) {
								if (s.equals("0")) {
									// 更新状态;并展开;
									((MessageObj) adapter.getItem(Integer.parseInt(position))).isShowDetail = true;
									((MessageObj) adapter.getItem(Integer.parseInt(position))).isRead = "1";
									adapter.notifyDataSetChanged();
								} else if (s.equals("1")) {
									// 删除;
									String flag = ((MessageObj) adapter.getItem(Integer.parseInt(position))).isRead;
									adapter.remove(Integer.parseInt(position));
									hasDeleteItem = true;
								}
							}
						}
					});
				}
			}
		}

		@Override
		public void onFail(final String error, final String[] params) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (params == null) {
						NoDataUtils.setNoDataView(MessagePersonActivity.this, emptyImage, oneText, twoText, button, error, 0);
					}
				}
			});
		};
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		getItemHeight();

		// 设置已读状态;
		if (((MessageObj) adapter.getItem(position - 1)).isRead.equals("0")) {
			Controller.getInstance().updateMessageState(GlobalConstants.NUM_MESSAGE_STATE, UserLogic.getInstance().getDefaultUserInfo().getUserId(), ((MessageObj) adapter.getItem(position - 1)).messageId, "0", callBack, (position - 1) + "");
		} else {

			if (!((MessageObj) adapter.getItem(position - 1)).isShowDetail) {
				if ((position + 1) == (firstVisiable + visibleCount) || position + 2 == (firstVisiable + visibleCount)) {
					android.os.Message msg = android.os.Message.obtain();
					msg.what = MOVE_TO_ITEM;
					msg.obj = position;
					mHandler.sendMessage(msg);
				}
			}

			((MessageObj) adapter.getItem(position - 1)).isShowDetail = !((MessageObj) adapter.getItem(position - 1)).isShowDetail;

			// 获取显示的条目
			// int size = adapter.getCount();
			// ToastUtil.shortToast(getActivity(), "点击了条目iiiii" + position
			// + "/n" + "总共的条目数为:+" + size);
			adapter.notifyDataSetChanged();
		}

	}

	private void getItemHeight() {

		ListView mPullListView = ((ListView) mPullToRefreshSwipeListView.getRefreshableView());
		ListAdapter listAdapter = mPullListView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		View listItem = listAdapter.getView(1, null, mPullListView);// 得到选中条目的View
		listItem.measure(0, 0);// 计算子项的宽高
		// averageHeight = listItem.getMeasuredHeight();//获取该子项的高度

		// totalHeight = averageHeight *7;
	}

	// 2016-9-19 修改记录 将removeCallBack放到onStop()中进行
	@Override
	public void onStop() {
		super.onStop();
		Controller.getInstance().removeCallback(callBack);
	}

	/**
	 * dip转px
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.left_button:
			//返回
			doBack();
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 返回时提醒
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			doBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	private void doBack() {
		GlobalConstants.isRefreshFragment = true;
		finish();
		
	}

	@Override
	public void loadData(int title, int page) {
		
	}

}
