package com.onyas.phoneguard.receiver;

import com.onyas.phoneguard.util.TaskUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.onyas.phoneguard.util.Logger;

public class ScreenOffReceiver extends BroadcastReceiver {

	private static final String TAG = "ScreenOffReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		Logger.i(TAG, "screen off");
		SharedPreferences sp = context.getSharedPreferences("config",
				context.MODE_PRIVATE);
		boolean autoclean = sp.getBoolean("autoclean", false);
		if(autoclean){
			TaskUtil.killAllProcess(context);
		}
	}

}
