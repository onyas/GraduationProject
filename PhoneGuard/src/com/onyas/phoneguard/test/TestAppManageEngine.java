package com.onyas.phoneguard.test;

import com.onyas.phoneguard.engine.AppInfoEngine;

import android.test.AndroidTestCase;

public class TestAppManageEngine extends AndroidTestCase {

	
	public void testgetAll(){
		AppInfoEngine appinfo = new AppInfoEngine(getContext());
		appinfo.getAllApps();
	}
	
}
