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

//	private static final String TAG = "AppManagerAdapter";
	private List<AppInfo> infos;
	private Context context;
	private static ImageView iv;
	private static TextView tv;

	public AppManagerAdapter(List<AppInfo> infos, Context context) {
		super();
		this.infos = infos;
		this.context = context;
	}

	
	public void setAppInfos(List<AppInfo> infos){
		this.infos = infos;
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

	/**
	 * 每个条目在显示的时候都会调用这个方法 convertView 转化view对象，历史view对象的缓存,就是拖动时候被回收掉的view对象
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		AppInfo appinfo = infos.get(position);
		if (convertView == null) {
			// 通过资源文件，创建view对象
//			Logger.i(TAG, "通过资源文件，创建view对象");
			view = View.inflate(context, R.layout.fun_3app_item, null);
		} else {
			// 使用历史缓存view对象，不用反复调用inflate方法
//			Logger.i(TAG, "使用历史缓存view对象");
			view = convertView;
		}
		iv = (ImageView) view.findViewById(R.id.iv_appmanager_function);
		tv = (TextView) view.findViewById(R.id.tv_appmanager_name);
		iv.setImageDrawable(appinfo.getIcon());
		tv.setText(appinfo.getAppname());
		return view;
	}

}
