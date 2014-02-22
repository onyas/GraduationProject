package com.onyas.phoneguard.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encoder {

	/**
	 * 对字符串进行MD5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encoder(String str) {
		StringBuffer sb = new StringBuffer();
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			byte[] bytes = digest.digest(str.getBytes());

			for (int i = 0; i < bytes.length; i++) {
				String s = Integer.toHexString(0xff & bytes[i]);
				if (s.length() == 1) {
					sb.append("0" + s);
				} else {
					sb.append(s);
				}
			}

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 对字符串进行MD5 30次加密    更安全
	 * http://www.cmd5.com/
	 * @param str
	 * @return
	 */
	public static String encoder30(String str) {
		for (int i = 0; i < 30; i++) {
			str = encoder(str);
		}
		return str;
	}

}
