package cn.com.cimgroup;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.logic.UserLogic;

/**
 * tabhost 父类
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年10月21日
 */
public class BaseFramentTabHost extends FragmentTabHost {
	private Context mContext;

	public BaseFramentTabHost(Context context) {
		super(context);
		mContext = context; 
	}

	public BaseFramentTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
	public void setCurrentTab(int index) {
		//进入个人中心时，进行登录判断，如果已经登录那么进行调整个人中心页面、否则跳转登录页面
//		if (index == 3) {
//			// 个人中心
//			UserInfo mUserInfo = UserLogic.getInstance().getDefaultUserInfo();
//			if (mUserInfo == null) {
//				// 当用户信息为空时，说明此账号已经不存在，那么进行登录
//				LoginActivity.forwartLoginActivity(mContext, CallThePage.LOBOPERCENTER);
//			} else {
//				super.setCurrentTab(index);
//			}
//		} else {
//			super.setCurrentTab(index);
//		}
		
		super.setCurrentTab(index);
	}

}
