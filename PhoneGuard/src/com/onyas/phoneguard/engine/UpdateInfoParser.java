package com.onyas.phoneguard.engine;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;


import android.util.Xml;

import com.onyas.phoneguard.domain.UpdateInfo;

public class UpdateInfoParser {

	/**
	 * 将xml输入流解析成对象
	 * @param is
	 * @return 
	 * @throws Exception 
	 */
	public static UpdateInfo parser(InputStream is) throws Exception{
		
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "utf-8");
		int type = parser.getEventType();
		UpdateInfo info = new UpdateInfo();
		while(type!=XmlPullParser.END_DOCUMENT){
			switch (type) {
			case XmlPullParser.START_TAG:
				if("version".equals(parser.getName())){
					String version = parser.nextText();
					info.setVersion(version);
				}else if("description".equals(parser.getName())){
					String description = parser.nextText();
					info.setDescription(description);
				}else if("apkurl".equals(parser.getName())){
					String apkurl = parser.nextText();
					info.setApkurl(apkurl);
				}
				break;

			default:
				break;
			}
			type =parser.next();
		}
		
		return info;
	}
	
}
