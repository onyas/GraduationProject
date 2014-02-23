package com.onyas.phoneguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

public class BootCompletedReceiver extends BroadcastReceiver {

	private SharedPreferences sp;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isprotecting = sp.getBoolean("isprotecting", false);
		if(isprotecting){
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSerial = tm.getSimSerialNumber();
			String realSerial = sp.getString("simSerialNum", "");
			if(!currentSerial.equals(realSerial))
			{
				System.out.println("SIM卡已经更换");
				SmsManager sm = SmsManager.getDefault();
				String destinationAddress = sp.getString("safeNumber", "");
				sm.sendTextMessage(destinationAddress, null, "手机可能被盗", null, null);
			}
		}
	}

}
