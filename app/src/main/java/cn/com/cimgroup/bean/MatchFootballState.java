package cn.com.cimgroup.bean;

import java.util.Map;
import java.util.TreeMap;

/**
 * 比赛结果状态
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-1-13
 */
public class MatchFootballState {

	private Map<Integer, String> mNumType = new TreeMap<Integer, String>();
	private Map<String, Integer> mNumName = new TreeMap<String, Integer>();

	public MatchFootballState(Integer[] numtype, String[] numname) {
		if (numtype != null && numname != null && numtype.length == numname.length) {
			for (int i = 0; i < numname.length; i++) {
				mNumType.put(numtype[i], numname[i]);
				mNumName.put(numname[i], numtype[i]);
			}
		}
	}

	/**
	 * 获取汉字 numName
	 * 
	 * @Description:
	 * @param numType
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-13
	 */
	public String getNumName(Integer numType) {
		return mNumType.get(numType);
	}

	/**
	 * 获取数字 numType
	 * 
	 * @Description:
	 * @param numName
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2015-1-13
	 */
	public int getNameType(String numName) {
		return mNumName.get(numName);
	}

	/**
	 * 返回所有结果
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2015-1-20
	 */
	public Map<Integer, String> getData(){
		return mNumType;
	}

}