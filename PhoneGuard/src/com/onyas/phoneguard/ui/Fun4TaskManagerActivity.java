package com.onyas.phoneguard.ui;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.util.TextFormatter;

public class Fun4TaskManagerActivity extends Activity {

	private TextView tv_taskmanager_allprocess;
	private TextView tv_taskmanager_aviamemory;
	private ActivityManager am ;
	private  List<RunningAppProcessInfo> runingprocessInfos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.fun_4taskmanager);
		if(flag){
			//请求系统的服务，让系统的title使用我们定义的样式
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.taskmanagertitle);
		}
		
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		tv_taskmanager_allprocess = (TextView) findViewById(R.id.tv_taskmanager_allprocess);
		tv_taskmanager_aviamemory = (TextView) findViewById(R.id.tv_taskmanager_aviamemory);
		
		setTitleData();
	}


	/**
	 * 设置title的数据
	 */
	private void setTitleData() {
		tv_taskmanager_allprocess.setText("运行中的进程:"+getProcessCount());
		tv_taskmanager_aviamemory.setText(getAvailMemory());
	}
	
	
	/**
	 * 得到系统正在运行的进程的个数
	 * @return 进程数
	 */
	private int getProcessCount(){
		runingprocessInfos = am.getRunningAppProcesses();
		return runingprocessInfos.size();
	}
	
	/**
	 * 得到可用内存
	 * @return 可用内存
	 */
	private String getAvailMemory(){
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availSize = outInfo.availMem;
		return "可用内存"+TextFormatter.sizeFormat(availSize);
	}
}
