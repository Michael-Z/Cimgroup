package cn.com.cimgroup.config;

import android.content.Context;
import cn.com.cimgroup.xutils.PreferenceUtil;
import cn.com.cimgroup.App;

/**
 * 数字彩期号及结束时间
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2015-2-26
 */
public class LotteryIssueConfig extends PreferenceUtil {

	private static volatile LotteryIssueConfig instance = null;

	public LotteryIssueConfig(String name) {
		super(name);
	}

	@Override
	protected Context getContext() {
		return App.getInstance();
	}

	public static LotteryIssueConfig getInstance() {
		if (instance == null) {
			synchronized (LotteryIssueConfig.class) {
				if (instance == null) {
					instance = new LotteryIssueConfig("lottery_issue");
				}
			}
		}
		return instance;
	}
	
	/**--------传入lotteryName操作偏好设置------------**/
	
	public void saveIssue(String lotteryName, String issue) {
		putString("issue_"+ lotteryName, issue);
	}

	public String getIssue(String lotteryName) {
		return getString("issue_" + lotteryName, "");
	}
	
	public void saveEndTime(String lotteryName, String endTime) {
		putString("issue_"+ lotteryName +"_time", endTime);
	}

	public String getEndTime(String lotteryName) {
		return getString("issue_"+ lotteryName +"_time", "");
	}
	
	
	/** ----------大乐透--------- **/
	
	public void saveDltIssue(String issue) {
		putString("issue_dlt", issue);
	}

	public String getDltIssue() {
		return getString("issue_dlt", "");
	}

	public void saveDltEndTime(String endTime) {
		putString("issue_dlt_time", endTime);
	}

	public String getDltEndTime() {
		return getString("issue_dlt_time", "");
	}
	
	/** ----------排列3--------- **/
	
	public void savePl3Issue(String issue) {
		putString("issue_pl3", issue);
	}

	public String getPl3Issue() {
		return getString("issue_pl3", "");
	}

	public void savePl3EndTime(String endTime) {
		putString("issue_pl3_time", endTime);
	}

	public String getPl3EndTime() {
		return getString("issue_pl3_time", "");
	}

	/** ----------排列5--------- **/
	
	public void savePl5Issue(String issue) {
		putString("issue_pl5", issue);
	}

	public String getPl5Issue() {
		return getString("issue_pl5", "");
	}

	public void savePl5EndTime(String endTime) {
		putString("issue_pl5_time", endTime);
	}

	public String getPl5EndTime() {
		return getString("issue_pl5_time", "");
	}
	
	/** ----------七星彩--------- **/
	
	public void saveQxcIssue(String issue) {
		putString("issue_qxc", issue);
	}
	
	public String getQxcIssue() {
		return getString("issue_qxc", "");
	}
	
	public void saveQxcEndTime(String endTime) {
		putString("issue_qxc_time", endTime);
	}
	
	public String getQxcEndTime() {
		return getString("issue_qxc_time", "");
	}
	
	/** ----------11选5--------- **/
	
	public void save11x5Issue(String issue) {
		putString("issue_11x5", issue);
	}
	
	public String get11x5Issue() {
		return getString("issue_11x5", "");
	}
	
	public void save11x5EndTime(String endTime) {
		putString("issue_11x5_time", endTime);
	}
	
	public String get11x5EndTime() {
		return getString("issue_11x5_time", "");
	}
}
