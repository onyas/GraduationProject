package com.onyas.phoneguard.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class TaskUtil {

	/**
	 * 杀死所有进程
	 * @param context
	 */
	public static void killAllProcess(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runappinfos = am.getRunningAppProcesses();
		for (RunningAppProcessInfo info : runappinfos) {
			am.killBackgroundProcesses(info.processName);
		}
	}

}
