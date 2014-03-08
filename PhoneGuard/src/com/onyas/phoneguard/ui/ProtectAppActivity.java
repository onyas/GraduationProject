package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.service.IService;
import com.onyas.phoneguard.service.WatchDogService;
import com.onyas.phoneguard.util.MD5Encoder;

public class ProtectAppActivity extends Activity {

	private ImageView iv_appicon;
	private TextView tv_appname;
	private EditText et_password;
	private SharedPreferences sp;
	private String packname;
	private String savedPwd;
	private IService iservice;
	private MyConn myconn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.protectapp);

		iv_appicon = (ImageView) findViewById(R.id.iv_protectapp_icon);
		tv_appname = (TextView) findViewById(R.id.tv_protectapp_name);
		et_password = (EditText) findViewById(R.id.et_protectapp_password);

		//�󶨵�����
		myconn = new MyConn();
		Intent intent = new Intent(this,WatchDogService.class);
		bindService(intent, myconn, BIND_AUTO_CREATE);
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		savedPwd = sp.getString("password", "");// �õ������ֻ������е�����

		// ��ɵ��������ĳ�ʼ��
		packname = getIntent().getStringExtra("packname");// �õ�����
		ApplicationInfo appinfo;
		try {
			appinfo = getPackageManager().getPackageInfo(packname, 0).applicationInfo;
			Drawable appicon = appinfo.loadIcon(getPackageManager());
			String appname = appinfo.loadLabel(getPackageManager()).toString();
			iv_appicon.setImageDrawable(appicon);
			tv_appname.setText(appname);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ȷ����ť�ĵ���¼�
	 * 
	 * @param view
	 */
	public void submit(View view) {
		String password = et_password.getText().toString().trim();
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, "���벻��Ϊ��", Toast.LENGTH_SHORT).show();
			return;
		} else {

			if (MD5Encoder.encoder30(password).equals(savedPwd)) {
				//֪ͨ���Ź�����ʱ��ȡ�����������ı���
				iservice.stopProtectApp(packname);
				finish();
			} else {
				Toast.makeText(this, "�������", Toast.LENGTH_SHORT).show();
				return;
			}

		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//��ֹ���ذ����¼��������·ַ�
		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK){
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private class MyConn implements ServiceConnection{

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			iservice = (IService) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindService(myconn);//�����
	}
	
}
