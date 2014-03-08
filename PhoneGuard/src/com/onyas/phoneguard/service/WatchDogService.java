package com.onyas.phoneguard.service;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.onyas.phoneguard.db.dao.AppLockDao;
import com.onyas.phoneguard.ui.ProtectAppActivity;

public class WatchDogService extends Service {

	protected static final String TAG = "WatchDogService";
	private AppLockDao dao;
	private List<String> lockapps;
	private ActivityManager am;
	private Intent protectAppIntent;
	private boolean flag;// 用于标识是否要停止线程
	private List<String> tempUnprotect;// 暂时停止保护的程序的集合
	private MyBinder mybinder;

	@Override
	public IBinder onBind(Intent intent) {

		return mybinder;
	}

	private class MyBinder extends Binder implements IService {
		@Override
		public void stopProtectApp(String packname) {
			stopProtect(packname);
		}

		@Override
		public void startProtectApp(String packname) {
			startProtect(packname);
		}
	}

	/**
	 * 暂时停止对应用程序的保护
	 * 
	 * @param packname
	 */
	public void stopProtect(String packname) {
		tempUnprotect.add(packname);
	}

	/**
	 * 重新开启对应用程序的保护
	 * 
	 * @param packname
	 */
	public void startProtect(String packname) {
		if (tempUnprotect.contains(packname)) {
			tempUnprotect.remove(packname);
		}
	}

	/**
	 * 服务第一次创建的时候就调用
	 */
	@Override
	public void onCreate() {

		getContentResolver()
				.registerContentObserver(
						Uri.parse("content://com.onyas.applockprovide"), true,
						new MyObserver(new Handler()));

		mybinder = new MyBinder();
		tempUnprotect = new ArrayList<String>();
		dao = new AppLockDao(this);
		lockapps = dao.findAll();// 得到所有要加密的应用程序
		protectAppIntent = new Intent(this, ProtectAppActivity.class);
		// 服务里面不存在任务栈，如果要在服务里面激活一个activity,就要加上这个Flag，如果手机卫士有任务栈，则复用之前的任务栈，
		// 如果把ProtectAppActivity放到一个新的任务栈里，不把它放到手机卫士里面，改为用activity的Single
		// Instance启用模式,并且这个任务栈里只有一个activity的实例
		protectAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		flag = true;
		am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);

		new Thread() {
			public void run() {

				// 开启看门狗
				while (flag) {
					try {
						// 得到当前正在运行程序的包名
						// 返回系统里面任务栈的信息，只返回最近的一条
						// 内容就是当前正在运行的进程所对应的任务栈
						List<RunningTaskInfo> taskinfos = am.getRunningTasks(1);
						RunningTaskInfo currentTast = taskinfos.get(0);
						// 获取当前用户可见的activity所在程序的包名
						String packname = currentTast.topActivity
								.getPackageName();
						// Log.i(TAG, "当前正在运行" + packname);

						if (lockapps.contains(packname)) {
							// 需要保护的，然后弹出输入密码的对话框
							Log.i(TAG, "需要锁定" + packname);

							// 当用户输入正确密码第二次看到用户程序的界面时，不要弹出输入密码的框
							if (tempUnprotect.contains(packname)) {
								sleep(1000);
								continue;
							}
							protectAppIntent.putExtra("packname", packname);
							startActivity(protectAppIntent);
						} else {
							// 放行执行
							Log.i(TAG, "不需要锁定" + packname);
						}

						sleep(1000);// 每隔1秒钟监视一次
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
	}

	private class MyObserver extends ContentObserver {

		public MyObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			Log.i(TAG, "数据库内容发生了改变");
			// 重新更新lockapps集合里面的内容
			lockapps = dao.findAll();
		}

	}
}
