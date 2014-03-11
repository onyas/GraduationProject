package com.onyas.phoneguard.ui.stub;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onyas.phoneguard.R;

public class MyToast {

	/**
	 * �Զ����Toast
	 * 
	 * @param context
	 *            ������
	 * @param resid
	 *            ��Դ�ļ�
	 * @param text
	 *            ��ʾ������
	 */
	public static void makeText(Context context, int resId, String text) {

		View view = View.inflate(context, R.layout.mytoast, null);
		ImageView iv = (ImageView) view.findViewById(R.id.iv_mytoast);
		TextView tv = (TextView) view.findViewById(R.id.tv_mytoast);

		iv.setImageResource(resId);
		tv.setText(text);

		Toast toast = new Toast(context);
		toast.setView(view);
		toast.setDuration(0);
		toast.show();
	}

}
