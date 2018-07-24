package cn.com.cimgroup.bean;

import java.io.Serializable;

/**
 * Created by small on 16/1/14.
 */
public class Product implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8527521998070869663L;
	/**商品id*/
    public String productId;       
    /**商品面值*/
    public String productMoney; 
    /**商品面值单位*/
    public String unit;
    /**商品售价 元*/
    public String productSaleMoney;     
    /**优惠价格*/
    public String productPreferential;   
    /**商品名称*/
    public String produceName;  
    
    public String produceDesc;  
    /**商品描述*/
    public String productDesc;
    /**失效时间*/
    public String expTime;
    /**兑换状态0:成功，1：处理中，2：失败*/
    public String exStatus;
    public String productName;
    /**0: 库存不足; 1:可以兑换*/
    public String isEnough;    
    /**兑换商品剩余数量*/
    public String productAmount; 
    //0 不能兑换, 1可以兑换;
    public String isSale = "1";  
    /**红包状态:0:可以销售,1:余额不足,2:库存不足:3暂停销售*/
    public String sale;


    /**产品类型:HB：红包  HF：充值卡  JDK：京东券  GMK：国美券  YHDK:一号店券  YMXK：亚马逊券  SNK：苏宁券*/
    public String productType;  
    public String exchangeTime; //  兑换时间;
    public String cell;         //手机号;
    public String passWord;     //卡密码;

    public static String HB = "HB";
    public static String HF = "HF";
    public static String JDK = "JDK";
    public static String GMK = "GMK";
    public static String YHDK = "YHDK";
    public static String YMXK = "YMXK";
    public static String SNK = "SNK";




    public static final String REDPKG = "0";
    public static final String LEMI = "1";

}
