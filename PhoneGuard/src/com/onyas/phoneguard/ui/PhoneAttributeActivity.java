package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.onyas.phoneguard.R;

public class PhoneAttributeActivity extends Activity {

	private EditText et_query_number;
	private TextView tv_phone_attribute;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.attributequery);
		
		et_query_number = (EditText) findViewById(R.id.et_query_number);
		tv_phone_attribute = (TextView) findViewById(R.id.tv_phone_attribute);
		
	}
	
	
	public void query(View v){
		
	}
}
