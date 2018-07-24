package cn.com.cimgroup.db;

import android.database.sqlite.SQLiteDatabase;
import cn.com.cimgroup.xutils.SQLiteHelper;
import cn.com.cimgroup.xutils.Singlton;
import cn.com.cimgroup.App;

/**
 * 核心数据库操作类，请不要直接new或调用此类
 * 
 * @Description:
 * @author:xuqq
 * @see:
 * @since:
 * @copyright www.wenchuang.com
 * @Date:2013-7-25
 */
class CoreDbHelper extends SQLiteHelper{
	
	protected static final int VERSION =13;
	
	/**
	 * 附件表
	 */
	public static String SQL_TABLE_TB_ATTACHMENTS_CREATE;
//	public static final String SQL_TABLE_TB_ATTACHMENTS_CLEAR = "DROP TABLE IF EXISTS " + Columns.TbAttachments.TB_NAME;


	protected CoreDbHelper(String uuid) {
		super(Singlton.getInstance(App.class), getDbName(uuid), null, VERSION);
	}

	// 如果是调试模式，数据库存储在sd卡上
	protected static String getDbName(String uuid) {
		String dbname = "";
//		if (WeMail.DEBUG_DB_SAVEINSDCARD) {
//			dbname = GlobalConstants.APPCATION_SCARD_DIRECTORY + "/" + uuid + ".db";
//		} else {
			dbname = uuid + ".db";
//		}
		return dbname;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		sql_table_create();
		db.execSQL(SQL_TABLE_TB_ATTACHMENTS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL(SQL_TABLE_TB_ATTACHMENTS_CLEAR);
		onCreate(db);
	}

	/**
	 * 建表语句
	 * 
	 * @Description:
	 * @see:
	 * @since:
	 * @author: xuqq
	 * @date:2013-7-29
	 */
	public void sql_table_create(){
//		StringBuffer tb_attachments = new StringBuffer();
//		tb_attachments.append("CREATE TABLE IF NOT EXISTS ")
//					.append(Columns.TbAttachments.TB_NAME).append(" (")
//					.append(Columns.TbAttachments.F_ID).append(" INTEGER PRIMARY KEY, ")
//					.append(Columns.TbAttachments.F_ATTACHMENT_ID).append(" TEXT,")
//					.append(Columns.TbAttachments.F_MESSAGE_UID).append(" TEXT,")
//					.append(Columns.TbAttachments.F_NAME).append(" TEXT,")
//					.append(Columns.TbAttachments.F_CONTENT_URI).append(" TEXT,")
//					.append(Columns.TbAttachments.F_SIZE).append(" INTEGER,")
//					.append(Columns.TbAttachments.F_CONTENT_TYPE).append(" TEXT,")
//					.append(Columns.TbAttachments.F_DOWNLOAD_STATE).append(" INTEGER,")
//					.append(Columns.TbAttachments.F_IS_INLINE).append(" INTEGER,")
//					.append(Columns.TbAttachments.F_CONTENT_ID).append(" TEXT,")
//					.append(Columns.TbAttachments.F_ENCODING).append(" TEXT,")
//					.append(Columns.TbAttachments.F_PARENT_ATTACHMENT_ID).append(" INTEGER DEFAULT -1,")
//					.append(Columns.TbAttachments.F_SOURCE_ATTACHMENT_ID).append(" 	TEXT,")
//					.append(Columns.TbAttachments.F_SOURCE_MESSAGE_UID).append(" TEXT,")
//					.append(Columns.TbAttachments.F_VOICE_DURATION).append(" INTEGER);");
//		SQL_TABLE_TB_ATTACHMENTS_CREATE = tb_attachments.toString();
		
		
	}
}
