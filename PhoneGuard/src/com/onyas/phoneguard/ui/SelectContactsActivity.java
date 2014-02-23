package com.onyas.phoneguard.ui;


import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.domain.ContactInfo;
import com.onyas.phoneguard.engine.ContactEngine;

public class SelectContactsActivity extends Activity {

	private ListView lv_contacts;
	private List<ContactInfo> contacts;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contacts_select);
		
		ContactEngine contactEngine = new ContactEngine(this);
		contacts = contactEngine.getContacts();
		
		lv_contacts = (ListView) findViewById(R.id.lv_select_contacts);
		lv_contacts.setAdapter(new ContactSelectAdapter());
		
		lv_contacts.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent data = new Intent();
				data.putExtra("number", contacts.get(position).getNumber());
				setResult(0, data);
				finish();
			}
		});
		
	}
	
	private class ContactSelectAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return contacts.size();
		}

		@Override
		public Object getItem(int position) {
			return contacts.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ContactInfo contact = contacts.get(position);
			LinearLayout ll = new LinearLayout(SelectContactsActivity.this);
			ll.setOrientation(LinearLayout.VERTICAL);
			TextView name = new TextView(SelectContactsActivity.this);
			name.setText(contact.getName());
			TextView number = new TextView(SelectContactsActivity.this);
			number.setText(contact.getNumber());
			ll.addView(name);
			ll.addView(number);
			return ll;
		}
		
	}
	
}
