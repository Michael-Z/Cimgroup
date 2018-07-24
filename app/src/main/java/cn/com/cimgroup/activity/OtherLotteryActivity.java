package cn.com.cimgroup.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.ControllLottery;
import cn.com.cimgroup.bean.GameObj;
import cn.com.cimgroup.config.ControllLotteryConfiguration;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;

public class OtherLotteryActivity extends BaseActivity implements OnClickListener {
	
	private ControllLottery controll;
	
	private String controllTimeId = "0";
	
	private ImageView p5Status;
	
	private ImageView qxcStatus;
	
	private ImageView dltStatus;
	
	private ImageView p3Status;
	
	private ImageView jqsStatus;
	
	private ImageView bqcStatus;
	
	private TextView p5Text;
	
	private TextView qxcText;
	
	private TextView dltText;
	
	private TextView p3Text;
	
	private TextView jqsText;
	
	private TextView bqcText;
	
	private TextView p5Title;
	
	private TextView qxcTitle;
	
	private TextView dltTitle;
	
	private TextView p3Title;
	
	private TextView jqsTitle;
	
	private TextView bqcTitle;
	
	private RelativeLayout p5Layout;
	
	private RelativeLayout bqcLayout;
	
	private RelativeLayout jqsLayout;
	
	private RelativeLayout qxcLayout;
	
	private RelativeLayout p3Layout;
	
	private RelativeLayout dltLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_other_lottery);
		super.onCreate(savedInstanceState);
		initView();
		controll = ControllLotteryConfiguration.getConfiguration().getLocalControllInfo();
		if (controll != null) {
			controllTimeId = controll.getTimeId();
		}
		
		Controller.getInstance().getHallControllLottery(GlobalConstants.NUM_CONTROLLLOTTERY, controllTimeId, "11", mBack);
	}
	
	private void initView() {
		TextView mWordTextView = (TextView) findViewById(R.id.textView_title_word);
		mWordTextView.setText(getResources().getString(R.string.lotteryhall_other));
		
		p5Layout = (RelativeLayout) findViewById(R.id.layout_hall_p5);
		
		bqcLayout = (RelativeLayout) findViewById(R.id.layout_hall_bqc);
		
		jqsLayout = (RelativeLayout) findViewById(R.id.layout_hall_jqs);
		
		qxcLayout = (RelativeLayout) findViewById(R.id.layout_hall_qxc);
		
		dltLayout = (RelativeLayout) findViewById(R.id.layout_hall_dlt);
		
		p3Layout = (RelativeLayout) findViewById(R.id.layout_hall_p3);
		
		//排列5的右上角图标 用来显示彩种状态 如今日开奖，劲爆活动，今日开奖
		p5Status = (ImageView) findViewById(R.id.image_other_p5_status);
		p5Text = (TextView) findViewById(R.id.textView_other_p5_text);
		p5Title = (TextView) findViewById(R.id.textView_other_p5);
		
		qxcStatus = (ImageView) findViewById(R.id.image_other_qxc_status);
		qxcText = (TextView) findViewById(R.id.textView_other_qxc_text);
		qxcTitle = (TextView) findViewById(R.id.textView_other_qxc);
		
		dltStatus = (ImageView) findViewById(R.id.image_other_dlt_status);
		dltText = (TextView) findViewById(R.id.textView_other_dlt_text);
		dltTitle = (TextView) findViewById(R.id.textView_other_dlt);
		
		p3Status = (ImageView) findViewById(R.id.image_other_p3_status);
		p3Text = (TextView) findViewById(R.id.textView_other_p3_text);
		p3Title = (TextView) findViewById(R.id.textView_other_p3);
		
		jqsStatus = (ImageView) findViewById(R.id.image_other_jqs_status);
		jqsText = (TextView) findViewById(R.id.textView_other_jqs_text);
		jqsTitle = (TextView) findViewById(R.id.textView_other_jqs);
		
		bqcStatus = (ImageView) findViewById(R.id.image_other_bqc_status);
		bqcText = (TextView) findViewById(R.id.textView_other_bqc_text);
		bqcTitle = (TextView) findViewById(R.id.textView_other_bqc);
		
		p5Layout.setOnClickListener(this);
		bqcLayout.setOnClickListener(this);
		jqsLayout.setOnClickListener(this);
		qxcLayout.setOnClickListener(this);
		p3Layout.setOnClickListener(this);
		dltLayout.setOnClickListener(this);
	}
	
	private CallBack mBack = new CallBack() {
		public void getHallControllLotterySuccess(final ControllLottery cLottery) {
			runOnUiThread(new Runnable() {

				public void run() {
					List<GameObj> list = cLottery.getGameList();
					if (list !=null) {
						for (int i = 0; i < list.size(); i++) {
							GameObj obj = list.get(i);
							setLotteryStatus(obj);
						}
					}
				}
			});
		};
		
		public void getHallControllLotteryError(String error) {
//			System.out.println("获取失败");
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			mActivity.finish();
			break;
		case R.id.layout_hall_p5:
			startActivity(LotteryPL5Activity.class);
			break;
		case R.id.layout_hall_bqc:
			Intent intent = new Intent(this, LotteryOldFootballActivity.class);
			intent.putExtra("lotoId", 2);
			startActivity(intent);
			break;
		case R.id.layout_hall_jqs:
			Intent intent1 = new Intent(this, LotteryOldFootballActivity.class);
			intent1.putExtra("lotoId", 1);
			startActivity(intent1);
			break;
		case R.id.layout_hall_qxc:
			startActivity(LotteryQxcActivity.class);
			break;
		case R.id.layout_hall_dlt:
			startActivity(LotteryDLTActivity.class);
			break;
		case R.id.layout_hall_p3:
			startActivity(LotteryPL3Activity.class);
			break;
		default:
			break;
		}
	}
	
	private void setLotteryStatus(GameObj obj) {
		int status = -1;
		//status 0-停售 1-销售 2-加奖 3-活动 4-今日开奖
		if (obj.getIsSale().equals("0")) {
			//是否销售：1、销售，0、停售
			status = 1;
		} else {
			if (!obj.getIsAward().equals("0")) {
				//是否嘉奖：0无图标，1嘉奖,2 活动
				if (obj.getIsAward().equals("1")) {
					status = 2;
				} else {
					status = 3;
				}
			} else {
				if (obj.getIsTodayAward().equals("1")) {
					//是否今日开奖：0否，1是
					status = 4;
				}
			}
		}
		switch (obj.getGameNo()) {
		case GlobalConstants.TC_JCZQ:
//			jczqStatus
			break;
			//这个case指向进球数
		case GlobalConstants.TC_JQ4:
			setTitleColor(jqsTitle, status);
			setControllLottery(jqsStatus, jqsText, jqsLayout, status);
//			jqsStatus
			break;
			//这个case指向半全场
		case GlobalConstants.TC_BQ6:
			setTitleColor(bqcTitle, status);
			setControllLottery(bqcStatus, bqcText, bqcLayout, status);
//			bqcStatus
			break;
		case GlobalConstants.TC_SF9:
			
			break;
		case GlobalConstants.TC_SF14:
//			sfcStatus
			break;
		case GlobalConstants.TC_DLT:
//			dltStatus
			setTitleColor(dltTitle, status);
			setControllLottery(dltStatus, dltText, dltLayout, status);
			break;
		case GlobalConstants.TC_PL3:
//			p3Status
			setTitleColor(p3Title, status);
			setControllLottery(p3Status, p3Text, p3Layout, status);
			break;
			//这个case指向排列5 
		case GlobalConstants.TC_PL5:
//			p5Status
			setTitleColor(p5Title, status);
			setControllLottery(p5Status, p5Text, p5Layout, status);
			break;
		case GlobalConstants.TC_QXC:
//			qxcStatus
			setTitleColor(qxcTitle, status);
			setControllLottery(qxcStatus, qxcText, qxcLayout, status);
			break;
		default:
			break;
		}
	}
	
	private void setTitleColor(TextView text, int status) {
		//status 0-停售 1-销售 2-加奖 3-活动 4-今日开奖
		switch (status) {
		case 2:
		case 3:
		case 4:
			text.setTextColor(getResources().getColor(R.color.color_red));
			break;
		default:
			break;
		}
	}
	
	public void setControllLottery(ImageView view, TextView text, RelativeLayout layout, int status){
		//status 0-停售 1-销售 2-加奖 3-活动 4-今日开奖
		switch (status) {
		case 0:
			layout.setEnabled(false);
			view.setImageDrawable(getResources().getDrawable(R.drawable.icon_lottery_stop));
			text.setText("暂停销售");
			view.setVisibility(View.VISIBLE);
			text.setTextColor(getResources().getColor(R.color.color_gray_indicator));
			break;
		case 2:
			layout.setEnabled(true);
			view.setImageDrawable(getResources().getDrawable(R.drawable.icon_lottery_other));
			text.setText("千万加奖");
			view.setVisibility(View.VISIBLE);
			text.setTextColor(getResources().getColor(R.color.color_yellow_light));
			break;
		case 3:
			layout.setEnabled(true);
			view.setImageDrawable(getResources().getDrawable(R.drawable.icon_lottery_other));
			text.setText("劲爆活动");
			view.setVisibility(View.VISIBLE);
			text.setTextColor(getResources().getColor(R.color.color_yellow_light));
			break;
		case 4:
			layout.setEnabled(true);
			view.setImageDrawable(getResources().getDrawable(R.drawable.icon_lottery_other));
			text.setText("今日开奖");
			view.setVisibility(View.VISIBLE);
			text.setTextColor(getResources().getColor(R.color.color_yellow_light));
			break;
		default:
			layout.setEnabled(true);
			view.setVisibility(View.INVISIBLE);
			text.setText("");
			break;
		}
	}

}
