package cn.com.cimgroup.xutils;

import java.io.File;
import java.util.Stack;

import android.app.Activity;
import android.net.NetworkInfo.State;

/**
 * activity 管理
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2013-7-24
 */
public class ActivityManager {

	private static Stack<Activity> stack = new Stack<Activity>();// Activity栈

	/**
	 * 移除所有activity
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static void popAll() {
		while (!stack.isEmpty()) {
			pop();
		}
	}

	/**
	 * 移除位于栈顶的activity
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static void pop() {
		Activity activity = stack.pop();
		if (activity != null && !activity.isFinishing()) {
			activity.finish();
		}
	}

	/**
	 * 移除指定activity
	 * 
	 * @Description:
	 * @param activity
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static void pop(Activity activity) {

//		for (Activity a : stack) {
//			if (a.getClass().equals(activity.getClass())) {
//				if (!a.isFinishing()) {
//					a.finish();
//				}
//			}
//		}
		stack.remove(activity);
	}
	
	public static void pop(Class<? extends Activity> activity) {

		for (Activity a : stack) {
			if (a.getClass().equals(activity)) {
				if (!a.isFinishing()) {
					a.finish();
				}
			}
		}
		stack.remove(activity);
	}

	/**
	 * 移除并关闭指定某一类的activity
	 * 
	 * @Description:
	 * @param cls
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static void popClass(Class<? extends Activity> cls) {
		Stack<Activity> newStack = new Stack<Activity>();
		for (Activity a : stack) {
			if (a.getClass().equals(cls)) {
				if (!a.isFinishing()) {
					a.finish();
				}
			} else {
				newStack.push(a);
			}
		}
		stack = newStack;
	}

	/**
	 * 获取在栈顶的activity
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static Activity current() {
		if (stack.isEmpty()) {
			return null;
		}
		return stack.peek();
	}

	/**
	 * 添加activity到栈中
	 * 
	 * @Description:
	 * @param activity
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static void push(Activity activity) {
		stack.push(activity);

	}

	/**
	 * 判断堆栈中是否存在此activity，如果存在则返回当前activity市里
	 * 
	 * @Description:
	 * @param activity
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015-3-2
	 */
	public static Activity isExistsActivity(Class<? extends Activity> activity) {
		for (Activity a : stack) {
			if (a.getClass().equals(activity)) {
				return a;
			}
		}
		return null;
	}

	/**
	 * 保留某一类的activity，其它的都关闭并移出栈
	 * 
	 * @Description:
	 * @param cls
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static void retain(Class<? extends Activity> cls) {
		Stack<Activity> newStack = new Stack<Activity>();
		for (Activity a : stack) {
			if (a.getClass().equals(cls)) {
				newStack.push(a);
			} else {
				if (!a.isFinishing()) {
					a.finish();
				}
			}
		}
		stack = newStack;
	}
	
	/**
	 *  删除某一元素之上的所有元素
	 * @Description:
	 * @param cls
	 * @author:www.wenchuang.com
	 * @date:2016-8-17
	 */
	public static void popTopOthers(Class<? extends Activity> cls) {
		int index  = stack.size();
		for (int i = 0; i < stack.size(); i++) {
			Activity a = stack.get(i);
			if (a.getClass().equals(cls)) {
				index = i;
			}
		}
		
		for (int i = 0; i < stack.size(); i++) {
			Activity a = stack.get(i);
			if (i > index) {
				if (!a.isFinishing()) {
					a.finish();
				}
			}
		}
	}
}