package cn.com.cimgroup.view.webview0;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.xutils.FileUtil;
import cn.com.cimgroup.xutils.Tick;
import cn.com.cimgroup.xutils.XLog;
import cn.com.cimgroup.App;
import cn.com.cimgroup.GlobalConstants;

/**
 * 与webview相关的文件操作类
 *  包括zip解压、相关文件的缓存处理等
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2014-12-8
 */
public class LotteryWebpageFileUtil {

	private static LotteryWebpageFileUtil mWebpageFileUtil;

	public static LotteryWebpageFileUtil getInstance() {
		if (mWebpageFileUtil == null) {
			mWebpageFileUtil = new LotteryWebpageFileUtil();
		}
		return mWebpageFileUtil;
	}

	public void init(final int maxExtractCount) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				extractAssetsZip(maxExtractCount);
			}
		}).start();
	}

	/**
	 * assets的h5页面解压到data/data/package/cache中， 正常情况下只解压一次。
	 * 除非data/data/package/cache中文件被人为删除或者没有解压完全h5文件夹，则从新解压
	 * 
	 * //TODO 可能会有部分文件因为不可抗拒因素导致文件损坏，或者丢失，这里没有做处理，可能后期需要考虑
	 * 
	 * @Description:
	 * @param maxExtractCount
	 *            防止单次解压失败
	 * @see:
	 * @since:
	 * @author:zhangjf
	 * @date:2014-12-11
	 */
	private void extractAssetsZip(int maxExtractCount) {
		Tick tick = new Tick();
		tick.begin();
		// 如果目录中不存在解压后的h5目录，或者数据库没有提示解压完成，那么需要从新解压
		if (!FileUtil.isExistDir(GlobalConstants.H5_FIRST_LEVE_NAME_PATH) || !LotteryWebViewDatabase.getInstance(App.getInstance()).isExistData()) {
			try {
				copyFile2Extract(GlobalConstants.H5_FIRST_LEVE_NAME, App.getInstance().getAssets().open(GlobalConstants.H5_ASSETS_NAME), GlobalConstants.H5_PATH);
				// 如果解压没有任何问题那么更新数据库
				LotteryWebViewDatabase.getInstance(App.getInstance()).addInsertCacheState(FileUtil.foldersName(new File(GlobalConstants.H5_FIRST_LEVE_NAME_PATH)));
				tick.end("loc assets extract date --");
				// 验证是否有新版本
//				Controller.getInstance().checkH52update();
			} catch (Exception e) {
				e.printStackTrace();
				// 因为程序第一次运行时assets中的文件非常重要，如果发生异常，我们要进行多次解压
				if (maxExtractCount > 0) {
					maxExtractCount--;
					XLog.d("loc assets extract count --", maxExtractCount + "-" + e.toString());
					init(maxExtractCount);
				}
			}
		} else {
			// 验证是否有新版本
//			Controller.getInstance().checkH52update();
		}
	}

	/**
	 * 将zip从from目录拷贝到指定文件夹进行解压
	 * 
	 * @Description:
	 * @param file
	 * @param from
	 * @param to
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @throws IOException
	 * @date:2014-12-8
	 */
	public void copyFile2Extract(String fileName, InputStream from, String to) throws IOException {
		// 如果本地存在，那么干掉
		File file = new File(GlobalConstants.H5_FIRST_LEVE_NAME_PATH);
		if (file != null && file.exists()) {
			deleteDirectory(file.getPath());
		}
		zipExtract2File(from, fileName, to);
	}

	/**
	 * 解压zip文件
	 * 
	 * @param context上下文对象
	 * @param assetName压缩包首级目录文件名
	 * @param outputDirectory输出目录
	 * @throws IOException
	 */
	public static void zipExtract2File(InputStream from, String assetName, String outputDirectory) throws IOException {
		// 创建解压目标目录
		File file = new File(outputDirectory);
		// 如果目标目录不存在，则创建
		if (!file.exists()) {
			file.mkdirs();
		}
		// 打开压缩文件
		ZipInputStream zipInputStream = new ZipInputStream(from);
		// 读取一个进入点
		ZipEntry zipEntry = zipInputStream.getNextEntry();
		// 使用1Mbuffer
		byte[] buffer = new byte[1024 * 1024];
		// 解压时字节计数
		int count = 0;
		Boolean isFirstDirectory = true;
		String fileName = null;
		while (zipEntry != null) {
			// 如果是一个目录
			if (zipEntry.isDirectory()) {
				if (isFirstDirectory) {
					isFirstDirectory = false;
					if (assetName == null || assetName.equals("")) {
						fileName = zipEntry.getName();
					} else {
						fileName = assetName;
					}
				} else {
					if (assetName != null && !assetName.equals("")) {
						fileName = assetName + zipEntry.getName().substring(zipEntry.getName().indexOf("/"));
					} else {
						fileName = zipEntry.getName();
					}
				}
				file = new File(outputDirectory + File.separator + fileName);
				file.mkdir();
			} else {
				if (assetName != null && !assetName.equals("")) {
					fileName = assetName + zipEntry.getName().substring(zipEntry.getName().indexOf("/"));
				} else {
					fileName = zipEntry.getName();
				}
				// 如果是文件
				file = new File(outputDirectory + File.separator + fileName);
				// 创建该文件
				file.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(file);
				while ((count = zipInputStream.read(buffer)) > 0) {
					fileOutputStream.write(buffer, 0, count);
				}
				fileOutputStream.close();
			}
			// 定位到下一个文件入口
			zipEntry = zipInputStream.getNextEntry();
		}
		zipInputStream.close();
	}

	/**
	 * 删除单个文件
	 * 
	 * @param filePath
	 *            被删除文件的文件名
	 * @return 文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String filePath) {
		File file = new File(filePath);
		if (file.isFile() && file.exists()) {
			return file.delete();
		}
		return false;
	}

	/**
	 * 删除文件夹以及目录下的文件
	 * 
	 * @param filePath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String filePath) {
		boolean flag = false;
		// 如果filePath不以文件分隔符结尾，自动添加文件分隔符
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		File dirFile = new File(filePath);
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		flag = true;
		File[] files = dirFile.listFiles();
		// 遍历删除文件夹下的所有文件(包括子目录)
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				// 删除子文件
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} else {
				// 删除子目录
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前空目录
		return dirFile.delete();
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param filePath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public boolean deleteFolder(String filePath) {
		File file = new File(filePath);
		if (!file.exists()) {
			return false;
		} else {
			if (file.isFile()) {
				// 为文件时调用删除文件方法
				return deleteFile(filePath);
			} else {
				// 为目录时调用删除目录方法
				return deleteDirectory(filePath);
			}
		}
	}
}
