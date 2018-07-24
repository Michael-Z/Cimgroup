package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View.OnClickListener;
import cn.com.cimgroup.bean.Match;

/**
 * 竞彩投注适配器父类足彩： 胜平负、让球胜平负、进球数、半全场、比分/篮球：让分胜负、胜负、胜分差、大小 的通用数据
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月6日
 */
public abstract class BaseLotteryBidOnlyCartAdapter extends BaseLotteryBidCartAdapter implements
		OnClickListener {

	/** 用于存储选择投注选项的索引 key=所选item在数据集中得索引位置 value=所选item的投注方式索引 **/
	protected Map<Integer, List<Integer>> mSelectItemIndexs = new HashMap<Integer, List<Integer>>();
	
	public BaseLotteryBidOnlyCartAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 获取足彩基本信息
	 */
	public List<List<Match>> getlotterybidMatchsInfos() {
		return mMatchs;
	}

	/**
	 * 获取选择的所有item的结果索引
	 * @Description:
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月6日
	 */
	public Map<Integer, List<Integer>> getSelectItemIndexs() {
		return mSelectItemIndexs;
	}
	
	/**
	 * 填充数据
	 */
	public void setData(List<List<Match>> mMatchs, List<String> spiltTimes, Map<Integer, List<Integer>> selectItemindexs) {
		this.mMatchs = mMatchs;
		this.mSpiltTimes = spiltTimes;
		this.mSelectItemIndexs = selectItemindexs;
		mSelectItemKeys = new ArrayList<Integer>(mSelectItemIndexs.keySet());
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mSelectItemKeys.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return getlotterybidMatchsInfos().get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
