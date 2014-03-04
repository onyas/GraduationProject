package com.onyas.phoneguard.ui;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.adpter.AppManagerAdapter;
import com.onyas.phoneguard.domain.AppInfo;
import com.onyas.phoneguard.engine.AppInfoEngine;

public class Fun3AppManagerActivity extends Activity implements OnClickListener {

	protected static final int LOADFINSH = 10;
	private ListView lv_listView;
	private LinearLayout ll_loading;
	private AppInfoEngine appEngine;
	private List<AppInfo> appinfos;
	private AppManagerAdapter adapter;
	private PopupWindow popupWindow;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOADFINSH:
				ll_loading.setVisibility(View.INVISIBLE);
				// ���������ø�listview������������
				adapter = new AppManagerAdapter(appinfos,
						Fun3AppManagerActivity.this);
				lv_listView.setAdapter(adapter);
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fun_3app_manager);

		ll_loading = (LinearLayout) findViewById(R.id.ll_appmanager_loading);
		lv_listView = (ListView) findViewById(R.id.lv_appmanager_list);

		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {

				appEngine = new AppInfoEngine(Fun3AppManagerActivity.this);
				appinfos = appEngine.getAllApps();
				Message msg = new Message();
				msg.what = LOADFINSH;
				handler.sendMessage(msg);
			};
		}.start();

		lv_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				dismissPopupWindow();

				// ��ȡ��ǰview�����ڴ����е�λ��
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int x = location[0] + 60;
				int y = location[1];

				// �Ӳ����ļ��õ�һ��view����
				View popupView = View.inflate(Fun3AppManagerActivity.this,
						R.layout.popup_window, null);
				LinearLayout ll_pupup = (LinearLayout) popupView
						.findViewById(R.id.ll_pupupwindow);
				LinearLayout ll_uninstall = (LinearLayout) popupView
						.findViewById(R.id.ll_pupup_uninstall);
				LinearLayout ll_start = (LinearLayout) popupView
						.findViewById(R.id.ll_pupup_start);
				LinearLayout ll_share = (LinearLayout) popupView
						.findViewById(R.id.ll_pupup_share);

				ll_uninstall.setOnClickListener(Fun3AppManagerActivity.this);
				ll_start.setOnClickListener(Fun3AppManagerActivity.this);
				ll_share.setOnClickListener(Fun3AppManagerActivity.this);

				// ��View��������Tag�������Ϳ��Ը���position��ȷ���ǵ���¼��������ĸ���������
				ll_uninstall.setTag(position);
				ll_start.setTag(position);
				ll_share.setTag(position);

				// ��popupWindow����һ�����ŵĶ���
				ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
				sa.setDuration(200);

				popupWindow = new PopupWindow(popupView,
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				Drawable bg = getResources().getDrawable(
						R.drawable.local_popup_bg);
				popupWindow.setBackgroundDrawable(bg);
				popupWindow.showAtLocation(parent, Gravity.TOP | Gravity.LEFT,
						x, y);

				ll_pupup.setAnimation(sa);
			}
		});

		lv_listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				dismissPopupWindow();
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				dismissPopupWindow();
			}
		});
	}

	private void dismissPopupWindow() {
		// ��ֻ֤��һ��popupWindow��ʵ������
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	public void onClick(View v) {
		//�������Ŀʱ�Ȱѵ�ǰ��popupwindow�ر�
		dismissPopupWindow();
		
		int position = (Integer) v.getTag();
		AppInfo appinfo = appinfos.get(position);
		String packagename = appinfo.getPackagename();
		switch (v.getId()) {
		case R.id.ll_pupup_uninstall:// ж��

			break;
		case R.id.ll_pupup_start:// ����
			// ���о����ҵ�manifest�е�application�еĵ�һ��activity�ڵ㣬Ȼ����;
			try {
				PackageInfo pckginfo = getPackageManager().getPackageInfo(
						packagename,
						PackageManager.GET_UNINSTALLED_PACKAGES
								| PackageManager.GET_ACTIVITIES);
				ActivityInfo[] activities = pckginfo.activities;
				Intent intent = new Intent();
				intent.setClassName(packagename, activities[0].name);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "�޷�����Ӧ�ó���", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.ll_pupup_share:// ����

			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "����");
			shareIntent.putExtra(Intent.EXTRA_TEXT, "hi,��һ���õ�Ӧ�ó���,"+appinfo.getAppname());
			shareIntent=Intent.createChooser(shareIntent, "����");
			startActivity(shareIntent);
			break;
		}
	}

}
