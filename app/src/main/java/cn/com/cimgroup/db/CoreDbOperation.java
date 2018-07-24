package cn.com.cimgroup.db;
import java.util.HashMap;
import java.util.Map;

/**
 * 核心数据库操作，使用{@link #getInstance(String)}获取实例
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2013-8-2
 */
public class CoreDbOperation extends CoreDbHelper {

	private String uuid = null;

	public CoreDbOperation(String uuid) {
		super(uuid);
		this.uuid = uuid;
	}

	// 单列集合
	private static Map<String, CoreDbOperation> instances = new HashMap<String, CoreDbOperation>();

	
	public static void delete(String uuid) {
		if (instances.containsKey(uuid)) {
			instances.remove(uuid);
		}
	}

}
