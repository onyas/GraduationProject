package com.onyas.phoneguard.engine;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * 用于获取手机GPS信息的单例类
 * 
 * @author Administrator
 * 
 */
public class GPSInfoEngine {

	private static Context context;

	private static GPSInfoEngine gpsinfo;
	private static MyLocationListener locationListener;
	private LocationManager lm;

	private GPSInfoEngine() {
	}

	/**
	 * 单例模式
	 * 
	 * @param contex
	 * @return
	 */
	public static synchronized GPSInfoEngine getInstance(Context con) {
		context = con;
		if (gpsinfo == null) {
			gpsinfo = new GPSInfoEngine();
		}
		return gpsinfo;
	}

	/**
	 * 得到手机的位置
	 * 
	 * @return
	 */
	public String getLocation() {
		// 得到位置服务
		 lm= (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		String provider = getBestProvider(lm);
		// 注册位置的监听器
		lm.requestLocationUpdates(provider, 6000, 50, getLocationListener());
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		String loction = sp.getString("location", "");
		return loction;
	}

	/**
	 * 停止GPS的监听
	 */
	public void stopGPSListener(){
		lm.removeUpdates(getLocationListener());
	}
	
	private synchronized MyLocationListener getLocationListener() {
		if (locationListener == null) {
			locationListener = new MyLocationListener();
		}
		return locationListener;
	}

	private class MyLocationListener implements LocationListener {

		/**
		 * 当手机位置发生变化时所回调的方法
		 */
		@Override
		public void onLocationChanged(Location location) {
			String latitude = "latitude:" + location.getLatitude();// 纬度
			String longitude = "longitude:" + location.getLongitude();// 经度

			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", latitude + "--" + longitude);
			editor.commit();
		}

		/**
		 * 当provider的状态发生变化时回调
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		/**
		 * 当provider开启时回调
		 */
		@Override
		public void onProviderEnabled(String provider) {

		}

		/**
		 * 当provider关闭时回调
		 */
		@Override
		public void onProviderDisabled(String provider) {

		}

	}

	/**
	 * 得到最好位置提供者
	 * 
	 * @return
	 */
	private String getBestProvider(LocationManager lm) {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 设置精确位置
		criteria.setAltitudeRequired(false);// 不关心海拔信息
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);// 对手机的耗电性能
		criteria.setSpeedRequired(true);// 对速度变化敏感
		criteria.setCostAllowed(true);// 设置开销为true
		return lm.getBestProvider(criteria, true);
	}

}
