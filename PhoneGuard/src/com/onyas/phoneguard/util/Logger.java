package com.onyas.phoneguard.util;

import android.util.Log;

public class Logger {

	public static int LOGLEVER = 100;// 在程序开发阶段该值比较大就可以，在发布的时候很小就可以
	public static int VERBOSE = 1;
	public static int DEBUG = 2;
	public static int INOF = 3;
	public static int WARN = 4;
	public static int ERROR = 5;

	public static void v(String tag, String msg) {
		if (LOGLEVER > VERBOSE) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		if (LOGLEVER > DEBUG) {
			Log.d(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (LOGLEVER > ERROR) {
			Log.e(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (LOGLEVER > INOF) {
			Logger.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (LOGLEVER > WARN) {
			Log.w(tag, msg);
		}
	}

}
