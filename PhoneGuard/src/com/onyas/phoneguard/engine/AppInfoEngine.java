package com.onyas.phoneguard.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.onyas.phoneguard.domain.AppInfo;

public class AppInfoEngine {

	private static final String TAG = "AppInfoEngine";
	private Context context;
	private PackageManager pm;

	public AppInfoEngine(Context context) {
		this.context = context;
		pm = context.getPackageManager();
	}

	/**
	 * 得到手机中所有已安装的应用程序
	 * 
	 * @return 应用程序的集合
	 */
	public List<AppInfo> getAllApps() {
		List<AppInfo> appInfos = new ArrayList<AppInfo>();

		// 把所有安装的应用程序得到,flag是指把所有的，包括没有删除干净的信息也得到
		List<PackageInfo> packinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

		for (PackageInfo info : packinfos) {

			String packagename = info.packageName;// 得到程序的包名
			Drawable icon = info.applicationInfo.loadIcon(pm);// 应用程序的图标
			String appname = info.applicationInfo.loadLabel(pm).toString();// 应用程序的名字
			boolean isSystemApp = !filterApp(info.applicationInfo);

			AppInfo appinfo = new AppInfo(icon, appname, packagename,
					isSystemApp);

			Log.i(TAG, packagename + "," + appname);
			if (filterApp(info.applicationInfo)) {
				Log.i(TAG, "三方应用");
			} else {
				Log.i(TAG, "系统应用");
			}
			appInfos.add(appinfo);
		}
		return appInfos;
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
