package cn.com.cimgroup.bean;

/**
 * Created by small on 16/1/17.
 */
public class MessageObj {
    public String messageId;
    public String titile;           //标题
    public String imgUrl;           //活动图标
    public String content;          //内容
    public String timeQuJian;       //时间
    public String isRead;           //0未查看   1  已查看
    public String isJumpURL;             //0是      1不是
    public String jumpURL;            // h5路径
    public String isNeedLogin;          //查看详情是否需要登录(用在活动列表);
    public boolean isShowDetail;         //是否展开详情;
}
