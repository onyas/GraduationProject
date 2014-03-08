package com.onyas.phoneguard.provider;

import com.onyas.phoneguard.db.dao.AppLockDao;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

/**
 * 只关注添加与删除的操作
 * 
 * @author Administrator
 * 
 */
public class AppLockProvider extends ContentProvider {

	private static final int INSERT = 1;
	private static final int DELETE = 2;
	private static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
	private static Uri changeuri = Uri
			.parse("content://com.onyas.applockprovide");
	private AppLockDao dao;

	static {
		matcher.addURI("com.onyas.applockprovider", "insert", INSERT);
		matcher.addURI("com.onyas.applockprovider", "delete", DELETE);
	}

	@Override
	public boolean onCreate() {
		dao = new AppLockDao(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		return null;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int res = matcher.match(uri);
		if (res == INSERT) {
			String packname = (String) values.get("packname");
			dao.add(packname);
			// 通知内容提供者哪个uri发生改变
			getContext().getContentResolver().notifyChange(changeuri, null);
		} else {
			throw new IllegalArgumentException("uri地址不正确");
		}
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int res = matcher.match(uri);
		if (res == DELETE) {
			String packname = selectionArgs[0];
			dao.delete(packname);
			// 通知内容提供者哪个uri发生改变
			getContext().getContentResolver().notifyChange(changeuri, null);
		} else {
			throw new IllegalArgumentException("uri地址不正确");
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

}
