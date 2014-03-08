package com.onyas.phoneguard.service;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.onyas.phoneguard.db.dao.AppLockDao;

public class WatchDogService extends Service {

	protected static final String TAG = "WatchDogService";
	private AppLockDao dao;
	private List<String> lockapps;
	private ActivityManager am;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 服务第一次创建的时候就调用
	 */
	@Override
	public void onCreate() {
		dao = new AppLockDao(this);
		lockapps = dao.findAll();// 得到所有要加密的应用程序

		am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);

		new Thread() {
			public void run() {

				// 开启看门狗
				while (true) {
					try {
						// 得到当前正在运行程序的包名
						// 返回系统里面任务栈的信息，只返回最近的一条
						// 内容就是当前正在运行的进程所对应的任务栈
						List<RunningTaskInfo> taskinfos = am.getRunningTasks(1);
						RunningTaskInfo currentTast = taskinfos.get(0);
						// 获取当前用户可见的activity所在程序的包名
						String packname = currentTast.topActivity
								.getPackageName();
						Log.i(TAG, "当前正在运行" + packname);

						if (lockapps.contains(packname)) {
							// 需要保护的，然后弹出输入密码的对话框
							Log.i(TAG, "需要锁定" + packname);
							
							
						} else {
							// 放行执行
						}

						
						sleep(1000);//每隔1秒钟监视一次
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

}
