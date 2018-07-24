package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by small on 16/1/28.
 */
public class TzObj implements Serializable {
	/**投注类型*/
    public String bettingType;
    /**彩种号*/
    public String gameNo;
    public String issue;
    public String multiple;
    public String totalSum;
    public String isChase;
    public String chase;
    public String isRedPackage;
    public String redPackageId;
    public String redPackageMoney;
    public String choiceType;
    public String planEndTime;
    public String stopCondition;
    public String gameType;
    public String redPkgType;       //0:购买红包 1：兑换红包 2：赠送红包
    public int lotteryName;
   public  Map<Integer, Map<String, String>> bettingReq;



}
