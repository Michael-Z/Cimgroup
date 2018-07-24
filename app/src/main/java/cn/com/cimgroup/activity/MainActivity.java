package cn.com.cimgroup.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFramentActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.frament.LoboDrawFrament;
import cn.com.cimgroup.frament.LoboHallFrament;
import cn.com.cimgroup.frament.LoboPersonFragment;
import cn.com.cimgroup.frament.LoboTogeBuyFrament;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.protocol.Command;
import cn.com.cimgroup.protocol.Parser;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.ToastUtil;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

@SuppressLint("ShowToast")
public class MainActivity extends BaseFramentActivity implements OnClickListener {

	private long mBackPressed;

	private FragmentManager mFragmentManagers;

	/** 自动更新的提示是否第一次显示 **/
	public static boolean isFirstShow = true;

	/** 用户登录请求失败的计数 **/
	public int repCount = 0;

	public static boolean showNotice = true;

	private SharedPreferences shared;

	// 需要展示的frament(用于其他页面回到主页时需要展示的frament,在onResume中执行)
	public int mForwardFrament = 4;
	
	private RadioGroup rg;
	private RadioButton firstBtn;
	private RadioButton secondBtn;
	private RadioButton thirdBtn;
	private RadioButton fourBtn;
	
	public boolean quick_bet_dlt_time_refresh = false;
	public boolean page_has_changed = false;

	public static ViewPager mViewPager;
	private List<Fragment> list = new ArrayList<Fragment>();

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
//        	Window window = getWindow();
//			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//							| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setNavigationBarColor(Color.TRANSPARENT);
//        }
		setContentView(R.layout.activity_main);
		initView();
//		initEvent();
		// 判断用户是否自动登陆
//		 isAutoLogin();

	}

	/**
	 * 初始化页面
	 * 
	 * @Description:
	 * @author:www.wenchuang.com
	 * @date:2015年10月22日
	 */
	private void initView() {
		// 得到fm
		mFragmentManagers = getSupportFragmentManager();
		
		((ImageView) findViewById(R.id.imageView_main_game)).setOnClickListener(this);
		
		rg = (RadioGroup) findViewById(R.id.tab_rg_menu);
		firstBtn = (RadioButton) findViewById(R.id.tab_rb_1);
		secondBtn = (RadioButton) findViewById(R.id.tab_rb_2);
		thirdBtn = (RadioButton) findViewById(R.id.tab_rb_3);
		fourBtn = (RadioButton) findViewById(R.id.tab_rb_4);
		
		firstBtn.setOnClickListener(this);
		secondBtn.setOnClickListener(this);
		thirdBtn.setOnClickListener(this);
		fourBtn.setOnClickListener(this);
		
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		
		firstBtn.setChecked(true);
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.tab_rb_1:
					firstBtn.setChecked(true);
					break;
				case R.id.tab_rb_2:
					secondBtn.setChecked(true);
					break;
				case R.id.tab_rb_3:
					thirdBtn.setChecked(true);
					break;
				case R.id.tab_rb_4:
					fourBtn.setChecked(true);
					break;

				default:
					break;
				}
			}
		});

		LoboHallFrament hallFrament = new LoboHallFrament();
		LoboDrawFrament drawFrament = new LoboDrawFrament();
		LoboTogeBuyFrament togeBuyFrament = new LoboTogeBuyFrament();
		LoboPersonFragment centerFrament = new LoboPersonFragment();
		list.add(hallFrament);
		list.add(drawFrament);
		list.add(togeBuyFrament);
		list.add(centerFrament);
		mViewPager.setAdapter(new MenuAdapter(getSupportFragmentManager()));
		mViewPager.setOnPageChangeListener(new ViewPagerListener());
	}

	@Override
	public void onResume() {

		/**
		 * 如果是从其他页面回到此页面，且要求展示某一个Frament时，进行修改//前提是其他页面进行调用了本页面的showFrament()函数
		 */
		if (mForwardFrament < 4) {
			setCurrentItem(mForwardFrament);
			mForwardFrament = 4;
		}
		super.onResume();
	}
	
	class MenuAdapter extends FragmentPagerAdapter {

		public MenuAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}

	class ViewPagerListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int index) {
			if (index == 0) {
				firstBtn.setChecked(true);
			} else if (index == 1) {
				secondBtn.setChecked(true);
			} else if (index == 2) {
				thirdBtn.setChecked(true);
			}  else if (index == 3) {
				fourBtn.setChecked(true);
			}
			page_has_changed = true;
		}
	}

	public void showFrament(int index) {
		this.mForwardFrament = index;
	}

	/**
	 * 手动选择选项卡
	 * 
	 * @Description:
	 * @param frament
	 * @author:www.wenchuang.com
	 * @date:2015年10月22日
	 */
	public void setCurrentItem(int item) {
		mViewPager.setCurrentItem(item, false);
	}

	
	/**
	 * 点击返回键响应方法
	 * 
	 * @Description:
	 * @param index
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年10月22日
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - mBackPressed) > 2000) {
				ToastUtil.shortToast(MainActivity.this, getResources()
						.getString(R.string.app_exit_hint));
				mBackPressed = System.currentTimeMillis();
			} else {
				ActivityManager.popAll();
				//影响极光推送
//				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
				Controller.getInstance().loginWithOpenid(
						GlobalConstants.URL_WECHAT_LOAD, openid, mBack);
			} else if (loginPattern == 2) {
				// 之前用户使用自营账号登录
				if (App.userInfo.getUserName() != null
						&& App.userInfo.getPassword() != null) {
					username = App.userInfo.getUserName();
					password = App.userInfo.getPassword();
					Controller.getInstance().login(GlobalConstants.NUM_LOGIN,
							username, password, mBack);
				}
			}
		}
	}

	CallBack mBack = new CallBack() {

		// 登录成功的回调，计数+登录
		@Override
		public void loginSuccess(UserInfo info) {
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
						}
					}
				} else {
					// 操作失败（）
					if (repCount < 3) {
						repCount++;
						initLoginState();
					} else {
						Intent intent = new Intent();
						intent.setClass(MainActivity.this, LoginActivity.class);
						if (App.userInfo != null) {
							App.userInfo = null;
							UserLogic.getInstance().saveUserInfo(App.userInfo);
						}
						startActivity(intent);
					}
				}
			}
		}

		// 登录失败的回调
		@Override
		public void loginFailure(String error) {
		}

		/** 使用openid登陆微信成功 **/
		public void loginWithOpenidSuccess(String json) {
			try {
				JSONObject object = new JSONObject(json);
				String resCode = object.getString("resCode");
				if (resCode != null && resCode.equals("0")) {
					App.userInfo = (UserInfo) Parser.parse(Command.LOGIN,
							object);
					UserLogic.getInstance().saveUserInfo(App.userInfo);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (CException e) {
				e.printStackTrace();
			}
		};

		/** 使用openid登陆微信失败 **/
		public void loginWithOpenidFailure(String json) {

		};

		@Override
		public void getQiNiuParamsSuccess(String json) {
			super.getQiNiuParamsSuccess(json);
			try {
				JSONObject object = new JSONObject(json);
				String resCode = object.optString("resCode", "");
				if (resCode.equals("0")) {
					String key = object.optString("key", "");
					String token = object.optString("token", "");
					File file = new File(mImagePath);
					UploadManager uploadManager = new UploadManager();
					// 第一种上传 不现实上传进度
					uploadManager.put(file, key, token,
							new UpCompletionHandler() {
								@Override
								public void complete(
										String key,ResponseInfo info,
										JSONObject res) {
									if (info.isOK()) {
										// 将结果提交给服务器
										mKey = key;
										updateServiceImage(mKey);
									}
								}
							}, null);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void getQiNiuParamsFailure(String error) {
			super.getQiNiuParamsFailure(error);
		}

		@Override
		public void updateUserHeaderImageSuccessStr(String json) {
			super.updateUserHeaderImageSuccessStr(json);
			try {
				JSONObject object = new JSONObject(json);
				String resCode = object.optString("resCode", "");
				if (resCode.equals("0")) {
					String imgUrl=App.userInfo.getHeadImgUrl();
					if (TextUtils.isEmpty(imgUrl)) {
						imgUrl="http://odw2vebhj.bkt.clouddn.com/";
					}
					imgUrl += System.currentTimeMillis();
					App.userInfo.setHeadImgUrl(imgUrl);
					UserLogic.getInstance().saveUserInfo(App.userInfo);
					Intent intent =new Intent();
					intent.setAction("com.lebocp.changephoto");
					intent.putExtra("filePath", "file:///"+mImagePath);
					sendBroadcast(intent);
				} else if (resCode.equals("3002")) {
					// 登陆超时，重新登陆
					load();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	};

	private final int CAMERA_WITH_DATA = 1;
	/** 本地图片选取标志 */
	private static final int FLAG_CHOOSE_IMG = 2;
	/** 截取结束标志 */
	private static final int FLAG_MODIFY_FINISH = 3;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == FLAG_CHOOSE_IMG && resultCode == RESULT_OK) {
			if (data != null) {
				Uri uri = data.getData();
				if (!TextUtils.isEmpty(uri.getAuthority())) {
					Cursor cursor = getContentResolver().query(uri,
							new String[] { MediaStore.Images.Media.DATA },
							null, null, null);
					if (null == cursor) {
						Toast.makeText(getApplicationContext(), "图片没找到", Toast.LENGTH_SHORT)
								.show();
						return;
					}
					cursor.moveToFirst();
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					cursor.close();

					Intent intent = new Intent(this,
							ActivityCutClipPicture.class);
					intent.putExtra("path", path);
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				} else {
					Intent intent = new Intent(this,
							ActivityCutClipPicture.class);
					intent.putExtra("path", uri.getPath());
					startActivityForResult(intent, FLAG_MODIFY_FINISH);
				}
			}
		} else if (requestCode == FLAG_MODIFY_FINISH && resultCode == RESULT_OK) {
			if (data != null) {
				mImagePath = data.getStringExtra("path");
				getQiNiuParams();
//				new Thread() {
//					public void run() {
//						try {
//							sleep(200);
//							int tab = mViewPager.getCurrentItem();
//							Fragment fragment = list.get(tab);
//							((LoboPerCenterFrament) fragment).setState(true);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//
//					};
//				}.start();
			}
		}
		switch (requestCode) {
		case CAMERA_WITH_DATA:
			// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
			startCropImageActivity(Environment.getExternalStorageDirectory()
					+ "/" + TMP_PATH);
			break;
		}
	}

	public String TMP_PATH = "temp.jpg";
	private String mImagePath = "";

	// 裁剪图片的Activity
	private void startCropImageActivity(String path) {
		Intent intent = new Intent(this, ActivityCutClipPicture.class);
		// Intent intent = new Intent(this, ActivityCutPicture.class);
		intent.putExtra("path", path);
		startActivityForResult(intent, FLAG_MODIFY_FINISH);
	}

	/**
	 * 启动相册
	 */
	public void startAlbum() {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, FLAG_CHOOSE_IMG);
	}

	/**
	 * 调起照相功能
	 */
	public void startCamera() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
				Environment.getExternalStorageDirectory(), TMP_PATH)));
		startActivityForResult(intent, CAMERA_WITH_DATA);
	}

	/**
	 * 获取七牛请求参数
	 */
	private void getQiNiuParams() {

		Controller.getInstance().getQiNiuParams(
				GlobalConstants.URL_GET_QINIU_PARAMS, App.userInfo.getUserId(),
				mBack);
	}

	private String mKey = "";

	/**
	 * 将头像上传结果返回给服务器
	 * 
	 * @param key
	 */
	private void updateServiceImage(String key) {
		Controller.getInstance().updateUserHeaderImage(
				GlobalConstants.URL_UPDATE_USER_HEADER_IMAGE,
				App.userInfo.getUserId(), key, mBack);
	}

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
											.parse(Command.LOGIN,
													jsonObject);
									UserLogic.getInstance().saveUserInfo(
											App.userInfo);
									updateServiceImage(mKey);
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
									ToastUtil.shortToast(MainActivity.this,
											json);
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
								updateServiceImage(mKey);
							}
						}

						@Override
						public void loginFailure(final String error) {
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									ToastUtil.shortToast(MainActivity.this,
											error);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tab_rb_1:
			mViewPager.setCurrentItem(0, false);
			break;
		case R.id.tab_rb_2:
			mViewPager.setCurrentItem(1, false);
			break;
		case R.id.tab_rb_3:
			mViewPager.setCurrentItem(2, false);
			break;
		case R.id.tab_rb_4:
			mViewPager.setCurrentItem(3, false);
			break;
		case R.id.imageView_main_game:
			startActivity(new Intent(MainActivity.this, MainGameActivity.class));
			break;
		default:
			break;
		}
	}

}
