package com.onyas.phoneguard.domain;

import android.graphics.drawable.Drawable;

public class TaskInfo {

	private String appname;// ������
	private Drawable appicon;// ����ͼ��
	private int pid;// ����ID
	private int memorysize;// ռ���ڴ�
	private String packname;
	private boolean isChecked;// �Ƿ�ѡ��

	public TaskInfo() {

	}

	public TaskInfo(String appname, Drawable appicon, int pid, int memorysize,
			String packname, boolean isChecked) {
		super();
		this.appname = appname;
		this.appicon = appicon;
		this.pid = pid;
		this.memorysize = memorysize;
		this.packname = packname;
		this.isChecked = isChecked;
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public Drawable getAppicon() {
		return appicon;
	}

	public void setAppicon(Drawable appicon) {
		this.appicon = appicon;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getMemorysize() {
		return memorysize;
	}

	public void setMemorysize(int memorysize) {
		this.memorysize = memorysize;
	}

	public String getPackname() {
		return packname;
	}

	public void setPackname(String packname) {
		this.packname = packname;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}
