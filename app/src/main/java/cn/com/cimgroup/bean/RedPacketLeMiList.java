package cn.com.cimgroup.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by small on 16/1/14.
 */
public class RedPacketLeMiList implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7267227563342443336L;
	public String betAccount;           //账户余额;
    public String redPkgNum;            //红包数量;
    public String redPkgAccount;        //红包金额;
    public String lemiAccount;          //乐米余额;
    public List<Product> productList;
    public String resCode;
    public String resMsg;
    public String message;

    public String userId;
    public String pageNo;
    public String total;


}
