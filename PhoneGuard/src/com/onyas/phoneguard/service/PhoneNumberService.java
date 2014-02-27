package com.onyas.phoneguard.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.engine.PhoneAddressEngine;

public class PhoneNumberService extends Service {

	private static final String TAG = "PhoneNumberService";
	private MyPhoneStateListener listener;
	private TelephonyManager tm;
	private WindowManager wm;
	private TextView tv;
	private SharedPreferences sp;

	@Override
	public void onCreate() {
		super.onCreate();

		sp = getSharedPreferences("config", Context.MODE_PRIVATE);

		listener = new MyPhoneStateListener();

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

	}

	private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 手机静止状态，没有呼叫
				if (tv != null) {
					wm.removeView(tv);
					tv = null;
				}
				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
				Log.i(TAG, "incoming num" + incomingNumber);
				String address = PhoneAddressEngine.getAddress(incomingNumber);
				Log.i(TAG, address);
				// Toast.makeText(getApplicationContext(), address,
				// Toast.LENGTH_SHORT).show();
				showLocation(address);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接通电话
				if (tv != null) {
					wm.removeView(tv);
					tv = null;
				}
				break;
			}

		}

	}

	/**
	 * 在手机屏幕上显示手机号码归属地
	 * 
	 * @param address
	 */
	private void showLocation(String address) {
		WindowManager.LayoutParams params = new LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;

		tv = new TextView(getApplicationContext());
		int id = sp.getInt("locatebgcolor", 0);
		int resid = R.drawable.call_locate_blue;
		switch (id) {
		case 0:
			resid = R.drawable.call_locate_white;
			break;
		case 1:
			resid = R.drawable.call_locate_orange;
			break;
		case 2:
			resid = R.drawable.call_locate_blue;
			break;
		case 3:
			resid = R.drawable.call_locate_gray;
			break;
		case 4:
			resid = R.drawable.call_locate_green;
			break;
		}
		Log.i(TAG, "加载的背景为+"+resid);
		tv.setBackgroundResource(resid);
		tv.setText(address);

		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.addView(tv, params);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);
		listener = null;
	}
}
