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
	private static TextView tv;// ��̬�������Ż�listView

	private ListView lv_applock;
	private AppInfoEngine appInfoEngine;
	private List<AppInfo> appinfos;
	private ProgressDialog pd;
	private AppLockAdapter adapter;
	private AppLockDao dao;
	private List<String> lockslist;// �����Ż�listView,��������ÿ�ζ������ݿ�����ȥ��ѯ

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
		pd.setMessage("���ڼ���Ӧ�ó������Ե�...");

		lv_applock = (ListView) findViewById(R.id.lv_applock);
		appInfoEngine = new AppInfoEngine(this);
		adapter = new AppLockAdapter();
		dao = new AppLockDao(this);
		lockslist = dao.findAll();// �����ݿ��еõ����б����ĳ���
		initUI();

		lv_applock.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ����һ��λ�ƶ���
				TranslateAnimation ta = new TranslateAnimation(
						Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 1.0f,
						Animation.RELATIVE_TO_SELF, 0.0f,
						Animation.RELATIVE_TO_SELF, 1.0f);
				ta.setDuration(100);
				view.startAnimation(ta);
				// �õ�����ͼƬ
				ImageView iv = (ImageView) view
						.findViewById(R.id.iv_applock_item_lock);
				// �õ�ÿһ��AppInfo����
				AppInfo info = (AppInfo) parent.getItemAtPosition(position);
				String packname = info.getPackagename();
				if (dao.find(packname)) {
					lockslist.remove(packname);// ���ڴ�����ɾ��
					// dao.delete(packname);//�����ݿ�����ɾ��
					// ��Ϊ��contentResolverִ��
					getContentResolver()
							.delete(Uri
									.parse("content://com.onyas.applockprovider/delete"),
									null, new String[] { packname });
					iv.setImageResource(R.drawable.unlock);
				} else {
					lockslist.add(packname);// ���ڴ�����
					// dao.add(packname);// �����ݿ���������
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

	// �����µ��̻߳�����е�app
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