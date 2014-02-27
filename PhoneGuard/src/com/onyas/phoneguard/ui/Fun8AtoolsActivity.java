package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.service.PhoneNumberService;

public class Fun8AtoolsActivity extends Activity implements OnClickListener {

	private TextView tv_atool_query,tv_atool_serstate,tv_atool_bgcolor;
	private CheckBox cb_atool_change;
	private Intent serviceIntent;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fun_8atools);
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		tv_atool_query = (TextView) findViewById(R.id.tv_atool_query);
		tv_atool_serstate = (TextView) findViewById(R.id.tv_atool_servicestate);
		tv_atool_bgcolor = (TextView) findViewById(R.id.tv_atool_bgcolor);
		cb_atool_change = (CheckBox) findViewById(R.id.cb_atool_servicechange);
		
		serviceIntent = new Intent(this, PhoneNumberService.class);
		
		cb_atool_change.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					startService(serviceIntent);
					tv_atool_serstate.setTextColor(getResources().getColor(R.color.textColor));
					tv_atool_serstate.setText("�����ط����ѿ���");
				}else{
					stopService(serviceIntent);
					tv_atool_serstate.setTextColor(Color.RED);
					tv_atool_serstate.setText("�����ط���δ����");
				}
			}
		});
		
		
		tv_atool_query.setOnClickListener(this);
		tv_atool_bgcolor.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_atool_query:
			Intent attributeQuery = new Intent(this,PhoneAttributeActivity.class);
			startActivity(attributeQuery);
			break;
		case R.id.tv_atool_bgcolor:
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("��������ʾ��ʾ���");
			String[] items = new String[]{"��͸��","������","�����","������","ƻ����"};
			builder.setSingleChoiceItems(items, 2, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Editor editor = sp.edit();
					editor.putInt("locatebgcolor", which);
					editor.commit();
				}
			});
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
				}
			});
			builder.create().show();
			break;
		}
	}
	
}
