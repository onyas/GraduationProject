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
			info = null;
		}

		return infos;
	}

	/**
	 * 从文件还原短信
	 * 
	 * @param path
	 *            文件的路径
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
