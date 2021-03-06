package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.adpter.MainUIAdpter;

public class MainActivity extends Activity implements OnItemClickListener {

	private static final String TAG = "MainActivity";
	private GridView gv_main_screen;
	private MainUIAdpter adpter;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		gv_main_screen = (GridView) this.findViewById(R.id.gv_main_screen);

		sp = getSharedPreferences("config", Context.MODE_PRIVATE);

		adpter = new MainUIAdpter(this);
		gv_main_screen.setAdapter(adpter);

		// 为每一个条目添加点击事件
		gv_main_screen.setOnItemClickListener(this);

		// 当长按"手机防盗"这个条目时，弹出可以修改名字的对话框
		gv_main_screen
				.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							final View view, int position, long id) {
						if (position == 0) {
							AlertDialog.Builder builder = new Builder(
									MainActivity.this);
							builder.setTitle("设置");
							builder.setIcon(R.drawable.icon5);

							final EditText et_name = new EditText(
									MainActivity.this);
							et_name.setHint("请输入要设置的名称");

							builder.setView(et_name);

							builder.setPositiveButton("确定",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											String name = et_name.getText()
													.toString().trim();
											if (TextUtils.isEmpty(name)) {
												Toast.makeText(
														getApplicationContext(),
														"名字不能为空",
														Toast.LENGTH_SHORT)
														.show();
												return;
											}
											Editor editor = sp.edit();
											editor.putString("phonelost", name);
											editor.commit();
											TextView tv_name = (TextView) view
													.findViewById(R.id.tv_main_name);
											tv_name.setText(name);
										}
									});
							builder.setNegativeButton("取消",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									});
							builder.create().show();
						}
						return false;
					}
				});

	}

	/**
	 * 
	 * 当gridView中的条目被点击时对应的回调 parent gridView view 当前被点击的条目 linearlayout
	 * position 当前条目对应的位置 id 行号
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(TAG, "当前条目" + position);
		switch (position) {
		case 0:// 手机防盗功能
			Log.i(TAG, "进入手机防盗功能");
			Intent phoneIntent = new Intent(MainActivity.this,
					Fun1PhoneProtectedActivity.class);
			startActivity(phoneIntent);
			break;
		case 1:// 通讯卫士功能
			Log.i(TAG, "进入通讯卫士功能");
			Intent TonxueIntent = new Intent(MainActivity.this,
					BlackListActivity.class);
			startActivity(TonxueIntent);
			break;
		case 2:// 应用程序管理
			Log.i(TAG, "进入通讯卫士功能");
			Intent AppIntent = new Intent(MainActivity.this,
					Fun3AppManagerActivity.class);
			startActivity(AppIntent);
			break;
		case 3:// 任务管理
			Log.i(TAG, "进入任务管理功能");
			Intent TaskIntent = new Intent(MainActivity.this,
					Fun4TaskManagerActivity.class);
			startActivity(TaskIntent);
			break;
		case 4:// 流量管理
			Log.i(TAG, "进入流量管理功能");
			Intent TrafficIntent = new Intent(MainActivity.this,
					Fun5TrafficManagerActivity.class);
			startActivity(TrafficIntent);
			break;
		case 5:// 手机杀毒
			Log.i(TAG, "进入手机杀毒功能");
			Intent anticIntent = new Intent(MainActivity.this,
					Fun6AntiVirusActivity.class);
			startActivity(anticIntent);
			break;
		case 6:// 系统优化
			Log.i(TAG, "进入系统优化");
			Intent fun7 = new Intent(MainActivity.this,
					Fun7SystemOptiActivity.class);
			startActivity(fun7);
			break;
		case 7:// 高级工具
			Log.i(TAG, "进入高级工具");
			Intent fun8 = new Intent(MainActivity.this,
					Fun8AtoolsActivity.class);
			startActivity(fun8);
			break;
		case 8:// 设置中心
			Log.i(TAG, "进入设置中心");
			Intent fun9 = new Intent(MainActivity.this,
					Fun9SettingCenterActivity.class);
			startActivity(fun9);
			break;
		default:
			break;
		}
	}

}
