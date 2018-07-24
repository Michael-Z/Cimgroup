package cn.com.cimgroup.xutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

/**
 * 
 * @ClassName: FileUtil
 * @Description: 文件处理类
 * @author:wangtf
 * @date 2011-9-23 上午11:09:24
 * 
 */
public class FileUtil {

	// 文件删除间隔时间
	public static int DELETE_EXPIRED_FILE_TIME = 1000 * 60 * 60 * 24 * 3;
	// 缓存文件大小90M
	public static double CACHE_SIZE = 50;
	// 允许缓存到sd卡时,sd卡的最下剩余空间
	public static double FREE_SD_SPACE_NEEDED_TO_CACHE = 30;

	public static boolean isDir(String path) {
		File file = new File(path);
		return file.isDirectory();
	}

	public static boolean isExistDir(String dir) {
		File file = new File(dir);
		return file.exists();
	}

	public static void deleteFile(String path) {
		File file = new File(path);
		deleteFile(file);
	}

	public static void deleteFile(File file) {

		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // delete()方法 你应该知道 是删除的意思;
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
			}
		}
	}
	
	/**
	 * 删除某文件夹和文件夹下的所有文件
	 */
	public static void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

	public static void createDir(String dir) {
		File file = new File(dir);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 
	 * @Title: updateFileTime
	 * @Description: 修改文件的最后修改时间
	 * @author wangtf
	 * @date 2011-10-18
	 * @return void
	 * @throws
	 * @parameters
	 */
	public static void updateFileTime(String dir, String fileName) {
		File file = new File(dir, fileName);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	/**
	 * 计算存储目录下的文件大小，当文件总大小大于规定的CACHE_SIZE或者sdcard剩余空间小于FREE_SD_SPACE_NEEDED_TO_CACHE的规定 那么删除40%最近没有被使用的文件 *
	 * 
	 * @param dirPath
	 *            * @param filename
	 */
	public void removeCache(String dirPath) {
		List<File> fileList = new ArrayList<File>();
		getFiles(dirPath, fileList);

		if (fileList.size() == 0) {
			return;
		}

		long dirSize = 0;
		dirSize = getFileDirSize(fileList, dirSize);

		if (dirSize > CACHE_SIZE * 1024 * 1024 || FREE_SD_SPACE_NEEDED_TO_CACHE > freeSpaceOnSd()) {
			int removeFactor = (int) ((0.4 * fileList.size()) + 1);
			FileLastModifSort fileLastModifSort = new FileLastModifSort();
			Collections.sort(fileList, fileLastModifSort);
			for (int i = 0; i < removeFactor; i++) {
				// if(files[i].getName().contains(WHOLESALE_CONV)) {
				fileList.get(i).delete();
				// }
			}
		}
	}

	
	/**
	 * 获取此file下的所以文件夹名称
	 * @Description:
	 * @param file
	 * @return
	 * @see: 
	 * @since: 
	 * @author:www.wenchuang.com
	 * @date:2014-12-9
	 */
	public static List<String> foldersName(File file){
		List<String> folders=new ArrayList<String>();
		if(file!=null){
			File [] files=file.listFiles();
			for(File f:files){
				if(f.isDirectory()){
					folders.add(f.getName());
				}
			}
		}
		return folders;
	}
	/**
	 * 
	 * @Description:计算文件大小
	 * @param fileList
	 * @param dirSize
	 * @return
	 * @see: 
	 * @since: 
	 * @author:chen zhong
	 * @date:2013-8-12
	 */
	public static long getFileDirSize(String dirPath) {
		long dirSize = 0;
		List<File> fileList = new ArrayList<File>();
		getFiles(dirPath, fileList);

		if (fileList.size() == 0) {
			return 0;
		}
		for (int i = 0; i < fileList.size(); i++) {
			dirSize += fileList.get(i).length();
		}
		return dirSize;
	}
	
	/**
	 * 
	 * @Title: findFiles
	 * @Description: 递归获取文件夹的大小
	 * @author wangtf
	 * @date 2011-10-19
	 * @return long
	 * @throws
	 * @parameters
	 */
	public static long getFileDirSize(List<File> fileList, long dirSize) {
		for (int i = 0; i < fileList.size(); i++) {
			dirSize += fileList.get(i).length();
		}
		return dirSize;
	}

	/**
	 * @Title: getFiles
	 * @Description: 获取指定目录下的文件
	 * @author wangtf
	 * @date 2011-10-19
	 * @return void
	 * @throws
	 * @parameters
	 */
	public static void getFiles(String filePath, List<File> fileList) {
		File file = new File(filePath);
		if (file.isFile()) {
			fileList.add(file);
			return;
		}
		// 否则是目录，继续遍历里面的文件
		File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				getFiles(files[i].getAbsolutePath(), fileList);
			}
		}
	}

	/**
	 * 
	 * @Title: removeExpiredCache
	 * @Description: 删除过期文件
	 * @author wangtf
	 * @date 2011-10-18
	 * @return void
	 * @throws
	 * @parameters
	 */
	public static void deleteExpiredFile(String dirPath) {
		List<File> fileList = new ArrayList<File>();
		getFiles(dirPath, fileList);
		for (int i = 0; i < fileList.size(); i++) {
			File file = fileList.get(i);
			if (System.currentTimeMillis() - file.lastModified() > DELETE_EXPIRED_FILE_TIME) {
				file.delete();
			}
		}
	}

	/**
	 * 
	 * @Title: freeSpaceOnSd
	 * @Description: 获取sd卡剩余空间
	 * @author wangtf
	 * @date 2011-10-18
	 * @return double MB
	 * @throws
	 * @parameters
	 */
	public static double freeSpaceOnSd() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File sdcardDir = Environment.getExternalStorageDirectory();
			StatFs sf = new StatFs(sdcardDir.getPath());
			long blockSize = sf.getBlockSize();
			long availCount = sf.getAvailableBlocks();

			return availCount * blockSize / (1024 * 1024);
		}
		return 0;
	}

	/**
	 * 
	 * @Title: freeSpaceOnSystem
	 * @Description: 获取系统剩余内存空间
	 * @author wangtf
	 * @date 2011-10-18
	 * @return double
	 * @throws
	 * @parameters
	 */
	public static double freeSpaceOnSystem() {
		File root = Environment.getRootDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSize();
		long availCount = sf.getAvailableBlocks();

		return availCount * blockSize / (1024 * 1024);
	}

	/**
	 * 
	 * @ClassName: FileLastModifSort
	 * @Description: 根据文件的最后修改时间进行排序
	 * @author:wangtf
	 * @date 2011-10-18 下午6:37:03
	 * 
	 */
	class FileLastModifSort implements Comparator<File> {

		public int compare(File arg0, File arg1) {
			if (arg0.lastModified() > arg1.lastModified()) {
				return 1;
			} else if (arg0.lastModified() == arg1.lastModified()) {
				return 0;
			} else {
				return -1;
			}
		}
	}

	/**
	 * 根据文件后缀名获得对应的MIME类型。
	 * 
	 * @param file
	 */
	private static String getMIMEType(String file_name) {
		String type = "*/*";
		String fName = file_name.toLowerCase();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end == "")
			return type;
		// 在MIME和文件类型的匹配表中找到对应的MIME类型。
		for (int i = 0; i < MIME_MapTable.length; i++) {
			if (end.equals(MIME_MapTable[i][0]))
				type = MIME_MapTable[i][1];
		}
		return type;
	}

	/**
	 * 打开文件
	 * 
	 * @param file
	 */
	public static Intent getOpenFileIntent(String file_name, Uri uri, Context context) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// 设置intent的Action属性
		intent.setAction(Intent.ACTION_VIEW);
		// 获取文件file的MIME类型
		String type = getMIMEType(file_name);
		intent.setDataAndType(uri, type);
		return intent;
	}

	// 建立一个MIME类型与文件后缀名的匹配表
	private final static String[][] MIME_MapTable = {
			// {后缀名， MIME类型}
	{ ".3gp", "video/3gpp" }, { ".apk", "application/vnd.android.package-archive" }, { ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" }, { ".bin", "application/octet-stream" }, { ".bmp", "image/bmp" }, { ".c", "text/plain" }, { ".class", "application/octet-stream" }, { ".conf", "text/plain" }, { ".cpp", "text/plain" }, { ".doc", "application/msword" }, { ".exe", "application/octet-stream" }, { ".gif", "image/gif" }, { ".gtar", "application/x-gtar" }, { ".gz", "application/x-gzip" }, { ".h", "text/plain" }, { ".htm", "text/html" }, { ".html", "text/html" }, { ".jar", "application/java-archive" }, { ".java", "text/plain" }, { ".jpeg", "image/jpeg" }, { ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" }, { ".log", "text/plain" }, { ".m3u", "audio/x-mpegurl" }, { ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" }, { ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" }, { ".m4v", "video/x-m4v" }, { ".mov", "video/quicktime" }, { ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" }, { ".mp4", "video/mp4" }, { ".mpc", "application/vnd.mpohun.certificate" }, { ".mpe", "video/mpeg" }, { ".mpeg", "video/mpeg" }, { ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" }, { ".mpga", "audio/mpeg" }, { ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" }, { ".pdf", "application/pdf" }, { ".png", "image/png" }, { ".pps", "application/vnd.ms-powerpoint" }, { ".ppt", "application/vnd.ms-powerpoint" }, { ".prop", "text/plain" }, { ".rar", "application/x-rar-compressed" }, { ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" }, { ".sh", "text/plain" }, { ".tar", "application/x-tar" }, { ".tgz", "application/x-compressed" }, { ".txt", "text/plain" }, { ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" }, { ".wmv", "audio/x-ms-wmv" }, { ".wps", "application/vnd.ms-works" },
			// {".xml", "text/xml"},
	{ ".xml", "text/plain" }, { ".z", "application/x-compress" }, { ".zip", "application/zip" },
			// word 2007
	{ "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" }, { "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" }, { "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" } };
	
	/**
	 * 判断是否挂起SdCard
	 * @return
	 */
	public static boolean isSdCard(){   
        String status=Environment.getExternalStorageState();
        if(status.equals(Environment.MEDIA_MOUNTED)){   
            return true;   
        } else {   
            return false;   
       }   
    }
	public static long forChannel(File f1,File f2) throws Exception{
        long time=new Date().getTime();
        int length=2097152;
        FileInputStream in=new FileInputStream(f1);
        FileOutputStream out=new FileOutputStream(f2);
        FileChannel inC=in.getChannel();
        FileChannel outC=out.getChannel();
        ByteBuffer b=null;
        while(true){
            if(inC.position()==inC.size()){
                inC.close();
                outC.close();
                return new Date().getTime()-time;
            }
            if((inC.size()-inC.position())<length){
                length=(int)(inC.size()-inC.position());
            }else
                length=2097152;
            b=ByteBuffer.allocateDirect(length);
            inC.read(b);
            b.flip();
            outC.write(b);
            outC.force(false);
        }
    }
}
