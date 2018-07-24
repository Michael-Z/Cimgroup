package cn.com.cimgroup.xutils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import cn.com.cimgroup.util.encryption.Base64Utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

/**
 * sharedPreference util
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2013-7-24
 */
public abstract class PreferenceUtil {

	private SharedPreferences sp;

	protected abstract Context getContext();
	

	public PreferenceUtil(String name) {
		sp = getContext().getSharedPreferences(name, 0);
	}

	public Editor edit() {
		return sp.edit();
	}
	

	/**
	 * put boolean value
	 * 
	 * @Description:
	 * @param key
	 * @param value
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected void putBool(String key, boolean value) {
		Editor ed = sp.edit();
		ed.putBoolean(key, value);
		ed.commit();
	}

	/**
	 * get boolean value
	 * 
	 * @Description:
	 * @param key
	 * @param defValue
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected boolean getBool(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}

	/**
	 * put int value
	 * 
	 * @Description:
	 * @param key
	 * @param value
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected void putInt(String key, int value) {
		Editor ed = sp.edit();
		ed.putInt(key, value);
		ed.commit();
	}

	/**
	 * get int value
	 * 
	 * @Description:
	 * @param key
	 * @param defValue
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	/**
	 * put long value
	 * 
	 * @Description:
	 * @param key
	 * @param value
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected void putLong(String key, long value) {
		Editor ed = sp.edit();
		ed.putLong(key, value);
		ed.commit();
	}

	/**
	 * 获取所有
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2015-2-4
	 */
	protected  Map<String, ?> getAll(){
		return sp.getAll();
	}

	
	/**
	 * get long value
	 * 
	 * @Description:
	 * @param key
	 * @param defValue
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected long getLong(String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	/**
	 * put string value
	 * 
	 * @Description:
	 * @param key
	 * @param value
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected void putString(String key, String value) {
		Editor ed = sp.edit();
		ed.putString(key, value);
		ed.commit();
	}

	/**
	 * get string value
	 * 
	 * @Description:
	 * @param key
	 * @param defValue
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected String getString(String key, String defValue) {
		return sp.getString(key, defValue);
	}

	/**
	 * put float value
	 * 
	 * @Description:
	 * @param key
	 * @param value
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected void putFloat(String key, float value) {
		Editor ed = sp.edit();
		ed.putFloat(key, value);
		ed.commit();
	}

	/**
	 * get float value
	 * 
	 * @Description:
	 * @param key
	 * @param defValue
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected float getFloat(String key, float defValue) {
		return sp.getFloat(key, defValue);
	}
	/**
	 * clear
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	protected void clear(){
		Editor ed = sp.edit();
		ed.clear();
		ed.commit();
	}
	
	/**
	 * 保存对象
	 * @Description:
	 * @param context
	 * @param key
	 * @param obj
	 * @author:www.wenchuang.com
	 * @date:2015-3-4
	 */
	public void saveObject(Context context, String key, Object obj) {
		try {
			// 保存对象
//			sp = context.getSharedPreferences("base64", context.MODE_PRIVATE);
			// 先将序列化结果写到byte缓存中，其实就分配一个内存空间
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(bos);
			// 将对象序列化写入byte缓存
			os.writeObject(obj);
			try {
				String oAuth_Base64 = new String(Base64Utils.encode(bos.toByteArray()));
				Editor editor = sp.edit();
				editor.putString(key, oAuth_Base64);
				editor.commit();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取对象
	 * @Description:
	 * @param context
	 * @param key
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015-3-4
	 */
	public Object readObject(Context context,String key){
		try {
//			sp = context.getSharedPreferences("base64", context.MODE_PRIVATE);
			if (sp.contains(key)) {
				String string = sp.getString(key, "");
				if (TextUtils.isEmpty(string)) {
					return null;
				} else {
					try {
						//读取字节  
						byte[] base64 = Base64Utils.decode(string);  
						//封装到字节流  
						ByteArrayInputStream bais = new ByteArrayInputStream(base64);
						//再次封装  
						ObjectInputStream bis = new ObjectInputStream(bais); 
						try {
							return bis.readObject();
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		}
		//所有异常返回null
		return null;
	}
}
