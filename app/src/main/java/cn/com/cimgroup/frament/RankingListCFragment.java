package cn.com.cimgroup.frament;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
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
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 娱乐场竞猜即时排行
 * 
 * @author 洪林
 * 
 */
public class RankingListCFragment extends BaseLoadFragment {

	/** 用户排行10+**/
	private static final int RANK_ELEVEN = 0;
	/** 用户排行10名之内**/
	private static final int RANK_WITHIN_TEN = 1;
	/** 用户第一次游戏 **/
	private static final int RANK_FIRST_JOIN = 2;
	private View mView;
	private boolean mShouldInitialize = true;
	/** 加载当前排行数据成功的监听**/
	public loadMyListFinished mListener;

	/**
	 * 用户所在排行榜中的位置 + 获得的总乐米奖金数
	 */
	private TextView tv_rank_location;
	/**
	 * 用户距离上榜所差的乐米数
	 */
	private TextView tv_rank_gap;
	/** 排行榜信息**/
	private ListView lv_rank_list;
	/** 当前排行信息集和（请求回来的）**/
	private List<RankingListBean> mList;
	/** 当前排行信息集和（上次请求的）**/
	private List<RankingListBean> activityList;
	/** 提示信息-盈利乐米**/
	private String theWinAmount;
	/** 提示信息-当前排行**/
	private String theRanking;
	/** 提示信息-距上榜相差金额**/
	private String difWinAmount;
	/** 用户名**/
	private String theUserName;

	/** 榜单中-用户排名**/
	private TextView tv_my_rank_left;
	/** 榜单中-用户名**/
	private TextView tv_my_rank_middle;
	/** 榜单中-盈利乐米**/
	private TextView tv_my_rank_right;
	/** 排名10+后的当前用户信息显示在底部**/
	private LinearLayout ll_rank_bottom;
	/** 标志位-用户首次参加**/
	private boolean firstJoin = true;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			case RANK_ELEVEN:

				ll_rank_bottom.setVisibility(View.VISIBLE);

				tv_my_rank_left.setText(theRanking);
				tv_my_rank_middle.setText(theUserName);
				tv_my_rank_right.setText("+"+theWinAmount);

				tv_rank_location.setText(Html.fromHtml("<font>您当前盈利</font>"
						+ "<font color='"
						+ getResources().getColor(R.color.color_red) + "'>"
						+ theWinAmount + "</font>" + "<font>乐米，据上榜仅差</font>"
						+ "<font color='"
						+ getResources().getColor(R.color.color_red) + "'>"
						+ difWinAmount + "</font>" + "<font>乐米</font>"));
//				tv_rank_gap.setText("继续努力哟！");
//				tv_rank_gap.setVisibility(View.GONE);

				break;

			case RANK_WITHIN_TEN:

				
					//用户参与 并且在前十名
					tv_rank_location.setText(Html.fromHtml("<font>您当前盈利</font>"
							+ "<font color='"
							+ getResources().getColor(R.color.color_red) + "'>"
							+ theWinAmount + "</font>" + "<font>乐米，目前排名第</font>"
							+ "<font color='"
							+ getResources().getColor(R.color.color_green_warm) + "'>"
							+ theRanking + "</font>" + "<font>名</font>"));
//					tv_rank_gap.setText("继续努力哟！");
//					tv_rank_gap.setVisibility(View.GONE);

				break;
			case RANK_FIRST_JOIN:
				
				String difAmount = "";
				if(mList != null && mList.size() > 0 ){
					difAmount = mList.get(mList.size()-1).getWinAmount();
				}
				
				if(firstJoin){
					//用户没有参与过
					tv_rank_location.setText(Html.fromHtml("<font>您当前盈利</font>"
							+ "<font color='"
							+ getResources().getColor(R.color.color_red) + "'>"
							+ "0" + "</font>" + "<font>乐米，距上榜仅差</font>"
							+ "<font color='"
							+ getResources().getColor(R.color.color_red) + "'>"
							+ difAmount + "</font>" + "<font>乐米</font>"));
//					tv_rank_gap.setText("继续努力哟！");
//					tv_rank_gap.setVisibility(View.GONE);
				}
				
				break;
			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		// 步骤1：在View为空的时候，加载布局（带pulltorefresh的fragment布局）
		if (mView == null) {
			mView = View.inflate(getActivity(),
					R.layout.fragment_ranking_list_c, null);
		} else {
			ViewGroup group = (ViewGroup) mView.getParent();
			if (group != null) {
				group.removeView(mView);
			}
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {

			initCurrentView();
			initEvent();
			
			//
			// adapter = new GuessRecordAdapter(getActivity(), null);
			// super.initLoad(mPullToRefreshListView, adapter);
		}
		return mView;
	}

	/**
	 * onresume的时候请求数据
	 */
	@Override
	public void onResume() {
		super.onResume();
//		Log.e("wushiqiu", "进入了onresume方法");
		initDate();
		GameRankIngListActivity activity = (GameRankIngListActivity) getActivity();
		activityList = new ArrayList<RankingListBean>();
		activityList = activity.returnRankListC();
		for (RankingListBean list : activityList) {
//			Log.e("wushiqiu", list.getUserName());
		}
		if(activityList != null && activityList.size() > 0){
			//无需请求
			mList.addAll(activityList);
		}else {
			sendHttprequest();
		}
	}

	/**
	 * onstop的时候清除数据
	 */
	@Override
	public void onStop() {
		super.onStop();
		// Log.e("wushiqiu", "切换排行，调用了onStop方法");
		// mList = null;
		//2016-9-19 修改记录 将removeCallBack放到onStop()中进行
		Controller.getInstance().removeCallback(mBack);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	private void initDate() {

		mList = new ArrayList<RankingListBean>();

	}

	private void sendHttprequest() {

		Controller.getInstance().myRankingListC(
				GlobalConstants.URL_GET_MY_RANKING_LIST_C,
				App.userInfo == null
						|| TextUtils.isEmpty(App.userInfo.getUserId()) ? ""
						: App.userInfo.getUserId(), mBack);

	}

	private void initEvent() {

	}

	private void initCurrentView() {
		tv_rank_location = (TextView) mView.findViewById(R.id.tv_rank_location);
//		tv_rank_gap = (TextView) mView.findViewById(R.id.tv_rank_gap);
		lv_rank_list = (ListView) mView.findViewById(R.id.lv_rank_list);

		tv_my_rank_left = (TextView) mView.findViewById(R.id.tv_my_rank_left);
		tv_my_rank_right = (TextView) mView.findViewById(R.id.tv_my_rank_right);
		tv_my_rank_middle = (TextView) mView
				.findViewById(R.id.tv_my_rank_middle);

		ll_rank_bottom = (LinearLayout) mView.findViewById(R.id.ll_rank_bottom);

		// 如果用户未登陆，则不展示提示文字
		// 如果把判断放到回调里，则文字会一闪而过， 所以判断放在开始处
		if (App.userInfo == null || App.userInfo.getUserId() == null
				|| TextUtils.isEmpty(App.userInfo.getUserId())) {
//			tv_rank_gap.setVisibility(View.INVISIBLE);
			tv_rank_location.setVisibility(View.INVISIBLE);
		}
	}

	public RankingListCFragment() {
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

		public void resultFailure(String error) {
		};

	};

	/**
	 * 解析读取的json字符串
	 * 
	 * @param json
	 */
	private void getResponseJson(String json) {

		try {
			JSONObject object = new JSONObject(json);
			String resCode = object.getString("resCode");
			if (resCode != null && resCode.equals("0")) {
				if (object.getString("list") != null
						&& !object.getString("list").isEmpty()) {
					JSONArray array = new JSONArray(object.getString("list"));
					// List<RankingListBean> lists = new
					// ArrayList<RankingListBean>();
					int size = array.length();
					for (int i = 0; i < size; i++) {
						JSONObject obj = array.getJSONObject(i);
						RankingListBean bean = new RankingListBean();
						if (i < 10) {
							// 设置榜单中的用户名
							bean.setUserName(obj.getString("userName"));
							// 设置榜单中的排行
							bean.setRanking(obj.getString("ranking"));
							// 设置榜单中的用户id
							bean.setUserId(obj.getString("userId"));
							// 设置榜单中的用户盈利乐米
							bean.setWinAmount(obj.getString("winAmount"));
							if (App.userInfo != null
									&& App.userInfo.getUserId() != null
									&& obj.getString("userId").equals(
											App.userInfo.getUserId())) {
								// 用户在前十名 则更新记录
								theWinAmount = obj.getString("winAmount");
								theRanking = obj.getString("ranking");
								difWinAmount = object.getString("difWinAmount");
								theUserName = obj.getString("userName");

								bean.setInTheRankingList(true);
								firstJoin = false;
								Message message = Message.obtain();
								message.what = RANK_WITHIN_TEN;
								handler.sendEmptyMessage(RANK_WITHIN_TEN);
							} else {
								bean.setInTheRankingList(false);
							}
							mList.add(bean);
						} else if (i == 10) {
							// 如果list中数据为11条 那么则表示最后一条为当前用户信息
							theWinAmount = obj.getString("winAmount");
							theRanking = obj.getString("ranking");
							difWinAmount = object.getString("difWinAmount");
							theUserName = obj.getString("userName");

							Message message = Message.obtain();
							message.what = RANK_ELEVEN;
							handler.sendEmptyMessage(RANK_ELEVEN);
						}

					}
					// 使用填充好的lists集和
					// mList.addAll(lists);
					if(mList.size() < 10){
						Message message = Message.obtain();
						message.what = RANK_FIRST_JOIN;
						handler.sendEmptyMessage(RANK_FIRST_JOIN);
					}
					//人为填充6条数据
//					for(int i=0;i<5;i++){
//						RankingListBean bean = new RankingListBean();
//						bean.setUserName("张"+i);
//						// 设置榜单中的排行
//						bean.setRanking(String.valueOf(5+i+1));
//						// 设置榜单中的用户id
//						bean.setUserId("10086"+i);
//						// 设置榜单中的用户盈利乐米
//						bean.setWinAmount(String.valueOf(216-i-1));
//						mList.add(bean);
//					}
//					RankingListBean b = new RankingListBean();
//					b.setUserName("戊时秋");
//					// 设置榜单中的排行
//					b.setRanking("10086");
//					// 设置榜单中的用户id
//					b.setUserId("1990");
//					// 设置榜单中的用户盈利乐米
//					b.setWinAmount("100");
//					mList.add(b);
//					theWinAmount = "100";
//					theRanking = "10086";
//					difWinAmount = "116";
//					theUserName = "戊时秋";
//					Message message = Message.obtain();
//					message.what = RANK_ELEVEN;
//					handler.sendEmptyMessage(RANK_ELEVEN);
					
					
					mListener.getMyList(mList);
					lv_rank_list.setAdapter(new MyAdapter());
				}

			} else if (resCode.equals("3002")) {
				// 登陆超时
				load();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			
			return mList.size() > 10 ? mList.size()- 1 : mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder mHolder;
			if (convertView == null) {
				convertView = View.inflate(getActivity(),
						R.layout.fragment_ranking_list_list, null);
				mHolder = new ViewHolder();
				mHolder.tv_rank = (TextView) convertView
						.findViewById(R.id.tv_rank);
				mHolder.tv_user_name = (TextView) convertView
						.findViewById(R.id.tv_user_name);
				mHolder.tv_lemi_amout = (TextView) convertView
						.findViewById(R.id.tv_lemi_amount);

				convertView.setTag(mHolder);
			} else {
				mHolder = (ViewHolder) convertView.getTag();
			}

			if (mList.get(position).isInTheRankingList()) {
				// mHolder.ll_list.setBackgroundColor(0xddd);
				mHolder.tv_rank.setTextColor(getActivity().getResources()
						.getColor(R.color.color_green_warm));
				mHolder.tv_user_name.setTextColor(getActivity().getResources()
						.getColor(R.color.color_green_warm));
				mHolder.tv_lemi_amout.setTextColor(getActivity().getResources()
						.getColor(R.color.color_red));
			} else {
				mHolder.tv_rank.setTextColor(getActivity().getResources()
						.getColor(R.color.color_gray_secondary));
				mHolder.tv_user_name.setTextColor(getActivity().getResources()
						.getColor(R.color.color_gray_secondary));
				mHolder.tv_lemi_amout.setTextColor(getActivity().getResources()
						.getColor(R.color.color_gray_secondary));
			}

			mHolder.tv_rank.setText(mList.get(position).getRanking());
			mHolder.tv_user_name.setText(mList.get(position).getUserName());
			mHolder.tv_lemi_amout.setText("+"
					+ mList.get(position).getWinAmount());

			return convertView;
		}

	}

	private class ViewHolder {

		/** 排行**/
		private TextView tv_rank;
		/** 用户名**/
		private TextView tv_user_name;
		/** 盈利乐米**/
		private TextView tv_lemi_amout;

	}

	/**
	 * 登陆超时的处理方法
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
											.myRankingListC(
													GlobalConstants.URL_GET_MY_RANKING_LIST_C,
													App.userInfo == null
															|| TextUtils
																	.isEmpty(App.userInfo
																			.getUserId()) ? ""
															: App.userInfo
																	.getUserId(),
													mBack);
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
										.myRankingListC(
												GlobalConstants.URL_GET_MY_RANKING_LIST_C,
												App.userInfo == null
														|| TextUtils
																.isEmpty(App.userInfo
																		.getUserId()) ? ""
														: App.userInfo
																.getUserId(),
												mBack);
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
	 * 加载当前排行信息完成的回调 
	 * @author 83920_000
	 *
	 */
	public interface loadMyListFinished {
		public void getMyList(List<RankingListBean> list);
	}

	/**
	 * 初始化当前排行信息接口的方法
	 * @param listener
	 */
	public void setOnLoadMyListFinished(loadMyListFinished listener) {
		this.mListener = listener;
	}
	
}
