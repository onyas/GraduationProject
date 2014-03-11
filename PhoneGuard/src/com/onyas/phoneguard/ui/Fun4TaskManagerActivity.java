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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;
	private long totalused;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			ll_taskmanager_loading.setVisibility(View.INVISIBLE);
			adapter = new TaskInfoAdapter();
			lv_taskmanager_list.setAdapter(adapter);
			String avail = TextFormatter.sizeFormat(getAvailMemory());
			String total = TextFormatter.sizeFormat(totalused * 1024
					+ getAvailMemory());
			tv_taskmanager_aviamemory
					.setText("可用\\总内存" + avail + "\\" + total);
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

		lv_taskmanager_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox cb_status = (CheckBox) view
						.findViewById(R.id.cb_taskitem_checked);
				Object obj = lv_taskmanager_list.getItemAtPosition(position);
				if (obj instanceof TaskInfo) {
					TaskInfo taskinfo = (TaskInfo) obj;
					if (taskinfo.getPackname().equals(getPackageName())
							|| taskinfo.getPackname().equals("system")
							|| taskinfo.getPackname().equals(
									"android.process.media")) {
						cb_status.setVisibility(View.INVISIBLE);
						return;
					}
					if (taskinfo.isChecked()) {
						taskinfo.setChecked(false);
						cb_status.setChecked(false);// 更改cb的状态
					} else {
						taskinfo.setChecked(true);
						cb_status.setChecked(true);
					}
				}
			}
		});

		fillData();
	}

	/**
	 * 给listView和title设置数据
	 */
	private void fillData() {
		setTitleData();
		ll_taskmanager_loading.setVisibility(View.VISIBLE);
		totalused = 0;
		new Thread() {
			public void run() {
				taskInfos = taskInfoEngine.getAllTasks();
				for (TaskInfo info : taskInfos) {
					totalused += info.getMemorysize();
				}
				handler.sendEmptyMessage(0);
			};
		}.start();

	}

	/**
	 * 设置title的数据
	 */
	private void setTitleData() {
		tv_taskmanager_allprocess.setText("运行中的进程:" + getProcessCount());
		tv_taskmanager_aviamemory.setText("剩余内存"
				+ TextFormatter.sizeFormat(getAvailMemory()));
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
	private long getAvailMemory() {
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	/**
	 * 杀死所有选中的进程
	 * 
	 * @param view
	 */
	public void clean(View view) {

		int count = 0;
		int memsize = 0;
		for (TaskInfo info : userTaskInfos) {
			if (info.isChecked()) {
				memsize += info.getMemorysize();
				am.killBackgroundProcesses(info.getPackname());
				count++;
				taskInfos.remove(info);
			}
		}

		for (TaskInfo info : systemTaskInfos) {
			if (info.isChecked()) {
				memsize += info.getMemorysize();
				am.killBackgroundProcesses(info.getPackname());
				count++;
				taskInfos.remove(info);
			}
		}
		String size = TextFormatter.kbFormat(memsize);
		Toast.makeText(this, "杀死了" + count + "个进程,释放了" + size + "资源",
				Toast.LENGTH_SHORT).show();
		// 更新listView,只是把taskinfo从taskinfos集合中移除
		adapter = new TaskInfoAdapter();
		lv_taskmanager_list.setAdapter(adapter);
	}

	/**
	 * 进入进程管理的界面
	 * 
	 * @param view
	 */
	public void setting(View view) {

	}

	private class TaskInfoAdapter extends BaseAdapter {

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
				String packname = taskInfo.getPackname();
				if (packname.equals(getPackageName())
						|| packname.equals("system")
						|| packname.equals("android.process.media")) {
					holder.cb_status.setVisibility(View.INVISIBLE);
				} else {
					holder.cb_status.setVisibility(View.VISIBLE);
				}
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
				String packname = taskInfo.getPackname();
				if (packname.equals(getPackageName())
						|| packname.equals("system")
						|| packname.equals("android.process.media")) {
					holder.cb_status.setVisibility(View.INVISIBLE);
				} else {
					holder.cb_status.setVisibility(View.VISIBLE);
				}
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
