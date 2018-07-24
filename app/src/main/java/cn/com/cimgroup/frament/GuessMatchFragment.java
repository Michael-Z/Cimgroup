package cn.com.cimgroup.frament;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.GameGuessMatchActivity;
import cn.com.cimgroup.activity.GuessMatchMessageActivity;
import cn.com.cimgroup.bean.GuessGameMessage;
import cn.com.cimgroup.bean.GuessMatchInfo;
import cn.com.cimgroup.bean.QuestionInfo;
import cn.com.cimgroup.dailog.GuessBettingDialog;
import cn.com.cimgroup.dailog.GuessBettingDialog.GuessBettingCallBack;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.response.GuessMessageResponse;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.view.CircleBitmapDisplayer;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 竞猜比赛Fragment
 * 
 * @author 秋风
 * 
 */
public class GuessMatchFragment extends BaseFrament implements
		GuessBettingCallBack, OnClickListener {
	private View mView;
	/** 比赛题目信息 */
	private GuessMatchInfo mMatchInfo;
	/** 获取比赛信息后的索引 */
	private String mMatchIndex;
	/** 比赛留言板信息 */
	private GuessMessageResponse messageResponse;
	/** 留言板信息 */
	private List<GuessGameMessage> mMessageList;
	/** 题目信息集合 */
	private List<QuestionInfo> mQuestList;
	// /** 比赛评论消息适配器 */
	// private MessageAdapter mMessageAdapter;
	/** 比赛题目适配器 */
	private QuestionAdapter mQuestionAdapter;

	/** 比赛时间 */
	private Button id_match_time;
	/** 主队球队图片 */
	private ImageView id_host_image;
	/** 客队球队图片 */
	private ImageView id_guest_image;
	/** 主队队名 */
	private TextView id_host_name;
	/** 客队队名 */
	private TextView id_guest_name;
	/** 截止时间 */
	private TextView id_end_time;
	/** 题目列表ListView */
	private ListView id_listview_question;
	// /** 留言板ListView */
	// private ListView id_listView_message;

	/** 留言板信息 */
	private LinearLayout id_message_layout;

	/** 获取消息成功 */
	private static final int GET_MESSAGE_OK = 0;
	/** 更多消息 */
	private ImageView id_message_more;

	/** 游戏投注弹出框 */
	private GuessBettingDialog mBettingDialog;

	private Handler mHandler;

	private boolean isBetting = true;

	private int mMessageListHeight = 0;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup viewgroup, @Nullable Bundle savedInstanceState) {
		Bundle bundle = getArguments();
		mMatchIndex = bundle == null ? "0" : bundle.getString("matchIndex");
		mView = inflater.inflate(R.layout.fragment_game_guess_match, viewgroup,
				false);
		initImageLoader();
		initView();
		initEvent();
		// ViewUtils.measureView(id_listView_message);
		// resetListViewHeight();

		return mView;
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		mBettingDialog.setOnGuessBettingCallBack(this);
		id_message_more.setOnClickListener(this);
		mView.findViewById(R.id.id_pre_match).setOnClickListener(this);
		mView.findViewById(R.id.id_next_match).setOnClickListener(this);

	}

	@SuppressWarnings("deprecation")
	private void initImageLoader() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.e_ui_game_image_d) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.e_ui_game_image_d) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.e_ui_game_image_d) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
				.displayer(new CircleBitmapDisplayer()) // 设置成圆角图片
				.build(); // 创建配置过得DisplayImageOption对象
	}

	public static GuessMatchFragment newInstance(String s) {
		GuessMatchFragment newFragment = new GuessMatchFragment();
		Bundle bundle = new Bundle();
		bundle.putString("matchIndex", s);
		newFragment.setArguments(bundle);
		return newFragment;
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_match_time = (Button) mView.findViewById(R.id.id_match_time);
		id_host_image = (ImageView) mView.findViewById(R.id.id_host_image);
		id_guest_image = (ImageView) mView.findViewById(R.id.id_guest_image);
		id_host_name = (TextView) mView.findViewById(R.id.id_host_name);
		id_guest_name = (TextView) mView.findViewById(R.id.id_guest_name);
		id_end_time = (TextView) mView.findViewById(R.id.id_end_time);
		id_message_more = (ImageView) mView.findViewById(R.id.id_message_more);
		id_listview_question = (ListView) mView
				.findViewById(R.id.id_listview_question);

		mQuestionAdapter = new QuestionAdapter();
		id_listview_question.setAdapter(mQuestionAdapter);

		// id_listView_message = (ListView) mView
		// .findViewById(R.id.id_listView_message);
		// mMessageAdapter = new MessageAdapter();
		// id_listView_message.setAdapter(mMessageAdapter);

		mBettingDialog = new GuessBettingDialog(getActivity());

		id_message_layout = (LinearLayout) mView
				.findViewById(R.id.id_message_layout);
	}

	/**
	 * 获取数据
	 */
	private long startTime = 0;
	private long endTime = 0;

	/** 获取View的位置 */
	private final static int GET_VIEW_LOCATION = 3;

	public void initDatas() {
		// endTime = System.currentTimeMillis();
		// if (endTime - startTime > 500) {
		if (mMatchInfoListener != null) {
			mMatchInfo = mMatchInfoListener.getMatchInfo(mMatchIndex);
		}
		if (mMatchInfo != null && id_match_time != null) {
			// 更新UI显示数据
			// 问题
			// 1、10110006接口会有theChoice（我的已选方案），在投注后是否需要更新整个的数据,从而获取最新的数据显示
			id_match_time.setText(mMatchInfo.getMatchName() + "  "
					+ mMatchInfo.getMatchTime());
			id_host_name.setText(mMatchInfo.getHostName());
			id_guest_name.setText(mMatchInfo.getGuestName());
			ImageLoader.getInstance().displayImage(mMatchInfo.getHostImg(),
					id_host_image);
			ImageLoader.getInstance().displayImage(mMatchInfo.getGuestImg(),
					id_guest_image);
			mQuestList = mMatchInfo.getQuestionList() == null ? new ArrayList<QuestionInfo>()
					: mMatchInfo.getQuestionList();
			mQuestionAdapter.notifyDataSetChanged();
			ViewUtils.setListViewHeightByShouCount(id_listview_question, 3);
			long time = DateUtil.getTimeFormatLong("yyyy-MM-dd HH:mm:ss",
					mMatchInfo.getMatchTime());
			Message msg = mHandler.obtainMessage();
			Map<String, String> map = new HashMap<String, String>();
			map.put("index", mMatchIndex);
			map.put("time", time + "");
			msg.obj = map;
			msg.what = 1;
			msg.sendToTarget();
			// 获取比赛的留言板
			mHandler.sendEmptyMessageDelayed(GET_VIEW_LOCATION, 300);

		}
	}

	public void setHandler(Handler handler) {
		mHandler = handler;
	}

	/**
	 * 获取留言板信息
	 */
	public void getMessageInfo() {
		showLoadingDialog();
		messageResponse=new GuessMessageResponse();
		Controller.getInstance().getMatchMessage(
				GlobalConstants.URL_GET_GUESS_MATCH_MESSAGE,
				mMatchInfo.getLeagueCode(), "5", "1", mBack);
	}

	/**
	 * 设置倒计时的值
	 * 
	 * @param time
	 */
	public void setTime(String time) {
		if (id_end_time != null) {
			id_end_time.setText("距投注截止：" + time);
			if (time.equals("本场竞猜已结束"))
				isBetting = false;
			else
				isBetting = true;
		}

	}

	/**
	 * 绑定监听
	 * 
	 * @param matchInfoListener
	 */
	public void setListener(MatchInfoListener matchInfoListener) {
		mMatchInfoListener = matchInfoListener;
	}

	private MatchInfoListener mMatchInfoListener;

	/** 获取比赛信息回调 */
	public interface MatchInfoListener {
		/**
		 * 获取比赛单场信息
		 * 
		 * @param fragmentIndex
		 *            碎片页码编号
		 * @return
		 */
		GuessMatchInfo getMatchInfo(String fragmentIndex);

		/**
		 * 切换到上一场或者下一场比赛
		 * 
		 * @param fragmentIndex
		 * @param switchType
		 *            left点击左侧；right点击右侧
		 */
		void switchMatch(String fragmentIndex, String switchType);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 比赛题目适配器
	 * 
	 * @author 秋风
	 * 
	 */
	private class QuestionAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mQuestList == null ? 0 : mQuestList.size();
		}

		@Override
		public Object getItem(int position) {
			return mQuestList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			QuestionViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.fragment_game_question_item, parent, false);
				holder = new QuestionViewHolder();
				holder.id_question = (TextView) view
						.findViewById(R.id.id_question);
				holder.id_together = (TextView) view
						.findViewById(R.id.id_together);
				holder.id_true = (TextView) view.findViewById(R.id.id_true);
				holder.id_false = (TextView) view.findViewById(R.id.id_false);
				holder.id_definitepool = (TextView) view
						.findViewById(R.id.id_definitepool);
				holder.id_negativepool = (TextView) view
						.findViewById(R.id.id_negativepool);
				view.setTag(holder);
			} else
				holder = (QuestionViewHolder) view.getTag();
			final QuestionInfo info = mQuestList.get(position);
			holder.id_true.setEnabled(true);
			holder.id_false.setSelected(true);

			if (TextUtils.isEmpty(info.getTheChoice())) {
				holder.id_true.setSelected(false);
				holder.id_true.setTextColor(getActivity().getResources()
						.getColor(R.color.color_gray_indicator));
				holder.id_false.setSelected(false);
				holder.id_false.setTextColor(getActivity().getResources()
						.getColor(R.color.color_gray_indicator));
			} else if (info.getTheChoice().equals("1")) {
				holder.id_true.setSelected(true);
				holder.id_true.setTextColor(getActivity().getResources()
						.getColor(R.color.color_white));
				holder.id_false.setSelected(false);
				holder.id_false.setTextColor(getActivity().getResources()
						.getColor(R.color.color_white));
				holder.id_false
						.setBackgroundResource(R.drawable.shape_round_gray_r3);
				holder.id_false.setEnabled(false);
			} else if (info.getTheChoice().equals("0")) {
				holder.id_true.setSelected(false);
				holder.id_true.setEnabled(false);
				holder.id_true.setTextColor(getActivity().getResources()
						.getColor(R.color.color_white));
				holder.id_false.setSelected(true);
				holder.id_false.setTextColor(getActivity().getResources()
						.getColor(R.color.color_white));
				holder.id_true
						.setBackgroundResource(R.drawable.shape_round_gray_r3);
			}

			holder.id_question.setText(info.getQuestion());
			holder.id_definitepool.setText(info.getDefinitePool());
			holder.id_negativepool.setText(info.getNegativePool());
			holder.id_together
					.setText((Integer.valueOf(TextUtils.isEmpty(info
							.getDefiniteUsers()) ? "0" : info
							.getDefiniteUsers()) + Integer.valueOf(TextUtils
							.isEmpty(info.getNegativeUsers()) ? "0" : info
							.getNegativeUsers()))
							+ "人参与");
			if (TextUtils.isEmpty(info.getTheChoice())
					|| info.getTheChoice().equals("")) {

			}
			holder.id_true.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 选择"会"
					if (isBetting)
						mBettingDialog.showDialog("1", info.getQuestionId());

				}
			});
			holder.id_false.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 选择"不会"
					if (isBetting)
						mBettingDialog.showDialog("0", info.getQuestionId());
				}
			});
			return view;
		}
	}

	/** 比赛题目ViewPager */
	class QuestionViewHolder {
		/** 比赛题目 */
		TextView id_question;
		/** 参与人数 */
		TextView id_together;
		/** 选项true */
		TextView id_true;
		/** 选项false */
		TextView id_false;
		/** 奖池true */
		TextView id_definitepool;
		/** 奖池false */
		TextView id_negativepool;
	}

	/**
	 * 留言板适配器
	 * 
	 * @author 秋风
	 * 
	 */
	private class MessageAdapter extends BaseAdapter {

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
		public View getView(int position, View view, ViewGroup parent) {
			MessageViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.fragment_game_message_item, parent, false);
				holder = new MessageViewHolder();
				holder.id_username = (TextView) view
						.findViewById(R.id.id_username);
				holder.id_time = (TextView) view.findViewById(R.id.id_time);
				holder.id_context = (TextView) view
						.findViewById(R.id.id_context);
				holder.id_line = view.findViewById(R.id.id_line);
				view.setTag(holder);
			} else
				holder = (MessageViewHolder) view.getTag();
			GuessGameMessage message = mMessageList.get(position);
			holder.id_username.setText(message.getUserName());
			holder.id_time.setText(message.getAddTime());
			holder.id_context.setText(message.getContent());
			holder.id_line
					.setBackgroundResource(position == mMessageList.size() - 1 ? R.color.transparent
							: R.color.layout_bg_color);
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
		/** item项的间隔线 */
		View id_line;

	}

	private CallBack mBack = new CallBack() {
		public void getMatchMessageSuccess(final String json) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					if (mMessageList == null)
						mMessageList = new ArrayList<GuessGameMessage>();
					else
						mMessageList.clear();
					getJson(json);
					
					
					
					
					if (messageResponse.getResCode().equals("0")) {
						mMessageList = messageResponse.getList();
						// mMessageAdapter.notifyDataSetChanged();
					} else {
						ToastUtil.shortToast(getActivity(),
								messageResponse.getResMsg());
					}
					id_message_layout.removeAllViews();
					if (mMessageList != null && mMessageList.size() != 0) {
						// Log.e("qiufeng", "数据数量:"+mMessageList.size());
						// ViewUtils.measureView(id_listView_message);
						// id_listView_message.setVisibility(View.VISIBLE);
						// ViewUtils.setListViewHeightBasedOnChildren(id_listView_message);
						// Log.e("qiufeng",
						// "measureHeight:"+id_listView_message.getMeasuredHeight());

						// if (mMessageListHeight == 0) {
						// mMessageListHeight = id_listView_message
						// .getMeasuredHeight()
						// + id_listView_message.getPaddingTop()
						// + id_listView_message.getPaddingBottom()
						// + id_listView_message.getDividerHeight();
						// }
						// resetListViewHeight();
						// 像信息layout种添加字view
						addChildrenToLayout();

					}
					// else
					// id_listView_message.setVisibility(View.GONE);

				}


			});
		};

		public void getMatchMessageFailure(final String error) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(getActivity(), error);
				}
			});
		};
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
			messageResponse.setTotal(object.optString("total",""));
			messageResponse.setPageNo(object.optString("pageNo","1"));
			JSONArray array=object.getJSONArray("list");
			GuessGameMessage message=null;
			int size=array.length();
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
	 * 向数据中添加子View
	 */
	private void addChildrenToLayout() {
		int size = mMessageList.size();
		for (int i = 0; i < size; i++) {
			View view = LayoutInflater.from(mFragmentActivity).inflate(
					R.layout.fragment_game_message_item, null);
			GuessGameMessage message = mMessageList.get(i);
			((TextView) view.findViewById(R.id.id_username)).setText(message
					.getUserName());
			((TextView) view.findViewById(R.id.id_time)).setText(message
					.getAddTime());
			((TextView) view.findViewById(R.id.id_context)).setText(message
					.getContent());
			view.findViewById(R.id.id_line).setBackgroundResource(
					i == mMessageList.size() - 1 ? R.color.transparent
							: R.color.layout_bg_color);
			id_message_layout.addView(view);
		}

	}

	// /**
	// * 重置ListView高度
	// */
	// private void resetListViewHeight() {
	// ViewGroup.LayoutParams params = id_listView_message.getLayoutParams();
	// params.height = mMessageListHeight
	// * (mMessageList == null || mMessageList.size() == 0 ? 0
	// : mMessageList.size());
	// // params.height =
	// //
	// mMessageListHeight*(mMessageList==null||mMessageList.size()==0?0:mMessageList.size())+DensityUtils.dip2px(48);
	// id_listView_message.setLayoutParams(params);
	// }
	private long mStartTime=0;
	@Override
	public void commit(final String lemi, String bettingType, String questionNo) {
		showLoadingDialog();
		if (System.currentTimeMillis()-mStartTime<500) {
			return;
		}
		Controller.getInstance().gameBetting(GlobalConstants.URL_GAME_BETTING,
				App.userInfo.getUserId(), "1", mMatchInfo.getLeagueCode(),
				questionNo, bettingType, lemi, new CallBack() {
					@Override
					public void resultSuccessStr(final String json) {
						super.resultSuccessStr(json);
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								hideLoadingDialog();
								getResopnseInfo(json, lemi);
							}
						});
					}

					@Override
					public void resultFailure(final String error) {
						super.resultFailure(error);
						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {
								hideLoadingDialog();
								ToastUtil.shortToast(getActivity(), error);
							}
						});
					}
				});

	}

	/**
	 * 解析返回结果并指定父Activity调用刷新数据
	 * 
	 * @param json
	 */
	private void getResopnseInfo(String json, String lemi) {
		try {
			JSONObject object = new JSONObject(json);
			String resCode = object.getString("resCode");
			if (resCode != null && resCode.equals("0")) {
				App.userInfo.setBetAcnt(object.getString("betAcnt"));
				App.userInfo.setIntegralAcnt(object.getString("integralAcnt"));
				UserLogic.getInstance().saveUserInfo(App.userInfo);
				
				QuestionInfo question = null;
				List<QuestionInfo> qList = new ArrayList<QuestionInfo>();
				JSONArray qArray = object.getJSONArray("questionList");
				int length = qArray.length();
				for (int j = 0; j < length; j++) {
					question = new QuestionInfo();
					JSONObject qo = qArray.getJSONObject(j);
					question.setNegativeUsers(qo.optString(
							"negativeUsers", ""));
					question.setDefinitePool(qo.optString(
							"definitePool", ""));
					question.setDefiniteUsers(qo.optString(
							"definiteUsers", ""));
					question.setQuestionId(qo.optString(
							"questionId", ""));
					question.setTheChoice(qo.optString("theChoice",
							""));
					question.setNegativePool(qo.optString(
							"negativePool", ""));
					question.setQuestion(qo.optString("question",
							""));
					question.setTotalUsers(qo.optString(
							"totalUsers", ""));
					qList.add(question);
				}
				
				List<QuestionInfo> questionList = mMatchInfo.getQuestionList();
				for (int j = 0; j < qList.size(); j++) {
					for (int i = 0; i < questionList.size(); i++) {
						if (qList.get(j).getQuestionId().equals(questionList.get(i).getQuestionId())) {
							questionList.get(i).setDefinitePool(qList.get(j).getDefinitePool());
							questionList.get(i).setDefiniteUsers(qList.get(j).getDefiniteUsers());
							questionList.get(i).setNegativePool(qList.get(j).getNegativePool());
							questionList.get(i).setNegativeUsers(qList.get(j).getNegativeUsers());
//							questionList.get(i).setQuestion(qList.get(j).getQuestion());
							questionList.get(i).setTheChoice(qList.get(j).getTheChoice());
							questionList.get(i).setTotalUsers(qList.get(j).getTotalUsers());
						}
					}
				}
				mMatchInfo.setQuestionList(questionList);
				mQuestionAdapter.notifyDataSetChanged();
//				((GameGuessMatchActivity) getActivity()).updateRequest();
				((GameGuessMatchActivity) getActivity()).setLemi();
				ToastUtil.shortToast(mFragmentActivity, "投注成功，投注金额为：" + lemi
						+ "米");
			} else {
				ToastUtil.shortToast(getActivity(), object.getString("resMsg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		endTime = System.currentTimeMillis();
		if (isVisibleToUser && endTime - startTime > 1000) {
			initDatas();
			if (mMatchInfo != null) {
				getMessageInfo();
			}
			startTime = endTime;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_message_more:
			// 天赚到留言板
			Intent messageIntent = new Intent();
			messageIntent.setClass(getActivity(),
					GuessMatchMessageActivity.class);
			messageIntent.putExtra("isInput", "true");
			messageIntent.putExtra("leagueCode", mMatchInfo.getLeagueCode());
			startActivity(messageIntent);
			break;
		case R.id.id_pre_match:
			// 切换到上一场比赛
			if (mMatchInfoListener != null) {
				mMatchInfoListener.switchMatch(mMatchIndex, "left");
			}

			break;
		case R.id.id_next_match:
			// 切换到下一场比赛
			if (mMatchInfoListener != null) {
				mMatchInfoListener.switchMatch(mMatchIndex, "right");
			}

			break;
		default:
			break;
		}

	}

	/**
	 * 获取引导图的三个参照物坐标
	 * 
	 * @return
	 */
	public Rect[] getViewLocation() {
		Rect top = ViewUtils.getViewLocation(id_host_image);
		Rect middle = ViewUtils                                                                                     
				.getViewLocation((TextView) ((View) id_listview_question
						.getChildAt(0)).findViewById(R.id.id_true));
		Rect bottom = ViewUtils.getViewLocation(id_message_more);
		return new Rect[] { top, middle, bottom };
	}
	
	//2016-9-19 修改记录 将removeCallBack放到onStop()中进行
	@Override
	public void onStop() {
		super.onStop();
		Controller.getInstance().removeCallback(mBack);
		//  If null, all callbacks and messages will be removed.
		mHandler.removeCallbacks(null);
	}

	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		
	}

}
