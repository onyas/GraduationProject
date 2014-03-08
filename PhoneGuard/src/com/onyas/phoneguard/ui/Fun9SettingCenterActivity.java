package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.Intent;
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fun_9setting);
		
		tv_content = (TextView) findViewById(R.id.tv_setting_servicestate);
		cb_status = (CheckBox) findViewById(R.id.cb_setting_servicechange);
		wdservice = new Intent(this,WatchDogService.class);
		
		if(cb_status.isChecked()){
			tv_content.setText("程序锁服务已开启");
		}else{
			tv_content.setText("程序锁服务末开启");
		}
		
		cb_status.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					startService(wdservice);
				}else{
					stopService(wdservice);
				}
			}
		});
	}
	
}
