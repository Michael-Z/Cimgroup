package cn.com.cimgroup.protocol;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.json.JSONObject;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.NetUtil;
import cn.com.cimgroup.xutils.XLog;

/**
 * 网络请求
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class Request {

	/** 超时时间 **/
	private static final int TIME_OUT = 20 * 1000;
	private static final String CHARSET = "UTF-8";
	private static final String BOUNDARY_MARK = "--";
	private static final String LINE_END = "\r\n";
	private static final String QUOTATION_MARK = "\"";
	private static final int BUFFER_SIZE = 1024;
	private static final String POST = "POST";
	private static final String GET = "GET";
	private static Map<String, String> head = new HashMap<String, String>();
	private static Map<String, JSONObject> allParam = new HashMap<String, JSONObject>();

	public static void setHead(String transactionType) {
		head.put("messageID", "1010101010");
		head.put("timeStamp", "20120406173212");
		head.put("transactionType", transactionType);
		head.put("sign", "0");
		head.put("terminal", "1");
		head.put("version", GlobalConstants.VERSION);
		head.put("channel", GlobalConstants.CHANNEL);
		TelephonyManager telephonyManager = (TelephonyManager) App
				.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telephonyManager.getDeviceId();
		head.put("imei", imei);
		head.put("ua", "android");
		head.put("betPlatform", "1");
	}

	public static Response get(String requestUrl, Map<String, String> params)
			throws CException {
		// 判断是否有网络
		if (!NetUtil.isConnected(App.getInstance())) {
			XLog.e("Request", "NO NET ERROR");
			throw new CException(CException.NET_ERROR);
		}
		Response response = new Response();
		try {
			String url = requestUrl;
			StringBuilder builder = new StringBuilder();
			if (params != null && params.size() > 0) {
				for (Entry<String, String> entry : params.entrySet()) {
					builder.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), CHARSET))
							.append("&");
				}
				builder.deleteCharAt(builder.length() - 1);
				url = url + "?" + builder.toString();
			}
			HttpURLConnection connection = prepareConnection(url, GET);// (HttpURLConnection)
			int code = connection.getResponseCode();
			XLog.e("Request", "url--> " + requestUrl + "?" + builder.toString()
					+ "  Code=" + code);
			response.setCode(code);
			if (code == 200) {
				response.setInputStream(connection.getInputStream());
			}
			// HttpCookieConfig.getInstance().getCookie(connection);
		} catch (IOException e) {
			// java.net.UnknownHostException
			e.printStackTrace();
			response.setCode(CException.IOERROR);
			throw new CException(CException.IOERROR);
		}
		return response;
	}

	/**
	 * 执行post请求
	 * 
	 * @Description:
	 * @param requestUrl
	 *            请求的地址
	 * @param params
	 *            请求参数
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public static Response post(String requestUrl, Map<String, Object> params)
			throws CException {

		Response response = new Response();
		// 判断是否有网络
		if (!NetUtil.isConnected(App.getInstance())) {
			XLog.e("Request", "NO NET ERROR");
			response.setCode(CException.NET_ERROR);
			throw new CException(CException.NET_ERROR);
		}
		try {
			setHead((String) params.get("transactionType"));
			allParam.put("header", new JSONObject(head));
			allParam.put("body", new JSONObject(params));
			// StringBuilder builder = new StringBuilder();
			// if (params != null) {
			// builder.append("{");
			// for (Entry<String, JSONObject> entry : allParam.entrySet()) {
			// //
			// builder.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(),
			// // CHARSET)).append("&");
			//
			// builder.append('"' + entry.getKey() +
			// '"').append(":").append(entry.getValue()).append(",");
			// }
			// builder.deleteCharAt(builder.length() - 1);
			// builder.append("}");
			//
			// }
			// XLog.e("Request", "url--> " + requestUrl + "?" +
			// builder.toString());
			// byte[] data = builder.toString().getBytes("utf-8");
			// HttpURLConnection connection = prepareConnection(requestUrl,
			// POST);
			// connection.setRequestProperty("Content-Type",
			// "application/json");
			XLog.e("Request", "url--> " + requestUrl + "?"
					+ new JSONObject(allParam).toString());
			StringBuilder builder = new StringBuilder(
					new JSONObject(allParam).toString());
			byte[] data = builder.toString().getBytes("utf-8");
			HttpURLConnection connection = prepareConnection(requestUrl, POST);
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Content-Length",
					String.valueOf(data.length));
			DataOutputStream outStream = new DataOutputStream(
					connection.getOutputStream());
			outStream.write(data);
			outStream.flush();
			outStream.close();
			int code = connection.getResponseCode();
			XLog.e("Request", "  Code=" + code);
			response.setCode(code);
			if (code == 200) {
//				String cookieval = connection.getHeaderField("set-cookie");
//				if (cookieval != null && App.userInfo != null) {
//					App.userInfo.setSessionId(cookieval.substring(cookieval
//							.lastIndexOf("=")));
//					UserLogic.getInstance().saveUserInfo(App.userInfo);
//				}
				response.setInputStream(connection.getInputStream());
			}
			HttpCookieConfig.getInstance().getCookie(connection);
		} catch (IOException e) {
			e.printStackTrace();
			response.setCode(CException.IOERROR);
			throw new CException(CException.IOERROR);
		}
		return response;
	}

	/**
	 * 执行post请求，并上传文件
	 * 
	 * @Description:
	 * @param requestUrl
	 * @param params
	 * @param file
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public static Response postWithFile(String requestUrl,
			Map<String, String> params, File file) throws CException {

		Response response = new Response();
		// 判断是否有网络
		if (!NetUtil.isConnected(App.getInstance())) {
			XLog.e("Request", "NO NET ERROR");
			throw new CException(CException.NET_ERROR);
		}
		try {
			HttpURLConnection connection = prepareConnection(requestUrl, POST);
			StringBuilder builder = new StringBuilder();
			String boundary = UUID.randomUUID().toString();
			// header
			connection.setRequestProperty("Content-Type", "multipart/form-data"
					+ ";boundary=" + boundary);
			// fields
			if (params != null) {
				for (Entry<String, String> entry : params.entrySet()) {
					// boundary
					builder.append(BOUNDARY_MARK).append(boundary)
							.append(LINE_END);
					// key
					builder.append("Content-Disposition: form-data; ");
					builder.append("name=").append(QUOTATION_MARK)
							.append(entry.getKey()).append(QUOTATION_MARK)
							.append(LINE_END);
					builder.append(LINE_END);
					// value
					builder.append(entry.getValue()).append(LINE_END);
				}
			}
			XLog.e("Request", "url--> " + requestUrl + "?" + builder.toString());

			DataOutputStream outStream = new DataOutputStream(
					connection.getOutputStream());
			outStream.write(builder.toString().getBytes());
			// file
			builder.setLength(0);
			// boundary
			builder.append(BOUNDARY_MARK).append(boundary).append(LINE_END);
			// file info
			builder.append("Content-Disposition: form-data; ");
			builder.append("name=").append(QUOTATION_MARK).append("file")
					.append(QUOTATION_MARK).append("; ");
			builder.append("filename=").append(QUOTATION_MARK)
					.append(file.getName()).append(QUOTATION_MARK);
			builder.append(LINE_END);
			// content type
			builder.append("Content-Type: ").append("application/octet-stream")
					.append(LINE_END);
			// header end
			builder.append(LINE_END);
			outStream.write(builder.toString().getBytes());
			// write file
			FileInputStream inputStream = new FileInputStream(file);
			int lenght = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((lenght = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, lenght);
			}
			outStream.write(LINE_END.getBytes());
			inputStream.close();
			// end
			builder.setLength(0);
			builder.append(BOUNDARY_MARK).append(boundary)
					.append(BOUNDARY_MARK).append(LINE_END);
			outStream.write(builder.toString().getBytes());
			outStream.flush();
			outStream.close();
			int code = connection.getResponseCode();
			XLog.e("Request", requestUrl + "  Code=" + code);
			if (code == 200) {
				response.setInputStream(connection.getInputStream());
			} else {
				response.setCode(connection.getResponseCode());
			}
			HttpCookieConfig.getInstance().getCookie(connection);
		} catch (IOException e) {
			e.printStackTrace();
			response.setCode(CException.IOERROR);
			throw new CException(CException.IOERROR);
		}
		return response;
	}

	/**
	 * 
	 * @Description:
	 * @param requestUrl
	 * @return
	 * @throws IOException
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:Jan 10, 2014
	 */
	private static HttpURLConnection prepareConnection(String requestUrl,
			String method) throws IOException {
		URL url = new URL(requestUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod(method);
		connection.setInstanceFollowRedirects(true);
		connection.setReadTimeout(TIME_OUT);
		connection.setConnectTimeout(TIME_OUT);
		connection.setRequestProperty("accept", "*/*");
		connection.setRequestProperty("Charset", CHARSET);
		connection.setRequestProperty("Connection", "Keep-Alive");
		HttpCookieConfig.getInstance().setCookie(connection);
		return connection;
	}

}
