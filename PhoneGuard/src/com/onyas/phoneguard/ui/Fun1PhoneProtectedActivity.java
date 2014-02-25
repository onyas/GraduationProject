package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.util.MD5Encoder;

public class Fun1PhoneProtectedActivity extends Activity implements OnClickListener {

	private static final String TAG = "PhoneProtectedActivity";
	private EditText et_password, et_password_confirm, et_login_pwd;
	private SharedPreferences sp;
	private Dialog dialog;
	private TextView tv_safe_num, tv_resetup;
	private CheckBox cb_open_protect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (isAlreadySavedPwd()) {
			Log.i(TAG, "存入密码，正常登录");
			showNormalDialog();
		} else {
			Log.i(TAG, "第一次登陆，需要设置密码");
			showFirstDialog();
		}

	}

	/**
	 * 显示第一次设置密码的对话框
	 */
	private void showFirstDialog() {
		dialog = new Dialog(this, R.style.MyDialog);
		// dialog.setContentView(R.layout.first_mydialog);
		View view = View.inflate(this, R.layout.first_mydialog, null);
		et_password = (EditText) view.findViewById(R.id.et_first_entry_pwd);
		et_password_confirm = (EditText) view
				.findViewById(R.id.et_first_entry__confrim_pwd);
		Button bt_ok = (Button) view.findViewById(R.id.bt_first_entry_confirm);
		Button bt_cancel = (Button) view
				.findViewById(R.id.bt_first_entry_cancel);
		bt_ok.setOnClickListener(this);
		bt_cancel.setOnClickListener(this);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 显示要登陆的对话框
	 */
	private void showNormalDialog() {
		dialog = new Dialog(this, R.style.MyDialog);
		View view = View.inflate(this, R.layout.login_mydialog, null);
		et_login_pwd = (EditText) view.findViewById(R.id.et_login_pwd);
		Button bt_login_ok = (Button) view.findViewById(R.id.bt_login_confirm);
		Button bt_login_cancel = (Button) view
				.findViewById(R.id.bt_login_cancel);
		bt_login_ok.setOnClickListener(this);
		bt_login_cancel.setOnClickListener(this);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
	}

	/**
	 * 判断SharePreferences中是否已经存入密码
	 * 
	 * @return
	 */
	private boolean isAlreadySavedPwd() {
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		String pwd = sp.getString("password", null);
		if (pwd == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断用户是否已经激活了设置引导
	 * 
	 * @return
	 */
	private boolean isAlreadyGuided() {
		return sp.getBoolean("alreadSetup", false);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_first_entry_confirm:// 点击确定，保存密码时
			String password = et_password.getText().toString().trim();
			String pwd_confirm = et_password_confirm.getText().toString()
					.trim();
			if (TextUtils.isEmpty(password) || TextUtils.isEmpty(pwd_confirm)) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			} else {
				if (!pwd_confirm.equals(password)) {
					Toast.makeText(this, "两次密码不同", Toast.LENGTH_SHORT).show();
					return;
				} else {
					Editor editor = sp.edit();
					editor.putString("password", MD5Encoder.encoder30(password));
					editor.commit();
					Toast.makeText(this, "密码设置成功", Toast.LENGTH_SHORT).show();

					// 第一次进入，所以直接进入设置引导界面
					finish();
					startSetupGuide();
				}
			}
			dialog.dismiss();
			break;
		case R.id.bt_first_entry_cancel:// 点击取消时
			dialog.dismiss();// 自定义的对话框，要显示的关闭
			break;

		case R.id.bt_login_cancel:// 点击取消时
			dialog.dismiss();// 自定义的对话框，要显示的关闭
			break;
		case R.id.bt_login_confirm:// 点击确定，登陆时
			String inputPassword = et_login_pwd.getText().toString().trim();
			if (TextUtils.isEmpty(inputPassword)) {
				Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			} else {
				String realPassword = sp.getString("password", "");
				if (!realPassword.equals(MD5Encoder.encoder30(inputPassword))) {
					Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
					return;
				} else {
					Log.i(TAG, "密码正确，进入手机防盗页面");
					if (isAlreadyGuided()) {
						Log.i(TAG, "已经设置过向导，直接进入功能页面");

						setContentView(R.layout.fun_1lost_protect);
						
						tv_resetup = (TextView) findViewById(R.id.tv_resetup);
						tv_safe_num = (TextView) findViewById(R.id.tv_safe_num);
						cb_open_protect = (CheckBox) findViewById(R.id.cb_protect);

						// 初始化状态 safeNumber
						sp = getSharedPreferences("config", Context.MODE_PRIVATE);
						String safeNum = sp.getString("safeNumber", "");
						tv_safe_num.setText("手机安全号码：\n" + safeNum);

						// 初始化checkbox的状态
						boolean isprotecting = sp.getBoolean("isprotecting",
								false);
						System.out.println("isprotecting==="+isprotecting);
						if (isprotecting) {
							cb_open_protect.setChecked(true);
							cb_open_protect.setText("手机防盗已开启");
						} else {
							cb_open_protect.setChecked(false);
							cb_open_protect.setText("手机防盗末开启");
						}
						cb_open_protect
								.setOnCheckedChangeListener(new OnCheckedChangeListener() {

									@Override
									public void onCheckedChanged(
											CompoundButton buttonView,
											boolean isChecked) {
										if (isChecked) {
											cb_open_protect.setText("手机防盗已开启");
											Editor editor = sp.edit();
											editor.putBoolean("isprotecting",
													true);
											editor.commit();
										} else {
											cb_open_protect.setText("手机防盗末开启");
											Editor editor = sp.edit();
											editor.putBoolean("isprotecting",
													false);
											editor.commit();
										}
									}
								});

						// 进入设置引导
						tv_resetup.setOnClickListener(this);

					} else {
						Log.i(TAG, "未设置过向导，进入向导设置界面");
						finish();
						startSetupGuide();
					}
				}
			}
			dialog.dismiss();
			break;
		case R.id.tv_resetup://进入设置引导
			finish();
			startSetupGuide();
			break;
		}
	}

	/**
	 * 进入设置引导界面
	 */
	private void startSetupGuide() {
		Intent setup1Intent = new Intent(getApplicationContext(),
				SetupGuideActivity.class);
		startActivity(setup1Intent);
	}

}
