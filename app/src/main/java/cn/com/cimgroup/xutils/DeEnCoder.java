package cn.com.cimgroup.xutils;

import android.annotation.SuppressLint;
import java.nio.charset.Charset;

/**
 * Java简单的加密解密算法，使用异或运算
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年11月26日
 */
@SuppressLint("NewApi")
public class DeEnCoder {

	 private static final String key0 = "0123456789";
//	private static final String key0 = "ABCDEFJHIJKLMNO0123456789";
	private static final Charset charset = Charset.forName("UTF-8");
	private static byte[] keyBytes = key0.getBytes(charset);

	/**
	 * 加密
	 * @Description:
	 * @param enc
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月24日
	 */
	public static String encode(String enc) {
		byte[] b = enc.getBytes(charset);
		for (int i = 0, size = b.length; i < size; i++) {
			for (byte keyBytes0 : keyBytes) {
				b[i] = (byte) (b[i] ^ keyBytes0);
			}
		}
		return new String(b);
	}

	/**
	 * 解密
	 * @Description:
	 * @param dec
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月24日
	 */
	public static String decode(String dec) {
		byte[] e = dec.getBytes(charset);
		byte[] dee = e;
		for (int i = 0, size = e.length; i < size; i++) {
			for (byte keyBytes0 : keyBytes) {
				e[i] = (byte) (dee[i] ^ keyBytes0);
			}
		}
		return new String(e);
	}

	public static void main(String[] args) {
		String s = "67013411236";
		String enc = encode(s);
		String dec = decode(enc);
		System.out.println(enc);
		System.out.println(dec);
	}
}
