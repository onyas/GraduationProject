package com.onyas.phoneguard.adpter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.domain.AppInfo;

public class AppManagerAdapter extends BaseAdapter {

	private List<AppInfo> infos;
	private Context context;
	
	public AppManagerAdapter(List<AppInfo> infos,Context context) {
		super();
		this.infos = infos;
		this.context = context;
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		AppInfo appinfo = infos.get(position);
		View view = View.inflate(context, R.layout.fun_3app_item, null);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_appmanager_function);
		TextView tv = (TextView) view.findViewById(R.id.tv_appmanager_name);
		iv.setImageDrawable(appinfo.getIcon());
		tv.setText(appinfo.getAppname());
		return view;
	}

}
