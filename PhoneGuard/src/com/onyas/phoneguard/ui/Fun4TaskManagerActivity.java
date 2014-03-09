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
			//����ϵͳ�ķ�����ϵͳ��titleʹ�����Ƕ������ʽ
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.taskmanagertitle);
		}
		
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		tv_taskmanager_allprocess = (TextView) findViewById(R.id.tv_taskmanager_allprocess);
		tv_taskmanager_aviamemory = (TextView) findViewById(R.id.tv_taskmanager_aviamemory);
		
		setTitleData();
	}


	/**
	 * ����title������
	 */
	private void setTitleData() {
		tv_taskmanager_allprocess.setText("�����еĽ���:"+getProcessCount());
		tv_taskmanager_aviamemory.setText(getAvailMemory());
	}
	
	
	/**
	 * �õ�ϵͳ�������еĽ��̵ĸ���
	 * @return ������
	 */
	private int getProcessCount(){
		runingprocessInfos = am.getRunningAppProcesses();
		return runingprocessInfos.size();
	}
	
	/**
	 * �õ������ڴ�
	 * @return �����ڴ�
	 */
	private String getAvailMemory(){
		MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		long availSize = outInfo.availMem;
		return "�����ڴ�"+TextFormatter.sizeFormat(availSize);
	}
}
