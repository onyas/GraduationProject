package com.onyas.phoneguard.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.onyas.phoneguard.db.AppLockDBHelper;
import com.onyas.phoneguard.db.BlackListDBHelper;

public class AppLockDao {

	private AppLockDBHelper dbHelper;
	private Context context;

	public AppLockDao(Context context) {
		super();
		this.context = context;
		this.dbHelper = new AppLockDBHelper(context);
	}

	/**
	 * 检查某一个包名是否在数据库中
	 * 
	 * @param number
	 *  
	 * @return
	 */
	public boolean find(String packname) {
		boolean exist = false;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select packname from lock where packname = ?",
					new String[] { packname });
			if (cursor.moveToNext()) {
				exist = true;
			}
			cursor.close();
			db.close();
		}
		return exist;
	}

	/**
	 * 向数据库中增加一条记录
	 * 
	 * @param number
	 */
	public void add(String packname) {
		if (find(packname))
			return;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("insert into lock(packname) values (?)",
					new Object[] { packname });
			db.close();
		}
	}

	/**
	 * 删除
	 * @param number
	 */
	public void delete(String packname){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from lock where packname=?",
					new Object[] { packname });
			db.close();
		}
	}
	
	
	/**
	 * 查找所有的记录
	 * @return
	 */
	public List<String> findAll(){
		List<String> list = new ArrayList<String>();
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery("select packname from lock", new String[]{});
			while(cursor.moveToNext()){
				list.add(cursor.getString(0));
			}
			cursor.close();
			db.close();
		}
		return list;
	}
	
}
