package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.GuessGameMessage;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.response.GuessMessageResponse;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.view.AutoListView;
import cn.com.cimgroup.view.AutoListView.OnLoadListener;
import cn.com.cimgroup.view.AutoListView.OnRefreshListener;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 比赛竞猜留言板
 * 
 * @author 秋风
 * 
 */
@SuppressLint("HandlerLeak")
public class GuessMatchMessageActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener, OnLoadListener {
	/** 留言题目Code */
	private String mLeagueCode;

	private int pageIndex = 1;

	private List<GuessGameMessage> mMessageList;

	private MessageAdapter mAdapter;

	private Intent mIntent;
	/** 是否默认为输入状态 */
	private String mIsInput;
	/** 消息容器 */
	private AutoListView id_list;

	/** 发送消息的内容 */
	private EditText id_send_context;

	/** 比赛留言板信息 */
	private GuessMessageResponse messageResponse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guess_message_message);
		// 初始化视图组件
		initView();
		// 绑定事件
		initEvent();
		// 初始化数据
		initDatas();
	}

	private void initDatas() {

		mIntent = getIntent();
		mIsInput = mIntent.getStringExtra("isInput");
		mLeagueCode = mIntent.getStringExtra("leagueCode");
		// 如果 mInput值为true，则默认开启键盘
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		id_list.setAdapter(mAdapter);
		findViewById(R.id.id_send).setOnClickListener(this);
		findViewById(R.id.id_back).setOnClickListener(this);
		id_list.setOnRefreshListener(this);
		id_list.setOnLoadListener(this);
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		mMessageList = new ArrayList<GuessGameMessage>();
		mAdapter = new MessageAdapter();

		id_send_context = (EditText) findViewById(R.id.id_send_context);
		id_list = (AutoListView) findViewById(R.id.id_list);
		
		id_send_context.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s)) {
					if (s.length()>140) {
						ToastUtil.shortToast(mActivity, "字数须在140字以内");
						id_send_context.setText(s.subSequence(0, 139));
					}
				}
			}
		});
	}

	/**
	 * 获取留言板信息
	 */
	public void getMessageInfo() {
		showLoadingDialog();
		Controller.getInstance().getMatchMessage(
				GlobalConstants.URL_GET_GUESS_MATCH_MESSAGE, mLeagueCode, "10",
				pageIndex + "", mBack);
	}

	private InputMethodManager imm;

	private final static int GET_INPUT = 0;

	@Override
	public void onResume() {
		super.onResume();
		getMessageInfo();
		mHandler.sendEmptyMessageDelayed(GET_INPUT, 500);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GET_INPUT:
				if (mIsInput.equals("true")) {
					imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					id_send_context.setFocusable(true);
					id_send_context.setFocusableInTouchMode(true);
					id_send_context.requestFocus();
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				}
				break;

			default:
				break;
			}
		};
	};
//	/**是否提示没有更多的提醒:进入时，如果没有十条信息，不提醒，留言后不提醒*/
//	private boolean isShowToast=false;
	/**
	 * 处理请求回调
	 */
	private CallBack mBack = new CallBack() {
		public void getMatchMessageSuccess(final String json) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					if (mMessageList!=null&&mMessageList.size()==1&&mMessageList.get(0).getUserId()==null) {
						mMessageList.clear();
					}
					messageResponse=new GuessMessageResponse();
					if (mMessageList == null)
							mMessageList = new ArrayList<GuessGameMessage>();
					getJson(json);
					if (messageResponse.getResCode().equals("0")) {
						if (mState.equals("load")) {
							if (pageIndex == 1) {
								mMessageList.clear();
							}
							if (mMessageList != null
									&& mMessageList.size() < Integer.parseInt(messageResponse.getTotal())) {
								id_list.setFooterState(View.VISIBLE);
								pageIndex++;
							} else {
								// 最后一次加载未到达10条
								id_list.setFooterState(View.GONE);
								ToastUtil.shortToast(mActivity, "没有更多留言了");
							}
							id_list.onLoadComplete();

						} else if (mState.equals("refresh")) {
							pageIndex = 1;
							id_list.onRefreshComplete();
							
							if (mMessageList != null
									&& mMessageList.size() <Integer.parseInt(messageResponse.getTotal())) {
								id_list.setFooterState(View.VISIBLE);
							} else {
								// 最后一次加载未到达10条
								id_list.setFooterState(View.GONE);
							}
						}
					} else {
						ToastUtil.shortToast(GuessMatchMessageActivity.this,
								messageResponse.getResMsg());
					}
					if (mMessageList.size()==0) {
						id_list.setFooterState(View.GONE);
						GuessGameMessage message=new GuessGameMessage();
						message.setContent("暂无留言");
						mMessageList.add(message);
					}
					mAdapter.notifyDataSetChanged();
				}
			});
		};

		public void getMatchMessageFailure(final String error) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(GuessMatchMessageActivity.this, error);
				}
			});
		};

		public void resultSuccessStr(final String json) {
			super.resultSuccessStr(json);
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (json != null) {
						try {
							JSONObject object = new JSONObject(json);
							String resCode = object.getString("resCode");
							if (resCode != null) {
								if (resCode.equals("0")) {
									pageIndex = 1;
									id_send_context.setText("");
									mMessageList.clear();
									mState = "refresh";
									getMessageInfo();
								} else {
									ToastUtil.shortToast(mActivity,
											object.getString("resMsg"));
								}

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}
			});
		}

		public void resultFailure(final String error) {
			super.resultFailure(error);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(mActivity, error);
				}
			});
		}
	};
	/**
	 * 解析数据
	 * @param json
	 */
	private void getJson(String json) {
		try {
			JSONObject object=new JSONObject(json);
			messageResponse.setResCode(object.optString("resCode",""));
			messageResponse.setResMsg(object.optString("resMsg",""));
			messageResponse.setTotal(object.optString("total","0"));
			messageResponse.setPageNo(object.optString("pageNo","1"));
			JSONArray array=object.getJSONArray("list");
			GuessGameMessage message=null;
			int size=array.length();
			if (mState.equals("refresh")) {
				mMessageList.clear();
			}
			for (int i = 0; i < size; i++) {
				JSONObject o=array.getJSONObject(i);
				message=new GuessGameMessage();
				message.setContent(o.optString("content",""));
				message.setUserName(o.optString("userName",""));
				message.setAddTime(o.optString("addTime",""));
				message.setUserId(o.optString("userId",""));
				mMessageList.add(message);
			}
			messageResponse.setList(mMessageList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 留言板适配器
	 * 
	 * @author 秋风
	 * 
	 */
	private class MessageAdapter extends BaseAdapter {
		
		private int emptyType=1;
		private int defaultType=0;
		@Override
		public int getCount() {
			return mMessageList == null ? 0 : mMessageList.size();
		}

		@Override
		public Object getItem(int position) {
			return mMessageList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
		
		@Override
		public int getItemViewType(int position) {
			
			return mMessageList.size()==1&&mMessageList.get(0).getUserId()==null?emptyType:defaultType;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			MessageViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(GuessMatchMessageActivity.this)
						.inflate(R.layout.fragment_game_message_item, parent,
								false);
				holder = new MessageViewHolder();
				holder.id_username = (TextView) view
						.findViewById(R.id.id_username);
				holder.id_time = (TextView) view.findViewById(R.id.id_time);
				holder.id_context = (TextView) view
						.findViewById(R.id.id_context);
				holder.id_line = view.findViewById(R.id.id_line);
				holder.id_top_layout=(LinearLayout) view.findViewById(R.id.id_top_layout);
				holder.id_empty_context=(TextView) view.findViewById(R.id.id_empty_context);
				LayoutParams lp=(LayoutParams) holder.id_empty_context.getLayoutParams();
				lp.height=DensityUtils.getScreenHeight(GuessMatchMessageActivity.this)-DensityUtils.dip2px(145);
				holder.id_empty_context.setLayoutParams(lp);
				view.setTag(holder);
			} else
				holder = (MessageViewHolder) view.getTag();
			GuessGameMessage message = mMessageList.get(position);
			if (mMessageList.size()==1&&message.getUserId()==null) {
				holder.id_top_layout.setVisibility(View.GONE);
				holder.id_context.setVisibility(View.GONE);
				holder.id_line.setVisibility(View.GONE);
				holder.id_empty_context.setVisibility(View.VISIBLE);
				holder.id_empty_context.setText(message.getContent());
			}else {
				holder.id_top_layout.setVisibility(View.VISIBLE);
				holder.id_context.setVisibility(View.VISIBLE);
				holder.id_line.setVisibility(View.VISIBLE);
				holder.id_empty_context.setVisibility(View.GONE);
				holder.id_username.setText(message.getUserName());
				holder.id_time.setText(message.getAddTime());
				holder.id_context.setText(message.getContent());
//				holder.id_line
//						.setVisibility(position == mMessageList.size() - 1 ? View.GONE
//								: View.VISIBLE);
			}
			return view;
		}

	}

	/**
	 * 留言板ViewHolder
	 * 
	 * @author 秋风
	 * 
	 */
	class MessageViewHolder {
		/** 留言人昵称 */
		TextView id_username;
		/** 留言时间 */
		TextView id_time;
		/** 留言内容 */
		TextView id_context;
		/** 虚线 */
		View id_line;
		
		LinearLayout id_top_layout;
		TextView id_empty_context;
	}
	/** 数据加载类型：刷新：refresh；加载：load */
	private String mState = "refresh";

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_send:
			// 发布留言
			if (App.userInfo != null
					&& !TextUtils.isEmpty(App.userInfo.getUserId()))
				sendMessage();
			else
				startActivity(LoginActivity.class);
			break;
		case R.id.id_back:
			// 返回
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 发布留言
	 */
	private void sendMessage() {
		String context = id_send_context.getText().toString().trim();
		if (!TextUtils.isEmpty(context)) {
			showLoadingDialog();
			Controller.getInstance().sendMatchMessage(
					GlobalConstants.URL_SEND_GUESS_MATCH_MESSAGE,
					App.userInfo.getUserId(), mLeagueCode, context, mBack);
		} else
			ToastUtil.shortToast(mActivity, "请输入留言内容");

	}

	@Override
	public void onRefresh() {
		mState = "refresh";
		pageIndex = 1;
		getMessageInfo();

	}

	@Override
	public void onLoad() {
		if (mState.equals("refresh")) {
			if (mMessageList != null && mMessageList.size() == 10) {
				pageIndex++;
			}
			mState = "load";
		}

		getMessageInfo();
	}
}
