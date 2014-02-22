package com.onyas.phoneguard.ui;

import com.onyas.phoneguard.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class PhoneProtectedActivity extends Activity {

	private static final String TAG = "PhoneProtectedActivity";
	private SharedPreferences sp;
	private Dialog dialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(isAlreadySavePwd())
		{
			Log.i(TAG, "�������룬������¼");
			showNormalDialog();
		}else
		{
			Log.i(TAG, "��һ�ε�½����Ҫ��������");
			showFirstDialog();
		}
		
		
	}


	/**
	 *��ʾ��һ����������ĶԻ���
	 */
	private void showFirstDialog() {
		dialog = new Dialog(this, R.style.MyDialog);
		dialog.setContentView(R.layout.first_mydialog);
		dialog.show();
	}


	/**
	 * ��ʾҪ��½�ĶԻ���
	 */
	private void showNormalDialog() {
		
	}


	/**
	 * �ж�SharePreferences���Ƿ��Ѿ���������
	 * @return
	 */
	private boolean isAlreadySavePwd() {
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		String pwd =sp.getString("password", null);
		if(pwd==null)
		{
			return false;
		}else
		{
			return true;
		}
	}
	
	
	
}
