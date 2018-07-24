package cn.com.cimgroup.xutils;

import com.google.gson.Gson;

/**
 * 操作JSON数据的工具类
 */
public class JsonHelper {

	private JsonHelper() {
	}

	public static Gson getInstence() {
		return SingleGson.gson;
	}

	private static class SingleGson {
		private static final Gson gson = new Gson();
	}

	/**
	 * 把对象转成Json
	 */
	public static String objectToJson(Object object) {
		try {
			return getInstence().toJson(object);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把Json转成对象
	 */
	public static <T> T jsonToObject(String json, Class<T> clazz) {
		try {
			return getInstence().fromJson(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}