package cn.com.cimgroup.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseLoadActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.RedPacketNoticeAdapter;
import cn.com.cimgroup.bean.RedPacketLeMiList;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.util.NoDataUtils;

/**
 * Created by small on 16/1/14. 乐米兑换记录;
 */
public class LeMiConvertNotesActivity extends BaseLoadActivity implements
		View.OnClickListener {

	private TextView leftButton;
	private TextView titleView;
	private PullToRefreshListView listView;
	private RedPacketNoticeAdapter adapter;
	protected static final int VOID_MESSAGE = 0;

	private Handler handler = new Handler() {

		public void handleMessage(Message msg) {

			switch (msg.what) {
			// 消息为空的时候给予提示
			case VOID_MESSAGE:
				// 展示活动
				// notice_or_message_void.setText("暂时没有活动");
				// notice_or_message_void.setVisibility(View.VISIBLE);
				RedPacketLeMiList obj1 = new RedPacketLeMiList();
				obj1.betAccount = "void_message_wushiqiu";
				List<RedPacketLeMiList> list = new ArrayList<RedPacketLeMiList>();
				list.add(obj1);
				adapter.replaceAll(list);
				break;

			default:
				break;
			}

		};

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lemi_convert_notice);

		findViewById();

	}

	private void findViewById() {
		leftButton = (TextView) findViewById(R.id.left_button);
		titleView = (TextView) findViewById(R.id.title_view);
		listView = (PullToRefreshListView) findViewById(R.id.list_view);
		adapter = new RedPacketNoticeAdapter(this, null);
		leftButton.setOnClickListener(this);
		initLoad(listView, adapter);
		titleView.setText("兑换记录");

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_button:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	@Override
	protected void loadData(int page) {
		Controller.getInstance().getConvertNotesList(
				GlobalConstants.NUM_CONVERT_LIST,
				getUserId(),
				page + "", "10", callBack);
	}
	/**
	 * 获取用户账户Id
	 * @return
	 */
	private String getUserId(){
		String userId="";
		if (App.userInfo!=null||!TextUtils.isEmpty(App.userInfo.getUserId())) {
			userId=App.userInfo.getUserId();
		}
		return userId;
	}

	private CallBack callBack = new CallBack() {
		@Override
		public void onSuccess(Object t) {
			if (t != null) {

				if (t instanceof RedPacketLeMiList) {
					if (("0").equals(((RedPacketLeMiList) t).total)) {
						// 返回的消息为0个
						// System.out.println("返回了0个消息");
						if (adapter.getCount() == 0) {
							Message message = Message.obtain();
							message.what = VOID_MESSAGE;
							handler.sendEmptyMessage(VOID_MESSAGE);
						}
					}
					final RedPacketLeMiList leMiList = (RedPacketLeMiList) t;

					LeMiConvertNotesActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							loadFinish();
							if (Integer.parseInt(leMiList.resCode) == 0) {
								if (isFirstPage()) {
									adapter.replaceAll(leMiList.productList);

								} else {
									adapter.addAll(leMiList.productList);
								}

								if (leMiList.productList == null
										|| leMiList.productList.size() == 0) {
									restorePage();
									NoDataUtils.setNoDataView(LeMiConvertNotesActivity.this, emptyImage, oneText, twoText, button, "0", 4);
								}
								if (adapter.getCount() < Integer
										.parseInt(leMiList.total)) {
									mListView
											.setMode(PullToRefreshBase.Mode.BOTH);
								}
							} else {
								NoDataUtils.setNoDataView(LeMiConvertNotesActivity.this, emptyImage, oneText, twoText, button, "0", 4);
							}
						}
					});
				}
			}
		}

		@Override
		public void onFail(final String error) {
			LeMiConvertNotesActivity.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					loadFinish();
					NoDataUtils.setNoDataView(LeMiConvertNotesActivity.this, emptyImage, oneText, twoText, button, error, 0);
				}
			});
		}
	};

	@Override
	public void loadData(int title, int page) {

	}

}
