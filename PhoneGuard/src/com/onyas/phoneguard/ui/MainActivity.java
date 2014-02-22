package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.adpter.MainUIAdpter;

public class MainActivity extends Activity implements OnItemClickListener {

	private static final String TAG = "MainActivity";
	private GridView gv_main_screen;
	private MainUIAdpter adpter;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		gv_main_screen = (GridView) this.findViewById(R.id.gv_main_screen);
		
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		
		adpter = new MainUIAdpter(this); 
		gv_main_screen.setAdapter(adpter);
		
		//Ϊÿһ����Ŀ��ӵ���¼�
		gv_main_screen.setOnItemClickListener(this);
		
		//������"�ֻ�����"�����Ŀʱ�����������޸����ֵĶԻ���
		gv_main_screen.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view,
					int position, long id) {
				if(position==0){
					AlertDialog.Builder builder = new Builder(MainActivity.this);
					builder.setTitle("����");
					builder.setIcon(R.drawable.icon5);
					
					final EditText et_name = new EditText(MainActivity.this);
					et_name.setHint("������Ҫ���õ�����");
					
					builder.setView(et_name);
					
					builder.setPositiveButton("ȷ��", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							String name = et_name.getText().toString().trim();
							if(TextUtils.isEmpty(name))
							{
								Toast.makeText(getApplicationContext(), "���ֲ���Ϊ��", Toast.LENGTH_SHORT).show();
								return;
							}
							Editor editor = sp.edit();
							editor.putString("phonelost", name);
							editor.commit();
							TextView tv_name = (TextView) view.findViewById(R.id.tv_main_name);
							tv_name.setText(name);
						}
					});
					builder.setNegativeButton("ȡ��", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					});
					builder.create().show();
				}
				return false;
			}
		});
		
	}

	/**
	 * 
	 * ��gridView�е���Ŀ�����ʱ��Ӧ�Ļص�
	 * parent	gridView
	 * view 	��ǰ���������Ŀ linearlayout
	 * position ��ǰ��Ŀ��Ӧ��λ��
	 * id		�к�
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.i(TAG, "��ǰ��Ŀ"+position);
		switch (position) {
		case 0://�ֻ���������
			Log.i(TAG, "�ֻ���������");
			Intent phoneIntent = new Intent(getApplicationContext(), PhoneProtectedActivity.class);
			startActivity(phoneIntent);
			break;

		default:
			break;
		}
	}
	
	
}
