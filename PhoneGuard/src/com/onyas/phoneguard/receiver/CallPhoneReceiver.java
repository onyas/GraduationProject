package com.onyas.phoneguard.receiver;

import com.onyas.phoneguard.ui.PhoneProtectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CallPhoneReceiver extends BroadcastReceiver {

	private static final String TAG = "CallPhoneReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData();
		//如果手机播打了2014这个号码，则自动打开手机防盗功能页面
		if("2014".equals(number)){
			Log.i(TAG, "播打了手机暗号");
			Intent phoneIntent = new Intent(context, PhoneProtectedActivity.class);
			//Context中有一个startActivity方法，Activity继承自Context，重载了startActivity方法。
			//如果使用 Activity的startActivity方法，不会有任何限制，而如果使用Context的startActivity方法的话，
			//就需要开启一个新的task，遇到上面那个异常的，都是因为使用了Context的startActivity方法。
			//解决办法是，加一个flag。intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(phoneIntent);
			//终止播打电话过程(也就是说手机播打2014时，打不出去，只是打开手机防盗功能页面)，不能用abortBroadcast()
			setResultData(null);
		}
	}

}
