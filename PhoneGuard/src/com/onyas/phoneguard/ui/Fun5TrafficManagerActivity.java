package com.onyas.phoneguard.ui;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.util.TextFormatter;

public class Fun5TrafficManagerActivity extends Activity {

	private TextView tv_mobile_total, tv_wifi_total;
	private ListView lv_content;
	private TrafficAdapter adapter;
	private PackageManager pm;
	private Timer timer;
	private TimerTask task;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fun_5trafficmanager);

		tv_wifi_total = (TextView) findViewById(R.id.tv_traffic_wifitotal);
		tv_mobile_total = (TextView) findViewById(R.id.tv_traffic_mobiletotal);
		lv_content = (ListView) findViewById(R.id.content);

		pm = getPackageManager();
		adapter = new TrafficAdapter();

		getTrafficInfo();

		lv_content.addHeaderView(View.inflate(this, R.layout.traffic_title,
				null));// 为listView添加标题,必须放在setAdapter的前面
		lv_content.setAdapter(adapter);

	}

	/**
	 * 得到2g/3g流量还有wifi的使用情况，并设置 数据到textView中
	 */
	private void getTrafficInfo() {
		long mobilerx = TrafficStats.getMobileRxBytes();
		long mobiletx = TrafficStats.getMobileTxBytes();
		long mobiletotal = mobilerx + mobiletx;
		tv_mobile_total.setText(TextFormatter.sizeFormat(mobiletotal));

		long totalrx = TrafficStats.getTotalRxBytes();
		long totaltx = TrafficStats.getTotalTxBytes();
		long total = totalrx + totaltx;
		long wifitotal = total - mobiletotal;
		tv_wifi_total.setText(TextFormatter.sizeFormat(wifitotal));
	}

	private class TrafficAdapter extends BaseAdapter {

		List<ResolveInfo> resolveinfos;

		/**
		 * 要构造方法里面获得所有能显示图标的activity
		 */
		public TrafficAdapter() {
			PackageManager pm = getPackageManager();
			Intent intent = new Intent();
			intent.setAction("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.LAUNCHER");
			resolveinfos = pm.queryIntentActivities(intent,
					PackageManager.MATCH_DEFAULT_ONLY);
		}

		@Override
		public int getCount() {

			return resolveinfos.size();
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
			View view = null;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.traffic_item, null);
			} else {
				view = convertView;
			}
			ViewHolder holder = new ViewHolder();
			holder.iv = (ImageView) view.findViewById(R.id.iv_trafficitem_icon);
			holder.tv_name = (TextView) view
					.findViewById(R.id.tv_trafficitem_name);
			holder.tv_tx = (TextView) view.findViewById(R.id.tv_trafficitem_tx);
			holder.tv_rx = (TextView) view.findViewById(R.id.tv_trafficitem_rx);

			ResolveInfo info = resolveinfos.get(position);
			holder.iv.setImageDrawable(info.loadIcon(pm));// 设置图标
			holder.tv_name.setText(info.loadLabel(pm).toString());// 设置名称
			String packname = info.activityInfo.packageName;
			try {
				PackageInfo packinfo = pm.getPackageInfo(packname, 0);
				int uid = packinfo.applicationInfo.uid;
				holder.tv_rx.setText(TextFormatter.sizeFormat(TrafficStats
						.getUidRxBytes(uid)));
				holder.tv_tx.setText(TextFormatter.sizeFormat(TrafficStats
						.getUidTxBytes(uid)));
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}

			return view;
		}

	}

	static class ViewHolder {
		ImageView iv;
		TextView tv_name;
		TextView tv_tx;
		TextView tv_rx;
	}

	@Override
	protected void onStart() {
		super.onStart();
		timer = new Timer();
		task = new TimerTask() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				handler.sendMessage(msg);
			}
		};
		timer.schedule(task, 1000, 2000);
	}

	@Override
	protected void onStop() {
		super.onStop();
		timer.cancel();
		timer = null;
		task = null;
	}
}
