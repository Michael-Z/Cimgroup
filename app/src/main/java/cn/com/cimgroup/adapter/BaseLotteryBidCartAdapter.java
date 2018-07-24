package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.HtmlCommonActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryTools;

/**
 * 竞彩购物车适配器父类，提供所有竞彩购物车的最通用的数据(足球：胜平负、让球胜平负、进球数、半全场、比分、混合过关 篮球：让球胜负、胜负、大小分、胜分差)
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2016年1月6日
 */
public abstract class BaseLotteryBidCartAdapter extends BaseAdapter implements
		OnClickListener {

	/**
	 * 购物车数据发生变化回调
	 * 
	 * @Description:
	 * @author:zhangjf
	 * @copyright www.wenchuang.com
	 * @Date:2016年1月6日
	 */
	public interface AdapterCartChange {
		public void onIndexClickCartChagne();
	}

	/** 购物车数据发生变化 **/
	protected AdapterCartChange mAdapterDataChagne;

	/** 足彩基本信息集合 (进球数、比分、胜平负、半全场、混合过关) **/
	protected List<List<Match>> mMatchs = null;

	protected List<String> mSpiltTimes = new ArrayList<String>();

	// protected Map<Integer, Integer> dans = new HashMap<Integer, Integer>();
	protected List<Integer> dans = new ArrayList<Integer>();

	/** 为已经选择的足彩的索引的集合  索引指的是所有足彩基本信息的index索引 **/
	protected List<Integer> mSelectItemKeys = null;

	/** 上下文 **/
	protected Context mContext;
	
	protected int mLotoId;

	public BaseLotteryBidCartAdapter(Context context) {
		this.mContext = context;
	}

	public void setData(List<List<Match>> mMatchs, List<String> spiltTimes,
			Map<Integer, List<Integer>> selectItemindexs) {

	};

	public void setData(List<List<Match>> mMatchs,
			Map<Integer, Map<Integer, List<Integer>>> selectItemindexs,
			List<String> spiltTimes) {
	}

	public List<Integer> getDans() {
		return dans;
	}

	public void setDans(List<Integer> dans) {
		this.dans = dans;
	}

	/**
	 * 添加数据发生改变回调
	 * 
	 * @Description:
	 * @param cartDataChange
	 * @author:www.wenchuang.com
	 * @date:2016年1月6日
	 */
	public void addDataChagneListener(AdapterCartChange cartDataChange) {
		mAdapterDataChagne = cartDataChange;
	}

	public abstract ViewSuperHolder getViewHolder();

	/**
	 * 获取足彩基本信息
	 * 
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月6日
	 */
	public List<List<Match>> getlotterybidMatchsInfos() {
		return mMatchs;
	}

	@Override
	public int getCount() {
		return mSelectItemKeys.size();
	}

	@Override
	public Object getItem(int position) {
		return getlotterybidMatchsInfos().get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewSuperHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext,
					R.layout.item_footballlottery_cart, null);
			viewHolder = getViewHolder();
			viewHolder.mMatchGuestView = (TextView) convertView
					.findViewById(R.id.txtView_lotterybid_cart_matchGuest);
			viewHolder.mDan = (TextView) convertView
					.findViewById(R.id.txtView_lotterybid_cart_dan);
			viewHolder.mMatchHomeView = (TextView) convertView
					.findViewById(R.id.txtView_lotterybid_cart_matchHome);
			viewHolder.mVS = (TextView) convertView
					.findViewById(R.id.txtView_lotterybid_cart_vs);
			viewHolder.mTrashView = (ImageView) convertView
					.findViewById(R.id.imgView_lotterybid_cart_trash);
			viewHolder.mMatchTypeView = (LinearLayout) convertView
					.findViewById(R.id.layoutView_lotterybid_cart_match_type);
			viewHolder.mDocView = (LinearLayout) convertView
					.findViewById(R.id.layout_football_shuoming);
			viewHolder.mTrashView.setOnClickListener(this);
			viewHolder.mDan.setOnClickListener(this);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewSuperHolder) convertView.getTag();
		}
		viewHolder.mTrashView.setTag(position);
		viewHolder.mDan.setTag(position);

		List<Integer> selectList = new ArrayList<Integer>(mSelectItemKeys);
		Collections.sort(selectList);
		int[] arr = FootballLotteryTools.getMatchPositioin(
				selectList.get(position), getlotterybidMatchsInfos());

		int count = 0;
		for (int i = 0; i < mMatchs.size(); i++) {
			count += mMatchs.get(i).size();
		}
		
		if (arr[0] <= count && mMatchs.size() > arr[0]) {
			if (mMatchs.get(arr[0]).size() > 0) {
				Match match = mMatchs.get(arr[0]).get(arr[1]);
				//竞猜篮球对阵名字顺序与其他彩种不同，需单独处理
				switch (mLotoId) {
				case FootballLotteryConstants.L502:
					
					break;
				case FootballLotteryConstants.L309:
				case FootballLotteryConstants.L308:
				case FootballLotteryConstants.L310:
				case FootballLotteryConstants.L307:
					viewHolder.mMatchHomeView.setText(match.getGuestTeamName());
					viewHolder.mMatchGuestView.setText(match.getHomeTeamName());
					break;
				default:
					viewHolder.mMatchHomeView.setText(match.getHomeTeamName());
					viewHolder.mMatchGuestView.setText(match.getGuestTeamName());
					break;
				}
			}
		}
		

		if (position == (mSelectItemKeys.size() - 1)) {
			viewHolder.mDocView.setVisibility(View.VISIBLE);
			viewHolder.mDocView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(mContext, HtmlCommonActivity.class);
					intent.putExtra("isWeb", false);
					intent.putExtra("lotteryName", 0);
					mContext.startActivity(intent);
				}
			});
		} else {
			viewHolder.mDocView.setVisibility(View.GONE);
		}
		return convertView;
	}

	protected class ViewSuperHolder {

		/** 主对 **/
		protected TextView mMatchHomeView;
		/** 客对 **/
		protected TextView mMatchGuestView;
		/** 删除此条记录 **/
		protected ImageView mTrashView;
		protected TextView mDan;
		protected TextView mVS;
		protected LinearLayout mMatchTypeView;
		protected LinearLayout mDocView;
	}

}
