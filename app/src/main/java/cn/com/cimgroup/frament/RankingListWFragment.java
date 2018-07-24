package cn.com.cimgroup.frament;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.GameRankIngListActivity;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.activity.MainActivity;
import cn.com.cimgroup.bean.RankingListBean;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.PopupWnd_RankList_W_Date;
import cn.com.cimgroup.popwindow.PopupWnd_RankList_W_Date.onDateSelecteListener;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 娱乐场竞猜周排行
 * 
 * @author 洪林
 * 
 */
public class RankingListWFragment extends BaseLoadFragment implements
		OnClickListener {

	/** 排行10+**/
	private static final int RANKING_ELEVEN = 0;
	private static final int RANKING_WITHIN_TEN = 1;
	private static final int RESPONSE_FAILED = 2;
	private View mView;
	private boolean mShouldInitialize = true;

	/** 底部排名栏 -> 排名**/
	private TextView tv_my_rank_left;
	/** 底部排名栏 -> 盈利乐米**/
	private TextView tv_my_rank_right;
	/** 底部排名栏 -> 用户名**/
	private TextView tv_my_rank_middle;
	/** 底部排名栏 -> 整体**/
	private LinearLayout ll_my_rank;
	/** 周次信息popwindow的背景**/
	private LinearLayout ll_popupwindow_bg;
	/** 周次信息->显示栏**/
	private TextView tv_spanner_text;
	/** 周排行**/
	private ListView lv_rank_list;
	/** 用于展示的周榜信息**/
	private List<RankingListBean> mList;
	/** 上次请求的周榜信息 为了防止重复请求**/
	private List<RankingListBean> tempMList;
	/** 用于展示的周期次信息**/
	private List<String> dateList;
	/** 上次请求的周榜信息 为了防止重复请求**/
	private List<String> tempDateList;
	/** 用户ID**/
	private String userId;
	/** 底部排名栏 -> 排名数据**/
	private String theRanking;
	/** 底部排名栏 -> 用户名数据**/
	private String theUserName;
	/** 底部排名栏 -> 盈利乐米数据 **/
	private String theWinAmount;
	/** 期次信息的popupwindow**/
	private PopupWnd_RankList_W_Date mPopupWindow;
	/** 数据适配器**/
	private MyAdapter myAdapter;
	/** 期次信息+下拉箭头 整体的布局**/
	private LinearLayout ll_form_a_spinner;
	// private ListView lv_date_list;
	/** 请求期次信息完成的监听**/
	private loadWeekListFinished mListener;
	
	/** 我的Handler**/
	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case RANKING_ELEVEN:
				//我的排行10+
				ll_my_rank.setVisibility(View.VISIBLE);

				tv_my_rank_left.setText(theRanking);
				tv_my_rank_middle.setText(theUserName);
				tv_my_rank_right.setText(theWinAmount);

				break;

			default:
				ll_my_rank.setVisibility(View.GONE);
				break;
			}

		};

	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// 步骤1：在View为空的时候，加载布局（带pulltorefresh的fragment布局）
		if (mView == null) {
			mView = View.inflate(getActivity(),
					R.layout.fragment_ranking_list_w, null);
		} else {
			ViewGroup group = (ViewGroup) mView.getParent();
			if (group != null) {
				group.removeView(mView);
			}
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {

			initCurrentView();
			//
			// adapter = new GuessRecordAdapter(getActivity(), null);
			// super.initLoad(mPullToRefreshListView, adapter);
		}
		return mView;
	}

	/**
	 * 初始化List和popupwindow
	 */
	private void initDate() {
		dateList = new ArrayList<String>();
		mPopupWindow = new PopupWnd_RankList_W_Date(getActivity(),
				ll_popupwindow_bg);
	}

	@Override
	public void onResume() {
		super.onResume();
		initDate();
		initEvent();
		GameRankIngListActivity activity = (GameRankIngListActivity) getActivity();
		tempDateList = new ArrayList<String>();
		tempMList = new ArrayList<RankingListBean>();

		tempDateList = activity.returnListWeek();
		tempMList = activity.returnRankListW();
		if(tempDateList != null && tempDateList.size() > 0){
			//不发送请求
			dateList.addAll(tempDateList);
		}else {
			//发送请求
			sendHttpRequest();
		}

	}

	@Override
	public void onPause() {
		super.onPause();
//		mList = null;
//		Controller.getInstance().removeCallback(dateCallBack);
//		Controller.getInstance().removeCallback(mBack);
		
	}

	@Override
	public void onStop() {
		super.onStop();
//		mList = null;
		//2016-9-19 修改记录 将removeCallBack放到onStop()中进行
		Controller.getInstance().removeCallback(dateCallBack);
		Controller.getInstance().removeCallback(mBack);
	}

	private void sendHttpRequest() {

		// 获取期次信息
		Controller.getInstance().getRankListDate(
				GlobalConstants.URL_GET_RANKING_LIST_DATE, "12", "1",
				dateCallBack);
	}

	private void initCurrentView() {
		
		tv_my_rank_left = (TextView) mView.findViewById(R.id.tv_my_rank_left);
		tv_my_rank_right = (TextView) mView.findViewById(R.id.tv_my_rank_right);
		tv_my_rank_middle = (TextView) mView
				.findViewById(R.id.tv_my_rank_middle);
		lv_rank_list = (ListView) mView.findViewById(R.id.lv_rank_list);
		ll_my_rank = (LinearLayout) mView.findViewById(R.id.ll_my_rank);
		ll_form_a_spinner = (LinearLayout) mView
				.findViewById(R.id.ll_form_a_spinner);
		// lv_date_list = (ListView) mView.findViewById(R.id.lv_date_list);
		ll_popupwindow_bg = (LinearLayout) mView
				.findViewById(R.id.ll_popupwindow_bg);
		tv_spanner_text = (TextView) mView.findViewById(R.id.tv_spanner_text);
		mList = new ArrayList<RankingListBean>();
		myAdapter = new MyAdapter();
		lv_rank_list.setAdapter(myAdapter);
	}

	/**
	 * 初始化监听
	 */
	private void initEvent() {
		ll_form_a_spinner.setOnClickListener(this);

		mPopupWindow.setOnDateSelectedListener(new onDateSelecteListener() {

			@Override
			public void dateSelected(String date) {
				// 用户点击了相应期次的按钮
				// 获取用户排行榜-周
				Controller.getInstance().myRankingListW(
								GlobalConstants.URL_GET_MY_RANKING_LIST_W,
								App.userInfo == null
										|| TextUtils.isEmpty(App.userInfo
												.getUserId()) ? ""
										: App.userInfo.getUserId(), date, mBack);

				tv_spanner_text.setText("第" + date + "期");
				mPopupWindow.hideWindow();

			}
		});
	}

	public RankingListWFragment() {
	}

	@Override
	protected void loadData(int page) {

	}

	@Override
	public void loadData(int title, int page) {

	}

	@Override
	protected void lazyLoad() {

	}

	// 排行榜信息-周 榜单信息
	private CallBack mBack = new CallBack() {

		public void resultSuccessStr(final String json) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					getResponseJson(json);
				}

			});

		};

		public void resultFailure(final String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(getActivity(), error);
				}
			});
		};

	};

	// 排行榜信息-周 期次信息
	private CallBack dateCallBack = new CallBack() {

		// 解析成功
		public void resultSuccessStr(final String json) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					getResponseJsonWeek(json);
					
					if(tempMList != null && tempMList.size() >0){
						//不发送请求
						mList.addAll(tempMList);
					}else {
						// 获取用户排行榜-周
						Controller.getInstance().myRankingListW(
								GlobalConstants.URL_GET_MY_RANKING_LIST_W,
								App.userInfo == null
										|| TextUtils.isEmpty(App.userInfo
												.getUserId()) ? "" : App.userInfo
										.getUserId(),//
								dateList == null || dateList.size() == 0 ? "1"
										: dateList.get(0), //
								mBack);
					}
				}

			});
		};

		// 解析失败
		public void resultFailure(final String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(getActivity(), error);
					Controller.getInstance().myRankingListW(
							GlobalConstants.URL_GET_MY_RANKING_LIST_W,
							App.userInfo == null
									|| TextUtils.isEmpty(App.userInfo
											.getUserId()) ? "" : App.userInfo
									.getUserId(), "30", mBack);
				}
			});

		};
	};

	/**
	 * 解析返回的json串
	 * 
	 * @param json
	 */
	private void getResponseJson(String json) {

		try {
			if (mList != null || mList.size() > 0) {
				mList.clear();
			}
			JSONObject object = new JSONObject(json);
			String resCode = object.getString("resCode");
			String resMessage = object.getString("resMsg");
			if (resCode != null && resCode.equals("0")) {
				if (object.getString("list") != null
						&& !TextUtils.isEmpty(object.getString("list"))) {
					JSONArray array = new JSONArray(object.getString("list"));
					int size = array.length();
					for (int i = 0; i < size; i++) {
						JSONObject obj = array.getJSONObject(i);
						RankingListBean bean = new RankingListBean();
						if (i < 10) {
							// 填充userId
							userId = obj.getString("userId");
							bean.setUserId(userId);
							// 填充排行
							bean.setRanking(obj.getString("ranking"));
							// 填充用户名
							bean.setUserName(obj.getString("userName"));
							// 填充盈利乐米
							bean.setWinAmount(obj.getString("winAmount"));
							if (App.userInfo == null
									|| TextUtils.isEmpty(App.userInfo
											.getUserId())) {
								bean.setInTheRankingList(false);
							} else {
								if (userId.equals(App.userInfo.getUserId())) {
									bean.setInTheRankingList(true);
								} else {
									bean.setInTheRankingList(false);
								}
							}
							mList.add(bean);
							mHandler.sendEmptyMessage(RANKING_WITHIN_TEN);
						} else if (i == 10) {
							theRanking = obj.getString("ranking");
							theUserName = obj.getString("userName");
							theWinAmount = obj.getString("winAmount");
							mHandler.sendEmptyMessage(RANKING_ELEVEN);
						}
					}
					mListener.getWeekList(mList);
				}
			} else if (resCode.equals("3002")) {
				load();
			} else {
				ToastUtil.shortToast(getActivity(), resMessage);
				mList.clear();
				theRanking = "";
				theUserName = "";
				theWinAmount = "";
				mHandler.sendEmptyMessage(RESPONSE_FAILED);
			}
			myAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 解析期次信息的json串
	 * 
	 * @param json
	 */
	private void getResponseJsonWeek(String json) {

		try {
			JSONObject object = new JSONObject(json);
			String resCode = object.getString("resCode");
			if (resCode != null && !TextUtils.isEmpty(resCode)
					&& resCode.equals("0")) {
				// 返回成功
				String result = object.getString("list");
				if (result != null && !TextUtils.isEmpty(result)) {
					JSONArray array = new JSONArray(result);
					int length = array.length();
					for (int i = 0; i < length; i++) {
						JSONObject obj = array.getJSONObject(i);
						String week = obj.getString("issue");
						// String startTime = obj.getString("startTimeStr");
						// String endTime = obj.getString("endTimeStr");
						if (week != null && !TextUtils.isEmpty(week)) {
							dateList.add(week);
						}
					}
					if (dateList != null && dateList.size() != 0) {
						tv_spanner_text.setText("第" + dateList.get(0) + "期");
					} else {
						tv_spanner_text.setText("暂无记录信息");
						// tv_spanner_text.setEnabled(false);
					}

				}
				
				mListener.getWeek(dateList);
			}
		} catch (JSONException e) {
//			Log.e("qifueng", "返回码：" + e.toString());
			e.printStackTrace();
		}

	}

	/**
	 * 登陆超时的处理
	 */
	private void load() {

		SharedPreferences shared = getActivity().getSharedPreferences(
				GlobalConstants.PATH_SHARED_MAC,
				android.content.Context.MODE_PRIVATE);
		int loginPattern = shared.getInt("loginpattern", 0);
		String openId = shared.getString("openid", "");
		boolean auto = shared.getBoolean("auto", false);
		if (loginPattern == 1 && auto) {
			// 之前的登陆状态为微信
			Controller.getInstance().loginWithOpenid(
					GlobalConstants.URL_WECHAT_LOAD, openId, new CallBack() {
						/** 使用openid登陆微信成功 **/
						public void loginWithOpenidSuccess(String json) {
							try {
								JSONObject jsonObject = new JSONObject(json);
								String resCode = jsonObject
										.getString("resCode");
								if (resCode != null && resCode.equals("0")) {
									App.userInfo = (UserInfo) Parser
											.parse(cn.com.cimgroup.protocol.Command.LOGIN,
													jsonObject);
									UserLogic.getInstance().saveUserInfo(
											App.userInfo);
									Controller
											.getInstance()
											.myRankingListW(
													GlobalConstants.URL_GET_MY_RANKING_LIST_W,
													App.userInfo == null
															|| TextUtils
																	.isEmpty(App.userInfo
																			.getUserId()) ? ""
															: App.userInfo
																	.getUserId(),
													"", mBack);
								}
							} catch (JSONException e) {
								e.printStackTrace();
							} catch (CException e) {
								e.printStackTrace();
							}
						}

						/** 使用openid登陆微信失败 **/
						public void loginWithOpenidFailure(final String json) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.shortToast(getActivity(), json);
								}
							});
						}
					});
		} else if (loginPattern == 2 && auto) {
			// 之前的登陆状态为自营
			Controller.getInstance().login(GlobalConstants.NUM_LOGIN,
					App.userInfo.getUserName(), App.userInfo.getPassword(),
					new CallBack() {
						@Override
						public void loginSuccess(UserInfo info) {
							if (info != null && info.getResCode().equals("0")) {
								String password = App.userInfo.getPassword();
								App.userInfo = info;
								App.userInfo.setPassword(password);
								// 将用户信息保存到配置文件中
								UserLogic.getInstance().saveUserInfo(
										App.userInfo);
								Controller
										.getInstance()
										.myRankingListW(
												GlobalConstants.URL_GET_MY_RANKING_LIST_W,
												App.userInfo == null
														|| TextUtils
																.isEmpty(App.userInfo
																		.getUserId()) ? ""
														: App.userInfo
																.getUserId(),
												"", mBack);
							}
						}

						@Override
						public void loginFailure(final String error) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.shortToast(getActivity(), error);
								}
							});
						}
					});
		} else if (!auto) {
			MainActivity mainActivity = (MainActivity) ActivityManager
					.isExistsActivity(MainActivity.class);
			if (mainActivity != null) {
				// 如果存在MainActivity实例，那么展示LoboHallFrament页面
				Intent intent = new Intent(mainActivity, LoginActivity.class);
				intent.putExtra("callthepage", CallThePage.LOBOHALL);
				mainActivity.startActivity(intent);
			} else {
				LoginActivity.forwartLoginActivity(App.getInstance(),
						CallThePage.LOBOHALL);
			}
		}

	}

	/**
	 * 周排行成绩适配器
	 * 
	 * @author 洪林
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {

			ViewHolder mHolder;
			if (view == null) {
				view = View.inflate(getActivity(),
						R.layout.fragment_ranking_list_list, null);
				mHolder = new ViewHolder();

				mHolder.tv_rank = (TextView) view.findViewById(R.id.tv_rank);
				mHolder.tv_user_name = (TextView) view
						.findViewById(R.id.tv_user_name);
				mHolder.tv_lemi_amount = (TextView) view
						.findViewById(R.id.tv_lemi_amount);

				view.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) view.getTag();
			}
			RankingListBean bean = mList.get(position);
			if (bean.inTheRankingList) {
				mHolder.tv_rank.setTextColor(getResources().getColor(
						R.color.color_green_warm));
				mHolder.tv_user_name.setTextColor(getResources().getColor(
						R.color.color_green_warm));
				mHolder.tv_lemi_amount.setTextColor(getResources().getColor(
						R.color.color_red));
			} else {
				mHolder.tv_rank.setTextColor(getResources().getColor(
						R.color.color_gray_secondary));
				mHolder.tv_user_name.setTextColor(getResources().getColor(
						R.color.color_gray_secondary));
				mHolder.tv_lemi_amount.setTextColor(getResources().getColor(
						R.color.color_gray_secondary));
			}
			mHolder.tv_rank.setText(bean.getRanking());
			mHolder.tv_user_name.setText(bean.getUserName());
			mHolder.tv_lemi_amount.setText("+" + bean.getWinAmount());

			return view;
		}

	}

	private static class ViewHolder {

		/** 用户排行**/
		public TextView tv_rank;
		/** 用户名**/
		public TextView tv_user_name;
		/** 盈利乐米**/
		public TextView tv_lemi_amount;

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_form_a_spinner:

			mPopupWindow.setDateList(dateList);
			mPopupWindow.show2hideWindow(ll_form_a_spinner);

			break;

		default:
			break;
		}

	}
	
	/** 接口--周排行信息获取完成**/
	public interface loadWeekListFinished{
		public void getWeekList(List<RankingListBean> rankList);
		public void getWeek(List<String> weekList);
	}
	
	public void setloadWeekListFinished(loadWeekListFinished listener){
		this.mListener = listener;
	}
	

}
