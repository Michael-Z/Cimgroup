package cn.com.cimgroup.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.GuestFutureObj;
import cn.com.cimgroup.bean.GuestNearObj;
import cn.com.cimgroup.bean.GuestRankObj;
import cn.com.cimgroup.bean.HostFutureObj;
import cn.com.cimgroup.bean.HostNearObj;
import cn.com.cimgroup.bean.HostRankObj;
import cn.com.cimgroup.bean.ScoreDetailAnalysis;
import cn.com.cimgroup.bean.ScoreMatchBean;
import cn.com.cimgroup.bean.VsObj;

public class ScoreAnalysisAdapter extends SimpleBaseAdapter<ScoreDetailAnalysis> {
	
	private ScoreMatchBean mBean;
	
	public ScoreAnalysisAdapter(Context context, ScoreMatchBean bean) {
		super(context, null);
		mBean = bean;
	}

	@Override
	public int getItemResource() {
		return R.layout.frament_score_analysis;
	}
	
	@Override
	public View getItemView(int position, View convertView, ViewHolder holder) {
		
		TextView zhuFuture = holder.getView(R.id.textView_score_analysis_future_zhu);
		TextView keFuture = holder.getView(R.id.textView_score_analysis_future_ke);
		
//		String[] arr = mRecode.split("\\+");
		zhuFuture.setText(mBean.getHostName());
		keFuture.setText(mBean.getGuestName());
		
		LinearLayout vsOut = holder.getView(R.id.layout_score_analysis_out_vs);
		LinearLayout nearOut = holder.getView(R.id.layout_score_analysis_out_near);
		LinearLayout futureOut = holder.getView(R.id.layout_score_analysis_out_future);
		LinearLayout jifenOut = holder.getView(R.id.layout_score_analysis_out_jifen);
		
		
		LinearLayout vs =  holder.getView(R.id.layout_score_analysis_vs);
		LinearLayout hostnear =  holder.getView(R.id.layout_score_analysis_hostnear);
		LinearLayout guestnear =  holder.getView(R.id.layout_score_analysis_guestnear);
		LinearLayout hostfuturevs =  holder.getView(R.id.layout_score_analysis_hostfuturevs);
		LinearLayout guestfuturevs =  holder.getView(R.id.layout_score_analysis_guestfuturevs);
		LinearLayout hostrank =  holder.getView(R.id.layout_score_analysis_hostrank);
		LinearLayout guestrank =  holder.getView(R.id.layout_score_analysis_guestrank);
		vs.removeAllViews();
		hostnear.removeAllViews();
		guestnear.removeAllViews();
		hostfuturevs.removeAllViews();
		guestfuturevs.removeAllViews();
		hostrank.removeAllViews();
		guestrank.removeAllViews();
		
		TextView vsText =  holder.getView(R.id.textView_score_analysis_vs_text);
		TextView zhuText =  holder.getView(R.id.textView_score_analysis_zhu_text);
		TextView keText =  holder.getView(R.id.textView_score_analysis_ke_text);
		
		ScoreDetailAnalysis analysis = mList.get(position);
		
		List<VsObj> vsList = analysis.getVsList();
		List<HostNearObj> hostNearList = analysis.getHostNearList();
		List<GuestNearObj> guestNearList = analysis.getGuestNearList();
		List<HostFutureObj> hostFutureVsList = analysis.getHostFutureVsList();
		List<GuestFutureObj> guestFutureVsList = analysis.getGuestFutureVsList();
		List<HostRankObj> hostRankList = analysis.getHostRankList();
		List<GuestRankObj> guestRankList = analysis.getGuestRankList();
		
		if (vsList.size() == 0) {
			vsOut.setVisibility(View.GONE);
		} else {
			vsOut.setVisibility(View.VISIBLE);
		}
		
		if (hostNearList.size() == 0) {
			nearOut.setVisibility(View.GONE);
		} else {
			nearOut.setVisibility(View.VISIBLE);
		}
		
		if (hostFutureVsList.size() == 0) {
			futureOut.setVisibility(View.GONE);
		} else {
			futureOut.setVisibility(View.VISIBLE);
		}
		
		if (hostRankList.size() == 0) {
			jifenOut.setVisibility(View.GONE);
		} else {
			jifenOut.setVisibility(View.VISIBLE);
		}
		
		String vsResult = analysis.getBothSideResult();
		String guestNResult = analysis.getGuestNearResult();
		String hostNResult = analysis.getHostNearResult();
		
		if (vsResult.indexOf("_") != -1) {
			String[] vsNArr = vsResult.split("\\_");
			vsText.setText(Html.fromHtml(context.getResources().getString(R.string.scoredetail_analysis_string, vsNArr[0], vsNArr[1], vsNArr[2], vsNArr[3], vsNArr[4], vsNArr[5])));
		}
		if (guestNResult.indexOf("_") != -1) {
			String[] guestNArr = guestNResult.split("\\_");
			keText.setText(Html.fromHtml(context.getResources().getString(R.string.scoredetail_analysis_string, guestNArr[0], guestNArr[1], guestNArr[2], guestNArr[3], guestNArr[4], guestNArr[5])));
		}
		if (hostNResult.indexOf("_") != -1) {
			String[] hostNArr = hostNResult.split("\\_");
			zhuText.setText(Html.fromHtml(context.getResources().getString(R.string.scoredetail_analysis_string, hostNArr[0], hostNArr[1], hostNArr[2], hostNArr[3], hostNArr[4], hostNArr[5])));
		}
		
		
		for (int i = 0; i < vsList.size(); i++) {
			VsObj vsObj = vsList.get(i);
			LinearLayout view = (LinearLayout) View.inflate(context, R.layout.item_score_analysis1_item1, null);
			AnalysisItem1 item = new AnalysisItem1(view);
			
			item.matchName.setText(vsObj.getMatchName());
			String duizhen = vsObj.getDuizhen();
			String[] duiArr = duizhen.split("\\_");
			item.zhu.setText(duiArr[0]);
			item.bifen.setText(duiArr[1]);
			item.ke.setText(duiArr[2]);
			switch (vsObj.getMatchResult()) {
			case "3":
				item.result.setText("胜");
				item.result.setTextColor(context.getResources().getColor(R.color.color_red));
				break;
			case "1":
				item.result.setText("平");
				item.result.setTextColor(context.getResources().getColor(R.color.color_green_warm));
				break;
			case "0":
				item.result.setText("负");
				item.result.setTextColor(context.getResources().getColor(R.color.hall_blue));
				break;
			default:
				break;
			}
			
			item.time.setText(vsObj.getMatchTime());
			
			vs.addView(view);
		}
		
		for (int i = 0; i < hostNearList.size(); i++) {
			HostNearObj hostNearObj = hostNearList.get(i);
			LinearLayout view1 = (LinearLayout) View.inflate(context, R.layout.item_score_analysis1_item1, null);
			AnalysisItem1 item1 = new AnalysisItem1(view1);
			
			item1.matchName.setText(hostNearObj.getHostNearMatchName());
			String duizhen = hostNearObj.getHostNearDuiZhen();
			String[] duiArr = duizhen.split("\\_");
			item1.zhu.setText(duiArr[0]);
			item1.bifen.setText(duiArr[1]);
			item1.ke.setText(duiArr[2]);
			
			switch (hostNearObj.getHostNearMatchResult()) {
			case "3":
				item1.result.setText("胜");
				item1.result.setTextColor(context.getResources().getColor(R.color.color_red));
				break;
			case "1":
				item1.result.setText("平");
				item1.result.setTextColor(context.getResources().getColor(R.color.color_green_warm));
				break;
			case "0":
				item1.result.setText("负");
				item1.result.setTextColor(context.getResources().getColor(R.color.hall_blue));
				break;
			default:
				break;
			}
			item1.time.setText(hostNearObj.getHostNearMatchTime());
			
			hostnear.addView(view1);
		}
		
		for (int i = 0; i < guestNearList.size(); i++) {
			GuestNearObj guestNearObj = guestNearList.get(i);
			LinearLayout view2 = (LinearLayout) View.inflate(context, R.layout.item_score_analysis1_item1, null);
			AnalysisItem1 item2 = new AnalysisItem1(view2);
			
			item2.matchName.setText(guestNearObj.getGuestNearMatchName());
			String duizhen = guestNearObj.getGuestNearDuiZhen();
			String[] duiArr = duizhen.split("\\_");
			item2.zhu.setText(duiArr[0]);
			item2.bifen.setText(duiArr[1]);
			item2.ke.setText(duiArr[2]);
			switch (guestNearObj.getGuestNearMatchResult()) {
			case "3":
				item2.result.setText("胜");
				item2.result.setTextColor(context.getResources().getColor(R.color.color_red));
				break;
			case "1":
				item2.result.setText("平");
				item2.result.setTextColor(context.getResources().getColor(R.color.color_green_warm));
				break;
			case "0":
				item2.result.setText("负");
				item2.result.setTextColor(context.getResources().getColor(R.color.hall_blue));
				break;
			default:
				break;
			}
			item2.time.setText(guestNearObj.getGuestNearMatchTime());
			
			guestnear.addView(view2);
		}
		
		for (int i = 0; i < hostFutureVsList.size(); i++) {
			HostFutureObj hostFutureObj = hostFutureVsList.get(i);
			LinearLayout view3 = (LinearLayout) View.inflate(context, R.layout.item_score_analysis1_item2, null);
			AnalysisItem2 item3 = new AnalysisItem2(view3);
			
			item3.matchName.setText(hostFutureObj.getHostFutureName());
			String duizhen = hostFutureObj.getHostFutureDZ();
			String[] duiArr = duizhen.split("\\_");
			item3.zhu.setText(duiArr[0]);
			item3.ke.setText(duiArr[1]);
			item3.result.setText(hostFutureObj.getHostFutureJG());
			item3.time.setText(hostFutureObj.getHostFutureTime());
			
			hostfuturevs.addView(view3);
		}
		
		for (int i = 0; i < guestFutureVsList.size(); i++) {
			GuestFutureObj guestFutureObj = guestFutureVsList.get(i);
			LinearLayout view4 = (LinearLayout) View.inflate(context, R.layout.item_score_analysis1_item2, null);
			AnalysisItem2 item4 = new AnalysisItem2(view4);
			
			item4.matchName.setText(guestFutureObj.getGuestFutureName());
			String duizhen = guestFutureObj.getGuestFutureDZ();
			String[] duiArr = duizhen.split("\\_");
			item4.zhu.setText(duiArr[0]);
			item4.ke.setText(duiArr[1]);
			item4.result.setText(guestFutureObj.getGuestFutureJG());
			item4.time.setText(guestFutureObj.getGuestFutureTime());
			
			guestfuturevs.addView(view4);
		}
		
		for (int i = 0; i < hostRankList.size(); i++) {
			HostRankObj hostRankObj = hostRankList.get(i);
			LinearLayout view5 = (LinearLayout) View.inflate(context, R.layout.item_score_analysis2_item1, null);
			AnalysisItem3 item5 = new AnalysisItem3(view5);
			
			hostRankObj.getHostRankResult();
			String guestAll = hostRankObj.getHostAllResult();
			String guestG = hostRankObj.getHostGResult();
			String guestH = hostRankObj.getHostHResult();
			
			
			String[] allArr = guestAll.split("\\_");
			item5.name.setText("总成绩");
			item5.match.setText(allArr[0]);
			item5.sheng.setText(allArr[1]);
			item5.ping.setText(allArr[2]);
			item5.fu.setText(allArr[3]);
			item5.jin.setText(allArr[4]);
			item5.shi.setText(allArr[5]);
			item5.jifen.setText(allArr[6]);
			item5.pai.setText(allArr[7]);
			
			hostrank.addView(view5);
			
			String[] hArr = guestAll.split("\\_");
			item5.name.setText("主 场");
			item5.match.setText(hArr[0]);
			item5.sheng.setText(hArr[1]);
			item5.ping.setText(hArr[2]);
			item5.fu.setText(hArr[3]);
			item5.jin.setText(hArr[4]);
			item5.shi.setText(hArr[5]);
			item5.jifen.setText(hArr[6]);
			item5.pai.setText(hArr[7]);
			
			hostrank.addView(view5);
			
			String[] gArr = guestAll.split("\\_");
			item5.name.setText("客 场");
			item5.match.setText(gArr[0]);
			item5.sheng.setText(gArr[1]);
			item5.ping.setText(gArr[2]);
			item5.fu.setText(gArr[3]);
			item5.jin.setText(gArr[4]);
			item5.shi.setText(gArr[5]);
			item5.jifen.setText(gArr[6]);
			item5.pai.setText(gArr[7]);
			
			hostrank.addView(view5);
			
		}
		
		for (int i = 0; i < guestRankList.size(); i++) {
			GuestRankObj guestRankObj = guestRankList.get(i);
			LinearLayout view6 = (LinearLayout) View.inflate(context, R.layout.item_score_analysis2_item1, null);
			AnalysisItem3 item6 = new AnalysisItem3(view6);
			
			guestRankObj.getGuestRankResult();
			String guestAll = guestRankObj.getGuestAllResult();
			String guestG = guestRankObj.getGuestGResult();
			String guestH = guestRankObj.getGuestHResult();
			
			String[] allArr = guestAll.split("\\_");
			item6.name.setText("总成绩");
			item6.match.setText(allArr[0]);
			item6.sheng.setText(allArr[1]);
			item6.ping.setText(allArr[2]);
			item6.fu.setText(allArr[3]);
			item6.jin.setText(allArr[4]);
			item6.shi.setText(allArr[5]);
			item6.jifen.setText(allArr[6]);
			item6.pai.setText(allArr[7]);
			
			guestrank.addView(view6);
			
			String[] hArr = guestAll.split("\\_");
			item6.name.setText("主 场");
			item6.match.setText(hArr[0]);
			item6.sheng.setText(hArr[1]);
			item6.ping.setText(hArr[2]);
			item6.fu.setText(hArr[3]);
			item6.jin.setText(hArr[4]);
			item6.shi.setText(hArr[5]);
			item6.jifen.setText(hArr[6]);
			item6.pai.setText(hArr[7]);
			
			guestrank.addView(view6);
			
			String[] gArr = guestAll.split("\\_");
			item6.name.setText("客 场");
			item6.match.setText(gArr[0]);
			item6.sheng.setText(gArr[1]);
			item6.ping.setText(gArr[2]);
			item6.fu.setText(gArr[3]);
			item6.jin.setText(gArr[4]);
			item6.shi.setText(gArr[5]);
			item6.jifen.setText(gArr[6]);
			item6.pai.setText(gArr[7]);
			
			guestrank.addView(view6);
			
		}
		
		return convertView;
	}
	
	public class AnalysisItem1 {
		
		private TextView matchName;
		
		private TextView time;
		
		private TextView zhu;
		
		private TextView ke;
		
		private TextView bifen;
		
		private TextView result;
		
		public AnalysisItem1(View view) {
			matchName = (TextView) view.findViewById(R.id.textView_score_analysis1_item1_matchname);
			time = (TextView) view.findViewById(R.id.textView_score_analysis1_item1_time);
			zhu = (TextView) view.findViewById(R.id.textView_score_analysis1_item1_zhu);
			ke = (TextView) view.findViewById(R.id.textView_score_analysis1_item1_ke);
			bifen = (TextView) view.findViewById(R.id.textView_score_analysis1_item1_bifen);
			result = (TextView) view.findViewById(R.id.textView_score_analysis1_item1_result);
		}
	}
	
	public class AnalysisItem2 {
		
		private TextView matchName;
		
		private TextView time;
		
		private TextView zhu;
		
		private TextView ke;
		
		private TextView result;
		
		public AnalysisItem2(View view) {
			matchName = (TextView) view.findViewById(R.id.textView_score_analysis1_item2_matchname);
			time = (TextView) view.findViewById(R.id.textView_score_analysis1_item2_time);
			zhu = (TextView) view.findViewById(R.id.textView_score_analysis1_item2_zhu);
			ke = (TextView) view.findViewById(R.id.textView_score_analysis1_item2_ke);
			result = (TextView) view.findViewById(R.id.textView_score_analysis1_item2_result);
		}
	}
	
	public class AnalysisItem3 {
		
		private TextView name;
		
		private TextView match;
		
		private TextView sheng;
		
		private TextView ping;
		
		private TextView fu;
		
		private TextView jin;
		
		private TextView shi;
		
		private TextView jifen;
		
		private TextView pai;
		
		public AnalysisItem3(View view) {
			name = (TextView) view.findViewById(R.id.textView_score_analysis2_item1_name);
			match = (TextView) view.findViewById(R.id.textView_score_analysis2_item1_match);
			sheng = (TextView) view.findViewById(R.id.textView_score_analysis2_item1_s);
			ping = (TextView) view.findViewById(R.id.textView_score_analysis2_item1_p);
			fu = (TextView) view.findViewById(R.id.textView_score_analysis2_item1_f);
			jin = (TextView) view.findViewById(R.id.textView_score_analysis2_item1_jin);
			shi = (TextView) view.findViewById(R.id.textView_score_analysis2_item1_shi);
			jifen = (TextView) view.findViewById(R.id.textView_score_analysis2_item1_jifen);
			pai = (TextView) view.findViewById(R.id.textView_score_analysis2_item1_pai);
			
		}
	}
}
