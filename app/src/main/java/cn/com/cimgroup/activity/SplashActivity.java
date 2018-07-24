package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Command;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.xutils.AppUtils;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.NetUtil;

/**
 * 欢迎首页
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
@SuppressLint("HandlerLeak")
public class SplashActivity extends BaseActivity {
	
	private SharedPreferences shared;
	
	/** 用户登录请求失败的计数 **/
	public int repCount = 0;

	private CallBack mCallBack = new CallBack() {
		public void sendMaxInfoSuccess(final String str) {
			new Thread() {
				public void run() {
					writeShared(str);
				}
			}.start();
//			isAutoLogin();
		};

		public void sendMaxInfoFailure(final String message) {
			// mActivity.runOnUiThread(new Runnable() {
			//
			// @Override
			// public void run() {
			// Log.e("qifueng", message);
			// ToastUtil.shortToast(mActivity, message);
			// }
			// });
//			isAutoLogin();
		};
		
		// 登录成功的回调，计数+登录
				@Override
				public void loginSuccess(UserInfo info) {
//					Log.e("wushiqiu", "步骤5：登陆回掉成功-自营");
					if (info != null) {
						if (Integer.parseInt(info.getResCode()) == 0) {
							// 操作成功
							if (Integer.parseInt(info.getResCode()) == 0) {
								if (App.userInfo.getPassword() != null) {
									String password = App.userInfo.getPassword();
									App.userInfo = info;
									App.userInfo.setPassword(password);
									// 将用户信息保存到配置文件中
									UserLogic.getInstance().saveUserInfo(App.userInfo);
//									Log.e("wushiqiu", "步骤7：操作成功-自营");
								}
							}
						} else {
							// 操作失败（）
							if (repCount < 3) {
								repCount++;
								initLoginState();
							} else {
//								Intent intent = new Intent();
//								intent.setClass(SplashActivity.this, LoginActivity.class);
//								if (App.userInfo != null) {
//									App.userInfo = null;
//									UserLogic.getInstance().saveUserInfo(App.userInfo);
//								}
//								startActivity(intent);
//								Log.e("wushiqiu", "步骤8：操作失败-自营");
							}
						}
					}
				}

				// 登录失败的回调
				@Override
				public void loginFailure(String error) {
//					Log.e("wushiqiu", "步骤6：登陆回调失败-自营");
				}

				/** 使用openid登陆微信成功 **/
				public void loginWithOpenidSuccess(String json) {
//					Log.e("wushiqiu", "步骤7：登陆回调成功-微信");
					try {
						JSONObject object = new JSONObject(json);
						String resCode = object.getString("resCode");
						if (resCode != null && resCode.equals("0")) {
							App.userInfo = (UserInfo) Parser.parse(Command.LOGIN,
									object);
							UserLogic.getInstance().saveUserInfo(App.userInfo);
//							Log.e("wushiqiu", "步骤9：操作成功-微信");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					} catch (CException e) {
						e.printStackTrace();
					}
				};

				/** 使用openid登陆微信失败 **/
				public void loginWithOpenidFailure(String json) {
//					Log.e("wushiqiu", "步骤8：登陆回调失败-微信");
				};
		
	};

	/**
	 * 写入shared
	 * 
	 * @param str
	 */
	private void writeShared(String str) {
		try {
			if (!TextUtils.isEmpty(str)) {
				JSONObject object = new JSONObject(str);
				String resCode = object.getString("resCode");
				SharedPreferences shared = getSharedPreferences(
						GlobalConstants.PATH_SHARED_MAC, MODE_PRIVATE);
				Editor ed = shared.edit();
				if (resCode != null && resCode.equals("0")) {
					ed.putBoolean("isRegistMac", true);
					ed.putBoolean("isRegistMacOk", true);
				} else {
					ed.putBoolean("isRegistMac", true);

				}
				ed.commit();
			}

		} catch (JSONException e) {
		}

	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		// 判断用户是否自动登陆
//		
//		
//		new Thread() {
//			public void run() {
//				try {
//					// 判断是否是第一次启动app 如果是的话 发送信息
//					SharedPreferences shared = getSharedPreferences(
//							GlobalConstants.PATH_SHARED_MAC, MODE_PRIVATE);
//					if (!shared.getBoolean("isRegistMac", false)
//							&& !shared.getBoolean("isRegistMacOk", false)
//							&& NetUtil.isConnected(mActivity)) {
//						// 发送mac信息10101031
//						// 发送条件isRegistMac：是否发送过（第一次启动发送）;isRegistMacOk:是否发送成功(发送过但未成功依旧发送)
//						Controller.getInstance().sendMaxInfo(
//								GlobalConstants.URL_SEND_MAC,
//								AppUtils.getMacAddress(), AppUtils.getSign(),
//								AppUtils.getPhoneType(), AppUtils.getIMEI(),
//								mCallBack);
//					}else {
//						isAutoLogin();
//					}
//					Thread.sleep(2000);
//					mHandler.sendEmptyMessage(0);
//				} catch (InterruptedException e) {
//				}
//			};
//		}.start();
//	}
	
	private void writeVersion() {
		SharedPreferences version = getSharedPreferences(GlobalConstants.PATH_VERSION, MODE_PRIVATE);
		Editor ed = version.edit();
		ed.putString("version", AppUtils.getVersionName());
		ed.commit();
	};

	/**
	 * 根据用户的设定来判断是否进行自动登录
	 */
	private void isAutoLogin() {
		shared = getSharedPreferences(GlobalConstants.PATH_SHARED_MAC,
				MODE_PRIVATE);
		// 判断用户信息是否存在，是否设置了自动登陆
		if (App.userInfo != null
				&& shared.getString("flag", "").equals("success")
				&& shared.getBoolean("auto", false)) {
//			Log.e("wushiqiu", "步骤1：进入到了自动登陆的判断中");
			initLoginState();
			Editor ed = shared.edit();
			ed.commit();
		} else if (App.userInfo != null && !shared.getBoolean("auto", false)) {
			// 未勾选自动登陆，则退出后再次登陆，清除用户信息
//			Log.e("wushiqiu", "步骤2：用户没有勾选自动登陆");
			App.userInfo = null;
			UserLogic.getInstance().saveUserInfo(App.userInfo);
		}
	}

	/**
	 * 初始化用户信息，进行登陆请求
	 */
	private void initLoginState() {
		if (App.userInfo != null) {
			String username;
			String password;
			int loginPattern = shared.getInt("loginpattern", 0);
			String openid = shared.getString("openid", "");
			if (loginPattern == 1 && !TextUtils.isEmpty(openid)) {
				// 之前用户使用微信账号登录 使用openid登陆微信
//				Log.e("wushiqiu", "步骤3：用户微信登陆，开始发送请求");
				Controller.getInstance().loginWithOpenid(
						GlobalConstants.URL_WECHAT_LOAD, openid, mCallBack);
			} else if (loginPattern == 2) {
				// 之前用户使用自营账号登录
				if (App.userInfo.getUserName() != null
						&& App.userInfo.getPassword() != null) {
//					Log.e("wushiqiu", "步骤4：用户自营登陆，开始发送请求");
					username = App.userInfo.getUserName();
					password = App.userInfo.getPassword();
					Controller.getInstance().login(GlobalConstants.NUM_LOGIN,
							username, password, mCallBack);
				}
			}
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Controller.getInstance().removeCallback(mCallBack);//ShapeTestActivity
			startActivity(new Intent(mActivity, MainActivity.class));
			mActivity.finish();
		};
	};
	
	//图片资源文件  
	private int[] images = { R.drawable.icon_init_start1, R.drawable.icon_init_start2, R.drawable.icon_init_start3 };
	private ViewPager viewPager;
	// 图片放置
	private List<ImageView> iamgeList;
	// 放4个小灰点的线性布局
	private LinearLayout linearLayout;
	private ImageView lan_Iv;
	// 小点之间的距离
	private int pointWidth;
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		int year = 2017;
		int month = 2;
		int day = 11;
		int currentMonth = DateUtil.getToMonth();
		int currentDay = DateUtil.getToday();
		int currentYear = DateUtil.getToYear();
		if ((year - currentYear) == 0 && (month - currentMonth) > 0) {
			setTheme(R.style.AppStartLoad2016);
		} else if ((year - currentYear) == 0 && (month - currentMonth) == 0) {
			if ((day - currentDay) >= 0) {
				setTheme(R.style.AppStartLoad2016);
			} else {
				setTheme(R.style.AppStartLoad);
			}
		} else {
			setTheme(R.style.AppStartLoad);
		}
		super.onCreate(savedInstanceState);
		// 放在setContentView()之前运行
		SharedPreferences version = getSharedPreferences(GlobalConstants.PATH_VERSION, MODE_PRIVATE);
//		if (true) {
		if (!version.getString("version", "").equals(AppUtils.getVersionName()) && GlobalConstants.isNeedOpenInfo) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			
			setContentView(R.layout.activity_splash);
			// 初始化
			initView();
			// 初始化图片和小点，图片的个数和小点的个数要始终一致
			for (int i = 0; i < images.length; i++) {
				ImageView imageView = new ImageView(this);
				imageView.setScaleType(ScaleType.FIT_XY);
				iamgeList.add(imageView);
	
				// 根据图片的个数去放置相应数量的小灰点
				ImageView huiImageView = new ImageView(this);
				huiImageView.setImageResource(R.drawable.icon_init_unselect);
				LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				huiImageView.setLayoutParams(layoutParams);
	
				if (i > 0) {
					// 给除了第一个小点的其它小点左边距
					layoutParams.leftMargin = 20;
				}
	
				linearLayout.addView(huiImageView);
			}
	
			/*
			 * 我们的代码现在都写在onCreate方法中，onCreate在调用的时候，界面底层的绘制工作还没有完成。所以，如果我们直接在onCreate方法里去 拿距离是拿不到
			 * dOnGlobalLayoutListener：在视图树全部都绘制完成的时候调用
			 */
			lan_Iv.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
	
				@Override
				public void onGlobalLayout() {
					// 视图全部绘制完成时，计算得到小点之间的距离
					pointWidth = linearLayout.getChildAt(1).getLeft() - linearLayout.getChildAt(0).getLeft();
//					System.out.println(pointWidth);
				}
			});
			// 绑定适配器
			viewPager.setAdapter(new MyAdapter());
			// 设置图片切换的监听事件
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {
	
				@Override
				public void onPageSelected(int position) {
					// 让滑倒最后一页显示按钮
					if (position == images.length - 1) {
						imageView.setVisibility(View.VISIBLE);
					} else {
						imageView.setVisibility(View.GONE);
	
					}
				}
	
				@Override
				// 在Viewpger的界面不断滑动的时候会触发
				// position：当前滑到了第几页从0开始计数
				public void onPageScrolled(int position, float offset, int arg2) {
					RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) lan_Iv.getLayoutParams();
					// 滑动的百分比乘上小点之间的距离，再加上当前位置乘上距离，即为蓝色小点的左边距
					layoutParams.leftMargin = (int) (pointWidth * offset + position * pointWidth);
					lan_Iv.setLayoutParams(layoutParams);
//					System.out.println("当前滑动的百分比   " + offset);
	
				}
	
				@Override
				public void onPageScrollStateChanged(int state) {
					// TODO Auto-generated method stub
	
				}
			});
			
		
		} else {
			
			new Thread() {
				public void run() {
					try {
						// 判断是否是第一次启动app 如果是的话 发送信息
						SharedPreferences shared = getSharedPreferences(
								GlobalConstants.PATH_SHARED_MAC, MODE_PRIVATE);
						if (!shared.getBoolean("isRegistMac", false)
								&& !shared.getBoolean("isRegistMacOk", false)
								&& NetUtil.isConnected(mActivity)) {
							// 发送mac信息10101031
							// 发送条件isRegistMac：是否发送过（第一次启动发送）;isRegistMacOk:是否发送成功(发送过但未成功依旧发送)
							Controller.getInstance().sendMaxInfo(
									GlobalConstants.URL_SEND_MAC,
									AppUtils.getMacAddress(), AppUtils.getSign(),
									AppUtils.getPhoneType(), AppUtils.getIMEI(),
									mCallBack);
						}else {
							isAutoLogin();
						}
						Thread.sleep(2000);
						mHandler.sendEmptyMessage(0);
					} catch (InterruptedException e) {
					}
				};
			}.start();
		}
	}

	/**
	 * 用于初始化相关控件的方法
	 */
	private void initView() {
		lan_Iv = (ImageView) findViewById(R.id.lan_Iv);
		linearLayout = (LinearLayout) findViewById(R.id.ll);
		imageView = (ImageView) findViewById(R.id.imageView_splash);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				writeVersion();
				startActivity(MainActivity.class);
			}
		});
		viewPager = (ViewPager) findViewById(R.id.vp);
		iamgeList = new ArrayList<ImageView>();
	}

	class MyAdapter extends PagerAdapter {

		@Override
		// 返回ViewPager里面有几页
		public int getCount() {
			// TODO Auto-generated method stub
			return images.length;
		}

		@Override
		// 用于判断是否有对象生成界面
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView imageView = iamgeList.get(position);
			imageView.setImageResource(images[position]);
			container.addView(imageView);

			return imageView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			container.removeView((View) object);
		}

	}

}
