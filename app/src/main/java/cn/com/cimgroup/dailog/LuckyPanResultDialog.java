package cn.com.cimgroup.dailog;

import cn.com.cimgroup.GlobalConstants;
import cn.com.cimgroup.R;
import cn.com.cimgroup.activity.MainGameActivity;
import cn.com.cimgroup.logic.CallBack;
import cn.com.cimgroup.logic.Controller;
import cn.com.cimgroup.util.thirdSDK.ShareUtils;
import cn.com.cimgroup.xutils.ToastUtil;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class LuckyPanResultDialog extends BaseDialog implements View.OnClickListener{
	
	/**
	 * dialog的上下文 
	 */
	private Context context;
	
	/**
	 * dialog的布局
	 */
	private View mView;
	
	/**
	 * 标志位，标识用户输或者赢
	 */
	private int winOrLose;
	private int winAmount;
	
	private ImageView iv_result;
	private TextView tv_explain;
	private TextView tv_play_again;
	private ImageView iv_share;
	private ImageView iv_try_again;
//	private LinearLayout tv_win_try_again;
//	private TextView tv_lost_try_again;
	
	private ShareUtils shareUtils = new ShareUtils();
	

	public LuckyPanResultDialog(Context context) {
		super(context);
		
		this.context = context;
		mView = View.inflate(context,R.layout.dialog_luckypan_result, null);
		initView();
		initEvent();
		setContentView(mView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
	}
	
	/**
	 * 初始化点击，监听等事件
	 */
	private void initEvent() {
		iv_share.setOnClickListener(this);
		iv_try_again.setOnClickListener(this);
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		
		iv_result = (ImageView) mView.findViewById(R.id.iv_result);
		tv_explain = (TextView) mView.findViewById(R.id.tv_explain);
		tv_play_again = (TextView) mView.findViewById(R.id.tv_play_again);
		iv_share = (ImageView) mView.findViewById(R.id.iv_share);
		iv_try_again = (ImageView) mView.findViewById(R.id.iv_try_again);
//		tv_win_try_again = (LinearLayout) mView.findViewById(R.id.tv_win_try_again);
//		tv_lost_try_again = (TextView) mView.findViewById(R.id.tv_lost_try_again);
	}

	/**
	 * 展示dialog
	 * 参数：1-赢     0-输
	 * @param result
	 */
	public void showDialog(int result, int winAmount){
		this.winOrLose = result;
		this.winAmount = winAmount;
		reLoadView();
		show();
	}

	/**
	 * 根据用户的输赢 重新加载dialog的内容显示
	 */
	private void reLoadView() {
		if(winOrLose == 1){
			iv_result.setImageResource(R.drawable.win);
//			tv_lost_try_again.setVisibility(View.GONE);
//			tv_win_try_again.setVisibility(View.VISIBLE);
			tv_play_again.setText("获得："+winAmount*2+"乐米");
			tv_explain.setText("恭喜您，中奖了！！");
		}else {
			iv_result.setImageResource(R.drawable.luckypan_result_fail);
			tv_play_again.setText("再来一次吧？");
//			tv_win_try_again.setVisibility(View.GONE);
//			tv_lost_try_again.setVisibility(View.VISIBLE);
			tv_explain.setText("真遗憾！大奖飞走 ~");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_share:
			//点击分享
			shareUtils.initApi(context);
			Controller.getInstance().share(GlobalConstants.NUM_SHARE, "0", "1", mBack);
			
			break;
		case R.id.iv_try_again:
			//点击确定 
			mListener.updateIntegralAcnt();
			dismiss();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 用户还要再嗨一次的接口
	 * @author leo
	 *
	 */
	public interface  onTryAgainPressedListener{
		public void updateIntegralAcnt();
	}
	
	private onTryAgainPressedListener mListener;
	
	public void setOnTryAgainPressedListener(onTryAgainPressedListener listener){
		this.mListener = listener;
	}
	
	/**
	 * 接口回调
	 * 用于微信朋友圈的分享
	 */
	private CallBack mBack = new CallBack() {
		
		public void shareSuccess(cn.com.cimgroup.bean.ShareObj obj) {
			
			if(obj != null && obj.getResCode() != null && !TextUtils.isEmpty(obj.getResCode())){
				if(obj.getResCode().equals("0")){
					Bitmap bm = BitmapFactory.decodeResource(context.getResources(), R.drawable.e_ui_game_share);
					shareUtils.shareToWX(obj.getTitle(), obj.getContent(), obj.getUrl(), bm, 0 );
				}
			}
			
		};
		public void shareFailure(String error) {
			ToastUtil.shortToast(context, error);
		};
		
	};
	
}
