package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.onyas.phoneguard.R;

public class SetupGuideActivity extends Activity implements OnClickListener {

	private Button bt_next;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup1);
		
		bt_next = (Button) this.findViewById(R.id.bt_next);
		bt_next.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_next:
			Intent intent = new Intent(this,SetupGuide2Activity.class);
			finish();
			startActivity(intent);
			//Activity切换时的动画效果
			overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
			break;
		}
	}
	
}
