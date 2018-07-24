package cn.com.cimgroup.activity;

import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.PlanContent;
import cn.com.cimgroup.bean.TzDetail;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.PlayInfo;
import cn.com.cimgroup.xutils.DateUtil;
import cn.com.cimgroup.xutils.StringUtil;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * 投注记录详情
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月6日
 */
public class Tz2DetailActivity extends BaseActivity implements OnClickListener {

	/** 投注跳转 */
	private TextView id_detail_betting_jump;
	private Intent intent;
	/**	方案编号*/
	private TextView orderIdView;
	/**投注时间*/
	private TextView tzTime;
	
	private TextView textView_tzdetail_num;

	private LinearLayout textView_tzdetail_items;

	private LinearLayout zhuiLayout;
	/**开奖号码*/
	private LinearLayout numLayout;
	/**订单状态*/
	private TextView id_detail_state;
	/**中奖号码*/
	private TextView numIssueView;
	/**投注方式*/
	private TextView id_play_method;
	/**订单编号*/
	private String orderId;
	/**玩法名称*/
	private String mGameName;
	/**玩法编号*/
	private String gameNo;
	
	/**标题第一行*/
	private String mTitle = "";
	/**投注金额*/
	private String mTzMoney = "";
	/**当前追期次数*/
	private String mNowTimes = "";
	/**总追期次数*/
	private String mAllTimes = "";
	/**档期追号状态*/
	private TextView id_tz_title;
	/**玩法类型*/
	private TextView id_game_name;
	private TextView id_detail_tz_money;
	/**投注状态--文字*/
	private TextView id_tz_state ;
	/**投注状态--图片*/
	private ImageView id_image_state;
	/**客服电话*/
	private TextView textView_title_service;
	
	/**红色*/
	private int mColorRed = 0;
	/**灰色*/
	private int mColorGrayLine = 0;
	/**浅灰色*/
	private int mColorGrayI = 0;
	/**灰色more竖线*/
	private  String mGrayVLine;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mcenter_record_tz_detail_second);
		
		mColorRed = getResources().getColor(R.color.color_red);
		mColorGrayLine = getResources().getColor(R.color.color_grey_bg);
		mColorGrayI = getResources().getColor(R.color.color_gray_indicator);
		mGrayVLine="<font color='"+mColorGrayLine+"'>| </font>";
		intent = getIntent();
		
		orderId = intent.getStringExtra("orderId");
		mGameName = intent.getStringExtra("gameName");
		gameNo = intent.getStringExtra("gameNo");
		
		mTitle = intent.getStringExtra("title");
		mTzMoney = intent.getStringExtra("tzMoney");
		mNowTimes = intent.getStringExtra("nowTimes");
		mAllTimes = intent.getStringExtra("allTimes");
		initViews();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Controller.getInstance().removeCallback(mBack);
	}

	private void initViews() {
//		scroll_tz_detail_second = (PullToRefreshScrollView) findViewById(R.id.scroll_tz_detail_second);
		
		
		id_tz_title = (TextView) findViewById(R.id.id_tz_title);
		id_game_name = (TextView) findViewById(R.id.id_game_name);
		
		id_detail_tz_money = (TextView) findViewById(R.id.id_detail_tz_money);
		id_tz_state = (TextView) findViewById(R.id.id_tz_state);
		id_image_state = (ImageView) findViewById(R.id.id_image_state);
		id_game_name.setText(mGameName);
		id_tz_title.setText(mTitle);
		id_detail_tz_money.setText(mTzMoney);
		
		id_detail_betting_jump = (TextView) findViewById(R.id.id_detail_betting_jump);
		id_detail_betting_jump.setOnClickListener(this);
		findViewById(R.id.textView_title_back).setOnClickListener(this);
		//标题
		TextView word = (TextView) findViewById(R.id.textView_title_word);
		word.setText(getResources().getString(R.string.tz_detail));
		//方案编号
		orderIdView = (TextView) findViewById(R.id.id_detail_orderid);

		id_detail_state = (TextView) findViewById(R.id.id_detail_state);

		numIssueView = (TextView) findViewById(R.id.id_detail_issue_num);

		tzTime = (TextView) findViewById(R.id.id_detail_tz_time);

		zhuiLayout = (LinearLayout) findViewById(R.id.layout_zhuidetail);
		zhuiLayout.setVisibility(View.GONE);

		numLayout = (LinearLayout) findViewById(R.id.id_detail_lotterynum_layout);

		id_play_method = (TextView) findViewById(R.id.id_play_method);

		showLoadingDialog();
		String tag = intent.getStringExtra("tag");
		if (!StringUtil.isEmpty(tag)) {
			orderIdView.setText(Html.fromHtml("方案编号：<font color = '"+mColorGrayI+"'>"+tag+"</font>"));
			Controller.getInstance().getLoBoTzDetail(tag,
					GlobalConstants.NUM_TZDGDETAIL, App.userInfo.getUserId(),
					mBack);
		}
		textView_tzdetail_num = (TextView) findViewById(R.id.textView_tzdetail_num);
		textView_tzdetail_items = (LinearLayout) findViewById(R.id.textView_tzdetail_items);
		textView_title_service = (TextView) findViewById(R.id.textView_title_service);
		textView_title_service.setVisibility(View.VISIBLE);
		textView_title_service.setOnClickListener(this);
	}

	/**
	 * 每一条代购的数据及处理
	 */
	public class ItemDetail {

		public TextView mItemDetailNum;
		public TextView mItemDetailBeiZhu;
		public TextView mItemDetailBei;

		public ItemDetail(View view) {
			mItemDetailNum = (TextView) view
					.findViewById(R.id.textView_tzdetail_item_num);
			mItemDetailBeiZhu = (TextView) view
					.findViewById(R.id.textView_tzdetail_item_beizhu);
			mItemDetailBei = (TextView) view
					.findViewById(R.id.textView_tzdetail_item_tzbei);
		}

	}


	private CallBack mBack = new CallBack() {
		// 代购
		public void getLoBoTzDetailSuccess(final TzDetail detail) {
			runOnUiThread(new Runnable() {
				public void run() {
					hideLoadingDialog();
					//TODO
					if (detail.getResCode().equals("0")) {
						numIssueView.setText(getResources()
								.getString(R.string.record_tz_detail_qi,
										detail.getIssue()));
						id_detail_state.setText(detail.getStatusDes());
						String winMoney = TextUtils.isEmpty(detail.getWinMoney())?"0":detail.getWinMoney();
						if (Integer.parseInt(winMoney) > 0) {
							//中奖
							id_tz_state.setText(String.format("恭喜您中了：%s米", detail.getWinMoney()));
							id_image_state.setImageResource(R.drawable.e_ui_detail_win);
						}else {
							String status = detail.getStatusDes();
							if (status.equals("未中奖")) {
								id_image_state.setImageResource(R.drawable.e_ui_detail_fail);
								id_tz_state.setText("木有中奖，继续努力哦~");
							}else if(status.equals("等待开奖")){
								id_image_state.setImageResource(R.drawable.e_ui_detail_wei);
								id_tz_state.setText("还未开奖哦~");
							}
						}
						id_detail_state.setText(Html.fromHtml("订单状态：<font color = '"+mColorGrayI+"'>"+detail.getStatusDes()+"</font>"));
						String time = DateUtil.getTimeInMillisToStr(detail
								.getCreateTime() + "");
						tzTime.setText(Html.fromHtml("认购时间：<font color = '"+mColorGrayI+"'>"+time+"</font>"));
						id_play_method.setText(Html.fromHtml("投注玩法：<font color = '"+mColorGrayI+"'>"+mGameName+"追号共"+mAllTimes+"期，第"+mNowTimes+"期,  "+getResources()
								.getString(R.string.tz_detail_tz_bei, detail.getCountNum(), detail.getMultiple())+"</font>"));

						if (!StringUtil.isEmpty(detail.getAwardCode())) {
							numLayout.setVisibility(View.VISIBLE);
							StringBuilder sb = new StringBuilder();
							switch (gameNo) {
							case GlobalConstants.TC_DLT:
								String[] awardArr = detail.getAwardCode()
										.split("\\ ");
								String[] redArr = awardArr[0].split("\\,");
								String[] blueArr = awardArr[1].split("\\,");
								for (String j : redArr) {
									sb.append(
											getResources().getString(
													R.string.tzlist_zhong_text,
													j)).append(" ");
								}
								
								sb.append(mGrayVLine);
								for (String j : blueArr) {
									sb.append(
											getResources()
													.getString(
															R.string.tzlist_zhong_text1,
															j)).append(" ");
								}
								sb.deleteCharAt(sb.length() - 1);
								textView_tzdetail_num.setText(Html.fromHtml(sb.toString()));
								break;
							default:
								String[] zdAwardArr = detail.getAwardCode()
										.split("\\,");
								for (String j : zdAwardArr) {
									sb.append(
											getResources().getString(
													R.string.tzlist_zhong_text,
													j)).append(" ");
								}
								sb.deleteCharAt(sb.length() - 1);
								textView_tzdetail_num.setText(Html.fromHtml(sb.toString()));
								break;
							}
						} else {
							numLayout.setVisibility(View.GONE);
							textView_tzdetail_num.setText("");
						}
						List<PlanContent> lists = detail.getList();
						if (lists != null) {
							for (int i = 0; i < lists.size(); i++) {
								View view = View.inflate(mActivity,
										R.layout.item_tz_detail, null);
								PlanContent plan = lists.get(i);
								ItemDetail item = new ItemDetail(view);
								StringBuilder sb = new StringBuilder();

								if (!StringUtil.isEmpty(detail.getAwardCode())) {
									switch (gameNo) {
									case GlobalConstants.TC_DLT:
										String[] awardArr = detail
												.getAwardCode().split("\\ ");
										String[] contentArr = plan
												.getCodeContent().split("\\|");
										String[] redArr = awardArr[0]
												.split("\\,");
										String[] blueArr = awardArr[1]
												.split("\\,");
										switch (plan.getCodePlay() + "") {
										case PlayInfo.DLT_DT:
											String[] contentRedDanArr = contentArr[0]
													.split("\\,");
											String[] contentRed1Arr = contentArr[1]
													.split("\\,");
											String[] contentBlueDanArr = contentArr[2]
													.split("\\,");
											String[] contentBlue1Arr = contentArr[3]
													.split("\\,");
											if (contentRedDanArr != null
													&& contentRedDanArr.length != 0
													&& !TextUtils
															.isEmpty(contentRedDanArr[0]
																	.toString()
																	.trim())) {
												sb.append("[");
												sb.append(getResources()
														.getString(
																R.string.tzlist_zhong_text,
																"胆"));
												for (String j : contentRedDanArr) {
													boolean aa = true;
													for (String k : redArr) {
														if (j.equals(k)) {
															aa = false;
															sb.append(
																	getResources()
																			.getString(
																					R.string.tzlist_zhong_text,
																					j))
																	.append(" ");
														}
													}
													if (aa) {
														sb.append(j)
																.append(" ");
													}
												}
												sb.deleteCharAt(sb.length() - 1);
												sb.append("]");
											}

											for (String j : contentRed1Arr) {
												boolean aa = true;
												for (String k : redArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}

											sb.append(mGrayVLine);

											if (contentBlueDanArr != null
													&& contentBlueDanArr.length != 0
													&& !TextUtils
															.isEmpty(contentBlueDanArr[0]
																	.toString()
																	.trim())) {
												sb.append("[");
												sb.append(getResources()
														.getString(
																R.string.tzlist_zhong_text1,
																"胆"));

												for (String j : contentBlueDanArr) {
													boolean aa = true;
													for (String k : blueArr) {
														if (j.equals(k)) {
															aa = false;
															sb.append(
																	getResources()
																			.getString(
																					R.string.tzlist_zhong_text,
																					j))
																	.append(" ");
														}
													}
													if (aa) {
														sb.append(j)
																.append(" ");
													}
												}
												sb.deleteCharAt(sb.length() - 1);
												sb.append("]");
											}

											for (String j : contentBlue1Arr) {
												boolean aa = true;
												for (String k : blueArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											break;
										default:
											
											String[] contentRedArr = contentArr[0]
													.split("\\,");
											String[] contentBlueArr = contentArr[1]
													.split("\\,");

											for (String j : contentRedArr) {
												boolean aa = true;
												for (String k : redArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}

											sb.append(mGrayVLine);

											for (String j : contentBlueArr) {
												boolean aa = true;
												for (String k : blueArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											break;
										}

										item.mItemDetailNum.setText(Html
												.fromHtml(sb.toString()));
										break;
									case GlobalConstants.TC_PL3:
										switch (plan.getCodePlay() + "") {
										case PlayInfo.PL3_ZXDS:
										case PlayInfo.PL3_ZXFS:
											String[] zdAwardArr = detail
													.getAwardCode()
													.split("\\,");
											String[] zdContentArr = plan
													.getCodeContent().split(
															"\\|");

											for (int j = 0; j < zdContentArr.length; j++) {
												String[] items = zdContentArr[j]
														.split("\\,");
												for (String k : items) {
													String[] temps = k
															.split("\\ ");
													for (String l : temps) {
														if (l.equals(zdAwardArr[j])) {
															sb.append(
																	getResources()
																			.getString(
																					R.string.tzlist_zhong_text,
																					l))
																	.append(" ");
														} else {
															sb.append(l)
																	.append(" ");
														}
													}
//													sb.deleteCharAt(sb.length() - 1);
												}
												sb.append(mGrayVLine);
											}
//											sb.deleteCharAt(sb.length() - 2);
											break;
										case PlayInfo.PL3_ZUX3DS:
										case PlayInfo.PL3_ZUX3FS:
											String[] zxAwardArr = detail
													.getAwardCode()
													.split("\\,");
											String[] zxContentArr = plan
													.getCodeContent().split(
															"\\,");

											Arrays.sort(zxAwardArr);
											for (int k = 0; k < zxContentArr.length; k++) {
												if (zxContentArr[k]
														.equals(zxAwardArr[k])) {
													sb.append(
															getResources()
																	.getString(
																			R.string.tzlist_zhong_text,
																			zxContentArr[k]))
															.append(" ");
												} else {
													sb.append(zxContentArr[k])
															.append(" ");
												}
											}
											break;
										case PlayInfo.PL3_ZUX6DS:
										case PlayInfo.PL3_ZUX6FS:
											String[] plAwardArr = detail
													.getAwardCode()
													.split("\\,");
											String[] plContentArr = plan
													.getCodeContent().split(
															"\\|");

											Arrays.sort(plAwardArr);
											for (String j : plContentArr) {
												boolean aa = true;
												for (String k : plAwardArr) {
													if (j.equals(k)) {
														aa = false;
														sb.append(
																getResources()
																		.getString(
																				R.string.tzlist_zhong_text,
																				j))
																.append(" ");
													}
												}
												if (aa) {
													sb.append(j).append(" ");
												}
											}
											break;
										case PlayInfo.PL3_ZXHZ:
										case PlayInfo.PL3_ZUXHZ:
											String[] zhAwardArr = detail
													.getAwardCode()
													.split("\\,");
											String zhContentArr = plan
													.getCodeContent();
											int awardAll = 0;
											for (String j : zhAwardArr) {
												awardAll += Integer.parseInt(j);
											}

											if (Integer.parseInt(zhContentArr) == awardAll) {
												sb.append(getResources()
														.getString(
																R.string.tzlist_zhong_text,
																plan.getCodeContent()));
											} else {
												sb.append(plan.getCodeContent());
											}
											break;
										default:
											break;
										}
										String pl3Str = sb.toString();
										if (pl3Str.lastIndexOf("|") != -1) {
											item.mItemDetailNum.setText(Html
													.fromHtml(pl3Str.substring(0, pl3Str.lastIndexOf("|"))));
										} else {
											item.mItemDetailNum.setText(Html
													.fromHtml(pl3Str));
										}

										break;
									case GlobalConstants.TC_QXC:
									case GlobalConstants.TC_PL5:
										String[] qxcAwardArr = detail
												.getAwardCode().split("\\,");
										String[] qxcContentArr = plan
												.getCodeContent().split("\\|");

										for (int j = 0; j < qxcContentArr.length; j++) {
											String[] arr = qxcContentArr[j]
													.split("\\,");
											for (String k : arr) {
												sb.append(" ");
												if (qxcAwardArr[j].equals(k)) {
													sb.append(
															getResources()
																	.getString(
																			R.string.tzlist_zhong_text,
																			k))
															.append(" ");
												} else {
													sb.append(k).append(" ");
												}
											}
											sb.append(mGrayVLine);
										}
										String pl5Str = sb.toString();
										item.mItemDetailNum.setText(Html
												.fromHtml(pl5Str.substring(0, pl5Str.lastIndexOf("|"))));
										break;
									default:
										String pl51Str = plan
												.getCodeContent().replace(",",
														" ").replace("|",mGrayVLine);
										item.mItemDetailNum.setText(Html.fromHtml(pl51Str));
										break;
									}
								} else {
									switch (gameNo) {
									case GlobalConstants.TC_DLT:
										String[] contentArr = plan
												.getCodeContent().split("\\|");
										switch (plan.getCodePlay() + "") {
										case PlayInfo.DLT_DT:
											String[] contentRedDanArr = contentArr[0]
													.split("\\,");
											String[] contentRed1Arr = contentArr[1]
													.split("\\,");
											String[] contentBlueDanArr = contentArr[2]
													.split("\\,");
											String[] contentBlue1Arr = contentArr[3]
													.split("\\,");

											if (contentRedDanArr != null
													&& contentRedDanArr.length != 0
													&& !TextUtils
															.isEmpty(contentRedDanArr[0]
																	.toString()
																	.trim())) {
												sb.append("[");
												sb.append(getResources()
														.getString(
																R.string.tzlist_zhong_text,
																"胆"));
												for (String j : contentRedDanArr) {
													sb.append(j).append(" ");
												}
												sb.deleteCharAt(sb.length() - 1);
												sb.append("]");
											}

											for (String j : contentRed1Arr) {
												sb.append(j).append(" ");
											}

											sb.append(mGrayVLine);
											if (contentBlueDanArr != null
													&& contentBlueDanArr.length != 0
													&& !TextUtils
															.isEmpty(contentBlueDanArr[0]
																	.toString()
																	.trim())) {
												sb.append("[");
												sb.append(getResources()
														.getString(
																R.string.tzlist_zhong_text1,
																"胆"));

												for (String j : contentBlueDanArr) {
													sb.append(j).append(" ");
												}
												sb.deleteCharAt(sb.length() - 1);
												sb.append("]");
											}

											for (String j : contentBlue1Arr) {
												sb.append(j).append(" ");
											}
											break;
										default:
											String[] contentRedArr = contentArr[0]
													.split("\\,");
											String[] contentBlueArr = contentArr[1]
													.split("\\,");

											for (String j : contentRedArr) {
												sb.append(j).append(" ");
											}
											sb.append(mGrayVLine);
											for (String j : contentBlueArr) {
												sb.append(j).append(" ");
											}
											break;
										}

										item.mItemDetailNum.setText(Html
												.fromHtml(sb.toString()));
										break;
									case GlobalConstants.TC_PL3:
										switch (plan.getCodeContent()) {
										case "27":
											item.mItemDetailNum
													.setText("9 | 9 | 9");
											break;
										case "26":
											item.mItemDetailNum
													.setText("8 | 9 | 9");
											break;
										case "01":
											item.mItemDetailNum
													.setText("0 | 0 | 1");
											break;
										case "00":
											item.mItemDetailNum
													.setText("0 | 0 | 0");
											break;
										default:
											String defStr = plan
													.getCodeContent()
													.replace(",", " ")
													.replace("|", mGrayVLine);
											item.mItemDetailNum.setText(Html.fromHtml(defStr));
											break;
										}
										break;
									default:
										String defStr = plan
												.getCodeContent().replace(",",
														" ").replace("|",mGrayVLine);
										item.mItemDetailNum.setText(Html.fromHtml(defStr));
										
										break;
									}
								}

								item.mItemDetailBeiZhu.setText(plan
										.getCodePlayDes()
										+ plan.getCodeNumbers()
										+ getResources().getString(
												R.string.betting1));
								item.mItemDetailBei.setText(plan
										.getCodeMultiple()
										+ getResources().getString(
												R.string.cart_add_bei));
								textView_tzdetail_items.addView(view);
							}
						}
					}
				}
			});
		};

		public void getLoBoTzDetailFailure(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					hideLoadingDialog();
					showToast(getResources().getString(
							R.string.record_tz_detail_failure));
				}
			});
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_title_service:
			//拨打客服电话
			callService();
			break;
		case R.id.textView_title_back:
			finish();
			break;
		case R.id.id_detail_betting_jump:
			// 跳转到各自的投注页面
			if (mGameName.equals("竞彩足球")) {
				startActivity(LotteryFootballActivity.class);
			} else if (mGameName.equals("大乐透")) {
				startActivity(LotteryDLTActivity.class);
			} else if (mGameName.equals("排列三")) {
				startActivity(LotteryPL3Activity.class);
			} else if (mGameName.equals("七星彩")) {
				startActivity(LotteryQxcActivity.class);
			} else if (mGameName.equals("胜负彩") || mGameName.equals("任选9场")) {
				Intent intent = new Intent(Tz2DetailActivity.this,
						LotteryOldFootballActivity.class);
				intent.putExtra("lotoId", 0);
				startActivity(intent);
			} else if (mGameName.equals("排列五")) {
				startActivity(LotteryPL5Activity.class);
			} else if (mGameName.equals("6场半全场")) {
				Intent intent = new Intent(Tz2DetailActivity.this,
						LotteryOldFootballActivity.class);
				intent.putExtra("lotoId", 2);
				startActivity(intent);
			} else if (mGameName.equals("4场进球")) {
				Intent intent = new Intent(Tz2DetailActivity.this,
						LotteryOldFootballActivity.class);
				intent.putExtra("lotoId", 1);
				startActivity(intent);
			}
			finish();
		default:
			break;
		}
	}

	private void callService() {
		 Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:010-65617701"));  
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		    startActivity(intent);  
	}

	@Override
	public void onResume() {
		super.onResume();
		id_detail_betting_jump.setText(mGameName + "投注");
	}

//	@Override
//	protected void loadData(int page) {
//		
//	}
//
//	@Override
//	public void loadData(int title, int page) {
//		
//	}
}
