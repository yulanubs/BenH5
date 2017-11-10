package org.benmobile.analysis.database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.benmobile.analysis.MobileLogConsts;
import org.benmobile.analysis.annotation.DatabaseKey;
import org.benmobile.analysis.database.obj.BaseDatabaseObj;


public class LogDatabaseHelper extends SQLiteOpenHelper{

	private static final String CREATE = "CREATE TABLE IF NOT EXISTS ";

	public static final String TABLE_MB_LOG_ACTION = "MB_LOG_ACTION";
	public static final String TABLE_MB_LOG_CLICK = "MB_LOG_CLICK";
	//	public static final String TABLE_MB_LOG_DEVICE = "MB_LOG_DEVICE";
	public static final String TABLE_MB_LOG_LAUNCH = "MB_LOG_LAUNCH";
	public static final String TABLE_MB_LOG_OPERATE = "MB_LOG_OPERATE";
	public static final String TABLE_MB_LOG_CRASH = "MB_LOG_CRASH";

	private static final String DEFAULT_KEYS = "ID INTEGER PRIMARY KEY AUTOINCREMENT, DEVICE_ID char(25), MARKET_ID char(5), VERSION char(10), ACCESS_TIME char(25), CLIENT_TYPE char(15), DEVICE_TYPE char(10), NETWORK char(10), NETWORK_DETAIL TEXT, USER_ID char(10), APPKEY char(25), ";

	private static final String CREATE_MB_LOG_ACTION = CREATE + TABLE_MB_LOG_ACTION+"(" + DEFAULT_KEYS + "CREATE_TIME char(25), PARAMS TEXT, ACTION_TYPE char(25), ACTION_NAME TEXT,CONSUME_TIME char(20))";
	private static final String CREATE_MB_LOG_CLICK = CREATE + TABLE_MB_LOG_CLICK + "(" + DEFAULT_KEYS + "CREATE_TIME char(25), PARAMS TEXT, MODULE_ID char(10), MODULE_DESC TEXT)";
	private static final String CREATE_MB_LOG_CRASH = CREATE + TABLE_MB_LOG_CRASH + "(" + DEFAULT_KEYS + "PLUGIN_ID char(50),OS char(25), RESOLUTION char(10),  OS_VERSION char(10), SDK_INT char(5), BRAND char(25), MODEL char(25),EXCEPTION_TYPE char(100),CLASS_NAME char(50),METHOD_NAME char(50),LINE_NUMBER char(10),CAUSE TEXT,STACK_TRACE TEXT )";
	//	private static final String CREATE_MB_LOG_DEVICE = CREATE + TABLE_MB_LOG_DEVICE + "(" + DEFAULT_KEYS + "PUSH_DEVICE_TOKEN char(25), OS char(25), RESOLUTION char(10), UPDATE_TIME char(25), OLD_VERSION char(10), IMEI char(25), IMSI char(25), OS_VERSION char(10), SDK_INT char(5), BRAND char(25), MODEL char(25), MAC char(25),LAT_LON char(25))";
	private static final String CREATE_MB_LOG_LAUNCH = CREATE + TABLE_MB_LOG_LAUNCH + "(" + DEFAULT_KEYS + "PUSH_DEVICE_TOKEN char(25),LAT_LON char(25))";
	private static final String CREATE_MB_LOG_OPERATE = CREATE + TABLE_MB_LOG_OPERATE + "(" + DEFAULT_KEYS + "PUSH_BATCH_ID char(25), VIEW_PAGE char(25), PARAMS TEXT, PRE_VIEW_PAGE char(25), PRE_VP_CONSTIME char(25))";

	private Object lock = new Object();

	public LogDatabaseHelper(Context context) {
		super(context, MobileLogConsts.LOG_DATABASE_NAME,
				null, MobileLogConsts.LOG_DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_MB_LOG_ACTION);
		db.execSQL(CREATE_MB_LOG_CLICK);
		db.execSQL(CREATE_MB_LOG_CRASH);
//		db.execSQL(CREATE_MB_LOG_DEVICE);
		db.execSQL(CREATE_MB_LOG_LAUNCH);
		db.execSQL(CREATE_MB_LOG_OPERATE);


	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	public void insert(BaseDatabaseObj obj){
		synchronized (lock) {
			ContentValues cv = new ContentValues();
			Field[] fs = obj.getClass().getFields();
			for(int i=0;i<fs.length;i++){
				Field f = fs[i];
				DatabaseKey an = f.getAnnotation(DatabaseKey.class);
				if (an == null){
					continue;
				}
				String key = an.databaseKey();
				try {
					cv.put(key, (String) f.get(obj));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (cv.size() > 0){
				SQLiteDatabase db = getWritableDatabase();
				db.insert(obj.getDatabaseTableName(), null, cv);
			}
		}
	}

	public void delete(String tableName,ArrayList<String> ids){
		synchronized (lock) {
			try {
				SQLiteDatabase db = getWritableDatabase();
				for(int i=0;i<ids.size();i++){
					db.delete(tableName, "ID = ?", new String[]{ids.get(i)});
				}

			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	}

	public <T extends BaseDatabaseObj>List<T> readAll(String tableName,Class<T> clazz){
		synchronized (lock) {
			SQLiteDatabase db = getReadableDatabase();
			List<T> rets = new ArrayList<T>();
			Cursor c = db.rawQuery("SELECT * FROM " + tableName, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				try {
					T o = clazz.getConstructor().newInstance();
					o.id = c.getInt(c.getColumnIndex("ID"));
					Field[] fs = clazz.getFields();
					for(int i=0;i<fs.length;i++){

						Field f = fs[i];
						DatabaseKey a = f.getAnnotation(DatabaseKey.class);
						if (a == null){
							continue;
						}
						String key = a.databaseKey();
						String value = c.getString(c.getColumnIndex(key));
						f.set(o, value);
					}
					rets.add(o);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
				} catch (Exception e) {
					// TODO: handle exception
				}
				c.moveToNext();
			}
			c.close();
			return rets;
		}
	}
}
