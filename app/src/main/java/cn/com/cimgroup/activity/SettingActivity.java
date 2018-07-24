package cn.com.cimgroup.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.ReChargeChannel;
import cn.com.cimgroup.bean.Upgrade;
import cn.com.cimgroup.config.SDConfig;
import cn.com.cimgroup.dailog.PromptDialog0;
import cn.com.cimgroup.dailog.PromptDialog0.onKeydownListener;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.util.JPushUtils;
import cn.com.cimgroup.xutils.AppUtils;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 设置页面
 * 
 * @author 秋风
 * 
 */
public class SettingActivity extends BaseActivity implements OnClickListener {
	/** 是否显示未填写状态 */
	private TextView id_validate_toast;
	/**设置声音开关*/
	private ToggleButton id_setting_voice;
	/**显示当前版本号*/
	private TextView id_setting_version;
	
	/** okhttp相关参数**/
	private OkHttpClient okHttpClient;
	private String result;
	private OkHttpClient client;
	private double totalProgress;
	private String version;
	
	private PromptDialog0 dialogProgress;
	
	protected static final int UPDATE_PROGRESS = 1;
	
	/**用户是否点击了返回按钮**/
	private boolean flag_back_pressed = false;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			dialogProgress.setOnKeydownListener(new onKeydownListener() {
				
				@Override
				public int onBackClicked() {
					// 返回
					flag_back_pressed = true;
					return 9999;
					
				}
			});
			switch (msg.what) {
			case UPDATE_PROGRESS:
				//版本更新--更新进度框进度
				DecimalFormat df = new DecimalFormat("#.00");
				dialogProgress.setMessage("安装进度为：" + df.format(msg.obj)
						+ "M/" + df.format(totalProgress) + "M");
				dialogProgress.setTitle("正在下载......");
				if (!dialogProgress.isShowing() && !flag_back_pressed) {
					dialogProgress.showDialog();
				}
				dialogProgress.updateProgress((int)((double)msg.obj/totalProgress*100));
				if (totalProgress == (double)msg.obj) {
					Intent intent = new Intent();
					intent.setAction("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					Uri data = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "temp"
							+ version + ".apk"));
					intent.setDataAndType(data,
							"application/vnd.android.package-archive");
					startActivity(intent);
					dialogProgress.hideDialog();
				}
				
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		/**下载相关的okhttp必备--初始化**/
		client = new OkHttpClient();
		initView();
		initEvent();
	}

	/**
	 * 绑定事件
	 */
	private void initView() {

		id_validate_toast = (TextView) findViewById(R.id.id_validate_toast);
		id_setting_voice=(ToggleButton) findViewById(R.id.id_setting_voice);
		id_setting_version=(TextView) findViewById(R.id.id_setting_version);
		
		id_setting_version.setText("当前版本："+AppUtils.getVersionName());
		
	}

	/**
	 * 绑定事件
	 */
	private void initEvent() {
		findViewById(R.id.id_back).setOnClickListener(this);
		findViewById(R.id.id_exit).setOnClickListener(this);
		
		findViewById(R.id.id_call_service).setOnClickListener(this);
		// 个人信息详情
		findViewById(R.id.id_person_info_layout).setOnClickListener(this);
		// 跳转修改密码
		findViewById(R.id.id_update_pwd_layout).setOnClickListener(this);
		// 跳转设置推送
		findViewById(R.id.id_set_push_layout).setOnClickListener(this);
		// 声音开关
		findViewById(R.id.id_set_voice_layout).setOnClickListener(this);
		// 跳转帮助
		findViewById(R.id.id_help_layout).setOnClickListener(this);
		// 检查升级
		findViewById(R.id.id_check_upLevel_layout).setOnClickListener(this);
		// 关于我们
		findViewById(R.id.id_about_us_layout).setOnClickListener(this);
		
		id_setting_voice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				switch (buttonView.getId()) {
				// 音量开关
				case R.id.id_setting_voice:
					AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
					if (isChecked) {
						audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 0, 0);
					} else {
						audioManager.setStreamVolume(AudioManager.STREAM_ALARM, 60, 0);
					}
					break;
				default:
					break;
				}
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
		resetDatas();
	}

	private void resetDatas() {
		if (StringUtil.isEmpty(App.userInfo.getBanktype())
				&& TextUtils.isEmpty(App.userInfo.getBankCardId())) {
			id_validate_toast.setVisibility(View.VISIBLE);
		} else {
			id_validate_toast.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_call_service:
			//拨打客服电话
			callService();
			break;
		case R.id.id_back:
			finish();
			break;
		case R.id.id_exit:
			exit();
			break;
		case R.id.id_person_info_layout:
			// 跳转个人中心
			startActivity(UserManageActivity.class);
			break;
		case R.id.id_set_push_layout:
			// 设置推送开关
			startActivity(CenterPushSetActivity.class);
			break;
		case R.id.id_update_pwd_layout:
			// 修改密码
			startActivity(SetPwdActvity.class);
			break;
		case R.id.id_set_voice_layout:
			// 设置声音

			break;
		case R.id.id_help_layout:
			// 帮助跳转
			Intent it = new Intent(SettingActivity.this, TextActiity.class);
			it.putExtra(TextActiity.TYPE, TextActiity.HELP);
			startActivity(it);
			break;
		case R.id.id_check_upLevel_layout:
			// 升级检查
			checkUpLevel();
			break;
		case R.id.id_about_us_layout:
			// 关于我们
			Intent aboutIt = new Intent(this, TextActiity.class);
			aboutIt.putExtra(TextActiity.TYPE, TextActiity.ABOUT);
			startActivity(aboutIt);
			break;

		default:
			break;
		}
	}
	/**拨打客服电话*/
	private void callService() {
		 Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:010-65617701"));  
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		    startActivity(intent);  
	}

	/**
	 * 检查升级
	 */
	private void checkUpLevel() {
		// 更新下载的提示框
		dialogProgress = new PromptDialog0(SettingActivity.this);
		flag_back_pressed = false;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (TextUtils.isEmpty(width)) {
			width = dm.widthPixels * dm.density + "";
			height = dm.heightPixels * dm.density + "";
		}
		Controller.getInstance().getHallUpgrade(GlobalConstants.NUM_UPGRADE,
				width, height, mBack);

	}

	String width = "";
	String height = "";

	private void exit() {
		App.userInfo = null;
		
		//添加推送退出别名空设置
		JPushUtils.getInstance(mActivity).setJPushAlias(true);
		UserLogic.getInstance().saveUserInfo(App.userInfo);
		// 删除用户设置的自动登陆的信息
		SharedPreferences shared = App.getInstance().getSharedPreferences(
				GlobalConstants.PATH_SHARED_MAC,
				android.content.Context.MODE_PRIVATE);
		Editor editor = shared.edit();
		editor.remove("openid");
		editor.remove("flag");
		editor.remove("rem");
		editor.remove("auto");
		editor.remove("loginpattern");
		editor.commit();
		ToastUtil.shortToast(this,
				getResources().getString(R.string.page_outlogin));
		//删除用户保存的银行卡充值快捷投注
		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName(
					"recharge");
			Object object = SDConfig.getConfiguration().readSDCard();
			ReChargeChannel mChannel = new ReChargeChannel();
			if (object != null) {
				mChannel = (ReChargeChannel) object;
			}
			mChannel.setBankInfos(null);
			mChannel.setTimeId("0");
			SDConfig.getConfiguration().saveToSDCard(mChannel);
		}
		GlobalConstants.isRefreshFragment = true;
		GlobalConstants.isShowLeMiFragment = true;
		GlobalConstants.personGameNo = "ALL";
		GlobalConstants.personEndIndex = 0;
		GlobalConstants.personTagIndex = 0;
		finish();
	}

	private CallBack mBack = new CallBack() {
		/**
		 * 版本更新请求成功
		 */
		@Override
		public void getHallUpgradeSuccess(final Upgrade upgrade) {
			super.getHallUpgradeSuccess(upgrade);
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					String isUpdate = upgrade.getIsUpdate();
					String isReview = upgrade.getIsReview();
					final String version = upgrade.getNewVersion();
					final String downUrl = upgrade.getDownUrl();
					final PromptDialog0 dialog1 = new PromptDialog0(
							SettingActivity.this);
					// 非强制更新，判断是否为最新版本，是否更新，是否在评审
					if (isUpdate != null && isUpdate.equals("y")) {
						// 请求成功
						dialog1.setTitle("软件更新");
						dialog1.setMessage("软件有新版本了，现在更新？");
						dialog1.setNegativeButton(
								"稍后再说",
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										dialog1.hideDialog();
									}
								});
						dialog1.setPositiveButton(
								"立即更新",
								new OnClickListener() {
									public void onClick(View v) {
										// dialog.hideDialog();
										// 判断用户网络状态 wifi-自动下载
										// 其他-提醒用户更换网络
										int type = NetUtil
												.getNetworkType(SettingActivity.this);
										// type = 1 表示已经连接wifi网络
										// 直接帮用户进行下载
										if (type == 1) {
//											downloadAPK(version, downUrl);
											downloadAPK_By_Okhttp(version, downUrl);
											dialog1.setClickable(2, 2);
											dialog1.hideDialog();
										} else if (type == 0) {
											ToastUtil.shortToast(
													SettingActivity.this,
													"您未连接网络，请连接后重试！");
										} else if (type == 2 || type == 3) {
											final PromptDialog0 dialogWlan = new PromptDialog0(
													SettingActivity.this);
											dialogWlan
													.setMessage("您现在处于2G/3G/4G联网模式下，确定使用此网络下载吗？");
											dialogWlan.setPositiveButton("确定",
													new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															// 点击安装
//															downloadAPK(
//																	version,
//																	downUrl);
															downloadAPK_By_Okhttp(version, downUrl);
															dialog1.setClickable(2, 2);
															dialog1.hideDialog();
														}
													});
											dialogWlan.setNegativeButton(
													"连接WIFI",
													new OnClickListener() {
														@Override
														public void onClick(
																View v) {
															// 非wifi网络，打开wifi连接界面
															dialogWlan
																	.hideDialog();
															startActivity(new Intent(
																	Settings.ACTION_WIFI_SETTINGS));
															NetUtil.closeUsertNet(SettingActivity.this);
														}
													});
											dialogWlan.showDialog();
										}
									}

								});
						// dialog.hideDialog();
						dialog1.showDialog();
					} else if (isUpdate != null && isUpdate.equals("n")) {
						// 当前已经是最新版本
						dialog1.setTitle("软件更新");
						dialog1.setMessage("当前已是最新版本！");
						dialog1.setPositiveButton(getString(R.string.btn_ok),
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										dialog1.hideDialog();
									}
								});
						dialog1.setNegativeButton("取消", new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								dialog1.hideDialog();
							}
						});
						dialog1.showDialog();
					}

				}
			});
		}

		/**
		 * 版本更新请求失败
		 */
		@Override
		public void getHallUpgradeError(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					ToastUtil.shortToast(SettingActivity.this, error);
				}
			});
		};

	};

	/**
	 * 下载apk的抽取方法 参数1：程序版本 下载后的文件名即为temp+version 为了避免用户重复下载 参数2：请求返回的donwURL地址
	 * 精确到.apk
	 * 
	 * @param version
	 * @param url
	 */
//	private void downloadAPK(final String version, String url) {
//		// 强制更新，直接进行下载
//		// url =
//		// "http://wap.apk.anzhi.com/data2/apk/201605/24/8a4181ec3ab56cb6084fbe71a39501bb_74636000.apk";
//		HttpUtils http = new HttpUtils();
//		// 更新的提示框保持不变 为红色框+标题 的dialog
//		final PromptDialog0 dialogProgress = new PromptDialog0(
//				SettingActivity.this);
//		http.download(url, "/mnt/sdcard/temp" + version + ".apk",
//				new RequestCallBack<File>() {
//					double currentProgress;
//					double totalProgress;
//					@Override
//					public void onSuccess(ResponseInfo<File> arg0) {
//						// 下载成功后关闭所有对话框，进行软件的安装
//						dialogProgress.hideDialog();
//						// dialog.setCancelable(true);
//						System.out.println("成功安装新版本的软件");
//						Intent intent = new Intent();
//						intent.setAction("android.intent.action.VIEW");
//						intent.addCategory("android.intent.category.DEFAULT");
//						Uri data = Uri.fromFile(new File(Environment
//								.getExternalStorageDirectory(), "temp"
//								+ version + ".apk"));
//						intent.setDataAndType(data,
//								"application/vnd.android.package-archive");
//						startActivity(intent);
//					}
//					@Override
//					public void onFailure(HttpException arg0, String arg1) {
//
//						ToastUtil.shortToast(SettingActivity.this,
//								"下载失败 ，请重新下载");
//						arg0.printStackTrace();
//					}
//					public void onLoading(long total, long current,
//							boolean isUploading) {
//						currentProgress = current / 1024.0 / 1024.0;
//						totalProgress = total / 1024.0 / 1024.0;
//						DecimalFormat df = new DecimalFormat("#.00");
//						dialogProgress.setMessage("安装进度为："
//								+ df.format(currentProgress) + "M/"
//								+ df.format(totalProgress) + "M");
//						dialogProgress.setTitle("正在下载......");
//						dialogProgress.showDialog();
//						super.onLoading(total, current, isUploading);
//					}
//				});
//		dialogProgress.setCancelable(false);
//	}
	
	private void downloadAPK_By_Okhttp (final String version, String url){
		final Request request = new Request.Builder().url(url).build();
		final Call call = client.newCall(request);
		this.version = version;
		call.enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, Response response) throws IOException {
				double currentProgress;
				// 参数准备
				InputStream is = null;
				byte[] buf = new byte[2048];
				int len = 0;
				FileOutputStream fos = null;
				// 请求数据解析
				try {
//					String str = "/mnt/sdcard/" + version + ".apk";
					File file = new File(Environment.getExternalStorageDirectory(), "temp" + version + ".apk");
					double total = response.body().contentLength();
					totalProgress = total / 1024.0 / 1024.0;
//					Log.e("wushiqiu", "total------>" + total);
					long current = 0;
					is = response.body().byteStream();
					fos = new FileOutputStream(file);
					// 更新下载的提示框
					dialogProgress.setTitle("正在下载......");
					dialogProgress.showProgress();
					while ((len = is.read(buf)) != -1 && !flag_back_pressed) {
						current += len;
						currentProgress = current / 1024.0 / 1024.0;
						fos.write(buf, 0, len);
//						Log.e("wushiqiu", "current------>" + current);

						Message msg = Message.obtain();
						msg.what = UPDATE_PROGRESS;
						msg.obj = currentProgress;
						mHandler.sendMessage(msg);


					}

					fos.flush();

				} catch (Exception e) {
				} finally {
					try {
						if (is != null) {
							is.close();
						}
						if (fos != null) {
							fos.close();
						}
					} catch (IOException e) {
						Log.e("wushiqiu", e.toString());
					}
				}
			}
		});

	}

}
