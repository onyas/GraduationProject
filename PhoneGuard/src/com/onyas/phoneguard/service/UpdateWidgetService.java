package com.onyas.phoneguard.service;

import java.util.Timer;
import java.util.TimerTask;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.receiver.ScreenOffReceiver;
import com.onyas.phoneguard.util.TaskUtil;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {

	private Timer timer;
	private TimerTask timertask;
	private AppWidgetManager appWidgetmanager;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	/**
	 * 参考 App Components/App Widgets
	 */
	@Override
	public void onCreate() {
		timer = new Timer();
		appWidgetmanager = AppWidgetManager
				.getInstance(getApplicationContext());
		timertask = new TimerTask() {
			@Override
			public void run() {

				ComponentName provider = new ComponentName(getPackageName(),
						getPackageName() + ".receiver.ProcessManagerWidget");
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.process_widget);

				views.setTextViewText(
						R.id.process_count,
						"进程数目:"
								+ TaskUtil
										.getProcessCount(getApplicationContext()));
				views.setTextColor(R.id.process_count, Color.BLACK);
				views.setTextViewText(
						R.id.process_memory,
						"可用内存:"
								+ TaskUtil
										.getAvailMemory(getApplicationContext()));
				views.setTextColor(R.id.process_memory, Color.BLACK);

				Intent intent = new Intent(UpdateWidgetService.this,
						ScreenOffReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						getApplicationContext(), 0, intent, 0);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);

				appWidgetmanager.updateAppWidget(provider, views);
			}
		};

		timer.schedule(timertask, 1000, 2000);
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		timer.cancel();
		timer = null;
		timertask = null;
		super.onDestroy();
	}

}
