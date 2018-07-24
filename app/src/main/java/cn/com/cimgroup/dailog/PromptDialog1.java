package cn.com.cimgroup.dailog;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import cn.com.cimgroup.R;
import android.R.color;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView.FindListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PromptDialog1 {
	
	private Dialog mImageDialog = null;
	private Context context;
	
	/**广告的关闭按钮**/
	private ImageView mCancleView;
	/**广告的显示控件**/
	private ImageView mPictureView;
	/**取消按钮**/
	private TextView mCanlBtnView;
	/**确定按钮**/
	private TextView mOkBtnView;
	/**ImageLoader的初始化**/
	private ImageLoader imageLoader;
	/**ImageLoader的参数设置**/
	private DisplayImageOptions options;
	
	public PromptDialog1(Context context) {
		
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		initImageOptions();
		initDialogView();
		
	}

	void initDialogView() {
		
		mImageDialog = new Dialog(context, R.style.prompt_dialog);
		View view = View.inflate(context, R.layout.dialog_prompt_1, null);
		mCancleView = (ImageView) view.findViewById(R.id.imageView_prompt_dialog1_calcle);
		mPictureView = (ImageView) view.findViewById(R.id.imageView_prompt_dialog1_picture);
		
		//加载图片不需要确认取消的按钮
//		mCanlBtnView = (TextView) view.findViewById(R.id.textView_prompt_dialog1_cancel);
//		mOkBtnView = (TextView) view.findViewById(R.id.textView_prompt_dialog1_ok);
//		mCanlBtnView.setVisibility(View.GONE);
//		mOkBtnView.setVisibility(View.GONE);
		mImageDialog.setContentView(view);
		mImageDialog.setCancelable(true);
		mImageDialog.setCanceledOnTouchOutside(true);
		
		mCancleView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mImageDialog.dismiss();
			}
		});
		
	}
 	
	/**
	 * 确认按钮的点击事件
	 * @param textName
	 * @param listener
	 */
	public void setPositvieButton(String textName,OnClickListener listener){
		
		mCanlBtnView.setText(textName);
		mCanlBtnView.setOnClickListener(listener);
		mImageDialog.dismiss();
		
	}
	
	/**
	 * 取消按钮的点击事件
	 * @param textName
	 * @param listener
	 */
	public void setNegativeButton(String textName,OnClickListener listener){
		
		mOkBtnView.setText(textName);
		mOkBtnView.setOnClickListener(listener);
		mImageDialog.dismiss();
		
	}
	
	
	/**
	 * 关闭当前dialog
	 * @param listener
	 */
	public void cancel(OnClickListener listener){
		
		mCancleView.setOnClickListener(listener);
		mImageDialog.dismiss();
		
	} 
	
	/**
	 * 隐藏当前按钮
	 */
	public void hideDialog() {
		if (mImageDialog != null && mImageDialog.isShowing()) {
			mImageDialog.hide();
		}
	}
	
	/**
	 * 设置广告的图片
	 * @param url
	 */
	public void setImageView(String url){
		
//		url = "http://192.168.2.181:18082/upload/images/20160119112051/2-1.jpg";
//		mPictureView.seti
		imageLoader.displayImage(url, mPictureView, options, new SimpleImageLoadingListener(){

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				super.onLoadingCancelled(imageUri, view);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				super.onLoadingFailed(imageUri, view, failReason);
			}

			@Override
			public void onLoadingStarted(String imageUri, View view) {
				super.onLoadingStarted(imageUri, view);
			}
		});
	}
	
	public void showDialog(){
		mImageDialog.show();
	}
	
	/**
	 * 设置dialog的大小
	 * @param p
	 */
	public void setDialogSize(WindowManager.LayoutParams p){
		
		Window dialogWindow = mImageDialog.getWindow();
		dialogWindow.setAttributes(p);
		
	}
	
	private void initImageOptions() {
		options = new DisplayImageOptions.Builder()
					.bitmapConfig(Config.RGB_565)
					.imageScaleType(ImageScaleType.EXACTLY)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.build();
	}
	
	

}
