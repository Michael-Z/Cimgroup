package cn.com.cimgroup.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 Created by small on 16/1/17.
 <p/>
 红包; */
public class RedPkgProduct implements Parcelable {
    public String redPkgType;    //红包类型 1:兑换, 2:赠送; 3:购买;
    public String redPkgMianE;  //红包面额;
    public String redPkgInvalidTime;//失效熬时间;
    public String redPkgLottery;    //all:通用型,   lotteryType:彩种类型;
    public String isInvalid;   //0:正常,  1:即将过期;
    public String isInvalidOrUsed;  //1:已使用, 2:以失效;
    public String redPkgDesc;       //红包描述;
    public String redPkgUseDesc;     //红包使用描述;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.redPkgType);
        dest.writeString(this.redPkgMianE);
        dest.writeString(this.redPkgInvalidTime);
        dest.writeString(this.redPkgLottery);
        dest.writeString(this.isInvalid);
    }

    public RedPkgProduct() {
    }

    protected RedPkgProduct(Parcel in) {
        this.redPkgType = in.readString();
        this.redPkgMianE = in.readString();
        this.redPkgInvalidTime = in.readString();
        this.redPkgLottery = in.readString();
        this.isInvalid = in.readString();
    }

    public static final Creator<RedPkgProduct> CREATOR = new Creator<RedPkgProduct>() {
        public RedPkgProduct createFromParcel(Parcel source) {
            return new RedPkgProduct(source);
        }

        public RedPkgProduct[] newArray(int size) {
            return new RedPkgProduct[size];
        }
    };
}
