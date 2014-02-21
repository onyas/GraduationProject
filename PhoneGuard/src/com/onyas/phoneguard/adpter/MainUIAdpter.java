package com.onyas.phoneguard.adpter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onyas.phoneguard.R;

public class MainUIAdpter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater;
	
	
	
	public MainUIAdpter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	private static String[] names = { "手机防盗", "通讯卫士", "软件管理", "任务管理", "上网管理", "手机杀毒",
			"系统优化", "高级工具", "设置中心" };
	private static int[] icons = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };

	@Override
	public int getCount() {
		return names.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = inflater.inflate(R.layout.main_screen_item, null);
		ImageView iv_main_fun = (ImageView) view.findViewById(R.id.iv_main_function);
		iv_main_fun.setImageResource(icons[position]);
		TextView tv_main_name = (TextView) view.findViewById(R.id.tv_main_name);
		tv_main_name.setText(names[position]);
		return view;
	}

}
