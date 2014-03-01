package com.onyas.phoneguard.ui;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.db.dao.BlackListDao;

public class BlackListActivity extends Activity {

	private static final String TAG = "BlackListActivity";
	private ListView lv_blacklist;
	private BlackListDao blackListDao;
	private BlackListItemAdapter adapter;
	private List<String> numbers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blacklist);
		
		lv_blacklist = (ListView) this.findViewById(R.id.lv_blacklist);
		
		blackListDao = new BlackListDao(this);
		
		numbers = blackListDao.findAll();
		adapter = new BlackListItemAdapter();
		lv_blacklist.setAdapter(adapter);
	}
	
	/**
	 * 自定义adapter
	 * @author Administrator
	 *
	 */
	private class BlackListItemAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return numbers.size();
		}

		@Override
		public Object getItem(int arg0) {
			return numbers.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			
			View view = View.inflate(BlackListActivity.this, R.layout.blacklist_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_blacklist_item);
			tv.setText(numbers.get(arg0));
			return view;
		}
		
	}
	
	
	/**
	 * 按钮的点击事件，弹出对话框，用于添加黑名单
	 * @param v
	 */
	public void click(View v){
		Log.i(TAG, "添加黑名单");
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("添加黑名单号码");
		final EditText et = new EditText(this);
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		builder.setView(et);
		builder.setPositiveButton("添加", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String number = et.getText().toString().trim();
				if(TextUtils.isEmpty(number)){
					Toast.makeText(BlackListActivity.this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
					return ;
				}else{
					blackListDao.add(number);
					//添加完电话号码以后，要更新listView;
				}
			}
		});
		
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		
		builder.create().show();
	}

}
