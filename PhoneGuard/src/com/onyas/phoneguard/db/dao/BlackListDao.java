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
	 * ���绰�����Ƿ������ݿ���
	 * 
	 * @param number
	 *            Ҫ���ĵ绰����
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
	 * �����ݿ�������һ����¼
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
	 * ɾ��
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
	 * ����
	 * @param oldNum �ɵĺ���
	 * @param newNum �µĺ���
	 */
	public void update(String oldNum,String newNum){
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		if (db.isOpen()) {
			db.execSQL("update blacklist set number = ? where number = ?",new Object[]{newNum,oldNum});
			db.close();
		}
	}
	
	
	/**
	 * �������еļ�¼
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
