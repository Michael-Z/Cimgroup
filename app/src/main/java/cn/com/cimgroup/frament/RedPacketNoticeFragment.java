package cn.com.cimgroup.frament;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.RedPacketNoticeAdapter;
import cn.com.cimgroup.bean.RedPkgUsed;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.logic.UserLogic;
import cn.com.cimgroup.util.DensityUtils;
import cn.com.cimgroup.util.NoDataUtils;

/**
 * Created by small on 16/1/13.
 * 有效红包;无效红包,红包记录;
 */
public class RedPacketNoticeFragment extends BaseLoadFragment {

    public final static String TYPE = "TYPE";
    public final static int USERD = 0;     //keyong
    public final static int NOT_USED = 1;       //不可用;




    private boolean mShouldInitialize = true;
    private boolean isPrepared;
    protected View mView;
    protected PullToRefreshListView mPullToRefreshListView;
    @SuppressWarnings("rawtypes")
	private RedPacketNoticeAdapter adapter;
    private int type;

    @SuppressLint("InflateParams")
	@SuppressWarnings("rawtypes")
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    	super.onCreateView(inflater, container, savedInstanceState);
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_red_packet, null);
        } else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
            mShouldInitialize = false;
        }

        if (mShouldInitialize) {
            mPullToRefreshListView = (PullToRefreshListView) mView.findViewById(R.id.list_view);
            mPullToRefreshListView.getRefreshableView().setDivider(getActivity().getResources().getDrawable(R.drawable.blue_gray_line));
            mPullToRefreshListView.getRefreshableView().setDividerHeight(DensityUtils.dip2px(5));
            adapter = new RedPacketNoticeAdapter(getActivity(),null);
            Bundle bundle = getArguments();
            if(bundle != null){
              type = bundle.getInt(TYPE);
            }
            adapter.setType(RedPacketNoticeAdapter.REDPKG);
            isPrepared = true;
            lazyLoad();
        }
        return mView;
    }

    @Override
    protected void loadData(int page) {
        if(type == NOT_USED){
            Controller.getInstance().getNoUsedRedList(GlobalConstants.NUM_REDPKG_NOUSED, UserLogic.getInstance().getDefaultUserInfo().getUserId(),page+"","10",callBack);
        }else{
            Controller.getInstance().getNoUsedRedList(GlobalConstants.NUM_REDPKG_USED, UserLogic.getInstance().getDefaultUserInfo().getUserId(),page+"","10",callBack);
        }
    }

    private CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(final Object t) {
            getActivity().runOnUiThread(new Runnable() {
                @SuppressWarnings("unchecked")
				@Override
                public void run() {
                    loadFinish();

                    RedPkgUsed redPkgUsed = (RedPkgUsed) t;
                    if (Integer.parseInt(redPkgUsed.resCode) == 0) {
	                    if (isFirstPage()) {
	                        adapter.replaceAll(redPkgUsed.redPkgList);
	
	                    } else {
	                        adapter.addAll(redPkgUsed.redPkgList);
	                    }
	                    
	                    if (redPkgUsed.redPkgList == null || redPkgUsed.redPkgList.size() == 0) {
	                        restorePage();
	                    }
	                    if (adapter.getCount() < Integer.parseInt(redPkgUsed.total)) {
	                        mListView.setMode(PullToRefreshBase.Mode.BOTH);
	                    }
	                    
	                    if (adapter.getCount() == 0) {
	                    	NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 2);
						}
                    } else {
                    	NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, "0", 2);
					}
                }
            });
        }

        @Override
        public void onFail(final String error) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadFinish();
                    NoDataUtils.setNoDataView(mFragmentActivity, emptyImage, oneText, twoText, button, error, 0);
                }
            });
        }
    };

    @Override
    public void loadData(int title, int page) {

    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        } else {
            // 初始化mPullToRefreshListView 上下拉刷新、添加适配器，加载数据等功能
            super.initLoad(mPullToRefreshListView, adapter);
        }
    }
    
  //2016-9-19 修改记录 将removeCallBack放到onStop()中进行
    @Override
    public void onStop() {
    	super.onStop();
    	Controller.getInstance().removeCallback(callBack);
    }
}
