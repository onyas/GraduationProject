package com.onyas.phoneguard.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.onyas.phoneguard.domain.ContactInfo;

public class ContactEngine {

	private Context context;


	public ContactEngine(Context context) {
		this.context = context;
	}

	/**
	 * 通过ContentResolver得到联系人信息 步骤 1、获取联系人id(raw_contacts表中的"_id"字段)
	 * 2、跟据联系人id到data表中获得mimetype_id，然后到memetypes表中获得所代表的函义,
	 * 在data表中获得信息(data1字段，根据mimetype_id字段区分)
	 * 
	 * @return 联系人信息的集合
	 */
	public List<ContactInfo> getContacts() {
		ContactInfo contact ;
		List<ContactInfo> contacts = new ArrayList<ContactInfo>();
		ContentResolver resolver = context.getContentResolver();
		
		// 1、查看源码中AndroidManifest.xml中找到名为ContactsProvider2的authorities
		// 2、查看源码中ContactsProvider2.java中UriMatcher找到对应的配置匹配格式
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri dataUri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, null, null, null, null);
		
		while (cursor.moveToNext()) {
			contact = new ContactInfo();
			String id = cursor.getString(cursor.getColumnIndex("_id"));
			Cursor dataCursor = resolver.query(dataUri, null,
					"raw_contact_id=?", new String[] { id }, null);
			while(dataCursor.moveToNext())
			{
				String data1 = dataCursor.getString(dataCursor.getColumnIndex("data1"));
				String type = dataCursor.getString(dataCursor.getColumnIndex("mimetype"));
				if(type.equals("vnd.android.cursor.item/name")){
					contact.setName(data1);
				}else if(type.equals("vnd.android.cursor.item/phone_v2")){
					contact.setNumber(data1);
				}
			}
			dataCursor.close();
			contacts.add(contact);
			contact=null;
		}
		cursor.close();
		return contacts;
	}

}
