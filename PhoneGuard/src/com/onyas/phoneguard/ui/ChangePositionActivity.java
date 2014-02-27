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
		
		// 通过布局初始化显示的位置
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		int x = sp.getInt("left", 40);
		int y = sp.getInt("top", 0);
		LayoutParams params = (LayoutParams) iv_change_position
				.getLayoutParams();
		params.leftMargin = x;
		params.topMargin = y;
		iv_change_position.setLayoutParams(params);

		// 设置图片的触摸事件
		iv_change_position.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		switch (v.getId()) {
		case R.id.iv_change_position:

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:// 手指按下
				startx = (int) event.getRawX();// 第一次按下时
				starty = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:// 手指移动

				int x = (int) event.getRawX();// 第一次按下时
				int y = (int) event.getRawY();

				// 使图片跟提示不会重合
				if (y < height/2) {
					tv_tip.layout(tv_tip.getLeft(), height-150, tv_tip.getRight(), height-100);
				} else {
					tv_tip.layout(tv_tip.getLeft(),100, tv_tip.getRight(), 150);
				}

				// 获取手指移动的距离
				int dx = x - startx;
				int dy = y - starty;

				int l = iv_change_position.getLeft();
				int t = iv_change_position.getTop();
				int r = iv_change_position.getRight();
				int b = iv_change_position.getBottom();
				// 重新更新视图的位置
				iv_change_position.layout(l + dx, t + dy, r + dx, b + dy);

				startx = (int) event.getRawX();// 获得移动后的位置
				starty = (int) event.getRawY();
				break;
			case MotionEvent.ACTION_UP:// 手指抬起
				// 将当前的位置保存下来
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

		return true;// 记得返回true;
	}

}
