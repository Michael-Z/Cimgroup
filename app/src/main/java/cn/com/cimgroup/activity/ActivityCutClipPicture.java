package cn.com.cimgroup.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.R;
import cn.com.cimgroup.view.cutpicture.ClipImageLayout;
import cn.com.cimgroup.xutils.BitmapUtils;

/**
 * 图片裁剪
 */
public class ActivityCutClipPicture extends BaseActivity implements
		OnClickListener {
	/** 保存按钮 */
	private TextView save;
	/** 返回 */
	private RelativeLayout id_back;
	/** 取消 */
	private TextView cancle;
	
	

	private ClipImageLayout clipImage;

	private Bitmap bitmap;
	private String mPath;
	public int screenWidth = 0;
	public int screenHeight = 0;
	/** 裁剪图片地址 */
	public String filename = Environment.getExternalStorageDirectory()
			.getPath() + "/my-java1.png";
	/** 压缩后图片地址 */
	private String outPath = Environment.getExternalStorageDirectory()
			.getPath() + "upLoad.png";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cut_clip_image);
		getWindowWH();
		initView();
		mPath = getIntent().getStringExtra("path");
		// 图片过大
		bitmap = createBitmap(mPath, screenWidth, screenHeight);
		int degreee = readBitmapDegree(mPath);
		if (bitmap != null) {
			if (degreee == 0) {
				// mClipImageLayout.setImageBitmap(bitmap);
			} else {
				bitmap = rotateBitmap(degreee, bitmap);
			}
		} else {
			finish();
		}

		// bitmap=BitmapFactory.decodeFile(mPath);
		clipImage.initLayout(bitmap, screenWidth);

	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		clipImage = (ClipImageLayout) findViewById(R.id.id_clipimage_layout);
		id_back = (RelativeLayout) findViewById(R.id.id_back);
		save = (TextView) findViewById(R.id.save);
		cancle = (TextView) findViewById(R.id.cancle);
		id_back.setOnClickListener(this);
		cancle.setOnClickListener(this);
		save.setOnClickListener(this);
	}

	public Bitmap createBitmap(String path, int w, int h) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			// 这里是整个方法的关键，inJustDecodeBounds设为true时将不为图片分配内存。
			BitmapFactory.decodeFile(path, opts);
			int srcWidth = opts.outWidth;// 获取图片的原始宽度
			int srcHeight = opts.outHeight;// 获取图片原始高度
			int destWidth = 0;
			int destHeight = 0;
			// 缩放的比例
			double ratio = 0.0;
			if (srcWidth < w || srcHeight < h) {
				ratio = 0.0;
				destWidth = srcWidth;
				destHeight = srcHeight;
			} else if (srcWidth > srcHeight) {// 按比例计算缩放后的图片大小，maxLength是长或宽允许的最大长度
				ratio = (double) srcWidth / w;
				destWidth = w;
				destHeight = (int) (srcHeight / ratio);
			} else {
				ratio = (double) srcHeight / h;
				destHeight = h;
				destWidth = (int) (srcWidth / ratio);
			}
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 缩放的比例，缩放是很难按准备的比例进行缩放的，目前我只发现只能通过inSampleSize来进行缩放，其值表明缩放的倍数，SDK中建议其值是2的指数值
			newOpts.inSampleSize = (int) ratio + 1;
			// inJustDecodeBounds设为false表示把图片读进内存中
			newOpts.inJustDecodeBounds = false;
			// 设置大小，这个一般是不准确的，是以inSampleSize的为准，但是如果不设置却不能缩放
			newOpts.outHeight = destHeight;
			newOpts.outWidth = destWidth;
			// 获取缩放后图片
			return BitmapFactory.decodeFile(path, newOpts);
		} catch (Exception e) {
			return null;
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.save:
			BitmapUtils.compressImage(clipImage.clip(), filename, 15);
			Intent intent = new Intent();
			intent.putExtra("path", filename);
			setResult(Activity.RESULT_OK, intent);
			finish();
			break;
		case R.id.id_back:
		case R.id.cancle:
			finish();
			break;
		}

	}

	/**
	 * 保存bitmap对象到本地
	 * 
	 * @param bitmap
	 */
	public void savaBitmap(Bitmap bitmap) {
		// String name="upLoad.png";
		BitmapUtils.compressImage(bitmap, outPath, 10);
		File f = new File(outPath);
		FileOutputStream fOut = null;
		try {
			f.createNewFile();
			fOut = new FileOutputStream(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取屏幕的高和宽
	 */
	private void getWindowWH() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
	}

	// 旋转图片
	private Bitmap rotateBitmap(int angle, Bitmap bitmap) {
		// 旋转图片 动作
		Matrix matrix = new Matrix();
		matrix.postRotate(angle);
		// 创建新的图片
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
				bitmap.getWidth(), bitmap.getHeight(), matrix, false);
		return resizedBitmap;
	}

	// 读取图像的旋转度
	private int readBitmapDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}
}
