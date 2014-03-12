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
	 * 把assets文件夹中的数据库文件拷贝到SD卡中
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
		 * 得到第groupPosition的第childPosition个子孩子
		 */
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		/**
		 * 返回子孩子的ID
		 */
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		/**
		 * 返回子孩子的视图
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
		 * 得到第groupPosition有多少个子孩子
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
		 * 一共有多少组
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
		 * 返回当前组号
		 */
		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		/**
		 * 返回组的视图
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
		 * 孩子节点是否可以选中
		 */
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

}
