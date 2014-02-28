package com.onyas.phoneguard.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.onyas.phoneguard.db.dao.AddressDao;

public class PhoneAddressEngine {

	/**
	 * �õ��ֻ����������
	 * 
	 * @param num
	 *            �ֻ�����
	 * @return ���ع�����
	 */
	public static String getAddress(String num) {

		// �ֻ�����11λ
		// �̶��绰7��8λ��Ϊ���ص绰
		// 10λ��=3λ����+7λ����
		// 11λ4λ����+7λ����� 3λ����+8λ���룬
		// 12λ=4λ����+8λ�绰����
		String address = num;
		String dbpath = Environment.getExternalStorageDirectory()
				+ "/address.db";
		String pattern = "^1[3458]\\d{9}$";
		SQLiteDatabase db;

		if (num.matches(pattern)) {// �ֻ�����
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
		} else {// �̶��绰
			int len = num.length();
			switch (len) {
			case 4:
				address = "ģ����";
				break;
			case 7:
				address = "���غ���";
				break;
			case 8:
				address = "���غ���";
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
