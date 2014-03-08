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
	private boolean flag;// ���ڱ�ʶ�Ƿ�Ҫֹͣ�߳�
	private List<String> tempUnprotect;// ��ʱֹͣ�����ĳ���ļ���
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
	 * ��ʱֹͣ��Ӧ�ó���ı���
	 * 
	 * @param packname
	 */
	public void stopProtect(String packname) {
		tempUnprotect.add(packname);
	}

	/**
	 * ���¿�����Ӧ�ó���ı���
	 * 
	 * @param packname
	 */
	public void startProtect(String packname) {
		if (tempUnprotect.contains(packname)) {
			tempUnprotect.remove(packname);
		}
	}

	/**
	 * �����һ�δ�����ʱ��͵���
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
		lockapps = dao.findAll();// �õ�����Ҫ���ܵ�Ӧ�ó���
		protectAppIntent = new Intent(this, ProtectAppActivity.class);
		// �������治��������ջ�����Ҫ�ڷ������漤��һ��activity,��Ҫ�������Flag������ֻ���ʿ������ջ������֮ǰ������ջ��
		// �����ProtectAppActivity�ŵ�һ���µ�����ջ��������ŵ��ֻ���ʿ���棬��Ϊ��activity��Single
		// Instance����ģʽ,�����������ջ��ֻ��һ��activity��ʵ��
		protectAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		flag = true;
		am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);

		new Thread() {
			public void run() {

				// �������Ź�
				while (flag) {
					try {
						// �õ���ǰ�������г���İ���
						// ����ϵͳ��������ջ����Ϣ��ֻ���������һ��
						// ���ݾ��ǵ�ǰ�������еĽ�������Ӧ������ջ
						List<RunningTaskInfo> taskinfos = am.getRunningTasks(1);
						RunningTaskInfo currentTast = taskinfos.get(0);
						// ��ȡ��ǰ�û��ɼ���activity���ڳ���İ���
						String packname = currentTast.topActivity
								.getPackageName();
						// Log.i(TAG, "��ǰ��������" + packname);

						if (lockapps.contains(packname)) {
							// ��Ҫ�����ģ�Ȼ�󵯳���������ĶԻ���
							Log.i(TAG, "��Ҫ����" + packname);

							// ���û�������ȷ����ڶ��ο����û�����Ľ���ʱ����Ҫ������������Ŀ�
							if (tempUnprotect.contains(packname)) {
								sleep(1000);
								continue;
							}
							protectAppIntent.putExtra("packname", packname);
							startActivity(protectAppIntent);
						} else {
							// ����ִ��
							Log.i(TAG, "����Ҫ����" + packname);
						}

						sleep(1000);// ÿ��1���Ӽ���һ��
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
			Log.i(TAG, "���ݿ����ݷ����˸ı�");
			// ���¸���lockapps�������������
			lockapps = dao.findAll();
		}

	}
}
