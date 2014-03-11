package com.onyas.phoneguard.ui;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.onyas.phoneguard.MyApplication;
import com.onyas.phoneguard.R;
import com.onyas.phoneguard.domain.TaskInfo;

public class AppPermissionActivity extends Activity {

	private TextView tv_name, tv_packname;
	private ScrollView sv_permission;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.apppermission);

		tv_name = (TextView) findViewById(R.id.tv_apppermission_name);
		tv_packname = (TextView) findViewById(R.id.tv_apppermission_packname);
		sv_permission = (ScrollView) findViewById(R.id.sv_permission);

		MyApplication myapp = (MyApplication) getApplication();
		TaskInfo taskinfo = myapp.taskinfo;// ��Ӧ�ó����������еõ����������

		tv_name.setText(taskinfo.getAppname());
		String packname = taskinfo.getPackname();
		tv_packname.setText(packname);

		// �鿴setting��Դ���룬������Ȩ���б���AppSecurityPermissions.getPermissionsView��������õ���
		// ��Ϊsdk��������AppSecurityPermissions������ͨ���������õ�Ȩ���б�
		try {
			Class clazz = getClass().getClassLoader().loadClass(
					"android.widget.AppSecurityPermissions");

			Constructor constructor = clazz.getConstructor(new Class[] {
					Context.class, String.class });

			Object object = constructor.newInstance(new Object[] { this,
					packname });

			Method method = clazz.getDeclaredMethod("getPermissionsView",
					new Class[] {});

			View view = (View) method.invoke(object, new Object[] {});

			sv_permission.addView(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		myapp.taskinfo = null;

	}

}
