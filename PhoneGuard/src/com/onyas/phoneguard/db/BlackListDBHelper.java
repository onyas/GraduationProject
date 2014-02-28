package com.onyas.phoneguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 当于管理数据库的类，可以创建，升级，打开数据库
 * @author Administrator
 *
 */
public class BlackListDBHelper extends SQLiteOpenHelper {

	public BlackListDBHelper(Context context) {
		super(context, "blacklist.db", null, 1);
	}

	/**
	 * 当创建数据库时
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacklist(_id integer primary key autoincrement,number varchar(20))");
	}

	/**
	 * 当升级数据库时
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
