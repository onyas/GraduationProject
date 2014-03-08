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
	 * �����һ�δ�����ʱ��͵���
	 */
	@Override
	public void onCreate() {
		dao = new AppLockDao(this);
		lockapps = dao.findAll();// �õ�����Ҫ���ܵ�Ӧ�ó���

		am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);

		new Thread() {
			public void run() {

				// �������Ź�
				while (true) {
					try {
						// �õ���ǰ�������г���İ���
						// ����ϵͳ��������ջ����Ϣ��ֻ���������һ��
						// ���ݾ��ǵ�ǰ�������еĽ�������Ӧ������ջ
						List<RunningTaskInfo> taskinfos = am.getRunningTasks(1);
						RunningTaskInfo currentTast = taskinfos.get(0);
						// ��ȡ��ǰ�û��ɼ���activity���ڳ���İ���
						String packname = currentTast.topActivity
								.getPackageName();
						Log.i(TAG, "��ǰ��������" + packname);

						if (lockapps.contains(packname)) {
							// ��Ҫ�����ģ�Ȼ�󵯳���������ĶԻ���
							Log.i(TAG, "��Ҫ����" + packname);
							
							
						} else {
							// ����ִ��
						}

						
						sleep(1000);//ÿ��1���Ӽ���һ��
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

}
