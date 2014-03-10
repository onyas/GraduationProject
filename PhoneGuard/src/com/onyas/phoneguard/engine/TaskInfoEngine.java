package com.onyas.phoneguard.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.onyas.phoneguard.domain.TaskInfo;

public class TaskInfoEngine {

	private PackageManager pm;
	private ActivityManager am;
	private List<RunningAppProcessInfo> runingProcessinfos;

	public TaskInfoEngine(Context context) {
		pm = context.getPackageManager();
		am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		runingProcessinfos = am.getRunningAppProcesses();
	}

	/**
	 * 得到所有的正在运行的进程的信息
	 * @return
	 */
	public List<TaskInfo> getAllTasks() {
		List<TaskInfo> taskinfos = new ArrayList<TaskInfo>();
		for (RunningAppProcessInfo info : runingProcessinfos) {
			TaskInfo taskinfo;
			try {
				int pid = info.pid;//得到进程的id
				String packname = info.processName;// 得到进程的包名
				ApplicationInfo appinfo = pm.getPackageInfo(packname, 0).applicationInfo;//根据包名得到packageManager,进而得到ApplicationInfo
				Drawable appicon = appinfo.loadIcon(pm);//得到程序的图标
				String appname = appinfo.loadLabel(pm).toString();//得到程序的名称
				MemoryInfo[] memoryinfos = am
						.getProcessMemoryInfo(new int[] { pid });
				int memorysize = memoryinfos[0].getTotalPrivateDirty();//得到程序占用的内存
				taskinfo = new TaskInfo(appname, appicon, pid, memorysize,
						packname, false);
				taskinfos.add(taskinfo);
				taskinfo = null;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}

		return taskinfos;
	}
}
