package com.onyas.phoneguard.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

	/**
	 * �������������ļ�
	 * 
	 * @param is
	 *            ������
	 * @param path
	 *            Ŀ�ĵ��ļ�
	 */
	public static void copyFile(InputStream is, String path) throws Exception {
		File file = new File(path);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			fos.write(buffer, 0, len);
		}
		fos.flush();
		fos.close();
	}
}
