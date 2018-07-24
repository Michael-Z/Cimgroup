package cn.com.cimgroup.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.LinkedHashMap;

import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.R;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * Created by small on 15/12/30.
 */
public class TextActiity extends BaseActivity implements View.OnClickListener {


    public static final String TYPE = "type";
    public static final int HELP = 0;
    public static final int ABOUT = 1;
    public static final int SERVER = 2;
    public static final int REDPKGEXP = 3;
    public static final int GAMEHELP = 4;

    private TextView textView,titleView;
    private int type;
    private LinearLayout serverView;

    private TextView leftButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_layout);
        findViewById();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getInt(TYPE);
            if (type == HELP) {
                initHelpView();
            } else if (type == ABOUT) {
                initAboutView();
            } else if (type == SERVER) {
                initServerView();
            } else if(type == REDPKGEXP){
                initRedPkgExp();
            } else if(type == GAMEHELP){
            	initGameHelpView();
            }
        }
    }

    /**
     * 初始化娱乐场玩法介绍
     * 
     */
    private void initGameHelpView() {
    	titleView.setText("娱乐场规则");
        String helpHtml =
                "<html lang=\"en\">" +
                        "<head>" +
                        "</head>" +
                        "<body>" +
                        "<h4 style=\"font-size:20px\">一、参与规则：</h4>" +
                        "<p>1、注册账号，登录网站或客户端，每日进入活动页面点击'竞猜打卡'，即可领取100竞猜乐米。竞猜打卡和首页签到不影响,可同时领取。</p>" +
                        "<p>2、乐米获取方法：商城购买、娱乐场竞猜赚取乐米或通过手机客户端分享获得乐米（每天首次分享）。</p>" +
                        "<p>3、恶意行为者取消参赛资格。</p>" +
                        "<h4 style=\"font-size:20px\">二、足球竞猜玩法： </h4>" +
                        "<p>1、在竞猜记录列表可以查看竞猜记录以及对应中奖情况。</p>" +
                        "<p>2、奖金计算方式：乐米奖励=投注乐米+奖池分得乐米。</p>" +
                        "<p>3、奖池乐米为 “未猜中”方投中乐米总数，奖池乐米的90%按照投注倍数平分给“猜中”用户，倍数越多，分得越多。涉及到比赛延期情况，返还本金，不计入排行榜。</p>"+
                        "<p>4、距离比赛开始5分钟截止投注。</p>"+
                        "<dl><dt>排行规则：</dt>"+
                        "<dd>1、每周一中午12点更新上周排行榜。<dd>"+
                        "<dd>2、累积中奖排行榜按照比赛时间（周一0:00~周日23:59）所获得的竞猜奖励数量进行排行，若乐米数量相同则竞猜次数较多者靠前。</dd><dl>"+
                        "<dl><dt>奖励：</dt>" +
                        "<dd>每期排行榜前十名可获与竞猜奖励同等数量的乐米作为奖励，乐米可在商城进行商品兑换。</dd></dl>"+
                        "<h4 >三、翻牌游戏玩法： </h4>" +
                        "<p>1、竞猜每轮选中牌面的数字的“大小奇偶”，并进行投注，投注乐米数量越多，中奖乐米越高。每次只能投注一个竞猜选项。</p>"+
                        "<p>2、如何判定中奖：</p>"+
                        "<p>牌面数字为1-10</p>"+
                        "<p>\t\t大：6、7、8、9、10</p>"+
                        "<p>\t\t小：1、2、3、4、5</p>"+
                        "<p>\t\t奇：1、3、5、7、9</p>"+
                        "<p>\t\t偶：2、4、6、8、10</p>"+
                        "<p>竞猜选项与开出牌面数字任意一张一致即为中奖</p>"+
                        "<p>3、玩法步骤：</p>"+
                        "<p>\t\t第一步：进入游戏页面，点击发牌按钮，开始发牌。</p>"+
                        "<p>\t\t第二步：选择投注选项及投注金额（可自定义金额）。</p>"+
                        "<p>\t\t第三步：点击翻牌查看结果，猜中即为赢，反之为输。</p>"+
                        "<h4>四、转盘游戏玩法：</h4>"+
                        "<p>1、竞猜转盘每轮开出的数字大小进行投注，投注乐米数量越多，中奖乐米越高。每次只能投注一个竞猜选项。</p>"+
                        "<p>2、如何判定中奖： </p>"+
                        "<p>转盘数字为1-8</p>"+
                        "<p>\t\t大：5、6、7、8</dp>"+
                        "<p>\t\t小：1、2、3、4</p>"+
                        "<p>竞猜选项与转出数字一致即为中奖，转到“金币”按中奖计算。</p>"+
                        "<p>玩法步骤：</p>"+
                        "<p>\t\t第一步：选择投注选项及投注金额（可自定义金额）。</p>"+
                        "<p>\t\t第二步：点击“立即开始”按钮转动转盘。</p>"+
                        "<p>\t\t第三步：转盘停止查看结果，猜中即为赢，反之为输。</p>"+
                        "<h3 style=\"font-weight:bold;\">注：本活动最终解释权归乐博彩票所有，同时我们将保留重新修改玩法规则的权利。</h3>"+
                        "</body>" +
                        "</html>";

        textView.setText(Html.fromHtml(helpHtml));
    
		
	}

	private void findViewById() {
        textView = (TextView) findViewById(R.id.text);
        serverView = (LinearLayout) findViewById(R.id.server_view);
        leftButton = (TextView) findViewById(R.id.left_button);
        titleView = (TextView) findViewById(R.id.title_view);
        leftButton.setOnClickListener(this);

        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void initHelpView() {
    	titleView.setText("帮助信息");
        String helpHtml =
                "<html lang=\"en\">" +
                        "<head>" +
                        "</head>" +
                        "<body>" +
                        "<h4 style=\"font-size:20px\">网站是否收费？</h4>" +
                        "<p>\t\t用户中奖后，本平台不向用户收取任何提成或佣金。</p>" +
                        "<h4>为什么要实名认证？</h4>" +
                        "<p>\t\t真实姓名与身份证号码是领取大奖的唯一凭证，绑定的银行卡必须与真实姓名一致。注册的手机号应为使用的手机号，以便中奖后收到通知。</p>" +
                        "<h4>个人信息填写错误？</h4>" +
                        "<p>\t\t个人信息填写错误的情况下不允许自己修改账户信息，可拨打客服热线：010-65617701；</p>" +
                        "<h4>派奖时间是多久？</h4>" +
                        "<p>\t\t足彩开奖当天，最晚下午可以派奖到帐户；数字彩开奖当天，23：30左右可以派奖到帐户；高频彩种在开奖后3-5分钟派奖到账户。</p>" +
                        "<h4>如何派奖？</h4>" +
                        "<p>1、单注单倍奖金1万元以下（不含1万元），由系统自动派奖到用户在乐博网的账户中；（请务必填写实名信息）</p>" +
                        "<p>2、单注单倍奖金1万元以上，客服将电话通知。</p>" +
                        "<p>注：合买订单无论中奖多少，全部由系统自动派奖到合买用户的奖金账户中。网站不收取任何手续费及佣金。</p>" +
                        "<h4>中奖奖金如何扣税？</h4>" +
                        "<p>\t\t根据彩票管理机构的相关规定，中奖彩票单注单倍奖金超过1万元（含1万元）时，中奖人需要缴纳奖金的20％作为个人偶然所得税；税金由彩票中心代扣代缴。" +
                        "选择倍投投注时，只要开奖后单注单倍奖金不超过1万元，就算翻倍后奖金超过1万元，也无须缴纳个人偶然所得税。</p>" +
                        "</body>" +
                        "</html>";

        textView.setText(Html.fromHtml(helpHtml));
    }


    private void initAboutView() {
    	titleView.setText("关于乐博");
        String aboutHtml = "<head>" +
                "</head>" +
                "<body>" +
                "<h2>" +
                "关于乐博：" +
                "</h2>" +
                "<p>" +
                "\t\t乐博为谊诺（北京）传媒科技有限公司（以下简称：谊诺科技公司）旗下的无纸化购彩平台。谊诺科技公司于2014年聚合多位彩票行业资深人事开始从事彩票方面的业务，我公司秉承“客户为本、诚信经营”的服务宗旨，为全国彩民提供全方位、多领域的购彩服务及资讯服务。" +
                "</p>" +
                "<p>" +
                "\t\t谊诺科技公司有员工近100人，其中产品研发人员占8成以上，并与移动运营商、银行、手机厂商、主流应用商店、中间件厂商、渠道分销商建立了长期的战略合作伙伴关系，构建了一整套完整的在线彩票服务体系，提供彩票在线销售、软件研发、技术支持、市场运营、系统运维等服务。" +
                "</p>" +
                "<p>" +
                "\t\t在注重产品服务的同时，公司还极为重视企业文化建设和员工培养，为员工营造出宽松愉快、积极向上的工作氛围，建立了一支富有工作激情、爱岗敬业、开拓创新、敢打硬仗的队伍。人才和技术的优势使谊诺科技公司以最快的速度开发新产品、抢占市场、获取竞争优势。" +
                "</p>" +
                "<h2>" +
                "公司优势：" +
                "</h2>" +
//                "<p>1、行业的优势</p>" +
                "<p>1、行业的优势<br />"+
                "\t\t我们的团队骨干都有着多年彩票行业背景，资源丰富。目前，已经与运营商、银行、手机厂商、主流应用商店、中间件厂商、渠道分销商等就投注业务及移动增值业务方面的合作协定协议。" +
                "</p>" +
//                "<p>2、完备的公司资质</p>" +
				"<p>2、完备的公司资质<br />" +
                "\t\t我们拥有“中华人民共和国电信与信息服务业务经营许可证”、 “中华人民共和国增值电信业务经营许可证”，对于开展电话销售彩票和互联网销售彩票提供有力的保证。</p>" +
//                "<p>3、优秀的团队</p>" +
				"<p>3、优秀的团队<br />" +
                "\t\t我们的管理团队由具有丰富行业经验、管理经验和技术研发经验的杰出人才组成，兼具了创业者的激情和职业经理人的管理智慧，堪称一支出色的团队。团队骨干伴随着中国的互联网、移动互联网、彩票行业经历了数次潮起潮落。多年的磨砺让团队对互联网、移动互联网行业有着深刻地认识。</p>" +
                "</body>" +
                "</html>";
        textView.setText(Html.fromHtml(aboutHtml));
    }


    private void initServerView() {

        String serverText = "<head>" +
                "</head>" +
                "<body>" +
                "</p>" +
                "\t\t 您在使用软件或者投注过程中,遇到任何问题都可以一键接通我们的客服专线,届时将会有我们的专业客服团队为您来解答。" +
                "</p>" +
                "</body>" +
                "</html>";

        textView.setText(Html.fromHtml(serverText));

        serverView.addView(initItem(R.drawable.server_phone, "联系客服:  ", getString(R.string.server_phone), R.drawable.connect_server, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.server_phone)));
                TextActiity.this.startActivity(intent);
            }
        }));
        serverView.addView(initItem(R.drawable.server_mail, "客服邮箱:  ", getString(R.string.server_mail), R.drawable.copy_server, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(getString(R.string.server_mail));
            }
        }));
        serverView.addView(initItem(R.drawable.server_qq, "客服QQ:  ", getString(R.string.server_tenc), R.drawable.copy_server, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(getString(R.string.server_tenc));
            }
        }));
    }

    private void initRedPkgExp(){
        titleView.setText("红包说明");
        textView.setText(Html.fromHtml(getString(R.string.redpkg_explain)));
    }

    private void copy(String string) {
        ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        cmb.setText(string);
        ToastUtil.shortToast(this, getString(R.string.copy_succ));
    }

    private LinearLayout initItem(int logoView, String title, String content, int operView, View.OnClickListener listener) {
        LinearLayout ll = new LinearLayout(this);
        TextView textView = new TextView(this);
        textView.setCompoundDrawablePadding(20);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(60, 10, 30, 20);
        textView.setLayoutParams(params);
        Drawable drawable = getResources().getDrawable(logoView);
        Drawable operDraw = getResources().getDrawable(operView);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        operDraw.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(drawable, null, null, null);
        ll.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        SpannableString ss = new SpannableString(title + content);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.color_black)), 0, title.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.hall_grey_word)), title.length(), title.length() + content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ss);
        ll.addView(textView);

        ImageView imageView = new ImageView(this);
        imageView.setImageResource(operView);

        imageView.setOnClickListener(listener);
        ll.addView(imageView);
        return ll;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_button:
                onBackPressed();
                break;
        }
    }
}
