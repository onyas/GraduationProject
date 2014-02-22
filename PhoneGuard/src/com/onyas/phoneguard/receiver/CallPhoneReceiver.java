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
		//����ֻ�������2014������룬���Զ����ֻ���������ҳ��
		if("2014".equals(number)){
			Log.i(TAG, "�������ֻ�����");
			Intent phoneIntent = new Intent(context, PhoneProtectedActivity.class);
			//Context����һ��startActivity������Activity�̳���Context��������startActivity������
			//���ʹ�� Activity��startActivity�������������κ����ƣ������ʹ��Context��startActivity�����Ļ���
			//����Ҫ����һ���µ�task�����������Ǹ��쳣�ģ�������Ϊʹ����Context��startActivity������
			//����취�ǣ���һ��flag��intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
			phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(phoneIntent);
			//��ֹ����绰����(Ҳ����˵�ֻ�����2014ʱ���򲻳�ȥ��ֻ�Ǵ��ֻ���������ҳ��)��������abortBroadcast()
			setResultData(null);
		}
	}

}
