package com.onyas.phoneguard.ui;

import com.onyas.phoneguard.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class Fun4TaskManagerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boolean flag = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.fun_4taskmanager);
		if(flag){
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.taskmanagertitle);
		}
	}
	
}
