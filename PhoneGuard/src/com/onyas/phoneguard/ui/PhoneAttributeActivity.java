package com.onyas.phoneguard.ui;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.engine.DownLoadFileTask;
import com.onyas.phoneguard.engine.PhoneAddressEngine;

public class PhoneAttributeActivity extends Activity {

	protected static final int ERROR = 10;
	protected static final int SUCCESS = 11;
	private EditText et_query_number;
	private TextView tv_phone_attribute;
	private String localFile = Environment.getExternalStorageDirectory()
			+ "/address.db";
	private ProgressDialog pd;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ERROR:
				Toast.makeText(PhoneAttributeActivity.this, "下载数据库文件失败",
						Toast.LENGTH_SHORT).show();
				break;
			case SUCCESS:
				Toast.makeText(PhoneAttributeActivity.this, "下载数据库文件成功",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.attributequery);

		et_query_number = (EditText) findViewById(R.id.et_query_number);
		tv_phone_attribute = (TextView) findViewById(R.id.tv_phone_attribute);

	}

	/**
	 * 点击查询按钮时所调用的方法
	 * 
	 * @param v
	 */
	public void query(View v) {

		if (isDBexist()) {

			//到数据库中查询
			String num = et_query_number.getText().toString().trim();
			String address = PhoneAddressEngine.getAddress(num);
			tv_phone_attribute.setText(address);
			
		} else {
			pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.show();
			// 如果手机上没有数据库，则开启新的线程从服务器下载数据库
			new Thread() {
				@Override
				public void run() {
					// 得到服务器中数据库的地址
					String path = getResources().getString(R.string.addressurl);
					try {
						DownLoadFileTask.getFile(path, localFile, pd);
						Looper.prepare();
						pd.dismiss();
						Toast.makeText(PhoneAttributeActivity.this, "下载数据库文件成功",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					} catch (Exception e) {
						e.printStackTrace();
						Looper.prepare();
						pd.dismiss();
						Toast.makeText(PhoneAttributeActivity.this, "下载数据库文件失败",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				}
			}.start();
			
		}

	}

	/**
	 * 检查手机号归属地数据库是否存在
	 * 
	 * @return
	 */
	private boolean isDBexist() {
		File file = new File(localFile);
		return file.exists();
	}

}
