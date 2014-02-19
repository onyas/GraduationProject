package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.onyas.phoneguard.R;

public class SplashActivity extends Activity {

	private TextView tv_version;
	private LinearLayout ll_main;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		//完成窗体的全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		ll_main = (LinearLayout) this.findViewById(R.id.ll_splash_main);
		tv_version = (TextView) this.findViewById(R.id.tv_splash_version);
		tv_version.setText(getVersion());
		
		AlphaAnimation aa = new AlphaAnimation(0.0f,1.0f); 
		aa.setDuration(1000);
		
		ll_main.startAnimation(aa);
	}

	/**
	 * 获取当前应用的版本号
	 * @return
	 */
	private String getVersion()
	{
		try {
			PackageManager manager = getPackageManager();
			PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
			return info.versionName;
		} catch (Exception e) {
			e.printStackTrace();
			return "版本号未知";
		}
	}
}
