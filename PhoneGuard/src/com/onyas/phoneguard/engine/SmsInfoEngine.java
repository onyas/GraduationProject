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
	 * 得到手机中所有的短信信息
	 * 
	 * @return
	 */
	public List<SmsInfo> getAllSmsInfo() {
		List<SmsInfo> infos = new ArrayList<SmsInfo>();
		ContentResolver resolver = context.getContentResolver();
		// 如果不知道主机名，可以把system/app/xxx.apk文件导出，然后用apktool得到AndroidManifest.xml文件，从而得到主机名
		// 里面具体的内容，可以查看SmsProvider源文件里面的uriMacher如何定义的
		// 方法:1、先把xxx.apk文件解压，得到xxx.dex文件，用dex2jar工具反编译该dex文件，得到classes_dex2jar.jar
		// 2、用jd-jui反编译工具得到程序的源文件
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
