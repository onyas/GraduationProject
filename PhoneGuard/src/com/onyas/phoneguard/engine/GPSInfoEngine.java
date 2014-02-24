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
 * ���ڻ�ȡ�ֻ�GPS��Ϣ�ĵ�����
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
	 * ����ģʽ
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
	 * �õ��ֻ���λ��
	 * 
	 * @return
	 */
	public String getLocation() {
		// �õ�λ�÷���
		 lm= (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		String provider = getBestProvider(lm);
		// ע��λ�õļ�����
		lm.requestLocationUpdates(provider, 6000, 50, getLocationListener());
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		String loction = sp.getString("location", "");
		return loction;
	}

	/**
	 * ֹͣGPS�ļ���
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
		 * ���ֻ�λ�÷����仯ʱ���ص��ķ���
		 */
		@Override
		public void onLocationChanged(Location location) {
			String latitude = "latitude:" + location.getLatitude();// γ��
			String longitude = "longitude:" + location.getLongitude();// ����

			SharedPreferences sp = context.getSharedPreferences("config",
					Context.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("location", latitude + "--" + longitude);
			editor.commit();
		}

		/**
		 * ��provider��״̬�����仯ʱ�ص�
		 */
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		/**
		 * ��provider����ʱ�ص�
		 */
		@Override
		public void onProviderEnabled(String provider) {

		}

		/**
		 * ��provider�ر�ʱ�ص�
		 */
		@Override
		public void onProviderDisabled(String provider) {

		}

	}

	/**
	 * �õ����λ���ṩ��
	 * 
	 * @return
	 */
	private String getBestProvider(LocationManager lm) {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// ���þ�ȷλ��
		criteria.setAltitudeRequired(false);// �����ĺ�����Ϣ
		criteria.setPowerRequirement(Criteria.POWER_MEDIUM);// ���ֻ��ĺĵ�����
		criteria.setSpeedRequired(true);// ���ٶȱ仯����
		criteria.setCostAllowed(true);// ���ÿ���Ϊtrue
		return lm.getBestProvider(criteria, true);
	}

}
