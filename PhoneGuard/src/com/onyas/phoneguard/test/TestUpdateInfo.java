package com.onyas.phoneguard.test;

import android.test.AndroidTestCase;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.domain.UpdateInfo;
import com.onyas.phoneguard.engine.UpdateInfoEngine;

public class TestUpdateInfo extends AndroidTestCase {

	public void testUpdateInfo() throws Exception {
		UpdateInfoEngine upEngine = new UpdateInfoEngine(getContext());
		UpdateInfo info = upEngine.getUpdateInfo(R.string.apkserver);
		assertEquals("2.0", info.getVersion());
	}

}
