package cn.com.cimgroup.util.thirdSDK;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;

import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ShareUtils {

	private IWXAPI api;
	private static String appId = "wx12250ad177d4b087";

	/** 腾讯Api */
	private Tencent mTencent;

	// private static IWeiboShareAPI mWeiboShareAPI;

	private Context mContext;

	/**
	 * 初始化Api
	 */
	public void initApi(Context context) {
		mContext = context;
		// 初始化腾讯API
		// if (mTencent != null) {
		mTencent = Tencent.createInstance("1105471815", mContext);
		// }

		// 初始化微信
		// if (api != null) {
		api = WXAPIFactory.createWXAPI(mContext, appId, false);
		api.registerApp(appId);
		// }

		// if (mWeiboShareAPI != null) {
		// mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(mContext, "");
		// // 将应用注册到微博客户端
		// mWeiboShareAPI.registerApp();
		//
		// }
	}

	private IWeiboHandler mWBHandler = new IWeiboHandler() {

	};

	// IUiListener qqShareLis = new IUiListener() {
	// @Override
	// public void onComplete(Object response) {
	// Toast.makeText(mContext, "success",
	// Toast.LENGTH_LONG).show();
	// }
	//
	// @Override
	// public void onError(UiError e) {
	// Toast.makeText(mContext, "onError",
	// Toast.LENGTH_LONG).show();
	// }
	//
	// @Override
	// public void onCancel() {
	// Toast.makeText(mContext, "onCancel",
	// Toast.LENGTH_LONG).show();
	// }
	// };

	/**
	 * 分享到QQ好友
	 */
	public void shareToQQ(String title, String summary, String url,
			String imageUrl, IUiListener qqShareListener) {
		final Bundle params = new Bundle();
		params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE,
				QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
		params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
		params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
		mTencent.shareToQQ((Activity) mContext, params, qqShareListener);
	}

	/**
	 * 分享到QQ空间
	 */
	public void shareToQQzone(String title, String url, String imageUrl,
			IUiListener qqShareListener) {
		final Bundle params = new Bundle();
		params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE,
				QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
		params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
		params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
		ArrayList<String> imageUrls = new ArrayList<String>();
		imageUrls.add(imageUrl);
		// imageUrls.add("http://www.opensnap.com.cn/images/300x250.jpg");
		params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
		mTencent.shareToQzone((Activity) mContext, params, qqShareListener);
	}

	/**
	 * 分享到微信（好友、朋友圈）
	 * 
	 * @param title
	 *			:分享标题
	 * @param desc
	 *			:分享描述
	 * @param mitmap
	 *			:分享图片
	 * @param sendType
	 *			：0为分享到微信好友，1为分享到微信朋友圈
	 */
	public void shareToWX(String title, String desc, String url, Bitmap mitmap,
			int sendType) {

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = desc;
		// Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(),
		// R.drawable.icon_hall_11x5);
		// msg.thumbData = bmpToByteArray(bm);
		msg.thumbData = bmpToByteArray(mitmap);
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "webpage" + System.currentTimeMillis();
		req.message = msg;
		req.scene = sendType == 0 ? SendMessageToWX.Req.WXSceneTimeline
				: SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}
	
	/**
	 * 
	 * @Description:
	 * 分享到微信（好友、朋友圈）
	 * 
	 * @param title
	 *			:分享标题
	 * @param desc
	 *			:分享描述
	 * @param imageUrl
	 *			:分享图片url
	 * @param sendType
	 *			：0为分享到微信好友，1为分享到微信朋友圈
	 * @author:www.wenchuang.com
	 * @date:2016-12-20
	 */
	public void shareToWX(String title, String desc, String url, String imageUrl,
			int sendType) {

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;
		final WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = desc;
		// Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(),
		// R.drawable.icon_hall_11x5);
		// msg.thumbData = bmpToByteArray(bm);
//		imageUrl = "http://img.ui.cn/data/file/1/3/3/810331.jpg";
		Bitmap bitmap = GetLocalOrNetBitmap(imageUrl);
		Bitmap thumb =Bitmap.createScaledBitmap(bitmap, 100, 100, true);//压缩Bitmap
		msg.thumbData = bmpToByteArray(thumb);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = "webpage" + System.currentTimeMillis();
		req.message = msg;
		req.scene = sendType == 0 ? SendMessageToWX.Req.WXSceneTimeline
				: SendMessageToWX.Req.WXSceneSession;
		api.sendReq(req);
	}

	/**
	 * 将Bitmap转换成字节数组
	 * 
	 * @param bmp
	 * @return
	 */
	private byte[] bmpToByteArray(Bitmap bmp) {
		byte[] result = null;
		ByteArrayOutputStream output = new ByteArrayOutputStream();// 初始化一个流对象
		bmp.compress(CompressFormat.PNG, 100, output);// 把bitmap100%高质量压缩 到
														// output对象里
		bmp.recycle();// 自由选择是否进行回收
		result = output.toByteArray();// 转换成功了
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public Bitmap getBitMBitmap(String urlpath) {  
		 Bitmap map = null;  
		 try {  
		  URL url = new URL(urlpath);  
		  URLConnection conn = url.openConnection();  
		  conn.connect();  
		  InputStream in;  
		  in = conn.getInputStream();  
		  map = BitmapFactory.decodeStream(in);  
		  } catch (IOException e) {  
		  e.printStackTrace();  
		 }  
		 return map;  
		} 
	
	public Bitmap createBitmapThumbnail(Bitmap bitMap) {  
		int width = bitMap.getWidth();  
		int height = bitMap.getHeight();  
		// 设置想要的大小  
		int newWidth = 99;  
		int newHeight = 99;  
		// 计算缩放比例  
		float scaleWidth = ((float) newWidth) / width;  
		float scaleHeight = ((float) newHeight) / height;  
		// 取得想要缩放的matrix参数  
		Matrix matrix = new Matrix();  
		matrix.postScale(scaleWidth, scaleHeight);  
		// 得到新的图片  
		Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,  
				matrix, true);  
		return newBitMap;  
	} 
	
	/**
	 * 把网络资源图片转化成bitmap
	 * @param url  网络资源图片
	 * @return  Bitmap
	 */
	public Bitmap GetLocalOrNetBitmap(String url) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(new URL(url).openStream(), 1024);
			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, 1024);
			copy(in, out);
			out.flush();
			byte[] data = dataStream.toByteArray();
			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			data = null;
			return bitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[1024];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}
}
