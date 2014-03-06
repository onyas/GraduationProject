package com.onyas.phoneguard.ui;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.domain.AppInfo;
import com.onyas.phoneguard.engine.AppInfoEngine;

public class AppLockActivity extends Activity {

	private static ImageView iv;
	private static TextView tv;//静态的用于优化listView 

	private ListView lv_applock;
	private AppInfoEngine appInfoEngine;
	private List<AppInfo> appinfos;
	private ProgressDialog pd;
	private AppLockAdapter adapter;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			pd.dismiss();
			lv_applock.setAdapter(adapter);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_lock);

		pd = new ProgressDialog(this);
		pd.setMessage("正在加载应用程序，请稍等...");

		lv_applock = (ListView) findViewById(R.id.lv_applock);
		appInfoEngine = new AppInfoEngine(this);
		adapter = new AppLockAdapter();
		initUI();
	}

	// 开启新的线程获得所有的app
	private void initUI() {
		pd.show();
		new Thread() {
			public void run() {
				appinfos = appInfoEngine.getAllApps();
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private class AppLockAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return appinfos.size();
		}

		@Override
		public Object getItem(int position) {
			return appinfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			AppInfo info = appinfos.get(position);
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.app_lock_item, null);
			} else {
				view = convertView;
			}

			iv = (ImageView) view.findViewById(R.id.iv_applock_icon);
			tv = (TextView) view.findViewById(R.id.tv_applock_name);

			iv.setImageDrawable(info.getIcon());
			tv.setText(info.getAppname());
			
			return view;
		}

	}
}
