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
	 * �õ��ֻ��������Ѱ�װ��Ӧ�ó���
	 * 
	 * @return Ӧ�ó���ļ���
	 */
	public List<AppInfo> getAllApps() {
		List<AppInfo> appInfos = new ArrayList<AppInfo>();

		// �����а�װ��Ӧ�ó���õ�,flag��ָ�����еģ�����û��ɾ���ɾ�����ϢҲ�õ�
		List<PackageInfo> packinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

		for (PackageInfo info : packinfos) {

			String packagename = info.packageName;// �õ�����İ���
			Drawable icon = info.applicationInfo.loadIcon(pm);// Ӧ�ó����ͼ��
			String appname = info.applicationInfo.loadLabel(pm).toString();// Ӧ�ó��������
			boolean isSystemApp = !filterApp(info.applicationInfo);

			AppInfo appinfo = new AppInfo(icon, appname, packagename,
					isSystemApp);

			Log.i(TAG, packagename + "," + appname);
			if (filterApp(info.applicationInfo)) {
				Log.i(TAG, "����Ӧ��");
			} else {
				Log.i(TAG, "ϵͳӦ��");
			}
			appInfos.add(appinfo);
		}
		return appInfos;
	}

	/**
	 * �ж�Ӧ�ó����Ƿ�Ϊ���������򣬴�Ϊsettings��Դ���룬(��github��������android,Ȼ����������settings)
	 * 
	 * @return
	 */
	private boolean filterApp(ApplicationInfo info) {
		if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {// �ֻ����������õĳ��򣬿�������������û������ˣ����Ϊ����
			return true;
		} else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
			return true;
		}
		return false;
	}
}
