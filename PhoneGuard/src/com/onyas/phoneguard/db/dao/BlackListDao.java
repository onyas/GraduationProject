package com.onyas.phoneguard.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.onyas.phoneguard.db.BlackListDBHelper;

public class BlackListDao {

	private BlackListDBHelper dbHelper;
	private Context context;

	public BlackListDao(Context context) {
		super();
		this.context = context;
		this.dbHelper = new BlackListDBHelper(context);
	}

	/**
	 * 检查电话号码是否在数据库中
	 * 
	 * @param number
	 *            要检查的电话号码
	 * @return
	 */
	public boolean find(String number) {
		boolean exist = false;
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		if (db.isOpen()) {
			Cursor cursor = db.rawQuery(
					"select number from blacklist where number = ?",
					new String[] { number });
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
	public void add(String number) {
		if (find(number))
			return;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("insert into blacklist(number) values (?)",
					new Object[] { number });
			db.close();
		}
	}

	/**
	 * 删除
	 * @param number
	 */
	public void delete(String number){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("delete from blacklist where number=?",
					new Object[] { number });
			db.close();
		}
	}
	
	/**
	 * 更新
	 * @param oldNum 旧的号码
	 * @param newNum 新的号码
	 */
	public void update(String oldNum,String newNum){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("update blacklist set number = ? where number = ?",new Object[]{newNum,oldNum});
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
			Cursor cursor = db.rawQuery("select number from blacklist", new String[]{});
			while(cursor.moveToNext()){
				list.add(cursor.getString(0));
			}
			cursor.close();
			db.close();
		}
		return list;
	}
	
}
