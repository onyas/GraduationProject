package com.onyas.phoneguard.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.adpter.AppManagerAdapter;
import com.onyas.phoneguard.domain.AppInfo;
import com.onyas.phoneguard.engine.AppInfoEngine;

public class Fun3AppManagerActivity extends Activity implements OnClickListener {

	protected static final int LOADFINSH = 10;
	private static final int DEL_APP = 11;
	protected static final int ALL_APPS = 12;
	protected static final int LOADFINSH_USERAPP = 13;
	private ListView lv_listView;
	private TextView tv_appmanager_title;
	private LinearLayout ll_loading;
	private AppInfoEngine appEngine;
	private List<AppInfo> appinfos, userAppInfos;
	private AppManagerAdapter adapter;
	private PopupWindow popupWindow;
	private boolean isLoading = false;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOADFINSH:
				ll_loading.setVisibility(View.INVISIBLE);
				// 把数据设置给listview的数组适配器
				adapter = new AppManagerAdapter(appinfos,
						Fun3AppManagerActivity.this);
				lv_listView.setAdapter(adapter);
				isLoading = false;
				break;
			case LOADFINSH_USERAPP:
				ll_loading.setVisibility(View.INVISIBLE);
				// 把数据设置给listview的数组适配器
				adapter = new AppManagerAdapter(userAppInfos,
						Fun3AppManagerActivity.this);
				lv_listView.setAdapter(adapter);
				isLoading = false;
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
		tv_appmanager_title = (TextView) findViewById(R.id.tv_appmanager_title);

		refreshUI(ALL_APPS);

		// 最上面文本的点击事件
		tv_appmanager_title.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				TextView tv = (TextView) v;
				// 解决在加载时点击文本框的bug;
				if (isLoading) {
					return;
				}
				if ("所有程序".equals(tv.getText().toString())) {
					tv.setText("用户程序");
					// 得到用户程序，并让adapter去通知listview去更新
					userAppInfos = getUserApps(appinfos);
					adapter.setAppInfos(userAppInfos);
					adapter.notifyDataSetChanged();
				} else {
					tv.setText("所有程序");
					adapter.setAppInfos(appinfos);
					adapter.notifyDataSetChanged();
				}
			}
		});

		lv_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				dismissPopupWindow();

				// 获取当前view对象在窗体中的位置
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int x = location[0] + 60;
				int y = location[1];

				// 从布局文件得到一个view对象
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

				// 给View对象设置Tag，这样就可以根据position来确定是点击事件作用于哪个程序上面
				ll_uninstall.setTag(position);
				ll_start.setTag(position);
				ll_share.setTag(position);

				// 给popupWindow设置一个缩放的动画
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

	/**
	 * 开启子线程，更新UI界面
	 */
	private void refreshUI(final int whichUI) {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				isLoading = true;
				//解决在用户程序界面卸载程序后，界面重新更新为所有程序的界面
				if(whichUI==ALL_APPS){
					appEngine = new AppInfoEngine(Fun3AppManagerActivity.this);
					appinfos = appEngine.getAllApps();
					Message msg = new Message();
					msg.what = LOADFINSH;
					handler.sendMessage(msg);
				}else{
					appEngine = new AppInfoEngine(Fun3AppManagerActivity.this);
					appinfos = appEngine.getAllApps();
					userAppInfos = getUserApps(appinfos);
					Message msg = new Message();
					msg.what = LOADFINSH_USERAPP;
					handler.sendMessage(msg);
				}
			};
		}.start();
	}

	private void dismissPopupWindow() {
		// 保证只有一个popupWindow的实例存在
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	@Override
	public void onClick(View v) {
		// 当点击条目时先把当前的popupwindow关闭
		dismissPopupWindow();

		// 默认是所有程序，如果是用户程的时候，则更改为从userAppInfos中得到appInfo
		int position = (Integer) v.getTag();
		AppInfo appinfo = appinfos.get(position);
		String packagename = appinfo.getPackagename();
		if ("用户程序".equals(tv_appmanager_title.getText().toString())) {
			appinfo = userAppInfos.get(position);
			packagename = appinfo.getPackagename();
		}

		switch (v.getId()) {
		case R.id.ll_pupup_uninstall:// 卸载
			if (appinfo.isSystemApp()) {
				Toast.makeText(this, "不能卸载系统应用程序", Toast.LENGTH_SHORT).show();
			} else {
				String uristr = "package:" + packagename;
				Uri uri = Uri.parse(uristr);
				Intent delIntent = new Intent();
				delIntent.setAction(Intent.ACTION_DELETE);
				delIntent.setData(uri);
				startActivityForResult(delIntent, DEL_APP);
			}
			break;
		case R.id.ll_pupup_start:// 运行
			// 运行就是找到manifest中的application中的第一个activity节点，然后开启;
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
				Toast.makeText(this, "无法启动应用程序", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.ll_pupup_share:// 分享

			Intent shareIntent = new Intent();
			shareIntent.setAction(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			shareIntent.putExtra(Intent.EXTRA_TEXT,
					"hi,有一个好的应用程序," + appinfo.getAppname());
			shareIntent = Intent.createChooser(shareIntent, "分享");
			startActivity(shareIntent);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case DEL_APP:
			if("所有程序".equals(tv_appmanager_title.getText().toString())){
				refreshUI(ALL_APPS);
			}else{
				refreshUI(DEL_APP);
			}
			break;
		}
	}

	/**
	 * 从所有应用程序中得到用户程序
	 * 
	 * @param appinfos
	 *            所有程序
	 * @return 用户程序
	 */
	private List<AppInfo> getUserApps(List<AppInfo> appinfos) {
		List<AppInfo> userAppinfos = new ArrayList<AppInfo>();
		for (AppInfo info : appinfos) {
			if (!info.isSystemApp()) {
				userAppinfos.add(info);
			}
		}
		return userAppinfos;
	}

}
