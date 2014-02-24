package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.receiver.MyAdminReceiver;

public class SetupGuide4Activity extends Activity implements OnClickListener {

	private Button bt_setup_finish, bt_previous;
	private CheckBox cb_open_protect;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup4);

		bt_setup_finish = (Button) findViewById(R.id.bt_setup_finish);
		bt_previous = (Button) findViewById(R.id.bt_previous);
		cb_open_protect = (CheckBox) findViewById(R.id.cb_open_protect);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);

		bt_setup_finish.setOnClickListener(this);
		bt_previous.setOnClickListener(this);

		// ��ʼ��checkbox��״̬
		boolean isprotecting = sp.getBoolean("isprotecting", false);
		if (isprotecting) {
			cb_open_protect.setChecked(true);
			cb_open_protect.setText("�ֻ������ѿ���");
		} else {
			cb_open_protect.setChecked(false);
			cb_open_protect.setText("�ֻ�����ĩ����");
		}
		cb_open_protect
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							cb_open_protect.setText("�ֻ������ѿ���");
							Editor editor = sp.edit();
							editor.putBoolean("isprotecting", true);
							editor.commit();
						} else {
							cb_open_protect.setText("�ֻ�����ĩ����");
							Editor editor = sp.edit();
							editor.putBoolean("isprotecting", false);
							editor.commit();
						}
					}
				});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_previous:
			Intent intent3 = new Intent(this, SetupGuide3Activity.class);
			finish();
			startActivity(intent3);
			// Activity�л�ʱ�Ķ���Ч��
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;

		case R.id.bt_setup_finish:// �������
			if (cb_open_protect.isChecked()) {
				finishSetupGuide();
				finish();
			} else {
				Toast.makeText(this, "ǿ�ҽ��鿪���ֻ�����", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	/**
	 * �����������
	 */
	private void finishSetupGuide() {
		Editor editor = sp.edit();
		editor.putBoolean("alreadSetup", true);// �����������
		editor.putBoolean("isprotecting", true);// �����ֻ�����
		editor.commit();

		//ע��㲥������Ϊadmin�豸
		DevicePolicyManager manager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		ComponentName mAdminName = new ComponentName(this, MyAdminReceiver.class);
		if (manager != null) {
			if (!manager.isAdminActive(mAdminName)) {
				Intent intent = new Intent(
						DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
						mAdminName);
				startActivity(intent);
			}
		}
	}

}
