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
	private static ImageView iv_main_fun;
	private static TextView tv_main_name;
	
	
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
		
		//getView 调用的次数多于getCount()，原因是不知道每一条目显示在界面上的大小
		//会先试着在界面上调整一下，然后在调用一遍或多遍getView()来最终确定位置
		//对于程序的效率来说下降了很多 ，所以把iv_main_fun与tv_main_name定义为静态的，这样程序中只持有一个实例
		
		View view = inflater.inflate(R.layout.main_screen_item, null);
		iv_main_fun = (ImageView) view.findViewById(R.id.iv_main_function);
		iv_main_fun.setImageResource(icons[position]);
		tv_main_name = (TextView) view.findViewById(R.id.tv_main_name);
		tv_main_name.setText(names[position]);
		return view;
	}

}
