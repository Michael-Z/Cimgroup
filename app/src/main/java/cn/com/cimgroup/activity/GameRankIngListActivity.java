package cn.com.cimgroup.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.bean.RankingListBean;
import cn.com.cimgroup.bean.ShareAddLeMiObj;
import cn.com.cimgroup.bean.ShareObj;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.frament.EmptyFragment;
import cn.com.cimgroup.frament.RankingListCFragment;
import cn.com.cimgroup.frament.RankingListCFragment.loadMyListFinished;
import cn.com.cimgroup.frament.RankingListWFragment;
import cn.com.cimgroup.frament.RankingListWFragment.loadWeekListFinished;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare.PlayMenuItemClick;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.util.thirdSDK.ShareUtils;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 排行榜
 * 
 * @author 洪林
 * 
 */
public class GameRankIngListActivity extends BaseActivity implements
		OnClickListener, PlayMenuItemClick {

	/** 当前排行的按钮**/
	private TextView tv_rank_current;
	/** 周排行的按钮**/
	private TextView tv_rank_week;
	/** fragment的替换背景**/
	private FrameLayout fl_continer;
	/** 标题栏-返回  设置成RelativeLayout为了增大点击范围**/
	private RelativeLayout id_back;
	/** 标题栏-分享 设置成RelativeLayout为了增大点击范围**/
	private RelativeLayout id_share;
	/** fragment的管理器**/
	private FragmentManager fm;
	/** fragment的事务**/
	private FragmentTransaction ft;
	/** 当前排行榜的Fragment**/
	private RankingListCFragment fragmentC;
	/** 周排行的Fragment**/
	private RankingListWFragment fragmentW;
	/** 数据为空的Fragment**/
	private EmptyFragment emptyFragment;

	/** 当前排行的保存List 用于保存fragment的请求数据 避免重复请求数据**/
	private List<RankingListBean> listC = new ArrayList<RankingListBean>();
	/** 周排行的保存List 用于保存fragment的请求数据 避免重复请求数据**/
	private List<RankingListBean> listW = new ArrayList<RankingListBean>();
	/** 周次信息的保存List 用于保存fragment的请求数据 避免重复请求数据**/
	private List<String> listWeek = new ArrayList<String>();

	/** 当前排行是否有数据 true-有 false-没有**/
	private boolean rankingListCFlag = false;
	/** 周次信息是否有数据 true-有 false-没有**/
	private boolean rankingListWFlag = false;

	/** 分享的popupwindow **/
	private PopupWndSwitchShare mPopMenu;
	/** 分享的类型 0：好友; 1：朋友圈 ； 2：QQ空间 ； 3: QQ好友 **/
	private int mType;
	private ShareUtils shareUtils = new ShareUtils();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking_list);

		initView();
		initData();
		
		initEvent();
		sendRequest();
	}

	/** 初始化数据**/
	private void initData() {
		// 初始化碎片管理器
		fm = getSupportFragmentManager();
	}

	/**
	 *  发送请求 如果数据为空 则展示空的fragment
	 */
	private void sendRequest() {
		// 发送当前排行的请求
		showLoadingDialog();
		int type = NetUtil.getNetworkType(GameRankIngListActivity.this);
		if (type == 0) {
			ft = fm.beginTransaction();
			EmptyFragment.setmErrCode(CException.NET_ERROR + "");
			EmptyFragment.setmType(0);
			ft.replace(R.id.fl_continer, emptyFragment);
			ft.commit();

			hideLoadingDialog();
		}else {
			Controller.getInstance().myRankingListC(
				GlobalConstants.URL_GET_MY_RANKING_LIST_C,
				App.userInfo == null
						|| TextUtils.isEmpty(App.userInfo.getUserId()) ? ""
						: App.userInfo.getUserId(), mBack);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		Log.e("wushiqiu", "切换时调用了activity的onresume方法");
	}

	/**
	 * 供当前排行的fragment调用的方法 返回请求好的数据 以防再次请求数据
	 * 
	 * @return 当前排行榜的信息
	 */
	public List<RankingListBean> returnRankListC() {
		return listC;
	}

	/**
	 * 供周排行的fragment调用的方法 返回请求好的数据 以防再次请求数据
	 * 
	 * @return
	 */
	public List<RankingListBean> returnRankListW() {
		return listW;
	}
	/**
	 * 供周排行的fragment调用的方法 返回请求好的数据 以防再次请求数据
	 * 
	 * @return
	 */
	public List<String> returnListWeek() {
		return listWeek;
	}

	/**
	 * 初始化点击事件
	 */
	@SuppressLint("ValidFragment")
	private void initEvent() {
		tv_rank_current.setOnClickListener(this);
		tv_rank_week.setOnClickListener(this);

		findViewById(R.id.id_back).setOnClickListener(this);
		findViewById(R.id.id_share).setOnClickListener(this);

		fragmentC = new RankingListCFragment();
		fragmentW = new RankingListWFragment();
		emptyFragment = new EmptyFragment() {
			
			@Override
			public void loadFirstData() {
				// TODO Auto-generated method stub
				sendRequest();
			}
		};

		// 当前排行信息请求成功的回调
		fragmentC.setOnLoadMyListFinished(new loadMyListFinished() {

			@Override
			public void getMyList(List<RankingListBean> list) {
				listC.addAll(list);
			}
		});
		// 周排行信息请求成功的回调
		fragmentW.setloadWeekListFinished(new loadWeekListFinished() {

			@Override
			public void getWeekList(List<RankingListBean> rankList) {
				listW.addAll(rankList);
			}

			@Override
			public void getWeek(List<String> weekList) {
				listWeek.addAll(weekList);
			}
		});
	}

	/** 进行fragment的加载 **/
	private void replaceFragment() {
		
		// 初始化碎片事务 更新成功，失败保持同时进行
		// 之所以在这里做一次 是因为如果不做 第一次进来页面时没有数据
		ft = fm.beginTransaction();

		if (rankingListCFlag) {
			EmptyFragment.setmErrCode("0");
			EmptyFragment.setmType(3);
			ft.replace(R.id.fl_continer, emptyFragment);
		} else {
			ft.replace(R.id.fl_continer, fragmentC);
		}
		ft.commit();

		hideLoadingDialog();
	}
	
	/**
	 * 初始化视图
	 */
	private void initView() {

		tv_rank_current = (TextView) findViewById(R.id.tv_rank_current);
		tv_rank_week = (TextView) findViewById(R.id.tv_rank_week);
		fl_continer = (FrameLayout) findViewById(R.id.fl_continer);

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_rank_current:
			// 这里可以进行判断 判断的时候 需要先判断点击的这个按钮有没有被点击 而不是判断另外一个
			// 因为当刚进来的时候 右边的按钮虽然是点击状态 但是没有被selected
			tv_rank_week.setClickable(true);
			tv_rank_week.setBackground(getResources().getDrawable(
					R.drawable.bg_shape_green_with_border_right));
			tv_rank_week.setTextColor(getResources().getColor(R.color.color_gray_secondary));

			tv_rank_current.setSelected(true);
			tv_rank_current.setClickable(false);
			tv_rank_current.setBackground(getResources().getDrawable(
					R.drawable.bg_shape_green_no_border_left));
			tv_rank_current
					.setTextColor(getResources().getColor(R.color.color_white));

			if(listC == null){
				sendRequest();
			}else if(listC != null && listC.size()==0 ){
				sendRequest();
			}else {
				ft = fm.beginTransaction();
				ft.replace(R.id.fl_continer, fragmentC);
				ft.commit();
			}

			break;
		case R.id.tv_rank_week:
			// the same to upside

			tv_rank_current.setClickable(true);
			tv_rank_current.setBackground(getResources().getDrawable(
					R.drawable.bg_shape_green_with_border_left));
			tv_rank_current
					.setTextColor(getResources().getColor(R.color.color_gray_secondary));

			tv_rank_week.setSelected(true);
			tv_rank_week.setClickable(false);
			tv_rank_week.setBackground(getResources().getDrawable(
					R.drawable.bg_shape_green_no_border_right));
			tv_rank_week.setTextColor(getResources().getColor(R.color.color_white));

			// 发送周次信息的请求 根据请求 在callback中打开需要的fragment
			Controller.getInstance().getRankListDate(
					GlobalConstants.URL_GET_RANKING_LIST_DATE, "12", "1",
					dateCallBack);

			break;

		case R.id.id_back:
			// 点击了返回
			finish();
			break;
		case R.id.id_share:
			// 点击了分享
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchShare(
						GameRankIngListActivity.this, this);
			}
			mPopMenu.showPopWindow(v);
			break;
		default:
			break;
		}
	}

	@Override
	public void PopShare(int type) {
		mType = type;

		shareUtils.initApi(GameRankIngListActivity.this);
		Controller.getInstance().share(GlobalConstants.NUM_SHARE, "0", "1",
				mBack);
	}

	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onComplete(Object response) {
			if (App.userInfo != null) {
				Controller.getInstance().shareAddLeMi(
						GlobalConstants.NUM_SHAREADDLEMI,
						App.userInfo.getUserId(), "0", "", mBack);
			}
			ToastUtil.shortToast(mActivity, "分享成功");
		}

		@Override
		public void onError(UiError e) {
			ToastUtil.shortToast(mActivity, e.errorMessage);
		}

		@Override
		public void onCancel() {
			ToastUtil.shortToast(mActivity, "取消分享");
		}
	};

	private CallBack mBack = new CallBack() {

		// 获取当前排行成功
		public void resultSuccessStr(final String json) {

			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					getResponseJson(json);
				}

			});

		};

		public void resultFailure(String error) {
		};

		// 分享获取url成功
		public void shareSuccess(final ShareObj obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (obj.getResCode().equals("0")) {
						switch (mType) {
						case 0:
						case 1:
							// 微信分享到_0：好友;_1：朋友圈
							Bitmap bm = BitmapFactory.decodeResource(
									getResources(), R.drawable.e_ui_game_share);
							shareUtils.shareToWX(obj.getTitle(),
									obj.getContent(), obj.getUrl(), bm, mType);
							break;
						case 2:
							// 分享到QQ空间
							shareUtils.shareToQQzone(obj.getTitle(),
									obj.getUrl(), obj.getImgUrl(),
									qqShareListener);
							break;
						case 3:
							// 分享到QQ
							shareUtils.shareToQQ(obj.getTitle(),
									obj.getContent(), obj.getUrl(),
									obj.getImgUrl(), qqShareListener);
							break;
						}
					}
				}
			});
		};

		// 分享获取url失败
		public void shareFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(GameRankIngListActivity.this, error);
				}
			});
		};

		// 分享获取乐米成功
		public void shareAddLeMiSuccess(final ShareAddLeMiObj obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (obj.getResCode().equals("0")) {
						double money=Double.parseDouble(TextUtils.isEmpty(App.userInfo.getIntegralAcnt())?"0":App.userInfo.getIntegralAcnt())+
								Double.parseDouble(TextUtils.isEmpty(obj.getAddIntegral())?"0":obj.getAddIntegral());
						App.userInfo.setIntegralAcnt(money+ "");
						UserLogic.getInstance().saveUserInfo(App.userInfo);
					}
				}
			});
		};

		// 分享获取乐米失败
		public void shareAddLeMiFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(GameRankIngListActivity.this, error);
				}
			});
		};

	};

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
					if (size == 0) {
						// 暂无数据
						rankingListCFlag = true;
					} else {
						// 有数据
					}
					// 加载fragment
					replaceFragment();
				}

			} else if (resCode.equals("3002")) {
				// 登陆超时
				load();
			} else if (resCode.equals("1000000")){
				//暂无数据
				rankingListCFlag = true;
				replaceFragment();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// 排行榜信息-周 期次信息
	private CallBack dateCallBack = new CallBack() {

		// 解析成功
		public void resultSuccessStr(final String json) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					getResponseJsonWeek(json);
				}
			});
		};

		// 解析失败
		public void resultFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
//					ToastUtil.shortToast(GameRankIngListActivity.this, error);
					ft = fm.beginTransaction();
					EmptyFragment.setmErrCode(error);
					EmptyFragment.setmType(0);
					ft.replace(R.id.fl_continer, emptyFragment);
					ft.commit();
				}
			});

		};
	};

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
					if(length == 0){
						//暂无数据
						rankingListWFlag = true;
						ft = fm.beginTransaction();
						EmptyFragment.setmErrCode("0");
						EmptyFragment.setmType(3);
						ft.replace(R.id.fl_continer, emptyFragment);
					}else {
						//有数据
						rankingListWFlag = false;
						ft = fm.beginTransaction();
						ft.replace(R.id.fl_continer, fragmentW);
					}
					ft.commit();
				}
			}else if (resCode.equals("3002")) {
				// 登陆超时
				load();
			} else if (resCode.equals("1000000")){
				//暂无数据
				rankingListWFlag = true;
				ft = fm.beginTransaction();
				EmptyFragment.setmErrCode("0");
				EmptyFragment.setmType(3);
				ft.replace(R.id.fl_continer, emptyFragment);
				ft.commit();
			} else {
				rankingListWFlag = true;
				ft = fm.beginTransaction();
				EmptyFragment.setmErrCode("0");
				EmptyFragment.setmType(3);
				ft.replace(R.id.fl_continer, emptyFragment);
				ft.commit();
			}
		} catch (JSONException e) {
			Log.e("qifueng", "返回码：" + e.toString());
			e.printStackTrace();
		}

	}

	/**
	 * 登陆超时的处理
	 */
	private void load() {

		SharedPreferences shared = getSharedPreferences(
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
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.shortToast(
											GameRankIngListActivity.this, json);
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
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil
											.shortToast(
													GameRankIngListActivity.this,
													error);
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
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == Constants.REQUEST_QQ_SHARE
				|| resultCode == Constants.ACTIVITY_OK) {
			Tencent.onActivityResultData(requestCode, resultCode, data,
					qqShareListener);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
