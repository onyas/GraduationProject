package com.onyas.phoneguard.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;

import com.onyas.phoneguard.engine.GPSInfoEngine;
/**
 * 接收到短信时，如果内容为     #*location*#
 * 终止短信广播，把当前手机的位置信息发送到出去
 * @author Administrator
 *
 */
public class SMSReceivedReceiver extends BroadcastReceiver{

	private static final String TAG = "SMSReceivedReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {

		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		
		for(Object pdu:pdus){
			SmsMessage sms = SmsMessage.createFromPdu((byte[])pdu);
			String sender = sms.getOriginatingAddress();
			String content = sms.getMessageBody();
			if("#*location*#".equals(content))
			{
				Log.i(TAG, content);
				abortBroadcast();
				GPSInfoEngine gps = GPSInfoEngine.getInstance(context);
				String location = gps.getLocation();
				if(!TextUtils.isEmpty(location)){
					SmsManager sm = SmsManager.getDefault();
					sm.sendTextMessage(sender, null, location, null, null);
				}
			}
		}
		
	}

}
