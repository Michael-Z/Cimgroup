package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.PushObj;
import cn.com.cimgroup.bean.PushResult;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.xutils.ToastUtil;

public class CenterPushSetActivity extends BaseActivity implements OnClickListener {
	
	private String mTitleText = "";
	
	private ImageView pushWin;
	
	private ImageView pushSFC;
	
	private ImageView pushDLT;
	
	private ImageView pushQXC;
	
	private ImageView pushP3;
	
	private ImageView pushP5;
	
	private ImageView pushZhuiStop;
	
	private ImageView pushLike;
	
	private String isSetting = "1";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mcenter_pushset);
		initView();
		isSetting = "1";
		Controller.getInstance().pushSet(GlobalConstants.NUM_PUSHSET, UserLogic.getInstance().getDefaultUserInfo().getUserId(), isSetting, new ArrayList<JSONObject>(), mBack);
	}
	
	private void initView() {
		mTitleText = getResources().getString(R.string.center_push);
		// title 左侧的文字
		TextView mTitleName = (TextView) findViewById(R.id.textView_title_word);
		mTitleName.setText(mTitleText);
		
		
		pushWin = (ImageView) findViewById(R.id.imageView_push_win);
		pushWin.setSelected(true);
		
		pushSFC = (ImageView) findViewById(R.id.imageView_push_sfc);
		pushSFC.setSelected(true);
		
		pushDLT = (ImageView) findViewById(R.id.imageView_push_dlt);
		pushDLT.setSelected(true);
		
		pushQXC = (ImageView) findViewById(R.id.imageView_push_qxc);
		pushQXC.setSelected(true);
		
		pushP3 = (ImageView) findViewById(R.id.imageView_push_p3);
		pushP3.setSelected(true);
		
		pushP5 = (ImageView) findViewById(R.id.imageView_push_p5);
		pushP5.setSelected(true);
		
		pushZhuiStop = (ImageView) findViewById(R.id.imageView_push_zhuistop);
		pushZhuiStop.setSelected(true);
		
		pushLike = (ImageView) findViewById(R.id.imageView_push_like);
		pushLike.setSelected(true);
		
		pushWin.setOnClickListener(this);
		pushSFC.setOnClickListener(this);
		pushDLT.setOnClickListener(this);
		pushQXC.setOnClickListener(this);
		pushP3.setOnClickListener(this);
		pushP5.setOnClickListener(this);
		pushZhuiStop.setOnClickListener(this);
		pushLike.setOnClickListener(this);
	}
	
	CallBack mBack = new CallBack() {
		
		public void pushSetSuccess(final PushResult obj) {
			runOnUiThread(new Runnable() {
				public void run() {
					if (obj.getResCode().equals("0")) {
						switch (isSetting) {
						case "0":
//							ToastUtil.shortToast(CenterPushSetActivity.this, obj.getResMsg());
							finish();
							break;
						case "1":
							List<PushObj> list = obj.getList();
							if (list.size() > 0) {
								for (PushObj pushObj : list) {
									switch (pushObj.getKey()) {
									case GlobalConstants.AWARD_PUSH:
										setIsSelect(pushWin, pushObj.getIsPush());
										break;
									case GlobalConstants.TC_SF14:
										setIsSelect(pushSFC, pushObj.getIsPush());
										break;
									case GlobalConstants.TC_DLT:
										setIsSelect(pushDLT, pushObj.getIsPush());
										break;
									case GlobalConstants.TC_QXC:
										setIsSelect(pushQXC, pushObj.getIsPush());
										break;
									case GlobalConstants.TC_PL3:
										setIsSelect(pushP3, pushObj.getIsPush());
										break;
									case GlobalConstants.TC_PL5:
										setIsSelect(pushP5, pushObj.getIsPush());
										break;
									case GlobalConstants.CHASE_STOP:
										setIsSelect(pushZhuiStop, pushObj.getIsPush());
										break;
									case GlobalConstants.WATCH_MATCH:
										setIsSelect(pushLike, pushObj.getIsPush());
										break;
										
									default:
										break;
									}
								}
							}
							break;
						default:
							break;
						}
					}
				}
			});
		};
		
		public void pushSetError(String message) {
			CenterPushSetActivity.this.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					CenterPushSetActivity.this.finish();
					
				}
			});
		};
	};

	private void setIsSelect(ImageView view, String isPush){
		switch (isPush) {
		case "0":
			view.setSelected(false);
			break;
		case "1":
			view.setSelected(true);
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_back:
			setPust();
			finish();
			break;
		case R.id.imageView_push_win:
			if (pushWin.isSelected()) {
				pushWin.setSelected(false);
			} else {
				pushWin.setSelected(true);
			}
			break;
		case R.id.imageView_push_sfc:
			if (pushSFC.isSelected()) {
				pushSFC.setSelected(false);
			} else {
				pushSFC.setSelected(true);
			}
			break;
		case R.id.imageView_push_dlt:
			if (pushDLT.isSelected()) {
				pushDLT.setSelected(false);
			} else {
				pushDLT.setSelected(true);
			}
			break;
		case R.id.imageView_push_qxc:
			if (pushQXC.isSelected()) {
				pushQXC.setSelected(false);
			} else {
				pushQXC.setSelected(true);
			}
			break;
		case R.id.imageView_push_p3:
			if (pushP3.isSelected()) {
				pushP3.setSelected(false);
			} else {
				pushP3.setSelected(true);
			}
			break;
		case R.id.imageView_push_p5:
			if (pushP5.isSelected()) {
				pushP5.setSelected(false);
			} else {
				pushP5.setSelected(true);
			}
			break;
		case R.id.imageView_push_zhuistop:
			if (pushZhuiStop.isSelected()) {
				pushZhuiStop.setSelected(false);
			} else {
				pushZhuiStop.setSelected(true);
			}
			break;
		case R.id.imageView_push_like:
			if (pushLike.isSelected()) {
				pushLike.setSelected(false);
			} else {
				pushLike.setSelected(true);
			}
			break;

		default:
			break;
		}
	}
	
	private void setPust(){
		isSetting = "0";
		List<JSONObject> list = new ArrayList<JSONObject>();
		Map<String, String> winObj = new LinkedHashMap<String, String>();
		winObj.put("key", GlobalConstants.AWARD_PUSH);
		winObj.put("isPush", pushWin.isSelected()?"1":"0");
//		switch (pushWin.isSelected() + "") {
//		case "false":
//			winObj.put("isPush", "0");
//			break;
//		case "true":
//			winObj.put("isPush", "1");
//			break;
//		default:
//			break;
//		}
		
		
		Map<String, String> sfcObj = new LinkedHashMap<String, String>();
		sfcObj.put("key", GlobalConstants.TC_SF14);
		sfcObj.put("isPush", pushSFC.isSelected()?"1":"0");
//		switch (pushSFC.isSelected() + "") {
//		case "false":
//			sfcObj.put("isPush", "0");
//			break;
//		case "true":
//			sfcObj.put("isPush", "1");
//			break;
//		default:
//			break;
//		}
//		
		
		Map<String, String> dltObj = new LinkedHashMap<String, String>();
		dltObj.put("key", GlobalConstants.TC_DLT);
		dltObj.put("isPush",pushDLT.isSelected()?"1": "0");
//		switch (pushDLT.isSelected() + "") {
//		case "false":
//			dltObj.put("isPush", "0");
//			break;
//		case "true":
//			dltObj.put("isPush", "1");
//			break;
//		default:
//			break;
//		}
		
		Map<String, String> qcxObj = new LinkedHashMap<String, String>();
		qcxObj.put("key", GlobalConstants.TC_QXC);
		qcxObj.put("isPush",pushQXC.isSelected()?"1": "0");
//		switch (pushQXC.isSelected() + "") {
//		case "false":
//			qcxObj.put("isPush", "0");
//			break;
//		case "true":
//			qcxObj.put("isPush", "1");
//			break;
//		default:
//			break;
//		}
		
		Map<String, String> p3Obj = new LinkedHashMap<String, String>();
		p3Obj.put("key", GlobalConstants.TC_PL3);
		p3Obj.put("isPush",pushP3.isSelected()?"1": "0");
//		switch (pushP3.isSelected() + "") {
//		case "false":
//			p3Obj.put("isPush", "0");
//			break;
//		case "true":
//			p3Obj.put("isPush", "1");
//			break;
//		default:
//			break;
//		}
		
		Map<String, String> p5Obj = new LinkedHashMap<String, String>();
		p5Obj.put("key", GlobalConstants.TC_PL5);
		p5Obj.put("isPush",pushP5.isSelected()? "1": "0");
//		switch (pushP5.isSelected() + "") {
//		case "false":
//			p5Obj.put("isPush", "0");
//			break;
//		case "true":
//			p5Obj.put("isPush", "1");
//			break;
//		default:
//			break;
//		}
		
		Map<String, String> zhuiObj = new LinkedHashMap<String, String>();
		zhuiObj.put("key", GlobalConstants.CHASE_STOP);
		zhuiObj.put("isPush",pushZhuiStop.isSelected()?"1": "0");
//		switch (pushZhuiStop.isSelected() + "") {
//		case "false":
//			zhuiObj.put("isPush", "0");
//			break;
//		case "true":
//			zhuiObj.put("isPush", "1");
//			break;
//		default:
//			break;
//		}
		
		Map<String, String> likeObj = new LinkedHashMap<String, String>();
		likeObj.put("key", GlobalConstants.WATCH_MATCH);
		likeObj.put("isPush",pushLike.isSelected()?"1": "0");
//		switch (pushLike.isSelected() + "") {
//		case "false":
//			likeObj.put("isPush", "0");
//			break;
//		case "true":
//			likeObj.put("isPush", "1");
//			break;
//		default:
//			break;
//		}
		
		list.add(new JSONObject(winObj));
		list.add(new JSONObject(sfcObj));
		list.add(new JSONObject(dltObj));
		list.add(new JSONObject(qcxObj));
		list.add(new JSONObject(p3Obj));
		list.add(new JSONObject(p5Obj));
		list.add(new JSONObject(zhuiObj));
		list.add(new JSONObject(likeObj));
		Controller.getInstance().pushSet(GlobalConstants.NUM_PUSHSET, UserLogic.getInstance().getDefaultUserInfo().getUserId(), isSetting, list, null);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			setPust();
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
