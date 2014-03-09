package com.onyas.phoneguard.util;

import java.text.DecimalFormat;

public class TextFormatter {

	/**
	 * 将一个long类型的数据转化为友好的数据大小的方法
	 * 
	 * @param size
	 *            long型数据
	 * @return 多大(byte,KB,MB,GB)
	 */
	public static String sizeFormat(long size) {
		DecimalFormat formater = new DecimalFormat("####.00");
		if (size < 1024) {
			return size + "bytes";
		} else if (size < 1024 * 1024) {
			float tmp = size / 1024f;
			String KBsize = formater.format(tmp);
			return KBsize + "KB";
		} else if (size < 1024 * 1024 * 1024) {
			float tmp = size / 1024f / 1024f;
			String MBsize = formater.format(tmp);
			return MBsize + "MB";
		} else if (size < 1024 * 1024 * 1024 * 1024) {
			float tmp = size / 1024f / 1024f / 1024f;
			String GBsize = formater.format(tmp);
			return GBsize + "GB";
		} else {
			return "error";
		}
	}

}
