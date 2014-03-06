package com.onyas.phoneguard.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.onyas.phoneguard.db.dao.AppLockDao;

public class TestAppLockDao extends AndroidTestCase {

	public void testAdd() {
		AppLockDao dao = new AppLockDao(getContext());
		dao.add("com.onyas.test");
		dao.add("com.onyas.test1");
		dao.add("com.onyas.test2");
		dao.add("com.onyas.test3");
	}

	public void testDelete() {
		AppLockDao dao = new AppLockDao(getContext());
		dao.delete("com.onyas.test");
	}

	public void testFindAll() {
		AppLockDao dao = new AppLockDao(getContext());
		List<String> list = dao.findAll();
		System.out.println(list.size());
	}
}
