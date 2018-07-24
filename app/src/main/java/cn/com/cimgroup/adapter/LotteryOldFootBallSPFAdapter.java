package cn.com.cimgroup.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryOldFootballActivity;
import cn.com.cimgroup.bean.Match;
import cn.com.cimgroup.frament.BaseLotteryBidFrament.MatchSearchType;
import cn.com.cimgroup.xutils.DateUtil;

/**
 * 足彩 胜负彩adapter
 * 
 * @Description:
 * @author:zhangjf
 * @copyright www.wenchuang.com
 * @Date:2015年11月16日
 */
public class LotteryOldFootBallSPFAdapter extends BaseLotteryBidOnlyAdapter
		implements OnClickListener {

	private int index = 0;

	private MatchSearchType mSearchType;

	private int num = 14;

	private String issue;

	public LotteryOldFootBallSPFAdapter(Context context,
			MatchSearchType searchType) {
		super(context);
		mSearchType = searchType;

	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		convertView = super.getGroupView(groupPosition, isExpanded,
				convertView, parent);
		ViewGroupHolder holder = (ViewGroupHolder) convertView.getTag();

		String titleWord = (((LotteryOldFootballActivity) mContext).textWord)
				.getText().toString();
		issue = titleWord.split("\\ ")[1].substring(0,
				(titleWord.split("\\ ")[1].length() - 1));

		for (int i = 0; i < infos.size(); i++) {
			if (issue.equals(infos.get(i).getIssue())) {
				index = i;
			}
		}

		StringBuilder sb = new StringBuilder();
		switch (index) {
		case 0:
			sb.append(mContext.getResources().getString(
					R.string.oldfootball_before_qi));
			sb.append(mContext.getResources().getString(
					R.string.record_tz_detail_qi, issue));
			break;
		case 1:
			sb.append(mContext.getResources().getString(
					R.string.oldfootball_current_qi));
			sb.append(mContext.getResources().getString(
					R.string.record_tz_detail_qi, issue));
			sb.append(DateUtil.getTimeInMillisToStr(infos.get(index)
					.getEndTime() + "")
					+ "截止");
			break;
		case 2:
			sb.append(mContext.getResources().getString(
					R.string.oldfootball_after_qi));
			sb.append(mContext.getResources().getString(
					R.string.record_tz_detail_qi, issue));
			sb.append(DateUtil.getTimeInMillisToStr(infos.get(index)
					.getEndTime() + "")
					+ "截止");
			break;
		default:
			break;
		}

		holder.mGroupTextView.setText(sb.toString());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = super.getChildView(groupPosition, childPosition,
				isLastChild, convertView, parent);

		String titleWord = (((LotteryOldFootballActivity) mContext).textWord)
				.getText().toString();
		issue = titleWord.split("\\ ")[1].substring(0,
				(titleWord.split("\\ ")[1].length() - 1));

		for (int i = 0; i < infos.size(); i++) {
			if (issue.equals(infos.get(i).getIssue())) {
				index = i;
			}
		}

		// 获取控件结果集 //TODO父类已经初始化了部分控件。并且进行了赋值
		ViewChildHolder mHolder = (ViewChildHolder) convertView.getTag();
		// 添加胜负彩所需要的控件
		LinearLayout matchTypeLayout = mHolder.mMatchTypeView;
		if (matchTypeLayout != null && matchTypeLayout.getChildCount() == 0) {
			LinearLayout view = (LinearLayout) View.inflate(mContext,
					R.layout.include_oldfootball_spf_select, null);
			matchTypeLayout.addView(view, new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mHolder.mFootballSPFViews = new TextView[] {
					(TextView) view
							.findViewById(R.id.txtView_oldfootball_spf_0),
					(TextView) view
							.findViewById(R.id.txtView_oldfootball_spf_1),
					(TextView) view
							.findViewById(R.id.txtView_oldfootball_spf_2) };
			mHolder.mDan = (TextView) view
					.findViewById(R.id.txtView_oldfootball_item_select);
			mHolder.id_select_dan_layout = (LinearLayout) view
					.findViewById(R.id.id_select_dan_layout);
			mHolder.mDan.setOnClickListener(this);
		}

		// 获取该条在全部比赛中得索引位置
		int dataPosition = super.getSelectClidIndexToAllData(groupPosition,
				childPosition);

		// 将比率显示出来
		Match match = mChildMatchs.get(groupPosition).get(childPosition);

		switch (mSearchType) {
		case TWIN:
			mHolder.id_select_dan_layout.setVisibility(View.GONE);
			num = 14;
			break;
		case SINGLE:
			mHolder.id_select_dan_layout.setVisibility(View.VISIBLE);
			num = 9;
			break;
		default:
			break;
		}

		int selectLen = mSelectItemIndexs.keySet().size();
		if (selectLen > 9 && dans.size() < 9) {
			if ((num - 1) == dans.size()) {
				mHolder.mDan.setEnabled(false);
			} else {
				if (mSelectItemIndexs.containsKey(dataPosition)) {
					mHolder.mDan.setEnabled(true);
				} else {
					mHolder.mDan.setEnabled(false);
				}
			}
		} else {
			mHolder.mDan.setEnabled(false);
		}

		// List<Integer> keys = new ArrayList<Integer>(dans.keySet());
		if (selectLen > 9 && dans.size() != 0) {
			for (int i = 0; i < dans.size(); i++) {
				int value = dans.get(i);
				if (dataPosition == value) {
					mHolder.mDan.setEnabled(true);
					mHolder.mDan.setSelected(true);
					break;
				} else {
					mHolder.mDan.setSelected(false);
				}
			}
		} else {
			mHolder.mDan.setSelected(false);
		}

		for (int i = 0; i < mHolder.mFootballSPFViews.length; i++) {
			if (index == 0 || index == 2) {
				mHolder.mFootballSPFViews[i].setEnabled(false);
			} else {
				mHolder.mFootballSPFViews[i].setEnabled(true);
			}
			mHolder.mFootballSPFViews[i]
					.setTag(new Object[] { dataPosition, i });
			mHolder.mFootballSPFViews[i].setOnClickListener(this);
			mHolder.mFootballSPFViews[i].setSelected(false);

			TextView textView = (TextView) mHolder.mFootballSPFViews[i];
			if (textView.getText().toString().equals(match.getMatchResult())) {
				mHolder.mFootballSPFViews[i].setSelected(true);
			} else {
				mHolder.mFootballSPFViews[i].setSelected(false);
			}

			if (index == 1) {
				if (mSelectItemIndexs.containsKey(dataPosition)) {
					List<Integer> selectWay = mSelectItemIndexs
							.get(dataPosition);
					if (selectWay != null && selectWay.contains(new Integer(i))) {
						mHolder.mFootballSPFViews[i].setSelected(true);
					}
				}
			}
		}

		mHolder.mDan.setTag(dataPosition);

		return convertView;
	}

	class ViewChildHolder extends ViewSuperChildHolder {

		// 胜平负
		private TextView mFootballSPFViews[];

		private TextView mDan;
		private LinearLayout id_select_dan_layout;

	}

	@Override
	public ViewSuperChildHolder getChildHolder() {
		return new ViewChildHolder();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.txtView_oldfootball_item_select:
			int position = (Integer) v.getTag();

			int selectLen = mSelectItemIndexs.keySet().size();
			if (selectLen > 9 && dans.size() < 9) {
				if ((num - 1) >= dans.size()) {
					if (!dans.contains(position)) {
						dans.add(position);
					} else {
						for (int i = 0; i < dans.size(); i++) {
							if (dans.get(i) == position) {
								dans.remove(i);
							}
						}
					}
					((LotteryOldFootballActivity) mContext)
							.changeSelectMatchCount2state(mSelectItemIndexs == null ? 0
									: mSelectItemIndexs.size());
					notifyDataSetChanged();
				}
			}
			break;
		default:
			// 点击投注选项控件
			Object[] obj = (Object[]) v.getTag();
			if (obj != null && obj.length == 2) {
				// 获取该条数据在整个数据集中得索引
				Integer dataPosition = (Integer) obj[0];
				// 每个item的投注选项索引
				Integer index = (Integer) obj[1];
				// 获取该条在全部比赛中得索引位置
				if (mSelectItemIndexs.containsKey(dataPosition)) {
					if (mSelectItemIndexs.get(dataPosition).contains(index)) {
						mSelectItemIndexs.get(dataPosition).remove(index);
						dans.remove(dataPosition);
						if (mSelectItemIndexs.get(dataPosition).size() == 0) {
							mSelectItemIndexs.remove(dataPosition);
							dans.remove(dataPosition);
						}
					} else {
						mSelectItemIndexs.get(dataPosition).add(index);
					}
				} else {
					mSelectItemIndexs.put(dataPosition,
							new ArrayList<Integer>());
					mSelectItemIndexs.get(dataPosition).add(index);
				}
				notifyDataSetChanged();
			}
			setSelectItemIndexs(mSelectItemIndexs);
			((LotteryOldFootballActivity) mContext)
					.changeSelectMatchCount2state(mSelectItemIndexs == null ? 0
							: mSelectItemIndexs.size());
			notifyDataSetChanged();
			break;
		}
	}

}
