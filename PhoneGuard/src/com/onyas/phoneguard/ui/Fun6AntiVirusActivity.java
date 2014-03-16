package com.onyas.phoneguard.ui;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.util.FileUtil;
import com.onyas.phoneguard.util.MD5Encoder;

public class Fun6AntiVirusActivity extends Activity {

	protected static final int FINSH = 10;
	private ImageView iv_animate;
	private AnimationDrawable ad;
	private SeekBar sb_anti;
	private LinearLayout ll_anti;
	private ScrollView sv_anti;
	private SQLiteDatabase db;
	private boolean isscanning = false;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == FINSH) {
				ll_anti.removeAllViews();
				ad.stop();
			}
			String text = (String) msg.obj;
			TextView tv = new TextView(Fun6AntiVirusActivity.this);
			tv.setText(text);
			tv.setTextColor(getResources().getColor(R.color.textColor));
			ll_anti.addView(tv);
			sv_anti.scrollBy(0, 20);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fun_6antivirtus);

		iv_animate = (ImageView) findViewById(R.id.iv_animate);
		iv_animate.setBackgroundResource(R.drawable.anti_anim);
		ad = (AnimationDrawable) iv_animate.getBackground();
		sb_anti = (SeekBar) findViewById(R.id.sb_anti);
		ll_anti = (LinearLayout) findViewById(R.id.ll_anti);
		sv_anti = (ScrollView) findViewById(R.id.sv_anti);

		File file = new File("/sdcard/antivirus.db");
		if (!file.exists()) {
			copyDBFile();
		}
		db = SQLiteDatabase.openDatabase("/sdcard/antivirus.db", null,
				SQLiteDatabase.OPEN_READONLY);

	}

	/**
	 * 把assets文件夹中的数据库文件拷贝到SD卡中
	 */
	private void copyDBFile() {
		AssetManager assetManager = getAssets();
		try {
			InputStream is = assetManager.open("antivirus.db");
			FileUtil.copyFile(is, "/sdcard/antivirus.db");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isscanning) {
			return false;
		}
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			isscanning = true;
			ad.start();
			new Thread() {
				public void run() {
					List<PackageInfo> packinfos = getPackageManager()
							.getInstalledPackages(
									PackageManager.GET_UNINSTALLED_PACKAGES
											| PackageManager.GET_SIGNATURES);
					sb_anti.setMax(packinfos.size());
					int count = 0;
					int virtuls = 0;
					for (PackageInfo info : packinfos) {
						count++;
						String str = info.signatures[0].toCharsString();
						String md5 = MD5Encoder.encoder(str);

						Message msg = Message.obtain();
						msg.obj = "正在扫描" + info.packageName;
						handler.sendMessage(msg);

						Cursor cursor = db.rawQuery(
								"select desc from datable where md5=?",
								new String[] { md5 });
						if (cursor.moveToNext()) {
							String desc = cursor.getString(0);
							msg = Message.obtain();
							msg.obj = info.packageName + ": " + desc;
							handler.sendMessage(msg);
							virtuls++;
						}
						cursor.close();
						sb_anti.setProgress(count);
					}
					Message msg = Message.obtain();
					msg.what = FINSH;
					msg.obj = "扫描完毕 ,共发现" + virtuls + "个病毒";
					handler.sendMessage(msg);
					isscanning = false;
				};
			}.start();
		}

		return super.onTouchEvent(event);
	}

	@Override
	protected void onDestroy() {
		if (db.isOpen()) {
			db.close();
		}
		super.onDestroy();
	}

}
