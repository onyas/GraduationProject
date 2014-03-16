package com.onyas.phoneguard.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.content.Context;

public class FileUtil {

	/**
	 * 把文件从输入流中复制到指定路径
	 * 
	 * @param is
	 *            输入流
	 * @param path
	 *            目的地文件
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
	 * 将输入流中的内容保存到应用程序自己的数据目录中
	 * 
	 * @param is
	 *            输入流
	 * @param context
	 *            上下文
	 * @param name
	 *            要保存的名字
	 * @param mode
	 *            权限
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
	 * 删除指定文件夹下的所有文件，包括子文件夹
	 * 
	 * @param dir
	 *            要删除的目录
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
