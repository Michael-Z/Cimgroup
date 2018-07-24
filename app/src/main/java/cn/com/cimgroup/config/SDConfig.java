package cn.com.cimgroup.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cn.com.cimgroup.xutils.XLog;
import android.os.Environment;

public class SDConfig {

	private String mFileName;

	private static SDConfig mConfiguration;

	private String path = "cimgroup/data";

	public SDConfig() {
	}

	/**
	 * 单例初始化配置文件
	 * 
	 * @Description:
	 * @param fileName
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2015年11月11日
	 */
	public static SDConfig getConfiguration() {

		if (mConfiguration == null) {

			mConfiguration = new SDConfig();
		}
		return mConfiguration;
	}

	public void setFileName(String fileName) {
		mFileName = fileName;
	}

	/**
	 * 判断SDCard是否存在 [当没有外挂SD卡时，内置ROM也被识别为存在sd卡]
	 * 
	 * @return
	 */
	public boolean isSdCardExist() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡根目录路径
	 * 
	 * @return
	 */
	public String getSdCardPath() {
		boolean exist = isSdCardExist();
		String sdpath = "";
		if (exist) {
			sdpath = Environment.getExternalStorageDirectory()
					.getAbsolutePath();
		} else {
			sdpath = "";
		}
		return sdpath;

	}

	/**
	 * 在SD卡上创建目录
	 */
	public File createDirOnSDCard(String dir) {
		File dirFile = new File(getSdCardPath() + File.separator + dir
				+ File.separator);
		XLog.i("createDirOnSDCard", getSdCardPath() + File.separator + dir
				+ File.separator);
		dirFile.mkdirs();
		return dirFile;
	}

	/**
	 * 在SD卡上创建文件
	 */
	public File createFileOnSDCard(String fileName, String dir)
			throws IOException {
		File file = new File(getSdCardPath() + File.separator + dir
				+ File.separator + fileName);
		XLog.i("createFileOnSDCard", getSdCardPath() + File.separator + dir
				+ File.separator + fileName);
		file.createNewFile();
		return file;
	}

	/**
	 * 判断SD卡上文件是否存在
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(getSdCardPath() + path + File.separator + fileName);
		return file.exists();
	}

	/**
	 * 将文本内容保存到sd卡的文件中
	 * 
	 * @Description:
	 * @param context
	 * @param fileName
	 * @param content
	 * @throws IOException
	 * @author:www.wenchuang.com
	 * @date:2015年11月11日
	 */
	public void saveToSDCard(Object content) {
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!isFileExist(mFileName)) {
			createDirOnSDCard(path);
		}
		File file;
		try {
			file = createFileOnSDCard(mFileName, path);
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(content);// 写入

			oos.close();
			fos.close();

		} catch (IOException e) {
			e.printStackTrace();
		} // 创建文件
	}

	/**
	 * 读取sd卡文件内容
	 * 
	 * @Description:
	 * @param fileName
	 * @return
	 * @throws IOException
	 * @author:www.wenchuang.com
	 * @date:2015年11月11日
	 */
	public Object readSDCard() {
		Object obj = null;
		if (!isFileExist(mFileName)) {
			createDirOnSDCard(path);
		}

		try {
			File file;
			file = createFileOnSDCard(mFileName, path);
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
			obj = ois.readObject();

			ois.close();
			fis.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
