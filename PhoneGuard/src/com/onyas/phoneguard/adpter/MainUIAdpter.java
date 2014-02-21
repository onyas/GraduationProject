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

	private static String[] names = { "�ֻ�����", "ͨѶ��ʿ", "�������", "�������", "��������", "�ֻ�ɱ��",
			"ϵͳ�Ż�", "�߼�����", "��������" };
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
		
		//getView ���õĴ�������getCount()��ԭ���ǲ�֪��ÿһ��Ŀ��ʾ�ڽ����ϵĴ�С
		//���������ڽ����ϵ���һ�£�Ȼ���ڵ���һ�����getView()������ȷ��λ��
		//���ڳ����Ч����˵�½��˺ܶ� �����԰�iv_main_fun��tv_main_name����Ϊ��̬�ģ�����������ֻ����һ��ʵ��
		
		View view = inflater.inflate(R.layout.main_screen_item, null);
		iv_main_fun = (ImageView) view.findViewById(R.id.iv_main_function);
		iv_main_fun.setImageResource(icons[position]);
		tv_main_name = (TextView) view.findViewById(R.id.tv_main_name);
		tv_main_name.setText(names[position]);
		return view;
	}

}
