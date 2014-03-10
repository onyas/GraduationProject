package com.onyas.phoneguard.ui;

import java.util.ArrayList;
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

	private TextView tv_taskmanager_allprocess;// 所有进程的个数
	private TextView tv_taskmanager_aviamemory;// 总的可用内存
	private ActivityManager am;
	private List<RunningAppProcessInfo> runingprocessInfos;
	private ListView lv_taskmanager_list;
	private LinearLayout ll_taskmanager_loading;
	private TaskInfoEngine taskInfoEngine;
	private List<TaskInfo> taskInfos;// 正在运行的进程的集合
	private TaskInfoAdapter adapter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ll_taskmanager_loading.setVisibility(View.INVISIBLE);
			adapter = new TaskInfoAdapter();
			lv_taskmanager_list.setAdapter(adapter);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.fun_4taskmanager);
		if (flag) {
			// 请求系统的服务，让系统的title使用我们定义的样式
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.taskmanagertitle);
		}

		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		taskInfoEngine = new TaskInfoEngine(this);

		tv_taskmanager_allprocess = (TextView) findViewById(R.id.tv_taskmanager_allprocess);
		tv_taskmanager_aviamemory = (TextView) findViewById(R.id.tv_taskmanager_aviamemory);
		lv_taskmanager_list = (ListView) findViewById(R.id.lv_taskmanager_list);
		ll_taskmanager_loading = (LinearLayout) findViewById(R.id.ll_taskmanager_loading);

		fillData();

	}

	/**
	 * 给listView和title设置数据
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
	 * 设置title的数据
	 */
	private void setTitleData() {
		tv_taskmanager_allprocess.setText("运行中的进程:" + getProcessCount());
		tv_taskmanager_aviamemory.setText(getAvailMemory());
	}

	/**
	 * 得到系统正在运行的进程的个数
	 * 
	 * @return 进程数
	 */
	private int getProcessCount() {
		runingprocessInfos = am.getRunningAppProcesses();
		return runingprocessInfos.size();
	}

	/**
	 * 得到可用内存
	 * 
	 * @return 可用内存
	 */
	private String getAvailMemory() {
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availSize = outInfo.availMem;
		return "可用内存" + TextFormatter.sizeFormat(availSize);
	}

	/**
	 * 杀死所有选中的进程
	 * 
	 * @param view
	 */
	public void clean(View view) {

	}

	/**
	 * 进入进程管理的界面
	 * 
	 * @param view
	 */
	public void setting(View view) {

	}

	private class TaskInfoAdapter extends BaseAdapter {

		private List<TaskInfo> userTaskInfos;
		private List<TaskInfo> systemTaskInfos;

		/**
		 * 在构造方法里面完成了用户列表和系统程序列表的区分
		 */
		public TaskInfoAdapter() {
			userTaskInfos = new ArrayList<TaskInfo>();
			systemTaskInfos = new ArrayList<TaskInfo>();

			for (TaskInfo taskinfo : taskInfos) {
				if (taskinfo.isSystemapp()) {
					systemTaskInfos.add(taskinfo);
				} else {
					userTaskInfos.add(taskinfo);
				}
			}

		}

		@Override
		public int getCount() {
			return taskInfos.size() + 2;// 加2，代表的是两个TextView,(用户进程，系统进程)
		}

		@Override
		public Object getItem(int position) {
			if (position == 0) {
				return position;
			} else if (position <= userTaskInfos.size()) {
				return userTaskInfos.get(position - 1);
			} else if (position == userTaskInfos.size() + 1) {
				return position;
			} else if (position <= taskInfos.size() + 2) {
				return systemTaskInfos.get(position - userTaskInfos.size() - 2);
			} else {
				return position;
			}
		}

		@Override
		public long getItemId(int position) {
			if (position == 0) {
				return -1;
			} else if (position <= userTaskInfos.size()) {
				return (position - 1);
			} else if (position == userTaskInfos.size() + 1) {
				return -1;
			} else if (position <= taskInfos.size() + 2) {
				return (position - userTaskInfos.size() - 2);
			} else {
				return -1;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// 把这些条目信息做一下分类，分成系统进程和用户进程

			if (position == 0) {
				TextView tv_user = new TextView(Fun4TaskManagerActivity.this);
				tv_user.setText("用户进程" + userTaskInfos.size() + "个");
				return tv_user;
			} else if (position <= userTaskInfos.size()) {// 用户进程的显示
				int currentPosition = position - 1;
				TaskInfo taskInfo = userTaskInfos.get(currentPosition);
				View view = View.inflate(getApplicationContext(),
						R.layout.taskmanageritem, null);
				ViewHolder holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_task_icon);
				holder.tv_name = (TextView) view
						.findViewById(R.id.tv_task_name);
				holder.tv_memorysize = (TextView) view
						.findViewById(R.id.tv_task_memorysize);
				holder.cb_status = (CheckBox) view
						.findViewById(R.id.cb_taskitem_checked);
				holder.iv_icon.setImageDrawable(taskInfo.getAppicon());
				holder.tv_name.setText(taskInfo.getAppname());
				holder.tv_memorysize.setText("占用内存"
						+ TextFormatter.kbFormat(taskInfo.getMemorysize()));
				holder.cb_status.setChecked(taskInfo.isChecked());
				return view;

			} else if (position == userTaskInfos.size() + 1) {
				TextView tv_sys = new TextView(Fun4TaskManagerActivity.this);
				tv_sys.setText("系统进程" + systemTaskInfos.size() + "个");
				return tv_sys;

			} else if (position <= taskInfos.size() + 2) {// 系统进程的显示
				int currentPosition = position - userTaskInfos.size() - 2;
				TaskInfo taskInfo = systemTaskInfos.get(currentPosition);
				View view = View.inflate(getApplicationContext(),
						R.layout.taskmanageritem, null);
				ViewHolder holder = new ViewHolder();
				holder.iv_icon = (ImageView) view
						.findViewById(R.id.iv_task_icon);
				holder.tv_name = (TextView) view
						.findViewById(R.id.tv_task_name);
				holder.tv_memorysize = (TextView) view
						.findViewById(R.id.tv_task_memorysize);
				holder.cb_status = (CheckBox) view
						.findViewById(R.id.cb_taskitem_checked);
				holder.iv_icon.setImageDrawable(taskInfo.getAppicon());
				holder.tv_name.setText(taskInfo.getAppname());
				holder.tv_memorysize.setText("占用内存"
						+ TextFormatter.kbFormat(taskInfo.getMemorysize()));
				holder.cb_status.setChecked(taskInfo.isChecked());
				return view;
			} else {
				return null;
			}

			/*
			 * TaskInfo taskInfo = taskInfos.get(position); View view =
			 * View.inflate(getApplicationContext(), R.layout.taskmanageritem,
			 * null); ViewHolder holder = new ViewHolder(); holder.iv_icon =
			 * (ImageView) view.findViewById(R.id.iv_task_icon); holder.tv_name
			 * = (TextView) view.findViewById(R.id.tv_task_name);
			 * holder.tv_memorysize = (TextView) view
			 * .findViewById(R.id.tv_task_memorysize); holder.cb_status =
			 * (CheckBox) view .findViewById(R.id.cb_taskitem_checked);
			 * holder.iv_icon.setImageDrawable(taskInfo.getAppicon());
			 * holder.tv_name.setText(taskInfo.getAppname());
			 * holder.tv_memorysize
			 * .setText("占用内存"+TextFormatter.kbFormat(taskInfo
			 * .getMemorysize()));
			 * holder.cb_status.setChecked(taskInfo.isChecked()); return view;
			 */
		}

	}

	static class ViewHolder {
		public ImageView iv_icon;
		public TextView tv_name;
		public TextView tv_memorysize;
		public CheckBox cb_status;
	}
}
