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
	 * �õ����е��������еĽ��̵���Ϣ
	 * 
	 * @return
	 */
	public List<TaskInfo> getAllTasks() {
		List<TaskInfo> taskinfos = new ArrayList<TaskInfo>();
		for (RunningAppProcessInfo info : runingProcessinfos) {
			TaskInfo taskinfo;
			try {
				int pid = info.pid;// �õ����̵�id
				String packname = info.processName;// �õ����̵İ���
				ApplicationInfo appinfo = pm.getPackageInfo(packname, 0).applicationInfo;// ���ݰ����õ�packageManager,�����õ�ApplicationInfo
				Drawable appicon = appinfo.loadIcon(pm);// �õ������ͼ��
				String appname = appinfo.loadLabel(pm).toString();// �õ����������
				MemoryInfo[] memoryinfos = am
						.getProcessMemoryInfo(new int[] { pid });
				int memorysize = memoryinfos[0].getTotalPrivateDirty();// �õ�����ռ�õ��ڴ�
				taskinfo = new TaskInfo(appname, appicon, pid, memorysize,
						packname, false);
				//�ж��Ƿ�ΪϵͳӦ�ó���
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
