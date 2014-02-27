package com.onyas.phoneguard.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.onyas.phoneguard.R;

public class ChangePositionActivity extends Activity implements OnTouchListener {

	private ImageView iv_change_position;
	private TextView tv_tip;
	private int startx, starty,height;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.change_position);

		iv_change_position = (ImageView) this
				.findViewById(R.id.iv_change_position);
		tv_tip = (TextView) this.findViewById(R.id.tv_tip);

		height = getWindowManager().getDefaultDisplay().getHeight();
		
		// ͨ�����ֳ�ʼ����ʾ��λ��
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		int x = sp.getInt("left", 40);
		int y = sp.getInt("top", 0);
		LayoutParams params = (LayoutParams) iv_change_position
				.getLayoutParams();
		params.leftMargin = x;
		params.topMargin = y;
		iv_change_position.setLayoutParams(params);

		// ����ͼƬ�Ĵ����¼�
		iv_change_position.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (v.getId()) {
		case R.id.iv_change_position:

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:// ��ָ����
				startx = (int) event.getRawX();// ��һ�ΰ���ʱ
				starty = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:// ��ָ�ƶ�

				int x = (int) event.getRawX();// ��һ�ΰ���ʱ
				int y = (int) event.getRawY();

				// ʹͼƬ����ʾ�����غ�
				if (y < height/2) {
					tv_tip.layout(tv_tip.getLeft(), height-150, tv_tip.getRight(), height-100);
				} else {
					tv_tip.layout(tv_tip.getLeft(),100, tv_tip.getRight(), 150);
				}

				// ��ȡ��ָ�ƶ��ľ���
				int dx = x - startx;
				int dy = y - starty;

				int l = iv_change_position.getLeft();
				int t = iv_change_position.getTop();
				int r = iv_change_position.getRight();
				int b = iv_change_position.getBottom();
				// ���¸�����ͼ��λ��
				iv_change_position.layout(l + dx, t + dy, r + dx, b + dy);

				startx = (int) event.getRawX();// ����ƶ����λ��
				starty = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_UP:// ��ָ̧��
				// ����ǰ��λ�ñ�������
				int top = iv_change_position.getTop();
				int left = iv_change_position.getLeft();
				Editor editor = sp.edit();
				editor.putInt("top", top);
				editor.putInt("left", left);
				editor.commit();
				break;
			}

			break;
		}

		return true;// �ǵ÷���true;
	}

}
