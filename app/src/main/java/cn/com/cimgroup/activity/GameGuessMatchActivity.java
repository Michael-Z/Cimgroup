package cn.com.cimgroup.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFramentActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.bean.GuessMatchInfo;
import cn.com.cimgroup.bean.QuestionInfo;
import cn.com.cimgroup.bean.ShareAddLeMiObj;
import cn.com.cimgroup.bean.ShareObj;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.frament.EmptyFragment;
import cn.com.cimgroup.frament.GuessMatchFragment;
import cn.com.cimgroup.frament.GuessMatchFragment.MatchInfoListener;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare;
import cn.com.cimgroup.popwindow.PopupWndSwitchShare.PlayMenuItemClick;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.response.GuessMatchResponse;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.thirdSDK.ShareUtils;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 比赛竞猜
 * 
 * @author 秋风
 * 
 */
@SuppressLint("HandlerLeak")
public class GameGuessMatchActivity extends BaseFramentActivity implements
		MatchInfoListener, OnClickListener, PlayMenuItemClick {
	/** ViewPager */
	private ViewPager id_viewpager;
	/** Fragment集合 */
	private List<Fragment> mFragment;
	/** ViewPager 适配器 */
	private MyFragmentPagerAdapter mAdapter;
	/** 竞猜游戏返回信息 */
	private GuessMatchResponse mMatchInfo;

	private List<GuessMatchInfo> mMatchInfos;
	/** 我的乐米 */
	private TextView id_my_lemi;

	/** 当前显示的碎片 */
	private Fragment mShowFragment;

	/** 分享用的工具类 **/
	private ShareUtils shareUtils;
	// /** 头部分享按钮 **/
	// private RelativeLayout id_share;
	/** 头部分享popupwindow **/
	private PopupWndSwitchShare mPopMenu;
	/** 分享类型 **/
	private int mType;

	/** 第一次进入比赛竞猜时给予指引 */
	private RelativeLayout id_guide_layout;
	/** 是否进行玩法指引 */
	private boolean isGuide = true;

	private SharedPreferences mShared;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_guess_match);
		mShared = getSharedPreferences(GlobalConstants.PATH_SHARED_MAC, 0x0000);
		isGuide = mShared.getBoolean("isGameGuideMatch", true);

		initView();
		initEvent();
		initData();
		initDatas();
	}

	private Handler mHandler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 时间倒计时
				Map<String, String> map = (Map<String, String>) msg.obj;
				String index = map.get("index");
				int pageIndex = id_viewpager.getCurrentItem();
				if (pageIndex == Integer.parseInt(index)) {
					long timeNow = System.currentTimeMillis();
					// long timeNow = new Date().getTime();
					long sub = (Long.parseLong(map.get("time")) - timeNow) / 1000;
					if (sub > 0) {
						Message m = mHandler.obtainMessage();
						m.obj = map;
						m.what = 1;
						mHandler.sendMessageDelayed(m, 1000);
						((GuessMatchFragment) mShowFragment).setTime(DateUtil
								.formatSecond(sub));
					} else {
						((GuessMatchFragment) mShowFragment).setTime("本场竞猜已结束");
					}
				}
				break;
			case 2:// 更新界面
				((GuessMatchFragment) mShowFragment).initDatas();
				((GuessMatchFragment) mShowFragment).getMessageInfo();
				break;
			case 3:
				if (isGuide && mMatchInfo != null
						&& mMatchInfo.getList() != null
						&& mMatchInfo.getList().size() != 0) {
					if (isGuide) {// 如果需要进行引导 则进行视图的实例化
						id_guide_layout = (RelativeLayout) findViewById(R.id.id_guide_layout);
						id_guide_layout.setVisibility(View.VISIBLE);
					}
					addGuideView(((GuessMatchFragment) mShowFragment)
							.getViewLocation());
					if (isGuide) {
						Editor ed = mShared.edit();
						ed.putBoolean("isGameGuideMatch", false);
						ed.commit();
					}
					isGuide = false;
				}
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 初始化Fragments碎片
	 */
	@SuppressLint("ValidFragment")
	private void initFragment(String toast) {
		if (toast == null) {
			toast = "哎呦~ToT,来的太巧了，暂时还没有比赛数据呜~";
		}
		mFragment = new ArrayList<Fragment>();

		// EmptyFragment fragment =
		// EmptyFragment.newInstance("哎呦~ToT,来的太巧了，暂时还没有比赛数据呜~");
		// mFragment.add(fragment);

		int size = mMatchInfos.size();

		if (mMatchInfos == null || size == 0) {
			// 没有数据
			EmptyFragment fragment = new EmptyFragment() {
				
				@Override
				public void loadFirstData() {
					// TODO Auto-generated method stub
					sendRequest();
				}
			};
			if (!mFragment.contains(fragment)) {
				mFragment.add(fragment);
			}
			
			// ////////////待测试 （没有比赛返回的时候待测试）
		} else {
			GuessMatchFragment fragment;
			for (int i = 0; i < size; i++) {
				fragment = GuessMatchFragment.newInstance(i + "");
				fragment.setListener(this);
				fragment.setHandler(mHandler);
				mFragment.add(fragment);
			}

			if (mFragment.size() != 0) {
				mShowFragment = mFragment.get(0);
				mHandler.sendEmptyMessageDelayed(2, 200);

			}
		}
		mAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		id_viewpager.setAdapter(mAdapter);
		id_viewpager.setCurrentItem(0);

	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_viewpager = (ViewPager) findViewById(R.id.id_viewpager);
		id_my_lemi = (TextView) findViewById(R.id.id_my_lemi);
	}

	/** 记录点击次数 */
	private int mClickCount = 0;

	/**
	 * 添加引导视图
	 */
	private void addGuideView(final Rect[] rects) {
		final long addStart = System.currentTimeMillis();
		mClickCount = 0;
		Bitmap grideTop = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_guide_guess_top);
		final Bitmap grideMiddle = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_guide_guess_middle);
		final Bitmap grideButtom = BitmapFactory.decodeResource(getResources(),
				R.drawable.e_ui_guide_guess_bottom);

		LayoutParams lpTop = new LayoutParams(grideTop.getWidth(),
				grideTop.getHeight());
		lpTop.topMargin = rects[0].top - DensityUtils.dip2px(20f);
		lpTop.addRule(RelativeLayout.CENTER_HORIZONTAL);
		final ImageView imTop = new ImageView(getApplicationContext());
		imTop.setBackgroundResource(R.drawable.e_ui_guide_guess_top);
		id_guide_layout.addView(imTop, lpTop);

		ObjectAnimator animatorTop = ObjectAnimator.ofFloat(imTop, "alpha", 0f,
				1f);
		animatorTop.setDuration(2000);
		animatorTop.start();

		LayoutParams lpMiddle = new LayoutParams(grideMiddle.getWidth(),
				grideMiddle.getHeight());
		lpMiddle.topMargin = rects[1].top + DensityUtils.dip2px(2f);
		lpMiddle.addRule(RelativeLayout.CENTER_HORIZONTAL);
		final ImageView imMiddle = new ImageView(getApplicationContext());
		imMiddle.setBackgroundResource(R.drawable.e_ui_guide_guess_middle);
		id_guide_layout.addView(imMiddle, lpMiddle);
		imMiddle.setAlpha(0f);

		LayoutParams lpBottom = new LayoutParams(grideButtom.getWidth(),
				grideButtom.getHeight());
		lpBottom.topMargin = rects[2].top - DensityUtils.dip2px(28f);
		lpBottom.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lpBottom.rightMargin = DensityUtils.dip2px(11f);
		final ImageView imBottom = new ImageView(getApplicationContext());
		imBottom.setBackgroundResource(R.drawable.e_ui_guide_guess_bottom);
		id_guide_layout.addView(imBottom, lpBottom);
		imBottom.setAlpha(0f);

		id_guide_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (System.currentTimeMillis() - addStart > 1500) {
					if (mClickCount == 0) {
						id_guide_layout.removeView(imTop);

						ObjectAnimator animatorMiddle = ObjectAnimator.ofFloat(
								imMiddle, "alpha", 0f, 1f);
						animatorMiddle.setDuration(1000);
						animatorMiddle.start();
					} else if (mClickCount == 1) {
						id_guide_layout.removeView(imMiddle);
						ObjectAnimator animatorBottom = ObjectAnimator.ofFloat(
								imBottom, "alpha", 0f, 1f);
						animatorBottom.setDuration(1000);
						animatorBottom.start();

					} else if (mClickCount == 2) {
						id_guide_layout.removeAllViews();
						id_guide_layout.setVisibility(View.GONE);
						id_guide_layout = null;
					}

					mClickCount++;
				}
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		shareUtils = new ShareUtils();
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		findViewById(R.id.id_back).setOnClickListener(this);
		findViewById(R.id.id_record).setOnClickListener(this);
		findViewById(R.id.id_sort).setOnClickListener(this);
		// findViewById(R.id.id_message).setOnClickListener(this);
		// 头部分享按钮
		findViewById(R.id.id_share).setOnClickListener(this);
		id_viewpager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	/**
	 * ViewPagerAdapter
	 * 
	 * @author 秋风
	 * 
	 */
	private class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return mFragment.get(position);
		}

		@Override
		public int getCount() {
			return mFragment.size();
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			mShowFragment = (Fragment) object;
			super.setPrimaryItem(container, position, object);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (mShowFragment != null
				&& mShowFragment instanceof GuessMatchFragment) {
			((GuessMatchFragment) mShowFragment).getMessageInfo();
		}

	}

	/**
	 * 获取数据
	 */
	private void initDatas() {
		setLemi();
		if (mMatchInfos == null) {
			mMatchInfo = new GuessMatchResponse();
			mMatchInfos = new ArrayList<GuessMatchInfo>();
		} else
			mMatchInfos.clear();
		sendRequest();
	}

	/**
	 * 显示乐米
	 */
	public void setLemi() {
		String moneyStr = "0";
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())) {

			BigDecimal money = new BigDecimal(TextUtils.isEmpty(App.userInfo
					.getIntegralAcnt()) ? "0" : App.userInfo.getIntegralAcnt());

			if (money.doubleValue() > 100000d) {
				moneyStr = money.divide(new BigDecimal("10000"), 2,
						RoundingMode.DOWN).toPlainString()
						+ "万";
			} else
				moneyStr = money.toPlainString();
		}
		id_my_lemi.setText("乐米剩余：" + moneyStr + "米");
	}

	/**
	 * 发送请求
	 */
	private void sendRequest() {
		isFragment = false;
		showLoadingDialog();
		Controller.getInstance().getMatchGameInfo(
				GlobalConstants.URL_GET_GUESS_MATCH_INFO,
				"9",
				App.userInfo == null
						|| TextUtils.isEmpty(App.userInfo.getUserId()) ? ""
						: App.userInfo.getUserId(), mBack);
	}

	/** 判断请求是否来自Fragment */
	private boolean isFragment = false;

	/**
	 * 发送请求
	 */
	public void updateRequest() {
		isFragment = true;
		Controller.getInstance().getMatchGameInfo(
				GlobalConstants.URL_GET_GUESS_MATCH_INFO,
				"9",
				App.userInfo == null
						|| TextUtils.isEmpty(App.userInfo.getUserId()) ? ""
						: App.userInfo.getUserId(), mBack);
	}

	/**
	 * 请求回调
	 */
	private CallBack mBack = new CallBack() {
		public void getMatchGameInfoFailure(final String message) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(GameGuessMatchActivity.this, message);
					EmptyFragment.setmErrCode(message);
					EmptyFragment.setmType(0);
					initFragment(message);
				}
			});
		};

		public void getMatchGameInfoSuccess(final String json) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					try {
						hideLoadingDialog();
						JSONObject object = new JSONObject(json);
						mMatchInfo.setResCode(object.optString("resCode", ""));
						mMatchInfo.setResMsg(object.optString("resMsg", ""));
						mMatchInfos.clear();
						GuessMatchInfo match = null;
						JSONArray mArray = object.getJSONArray("list");
						List<QuestionInfo> qList = null;
						int size = mArray.length();
						for (int i = 0; i < size; i++) {
							JSONObject o = mArray.getJSONObject(i);
							match = new GuessMatchInfo();
							match.setGuestName(o.optString("guestName", ""));
							match.setGuestImg(o.optString("guestImg", ""));
							match.setHostName(o.optString("hostName", ""));
							match.setLeagueCode(o.optString("leagueCode", ""));
							match.setMatchName(o.optString("matchName", ""));
							match.setMatchImg(o.optString("matchImg", ""));
							match.setMatchTime(o.optString("matchTime", ""));
							match.setHostImg(o.optString("hostImg", ""));
							qList = new ArrayList<QuestionInfo>();
							QuestionInfo question = null;
							JSONArray qArray = o.getJSONArray("questionList");
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
							match.setQuestionList(qList);
							mMatchInfos.add(match);
						}
						mMatchInfo.setList(mMatchInfos);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if (mMatchInfo != null && mMatchInfo.getResCode() != null) {
						if (mMatchInfo.getResCode().equals("0")) {
							setLemi();// 初始化乐米信息
							mMatchInfos = mMatchInfo.getList();
							if (mMatchInfos != null && mMatchInfos.size() != 0) {
								if (isFragment) {// 如果请求来自Fragment说明不是初始化Fragment的操作
									((GuessMatchFragment) mShowFragment)
											.initDatas();
								} else {
									EmptyFragment.setmErrCode("0");
									EmptyFragment.setmType(8);
									initFragment(null);// 如果返回结果不为空，开始初始化碎片
								}
							}
							// 请求成功,整理数据
						} else if (mMatchInfo.getResCode().equals("3002")) {
							// 登陆超时
							load();
						} else if (mMatchInfo.getResCode().equals("1000000")) {
							// 暂无数据
							EmptyFragment.setmErrCode("0");
							EmptyFragment.setmType(8);
							initFragment(null);
						} else {
							// 处理其他情况
							EmptyFragment.setmErrCode("0");
							EmptyFragment.setmType(8);
							initFragment(null);
						}
					}
				}
			});
		};

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

		public void shareFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(GameGuessMatchActivity.this, error);
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
					}
				}
			});
		};

		public void shareAddLeMiFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.shortToast(GameGuessMatchActivity.this, error);
				}
			});
		};
	};

	/**
	 * 登陆超时,重新登陆
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
									sendRequest();
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
											GameGuessMatchActivity.this, json);
									finish();
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
								sendRequest();
							}
						}

						@Override
						public void loginFailure(final String error) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.shortToast(
											GameGuessMatchActivity.this, error);
									finish();
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
	public GuessMatchInfo getMatchInfo(String fragmentIndex) {
		if (mMatchInfos == null || mMatchInfos.size() == 0) {
			return null;
		}
		return mMatchInfos.get(TextUtils.isEmpty(fragmentIndex) ? 0 : Integer
				.parseInt(fragmentIndex));
	}

	@Override
	public void switchMatch(String fragmentIndex, String switchType) {
		if (!TextUtils.isEmpty(fragmentIndex)) {
			int index = Integer.parseInt(fragmentIndex);

			if (switchType.equals("left")) {
				// 向左侧滑动切换
				if (index > 0) {
					id_viewpager.setCurrentItem(index - 1);
				}
			} else {
				// 向右侧滑动切换
				if (index < mMatchInfos.size() - 1) {
					id_viewpager.setCurrentItem(index + 1);
				}
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_record:
			// 竞猜记录入口
			Intent intent = new Intent();
			if (App.userInfo != null && App.userInfo.getUserId() != null) {
				intent.setClass(GameGuessMatchActivity.this,
						GuessRecordActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(GameGuessMatchActivity.this,
						LoginActivity.class);
				startActivity(intent);
			}

			break;
		// case R.id.id_message:
		// // int index = mShowFragment.mMatchIndex == null ? 0 : Integer
		// // .valueOf(mShowFragment.mMatchIndex);
		// // 留言
		// if (mMatchInfos == null || mMatchInfos.size() == 0) {
		// ToastUtil.shortToast(getApplicationContext(), "暂无比赛信息");
		// } else {
		// Intent messageIntent = new Intent(this,
		// GuessMatchMessageActivity.class);
		// messageIntent.putExtra("isInput", "true");// 设置是否直接弹出输入键盘
		// messageIntent.putExtra("leagueCode",
		// mMatchInfos.get(id_viewpager.getCurrentItem())
		// .getLeagueCode());
		// startActivity(messageIntent);
		// }
		// break;
		case R.id.id_sort:
			// 跳转排行榜
			// startActivity(GameRankIngListActivity.class);
			startActivity(new Intent(GameGuessMatchActivity.this,
					GameRankIngListActivity.class));

			break;
		case R.id.id_back:
			finish();
			break;
		case R.id.id_share:
			// 分享-标题
			if (mPopMenu == null) {
				mPopMenu = new PopupWndSwitchShare(GameGuessMatchActivity.this,
						this);
			}
			mPopMenu.showPopWindow(v);
			break;
		default:
			break;
		}
	}

	/**
	 * 分享按钮点击的回调
	 */
	@Override
	public void PopShare(int type) {

		mType = type;

		shareUtils.initApi(GameGuessMatchActivity.this);
		Controller.getInstance().share(GlobalConstants.NUM_SHARE, "0", "1",
				mBack);
	}

	/**
	 * qq分享的回调
	 */
	IUiListener qqShareListener = new IUiListener() {
		@Override
		public void onComplete(Object response) {
			if (App.userInfo != null) {
				Controller.getInstance().shareAddLeMi(
						GlobalConstants.NUM_SHAREADDLEMI,
						App.userInfo.getUserId(), "0", "", mBack);
			}
			ToastUtil.shortToast(GameGuessMatchActivity.this, "分享成功");
		}

		@Override
		public void onError(UiError e) {
			ToastUtil.shortToast(GameGuessMatchActivity.this, e.errorMessage);
		}

		@Override
		public void onCancel() {
			ToastUtil.shortToast(GameGuessMatchActivity.this, "取消分享");
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if (id_guide_layout != null) {
				// int a = id_guide_layout.getVisibility();
				// int b = View.VISIBLE;
				// if (a != b) {
				// finish();
				// return true;
				// } else
				// return false;
				return false;
			} else {
				finish();
				return true;

			}

		}
		return super.onKeyDown(keyCode, event);
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
