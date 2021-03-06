package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.engine.SmsInfoEngine;
import com.onyas.phoneguard.service.BackupSmsService;
import com.onyas.phoneguard.service.PhoneNumberService;

public class Fun8AtoolsActivity extends Activity implements OnClickListener {

	private TextView tv_atool_query, tv_atool_serstate, tv_atool_bgcolor,
			tv_atool_locate, tv_atool_sms_restore, tv_atool_sms_backup,tv_atool_app_lock,tv_atool_commonnum;
	private CheckBox cb_atool_change;
	private Intent serviceIntent;
	private SharedPreferences sp;
	private SmsInfoEngine smsInfoEngine;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fun_8atools);

		sp = getSharedPreferences("config", Context.MODE_PRIVATE);

		tv_atool_app_lock = (TextView) findViewById(R.id.tv_atool_app_lock);
		tv_atool_sms_backup = (TextView) findViewById(R.id.tv_atool_sms_backup);
		tv_atool_sms_restore = (TextView) findViewById(R.id.tv_atool_sms_restore);
		tv_atool_query = (TextView) findViewById(R.id.tv_atool_query);
		tv_atool_locate = (TextView) findViewById(R.id.tv_atool_locate);
		tv_atool_serstate = (TextView) findViewById(R.id.tv_atool_servicestate);
		tv_atool_bgcolor = (TextView) findViewById(R.id.tv_atool_bgcolor);
		cb_atool_change = (CheckBox) findViewById(R.id.cb_atool_servicechange);
		tv_atool_commonnum= (TextView) findViewById(R.id.tv_atool_commonnum);

		
		serviceIntent = new Intent(this, PhoneNumberService.class);

		cb_atool_change
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							startService(serviceIntent);
							tv_atool_serstate.setTextColor(getResources()
									.getColor(R.color.textColor));
							tv_atool_serstate.setText("归属地服务已开启");
						} else {
							stopService(serviceIntent);
							tv_atool_serstate.setTextColor(Color.RED);
							tv_atool_serstate.setText("归属地服务未开启");
						}
					}
				});

		tv_atool_query.setOnClickListener(this);
		tv_atool_bgcolor.setOnClickListener(this);
		tv_atool_locate.setOnClickListener(this);
		tv_atool_sms_backup.setOnClickListener(this);
		tv_atool_sms_restore.setOnClickListener(this);
		tv_atool_app_lock.setOnClickListener(this);
		tv_atool_commonnum.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.tv_atool_query:
			Intent attributeQuery = new Intent(this,
					PhoneAttributeActivity.class);
			startActivity(attributeQuery);
			break;
		case R.id.tv_atool_bgcolor:
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("归属地提示显示风格");
			String[] items = new String[] { "半透明", "活力橙", "天空蓝", "金属灰", "苹果绿" };
			builder.setSingleChoiceItems(items, 2,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Editor editor = sp.edit();
							editor.putInt("locatebgcolor", which);
							editor.commit();
						}
					});
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			builder.create().show();
			break;
		case R.id.tv_atool_locate:
			Intent intent = new Intent(this, ChangePositionActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_atool_sms_backup:
			// 备份短信是个费时的操作，所以需要放在子线程里面进行，但如果这时手机内存不足，应用程序处于后台时行时，
			// 该子线程会被回收掉，所以为了提升应用程序的优先级，我们用service组件来完成短信的备份
			Intent service = new Intent(this, BackupSmsService.class);
			startService(service);
			break;
		case R.id.tv_atool_sms_restore:
			// 读取备份的xml文件，解析里面的信息，插入到短信应用里面
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setCancelable(false);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.setMessage("正在还原短信");
			pd.show();
			smsInfoEngine = new SmsInfoEngine(this);
			new Thread() {
				public void run() {
					try {
						smsInfoEngine.restoreSms("/sdcard/smsbackup.xml",pd);
						pd.dismiss();
						Looper.prepare();
						Toast.makeText(Fun8AtoolsActivity.this, "还原成功",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					} catch (Exception e) {
						e.printStackTrace();
						pd.dismiss();
						Looper.prepare();
						Toast.makeText(Fun8AtoolsActivity.this, "还原失败",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
					pd.dismiss();
				};
			}.start();
			break;
			
		case R.id.tv_atool_app_lock://程序锁
			Intent lockIntent = new Intent(this,AppLockActivity.class);
			startActivity(lockIntent);
			break;
		case R.id.tv_atool_commonnum:
			Intent commonnumIntent = new Intent(this,CommonNumActivity.class);
			startActivity(commonnumIntent);
		break;
		}
	
	}

}
