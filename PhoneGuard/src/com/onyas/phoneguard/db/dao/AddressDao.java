package com.onyas.phoneguard.db.dao;

import java.io.File;

import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	
	
	/**
	 * @param path	数据库的路径
	 * @return	数据库的对象
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
