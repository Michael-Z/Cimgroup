package cn.com.cimgroup.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;

/**
 * Created by small on 16/1/15.
 */
public class UserCount implements Parcelable {

    public String userId;
    public String betAccount;               //投注账户
    public String lemiAccount;              //乐米账户
    public String redPkgNum;                //红包剩余总量;
    public String redPkgAccount;            //红包剩余总额;
    public String resCode;
    public String resMsg;

    public String buyRedMoney;              //购买红包金额
    public String convertRedNum;            //兑换红包个数;
    public String convertRedTotalMoney;       //兑换红包金额;
    public HashMap<String ,List<TcjczqObj>> convertRedList;             //兑换红包列表
    public String giveRedNum;               //赠送红包个数;
    public String giveRedTotalMoney;        //赠送红包金额;
    public HashMap<String ,List<TcjczqObj>> giveRedList;                //赠送红包

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.betAccount);
        dest.writeString(this.lemiAccount);
        dest.writeString(this.redPkgNum);
        dest.writeString(this.redPkgAccount);
        dest.writeString(this.resCode);
        dest.writeString(this.resMsg);
        dest.writeString(this.buyRedMoney);
        dest.writeString(this.convertRedNum);
        dest.writeString(this.convertRedTotalMoney);
        dest.writeString(this.giveRedNum);
        dest.writeString(this.giveRedTotalMoney);
    }

    public UserCount() {
    }

    protected UserCount(Parcel in) {
        this.userId = in.readString();
        this.betAccount = in.readString();
        this.lemiAccount = in.readString();
        this.redPkgNum = in.readString();
        this.redPkgAccount = in.readString();
        this.resCode = in.readString();
        this.resMsg = in.readString();
        this.buyRedMoney = in.readString();
        this.convertRedNum = in.readString();
        this.convertRedTotalMoney = in.readString();
        this.giveRedNum = in.readString();
        this.giveRedTotalMoney = in.readString();
    }

    public static final Creator<UserCount> CREATOR = new Creator<UserCount>() {
        public UserCount createFromParcel(Parcel source) {
            return new UserCount(source);
        }

        public UserCount[] newArray(int size) {
            return new UserCount[size];
        }
    };
}
