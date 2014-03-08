package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.service.WatchDogService;

public class Fun9SettingCenterActivity extends Activity {

	private TextView tv_content;
	private CheckBox cb_status;
	private Intent wdservice;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fun_9setting);

		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean lockappServiceStatus = sp.getBoolean("lockappServiceStatus", false);
		
		
		tv_content = (TextView) findViewById(R.id.tv_setting_servicestate);
		cb_status = (CheckBox) findViewById(R.id.cb_setting_servicechange);
		wdservice = new Intent(this,WatchDogService.class);
		
		
		//根据sp里面保存的状态，初始化复选框的状态
		if(lockappServiceStatus){
			cb_status.setChecked(true);
			tv_content.setText("程序锁服务已开启");
		}else{
			cb_status.setChecked(false);
			tv_content.setText("程序锁服务末开启");
		}
		
		//根据复选状的选中状态，开启或停止服务，并且保存用户的操作到sp
		cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor editor = sp.edit();
				if(isChecked){
					startService(wdservice);
					editor.putBoolean("lockappServiceStatus", true);
					tv_content.setText("程序锁服务已开启");
				}else{
					stopService(wdservice);
					editor.putBoolean("lockappServiceStatus", false);
					tv_content.setText("程序锁服务末开启");
				}
				editor.commit();
			}
		});
	}
	
}
