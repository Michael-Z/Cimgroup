package cn.com.cimgroup.protocol;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.activity.LoginActivity;
import cn.com.cimgroup.activity.MainActivity;
import cn.com.cimgroup.activity.LoginActivity.CallThePage;
import cn.com.cimgroup.bean.UserInfo;
import cn.com.cimgroup.frament.LoboHallFrament;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.ActivityManager;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;
import cn.com.cimgroup.xutils.XLog;

/**
 * 网络请求服务器响应
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class Response {

	public static final int ERROR = 10001;
	public static final int SUCCESS = 10002;
	private static int code;
	private InputStream inputStream;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public JSONArray toJsonArray() throws CException {
		ByteArrayOutputStream outStream = null;
		JSONArray json = null;
		try {
			if (code == 200) {
				if (inputStream != null) {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[512];
					int length = -1;
					while ((length = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, length);
					}
					outStream.flush();
					String jsonString = new String(outStream.toByteArray());
					XLog.d("Response", jsonString);
					if (jsonString != null && !"".equals(jsonString)) {
						json = new JSONArray(jsonString);
					}
					code = CException.JSONOBTAINSUCCESS;
				} else {
					XLog.e("Response", "toJsonArray inputstream is null !");
					code = CException.IOERROR;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			code = CException.JSONFORWARTERROR;
		} catch (Exception e) {
			e.printStackTrace();
			code = CException.IOERROR;
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				mop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	/**
	 * 将响应结果解析成JSON
	 * @Description:
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public JSONObject toJson() throws CException {
		ByteArrayOutputStream outStream = null;
		JSONObject json = null;
		try {
			if (code == 200) {
				if (inputStream != null) {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[512];
					int length = -1;
					while ((length = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, length);
					}
					outStream.flush();
					String jsonString = new String(outStream.toByteArray());
					System.out.println("-----------响应json是--------------"+jsonString);
					XLog.d("Response", jsonString);
					if (jsonString != null && !"".equals(jsonString)) {
						json = new JSONObject(jsonString);
						
						if (!json.isNull("error")) {
							code = CException.JSONISNULLERROR;
						} else {
							code = CException.JSONOBTAINSUCCESS;
							JSONObject body = (JSONObject) json.get("body");
							JSONObject header = (JSONObject) json.get("header");
							
							//超时处理------此处还需测试
//							if (body.optInt("resCode") == 3002) {
//								UserLogic.getInstance().clearUserInfo();
								//一期bug314（登陆页面弹出问题）修改
//								UserInfo userInfo = UserLogic.getInstance().getDefaultUserInfo();
//								if (userInfo != null) {
//									Controller.getInstance().login(GlobalConstants.NUM_LOGIN, userInfo.getUserName(), userInfo.getPassword(), new CallBack() {
//									});
//								} else {
//									App.userInfo.setResCode("3002");
//									UserLogic.getInstance().saveUserInfo(App.userInfo);
//								}
//									MainActivity mainActivity = (MainActivity) ActivityManager.isExistsActivity(MainActivity.class);
//									if (mainActivity != null) {
//										// 如果存在MainActivity实例，那么展示LoboHallFrament页面
//										Intent intent = new Intent(mainActivity, LoginActivity.class);
//										intent.putExtra("callthepage", CallThePage.LOBOHALL);
//										mainActivity.startActivity(intent);
//									} else {
//										LoginActivity.forwartLoginActivity(App.getInstance(), CallThePage.LOBOHALL);
//									}
//								}
//							}
							
							if (StringUtil.isEmpty(json.get("body").toString())) {
								json = new JSONObject();
							} else {
								json = (JSONObject) json.get("body");
							}
//							json.put("resCode", header.optString("resCode"));
//							json.put("resMsg", header.optString("resMsg"));
//							
//							if (!header.optString("resCode").equals("0")) {
//								ToastUtil.shortToast(App.getInstance(), header.optString("resMsg"));
//							}
							
						}
					}
				} else {
					XLog.e("Response", "toJson inputstream is null !");
					code = CException.IOERROR;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			code = CException.JSONFORWARTERROR;
		} catch (Exception e) {
			e.printStackTrace();
			code = CException.IOERROR;
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				mop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	/**
	 * 将响应结果解析成String
	 * @Description:
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public JSONObject toStr2JsonObj() throws CException {
		ByteArrayOutputStream outStream = null;
		JSONObject json = null;
		try {
			if (code == 200) {
				if (inputStream != null) {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[512];
					int length = -1;
					while ((length = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, length);
					}
					outStream.flush();
					String jsonString = new String(outStream.toByteArray());
					XLog.d("Response", jsonString);
					String jsonString2 = jsonString.substring(jsonString.indexOf("(") + 1, jsonString.lastIndexOf("") - 1);
					if (jsonString2 != null && !"".equals(jsonString2)) {
						json = new JSONObject(jsonString2);
						if (json.isNull("error")) {
							code = CException.JSONISNULLERROR;
						} else {
							code = CException.JSONOBTAINSUCCESS;
						}
					}
				} else {
					XLog.e("Response", "toJson inputstream is null !");
					code = CException.IOERROR;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			code = CException.JSONFORWARTERROR;
		} catch (Exception e) {
			e.printStackTrace();
			code = CException.IOERROR;
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				mop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	/**
	 * 将对应的string经过简单处理转换为jsonArray
	 * @Description:
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public JSONArray toStr2JsonArray() throws CException {
		ByteArrayOutputStream outStream = null;
		JSONArray json = null;
		try {
			if (code == 200) {
				if (inputStream != null) {
					outStream = new ByteArrayOutputStream();
					byte[] buffer = new byte[512];
					int length = -1;
					while ((length = inputStream.read(buffer)) != -1) {
						outStream.write(buffer, 0, length);
					}
					outStream.flush();
					String jsonString = new String(outStream.toByteArray());
					XLog.d("Response", jsonString);
					String jsonString2 = jsonString.substring(jsonString.indexOf("[") , jsonString.lastIndexOf(""));
					if (jsonString2 != null && !"".equals(jsonString2)) {
						json = new JSONArray(jsonString2);
					}
					code = CException.JSONOBTAINSUCCESS;
				} else {
					XLog.e("Response", "toJsonArray inputstream is null !");
					code = CException.IOERROR;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			code = CException.JSONFORWARTERROR;
		} catch (Exception e) {
			e.printStackTrace();
			code = CException.IOERROR;
		} finally {
			try {
				if (outStream != null) {
					outStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				mop();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	/**
	 * 将响应结果写入指定路径的文件
	 * @Description:
	 * @param path
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public void toFile(String path) throws CException {
		FileOutputStream outputStream = null;
		try {
			if (code == 200) {
				if (inputStream != null) {
					File file = new File(path);
					outputStream = new FileOutputStream(file);
					IOUtils.copy(inputStream, outputStream);
					code = SUCCESS;
				} else {
					XLog.e("Response", "toFile inputstream is null !");
					code = CException.IOERROR;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			code = CException.IOERROR;
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (outputStream != null) {
					outputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
	public String inputStream2String () {
		if (inputStream != null) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			try {
				while ((line = bufferedReader.readLine()) != null) {
					buffer.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return buffer.toString();
		}
		return "";
	}
	
	/**
	 * 是否执行成功
	 * @Description:
	 * @return
	 * @throws CException
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public boolean isExecuteSuccess() throws CException {
		toJson();
		return isRequest();
	}

	/**
	 * 网络请求是否成功
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月6日
	 */
	public Boolean isRequest() {
		return code == SUCCESS;
	}

	public static Boolean isSuccess() {
		return code == 200;
	}

	private boolean mop() throws CException {
		if (code == CException.JSONOBTAINSUCCESS) {
			return true;
		} else {
			XLog.e("Response", " ERROR CODE: " + code);
			throw new CException(code);
		}
	}
}
