package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.com.cimgroup.App;
import cn.com.cimgroup.BaseFrament;
import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.GlobalTools;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.LotteryFootballActivity;
import cn.com.cimgroup.bean.LeMi;
import cn.com.cimgroup.bean.LeMiDetail;
import cn.com.cimgroup.dailog.LotteryProgressDialog;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.protocol.CException;
import cn.com.cimgroup.util.NoDataUtils;
import cn.com.cimgroup.util.ViewUtils;
import cn.com.cimgroup.xutils.NetUtil;

/**
 * 个人中心乐米明细
 * 
 * @author 秋风
 * 
 */
@SuppressLint("InflateParams")
public class PersonInfoLMDetailFragment extends BaseFrament implements
		OnClickListener {

	private LotteryProgressDialog mDialog;
	private View mView;
	/** 乐米明细ListView */
	private ListView id_lemi_detail_listview;
	/** 列表下方横线 */
	private View id_bottom_line;

	/** 乐米明细适配器 */
	private LemiDetailAdapter mAdapter;
	/** 乐米明细数据源 */
	private List<LeMiDetail> mList;
	private int page = 1;
	/** 每页显示数据数量 */
	private static final int PAGE_COUNAT = 10;

	/** 没有数据的时候显示娱乐场跳转 */
	private ViewStub id_empty_viewstub;
	
	private ImageView emptyImage;
	private TextView oneText;
	private TextView twoText;
	private TextView button;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (mView == null) {
			mView = inflater.inflate(R.layout.fragment_person_info_lemi_detail,
					null);
			initView();
		} else {
			ViewGroup parent = (ViewGroup) mView.getParent();
			if (parent != null) {
				parent.removeView(mView);
			}
		}
		
		return mView;
	}
	
	/** 显示娱乐场跳转 */
	private void initViewStub(boolean isShow, String errCode, int type) {
		if (null == id_empty_viewstub) {
			id_empty_viewstub = (ViewStub) mView
					.findViewById(R.id.id_empty_viewstub);
			id_empty_viewstub.inflate();
//			mView.findViewById(R.id.id_detail_jump_game).setOnClickListener(
//					this);
			
			emptyImage = (ImageView) mView.findViewById(R.id.imageView_load_empty);
			
			oneText = (TextView) mView.findViewById(R.id.textView_load_one);
			
			twoText = (TextView) mView.findViewById(R.id.textView_load_two);
			
			button = (TextView) mView.findViewById(R.id.button_load);
			
			button.setOnClickListener(this);
		}
		if (isShow) {
			id_empty_viewstub.setVisibility(View.VISIBLE);
			NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, errCode, type);
		} else {
			id_empty_viewstub.setVisibility(View.GONE);
		}
		

	}

	/**
	 * 初始化视图组件
	 */
	private void initView() {
		id_lemi_detail_listview = (ListView) mView
				.findViewById(R.id.id_lemi_detail_listview);
		id_bottom_line = mView.findViewById(R.id.id_bottom_line);
		mList = new ArrayList<LeMiDetail>();
		mAdapter = new LemiDetailAdapter();
		id_lemi_detail_listview.setAdapter(mAdapter);
	}

	/**
	 * 获取碎片实例
	 * 
	 * @param tag
	 * @return
	 */
	public static PersonInfoLMDetailFragment newInstance(String tag) {
		PersonInfoLMDetailFragment fragment = new PersonInfoLMDetailFragment();
		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);
		fragment.setArguments(bundle);
		return fragment;

	}
	private boolean isFirst=true;
	@Override
	public void onResume() {
		super.onResume();
		if (isFirst) {
			isFirst=false;
			getDatas();
		}else {
			if (GlobalConstants.isRefreshFragment) {
//				if (page > 1) {
//					page--;
//				}
//				getDatas();
				refreshDatas();
			}else {
				if (mList==null||mList.size() == 0) {
					refreshDatas();
				}
			}
			
			
		}
		

	}

	/** 获取数据 */
	private void getDatas() {
		if (App.userInfo != null
				&& !TextUtils.isEmpty(App.userInfo.getUserId())&&GlobalConstants.personTagIndex == 1) {
			if (!NetUtil.isConnected(getActivity())) {
				initViewStub(true,"10000", 0);
				isCanLoadDatas = false;
				notifyParentHideLoading();
				return;
			}
			if (null!= mFragmentListener) {
				mFragmentListener.showLodingDialog();
			}
			Controller.getInstance().getLeMiClearList(GlobalConstants.NUM_LEMI,
					UserLogic.getInstance().getDefaultUserInfo().getUserId(),
					"", "", "", PAGE_COUNAT+"", page + "", mCallBack);
		}
	}

	/**
	 * 通知父布局收回刷新
	 */
	private void notifyParentHideLoading() {
		if (mFragmentListener != null) {
			mFragmentListener.hideLodingDialog();
			if (isRefresh) {
				mFragmentListener.onRefreshFinish(isCanLoadDatas);
			} else {
				mFragmentListener.onLoadFinish(isCanLoadDatas);
			}

		}
	}

	private CallBack mCallBack = new CallBack() {
		@Override
		public void onSuccess(final Object t) {
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					LeMi lemi = (LeMi) t;
					List<LeMiDetail> lemiList = lemi.list;
					if (lemi.resCode == 0) {
						if (isRefresh) {
							mList.clear();
						}
						mList.addAll(lemiList);
					} else {
//						ToastUtil.shortToast(getActivity(), lemi.resMsg);
					}
					// addTestDatas();
					mAdapter.notifyDataSetChanged();
					if (lemiList != null && lemiList.size() == PAGE_COUNAT) {
						page++;
						isCanLoadDatas = true;
						// 通知父容器可以加载更多数据
					} else {
						isCanLoadDatas = false;
						// 通知父容器不能加载更多数据
					}
					if (mList == null || mList.size() == 0) {
						id_lemi_detail_listview.setVisibility(View.GONE);
						id_bottom_line.setVisibility(View.GONE);
						initViewStub(true, "0", 1);
					} else {
						ViewUtils.setListViewHeightBasedOnChildren(id_lemi_detail_listview);
						id_lemi_detail_listview.setVisibility(View.VISIBLE);
						id_bottom_line.setVisibility(View.VISIBLE);
						initViewStub(false, "0", 1);
					}
					if (mFragmentListener != null) {
						if (isRefresh) {
							mFragmentListener.onRefreshFinish(isCanLoadDatas);
						} else {
							mFragmentListener.onLoadFinish(isCanLoadDatas);
						}
						mFragmentListener.hideLodingDialog();

					}
					GlobalConstants.isRefreshFragment = false;
				}

			});
		}

		@Override
		public void onFail(final String error) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (mList == null || mList.size() == 0) {
						id_lemi_detail_listview.setVisibility(View.GONE);
						id_bottom_line.setVisibility(View.GONE);
						initViewStub(true, error, 0);
						isCanLoadDatas = false;
					} else {
						int num=0;
						int size=mList.size();
						for (int i = 0; i < size; i++) {
							if (mList.get(i).typeDes.length()>7) {
								num++;
							}
						}
						ViewUtils.setListViewHeightBasedOnChildren(id_lemi_detail_listview);
						id_lemi_detail_listview.setVisibility(View.VISIBLE);
						id_bottom_line.setVisibility(View.VISIBLE);
						initViewStub(false, error, 0);
					}
					if (mFragmentListener != null) {
						if (isRefresh) {
							mFragmentListener.onRefreshFinish(isCanLoadDatas);
						} else {
							mFragmentListener.onLoadFinish(isCanLoadDatas);
							mFragmentListener.hideLodingDialog();
						}
					}
				}
			});
		};
	};

	/** 添加测试数据 */
	@SuppressWarnings("unused")
	private void addTestDatas() {
		for (int i = 0; i < 15; i++) {
			LeMiDetail detail = new LeMiDetail();
			detail.dealType = "收支，支出";
			detail.money = ((i + 1) * 23) + "";
			detail.inOut = "+";
			detail.balance = "1231.43";
			detail.typeDes = "收支";
			detail.detailMsg = "收支……钱";
			mList.add(detail);
		}

	}

	/**
	 * 乐米明细适配器
	 * 
	 * @author 秋风
	 * 
	 */
	class LemiDetailAdapter extends BaseAdapter {

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
			if (null == view) {
				holder = new ViewHolder();
				view = LayoutInflater.from(mFragmentActivity).inflate(
						R.layout.item_mylemi, null);
				holder.time_view = (TextView) view.findViewById(R.id.time_view);
				holder.type_view = (TextView) view.findViewById(R.id.type_view);
				holder.num_view = (TextView) view.findViewById(R.id.num_view);
				holder.marks_view = (TextView) view
						.findViewById(R.id.marks_view);
//				holder.textView_lemi_rest = (TextView) view
//						.findViewById(R.id.textView_lemi_rest);
				view.setTag(holder);
			} else
				holder = (ViewHolder) view.getTag();
			LeMiDetail item = mList.get(position);
			holder.time_view.setText("时间:  "
					+ GlobalTools.timeStamp2Date(item.createTime, null));
			holder.type_view.setText("类型:  " + item.typeDes);
			String numTotal = "乐米:  " + item.inOut + item.money;
			SpannableStringBuilder ssb = new SpannableStringBuilder(numTotal);
			ssb.setSpan(new ForegroundColorSpan(getActivity().getResources()
					.getColor(R.color.hall_black_word)), 0, 3,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			if (item.inOut.equals("-")) {
				ssb.setSpan(new ForegroundColorSpan(getActivity()
						.getResources().getColor(R.color.color_green_warm)), 4,
						numTotal.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else {
				ssb.setSpan(new ForegroundColorSpan(getActivity()
						.getResources().getColor(R.color.color_red)), 4,
						numTotal.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			holder.num_view.setText(ssb);
			holder.marks_view.setText("备注:  " + item.detailMsg);
//			BigDecimal money = new BigDecimal(item.balance);
//			String moneyStr = "";
//			if (money.doubleValue() > 100000d) {
//				moneyStr = money.divide(new BigDecimal("10000"), 2,
//						RoundingMode.DOWN).toPlainString()
//						+ "万";
//			} else
//				moneyStr = money.toPlainString();

//			holder.textView_lemi_rest.setText("剩余乐米:" + moneyStr+"米");
			return view;
		}

		class ViewHolder {
			/** 时间 */
			TextView time_view;
			/** 类型 */
			TextView type_view;
			/** 操作金额数量 */
			TextView num_view;
			/** 备注 */
			TextView marks_view;
//			/** 乐米剩余 */
//			TextView textView_lemi_rest;
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		Controller.getInstance().removeCallback(mCallBack);
	}

	@Override
	protected void lazyLoad() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_load:
			
			switch (Integer.parseInt(NoDataUtils.getmErrCode())) {
			
			case CException.NET_ERROR:
			case CException.IOERROR:
				getDatas();
				break;
			case 0:
				//type 1,投注记录、资金明细、红包明细-立即购彩
				switch (NoDataUtils.getmType()) {
				case 1:
					startActivity(LotteryFootballActivity.class);
					break;
				default:
					break;
				}
			default:
				break;
			}
			
			break;
		default:
			break;
		}

	}

	/** 是否是刷新数据 */
	private boolean isRefresh = true;
	/** 是否可以加载更多 */
	private boolean isCanLoadDatas = true;

	/** 刷新数据 */
	public void refreshDatas() {
		page = 1;
		isRefresh = true;
		getDatas();
	}

	/** 加载数据 */
	public void loadDatas() {
		isRefresh = false;
		getDatas();
	}

	private OnPersonIfnoLMDetailListener mFragmentListener;

	public void setOnPersonIfnoLMDetailListener(
			OnPersonIfnoLMDetailListener listener) {
		mFragmentListener = listener;
	}

	public interface OnPersonIfnoLMDetailListener {
		/***
		 * 信息加载完成
		 * 
		 * @param isCanLoadDatas
		 */
		void onLoadFinish(boolean isCanLoadDatas);

		void onRefreshFinish(boolean isCanLoadDatas);
		void showLodingDialog();
		void hideLodingDialog();
	}
}
