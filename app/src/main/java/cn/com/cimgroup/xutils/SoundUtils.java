package cn.com.cimgroup.xutils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.com.cimgroup.bean.GetCode;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

/**
 * 声音播放工具类
 * 
 * @author 秋风
 * 
 */
public class SoundUtils {
	private static MediaPlayer mPlayer;

	/**
	 * 开始播放音频（raw）
	 * 
	 * @param context
	 * @param url
	 */
	public static void startSoundFromResource(Context context, int soundId) {
		try {
			mPlayer = MediaPlayer.create(context, soundId);
			if (mPlayer.isPlaying()) {
				mPlayer.stop();
				mPlayer.release();
				mPlayer = MediaPlayer.create(context, soundId);
			}
//			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} 
	}

	public static void startSoundFromAssest(Context context, String url) {
		AssetManager assetManager = context.getAssets();
		try {
			AssetFileDescriptor fileDescriptor = assetManager.openFd(url);
			// if (mPlayer==null) {
			// mPlayer = new MediaPlayer();
			// }
			// if (mPlayer.isPlaying()) {
			// mPlayer.stop();
			// }
			mPlayer = new MediaPlayer();
			mPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
					fileDescriptor.getStartOffset(), fileDescriptor.getLength());
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	private static SoundPool mSoundPool=null;
	/**
	 * 播放声音
	 * 
	 * @param context
	 * @param soundId
	 */
	public static void playSound(Context context, int soundId) {
		SoundPool soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		int id= soundPool.load(context, soundId, 1);
		soundPool.play(id, 1, 1, 0, 0, 1);
	}
	
	private static Map<Integer, Integer> mSoundIdsMap;
	/**
	 * 设置音效源
	 * @param soundIdsMap
	 */
	public static void setIds(Map<Integer, Integer> soundIdsMap){
		if (mSoundIdsMap==null) {
			mSoundIdsMap=new HashMap<Integer, Integer>();
		}else
			mSoundIdsMap.clear();
		if (soundIdsMap!=null) {
			mSoundIdsMap=soundIdsMap;
		}
	}
	
	public static SoundPool getInstance(){
		if (mSoundPool==null) {
			mSoundPool=new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		}
		return mSoundPool;
	}
	/**
	 * 播放音效
	 * @param soundId
	 */
	public static void playSound(int soundId){
		
		mSoundPool.play(mSoundIdsMap.get(soundId), 1,1, 0, 0, 1);
	}
	private static float volumnRatio;
	/**
	 * 播放音效
	 * @param soundId
	 */
	public static void playSound(int soundId,Context context){
		
		AudioManager am = (AudioManager)context.getSystemService(context.AUDIO_SERVICE); 
		// 设定调整音量为媒体音量,当暂停播放的时候调整音量就不会再默认调整铃声音量了，
		((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC); 
		// 获取最大音量值
		float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC); 
		// 不断获取当前的音量值
		float audioCurrentVolumn = am.getStreamVolume(AudioManager.STREAM_MUSIC); 
		//最终影响音量

		volumnRatio = audioCurrentVolumn/audioMaxVolumn; 


		mSoundPool.play(mSoundIdsMap.get(soundId), volumnRatio, volumnRatio, 0, 0, 1);
	}

}
