package cn.com.cimgroup.util.encryption;

import javax.crypto.Cipher;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * 3DES加密
 * @Description:
 * @author:zhangjf 
 * @see:   
 * @since:      
 * @copyright www.wenchuang.com
 * @Date:2014-12-5
 */
public class Des3 {

	private static final String Algorithm = "DESede";
//	private static String keybyteStr = "14CBDB8148D23F24A6B1248B";
	private static String keybyteStr = "C43A0E7AE4E975CAC2AA9A6E";
	private static final byte[] keybyte = keybyteStr.getBytes();

	/**
	 * 加密
	 * @Description:
	 * @param src
	 * @return
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2014-12-5
	 */
	public static byte[] encryptMode(byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.ENCRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
		return null;
	}

	/**
	 * 解密
	 * @Description:
	 * @param src
	 * @return
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2014-12-5
	 */
	public static byte[] decryptMode(byte[] src) {
		try {
			SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);

			Cipher c1 = Cipher.getInstance(Algorithm);
			c1.init(Cipher.DECRYPT_MODE, deskey);
			return c1.doFinal(src);
		} catch (java.security.NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (javax.crypto.NoSuchPaddingException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}

		return null;
	}
}
