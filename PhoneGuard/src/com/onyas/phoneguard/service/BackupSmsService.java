package com.onyas.phoneguard.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlSerializer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Xml;
import android.widget.Toast;

import com.onyas.phoneguard.domain.SmsInfo;
import com.onyas.phoneguard.engine.SmsInfoEngine;

public class BackupSmsService extends Service {

	private  SmsInfoEngine smsEngine=new SmsInfoEngine(this);
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		
		new Thread(){
			public void run() {
				try {
					List<SmsInfo> smsinfos = smsEngine.getAllSmsInfo();
					File file = new File("/sdcard/smsbackup.xml");
					XmlSerializer serializer = Xml.newSerializer();
					FileOutputStream os = new FileOutputStream(file);
					serializer.setOutput(os, "utf-8");
					
					serializer.startDocument("utf-8", true);
					
					serializer.startTag(null, "smss");
					//count属性用于记录一共有多少短信
					serializer.attribute(null, "count",smsinfos.size()+"");
					
					for(SmsInfo info :smsinfos){
						serializer.startTag(null, "sms");
						
						serializer.startTag(null, "id");
						serializer.text(info.getId());
						serializer.endTag(null, "id");
						
						serializer.startTag(null, "address");
						serializer.text(info.getAddress());
						serializer.endTag(null, "address");
						
						serializer.startTag(null, "date");
						serializer.text(info.getDate());
						serializer.endTag(null, "date");
						
						serializer.startTag(null, "type");
						serializer.text(info.getType()+"");
						serializer.endTag(null, "type");
						
						serializer.startTag(null, "body");
						serializer.text(info.getBody());
						serializer.endTag(null, "body");
						
						serializer.endTag(null, "sms");
					}
					
					
					serializer.endTag(null, "smss");
					serializer.endDocument();
					
					os.flush();//把文件缓冲区的内容写出去
					os.close();
					
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "备份完成", Toast.LENGTH_SHORT).show();
					Looper.loop();
				} catch (Exception e) {
					e.printStackTrace();
					Looper.prepare();
					Toast.makeText(getApplicationContext(), "备份失败", Toast.LENGTH_SHORT).show();
					Looper.loop();
				}
			};
		}.start();
		
	}
	
}
