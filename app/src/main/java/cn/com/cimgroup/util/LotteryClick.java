package cn.com.cimgroup.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.widget.BaseAdapter;
import cn.com.cimgroup.App;
import cn.com.cimgroup.xutils.StringUtil;
import cn.com.cimgroup.xutils.ToastUtil;

/**
 * 
 * @Description:各彩种GridView选号规则
 * @author:Youzh
 * @see:
 * @since:
 * @copyright www.wozhongla.com
 * @Date:2015-3-22
 */
public class LotteryClick {

	private static void showToast(String msg) {
		ToastUtil.shortToast(App.getInstance(), msg);
	}

	/**
	 * @Description:无规则的选球Adapter
	 * @param position
	 * @param list
	 * @param adapter
	 * @author:Youzh
	 * @date:2015-5-11
	 */
	public static void clickNotify(int position, ArrayList<Integer> list,
			BaseAdapter adapter) {
		if (list.size() > 0) {
			if (list.contains(position)) {
				list.remove((Integer) position);
			} else {
				list.add(position);
			}
		} else {
			list.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * @Description:点击更新Adapter
	 * @param position
	 * @param list
	 * @param adapter
	 * @param hintText最多几个
	 * @author:Youzh
	 * @date:2015-5-11
	 */
	public static void clickNotify(int position, ArrayList<Integer> list,
			BaseAdapter adapter, int hintText) {
		if (list.size() > 0) {
			if (list.contains(position)) {
				list.remove((Integer) position);
			} else {
				if (list.size() >= hintText) {
					showToast("最多" + hintText + "个号码");
					return;
				}
				list.add(position);
			}
		} else {
			list.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * @Description: 仅仅只能选一个
	 * @param position
	 * @param list
	 * @param adapter
	 * @author:Youzh
	 * @date:2015-5-11
	 */
	public static void clickOnlyNotify(int position, ArrayList<Integer> list,
			BaseAdapter adapter) {
		if (list.size() > 0) {
			if (list.contains(position)) {
				list.remove((Integer) position);
			} else {
				list.clear();
				list.add(position);
			}
		} else {
			list.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * @Description:双色球标准红球选号规则
	 * @param position
	 * @param list
	 * @param adapter
	 * @author:Youzh
	 * @date:2015-3-22
	 */
	public static void clickRedNotify(int position, ArrayList<Integer> list,
			ArrayList<Integer> buleList, BaseAdapter adapter) {
		// int buleSize = buleList.size();
		int redSize = list.size();
		if (redSize > 0) {
			if (list.contains(position)) {
				list.remove((Integer) position);
			} else {
				if (redSize >= 32) {
					showToast("最多选择32个红球");
					return;
				}
				// if (buleSize > 2 && buleSize < 16 && redSize > 23) {
				// showToast("最多选24个红球");
				// return;
				// }
				// if (buleSize > 2 && buleSize < 8 && redSize > 23) {
				// return;
				// }
				list.add(position);
			}
		} else {
			list.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description:双色球标准蓝球选号规则
	 * @param position
	 * @param redlist
	 * @param bulelist
	 * @param adapter
	 * @author:Youzh
	 * @date:2015-3-22
	 */
	public static void clickBuleNotify(int position,
			ArrayList<Integer> redlist, ArrayList<Integer> bulelist,
			BaseAdapter adapter) {
		int redSize = redlist.size();
		int buleSize = bulelist.size();
		if (buleSize > 0) {
			if (bulelist.contains(position)) {
				bulelist.remove((Integer) position);
			} else {
				if (redSize > 6 && redSize <= 24 && buleSize > 1) {
					if (buleSize == 15) {
						showToast("蓝球不能超过15个");
						return;
					}
				}
				if (redSize > 24 && buleSize > 1) {
					if (buleSize == 8) {
						showToast("蓝球不能超过8个");
						return;
					}
				}
				bulelist.add(position);
			}
		} else {
			bulelist.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description:双色球胆拖红球胆码选号规则
	 * @param position
	 * @param redDanList
	 * @param redTuoList
	 * @param redTuoAdapter
	 * @param adapter
	 * @author:Youzh
	 * @date:2015-3-22
	 */
	public static void clickNotifyDan(int position,
			ArrayList<Integer> redDanList, ArrayList<Integer> redTuoList,
			BaseAdapter redTuoAdapter, BaseAdapter adapter) {
		if (redTuoList.contains(position)) {
			// redTuoList.remove((Integer) position);
			// redTuoAdapter.notifyDataSetChanged();
			showToast("红色胆码与红色拖码不可相同");
			return;
		}
		if (redDanList.size() > 0) {
			if (redDanList.contains(position)) {
				redDanList.remove((Integer) position);
			} else {
				if (redDanList.size() >= 5) {
					showToast("最多能选择5个红色胆码");
					return;
				}
				redDanList.add(position);
			}
		} else {
			redDanList.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description:双色球胆拖红球拖码选号规则
	 * @param position
	 * @param redTuoList
	 * @param redDanList
	 * @param redDanAdapter
	 * @param adapter
	 * @author:Youzh
	 * @date:2015-3-22
	 */
	public static void clickNotifyTuo(int position,
			ArrayList<Integer> redTuoList, ArrayList<Integer> redDanList,
			BaseAdapter redDanAdapter, BaseAdapter adapter) {
		if (redDanList.contains(position)) {
			// redDanList.remove((Integer) position);
			// redDanAdapter.notifyDataSetChanged();
			showToast("红色胆码与红色拖码不可相同");
			return;
		}
		if (redTuoList.size() > 0) {
			if (redTuoList.contains(position)) {
				redTuoList.remove((Integer) position);
			} else {
				if (redTuoList.size() >= 24) {
					showToast("最多能选择24个红色拖码");
					return;
				}
				redTuoList.add(position);
			}
		} else {
			redTuoList.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description:双色球胆拖蓝球选号规则
	 * @param position
	 * @param redDanList
	 * @param redTuoList
	 * @param buleList
	 * @param adapter
	 * @author:Youzh
	 * @date:2015-3-22
	 */
	public static void clickDanTuoBlueNotify(int position,
			ArrayList<Integer> redDanList, ArrayList<Integer> redTuoList,
			ArrayList<Integer> buleList, BaseAdapter adapter) {
		int danSize = redDanList.size();
		int tuoSize = redTuoList.size();
		int buleSize = buleList.size();
		int dantuoSize = danSize + tuoSize;
		if (buleList.size() > 0) {
			if (buleList.contains(position)) {
				buleList.remove((Integer) position);
			} else {
				if (tuoSize < 16 && dantuoSize > 6 && buleSize > 1) {
					if (buleSize >= 15) {
						showToast("蓝球不能超过15个");
						return;
					}
				}
				if (tuoSize > 16 && dantuoSize > 6 && buleSize > 1) {
					if (buleSize >= 7) {
						showToast("蓝球不能超过7个");
						return;
					}
				}
				buleList.add(position);
			}
		} else {
			buleList.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * @Description:双色球的标准钱数
	 * @param redBiaozhunPosList
	 * @param buleBiaozhunPosList
	 * @return
	 * @author:Youzh
	 * @date:2015-5-11
	 */
	public static int clickSsqTzHint(ArrayList<Integer> redBiaozhunPosList,
			ArrayList<Integer> buleBiaozhunPosList) {
		int num = LotteryBettingUtil.Combination(6, redBiaozhunPosList.size())
				* buleBiaozhunPosList.size();
		int money = num * 2;
		return money;
	}

	/**
	 * @Description:双色球的胆拖钱数
	 * @param redDanPosList
	 * @param redTuoPosList
	 * @param buleDanTuoPosList
	 * @return
	 * @author:Youzh
	 * @date:2015-5-11
	 */
	public static int clickSsqDtHint(ArrayList<Integer> redDanPosList,
			ArrayList<Integer> redTuoPosList,
			ArrayList<Integer> buleDanTuoPosList) {
		int redDanSize = redDanPosList.size();
		int redTuoSize = redTuoPosList.size();
		int buleSize = buleDanTuoPosList.size();
		int num = LotteryBettingUtil.danTuoNum(redTuoSize, redDanSize) * buleSize;
		int money = num * 2;
		return money;
	}

	/**
	 * @Description:七乐彩的复式钱数
	 * @param qlcList
	 * @return
	 * @author:Youzh
	 * @date:2015-5-11
	 */
	public static int clickQlcHint(ArrayList<Integer> qlcList) {
		int num = LotteryBettingUtil.Combination(7, qlcList.size());
		int money = num * 2;
		return money;
	}

	/**
	 * @Description:七乐彩的复式钱数
	 * @param qlcList
	 * @return
	 * @author:Wangzhi
	 * @date:2015-11-4
	 */
	public static int clickQlcDtHint(ArrayList<Integer> dList,
			ArrayList<Integer> tList) {
		int num = LotteryBettingUtil.Combination(7 - dList.size(), tList.size());
		int money = num * 2;
		return money;
	}

	/**
	 * @Description:七乐彩复式选号规则
	 * @param position
	 * @param list
	 * @param adapter
	 * @author:wangzhi
	 * @date:2015-11-4
	 */
	public static void clickQlxRedNotify(int position, ArrayList<Integer> list,
			BaseAdapter adapter) {
		// int buleSize = buleList.size();
		int redSize = list.size();
		if (redSize > 0) {
			if (list.contains(position)) {
				list.remove((Integer) position);
			} else {
				list.add(position);
			}
		} else {
			list.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description:七乐彩胆拖,胆码选号规则
	 * @param position
	 * @param redDanList
	 * @param redTuoList
	 * @param redTuoAdapter
	 * @param adapter
	 * @author:Wangzhi
	 * @date:2015-11-4
	 */
	public static void clickQlcNotifyDan(int position,
			ArrayList<Integer> redDanList, ArrayList<Integer> redTuoList,
			BaseAdapter adapter) {
		if (redTuoList.contains(position)) {
			// redTuoList.remove((Integer) position);
			// redTuoAdapter.notifyDataSetChanged();
			showToast("已经选择了相同的拖码");
			return;
		}
		if (redDanList.size() > 0) {
			if (redDanList.contains(position)) {
				redDanList.remove((Integer) position);
			} else {
				if (redDanList.size() >= 6) {
					showToast("最多能选择6个红色胆码");
					return;
				}
				redDanList.add(position);
			}
		} else {
			redDanList.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 
	 * @Description:七乐彩胆拖，拖码选号规则
	 * @param position
	 * @param redTuoList
	 * @param redDanList
	 * @param redDanAdapter
	 * @param adapter
	 * @author:Wangzhi
	 * @date:2015-11-4
	 */
	public static void clickQlcNotifyTuo(int position,
			ArrayList<Integer> redTuoList, ArrayList<Integer> redDanList,
			BaseAdapter adapter) {
		if (redDanList.contains(position)) {
			showToast("已经选择了相同的胆码");
			return;
		}
		if (redTuoList.contains(position)) {
			redTuoList.remove((Integer) position);
		} else {
			redTuoList.add(position);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * @Description:快乐8的钱数
	 * @return
	 * @author:Youzh
	 * @date:2015-5-11
	 */
	public static int clickKl8Hint(int tag, ArrayList<Integer> qlcList) {
		int num = LotteryBettingUtil.Combination(tag, qlcList.size());
		int money = num * 2;
		return money;
	}

	/**
	 * @Description:胜负彩注数计算
	 * @return
	 * @author:Youzh
	 * @date:2015-5-11
	 */
	public static int clickSfcHint(int lotid,
			Map<Integer, List<Integer>> selectItemIndexs, List<Integer> dans) {
		int num = 1, length = 0;
		if (lotid == 0) {// 胜负彩14场
			length = 14;
		} else if (lotid == 1) {
			length = 8;
		} else if (lotid == 2) {
			length = 12;
		} else {// 胜负彩任选9
			length = 9;
		}
		int macthSize = selectItemIndexs.keySet().size();
		if (macthSize < length) {
			return 0;
		}
		if (macthSize == length) {
			Set<Entry<Integer, List<Integer>>> entrySet = selectItemIndexs
					.entrySet();
			for (Entry<Integer, List<Integer>> item : entrySet) {
				num *= item.getValue().size();
			}
		} else {
			Set<Integer> keySet = selectItemIndexs.keySet();
			List<Integer> list = new ArrayList<Integer>();
			list.addAll(keySet);
			List<String> combination = LotteryBettingUtil.combination(list, 9, dans);
			num = 0;
			
			List<Integer> indexList = new ArrayList<Integer>();
			for (int j = 0; j < combination.size(); j++) {
				List<String> temp = Arrays.asList(combination.get(j).split("\\,"));
				if (dans.size() > 0) {
					for (int i : dans) {
						DecimalFormat format = new DecimalFormat("00");
						if (!temp.contains(format.format(i + 1))) {
							indexList.add(j);
						}
					}
				}
			}
			if (indexList.size() > 0) {
				for (int i = 0; i < indexList.size(); i++) {
					for (int j = 0; j < combination.size(); j++) {
						if (j == i) {
							combination.remove(j);
						}
					}
				}
			}
			
			boolean isDouble = false;
			int tmp = 1;
			for (String codes : combination) {
				tmp = 1;
				String[] split = codes.split(",");
				for (String code : split) {
					List<Integer> list2 = selectItemIndexs.get(Integer.parseInt(code) - 1);
					if (list2 != null) {
						if (list2.size() > 1) {
							tmp *= list2.size();
							isDouble = true;
						}
					}
				}
				if (isDouble) {
					num += tmp;
				} else {
					num++;
				}
			}
		}
		return num;
	}
}
