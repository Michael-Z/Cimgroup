package cn.com.cimgroup.activity;

import android.R.color;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
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
import cn.com.cimgroup.adapter.GuessRecordAdapter;
import cn.com.cimgroup.bean.GuessRecordBean;
import cn.com.cimgroup.bean.ShareAddLeMiObj;
import cn.com.cimgroup.bean.ShareObj;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.bean.YlcRaceRecordBean;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare.PlayMenuItemClick;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.thirdSDK.ShareUtils;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.ToastUtil;

public class GuessRecordActivity extends BaseActivity implements
		OnClickListener, PlayMenuItemClick,OnItemClickListener{

	private PullToRefreshListView mPullToRefreshListView;

	private GuessRecordAdapter adapter;
	/** 分享按钮 **/
	private RelativeLayout id_share;
	/** 返回按钮 **/
	private RelativeLayout id_back;

	private int page = 1;
	private int oldPage = 0;
	/** 分享的popupwindow **/
	private PopupWndSwitchShare mPopMenu;
	/** 分享的类型 0：好友; 1：朋友圈 ； 2：QQ空间 ； 3: QQ好友 **/
	private int mType;
	private ShareUtils shareUtils = new ShareUtils();
	/** 数据集合 **/
	private List<GuessRecordBean> mRecordList;
	
	/**本周命中率*/
	private TextView id_win_ratio;
	private ImageView id_image;
	/**本周竞猜次数*/
	private TextView id_times;
	/**本周猜中及开奖次数*/
	private TextView id_lottery_times;
	/**本周猜中次数*/
	private TextView id_win_times;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_guess_record);

		initCurrentView();
		initdata();
		initEvent();
		initLoad();
//		sendRequest();

	 // GuessRecordFragment fragment = new GuessRecordFragment();
	}

	/** 初始化pulltorefresh **/
	private void initLoad() {
		// 设置刷新监听器
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadFirstData();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				loadNextData();
			}

		});
		mPullToRefreshListView.setRefreshing(false);
		// mListView.setEmptyView(View.inflate(mFragmentActivity,
		// R.layout.layout_empty, null));
		mPullToRefreshListView.setMode(Mode.PULL_FROM_START);
		mPullToRefreshListView.setAdapter(adapter);
		mPullToRefreshListView.setOnItemClickListener(this);
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
	private boolean isFirst = true;
	/**加载指定页面数据**/
	protected void loadData(int page) {
		this.page = page;
		int type = NetUtil.getNetworkType(GuessRecordActivity.this);
		if (type == 0) {
			mPullToRefreshListView.setEmptyView(addEmptyView("网络请求超时"));
			mPullToRefreshListView.setRefreshing();
			loadFinish();
			ToastUtil.shortToast(GuessRecordActivity.this, "网络请求超时");
		}else {
			if (isFirst) {
				isFirst= false;
				Controller.getInstance().getMatchGuessWinning(GlobalConstants.URL_GET_GUESS_MATCH_WINNING, App.userInfo.getUserId(), mBack);
			}else {
			// 发送请求-获取用户竞猜记录
				getMyGuessRecord();
			}
			
		}
		
	}
	/**获取竞猜记录列表*/
	private void getMyGuessRecord(){
		Controller.getInstance().myGuessRecord(
				GlobalConstants.URL_GET_GUESS_RECORD,
				App.userInfo == null || App.userInfo.getUserId() == null ? ""
						: App.userInfo.getUserId(), " ", " ", " ", "10",
				page + "", mBack);
	}
	
	/**
	 * 还原页码
	 */
	protected void restorePage() {
		page = oldPage;
	}

	/** 初始化参数 **/
	private void initdata() {
		mRecordList = new ArrayList<GuessRecordBean>();
		adapter = new GuessRecordAdapter(GuessRecordActivity.this, null);
	}

	/** 初始化当前视图 **/
	private void initCurrentView() {
		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.list_view);
		LinearLayout header = (LinearLayout) View.inflate(mActivity, R.layout.item_guess_record_header, null);
		id_win_ratio = (TextView) header.findViewById(R.id.id_win_ratio);
		id_image = (ImageView) header.findViewById(R.id.id_image);
		id_times = (TextView) header.findViewById(R.id.id_times);
		id_lottery_times = (TextView) header.findViewById(R.id.id_lottery_times);
		id_win_times = (TextView) header.findViewById(R.id.id_win_times);
		
		mPullToRefreshListView.getRefreshableView().addHeaderView(header);
		
		id_share = (RelativeLayout) findViewById(R.id.id_share);
		id_back = (RelativeLayout) findViewById(R.id.id_back);

	}

	/** 初始化点击事件 **/
	private void initEvent() {
		id_share.setOnClickListener(this);
		id_back.setOnClickListener(this);
	}


	private void loadFinish() {
		hideLoadingDialog();
		mPullToRefreshListView.onRefreshComplete();
	}

	private CallBack mBack = new CallBack() {
		public void getMatchGuessWinningSuccess(final String json) {
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					getJson(json);
					getMyGuessRecord();
				}
			});
		};
		public void getMatchGuessWinningFail(final String message) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					getMyGuessRecord();
				}
			});
		};
		
		

		// 获取竞猜记录成功的回调
		public void resultSuccessStr(final String json) {
			// super.resultSuccessStr(json);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					getResponseInfo(json);
					loadFinish();
				}

			});
		};

		// 获取竞猜记录失败的回调
		public void resultFailure(final String error) {
			// super.resultFailure(error);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					
					ToastUtil.shortToast(GuessRecordActivity.this, error);
					mPullToRefreshListView.setEmptyView(addEmptyView("暂无数据"));
					mPullToRefreshListView.setRefreshing();
					mRecordList.clear();
					loadFinish();
				}
			});
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
					ToastUtil.shortToast(GuessRecordActivity.this, error);
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
					ToastUtil.shortToast(GuessRecordActivity.this, error);
				}
			});
		};
	};
	/**
	 * 处理用户胜率返回数据
	 * @param json
	 */
	private void getJson(String json) {
		try {
			Log.e("qiufeng","本周命中率"+ json);
			JSONObject object = new JSONObject(json);
			String resCode = object.optString("resCode","");
			if (resCode.equals("0")) {
				//剩余乐米
				String lmye = object.optString("lmye","0.0");
				//本周竞猜次数
				String betTimes = object.optString("betTimes","0");
				//猜中次数
				String winTimes = object.optString("winTimes","");
				//本周已开奖次数
				String award = object.optString("award","0");
				//胜率
				int winRatio = object.optInt("winRatio",0);
				if (winRatio > 50) {
					id_image.setImageResource(R.drawable.e_ui_ratio_hight);
				}else
					id_image.setImageResource(R.drawable.e_ui_ratio_low);
				id_win_ratio.setText(Html.fromHtml("本周命中率:"+"<big> "+(winRatio+"%")+"</big>"));
				id_times.setText(String.format("本周总竞猜数：%s次",  betTimes));
				id_lottery_times.setText(String.format("已开奖数：%s次", award));
				id_win_times.setText(String.format("本周猜中：%s次", winTimes));
			}else {
				String resMsg = object.optString("resMsg","");
				ToastUtil.shortToast(mActivity, resMsg);
			}
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 获取中奖率文字颜色
	 * @param winRatio
	 * @return
	 */
	private int getTextColor(double winRatio) {
		if (winRatio < 50) {
			return getResources().getColor(R.color.color_green_warm);
		}else {
			return getResources().getColor(R.color.color_red);
		}
		
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

	@SuppressWarnings({ "unchecked"})
	private void getResponseInfo(String json) {
		try {
//			Log.e("qiufeng", json);
			JSONObject object = new JSONObject(json);
			String resCode = object.getString("resCode");
			if (resCode != null && resCode.equals("0")) {
				JSONArray list = object.getJSONArray("list");
				List<GuessRecordBean> lists = new ArrayList<GuessRecordBean>();
				int size = list.length();
				if(size == 0){
					//暂无数据
//					addEmptyView("暂无数据");
					mPullToRefreshListView.setEmptyView(addEmptyView("暂无数据"));
				}
				for (int i = 0; i < size; i++) {
					JSONObject o = list.getJSONObject(i);
					GuessRecordBean bean = new GuessRecordBean();
					String addTime = o.optString("matchTime", "");
					// 填充timerBean
					bean.setAddTime(addTime);
					bean.setHostCode(o.optString("hostCode","已删除题目"));
					bean.setHostName(o.optString("hostName","已删除题目"));
					// bean.setHostName("中洲队");
					bean.setGuestCode(o.optString("guestCode","已删除题目"));
					bean.setGuestName(o.optString("guestName","已删除题目"));
					// bean.setGuestName("恶魔队");
					// 暂时还没有期号 用日期中截取的年月日拼出来
					String[] temporaryDate = addTime.split(" |-");
					String dateNo = temporaryDate[0] + temporaryDate[1]
							+ temporaryDate[2];
					bean.setDateNo("第" + dateNo + "期");
					bean.setStatus(o.optString("status","已删除题目"));
					bean.setBetAmount(o.optString("betAmount","0"));
					bean.setWinAmount(o.optString("winAmount", ""));
					bean.setReturnAmount(o.optString("returnAmount", ""));
					bean.setContent(o.optString("content",""));
					bean.setQuestion(o.optString("question","题目无效"));
					bean.setLotteryResult(o.optString("lotteryResult", ""));
					
					JSONObject rObject = o.optJSONObject("YlcRaceRecordBean");
					YlcRaceRecordBean getRecordBean = new YlcRaceRecordBean();
					String definitePool="0";
					String negativePool = "0";
					String definiteUsers ="0";
					String negativeUsers = "0";
					if (null != rObject) {
						definitePool = rObject.optString("definitePool", "0");
						negativePool = rObject.optString("negativePool", "0");
						definiteUsers = rObject.optString("definiteUsers", "0");
						negativeUsers = rObject.optString("negativeUsers", "0");
					}
					getRecordBean.setDefinitePool(definitePool);
					getRecordBean.setNegativePool(negativePool);
					getRecordBean.setDefiniteUsers(definiteUsers);
					getRecordBean.setNegativeUsers(negativeUsers);
					
					bean.setRecordBean(getRecordBean);
					lists.add(bean);
				}
				if (isFirstPage()) {
					mRecordList.clear();
				}
				mRecordList.addAll(lists);
				adapter.setDatas(mRecordList);
				mPullToRefreshListView.setRefreshing();
				if (Integer.parseInt(object.getString("total")) > adapter
						.getCount()) {
					mPullToRefreshListView.setMode(Mode.BOTH);
				} 
				;
			} else if (resCode.equals("3002")) {
				// 登陆超时
				load();
			} else if (resCode.equals("1000000")) {
				// 暂无数据
				mPullToRefreshListView.setEmptyView(addEmptyView("暂无数据"));
			}else {
				mPullToRefreshListView.setEmptyView(addEmptyView("暂无数据"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

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
											.myGuessRecord(
													GlobalConstants.URL_GET_GUESS_RECORD,
													App.userInfo == null
															|| App.userInfo
																	.getUserId() == null ? ""
															: App.userInfo
																	.getUserId(),
													" ", " ", " ", "10", "1",
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
											GuessRecordActivity.this, json);
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
										.myGuessRecord(
												GlobalConstants.URL_GET_GUESS_RECORD,
												App.userInfo == null
														|| App.userInfo
																.getUserId() == null ? ""
														: App.userInfo
																.getUserId(),
												" ", " ", " ", "10", "1", mBack);
							}
						}

						@Override
						public void loginFailure(final String error) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_back:
			// 点击标题的返回
			onBackPressed();
			break;
		case R.id.id_share:
			// 点击分享
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchShare(GuessRecordActivity.this,
						this);
			}
			mPopMenu.showPopWindow(v);
			break;
		default:
			break;
		}
	}

	public void PopShare(int type) {
		mType = type;

		shareUtils.initApi(GuessRecordActivity.this);
		Controller.getInstance().share(GlobalConstants.NUM_SHARE, "0", "1",
				mBack);
	}

	/**
	 * 判断是否是首页
	 * 
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月2日
	 */
	protected Boolean isFirstPage() {
		return page == 1;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 == 1) {
			return;
		}
		Intent intent = new Intent(GuessRecordActivity.this,
				GameRecordDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("data", mRecordList.get(arg2 - 2));
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	/** 添加空白显示页**/
	@SuppressWarnings("ResourceType")
	private RelativeLayout addEmptyView(String text){
		
		//初始化relativeLayout 用于放置显示为空的图片和文字
		RelativeLayout myLayout = new RelativeLayout(GuessRecordActivity.this);
		LayoutParams layoutLP = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		myLayout.setLayoutParams(layoutLP);
		myLayout.setBackgroundColor(getResources().getColor(color.white));
		
		//添加 文字 显示
		TextView textView = new TextView(GuessRecordActivity.this);
		textView.setId(100);
		textView.setText(text);
		textView.setTextColor(getResources().getColor(R.color.color_gray_indicator));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		LayoutParams textLP = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		textLP.addRule(RelativeLayout.CENTER_IN_PARENT);
		myLayout.addView(textView, textLP);
		
		//添加 兔子 图片
		ImageView imageView = new ImageView(GuessRecordActivity.this);
		imageView.setId(101);
		imageView.setImageResource(R.drawable.e_ui_game_guess_emty);
		LayoutParams imageLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		imageLP.addRule(RelativeLayout.ABOVE,100);
		imageLP.addRule(RelativeLayout.CENTER_HORIZONTAL);
		imageLP.topMargin = DensityUtils.dip2px(10);
		myLayout.addView(imageView,imageLP);
		return myLayout;
		
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
