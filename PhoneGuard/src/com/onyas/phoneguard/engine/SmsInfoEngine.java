package com.onyas.phoneguard.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

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
			info=null;
		}

		return infos;
	}

}
