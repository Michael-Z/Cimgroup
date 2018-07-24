package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.ScoreDetailActivity;
import cn.com.cimgroup.bean.ScoreDetailGame;
import cn.com.cimgroup.bean.ScoreDetailGameEvent;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.NoDataUtils;

/**
 * 比分直播事件列表
 * 
 * @author 秋风
 * 
 */
@SuppressLint("InflateParams")
public class ScoreGameFrament extends BaseLoadFragment {

	private boolean mShouldInitialize = true;

	private View mView;
	/** 事件列表适配器 */
	private ScoreAdapter mAdapter;

	private PullToRefreshListView mPullToRefreshListView;

	private GridView gridview;

	// 标志位，标志已经初始化完成。
	private boolean isPrepared;
	/** 比赛详情 */
	private List<ScoreDetailGameEvent> mList;
	/**二级标题布局*/
	private LinearLayout id_title_layout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (mView == null) {
			mView = inflater.inflate(R.layout.frament_score_game, null);
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
			mShouldInitialize = false;
		}
		if (mShouldInitialize) {
			addToastView();
			gridview = (GridView) mView.findViewById(R.id.gridview_score_game);
			initGridView();
			id_title_layout = (LinearLayout) mView.findViewById(R.id.id_title_layout);
			mPullToRefreshListView = (PullToRefreshListView) mView
					.findViewById(R.id.score_game_listView);
			mList = new ArrayList<ScoreDetailGameEvent>();
			// scoreGameAdapter = new ScoreGameAdapter(mFragmentActivity, null);
			mAdapter = new ScoreAdapter();
			mPullToRefreshListView.setAdapter(mAdapter);
//			mPullToRefreshListView.setEmptyView(id_state_toast);
			// mPullToRefreshListView.setAdapter(scoreGameAdapter);
			isPrepared = true;
			lazyLoad();
		}
		return mView;
	}
	/** 没有消息显示时的提示 */
	private TextView id_state_toast;
	private void addToastView() {
		id_state_toast = new TextView(getActivity());
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		id_state_toast.setGravity(Gravity.CENTER);
		id_state_toast.setTextColor(getActivity().getResources().getColor(R.color.color_gray_secondary));
		if (((ScoreDetailActivity) getActivity()).bean.getStatus().equals("0")) {
			id_state_toast.setText("比赛尚未开始");
		} else {
			id_state_toast.setText("暂无数据");
		}
		
		id_state_toast.setLayoutParams(lp);
	}

	private void initGridView() {
		// 生成动态数组，并且转入数据
		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> map1 = new HashMap<String, Object>();
		map1.put("ItemImage", R.drawable.icon_score_game_in);// 添加图像资源的ID
		map1.put("ItemText", "入球");// 按序号做ItemText
		lstImageItem.add(map1);
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		map2.put("ItemImage", R.drawable.icon_score_game_dian);// 添加图像资源的ID
		map2.put("ItemText", "点球");// 按序号做ItemText
		lstImageItem.add(map2);
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		map3.put("ItemImage", R.drawable.icon_score_game_wu);// 添加图像资源的ID
		map3.put("ItemText", "乌龙");// 按序号做ItemText
		lstImageItem.add(map3);
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		map4.put("ItemImage", R.drawable.icon_score_game_wuxiao);// 添加图像资源的ID
		map4.put("ItemText", "入球无效");// 按序号做ItemText
		lstImageItem.add(map4);
		HashMap<String, Object> map5 = new HashMap<String, Object>();
		map5.put("ItemImage", R.drawable.icon_score_game_yred);// 添加图像资源的ID
		map5.put("ItemText", "两黄变一红");// 按序号做ItemText
		lstImageItem.add(map5);
		HashMap<String, Object> map6 = new HashMap<String, Object>();
		map6.put("ItemImage", R.drawable.icon_score_game_huang);// 添加图像资源的ID
		map6.put("ItemText", "黄牌");// 按序号做ItemText
		lstImageItem.add(map6);
		HashMap<String, Object> map7 = new HashMap<String, Object>();
		map7.put("ItemImage", R.drawable.icon_score_game_red);// 添加图像资源的ID
		map7.put("ItemText", "红牌");// 按序号做ItemText
		lstImageItem.add(map7);
		// HashMap<String, Object> map8 = new HashMap<String, Object>();
		// map8.put("ItemImage", R.drawable.icon_score_game_huan);// 添加图像资源的ID
		// map8.put("ItemText", "换人");// 按序号做ItemText
		// lstImageItem.add(map8);
		// 生成适配器的ImageItem <====> 动态数组的元素，两者一一对应
		SimpleAdapter saImageItems = new SimpleAdapter(mFragmentActivity, // 没什么解释
				lstImageItem,// 数据来源
				R.layout.gridview_score_game,// night_item的XML实现

				// 动态数组与ImageItem对应的子项
				new String[] { "ItemImage", "ItemText" },

				// ImageItem的XML文件里面的一个ImageView,两个TextView ID
				new int[] { R.id.ItemImage, R.id.ItemText });
		// 添加并且显示
		gridview.setAdapter(saImageItems);
		gridview.setEnabled(false);
	}

	/** 网络请求开始时间 */
	private long beginTime = 0;

	@Override
	protected void loadData(int page) {
		refreshDatas();

	}

	/**
	 * 刷新、获取新数据
	 */
	private void refreshDatas() {
		long endTime = System.currentTimeMillis();
		if (endTime - beginTime > 800) {
			String matchId = ((ScoreDetailActivity) getActivity()).bean
					.getMatchId();
			String gameNo = ((ScoreDetailActivity) getActivity()).gameNo;
			Controller.getInstance().getScoreDetailGame(
					GlobalConstants.NUM_SCOREDETAILGAME, gameNo, matchId,
					mCallBack);
			beginTime = endTime;
		}
	}

	@Override
	public void loadData(int title, int page) {

	}

	private CallBack mCallBack = new CallBack() {
		public void getScoreDetailGameSuccess(final ScoreDetailGame game) {
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					loadFinish();
					if (game != null && game.getResCode().equals("0")) {
						mList = game.getList();
						if (mList != null && mList.size() != 0) {
							mPullToRefreshListView.setVisibility(View.VISIBLE);
							String code;
							for (int i = 0; i < mList.size(); i++) {
								code = mList.get(i).getEvent().split("\\_")[1];
								if (code.equals("8") || code.equals("9")
										|| code.equals("10") || code.equals("104")
										|| code.equals("102") || code.equals("105")) {
									// 移除掉换人、换入、换下、
									mList.remove(i);
									i--;
								}
							}
							mAdapter.notifyDataSetChanged();
							// if (isFirstPage()) {
							// scoreGameAdapter.replaceAll(mList);
							// } else {
							// scoreGameAdapter.addAll(mList);
							// }
						} else {
//							mPullToRefreshListView.setVisibility(View.GONE);
							NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 12);
							emptyLinearLayout.setPadding(0, 0, 0, 0);
						}
					} else {
						NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 12);
						emptyLinearLayout.setPadding(0, 0, 0, 0);
					}
					if (mList.size() == 0) {
						id_title_layout.setVisibility(View.GONE);
						switch (((ScoreDetailActivity) getActivity()).bean.getStatus()) {
						case "0":
							NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 11);
							emptyLinearLayout.setPadding(0, 0, 0, 0);
							break;
						case "2":
							NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 14);
							emptyLinearLayout.setPadding(0, 0, 0, 0);
							break;
						default:
							break;
						}
					}else {
						id_title_layout.setVisibility(View.VISIBLE);
					}
				}
			});
		};

		public void getScoreDetailGameFailure(final String error) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 还原页码
					restorePage();
//					mPullToRefreshListView.setVisibility(View.GONE);
					loadFinish();
					NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, error, 0);
					emptyLinearLayout.setPadding(0, 0, 0, 0);
					id_title_layout.setVisibility(View.GONE);
				}
			});
			super.onFail(error);
		};
	};

	/**
	 * 比分直播事件适配器
	 * 
	 * @author 秋风
	 * 
	 */
	private class ScoreAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			ViewHolder holder = null;
			if (view == null) {
				view = LayoutInflater.from(getActivity()).inflate(
						R.layout.item_score_game, parent, false);
				holder = new ViewHolder();
				holder.id_zhu_line = view.findViewById(R.id.id_zhu_line);
				holder.id_ke_line = view.findViewById(R.id.id_ke_line);
				holder.id_score_topline = view
						.findViewById(R.id.id_score_topline);
				holder.id_zhu_event_image = (ImageView) view
						.findViewById(R.id.id_zhu_event_image);
				holder.id_ke_event_image = (ImageView) view
						.findViewById(R.id.id_ke_event_image);
				holder.id_zhu_event_text = (TextView) view
						.findViewById(R.id.id_zhu_event_text);
				holder.id_ke_event_text = (TextView) view
						.findViewById(R.id.id_ke_event_text);
				holder.id_event_time = (TextView) view
						.findViewById(R.id.id_event_time);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			
			view.setVisibility(View.VISIBLE);
			ScoreDetailGameEvent object = mList.get(position);
			if (position == mList.size() - 1) {

			}
			holder.id_score_topline.setVisibility(position == 0 ? View.VISIBLE
					: View.GONE);
			/*
			 * 主(客)队_效果_球员名字_时间 说明： 主客队：主队1，客队0
			 * 效果：1.入球；2.点球；3.乌龙球4.黄牌；5.红牌；6.两黄变一红；7.入球无效；8.换人；9.换入；10.换下 例如：
			 * 主队：1_2_谁_10’ 客队：0_2_谁_10
			 */
			String[] event = object.getEvent().split("\\_");
			holder.id_event_time.setText(event[3]);
			Drawable draw = null;
			switch (event[1]) {
			case "1":
				draw = getActivity().getResources().getDrawable(
						R.drawable.icon_score_game_in);
				break;
			case "2":
				draw = getActivity().getResources().getDrawable(
						R.drawable.icon_score_game_wu);
				break;
			case "3":
			case "205":
				draw = getActivity().getResources().getDrawable(
						R.drawable.icon_score_game_dian);
				break;
			case "4":
			case "202":
				draw = getActivity().getResources().getDrawable(
						R.drawable.icon_score_game_huang);
				break;
			case "5":
			case "203":
				draw = getActivity().getResources().getDrawable(
						R.drawable.icon_score_game_red);
				break;
			case "6":
			case "204":
				draw = getActivity().getResources().getDrawable(
						R.drawable.icon_score_game_yred);
				break;
			case "7":
				draw = getActivity().getResources().getDrawable(
						R.drawable.icon_score_game_wuxiao);
				break;
			case "8":
			case "9":
			case "10":
			case "104":
			case "102":
			case "105":
				draw = getActivity().getResources().getDrawable(
						R.drawable.icon_score_game_huan);
				break;
			}
			if (event[0].equals("1")) {
				// 状态
				holder.id_zhu_line.setVisibility(View.VISIBLE);
				holder.id_zhu_event_image.setVisibility(View.VISIBLE);
				holder.id_zhu_event_text.setVisibility(View.VISIBLE);
				holder.id_ke_line.setVisibility(View.GONE);
				holder.id_ke_event_image.setVisibility(View.GONE);
				holder.id_ke_event_text.setVisibility(View.GONE);
				// 赋值
				holder.id_zhu_event_image.setImageDrawable(draw);
				holder.id_zhu_event_text.setText(event[2]);

			} else {
				// 状态
				holder.id_zhu_line.setVisibility(View.GONE);
				holder.id_zhu_event_image.setVisibility(View.GONE);
				holder.id_zhu_event_text.setVisibility(View.GONE);
				holder.id_ke_line.setVisibility(View.VISIBLE);
				holder.id_ke_event_image.setVisibility(View.VISIBLE);
				holder.id_ke_event_text.setVisibility(View.VISIBLE);
				// 赋值
				holder.id_ke_event_image.setImageDrawable(draw);
				holder.id_ke_event_text.setText(event[2]);
			}

			return view;
		}
	}

	final static class ViewHolder {
		/** 主队图片文字分割线 */
		View id_zhu_line;
		/** 客队图片文字分割线 */
		View id_ke_line;
		/** 顶部线 */
		View id_score_topline;
		/** 主队事件图片 */
		ImageView id_zhu_event_image;
		/** 客队事件图片 */
		ImageView id_ke_event_image;
		/** 主队事件 */
		TextView id_zhu_event_text;
		/** 客队事件 */
		TextView id_ke_event_text;
		/** 事件发生时间 */
		TextView id_event_time;
	}

	@Override
	protected void lazyLoad() {
		if (!isPrepared || !isVisible) {
			return;
		} else {
			// 初始化mPullToRefreshListView 上下拉刷新、添加适配器，加载数据等功能
			super.initLoad(mPullToRefreshListView, mAdapter);
			// super.initLoad(mPullToRefreshListView, scoreGameAdapter);
		}
	}

	/**
	 * 解析推送过来的信息
	 * 
	 * @param map
	 */
	public void updateDatas(Map<String, String> map) {
		if (mList != null && map != null) {
			ScoreDetailGameEvent event = new ScoreDetailGameEvent();
			event.setEvent((Integer.parseInt(map.get("goalTeam")) - 1) + "_"
					+ map.get("eventType") + "_" + map.get("player") + "_"
					+ map.get("time")
					+ (map.get("goalTeam").equals("1") ? "'" : ""));
			mList.add(event);
			mAdapter.notifyDataSetChanged();
			mPullToRefreshListView.getRefreshableView().setSelection(mAdapter.getCount() - 1);
		}
	}
}
