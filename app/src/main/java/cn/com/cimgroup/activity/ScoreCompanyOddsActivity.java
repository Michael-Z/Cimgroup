package cn.com.cimgroup.activity;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cn.com.cimgroup.BaseLoadActivity;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.Company;
import cn.com.cimgroup.bean.ScoreCompanyOdds;
import cn.com.cimgroup.bean.TheOdds;
import cn.com.cimgroup.config.SDConfig;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class ScoreCompanyOddsActivity extends BaseLoadActivity implements OnClickListener {

	private PullToRefreshListView mPullToRefreshListView;

	private ListViewAdapter listAdapter;

	private ListView listView;
	
	private List<Company> companies = null;
	
	private ScoreCompanyOddsAdapter scoreCompanyOddsAdapter;

	private String timeId = "0";

	private String companyId;

	private String matchId;

	private String gameNo;

	private String companyType;
	/**公司名称*/
	private String mCompanyName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frament_score_company_odds);
		
		
		if (SDConfig.getConfiguration().isSdCardExist()) {
			SDConfig.getConfiguration().setFileName("companies");
			ScoreCompanyOdds odds = (ScoreCompanyOdds) SDConfig.getConfiguration().readSDCard();
			if (odds != null) {
				companies = odds.getCompanyList();
				if (companies != null) {
					timeId = odds.getTimeId();
				}
			}
		}

		Intent intent = getIntent();
		companyId = intent.getStringExtra("companyId");
		matchId = intent.getStringExtra("matchId");
		gameNo = intent.getStringExtra("gameNo");
		companyType = intent.getStringExtra("companyType");
		mCompanyName=intent.getStringExtra("companyName");
		((TextView) findViewById(R.id.textView_title_word)).setText(mCompanyName);
//		switch (companyType) {
//		case "1":
//			((TextView) findViewById(R.id.textView_title_word)).setText(getString(R.string.score_odds_europe));
//			break;
//		case "2":
//			((TextView) findViewById(R.id.textView_title_word)).setText(getString(R.string.score_odds_asia));
//			break;
//		default:
//			break;
//		}
		
		((TextView) findViewById(R.id.textView_title_back)).setOnClickListener(this);

		mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullrefreshView_score_company_odds);
		
		listView = (ListView) findViewById(R.id.listView_score_company_odds);
		
		initLoad(mPullToRefreshListView, scoreCompanyOddsAdapter);
//		Controller.getInstance().getScoreCompany(GlobalConstants.NUM_SCORECOMPANY, timeId, mCallBack);
	}

	@Override
	protected void loadData(int page) {
		// TODO Auto-generated method stub
		showLoadingDialog();
		Controller.getInstance().getScoreOdds(GlobalConstants.NUM_SCOREODDS, companyId, gameNo, matchId, companyType, page + "", mCallBack);
	}

	@Override
	public void loadData(int title, int page) {
		// TODO Auto-generated method stub

	}

	private CallBack mCallBack = new CallBack() {
//		public void getScoreCompanySuccess(final ScoreCompanyOdds obj) {
//			runOnUiThread(new Runnable() {
//				@SuppressWarnings("unchecked")
//				public void run() {
//					if (Integer.parseInt(obj.getResCode()) == 0) {
//						initLoad(mPullToRefreshListView, scoreCompanyOddsAdapter);
//						if (isFirstPage()) {
//							if (obj.getCompanyList().size() == 0) {
//								for (int i = 0; i < companies.size(); i++) {
//									Company company = companies.get(i);
//									if(company.getCompanyId().equals(companyId)){
//										companies.remove(i);
//										companies.add(0, company);
//									}
//								}
//								listAdapter = new ListViewAdapter(ScoreCompanyOddsActivity.this, companies);
//							} else {
//								if (SDConfig.getConfiguration().isSdCardExist()) {
//									SDConfig.getConfiguration().setFileName("companies");
//									SDConfig.getConfiguration().saveToSDCard(obj);
//								}
//								companies = obj.getCompanyList();
//								List<Company> list = obj.getCompanyList();
//								for (int i = 0; i < list.size(); i++) {
//									Company company = list.get(i);
//									if(company.getCompanyId().equals(companyId)){
//										list.remove(i);
//										list.add(0, company);
//									}
//								}
//								listAdapter = new ListViewAdapter(ScoreCompanyOddsActivity.this, list);
//							}
//							listView.setAdapter(listAdapter);
//							listView.setOnItemClickListener(new OnItemClickListener() {
//
//								@Override
//								public void onItemClick(AdapterView<?> parent,
//										View view, int position, long id) {
//									// TODO Auto-generated method stub
//									Company company = (Company) listAdapter.getItem(position);
//									companyId = company.getCompanyId();
//									for (int i = 0; i < companies.size(); i++) {
//										Company company1 = companies.get(i);
//										if(company1.getCompanyId().equals(companyId)){
//											companies.remove(i);
//											companies.add(0, company1);
//										}
//									}
//									listAdapter.notifyDataSetChanged();
//									showLoadingDialog();
//									Controller.getInstance().getScoreOdds(GlobalConstants.NUM_SCOREODDS, companyId, gameNo, matchId, companyType, "1", mCallBack);
//								}
//							});
//							listAdapter.notifyDataSetChanged();
//						} else {
////							scoreCompanyOddsAdapter.addAll(obj.getOdds());
//						}
//						if (obj.getCompanyList() == null || obj.getCompanyList().size() == 0) {
//							// 还原页码
//							restorePage();
//						}
//					} else {
//						showToast(obj.getResMsg());
//					}
//				}
//			});
//		};
		
		public void getScoreOddsSuccess(final ScoreCompanyOdds obj) {
			runOnUiThread(new Runnable() {
				@SuppressWarnings("unchecked")
				public void run() {
					loadFinish();
					if (Integer.parseInt(obj.getResCode()) == 0) {
						if (isFirstPage()) {
							scoreCompanyOddsAdapter = new ScoreCompanyOddsAdapter(ScoreCompanyOddsActivity.this, obj.getList());
							mPullToRefreshListView.setAdapter(scoreCompanyOddsAdapter);
							scoreCompanyOddsAdapter.notifyDataSetChanged();
						} else {
							scoreCompanyOddsAdapter.addAll(obj.getList());
						}
						if (obj.getList() == null || obj.getList().size() == 0) {
							// 还原页码
							restorePage();
						}
						if(scoreCompanyOddsAdapter.getCount()>0){
							mPullToRefreshListView.setMode(Mode.BOTH);
						} else {
							NoDataUtils.setNoDataView(ScoreCompanyOddsActivity.this, emptyImage, oneText, twoText, button, "0", 13);
						}
					} else {
						NoDataUtils.setNoDataView(ScoreCompanyOddsActivity.this, emptyImage, oneText, twoText, button, "0", 13);
					}
				}
			});
		};
		
		public void getScoreCompanyFailure(String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					loadFinish();
					// 还原页码
					restorePage();
//					initLoad(mPullToRefreshListView, scoreCompanyOddsAdapter);
//					if (companies != null) {
//						for (int i = 0; i < companies.size(); i++) {
//							Company company = companies.get(i);
//							if(company.getCompanyId().equals(companyId)){
//								companies.remove(i);
//								companies.add(0, company);
//							}
//						}
//						listAdapter = new ListViewAdapter(ScoreCompanyOddsActivity.this, companies);
//						listView.setAdapter(listAdapter);
//						listView.setOnItemClickListener(new OnItemClickListener() {
//
//							@Override
//							public void onItemClick(AdapterView<?> parent,
//									View view, int position, long id) {
//								// TODO Auto-generated method stub
//								Company company = (Company) listAdapter.getItem(position);
//								companyId = company.getCompanyId();
//								for (int i = 0; i < companies.size(); i++) {
//									Company company1 = companies.get(i);
//									if(company1.getCompanyId().equals(companyId)){
//										companies.remove(i);
//										companies.add(0, company1);
//									}
//								}
//								listAdapter.notifyDataSetChanged();
//								showLoadingDialog();
//								Controller.getInstance().getScoreOdds(GlobalConstants.NUM_SCOREODDS, companyId, gameNo, matchId, companyType, "1", mCallBack);
//							}
//						});
//						listAdapter.notifyDataSetChanged();
//					}
					
					// TODO Auto-generated method stub
					// ToastUtil.shortToast(mFragmentActivity, error);
				}
			});
			super.onFail(error);
		};
		
		public void getScoreOddsFailure(final String error) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 还原页码
					restorePage();
					loadFinish();
					// TODO Auto-generated method stub
					// ToastUtil.shortToast(mFragmentActivity, error);
					NoDataUtils.setNoDataView(ScoreCompanyOddsActivity.this, emptyImage, oneText, twoText, button, error, 0);
				}
			});
			super.onFail(error);
		};
	};

	public class ListViewAdapter<T> extends BaseAdapter {
		private Context context; // 运行上下文
		private List<T> listItems;
		private LayoutInflater listContainer; // 视图容器

		public final class ListItemView { // 自定义控件集合
			public TextView info;
		}

		public ListViewAdapter(Context context, List<T> listItems) {
			this.context = context;
			listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
			this.listItems = listItems;
		}
		
		public void setData(List<T> data) {
			listItems = data;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItems.size();
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return listItems.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		/**
		 * ListView Item设置
		 */
		@SuppressLint("NewApi")
		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// 自定义视图
			ListItemView listItemView = null;
			if (convertView == null) {
				listItemView = new ListItemView();
				// 获取list_item布局文件的视图
				convertView = listContainer.inflate(R.layout.item_company_list, null);
				// 获取控件对象
				listItemView.info = (TextView) convertView
						.findViewById(R.id.textView_score_company_item_text);
//				 设置控件集到convertView
				convertView.setTag(listItemView);
			} else {
				listItemView = (ListItemView) convertView.getTag();
			}
			
			convertView.setMinimumHeight(60);
			
			Company company = (Company) listItems.get(position);
			
			listItemView.info.setText(company.getCompanyName());
			
			if (position == 0) {
				convertView.setBackground(context.getResources().getDrawable(R.drawable.bg_feature_item_p));
				convertView.setSelected(true);
			} else {
				convertView.setSelected(false);
			}
			
			return convertView;
		}

	}
	
	
	public class ScoreCompanyOddsAdapter<T> extends BaseAdapter {
		private Context context; // 运行上下文
		private List<T> listItems;
		private LayoutInflater listContainer; // 视图容器

		public final class ListItemView { // 自定义控件集合
			public TextView itemS;
			public TextView itemP;
			public TextView itemF;
			public TextView itemTime;
		}

		public ScoreCompanyOddsAdapter(Context context, List<T> listItems) {
			this.context = context;
			listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
			this.listItems = listItems;
		}
		
		public void setData(List<T> data) {
			listItems = data;
		}
		@Override
		public int getCount() {
			return listItems.size();
		}
		@Override
		public Object getItem(int arg0) {
			return listItems.get(arg0);
		}
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		
		public void addAll(List<T> elem) {
			listItems.addAll(elem);
	        notifyDataSetChanged();
	    }

	    public void replaceAll(List<T> elem) {
	    	listItems.clear();
	    	listItems.addAll(elem);
	        notifyDataSetChanged();
	    }

		/**
		 * ListView Item设置
		 */
		@SuppressWarnings("unchecked")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// 自定义视图
			ListItemView listItemView = null;
			if (convertView == null) {
				listItemView = new ListItemView();
				// 获取list_item布局文件的视图
				convertView = listContainer.inflate(R.layout.item_score_company_odds, null);
				// 获取控件对象
				listItemView.itemS = (TextView) convertView.findViewById(R.id.textView_score_odds_item_s);
				listItemView.itemP = (TextView) convertView.findViewById(R.id.textView_score_odds_item_p);
				listItemView.itemF = (TextView) convertView.findViewById(R.id.textView_score_odds_item_f);
				listItemView.itemTime = (TextView) convertView.findViewById(R.id.textView_score_odds_item_time);
//				 设置控件集到convertView
				convertView.setTag(listItemView);
			} else {
				listItemView = (ListItemView) convertView.getTag();
			}
			
			TheOdds odds = (TheOdds) listItems.get(position);
			
//			listItemView.itemS.setText(odds.getWin());
			listItemView.itemS.setText(odds.getWin().split("\\,")[0]);
			String aa = odds.getWin().split("\\,")[1];
			switch (odds.getWin().split("\\,")[1]) {
			case "-1":
				listItemView.itemS.setTextColor(context.getResources().getColor(R.color.color_green_warm));
				listItemView.itemS.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_down), null);
				break;
			case "0":
				listItemView.itemS.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
				listItemView.itemS.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				break;
			case "1":
				listItemView.itemS.setTextColor(context.getResources().getColor(R.color.color_red));
				listItemView.itemS.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_up), null);
				break;
			default:
				break;
			}
			if(odds.getEquals().indexOf(",") != -1){
				listItemView.itemP.setText(odds.getEquals().split("\\,")[0]);
				switch (odds.getEquals().split("\\,")[1]) {
				case "-1":
					listItemView.itemP.setTextColor(context.getResources().getColor(R.color.color_green_warm));
					listItemView.itemP.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_down), null);
					break;
				case "0":
					listItemView.itemP.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
					listItemView.itemP.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
					break;
				case "1":
					listItemView.itemP.setTextColor(context.getResources().getColor(R.color.color_red));
					listItemView.itemP.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_up), null);
					break;
				default:
					break;
				}
			} else {
				listItemView.itemP.setText(odds.getEquals());
				listItemView.itemP.setTextColor(context.getResources().getColor(R.color.color_gray_secondary));
				listItemView.itemP.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
			}
			
//			listItemView.itemF.setText(odds.getLose());
			listItemView.itemF.setText(odds.getLose().split("\\,")[0]);
			switch (odds.getLose().split("\\,")[1]) {
			case "-1":
				listItemView.itemF.setTextColor(context.getResources().getColor(R.color.color_green_warm));
				listItemView.itemF.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_down), null);
				break;
			case "0":
				listItemView.itemF.setTextColor(context.getResources().getColor(R.color.color_black));
				listItemView.itemF.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				break;
			case "1":
				listItemView.itemF.setTextColor(context.getResources().getColor(R.color.color_red));
				listItemView.itemF.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.drawable.icon_score_data_up), null);
				break;
			default:
				break;
			}
			
			
			listItemView.itemTime.setText(odds.getTimeDesc());

			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.textView_title_back:
			finish();
			break;
		case R.id.textView_title_word:
			
			break;
		default:
			break;
		}
	}

}
