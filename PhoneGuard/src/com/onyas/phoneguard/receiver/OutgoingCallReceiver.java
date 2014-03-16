package com.onyas.phoneguard.receiver;

import com.onyas.phoneguard.ui.Fun1PhoneProtectedActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.onyas.phoneguard.util.Logger;

public class OutgoingCallReceiver extends BroadcastReceiver {

	private static final String TAG = "CallPhoneReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		String number = getResultData();
		//����ֻ�������2014������룬���Զ����ֻ���������ҳ��
		if("2014".equals(number)){
			Logger.i(TAG, "�������ֻ�����");
			Intent phoneIntent = new Intent(context, Fun1PhoneProtectedActivity.class);
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
