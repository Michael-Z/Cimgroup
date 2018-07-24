package cn.com.cimgroup.xutils;
/**
 * 可以用来计算某一任务花费的时间
 * @Description:
 * @author:zhangjf  
 * @see:   
 * @since:      
 * @copyright www.wenchuang.com
 * @Date:2013-7-24
 */
public class Tick {

	private long start = 0;
	private long end = 0;
	
	public Tick() {
		
	}
	public Tick(boolean autoBegin) {
		if(autoBegin) {
			begin();
		}
	}
	
	/**
	 * 开始计时
	 * @Description:
	 * @return  当前时间戳, 毫秒
	 * @see: 
	 * @since: 
	 * @author: yangentao
	 * @date:2012-7-6
	 */
	 
	public long begin() {
		start = System.currentTimeMillis();
		return start;
	}
	
	/**
	 * 停止计时
	 * @Description:
	 * @return 从开始到结束的时间, 单位毫秒
	 * @see: 
	 * @since: 
	 * @author: yangentao
	 * @date:2012-7-5
	 */
	 
	public long end() {
		end = System.currentTimeMillis();
		return end - start;
	}
	
	 
	 
	/**
	 * 结束计时
	 * @Description:
	 * @param prefix  打印信息
	 * @return 
	 * @see: 
	 * @since: 
	 * @author: yangentao
	 * @date:2012-7-6
	 */
	public long end(String prefix) {
		
		long tick = end();
		if(prefix !=null) {
			XLog.d(Thread.currentThread().getStackTrace()[1].getMethodName(),"[TimeTick] " + prefix + ": " + tick);
		}
		return tick;
	}

	/**
	 * 返回开始和结束的时间差
	 * @Description:
	 * @return
	 * @see: 
	 * @since: 
	 * @author: yangentao
	 * @date:2012-7-6
	 */
	 
	public long getTick() {
		return end - start;
	}
	
	/**
	 * 开始计时
	 * @Description:
	 * @return 计时器
	 * @see: 
	 * @since: 
	 * @author: yangentao
	 * @date:2012-7-6
	 */
	public static Tick go() {
		Tick t = new Tick(true);
		return t;
	}
	
}
