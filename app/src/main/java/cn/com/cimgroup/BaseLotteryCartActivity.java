package cn.com.cimgroup;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.view.FlowLayout;

/**
 * 数字彩购物车每条数据 ITEM处理基类
 * @Description:
 * @author:zhangjf 
 * @copyright www.wenchuang.com
 * @Date:2015年10月21日
 */
public class BaseLotteryCartActivity extends BaseActivity {

	protected int textSize = 18;
	protected CartLayout mClickCartLayout;
	protected int tzTotalNum = 0; // 投注总注数
	protected int tzTotalMoney = 0; // 投注总钱数
	protected int itemClickNum = 0; // 某一条的注数
	protected int itemClickMoney = 0;// 某一条的钱数
	private int keepNum = 0; // 记录 某一条的注数
	private int keepMoney = 0;// 记录某一条的钱数
	protected List<CartLayout> itemDataList = new ArrayList<CartLayout>();

	protected void initKeep() {
		keepNum = 0;
		keepMoney = 0;
	}

	protected void initNumMoney(int itemNum, int itemMoney) {
		tzTotalNum -= keepNum;
		tzTotalMoney -= keepMoney;
		tzTotalNum += itemNum;
		tzTotalMoney += itemMoney;
	}

	/**
	 * 初始化每个购物车item
	 * @Description:
	 * @param mLLCart
	 * @return
	 * @author:www.wenchuang.com
	 * @date:2016年1月20日
	 */
	protected CartLayout initSetLayout(LinearLayout mLLCart) {
		View cartLayout = View.inflate(mActivity, R.layout.item_lottery_cart, null);
		CartLayout cartHolder = new CartLayout(cartLayout, mLLCart);
		itemDataList.add(cartHolder);
		cartHolder.mTVCartDel.setTag(cartLayout);
		cartLayout.setTag(cartHolder);
		mLLCart.addView(cartLayout, 0);
		return cartHolder;
	}

	protected void initSetTag(CartLayout cartLayout, int itemNum, int itemMoney) {
		initNumMoney(itemNum, itemMoney);

		cartLayout.mTVCartDel.setTag(R.id.tag_item_num, itemNum);
		cartLayout.mTVCartDel.setTag(R.id.tag_item_money, itemMoney);
		cartLayout.mRLCart.setTag(R.id.tag_item_num, itemNum);
		cartLayout.mRLCart.setTag(R.id.tag_item_money, itemMoney);
		// cartLayout.mTVCartMoney.setText(mActivity.getResources().getString(R.string.item_cart_num_money,
		// itemNum, itemMoney));
		cartLayout.updataLayout();
	}

	/**
	 * 购物车 每一条的数据及处理
	 */
	public class CartLayout implements OnClickListener {

		public LinearLayout mRLCart;
		public ImageView mTVCartDel;
		public FlowLayout mFLCartNum;
		public TextView mTVCartType;
		public TextView mTVCartMoney;
		private LinearLayout mLLCart;
		public boolean isZhuiAdd = false;
		private int money;

		/**
		 * 更新页面显示的数据
		 * @Description:
		 * @author:www.wenchuang.com
		 * @date:2016年1月20日
		 */
		public void updataLayout() {
			int num = (Integer) mTVCartDel.getTag(R.id.tag_item_num);
			money = isZhuiAdd ? num * 3 : num * 2;
			mRLCart.setTag(R.id.tag_item_money, money);
			mTVCartMoney.setText(mActivity.getResources().getString(R.string.item_cart_num_money, num, money));
//			isZhuiAdd = !isZhuiAdd;
		}

		public CartLayout(View view, LinearLayout mLLCart) {
			this.mLLCart = mLLCart;
			mRLCart = (LinearLayout) view.findViewById(R.id.item_cart_rl);
			mTVCartDel = (ImageView) view.findViewById(R.id.item_cart_del);
			mFLCartNum = (FlowLayout) view.findViewById(R.id.item_cart_num_fl);
			mTVCartType = (TextView) view.findViewById(R.id.item_cart_type);
			mTVCartMoney = (TextView) view.findViewById(R.id.item_cart_money);
			mRLCart.setOnClickListener(this);
			mTVCartDel.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.item_cart_del:
				View view = (View) v.getTag();
				mLLCart.removeView(view);
				itemDataList.remove(this);
				int num = (Integer) v.getTag(R.id.tag_item_num);
				int money = (Integer) v.getTag(R.id.tag_item_money);
				tzTotalNum -= num;
				tzTotalMoney -= money;
				getTotalNumMoney();
				notifyCart();
				break;
			case R.id.item_cart_rl:
				mClickCartLayout = this;
				keepNum = (Integer) v.getTag(R.id.tag_item_num);
				keepMoney = (Integer) v.getTag(R.id.tag_item_money);
				itemCartClick(v);
				break;
			}
		}

		public int getItemMoney() {
			return money;
		}
	}

	protected void itemCartClick(View v) {
	}

	protected void notifyCart() {
	}

	// 获取注数 跟倍数 OR 期数跟总钱数
	protected void getTotalNumMoney() {
	}

	protected String getBuyTypeTitle(int buyType) {
		String buyTypeStr = "";
//		if (buyType == 0) {
//			buyTypeStr = getResources().getString(R.string.lotterybid_cart_dg);
//		} else if (buyType == 1) {
//			buyTypeStr = getResources().getString(R.string.lotterybid_cart_zh);
//		} else if (buyType == 2) {
//			buyTypeStr = getResources().getString(R.string.lotterybid_cart_hm);
//		}
		return buyTypeStr;
	}
}
