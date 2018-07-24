package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;
import android.view.View.OnClickListener;
import cn.com.cimgroup.bean.LoBoPeriodInfo;
import cn.com.cimgroup.bean.Match;
//import cn.com.cimgroup.other.beans.LotteryBidSp;

/**
 * 竞彩投注适配器父类足彩： 胜平负、让球胜平负、进球数、半全场、比分/篮球：让分胜负、胜负、胜分差、大小 的通用数据
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2016年1月6日
 */
public abstract class BaseLotteryBidOnlyAdapter extends BaseLotteryBidAdapter implements
		OnClickListener {

	/** 用于存储选择投注选项的索引 key=所选item在数据集中得索引位置  value=所选item的投注方式索引**/
	protected Map<Integer, List<Integer>> mSelectItemIndexs = new HashMap<Integer, List<Integer>>();
	
	protected List<LoBoPeriodInfo> infos = new ArrayList<LoBoPeriodInfo>();
	
//	public Map<Integer, Integer> dans = new HashMap<Integer, Integer>();
	
	public List<Integer> dans = new ArrayList<Integer>();
	
	public BaseLotteryBidOnlyAdapter(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 选择的投注方式，外部赋值
	 * @Description:
	 * @param selectItemindex
	 * @author:www.wenchuang.com
	 * @date:2016年1月6日
	 */
	public void setSelectItemIndexs(Map<Integer, List<Integer>> selectItemindex) {
		if(selectItemindex==null){
			selectItemindex=new TreeMap<Integer, List<Integer>>();
		}
		this.mSelectItemIndexs = selectItemindex;
		this.notifyDataSetChanged();
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
	 * @Description:
	 * @param spiltTimes
	 * @param mMatchs
	 * @author:www.wenchuang.com
	 * @date:2016年1月6日
	 */
	public void setData(List<String> spiltTimes, List<List<Match>> mMatchs) {
		this.mGroupSpiltTimes = spiltTimes;
		this.mChildMatchs = mMatchs;
		notifyDataSetChanged();
	}
	
	/**
	 * 填充数据
	 * @Description:
	 * @param spiltTimes
	 * @param mMatchs
	 * @author:www.wenchuang.com
	 * @date:2016年1月6日
	 */
	public void setData(List<String> spiltTimes, List<List<Match>> mMatchs, List<LoBoPeriodInfo> infos) {
		this.mGroupSpiltTimes = spiltTimes;
		this.mChildMatchs = mMatchs;
		this.infos = infos;
		notifyDataSetChanged();
	}
	
	public List<Integer> getDans(){
		return dans;
	}
}
