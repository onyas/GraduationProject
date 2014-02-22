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
			Log.i(TAG, "存入密码，正常登录");
			showNormalDialog();
		}else
		{
			Log.i(TAG, "第一次登陆，需要设置密码");
			showFirstDialog();
		}
		
		
	}


	/**
	 *显示第一次设置密码的对话框
	 */
	private void showFirstDialog() {
		dialog = new Dialog(this, R.style.MyDialog);
		dialog.setContentView(R.layout.first_mydialog);
		dialog.show();
	}


	/**
	 * 显示要登陆的对话框
	 */
	private void showNormalDialog() {
		
	}


	/**
	 * 判断SharePreferences中是否已经存入密码
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
