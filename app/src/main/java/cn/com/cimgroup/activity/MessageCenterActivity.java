package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.MessageCenterAdapter;
import cn.com.cimgroup.frament.GridFragment;
import cn.com.cimgroup.frament.RedPacketNoticeFragment;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.view.Msg_ViewPagerIndicator;
import cn.com.cimgroup.view.NoScrollViewPager;
import cn.com.cimgroup.xutils.ActivityManager;

/**
 * Created by small on 15/12/28.
 * <p/>
 * 消息中心/乐米兑换;
 */
public class MessageCenterActivity extends BaseActivity implements
		View.OnClickListener {

	public static final String TYPE = "TYPE";
	public static final int LEMICONVERT = 0x00002;
	public static final int REDPACKET = 0x00003;

	private TextView leftButton;
	private ImageView rightBtton;
	private TextView titleView, buyRedPacket, rightTextView;
	private Msg_ViewPagerIndicator tabs;
	// private ViewPagerIndicator tabs_lemi;
	// private ViewPager viewPager;
	private NoScrollViewPager viewPager;
	private MessageCenterAdapter adapter;
	private List<String> titleList;
	private List<BaseFrament> fragmentList;
	private int type = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			type = bundle.getInt(TYPE,-1);
		}

		// tabs这个控件是ViewPagerIndicator
		// viewPager这个控件是ViewPager
		setContentView(R.layout.activity_message_center);
		titleList = new ArrayList<String>();
		fragmentList = new ArrayList<BaseFrament>();
		initView();
		//设置可见的tab个数
		tabs.setVisibleTabCount(titleList.size());
		// 设置Tab上的标题 消息/活动
		tabs.setTabItemTitles(titleList, type);
		// 1设置ViewPager的Adapter
		adapter = new MessageCenterAdapter(getSupportFragmentManager(),
				titleList, fragmentList);
		// 2将ViewPager和adapter进行关联
		viewPager.setAdapter(adapter);
		// 设置关联的ViewPager 将ViewPagerIndicator和ViewPager关联起来
		tabs.setViewPager(viewPager, 0, type);
		
	}

	private void initMessageData() {
//		titleList.add("消息");
//		titleList.add("活动");

		
//		MessagePersonFragment fragment_Msg = new MessagePersonFragment();
//		fragmentList.add(fragment_Msg);
//		
//		MessagePersonFragment_Act fragment_Act = new MessagePersonFragment_Act();
//		fragmentList.add(fragment_Act);
	}

	// 在onresume（）方法中去判断登陆超时，如果超时，跳转登陆，关闭当前activity
	@Override
	public void onResume() {
		if (App.userInfo != null && App.userInfo.getResCode() != null
				&& App.userInfo.getResCode().equals("3002")) {
			App.userInfo.setResCode("0");
			UserLogic.getInstance().saveUserInfo(App.userInfo);
			finish();
		}
		super.onResume();
	}

	private void initRedPacketData() {
//		buyRedPacket.setVisibility(View.VISIBLE);
		rightBtton.setVisibility(View.VISIBLE);
		titleView.setText("红包");
		titleList.add("可用");
		titleList.add("使用/过期");
		for (int i = 0; i < 2; i++) {
			RedPacketNoticeFragment tempFragment = new RedPacketNoticeFragment();
			Bundle bundle = new Bundle();
			bundle.putInt(RedPacketNoticeFragment.TYPE, i);
			tempFragment.setArguments(bundle);
			fragmentList.add(tempFragment);
		}
		rightBtton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MessageCenterActivity.this,
						TextActiity.class);
				intent.putExtra(TextActiity.TYPE, TextActiity.REDPKGEXP);
				startActivity(intent);
			}
		});
	}

	private String[] lemiConvertArray = { "充值卡", "京东券", "国美券", "苏宁券", "红包" };
	private int[] lemiConvertProdect = { 
			GridFragment.LEMITELE, GridFragment.LEMIJD, GridFragment.LEMIGM,
			GridFragment.LEMISN, GridFragment.LEMIRED};

	private void initLemiData() {
		titleView.setText("乐米兑换");
		rightTextView.setText("兑换记录");
		rightTextView.setVisibility(View.VISIBLE);
		rightTextView.setOnClickListener(this);

		for (int i = 0; i < lemiConvertArray.length; i++) {
			titleList.add(lemiConvertArray[i]);
			Bundle tempBundle = new Bundle();
			GridFragment tempFragment = new GridFragment();
			tempBundle.putInt(GridFragment.TYPE, lemiConvertProdect[i]);
			tempFragment.setArguments(tempBundle);
			fragmentList.add(tempFragment);
		}
	}

	@SuppressLint("ResourceAsColor")
	private void initView() {
		leftButton = (TextView) findViewById(R.id.left_button);
		rightBtton = (ImageView) findViewById(R.id.right_btton);
		titleView = (TextView) findViewById(R.id.title_view);
		tabs = (Msg_ViewPagerIndicator) findViewById(R.id.tabs);
		buyRedPacket = (TextView) findViewById(R.id.buy_red_packet);
		viewPager = (NoScrollViewPager) findViewById(R.id.view_pager);
		rightTextView = (TextView) findViewById(R.id.right_textview);

		tabs.setTabItemTextColor(R.color.color_black, R.color.color_red);
		// 设置选项卡item 选中、未选中背景色
		tabs.setTabItemBgColor(R.color.color_white, R.color.color_white);
		tabs.setIndicatorHeight(4);
		tabs.setTextSize(16);

		buyRedPacket.setOnClickListener(this);
		leftButton.setOnClickListener(this);

		switch (type) {
		case REDPACKET:
			// 红包详情;
			initRedPacketData();
			break;
		case LEMICONVERT:
			// 乐米兑换;
			initLemiData();
			break;
		default:
//			titleView.setText("消息/活动");
//			initMessageData();
			break;
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_button:
			finish();
			break;
		case R.id.buy_red_packet:
			// 购买红包;
			Intent buyRed = new Intent(MessageCenterActivity.this,
					BuyRedPacketActivity.class);
			buyRed.putExtra(GridFragment.TYPE, GridFragment.BUYREDPACK);
			startActivity(buyRed);
			finish();
			break;
		case R.id.right_textview:
			// 兑换记录;
			if (App.userInfo == null) {
				startActivity(LoginActivity.class);
			}else {
				GlobalConstants.isRefreshFragment = true;
				GlobalConstants.personTagIndex = 2;
				GlobalConstants.isShowLeMiFragment = true;
				MainActivity main = (MainActivity) ActivityManager
						.isExistsActivity(MainActivity.class);
				if (main != null) {
					// 如果存在MainActivity实例，那么展示LoboHallFrament页面
					ActivityManager.retain(MainActivity.class);
					main.showFrament(3);
				}
				
//				startActivity(LeMiConvertNotesActivity.class);
			}
			break;
		}
	}

	/**
	 * 设置消息的状态 调用indicator中的方法 对外提供方法 供子fragment调用
	 * 
	 * @param type
	 *            消息类型 0-全部 1-消息 2-活动
	 * @param finish
	 *            消息状态 0-设置未读完 1-设置已读完
	 */
	public void setMessageState(int type, int finish) {
		tabs.setMessageState(type, finish);
	}
}
