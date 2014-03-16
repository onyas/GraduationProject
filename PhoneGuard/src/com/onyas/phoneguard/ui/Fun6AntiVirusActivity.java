package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.onyas.phoneguard.R;

public class Fun6AntiVirusActivity extends Activity {

	private ImageView iv_animate;
	private AnimationDrawable ad;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fun_6antivirtus);

		iv_animate = (ImageView) findViewById(R.id.iv_animate);

		iv_animate.setBackgroundResource(R.drawable.anti_anim);
		ad = (AnimationDrawable) iv_animate.getBackground();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			ad.start();
		}

		return super.onTouchEvent(event);
	}
}
