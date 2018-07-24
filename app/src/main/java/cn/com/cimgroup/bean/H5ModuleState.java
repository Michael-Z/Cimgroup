package cn.com.cimgroup.bean;

/**
 * 网络请求获取，与本地缓存数据状态进行对比
 * @Description:
 * @author:zhangjf 
 * @see:   
 * @since:      
 * @copyright www.wenchuang.com
 * @Date:2014-12-10
 */
public class H5ModuleState {

	private String version;//缓存版本号
	private String fileName;//文件名称
	private String downUrl;//下载地址
	
	public String getVersion() {
		return version;
	}
	
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getDownUrl() {
		return downUrl;
	}
	
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	

}
