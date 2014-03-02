package com.onyas.phoneguard.engine;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import com.onyas.phoneguard.domain.SmsInfo;

public class SmsInfoEngine {

	private Context context;

	public SmsInfoEngine(Context context) {
		this.context = context;
	}

	/**
	 * �õ��ֻ������еĶ�����Ϣ
	 * 
	 * @return
	 */
	public List<SmsInfo> getAllSmsInfo() {
		List<SmsInfo> infos = new ArrayList<SmsInfo>();
		ContentResolver resolver = context.getContentResolver();
		// �����֪�������������԰�system/app/xxx.apk�ļ�������Ȼ����apktool�õ�AndroidManifest.xml�ļ����Ӷ��õ�������
		// �����������ݣ����Բ鿴SmsProviderԴ�ļ������uriMacher��ζ����
		// ����:1���Ȱ�xxx.apk�ļ���ѹ���õ�xxx.dex�ļ�����dex2jar���߷������dex�ļ����õ�classes_dex2jar.jar
		// 2����jd-jui�����빤�ߵõ������Դ�ļ�
		Uri uri = Uri.parse("content://sms/");

		Cursor cursor = resolver.query(uri, new String[] { "_id", "address",
				"type", "date", "body" }, null, null, "date desc");
		SmsInfo info;
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			String address = cursor.getString(1);
			int type = cursor.getInt(2);
			String date = cursor.getString(3);
			String body = cursor.getString(4);
			System.out.println("id=" + id + ",address=" + address + ",type="
					+ type + ",date=" + date + ",body=" + body);
			info = new SmsInfo(id, address, date, type, body);
			infos.add(info);
			info = null;
		}

		return infos;
	}

	/**
	 * ���ļ���ԭ����
	 * 
	 * @param path
	 *            �ļ���·��
	 */
	public void restoreSms(String path) throws Exception{
			ContentValues values=null;
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(fis, "utf-8");
			int type = parser.getEventType();
			while (type != XmlPullParser.END_DOCUMENT) {

				switch (type) {
				case XmlPullParser.START_TAG:
					if ("sms".equals(parser.getName())) {
						values = new ContentValues();
					}else if("address".equals(parser.getName())){
						values.put("address", parser.nextText());
					}else if("date".equals(parser.getName())){
						values.put("date", parser.nextText());
					}else if("type".equals(parser.getName())){
						values.put("type", parser.nextText());
					}else if("body".equals(parser.getName())){
						values.put("body", parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					if ("sms".equals(parser.getName())) {
						ContentResolver resolver = context.getContentResolver();
						resolver.insert(Uri.parse("content://sms/"), values);
					}
					break;
				}
				type = parser.next();
			}
	
	}

}
