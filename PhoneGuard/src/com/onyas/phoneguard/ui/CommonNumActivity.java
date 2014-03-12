package com.onyas.phoneguard.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.util.FileUtil;

public class CommonNumActivity extends Activity {

	private ExpandableListView exlv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commonnum);

		exlv = (ExpandableListView) findViewById(R.id.exlv);

		File file = new File("/sdcard/commonnum.db");
		if (!file.exists()) {
			copyDBFile();
		}

		exlv.setAdapter(new MyAdapter());
	}

	/**
	 * ��assets�ļ����е����ݿ��ļ�������SD����
	 */
	private void copyDBFile() {
		AssetManager assetManager = getAssets();
		try {
			InputStream is = assetManager.open("commonnum.db");
			FileUtil.copyFile(is, "/sdcard/commonnum.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class MyAdapter extends BaseExpandableListAdapter {

		/**
		 * �õ���groupPosition�ĵ�childPosition���Ӻ���
		 */
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		/**
		 * �����Ӻ��ӵ�ID
		 */
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		/**
		 * �����Ӻ��ӵ���ͼ
		 */
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {

			TextView tv = new TextView(CommonNumActivity.this);

			StringBuilder sb = new StringBuilder();
			int tableindex = groupPosition + 1;
			int childpro = childPosition + 1;
			String sql = "select number,name from table" + tableindex;
			SQLiteDatabase db = SQLiteDatabase.openDatabase(
					"/sdcard/commonnum.db", null, SQLiteDatabase.OPEN_READONLY);
			if (db.isOpen()) {
				Cursor cursor = db.rawQuery(sql + " where _id=?",
						new String[] { childpro + "" });
				while (cursor.moveToNext()) {
					sb.append(cursor.getString(0));
					sb.append(":");
					sb.append(cursor.getString(1));
				}
				cursor.close();
				db.close();
			}
			tv.setText("                  " + sb.toString());
			return tv;
		}

		/**
		 * �õ���groupPosition�ж��ٸ��Ӻ���
		 */
		@Override
		public int getChildrenCount(int groupPosition) {
			int count = 0;
			int tableindex = groupPosition + 1;
			String sql = "select count(*) from table" + tableindex;
			SQLiteDatabase db = SQLiteDatabase.openDatabase(
					"/sdcard/commonnum.db", null, SQLiteDatabase.OPEN_READONLY);
			if (db.isOpen()) {
				Cursor cursor = db.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					count = cursor.getInt(0);
				}
				cursor.close();
				db.close();
			}
			return count;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		/**
		 * һ���ж�����
		 */
		@Override
		public int getGroupCount() {
			int count = 0;
			SQLiteDatabase db = SQLiteDatabase.openDatabase(
					"/sdcard/commonnum.db", null, SQLiteDatabase.OPEN_READONLY);
			if (db.isOpen()) {
				Cursor cursor = db.rawQuery("select count(*) from classlist",
						null);
				while (cursor.moveToNext()) {
					count = cursor.getInt(0);
				}
				cursor.close();
				db.close();
			}
			return count;
		}

		/**
		 * ���ص�ǰ���
		 */
		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		/**
		 * ���������ͼ
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			TextView tv = new TextView(CommonNumActivity.this);

			String name = "";
			int idx = groupPosition + 1;
			SQLiteDatabase db = SQLiteDatabase.openDatabase(
					"/sdcard/commonnum.db", null, SQLiteDatabase.OPEN_READONLY);
			if (db.isOpen()) {
				Cursor cursor = db.rawQuery(
						"select name from classlist where idx=?",
						new String[] { idx + "" });
				while (cursor.moveToNext()) {
					name = cursor.getString(0);
				}
				cursor.close();
				db.close();
			}
			tv.setText("                  " + name);
			return tv;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		/**
		 * ���ӽڵ��Ƿ����ѡ��
		 */
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

}
