package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.util.MD5Encoder;

public class ProtectAppActivity extends Activity {

	private ImageView iv_appicon;
	private TextView tv_appname;
	private EditText et_password;
	private SharedPreferences sp;
	private String savedPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.protectapp);

		iv_appicon = (ImageView) findViewById(R.id.iv_protectapp_icon);
		tv_appname = (TextView) findViewById(R.id.tv_protectapp_name);
		et_password = (EditText) findViewById(R.id.et_protectapp_password);

		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		savedPwd = sp.getString("password", "");// �õ������ֻ������е�����

		// ��ɵ��������ĳ�ʼ��
		String packname = getIntent().getStringExtra("packname");// �õ�����
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
				finish();
			} else {
				Toast.makeText(this, "�������", Toast.LENGTH_SHORT).show();
				return;
			}

		}
	}
}
