package com.onyas.phoneguard.engine;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;

import com.onyas.phoneguard.R;
import com.onyas.phoneguard.domain.UpdateInfo;

public class UpdateInfoEngine {

	private Context context;

	public UpdateInfoEngine(Context context) {
		this.context = context;
	}

	/**
	 * @param urlId
	 *            服务器路径string对应的Id
	 * @return 得到从服务器获得的信息
	 * @throws Exception
	 */
	public UpdateInfo getUpdateInfo(int urlId) throws Exception {
		String path = context.getResources().getString(R.string.apkserver);
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(2000);
		conn.setRequestMethod("GET");
		InputStream is = conn.getInputStream();
		return UpdateInfoParser.parser(is);
	}

}
