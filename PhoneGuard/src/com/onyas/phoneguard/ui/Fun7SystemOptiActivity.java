package com.onyas.phoneguard.ui;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.domain.CacheInfo;
import com.onyas.phoneguard.util.FileUtil;
import com.onyas.phoneguard.util.TextFormatter;

public class Fun7SystemOptiActivity extends ListActivity {

	private ListView lv;
	private SystemOptiAdapter adapter;
	private Map<String, CacheInfo> cacheMaps;
	private PackageManager pm;
	private List<PackageInfo> packinfos;
	private TextView tv_status;
	private SeekBar seekbar;
	private SQLiteDatabase db;
	private String path = "/data/data/com.onyas.phoneguard/files/clearpath.db";

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			String text = (String) msg.obj;
			tv_status.setText(text);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fun_7systemopti);
		lv = getListView();
		tv_status = (TextView) findViewById(R.id.tv_system_opti_status);
		seekbar = (SeekBar) findViewById(R.id.seekbar);

		cacheMaps = new HashMap<String, CacheInfo>();
		pm = getPackageManager();

		// 如果当前手机中没有数据库文件，则到类加载路径中去下载
		File file = new File(path);
		if (!file.exists()) {
			try {
				InputStream is = getClass().getClassLoader()
						.getResourceAsStream("clearpath.db");
				FileUtil.copyFile(is, this, "clearpath.db", MODE_PRIVATE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);

		packinfos = pm
				.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
		for (PackageInfo info : packinfos) {
			String appname = info.applicationInfo.loadLabel(pm).toString();
			String packname = info.packageName;
			CacheInfo cacheInfo = new CacheInfo();
			cacheInfo.setName(appname);
			cacheInfo.setPackname(packname);

			cacheMaps.put(packname, cacheInfo);
			setCacheInfo(packname);
		}

		adapter = new SystemOptiAdapter();
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				/*
				 * <intent-filter> <action
				 * android:name="android.settings.APPLICATION_DETAILS_SETTINGS"
				 * /> <category android:name="android.intent.category.DEFAULT"
				 * /> <data android:scheme="package" /> </intent-filter>
				 */
				CacheInfo cinfo = (CacheInfo) lv.getItemAtPosition(arg2);
				Intent intent = new Intent();
				intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(Uri.parse("package:" + cinfo.getPackname()));
				startActivity(intent);
			}
		});

	}

	/**
	 * 通过反射与aidl得到系统隐藏的方法，从面得到PackageStats的cacheSize，codeSize，dataSize 这是一个异步的方法
	 * 
	 * @param packname
	 */
	private void setCacheInfo(final String packname) {

		try {
			Method method = PackageManager.class.getMethod(
					"getPackageSizeInfo", new Class[] { String.class,
							IPackageStatsObserver.class });
			method.invoke(pm, new Object[] { packname,
					new IPackageStatsObserver.Stub() {

						@Override
						public void onGetStatsCompleted(PackageStats pStats,
								boolean succeeded) throws RemoteException {

							long cacheSize = pStats.cacheSize;
							long codeSize = pStats.codeSize;
							long dataSize = pStats.dataSize;

							CacheInfo info = cacheMaps.get(packname);
							info.setCachesize(TextFormatter
									.sizeFormat(cacheSize));
							info.setCodesize(TextFormatter.sizeFormat(codeSize));
							info.setDatasize(TextFormatter.sizeFormat(dataSize));

							cacheMaps.put(packname, info);
						}
					} });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SystemOptiAdapter extends BaseAdapter {

		private Set<Entry<String, CacheInfo>> sets;
		private List<CacheInfo> cacheInfos;

		public SystemOptiAdapter() {
			sets = cacheMaps.entrySet();
			cacheInfos = new ArrayList<CacheInfo>();
			for (Entry<String, CacheInfo> entry : sets) {
				cacheInfos.add(entry.getValue());
			}
		}

		@Override
		public int getCount() {
			return cacheInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return cacheInfos.get(arg0);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CacheInfo cacheInfo = cacheInfos.get(position);
			View view = null;
			if (convertView == null) {
				view = View.inflate(Fun7SystemOptiActivity.this,
						R.layout.systemopti_item, null);
			} else {
				view = convertView;
			}

			TextView tv_name = (TextView) view
					.findViewById(R.id.tv_system_opti_name);
			TextView tv_cachesize = (TextView) view
					.findViewById(R.id.tv_system_opti_cachesize);
			TextView tv_codesize = (TextView) view
					.findViewById(R.id.tv_system_opti_codesize);
			TextView tv_datasize = (TextView) view
					.findViewById(R.id.tv_system_opti_datasize);

			tv_name.setText(cacheInfo.getName());
			tv_cachesize.setText(cacheInfo.getCachesize());
			tv_codesize.setText(cacheInfo.getCodesize());
			tv_datasize.setText(cacheInfo.getDatasize());

			return view;
		}

	}

	/**
	 * 按钮的点击事件，到数据库中去查当前手机的安装包中有没有指定的路径
	 * 
	 * @param view
	 */
	public void clear(View view) {
		new Thread() {
			public void run() {
				List<PackageInfo> packinfos = getPackageManager()
						.getInstalledPackages(
								PackageManager.GET_UNINSTALLED_PACKAGES);
				seekbar.setMax(packinfos.size());
				int total = 0;
				for (PackageInfo info : packinfos) {
					String packname = info.packageName;
					Cursor cursor = db.rawQuery(
							"select filepath from softdetail where apkname=?",
							new String[] { packname });
					if (cursor.moveToNext()) {
						String path = cursor.getString(0);
						System.out.println("清除了" + packname);
						File file = new File(
								Environment.getExternalStorageDirectory(), path);
						FileUtil.deleteDir(file);
					}
					total++;
					seekbar.setProgress(total);
					cursor.close();
					Message msg = Message.obtain();
					msg.obj = "清除" + packname;
					handler.sendMessage(msg);
				}
				Message msg = Message.obtain();
				msg.obj = "清除完毕";
				handler.sendMessage(msg);
			};
		}.start();
	}

}
