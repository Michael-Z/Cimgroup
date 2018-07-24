package cn.com.cimgroup.bean;

/**
 * Created by small on 16/1/6.
 * 查询乐米明细条目;
 */
public class LeMiDetail {
    public String createTime;       //交易时间;

    //1、商品兑换
//    2、签到
//    3、购彩
//    4、购买乐米
//    5、注册
//    6、首次充值 ，
//    7商品兑换，
//    8兑换失败退乐米
//    9、乐米中奖
    public String dealType;
    public String money;    //收入金额
    public String inOut;    //进账/
    public String balance;  //余额
    public String typeDes;
    public String detailMsg;    //详细信息;

}
