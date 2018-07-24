package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.util.FootballLotteryConstants;
import cn.com.cimgroup.util.FootballLotteryHHGGUtil;
import cn.com.cimgroup.util.FootballLotteryUtil;


/**
 * 购物车N串Madapter TODO 目前只是实现了N串1
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月25日
 */
public class LotteryFootBallNchuanMAdapter extends BaseAdapter {

	private Context mContext;
	//最大的串
	private int maxChuan=0;
	private int count = 0;

	/** 获取索引N串1数据 **/
	private Map<String, String> mChuan1Num = new TreeMap<String, String>(FootballLotteryConstants.getmChuan1Num());
	/** 获取索引N串1的索引 **/
	private List<String> nChuamn1Key = new ArrayList(mChuan1Num.keySet());
	
	/** 选择的串 **/
	private List<String> mSelectChuan = new ArrayList<String>();
	
	private int dansLen = 0;
	
	private FootballLotteryHHGGUtil mFootballLotteryHHGGUtil;
	
	private FootballLotteryUtil mFootballLotteryUtil;
	
	private int mIotoId;
	
	/**
	 * 非混合过关构造函数
	 * @param context
	 * @param count
	 * @param lotoId
	 */
	public LotteryFootBallNchuanMAdapter(Context context, int count, int lotoId, Map<Integer, List<Integer>> mSelectItemindexs, FootballLotteryUtil footballLotteryUtil) {
		this.mContext = context;
		mFootballLotteryUtil = footballLotteryUtil;
		mIotoId = lotoId;
		// 比分
		if (lotoId == FootballLotteryConstants.L302) {
			maxChuan = 4;
			// 让球胜平负
		} else if (lotoId == FootballLotteryConstants.L301) {
			maxChuan = 8;
			// 胜平负
		} else if (lotoId == FootballLotteryConstants.L320) {
			maxChuan = 8;
			// 进球数
		} else if (lotoId == FootballLotteryConstants.L303) {
			maxChuan = 6;
			// 半全场
		} else if (lotoId == FootballLotteryConstants.L304) {
			maxChuan = 4;
			// 混合过关
		} else if (lotoId == FootballLotteryConstants.L305) {
			// 篮球让球胜负
		} else if (lotoId == FootballLotteryConstants.L306) {
			maxChuan = 8;
			// 篮球胜负
		} else if (lotoId == FootballLotteryConstants.L307) {
			maxChuan = 8;
			// 胜分差
		} else if (lotoId == FootballLotteryConstants.L308) {
			maxChuan = 4;
			// 大小分
		} else if (lotoId == FootballLotteryConstants.L309) {
			maxChuan = 8;
			
		} else {
			maxChuan = 8;
		}
		if (count > maxChuan) {
			count = maxChuan;
		}
		setBidN(count, mSelectItemindexs);
	}
	
	/**
	 * 混合过关构造函数
	 * @param context
	 * @param selectItemindex
	 */
	public LotteryFootBallNchuanMAdapter(Context context, Map<Integer, Map<Integer, List<Integer>>> selectItemindex, FootballLotteryHHGGUtil footballLotteryHHGGUtil) {
		this.mContext = context;
		mFootballLotteryHHGGUtil = footballLotteryHHGGUtil;
		setN(count,selectItemindex);
	}
	
	/**
	 * 非混合过关设置串数
	 * @Description:
	 * @param n
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void setBidN(int n, Map<Integer, List<Integer>> mSelectItemindexs) {
		mChuan1Num = new TreeMap<String, String>(FootballLotteryConstants.getmChuan1Num());
		nChuamn1Key = new ArrayList(mChuan1Num.keySet());
		List<Integer> dans = mFootballLotteryUtil.getDans();
		dansLen = dans.size();
		
//		if (n > nChuamn1Key.size()) {
//			count = nChuamn1Key.size();
//		} else {
//			count = n;
//		}
		
		if (mIotoId == FootballLotteryConstants.L302) {
			maxChuan = 4;
			// 让球胜平负
		} else if (mIotoId == FootballLotteryConstants.L301) {
			maxChuan = 8;
			// 胜平负
		} else if (mIotoId == FootballLotteryConstants.L320) {
			maxChuan = 8;
			// 进球数
		} else if (mIotoId == FootballLotteryConstants.L303) {
			maxChuan = 6;
			// 半全场
		} else if (mIotoId == FootballLotteryConstants.L304) {
			maxChuan = 4;
			// 混合过关
		} else if (mIotoId == FootballLotteryConstants.L305) {
			// 篮球让球胜负
		} else if (mIotoId == FootballLotteryConstants.L306) {
			maxChuan = 8;
			// 篮球胜负
		} else if (mIotoId == FootballLotteryConstants.L307) {
			maxChuan = 8;
			// 胜分差
		} else if (mIotoId == FootballLotteryConstants.L308) {
			maxChuan = 4;
			// 大小分
		} else if (mIotoId == FootballLotteryConstants.L309) {
			maxChuan = 8;
			
		} else {
			maxChuan = 8;
		}
		
		count = maxChuan;
		if (count > mSelectItemindexs.size()) {
			count = mSelectItemindexs.size();
		}
		
		// 如果n和当前所选场次数相等时，清空胆
		if (n == count) {
			dansLen = 0;
			mFootballLotteryUtil.addDans(new ArrayList<Integer>());
		}
			
		if ((dansLen - 1) > 0) {
			count = count - dansLen;
			for (int i = 0; i < (dansLen - 1); i++) {
				mChuan1Num.remove(nChuamn1Key.get(i));
			}
			nChuamn1Key = new ArrayList(mChuan1Num.keySet());
		} else if ((dansLen - 1) == 0) {
			
			count--;
		} else {
			mChuan1Num = new TreeMap<String, String>(FootballLotteryConstants.getmChuan1Num());
			nChuamn1Key = new ArrayList(mChuan1Num.keySet());
			if (mSelectItemindexs.size() > count) {
				count--;
			} else {
				if (n > mSelectItemindexs.size()) {
					n = mSelectItemindexs.size();
				}
				count = n - 1;
			}
			
		}
		
		notifyDataSetChanged();
	}
	
	
	/**
	 * 混合过关设置串数
	 * @param context
	 * @param selectItemindex
	 */
	public void setN(int n, Map<Integer, Map<Integer, List<Integer>>> selectItemindex) {
		mChuan1Num = new TreeMap<String, String>(FootballLotteryConstants.getmChuan1Num());
		nChuamn1Key = new ArrayList(mChuan1Num.keySet());
		getChuanCount(selectItemindex);
		
		// 如果n和当前所选场次数相等时，清空胆
		if (n == count) {
			mFootballLotteryHHGGUtil.addDans(new ArrayList<Integer>());
		}
		n = count;
		
		if (n > nChuamn1Key.size()) {
			count = nChuamn1Key.size();
		} else {
			count = n - 1;
		}
		n = 0;
		dansLen = mFootballLotteryHHGGUtil.getDans().size();
		List<Integer> dans = mFootballLotteryHHGGUtil.getDans();
		if ((dansLen - 1) > 0) {
			count = count - (dansLen - 1);
			for (int i = 0; i < (dans.size() - 1); i++) {
				mChuan1Num.remove(nChuamn1Key.get(i));
			}
			nChuamn1Key = new ArrayList(mChuan1Num.keySet());
		}
		
		notifyDataSetChanged();
	}

	/**
	 * 设置默认选中
	 * @Description:
	 * @param index
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void setSelectIndex(int index) {
		if(nChuamn1Key != null && nChuamn1Key.get(index) != null){
			if(!mSelectChuan.contains(mChuan1Num.get(nChuamn1Key.get(index)))){
				mSelectChuan.add(mChuan1Num.get(nChuamn1Key.get(index)));
				notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * 获取当前选择的串
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public List<String> getSelectData() {
		List<String> resultChuan=new ArrayList<String>();
		resultChuan.addAll(mSelectChuan);
		return resultChuan;
	}
	
	/**
	 * 手动设置选择的串
	 * @Description:
	 * @param selectChuan
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void setSelectData(List<String> selectChuan){
		this.mSelectChuan=selectChuan==null?new ArrayList<String>():selectChuan;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return count == -1 ? 0 : count;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_footballlottery_nchuanm, null);
			holder = new ViewHolder();
			holder.mItemSelectView = (TextView) convertView.findViewById(R.id.btnView_football_itemselect);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 判断当前的串是否已经选择
		if (mSelectChuan.contains(mChuan1Num.get(nChuamn1Key.get(position)))) {
			holder.mItemSelectView.setSelected(true);
		} else {
			holder.mItemSelectView.setSelected(false);
		}
		holder.mItemSelectView.setTag(mChuan1Num.get(nChuamn1Key.get(position)));
		holder.mItemSelectView.setText(mChuan1Num.get(nChuamn1Key.get(position)));
		holder.mItemSelectView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String value = (String) v.getTag();
				if (mSelectChuan.contains(value)) {
					if (mSelectChuan.size() > 1) {
						mSelectChuan.remove(value);
					}
				} else {
					if (mSelectChuan.size() > 0) {
						String temp = mSelectChuan.get(0).split(mContext.getResources().getString(R.string.football_chuan))[0];
						String curr = value.split(mContext.getResources().getString(R.string.football_chuan))[0];
						if (Integer.parseInt(curr) < Integer.parseInt(temp)) {
							mSelectChuan.add(0, value);
						} else {
							mSelectChuan.add(value);
						}
					}
//					if (mFootballLotteryUtil != null) {
//						if (Integer.parseInt(mSelectChuan.get(mSelectChuan.size() - 1).split(mContext.getResources().getString(R.string.football_chuan))[0]) == maxChuan) {
//							mChuan1Num = new TreeMap<String, String>(FootballLotteryConstants.getmChuan1Num());
//							nChuamn1Key = new ArrayList(mChuan1Num.keySet());
//							count = maxChuan - 1;
//						}
//					}
				}
				notifyDataSetChanged();

			}
		});
		return convertView;
	}

	class ViewHolder {

		public TextView mItemSelectView;
	}
	
	/**
	 * 计算最大串数
	 * @Description:
	 * @param selectItemindex
	 * @author:www.wenchuang.com
	 * @date:2016年1月25日
	 */
	public void getChuanCount(Map<Integer, Map<Integer, List<Integer>>> selectItemindex){
		count = 8;
		if(selectItemindex != null){
			int indexLen = selectItemindex.size();
			int maxChuanLen = 8;
			List<Integer> keys = new ArrayList<Integer>(selectItemindex.keySet());
			for(int i = 0;i < keys.size(); i++){
				Map<Integer, List<Integer>> itemIndexs = selectItemindex.get(keys.get(i));
				List<Integer> itemKeys = new ArrayList<Integer>(itemIndexs.keySet());
				for (int j = 0; j < itemKeys.size(); j++) {
					if (itemKeys.get(j) == FootballLotteryConstants.L302 || itemKeys.get(j) == FootballLotteryConstants.L304 || itemKeys.get(j) == FootballLotteryConstants.L308) {
						count = 4;
						maxChuanLen = 4;
						if (indexLen > maxChuanLen) {
							count = maxChuanLen;
						}else {
							count = indexLen;
						}
						return;
					}else if (itemKeys.get(j) == FootballLotteryConstants.L303) {
						if (maxChuanLen > 6) {
							maxChuanLen = 6;
						}
					}else {
						if (maxChuanLen > 8) {
							maxChuanLen = 8;
						}
						
					}
				}
			}
			if (indexLen > maxChuanLen) {
				count = maxChuanLen;
			}else {
				count = indexLen;
			}
		}
	}

	public Map<String, String> getmChuan1Num() {
		return mChuan1Num;
	}

	public void setmChuan1Num(Map<String, String> mChuan1Num) {
		this.mChuan1Num = mChuan1Num;
	}

	public List<String> getnChuamn1Key() {
		return nChuamn1Key;
	}

	public void setnChuamn1Key(List<String> nChuamn1Key) {
		this.nChuamn1Key = nChuamn1Key;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
