package com.onyas.phoneguard.ui;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.domain.TaskInfo;
import com.onyas.phoneguard.engine.TaskInfoEngine;
import com.onyas.phoneguard.util.TextFormatter;

public class Fun4TaskManagerActivity extends Activity {

	private TextView tv_taskmanager_allprocess;// ���н��̵ĸ���
	private TextView tv_taskmanager_aviamemory;// �ܵĿ����ڴ�
	private ActivityManager am;
	private List<RunningAppProcessInfo> runingprocessInfos;
	private ListView lv_taskmanager_list;
	private LinearLayout ll_taskmanager_loading;
	private TaskInfoEngine taskInfoEngine;
	private List<TaskInfo> taskInfos;// �������еĽ��̵ļ���
	private TaskInfoAdapter adapter;
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ll_taskmanager_loading.setVisibility(View.INVISIBLE);
			lv_taskmanager_list.setAdapter(adapter);
		}
		
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.fun_4taskmanager);
		if (flag) {
			// ����ϵͳ�ķ�����ϵͳ��titleʹ�����Ƕ������ʽ
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.taskmanagertitle);
		}

		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		taskInfoEngine = new TaskInfoEngine(this);
		adapter = new TaskInfoAdapter();
		tv_taskmanager_allprocess = (TextView) findViewById(R.id.tv_taskmanager_allprocess);
		tv_taskmanager_aviamemory = (TextView) findViewById(R.id.tv_taskmanager_aviamemory);
		lv_taskmanager_list = (ListView) findViewById(R.id.lv_taskmanager_list);
		ll_taskmanager_loading = (LinearLayout) findViewById(R.id.ll_taskmanager_loading);

		fillData();

	}

	/**
	 * ��listView��title��������
	 */
	private void fillData() {
		setTitleData();
		ll_taskmanager_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				taskInfos = taskInfoEngine.getAllTasks();
				handler.sendEmptyMessage(0);
			};
		}.start();

	}

	/**
	 * ����title������
	 */
	private void setTitleData() {
		tv_taskmanager_allprocess.setText("�����еĽ���:" + getProcessCount());
		tv_taskmanager_aviamemory.setText(getAvailMemory());
	}

	/**
	 * �õ�ϵͳ�������еĽ��̵ĸ���
	 * 
	 * @return ������
	 */
	private int getProcessCount() {
		runingprocessInfos = am.getRunningAppProcesses();
		return runingprocessInfos.size();
	}

	/**
	 * �õ������ڴ�
	 * 
	 * @return �����ڴ�
	 */
	private String getAvailMemory() {
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availSize = outInfo.availMem;
		return "�����ڴ�" + TextFormatter.sizeFormat(availSize);
	}

	/**
	 * ɱ������ѡ�еĽ���
	 * 
	 * @param view
	 */
	public void clean(View view) {

	}

	/**
	 * ������̹���Ľ���
	 * 
	 * @param view
	 */
	public void setting(View view) {

	}

	private class TaskInfoAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return taskInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return taskInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TaskInfo taskInfo = taskInfos.get(position);
			View view = View.inflate(getApplicationContext(),
					R.layout.taskmanageritem, null);
			ViewHolder holder = new ViewHolder();
			holder.iv_icon = (ImageView) view.findViewById(R.id.iv_task_icon);
			holder.tv_name = (TextView) view.findViewById(R.id.tv_task_name);
			holder.tv_memorysize = (TextView) view
					.findViewById(R.id.tv_task_memorysize);
			holder.cb_status = (CheckBox) view
					.findViewById(R.id.cb_taskitem_checked);
			holder.iv_icon.setImageDrawable(taskInfo.getAppicon());
			holder.tv_name.setText(taskInfo.getAppname());
			holder.tv_memorysize.setText("ռ���ڴ�"+TextFormatter.kbFormat(taskInfo.getMemorysize()));
			holder.cb_status.setChecked(taskInfo.isChecked());
			return view;
		}

	}

	static class ViewHolder {
		public ImageView iv_icon;
		public TextView tv_name;
		public TextView tv_memorysize;
		public CheckBox cb_status;
	}
}
