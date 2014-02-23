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
	 * ͨ��ContentResolver�õ���ϵ����Ϣ ���� 1����ȡ��ϵ��id(raw_contacts���е�"_id"�ֶ�)
	 * 2��������ϵ��id��data���л��mimetype_id��Ȼ��memetypes���л��������ĺ���,
	 * ��data���л����Ϣ(data1�ֶΣ�����mimetype_id�ֶ�����)
	 * 
	 * @return ��ϵ����Ϣ�ļ���
	 */
	public List<ContactInfo> getContacts() {
		ContactInfo contact ;
		List<ContactInfo> contacts = new ArrayList<ContactInfo>();
		ContentResolver resolver = context.getContentResolver();
		
		// 1���鿴Դ����AndroidManifest.xml���ҵ���ΪContactsProvider2��authorities
		// 2���鿴Դ����ContactsProvider2.java��UriMatcher�ҵ���Ӧ������ƥ���ʽ
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
