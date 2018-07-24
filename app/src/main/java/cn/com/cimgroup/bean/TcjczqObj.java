package cn.com.cimgroup.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Description:
 * @author:yang
 * @copyright www.wenchuang.com
 * @Date 16/1/21
 */
public class TcjczqObj implements  Serializable{

    public static final int GIVE = 0;
    public static final int CONVERT = 1;
    public static final int BUY = 2;

    public int type;

    public String buyRedMoney;          //购买红包金额;

    public String converRedPkgID;      //兑换红包编号;
    public String converRedMoney;      //兑换红包面额;
    public String converTime;           //兑换时间;
    public String converFailTime;          //失效时间;
    public String converRedLeMi;           //兑换红包所用的乐米;
    public String converDes;            //兑换红包描述;

    public String giveRedPkgID;         //赠送换红包编号;
    public String giveRedMoney;         //赠送红包面额;
    public String giveTime;             //赠送红包时间;
    public String giveFailTime;         //赠送红包失效时间;
    public String giveRedLottery;       // -1：通用型 ,其它对应是彩种编号
    public String giveDes;              //获赠红包描述

    public Boolean choose = false;
}

