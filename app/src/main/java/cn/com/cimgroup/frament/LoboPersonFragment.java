package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.R;
import cn.com.cimgroup.frament.LoginFragment.OnLoginListener;

/**
 * 个人中心
 * 
 * @author 秋风
 * 
 */
@SuppressLint({ "CommitTransaction", "InflateParams" })
public class LoboPersonFragment extends BaseFrament {
	private FragmentManager mManager;
	private View mView;

	private LoginFragment loginFragment;

	private PersonInfoFragment infoFragment;
	/** 标识：login：当前显示login页面：person：当前显示个人信息页面 */
	private String tag = "login";

	private long startTime = 0;

	private boolean mShouldInitialize = true;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_person, null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		if (mManager == null) {
			mManager = getChildFragmentManager();
		}
		if (mShouldInitialize) {
			FragmentTransaction mTransaction = mManager.beginTransaction();
			if (App.userInfo == null) {
				// 如果用户信息为空，则切换登陆界面
				if (null == loginFragment) {
					loginFragment = new LoginFragment();
				}
				mTransaction.add(R.id.id_replace_layout, loginFragment);
				tag = "login";
				loginFragment.setOnLoginListener(new OnLoginListener() {
					@Override
					public void onLogin() {
						FragmentTransaction transaction = mManager
								.beginTransaction();
						if (infoFragment != null && !infoFragment.isAdded()) {
							transaction.hide(loginFragment)
							.show(infoFragment)
							.commitAllowingStateLoss();
						}else {
							infoFragment = new PersonInfoFragment();
							transaction.hide(loginFragment)
								.add(R.id.id_replace_layout, infoFragment)
								.commitAllowingStateLoss();
						}
						tag = "person";
					}
				});
			} else {
				// 如果用户信息不为空即已登陆，切换个人中心界面
				if (null == infoFragment) {
					infoFragment = new PersonInfoFragment();
				}
				mTransaction.replace(R.id.id_replace_layout, infoFragment);
				tag = "person";
			}
			mTransaction.commit();
		}
		return mView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (System.currentTimeMillis() - startTime > 500) {
			resetFragment();
			startTime = System.currentTimeMillis();
		}
	}

	/**
	 * 重置Fragment
	 */
	private void resetFragment() {
		if (tag.equals("person")) {
			if (App.userInfo == null
					|| TextUtils.isEmpty(App.userInfo.getUserId())) {
				if (null == loginFragment) {
					loginFragment = new LoginFragment();
					loginFragment.setOnLoginListener(new OnLoginListener() {
						@Override
						public void onLogin() {
							FragmentTransaction transaction = mManager
									.beginTransaction();
							if (infoFragment != null && !infoFragment.isAdded()) {
								transaction.hide(loginFragment)
								.show(infoFragment)
								.commitAllowingStateLoss();
							}else {
								infoFragment = new PersonInfoFragment();
							transaction.hide(loginFragment)
									.add(R.id.id_replace_layout, infoFragment)
									.commitAllowingStateLoss();
							}
							tag = "person";
						}
					});
				}
				FragmentTransaction transaction = mManager.beginTransaction();
				if (!loginFragment.isAdded()) {
					transaction.hide(infoFragment)
							.add(R.id.id_replace_layout, loginFragment)
							.commitAllowingStateLoss();
				} else {
					transaction.hide(infoFragment).show(loginFragment)
							.commitAllowingStateLoss();
				}
				tag = "login";
				
			}
		} else if (tag.equals("login")) {
			if (App.userInfo != null
					&& !TextUtils.isEmpty(App.userInfo.getUserId())) {
				if (mManager == null) {
					mManager = getChildFragmentManager();
				}
				FragmentTransaction transaction = mManager.beginTransaction();
				if (infoFragment != null && infoFragment.isAdded()) {
					transaction.hide(loginFragment)
					.show(infoFragment)
					.commitAllowingStateLoss();
				}else {
					infoFragment = new PersonInfoFragment();
					transaction.hide(loginFragment)
					.add(R.id.id_replace_layout, infoFragment)
					.commitAllowingStateLoss();
				}
				tag = "person";
			}
		}
	}

	@Override
	protected void lazyLoad() {

	}

	@Override
	public void onPause() {
		super.onPause();
	}

}
