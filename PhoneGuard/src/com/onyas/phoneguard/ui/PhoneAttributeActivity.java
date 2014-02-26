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
				Toast.makeText(PhoneAttributeActivity.this, "�������ݿ��ļ�ʧ��",
						Toast.LENGTH_SHORT).show();
				break;
			case SUCCESS:
				Toast.makeText(PhoneAttributeActivity.this, "�������ݿ��ļ��ɹ�",
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
	 * �����ѯ��ťʱ�����õķ���
	 * 
	 * @param v
	 */
	public void query(View v) {

		if (isDBexist()) {

			//�����ݿ��в�ѯ
			String num = et_query_number.getText().toString().trim();
			String address = PhoneAddressEngine.getAddress(num);
			tv_phone_attribute.setText(address);
			
		} else {
			pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pd.show();
			// ����ֻ���û�����ݿ⣬�����µ��̴߳ӷ������������ݿ�
			new Thread() {
				@Override
				public void run() {
					// �õ������������ݿ�ĵ�ַ
					String path = getResources().getString(R.string.addressurl);
					try {
						DownLoadFileTask.getFile(path, localFile, pd);
						Looper.prepare();
						pd.dismiss();
						Toast.makeText(PhoneAttributeActivity.this, "�������ݿ��ļ��ɹ�",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					} catch (Exception e) {
						e.printStackTrace();
						Looper.prepare();
						pd.dismiss();
						Toast.makeText(PhoneAttributeActivity.this, "�������ݿ��ļ�ʧ��",
								Toast.LENGTH_SHORT).show();
						Looper.loop();
					}
				}
			}.start();
			
		}

	}

	/**
	 * ����ֻ��Ź��������ݿ��Ƿ����
	 * 
	 * @return
	 */
	private boolean isDBexist() {
		File file = new File(localFile);
		return file.exists();
	}

}
