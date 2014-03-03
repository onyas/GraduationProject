package com.onyas.phoneguard.ui;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

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
	private PopupWindow popupWindow;
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
		
		lv_listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				dismissPopupWindow();
				
				
				//获取当前view对象在窗体中的位置
				int[] location =new int[2];
				view.getLocationInWindow(location);
				
				int x = location[0]+60;
				int y = location[1];
				TextView tv = new TextView(Fun3AppManagerActivity.this);
				AppInfo info = (AppInfo) lv_listView.getItemAtPosition(position);//得到当前被选中的条目
				tv.setTextSize(20);
				tv.setTextColor(Color.RED);
				tv.setText(info.getAppname());
				
				popupWindow = new PopupWindow(tv, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
				Drawable bg = new ColorDrawable(Color.GREEN);
				popupWindow.setBackgroundDrawable(bg);
				popupWindow.showAtLocation(parent, Gravity.TOP|Gravity.LEFT, x, y);
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
		//保证只有一个popupWindow的实例存在
		if(popupWindow!=null){
			popupWindow.dismiss();
			popupWindow=null;
		}
	}

}
