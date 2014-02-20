package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.domain.UpdateInfo;
import com.onyas.phoneguard.engine.UpdateInfoEngine;

public class SplashActivity extends Activity {

	private static final String TAG = "SplashActivity";
	private TextView tv_version;
	private LinearLayout ll_main;
	private UpdateInfo info;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				showUpdateDialog();
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 取消标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		// 完成窗体的全屏显示
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		ll_main = (LinearLayout) this.findViewById(R.id.ll_splash_main);
		tv_version = (TextView) this.findViewById(R.id.tv_splash_version);
		String version = getVersion();
		tv_version.setText(version);

		new Thread(new VersionThread()).start();

		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(1000);

		ll_main.startAnimation(aa);
	}

	/**
	 * 开启新的线程用于检查版本号是否一致
	 * 
	 * @author Administrator
	 * 
	 */
	public class VersionThread implements Runnable {

		@Override
		public void run() {
			if (isNeedUpdate(getVersion())) {
				Log.i(TAG, "弹出升级对话框");
				Message msg = new Message();
				msg.what=1;
				handler.sendMessage(msg);
			}
		}

	}

	/**
	 * 弹出升级对话框
	 */
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(R.drawable.icon5);
		builder.setTitle("升级得醒");
		builder.setMessage(info.getDescription());
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "下载apk文件" + info.getApkurl());
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "用户取消，进入主界面");
				loadMainUI();
			}
		});

		builder.create().show();
	}

	/**
	 * 
	 * @param version
	 *            当前客户端的版本信息
	 * @return 是否需要更新
	 */
	private boolean isNeedUpdate(String version) {
		UpdateInfoEngine updateEngine = new UpdateInfoEngine(this);
		try {
			info = updateEngine.getUpdateInfo(R.string.apkserver);
			String serverVersion = info.getVersion();
			if (serverVersion.equals(version)) {
				Log.i(TAG, "版本号相同，不需要升级，直接进入主界面");
				loadMainUI();
				return false;
			} else {
				Log.i(TAG, "版本号不相同，需要升级");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "获取服务器信息异常,进入主界面", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "获取服务器信息异常,进入主界面");
			loadMainUI();
			return false;
		}
	}

	/**
	 * 进入主界面
	 */
	private void loadMainUI() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	/**
	 * 获取当前应用的版本号
	 * 
	 * @return
	 */
	private String getVersion() {
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
