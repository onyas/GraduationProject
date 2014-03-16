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
import com.onyas.phoneguard.util.Logger;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
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

		//��listView����ע��һ��context Menu
		registerForContextMenu(lv_blacklist);

		blackListDao = new BlackListDao(this);

		numbers = blackListDao.findAll();
		adapter = new BlackListItemAdapter();
		lv_blacklist.setAdapter(adapter);
	}

	/**
	 * �Զ���adapter
	 * 
	 * @author Administrator
	 * 
	 */
	private class BlackListItemAdapter extends BaseAdapter {

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

			View view = View.inflate(BlackListActivity.this,
					R.layout.blacklist_item, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_blacklist_item);
			tv.setText(numbers.get(arg0));
			return view;
		}

	}

	/**
	 * ��ť�ĵ���¼��������Ի���������Ӻ�����
	 * 
	 * @param v
	 */
	public void click(View v) {
		Logger.i(TAG, "��Ӻ�����");
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��Ӻ���������");
		final EditText et = new EditText(this);
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		builder.setView(et);
		builder.setPositiveButton("���", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String number = et.getText().toString().trim();
				if (TextUtils.isEmpty(number)) {
					Toast.makeText(BlackListActivity.this, "�绰���벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					blackListDao.add(number);
					// �����绰�����Ժ�Ҫ����listView;
					// ��һ�ַ���,ȱ����Ҫ����ˢ����������
					// numbers = blackListDao.findAll();
					// adapter = new BlackListItemAdapter();
					// lv_blacklist.setAdapter(adapter);
					// �ڶ��ַ���
					refreshList();

				}
			}
		});

		builder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		builder.create().show();
	}

	/**
	 * ����ListView
	 */
	private void refreshList() {
		numbers = blackListDao.findAll();
		// ��������֪ͨlistview�������ݿ�
		adapter.notifyDataSetChanged();
	}

	/**
	 * �ο���doc/index.html/Develop/User interface/Menus
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		// ��ǰ��ѡ�е���Ŀid
		int id = (int) info.id;
		String number = numbers.get(id);

		switch (item.getItemId()) {
		case R.id.update_blacklist:
			popDialog(number);
			break;
		case R.id.delete_blacklist:
			blackListDao.delete(number);
			refreshList();
			break;
		}
		return false;
	}

	private void popDialog(final String num) {

		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("���ĺ���������");
		final EditText et = new EditText(this);
		et.setInputType(InputType.TYPE_CLASS_PHONE);
		et.setText(num);
		builder.setView(et);
		builder.setPositiveButton("ȷ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String number = et.getText().toString().trim();
				if (TextUtils.isEmpty(number)) {
					Toast.makeText(BlackListActivity.this, "�绰���벻��Ϊ��",
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					blackListDao.update(num, number);
					refreshList();
				}
			}
		});

		builder.setNegativeButton("ȡ��", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});

		builder.create().show();
	}
}
