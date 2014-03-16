package com.onyas.phoneguard.test;

import java.util.List;

import android.test.AndroidTestCase;
import com.onyas.phoneguard.util.Logger;

import com.onyas.phoneguard.db.dao.BlackListDao;

public class TestBlackListDao extends AndroidTestCase {

	private static final String TAG = "TestBlackListDao";

	public void testadd(){
		BlackListDao dao = new BlackListDao(getContext());
		for(int i=10;i<40;i++)
		{
			dao.add("13812345"+i);
		}
	}
	
	public void testfind(){
		BlackListDao dao = new BlackListDao(getContext());
		boolean b = dao.find("13812345679");
		assertEquals(true, b);
	}
	
	public void testfindall(){
		BlackListDao dao = new BlackListDao(getContext());
		List<String> list = dao.findAll();
		for(int i=0;i<list.size();i++){
			Logger.i(TAG, list.get(i));
		}
		assertEquals(10, list.size());
	}
	
}
