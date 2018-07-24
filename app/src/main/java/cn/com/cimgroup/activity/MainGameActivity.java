package cn.com.cimgroup.activity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.HallGameInfo;
import cn.com.cimgroup.bean.ShareAddLeMiObj;
import cn.com.cimgroup.bean.ShareObj;
import cn.com.cimgroup.bean.SignList;
import cn.com.cimgroup.bean.SignStatus;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.config.SDConfig;
import cn.com.cimgroup.dailog.PromptDialog_Black_Fillet;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare.PlayMenuItemClick;
import cn.com.cimgroup.response.HallGameResponse;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.util.thirdSDK.ShareUtils;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.ToastUtil;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 娱乐场主页
 * 
 * @author 秋风
 * 
 */
public class MainGameActivity extends BaseActivity implements OnClickListener,
		PlayMenuItemClick {
	/** 娱乐场游戏集合 */
	private List<HallGameInfo> mList;
	/** 游戏列表适配器 */
	private GameListAdapter mAdapter;
	/** 游戏列表ListView */
	private ListView id_game_listview;
	/** 滚动ScrollView */
	private ScrollView id_scrollview;
	/** 签到得乐米 */
	private ImageView id_game_sign;
	/** 乐米剩余 */
	private TextView id_surplus_lemi;
	/** 分享渠道Pop */
	private PopupWndSwitchShare mPopMenu;

	private int mType;
	/** 本地缓存游戏列表 */
	private HallGameResponse mGameResponse;
	// private GameListChannel gameChannel = null;
	/** 游戏列表更新时间 */
	private String timeId;

	private ShareUtils shareUtils = new ShareUtils();

	private int totalCount = DateUtil.getDaysOfMonth(DateUtil.getToYear(),
			DateUtil.getToMonth());
	private String month;
	/**是否是第一次进入  非第一次进入，并且用户未签到 则请求签到信息*/
	private boolean  isFirstInto = true;
	/**用户是否已签到*/
	private boolean isSign = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_game);
		// 获取本地缓存游戏列表
		initGameChannelInfo();
		initView();
		initEvent();
		getGameList();
	}

	/**
	 * 获取本地缓存游戏列表
	 */
	private void initGameChannelInfo() {
		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName("gamelist");
			Object object = SDConfig.getConfiguration().readSDCard();
			if (object != null) {
				mGameResponse = (HallGameResponse) object;
			}
			if (mGameResponse == null || mGameResponse.getList() == null
					|| mGameResponse.getList().size() == 0
					|| mGameResponse.getList().get(0) == null) {
				timeId = "0";
				mList=new ArrayList<HallGameInfo>();
			} else {
				timeId = mGameResponse.getTimeId();
				mList = mGameResponse.getList();
			}
			//防止存储时错误，默认为0
			if (TextUtils.isEmpty(timeId)) {
				timeId="0";
			}
		}
	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_game_sign = (ImageView) findViewById(R.id.id_game_sign);
		id_surplus_lemi = (TextView) findViewById(R.id.id_surplus_lemi);
		id_game_listview = (ListView) findViewById(R.id.id_game_listview);
		id_scrollview = (ScrollView) findViewById(R.id.id_scrollview);

		id_border_left = findViewById(R.id.id_border_left);
		id_border_right = findViewById(R.id.id_border_right);
		minHeight = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_game_left1).getHeight();
		mAdapter = new GameListAdapter();
		id_game_listview.setAdapter(mAdapter);
		if (mList.size()!=0) {
			updateListUi();
		}

	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		findViewById(R.id.id_back).setOnClickListener(this);// 返回
		findViewById(R.id.id_share).setOnClickListener(this);// 分享
		// findViewById(R.id.id_record).setOnClickListener(this);// 竞猜记录
		// findViewById(R.id.id_sort).setOnClickListener(this);// 竞猜排行
		findViewById(R.id.id_explain).setOnClickListener(this);// 玩法介绍
		findViewById(R.id.id_recharge).setOnClickListener(this);// 充值
		findViewById(R.id.id_excharge).setOnClickListener(this);// 兑换
		// findViewById(R.id.id_guess).setOnClickListener(this);// 竞猜游戏
		// findViewById(R.id.id_card).setOnClickListener(this);// 翻牌
		// findViewById(R.id.id_truntable).setOnClickListener(this);// 转盘
		id_game_sign.setOnClickListener(this);// 签到得乐米

	}

	int i = 0;

	@Override
	public void onResume() {
		super.onResume();
		initLemi();
		if (!isFirstInto&&!isSign) {
			getSignList();
		}
		isFirstInto = false;
		
	}

	/**
	 * 获取游戏列表信息
	 */
	private void getGameList() {
		Controller.getInstance().getGameList(GlobalConstants.URL_GET_GAME_LIST,timeId,
				mBack);
	}

	/**
	 * 初始化数据
	 */
	private void initLemi() {
		String moneyStr = "0";
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
			// 获取开放游戏、获取签到状态
			BigDecimal money = new BigDecimal(TextUtils.isEmpty(App.userInfo
					.getIntegralAcnt()) ? "0" : App.userInfo.getIntegralAcnt());

			if (money.doubleValue() > 100000d) {
				moneyStr = money.divide(new BigDecimal("10000"), 2,
						RoundingMode.DOWN).toPlainString()
						+ "万";
			} else
				moneyStr = money.toPlainString();
		}
		id_surplus_lemi.setText("乐米剩余:" + moneyStr + "米");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_back:
			// 返回(跳转)
			finish();
			break;
		case R.id.id_share:
			// 分享(跳转)
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchShare(mActivity, this);
			}
			mPopMenu.showPopWindow(v);
			break;
		// case R.id.id_record:
		// // 竞猜记录(跳转)
		// if (App.userInfo != null
		// && !TextUtils.isEmpty(App.userInfo.getUserId())) {
		// startActivity(GuessRecordActivity.class);
		// } else {
		// startActivity(LoginActivity.class);
		// }
		//
		// break;
		// case R.id.id_sort:
		// // 竞猜排行(跳转)
		// startActivity(GameRankIngListActivity.class);
		// break;
		case R.id.id_explain:
			// 玩法介绍(跳转)
			Intent intent = new Intent();
			intent.putExtra(TextActiity.TYPE, TextActiity.GAMEHELP);
			intent.setClass(MainGameActivity.this, TextActiity.class);
			startActivity(intent);
			break;
		case R.id.id_game_sign:
			// 签到得乐米(请求)
			if (App.userInfo != null
					&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
				sign();
			} else {
				startActivity(LoginActivity.class);
			}

			break;
		case R.id.id_recharge:
			if (App.userInfo != null
					&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
				// 充值(跳转)
				if (!TextUtils.isEmpty(App.userInfo.getRealName())) {
					Intent buyLeMi = new Intent(MainGameActivity.this,
							BuyRedPacketActivity.class);
					buyLeMi.putExtra("type", 0x1007);
					startActivity(buyLeMi);
				} else {
					isValidate();
				}
			} else {
				startActivity(LoginActivity.class);
			}

			break;
		case R.id.id_excharge:
			// 兑换(跳转)
			exchargeLemi();
			break;
		// case R.id.id_guess:
		// // 竞猜游戏(跳转)
		// startActivity(GuessMatchActivity.class);
		// break;
		// case R.id.id_card:
		// startActivity(GameFlopActivity.class);
		// // 翻牌游戏(跳转)
		// break;
		// case R.id.id_truntable:
		// // 大转盘游戏(跳转)
		// startActivity(LuckyPanActivity.class);
		// break;

		default:
			break;
		}

	}

	/**
	 * 签到
	 */
	private void sign() {
		showLoadingDialog();
		Controller.getInstance().sign(GlobalConstants.NUM_SIGN,
				App.userInfo.getUserId(), "",
				DateUtil.getToYear() + "-" + month + "-01",
				DateUtil.getToYear() + "-" + month + "-" + totalCount, "1",
				mBack);
	}

	/**
	 * 获取签到信息
	 */
	private void getSignList() {
		if (DateUtil.getToMonth() <= 9) {
			month = "0" + DateUtil.getToMonth();
		} else {
			month = DateUtil.getToMonth() + "";
		}
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
			Controller.getInstance().getSignList(GlobalConstants.NUM_SIGNLIST,
					App.userInfo.getUserId(),
					DateUtil.getToYear() + "-" + month + "-01",
					DateUtil.getToYear() + "-" + month + "-" + totalCount, "1",
					mBack);
		}

	}

	/**
	 * 兑换乐米
	 */
	private void exchargeLemi() {
		if (App.userInfo != null) {
			Intent convertIntent = new Intent(MainGameActivity.this,
					MessageCenterActivity.class);
			convertIntent.putExtra(MessageCenterActivity.TYPE,
					MessageCenterActivity.LEMICONVERT);
			startActivity(convertIntent);
		} else {
			startActivity(LoginActivity.class);
		}

	}

	/**
	 * 回调
	 */
	private CallBack mBack = new CallBack() {
		public void signSuccess(final SignStatus status) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					if (status != null && status.getResCode() != null) {
						if (status.getResCode().equals("0")) {
							setSingState(true);
							// 签到成功 获取当前乐米数
							Controller.getInstance().getUserInfo(
									GlobalConstants.NUM_GETUSERINFO,
									App.userInfo.getUserId(), mBack);
						} else {
							setSingState(false);
							ToastUtil.shortToast(mActivity, status.getResMsg());
						}
					}
				}
			});
		};

		public void signError(final String error) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(MainGameActivity.this, error);
				}
			});
		};

		public void getSignListSuccess(final SignList signList) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (signList != null && signList.getResCode() != null) {
						if (signList.getResCode().equals("0")) {
							if (signList.getList() != null
									&& signList.getList().size() != 0) {
								if (DateUtil.getToday() == Integer
										.valueOf(TextUtils.isEmpty(signList
												.getList().get(0).getDay()) ? "0"
												: signList.getList().get(0)
														.getDay())) {
									setSingState(true);
									isSign=false;
								} else {
									setSingState(false);
									isSign=true;
								}
							} else {
								setSingState(false);
								isSign=true;
							}
						} else {
							ToastUtil.shortToast(mActivity,
									signList.getResMsg());
							isSign=true;
						}
					}
				}
			});
		};

		public void getSignListError(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(mActivity, error);
					isSign=true;
				}
			});
		};

		public void getUserInfoSuccess(final UserInfo info) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					if (info.getResCode() != null
							&& info.getResCode().equals("0")) {
						if (App.userInfo != null
								&& App.userInfo.getUserId() != null) {
							info.setUserId(App.userInfo.getUserId());
						}
						if (App.userInfo != null
								&& App.userInfo.getPassword() != null) {
							info.setPassword(App.userInfo.getPassword());
						}
						App.userInfo = info;
						// 更新本地文件
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						initLemi();
					}

				}
			});
		};

		public void getUserInfoFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(mActivity, error);
				}
			});
		}

		public void shareSuccess(final ShareObj obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (obj.getResCode().equals("0")) {
						switch (mType) {
						case 0:
						case 1:
							// 微信分享到_0：好友;_1：朋友圈
							// Bitmap
							// bitmap=shareUtils.getBitMBitmap(obj.getImgUrl());
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

		public void shareFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(mActivity, error);
				}
			});
		};

		public void shareAddLeMiSuccess(final ShareAddLeMiObj obj) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (obj.getResCode().equals("0")) {
						double money = Double.parseDouble(TextUtils
								.isEmpty(App.userInfo.getIntegralAcnt()) ? "0"
								: App.userInfo.getIntegralAcnt())
								+ Double.parseDouble(TextUtils.isEmpty(obj
										.getAddIntegral()) ? "0" : obj
										.getAddIntegral());
						App.userInfo.setIntegralAcnt(money + "");
						UserLogic.getInstance().saveUserInfo(App.userInfo);
						// 更新UI
						initLemi();
					}
				}
			});
		};

		public void shareAddLeMiFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(mActivity, error);
				}
			});
		};

		public void getGameListSuccess(final String json) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					try {
						JSONObject object = new JSONObject(json);
						if (mGameResponse== null) {
							mGameResponse=new HallGameResponse();
						}
						mGameResponse.setResCode(object
								.optString("resCode", ""));
						mGameResponse.setResMsg(object.optString("resMsg", ""));
						String time = object.optString("timeId","");
						if ( TextUtils.isEmpty(time)) {
//							if (mGameResponse.getList() == null
//									|| mGameResponse.getList().size() == 0) {
								// 说明服务器未更新接口，走原数据
								// 解析原有数据接口信息
								saveGameList(time, object);
//							}
						} else if (time.equals(timeId)) {
							// 说明没有更改，使用本地数据
						} else {
							// 说明游戏列表已更改，使用服务器返回信息
							saveGameList(time, object);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (mGameResponse.getResCode() != null
							&& mGameResponse.getResCode().equals("0")) {
						mAdapter.notifyDataSetChanged();
						updateListUi();
						// addTestDatas();

					} else {
						ToastUtil.shortToast(mActivity,
								mGameResponse.getResMsg());
					}
					getSignList();
				}
			});
		};

		public void getGameListFailure(String message) {
			getSignList();
		};

	};

	/**
	 * 修改显示区域布局
	 */
	private void updateListUi() {
		ViewUtils.setListViewHeightBasedOnChildren(id_game_listview);

		ViewUtils.measureView(id_scrollview);
		mScrollViewHeight = id_scrollview.getMeasuredHeight();
		LayoutParams lpLeft = (LayoutParams) id_border_left
				.getLayoutParams();
		LayoutParams lpRight = (LayoutParams) id_border_right
				.getLayoutParams();
		int space = mScrollViewHeight - DensityUtils.dip2px(105);
		if (mList != null && mList.size() > 3) {
			lpLeft.height = space;
			lpRight.height = space;
		} else {
			lpLeft.height = minHeight + DensityUtils.dip2px(20);
			lpRight.height = minHeight + DensityUtils.dip2px(20);
		}
		// id_border_left.setBackgroundResource(R.drawable.bitmap_game_v_left);
		id_border_left.setLayoutParams(lpLeft);
		// id_border_right.setBackgroundResource(R.drawable.bitmap_game_v_right);
		id_border_right.setLayoutParams(lpRight);
		id_border_left.invalidate();
		id_border_right.invalidate();
	}

	/**
	 * 解析原有数据接口信息
	 * 
	 * @param object
	 */
	private void getOldGameListInfo(JSONObject object) {
		try {
			JSONArray array = object.getJSONArray("list");
			int size = array.length();
			mList.clear();
			HallGameInfo info = null;
			for (int i = 0; i < size; i++) {
				JSONObject o = array.getJSONObject(i);
				info = new HallGameInfo();
				info.setJumpType(o.optString("jumpType", ""));
				info.setJumpUrl(o.optString("jumpUrl", ""));
				info.setStatus(o.optString("status", ""));
				info.setStatusStr(o.optString("statusStr", ""));
				info.setGameId(o.optString("gameId", ""));
				info.setGameName(o.optString("gameName", ""));
				info.setGameImg(o.optString("gameImg", ""));
				mList.add(info);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析并保存游戏列表
	 * 
	 * @param timeId
	 * @param object
	 */
	private void saveGameList(String timeId, JSONObject object) {
		mGameResponse.setTimeId(timeId);
		getOldGameListInfo(object);
		mGameResponse.setList(mList);
		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName("gamelist");
			SDConfig.getConfiguration().saveToSDCard(mGameResponse);
		}
	}

	private int mScrollViewHeight = 0;
	private View id_border_left;
	private View id_border_right;

	private int minHeight;

	/**
	 * 游戏列表添加测试数据
	 */
	@SuppressWarnings("unused")
	private void addTestDatas() {
		/**
		 * this.gameId = gameId; this.gameName = gameName; this.status = status;
		 * this.statusStr = statusStr; this.jumpType = jumpType; this.jumpUrl =
		 * jumpUrl; this.gameImg = gameImg;
		 */
		mList.add(new HallGameInfo("3", "黑白棋", "0", "0", "0", "0", "0"));
		mList.add(new HallGameInfo("4", "当空接龙", "0", "0", "0", "0", "0"));
		mList.add(new HallGameInfo("5", "扎金花", "0", "0", "0", "0", "0"));

	}

	/**
	 * 设置签到按钮状态
	 * 
	 * @param isSign
	 */
	private void setSingState(boolean isSign) {
		if (isSign) {
			id_game_sign.setImageResource(R.drawable.e_ui_game_sign_c);
			id_game_sign.setEnabled(false);
		} else {
			id_game_sign.setImageResource(R.drawable.e_ui_game_sign_n);
			id_game_sign.setEnabled(true);
		}
	}

	@Override
	public void PopShare(int type) {
		mType = type;

		shareUtils.initApi(MainGameActivity.this);
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

	/**
	 * 账户未完善信息，是否去完善
	 */
	private void isValidate() {
		// final PromptDialog0 dialog = new
		// PromptDialog0(MainGameActivity.this);
		final PromptDialog_Black_Fillet dialog = new PromptDialog_Black_Fillet(
				MainGameActivity.this);
		dialog.setMessage("您未实名认证，请先完成认证？");
		dialog.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.hideDialog();

			}
		});
		dialog.setPositiveButton("去认证", new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.hideDialog();
				startActivity(UserManageActivity.class);
			}
		});
		dialog.showDialog();
	}

	/**
	 * 游戏大厅游戏列表适配器
	 * 
	 * @author 秋风
	 * 
	 */
	private class GameListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList == null ? 0 : mList.size();
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
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(mActivity).inflate(
						R.layout.hall_game_item, parent, false);
				holder = new ViewHolder();
				holder.id_title = (TextView) view.findViewById(R.id.id_title);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();
			final HallGameInfo game = mList.get(position);
			holder.id_title.setText(game.getGameName());
			if (position == 0)
				holder.id_title
						.setBackgroundResource(R.drawable.e_ui_game_first_btn);
			else if (position < mList.size() - 1)
				holder.id_title
						.setBackgroundResource(R.drawable.e_ui_game_second_btn);
			else
				holder.id_title
						.setBackgroundResource(R.drawable.e_ui_game_third_btn);
			holder.id_title.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (game.getGameId()) {
					case "1":
						// 竞猜游戏(跳转)
						if (game.getStatus().equals("0")) {
							if (game.getJumpType().equals("0")) {
								startActivity(GameGuessMatchActivity.class);
							} else {
								jumpToWebView(game.getJumpUrl(),
										game.getGameName());
							}

						}

						// Intent intentx = new Intent(MainGameActivity.this,
						// GameWebViewActivity.class);
						// intentx.putExtra("url",
						// "http://192.168.2.171:7777/disport/guess.html");
						// startActivity(intentx);
						break;
					case "2":
						// 翻牌游戏(跳转)
						if (game.getStatus().equals("0")) {
							if (game.getJumpType().equals("0")) {
								startActivity(GameFlopActivity.class);
							} else {
								jumpToWebView(game.getJumpUrl(),
										game.getGameName());
							}

						}
						break;
					case "3":

						// http://192.168.2.171:7777/disport/andriod.html
//						 jumpToWebView(
//						 "http://192.168.2.171:7777/disport/andriod.html",
//						 game.getGameName());

						// jumpToWebView(
						// url,
						// "走势图");

						// 大转盘游戏(跳转)
						if (game.getStatus().equals("0")) {
							if (game.getJumpType().equals("0")) {
								startActivity(GameLuckyPanActivity.class);
							} else {
								jumpToWebView(game.getJumpUrl(),
										game.getGameName());
							}
						}
						break;
					default:
						if (game.getJumpType().equals("0")) {
							startActivity(GameLuckyPanActivity.class);
						} else {
							jumpToWebView(game.getJumpUrl(), game.getGameName());
						}
						break;
					}
				}

			});
			view.setEnabled(false);
			return view;
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

	/**
	 * 跳转到网页游戏
	 * 
	 * @param url
	 * @param gameName
	 */
	private void jumpToWebView(String url, String gameName) {
		// Intent intents = new Intent(MainGameActivity.this,
		// ZSTWebViewActivity.class);
		Intent intents = new Intent(MainGameActivity.this,
				GameWebViewActivity.class);
		intents.putExtra("url", url);
		intents.putExtra("title", gameName);
		startActivity(intents);

	}

	/** 游戏列表ViewHolder */
	class ViewHolder {
		/** 游戏名称显示 */
		TextView id_title;
	}

}
