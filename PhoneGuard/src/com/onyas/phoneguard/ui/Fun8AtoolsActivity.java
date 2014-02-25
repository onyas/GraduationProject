package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.onyas.phoneguard.R;

public class Fun8AtoolsActivity extends Activity implements OnClickListener {

	private TextView tv_atool_query;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fun_8atools);
		
		tv_atool_query = (TextView) findViewById(R.id.tv_atool_query);
		
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
