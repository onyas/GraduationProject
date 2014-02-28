package com.onyas.phoneguard.db.dao;

import java.io.File;

import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	
	
	/**
	 * @param path	���ݿ��·��
	 * @return	���ݿ�Ķ���
	 */
	public static SQLiteDatabase getAddressDB(String path) {
		File file = new File(path);
		if(file.exists()){
			return SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE);
		}
		return null;
		
	}

}
