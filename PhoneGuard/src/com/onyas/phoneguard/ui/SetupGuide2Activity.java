package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.onyas.phoneguard.R;

public class SetupGuide2Activity extends Activity implements OnClickListener {

	private Button bt_next, bt_previous, bt_bind;
	private CheckBox cb_bind;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup2);

		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		bt_next = (Button) this.findViewById(R.id.bt_next);
		bt_previous = (Button) this.findViewById(R.id.bt_previous);
		bt_bind = (Button) this.findViewById(R.id.bt_bind);

		cb_bind = (CheckBox) this.findViewById(R.id.cb_bind);
		
		//���ȣ���ʼ����ѡ���״̬
		String simSerialNum = sp.getString("simSerialNum", null);
		if(simSerialNum!=null){
			cb_bind.setChecked(true);
			cb_bind.setText("�Ѿ���");
		}else{
			cb_bind.setChecked(false);
			cb_bind.setText("û�а�");
			removeSimSerialNum();
		}
		
		cb_bind.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if(isChecked){
					cb_bind.setText("�Ѿ���");
				}else{
					cb_bind.setText("û�а�");
					removeSimSerialNum();
				}

			}
		});

		bt_next.setOnClickListener(this);
		bt_previous.setOnClickListener(this);
		bt_bind.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_bind://�����ʱ
			saveSimSerialNum();
			cb_bind.setChecked(true);
			cb_bind.setText("�Ѿ���");
			break;
		case R.id.bt_next:
			Intent intent3 = new Intent(this, SetupGuide3Activity.class);
			finish();
			startActivity(intent3);
			// Activity�л�ʱ�Ķ���Ч��
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		case R.id.bt_previous:
			Intent intent1 = new Intent(this, SetupGuideActivity.class);
			finish();
			startActivity(intent1);
			// Activity�л�ʱ�Ķ���Ч��
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		}

	}

	/**
	 * ����SIM�����кŵ�SharedPreferences
	 */
	private void saveSimSerialNum() {
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		String simserialNum = tm.getSimSerialNumber();
		Editor editor = sp.edit();
		editor.putString("simSerialNum", simserialNum);
		editor.commit();
	}
	
	/**
	 * ��SharedPreferences�е�simSerialNum�ÿ�
	 */
	private void removeSimSerialNum() {
		Editor editor = sp.edit();
		editor.putString("simSerialNum", null);
		editor.commit();
	}

}
