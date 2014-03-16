package com.onyas.phoneguard.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

public class FileUtil {

	/**
	 * ���ļ����������и��Ƶ�ָ��·��
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

	/**
	 * 
	 * ���������е����ݱ��浽Ӧ�ó����Լ�������Ŀ¼��
	 * 
	 * @param is
	 *            ������
	 * @param context
	 *            ������
	 * @param name
	 *            Ҫ���������
	 * @param mode
	 *            Ȩ��
	 * @throws Exception
	 */
	public static void copyFile(InputStream is, Context context, String name,
			int mode) throws Exception {
		FileOutputStream fos = context.openFileOutput(name, mode);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			fos.write(buffer, 0, len);
		}
		fos.flush();
		fos.close();
	}

	/**
	 * ɾ��ָ���ļ����µ������ļ����������ļ���
	 * 
	 * @param dir
	 *            Ҫɾ����Ŀ¼
	 */
	public static void deleteDir(File dir) {
		File[] files = dir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				deleteDir(file);
			} else {
				file.delete();
			}
		}
	}
}
