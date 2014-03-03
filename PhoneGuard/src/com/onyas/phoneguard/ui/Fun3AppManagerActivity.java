package com.onyas.phoneguard.ui;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.adpter.AppManagerAdapter;
import com.onyas.phoneguard.domain.AppInfo;
import com.onyas.phoneguard.engine.AppInfoEngine;

public class Fun3AppManagerActivity extends Activity {

	protected static final int LOADFINSH = 10;
	private ListView lv_listView;
	private LinearLayout ll_loading;
	private AppInfoEngine appEngine;
	private List<AppInfo> appinfos;
	private AppManagerAdapter adapter;
	private Handler handler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case LOADFINSH:
				ll_loading.setVisibility(View.INVISIBLE);
				//把数据设置给listview的数组适配器
				adapter = new AppManagerAdapter(appinfos, Fun3AppManagerActivity.this);
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
	}

}
