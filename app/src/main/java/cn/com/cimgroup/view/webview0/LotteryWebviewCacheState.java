package cn.com.cimgroup.view.webview0;

import cn.com.cimgroup.bean.H5ModuleState;


/**
 * webview 文件缓存操作类实体类
 * @Description:
 * @author:zhangjf 
 * @see:   
 * @since:      
 * @copyright www.wenchuang.com
 * @Date:2014-12-8
 */
public class LotteryWebviewCacheState extends H5ModuleState{

	
	private String datatime;//更新时间
	private String state;//0 已下载未解压  1已经解压 2文件以覆盖 3缓存已经删除
	
	public String getDatatime() {
		return datatime;
	}
	
	public void setDatatime(String datatime) {
		this.datatime = datatime;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}
	
	
}
