package cn.com.cimgroup.protocol;

import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.App;
import cn.com.cimgroup.R;

/**
 * 异常捕获
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class CException extends Exception {

	/** 无网络 **/
	public static final int NET_ERROR = 10000;
	/** IO异常 **/
	public static final int IOERROR = 10001;
	/** JSON格式有误 **/
	public static final int JSONFORWARTERROR = 10002;
	/** JSON获取后为空 **/
	public static final int JSONISNULLERROR = 10003;
	/** JSON解析失败 **/
	public static final int JSONPARSEERROR = 10004;

	public static final int JSONOBTAINSUCCESS = 10013;// JSON获取成功

	private static final long serialVersionUID = 1L;

	public static final int UNSPECIFIED_EXCEPTION = 0;

	public static final int NETWORK_REQUEST_FAILURE = -1;
	public static final int NETWORK_UNAVAILABLE = -2;
	public static final int NETWORK_FAILURE = -3;
	public static final int UBKNOWN_HOST = 998;
	public static final int TIME_OUT = 999;
	public static final int TEMPORARILY_MOVED = 302;
	public static final int ACCESS_DENIED = 403;
	public static final int SERVER_ERROR = 500;
	public static int exceptionType;

	public CException(int type) {
		exceptionType = type;
		NoDataUtils.setmErrCode(exceptionType + "");
	}

	public CException(int type, String message) {
		super(message);
		exceptionType = type;
		NoDataUtils.setmErrCode(exceptionType + "");
	}

	public CException(String message) {
		super(message);
	}

	public int getCode() {
		return exceptionType;
	}

	public String getError() {
		return "";
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		String messageError = super.getMessage();
		if (StringUtil.isEmpty(messageError)) {
			switch (exceptionType) {
			// 无网络
			case NET_ERROR:
				messageError = App.getInstance().getString(R.string.nodata_no_net);
				break;
			// IO异常(包括超时等)
			case IOERROR:
				messageError = App.getInstance().getString(R.string.nodata_no_net);
				break;
			// JSON格式有误
			case JSONFORWARTERROR:
				// JSON为空
			case JSONISNULLERROR:
				// JSON解析失败
			case JSONPARSEERROR:
				messageError = App.getInstance().getString(R.string.exception_json_error);
				break;
			default:
				messageError = App.getInstance().getString(R.string.exception_json_error);
				break;
			}
		}
		return messageError;
	}

}
