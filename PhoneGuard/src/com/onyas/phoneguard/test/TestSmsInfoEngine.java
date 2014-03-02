package com.onyas.phoneguard.test;

import java.util.List;

import android.test.AndroidTestCase;

import com.onyas.phoneguard.domain.SmsInfo;
import com.onyas.phoneguard.engine.SmsInfoEngine;

public class TestSmsInfoEngine extends AndroidTestCase{

	public void testGetSmsInfoEngine(){
		SmsInfoEngine info = new SmsInfoEngine(getContext());
		List<SmsInfo> infos = info.getAllSmsInfo();
	}
	
}
