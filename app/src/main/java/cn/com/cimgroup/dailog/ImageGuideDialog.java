package cn.com.cimgroup.dailog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import cn.com.cimgroup.R;

/**
 * 图片指引Dialog
 * 
 * @author 秋风
 * 
 */
public class ImageGuideDialog extends BaseDialog implements
		View.OnClickListener {
	private View mView;

	private Context mContext;
	/** 标签 */
	private String mTag = "";

	public ImageGuideDialog(Context context) {
		super(context);
		mContext = context;
		mView = View.inflate(context, R.layout.dialog_image_guide, null);
		initView();
		initEvent();
		setContentView(mView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	public ImageGuideDialog(Context context, String tag) {
		super(context);
		mContext = context;
		mView = View.inflate(context, R.layout.dialog_image_guide, null);
		mTag = tag;
		initView();
		initEvent();
		setContentView(mView, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
	}

	/**
	 * 初始化Dialog
	 */
	private void initView() {
	}

	private void initEvent() {
		mView.findViewById(R.id.id_jump).setOnClickListener(this);
		mView.findViewById(R.id.id_cancel).setOnClickListener(this);
		mView.findViewById(R.id.id_parent_layout).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_jump:
			hide();
			if (mListener!=null) {
				mListener.commit();
			}
			break;
		case R.id.id_cancel:
			hide();
			if (mListener!=null) {
				mListener.cancel();
			}
			break;
		case R.id.id_parent_layout:
			break;

		default:
			break;
		}
	}
	/**操作监听*/
	private GuideDialogListener mListener;

	public void setOnDialogListener(GuideDialogListener mListener) {
		this.mListener = mListener;
	}

	/**
	 * 行为监听
	 * 
	 * @author 秋风
	 * 
	 */
	public interface GuideDialogListener {
		void commit();

		void cancel();
	}

}
