package com.onyas.phoneguard.ui;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
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
import com.onyas.phoneguard.engine.DownLoadFileTask;
import com.onyas.phoneguard.engine.UpdateInfoEngine;

public class SplashActivity extends Activity {

	private static final String TAG = "SplashActivity";
	private TextView tv_version;
	private LinearLayout ll_main;
	private ProgressDialog pd;
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
		// ȡ��������
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		// ��ɴ����ȫ����ʾ
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		ll_main = (LinearLayout) this.findViewById(R.id.ll_splash_main);
		tv_version = (TextView) this.findViewById(R.id.tv_splash_version);
		pd = new ProgressDialog(this);
		pd.setMessage("��������...");
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		String version = getVersion();
		tv_version.setText(version);

		new Thread(new GetVersionThread()).start();

		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);

		ll_main.startAnimation(aa);
	}

	/**
	 * �����µ��߳����ڼ��汾���Ƿ�һ��
	 * 
	 * @author Administrator
	 * 
	 */
	private class GetVersionThread implements Runnable {

		@Override
		public void run() {

			try {
				Thread.sleep(2000);
				if (isNeedUpdate(getVersion())) {
					Log.i(TAG, "���������Ի���");
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
				Looper.prepare();
				Toast.makeText(getApplicationContext(), "��ȡ��������Ϣ�쳣,����������", Toast.LENGTH_SHORT).show();
				Log.i(TAG, "��ȡ��������Ϣ�쳣,����������");
				loadMainUI();
				Looper.loop();
			}
			
			
		}

	}

	/**
	 * ����Ҫ�����ļ����߳�
	 * 
	 * @author Administrator
	 * 
	 */
	private class DownLoadFileThread implements Runnable {

		private String path;// �������ļ���ַ
		private String filePath;// �����ļ���ַ

		public DownLoadFileThread(String path, String filePath) {
			this.path = path;
			this.filePath = filePath;
		}

		@Override
		public void run() {
			try {
				File file = DownLoadFileTask.getFile(path, filePath, pd);
				Log.i(TAG, "�ɹ������ļ���");
				pd.dismiss();
				installApk(file);
			} catch (Exception e) {
				e.printStackTrace();
				pd.dismiss();
				Toast.makeText(getApplicationContext(), "�����ļ�������",
						Toast.LENGTH_SHORT).show();
				loadMainUI();
			}
		}

	}

	/**
	 * ��װapk�ļ�
	 * 
	 * @param file
	 */
	private void installApk(File file) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		finish();
		startActivity(intent);
	}
	
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 1:
			if(resultCode==Activity.RESULT_CANCELED){
				loadMainUI();
			}
			break;

		default:
			break;
		}
	}






	/**
	 * ���������Ի���
	 */
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setIcon(R.drawable.icon5);
		builder.setTitle("��������");
		builder.setMessage(info.getDescription());
		builder.setCancelable(false);
		builder.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					Log.i(TAG, "����apk�ļ�" + info.getApkurl());
					pd.show();
					DownLoadFileThread downloadThread = new DownLoadFileThread(
							info.getApkurl(), Environment
									.getExternalStorageDirectory().getPath()
									+ "/newapk.apk");
					new Thread(downloadThread).start();
				} else {
					Toast.makeText(getApplicationContext(), "sd��������",
							Toast.LENGTH_SHORT).show();
					loadMainUI();
				}

			}
		});

		builder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "�û�ȡ��������������");
				loadMainUI();
			}
		});

		builder.create().show();
	}

	/**
	 * 
	 * @param version
	 *            ��ǰ�ͻ��˵İ汾��Ϣ
	 * @return �Ƿ���Ҫ����
	 */
	private boolean isNeedUpdate(String version) {
		UpdateInfoEngine updateEngine = new UpdateInfoEngine(this);
		try {
			info = updateEngine.getUpdateInfo(R.string.apkserver);
			String serverVersion = info.getVersion();
			if (serverVersion.equals(version)) {
				Log.i(TAG, "�汾����ͬ������Ҫ������ֱ�ӽ���������");
				loadMainUI();
				return false;
			} else {
				Log.i(TAG, "�汾�Ų���ͬ����Ҫ����");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "��ȡ��������Ϣ�쳣,����������", Toast.LENGTH_SHORT).show();
			Log.i(TAG, "��ȡ��������Ϣ�쳣,����������");
			loadMainUI();
			return false;
		}
	}

	/**
	 * ����������
	 */
	private void loadMainUI() {
		Intent intent = new Intent(this, MainActivity.class);
		finish();
		startActivity(intent);
	}

	/**
	 * ��ȡ��ǰӦ�õİ汾��
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
			return "�汾��δ֪";
		}
	}
}
