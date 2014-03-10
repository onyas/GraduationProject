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
	 * 
	 * @return
	 */
	public List<TaskInfo> getAllTasks() {
		List<TaskInfo> taskinfos = new ArrayList<TaskInfo>();
		for (RunningAppProcessInfo info : runingProcessinfos) {
			TaskInfo taskinfo;
			try {
				int pid = info.pid;// 得到进程的id
				String packname = info.processName;// 得到进程的包名
				ApplicationInfo appinfo = pm.getPackageInfo(packname, 0).applicationInfo;// 根据包名得到packageManager,进而得到ApplicationInfo
				Drawable appicon = appinfo.loadIcon(pm);// 得到程序的图标
				String appname = appinfo.loadLabel(pm).toString();// 得到程序的名称
				MemoryInfo[] memoryinfos = am
						.getProcessMemoryInfo(new int[] { pid });
				int memorysize = memoryinfos[0].getTotalPrivateDirty();// 得到程序占用的内存
				taskinfo = new TaskInfo(appname, appicon, pid, memorysize,
						packname, false);
				//判断是否为系统应用程序
				if (filterApp(appinfo)) {
					taskinfo.setSystemapp(false);
				}else{
					taskinfo.setSystemapp(true);
				}
				taskinfos.add(taskinfo);
				taskinfo = null;
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
		}

		return taskinfos;
	}

	/**
	 * 判断应用程序是否为第三方程序，此为settings的源代码，(在github上面搜索android,然后在里面搜settings)
	 * 
	 * @return
	 */
	private boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {// 手机里面有内置的程序，可以升级，如果用户升级了，则成为三方
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}
}
