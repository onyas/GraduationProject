package com.onyas.phoneguard.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {

	private Drawable icon;
	private String appname;
	private String packagename;
	private boolean isSystemApp;
	
	public AppInfo() {
	}
	
	public AppInfo(Drawable icon, String appname, String packagename,
			boolean isSystemApp) {
		this.icon = icon;
		this.appname = appname;
		this.packagename = packagename;
		this.isSystemApp = isSystemApp;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppname() {
		return appname;
	}
	public void setAppname(String appname) {
		this.appname = appname;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public boolean isSystemApp() {
		return isSystemApp;
	}
	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}
	
	
	
}
