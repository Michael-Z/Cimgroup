package cn.com.cimgroup.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.IssueList;
import cn.com.cimgroup.bean.LotteryDrawBasketballList;
import cn.com.cimgroup.bean.LotteryDrawBasketballMatch;
import cn.com.cimgroup.bean.LotteryDrawInfo;
import cn.com.cimgroup.bean.MatchAgainstSpInfo;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.popwindow.PopWindowFootballLotteryWay;
import cn.com.cimgroup.popwindow.PopWindowFootballLotteryWay.onItemClick;
import cn.com.cimgroup.popwindow.PopupWndSwitchCommon;
import cn.com.cimgroup.popwindow.PopupWndSwitchCommon.PlayMenuItemClick;

@SuppressLint({ "CutPasteId", "SimpleDateFormat" })
public class LotteryDrawBasketballListActivity extends BaseActivity implements OnClickListener,PlayMenuItemClick,onItemClick{

	private LotteryDrawInfo mInfo;
//	/** 期数+比赛数 **/
//	private TextView time;
	/** 比赛内容 **/
	private LinearLayout info;
	/** 比赛结果List**/
	private List<LotteryDrawBasketballMatch> mList;
	
	private TextView title_time;
	/** 查看其他其次的popupwindow**/
	private PopupWndSwitchCommon commonPop;
	
	private ImageView iv_basket_more;
	private TextView tv_basketball_back;
	private LinearLayout ll_basketball_title;
	private TextView  tv_basketball_select;
	private LinearLayout pop_common_bg;
	private TextView tv_jclq_betting;
	
	private String issueNo;
	
	private List<String[]> select_content;
	
	private PopWindowFootballLotteryWay mPopWindowJclq;
	
	private int mSelectIndex ;
	
	private static final int SF = 0;
	private static final int RFSF = 1;
	private static final int DXF = 2;
	private static final int SFC = 3;
	
//	private List<MatchAgainstSpInfo> mMatchInfo;
	private Map<String,String> matchInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_lotterydraw_basketball_list);
		super.onCreate(savedInstanceState);

		// 获取传递的数据
		Intent intent = getIntent();
		mInfo = (LotteryDrawInfo) intent
				.getSerializableExtra("LotteryDrawInfo");
		initView();
		showLoadingDialog();
		issueNo=mInfo.getIssueNo();
		//获取篮彩信息，因为和足球是同一个接口 所以未加以区分
		Controller.getInstance().syncLotteryDrawFootballList(
				GlobalConstants.NUM_LOTTERYDRAWFOOTBALLLIST, mInfo.getGameNo(), issueNo,
				mBack);

	};
	
	public CallBack mBack = new CallBack() {
		//获取篮彩列表成功
		public void syncLotteryDrawBasketballListSuccess(final LotteryDrawBasketballList info) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					//操作成功 返回数据
					if(Integer.parseInt(info.getResCode()) == 0){
						mList = info.getMatchList();
//						mMatchInfo = info.getMatchAgainstSpInfoList();
						addUI(mList);
						//获取指定类型的对阵查询时间表
						Controller.getInstance().getMatchTime(GlobalConstants.NUM_MATCHTIME,GlobalConstants.TC_JLSF, "5", mBack);
					}					
				}
			});
		};
		//获取篮彩列表失败
		public void syncLotteryDrawBasketballListError(String error) {
//			Controller.getInstance().getMatchTime(GlobalConstants.NUM_MATCHTIME,GlobalConstants.TC_JCLQ, "1", mBack);
		};
		
//		获取竞彩篮球比赛日期成功
		public void getMatchTimeSuccess(final IssueList list) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					hideLoadingDialog();
					List<String> result = new ArrayList<String>();
					if(list.getResCode().equals("0")){
						for(int i=0;i<list.getIssues().size();i++){
							result.add(list.getIssues().get(i).getIssue());
						}
					}
					if(commonPop == null){
						commonPop = new PopupWndSwitchCommon(LotteryDrawBasketballListActivity.this, result, LotteryDrawBasketballListActivity.this);
					}else {
						
					}
				}
			});
		};
//		获取竞彩篮球比赛日期失败
		public void getMatchTimeFailure(String error) {
		};
		
	};

	// 初始化视图
	private void initView() {
		
		mSelectIndex = SF;

//		time = (TextView) findViewById(R.id.tv_basketball_time);
		info = (LinearLayout) findViewById(R.id.ll_basketball_info);
		
		title_time = (TextView) findViewById(R.id.tv_basketball_time);
		
		tv_basketball_back = (TextView) findViewById(R.id.tv_basketball_back);
		iv_basket_more = (ImageView) findViewById(R.id.iv_basket_more);
		iv_basket_more.setVisibility(View.VISIBLE);
		ll_basketball_title = (LinearLayout) findViewById(R.id.ll_basketball_title);
		tv_basketball_select = (TextView) findViewById(R.id.tv_basketball_select);
		pop_common_bg = (LinearLayout) findViewById(R.id.pop_common_bg);
		tv_jclq_betting = (TextView) findViewById(R.id.tv_jclq_betting);
		
		mPopWindowJclq = new PopWindowFootballLotteryWay(this, pop_common_bg);
		mPopWindowJclq.setTitleText(getResources().getString(R.string.scorelist_title_match));
		select_content = new ArrayList<String[]>();
		initData();
		mPopWindowJclq.setData(select_content,mSelectIndex);
		mPopWindowJclq.addCallBack(this);
		tv_basketball_select.setText(mPopWindowJclq.getData().get(mSelectIndex)[1]);
		
		tv_basketball_back.setOnClickListener(this);
		iv_basket_more.setOnClickListener(this);
		tv_basketball_select.setOnClickListener(this);
		tv_jclq_betting.setOnClickListener(this);
	}
	
	/**
	 * 填充头部popupWindow中的内容
	 */
	private void initData() {
		select_content.add(new String[]{"0","胜负"});
		select_content.add(new String[]{"1","让分胜负"});
		select_content.add(new String[]{"2","大小分"});
		select_content.add(new String[]{"3","胜分差"});
	}

	//将数据以条目（item）的形式展示在布局中
	public void addUI(List<LotteryDrawBasketballMatch> list){
		info.removeAllViews();
		//填充title
		title_time.setText("第"+issueNo+"期  共"+(list==null?0:list.size())+"场比赛");
		if(list != null){			
			//向item中填充数据
			for(int i=0;i<list.size();i++){
				boolean flag = true;
				View view = View.inflate(this, R.layout.item_lotterydraw_basketball_list, null);
				ViewHolder holder = new ViewHolder(view);
				LotteryDrawBasketballMatch content = list.get(i);
//				String week = content.getWeek();
//				String weekStr = null;
//				if("1".endsWith(week)){
//					weekStr = "星期一";
//				}else if("2".endsWith(week)){
//					weekStr = "星期二";
//				}else if("3".endsWith(week)){
//					weekStr = "星期三";
//				}else if("4".endsWith(week)){
//					weekStr = "星期四";
//				}else if("5".endsWith(week)){
//					weekStr = "星期五";
//				}else if("6".endsWith(week)){
//					weekStr = "星期六";
//				}else if("7".endsWith(week)){
//					weekStr = "星期日";
//				}
				
				//填充对阵球队
				holder.host_name.setText(content.getHomeTeamName());
				holder.guest_name.setText(content.getGuestTeamName());
				//填充左端的icon（日期）
				holder.left_content_top.setText(content.getLeagueShort());
				holder.left_content_left.setText(content.getMatchSort());
				Long time = content.getMatchTimeDate();
				Date date = new Date(time);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				holder.left_content_right.setText((sdf.format(date)).substring((sdf.format(date).length()-8), (sdf.format(date).length()-3)));
				//填充右端的icon（比赛结果）
				String score = content.getFullScore();
				String realTimeSp;
				String indexInfo[] = new String[]{};
				if( !TextUtils.isEmpty(score) ){
					//截取比分 用于区分显示和计算胜负结果 devideScore[0]->主队比分    devideScore[1]->客队比分
					String[] devideScore = score.split("\\:");
					holder.right_content_left.setText(devideScore[1]);
					holder.right_content_right.setText(devideScore[0]);
					
					matchInfo = new HashMap<String,String>();
					for(MatchAgainstSpInfo info : content.getMatchAgainstSpInfoList() ){
						matchInfo.put(info.getPlayMethod(), info.getRealTimeSp());
					}
					
					switch (mSelectIndex) {
					case SF:
						//计算胜负结果
						if(Integer.parseInt(devideScore[0]) > Integer.parseInt(devideScore[1])){
							//主胜
							holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_red));
							holder.right_content_top.setText("主"+getResources().getString(R.string.victory));
						}else{
							holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.hall_blue));
							holder.right_content_top.setText("主"+getResources().getString(R.string.negative));
						}
						break;
					case  RFSF:
						if(matchInfo.get("TC_JLXSF") != null){
							realTimeSp = matchInfo.get("TC_JLXSF").toString();
							indexInfo = realTimeSp.split("\\|");
							String resultRFSF = indexInfo[0];
							//设置对阵信息（因为不同玩法，对阵信息不同，估需覆盖之前的内容）
							holder.host_name.setText(content.getHomeTeamName()+"("+resultRFSF+")");
							holder.guest_name.setText(content.getGuestTeamName());
							if(Integer.parseInt(devideScore[0]) + Double.parseDouble(resultRFSF) > Integer.parseInt(devideScore[1])){
								//主胜
								holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_red));
								holder.right_content_top.setText("主"+getResources().getString(R.string.victory));
							}else{
								holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.hall_blue));
								holder.right_content_top.setText("主"+getResources().getString(R.string.negative));
							}
						}else{
							flag = false;
						}
						break;
					case  DXF:
						if(matchInfo.get("TC_JLDXF") != null){
							realTimeSp = matchInfo.get("TC_JLDXF").toString();
							indexInfo = realTimeSp.split("\\|");
							String resultDXF = indexInfo[0];
							//设置对阵信息（因为不同玩法，对阵信息不同，估需覆盖之前的内容）
							holder.host_name.setText(content.getHomeTeamName()+"("+resultDXF+")");
							holder.guest_name.setText(content.getGuestTeamName());
							if(Integer.parseInt(devideScore[0]) + Integer.parseInt(devideScore[1]) > Double.parseDouble(resultDXF)){
								//大分
								holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_red));
								holder.right_content_top.setText("大分");
							}else{
								holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.hall_blue));
								holder.right_content_top.setText("小分");
							}
						}else{
							flag = false;
						}
						break;
					case  SFC:
						if(matchInfo.get("TC_JLSFC") != null){
//							realTimeSp = matchInfo.get("TC_JLSFC").toString();
							holder.host_name.setText(content.getHomeTeamName()+"("+Integer.parseInt(devideScore[1])+":"+Integer.parseInt(devideScore[0])+")");
							holder.guest_name.setText(content.getGuestTeamName());
							int resultSFC = Integer.parseInt(devideScore[0]) - Integer.parseInt(devideScore[1]);
							if(resultSFC > 0){
								if( resultSFC >= 1 && resultSFC <= 5){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_red));
									holder.right_content_top.setText("主"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("1");
									holder.right_content_right.setText("5");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 6 && resultSFC <= 10){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_red));
									holder.right_content_top.setText("主"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("6");
									holder.right_content_right.setText("10");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 11 && resultSFC <= 15){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_red));
									holder.right_content_top.setText("主"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("11");
									holder.right_content_right.setText("15");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 16 && resultSFC <= 20){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_red));
									holder.right_content_top.setText("主"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("16");
									holder.right_content_right.setText("20");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 21 && resultSFC <= 25){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_red));
									holder.right_content_top.setText("主"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("21");
									holder.right_content_right.setText("25");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 26){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_red));
									holder.right_content_top.setText("主"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("26");
									holder.right_content_center.setText("+");
								}
							}else{
								resultSFC = Math.abs(resultSFC);
								if( resultSFC >= 1 && resultSFC <= 5){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.hall_blue));
									holder.right_content_top.setText("客"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("1");
									holder.right_content_right.setText("5");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 6 && resultSFC <= 10){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.hall_blue));
									holder.right_content_top.setText("客"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("6");
									holder.right_content_right.setText("10");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 11 && resultSFC <= 15){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.hall_blue));
									holder.right_content_top.setText("客"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("11");
									holder.right_content_right.setText("15");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 16 && resultSFC <= 20){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.hall_blue));
									holder.right_content_top.setText("客"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("16");
									holder.right_content_right.setText("20");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 21 && resultSFC <= 25){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.hall_blue));
									holder.right_content_top.setText("客"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("21");
									holder.right_content_right.setText("25");
									holder.right_content_center.setText("-");
								}else if(resultSFC >= 26){
									holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.hall_blue));
									holder.right_content_top.setText("客"+getResources().getString(R.string.victory));
									holder.right_content_left.setText("26");
									holder.right_content_center.setText("+");
								}
							
						}
						}else{
//							holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.white));
//							holder.right_content_top.setVisibility(View.GONE);
//							holder.right_content_left.setVisibility(View.GONE);
//							holder.right_content_right.setVisibility(View.GONE);
//							holder.right_content_center.setVisibility(View.GONE);
//							holder.host_name.setText(content.getHomeTeamName()+"("+Integer.parseInt(devideScore[1])+":"+Integer.parseInt(devideScore[0])+")");
//							holder.guest_name.setText(content.getGuestTeamName());
//							holder.tv_no_sale.setVisibility(View.VISIBLE);
							flag = false;
							
						}
						break;
					default:
						break;
					}
				}else{
					//比赛未开始
					holder.right_content_left.setText("");
					holder.right_content_right.setText("");
					holder.right_content_top.setText("");
					holder.ll_icon_right.setBackgroundColor(getResources().getColor(R.color.color_white));
					holder.right_content_center.setVisibility(View.GONE);
				}
				if(flag){
					info.addView(view);
				}
			}
		}
	}
	
	public class ViewHolder{
		
		LinearLayout ll_icon_left;
		LinearLayout ll_icon_right;
		TextView left_content_top;
		TextView left_content_left;
		TextView left_content_center;
		TextView left_content_right;
		TextView right_content_top;
		TextView right_content_left;
		TextView right_content_center;
		TextView right_content_right;
		TextView host_name;
		TextView guest_name;
		TextView tv_no_sale;
		
		public ViewHolder(View view) {
			
			ll_icon_left = (LinearLayout) view.findViewById(R.id.ll_icon_left);
			ll_icon_right = (LinearLayout) view.findViewById(R.id.ll_icon_right);
			/** 左边icon的头**/
			left_content_top = (TextView) view.findViewById(R.id.tv_match_left_top);
			/** 左边icon的左下部**/
			left_content_left = (TextView) view.findViewById(R.id.tv_bottom_left_left);
			/** 左边icon的右下部**/
			left_content_right = (TextView) view.findViewById(R.id.tv_bottom_left_right);
			/** 右边icon的头部**/
			right_content_top = (TextView) view.findViewById(R.id.tv_match_right_top);
			/** 右边icon的左下部**/
			right_content_left = (TextView) view.findViewById(R.id.tv_bottom_right_left);
			/** 右边icon的底中部**/
			right_content_center = (TextView) view.findViewById(R.id.tv_bottom_right_center);
			/** 右边icon的右下部**/
			right_content_right = (TextView) view.findViewById(R.id.tv_bottom_right_right);
			/** 主队**/
			host_name = (TextView) view.findViewById(R.id.tv_host_team_name);
			/** 客队**/
			guest_name = (TextView) view.findViewById(R.id.tv_guest_team_name);
			
			tv_no_sale = (TextView) view.findViewById(R.id.tv_no_sale);
			
		}
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_basketball_back:
			finish();
			break;
		case R.id.tv_basketball_select:
			// 打开投注方式popWindow
			mPopWindowJclq.show2hideWindow(ll_basketball_title);
			pop_common_bg.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_basket_more:
			if (commonPop != null) {
				commonPop.showPopWindow(v);
			}
			break;
		case R.id.tv_jclq_betting:
			startActivity(LotteryBasketballActivity.class);
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void PopTzList(View v) {
		showLoadingDialog();
		issueNo = (String) v.getTag();
		Controller.getInstance().syncLotteryDrawFootballList(
				GlobalConstants.NUM_LOTTERYDRAWFOOTBALLLIST, mInfo.getGameNo(),
				issueNo, mBack);
		
	}

	/**
	 * popupwindow中item的点击事件
	 */
	@Override
	public void onSelectItemClick(String[] selectData, int position) {
		tv_basketball_select.setText(selectData[1]);
		//根据点击的item 加载对应的布局
		if(mSelectIndex != position){
			mSelectIndex = position;
		}
		addUI(mList);
	}
	

}
