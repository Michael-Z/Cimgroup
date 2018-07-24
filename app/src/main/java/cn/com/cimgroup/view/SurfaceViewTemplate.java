package cn.com.cimgroup.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

import cn.com.cimgroup.R;

public class SurfaceViewTemplate extends SurfaceView implements Callback,
		Runnable {

	private SurfaceHolder mHolder;
	private Canvas mCanvas;

	/**
	 * 新线程，用于控制surfaceView的动画切换 surfaceView中，是在子线程中绘制控件，而不是在ondraw（）方法中
	 */
	private Thread t;

	/**
	 * 线程的控制开关
	 */
	private boolean isRunning;

	// 获奖描述
	private String[] mStrs = new String[] { "3", "4", " ", "5",
			"6", "7","8"," ","1","2" };
	// 获奖图片
	private int[] mImages = new int[] { R.drawable.win,R.drawable.win };
	// Image转化为Bitmap
	private Bitmap[] mImageBitmaps;
	// 设置背景颜色
	private int[] colors = new int[] { 0xFFf83c3c, 0xFF9532fa, 0xFF3899f3,
			0xFFf83c3c, 0xFF9532fa, 0xFFfad029 ,0xFF3899f3
			, 0xFFf83c3c ,0xFF9532fa, 0xFFfad029 };
	// 设置第二层背景的渐变色（红色）
	private int[] bgRedColors = new int[]{0xffcb3130 ,0xffb02726,0xff861716};
	// 设置第三层背景的渐变色（黄色）
	private int[] bgYellowColors = new int[]{0xffffeeac , 0xffffdb85 ,0xfffdce32};

	private int mItemCount = 10;
	// 设置外层灯泡的数量
	private int mOutsideBulbsCount = 24;
	// 设置内层灯泡的数量
	private int mInsideBulbsCount = 10;
	// 设置外层灯泡的图片
	private Bitmap outsideBulbBitmapDark;
	private Bitmap outsideBulbBitmapLBright;
	private Bitmap mBitmap;
	//设置内层的灯泡图片
	private Bitmap insideBulbBitmapDark;
	private Bitmap insideBulbBitmapBright;
	
	// 整个盘块的范围
	private RectF mRange = new RectF();
	// 整个盘块的直径
	private int mRadius;

	// 绘制圆盘的画笔
	private Paint mArcPaint;
	// 绘制文本的画笔
	private Paint mTextPaint;
	// 绘制最底层背景的画笔
	private Paint mBgPaint;
	// 绘制第二层背景的画笔
	private Paint mBgPaint2;
	// 普通的画笔，在drawBitmap的时候使用
	private Paint mSimplePaint;
	// 阴影用画笔
	private Paint mShadowPaint;
	// 转动的速度
	private double mSpeed = 0;
	// 开始角度 设置volatile是为了是变量在线程中透明
	private volatile float mStartAngle = 0;
	// 用户点击了转盘停止的标志
	private boolean isShouldEnd = false;
	// 转盘中心
	private int mCenter;
	// 设置圆盘的padding值 以paddingLeft为准
	private int mPadding;
	// 背景的加载
	private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.luckypan_iv_bg_o);
	//开始按钮的加载
	private Bitmap mBtnBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.luckypan_button);
	//开始按钮的画笔
	private Paint mBtnPaint;
	// 字体的大小
	private float mTextSize = TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 25, getResources()
					.getDisplayMetrics());
	
	private boolean mFlag = false;
	
	private Handler mHandler = new Handler(){
		
	};
	
	private boolean shine;
	
	public SurfaceViewTemplate(Context context, AttributeSet attrs) {
		super(context, attrs);

		mHolder = getHolder();
		// 增加的callback都类似于接口回调
		mHolder.addCallback(this);

		// 设置可点击和可获取焦点
		setFocusable(true);
		setFocusableInTouchMode(true);

		// 设置常亮
		setKeepScreenOn(true);
	}

	public SurfaceViewTemplate(Context context) {
		// 修改方法本身继承的父类方法，改为调用两个参的构造方法；
		this(context, null);
		// super(context);
	}
	
	//将
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		int width = Math.min(getMeasuredHeight(), getMeasuredWidth());
		
		int bgWidth = mBgBitmap.getWidth();
		int bgHeight = mBgBitmap.getHeight();
		
		
		//左端边距
//		mPadding = getPaddingLeft();
		mPadding = max(getPaddingLeft(), getPaddingRight(), getPaddingBottom(), getPaddingTop());
		//直径
		mRadius = bgWidth - mPadding*4;
		//中心点
		mCenter = width/2;
		
		//将整个游戏的背景设置为方形
		setMeasuredDimension(width, width);
		
	}

	private int max(int ...params) {
		
		int size = params.length;
		int padding = params[0];
		for(int i=1; i<size; i++){
			if(padding < params[i]){
				padding = params[i];
			}
		}
		
		return padding;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		System.out.println("线程被创建了");
		//初始化绘制盘块的画笔(弧形)
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);
		mArcPaint.setDither(true);
		
		//初始化转盘文字的画笔
		mTextPaint = new Paint();
		mTextPaint.setColor(0xffffffff);
		mTextPaint.setTextSize(mTextSize);
		
		//初始化转盘开始按钮的画笔
		mBtnPaint = new Paint();
		mBtnPaint.setAntiAlias(true);
		mBtnPaint.setDither(true);
		
		//simple画笔
		mSimplePaint = new Paint();
		mSimplePaint.setAntiAlias(true);
		mSimplePaint.setDither(true);
		
		//初始化背景的绘制画笔
		mBgPaint = new Paint();
		mBgPaint.setAntiAlias(true);
		mBgPaint.setDither(true);
		mBgPaint.setColor(0xffffcc00);
		
		//初始化第二层背景的绘制画笔
		mBgPaint2 = new Paint();
		mBgPaint2.setAntiAlias(true);
		mBgPaint2.setDither(true);
		mBgPaint2.setColor(0xffee1d24);
		
		//初始化阴影层的画笔
		mShadowPaint = new Paint();
		mShadowPaint.setAntiAlias(true);
		mShadowPaint.setDither(true);
		
		//初始化盘块的范围
//		mRange = new RectF(getPaddingLeft(), getPaddingLeft(), getPaddingLeft()+mRadius, getPaddingLeft()+mRadius);
		int left = mCenter - mBgBitmap.getWidth()/2 + mPadding*3;
		int top = mCenter - mBgBitmap.getHeight()/2 + mPadding*3;
		int right = mCenter + mBgBitmap.getWidth()/2 - mPadding*3;
		int bottom = mCenter + mBgBitmap.getHeight()/2 - mPadding*3;
		mRange = new RectF(left,top,right,bottom);
		
		//初始化图片资源，将r.id.XXX转换为bitmap
		mImageBitmaps = new Bitmap[2];
		for(int i=0;i< 2;i++){
			mImageBitmaps[i] = BitmapFactory.decodeResource(getResources(), mImages[i]);	
		}
		
		// 开启线程
		isRunning = true;

		t = new Thread(this);
		t.start();

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
//		System.out.println("线程被改变");

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
//		System.out.println("线程已经被销毁");
		isRunning = false;
	}

	// 开启线程，进行ui的绘制
	@Override
	public void run() {

		while (isRunning) {
			
			long startTime = System.currentTimeMillis();
			draw();
			long endTime = System.currentTimeMillis();
			if(endTime - startTime < 50){
				try {
					t.sleep(50 - (endTime - startTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void draw() {

		try {
			mCanvas = mHolder.lockCanvas();
			if (mCanvas != null) {
				
				// do sth
				//绘制圆形背景
				drawBg();
				//绘制外层灯泡
				
//				if(mSpeed == 0){
					for(int i=0;i<mOutsideBulbsCount;i++){
						drawOutsideBlub(i,true);
						shine = true;
				}
//				}else if(mSpeed > 0){
//					Runnable runnable = new Runnable() {
//						
//						@Override
//						public void run() {
//							if(shine){
//								for(int i=0;i<mOutsideBulbsCount;i++){
//									drawOutsideBlub(i,false);
//									shine = false;
//								}
//							}else {
//								for(int i=0;i<mOutsideBulbsCount;i++){
//									drawOutsideBlub(i,true);
//									shine = true;
//								}
//							}
//							mHandler.postDelayed(this, 2000);
//						}
//					};
//					mHandler.postDelayed(runnable, 2000);
//				}
//				
//				if(mSpeed == 0){
//					mHandler.removeCallbacks(this);
//				}
				
				//绘制盘块
				float tempAngle = mStartAngle;
				float sweepAngle = 360/mItemCount;
				for(int i =0;i<mItemCount;i++){
					//每个中奖区域的绘制
					this.setLayerType(View.LAYER_TYPE_SOFTWARE,mArcPaint);//设置为SOFTWARE才会实现阴影
					mArcPaint.setShadowLayer(15, 1, 1, colors[i]);
					mArcPaint.setColor(colors[i]);
					mCanvas.drawArc(mRange, tempAngle, sweepAngle, true, mArcPaint);
					
					
					//绘制盘块上的图片
					//当i=2或者7的时候 显示中奖图片
					if(i == 2){
						drawIcon(tempAngle ,mImageBitmaps[0]);
					}else if (i == 7){
						drawIcon(tempAngle ,mImageBitmaps[1]);
					}else{
						//绘制盘块上的文本
						drawText(tempAngle,sweepAngle,mStrs[i]);
					}
					
					
					//绘制开始按钮
					drawBtnStart();
					
					
					tempAngle += sweepAngle;
				}
				
				//绘制内层灯泡
				for(int i=0;i<mInsideBulbsCount;i++){
					drawInsideBulb(i);
				}
				//为speed赋值 这可以使转盘旋转
					mStartAngle += mSpeed;
				
				//判断是否点击了停止按钮
				if(isShouldEnd){
					mSpeed -= 1;
				}
				//转盘停止
				//为了达到在指定位置停止，加入了luckyStart()方法控制速度
				//因此转盘停止时的速度是不确定的 要判断停止时间 需要进行计算
				//这里为了简化计算，当速度减小到接近0的时候 给用户弹出结果
				if(mSpeed >= 1 && mSpeed <=2){
					mListener.finish();
				}
				//速度小于0  此时圆盘停止
				if(mSpeed <= 0){
					mSpeed = 0;
					isShouldEnd = false;
				}
				
			}
		} catch (Exception e) {
		} finally {
			if (mCanvas != null) {
				mHolder.unlockCanvasAndPost(mCanvas);
			}
		}

	}

	//绘制最内层的灯泡
	private void drawInsideBulb(int i) {
		
		//初始化灯泡的图片
		insideBulbBitmapDark = BitmapFactory.decodeResource(getResources(), R.drawable.light_inside_dark);
		insideBulbBitmapBright = BitmapFactory.decodeResource(getResources(), R.drawable.light_inside_bright);
		
		//灯泡图片的宽度 是计算灯泡位置的依据
		float imageWidth = insideBulbBitmapDark.getWidth();
		
		//计算灯泡的位置
		//每个灯泡的弧度
		float startAngle = 0;
		float angle = (float) ((startAngle + i*(360/mInsideBulbsCount)) * Math.PI/180);
		
		//中间箭头控件的整个大小
		float width = mBtnBitmap.getWidth();
		//灯泡中心点的坐标位置
		float x = (float) (mCenter + (width/2/4*3)*Math.cos(angle));
		float y = (float) (mCenter + (width/2/4*3)*Math.sin(angle));
		
		mCanvas.drawBitmap(insideBulbBitmapBright, null, new RectF(x-imageWidth/2, y-imageWidth/2, x+imageWidth/2, y+imageWidth/2),mSimplePaint);
	}

	/**
	 * 绘制最外层的灯泡
	 */
	private void drawOutsideBlub(int n ,boolean flag) {
		//初始化灯泡图片
		outsideBulbBitmapDark = BitmapFactory.decodeResource(getResources(), R.drawable.light_outside_dark);
		outsideBulbBitmapLBright = BitmapFactory.decodeResource(getResources(), R.drawable.light_outside_bright);
		
		//灯泡图片的大小
		float imgWidth = outsideBulbBitmapDark.getWidth();
		//计算灯泡的位置
		//每个灯泡的弧度
		float startAngle = 0;
		float angle = (float) ((startAngle + n*(360/mOutsideBulbsCount)) * Math.PI/180);
		
		float x = (float) (mCenter + (mRadius/2+mPadding*2)*Math.cos(angle));
		float y = (float) (mCenter + (mRadius/2+mPadding*2)*Math.sin(angle));
		
		if(flag){
			mBitmap = outsideBulbBitmapLBright;
		}else{
			mBitmap = outsideBulbBitmapDark;
		}
		
		mCanvas.drawBitmap(mBitmap, null, new RectF(x-imgWidth/2, y-imgWidth/2, x+imgWidth/2, y+imgWidth/2), mSimplePaint);
		
	}

	//绘制开始按钮
	private void drawBtnStart() {
		
		int left = mCenter - mBtnBitmap.getWidth()/2;
		int top = mCenter - (mBtnBitmap.getHeight() - mBtnBitmap.getWidth()/2);
//		int right = mCenter + mRadius/2/3;
//		int bottom = mCenter + mRadius/2/3;
		int right = mCenter + mBtnBitmap.getWidth()/2;
		int bottom = mCenter + mBtnBitmap.getWidth()/2;
		mCanvas.drawBitmap(mBtnBitmap, null, new Rect(left,top,right,bottom), null);
	}

	/**
	 * 绘制盘块上的图片（中奖图）
	 * @param tempAngle
	 * @param i
	 */
	private void drawIcon(float tempAngle, Bitmap bitmap) {
		
		//设置图片宽度为直径的1/8
		int imageWidth = mRadius / 5;
//		int imageWidth = mImageBitmaps[0].getWidth();
		
		// 1度 = pi/180  360度 = 2pi弧度
		//确定图片的偏移弧度 用来计算偏移量
		float angle = (float) ((tempAngle + 360/mItemCount/2) * Math.PI/180);
		
		//图片中心x，y点的坐标，针对于左上角
		int x = (int) (mCenter + mRadius/2/1.35*Math.cos(angle));
		int y = (int) (mCenter + mRadius/2/1.35*Math.sin(angle));
		
		//竖直的偏移量是mRadius/2/3.5f;
		//确认图片的位置 将图片放入一个矩形框中
		//x，y 分别为图片中心点的坐标 而不是左上右下这四个点坐标 所以需要计算下
		Rect rect = new Rect(x - imageWidth/2, y - imageWidth/2, x + imageWidth/2, y+imageWidth/2);
		mCanvas.drawBitmap(bitmap, null,rect, null);
		
	}

	/**
	 * 绘制盘块上的内容（中奖 描述）
	 * @param tempAngle
	 * @param sweepAngle
	 * @param string
	 */
	private void drawText(float tempAngle, float sweepAngle, String string) {
		
		Path path = new Path();
		path.addArc(mRange, tempAngle, sweepAngle);
		
		
		//设置水平偏移量 对应drawTextOnPath（）中的参数3
		//计算 = 每个盘块弧长/2 - 文字弧长/2
		float textWidth = mTextPaint.measureText(string);
		int hOffset = (int) (mRadius * Math.PI /mItemCount /2 - textWidth/2);
		
		//设置竖直偏移量对应drawTextOnPath（）中的参数4 
		//大小自行设置 这里是半径的1/3
		float vOffset = mRadius/2/3.5f;
		
		mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
		
		
	}

	/**
	 * 绘制背景图 背景圆盘（实际情况下是带灯泡的图片）
	 * 背景的矩形的padding设计为自定义控件padding/2
	 */
	private void drawBg() {
		
		//这个颜色是控件背景的矩形的颜色
//		mCanvas.drawColor(0xffff0000);
//		mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding, mPadding, getMeasuredWidth()-mPadding, getMeasuredHeight()-mPadding), null);
		//这个颜色才是背景颜色
		//绘制最底层背景 颜色为土黄色
		mCanvas.drawCircle(mCenter, mCenter, mRadius/2+mPadding*4, mBgPaint);
		//绘制第二层背景 颜色为红色
		int scale = dip2px(getContext(), 2);
		LinearGradient shader = new LinearGradient(mCenter, mCenter-( mRadius/2+mPadding*4-scale), mCenter, mCenter+( mRadius/2+mPadding*4-scale)
				,bgRedColors, null, Shader.TileMode.REPEAT);
		mBgPaint2.setShader(shader);
		mCanvas.drawCircle(mCenter, mCenter, mRadius/2+mPadding*4-scale, mBgPaint2);
		//绘制第三层背景 颜色为土黄色 渐变
		int radius3 = (mBgBitmap.getWidth()/2-2*mPadding);
		LinearGradient shader1 = new LinearGradient(mCenter, mCenter-radius3, mCenter, mCenter+radius3, bgYellowColors, null, Shader.TileMode.REPEAT);
		mSimplePaint.setShader(shader1);
		mCanvas.drawCircle(mCenter, mCenter,radius3 , mSimplePaint);
		//绘制最后一层背景 为灰色 为了模拟阴影效果
//		int width = dip2px(getContext(), 1f);
//		mShadowPaint.setColor(0xff8B5742);
//		mCanvas.drawCircle(mCenter, mCenter, (mBgBitmap.getWidth()-6*mPadding)/2+width, mShadowPaint);
		
	}
	
	/**
	 * 点击启动旋转
	 */
	public void luckyStart(int index){
		
		//计算每一项的角度
		float angle = 360/mItemCount;
		
		//计算每个格子的中奖范围
		//0->234~270  270-36*(i+1) ~ 270 - i*36
		//1->198~234
//		float from = 210 - index *angle;
//		float end = from + angle;
		float from = 270-angle*(index+1);
		float end = from + angle;
		
		//设置用户点击停止 转盘还需要转动的距离
		//设置前边的n*360是自定义的 
		float targetFrom = 6 *360 + from;
		float targetEnd = 6 *360 + end;
		
		/**
		 * <pre>
		 * 
		 * v1是一个等差数列 每次递减1 知道速度为0
		 * 这个公式里的+1 是为了将float数转换为int数 
		 * (v1+0) *(v1+1) /2 = targetForm;
		 * 由一元二次方程得停止的速度
		 * v1 = (-1 + Math.sqrt(1+ 8*targetForm))/2
		 * <pre>
		 */
		
		float v1 = (float) ((-1 + Math.sqrt(1+ 8*targetFrom))/2);
		float v2 = (float) ((-1 + Math.sqrt(1+ 8*targetEnd))/2);
		
		mSpeed = v1 + Math.random() *(v2 - v1);
//		mSpeed = v2;
		
//		mSpeed = 5;
		isShouldEnd = false;
		
	}
	
	/**
	 * 点击了停止旋转
	 */
	public void luckyEnd(){
		mStartAngle = 0;
		isShouldEnd = true;
	}
	
	/**
	 * 判断转盘是否已经停止了旋转
	 * 这里之所以不用isShouldEnd来判断 是因为用户点击了停止之后 转盘还要自己转动一段时间
	 * @return
	 */
	public boolean isStart(){
		return mSpeed != 0;
	}
	
	/**
	 * 获取停止按钮是否按下的状态
	 * @return
	 */
	public boolean isEndPressed(){
		return isShouldEnd;
	}
	
	/**
     * convert dp to px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    
    /**
     * 定义一个接口 用于转盘完成的接口回调
     * @author leo
     *
     */
    public interface onLuckyPanStopListener{
    	public void finish();
    }
    //初始化这个接口
    public onLuckyPanStopListener mListener;
    
    public void setOnLuckyPanStopListener(onLuckyPanStopListener listener){
    	if (listener != null) {
        	this.mListener = listener;
		}
    }

}
