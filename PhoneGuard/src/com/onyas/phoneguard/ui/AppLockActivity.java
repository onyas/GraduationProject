package com.onyas.phoneguard.ui;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.db.dao.AppLockDao;
import com.onyas.phoneguard.domain.AppInfo;
import com.onyas.phoneguard.engine.AppInfoEngine;

public class AppLockActivity extends Activity {

	private static ImageView iv, iv_lock;
	private static TextView tv;// 静态的用于优化listView

	private ListView lv_applock;
	private AppInfoEngine appInfoEngine;
	private List<AppInfo> appinfos;
	private ProgressDialog pd;
	private AppLockAdapter adapter;
	private AppLockDao dao;
	private List<String> lockslist;// 用于优化listView,这样不用每次都到数据库里面去查询

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
		dao = new AppLockDao(this);
		lockslist = dao.findAll();// 从数据库中得到所有保护的程序
		initUI();

		lv_applock.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 定义一个位移动画
				TranslateAnimation ta = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 1.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 1.0f);
				ta.setDuration(100);
				view.startAnimation(ta);
				// 得到锁的图片
				ImageView iv = (ImageView) view
						.findViewById(R.id.iv_applock_item_lock);
				// 得到每一个AppInfo对象
				AppInfo info = (AppInfo) parent.getItemAtPosition(position);
				String packname = info.getPackagename();
				if (dao.find(packname)) {
					lockslist.remove(packname);// 从内存里面删除
					// dao.delete(packname);//从数据库里面删除
					// 改为用contentResolver执行
					getContentResolver()
							.delete(Uri
									.parse("content://com.onyas.applockprovider/delete"),
									null, new String[] { packname });
					iv.setImageResource(R.drawable.unlock);
				} else {
					lockslist.add(packname);// 向内存添加
					// dao.add(packname);// 向数据库里面添加
					ContentValues values = new ContentValues();
					values.put("packname", packname);
					getContentResolver()
							.insert(Uri
									.parse("content://com.onyas.applockprovider/insert"),
									values);
					iv.setImageResource(R.drawable.lock);
				}

			}
		});
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
			iv_lock = (ImageView) view.findViewById(R.id.iv_applock_item_lock);
			iv.setImageDrawable(info.getIcon());
			tv.setText(info.getAppname());
			if (lockslist.contains(info.getPackagename())) {
				iv_lock.setImageResource(R.drawable.lock);
			} else {
				iv_lock.setImageResource(R.drawable.unlock);
			}
			return view;
		}

	}
}
