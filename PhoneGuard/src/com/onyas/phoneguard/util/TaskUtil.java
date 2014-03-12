package com.onyas.phoneguard.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

public class TaskUtil {

	/**
	 * ɱ�����н���
	 * 
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

	/**
	 * �õ����̵���Ŀ
	 * 
	 * @param context
	 */
	public static int getProcessCount(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runappinfos = am.getRunningAppProcesses();
		return runappinfos.size();
	}

	/**
	 * �õ������ڴ�
	 * 
	 * @param context
	 */
	public static String getAvailMemory(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		return TextFormatter.sizeFormat(outInfo.availMem);
	}
}
