package com.onyas.phoneguard;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.onyas.phoneguard.domain.TaskInfo;
import com.onyas.phoneguard.receiver.ScreenOffReceiver;

public class MyApplication extends Application {
	
	public TaskInfo taskinfo;
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		filter.setPriority(1000);
		ScreenOffReceiver receiver = new ScreenOffReceiver();
		registerReceiver(receiver, filter);
	}
}
