package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onyas.phoneguard.R;

public class SetupGuide3Activity extends Activity implements OnClickListener {

	private Button bt_contact, bt_next, bt_previous;
	private EditText et_safe_num;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup3);

		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		bt_contact = (Button) findViewById(R.id.bt_select_contact);
		bt_next = (Button) findViewById(R.id.bt_next);
		bt_previous = (Button) findViewById(R.id.bt_previous);
		et_safe_num = (EditText) findViewById(R.id.et_safe_number);

		bt_contact.setOnClickListener(this);
		bt_next.setOnClickListener(this);
		bt_previous.setOnClickListener(this);

	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			String number = data.getStringExtra("number");
			et_safe_num.setText(number);
		}
	}
	
	
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_select_contact:
			Intent intent = new Intent(this, SelectContactsActivity.class);
			startActivityForResult(intent, 0);
			break;
		case R.id.bt_next:
			String number = et_safe_num.getText().toString().trim();
			if(TextUtils.isEmpty(number)){
				Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
				return ;
			}else{
				Editor editor = sp.edit();
				editor.putString("safeNumber", number);
				editor.commit();
			}
			Intent intent4 = new Intent(this, SetupGuide4Activity.class);
			finish();
			startActivity(intent4);
			// Activity切换时的动画效果
			overridePendingTransition(R.anim.translate_in, R.anim.translate_out);
			break;
		case R.id.bt_previous:
			Intent intent2 = new Intent(this, SetupGuide2Activity.class);
			finish();
			startActivity(intent2);
			// Activity切换时的动画效果
			overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
			break;
		}
	}

}
