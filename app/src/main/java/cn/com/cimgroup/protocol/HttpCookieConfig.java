package cn.com.cimgroup.protocol;

import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import cn.com.cimgroup.xutils.PreferenceUtil;
import cn.com.cimgroup.xutils.XLog;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;

/**
 * 操作HTTP请求过来的COOKIE TODO 暂时没有对cookie过期进行处理
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-2-4
 */
public class HttpCookieConfig extends PreferenceUtil {

	// 文件名
	private static final String PREFERENCE_NAME = "httpcookie_config";

	public HttpCookieConfig(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	private static HttpCookieConfig instance;

	public static HttpCookieConfig getInstance() {
		if (instance == null) {
			instance = new HttpCookieConfig(PREFERENCE_NAME);
		}
		return instance;
	}

	/**
	 * 获取HttpURLConnection传递的cookie
	 * 
	 * @Description:
	 * @param connection
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-23
	 */
	public void getCookie(HttpURLConnection connection) {
		if (connection != null) {
			Map<String, List<String>> heads = connection.getHeaderFields();
			if (heads != null) {
				List<String> cookie = heads.get("Set-Cookie");
				XLog.d("getCookie cookie count-->"+(cookie==null?0:cookie.size()));
				if (cookie != null)
					for (int i = 0; i < cookie.size(); i++) {
						String cookieStr = cookie.get(i).indexOf(";") > 0 ? cookie.get(i).substring(0, cookie.get(i).indexOf(";")) : cookie.get(i);
						String cookieKey = cookieStr.substring(0, cookieStr.lastIndexOf("="));
						String cookieValue = cookieStr.substring(cookieStr.lastIndexOf("=") + 1);
						putString(cookieKey, cookieValue);
						XLog.d(cookieStr);
					}
			}
		}
	}

	/**
	 * set cookie to httprulconnection
	 * 
	 * @Description:
	 * @param mCookieMap
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-23
	 */
	@SuppressWarnings("unchecked")
	public void setCookie(HttpURLConnection connection) {

		Map<String, ?> allData = getAll();
		if (connection != null && allData != null) {
			StringBuilder builder = new StringBuilder();
			if (allData != null) {
				Iterator<?> iterator = allData.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<String, ?> entry = (Entry<String, ?>) iterator.next();
					builder.append(entry.getKey()).append("=").append(entry.getValue()).append(";");
					XLog.d("key="+entry.getKey()+" value="+entry.getValue());
				}
			}
			if (builder.length() > 0) {
				builder.delete(builder.length() - 1, builder.length());
			}
			if (builder.length() > 0) {
				GlobalConstants.JSESSIONID = builder.toString();
				connection.setRequestProperty("Cookie", builder.toString());
			}
		}
	}
	/**
	 * 清空cookie
	 * @Description:
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2015-2-6
	 */
	public void clearCookie(){
		clear();
	}

	@Override
	protected Context getContext() {
		// TODO Auto-generated method stub
		return App.getInstance();
	}

}
