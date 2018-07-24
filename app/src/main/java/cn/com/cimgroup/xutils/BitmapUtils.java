package cn.com.cimgroup.xutils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.util.Log;
/**
 * Bitmap帮助类
 * @author 秋风
 *
 */
public class BitmapUtils {
	/**
	 * 把图片压缩到任意kb以下
	 * @param bitmap_压缩的文件
	 * @param outPath_输出文件名
	 * @param fileSize_压缩大小
	 */
	public static void compressImage(Bitmap bitmap, String outPath,int fileSize) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 95;
			while (baos.toByteArray().length / 1024 > fileSize) {
				baos.reset();
				options -= 5;
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}
			File file = new File(outPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, new FileOutputStream(file));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
