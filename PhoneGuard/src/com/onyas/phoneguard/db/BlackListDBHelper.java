package com.onyas.phoneguard.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ���ڹ������ݿ���࣬���Դ����������������ݿ�
 * @author Administrator
 *
 */
public class BlackListDBHelper extends SQLiteOpenHelper {

	public BlackListDBHelper(Context context) {
		super(context, "blacklist.db", null, 1);
	}

	/**
	 * ���������ݿ�ʱ
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blacklist(_id integer primary key autoincrement,number varchar(20))");
	}

	/**
	 * ���������ݿ�ʱ
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
