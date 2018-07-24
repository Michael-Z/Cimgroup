package cn.com.cimgroup.xutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	private static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public static String bytes2HexString(byte[] data) {
		StringBuffer sb = new StringBuffer(32);
		for (byte b : data) {
			char low = DIGITS[b & 0x0F];
			char high = DIGITS[(b & 0xF0) >>> 4];
			sb.append(high);
			sb.append(low);
		}
		return sb.toString();
	}
	
	public static String join(Object[] array, String separator) {
		if (array == null) {
			return null;
		}
		int arraySize = array.length;
		int bufSize = (arraySize == 0 ? 0 : ((array[0] == null ? 16 : array[0].toString().length()) + 1) * arraySize);
		StringBuffer buf = new StringBuffer(bufSize);

		for (int i = 0; i < arraySize; i++) {
			if (i > 0) {
				buf.append(separator);
			}
			if (array[i] != null) {
				buf.append(array[i]);
			}
		}
		return buf.toString();
	}

	/**
	 * 字符串equal比较
	 * 
	 * @Description:
	 * @param s1
	 * @param s2
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static boolean equal(String s1, String s2) {
		if (s1 == s2) {
			return true;
		}
		if (s1 == null) {
			return false;
		}
		return s1.equals(s2);
	}

	/**
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-7-24
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0 ||str.equals("null");
	}

	/**
	 * val 属于区间[from, to]
	 * 
	 * @Description:
	 * @param val
	 * @param from
	 * @param to
	 * @return
	 * @see:
	 * @since:
	 * @author: yangentao
	 * @date:2012-7-18
	 */

	public static boolean inRange11(int val, int from, int to) {
		return val >= from && val <= to;
	}

	/**
	 * val 属于区间[from, to)
	 * 
	 * @Description:
	 * @param val
	 * @param from
	 * @param to
	 * @return
	 * @see:
	 * @since:
	 * @author: yangentao
	 * @date:2012-7-18
	 */

	public static boolean inRange10(int val, int from, int to) {
		return val >= from && val < to;
	}

	/**
	 * 将a-z的小写字符转换成A-Z的大写字符, 如果给定的字符不在[a,z]区间内, 则返回原字符
	 * 
	 * @Description:
	 * @param ch
	 * @return
	 * @see:
	 * @since:
	 * @author: yangentao
	 * @date:2012-7-18
	 */

	public static char toUpper(char ch) {
		if (inRange11(ch, 'a', 'z')) {
			return (char) (ch - 'a' + 'A');
		}
		return ch;
	}

	/**
	 * 去掉字符串中的换行；用于邮件列表中预览的显示
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: xuqq
	 * @date:2013-8-2
	 */
	public static String deleteEmptyLine(String str) {
		String dest = str;
		if (str != null) {
			Pattern p = Pattern.compile("\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * tab 换为一个空格，多个空格合并；用于邮件列表中预览显示
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: xuqq
	 * @date:2013-8-2
	 */
	public static String combineBlank(String str) {
		String dest = str;
		if (str != null) {
			Pattern p = Pattern.compile("\t");
			Matcher m = p.matcher(str);
			dest = (m.replaceAll(" ")).trim().replaceAll(" +", " ");
		} else {
			dest = "";
		}
		return dest;
	}

	/**
	 * 将以“,”分隔的字符串转换成String[]
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-8-7
	 */
	public static String[] str2Array(String str) {
		if (isEmpty(str)) {
			return null;
		}
		return str.split(",");
	}

	/**
	 * 将String[]转换成以“,”分隔的字符串
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-8-7
	 */
	public static String array2String(String[] arr) {
		if (arr == null || arr.length == 0) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (String str : arr) {
			if (i > 0) {
				buffer.append(",");
			}
			buffer.append(str);
			i++;
		}
		return buffer.toString();
	}

	/**
	 * 将String集合转换成以“,”分隔的字符串
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-8-7
	 */
	public static String list2String(Collection<String> c) {
		if (c == null || c.isEmpty()) {
			return null;
		}
		StringBuffer buffer = new StringBuffer();
		int i = 0;
		for (String str : c) {
			if (i > 0) {
				buffer.append(",");
			}
			buffer.append(str);
			i++;
		}
		return buffer.toString();
	}

	/**
	 * 将以","分隔的string转成list
	 * 
	 * @Description:
	 * @param str
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-8-12
	 */
	public static List<String> str2List(String str) {
		return str2List(str, ",");
	}

	/**
	 * string转成list
	 * 
	 * @Description:
	 * @param str
	 * @param split
	 *            分隔符
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-8-12
	 */
	public static List<String> str2List(String str, String split) {
		if (isEmpty(str)) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		if (split == null) {
			list.add(str);
		} else {
			list.addAll(Arrays.asList(str.split(split)));
		}
		return list;
	}

	/**
	 * 提供UUID
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-8-12
	 */
	public static String getUUid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 将一组数据（字符串）按照给定的分割符组合成一个字符串<br>
	 * 分隔符只出现在数据中间
	 * 
	 * @Description:
	 * @param parts
	 * @param separator
	 *            分隔符
	 * @return
	 * @see:
	 * @since:
	 * @author: xulei
	 * @date:Aug 22, 2013
	 */
	public static String combine(Object[] parts, char separator) {
		if (parts == null) {
			return null;
		} else if (parts.length == 0) {
			return "";
		} else if (parts.length == 1) {
			return parts[0].toString();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(parts[0]);
		for (int i = 1; i < parts.length; ++i) {
			sb.append(separator);
			sb.append(parts[i]);
		}
		return sb.toString();
	}

	/**
	 * 获取UUID
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author: zhangjf
	 * @date:2013-10-8
	 */
	public static String buildUUID() {
		String uid = UUID.randomUUID().toString();
		return uid.replaceAll("-", "");
	}

	public static String replace(String text, String name) {
		return text.replace(name, "");
	}
}
