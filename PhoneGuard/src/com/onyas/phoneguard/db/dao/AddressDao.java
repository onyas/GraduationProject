package com.onyas.phoneguard.db.dao;

import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	
	
	/**
	 * @param path	���ݿ��·��
	 * @return	���ݿ�Ķ���
	 */
	public static SQLiteDatabase getAddressDB(String path) {
		return SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READWRITE);
	}

}
