package com.onyas.phoneguard.receiver;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.onyas.phoneguard.service.UpdateWidgetService;

public class ProcessManagerWidget extends AppWidgetProvider {

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		//ֹͣ����
		Intent service = new Intent(context,UpdateWidgetService.class);
		context.stopService(service);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		//��������
		Intent service = new Intent(context,UpdateWidgetService.class);
		context.startService(service);
	}

}
