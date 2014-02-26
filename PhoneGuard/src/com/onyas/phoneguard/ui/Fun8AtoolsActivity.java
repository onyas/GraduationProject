package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.Intent;
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

	private TextView tv_atool_query;
	private TextView tv_atool_serstate;
	private CheckBox cb_atool_change;
	private Intent serviceIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fun_8atools);
		
		tv_atool_query = (TextView) findViewById(R.id.tv_atool_query);
		tv_atool_serstate = (TextView) findViewById(R.id.tv_atool_servicestate);
		cb_atool_change = (CheckBox) findViewById(R.id.cb_atool_servicechange);
		
		serviceIntent = new Intent(this, PhoneNumberService.class);
		
		cb_atool_change.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					startService(serviceIntent);
					tv_atool_serstate.setTextColor(getResources().getColor(R.color.textColor));
					tv_atool_serstate.setText("归属地服务已开启");
				}else{
					stopService(serviceIntent);
					tv_atool_serstate.setTextColor(Color.RED);
					tv_atool_serstate.setText("归属地服务未开启");
				}
			}
		});
		
		
		tv_atool_query.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_atool_query:
			Intent attributeQuery = new Intent(this,PhoneAttributeActivity.class);
			startActivity(attributeQuery);
			break;
		}
	}
	
}
