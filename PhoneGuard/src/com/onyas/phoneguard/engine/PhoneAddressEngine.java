package com.onyas.phoneguard.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.onyas.phoneguard.db.dao.AddressDao;

public class PhoneAddressEngine {

	/**
	 * 得到手机号码归属地
	 * 
	 * @param num
	 *            手机号码
	 * @return 返回归属地
	 */
	public static String getAddress(String num) {

		// 手机号码11位
		// 固定电话7，8位的为本地电话
		// 10位的=3位区号+7位号码
		// 11位4位区号+7位号码或 3位区号+8位号码，
		// 12位=4位区号+8位电话号码
		String address = num;
		String dbpath = Environment.getExternalStorageDirectory()
				+ "/address.db";
		String pattern = "^1[3458]\\d{9}$";
		SQLiteDatabase db;

		if (num.matches(pattern)) {// 手机号码
			db = AddressDao.getAddressDB(dbpath);
			if(db==null)
				return "";
			if (db.isOpen()) {
				Cursor cursor = db.rawQuery(
						"select city from info where mobileprefix=?",
						new String[] { num.substring(0, 7) });
				if (cursor.moveToNext()) {
					address = cursor.getString(0);
				}
				cursor.close();
				db.close();
			}
		} else {// 固定电话
			int len = num.length();
			switch (len) {
			case 4:
				address = "模拟器";
				break;
			case 7:
				address = "本地号码";
				break;
			case 8:
				address = "本地号码";
				break;

			case 10:
				db = AddressDao.getAddressDB(dbpath);
				if (db.isOpen()) {
					Cursor cursor = db.rawQuery(
							"select city from info where area=? limit 1",
							new String[] { num.substring(0, 3) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					}
					cursor.close();
					db.close();
				}
				break;
			case 11:
				db = AddressDao.getAddressDB(dbpath);
				if (db.isOpen()) {
					Cursor cursor = db.rawQuery(
							"select city from info where area=? limit 1",
							new String[] { num.substring(0, 3) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					}

					Cursor cursor2 = db.rawQuery(
							"select city from info where area=? limit 1",
							new String[] { num.substring(0, 4) });
					if (cursor2.moveToNext()) {
						address = cursor.getString(0);
					}

					cursor.close();
					cursor.close();
					db.close();
				}
				break;

			case 12:
				db = AddressDao.getAddressDB(dbpath);
				if (db.isOpen()) {
					Cursor cursor = db.rawQuery(
							"select city from info where area=? limit 1",
							new String[] { num.substring(0, 4) });
					if (cursor.moveToNext()) {
						address = cursor.getString(0);
					}
					cursor.close();
					db.close();
				}
				break;

			}
		}

		return address;
	}

}
