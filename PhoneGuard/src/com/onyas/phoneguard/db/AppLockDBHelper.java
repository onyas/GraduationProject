package com.onyas.phoneguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 当于管理数据库的类，可以创建，升级，打开数据库
 * @author Administrator
 *
 */
public class AppLockDBHelper extends SQLiteOpenHelper {

	public AppLockDBHelper(Context context) {
		super(context, "applock.db", null, 1);
	}

	/**
	 * 当创建数据库时
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table lock(_id integer primary key autoincrement,packname varchar(20))");
	}

	/**
	 * 当升级数据库时
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
