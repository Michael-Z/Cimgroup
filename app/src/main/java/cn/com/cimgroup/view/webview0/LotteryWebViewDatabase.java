package cn.com.cimgroup.view.webview0;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * lotteryWebviwe操作数据库 li、用于对webview的本地文件状态进行保存操作
 * 
 * @Description:
 * @author:zhangjf
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2014-12-8
 */
public class LotteryWebViewDatabase {

	// log tag
	protected static final String LOGTAG = "LotteryWebViewDatabase";
	// 数据库名称
	private static final String DATABASE_FILE = "lottery_webviewCache.db";
	private static SQLiteDatabase mCacheDatabase = null;
	// 采用单利模式操作此数据库
	private static LotteryWebViewDatabase mInstance;
	/** 当前数据库版本 **/
	private static final int CACHE_DATABASE_VERSION = 4;
	private static final int DATABASE_VERSION = 10;

	// 表列
	private static final String table_name = "webview_operation";

	private static final String _id = "_id";

	private static final String version = "version";

	private static final String filename = "filename";

	private static final String downUrl = "downUrl";

	private static final String datatime = "datatime";

	private static final String state = "state";

	public static synchronized LotteryWebViewDatabase getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new LotteryWebViewDatabase();
			try {
				mCacheDatabase = context.openOrCreateDatabase(DATABASE_FILE, 0, null);
			} catch (SQLiteException e) {
				// try again by deleting the old db and create a new one
				if (context.deleteDatabase(DATABASE_FILE)) {
					mCacheDatabase = context.openOrCreateDatabase(DATABASE_FILE, 0, null);
				}
			}
			// mCacheDatabase should not be null,
			// the only case is RequestAPI test has problem to create db
			if (mCacheDatabase != null && mCacheDatabase.getVersion() != CACHE_DATABASE_VERSION) {
				mCacheDatabase.beginTransaction();
				try {
					upgradeCacheDatabase();
					bootstrapCacheDatabase();
					mCacheDatabase.setTransactionSuccessful();
				} finally {
					mCacheDatabase.endTransaction();
				}
				// Erase the files from the file system in the
				// case that the database was updated and the
				// there were existing cache content
			}

			if (mCacheDatabase != null) {
				// use read_uncommitted to speed up READ
				mCacheDatabase.execSQL("PRAGMA read_uncommitted = true;");
				// as only READ can be called in the non-WebViewWorkerThread,
				// and read_uncommitted is used, we can turn off database lock
				// to use transaction.
				mCacheDatabase.setLockingEnabled(false);
			}
		}
		return mInstance;
	}

	// 更新版本号
	private static void upgradeCacheDatabase() {
		int oldVersion = mCacheDatabase.getVersion();
		if (oldVersion != 0) {
			Log.i(LOGTAG, "Upgrading cache database from version " + oldVersion + " to " + DATABASE_VERSION + ", which will destroy all old data");
		}
		mCacheDatabase.execSQL("DROP TABLE IF EXISTS " + table_name);
		mCacheDatabase.setVersion(CACHE_DATABASE_VERSION);
	}

	// 创建表结构
	private static void bootstrapCacheDatabase() {
		if (mCacheDatabase != null) {
			mCacheDatabase.execSQL("CREATE TABLE " + table_name + " (" + _id + " INTEGER PRIMARY KEY, " + version + " TEXT, " + filename + "  TEXT NOT NULL UNIQUE, " + downUrl + " TEXT, " + datatime + " TEXT, " + state + " INTEGER);");
			// mCacheDatabase.execSQL("CREATE INDEX cacheUrlIndex ON "+table_name+" (" + downUrl + ")");
		}
	}

	/**
	 * 修改或者新增数据库webviwe缓存状态
	 * 
	 * @Description:
	 * @param webviewCache
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2014-12-8
	 */
	public void addOrUpdateCacheState(LotteryWebviewCacheState webviewCache) {

		long result = -11;
		ContentValues values = parseContentValues(webviewCache);
		if (webviewCache.getFileName() != null) {
			if (isExistData(webviewCache.getFileName())) {
				result = mCacheDatabase.update(table_name, values, filename + "=?", new String[] { webviewCache.getFileName() });
				return;
			}
		}
		values.put(filename, webviewCache.getFileName());
		result = mCacheDatabase.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		System.out.println(result);
	}

	/**
	 * 首次初始化数据库数据，内容为本地的h5资源
	 * 
	 * @Description:
	 * @param files
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2014-12-9
	 */
	public void addInsertCacheState(List<String> files) {
		if (files != null) {
			for (int i = 0; i < files.size(); i++) {
				ContentValues values = new ContentValues();
				values.put(version, "1.0");
				values.put(filename, files.get(i));
				values.put(downUrl, "0");
				values.put(datatime, String.valueOf(System.currentTimeMillis()));
				values.put(state, "2");
				mCacheDatabase.insertWithOnConflict(table_name, null, values, SQLiteDatabase.CONFLICT_REPLACE);
			}
		}

	}

	/**
	 * 判断是否存在数据
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2014-12-9
	 */
	public Boolean isExistData(String fileName) {
		Cursor cursor = null;
		try {
			cursor = mCacheDatabase.rawQuery("SELECT _id FROM " + table_name + " WHERE " + filename + "='" + fileName + "'", null);
			if (cursor.moveToFirst()) {
				cursor.close();
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return false;
	}

	/**
	 * 判断是否存在数据
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2014-12-9
	 */
	public Boolean isExistData() {
		Cursor cursor = null;
		try {
			cursor = mCacheDatabase.rawQuery("SELECT _id FROM " + table_name, null);
			if (cursor.moveToFirst()) {
				cursor.close();
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return false;
	}

	/**
	 * 获取所有本地h5缓存状态信息
	 * 
	 * @Description:
	 * @return
	 * @see:
	 * @since:
	 * @author:www.wenchuang.com
	 * @date:2014-12-10
	 */
	public synchronized List<LotteryWebviewCacheState> getLotteryWebviewCacheStates() {
		Cursor cursor = null;
		try {
			cursor = mCacheDatabase.query(table_name, null, null, null, null, null, null);
			List<LotteryWebviewCacheState> lotteryWebviewCacheStates = parseLotteryWebviewCacheStates(cursor);
			if (lotteryWebviewCacheStates != null && lotteryWebviewCacheStates.size() > 0) {
				return lotteryWebviewCacheStates;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return null;
	}

	/**
	 * 根据文件夹名称查询此文件夹所代表的缓存信息
	 */
	public LotteryWebviewCacheState getLotteryWebviewCacheState(String fileName) {
		Cursor cursor = null;
		try {
			cursor = mCacheDatabase.query(table_name, null, filename + "=?", new String[] { fileName }, null, null, null);
			List<LotteryWebviewCacheState> lotteryWebviewCacheStates = parseLotteryWebviewCacheStates(cursor);
			if (lotteryWebviewCacheStates != null && lotteryWebviewCacheStates.size() > 0) {
				return lotteryWebviewCacheStates.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return null;
	}

	private List<LotteryWebviewCacheState> parseLotteryWebviewCacheStates(Cursor cursor) {
		List<LotteryWebviewCacheState> lotteryWebviewCacheStates = new ArrayList<LotteryWebviewCacheState>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				LotteryWebviewCacheState lotteryWebviewCacheState = new LotteryWebviewCacheState();
				lotteryWebviewCacheState.setVersion(cursor.getString(cursor.getColumnIndex(version)));
				lotteryWebviewCacheState.setDatatime(cursor.getString(cursor.getColumnIndex(datatime)));
				lotteryWebviewCacheState.setDownUrl(cursor.getString(cursor.getColumnIndex(downUrl)));
				lotteryWebviewCacheState.setFileName(cursor.getString(cursor.getColumnIndex(filename)));
				lotteryWebviewCacheState.setState(cursor.getString(cursor.getColumnIndex(state)));
				lotteryWebviewCacheStates.add(lotteryWebviewCacheState);
			}
		}
		return lotteryWebviewCacheStates;

	}

	private ContentValues parseContentValues(LotteryWebviewCacheState webviewCache) {
		ContentValues values = new ContentValues();
		if (webviewCache != null) {
			if (webviewCache.getVersion() != null && !webviewCache.getVersion().equals("")) {
				values.put(version, webviewCache.getVersion());
			}
			if (webviewCache.getDownUrl() != null && !webviewCache.getDownUrl().equals("")) {
				values.put(downUrl, webviewCache.getDownUrl());
			}
			if (webviewCache.getDatatime() != null && !webviewCache.getDatatime().equals("")) {
				values.put(datatime, webviewCache.getDatatime());
			}
			if (webviewCache.getState() != null && !webviewCache.getState().equals("")) {
				values.put(state, webviewCache.getState());
			}
		}
		return values;
	}

}
