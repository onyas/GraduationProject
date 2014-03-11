package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.onyas.phoneguard.R;

public class TaskSettingActivity extends Activity {

	private CheckBox cb_show_systeminfo, cb_auto_clean;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasksetting);

		cb_show_systeminfo = (CheckBox) findViewById(R.id.cb_task_setting_status);
		cb_auto_clean = (CheckBox) findViewById(R.id.cb_task_auto_clean);
		//�Ƿ���ʾϵͳ����
		sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean showsystemprocess = sp.getBoolean("showsystemprocess", true);

		if (showsystemprocess) {
			cb_show_systeminfo.setChecked(true);
		} else {
			cb_show_systeminfo.setChecked(false);
		}
		cb_show_systeminfo
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							Editor editor = sp.edit();
							editor.putBoolean("showsystemprocess", true);
							editor.commit();
							setResult(200);// ���÷���ֵΪ200��ֻ�е���ѡ���״̬�ı�ʱ��ǰһ��activity��ȥˢ������
						} else {
							Editor editor = sp.edit();
							editor.putBoolean("showsystemprocess", false);
							editor.commit();
							setResult(200);
						}
					}
				});
		//����ʱ�Զ��������
		boolean autoclean = sp.getBoolean("autoclean", false);
		if (autoclean) {
			cb_auto_clean.setChecked(true);
		} else {
			cb_auto_clean.setChecked(false);
		}
		cb_auto_clean.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					Editor editor = sp.edit();
					editor.putBoolean("autoclean", true);
					editor.commit();
				} else {
					Editor editor = sp.edit();
					editor.putBoolean("autoclean", false);
					editor.commit();
				}
			}
		});

	}

}
