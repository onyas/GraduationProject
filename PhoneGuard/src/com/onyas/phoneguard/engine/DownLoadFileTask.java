package com.onyas.phoneguard.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;

public class DownLoadFileTask {

	/**
	 * 
	 * @param path	�������ļ���ַ
	 * @param filePath	�����ļ���ַ
	 * @param pd	������
	 * @return	�������ļ�
	 * @throws Exception
	 */
	public static File getFile(String path,String filePath,ProgressDialog pd) throws Exception{
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(2000);
		conn.setRequestMethod("GET");
		int code = conn.getResponseCode();
		if(code==200)
		{
			int total = conn.getContentLength();
			pd.setMax(total);
			InputStream is = conn.getInputStream();
			File file = new File(filePath);
			FileOutputStream fos = new FileOutputStream(file);
		
			byte[] buffer = new byte[1024];
			int len=0,process=0;
			while((len=is.read(buffer))!=-1){
				fos.write(buffer, 0, len);
				process+=len;
				pd.setProgress(process);
			}
			
			fos.flush();
			fos.close();
			is.close();
			return file;
		}
		return null;
	}
	
}
